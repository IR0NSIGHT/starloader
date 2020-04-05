package api.gui.custom;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;

public class CustomHudText extends GUITextOverlay{
    //150, 10, FontLibrary.getBlenderProHeavy30(), Color.red, ev.getInputState()
    public CustomHudText(int x, int y, UnicodeFont unicodeFont, Color color, InputState inputState) {
        super(x, y, unicodeFont, color, inputState);
    }

}
