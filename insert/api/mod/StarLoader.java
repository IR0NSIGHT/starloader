package api.mod;

import api.listener.events.ServerPingEvent;
import api.listener.listeners.*;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;

import javax.swing.*;
import java.util.ArrayList;
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

    public static List<Listener> getListeners(int id){
        return listeners.get(id);
    }
    public static void registerListener(Listener l){
        try {
            //Listeners have a static variable called id
            Integer id = (Integer) l.getClass().getField("id").get(null);
            getListeners(id).add(l);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //fire client event methods:
    public static void fireChatReceiveEvent(PlayerState player, ChatMessage message){
        for (Listener l : getListeners(ChatReceiveListener.id)){
            ((ChatReceiveListener) l).onChatReceive(player, message);
        }
    }
    public static void fireTargetPanelDraw(TargetPanel panel){
        for (Listener l : getListeners(TargetPanelDrawListener.id)){
            ((TargetPanelDrawListener) l).onDraw(panel);
        }
    }

    //Fire server events
    public static void firePlayerChatEvent(PlayerState player, ChatMessage message){
        for (Listener l : getListeners(PlayerChatListener.id)){
            ((PlayerChatListener) l).onPlayerChat(player, message);
        }
    }
    public static void fireServerPingEvent(ServerPingEvent event){
        for (Listener l : getListeners(ServerPingListener.id)){
            ((ServerPingListener) l).onChatEvent(event);
        }
    }
}
