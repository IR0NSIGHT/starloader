package api.listener.events;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;

public class EntitySpawnEvent extends Event {
    public static int id = 7;
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
}
