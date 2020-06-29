package api.listener.events.controller.fleet;

import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.common.data.fleet.FleetManager;

public class FleetCacheEvent extends FleetEvent{
    public FleetCacheEvent(FleetManager fleetManager, Fleet fleet) {
        super(fleetManager, fleet);
    }
}
