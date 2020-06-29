package api.listener.events.register;

import api.listener.events.Event;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerModule;
import org.schema.game.common.controller.elements.ShipManagerContainer;

import java.util.ArrayList;

public class ElementRegisterEvent extends Event {
    private ShipManagerContainer container;

    public ElementRegisterEvent(ShipManagerContainer container){

        this.container = container;
    }

    private ArrayList<ManagerModule> modules = new ArrayList<ManagerModule>();
    public void addModuleCollection(ManagerModule manager){
        modules.add(manager);
    }

    public SegmentController getSegmentController(){
        return container.getSegmentController();
    }

    public ArrayList<ManagerModule> getModules() {
        return modules;
    }
}
