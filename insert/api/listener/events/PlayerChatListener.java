package api.listener.events;

import api.listener.type.ServerListener;


@FunctionalInterface
@ServerListener
public interface PlayerChatListener extends Listener{
    void onPlayerChat(PlayerState whoChatted, ChatMessage message);
}
