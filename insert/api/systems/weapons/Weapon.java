package api.systems.weapons;

import org.schema.game.common.controller.elements.weapon.WeaponUnit;

public class Weapon {

    private WeaponUnit internalWeapon;

    public Weapon(WeaponUnit internalWeapon) {
        this.internalWeapon = internalWeapon;
    }

    public float getBaseDamage() {
        return internalWeapon.getBaseDamage();
    }

    public float getDamageWithoutEffects() {
        return internalWeapon.getDamageWithoutEffect();
    }

    public float getDamage() {
        return internalWeapon.getDamage();
    }
}
