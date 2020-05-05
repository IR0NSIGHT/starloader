package api.listener.events.register;

import api.entity.Entity;
import api.listener.events.Event;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.UsableControllableElementManager;

import java.util.ArrayList;

public class ElementRegisterEvent extends Event {
    private ShipManagerContainer container;

    public ElementRegisterEvent(ShipManagerContainer container){

        this.container = container;
    }

    private ArrayList<ManagerModuleCollection> modules = new ArrayList<ManagerModuleCollection>();
    public void addInternal(ManagerModuleCollection manager){
        modules.add(manager);
    }

    public SegmentController getSegmentController(){
        return container.getSegmentController();
    }
    public Entity getEntity(){
        return new Entity(container.getSegmentController());
    }

    public ArrayList<ManagerModuleCollection> getModules() {
        return modules;
    }
}
