//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.addons.custom;

import api.entity.Entity;
import api.server.Server;
import org.schema.common.util.StringTools;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.RecharchableActivatableDurationSingleModule;
import org.schema.game.common.controller.elements.SingleModuleActivation;
import org.schema.game.common.controller.elements.jumpprohibiter.InterdictionAddOn;
import org.schema.game.common.controller.elements.scanner.ScanAddOnChargeValueUpdate;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ServerValueRequestUpdate.Type;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;

public abstract class CustomAddOn extends RecharchableActivatableDurationSingleModule {
    Entity entity;
    public CustomAddOn(ManagerContainer<?> var1) {
        super(var1);
        entity = new Entity(getSegmentController());
    }
    public boolean entityHasEffect(StatusEffectType type){
        return this.getConfigManager().apply(type, 1F) == 10F;
    }

    public void sendChargeUpdate() {
        if (this.isOnServer()) {
            ScanAddOnChargeValueUpdate var1;
            (var1 = new ScanAddOnChargeValueUpdate()).setServer(((ManagedSegmentController)this.getSegmentController()).getManagerContainer(), this.getUsableId());
            assert var1.getType() == ValTypes.SCAN_CHARGE_REACTOR;
            ((NTValueUpdateInterface)this.getSegmentController().getNetworkObject()).getValueUpdateBuffer().add(new RemoteValueUpdate(var1, this.getSegmentController().isOnServer()));
        }

    }

    public boolean isDischargedOnHit() {
        return true;
    }

    public void onChargedFullyNotAutocharged() {
        this.getSegmentController().popupOwnClientMessage("Addon not fully charged", 1);
    }

    public abstract float getChargeRate();

    public void dischargeToZero(){
        this.setCharge(0);
        this.setCharges(0);
        SingleModuleActivation mod = this.activation;
        //this.activation = null;
        this.sendChargeUpdate();
        //this.activation = mod;
    }

    public float getChargeRateFull() {
        return getChargeRate();
       // float var1 = VoidElementManager.SCAN_CHARGE_NEEDED;
       // return this.getConfigManager().apply(StatusEffectType.SCAN_CHARGE_TIME, var1);
    }

    public boolean canExecute() {
        return !this.isActive();
    }

    public abstract double getPowerConsumedPerSecondResting();
    /*    float var1 = VoidElementManager.SCAN_CONSUMPTION_RESTING + this.getMassWithDocks() * VoidElementManager.SCAN_CONSUMPTION_RESTING_ADDED_BY_MASS;
        double var2 = (double)this.getConfigManager().apply(StatusEffectType.SCAN_POWER_TOPOFF_RATE, var1);
        if (this.isActive()) {
            return this.getConfigManager().apply(StatusEffectType.SCAN_ACTIVE_RESTING_POWER_CONS, false) ? this.getConfigManager().apply(StatusEffectType.SCAN_ACTIVE_RESTING_POWER_CONS_MULT, this.getPowerConsumedPerSecondCharging()) : var2;
        } else {
            return this.getConfigManager().apply(StatusEffectType.SCAN_INACTIVE_RESTING_POWER_CONS, false) ? this.getConfigManager().apply(StatusEffectType.SCAN_INACTIVE_RESTING_POWER_CONS_MULT, this.getPowerConsumedPerSecondCharging()) : var2;
        }
    }*/

    public abstract double getPowerConsumedPerSecondCharging();/* {
        float var1 = VoidElementManager.SCAN_CONSUMPTION_CHARGING + this.getMassWithDocks() * VoidElementManager.SCAN_CONSUMPTION_CHARGING_ADDED_BY_MASS;
        return (double)this.getConfigManager().apply(StatusEffectType.SCAN_POWER_CHARGE_RATE, var1);
    }*/

    public boolean isAutoCharging() {
        return true;
    }

    public boolean isAutoChargeToggable() {
        return true;
    }

    public abstract long getUsableId();//PlayerUsableInterface.whatever

    public void chargingMessage() {
        this.getSegmentController().popupOwnClientMessage("Custom add-on charging", 1);
    }

    public void onCooldown(long var1) {
        this.getSegmentController().popupOwnClientMessage(StringTools.format("On cooldown: \n(%d secs)", new Object[]{var1}), 3);
    }

    public void onUnpowered() {
        this.getSegmentController().popupOwnClientMessage("Add-on Unpowered", 3);
    }

    public String getTagId() {
        return "RSCN";
    }

    public int updatePrio() {
        return 1;
    }

    public PowerConsumerCategory getPowerConsumerCategory(){
        return PowerConsumerCategory.OTHERS;
    }

    public boolean isPlayerUsable() {
        return true;//!((GameStateInterface)this.getSegmentController().getState()).getGameState().isModuleEnabledByDefault(this.getUsableId()) && !this.getConfigManager().apply(StatusEffectType.SCAN_SHORT_RANGE_SCANNER_ENABLE, false) ? false : super.isPlayerUsable();
    }

    public abstract String getWeaponRowName();

    public abstract short getWeaponRowIcon(); //{
        //return (short) (Math.random()*700);
    //}

    public boolean isPowerConsumerActive() {
        return true;
    }

    public abstract float getDuration();// {
        //return this.getConfigManager().apply(StatusEffectType.SCAN_USAGE_TIME, VoidElementManager.SCAN_DURATION_BASIC);
    //}

//    public float getActiveStrength() {
//        return this.isActive() ? this.getConfigManager().apply(StatusEffectType.SCAN_STRENGTH, VoidElementManager.SCAN_STRENGTH_BASIC) : 0.0F;
//    }
    public abstract boolean onExecute();

    @Override
    public boolean executeModule() {
        if(this.getCharge() >= 1) {
            boolean success = super.executeModule();
            if (success) {
                onExecute();
            }
            return success;
        }
        return false;
    }
    public void onDeactivateFromTime(){
    }

    public void update(Timer var1) {
        boolean active = this.activation != null;
        super.update(var1);
        if (this.isActive()) {
            onActive();
        }else{
            onInactive();
        }
        if(active && this.activation == null){
            this.onDeactivateFromTime();
        }
    }
    public abstract void onActive();
    public abstract void onInactive();

    public abstract String getName();

    protected Type getServerRequestType() {
        return Type.SCAN;
    }

    protected boolean isDeactivatableManually() {
        return false;
    }

    protected void onNoLongerConsumerActiveOrUsable(Timer var1) {

    }

    public String getExecuteVerb() {
        return "Execute";
    }
}
