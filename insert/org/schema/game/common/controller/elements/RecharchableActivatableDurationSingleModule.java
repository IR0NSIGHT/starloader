//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements;

import org.schema.game.client.data.GameClientState;
import org.schema.schine.graphicsengine.core.Timer;

public abstract class RecharchableActivatableDurationSingleModule extends RecharchableSingleModule implements ManagerActivityInterface {
    public SingleModuleActivation activation;

    public RecharchableActivatableDurationSingleModule(ManagerContainer<?> var1) {
        super(var1);
    }

    public boolean executeModule() {
        if (this.getSegmentController().isOnServer()) {
            if (this.getCharges() > 0) {
                this.activation = new SingleModuleActivation();
                this.activation.startTime = System.currentTimeMillis();
                this.setCharge(0.0F);
                this.removeCharge();
                this.sendChargeUpdate();
                System.err.println("[SERVER][RECHARGE] ACTIVATE " + this.getWeaponRowName() + "; " + this.getSegmentController());
                return true;
            }

            System.err.println("[SERVER][RECHARGE] NO CHRAGES FOR ACTIVATE " + this.getWeaponRowName() + "; " + this.getSegmentController());
        }

        return false;
    }

    public void dischargeFully() {
        super.dischargeFully();
        this.deactivateManually();
    }

    protected void setActiveFromTag(boolean var1) {
        if (var1) {
            this.activation = new SingleModuleActivation();
            this.activation.startTime = System.currentTimeMillis();
        }

    }

    private void resetActivation() {
        this.activation = null;
    }

    protected void deactivateManually() {
        this.activation = null;
        this.sendChargeUpdate();
    }

    protected abstract boolean isDeactivatableManually();

    public void update(Timer var1) {
        super.update(var1);
        float var2;
        if (this.isActive() && (var2 = this.getDuration()) >= 0.0F && var1.currentTime - this.activation.startTime > (long)(var2 * 1000.0F)) {
            this.activation = null;
            System.err.println(this.getSegmentController().getState() + " " + this.getName() + " " + this.getSegmentController() + " DEACTIVATED BY DURATION TIMEOUT!");
            this.sendChargeUpdate();
        }

    }

    public long getTimeLeftMs() {
        long var1 = -1L;
        if (this.activation != null && this.getDuration() >= 0.0F) {
            long var3 = System.currentTimeMillis() - this.activation.startTime;
            long var5;
            if ((var5 = (long)(this.getDuration() * 1000.0F) - var3) > 0L) {
                var1 = var5;
            }
        }

        return var1;
    }

    public long getStarted() {
        return this.activation != null ? this.activation.startTime : 0L;
    }

    public void receivedActive(long var1) {
        if (var1 <= 0L) {
            this.activation = null;
        } else {
            if (this.activation == null) {
                this.activation = new SingleModuleActivation();
            }

            if (this.activation.startTime != var1) {
                this.activation.startTime = var1;
                System.err.println("[CLIENT][RECHARGE] " + ((GameClientState)this.getState()).getPlayerName() + " ACTIVATE " + this.getWeaponRowName() + "; " + this.getSegmentController());
            }

        }
    }

    public abstract float getDuration();

    public boolean isActive() {
        return this.activation != null;
    }

    public boolean isAutoChargeOn() {
        return super.isAutoChargeOn() && !this.isActive();
    }

    public ManagerActivityInterface getActivityInterface() {
        return this;
    }
}
