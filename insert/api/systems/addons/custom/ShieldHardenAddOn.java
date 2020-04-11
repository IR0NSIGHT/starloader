package api.systems.addons.custom;

import api.element.block.Blocks;
import api.server.Server;
import org.schema.common.util.StringTools;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.scanner.ScanAddOn;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.schine.common.language.Lng;

import javax.vecmath.Vector3f;

//Example class
public class ShieldHardenAddOn extends CustomAddOn{
    public ShieldHardenAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return 60;
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return entity.getMass();
    }

    @Override
    public long getUsableId() {
        return Blocks.FORCEFIELD_BLUE.getPlayerUsableId();
    }

    @Override
    public String getWeaponRowName() {
        return "Shield Harden";
    }

    @Override
    public boolean isPlayerUsable() {
        return true;//entityHasEffect(StatusEffectType.CUSTOM_EFFECT_05);
        //float apply = this.getConfigManager().apply(StatusEffectType.CUSTOM_EFFECT_01, 1F);
        //return apply == 100;
    }

    @Override
    public short getWeaponRowIcon() {
        return Blocks.FORCEFIELD_BLUE.getId();
    }

    @Override
    public float getDuration() {
        return 10F;
    }

    @Override
    public void onDeactivateFromTime() {

    }

    @Override
    public boolean onExecute() {
        //this.dischargeFully();
        this.dischargeToZero();
        return true;
    }

    @Override
    public void onActive() {
        this.getSegmentController().sendServerMessage("!! SHIELDS HARDENED !!", 1);
    }

    @Override
    public void onInactive() {

    }
}
