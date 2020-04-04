package api.gui.custom;

import api.element.block.Block;
import api.entity.Entity;
import api.main.GameClient;
import api.systems.Shield;
import org.schema.game.common.controller.SegmentController;

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
        setGlowIntensity(100000);
        setWidth(11111);
        setPos(200,200,1);
    }

    @Override
    public void onUpdate() {
        Entity currentEntity = GameClient.getCurrentEntity();
        this.setEntity(currentEntity);
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
