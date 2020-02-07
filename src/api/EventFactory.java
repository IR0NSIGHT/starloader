package api;

import api.listener.events.ChatReceiveListener;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;
import org.schema.schine.graphicsengine.core.ChatListener;

import java.util.ArrayList;

public class EventFactory {
    public static ArrayList<ChatReceiveListener> chatListeners = new ArrayList<>();
    public static void fireChatEvent(PlayerState player, ChatMessage message){
        StarLoader.chatListeners.forEach(e -> e.onChatReceive(player, message));
    }
}
