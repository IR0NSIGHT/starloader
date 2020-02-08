import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAdder {
    static ArrayList<String> filesToAdd = new ArrayList<>();
    public static void registerFile(String file){
        filesToAdd.add(filePath(file) + ".class");
    }
    public static void mkDefaults(){
        registerFile("SMModLoader");
        registerFile("api.StarMod");
        //registerFile("org.schema.game.client.data.GameClientState");
    }

    public static void main(String[] args) {
        try (Stream<Path> walk = Files.walk(Paths.get("C:\\projects"))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            result.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String filePath(String s) {

        return s.replace(".", File.separator);
    }
}
