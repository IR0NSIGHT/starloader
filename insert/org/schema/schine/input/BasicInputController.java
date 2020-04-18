//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.input;

import api.listener.events.KeyPressEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.schema.schine.common.TextAreaInput;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.Draggable;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.GUIUniqueExpandableInterface;
import org.schema.schine.graphicsengine.forms.gui.NoKeyboardInput;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContextPane;
import org.schema.schine.network.client.ClientState;
import org.schema.schine.network.client.DelayedDropDownSelectedChanged;
import org.schema.schine.network.client.GUICallbackController;

public class BasicInputController {
    private final List<DialogInterface> playerInputs = new ObjectArrayList();
    private final JoystickMappingFile joystick = new JoystickMappingFile();
    private final ObjectArrayFIFOQueue<DelayedDropDownSelectedChanged> delayedDropDowns = new ObjectArrayFIFOQueue();
    private boolean grabbigStoppedFlag;
    private boolean wasDisplayActive;
    private TextAreaInput lastSelectedInput;
    private final List<MouseEvent> mouseEvents = new ObjectArrayList();
    private final List<DialogInterface> deactivatedPlayerInputs = new ObjectArrayList();
    public static Object grabbedObjectLeftMouse;
    private TextAreaInput currentActiveField;
    private GUIUniqueExpandableInterface currentActiveDropdown;
    private final GUICallbackController guiCallbackController = new GUICallbackController();
    private Draggable draggable;
    private GUIContextPane currentContextPane;
    private GUIContextPane currentContextPaneDrawing;
    private long lastDeactivatedMenu;
    private List<DialogInterface> tmpInputs = new ObjectArrayList();
    private GUIElement lockedOnScrollBar;
    private short lockedOnScrollBarUpdateNum;

    public BasicInputController() {
    }

