package api.listener.events.systems;

import api.entity.Entity;
import api.listener.events.Event;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;

public class InterdictionCheckEvent extends Event {
    public static int id = idLog++;
    private final Entity entity;
    private final JumpAddOn addOn;
    private boolean isInterdicted = false;

    public InterdictionCheckEvent(JumpAddOn jumpAddOn, SegmentController segmentController, boolean retVal) {
        this.addOn = jumpAddOn;
        this.entity = new Entity(segmentController);
        this.isInterdicted = retVal;
        setCanceled(true);

    }

    public void setInterdicted(boolean b) {
        isInterdicted = b;
        setCanceled(false);
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isInterdicted() {
        return isInterdicted;
    }

    public JumpAddOn getAddOn() {
        return addOn;
    }
}
