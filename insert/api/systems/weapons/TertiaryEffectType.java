package api.systems.weapons;

import org.schema.game.common.controller.damage.effects.InterEffectHandler;

public enum TertiaryEffectType {
    HEAT(InterEffectHandler.InterEffectType.HEAT),
    KINETIC(InterEffectHandler.InterEffectType.KIN),
    ELECTROMAGNETIC(InterEffectHandler.InterEffectType.EM),
    NO_EFFECT(null);
    private InterEffectHandler.InterEffectType internalType;

    TertiaryEffectType(InterEffectHandler.InterEffectType internalType){

        this.internalType = internalType;
    }

    public InterEffectHandler.InterEffectType getInternalType() {
        return internalType;
    }
}
