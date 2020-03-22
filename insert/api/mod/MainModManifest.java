package api.mod;

import api.DebugFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainModManifest {
    private static MainModManifest instance;
    public static MainModManifest getManifest(){
        if(instance == null){
            instance = new MainModManifest();
        }
        return instance;
    }

    public static void main(String[] args) {
        MainModManifest manifest = getManifest();
        for (ModInfo modInfo : manifest.allModInfo) {
            System.out.println(modInfo.toString());
        }
    }
    private ArrayList<ModInfo> allModInfo = new ArrayList<>();
    private MainModManifest(){
        URL url = null;
        try {
            url = new URL("https://pastebin.com/raw/f1X7ZK4V");
            Scanner s = new Scanner(url.openStream());
            while (s.hasNext()){
                String modLine = s.next();
                System.out.println(modLine);
                String[] split = modLine.split(",");
                ModInfo info = new ModInfo(split[0], split[1]);
                info.downloadURL = split[2];
                allModInfo.add(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getURL(ModInfo info){
        DebugFile.info("Getting URL for: " + info.serialize());
        for (ModInfo mod : allModInfo) {
            DebugFile.info("Trying against: " + mod.serialize());
            //.serialize() is toString but only with name and version
            //So we compare a mods name and version to the manifest to get its download url
            if(mod.serialize().equals(info.serialize())){
                return mod.downloadURL;
            }
        }
        DebugFile.err("MOD MANIFEST WAS NOT FOUND ::: FAILED TO DOWNLOAD DEPENDENCY: " + info.name + " Version: " + info.version);
        return "";
    }
}
