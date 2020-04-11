package api.systems;

import api.entity.Entity;
import org.schema.game.common.controller.elements.ShieldLocal;

public class Shield {

    private ShieldLocal internalShield;

    public Shield(ShieldLocal local){
        internalShield = local;
    }

    public ShieldLocal getInternalShield() {
        return internalShield;
    }

    public float getRadius() {
        return internalShield.radius;
    }

    public double getCurrentShields() {
        return internalShield.getShields();
    }

    public double getMaxCapacity() {
        return internalShield.getShieldCapacity();
    }

    public double getRegen() {
        return internalShield.rechargePerSecond;
    }

    public double getUpkeep() {
        return internalShield.getShieldUpkeep();
    }

    public boolean isDepleted() {
        return getCurrentShields() <= 0;
    }

    public boolean isActive() {
        return internalShield.active;
    }

    public float getPercent(){
        return internalShield.getPercentOne();
    }

    public Entity getEntity() {
        return new Entity(internalShield.shieldLocalAddOn.getSegmentController());
    }
}
