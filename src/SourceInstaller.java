import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SourceInstaller {
    //Installs starloader into Starmade from source
    //1. Delete api/
    //2. Add all files to api/
    //3. Strip //INSERTED CODE lines from source (src/) and (schine/src/)
    //4. Re-add them
    //5. Replace methods (manually for now)
    public static void main(String[] args) {
        try {
            deleteApi();
            addApi();
            stripInsertLines();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String sourceDir = "C:\\Users\\Jake\\IdeaProjects\\StarMadeGit";
    public static void deleteApi(){
        try {
            FileUtils.deleteDirectory(new File(sourceDir + "\\src\\api\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addApi(){
        try {
            FileUtils.copyDirectory(new File("insert\\api\\"), new File(sourceDir + "\\src\\api\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> readFileToBuffer(File f){
        ArrayList<String> list = new ArrayList<String>();
        try {
            Scanner s = new Scanner(f);
            while (s.hasNext()){
                list.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void stripInsertLines() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(sourceDir + "\\src\\org"), null, true);
        files.addAll(FileUtils.listFiles(new File(sourceDir + "\\schine\\src"), null, true));
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()){
            File next = iterator.next();
            ArrayList<String> lines = readFileToBuffer(next);
            int totalLines = lines.size();
            int lineNum = 0;
            boolean deletingLines = false;
            boolean modifiedFile = false;
            //Check if file is modified
            for(String lineStr : lines){
                //jank
                if(lineStr.contains("//INSERTED CODE")){
                    modifiedFile = true;
                    break;
                }
            }
            if(modifiedFile) {
                FileWriter writer = new FileWriter(next, false);
                for (String lineStr : lines) {
                    lineNum++;
                    if (lineStr.contains("//INSERTED CODE")) {
                        deletingLines = true;
                    }
                    if (!deletingLines) {
                        writer.write(lineStr);
                        if (lineNum <= lines.size()) {
                            writer.write("\n");
                        }
                    } else {
                        if (lineStr.contains("///")) {
                            deletingLines = false;
                        }
                    }

                }
                writer.close();
            }
        }
    }
    public static void addInsertLines(){


        Collection<File> files = FileUtils.listFiles(new File(sourceDir + "\\insert\\org"), null, true);
        for (File f : files){
            ArrayList<String> lines = readFileToBuffer(f);
            ArrayList<Pair<Integer, String>> list = new ArrayList<Pair<Integer, String>>();
            boolean reading = false;
            int insertionNum = -1;
            for (String line : lines) {
                if(line.contains("//INSERTED CODE")){
                    reading = true;
                    insertionNum = Integer.parseInt(line.split(Pattern.quote("@"))[1]);
                }
                if(reading){
                    list.add(new ImmutablePair<Integer, String>(insertionNum, line));
                    insertionNum++;
                    if(line.contains("///")){
                        reading = false;
                    }
                }
            }
            try {
                System.out.println(f.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Now that we've gathered all the insert lines, insert them

        }
    }
}
