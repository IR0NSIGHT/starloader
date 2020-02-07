package api;

import api.listener.events.ChatReceiveListener;
import api.listener.events.Listener;
import org.schema.schine.graphicsengine.core.ChatListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StarLoader {
    public static ArrayList<ClientMod> clientMods = new ArrayList<>();
    public static ArrayList<ChatReceiveListener> chatListeners = new ArrayList<>();
    public static void registerChatListener(ChatReceiveListener listener){
        chatListeners.add(listener);
    }
}
