package api;

import api.listener.events.Listener;

public class ClientMod {
    public void onEnable(){
        StarLoader.clientMods.add(this);
    }
}
