package api;

import org.schema.game.client.view.gui.faction.newfaction.FactionPanelNew;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.input.InputState;

public class MyFactionPanelNew extends FactionPanelNew {
    public MyFactionPanelNew(InputState inputState) {
        super(inputState);
    }
    private GUIContentPane tab;
    @Override
    public void recreateTabs() {
        super.recreateTabs();
        tab = this.factionPanel.addTab("Sieged Systems");
        createMyPane();
    }
    public void createMyPane() {
        GUITextOverlay var1;
        (var1 = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium20(), this.getState())).setTextSimple(this.getOwnFaction().getName() + ", ");

        this.tab.setTextBoxHeightLast(110);
        this.tab.addNewTextBox(10);
        var1.setPos(4.0F, 4.0F, 0.0F);
        this.tab.getContent(0).attach(var1);
    }
}
