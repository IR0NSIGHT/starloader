//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.controller.manager;

import api.listener.events.gui.ControlManagerActivateEvent;
import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.vecmath.Vector3f;
import org.schema.game.client.controller.PlayerGameDropDownInput;
import org.schema.game.client.controller.PlayerGameOkCancelInput;
import org.schema.game.client.controller.PlayerInput;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.SimplePlayerCommands;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.common.InputHandler;
import org.schema.schine.common.JoystickInputHandler;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.AbstractScene;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.Suspendable;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.input.JoystickEvent;
import org.schema.schine.input.KeyEventInterface;

public abstract class AbstractControlManager extends Observable implements InputHandler, Suspendable {
    public static final int CONTROLLER_PLAYER_EXTERN = 0;
    public static final int CONTROLLER_SHIP_EXTERN = 1;
    public static final int CONTROLLER_SHIP_BUILD = 2;
    private final GameClientState state;
    private boolean suspended = false;
    private boolean active;
    private HashSet<AbstractControlManager> controlManagers;
    private int delayedActive = -1;
    private boolean treeActive;
    private long suspentionFreedTime;
    private int hinderInteraction;
    private long hinderInteractionStart;

    public AbstractControlManager(GameClientState var1) {
        this.state = var1;
        this.setControlManagers(new HashSet());
    }

    public boolean isAllowedToBuildAndSpawnShips() {
        Faction var1;
        return (var1 = this.getState().getFactionManager().getFaction(this.getState().getPlayer().getFactionId())) == null || !var1.isFactionMode(4);
    }

    public void activate(AbstractControlManager var1) {
        Iterator var2 = this.getControlManagers().iterator();

        while(var2.hasNext()) {
            AbstractControlManager var3;
            if ((var3 = (AbstractControlManager)var2.next()) != var1) {
                var3.setActive(false);
            }
        }

        if (!var1.isActive()) {
            var1.setActive(true);
        }

    }

