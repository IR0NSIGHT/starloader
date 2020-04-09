package api.universe;

import api.DebugFile;
import api.entity.Entity;
import api.entity.Ship;
import api.entity.Station;
import api.faction.Faction;
import api.main.GameServer;
import api.server.Server;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import java.io.IOException;
import java.util.ArrayList;

public class Sector {

    private org.schema.game.common.data.world.Sector internalSector;

    public Sector(org.schema.game.common.data.world.Sector internalSector) throws IOException {
        this.internalSector = internalSector;
    }

    public ArrayList<Entity> getEntities() {
        /**
         * Gets all entities in the sector.
         */
        ArrayList<Entity> entities = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            entities.add(new Entity((SegmentController) internalEntity));
        }
        return entities;
    }

    public ArrayList<Ship> getShips() {
        /**
         * Gets all ships in the sector.
         */
        ArrayList<Ship> ships = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            if(internalEntity.getType().equals(SimpleTransformableSendableObject.EntityType.SHIP)) {
                ships.add(new Ship((SegmentController) internalEntity));
            }
        }
        return ships;
    }

    public ArrayList<Station> getStations() {
        /**
         * Gets all stations in the sector.
         */
        ArrayList<Station> stations = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            if(internalEntity.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
                stations.add(new Station((SegmentController) internalEntity));
            }
        }
        return stations;
    }

    public System getSystem() throws IOException {
        /**
         * Gets the system the sector is in.
         */
        return new System(GameServer.getServerState().getUniverse().getStellarSystemFromSecPos(getCoordinates()));
    }

    public Vector3i getCoordinates() {
        /**
         * Gets the coordinates of the sector.
         */
        return internalSector.pos;
    }

    public int getSectorId() {
        /**
         * Gets the sector's internal id.
         */
        return internalSector.getSectorId();
    }

    public org.schema.game.common.data.world.Sector getInternalSector() {
        /**
         * Gets the game's internal sector. Don't use this unless you know what you're doing!
         */
        return internalSector;
    }

    public void spawnEntity(String catalogName, String name, Faction faction) {
        /**
         * Spawns the specified entity in the sector if it exists in the catalog. Only works for server mods.
         */
        assert true : "Only server mods can spawn ships!";
        boolean docked = false;
        int x = getCoordinates().x;
        int y = getCoordinates().y;
        int z = getCoordinates().z;

        String command = "spawn_entity_pos "
                + catalogName + " \"" + name + "\" " + x + " " + y + " " + z + " " +
                0 + " " + 0 + " " + 0 + " " + faction.getID() + " true";
        DebugFile.info("[SERVER] Executing command: " + command);
        Server.executeAdminCommand(command);
        DebugFile.log("Executed");
    }

    public void spawnEntity(String catalogName, String name, Faction faction, Vector3i localPos) {
        /**
         * Spawns the specified entity in the sector at the specified position if it exists in the catalog. Only works for server mods.
         */
        assert true : "Only server mods can spawn ships!";
        boolean docked = false;
        int sectorX = getCoordinates().x;
        int sectorY = getCoordinates().y;
        int sectorZ = getCoordinates().z;

        String command = "spawn_entity_pos "
                + catalogName + " \"" + name + "\" " + sectorX + " " + sectorY + " " + sectorZ + " " +
                localPos.x + " " + localPos.y + " " + localPos.z + " " + faction.getID() + " true";
        DebugFile.info("[SERVER] Executing command: " + command);
        Server.executeAdminCommand(command);
        DebugFile.log("Executed");
    }
}