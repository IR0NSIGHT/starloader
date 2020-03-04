package api.mod;

import api.listener.events.Event;
import api.listener.events.ServerPingEvent;

public class EventFactory {
    public static void fireEvent(Event event){
        if(event instanceof ServerPingEvent){

        }
    }
}
