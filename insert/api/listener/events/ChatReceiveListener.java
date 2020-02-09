package api.listener.events;


import api.listener.type.ClientListener;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;

@ClientListener
public interface ChatReceiveListener extends Listener {
    void onChatReceive(PlayerState player, ChatMessage message);
}
