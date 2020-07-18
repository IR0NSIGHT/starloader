//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.ai.stateMachines;

import api.listener.events.state.StateTransitionEvent;
import api.mod.StarLoader;
import org.schema.schine.ai.AiEntityStateInterface;

public abstract class State {
    protected AiEntityStateInterface gObj;
    private boolean newState = true;
    private FSMStateData stateData;
    private FiniteStateMachine machine;
    private boolean init = false;

    public State(AiEntityStateInterface var1) {
        this.gObj = var1;
        this.setStateData(new FSMStateData(this));
    }

    public boolean containsTransition(Transition var1) {
        return this.stateData.existsOutput(var1);
    }

    public State addTransition(Transition var1, State var2) {
        this.getStateData().addTransition(var1, var2);
        return this;
    }

    public State addTransition(Transition var1, State var2, int var3, Object var4) {
        this.getStateData().addTransition(var1, var2, var3, var4);
        return this;
    }

    public boolean equals(Object var1) {
        return var1 != null && this.getClass().equals(var1.getClass());
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public AiEntityStateInterface getEntityState() {
        return this.gObj;
    }

    public FSMStateData getStateData() {
        return this.stateData;
    }

    public void setStateData(FSMStateData var1) {
        this.stateData = var1;
    }

    public boolean isNewState() {
        return this.newState;
    }

    public void setNewState(boolean var1) {
        this.newState = var1;
    }

    public abstract boolean onEnter();

    public abstract boolean onExit();

    public abstract boolean onUpdate() throws FSMException;

    public boolean removeTransition(Transition var1) {
        return this.getStateData().removeTransition(var1);
    }

    public void stateTransition(Transition var1) throws FSMException {
        this.stateTransition(var1, 0);
    }

    public void stateTransition(Transition t, int subId) throws FSMException {
        assert this.getEntityState() != null;

        assert this.getMachine() != null;

        assert this.getMachine().getFsm() != null;

        this.getMachine().getFsm().stateTransition(t, subId);
        //INSERTED CODE @199
        StarLoader.fireEvent(new StateTransitionEvent(this, t, subId), true);
        ///
    }

    public void initRecusively(FiniteStateMachine var1) {
        if (!this.init) {
            this.init = true;
            var1.init(this);
            this.stateData.initRecusrively(var1);
        }

    }

    public void setMachineRecusively(FiniteStateMachine var1) {
        if (this.getMachine() == null) {
            this.setMachine(var1);
            this.stateData.setRecusrively(var1);
        }

    }

    public void init(FiniteStateMachine var1) {
    }

    public FiniteStateMachine getMachine() {
        return this.machine;
    }

    public void setMachine(FiniteStateMachine var1) {
        this.machine = var1;
    }

    public String getDescString() {
        return "";
    }
}
