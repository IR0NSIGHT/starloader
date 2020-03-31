package api.entity;

import api.faction.Faction;
import api.systems.Reactor;
import api.systems.Shield;
import api.universe.Universe;
import api.utils.StarVector;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.SpaceStationManagerContainer;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.GlUtil;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Entity {
    public SegmentController internalEntity;
//    private double speed;
//    private double maxSpeed;
//    private boolean hasReactor;
//    private List<Reactor> reactors;
//    private Reactor activeReactor;
//    private boolean hasShields;
//    private List<Shield> shields;
//    private String name;
//    private Faction faction;
//    private Universe universe;
//    private EntityType entityType;

    public Entity(SegmentController controller) {
        internalEntity = controller;
    }

    public ArrayList<Ship> getDockedEntities(){
        ArrayList<SegmentController> collection = new ArrayList<>();
        internalEntity.railController.getDockedRecusive(collection);
        ArrayList<Ship> ships = new ArrayList<>();
        for (SegmentController controller : collection){
            if(controller instanceof org.schema.game.common.controller.Ship){
                ships.add(new Ship((org.schema.game.common.controller.Ship) controller));
            }
        }
        return ships;
    }
    public ArrayList<Shield> getShields(){
        ManagerContainer<?> manager = getManagerContainer();
        ArrayList<Shield> shields = new ArrayList<>();
        if(manager instanceof ShipManagerContainer){
            Collection<ShieldLocal> allShields = ((ShipManagerContainer) manager).getShieldAddOn().getShieldLocalAddOn().getAllShields();
            for (ShieldLocal sh : allShields){
                shields.add(new Shield(sh));
            }
            return shields;
        }else if(manager instanceof SpaceStationManagerContainer){
            //TODO implement other types of shields
        }else{
            //no shields
        }
        return shields;
    }
    private ManagerContainer<?> getManagerContainer(){
        return ((ManagedSegmentController<?>) internalEntity).getManagerContainer();
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

    public StarVector getVelocity(){
        return new StarVector(internalEntity.getPhysicsObject().getLinearVelocity(new Vector3f()));
    }
    public float getSpeed(){
        return getVelocity().length();
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
        EntityType entityType = null;
        switch(internalEntity.getType()){
            case SPACE_STATION: entityType = EntityType.STATION; break;
            case SHIP: entityType = EntityType.SHIP; break;
            case ASTEROID: entityType = EntityType.ASTEROID; break;
            case PLANET_CORE: entityType = EntityType.PLANETCORE; break;
            case PLANET_SEGMENT: entityType = EntityType.PLANETSEGMENT; break;
            case SHOP: entityType = EntityType.SHOP; break;
        }
        return entityType;
    }

    public boolean isOnServer(){
        return internalEntity.isOnServer();
    }
}
