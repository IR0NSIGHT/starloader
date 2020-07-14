package api;

import api.mod.StarMod;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
                writer.append("[").append(mod.getName()).append("] [Stacktrace]");
            }
            System.err.println("[StarLoader Mod Error]");
            String excMessage = ExceptionUtils.getMessage(e);
            writer.append(excMessage).append("\n");
            System.err.println(excMessage);

            for ( StackTraceElement ste : e.getStackTrace()){
                writer.append(ste.toString()).append("\n");
                System.err.println(ste.toString());
            }
            writer.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }
    public static void log(String s, StarMod mod){
        try {
            FileWriter writer = getWriter();
            if(mod == null){
                writer.append("[StarLoader] ");
            }else{
                writer.append("[").append(mod.getName()).append("] ");
            }
            writer.append(s).append("\n");
            //Also write to logs/starmade0.log
            System.err.println("[DebugFile] [StarLoader]" + s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clear(){
        try {
            writer = new FileWriter("starloader.log", false);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void err(String s){
        log("[ERROR] " + s, null);
    }
    public static void info(String s){
        log("[INFO] " + s, null);
    }
    public static void warn(String s){
        log("[WARNING] " + s, null);
    }
    public static void log(String s){
        log(s, null);
    }

}
