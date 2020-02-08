import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAdder {
    static ArrayList<String> filesToAdd = new ArrayList<>();
    public static void registerFile(String file){
        filesToAdd.add(filePath(file) + ".class");
    }
    public static void registerFileRaw(String file){
        filesToAdd.add(file);
    }
    public static void mkDefaults(){
        //Classes that are not in /api .... for some reason ...
        registerFile("SMModLoader");
        //Register all classes in 'api'
        for (String cl : getAPIClasses()){
            //classes\api\listener\events\ChatReceiveListener.class turns into:
            // api.listener.events.ChatReceiveListener
            String replace = cl.replace("classes" + File.separator, "");

            registerFileRaw(replace);
        }
    }

    public static List<String> getAPIClasses() {
        try (Stream<Path> walk = Files.walk(Paths.get("classes"))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("something broke");
    }
    public static String filePath(String s) {

        return s.replace(".", File.separator);
    }
}
