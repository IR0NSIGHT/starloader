package api.listener.events.register;

import api.listener.events.Event;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.UsableControllableElementManager;

import java.util.ArrayList;

public class ElementRegisterEvent extends Event {
    public static int id = idLog++;
    private ShipManagerContainer container;

    public ElementRegisterEvent(ShipManagerContainer container){

        this.container = container;
    }

    public ArrayList<UsableControllableElementManager<?,?,?>> internalManagers = new ArrayList<UsableControllableElementManager<?,?,?>>();
    public void addInternal(UsableControllableElementManager<?,?,?> manager){
        internalManagers.add(manager);
    }
}
