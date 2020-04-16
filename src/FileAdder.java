import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileAdder {
    static ArrayList<NamedInputStream> filesToAdd = new ArrayList<NamedInputStream>();
    public static void mkDefaults(){
        //Just need a class to get dir
        FileAdder base = new FileAdder();

        InputStream in = base.getClass().getResourceAsStream("/api/SMModLoader.class");
        filesToAdd.add(new NamedInputStream(in, "api/SMModLoader.class"));
        try {
            listPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void listPath() throws IOException {
        CodeSource src = FileAdder.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while(true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null) {
                    System.out.println("broke");
                    break;
                }
                String name = e.getName();
                if (name.startsWith("org") || name.startsWith("api")) {
                    System.out.println("Found class: " + name);
                    filesToAdd.add(new NamedInputStream(FileAdder.class.getResourceAsStream(name), name));
                }
            }
        } else {
            System.out.println("stuffs broke yo");
        }
    }
    public static void main(String[] args) {
        try {
            listPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
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
        registerFile("api.SMModLoader");

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

    public static String filePath(String s) {

        return s.replace(".", File.separator);
    }*/
}
