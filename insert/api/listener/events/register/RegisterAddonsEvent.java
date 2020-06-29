package api.listener.events.register;

import api.listener.events.Event;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.RecharchableSingleModule;

import java.util.ArrayList;

public class RegisterAddonsEvent extends Event {
    private ManagerContainer<?> container;
    public RegisterAddonsEvent(ManagerContainer<?> container){
        this.container = container;
    }
    public ManagerContainer<?> getContainer() {
        return container;
    }

    public ArrayList<RecharchableSingleModule> addons = new ArrayList<RecharchableSingleModule>();
    public void addModule(RecharchableSingleModule addOn){
        addons.add(addOn);
    }
}
