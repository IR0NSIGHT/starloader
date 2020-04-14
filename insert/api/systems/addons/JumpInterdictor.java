package api.systems.addons;

import org.schema.game.common.controller.elements.jumpprohibiter.InterdictionAddOn;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;

public class JumpInterdictor {
    private InterdictionAddOn addOn;

    public JumpInterdictor(InterdictionAddOn addOn){

        this.addOn = addOn;
    }

    public InterdictionAddOn getAddOn() {
        return addOn;
    }

    public int getStrength(){
        return addOn.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_STRENGTH, 1);
    }
    public int getDistance(){
        return addOn.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_DISTANCE, 1);
    }

    public boolean isActive() {
        return addOn.isActive();
    }
}
