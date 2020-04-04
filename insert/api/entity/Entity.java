package api.entity;

import api.element.block.Block;
import api.element.block.Blocks;
import api.faction.Faction;
import api.inventory.ItemStack;
import api.systems.Reactor;
import api.systems.Shield;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.*;
import org.schema.game.common.controller.elements.power.reactor.MainReactorUnit;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.schine.graphicsengine.core.GlUtil;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.*;

public class Entity {
    public SegmentController internalEntity;

    public Entity(SegmentController controller) {
        internalEntity = controller;
    }

    public Faction getFaction() throws IOException {
        /**
         * Gets the faction the entity is currently part of. Returns null if the entity has no faction.
         */
        if(internalEntity.isInExitingFaction()) {
            return new Faction(internalEntity.getFaction());
        }
        return null;
    }

    public void setFaction(Faction faction) {
        /**
         * Sets the entity's faction.
         */
        internalEntity.setFactionId(faction.getID());
    }

    public Vector3f getDirection() {
        /**
         * Gets a Vector3f of the entity's current direction.
         */
        return GlUtil.getForwardVector(new Vector3f(), internalEntity.getWorldTransform());
    }

    public Vector3f getVelocity() {
        /**
         * Gets a Vector3f of the entity's current velocity.
         */
        return internalEntity.getPhysicsObject().getLinearVelocity(new Vector3f());
    }

    public String getName() {
        /**
         * Gets the entity's current name.
         */
        return internalEntity.getName();
    }

    public void setName(String name) {
        /**
         * Sets the entity's name.
         */
        internalEntity.setRealName(name);
    }

    public float getMass() {
        /**
         * Gets the entity's total mass including docked entities.
         */
        return internalEntity.getMassWithDocks();
    }

    public void setMass(Float mass) {
        /**
         * Sets the entity's mass. Doesn't change the mass of any docked entities.
         */
        internalEntity.setMass(mass);
    }

    public float getMassWithoutDocks() {
        /**
         * Gets the entity's mass without docks.
         */
        return internalEntity.getMass();
    }

