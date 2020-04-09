package api.systems.weapons;

import api.entity.missiles.LockonMissileEntity;
import api.entity.missiles.MissileEntity;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;
import org.schema.game.common.data.missile.FafoMissile;

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
