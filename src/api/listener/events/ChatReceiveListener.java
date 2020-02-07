package api.listener.events;


import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;

public interface ChatReceiveListener extends Listener {
    void onChatReceive(PlayerState player, ChatMessage message);
}
