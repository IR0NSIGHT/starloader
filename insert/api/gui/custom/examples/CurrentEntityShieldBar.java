package api.gui.custom.examples;

import api.entity.Entity;
import api.gui.custom.EntityShieldBar;
import api.main.GameClient;

public class CurrentEntityShieldBar extends EntityShieldBar {
    @Override
    public void create() {
        setPos(200,200,1);
    }

    float xPos = 200;
    float yPos = 200;
    float r = 0;
    @Override
    public void onUpdate() {
        Entity currentEntity = GameClient.getCurrentEntity();
        this.setEntity(currentEntity);
//        r+= 0.05;
//        float x = (float) (xPos+Math.sin(r)*100);
//        float y = (float) (yPos+Math.cos(r)*100);
//        setPos(x, y, 1);

    }
}
