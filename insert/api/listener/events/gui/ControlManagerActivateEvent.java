package api.listener.events.gui;

import api.listener.events.Event;
import org.schema.game.client.controller.manager.AbstractControlManager;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;

public class ControlManagerActivateEvent extends Event {

    private AbstractControlManager controlManager;
    private GUIElement guiPanel;

    public ControlManagerActivateEvent(AbstractControlManager controlManager, GUIElement guiPanel) {
        this.controlManager = controlManager;
        this.guiPanel = guiPanel;
    }

    public AbstractControlManager getControlManager() {
        return controlManager;
    }

    public GUIElement getGUIPanel() {
        return guiPanel;
    }
}