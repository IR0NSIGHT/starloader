package api.gui.custom.examples;

import api.entity.Entity;
import api.gui.custom.EntityReactorBar;
import api.gui.custom.EntityShieldBar;
import api.listener.events.gui.HudCreateEvent;
import api.main.GameClient;
import api.utils.StarRunnable;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;

import javax.vecmath.Vector4f;
import java.awt.*;

public class BasicInfoPanel{
    EntityShieldBar bar;
    PilotElement pilot;
    ShipNameElement shipName;
    EntityReactorBar reactorBar;
    public BasicInfoPanel(HudCreateEvent ev){
        bar = new EntityShieldBar() {
            @Override
            public void create() {
                setColor(new Color(255, 230,0, 200));

            }

            @Override
            public void onUpdate() {
            }

        };
        pilot = new PilotElement(FontLibrary.getBlenderProMedium16(), ev.getInputState());
        reactorBar = new EntityReactorBar() {
            @Override
            public void create() {
                setColor(new Color(0, 255, 121, 200));
            }

            @Override
            public void onUpdate() {
            }
        };
        shipName = new ShipNameElement(FontLibrary.getBlenderProMedium13(), ev.getInputState());

        ev.addElement(bar);
        ev.addElement(pilot);
        ev.addElement(reactorBar);
        ev.addElement(shipName);
//        new StarRunnable(){
//            @Override
//            public void run() {
//                BasicInfoPanel.this.setEntity(GameClient.getCurrentEntity());
//            }
//        }.runTimer(5);
    }
    public void setEntity(Entity e){
        pilot.setEntity(e);
        shipName.setEntity(e);
        bar.setEntity(e);
        reactorBar.setEntity(e);
    }
    public void setPosition(int x, int y){
        pilot.getPos().set(new float[]{x-20, y+6,1});
        reactorBar.getPos().set(new float[]{x+40,y,1});
        bar.getPos().set(new float[]{x+300,y,1});
        shipName.getPos().set(new float[]{x+560,y+5,1});
    }

}
