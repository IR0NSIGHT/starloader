package api.systems;

import api.systems.modules.EffectModule;
import api.systems.weapons.Weapon;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;

public class WeaponSystem {

    private WeaponElementManager internalWeaponSystem;

    public WeaponSystem(WeaponElementManager internalWeaponSystem) {
        this.internalWeaponSystem = internalWeaponSystem;
    }

    public Weapon getPrimary() {
        return null;
    }

    public Weapon getSecondary() {
        return null;
    }

    public EffectModule getEffect() {
        return null;
    }
}