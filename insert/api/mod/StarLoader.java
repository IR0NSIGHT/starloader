package api.mod;

import api.DebugFile;
import api.entity.Player;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.ServerPingEvent;
import api.listener.events.block.BlockActivateEvent;
import api.server.Server;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StarLoader {
    public static ArrayList<StarMod> starMods = new ArrayList<>();
    //TODO assign each of these a id and use an array
    //Allocate size for 20 listeners
    public static ArrayList<ArrayList<Listener>> listeners = new ArrayList<>();
    static{
        for (int i = 0; i < 40; i++) {
            listeners.add(new ArrayList<Listener>());
        }
    }
    public static void clearListeners(){
        listeners.clear();
        for (int i = 0; i < 40; i++) {
            listeners.add(new ArrayList<Listener>());
        }
    }

    public static List<Listener> getListeners(int id){
        return listeners.get(id);
    }

    public static void enableMod(StarMod mod){
        DebugFile.log("== Enabling Mod " + mod.getInfo().toString());
        mod.onEnable();
        mod.flagEnabled(true);
    }
    public static void dumpModInfos(){
        for (StarMod mod : StarLoader.starMods) {
            DebugFile.log(mod.toString());
        }
    }

    private static int getIdFromEvent(Class<? extends Event> clazz){
        try {
            //Events have a static variable called id
            return (Integer) clazz.getField("id").get(null);
            //I could also make it a hashmap<string, listener> and no id's would be needed... but whatever
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            DebugFile.logError(e, null);
        }
        return 0;
    }
    public static void registerListener(Class<? extends Event> clazz, Listener l) {
        DebugFile.log("Registering listener " + clazz.getName());
        int id = getIdFromEvent(clazz);
        getListeners(id).add(l);
    }

    //fire event methods:
    public static void fireEvent(Class<? extends Event> clazz, Event ev){
        int id = getIdFromEvent(clazz);
        //DebugFile.log("Firing Event: " +clazz.getName());
        for (Listener listener : getListeners(id)) {
            try {
                listener.onEvent(ev);
            }catch (Exception e){
                DebugFile.log("Exception during event: " + clazz.getName());
                DebugFile.logError(e, null);
                if(Server.isInitialized()) {
                    Server.broadcastMessage("An error occurred during event: " + clazz);
                }

            }
        }
    }
}
