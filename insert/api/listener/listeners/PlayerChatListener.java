package api.listener.listeners;

import api.listener.type.ServerListener;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.network.objects.ChatMessage;


@ServerListener
public interface PlayerChatListener extends Listener{
    public abstract void onPlayerChat(PlayerState whoChatted, ChatMessage message);
    Integer id = 1;
}
