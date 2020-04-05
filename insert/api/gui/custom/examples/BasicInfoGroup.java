package api.gui.custom.examples;

import api.entity.Entity;
import api.listener.events.gui.HudCreateEvent;
import api.main.GameClient;
import api.utils.StarRunnable;

import java.util.ArrayList;

public class BasicInfoGroup {
    //6 BasicInfoPanels
    ArrayList<BasicInfoPanel> panels = new ArrayList<>();
    public BasicInfoGroup(HudCreateEvent ev){
        for (int i = 0; i < 4; i++) {
            BasicInfoPanel e = new BasicInfoPanel(ev);
            e.setPosition(330, 40+(i*50));

            panels.add(e);
        }
        new StarRunnable(){
            @Override
            public void run() {
                BasicInfoGroup.this.setEntity(GameClient.getCurrentEntity());
            }
        }.runTimer(5);
    }
    public void setEntity(Entity e){
        for (BasicInfoPanel panel : panels){
            panel.setEntity(e);
        }
    }
}
