package api.universe;

import api.DebugFile;
import api.entity.Entity;
import api.entity.Ship;
import api.entity.Station;
import api.faction.Faction;
import api.main.GameServer;
import api.mod.StarLoader;
import api.server.Server;
import api.utils.StarRunnable;
import api.utils.callbacks.EntitySpawnCallback;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import java.io.IOException;
import java.util.ArrayList;

public class Sector {

    private org.schema.game.common.data.world.Sector internalSector;

    public Sector(org.schema.game.common.data.world.Sector internalSector) {
        this.internalSector = internalSector;
    }

    public ArrayList<Entity> getEntities() {
        /**
         * Gets all entities in the sector.
         */
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            if(internalEntity instanceof SegmentController) {
                entities.add(new Entity((SegmentController) internalEntity));
            }
        }
        return entities;
    }

    public ArrayList<Ship> getShips() {
        /**
         * Gets all ships in the sector.
         */
        ArrayList<Ship> ships = new ArrayList<Ship>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            if(internalEntity instanceof org.schema.game.common.controller.Ship) {
                ships.add(new Ship((SegmentController) internalEntity));
            }
        }
        return ships;
    }

    public ArrayList<Station> getStations() {
        /**
         * Gets all stations in the sector.
         */
        ArrayList<Station> stations = new ArrayList<Station>();
        for(SimpleTransformableSendableObject<?> internalEntity : internalSector.getEntities()) {
            if(internalEntity.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
                stations.add(new Station((SegmentController) internalEntity));
            }
        }
        return stations;
    }

    public System getSystem() {
        /**
         * Gets the system the sector is in.
         */
        if(GameServer.getServerState() != null){
            try {
                return new System(GameServer.getServerState().getUniverse().getStellarSystemFromSecPos(getCoordinates()));
            } catch (IOException e) {
                return null;
            }
        }else{
            return null;
        }
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
        spawnEntity(catalogName, name, faction, new Vector3i(0,0,0), null);
    }
    public void spawnEntity(String catalogName, final String name, Faction faction, Vector3i localPos) {
        /**
         * Spawns the specified entity in the sector at the specified position if it exists in the catalog. Only works for server mods.
         */
        spawnEntity(catalogName, name, faction, localPos, null);
    }

    public void spawnEntity(String catalogName, final String name, Faction faction, Vector3i localPos, final EntitySpawnCallback callback) {
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

        if(callback != null) {
            new StarRunnable() {
                @Override
                public void run() {
                    for (Entity ship : getEntities()){
                        if(ship.getName().equals(name)){
                            callback.onEntitySpawn(ship);
                            cancel();
                        }
                    }
                }
            }.runTimer(1);
        }
    }

    /**
     * Gets nearby sector based on cubic radius
     * @param radius
     * @return
     */
    public ArrayList<Sector> getNearbySectors(int radius){
        ArrayList<Sector> sectors = new ArrayList<Sector>();
        Vector3i coordinates = this.getCoordinates();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    org.schema.game.common.data.world.Sector sector = Universe.getUniverse().getSector(coordinates.x + x, coordinates.y + y, coordinates.z + z);
                    sectors.add(new Sector(sector));

                }
            }
        }
        return sectors;
    }
}