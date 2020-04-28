package api.listener.events.calculate;

import api.entity.Entity;
import api.listener.events.Event;
import org.schema.game.common.controller.elements.power.reactor.PowerImplementation;

public class CurrentPowerCalculateEvent extends Event {
    public static int id = idLog++;
    private final PowerImplementation impl;
    private double power;
    private Entity entity;

    public CurrentPowerCalculateEvent(PowerImplementation impl, double power){
        this.entity = new Entity(impl.getSegmentController());
        this.impl = impl;
        this.power = power;
    }

    public void setPower(double power){
        this.power = power;
    }

    public void addPower(double power){
        this.power += power;
    }

    public PowerImplementation getImpl() {
        return impl;
    }

    public double getPower() {
        return power;
    }

    public Entity getEntity() {
        return entity;
    }
}
