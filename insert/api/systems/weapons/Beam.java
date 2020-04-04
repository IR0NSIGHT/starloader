package api.systems.weapons;

import api.systems.WeaponSystem;
import org.schema.game.common.controller.elements.BeamState;

public class Beam extends Weapon {
    private BeamState internalBeam;

    public Beam(BeamState internalBeam) {
        this.internalBeam = internalBeam;
       // hitController.getEffectContainer().get(HitReceiverType.SHIELD).getStrength(InterEffectHandler.InterEffectType.EM);
    }

    public BeamState getInternalBeam() {
        return internalBeam;
    }

    private WeaponSystem secondary;
    private TertiaryEffectType tertiaryEffectType;

    private float effectPercent = 0F;

    public WeaponSystem getSecondary() {
        return secondary;
    }

    public TertiaryEffectType getTertiaryEffectType() {
        return tertiaryEffectType;
    }

    public float getEffectPercent() {
        return effectPercent;
    }
}
