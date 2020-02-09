package api.mod;

import api.listener.events.Listener;
import org.schema.game.common.controller.elements.VoidElementManager;

public class StarMod {
    public String modName;
    public String modAuthor;
    public String modDescription;
    public String modVersion;
    public String modSMVersion;
    public void onEnable(){
        register();
    }
    public void register(){
        StarLoader.starMods.add(this);
    }
    public void registerListener(Listener listener){
        StarLoader.registerListener(listener);

    }

}
