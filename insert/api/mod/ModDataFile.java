package api.mod;

import api.DebugFile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ModDataFile {
    //name,last download date,client enabled
    //Turret Hotkey,112312,enabled
    //If a user downloads a mod manualy, it will not be in the file, so whenever we try to access it, create it
    private HashMap<String, Pair<Integer, Boolean>> data = new HashMap<String, Pair<Integer, Boolean>>();

    @NotNull
    private Pair<Integer, Boolean> getClientData(String name){
        Pair<Integer, Boolean> pair = data.get(name);
        if(pair != null) return pair;
        data.put(name, new ImmutablePair<Integer, Boolean>(-1, true));
        return data.get(name);
    }
    private static ModDataFile instance;
    public static ModDataFile getInstance() {
        if(instance == null){
            instance = new ModDataFile();
        }
        return instance;//
    }
    public void onDownloadedMod(String name, int downloadDate){
        data.put(name, new ImmutablePair<Integer, Boolean>(downloadDate, true));
        write();
    }
    //Mod data g/s
    public boolean isClientEnabled(String modName){
        return getClientData(modName).getRight();
    }
    public void setClientEnabled(String info, boolean value){
        int date = getClientData(info).getLeft();
        data.put(info, new ImmutablePair<Integer, Boolean>(date, value));
        write();
    }
    public int getDownloadDate(String name){
        return getClientData(name).getLeft();
    }

    public static void main(String[] args) {
        ModDataFile file = ModDataFile.getInstance();

        System.out.println(file.isClientEnabled("bruh"));
        file.setClientEnabled("bruh", true);
        System.out.println(file.isClientEnabled("bruh"));
        file.setClientEnabled("bruh", false);
        System.out.println(file.isClientEnabled("bruh"));
    }
    private ModDataFile(){
        try {
            File source = new File("clientmods.txt");
            if(!source.exists()){
                boolean newFile = source.createNewFile();
                if(!newFile){
                    DebugFile.err("Failed to create clientmods.txt");
                }
            }
            Scanner scanner = new Scanner(source);
            while (scanner.hasNext()){
                System.out.println();
                String[] split = scanner.nextLine().split(",");
                data.put(split[0], new ImmutablePair<Integer, Boolean>(Integer.parseInt(split[1]), Boolean.parseBoolean(split[2])));
                //enabledMods.add(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void write(){
        try {
            FileWriter writer = new FileWriter("clientmods.txt");
            for (Map.Entry<String, Pair<Integer, Boolean>> entry : data.entrySet()) {
                writer.write(entry.getKey() + ",");
                writer.write(entry.getValue().getLeft() + ",");
                writer.write(String.valueOf(entry.getValue().getRight()));
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}