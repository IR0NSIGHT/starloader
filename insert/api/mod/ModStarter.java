package api.mod;

import api.DebugFile;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ModStarter {
    public static void preServerStart(){
        //Enable all mods for now, later there will be a gui to turn them on or off
        DebugFile.log("[Server] Enabling mods...");
        for (StarMod mod : StarLoader.starMods){
            DebugFile.log("[Server] >>> Enabling: " + mod.modName);
            mod.onEnable();
            DebugFile.log("[Server] <<< Enabled: " + mod.modName);
        }
    }
    public static void postServerStart(){
        //whatever lol
    }
    //doesnt work cause of user agent stuff
    /*public static void downloadFile(URL url, String fileName) throws IOException {
        FileUtils.copyURLToFile(url, new File(fileName));
    }*/
    public static void downloadFile(URL url, String fileName) throws IOException {
        System.setProperty("http.agent", "StarMade-Client");
        FileUtils.copyURLToFile(url, new File(fileName));
    }
    public static boolean preClientConnect(String serverHost){
        //TODO only enable the ones that are enabled on the server, and the ones that are set to force enable
        DebugFile.log("Enabling mods...");
        ArrayList<ModInfo> serverMods = ServerModInfo.getServerInfo(serverHost);
        for (StarMod mod : StarLoader.starMods){
            System.err.println("[Client] >>> Found mod: " + mod.modName);
            if(serverMods == null){
                DebugFile.log("Mod info not found for: " + serverHost + " This is likely because they direct connected");
            }else {
                DebugFile.log("Mod info WAS found");
                for (ModInfo serverMod : serverMods) {
                    DebugFile.log("le test: " + serverMod.name);
                    if (serverMod.name.equals(mod.modName)) {
                        DebugFile.log("[Client] >>> Correct mod name: " + serverMod.name);
                        if (serverMod.version.equals(mod.modVersion)) {
                            serverMods.remove(serverMod);
                            mod.onEnable();
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
                DebugFile.log("WE NEED TO DOWNLOAD: " + sMod.toString());
                sMod.fetchDownloadURL();
            }
            JOptionPane.showMessageDialog(null, "We are going to need to download some mods... fancy gui coming later");
            /*try {
                //downloadFile(new URL("https://starmadedock.net/content/turret-hotkey.8054/download"), "TurretHotKey.jar");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            DebugFile.log("We are going to download some mods, so dont start the client yet");
            return false;
        }else{
            DebugFile.log("all good");
            return true;
        }
    }
    public static void postClientConnect(){

    }

    public static void main(String[] args) {
        try {
            downloadFile(new URL("https://starmadedock.net/content/turret-hotkey.8054/download"), "TurretHotKey.jar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
