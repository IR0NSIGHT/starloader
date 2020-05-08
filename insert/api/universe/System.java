package api.universe;

import api.entity.Entity;
import api.faction.Faction;
import api.main.GameServer;
import api.mod.StarLoader;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.server.data.Galaxy;
import org.schema.game.server.data.GameServerState;

import java.io.IOException;
import java.sql.SQLException;
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

    public Faction getFaction(){
        return Faction.fromId(internalSystem.getOwnerFaction());
    }
    public String getOwnerUID(){
        return internalSystem.getOwnerUID();
    }
    public Vector3i getOwnerPos(){
        return internalSystem.getOwnerPos();
    }
    public void resetClaim(){
        internalSystem.setOwnerFaction(0);
    }
    public boolean isClaimed(){
        return getOwnerFaction().getID() != 0;
    }
    public void claim(Entity e){
        internalSystem.setOwnerUID(e.getUID());
        internalSystem.setOwnerFaction(e.getFaction().getID());
        internalSystem.getOwnerPos().set(e.getSectorPosition());
        GameServerState server = GameServer.getServerState();
        try {
            server.getDatabaseIndex().getTableManager().getSystemTable().updateOrInsertSystemIfChanged(internalSystem, true);

            server.getUniverse()
                    .getGalaxyFromSystemPos(internalSystem.getPos()).getNpcFactionManager()
                    .onSystemOwnershipChanged(0, internalSystem.getOwnerFaction(), internalSystem.getPos());
            StarLoader.getGameState().sendGalaxyModToClients(internalSystem, e.getSectorPosition());

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
