package api.systems.weapons;

import org.schema.game.common.controller.elements.BeamState;

public class DamageBeamShot extends Weapon {
    private BeamState internalBeam;

    public DamageBeamShot(BeamState internalBeam) {
        this.internalBeam = internalBeam;
       // hitController.getEffectContainer().get(HitReceiverType.SHIELD).getStrength(InterEffectHandler.InterEffectType.EM);
    }

    public BeamState getInternalBeam() {
        return internalBeam;
    }

    private Weapon secondary;
    private TertiaryEffectType tertiaryEffectType;

    private float effectPercent = 0F;

    public Weapon getSecondary() {
        return secondary;
    }

    public TertiaryEffectType getTertiaryEffectType() {
        return tertiaryEffectType;
    }

    public float getEffectPercent() {
        return effectPercent;
    }
}
