package api.listener.events.register;

import api.listener.events.Event;
import api.systems.addons.custom.CustomAddOn;
import org.schema.game.common.controller.elements.ManagerContainer;

import java.util.ArrayList;

public class RegisterAddonsEvent extends Event {
    public static int id = idLog++;
    private ManagerContainer<?> container;
    public RegisterAddonsEvent(ManagerContainer<?> container){
        this.container = container;
    }
    public ManagerContainer<?> getContainer() {
        return container;
    }

    public ArrayList<CustomAddOn> addons = new ArrayList<CustomAddOn>();
    public void addAddOn(CustomAddOn addOn){
        addons.add(addOn);
    }
}
