package api.listener.events;

import org.schema.game.common.controller.SegmentController;

public class SegmentControllerOverheatEvent extends Event {
    SegmentController entity;
    int lastDamager;
    public SegmentControllerOverheatEvent(SegmentController entity, int lastDamager) {
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
