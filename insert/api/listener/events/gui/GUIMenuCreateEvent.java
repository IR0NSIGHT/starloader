package api.listener.events.gui;

import api.listener.events.Event;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.input.InputState;

public class GUIMenuCreateEvent extends Event {

    private GUIMainWindow guiWindow;
    private InputState inputState;

    public GUIMenuCreateEvent(GUIMainWindow guiWindow, InputState inputState) {
        this.guiWindow = guiWindow;
        this.inputState = inputState;
    }

    public GUIMainWindow getGuiWindow() {
        return guiWindow;
    }

    public InputState getInputState() {
        return inputState;
    }
}