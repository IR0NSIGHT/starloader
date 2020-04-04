package api.gui.custom;

import api.entity.Entity;
import api.systems.Shield;

public class EntityShieldBar extends CustomHudBar {

    public Entity entity;
    public void setEntity(Entity e){
        entity = e;
    }

    @Override
    public boolean drawBar() {
        return entity != null;
    }

    @Override
    public void create() {
        setGlowIntensity(100);
        setWidth(300);
        setPos(200,200,1);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public float getFilled() {
        Shield lastHitShield = entity.getLastHitShield();
        if(lastHitShield == null){
            return 0F;
        }
        return lastHitShield.getPercent();
    }

    @Override
    public String getText() {
        return "le shield";
    }
}
