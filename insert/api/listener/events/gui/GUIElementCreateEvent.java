package api.listener.events.gui;

import api.listener.events.Event;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.input.InputState;

public class GUIElementCreateEvent extends Event {

    private GUIElement guiElement;
    private InputState inputState;

    public GUIElementCreateEvent(GUIElement guiElement, InputState inputState) {
        this.guiElement = guiElement;
        this.inputState = inputState;
    }

    public GUIElement getGuiWindow() {
        return guiElement;
    }

    public InputState getInputState() {
        return inputState;
    }
}