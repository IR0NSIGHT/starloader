package org.schema.game.client.view.mainmenu.custom;

import org.schema.game.client.controller.GameMainMenuController;
import org.schema.game.client.view.mainmenu.MainMenuInputDialog;
import org.schema.game.client.view.mainmenu.gui.effectconfig.GUIEffectConfigPanel;
import org.schema.game.client.view.mainmenu.gui.effectconfig.GUIEffectStat;
import org.schema.game.common.data.blockeffects.config.ConfigManagerInterface;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;

public class ModsListDialog extends MainMenuInputDialog {
    private final GUIEffectConfigPanel p;
    private GUIEffectStat stat;

    public ModsListDialog(GameMainMenuController var1, GUIEffectStat var2) {
        super(var1);
        this.p = new GUIEffectConfigPanel(var1, (ConfigManagerInterface)null, var2, this);
        this.p.onInit();
        this.stat = var2;
    }

    public void handleMouseEvent(MouseEvent var1) {
    }

    public GUIElement getInputPanel() {
        return this.p;
    }

    public void onDeactivate() {
        this.p.cleanUp();
    }

    public void update(Timer var1) {
        super.update(var1);
        this.stat.updateLocal(var1);
    }

    public boolean isInside() {
        return this.p.isInside();
    }
}