    public void updateInput(InputController var1, Timer var2) {
        while(!this.getDelayedDropDowns().isEmpty()) {
            ((DelayedDropDownSelectedChanged)this.getDelayedDropDowns().dequeue()).execute();
        }

        if (var1.beforeInputUpdate()) {
            this.joystick.updateInput();
            Keyboard.enableRepeatEvents(this.getCurrentActiveField() != null || !var1.getPlayerInputs().isEmpty() || var1.isChatActive());
            this.tmpInputs.addAll(var1.getPlayerInputs());

            int var3;
            for(var3 = 0; var3 < this.tmpInputs.size(); ++var3) {
                ((DialogInterface)this.tmpInputs.get(var3)).update(var2);
            }

            this.tmpInputs.clear();

            for(var3 = 0; var3 < var1.getPlayerInputs().size(); ++var3) {
                if (((DialogInterface)var1.getPlayerInputs().get(var3)).checkDeactivated()) {
                    ((DialogInterface)var1.getPlayerInputs().get(var3)).deactivate();
                    --var3;
                }
            }

            ObjectArrayList var10 = new ObjectArrayList();
            ObjectArrayList var9 = new ObjectArrayList();
            ObjectArrayList var4 = new ObjectArrayList();
            ObjectArrayList var5 = new ObjectArrayList();
            MouseEvent var7;
            if (Display.isActive()) {
                if (this.wasDisplayActive) {
                    while(Keyboard.next()) {
                        KeyboardEvent e;
                        (e = new KeyboardEvent()).charEvent = true;
                        e.charac = Keyboard.getEventCharacter();
                        e.key = Keyboard.getEventKey();
                        e.state = Keyboard.getEventKeyState();
                        //INSERTED CODE @109
                        StarLoader.fireEvent(KeyPressEvent.class, new KeyPressEvent(e));
                        ///
                        var10.add(e);
                    }

                    while(this.joystick.next()) {
                        try {
                            if (Controllers.isEventAxis()) {
                                this.joystick.axisEvent = true;
                            }

                            if (Controllers.isEventButton()) {
                                JoystickEvent var12 = new JoystickEvent();
                                var5.add(var12);
                                (var7 = new MouseEvent()).button = -1;
                                var7.state = false;
                                if (this.joystick.getLeftMouse().isDown()) {
                                    var7.button = 0;
                                } else if (this.joystick.getRightMouse().isDown()) {
                                    var7.button = 1;
                                }

                                if (var7.button >= 0) {
                                    var4.add(var7);
                                }
                            }
                        } catch (Exception var8) {
                            var8.printStackTrace();
                            if (var1.getState() instanceof ClientState) {
                                ((ClientState)var1.getState()).getController().alertMessage("Joystick/Gamepad error! Input not processed.");
                            }
                        }
                    }

                    var1.handleLocalMouseInput();
                    this.getMouseEvents().clear();
                    Mouse.poll();

                    while(Mouse.next()) {
                        MouseEvent var13;
                        (var13 = new MouseEvent()).button = Mouse.getEventButton();
                        var13.dx = Mouse.getEventDX();
                        var13.dy = Mouse.getEventDY();
                        var13.x = Mouse.getEventX();
                        var13.y = Mouse.getEventY();
                        var13.dWheel = Mouse.getEventDWheel();
                        var13.state = Mouse.getEventButtonState();
                        var9.add(var13);
                    }
                }
            } else {
                while(Keyboard.next()) {
                }

                while(Mouse.next()) {
                }
            }

            Iterator var14 = var10.iterator();

            while(var14.hasNext()) {
                KeyboardEvent var15 = (KeyboardEvent)var14.next();
                if (this.getCurrentActiveField() != null) {
                    if (Keyboard.getEventKey() != 1) {
                        Keyboard.enableRepeatEvents(true);
                        this.getCurrentActiveField().handleKeyEvent(var15);
                    } else {
                        Keyboard.enableRepeatEvents(false);
                        this.setCurrentActiveField((TextAreaInput)null);
                    }
                } else {
                    var1.handleKeyEvent(var15);
                }
            }

            for(var14 = var5.iterator(); var14.hasNext(); Controller.checkJoystick = false) {
                JoystickEvent var16 = (JoystickEvent)var14.next();
                var1.handleJoystickEventButton(var16);
                Controller.checkJoystick = true;
                if (this.getCurrentActiveField() != null) {
                    this.getCurrentActiveField().handleKeyEvent(var16);
                }
            }

            var14 = var4.iterator();

            while(var14.hasNext()) {
                var7 = (MouseEvent)var14.next();
                if (GLFrame.activeForInput) {
                    this.getMouseEvents().add(var7);
                    var1.handleMouseEvent(var7);
                }
            }

            this.getMouseEvents().clear();
            if (this.grabbigStoppedFlag) {
                if (this.getDragging() != null && !this.getDragging().isStickyDrag()) {
                    this.getDragging().reset();
                    this.setDragging((Draggable)null);
                }

                this.grabbigStoppedFlag = false;
            }

            boolean var17 = grabbedObjectLeftMouse != null;
            Iterator var18 = var9.iterator();

            MouseEvent var11;
            while(var18.hasNext()) {
                var11 = (MouseEvent)var18.next();
                this.getMouseEvents().add(var11);
                if (var17 && !Mouse.isButtonDown(0)) {
                    grabbedObjectLeftMouse = null;
                }

                if (var17 && !Mouse.isButtonDown(0)) {
                    grabbedObjectLeftMouse = null;
                } else {
                    var1.onMouseEvent(var11);
                }

                if (var11.pressedLeftMouse() || var11.pressedRightMouse()) {
                    this.setCurrentActiveField((TextAreaInput)null);
                    Keyboard.enableRepeatEvents(false);
                }

                if (GLFrame.activeForInput) {
                    this.getGuiCallbackController().execute(var11, var1.getState());
                }

                if (!var11.state) {
                    this.grabbigStoppedFlag = var11.button == 0;
                }
            }

            this.wasDisplayActive = Display.isActive();
            this.setCurrentActiveDropdown((GUIUniqueExpandableInterface)null);
            this.getGuiCallbackController().reset();
            var18 = var4.iterator();

            while(var18.hasNext()) {
                var11 = (MouseEvent)var18.next();
                var1.mouseButtonEventNetworktransmission(var11);
            }

            var18 = var9.iterator();

            while(var18.hasNext()) {
                var11 = (MouseEvent)var18.next();
                var1.mouseButtonEventNetworktransmission(var11);
            }

            if (this.lockedOnScrollBar != null && Math.abs(this.lockedOnScrollBar.getState().getNumberOfUpdate() - this.lockedOnScrollBarUpdateNum) > 2) {
                this.lockedOnScrollBar = null;
            }

        }
    }

