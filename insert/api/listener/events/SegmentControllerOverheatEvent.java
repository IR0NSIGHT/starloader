package api.listener.events;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;

public class SegmentControllerOverheatEvent extends Event {

    private SegmentController entity;
    private Damager lastDamager;

    public SegmentControllerOverheatEvent(SegmentController entity, Damager lastDamager) {
        this.entity = entity;
        this.lastDamager = lastDamager;
    }

    public SegmentController getEntity() {
        return entity;
    }

    public Damager getLastDamager() {
        return lastDamager;
    }
}
