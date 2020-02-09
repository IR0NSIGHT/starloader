package api;

import api.mod.StarMod;

import java.io.FileWriter;
import java.io.IOException;

public class DebugFile {
    private static FileWriter writer = null;
    public static void log(String s, StarMod mod){
        try {
            writer = new FileWriter("starloader.log", true);
            if(mod == null){
                writer.append("[StarLoader] ");
            }else{
                writer.append("[" + mod.modName + "] ");
            }
            writer.append(s + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void log(String s){
        log(s, null);
    }

}
