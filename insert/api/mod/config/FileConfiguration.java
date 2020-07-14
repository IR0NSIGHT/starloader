package api.mod.config;

import api.DebugFile;
import api.mod.StarMod;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
//TODO: Switch off to TOML4J
public class FileConfiguration {
    private StarMod mod;
    private HashMap<String, String> values = new HashMap<String, String>();

    private String configPath;
    public FileConfiguration(StarMod mod, String name){
        this.mod = mod;
        this.configPath = "modconfigs" + File.separator + mod.getName() + File.separator + name +".yml";
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
    public String getConfigurableValue(String path, String defaultVal){
        String string = getString(path);
        if(string == null){
            set(path, defaultVal);
            return defaultVal;
        }
        return string;
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
    //-- todo remove and migrate to TOML
    public void setList(String path, ArrayList<String> list){
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        set(path, sb);
    }
    public ArrayList<String> getList(String path){
        ArrayList<String> r = new ArrayList<String>();
        String string = getString(path);
        if(string == null) return r;
        r.addAll(Arrays.asList(string.split(",")));
        return r;
    }
    //--
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

    public Set<String> getKeys() {
        return values.keySet();
    }
}