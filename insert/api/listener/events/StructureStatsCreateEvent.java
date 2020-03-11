package api.listener.events;

import api.listener.helpers.StructureTab;
import org.schema.game.client.view.gui.advanced.tools.LabelResult;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.client.view.gui.advancedstats.AdvancedStructureStatsGUISGroup;
import org.schema.game.client.view.gui.advancedstats.AdvancedStructureStatsGeneral;
import org.schema.game.common.controller.SegmentController;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;

public class StructureStatsCreateEvent extends Event {
    public static int id = 3;
    private StructureTab tab;
    private AdvancedStructureStatsGUISGroup group;
    private GUIContentPane contentPane;
    private AdvancedStructureStatsGUISGroup guiObject;

    public StructureStatsCreateEvent(StructureTab tab, AdvancedStructureStatsGUISGroup group, GUIContentPane contentPane) {
        this.tab = tab;
        this.group = group;
        this.contentPane = contentPane;
    }

    public void addStatLabel(int priority, StatLabelResult result){
        group.addStatLabel(contentPane.getContent(0), 0, priority, result);
    }

    public SegmentController getCurrentShip(){
        return group.getSegCon();
    }

    public StructureTab getTab() {
        return tab;
    }

    public AdvancedStructureStatsGUISGroup getGroup() {
        return group;
    }

    public GUIContentPane getContentPane() {
        return contentPane;
    }
}
