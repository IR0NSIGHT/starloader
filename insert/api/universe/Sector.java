package api.universe;
import api.entity.Entity;
import api.main.GameServer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

import java.util.ArrayList;

public class Sector {

    private System system;
    private Vector3i coordinates;
    private ArrayList<Entity> entites;
    private int sectorId;
    private org.schema.game.common.data.world.Sector internalSector;

    public Sector(org.schema.game.common.data.world.Sector internalSector) {
        this.internalSector = internalSector;
        this.coordinates = internalSector.pos;
        this.sectorId = internalSector.getSectorId();
        //this.system = ?
        //Todo: Figure out how to get the internal System from the internal sector and convert to API System class
    }

    public System getSystem() {
        return system;
    }

    public Vector3i getCoordinates() {
        return coordinates;
    }

    public int getSectorId() {
        return sectorId;
    }

    public ArrayList<Entity> getEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<SegmentController> internalEntities = new ArrayList<>();
        for(SimpleTransformableSendableObject<?> entity : internalSector.getEntities()) {
            internalEntities.add((SegmentController) entity);
        }

        for(SegmentController segmentController : internalEntities) {
            entities.add(new Entity(segmentController));
        }
        return entities;
    }

    public org.schema.game.common.data.world.Sector getInternalSector() {
        return internalSector;
    }
}