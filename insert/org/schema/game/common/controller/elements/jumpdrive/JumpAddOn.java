//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.jumpdrive;

import api.listener.events.systems.InterdictionCheckEvent;
import api.mod.StarLoader;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.RecharchableSingleModule;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.world.Sector;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate.Type;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;

public class JumpAddOn extends RecharchableSingleModule {
    private long lastSentJmpMsg;
    private static final int DEFAULT_MAX_CHARGES = 1;

    public int getMaxCharges() {
        return this.getConfigManager().apply(StatusEffectType.JUMP_MULTI_CHARGE_COUNT, 1);
    }

    public JumpAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    public int getDistance() {
        return (int)this.getConfigManager().apply(StatusEffectType.JUMP_DISTANCE, VoidElementManager.REACTOR_JUMP_DISTANCE_DEFAULT);
    }
    public void sendChargeUpdate() {
        if (this.isOnServer()) {
            JumpAddOnChargeValueUpdate var1;
            (var1 = new JumpAddOnChargeValueUpdate()).setServer(((ManagedSegmentController)this.getSegmentController()).getManagerContainer(), this.getUsableId());

            assert var1.getType() == ValTypes.JUMP_CHARGE_REACTOR;

            ((NTValueUpdateInterface)this.getSegmentController().getNetworkObject()).getValueUpdateBuffer().add(new RemoteValueUpdate(var1, this.getSegmentController().isOnServer()));
        }

    }

    public boolean isDischargedOnHit() {
        return true;
    }

    public boolean executeModule() {
        if (this.getSegmentController().isOnServer()) {
            if (this.getCharges() > 0) {
                if (!this.isInterdicted()) {
                    if (this.getSegmentController().engageJump(this.getDistance())) {

                        this.removeCharge();
                        this.setCharge(0.0F);
                        this.sendChargeUpdate();
                        return true;
                    }
                } else if (this.getState().getUpdateTime() - this.lastSentJmpMsg > 3000L) {
                    this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{41}, 3);
                    this.lastSentJmpMsg = this.getState().getUpdateTime();
                }
            } else if (this.getState().getUpdateTime() - this.lastSentJmpMsg > 3000L) {
                this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{42, StringTools.formatPointZero(this.getCharge() * 100.0F)}, 3);
                this.lastSentJmpMsg = this.getState().getUpdateTime();
            }
        }

        return false;
    }

    //REPLACE METHOD
    private boolean isInterdicted() {
        assert this.isOnServer();
        GameServerState var1;
        Sector var2;
        boolean retVal = false;
        if ((var2 = (var1 = (GameServerState)this.getState()).getUniverse().getSector(this.getSegmentController().getSectorId())) == null) {
            System.err.println("[SERVER][JUMP] " + this.getSegmentController() + " IS NOT IN A SECTOR " + this.getSegmentController().getSectorId());
        } else {
            Vector3i var3 = new Vector3i();

            for(int x = -1; x <= 1; ++x) {
                for(int y = -1; y <= 1; ++y) {
                    for(int z = -1; z <= 1; ++z) {
                        var3.set(var2.pos.x + z, var2.pos.y + y, var2.pos.z + x);
                        Sector var7;
                        if ((var7 = var1.getUniverse().getSectorWithoutLoading(var3)) != null && var7.isInterdicting(this.getSegmentController(), var2)) {
                            this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{43, var7.pos.toStringPure()}, 3);
                            retVal = true;
                            break;
                        }
                    }
                }
            }
        }
        InterdictionCheckEvent event = new InterdictionCheckEvent(this, this.segmentController, retVal);
        StarLoader.fireEvent(InterdictionCheckEvent.class, event, this.isOnServer());

        if(!event.useDefault){
            return event.isInterdicted();
        }else{
            return retVal;
        }
    }
    //

    public void onChargedFullyNotAutocharged() {
        this.getSegmentController().popupOwnClientMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_1, 1);
    }

    public float getChargeRateFull() {
        float var1 = VoidElementManager.REACTOR_JUMP_CHARGE_NEEDED_IN_SEC + VoidElementManager.REACTOR_JUMP_CHARGE_NEEDED_IN_SEC_EXTRA_PER_MASS * this.getMassWithDocks() + Math.max(0.0F, ((float)Math.log10((double)this.getMassWithDocks()) + VoidElementManager.REACTOR_JUMP_CHARGE_NEEDED_IN_SEC_LOG_OFFSET) * VoidElementManager.REACTOR_JUMP_CHARGE_NEEDED_IN_SEC_LOG_FACTOR) * VoidElementManager.REACTOR_JUMP_CHARGE_NEEDED_IN_SEC;
        return this.getConfigManager().apply(StatusEffectType.JUMP_CHARGE_TIME, var1);
    }

    public boolean canExecute() {
        if (!this.getSegmentController().getDockingController().isDocked() && !this.getSegmentController().railController.isDockedOrDirty()) {
            if (this.getSegmentController().getPhysicsDataContainer().getObject() != null) {
                return true;
            } else {
                this.getSegmentController().sendControllingPlayersServerMessage(new Object[]{44}, 0);
                return false;
            }
        } else {
            this.getSegmentController().popupOwnClientMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_2, 3);
            return false;
        }
    }

    public double getPowerConsumedPerSecondResting() {
        return (double)this.getConfigManager().apply(StatusEffectType.JUMP_POWER_TOPOFF_RATE, VoidElementManager.REACTOR_JUMP_POWER_CONSUMPTION_RESTING_PER_MASS) * (double)this.getMassWithDocks();
    }

    public double getPowerConsumedPerSecondCharging() {
        return (double)this.getConfigManager().apply(StatusEffectType.JUMP_POWER_CHARGE_RATE, VoidElementManager.REACTOR_JUMP_POWER_CONSUMPTION_CHARGING_PER_MASS) * (double)this.getMassWithDocks();
    }

    public boolean isAutoCharging() {
        return this.getConfigManager().apply(StatusEffectType.JUMP_AUTO_CHARGE, false);
    }

    public boolean isAutoChargeToggable() {
        return this.getConfigManager().apply(StatusEffectType.JUMP_AUTO_CHARGE, false);
    }

    public long getUsableId() {
        return -9223372036854775802L;
    }

    public void chargingMessage() {
        this.getSegmentController().popupOwnClientMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_4, 1);
    }

    public void onCooldown(long var1) {
        this.getSegmentController().popupOwnClientMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_5, new Object[]{var1}), 3);
    }

    public void onUnpowered() {
        this.getSegmentController().popupOwnClientMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_6, 3);
    }

    public String getTagId() {
        return "JAO";
    }

    public int updatePrio() {
        return 1;
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.JUMP_DRIVE;
    }

    public String getWeaponRowName() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_7;
    }

    public short getWeaponRowIcon() {
        return 544;
    }

    public boolean isPlayerUsable() {
        return !((GameStateInterface)this.getSegmentController().getState()).getGameState().isModuleEnabledByDefault(this.getUsableId()) && !this.getConfigManager().apply(StatusEffectType.JUMP_DRIVE_ENABLE, false) ? false : super.isPlayerUsable();
    }

    public boolean isPowerConsumerActive() {
        return true;
    }

    public String getName() {
        return "JumpAddOn";
    }

    protected Type getServerRequestType() {
        return Type.JUMP;
    }

    protected void onNoLongerConsumerActiveOrUsable(Timer var1) {
    }

    public String getExecuteVerb() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_JUMPDRIVE_JUMPADDON_9;
    }
}
