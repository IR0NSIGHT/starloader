package api.listener.events;

import api.listener.type.ServerEvent;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.universe.Sector;
import api.universe.Universe;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;


@ServerEvent
public class ShipJumpEngageEvent extends Event {
    private final SegmentController controller;
    private Vector3i originalSector;
    private Vector3i newSector;

    public ShipJumpEngageEvent(SegmentController controller, Vector3i originalSector, Vector3i newSector){
        this.controller = controller;
        this.originalSector = originalSector;
        this.newSector = newSector;
    }

    public SegmentController getController() {
        return controller;
    }

    public Vector3i getOriginalSectorPos() {
        return originalSector;
    }
    public Sector getOriginalSector(){
        return Universe.getUniverse().getSector(originalSector);
    }
    public Sector getDestination(){
        return Universe.getUniverse().getSector(newSector);
    }
}
