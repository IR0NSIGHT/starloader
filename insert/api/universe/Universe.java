package api.universe;

import api.DebugFile;
import api.main.GameServer;
import api.mod.StarLoader;
import api.server.Server;
import api.utils.StarRunnable;
import api.utils.callbacks.ShipSpawnCallback;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.gui.catalog.newcatalog.CatalogScrollableListNew;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SegmentControllerHpController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.player.catalog.CatalogManager;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.controller.BluePrintController;
import org.schema.game.server.controller.EntityAlreadyExistsException;
import org.schema.game.server.controller.EntityNotFountException;
import org.schema.game.server.controller.GameServerController;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.game.server.data.blueprint.ChildStats;
import org.schema.game.server.data.blueprint.SegmentControllerOutline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

    public void spawnShip(Vector3i sector, int localX, int localY, int localZ, String catalogName, String spawnName, int factionId){
        spawnShip(sector,localX,localY,localZ,catalogName, spawnName, factionId, null);
    }
    //Cant return ship because it is put into a queue, then spawned
    //could just force spawn it (code above) but its probably best to not mess with that
    public void spawnShip(final Vector3i sector, int localX, int localY, int localZ, String catalogName, final String spawnName, int factionId, final ShipSpawnCallback spawnCallback){
        //TODO: prevent client mods from breaking if they try to spawn ships in:
        assert true : "Only server mods can spawn ships!";
        boolean docked = false;
        //True makes it spawn with AI active.
        //Server spawn:
        String command = "spawn_entity_pos "
                + catalogName + " \"" + spawnName + "\" " + sector.x + " " + sector.y + " " + sector.z + " " +
                localX + " " + localY + " " + localZ + " " + factionId + " true";
        DebugFile.info("[SERVER] Executing command: " + command);
        Server.executeAdminCommand(command);
        DebugFile.log("Executed");
        //Ship spawn callback:
        //Cannot directly return ship because it doesnt instantly spawn
        if(spawnCallback != null) {
            new StarRunnable() {
                @Override
                public void run() {
                    for (Ship ship : Universe.getUniverse().getShips(sector)){
                        if(ship.getRealName().equals(spawnName)){
                            Server.broadcastMessage(ship.getRealName());
                            spawnCallback.onShipSpawn(ship);
                            cancel();
                        }
                    }
                }
            }.runTimer(1);
        }

        //Client request spawn:
        //this.getState().getController().sendAdminCommand(AdminCommands.LOAD_AS_FACTION, catalogName, spawnName, factionId);
    }
    public ArrayList<SimpleTransformableSendableObject<?>> getEntities(){
        ArrayList<SimpleTransformableSendableObject<?>> r = new ArrayList<>();
        for (Integer sectorId : GameServer.getServerState().activeSectors) {
            Sector sector = GameServer.getServerState().getUniverse().getSector(sectorId);
            r.addAll(sector.getEntities());
        }
        return r;
    }
    public ArrayList<Ship> getShips(){
        ArrayList<Ship> r = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> entity : getEntities()){
            if(entity instanceof Ship){
                r.add((Ship) entity);
            }
        }
        return r;
    }
    public ArrayList<Ship> getShips(Vector3i sector){
        ArrayList<Ship> r = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> entity : getSector(sector).getEntities()){
            if(entity instanceof Ship){
                r.add((Ship) entity);
            }
        }
        return r;
    }

    public Sector getSector(int x, int y, int z) {
        return getSector(new Vector3i(x,y,z));
    }
    public Sector getSector(Vector3i v) {
        try {
            return GameServer.getServerState().getUniverse().getSector(v, true);
        } catch (IOException e) {
            DebugFile.log("[ERROR] Getting sector returned error");
            e.printStackTrace();
        }
        return null;
    }


    private static Universe universe;

    public static Universe getUniverse() {
        if(universe == null) universe = new Universe();
        return universe;
    }
}
