package api.mod.config;

import api.DebugFile;
import api.mod.StarMod;
import org.schema.game.common.controller.elements.scanner.ScanAddOn;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileConfiguration {
    private StarMod mod;
    private HashMap<String, String> values = new HashMap<String, String>();

    private String configPath;
    public FileConfiguration(StarMod mod){
        this.mod = mod;
        this.configPath = "modconfigs" + File.separator + mod.modName + File.separator + "config.yml";
        reloadConfig();
    }
    public void reloadConfig(){
        values.clear();
        read();
    }
    private void read(){
        try {
            Scanner scanner = new Scanner(new File(configPath));
            while (scanner.hasNext()){
                String next = scanner.nextLine();
                //no wack lines
                if(next.length() > 0){
                    String[] split = next.split(Pattern.quote(": "));
                    if(split.length > 2){
                        DebugFile.err("Too many colons on: " + next);
                    }
                    values.put(split[0], split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            DebugFile.warn("Config file: " + configPath + " not found, writing...");
            saveConfig();
            //e.printStackTrace();
        }
    }
    public int getInt(String path){
        return Integer.parseInt(values.get(path));
    }
    public double getDouble(String path){
        return Double.parseDouble(values.get(path));
    }
    public String getString(String path){
        return values.get(path);
    }
    public void set(String path, Object value){
        if(value == null){
            values.remove(path);
            return;
        }
        values.put(path, value.toString());
    }
    public void saveDefault(String... def){
        if(values.isEmpty()) {
            for (String s : def) {
                String[] split = s.split(Pattern.quote(": "));
                values.put(split[0], split[1]);
            }
            saveConfig();
        }
    }
    public void saveConfig(){
        try {
            File file = new File(configPath);
            file.getParentFile().mkdirs();
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            for (String key : values.keySet()) {
                String value = values.get(key);
                writer.write(key + ": " + value + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}