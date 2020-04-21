package api.listener.events.register;

import api.element.block.Blocks;
import api.listener.events.Event;
import api.systems.modules.custom.CustomShipBeamElement;
import org.schema.game.common.controller.elements.ShipManagerContainer;

import java.util.ArrayList;

public class ElementRegisterEvent extends Event {
    public static int id = idLog++;
    private ShipManagerContainer container;

    public ElementRegisterEvent(ShipManagerContainer container){

        this.container = container;
    }
    private ArrayList<CustomShipBeamElement> customs = new ArrayList<CustomShipBeamElement>();
    public void registerElement(CustomShipBeamElement beamElement){
        customs.add(beamElement);
    }

    public ArrayList<CustomShipBeamElement> getCustoms() {
        return customs;
    }

    public void addElement(CustomShipBeamElement e) {
        customs.add(e);
        //Set elements as controllers, for ease of use.
        e.getControllerBlock().getInfo().controlledBy.add(Blocks.SHIP_CORE.getId());
        e.getModuleBlock().getInfo().controlledBy.add(e.getControllerBlock().getId());
        e.getControllerBlock().getInfo().controlling.add(e.getModuleBlock().getId());
    }
}
