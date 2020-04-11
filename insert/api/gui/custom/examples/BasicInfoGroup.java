package api.gui.custom.examples;

import api.entity.Entity;
import api.entity.EntityType;
import api.entity.Ship;
import api.listener.events.gui.HudCreateEvent;
import api.main.GameClient;
import api.server.Server;
import api.systems.Reactor;
import api.utils.StarRunnable;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamCollectionManager;

import java.util.ArrayList;

public class BasicInfoGroup {
    //6 BasicInfoPanels
    ArrayList<BasicInfoPanel> panels = new ArrayList<>();
    public BasicInfoGroup(HudCreateEvent ev){
        for (int i = 0; i < 6; i++) {
            BasicInfoPanel e = new BasicInfoPanel(ev);
            e.setPosition(330, 40+(i*30));

            panels.add(e);
        }
        new StarRunnable(){
            @Override
            public void run() {
                ArrayList<Ship> controlledShips = new ArrayList<>();
                for(Entity en : GameClient.getNearbyEntities()) {
                    if(en.getEntityType() == EntityType.SHIP){
                        Ship ship = en.toShip();
                        if(!ship.isDocked()) {
                             if(en.getAttachedPlayers().size() > 0){
                            //if(!en.getAttachedPlayers().get(0).getName().equals(GameClient.getClientPlayerState().getName())){
                            controlledShips.add(ship);
                            //}
                            }
                        }
                    }
                }
                for (BasicInfoPanel panel : panels) {
                    panel.setEntity(null);
                }
                for (int i = 0; i < controlledShips.size(); i++) {
                    if(i > 5){
                        break;
                    }
                    panels.get(i).setEntity(controlledShips.get(i));
                }
                //BasicInfoGroup.this.setEntity(GameClient.getCurrentEntity());
            }
        }.runTimer(50);
    }
    public void setEntity(Entity e){
        for (BasicInfoPanel panel : panels){
            panel.setEntity(e);
        }
    }
}
