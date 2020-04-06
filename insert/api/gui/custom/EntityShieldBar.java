package api.gui.custom;

import api.entity.Entity;
import api.main.GameClient;
import api.systems.Shield;
import org.schema.common.util.StringTools;

public abstract class EntityShieldBar extends CustomHudBar {

    public Entity entity;
    public void setEntity(Entity e){
        entity = e;
    }

    @Override
    public boolean drawBar() {
        return entity != null;
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
        Shield lastHitShield = entity.getLastHitShield();
        if(lastHitShield == null){
            return "Shields: N/A";
        }
        return "Shields: [" + StringTools.massFormat(lastHitShield.getCurrentShields())
                + " / " + StringTools.massFormat(lastHitShield.getMaxCapacity()) + "]";
    }
}
