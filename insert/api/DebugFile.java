package api;

import api.mod.StarMod;
import org.schema.game.common.Starter;
import org.schema.game.common.data.player.PlayerState;

import java.io.FileWriter;
import java.io.IOException;

public class DebugFile {
    private static FileWriter writer = null;
    private static FileWriter getWriter() throws IOException {
        if(writer == null){
            writer = new FileWriter("starloader.log", true);
        }
        return writer;
    }
    public static void logError(Exception e, StarMod mod){
        try {

            FileWriter writer = getWriter();
            if(mod == null){
                writer.append("[StarLoader] [Stacktrace]");
            }else{
                writer.append("[" + mod.modName + "] [Stacktrace]");
            }
            writer.append(e.getLocalizedMessage()).append("\n");
            for ( StackTraceElement ste : e.getStackTrace()){
                writer.append(ste.toString()).append("\n");
            }
            writer.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }
    public static void log(String s, StarMod mod){
        //FIXME save file without closing it every time.
        try {
            FileWriter writer = getWriter();
            if(mod == null){
                writer.append("[StarLoader] ");
            }else{
                writer.append("[").append(mod.modName).append("] ");
            }
            writer.append(s).append("\n");
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
    public static void err(String s){
        log("[ERROR] " + s, null);
    }
    public static void log(String s){
        log(s, null);
    }

}
