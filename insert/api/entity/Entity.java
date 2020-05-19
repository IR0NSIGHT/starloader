package api.entity;

import api.element.block.Block;
import api.element.block.Blocks;
import api.faction.Faction;
import api.main.GameServer;
import api.systems.Reactor;
import api.systems.Shield;
import api.systems.addons.JumpInterdictor;
import api.systems.addons.custom.CustomAddOn;
import api.universe.Sector;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.elements.*;
import org.schema.game.common.controller.elements.power.reactor.MainReactorUnit;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.GlUtil;

import javax.vecmath.Vector3f;
import java.util.*;

public class Entity {
    public SegmentController internalEntity;

    public Entity(SegmentController controller) {
        if(controller == null){
            throw new IllegalArgumentException("controller cannot be null!");
        }
        internalEntity = controller;
    }

    public Faction getFaction() {
        /**
         * Gets the faction the entity is currently part of. Returns null if the entity has no faction.
         */
        if (internalEntity.isInExitingFaction()) {
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
         * Gets the entity's current name + Pilot/Faction info.
         */
        return internalEntity.getName();
    }

    public String getRealName() {
        /**
         * Gets the entity's REAL name.
         */
        return internalEntity.getRealName();
    }

    public String getUID() {
        return internalEntity.getUniqueIdentifier();
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

    public boolean isDocked() {
        return internalEntity.isDocked();
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
        if (getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
            return internalEntity.getSpeedCurrent();
        } else {
            return 0;
        }
    }

    public float getMaxSpeed() {
        /**
         * Gets the entity's max speed. Returns 0 if the entity is immobile.
         */
        if (getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.PLANETSEGMENT) || getEntityType().equals(EntityType.ASTEROID)) {
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
        if (hasAnyReactors()) {
            ManagerContainer<?> manager = getManagerContainer();
            if (getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.STATION)) {
                ReactorTree activeReactor = manager.getPowerInterface().getActiveReactor();
                if (activeReactor != null) {
                    return new Reactor(activeReactor);
                }
            }
        }
        return null;
    }

    public Reactor getReactor(int i) {
        /**
         * Gets the specified reactor from the entity. Returns null if the entity doesn't have any reactors.
         */
        if (hasAnyReactors()) {
            return getReactors().get(i);
        }
        return null;
    }

    public ArrayList<Reactor> getReactors() {
        /**
         * Gets an ArrayList of all the entity's reactors. Returns null if the entity doesn't have any reactors.
         */
        if (hasAnyReactors()) {
            ManagerContainer<?> manager = getManagerContainer();
            ArrayList<Reactor> reactors = new ArrayList<Reactor>();
            if (getEntityType().equals(EntityType.SHIP) || getEntityType().equals(EntityType.STATION)) {
                if (manager instanceof ShipManagerContainer) {
                    List<MainReactorUnit> allReactors = manager.getPowerInterface().getMainReactors();
                    for (MainReactorUnit reactorUnit : allReactors) {
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
        if (getEntityType() != EntityType.STATION && getEntityType() != EntityType.SHOP && getEntityType() != EntityType.PLANETCORE) {
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
        ArrayList<SegmentController> collection = new ArrayList<SegmentController>();
        internalEntity.railController.getDockedRecusive(collection);
        ArrayList<Ship> ships = new ArrayList<Ship>();
        for (SegmentController controller : collection) {
            if (controller instanceof org.schema.game.common.controller.Ship) {
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

    public void setVulnerable(boolean val) {
        internalEntity.setVulnerable(val);
    }

    public boolean isVulnerable() {
        return internalEntity.isVulnerable();
    }

    public ArrayList<Shield> getShields() {
        /**
         * Gets an ArrayList of all the entity's shields. Returns null if the entity is not a ship or space station.
         */
        ManagerContainer<?> manager = getManagerContainer();
        ArrayList<Shield> shields = new ArrayList<Shield>();
        if (manager instanceof ShieldContainerInterface) {
            Collection<ShieldLocal> allShields = ((ShieldContainerInterface) manager).getShieldAddOn().getShieldLocalAddOn().getAllShields();
            for (ShieldLocal sh : allShields) {
                shields.add(new Shield(sh));
            }
            return shields;
        }
        return new ArrayList<Shield>();
    }

    public ManagerContainer<?> getManagerContainer() {
        /**
         * Used in the API for getting entity systems and modules. Don't use unless you know what you're doing!
         */
        return ((ManagedSegmentController<?>) internalEntity).getManagerContainer();
    }

    public EntityType getEntityType() {
        /**
         * Gets the entity's type.
         */
        EntityType entityType = null;
        switch (internalEntity.getType()) {
            case SPACE_STATION:
                entityType = EntityType.STATION;
                break;
            case SHIP:
                entityType = EntityType.SHIP;
                break;
            case ASTEROID:
                entityType = EntityType.ASTEROID;
                break;
            case PLANET_CORE:
                entityType = EntityType.PLANETCORE;
                break;
            case PLANET_SEGMENT:
                entityType = EntityType.PLANETSEGMENT;
                break;
            case SHOP:
                entityType = EntityType.SHOP;
                break;
        }
        return entityType;
    }

    public boolean isOnServer() { //Does this need to be public?
        return internalEntity.isOnServer();
    }

    public Block getBlockAt(int x, int y, int z) {
        return new Block(internalEntity.getSegmentBuffer().getPointUnsave(x, y, z));
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
        HashMap<Blocks, Integer> blocks = new HashMap<Blocks, Integer>();

        for (Blocks value : Blocks.values()) {
            blocks.put(value, getBlockAmount(value));
        }
        return blocks;
    }

    public Shield getLastHitShield() {
        /**
         * Gets the shield group last hit by any damage.
         */
        ManagerContainer<?> manager = getManagerContainer();
        if (manager instanceof ShieldContainerInterface) {
            ShieldLocal lastHitShield = ((ShieldContainerInterface) manager).getShieldAddOn().getShieldLocalAddOn().getLastHitShield();
            if (lastHitShield == null) {
                return null;
            }
            return new Shield(lastHitShield);
        }
        return null;
    }

    public ArrayList<Player> getAttachedPlayers() {
        /**
         * Gets an arraylist of players currently attached to the entity.
         */
        ArrayList<Player> pl = new ArrayList<Player>();
        if (internalEntity instanceof PlayerControllable) {
            for (PlayerState attachedPlayer : ((PlayerControllable) internalEntity).getAttachedPlayers()) {
                pl.add(new Player(attachedPlayer));
            }
        } else {
            return new ArrayList<Player>();
        }
        return pl;
    }

    public Ship toShip() {
        return new Ship(internalEntity);
    }

    //CUSTOM ADD ON SUPPORT
    private static HashMap<String, CustomAddOn> nameAddonMap = null;
    private static HashMap<Class<? extends CustomAddOn>, CustomAddOn> classAddonMap = null;
    public Collection<CustomAddOn> getCustomAddons() {
        generateAddonLookup();
        return nameAddonMap.values();
    }

    public CustomAddOn getCustomAddon(Class<? extends CustomAddOn> clazz) {
        generateAddonLookup();
        return classAddonMap.get(clazz);
    }
    public CustomAddOn getCustomAddon(String name) {
        generateAddonLookup();
        return nameAddonMap.get(name);
    }
    private void generateAddonLookup(){
        if(nameAddonMap == null){
            nameAddonMap = new HashMap<String, CustomAddOn>();
            classAddonMap = new HashMap<Class<? extends CustomAddOn>, CustomAddOn>();
            ManagerContainer<?> manager = getManagerContainer();
            for (PlayerUsableInterface usableAddon : manager.getPlayerUsable()) {
                if (usableAddon instanceof CustomAddOn) {
                    CustomAddOn customAddOn = (CustomAddOn) usableAddon;
                    nameAddonMap.put(customAddOn.getName(), customAddOn);
                    classAddonMap.put(customAddOn.getClass(), customAddOn);
                }
            }
        }
    }
    //

    public Sector getSector() {
        if (GameServer.getServerState() != null) {
            org.schema.game.common.data.world.Sector sector = GameServer.getServerState().getUniverse().getSector(internalEntity.getSectorId());
            return new Sector(sector);
        } else {
            //TODO what to do if client?
            return null;
        }
    }

    public boolean isStation() {
        return internalEntity instanceof SpaceStation;
    }

    public boolean isShip() {
        return internalEntity instanceof org.schema.game.common.controller.Ship;
    }

    public Vector3i getSectorPosition() {
        return internalEntity.getSector(new Vector3i());
    }

    public String getMassString() {
        return StringTools.massFormat(getMass());
    }

    public JumpInterdictor getInterdictionAddOn() {
        return new JumpInterdictor(getManagerContainer().getInterdictionAddOn());
    }

    public ConfigEntityManager getConfigManager() {
        return internalEntity.getConfigManager();
    }

    public Station toStation() {
        return new Station(internalEntity);
    }

    public ArrayList<Block> getBlocksInArea(Vector3i min, Vector3i max) {
        /**
         * Gets all the blocks in a specified area on the entity;
         */
        ArrayList<Block> blocks = new ArrayList<Block>();

        for (int y = min.y; y < max.y; y++) {
            for (int x = min.x; x < max.x; x++) {
                for (int z = min.z; z < max.z; z++) {
                    blocks.add(getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    /**
     * Gets all of the ships manager modules, manager modules are the 'parts' of the ship, cannon system collective, rail system collective, etc.
     * @return A list of all the the modules
     * TODO: Make a helper class for manager modules
     */
    public ObjectArrayList<ManagerModule<?, ?, ?>> getManagerModules(){
        return getManagerContainer().getModules();
    }

    /**
     * Get a manager module of specified type
     * @param classType the type of class
     * @return the array
     */
    public <EM extends UsableElementManager> EM getElementManager(Class<EM> classType){
        for (ManagerModule<?, ?, ?> managerModule : getManagerModules()) {
            UsableElementManager<?, ?, ?> elementManager = managerModule.getElementManager();
            if(elementManager.getClass().equals(classType)){
                return (EM) elementManager;
            }
        }
        return null;
    }
    public <CM extends ElementCollectionManager> ArrayList<ElementCollectionManager> getCollectionManagers(Class<CM> classType){
        ArrayList<ElementCollectionManager> ecms = new ArrayList<ElementCollectionManager>();
        for (ManagerModule<?, ?, ?> module : getManagerContainer().getModules()) {
            if(module instanceof ManagerModuleCollection){
                for (Object cm : ((ManagerModuleCollection) module).getCollectionManagers()) {
                    if(cm.getClass().equals(classType)) {
                        ecms.add((ElementCollectionManager) cm);
                    }
                }
            }else if(module instanceof ManagerModuleSingle){
                ElementCollectionManager cm = ((ManagerModuleSingle) module).getCollectionManager();
                if(cm.getClass().equals(classType)){
                    ecms.add(cm);
                }
            }//else{ something broke }
        }
        return ecms;
    }
    public ArrayList<ElementCollectionManager> getAllCollectionManagers(){
        ArrayList<ElementCollectionManager> ecms = new ArrayList<ElementCollectionManager>();
        for (ManagerModule<?, ?, ?> module : getManagerContainer().getModules()) {
            if(module instanceof ManagerModuleCollection){
                for (Object cm : ((ManagerModuleCollection) module).getCollectionManagers()) {
                        ecms.add((ElementCollectionManager) cm);
                }
            }else if(module instanceof ManagerModuleSingle){
                ElementCollectionManager cm = ((ManagerModuleSingle) module).getCollectionManager();
                    ecms.add(cm);
            }
        }
        return ecms;
    }

}
