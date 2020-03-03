package api.listener.events;

import api.listener.type.ClientEvent;
import org.schema.schine.input.KeyboardEvent;

@ClientEvent
public class KeyPressEvent extends Event{
    public static int id = 5;
    private KeyboardEvent event;

    public KeyPressEvent(KeyboardEvent event) {
        this.event = event;
    }
//
    public char getChar(){
        return event.getCharacter();
    }
    public int getKey(){
        return event.getKey();
    }
    public boolean isKeyDown(){
        return event.state;
    }
    public KeyboardEvent getRawEvent(){
        return event;
    }
}
