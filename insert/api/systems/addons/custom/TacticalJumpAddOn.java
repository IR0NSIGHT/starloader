package api.systems.addons.custom;

import api.element.block.Blocks;
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
        return 10F;
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
        return Blocks.NOCX_CRYSTAL.getPlayerUsableId();
    }

    @Override
    public String getWeaponRowName() {
        return "Tactical Drive";
    }

    @Override
    public boolean isPlayerUsable() {
        return entityHasEffect(StatusEffectType.CUSTOM_EFFECT_01);
    }

    @Override
    public short getWeaponRowIcon() {
        return Blocks.NOCX_CRYSTAL.getId();
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

    @Override
    public String getName() {
        return "Tactical Drive";
    }
}
