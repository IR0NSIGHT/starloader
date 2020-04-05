package api.mod;

import api.DebugFile;
import api.SMModLoader;
import api.main.GameClient;
import api.main.GameServer;
import api.utils.StarRunnable;
import org.apache.commons.io.FileUtils;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.network.ServerListRetriever;
import org.schema.schine.network.StarMadeNetUtil;

import javax.net.ssl.SSLContext;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class ModStarter {
    public static void preServerStart(){
        //Enable all mods for now, later there will be a gui to turn them on or off
        DebugFile.log("[Server] Enabling mods...");
        for (StarMod mod : StarLoader.starMods){
            DebugFile.log("[Server] >>> Enabling: " + mod.modName);
            mod.onEnable();
            mod.flagEnabled(true);
            DebugFile.log("[Server] <<< Enabled: " + mod.modName);
        }
    }
    public static void postServerStart(){
        //whatever lol
    }

    public static void downloadFile(URL url, String fileName) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        final URLConnection openConnection = url.openConnection();
        openConnection.setConnectTimeout(10000000);
        openConnection.setRequestProperty("User-Agent", "StarMade-Client");
        //Fixes some errors with java 7 not downloading properly
        SSLContext ssl = SSLContext.getInstance("TLSv1.2");
        ssl.init(null, null, new SecureRandom());
        System.setProperty("https.protocols", "TLSv1.2");
        //
        FileUtils.copyInputStreamToFile(openConnection.getInputStream(), new File(fileName));
    }
    public static boolean preClientConnect(String serverHost, int serverPort){
        DebugFile.log("Disabling existing mods:");
        for (StarMod mod : StarLoader.starMods){
            if(mod.isEnabled()) {
                mod.onDisable();
                mod.flagEnabled(false);
            }
        }
        //TODO only enable the ones that are enabled on the server, and the ones that are set to force enable
        DebugFile.log("Enabling mods...");
        ArrayList<ModInfo> serverMods = ServerModInfo.getServerInfo(ServerModInfo.getServerUID(serverHost, serverPort));
        for (StarMod mod : StarLoader.starMods){
            System.err.println("[Client] >>> Found mod: " + mod.modName);
            if(serverMods == null) {
                if(serverHost.equals("localhost:4242")){
                    DebugFile.log("Connecting to own server, mods are already enabled by the server");
                }else {
                    DebugFile.log("Mod info not found for: " + serverHost + ":" + serverPort + " This is likely because they direct connected");
                    GameClient.setLoadString("Getting server mod info...");
                    StarMadeNetUtil starMadeNetUtil = new StarMadeNetUtil();
                    try {
                        System.err.println(starMadeNetUtil.getServerInfo(serverHost, serverPort, 5000).toString());
                        //should register the mods.
                    } catch (IOException e) {
                        //???
                        e.printStackTrace();
                    }
                    mod.onEnable();
                    mod.flagEnabled(true);
                }
            }else {
                DebugFile.log("Mod info WAS found");
                for (ModInfo serverMod : serverMods) {
                    DebugFile.log("le test: " + serverMod.name);
                    if (serverMod.name.equals(mod.modName)) {
                        DebugFile.log("[Client] >>> Correct mod name: " + serverMod.name);
                        if (serverMod.version.equals(mod.modVersion)) {
                            serverMods.remove(serverMod);
                            mod.onEnable();
                            mod.flagEnabled(true);
                            DebugFile.log("[Client] <<< Enabled: " + mod.modName);
                            break;
                        }
                    }
                }
            }
        }
        //DebugFile.log(serverMods.toString());
        if(serverMods != null && !serverMods.isEmpty()){
            //Now we need to download them from the client
            DebugFile.log("=== DEPENCIES NOT MET, DOWNLOADING MODS ===");
            for(ModInfo sMod : serverMods){
                sMod.fetchDownloadURL();
                DebugFile.log("WE NEED TO DOWNLOAD: " + sMod.toString());
                try {
                    String fileName = "mods/" + sMod.name + ".jar";
                    GameClient.setLoadString("Downloading mod: " + sMod.name);
                    downloadFile(new URL(sMod.downloadURL), fileName);
                    DebugFile.log("Successfully downloaded mod: " + sMod.name + ", version: " + sMod.version + ", from: " + sMod.downloadURL + ", into: " + sMod.name + ".jar");
                    //Get file, convert to URL
                    URL[] url = new URL[]{new File(fileName).toURI().toURL()};
                    GameClient.setLoadString("Done downloading, Loading mod: " + sMod.name);
                    StarMod starMod = SMModLoader.loadModFromJar(new URLClassLoader(url), new JarFile(fileName));
                    GameClient.setLoadString("Mod loaded, enabling mod...");
                    starMod.onEnable();
                    starMod.flagEnabled(true);

                } catch (Exception e) {
                    DebugFile.log("Failed to download, reason: ");
                    DebugFile.logError(e, null);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to download mods... please send in starloader.log and log/starmade0.log");
                }
            }
            //JOptionPane.showMessageDialog(null, "We are going to need to download some mods... fancy gui coming later");
            //DebugFile.log("We are going to download some mods, so dont start the client yet");
            return true;
        }else{
            DebugFile.log("all good");
            return true;
        }
    }
    public static void postClientConnect(){

    }
    public static void onClientLeave(){
        StarRunnable.deleteAll();
        for (StarMod mod : StarLoader.starMods){
            mod.onDisable();
        }
    }

    public static void main(String[] args) {
        try {
            downloadFile(new URL("https://starmadedock.net/content/turret-hotkey.8054/download"), "TurretHotKey.jar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
