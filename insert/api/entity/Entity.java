package api.entity;

import api.faction.Faction;
import api.systems.Reactor;
import api.systems.Shield;
import api.universe.Universe;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.GlUtil;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.List;

public class Entity {
    public SegmentController internalEntity;

    public Entity(SegmentController controller) {
        internalEntity = controller;
    }

    public Faction getFaction() throws IOException {
        return new Faction(internalEntity.getFaction());
    }

    public void setFaction(Faction faction) {
        internalEntity.setFactionId(faction.getID());
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
