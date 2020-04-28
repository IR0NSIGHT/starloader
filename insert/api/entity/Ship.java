package api.entity;

import api.element.block.Blocks;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.gui.advancedstats.AdvancedStructureStatsGeneral;
import org.schema.game.client.view.gui.advancedstats.AdvancedStructureStatsThruster;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.ai.AIConfiguationElements;
import org.schema.game.common.controller.ai.Types;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.data.explosion.ExplosionCollisionSegmentCallback;
import org.schema.schine.common.language.Lng;

public class Ship extends Entity {

    private org.schema.game.common.controller.Ship internalShip;

    public Ship(SegmentController controller) {
        super(controller);
        internalShip = (org.schema.game.common.controller.Ship) internalEntity;
    }

    public org.schema.game.common.controller.Ship getInternalShip() {
        /**
         * Gets the game's internal version of the Ship. Don't use this unless you know what you're doing!
         */
        return internalShip;
    }

    public Player getPilot() {
        /**
         * Gets the player currently piloting the ship. Returns null if there is no pilot.
         */
        if(internalShip.isConrolledByActivePlayer()) {
            return new Player(internalShip.getAttachedPlayers().get(0));
        }
        return null;
    }


    public Entity getDockedLocal() {
        /**
         * Gets the entity the ship is currently docked to.
         */
        return new Entity(internalShip.getDockingController().getLocalMother());
    }

    public Entity getDockedRoot() {
        /**
         * Gets the root entity the ship is currently docked to.
         */
        return new Entity(internalShip.getDockingController().getRoot());
    }

    public Vector3i getDockerLocation() {
        /**
         * Gets the Vector3i location of the currently used rail docker. Returns null if the Ship isn't docked to anything.
         */
        if(isDocked()) {
            float x = internalShip.getDockingController().getDockingPos().origin.x;
            float y = internalShip.getDockingController().getDockingPos().origin.y;
            float z = internalShip.getDockingController().getDockingPos().origin.z;
            return new Vector3i(x, y, z);
        }
        return null;
    }

    public float getRootMissileCapacity() {
        /**
         * Gets the current missile capacity of the root entity the ship is docked to. Returns 0 if the ship isn't docked to anything.
         */
        if(isDocked()) {
            return internalShip.getRootMissileCapacity();
        }
        return 0;
    }

    public float getRootMissileCapacityMax() {
        /**
         * Gets the maximum missile capacity of the root entity the ship is docked to. Returns 0 if the ship isn't docked to anything.
         */
        if(isDocked()) {
            return internalShip.getRootMissileCapacityMax();
        }
        return 0;
    }

    public boolean isInFleet() {
        /**
         * Checks to see if the ship is currently in a fleet.
         */
        return internalShip.isInFleet();
    }

    public Fleet getFleet() {
        /**
         * Gets the ship's current fleet. Returns null if the ship is not in a fleet.
         */
        if(isInFleet()) {
            return new Fleet(internalEntity.getFleet());
        }
        return null;
    }

    public AIController getAI() {
        /**
         * Gets the ship's AI. Returns null if the entity has no ai module.
         */
        if(this.getBlockAmount(Blocks.BOBBY_AI_MODULE) > 0){
            return new AIController(this);
        }

        return null;
    }
    public ShipManagerContainer getShipManagerContainer(){
        return (ShipManagerContainer) getManagerContainer();
    }

    public float getThrustMassRatio(){
        return getShipManagerContainer().getThrusterElementManager().getThrustMassRatio();
    }
    public float getThrust(){
        return getShipManagerContainer().getThrusterElementManager().getActualThrust();
    }

    public boolean isAiControlled(){
        return internalShip.isAIControlled();
    }
}
