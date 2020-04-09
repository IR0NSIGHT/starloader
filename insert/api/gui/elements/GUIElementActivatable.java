package api.gui.elements;

import org.schema.schine.graphicsengine.core.settings.StateParameterNotFoundException;
import org.schema.schine.input.InputState;

public class GUIElementActivatable extends GUIElement {

    public GUIElementActivatable(InputState inputState, int width, int height) {
        super(inputState, width, height);

    }

    public GUIElementActivatable(InputState inputState, int width, int height, int posX, int posY) {
        super(inputState, width, height, posX, posY);

    }

    protected void activate() throws StateParameterNotFoundException {

    }

    protected void deactivate() throws StateParameterNotFoundException {

    }

    protected boolean isActivated() {
        return false;
    }
}
