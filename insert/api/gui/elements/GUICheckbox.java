package api.gui.elements;

import org.schema.schine.graphicsengine.core.settings.StateParameterNotFoundException;
import org.schema.schine.graphicsengine.forms.gui.GUICheckBox;
import org.schema.schine.input.InputState;

public class GUICheckbox extends GUIElementActivatable {

    private GUICheckBox internalCheckBox;

    public GUICheckbox(InputState inputState, int width, int height) {
        super(inputState, width, height);
        internalCheckBox = new GUICheckBox(inputState) {
            @Override
            protected void activate() throws StateParameterNotFoundException {

            }

            @Override
            protected void deactivate() throws StateParameterNotFoundException {

            }

            @Override
            protected boolean isActivated() {
                return false;
            }
        };

    }

    public GUICheckbox(InputState inputState, int width, int height, int posX, int posY) {
        super(inputState, width, height, posX, posY);
    }
}
