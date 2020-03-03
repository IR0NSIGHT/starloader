package api.listener.listeners;

import api.listener.events.ServerPingEvent;
import api.listener.type.ServerListener;

@ServerListener
public interface ServerPingListener {
    void onChatEvent(ServerPingEvent event);
    Integer id = 4;
}
