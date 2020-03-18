//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.effectblock;

import javax.vecmath.Vector3f;
import org.schema.common.util.StringTools;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.RecharchableActivatableDurationSingleModule;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.BlockEffect;
import org.schema.game.common.data.blockeffects.BlockEffectTypes;
import org.schema.game.common.data.blockeffects.TakeOffEffect;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate.Type;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;

public class EffectAddOn extends RecharchableActivatableDurationSingleModule {
    private final BlockEffectTypes type;

    public EffectAddOn(ManagerContainer<?> var1, BlockEffectTypes var2) {
        super(var1);
        this.type = var2;

        assert var2 != null;

    }

    public double getPowerConsumedPerSecondResting() {
        return 0.0D;
    }

    public double getPowerConsumedPerSecondCharging() {
        switch(this.getPowerConsumptionEffect(this.type)) {
            case THRUSTER_BLAST_POWER_CONSUMPTION_CHARGING:
                if (this.man instanceof ShipManagerContainer) {
                    return this.getConfigManager().apply(this.getPowerConsumptionEffect(this.type), ((ShipManagerContainer)this.man).getThrusterElementManager().getPowerConsumedPerSecondCharging());
                }
            default:
                return this.getConfigManager().apply(this.getPowerConsumptionEffect(this.type), 1.0D);
        }
    }

    public int getMaxCharges() {
        return this.getMaxCharges(this.type);
    }

    public StatusEffectType getPowerConsumptionEffect(BlockEffectTypes var1) {
        switch(var1) {
            case TAKE_OFF:
                return StatusEffectType.THRUSTER_BLAST_POWER_CONSUMPTION_CHARGING;
            default:
                return StatusEffectType.THRUSTER_BLAST_POWER_CONSUMPTION_CHARGING;
        }
    }

