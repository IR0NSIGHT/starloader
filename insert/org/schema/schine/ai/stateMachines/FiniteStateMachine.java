//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.ai.stateMachines;

import api.listener.events.state.FSMStateEnterEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.schema.schine.ai.AiEntityStateInterface;
import org.schema.schine.ai.MachineProgram;

public abstract class FiniteStateMachine<E> {
    State currentState;
    private FSMclass fsm;
    private AiEntityStateInterface obj;
    private MachineProgram<?> robotProgram;
    private State startState;
    private Object2ObjectOpenHashMap<String, State> directStates;

    public FiniteStateMachine(AiEntityStateInterface var1, MachineProgram<?> var2, E var3, State var4) {
        this.obj = var1;
        this.setMachineProgram(var2);
        this.createFSM(var3);
        this.setStartingState(var4);
    }

    public FiniteStateMachine(AiEntityStateInterface var1, MachineProgram<?> var2, E var3) {
        assert var1 != null;

        this.obj = var1;
        this.setMachineProgram(var2);
        this.createFSM(var3);

        assert this.getStartState() != null;

    }

    public abstract void createFSM(E var1);

    public FSMclass getFsm() {
        return this.fsm;
    }

    public MachineProgram<?> getMachineProgram() {
        return this.robotProgram;
    }

    public void setMachineProgram(MachineProgram<?> var1) {
        this.robotProgram = var1;
    }

    public AiEntityStateInterface getObj() {
        return this.obj;
    }

    public abstract void onMsg(Message var1);

    public void setStartingState(State var1) {
        var1.initRecusively(this);
        var1.setMachineRecusively(this);
        this.setState(var1);
        this.startState = var1;
        this.fsm = new FSMclass(var1, this);
    }

    public void setState(State var1) {
        assert this.obj != null;

        this.currentState = var1;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public void update() throws FSMException {
        if (this.currentState == null) {
            throw new FSMException("[CRITICAL] no state set! please set the FiniteStateMachine.setStartState(State state) Method in createFSM()");
        } else {
            State s = this.currentState;
            if (s.isNewState()) {
                s.onEnter();
                s.setNewState(false);
                //INSERTED CODE @172
                FSMStateEnterEvent event = new FSMStateEnterEvent(this, s);
                StarLoader.fireEvent(event, true);
                ///
            } else {
                s.onUpdate();
            }
        }
    }

    public void init(State var1) {
        var1.init(this);
    }

    public void reset() {
        this.setState(this.startState);
        this.startState.setNewState(true);
    }

    public Object2ObjectOpenHashMap<String, State> getDirectStates() {
        return this.directStates;
    }

    public void setDirectStates(Object2ObjectOpenHashMap<String, State> var1) {
        this.directStates = var1;
    }

    public State getStartState() {
        return this.startState;
    }
}
