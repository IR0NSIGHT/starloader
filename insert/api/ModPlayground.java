package api;

import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.KeyPressEvent;
import api.listener.events.StructureStatsCreateEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
    }

    @Override
    public void onEnable() {

        //This is to register a new listener to listen for StructureStatsCreateEvent
        StarLoader.registerListener(StructureStatsCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                final StructureStatsCreateEvent e = ((StructureStatsCreateEvent) event);
                e.addStatLabel(20, new StatLabelResult() {
                    @Override
                    public String getValue() {
                        //SegmentControllers are ships or stations.
                        //It's going to take a while to get used to naming, dont worry about it.
                        SegmentController ship = e.getCurrentShip();
                        if(ship != null){
                            return String.valueOf(ship.railController.getDockedCount());
                        }
                        return "No ship";
                    }

                    @Override
                    public int getStatDistance() {
                        //Controls how far to the right it is.
                        return 30;
                    }

                    @Override
                    public String getName() {
                        return "Docked Entities: ";
                    }
                });
            }
        });
    }
}