    public void openExitTutorialPanel(final PlayerInput var1) {
        (new PlayerGameOkCancelInput("AbstractControlManager_TUTORIALS_END", this.getState(), "Confirm", "Do you really want to end the tutorial\nYou will be teleported back into the game \nand get your inventory back!") {
            public boolean isOccluded() {
                return false;
            }

            public void onDeactivate() {
            }

            public void pressedOK() {
                if (this.getState().getPlayer().getFirstControlledTransformableWOExc() != null && this.getState().getPlayer().getFirstControlledTransformableWOExc() instanceof PlayerCharacter) {
                    this.getState().getPlayer().sendSimpleCommand(SimplePlayerCommands.END_TUTORIAL, new Object[0]);
                    Vector3f[] var1x = new Vector3f[]{new Vector3f((Float)ServerConfig.DEFAULT_SPAWN_POINT_X_1.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Y_1.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Z_1.getCurrentState()), new Vector3f((Float)ServerConfig.DEFAULT_SPAWN_POINT_X_2.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Y_2.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Z_2.getCurrentState()), new Vector3f((Float)ServerConfig.DEFAULT_SPAWN_POINT_X_3.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Y_3.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Z_3.getCurrentState()), new Vector3f((Float)ServerConfig.DEFAULT_SPAWN_POINT_X_4.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Y_4.getCurrentState(), (Float)ServerConfig.DEFAULT_SPAWN_POINT_Z_4.getCurrentState())};
                    Transform var2;
                    (var2 = new Transform()).setIdentity();
                    Random var3 = new Random();
                    var2.origin.set(var1x[var3.nextInt(var1x.length)]);
                    this.getState().getCharacter().getGhostObject().setWorldTransform(var2);
                    this.deactivate();
                    if (var1 != null) {
                        var1.deactivate();
                    }

                } else {
                    this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_MANAGER_ABSTRACTCONTROLMANAGER_0, 0.0F);
                }
            }
        }).activate();
    }

    public void openExitTestSectorPanel(final PlayerInput var1) {
        (new PlayerGameOkCancelInput("AbstractControlManager_TUTORIALS_END", this.getState(), "Confirm", "Do you really want to exit the test sector?\nYou will be teleported back into the game \nand get your original inventory back!") {
            public boolean isOccluded() {
                return false;
            }

            public void onDeactivate() {
            }

            public void pressedOK() {
                if (this.getState().getPlayer().getFirstControlledTransformableWOExc() != null && this.getState().getPlayer().getFirstControlledTransformableWOExc() instanceof PlayerCharacter) {
                    this.getState().getPlayer().sendSimpleCommand(SimplePlayerCommands.END_SHIPYARD_TEST, new Object[0]);
                    this.deactivate();
                    if (var1 != null) {
                        var1.deactivate();
                    }

                } else {
                    this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_MANAGER_ABSTRACTCONTROLMANAGER_1, 0.0F);
                }
            }
        }).activate();
    }

    public void openTutorialPanel() {
        ObjectArrayList var1 = new ObjectArrayList();
        ObjectArrayList var2 = new ObjectArrayList(this.getState().getController().getTutorialMode().getMachineNames());

        for(int var3 = 0; var3 < var2.size(); ++var3) {
            if (((String)var2.get(var3)).startsWith("TutorialBasics")) {
                var2.remove(var3);
                --var3;
            }
        }

        Collections.sort(var2);
        Iterator var7 = var2.iterator();

        while(var7.hasNext()) {
            String var6;
            if (!(var6 = (String)var7.next()).equals("Basics Tutorial")) {
                GUIAncor var4 = new GUIAncor(this.getState(), 300.0F, 24.0F);
                GUITextOverlay var5;
                (var5 = new GUITextOverlay(300, 24, this.getState())).setTextSimple(var6);
                var5.setPos(5.0F, 4.0F, 0.0F);
                var4.attach(var5);
                var1.add(var4);
                var4.setUserPointer(var6);
            }
        }

        (new PlayerGameDropDownInput("AbstractControlManager_TUTORIALS", this.getState(), "Tutorials", 24, "Choose a tutorial", var1) {
            public void onDeactivate() {
            }

            public boolean isOccluded() {
                return false;
            }

            public void pressedOK(GUIListElement var1) {
                this.getState().getController().getTutorialMode().setCurrentMachine(var1.getContent().getUserPointer().toString());
                this.getState().getController().getTutorialMode().getMachine().reset();
                this.deactivate();
            }
        }).activate();
    }

    public void activateAll(boolean var1) {
        Iterator var2 = this.getControlManagers().iterator();

        while(var2.hasNext()) {
            ((AbstractControlManager)var2.next()).setActive(var1);
        }

    }

    public void activateDelayed() {
        if (this.delayedActive >= 0) {
            this.setActive(this.delayedActive == 1);
        }

        this.delayedActive = -1;
        Iterator var1 = this.getControlManagers().iterator();

        while(var1.hasNext()) {
            ((AbstractControlManager)var1.next()).activateDelayed();
        }

    }

    public synchronized void deleteObserver(Observer var1) {
        super.deleteObserver(var1);
    }

    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    public HashSet<AbstractControlManager> getControlManagers() {
        return this.controlManagers;
    }

    public void setControlManagers(HashSet<AbstractControlManager> var1) {
        this.controlManagers = var1;
    }

    public GameClientState getState() {
        return this.state;
    }

    public long getSuspentionFreedTime() {
        return this.suspentionFreedTime;
    }

    public boolean handleJoystickInputPanels(JoystickEvent var1) {
        synchronized(this.getState().getController().getPlayerInputs()) {
            int var3;
            DialogInterface var5;
            if ((var3 = this.getState().getController().getPlayerInputs().size()) > 0 && (var5 = (DialogInterface)this.getState().getController().getPlayerInputs().get(var3 - 1)) instanceof JoystickInputHandler) {
                ((JoystickInputHandler)var5).handleJoystickEvent(var1);
                return true;
            }
        }

        return false;
    }

    public void handleKeyEvent(KeyEventInterface var1) {
        if (!this.isSuspended() && !this.isHinderedInteraction()) {
            Iterator var2 = this.getControlManagers().iterator();

            while(var2.hasNext()) {
                AbstractControlManager var3;
                if (!(var3 = (AbstractControlManager)var2.next()).isSuspended() && var3.isActive()) {
                    var3.handleKeyEvent(var1);
                }
            }
        }

    }

    public void handleMouseEvent(MouseEvent var1) {
        if (!this.isSuspended() && !this.isHinderedInteraction()) {
            Iterator var2 = this.getControlManagers().iterator();

            while(var2.hasNext()) {
                AbstractControlManager var3;
                if (!(var3 = (AbstractControlManager)var2.next()).isSuspended() && var3.isActive()) {
                    var3.handleMouseEvent(var1);
                }
            }
        }

    }

    public void hinderInteraction(int var1) {
        this.hinderInteraction = var1;
        this.hinderInteractionStart = System.currentTimeMillis();
        this.state.setHinderedInput(var1);
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isHinderedInteraction() {
        return System.currentTimeMillis() - this.hinderInteractionStart < (long)this.hinderInteraction;
    }

    public boolean isSuspended() {
        return this.suspended;
    }

    public boolean isTreeActive() {
        return this.treeActive;
    }

    public void suspend(boolean var1) {
        if (var1 != this.suspended) {
            this.onSuspend(var1);
            this.suspended = var1;
        }

        if (!var1) {
            this.suspentionFreedTime = System.currentTimeMillis();
        }

    }

    public void setActive(boolean active) {
        boolean var10000 = this.active;
        boolean var2 = active != this.active;
        this.active = active;
        //INSERTED CODE @378
        ControlManagerActivateEvent controlManagerActivateEvent = new ControlManagerActivateEvent(this, active);
        StarLoader.fireEvent(ControlManagerActivateEvent.class, controlManagerActivateEvent, false);
        if(controlManagerActivateEvent.isCanceled()){
            this.active = !this.active;
        }
        ///
        if (var2) {
            this.onSwitch(active);
            if (!this.state.isPassive()) {
                this.getState().getWorldDrawer().getGuiDrawer().notifySwitch(this);
            }

            this.setChanged();
            this.notifyObservers("ON_SWITCH");
        }

    }

    public boolean isDelayedActive() {
        return this.delayedActive >= 0;
    }

    public void setDelayedActive(int var1) {
        this.delayedActive = var1;
    }

    public void onSuspend(boolean var1) {
        this.getState().getPlayer().getControllerState().setSuspended(var1);
    }

    public void onSwitch(boolean var1) {
        this.treeActive = var1;
        Iterator var2 = this.getControlManagers().iterator();

        while(var2.hasNext()) {
            AbstractControlManager var3;
            (var3 = (AbstractControlManager)var2.next()).onSwitch(var1 && var3.isActive());
        }

    }

    public void printActive(int var1) {
        if (this.isActive()) {
            String var2 = "";

            for(int var3 = 0; var3 < var1; ++var3) {
                var2 = var2 + "->";
            }

            var2 = var2 + this.getClass().getSimpleName();
            AbstractScene.infoList.add("|-- " + var2);
            Iterator var4 = this.getControlManagers().iterator();

            while(var4.hasNext()) {
                ((AbstractControlManager)var4.next()).printActive(var1 + 1);
            }
        }

    }

    public boolean isTreeActiveAndNotSuspended() {
        return this.isTreeActive() && !this.isSuspended();
    }

    public void setDelayedActive(boolean var1) {
        this.delayedActive = var1 ? 1 : 0;
    }

    public void update(Timer var1) {
        if (!this.isSuspended()) {
            Iterator var2 = this.getControlManagers().iterator();

            while(var2.hasNext()) {
                AbstractControlManager var3;
                if (!(var3 = (AbstractControlManager)var2.next()).isSuspended() && var3.isActive()) {
                    var3.update(var1);
                }
            }
        }

    }
}
