package api.universe;

import api.faction.Faction;
import api.main.GameServer;
import api.mod.StarLoader;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.server.data.Galaxy;

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
        ArrayList<Sector> sectors = new ArrayList<Sector>();
        //((VoidSystem) internalSystem).getSunSectorPosAbs(Galaxy.ge)

        //Todo:Figure out how to get all sectors in a system.
        return sectors;
    }
    public Faction getOwnerFaction(){
        org.schema.game.common.data.player.faction.Faction internalFaction = StarLoader.getGameState().getFactionManager().getFaction(internalSystem.getOwnerFaction());
        return new Faction(internalFaction);
    }

    public StellarSystem getInternalSystem() {
        return internalSystem;
    }
}
