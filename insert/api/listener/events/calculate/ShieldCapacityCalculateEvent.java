package api.listener.events.calculate;

import api.listener.events.Event;
import api.systems.Shield;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityCollectionManager;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityUnit;

import java.util.ArrayList;

public class ShieldCapacityCalculateEvent extends Event {
    public static int id = idLog++;
    private final ShieldCapacityUnit unit;
    private final Shield shield;
    private double capacity;

    public ShieldCapacityCalculateEvent(ShieldCapacityUnit unit, ShieldLocal local, double capacity) {
        this.unit = unit;
        this.shield = new Shield(local);
        this.capacity = capacity;
    }

    public void setShields(long shields) {
        capacity = shields;
    }

    public void addShields(long shields) {
        capacity += shields;
    }

    public void subtractShields(long shields) {
        capacity -= shields;
    }

    public ShieldCapacityUnit getUnit() {
        return unit;
    }

    public Shield getShield() {
        return shield;
    }

    public double getCapacity() {
        return capacity;
    }
}
