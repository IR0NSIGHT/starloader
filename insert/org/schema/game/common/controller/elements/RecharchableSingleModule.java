//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements;

import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer.Hos;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.client.view.gui.weapon.WeaponSegmentControllerUsableElement;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate.Type;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.MouseEvent.ShootButton;
import org.schema.schine.graphicsengine.core.settings.ContextFilter;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import org.schema.schine.input.InputType;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;

public abstract class RecharchableSingleModule extends SegmentControllerUsable implements ManagerReloadInterface, ManagerUpdatableInterface, TagModuleUsableInterface, PowerConsumer {
    private static final long RELOAD_AFTER_USE_MS = 400L;
    private float initialCharge;
    public long lastSentZero;
    private short lastCharge;
    private float charge;
    private float powered;
    private long lastUse;
    private short chargedOnCycle;
    private boolean hasChargedACycle;
    private boolean autoChargeOn;
    private boolean checkedForInitialMetaData;
    private int charges;
    private boolean updating;
    protected GUITextOverlay chargesText;

    public int getMaxCharges() {
        return 1;
    }

    public void dischargeFully() {
        this.charge = 0.0F;
        this.charges = 0;
    }

    public RecharchableSingleModule(ManagerContainer<?> var1) {
        super(var1);
        var1.addRechargeSingleModule(this);
    }

    public float getMassWithDocks() {
        return this.getSegmentController().getMassWithDocks();
    }

    public boolean canUpdate() {
        return this.isPowerConsumerActive() && this.isPlayerUsable();
    }

    public void onNoUpdate(Timer var1) {
        if (this.updating) {
            this.onNoLongerConsumerActiveOrUsable(var1);
            this.updating = false;
        }

    }

    protected abstract void onNoLongerConsumerActiveOrUsable(Timer var1);

    public void update(Timer var1) {
        this.updating = true;
        if (((ManagedSegmentController)this.getSegmentController()).getManagerContainer().isRequestedInitalValuesIfNeeded()) {
            BlockMetaDataDummy var2;
            if (this.getSegmentController().isOnServer() && !this.checkedForInitialMetaData && (var2 = (BlockMetaDataDummy)this.getContainer().getInitialBlockMetaData().remove(this.getUsableId())) != null) {
                this.applyMetaData(var2);
            }

            this.checkedForInitialMetaData = true;
            if (this.getSegmentController().isOnServer() && this.initialCharge > 0.0F) {
                this.setCharge(this.initialCharge);
                this.initialCharge = 0.0F;
                this.sendChargeUpdate();
            }

            if (this.isCharged() && !this.isAllChargesCharged()) {
                this.addCharge();
                if (!this.isAllChargesCharged()) {
                    this.setCharge(0.0F);
                }

                this.sendChargeUpdate();
            }

        }
    }

    protected abstract Type getServerRequestType();

    private void applyMetaData(BlockMetaDataDummy var1) {
        this.initialCharge = ((ChargeMetaDummy)var1).charge;
    }

    public BlockMetaDataDummy getDummyInstance() {
        return new ChargeMetaDummy(this);
    }

    public abstract void sendChargeUpdate();

    public void onHit(double var1, int var3) {
        if (this.getSegmentController().isOnServer() && this.isDischargedOnHit() && this.getCharge() > 0.0F && System.currentTimeMillis() - this.lastSentZero > 5000L) {
            this.setCharge(0.0F);
            this.sendChargeUpdate();
            this.lastSentZero = System.currentTimeMillis();
        }

    }

    public abstract boolean isDischargedOnHit();

    public float getChargeAddedPerSec() {
        return 1.0F / this.getChargeRateFull();
    }

    public abstract boolean executeModule();

