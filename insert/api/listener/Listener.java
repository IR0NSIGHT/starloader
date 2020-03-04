package api.listener;

import api.listener.events.Event;

public interface Listener {
    void onEvent(Event event);
}
