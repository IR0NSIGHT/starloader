package api.entity;

import api.faction.Faction;
import api.systems.Reactor;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.GlUtil;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.List;

public class Entity {

    private SegmentController internalEntity;

    public Entity(SegmentController controller) {
        internalEntity = controller;
    }

    public Faction getFaction() throws IOException {
        return new Faction(internalEntity.getFaction());
    }

    public void setFaction(Faction faction) {
        internalEntity.setFactionId(faction.getID());
    }

    public String getName() {
        return internalEntity.getRealName();
    }

    public void setName(String name) {
        internalEntity.setRealName(name);
    }

    public float getMass() {
        return internalEntity.getMassWithDocks();
    }

    public void setMass(Float mass) {
        internalEntity.setMass(mass);
    }

    public float getMassWithoutDocks() {
        return internalEntity.getMass();
    }

    public float getSpeed() {
        if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
            return internalEntity.getSpeedCurrent();
        } else {
            return 0;
        }
    }

    public float getMaxSpeed() {
        if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
            return internalEntity.getMaxServerSpeed();
        } else {
            return 0;
        }
    }

    public float getHP() {
        return getReactor().getHp();
    }

    public float getMaxHP() {
        return getReactor().getMaxHp();
    }

    public float getMissileCapacity() {
        return internalEntity.getMissileCapacity();
    }

    public float getMissileCapacityMax() {
        return internalEntity.getMissileCapacityMax();
    }

    public Fleet getFleet() {
        Fleet fleet = new Fleet(internalEntity.getFleet());
        return fleet;
    }

    public Reactor getReactor() {
        //Todo:Construct a Reactor object from the internal ship's current active reactor
        return null;
    }

    public List<Reactor> getAllReactors() {
        //Todo:Construct a list of all Reactors from the internal ship
        return null;
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
        //(For some reason a Switch statement won't work here, I tried
        EntityType entityType = null;
        if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.SPACE_STATION) {
            entityType = EntityType.STATION;
        } else if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.SHIP) {
            entityType = EntityType.SHIP;
        } else if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.ASTEROID) {
            entityType = EntityType.ASTEROID;
        } else if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.PLANET_CORE) {
            entityType = EntityType.PLANETCORE;
        } else if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.PLANET_SEGMENT) {
            entityType = EntityType.PLANETSEGMENT;
        } else if(internalEntity.getType() == SimpleTransformableSendableObject.EntityType.SHOP) {
            entityType = EntityType.SHOP;
        }
        return entityType;
    }

    public boolean isOnServer(){
        return internalEntity.isOnServer();
    }
}