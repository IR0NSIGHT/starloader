package api.systems.addons.custom;

import api.entity.Entity;
import api.utils.StarRunnable;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementKeyMap;

import javax.vecmath.Vector3f;

//Example class
public class TacticalJumpAddOn extends CustomAddOn{
    public TacticalJumpAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return this.getConfigManager().apply(StatusEffectType.CUSTOM_EFFECT_02, 10F);
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 2;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return 100;
    }

    @Override
    public long getUsableId() {
        return PlayerUsableInterface.USABLE_ID_REPULSOR;
    }

    @Override
    public String getWeaponRowName() {
        return "Tactical Drive";
    }

    @Override
    public boolean isPlayerUsable() {
        float apply = this.getConfigManager().apply(StatusEffectType.CUSTOM_EFFECT_01, 1F);
        return apply == 100;
    }

    @Override
    public short getWeaponRowIcon() {
        return ElementKeyMap.CRYS_NOCX;
    }

    @Override
    public float getDuration() {
        return 1;
    }

    @Override
    public boolean onExecute() {
        final Entity entity = new Entity(this.segmentController);
        final Vector3f v = entity.getVelocity();
        Vector3f direction = entity.getDirection();
        direction.scale(10000);
        entity.setVelocity(direction);
        entity.playEffect((byte) 3);
        dischargeFully();
        for (int i = 0; i < 3; i++) {
            new StarRunnable() {
                @Override
                public void run() {
                    entity.playEffect((byte) 2);
                    entity.setVelocity(new Vector3f(2, 2, 2));
                }
            }.runLater(10 + i);
        }
        return true;
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onInactive() {

    }
}
