//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.listener.events.EntityOverheatEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.HpTrigger.HpTriggerType;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.power.PowerAddOn;
import org.schema.game.common.controller.elements.power.PowerManagerInterface;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.blockeffects.BlockEffectTypes;
import org.schema.game.common.data.blockeffects.ControllessEffect;
import org.schema.game.common.data.blockeffects.PowerRegenDownEffect;
import org.schema.game.common.data.blockeffects.ShieldRegenDownEffect;
import org.schema.game.common.data.blockeffects.ThrusterOutageEffect;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.player.SimplePlayerCommands;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.network.objects.NetworkSegmentController;
import org.schema.game.network.objects.remote.RemoteSegmentPiece;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class SegmentControllerHpController implements SegmentControllerHpControllerInterface {
    private static final byte HP_CONTROLLER_CLASS_ID = 1;
    private final SendableSegmentController segmentController;
    private final boolean[] used = new boolean[HpTriggerType.values().length];
    boolean useHPLong = false;
    private ElementCountMap currentHPMatch = new ElementCountMap();
    private int hpInt;
    private int maxHpInt;
    private long hpLong;
    private long maxHpLong;
    private long rebootStarted;
    private boolean maxHPDirty;
    private boolean hpDirty;
    private long rebootTime;
    private boolean requestedTimeClient;
    private long lastHp;
    private boolean loadedFromTag;
    private boolean rebootRecover;
    private int lastDamager = -1;
    private float accumul;
    private boolean filledUpFromOld;

    public SegmentControllerHpController(SendableSegmentController var1) {
        this.segmentController = var1;
    }

    private long getHpInternal() {
        if (this.isUsingNewHp()) {
            return this.segmentController.getReactorHp();
        } else {
            return this.useHPLong ? this.hpLong : (long)this.hpInt;
        }
    }

    private void setHpInternal(long var1) {
        boolean var3 = this.useHPLong;
        this.useHPLong = var1 >= 2147483647L;

        assert this.useHPLong == var3 : var3 + " -> " + this.useHPLong;

        if (this.useHPLong) {
            this.hpLong = var1;
        } else {
            assert var1 <= 2147483647L : var1;

            this.hpInt = (int)var1;
        }
    }

    private long getMaxHpInternalOld() {
        return this.useHPLong ? this.maxHpLong : (long)this.maxHpInt;
    }

    private boolean isUsingNewHp() {
        return !this.hadOldPowerBlocks() && this.segmentController.isUsingPowerReactors();
    }

    private long getMaxHpInternal() {
        return this.isUsingNewHp() ? this.segmentController.getReactorHpMax() : this.getMaxHpInternalOld();
    }

    private void setMaxHpInternal(long var1) {
        this.useHPLong = var1 >= 2147483647L;
        if (this.useHPLong) {
            this.maxHpLong = var1;
        } else {
            assert var1 <= 2147483647L : var1;

            this.maxHpInt = (int)var1;
        }
    }

    private void setLastDamager(Damager var1) {
        if (var1 != null) {
            if (var1.getOwnerState() != null) {
                this.lastDamager = var1.getOwnerState().getId();
                return;
            }

            if (var1 instanceof SimpleTransformableSendableObject) {
                this.lastDamager = ((SimpleTransformableSendableObject)var1).getId();
                return;
            }
        } else {
            this.lastDamager = -1;
        }

    }

    public void triggerOverheating() {
        assert this.isOnServer();

        if (!this.segmentController.isCoreOverheating() && !this.isRebooting()) {
            EntityOverheatEvent event = new EntityOverheatEvent(this.segmentController, lastDamager);
            StarLoader.fireEvent(EntityOverheatEvent.class, event);

            System.err.println("[SERVER] Overheating triggered for " + this.segmentController);
            Sendable var1 = (Sendable)this.segmentController.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.lastDamager);
            Damager var2 = null;
            if (var1 != null && var1 instanceof Damager) {
                var2 = (Damager)var1;
            }

            this.segmentController.startCoreOverheating(var2);
        }

    }

    private void handleEffectsServer() {
        if (this.segmentController.getSegmentBuffer().isFullyLoaded() && this.segmentController instanceof Ship && this.segmentController.getTotalElements() == 1) {
            SegmentPiece var8 = this.getSegmentController().getSegmentBuffer().getPointUnsave(Ship.core);
            if (!this.segmentController.isCoreOverheating() && var8 != null && var8.getType() == 1 && var8.getHitpointsByte() == 0) {
                System.err.println("[SERVER] Core AT 0 HP destroyed for " + this.segmentController + ", which is in new power system, is not docked, and has no active reactor (-> death on core destruction)");
                this.triggerOverheating();
            }

        } else if (this.segmentController.isNewPowerSystemNoReactor()) {
            if (!this.segmentController.isCoreOverheating() && this.segmentController.isNewPowerSystemNoReactorOverheatingCondition()) {
                System.err.println("[SERVER] Core destroyed for " + this.segmentController + ", which is in new power system, is not docked, and has no active reactor (-> death on core destruction)");
                this.triggerOverheating();
            }

        } else if (!this.segmentController.railController.isDockedAndExecuted() || !this.segmentController.railController.getRoot().hasActiveReactors()) {
            if (this.isUsingNewHp() || !(this.segmentController instanceof EditableSendableSegmentController) || this.segmentController.getState().getUpdateTime() <= ((EditableSendableSegmentController)this.segmentController).lastDamageTaken + 5000L) {
                try {
                    ObjectArrayList var2;
                    if ((var2 = VoidElementManager.HP_CONDITION_TRIGGER_LIST.get(this.segmentController.isUsingPowerReactors())) == null || var2.size() == 0) {
                        if (var2 == null) {
                            System.err.println("ERROR: NO HP CONDITIONS: " + this.segmentController + "; " + this.segmentController.getTypeString() + "; " + this.segmentController.isUsingPowerReactors() + ": Check: " + VoidElementManager.HP_CONDITION_TRIGGER_LIST.checkString());
                        }

                        return;
                    }
                } catch (Exception var7) {
                    throw new RuntimeException("Happened on entity: " + this.segmentController, var7);
                }

                double var9 = this.getHpPercent();
                Arrays.fill(this.used, false);
                int var1 = 0;
                ObjectArrayList var4 = VoidElementManager.HP_CONDITION_TRIGGER_LIST.get(this.segmentController.isUsingPowerReactors());

                int var5;
                for(var5 = 0; var5 < var4.size(); ++var5) {
                    HpCondition var6 = (HpCondition)var4.get(var5);
                    if (this.segmentController.isHandleHpCondition(var6.trigger.type) && var9 <= (double)var6.hpPercent && !this.used[var6.trigger.type.ordinal()]) {
                        this.used[var6.trigger.type.ordinal()] = true;
                        switch(var6.trigger.type) {
                            case CONTROL_LOSS:
                                if (this.segmentController.railController.isRoot()) {
                                    this.applyLostControl(true);
                                }
                                break;
                            case OVERHEATING:
                                this.triggerOverheating();
                                break;
                            case POWER:
                                this.applyPowerdown(var6.trigger.amount);
                                break;
                            case SHIELD:
                                this.applyShielddown(var6.trigger.amount);
                                break;
                            case THRUST:
                                if (this.segmentController.railController.isRoot()) {
                                    this.applySlowdown(var6.trigger.amount);
                                }
                        }

                        ++var1;
                        if (var1 == this.used.length) {
                            break;
                        }
                    }
                }

                for(var5 = 0; var5 < this.used.length; ++var5) {
                    if (!this.used[var5]) {
                        HpTriggerType var10 = HpTriggerType.values()[var5];
                        switch(var10) {
                            case CONTROL_LOSS:
                                this.applyLostControl(false);
                                break;
                            case OVERHEATING:
                                if (this.segmentController.isCoreOverheating()) {
                                    this.segmentController.stopCoreOverheating();
                                }
                                break;
                            case POWER:
                                this.applyPowerdown(1.0F);
                                break;
                            case SHIELD:
                                this.applyShielddown(1.0F);
                                break;
                            case THRUST:
                                this.applySlowdown(1.0F);
                        }
                    }
                }

            }
        }
    }

    private void applyLostControl(boolean var1) {
        if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.CONTROLLESS)) {
            if (var1) {
                return;
            }

            this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.CONTROLLESS).end();
        }

        if (var1) {
            this.segmentController.getBlockEffectManager().addEffect(new ControllessEffect(this.segmentController));
        }

    }

    private void applySlowdown(float var1) {
        if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.THRUSTER_OUTAGE)) {
            if (((ThrusterOutageEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.THRUSTER_OUTAGE)).getForce() == var1) {
                return;
            }

            this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.THRUSTER_OUTAGE).end();
        }

        if (var1 < 1.0F) {
            this.segmentController.getBlockEffectManager().addEffect(new ThrusterOutageEffect(this.segmentController, var1));
        }

    }

    private void applyShielddown(float var1) {
        if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.NO_SHIELD_RECHARGE)) {
            if (((ShieldRegenDownEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_SHIELD_RECHARGE)).getForce() == var1) {
                return;
            }

            this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_SHIELD_RECHARGE).end();
        }

        if (var1 < 1.0F) {
            this.segmentController.getBlockEffectManager().addEffect(new ShieldRegenDownEffect(this.segmentController, var1));
        }

    }

    private void applyPowerdown(float var1) {
        if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.NO_POWER_RECHARGE)) {
            if (((PowerRegenDownEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_POWER_RECHARGE)).getForce() == var1) {
                return;
            }

            this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_POWER_RECHARGE).end();
        }

        if (var1 < 1.0F) {
            this.segmentController.getBlockEffectManager().addEffect(new PowerRegenDownEffect(this.segmentController, var1));
        }

    }

    public float onHullDamage(Damager var1, float var2, short var3, DamageDealerType var4) {
        if (this.isRebooting()) {
            this.segmentController.sendControllingPlayersServerMessage(new Object[]{130}, 3);
            this.cancelReboot();
        }

        return var2;
    }

    public void forceDamage(float var1) {
        if (this.isOnServer()) {
            this.setHpInternal(Math.max(0L, this.getHpInternal() - (long)var1));
            this.setLastDamager((Damager)null);
        }

    }

    public void onAddedElementsSynched(int[] var1, int[] var2) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (this.isOnServer() && !this.loadedFromTag) {
                if (this.getHpInternal() == this.getMaxHpInternal()) {
                    this.hpDirty = true;
                }

                this.currentHPMatch.add(var1, var2);
                this.maxHPDirty = true;
            }

        }
    }

    public void onAddedElementSynched(short var1) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (this.isOnServer() && !this.loadedFromTag) {
                if (this.getHpInternal() == this.getMaxHpInternal()) {
                    this.hpDirty = true;
                }

                this.currentHPMatch.inc(var1);
                this.maxHPDirty = true;
            }

        }
    }

    public void updateLocal(Timer var1) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (this.isOnServer()) {
                if (this.segmentController.railController.isDockedAndExecuted() && this.segmentController.isNewPowerSystemNoReactorOverheatingCondition()) {
                    this.triggerOverheating();
                    return;
                }

                if (this.maxHPDirty) {
                    this.setMaxHpInternal(this.currentHPMatch.getMaxHP());
                    this.maxHPDirty = false;
                }

                if (this.hpDirty) {
                    this.setHpInternal(this.getMaxHpInternal());
                    this.hpDirty = false;
                }

                if (!this.getSegmentController().railController.hasActiveDockingRequest()) {
                    if (this.getSegmentController().railController.isDockedAndExecuted() && !this.filledUpFromOld && ((SegmentControllerHpController)this.getSegmentController().railController.getRoot().getHpController()).filledUpFromOld && this.getHpInternal() == 1L) {
                        this.hpDirty = true;
                        this.filledUpFromOld = true;
                        return;
                    }

                    this.handleEffectsServer();
                }
            }

            if (this.isRebooting()) {
                if (this.getRebootTimeLeftMS() <= 0L) {
                    this.forceReset();
                }

                if (this.segmentController.isClientOwnObject()) {
                    ((GameClientState)this.segmentController.getState()).getController().showBigTitleMessage("reboot", StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_11, new Object[]{StringTools.formatTimeFromMS(this.getRebootTimeLeftMS())}), 0.0F);
                }
            }

            if (this.isOnServer() && (this.getHpInternal() != this.lastHp || this.segmentController.isNewPowerSystemNoReactor() && this.segmentController.isNewPowerSystemNoReactorOverheatingCondition())) {
                this.handleEffectsServer();
            }

            this.lastHp = this.getHpInternal();
        }
    }

    public boolean hadOldPowerBlocks() {
        return this.currentHPMatch.get((short)2) > 0;
    }

    public void updateFromNetworkObject(NetworkSegmentController var1) {
        if (!this.isOnServer()) {
            this.useHPLong = this.segmentController.getNetworkObject().useHpLong.getBoolean();
            if (this.useHPLong) {
                this.hpLong = this.segmentController.getNetworkObject().hpLong.getLong();
                this.maxHpLong = this.segmentController.getNetworkObject().hpMaxLong.getLong();
            } else {
                this.hpInt = this.segmentController.getNetworkObject().hpInt.getInt();
                this.maxHpInt = this.segmentController.getNetworkObject().hpMaxInt.getInt();
            }

            if (this.segmentController.getNetworkObject().rebootStartTime.getLong() > 0L) {
                this.rebootStarted = this.segmentController.getNetworkObject().rebootStartTime.getLong() - (long)((GameClientState)this.segmentController.getState()).getServerTimeDifference();
            } else {
                this.rebootStarted = this.segmentController.getNetworkObject().rebootStartTime.getLong();
            }

            this.rebootTime = this.segmentController.getNetworkObject().rebootDuration.getLong();
            this.rebootRecover = this.segmentController.getNetworkObject().rebootRecover.getBoolean();
        }

    }

    public void initFromNetwork(NetworkSegmentController var1) {
        this.updateFromNetworkObject(var1);
    }

    public void updateToNetworkObject() {
        if (this.isOnServer()) {
            this.segmentController.getNetworkObject().useHpLong.set(this.useHPLong);
            if (this.useHPLong) {
                this.segmentController.getNetworkObject().hpLong.set(this.hpLong);
                this.segmentController.getNetworkObject().hpMaxLong.set(this.maxHpLong);
            } else {
                this.segmentController.getNetworkObject().hpInt.set(this.hpInt);
                this.segmentController.getNetworkObject().hpMaxInt.set(this.maxHpInt);
            }

            this.segmentController.getNetworkObject().rebootStartTime.set(this.rebootStarted);
            this.segmentController.getNetworkObject().rebootDuration.set(this.rebootTime);
            this.segmentController.getNetworkObject().rebootRecover.set(this.rebootRecover);
        }

    }

    public void updateToFullNetworkObject() {
        this.updateToNetworkObject();
    }

    public void onRemovedElementSynched(short var1) {
    }

    public void reboot(boolean var1) {
        if (!this.isOnServer()) {
            if (!((ManagedSegmentController)this.segmentController).getManagerContainer().getPowerInterface().isAnyRebooting()) {
                ((GameClientState)this.segmentController.getState()).getPlayer().sendSimpleCommand(SimplePlayerCommands.REBOOT_STRUCTURE, new Object[]{this.segmentController.getId(), var1});
                if (this.segmentController instanceof ManagedSegmentController) {
                    ((ManagedSegmentController)this.segmentController).getManagerContainer().getPowerInterface().requestRecalibrate();
                    return;
                }
            }
        } else {
            this.rebootRecover = false;
            if (var1) {
                this.forceReset();
            } else {
                if (this.segmentController instanceof ManagedSegmentController) {
                    if (((ManagedSegmentController)this.segmentController).getManagerContainer() instanceof ShieldContainerInterface) {
                        ShieldAddOn var2;
                        (var2 = ((ShieldContainerInterface)((ManagedSegmentController)this.segmentController).getManagerContainer()).getShieldAddOn()).onHit(0L, (short)0, (double)((long)Math.ceil(var2.getShields())), DamageDealerType.GENERAL);
                        var2.getShieldLocalAddOn().dischargeAllShields();
                    }

                    if (((ManagedSegmentController)this.segmentController).getManagerContainer() instanceof PowerManagerInterface) {
                        PowerAddOn var10000 = ((PowerManagerInterface)((ManagedSegmentController)this.segmentController).getManagerContainer()).getPowerAddOn();
                        var10000.consumePowerInstantly(var10000.getPower());
                    }
                }

                if (this.segmentController.isUsingOldPower()) {
                    this.rebootStarted = System.currentTimeMillis();
                    this.rebootTime = this.getRebootTimeMS();
                }
            }

            if (this.segmentController.isCoreOverheating()) {
                this.rebootRecover = true;
                this.segmentController.stopCoreOverheating();
            }
        }

    }

    public void forceReset() {
        if (this.isOnServer()) {
            if (this.segmentController instanceof Ship && this.segmentController.getSegmentBuffer().getPointUnsave(Ship.core) == null) {
                System.err.println("[ERROR] Core not loaded");
                return;
            }

            this.rebootStarted = 0L;
            this.rebootTime = 0L;
            this.currentHPMatch.resetAll();
            this.currentHPMatch.add(this.segmentController.getElementClassCountMap());
            this.maxHPDirty = true;
            this.hpDirty = true;
            SegmentPiece var1;
            if (this.segmentController instanceof Ship && (var1 = this.segmentController.getSegmentBuffer().getPointUnsave(Ship.core)).getType() == 1 && var1.isDead()) {
                var1.setHitpointsByte(1);

                try {
                    var1.getSegment().getSegmentData().applySegmentData(var1, System.currentTimeMillis());
                } catch (SegmentDataWriteException var3) {
                    SegmentDataWriteException.replaceData(var1.getSegment());

                    try {
                        var1.getSegment().getSegmentData().applySegmentData(var1, System.currentTimeMillis());
                    } catch (SegmentDataWriteException var2) {
                        throw new RuntimeException(var2);
                    }
                }

                ((Ship)this.segmentController).sendBlockMod(new RemoteSegmentPiece(var1, this.isOnServer()));
            }
        }

    }

    public boolean isRebooting() {
        return this.rebootStarted > 0L;
    }

    public long getRebootTimeLeftMS() {
        return this.rebootStarted + this.rebootTime - System.currentTimeMillis();
    }

    public long getRebootTimeMS() {
        if (this.isOnServer()) {
            long var1;
            if (this.getSegmentController().isUsingOldPower()) {
                var1 = Math.max((long)(this.getHpMissingPercent() * VoidElementManager.SHIP_REBOOT_TIME_IN_SEC_PER_MISSING_HP_PERCENT * Math.max(1.0D, this.currentHPMatch.getMass() * VoidElementManager.SHIP_REBOOT_TIME_MULTIPLYER_PER_MASS) * 1000.0D), (long)(VoidElementManager.SHIP_REBOOT_TIME_MIN_SEC * 1000.0D));
            } else {
                var1 = (long)(((ManagedSegmentController)this.segmentController).getManagerContainer().getPowerInterface().getRebootTimeSec() * 1000.0F);
            }

            return var1;
        } else {
            if (!this.isRequestedTimeClient()) {
                ((GameClientState)this.segmentController.getState()).getPlayer().sendSimpleCommand(SimplePlayerCommands.REBOOT_STRUCTURE_REQUEST_TIME, new Object[]{this.segmentController.getId()});
                this.setRequestedTimeClient(true);
            }

            return this.rebootTime;
        }
    }

    public void onElementDestroyed(Damager var1, ElementInformation var2, DamageDealerType var3, long var4) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (this.isOnServer()) {
                double var6;
                int var8 = (int)((var6 = (double)var2.structureHP) + (double)this.getSystemStabilityPenalty() * var6);
                this.setHpInternal(Math.max(0L, this.getHpInternal() - (long)var8));
                this.setLastDamager(var1);
            }

        }
    }

    public void onManualRemoveBlock(ElementInformation var1) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (this.isOnServer()) {
                if (this.getHpInternal() == this.getMaxHpInternal()) {
                    this.currentHPMatch.dec(var1.getId());
                    this.maxHPDirty = true;
                    this.hpDirty = true;
                } else {
                    this.setHpInternal(Math.max(0L, this.getHpInternal() - (long)var1.structureHP));
                }

                this.setLastDamager((Damager)null);
            }

        }
    }

    public double getHpPercent() {
        return this.getMaxHpInternal() > 0L ? (double)this.getHpInternal() / (double)this.getMaxHpInternal() : 1.0D;
    }

    public void setHpPercent(float var1) {
        this.setHpInternal((long)((double)var1 * (double)this.getHpInternal()));
    }

    public boolean isRebootingRecoverFromOverheating() {
        return this.isRebooting() && this.rebootRecover;
    }

    public float getSystemStabilityPenalty() {
        return VoidElementManager.HP_DEDUCTION_LOG_FACTOR * Math.max(0.0F, Math.max(0.0F, (float)Math.log10((double)this.getMaxHpInternal())) + VoidElementManager.HP_DEDUCTION_LOG_OFFSET);
    }

    public SegmentController getSegmentController() {
        return this.segmentController;
    }

    public boolean isOnServer() {
        return this.segmentController.isOnServer();
    }

    public void checkOneHp() {
        if (this.getSegmentController().usedOldPowerFromTag && this.getMaxHpInternalOld() == 0L) {
            this.setMaxHpInternal(this.currentHPMatch.getMaxHP());
            if (this.getMaxHpInternalOld() == 0L) {
                this.currentHPMatch.resetAll();
                this.setMaxHpInternal(0L);
                this.setHpInternal(0L);
                this.loadedFromTag = false;
                return;
            }
        } else if (this.hadOldPowerBlocks() && this.getHpInternal() == 1L) {
            this.maxHPDirty = true;
            this.hpDirty = true;
            this.filledUpFromOld = true;
            System.err.println("[HP] reset hp of " + this.getSegmentController() + " with old power since it was at 1");
        }

    }

    public void fromTagStructure(Tag var1) {
        Tag[] var2;
        if ((Byte)(var2 = (Tag[])var1.getValue())[0].getValue() == 1) {
            this.loadedFromTag = true;
            if ((var2 = (Tag[])var2[1].getValue())[0].getType() == Type.LONG) {
                this.useHPLong = true;
                this.hpLong = (Long)var2[0].getValue();
                this.maxHpLong = (Long)var2[1].getValue();
            } else {
                this.useHPLong = false;
                this.hpInt = (Integer)var2[0].getValue();
                this.maxHpInt = (Integer)var2[1].getValue();
            }

            var2[2].getType();
            Type var10000 = Type.LONG;
            this.rebootStarted = (Long)var2[4].getValue();
            this.rebootTime = (Long)var2[5].getValue();
            this.currentHPMatch = (ElementCountMap)var2[6].getValue();
            this.rebootRecover = var2.length > 7 && var2[7].getType() == Type.BYTE && (Byte)var2[7].getValue() != 0;
            if (this.getHpInternal() > this.getMaxHpInternal()) {
                this.setMaxHpInternal(this.currentHPMatch.getMaxHP());
                this.setHpInternal(this.getMaxHpInternal());
                this.maxHPDirty = true;
                this.hpDirty = true;
            }
        }

    }

    public Tag toTagStructure() {
        Tag var1 = new Tag(Type.BYTE, (String)null, (byte)1);
        Tag var2;
        Tag var3;
        if (this.useHPLong) {
            var2 = new Tag(Type.LONG, (String)null, this.hpLong);
            var3 = new Tag(Type.LONG, (String)null, this.maxHpLong);
        } else {
            var2 = new Tag(Type.INT, (String)null, this.hpInt);
            var3 = new Tag(Type.INT, (String)null, this.maxHpInt);
        }

        Tag var4 = new Tag(Type.LONG, (String)null, 0L);
        Tag var5 = new Tag(Type.LONG, (String)null, 0L);
        System.err.println("[HP][SAVE] saved " + this.getSegmentController() + "; Reactors: " + this.getSegmentController().isUsingPowerReactors() + "; HP: long " + this.hpLong + "/" + this.maxHpLong + "; int " + this.hpInt + "/" + this.maxHpInt + "; using long " + this.useHPLong);
        Tag var6 = new Tag(Type.LONG, (String)null, this.rebootStarted);
        Tag var7 = new Tag(Type.LONG, (String)null, this.rebootTime);
        Tag var8 = new Tag(Type.SERIALIZABLE, (String)null, new ElementCountMap(this.currentHPMatch));
        Tag var9 = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.rebootRecover ? 1 : 0)));
        return new Tag(Type.STRUCT, (String)null, new Tag[]{var1, new Tag(Type.STRUCT, (String)null, new Tag[]{var2, var3, var4, var5, var6, var7, var8, var9, FinishTag.INST}), FinishTag.INST});
    }

    private void cancelReboot() {
        this.rebootStarted = 0L;
        this.rebootTime = 0L;
    }

    public double getHpMissingPercent() {
        return 1.0D - this.getHpPercent();
    }

    public boolean isRequestedTimeClient() {
        return this.requestedTimeClient;
    }

    public void setRequestedTimeClient(boolean var1) {
        this.requestedTimeClient = var1;
    }

    public void setRebootTimeServerForced(long var1) {
        this.rebootTime = var1;
    }

    public long getHp() {
        return this.getHpInternal();
    }

    public long getMaxHp() {
        return this.getMaxHpInternal();
    }

    public String getDebuffString() {
        if (this.getHpInternal() == this.getMaxHpInternal()) {
            return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_1;
        } else {
            StringBuffer var1 = new StringBuffer();
            if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.CONTROLLESS)) {
                if (var1.length() == 0) {
                    var1.append(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_2);
                } else {
                    var1.append(", Controls");
                }
            }

            String var2;
            if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.NO_POWER_RECHARGE)) {
                var2 = "Power Recharge (" + StringTools.formatPointZero(((PowerRegenDownEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_POWER_RECHARGE)).getForce() * 100.0F) + "%)";
                if (var1.length() == 0) {
                    var1.append(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_4);
                } else {
                    var1.append(", ");
                }

                var1.append(var2);
            }

            if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.NO_SHIELD_RECHARGE)) {
                var2 = "Shield Recharge (" + StringTools.formatPointZero(((ShieldRegenDownEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.NO_SHIELD_RECHARGE)).getForce() * 100.0F) + "%)";
                if (var1.length() == 0) {
                    var1.append(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_3);
                } else {
                    var1.append(", ");
                }

                var1.append(var2);
            }

            if (this.segmentController.getBlockEffectManager().hasEffect(BlockEffectTypes.THRUSTER_OUTAGE)) {
                var2 = "Thrust (" + StringTools.formatPointZero(((ThrusterOutageEffect)this.segmentController.getBlockEffectManager().getEffect(BlockEffectTypes.THRUSTER_OUTAGE)).getForce() * 100.0F) + "%)";
                if (var1.length() == 0) {
                    var1.append(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_5);
                } else {
                    var1.append(", ");
                }

                var1.append(var2);
            }

            if (this.segmentController.isCoreOverheating()) {
                var1.append("\n" + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_6 + StringTools.formatTimeFromMS(this.segmentController.getCoreOverheatingTimeLeftMS(System.currentTimeMillis())) + "!");
            } else if (this.isRebooting()) {
                var1.append("\n" + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_7 + StringTools.formatTimeFromMS(this.getRebootTimeLeftMS()) + "!");
            }

            if (var1.length() == 0) {
                var1.append(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_8, new Object[]{KeyboardMappings.REBOOT_SYSTEMS.getKeyChar()}));
            } else {
                var1.append("\n" + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLERHPCONTROLLER_9);
            }

            return var1.toString();
        }
    }

    public void onManualAddBlock(ElementInformation var1) {
        if (this.segmentController.hasStructureAndArmorHP()) {
            if (!this.loadedFromTag) {
                this.loadedFromTag = true;
            } else {
                if (this.isOnServer()) {
                    if (this.getHpInternal() == this.getMaxHpInternal()) {
                        this.hpDirty = true;
                    }

                    this.setLastDamager((Damager)null);
                    this.currentHPMatch.inc(var1.getId());
                    this.maxHPDirty = true;
                }

            }
        }
    }

    public long getShopRebootCost() {
        return (long)((double)(this.getRebootTimeMS() / 1000L) * (double)((GameStateInterface)this.segmentController.getState()).getGameState().getShopRebootCostPerSecond());
    }
}
