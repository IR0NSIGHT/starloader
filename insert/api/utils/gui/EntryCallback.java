package api.utils.gui;

import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;

public interface EntryCallback<T> {
    void on(SimpleGUIList self, T entry, MouseEvent event);
}
