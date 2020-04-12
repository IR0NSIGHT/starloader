//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.jumpprohibiter;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.Collection;
import java.util.Iterator;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.RecharchableActivatableDurationSingleModule;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;
import org.schema.game.common.controller.elements.power.reactor.PowerImplementation;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.blockeffects.config.ConfigGroup;
import org.schema.game.common.data.blockeffects.config.ConfigProviderSource;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate.Type;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;

public class InterdictionAddOn extends RecharchableActivatableDurationSingleModule implements ConfigProviderSource {
    private final Collection<ConfigGroup> configCollection = new ObjectArrayList();
    private boolean wasActive;
    private long firstUpdate;

    public InterdictionAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    public double getPowerConsumedPerSecondResting() {
        double var1 =
                this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_POWER_CONSUMPTION, this.getBasePowerConsumedPerSecond());
        return this.isActive() ?
                this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_ACTIVE_RESTING_POWER_CONS, var1) :
                this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_INACTIVE_RESTING_POWER_CONS, var1);
    }

    public double getPowerConsumedPerSecondCharging() {
        return this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_POWER_CONSUMPTION, this.getBasePowerConsumedPerSecond());
    }

    private double getBasePowerConsumedPerSecond() {
        return (double)((float)PowerImplementation.getMinNeededFromReactorLevelRaw(this.getConfigManager()
                .apply(StatusEffectType.WARP_INTERDICTION_STRENGTH, 1))
                * VoidElementManager.REACTOR_RECHARGE_PERCENT_PER_SECOND * VoidElementManager.REACTOR_POWER_CAPACITY_MULTIPLIER);
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.OTHERS;
    }

    public ConfigEntityManager getConfigManager() {
        return this.getSegmentController().getConfigManager();
    }

    public boolean isPowerConsumerActive() {
        return this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION, false);
    }

    public long getUsableId() {
        return -9223372036854775777L;
    }

    public boolean isPlayerUsable() {
        return super.isPlayerUsable() && this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION, false);
    }

    public String getTagId() {
        return "INTR";
    }

    public boolean executeModule() {
        System.err.println("[JUMPINTERDICTION] EXECUTING " + this.getSegmentController() + " " + this.getState());
        super.executeModule();
        return true;
    }

    public int updatePrio() {
        return 1;
    }

    public void update(Timer var1) {
        super.update(var1);
        if (this.firstUpdate <= 0L) {
            this.firstUpdate = var1.currentTime;
        }

        if (this.isActive()) {
            if (this.isOnServer() && this.getPowered() < 1.0F && this.getSegmentController().isFullyLoaded() && var1.currentTime - this.firstUpdate > 5000L) {
                this.deactivateManually();
                this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{47}, 3);
            } else {
                this.getContainer().getPowerInterface().registerProjectionConfigurationSource(this);
            }
        } else {
            this.getContainer().getPowerInterface().unregisterProjectionConfigurationSource(this);
        }

        if (!this.wasActive && this.isActive()) {
            this.configCollection.clear();
            this.getCurrentInterdictionConfigGroup(this.configCollection);
        }

        this.wasActive = this.isActive();
    }

    public String getReloadStatus(long var1) {
        return this.isActive() ? StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPPROHIBITER_INTERDICTIONADDON_2, new Object[]{StringTools.formatPointZero(this.getPowerConsumedPerSecondResting())}) : super.getReloadStatus(var1);
    }

    public void sendChargeUpdate() {
        if (this.isOnServer()) {
            InterdictionAddOnChargeValueUpdate var1;
            (var1 = new InterdictionAddOnChargeValueUpdate()).setServer(((ManagedSegmentController)this.getSegmentController()).getManagerContainer(), this.getUsableId());

            assert var1.getType() == ValTypes.INTERDICTION_CHARGE;

            ((NTValueUpdateInterface)this.getSegmentController().getNetworkObject()).getValueUpdateBuffer().add(new RemoteValueUpdate(var1, this.getSegmentController().isOnServer()));
        }

    }

    public boolean isDischargedOnHit() {
        return false;
    }

    public void handle(ControllerStateInterface var1, Timer var2) {
        if (!this.isOnServer() && this.getPowered() < 1.0F) {
            ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPPROHIBITER_INTERDICTIONADDON_4);
        } else {
            super.handle(var1, var2);
        }
    }

    public void onChargedFullyNotAutocharged() {
        this.getSegmentController().popupOwnClientMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPPROHIBITER_INTERDICTIONADDON_1, new Object[]{this.getName()}), 1);
    }

    public float getChargeRateFull() {
        return this.getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_COOLDOWN, 1.0F);
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
        return this.getPowered() >= 1.0F;
    }

    public String getWeaponRowName() {
        return this.getName();
    }

    public short getWeaponRowIcon() {
        return 681;
    }

    public String getName() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPPROHIBITER_INTERDICTIONADDON_3;
    }

    protected Type getServerRequestType() {
        return Type.JUMP_INTERDICTION;
    }

    protected boolean isDeactivatableManually() {
        return true;
    }

    public float getDuration() {
        return -1.0F;
    }

    protected void onNoLongerConsumerActiveOrUsable(Timer var1) {
        this.deactivateManually();
        this.getContainer().getPowerInterface().unregisterProjectionConfigurationSource(this);
    }

    public ShortList getAppliedConfigGroups(ShortList var1) {
        Iterator var2 = this.configCollection.iterator();

        while(var2.hasNext()) {
            ConfigGroup var3 = (ConfigGroup)var2.next();
            var1.add(var3.ntId);
        }

        return var1;
    }

    private void getCurrentInterdictionConfigGroup(Collection<ConfigGroup> var1) {
        this.getContainer().getPowerInterface().getReactorSet()
                .getAllReactorElementsWithConfig(this.getConfigManager().getConfigPool(),
                        StatusEffectType.WARP_INTERDICTION_ACTIVE, var1);
        this.getContainer().getPowerInterface().getReactorSet()
                .getAllReactorElementsWithConfig(this.getConfigManager().getConfigPool(),
                        StatusEffectType.WARP_INTERDICTION_DISTANCE, var1);
        this.getContainer().getPowerInterface().getReactorSet()
                .getAllReactorElementsWithConfig(this.getConfigManager().getConfigPool(),
                        StatusEffectType.WARP_INTERDICTION_STRENGTH, var1);
        System.err.println("[INTERDICTION_ADD_ON] " + this.getSegmentController().getState() + " " + this.getSegmentController() + " INTERDICT " + var1);
    }

    public long getSourceId() {
        return this.getUsableId();
    }

    public String getExecuteVerb() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPPROHIBITER_INTERDICTIONADDON_5;
    }
}