    public float getSpeed() {
        /**
         * Gets the entity's current speed. Returns 0 if the entity is immobile.
         */
        if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
            return internalEntity.getSpeedCurrent();
        } else {
            return 0;
        }
    }

    public float getMaxSpeed() {
        /**
         * Gets the entity's max speed. Returns 0 if the entity is immobile.
         */
        if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
            return internalEntity.getMaxServerSpeed();
        } else {
            return 0;
        }
    }

    public float getHP() {
        /**
         * Gets the entity's current Reactor HP.
         */
        return getCurrentReactor().getHp();
    }

    public float getMaxHP() {
        /**
         * Gets the entity's maximum Reactor HP.
         */
        return getCurrentReactor().getMaxHp();
    }

    public float getMissileCapacity() {
        /**
         * Gets the entity's current missile capacity. Doesn't include the capacity of whatever the entity is docked to.
         */
        return internalEntity.getMissileCapacity();
    }

    public float getMissileCapacityMax() {
        /**
         * Gets the entity's maximum missile capacity. Doesn't include the capacity of whatever the entity is docked to.
         */
        return internalEntity.getMissileCapacityMax();
    }

    public boolean hasAnyReactors() {
        /**
         * Checks if the entity has any reactors.
         */
        return internalEntity.hasAnyReactors();
    }

    public Reactor getCurrentReactor() {
        /**
         * Gets the entity's currently active Reactor. Returns null if the entity doesn't have any reactors.
         */
        if(hasAnyReactors()) {
            ManagerContainer<?> manager = getManagerContainer();
            if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.STATION)) {
                if(manager instanceof ShipManagerContainer) {
                    return new Reactor(((ShipManagerContainer) manager).getPowerInterface().getActiveReactor());
                } else if(manager instanceof SpaceStationManagerContainer) {
                    return new Reactor(((SpaceStationManagerContainer) manager).getPowerInterface().getActiveReactor());
                }
            }
        }
        return null;
    }

    public Reactor getReactor(int i) {
        /**
         * Gets the specified reactor from the entity. Returns null if the entity doesn't have any reactors.
         */
        if(hasAnyReactors()) {
            return getReactors().get(i);
        }
        return null;
    }

    public ArrayList<Reactor> getReactors() {
        /**
         * Gets an ArrayList of all the entity's reactors. Returns null if the entity doesn't have any reactors.
         */
        if(hasAnyReactors()) {
            ManagerContainer<?> manager = getManagerContainer();
            ArrayList<Reactor> reactors = new ArrayList<>();
            if(getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.STATION)) {
                if(manager instanceof ShipManagerContainer) {
                    List<MainReactorUnit> allReactors = ((ShipManagerContainer) manager).getPowerInterface().getMainReactors();
                    for (MainReactorUnit reactorUnit : allReactors) {
                        reactors.add(new Reactor(reactorUnit.getPowerInterface().getActiveReactor()));
                    }
                } else if(manager instanceof SpaceStationManagerContainer) {
                    List<MainReactorUnit> allReactors = ((SpaceStationManagerContainer) manager).getPowerInterface().getMainReactors();
                    for(MainReactorUnit reactorUnit : allReactors) {
                        reactors.add(new Reactor(reactorUnit.getPowerInterface().getActiveReactor()));
                    }
                }
            }
            return reactors;
        }
        return null;
    }

    public void setVelocity(Vector3f direction) {
        /**
         * Sets the entity's velocity. Doesn't do anything if the entity is immobile.
         */
        if(getEntityType() != EntityType.STATION && getEntityType() != EntityType.SHOP && getEntityType() != EntityType.PLANETCORE) {
            //Stations, Shops, and Planet Cores shouldn't have velocity
            internalEntity.getPhysicsObject().setLinearVelocity(direction);
        }
    }

    public void playEffect(byte value) {
        /**
         * Plays the specified graphical effect on the entity.
         */
        internalEntity.executeGraphicalEffectServer(value);
    }

    public ArrayList<Ship> getDockedEntities() {
        /**
         * Gets an ArrayList of ships currently docked to this entity.
         */
        ArrayList<SegmentController> collection = new ArrayList<>();
        internalEntity.railController.getDockedRecusive(collection);
        ArrayList<Ship> ships = new ArrayList<>();
        for (SegmentController controller : collection) {
            if(controller instanceof org.schema.game.common.controller.Ship){
                ships.add(new Ship((org.schema.game.common.controller.Ship) controller));
            }
        }
        return ships;
    }

    public Shield getShield(int i) {
        /**
         * Gets the entity's specified shield. Returns null if the entity is not a ship or space station.
         */
        return getShields().get(i);
    }

    public ArrayList<Shield> getShields() {
        /**
         * Gets an ArrayList of all the entity's shields. Returns null if the entity is not a ship or space station.
         */
        ManagerContainer<?> manager = getManagerContainer();
        ArrayList<Shield> shields = new ArrayList<>();
        if(manager instanceof ShieldContainerInterface) {
            Collection<ShieldLocal> allShields = ((ShieldContainerInterface) manager).getShieldAddOn().getShieldLocalAddOn().getAllShields();
            for(ShieldLocal sh : allShields) {
                shields.add(new Shield(sh));
            }
            return shields;
        }
        return null;
    }

    public ManagerContainer<?> getManagerContainer() {
        return ((ManagedSegmentController<?>) internalEntity).getManagerContainer();
    }

    public EntityType getEntityType() {
        /**
         * Gets the entity's type.
         */
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

    public boolean isOnServer() { //Does this need to be public?
        return internalEntity.isOnServer();
    }

    public int getBlockAmount(Blocks block) {
        /**
         * Gets how many of the specified block the entity has. Does not include docked or root entities.
         */
        return internalEntity.getElementClassCountMap().get(block.getId());
    }


    public HashMap<Blocks, Integer> getBlocks() {
        /**
         * Gets a Map of every block the entity has and how many of each are present. Does not include docked or root entities.
         */
        HashMap<Blocks, Integer> blocks = new HashMap<>();

        for(Blocks value : Blocks.values()) {
            blocks.put(value, getBlockAmount(value));
        }
        return blocks;
    }

    public Shield getLastHitShield() {
        ManagerContainer<?> manager = getManagerContainer();
        if(manager instanceof ShieldContainerInterface){
            ShieldLocal lastHitShield = ((ShieldContainerInterface) manager).getShieldAddOn().getShieldLocalAddOn().getLastHitShield();
            if(lastHitShield == null){
                return null;
            }
            return new Shield(lastHitShield);
        }
        return null;
    }
}
