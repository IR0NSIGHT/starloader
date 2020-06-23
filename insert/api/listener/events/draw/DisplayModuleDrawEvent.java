package api.listener.events.draw;

import api.listener.events.Event;
import org.schema.game.client.view.SegmentDrawer;
import org.schema.game.client.view.textbox.AbstractTextBox;

public class DisplayModuleDrawEvent extends Event {


    private final SegmentDrawer.TextBoxSeg.TextBoxElement boxElement;
    private final AbstractTextBox abstractTextBox;

    public DisplayModuleDrawEvent(SegmentDrawer.TextBoxSeg.TextBoxElement boxElement, AbstractTextBox abstractTextBox) {
        this.boxElement = boxElement;
        this.abstractTextBox = abstractTextBox;
    }

    public SegmentDrawer.TextBoxSeg.TextBoxElement getBoxElement() {
        return boxElement;
    }

    public AbstractTextBox getAbstractTextBox() {
        return abstractTextBox;
    }
}