    public void charge(float var1, boolean var2, float var3, float var4) {
        if (!this.isCharged() && this.lastCharge != this.getState().getNumberOfUpdate()) {
            float var6 = this.getCharge();
            if ((double)var3 < VoidElementManager.REACTOR_MODULE_DISCHARGE_MARGIN) {
                int var5 = this.getCharges();
                this.discharge((VoidElementManager.REACTOR_MODULE_DISCHARGE_MARGIN - (double)var3) * (double)this.getChargeAddedPerSec());
                if (this.getCharge() == 0.0F && var5 > 0) {
                    this.setCharges(var5 - 1);
                    this.setCharge(1.0F);
                    if (this.isOnServer()) {
                        this.sendChargeUpdate();
                    }
                }
            } else {
                var1 = var4 * this.getChargeAddedPerSec() * var1;
                this.setCharge(Math.min(1.0F, this.getCharge() + var1));
                if (this.isCharged()) {
                    if (!this.isAutoCharging()) {
                        this.onChargedFullyNotAutocharged();
                    }

                    if (var6 < this.getCharge()) {
                        this.sendChargeUpdate();
                    }
                }
            }

            this.lastCharge = this.getState().getNumberOfUpdate();
        }

    }

    public void removeCharge() {
        this.charges = Math.max(0, this.getCharges() - 1);
    }

    public void addCharge() {
        this.charges = Math.min(this.getMaxCharges(), this.getCharges() + 1);
    }

    public abstract void onChargedFullyNotAutocharged();

    public abstract float getChargeRateFull();

    public boolean isAllChargesCharged() {
        return this.getCharges() >= this.getMaxCharges();
    }

    public boolean hasCharges() {
        return this.getCharges() > 0;
    }

    public boolean isCharged() {
        return this.getCharge() >= 1.0F;
    }

    public float getCharge() {
        return this.charge;
    }

    public void setCharge(float var1) {
        this.charge = var1;
    }

    public void discharge(double var1) {
        this.setCharge((float)Math.max(0.0D, (double)this.getCharge() - var1));
    }

    public boolean isPowerCharging(long var1) {
        boolean var3 = this.isAllChargesCharged() && this.isAutoChargeOn() && !this.isAllChargesCharged();
        boolean var2 = this.getState().getNumberOfUpdate() <= this.chargedOnCycle + 3;
        if (this.hasChargedACycle) {
            if (var2) {
                var2 = true;
                return var3 || var2;
            }

            this.hasChargedACycle = false;
        }

        var2 = false;
        return var3 || var2;
    }

    public void setPowered(float var1) {
        this.powered = var1;
    }

    public float getPowered() {
        return this.powered;
    }

    public ConfigEntityManager getConfigManager() {
        return this.getSegmentController().getConfigManager();
    }

    public abstract boolean isAutoCharging();

    public void reloadFromReactor(double var1, Timer var3, float var4, boolean var5, float var6) {
        if (!this.isAutoCharging()) {
            this.setAutoChargeOn(false);
        } else if (!this.isAutoChargeToggable()) {
            this.setAutoChargeOn(true);
        }

        if (this.isAutoChargeOn() && !this.isCharged()) {
            this.charge(var4, var5, var6, this.getPowered());
            this.hasChargedACycle = true;
            this.chargedOnCycle = this.getState().getNumberOfUpdate();
        }

    }

    public boolean isControllerConnectedTo(long var1, short var3) {
        return var3 == 1;
    }

    public boolean isPlayerUsable() {
        return this.getSegmentController().hasActiveReactors() && this.isPowerConsumerActive();
    }

    public void handleControl(ControllerStateInterface var1, Timer var2) {
        if ((var1.isPrimaryShootButtonDown() || var1.isSecondaryShootButtonDown()) && var1.isFlightControllerActive()) {
            this.handle(var1, var2);
        }

    }

