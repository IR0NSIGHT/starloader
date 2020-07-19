package api.listener.events;

import api.listener.type.ClientEvent;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;

@Deprecated
public class TargetPanelDrawEvent extends Event {
    private TargetPanel panel;

    public TargetPanelDrawEvent(TargetPanel panel){

        this.panel = panel;
    }

    public TargetPanel getPanel() {
        return panel;
    }
}
