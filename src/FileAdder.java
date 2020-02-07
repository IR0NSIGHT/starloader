import java.io.File;
import java.util.ArrayList;

public class FileAdder {
    static ArrayList<String> filesToAdd = new ArrayList<>();
    public static void registerFile(String file){
        filesToAdd.add(filePath(file) + ".class");
    }
    public static void mkDefaults(){
        registerFile("SMModLoader");
        //registerFile("org.schema.game.client.data.GameClientState");
    }
    public static String filePath(String s) {

        return s.replace(".", File.separator);
    }
}
