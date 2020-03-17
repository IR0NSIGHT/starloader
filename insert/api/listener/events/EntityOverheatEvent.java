package api.listener.events;

import org.schema.game.common.controller.SegmentController;

public class EntityOverheatEvent extends Event{
    public static int id = 8;
    SegmentController entity;
    int lastDamager;
    public EntityOverheatEvent(SegmentController entity, int lastDamager) {
        this.entity = entity;
        this.lastDamager = lastDamager;
    }

    public SegmentController getEntity() {
        return entity;
    }

    public int getLastDamager() {
        return lastDamager;
    }
}
