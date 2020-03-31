package api.entity;

import org.schema.game.common.controller.SegmentController;

public class Ship extends Entity {

    private org.schema.game.common.controller.Ship internalShip;

    public Ship(SegmentController controller) {
        super(controller);
    }

    public org.schema.game.common.controller.Ship getInternalShip() {
        return internalShip;
    }

    public Player getPlayerControl(){
        return new Player(internalShip.getAttachedPlayers().get(0));
    }
}
