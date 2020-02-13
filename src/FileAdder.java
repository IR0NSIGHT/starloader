import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdder {
    static ArrayList<String> filesToAdd = new ArrayList<>();
    public static void registerFile(String file){
        System.out.println("Reg: " + filePath(file) + ".class");
        filesToAdd.add(filePath(file) + ".class");
    }
    public static void registerFileRaw(String file){
        System.out.println("Regx: " + file);
        filesToAdd.add(file);
    }
    public static void mkDefaults(){
        //Classes that are not in /api .... for some reason ...
        registerFile("SMModLoader");
        //registerFile("org.schema.game.client.view.gui.shiphud.newhud.TargetPowerBar");
        //registerFile("org.schema.game.client.view.gui.shiphud.newhud.TargetPanel");

        //maybe make it so anything in org.schema is imported

        //Register all classes in 'api'
        for (String cl : getAPIClasses("api")){
            //classes\api\listener\events\ChatReceiveListener.class turns into:
            // api.listener.events.ChatReceiveListener
            String replace = cl.replace("classes" + File.separator, "");

            registerFileRaw(replace);
        }
        for (String cl : getAPIClasses("org")){
            String replace = cl.replace("classes" + File.separator, "");

            registerFileRaw(replace);
        }
    }

    public static void main(String[] args) {
        getAPIClasses("api");
    }

    public static List<String> getAPIClasses(String subclass){
        ArrayList<String> strValues = new ArrayList<>();
        for(File f : getFiles(new File("classes" + File.separator + subclass))){
            strValues.add(f.getPath());
            System.out.println(f.getPath());
        }
        return strValues;
    }
    public static List<File> getFiles(File folder){
        ArrayList<File> ret = new ArrayList<>();
        for (File f : folder.listFiles()){
            if(f.isDirectory()){
                ret.addAll(getFiles(f));
            }else{
                ret.add(f);
            }
        }
        return ret;
    }
    /*
    public static List<String> getAPIClasses() {
        try (Stream<Path> walk = Files.walk(Paths.get("classes"))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("something broke");
    }*/
    public static String filePath(String s) {

        return s.replace(".", File.separator);
    }
}
