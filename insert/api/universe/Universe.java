package api.universe;

import api.DebugFile;
import api.main.GameServer;
import api.server.Server;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.gui.catalog.newcatalog.CatalogScrollableListNew;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.admin.AdminCommands;

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
    public Ship spawnShip(Sector sector, int localX, int localY, int localZ, String catalogName, String spawnName, int factionId){
        //TODO: prevent client mods from breaking if they do something like this:
        assert true : "Only server mods can spawn ships!";
        boolean docked = false;
        //True makes it spawn with AI active.
        //Server spawn:
        Vector3i sectorPos = sector.pos;
        Server.executeAdminCommand("spawn_entity_pos "
                + catalogName + " " + spawnName + " " + sectorPos.x + " " + sectorPos.y + " " + sectorPos.z + " " +
                localX + " " + localY + " " + localZ + " " + factionId + " true");
        //Is there a better way to do this? probably
        for (SimpleTransformableSendableObject<?> entity : sector.getEntities()) {
            if(entity instanceof Ship && entity.getName().equals(spawnName)){
                return (Ship) entity;
            }
        }
        return null;
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

    private Sector getSector(int x, int y, int z) {
        try {
            return GameServer.getServerState().getUniverse().getSector(new Vector3i(x, y, z), true);
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
