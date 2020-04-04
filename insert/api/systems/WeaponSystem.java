package api.systems;

import api.entity.Entity;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;

public class WeaponSystem {

    private ControlBlockElementCollectionManager internalWeaponSystem;

    public WeaponSystem(ControlBlockElementCollectionManager internalWeaponSystem) {
        this.internalWeaponSystem = internalWeaponSystem;
    }

    public WeaponSystem getSecondary() {
        /**
         * Returns the secondary computer group for the weapon system. Returns null if the system has no secondary computer group.
         */
        return new WeaponSystem(internalWeaponSystem.getSupportCollectionManager());
    }

    public WeaponSystem getEffect() {
        /**
         * Returns the effect weapon computer group for the module system. Returns null if the system has no effect.
         */
        return new WeaponSystem(internalWeaponSystem.getEffectCollectionManager());
    }

    public Entity getEntity() {
        /**
         * Gets the entity the module system is on.
         */
        return new Entity(internalWeaponSystem.getSegmentController());
    }

    public Vector3i getControllerPosition() {
        /**
         * Gets the location of the controlling computer.
         */
        return internalWeaponSystem.getControllerPos();
    }
}