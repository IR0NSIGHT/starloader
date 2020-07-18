//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.shipyard.orders.states;

import java.util.logging.Level;

import api.listener.events.state.ShipyardEnterStateEvent;
import api.mod.StarLoader;
import org.schema.common.LogUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.elements.shipyard.ShipyardCollectionManager;
import org.schema.game.common.controller.elements.shipyard.orders.ShipyardEntityState;
import org.schema.schine.ai.stateMachines.FSMException;
import org.schema.schine.ai.stateMachines.State;
import org.schema.schine.ai.stateMachines.Transition;

public abstract class ShipyardState extends State {
    private int ticksDone;
    private long startTime;
    public long loadedStartTime = -1L;
    public int loadedTicksDone = -1;
    private boolean loadedFromTag;

    public ShipyardState(ShipyardEntityState var1) {
        super(var1);
    }

    public ShipyardEntityState getEntityState() {
        return (ShipyardEntityState)super.getEntityState();
    }

    public void stateTransition(Transition var1) throws FSMException {
        if (ShipyardCollectionManager.DEBUG_MODE) {
            try {
                throw new Exception("!!!!DEBUG MODE!!!! AUTO EXCEPTION :: TRANSITION :: " + this.getClass().getSimpleName() + " using Transition " + var1.name());
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        try {
            throw new Exception("!!!!DEBUG MODE!!!! AUTO EXCEPTION :: TRANSITION :: " + this.getClass().getSimpleName() + " using Transition " + var1.name());
        } catch (Exception var3) {
            LogUtil.sy().log(Level.FINE, this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + " using Transition " + var1.name(), var3);
            super.stateTransition(var1);
        }
    }

    public abstract boolean onEnterS();

    public final boolean onEnter() {
        LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": ON_ENTER_STATE");
        if (this.loadedStartTime > 0L) {
            this.startTime = this.loadedStartTime;
            this.loadedStartTime = -1L;
        } else {
            this.startTime = System.currentTimeMillis();
        }

        if (this.loadedTicksDone >= 0) {
            this.ticksDone = this.loadedTicksDone;
            this.loadedTicksDone = -1;
        } else {
            this.ticksDone = 0;
        }

        this.getEntityState().setCompletionOrderPercentAndSendIfChanged(0.0D);
        boolean var1 = this.onEnterS();
        this.loadedFromTag = false;
        //INSERTED CODE @77
        StarLoader.fireEvent(new ShipyardEnterStateEvent(this), true);
        ///
        return var1;
    }

    private long getTickRate() {
        return 1000L;
    }

    public double getTickCount() {
        return (double)((System.currentTimeMillis() - this.startTime) / this.getTickRate());
    }

    public int getTicksDone() {
        int var1 = (int)this.getTickCount();
        int var2 = 0;
        if (var1 > this.ticksDone) {
            var2 = var1 - this.ticksDone;
            this.ticksDone = var1;
        }

        return var2;
    }

    public boolean canCancel() {
        return false;
    }

    public boolean isLoadedFromTag() {
        return this.loadedFromTag;
    }

    public void setLoadedFromTag(boolean var1) {
        this.loadedFromTag = var1;
    }

    public String getClientShortDescription() {
        return this.getClass().getSimpleName();
    }

    public boolean canEdit() {
        return false;
    }

    public boolean hasBlockGoal() {
        return false;
    }

    public boolean canUndock() {
        return false;
    }

    public boolean isPullingResources() {
        return false;
    }

    public void pullResources() {
    }

    public void onShipyardRemoved(Vector3i var1) {
    }

    public long getStartTime() {
        return this.startTime;
    }
}
