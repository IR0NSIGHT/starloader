package api.systems.weapons;

import org.schema.game.common.controller.elements.FireingUnit;

public class Weapon {

    private FireingUnit internalWeapon;

    public Weapon(FireingUnit internalWeapon) {
        this.internalWeapon = internalWeapon;
    }
}
