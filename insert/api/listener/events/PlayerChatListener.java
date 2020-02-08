package api.listener.events;

import api.listener.type.ServerListener;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;


@FunctionalInterface
@ServerListener
public interface PlayerChatListener extends Listener{
    void onPlayerChat(PlayerState whoChatted, ChatMessage message);
}
