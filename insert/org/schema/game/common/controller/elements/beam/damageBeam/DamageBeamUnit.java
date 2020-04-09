//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.beam.damageBeam;

import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.beam.BeamUnit;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.combination.modifier.BeamUnitModifier;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.schine.graphicsengine.core.Timer;

public class DamageBeamUnit extends BeamUnit<DamageBeamUnit, DamageBeamCollectionManager, DamageBeamElementManager> {
    public DamageBeamUnit() {
    }

    public ControllerManagerGUI createUnitGUI(GameClientState var1, ControlBlockElementCollectionManager<?, ?, ?> var2, ControlBlockElementCollectionManager<?, ?, ?> var3) {
        return ((DamageBeamElementManager)((DamageBeamCollectionManager)this.elementCollectionManager).getElementManager()).getGUIUnitValues(this, (DamageBeamCollectionManager)this.elementCollectionManager, var2, var3);
    }

    @Override
    public void doShot(ControllerStateInterface var1, Timer var2, ShootContainer var3) {
        super.doShot(var1, var2, var3);
    }

    private float getBaseConsume() {
        return (float)(this.size() + this.getEffectBonus()) * DamageBeamElementManager.POWER_CONSUMPTION;
    }

    public float getMaxEffectiveRange() {
        return DamageBeamElementManager.MAX_EFFECTIVE_RANGE;
    }

    public float getMinEffectiveRange() {
        return DamageBeamElementManager.MIN_EFFECTIVE_RANGE;
    }

    public float getMaxEffectiveValue() {
        return DamageBeamElementManager.MAX_EFFECTIVE_VALUE;
    }

    public float getMinEffectiveValue() {
        return DamageBeamElementManager.MIN_EFFECTIVE_VALUE;
    }

    public void flagBeamFiredWithoutTimeout() {
        ((DamageBeamCollectionManager)this.elementCollectionManager).flagBeamFiredWithoutTimeout(this);
    }

    public float getBeamPowerWithoutEffect() {
        return (float)this.size() * this.getBaseBeamPower();
    }

    public float getBeamPower() {
        return (float)(this.size() + this.getEffectBonus()) * this.getBaseBeamPower();
    }

    public float getBaseBeamPower() {
        return this.getSegmentController().getConfigManager().apply(StatusEffectType.WEAPON_DAMAGE, this.getDamageType(), DamageBeamElementManager.DAMAGE_PER_HIT.get(this.getSegmentController().isUsingPowerReactors()));
    }

    public float getPowerConsumption() {
        return this.getBaseConsume() * this.getExtraConsume();
    }

    public float getDistanceRaw() {
        return DamageBeamElementManager.DISTANCE * ((GameStateInterface)this.getSegmentController().getState()).getGameState().getWeaponRangeReference();
    }

    public float getDistanceFull() {
        ControlBlockElementCollectionManager var1 = CombinationAddOn.getEffect(((DamageBeamCollectionManager)this.elementCollectionManager).getEffectConnectedElement(), (ManagerModuleCollection)null, this.getSegmentController());
        ControlBlockElementCollectionManager var2;
        if ((var2 = (ControlBlockElementCollectionManager)((DamageBeamElementManager)((DamageBeamCollectionManager)this.elementCollectionManager).getElementManager()).getCollectionManagersMap().get(ElementCollection.getPosIndexFrom4(((DamageBeamCollectionManager)this.elementCollectionManager).getSlaveConnectedElement()))) != null) {
            int var3 = ElementCollection.getType(((DamageBeamCollectionManager)this.elementCollectionManager).getSlaveConnectedElement());

            assert !ElementKeyMap.getInfo((short)var3).isEffectCombinationController() : ElementKeyMap.toString((short)var3);

            return ((BeamUnitModifier)((DamageBeamElementManager)((DamageBeamCollectionManager)this.elementCollectionManager).getElementManager()).getAddOn().getGUI(this.elementCollectionManager, this, var2, var1)).outputDistance;
        } else {
            return this.getConfigManager().apply(StatusEffectType.WEAPON_RANGE, this.getDamageType(), this.getDistanceRaw());
        }
    }

    public float getTickRate() {
        return DamageBeamElementManager.TICK_RATE;
    }

    public float getExtraConsume() {
        return 1.0F + (float)Math.max(0, ((DamageBeamCollectionManager)this.elementCollectionManager).getElementCollections().size() - 1) * DamageBeamElementManager.ADDITIONAL_POWER_CONSUMPTION_PER_UNIT_MULT;
    }

    public float getBasePowerConsumption() {
        return DamageBeamElementManager.POWER_CONSUMPTION;
    }

    public float getPowerConsumptionWithoutEffect() {
        return (float)this.size() * DamageBeamElementManager.POWER_CONSUMPTION;
    }

    public double getPowerConsumedPerSecondResting() {
        return ((DamageBeamElementManager)((DamageBeamCollectionManager)this.elementCollectionManager).getElementManager()).calculatePowerConsumptionCombi(this.getPowerConsumedPerSecondRestingPerBlock(), false, this);
    }

    public double getPowerConsumedPerSecondCharging() {
        return ((DamageBeamElementManager)((DamageBeamCollectionManager)this.elementCollectionManager).getElementManager()).calculatePowerConsumptionCombi(this.getPowerConsumedPerSecondChargingPerBlock(), true, this);
    }

    public double getPowerConsumedPerSecondRestingPerBlock() {
        double var1 = (double)DamageBeamElementManager.REACTOR_POWER_CONSUMPTION_RESTING;
        return this.getConfigManager().apply(StatusEffectType.WEAPON_TOP_OFF_RATE, this.getDamageType(), var1);
    }

    public double getPowerConsumedPerSecondChargingPerBlock() {
        double var1 = (double)DamageBeamElementManager.REACTOR_POWER_CONSUMPTION_CHARGING;
        return this.getConfigManager().apply(StatusEffectType.WEAPON_CHARGE_RATE, this.getDamageType(), var1);
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.BEAMS;
    }

    public boolean isLatchOn() {
        return DamageBeamElementManager.LATCH_ON != 0;
    }

    public boolean isCheckLatchConnection() {
        return DamageBeamElementManager.CHECK_LATCH_CONNECTION != 0;
    }

    public HitType getHitType() {
        return HitType.WEAPON;
    }

    public boolean isFriendlyFire() {
        return DamageBeamElementManager.FRIENDLY_FIRE != 0;
    }

    public boolean isAimable() {
        return DamageBeamElementManager.AIMABLE != 0;
    }

    public float getAcidDamagePercentage() {
        return DamageBeamElementManager.ACID_DAMAGE_PERCENTAGE;
    }

    public boolean isPenetrating() {
        return DamageBeamElementManager.PENETRATION != 0;
    }

    public float getDamage() {
        return this.getBeamPower();
    }
}