    public void addHudConext(ControllerStateUnit var1, HudContextHelpManager var2, Hos var3) {
        String var5 = this.autoChargeOn ? Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_3 : Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_4;
        boolean var4 = this.isAutoCharging();
        if (this.isActive() && this.isDeactivatableManually() && !this.isAutoChargeToggable()) {
            var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_5, var3, ContextFilter.IMPORTANT);
        } else if (!this.isActive() && var4 && this.isAutoChargeToggable()) {
            var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_9, new Object[]{var5}), var3, ContextFilter.IMPORTANT);
        } else if (!this.isCharged() && !this.isActive()) {
            if (this.isAutoChargeToggable()) {
                var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_6, new Object[]{var5}), var3, ContextFilter.IMPORTANT);
            } else {
                var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_8, new Object[]{var5}), var3, ContextFilter.IMPORTANT);
            }
        } else {
            if (this.isCharged() && !this.isActive()) {
                if (this.isAutoChargeToggable()) {
                    var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_7, new Object[]{var5}), var3, ContextFilter.IMPORTANT);
                }

                if (this.hasCharges()) {
                    if (!this.isAutoChargeToggable() && this.autoChargeOn) {
                        var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), this.getExecuteVerb(), var3, ContextFilter.IMPORTANT);
                        return;
                    }

                    var2.addHelper(InputType.MOUSE, ShootButton.SECONDARY_FIRE.getButton(), this.getExecuteVerb(), var3, ContextFilter.IMPORTANT);
                }
            }

        }
    }

    public abstract String getExecuteVerb();

    public void handle(ControllerStateInterface var1, Timer var2) {
        long var3 = var2.currentTime - this.lastUse;
        boolean var5 = false;
        if (var3 > 400L) {
            if (this.isActive() && this.isDeactivatableManually() && (var1.clickedOnce(ShootButton.PRIMARY_FIRE.getButton()) && !this.isAutoChargeToggable() || var1.clickedOnce(ShootButton.SECONDARY_FIRE.getButton()) && this.isAutoChargeToggable())) {
                if (this.isOnServer() || var1.getPlayerState() == ((GameClientState)this.getState()).getPlayer() || !(this instanceof RecharchableActivatableDurationSingleModule)) {
                    this.deactivateManually();
                }
            } else {
                boolean var6 = this.isAutoCharging();
                if (!this.isActive() && var6 && (var1.clickedOnce(ShootButton.PRIMARY_FIRE.getButton()) || !this.isAutoChargeToggable())) {
                    this.autoChargeOn = !this.autoChargeOn;
                    var5 = true;
                } else if (!this.isCharged() && !this.isActive() && (var1.isMouseButtonDown(ShootButton.PRIMARY_FIRE.getButton()) || var1.clickedOnce(ShootButton.PRIMARY_FIRE.getButton()))) {
                    if (var6 && (var1.clickedOnce(ShootButton.PRIMARY_FIRE.getButton()) || !this.isAutoChargeToggable())) {
                        this.autoChargeOn = !this.autoChargeOn;
                        var5 = true;
                    } else if (!var6 && this.autoChargeOn) {
                        this.setAutoChargeOn(false);
                        var5 = true;
                    }

                    if (!this.isAutoChargeToggable() && !this.isAutoChargeOn()) {
                        this.chargingMessage();
                        this.charge(var2.getDelta(), !this.isCharged(), 1.0F, this.getPowered());
                        this.hasChargedACycle = true;
                        this.chargedOnCycle = this.getState().getNumberOfUpdate();
                    }
                }

                if (this.hasCharges() && (var1.clickedOnce(ShootButton.SECONDARY_FIRE.getButton()) || var1.clickedOnce(ShootButton.PRIMARY_FIRE.getButton()) && !this.isAutoChargeToggable() && this.autoChargeOn)) {
                    if (this.canExecute()) {
                        this.executeModule();
                        this.lastUse = var2.currentTime;
                    } else {
                        System.err.println(this.getState() + "[RECHARGESINFLEMODULE] CANNOT EXECUTE: " + this);
                    }
                }
            }
        } else if (var3 > 500L) {
            long var8 = (400L - var3) / 1000L;
            this.onCooldown(var8);
        }

        if (this.getPowered() <= 1.0E-7F && !this.isOnServer()) {
            this.onUnpowered();
        }

        if (var5) {
            this.sendChargeUpdate();
            if (!this.isOnServer()) {
                this.requestValueOnClient();
            }
        }

    }

    private void requestValueOnClient() {
        ServerValueRequestUpdate var1 = new ServerValueRequestUpdate(this.getServerRequestType());

        assert var1.getType() == ValTypes.SERVER_UPDATE_REQUEST;

        var1.setServer(this.getContainer());
        ((NTValueUpdateInterface)this.getSegmentController().getNetworkObject()).getValueUpdateBuffer().add(new RemoteValueUpdate(var1, this.getSegmentController().isOnServer()));
    }

    protected void deactivateManually() {
    }

    protected boolean isDeactivatableManually() {
        return false;
    }

    public boolean isActive() {
        return false;
    }

    public abstract void chargingMessage();

    public abstract void onUnpowered();

    public abstract void onCooldown(long var1);

    public abstract boolean canExecute();

    public abstract boolean isAutoChargeToggable();

    public boolean isOnServer() {
        return this.segmentController.isOnServer();
    }

    public ManagerReloadInterface getReloadInterface() {
        return this;
    }

    public ManagerActivityInterface getActivityInterface() {
        return null;
    }

    public long getTimeLeftMs() {
        return -1L;
    }

    public String getReloadStatus(long var1) {
        if (this.getTimeLeftMs() > -1L) {
            return StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_0, new Object[]{StringTools.formatPointZero((double)this.getTimeLeftMs() / 1000.0D)});
        } else {
            return this.isActive() ? Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_2 : StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_RECHARCHABLESINGLEMODULE_1, new Object[]{StringTools.formatPointZero((double)this.getCharge() * 100.0D), this.getCharges(), this.getMaxCharges()});
        }
    }

    public void drawReloads(Vector3i var1, Vector3i var2, long var3) {
        float var5 = this.getCharge();
        if (this.chargesText == null) {
            this.chargesText = new GUITextOverlay(10, 10, FontSize.MEDIUM.getFont(), (InputState)this.getState());
            this.chargesText.onInit();
        }

        UsableControllableElementManager.drawReload((InputState)this.getState(), var1, var2, UsableControllableFireingElementManager.reloadColor, false, var5, false, (float)this.getCharges(), this.getMaxCharges(), this.getTimeLeftMs(), this.chargesText);
    }

    public WeaponRowElementInterface getWeaponRow() {
        return new WeaponSegmentControllerUsableElement(this);
    }

    public Tag toTagStructure() {
        return new Tag(org.schema.schine.resource.tag.Tag.Type.STRUCT, this.getTagId(), new Tag[]{new Tag(org.schema.schine.resource.tag.Tag.Type.STRUCT, (String)null, new Tag[]{new Tag(org.schema.schine.resource.tag.Tag.Type.LONG, (String)null, this.getUsableId()), this.toTagStructurePriv(), FinishTag.INST}), FinishTag.INST});
    }

    public Tag toTagStructurePriv() {
        return new Tag(org.schema.schine.resource.tag.Tag.Type.STRUCT, (String)null, new Tag[]{new Tag(org.schema.schine.resource.tag.Tag.Type.FLOAT, (String)null, encodeCharge(this.charge, this.charges)), new Tag(org.schema.schine.resource.tag.Tag.Type.BYTE, (String)null, Byte.valueOf((byte)(this.autoChargeOn ? 1 : 0))), new Tag(org.schema.schine.resource.tag.Tag.Type.BYTE, (String)null, Byte.valueOf((byte)(this.isActive() ? 1 : 0))), FinishTag.INST});
    }

    public void fromTagStructrePriv(Tag var1, int var2) {
        float var3;
        if (var1.getType() == org.schema.schine.resource.tag.Tag.Type.FLOAT) {
            var3 = var1.getFloat();
        } else {
            Tag[] var4;
            var3 = (var4 = var1.getStruct())[0].getFloat();
            this.autoChargeOn = var4[1].getBoolean();
            if (var4[2].getType() == org.schema.schine.resource.tag.Tag.Type.BYTE) {
                this.setActiveFromTag(var4[2].getBoolean());
            }
        }

        this.charges = Math.min(this.getMaxCharges(), decodeCharges(var3));
        this.charge = Math.min(1.0F, decodeCharge(var3));
    }

    protected void setActiveFromTag(boolean var1) {
    }

    public abstract String getWeaponRowName();

    public abstract short getWeaponRowIcon();

    public boolean isAutoChargeOn() {
        return this.autoChargeOn || this.segmentController.isAIControlled();
    }

    public void setAutoChargeOn(boolean var1) {
        this.autoChargeOn = var1;
    }

    public static float encodeCharge(float var0, int var1) {
        return var0 + (float)(var1 * 10);
    }

    public static int decodeCharges(float var0) {
        return (int)var0 / 10;
    }

    public static float decodeCharge(float var0) {
        return var0 - (float)decodeCharges(var0) * 10.0F;
    }

    public int getCharges() {
        return this.charges;
    }

    public void setCharges(int var1) {
        this.charges = var1;
    }
}
