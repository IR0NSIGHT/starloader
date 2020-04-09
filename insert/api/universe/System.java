package api.universe;

import org.schema.game.common.data.world.StellarSystem;
import java.util.ArrayList;

public class System {

    private StellarSystem internalSystem;

    public System(StellarSystem internalSystem) {
        this.internalSystem = internalSystem;
    }

    public ArrayList<Sector> getSectors() {
        /**
         * Gets all the sectors inside the system. Currently doesn't work.
         */
        ArrayList<Sector> sectors = new ArrayList<>();
        //Todo:Figure out how to get all sectors in a system.
        return sectors;
    }
}
