package api.systems;

import org.schema.game.common.controller.elements.ShieldLocal;

public class Shield {
    ShieldLocal internalShield;
    public Shield(ShieldLocal local){
        internalShield = local;
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
}