    private int getMaxCharges(BlockEffectTypes var1) {
        switch(var1) {
            case TAKE_OFF:
                return this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_BLAST_MULTI_CHARGE_COUNT, 1);
            default:
                return 1;
        }
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.OTHERS;
    }

    public ConfigEntityManager getConfigManager() {
        return this.getSegmentController().getConfigManager();
    }

    public boolean isPowerConsumerActive() {
        return this.getConfigManager().apply(this.type.getAssociatedStatusEffectType(), false);
    }

    public long getUsableId() {
        return this.type.getUsableId();
    }

    public String getTagId() {
        return "EF" + this.getUsableId();
    }

    public int updatePrio() {
        return 1;
    }

    public void sendChargeUpdate() {
        if (this.isOnServer()) {
            EffectAddOnChargeValueUpdate var1;
            (var1 = new EffectAddOnChargeValueUpdate()).setServer(((ManagedSegmentController)this.getSegmentController()).getManagerContainer(), this.getUsableId());

            assert var1.getType() == ValTypes.EFFECT_ADD_ON_CHARGE;

            ((NTValueUpdateInterface)this.getSegmentController().getNetworkObject()).getValueUpdateBuffer().add(new RemoteValueUpdate(var1, this.getSegmentController().isOnServer()));
        }

    }

    public boolean isDischargedOnHit() {
        return false;
    }

    public void update(Timer var1) {
        super.update(var1);
        if (this.isActive()) {
            this.startEffect();
            if (this.isOnServer() && this.type.oneTimeUse) {
                this.deactivateManually();
                return;
            }
        } else if (!this.type.oneTimeUse) {
            this.endEffect();
        }

    }

    public void endEffect() {
        SendableSegmentController var1;
        if ((var1 = (SendableSegmentController)this.segmentController).getBlockEffectManager().hasEffect(this.type)) {
            var1.getBlockEffectManager().getEffect(this.type).end();
        }

    }

    public void startEffect() {
        SendableSegmentController var1;
        if (!(var1 = (SendableSegmentController)this.segmentController).getBlockEffectManager().hasEffect(this.type)) {
            BlockEffect var2 = this.type.effectFactory.getInstance().getInstanceFromNT(var1);
            this.configureEffect(var2);
            var1.getBlockEffectManager().addEffect(var2);
        }
        /*
        tac drive code:
        SendableSegmentController var1;
        if (!(var1 = (SendableSegmentController)this.segmentController).getBlockEffectManager().hasEffect(this.type)) {
            BlockEffect var2 = this.type.effectFactory.getInstance().getInstanceFromNT(var1);
            this.configureEffect(var2);
            TakeOffEffect effect = (TakeOffEffect) var2;
            final Entity entity = new Entity(this.segmentController);
            final Vector3f v = entity.getVelocity();
            Vector3f direction = entity.getDirection();
            direction.scale(10000);
            entity.setVelocity(direction);
            entity.playEffect((byte) 3);
            for (int i = 0; i < 3; i++) {
                new StarRunnable(){
                    @Override
                    public void run() {
                        entity.playEffect((byte) 2);
                        entity.setVelocity(new Vector3f(2,2,2));
                    }
                }.runLater(10+i);
            }
        }
         */
    }

    private void configureEffect(BlockEffect var1) {
        switch(this.type) {
            case TAKE_OFF:
                TakeOffEffect var5 = (TakeOffEffect)var1;
                float var2 = this.getConfigManager().apply(StatusEffectType.THRUSTER_BLAST_STRENGTH, 1.0F);
                Vector3f var3 = ((ShipManagerContainer)this.man).getThrusterElementManager().getInputVectorNormalize(new Vector3f());
                float var4 = ((ShipManagerContainer)this.man).getThrusterElementManager().getThrustMassRatio();
                if (var3.lengthSquared() == 0.0F) {
                    GlUtil.getForwardVector(var3, this.segmentController.getWorldTransform());
                }

                var5.getDirection().set(var3);
                System.err.println(this.getState() + " BLAST EFFECT CONF: thrustMassRatio " + var4 + "; Mass " + this.segmentController.getMass() + "; strength: " + var2 + " -> " + var4 * this.segmentController.getMass() * var2);
                if ((double)var4 <= 0.001D && this.isOnServer()) {
                    this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{38}, 3);
                }

                var5.setForce(var4 * this.segmentController.getMass() * var2);
                return;
            default:
                throw new RuntimeException("Effect not configured: " + var1.getType().name());
        }
    }

    public void onChargedFullyNotAutocharged() {
        this.getSegmentController().popupOwnClientMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_EFFECTBLOCK_EFFECTADDON_0, new Object[]{this.type.getName()}), 1);
    }

    public float getChargeRateFull() {
        switch(this.type) {
            case TAKE_OFF:
                return this.getConfigManager().apply(StatusEffectType.THRUSTER_BLAST_COOLDOWN, 1.0F);
            default:
                throw new RuntimeException("no mapped consumption: " + this.type.getName());
        }
    }

    public boolean isAutoCharging() {
        return false;
    }

    public boolean isAutoChargeToggable() {
        return false;
    }

    public void chargingMessage() {
    }

    public void onUnpowered() {
    }

    public void onCooldown(long var1) {
    }

    public boolean canExecute() {
        return true;
    }

    public String getWeaponRowName() {
        return this.type.getName();
    }

    public short getWeaponRowIcon() {
        switch(this.type) {
            case TAKE_OFF:
                return 8;
            default:
                return 476;
        }
    }

    public String getName() {
        return "EffectAddOn";
    }

    protected Type getServerRequestType() {
        return Type.EFFECT;
    }

    protected void onNoLongerConsumerActiveOrUsable(Timer var1) {
        this.endEffect();
    }

    protected boolean isDeactivatableManually() {
        return true;
    }

    public float getDuration() {
        return -1.0F;
    }

    public String getExecuteVerb() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_EFFECTBLOCK_EFFECTADDON_2;
    }
}
