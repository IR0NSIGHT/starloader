package api.mod;

import api.config.BlockConfig;
import api.mod.config.FileConfiguration;
import org.schema.game.common.data.physics.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class StarMod {
    public String modName;
    public String modAuthor;
    public String modDescription;
    public String modVersion;
    public String modSMVersion;
    public boolean forceEnable;
    public boolean serverSide = false;
    private boolean isEnabled = false;
    private ArrayList<Pair<String>> dependencies = new ArrayList<Pair<String>>();

    public boolean isEnabled(){
        return isEnabled;
    }
    public void flagEnabled(boolean flag){
        isEnabled = flag;
    }
    /**
     * Where mods are enabled before/during world load
     */
    public void onEnable(){
        //register();
    }

    /**
     * When a player leaves a server, mods are disabled
     */
    public void onDisable(){

    }

    /**
     * Mods that do stuff directly when the game starts
     * Happends for EVERY mod, not just the ones enabled
     */
    public void onGameStart(){

    }

    //Builder style setters
    public StarMod addDependency(String name, String version){
        dependencies.add(new Pair<String>(name, version));
        return this;
    }
    public StarMod setModName(String modName) {
        this.modName = modName;
        return this;
    }

    public StarMod setModAuthor(String modAuthor) {
        this.modAuthor = modAuthor;
        return this;
    }

    public StarMod setModDescription(String modDescription) {
        this.modDescription = modDescription;
        return this;
    }

    public StarMod setModVersion(String modVersion) {
        this.modVersion = modVersion;
        return this;
    }

    public StarMod setModSMVersion(String modSMVersion) {
        this.modSMVersion = modSMVersion;
        return this;
    }
    public StarMod setServerSide(boolean server){
        serverSide = server;
        return this;
    }
    public boolean isValid(){
        return modName != null && modVersion != null;
    }

    @Override
    public String toString() {
        return "StarMod{" +
                "modName='" + modName + '\'' +
                ", modAuthor='" + modAuthor + '\'' +
                ", modDescription='" + modDescription + '\'' +
                ", modVersion='" + modVersion + '\'' +
                ", modSMVersion='" + modSMVersion + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }

    //Gets the info that will be sent to the client
    public ModInfo getInfo(){
        return new ModInfo(modName, modVersion);
    }

    public void onBlockConfigLoad(BlockConfig config){

    }
    //
    private HashMap<String, FileConfiguration> config = new HashMap<String, FileConfiguration>();
    public FileConfiguration getConfig(String name){
        FileConfiguration namedConfig = config.get(name);
        if(namedConfig == null){
            config.put(name, new FileConfiguration(this, name));
        }
        return namedConfig;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    //

    public ArrayList<Pair<String>> getDependencies() {
        return dependencies;
    }
}
