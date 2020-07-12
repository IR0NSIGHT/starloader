package api.mod;

import api.DebugFile;
import api.ModPlayground;
import api.SMModLoader;
import api.network.Packet;
import api.utils.StarRunnable;
import org.apache.commons.io.FileUtils;
import org.schema.game.common.data.physics.Pair;
import org.schema.schine.graphicsengine.core.Controller;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.jar.JarFile;

public class ModStarter {
    public static void preServerStart() {
        //Enable all mods in the mods folder
        DebugFile.log("[Server] Enabling mods...");
        ArrayList<StarMod> mods = StarLoader.starMods;
        ArrayList<StarMod> toEnable = new ArrayList<StarMod>();
        for (StarMod mod : mods) {
            if(EnabledModFile.getInstance().isClientEnabled(mod.getInfo())){
                toEnable.add(mod);
            }
        }
        enableMods(toEnable);
    }

    public static void postServerStart() {
        //whatever lol
    }

    public static void downloadFile(URL url, String fileName) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        final URLConnection openConnection = url.openConnection();
        openConnection.setConnectTimeout(20000);
        openConnection.setRequestProperty("User-Agent", "StarMade-Client");
        //Fixes some errors with java 7 not downloading properly
        SSLContext ssl = SSLContext.getInstance("TLSv1.2");
        ssl.init(null, null, new SecureRandom());
        System.setProperty("https.protocols", "TLSv1.2");
        //
        FileUtils.copyInputStreamToFile(openConnection.getInputStream(), new File(fileName));
    }

    public static void disableAllMods() {
        DebugFile.log("==== Disabling All Mods ====");
        for (StarMod mod : StarLoader.starMods) {
            if (mod.isEnabled()) {
                mod.onDisable();
                mod.flagEnabled(false);
            }
        }
        StarLoader.clearListeners();
        StarRunnable.deleteAll();
    }

    public static boolean preClientConnect(String serverHost, int serverPort) {

        DebugFile.log("Enabling mods...");
        ArrayList<ModInfo> serverMods = ServerModInfo.getServerInfo(ServerModInfo.getServerUID(serverHost, serverPort));
        if (serverMods == null) {
            DebugFile.log("Mod info not found for: " + serverHost + ":" + serverPort + " This is likely because they direct connected");
            Controller.getResLoader().setLoadString("Getting server mod info...");
            StarMadeNetUtil starMadeNetUtil = new StarMadeNetUtil();
            try {
                System.err.println(starMadeNetUtil.getServerInfo(serverHost, serverPort, 9000).toString());
                //should register the mods.
                serverMods = ServerModInfo.getServerInfo(ServerModInfo.getServerUID(serverHost, serverPort));
            } catch (IOException e) {
                //???
                e.printStackTrace();
            }
        }
        if (serverMods == null) {
            DebugFile.log("Mods not found even after refresh... rip");
            serverMods = new ArrayList<ModInfo>();
        }
        if (serverHost.equals("localhost")) {
            DebugFile.log("Connecting to own server, mods are already enabled by the server");
        } else {
            disableAllMods();
            ArrayList<StarMod> enableQueue = new ArrayList<StarMod>();
            for (StarMod mod : StarLoader.starMods) {
                System.err.println("[Client] >>> Found mod: " + mod.modName);
                //DebugFile.log("Mod info WAS found");
                for (ModInfo serverMod : serverMods) {
                    DebugFile.log("le test: " + serverMod.name);
                    if (serverMod.name.equals(mod.modName)) { //|| EnabledModFile.getInstance().isClientEnabled(mod.getInfo())) {
                        DebugFile.log("[Client] >>> Correct mod name: " + serverMod.name);
                        if (serverMod.version.equals(mod.modVersion)) {
                            serverMods.remove(serverMod);
                            enableQueue.add(mod);
                            DebugFile.log("[Client] <<< Enabled: " + mod.modName);
                            break;
                        }
                    }
                }
            }
            //DebugFile.log(serverMods.toString());
            if (!serverMods.isEmpty()) {
                //Now we need to download them from the client
                DebugFile.log("=== DEPENDENCIES NOT MET, DOWNLOADING MODS ===");
                for (ModInfo sMod : serverMods) {
                    sMod.fetchDownloadURL();
                    DebugFile.log("WE NEED TO DOWNLOAD: " + sMod.toString());
                    try {
                        String fileName = "mods/" + sMod.name + ".jar";
                        Controller.getResLoader().setLoadString("Downloading mod: " + sMod.name);
                        downloadFile(new URL(sMod.downloadURL), fileName);
                        DebugFile.log("Successfully downloaded mod: " + sMod.name + ", version: " + sMod.version + ", from: " + sMod.downloadURL + ", into: " + sMod.name + ".jar");
                        //Get file, convert to URL
                        URL[] url = new URL[]{new File(fileName).toURI().toURL()};
                        Controller.getResLoader().setLoadString("Done downloading, Loading mod: " + sMod.name);
                        StarMod starMod = SMModLoader.loadModFromJar(new URLClassLoader(url), new JarFile(fileName));
                        Controller.getResLoader().setLoadString("Mod loaded.");
                        enableQueue.add(starMod);

                    } catch (Exception e) {
                        DebugFile.log("Failed to download, reason: ");
                        DebugFile.logError(e, null);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to download mods... please send in starloader.log and log/starmade0.log");
                    }
                }
                //JOptionPane.showMessageDialog(null, "We are going to need to download some mods... fancy gui coming later");
                //DebugFile.log("We are going to download some mods, so dont start the client yet");
            }
            enableMods(enableQueue);
        }
        //Force enable any test mods
        for (StarMod starMod : StarLoader.starMods) {
            if (!starMod.isEnabled() && starMod.forceEnable) {//EnabledModFile.getInstance().isClientEnabled(starMod.getInfo())
                StarLoader.enableMod(starMod);
            }
        }

        DebugFile.log("===== Done downloading, listing mods =====");
        StarLoader.dumpModInfos();
        DebugFile.log("==========================================");
        return true;
    }

    public static void postClientConnect() {

    }

    //TODO: add this, currently just disables them on server join
    public static void onClientLeave() {
        disableAllMods();
    }

    public static void main(String[] args) {
        try {
            downloadFile(new URL("https://starmadedock.net/content/turret-hotkey.8054/download"), "TurretHotKey.jar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableMods(ArrayList<StarMod> mods) {
        //1. Sort all mods by their name

        Collections.sort(mods, new Comparator<StarMod>() {
            @Override
            public int compare(StarMod mod1, StarMod mod2) {
                int m1Hash = mod1.modName.hashCode();
                int m2Hash = mod2.modName.hashCode();
                if (m1Hash == m2Hash) {
                    return 0;
                } else if (m1Hash < m2Hash) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        //2. Recursively enable mods in order
        Packet.clearPackets();
        for (StarMod mod : mods) {
            enableModRec(mod);
        }
        //Reason: Mods need to be enabled the same on the server and on the client in the same order for packet reasons


    }

    private static void enableModRec(StarMod mod) {
        for (Pair<String> dependency : mod.getDependencies()) {
            StarMod dep = fromInfo(new ModInfo(dependency.a, dependency.b));
            enableModRec(dep);
        }
        if(!mod.isEnabled()) {
            StarLoader.enableMod(mod);
        }
    }

    private static StarMod fromInfo(ModInfo info) {
        for (StarMod mod : StarLoader.starMods) {
            if (mod.getInfo().equals(info)) {
                return mod;
            }
        }
        throw new RuntimeException("Could not get ModInfo: " + info.toString());
    }
}
