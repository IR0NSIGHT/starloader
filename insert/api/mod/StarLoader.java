package api.mod;

import api.DebugFile;
import api.entity.Player;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.ServerPingEvent;
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
        for (int i = 0; i < 30; i++) {
            listeners.add(new ArrayList<Listener>());
        }
    }
    public static List<Player> getOnlinePlayers(){
        return null;
    }
    public static Collection<PlayerState> getOnlinePlayerStates(){
        return GameClientState.instance.getOnlinePlayersLowerCaseMap().values();
    }

    public static List<Listener> getListeners(int id){
        return listeners.get(id);
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

    //fire client event methods:
    public static void fireEvent(Class<? extends Event> clazz, Event event){
        int id = getIdFromEvent(clazz);
        for (Listener listener : getListeners(id)) {
            try {
                listener.onEvent(event);
            }catch (Exception e){
                DebugFile.log("Exception during event: " + clazz.getName());

            }
        }
    }
}
