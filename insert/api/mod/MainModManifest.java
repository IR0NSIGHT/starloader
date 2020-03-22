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
        getManifest();
    }
    private ArrayList<ModInfo> allModInfo = new ArrayList<>();
    private MainModManifest(){
        URL url = null;
        try {
            url = new URL("https://pastebin.com/raw/7rjKV1ZG");
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
        for (ModInfo mod : allModInfo) {
            if(mod.toString().equals(info.toString())){
                return mod.downloadURL;
            }
        }
        DebugFile.err("MOD MANIFEST WAS NOT FOUND ::: FAILED TO DOWNLOAD DEPENDENCY: " + info.name + " Version: " + info.version);
        return "";
    }
}
