package api.entity;

import api.faction.Faction;
import api.systems.Reactor;
import api.systems.Shield;
import api.universe.Universe;
import org.schema.game.common.controller.SegmentController;
import org.schema.schine.graphicsengine.core.GlUtil;
import javax.vecmath.Vector3f;
import java.util.List;

public class Entity {
    public SegmentController internalEntity;
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
    private EntityType entityType;

    public Entity(SegmentController controller) {
        internalEntity = controller;
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

    public Vector3f getDirection(){
        return GlUtil.getForwardVector(new Vector3f(), internalEntity.getWorldTransform());
    }

    public Vector3f getVelocity(){
        return internalEntity.getPhysicsObject().getLinearVelocity(new Vector3f());
    }

    public void setVelocity(Vector3f direction) {
        if(getEntityType() != EntityType.STATION && getEntityType() != EntityType.SHOP && getEntityType() != EntityType.PLANETCORE) {
            //Stations, Shops, and Planet Cores shouldn't have velocity
            internalEntity.getPhysicsObject().setLinearVelocity(direction);
        }
    }

    public void playEffect(byte value){
        internalEntity.executeGraphicalEffectServer(value);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}
