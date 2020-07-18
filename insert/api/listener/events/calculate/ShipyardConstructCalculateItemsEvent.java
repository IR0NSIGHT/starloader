package api.listener.events.calculate;

import api.listener.events.Event;
import org.schema.game.common.controller.elements.shipyard.orders.states.Constructing;

public class ShipyardConstructCalculateItemsEvent extends Event {
    private Constructing constructingStatef;

    public ShipyardConstructCalculateItemsEvent(Constructing constructingState){

        constructingStatef = constructingState;
    }

    public Constructing getConstructingState() {
        return constructingStatef;
    }
}
