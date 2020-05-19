package api.universe;

import api.DebugFile;
import api.entity.Entity;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.server.Server;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.Sector;
import org.schema.schine.network.objects.Sendable;

import java.io.IOException;

public class Universe {

    private Server server;

    public Universe() {

    }

    public Server getServer() {
        return server;

    }
    /*public SegmentController spawnSegmentController(Sector sector, String catalogName, String name, int factionId){
        BluePrintController blueprintController = BluePrintController.active;
        Transform transform = new Transform();
        transform.setIdentity();
        final SegmentControllerOutline<?> loadBluePrint;
        try {
            loadBluePrint = blueprintController.loadBluePrint(GameServer.getServerState(), catalogName, name, transform, -1, factionId, blueprintController.readBluePrints(), sector.pos, null, "<system>", Sector.buffer, true, null, new ChildStats(false));
            loadBluePrint.spawnSectorId = new Vector3i(sector.pos);
        } catch (EntityNotFountException | IOException | EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
        GameServerController
    }*/

    public api.universe.Sector getSector(int x, int y, int z) {
        /**
         * Gets a sector using it's coordinates.
         */
        return getSector(new Vector3i(x,y,z));
    }
    /**
     * Gets a sector using it's vector coordinates.
     */
    public api.universe.Sector getSector(Vector3i sectorCoords) {
        return getSector(sectorCoords, true);
    }
    public api.universe.Sector getSector(Vector3i sectorCoords, boolean load) {
        try {
            Sector sector = GameServer.getServerState().getUniverse().getSector(sectorCoords, load);

            if(sector == null){
                return null;
            }
            return new api.universe.Sector(sector);
        } catch (IOException e) {
            DebugFile.log("[ERROR] Getting sector returned error");
            e.printStackTrace();
        }
        return null;
    }

    public System getSystem(Vector3i systemCoords) {
        /**
         * Gets a system using it's vector coordinates.
         */
        try {
            return new System(GameServer.getServerState().getUniverse().getStellarSystemFromStellarPos(systemCoords));
        } catch (IOException e) {
            DebugFile.log("[ERROR] Getting system returned error");
            e.printStackTrace();
        }
        return null;
    }

    public System getSystem(int x, int y, int z) {
        /**
         * Gets a system using it's coordinates.
         */
        Vector3i systemCoords = new Vector3i();
        systemCoords.x = x;
        systemCoords.y = y;
        systemCoords.z = z;

        try {
            return new System(GameServer.getServerState().getUniverse().getStellarSystemFromStellarPos(systemCoords));
        } catch (IOException e) {
            DebugFile.log("[ERROR] Getting system returned error");
            e.printStackTrace();
        }
        return null;
    }
    public Entity getEntityFromId(int id){
        return null;// TODO this
        //Universe.getUniverse().getSector()
    }

    private static Universe universe;

    public static Universe getUniverse() {
        /**
         * Gets the server universe.
         */
        if(universe == null) universe = new Universe();
        return universe;
    }
}
