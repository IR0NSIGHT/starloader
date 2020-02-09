package api.mod;

import api.DebugFile;
import api.listener.events.ChatReceiveListener;
import api.listener.events.Listener;
import api.listener.events.PlayerChatListener;
import api.listener.events.TargetPanelDrawListener;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class StarLoader {
    public static ArrayList<StarMod> starMods = new ArrayList<>();
    //TODO assign each of these a id and use an array
    private static HashMap<Class<? extends Listener>, ArrayList<Listener>> listeners = new HashMap<>();

    //Register a listener
    public static void registerListener(Listener listener){
        Class<? extends Listener> listenerClass = listener.getClass();
        if(!listeners.containsKey(listener.getClass())){
            listeners.put(listener.getClass(), new ArrayList<Listener>());
        }
        ArrayList<Listener> specificListeners = listeners.get(listenerClass);
        specificListeners.add(listener);
    }
    //Get listeners of a specific type
    private static ArrayList<Listener> getListeners(Class<? extends Listener> type){
        DebugFile.log("Getting listener: " + type.toString());
        ArrayList<Listener> specificListeners = listeners.get(type);
        if (specificListeners == null) {
            DebugFile.log("No listeners found");
            return new ArrayList<>();
        }
        DebugFile.log(specificListeners.size() + " listeners found");
        return specificListeners;
    }

    //fire client event methods:
    public static void fireChatReceiveEvent(PlayerState player, ChatMessage message){
        for (Listener l : getListeners(ChatReceiveListener.class)){
            ((ChatReceiveListener) l).onChatReceive(player, message);
        }
    }
    public static void fireTargetPanelDraw(TargetPanel panel){
        DebugFile.log("Firing targetdraw");
        for (Listener l : getListeners(TargetPanelDrawListener.class)){
            DebugFile.log("Passing to mod...");
            ((TargetPanelDrawListener) l).onDraw(panel);
        }
    }

    //Fire server events
    public static void firePlayerChatEvent(PlayerState player, ChatMessage message){
        for (Listener l : getListeners(PlayerChatListener.class)){
            ((PlayerChatListener) l).onPlayerChat(player, message);
        }
    }
}
