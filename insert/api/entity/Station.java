package api.entity;

import org.schema.game.common.controller.SegmentController;

public class Station extends Entity {

    public Station(SegmentController controller) {
        super(controller);
        this.setEntityType(EntityType.STATION);
    }

}
