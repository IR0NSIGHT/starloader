import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        getAPIClasses();
    }
    public static List<String> getAPIClasses(){
        for(File f : getFiles(new File("classes"))){
            System.out.println(f.getPath());
        }
        return null;
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
