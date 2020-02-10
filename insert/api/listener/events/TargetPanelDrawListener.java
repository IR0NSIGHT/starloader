package api.listener.events;

import api.listener.type.ClientListener;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPanel;

@ClientListener
public interface TargetPanelDrawListener extends Listener {
    void onDraw(TargetPanel panel);
}
