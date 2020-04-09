package api.entity;

import api.element.block.Block;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.ai.AIGameConfiguration;
import org.schema.game.common.controller.ai.AIShipConfiguration;
import org.schema.game.server.ai.AIShipControllerStateUnit;
import org.schema.game.server.ai.ShipAIEntity;

public class AIController {

    private AIGameConfiguration<ShipAIEntity, org.schema.game.common.controller.Ship> internalShipAI;
    private Ship ship;

    public AIController(Ship ship) {
        internalShipAI = ((org.schema.game.common.controller.Ship) ship.internalEntity).getAiConfiguration();
        this.ship = ship;
    }

    public Ship getShip() {
        /**
         * Gets the ship the AI is part of.
         */
        return ship;
    }

    public Block getAIModule() {
        /**
         * Gets the AI module block.
         */
        return new Block(internalShipAI.getControllerBlock());
    }

    public boolean isActive() {
        /**
         * Checks if the AI module is activated.
         */
        return internalShipAI.getAiEntityState().isActive();
    }

    public Entity getTargetedEntity() {
        /**
         * Gets the entity currently targeted by the AI. Returns null if the entity isn't a ship or turret.
         */
        AIShipControllerStateUnit aiShipController = new AIShipControllerStateUnit(internalShipAI.getAiEntityState());
        return new Entity((SegmentController) aiShipController.getAquiredTarget());
    }

}