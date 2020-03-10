//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.beam;

import javax.vecmath.Vector4f;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.elements.beam.harvest.SalvageUnit;
import org.schema.game.common.data.element.CustomOutputUnit;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.element.beam.BeamReloadCallback;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.schine.graphicsengine.core.Timer;

public abstract class BeamUnit<E extends BeamUnit<E, CM, EM>, CM extends BeamCollectionManager<E, CM, EM>, EM extends BeamElementManager<E, CM, EM>> extends CustomOutputUnit<E, CM, EM> implements BeamReloadCallback {
    public BeamUnit() {
    }

    public abstract float getBeamPowerWithoutEffect();

    public abstract float getBeamPower();

    public abstract float getBaseBeamPower();

    public abstract float getPowerConsumption();

    public boolean isLatchOn() {
        return false;
    }

    public abstract HitType getHitType();

    public float getReloadTimeMs() {
        return this.getCoolDownSec() * 1000.0F;
    }

    public final boolean isPowerCharging(long var1) {
        return super.isPowerCharging(var1) || var1 - ((BeamCollectionManager)this.elementCollectionManager).lastBeamFired < 300L;
    }

    public float getInitializationTime() {
        return this.getReloadTimeMs();
    }

    public float getMaxEffectiveRange() {
        return 1.0F;
    }

    public float getMinEffectiveRange() {
        return 0.0F;
    }

    public float getMaxEffectiveValue() {
        return 1.0F;
    }

    public float getMinEffectiveValue() {
        return 1.0F;
    }

    protected DamageDealerType getDamageType() {
        return DamageDealerType.BEAM;
    }

    public abstract float getDistanceRaw();

    public float getTickRate() {
        return ((BeamElementManager)((BeamCollectionManager)this.elementCollectionManager).getElementManager()).getTickRate();
    }

    public float getCoolDownSec() {
        return ((BeamElementManager)((BeamCollectionManager)this.elementCollectionManager).getElementManager()).getCoolDown();
    }

    public float getBurstTime() {
        return ((BeamElementManager)((BeamCollectionManager)this.elementCollectionManager).getElementManager()).getBurstTime();
    }

    public float getInitialTicks() {
        return ((BeamElementManager)((BeamCollectionManager)this.elementCollectionManager).getElementManager()).getInitialTicks();
    }

    public int getEffectBonus() {
        return Math.min(this.size(), (int)((double)this.size() / (double)((BeamCollectionManager)this.elementCollectionManager).getTotalSize() * (double)((BeamCollectionManager)this.elementCollectionManager).getEffectTotal()));
    }

    public float getExtraConsume() {
        return 1.0F;
    }

    public float getFiringPower() {
        return this.getBeamPower();
    }

    public Vector4f getColor() {
        return ((BeamCollectionManager)this.elementCollectionManager).getColor();
    }

    public void doShot(ControllerStateInterface var1, Timer var2, ShootContainer var3) {
        boolean var4 = var1.getPlayerState() != null && var1.getPlayerState().isMouseButtonDown(0) && this.getSegmentController().isClientOwnObject() && this instanceof SalvageUnit;
        boolean var5 = ((BeamCollectionManager)this.elementCollectionManager).isInFocusMode();
        //Set to true to re-enable AI leading shots
        var1.getShootingDir(this.getSegmentController(), var3, this.getDistanceFull(), 3000.0F, ((BeamCollectionManager)this.elementCollectionManager).getControllerPos(), var5, false);
        if (!this.isAimable()) {
            var3.shootingDirTemp.set(var3.shootingDirStraightTemp);
        }

        var3.shootingDirTemp.normalize();
        BeamElementManager var7 = (BeamElementManager)((BeamCollectionManager)this.elementCollectionManager).getElementManager();
        BeamCollectionManager var6 = (BeamCollectionManager)this.elementCollectionManager;
        var7.doShot(this, var6, var3, var1.getPlayerState(), var1.getBeamTimeout(), var2, var4);
    }

    public boolean isFriendlyFire() {
        return false;
    }

    public boolean isAimable() {
        return true;
    }

    public float getAcidDamagePercentage() {
        return 0.0F;
    }

    public boolean isPenetrating() {
        return false;
    }

    public boolean isCheckLatchConnection() {
        return false;
    }
}
