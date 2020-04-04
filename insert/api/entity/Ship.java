package api.entity;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;

public class Ship extends Entity {

    private org.schema.game.common.controller.Ship internalShip;

    public Ship(SegmentController controller) {
        super(controller);
        this.internalShip = (org.schema.game.common.controller.Ship) internalEntity;
    }

    private org.schema.game.common.controller.Ship getInternalShip() {
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

    public boolean isDocked() {
        /**
         * Checks if the ship is currently docked.
         */
        return internalShip.isDocked();
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

    public Vector3i getDockLocation() {
        /**
         * Gets the Vector3i location of the currently used docking module. Returns null if the Ship isn't docked to anything.
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
}
