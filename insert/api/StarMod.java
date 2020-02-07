package api;

import api.listener.events.ChatReceiveListener;

public class StarMod {
    public void onEnable(){
        register();
    }
    public void register(){
        StarLoader.starMods.add(this);
    }
    static{

    }
}
