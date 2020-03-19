package api.listener.events;

import api.listener.type.ServerEvent;
import api.main.GameClient;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;


@ServerEvent
public class ShipJumpEngageEvent extends Event {
    public static int id = 9;
    private final SegmentController controller;
    private final JumpAddOn addOn;
    private Vector3i originalSector;

    public ShipJumpEngageEvent(SegmentController controller, JumpAddOn addOn, Vector3i originalSector){

        this.controller = controller;
        this.addOn = addOn;
        this.originalSector = originalSector;
    }

    public SegmentController getController() {
        return controller;
    }

    public JumpAddOn getAddOn() {
        return addOn;
    }

    public Vector3i getOriginalSector() {
        return originalSector;
    }
}