    public JoystickMappingFile getJoystick() {
        return this.joystick;
    }

    public void initialize() {
        try {
            this.joystick.init();
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public ObjectArrayFIFOQueue<DelayedDropDownSelectedChanged> getDelayedDropDowns() {
        return this.delayedDropDowns;
    }

    public TextAreaInput getLastSelectedInput() {
        return this.lastSelectedInput;
    }

    public void setLastSelectedInput(TextAreaInput var1) {
        this.lastSelectedInput = var1;
    }

    public TextAreaInput getCurrentActiveField() {
        return this.currentActiveField;
    }

    public void setCurrentActiveField(TextAreaInput var1) {
        this.currentActiveField = var1;
    }

    public void setCurrentActiveDropdown(GUIUniqueExpandableInterface var1) {
        this.currentActiveDropdown = var1;
    }

    public GUIUniqueExpandableInterface getCurrentActiveDropdown() {
        return this.currentActiveDropdown;
    }

    public GUICallbackController getGuiCallbackController() {
        return this.guiCallbackController;
    }

    public List<MouseEvent> getMouseEvents() {
        return this.mouseEvents;
    }

    public Draggable getDragging() {
        return this.draggable;
    }

    public void setDragging(Draggable var1) {
        if (this.draggable != null) {
            this.draggable.reset();
        }

        assert var1 == null || var1.getType() != 0;

        this.draggable = var1;
    }

    public void handleGUIMouseEvent(GUIElement var1, int var2) {
        if (var1 instanceof GUIListElement) {
            ((GUIListElement)var1).setSelected(!((GUIListElement)var1).isSelected());
        }

    }

    public GUIContextPane getCurrentContextPane() {
        return this.currentContextPane;
    }

    public void setCurrentContextPane(GUIContextPane var1) {
        this.currentContextPane = var1;
    }

    public GUIContextPane getCurrentContextPaneDrawing() {
        return this.currentContextPaneDrawing;
    }

    public void setCurrentContextPaneDrawing(GUIContextPane var1) {
        this.currentContextPaneDrawing = var1;
    }

    public long getLastDeactivatedMenu() {
        return this.lastDeactivatedMenu;
    }

    public void setLastDeactivatedMenu(long var1) {
        this.lastDeactivatedMenu = var1;
    }

    public List<DialogInterface> getPlayerInputs() {
        return this.playerInputs;
    }

    public List<DialogInterface> getDeactivatedPlayerInputs() {
        return this.deactivatedPlayerInputs;
    }

    public boolean handleKeyEventInputPanels(KeyEventInterface var1) {
        int var2;
        DialogInterface var3;
        if ((var2 = this.getPlayerInputs().size()) > 0 && !((var3 = (DialogInterface)this.getPlayerInputs().get(var2 - 1)) instanceof NoKeyboardInput)) {
            var3.handleKeyEvent(var1);
            return true;
        } else {
            return false;
        }
    }

    public void drawDropdownAndContext() {
        GlUtil.glDisable(2929);
        if (this.getCurrentActiveDropdown() != null) {
            this.getCurrentActiveDropdown().drawExpanded();
        }

        if (this.getCurrentContextPane() != null) {
            this.setCurrentContextPaneDrawing(this.getCurrentContextPane());
            this.getCurrentContextPane().draw();
            this.setCurrentContextPaneDrawing((GUIContextPane)null);
        }

    }

    public boolean isScrollLockOn(GUIScrollablePanel var1) {
        return this.lockedOnScrollBar != null && this.lockedOnScrollBar != var1;
    }

    public void scrollLockOn(GUIElement var1) {
        this.lockedOnScrollBar = var1;
        this.lockedOnScrollBarUpdateNum = var1.getState().getNumberOfUpdate();
    }
}
