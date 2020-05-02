package api.listener.events;

import api.entity.Entity;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;

public class EntitySpawnEvent extends Event {
    private final Vector3i sector;
    SegmentController controller;

    public EntitySpawnEvent(Vector3i sector, SegmentController entity) {
        this.sector = sector;
        controller = entity;
    }

    public Vector3i getSector() {
        return sector;
    }

    public SegmentController getController() {
        return controller;
    }

    public Entity getEntity(){
        return new Entity(controller);
    }
}
