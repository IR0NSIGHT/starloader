package api;

import api.mod.StarMod;

import java.io.FileWriter;
import java.io.IOException;

public class DebugFile {
    private static FileWriter writer = null;
    public static void logError(Exception e, StarMod mod){
        try {
            writer = new FileWriter("starloader.log", true);
            if(mod == null){
                writer.append("[StarLoader] ");
            }else{
                writer.append("[" + mod.modName + "] ");
            }
            writer.append(e.getMessage() + "\n");
            for ( StackTraceElement ste : e.getStackTrace()){
                writer.append(ste.toString() + "\n");
            }
            writer.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }
    public static void log(String s, StarMod mod){
        //FIXME save file without closing it every time.
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
    public static void clear(){
        try {
            writer = new FileWriter("starloader.log", false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void log(String s){
        log(s, null);
    }

}
