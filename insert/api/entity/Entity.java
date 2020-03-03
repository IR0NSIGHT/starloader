package api.entity;

import api.faction.Faction;
import api.systems.Reactor;
import api.systems.Shield;
import api.universe.Universe;
import java.util.List;

public class Entity {

    private List<Ship> dockedEntities;
    private double speed;
    private double maxSpeed;
    private boolean hasReactor;
    private List<Reactor> reactors;
    private Reactor activeReactor;
    private boolean hasShields;
    private List<Shield> shields;
    private String name;
    private Faction faction;
    private Universe universe;
    private Fleet fleet;

    public Entity() {

    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public Universe getUniverse() {
        return universe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public List<Ship> getDockedEntities() {
        return dockedEntities;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean hasReactor() {
        return hasReactor;
    }

    public void setHasReactor(boolean hasReactor) {
        this.hasReactor = hasReactor;
    }

    public boolean hasShields() {
        return hasShields;
    }

    public void setHasShields(boolean hasShields) {
        this.hasShields = hasShields;
    }

    public List<Reactor> getReactors() {
        return reactors;
    }

    public void setReactors(List<Reactor> reactors) {
        this.reactors = reactors;
    }

    public Reactor getActiveReactor() {
        return activeReactor;
    }

    public void setActiveReactor(Reactor activeReactor) {
        this.activeReactor = activeReactor;
    }

    public List<Shield> getShields() {
        return shields;
    }

    public void setShields(List<Shield> shields) {
        this.shields = shields;
    }
}
