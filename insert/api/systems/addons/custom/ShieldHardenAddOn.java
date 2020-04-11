package api.systems.addons.custom;

import api.element.block.Blocks;
import api.server.Server;
import api.systems.Shield;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.SingleModuleActivation;
import org.schema.schine.graphicsengine.core.Timer;

import javax.vecmath.Vector3f;

//Example class
public class ShieldHardenAddOn extends CustomAddOn{
    public ShieldHardenAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return 5F;//this.getConfigManager().apply(StatusEffectType.CUSTOM_EFFECT_02, 1F);
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
        return true;
        ///float apply = this.getConfigManager().apply(StatusEffectType.CUSTOM_EFFECT_01, 1F);
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
    public boolean onExecute() {
        //dischargeFully();
        return true;
    }

    @Override
    public void onActive() {
        entity.setVelocity(new Vector3f(10,10,10));
    }

    @Override
    public void onInactive() {

    }
}
