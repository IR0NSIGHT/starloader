package api.systems;

import api.element.block.Block;
import api.entity.Entity;
import api.systems.weapons.Weapon;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;

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

    public Block getControllerBlock() {
        /**
         * Gets the controlling computer block.
         */
        return new Block(internalWeaponSystem.getControllerElement());
    }

    public Weapon getWeapon() {
        /**
         * Gets the system's controlling weapon.
         */
        WeaponCollectionManager weaponCollectionManager = new WeaponCollectionManager(internalWeaponSystem.getControllerElement(), internalWeaponSystem.getSegmentController(), (WeaponElementManager) internalWeaponSystem.getElementManager());
        return new Weapon(weaponCollectionManager.getInstance());
    }
}