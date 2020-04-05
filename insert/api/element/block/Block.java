package api.element.block;

import api.entity.Entity;
import api.inventory.Inventory;
import api.inventory.InventoryType;
import api.systems.WeaponSystem;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.elements.cargo.CargoElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.*;

import java.util.List;

public class Block {
    private SegmentPiece internalBlock;

    public Block(SegmentPiece internalBlock) {
        this.internalBlock = internalBlock;
    }

    public Blocks getType(){
        return Blocks.fromId(internalBlock.getType());
    }
    public Vector3i getLocation(){
        return new Vector3i(internalBlock.x, internalBlock.y, internalBlock.z);
    }
    public float distance(Block other){
        return SegmentPiece.getWorldDistance(internalBlock, other.internalBlock);
    }
    public Vector3i getWorldLocation(){
        return internalBlock.getAbsolutePos(new Vector3i());
    }
    public Entity getEntity(){
        return new Entity(internalBlock.getSegmentController());
    }
    public int getHp(){
        return internalBlock.getHitpointsFull();
    }
    public void setHp(int i){
        internalBlock.setHitpointsFull(i);
    }
    public int getMaxHp(){
        return getType().getInfo().maxHitPointsFull;
    }

    public Inventory getInventory() {
        /**
         * Gets the block's inventory. Returns null if the block has no inventory.
         */
        if(getType() == Blocks.MICRO_ASSEMBLER || getType() == Blocks.CAPSULE_REFINERY || getType() == Blocks.BASIC_FACTORY || getType() == Blocks.STANDARD_FACTORY || getType() == Blocks.ADVANCED_FACTORY) {
            CargoElementManager cargoElementManager = new CargoElementManager(getInternalSegmentPiece().getSegmentController());
            return new Inventory(cargoElementManager.getManagerContainer().getInventory(getLocation()), InventoryType.FACTORY);
        } else if(getType() == Blocks.STORAGE) {
            CargoElementManager cargoElementManager = new CargoElementManager(getInternalSegmentPiece().getSegmentController());
            return new Inventory(cargoElementManager.getManagerContainer().getInventory(getLocation()), InventoryType.STORAGE_BOX);
        } else if(getType() == Blocks.SHIPYARD_COMPUTER_0) {
            CargoElementManager cargoElementManager = new CargoElementManager(getInternalSegmentPiece().getSegmentController());
            return new Inventory(cargoElementManager.getManagerContainer().getInventory(getLocation()), InventoryType.SHIPYARD);
        }
        return null;
    }

    public Entity getDocked() {
        /**
         * Gets the entity currently docked to the block if there is one. Only returns a value if the block is a type of rail.
         */
        if(getType() == Blocks.RAIL_BASIC || getType() == Blocks.RAIL_TURRET_AXIS || getType() == Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE || getType() == Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE || getType() == Blocks.RAIL_UNLOAD_0 || getType() == Blocks.RAIL_LOAD_0 || getType() == Blocks.SHOOTOUT_RAIL || getType() == Blocks.PICKUP_POINT || getType() == Blocks.PICKUP_RAIL_0) {
            ElementDocking dockedEntity = getInternalSegmentPiece().getSegmentController().getDockingController().getDockedOn();
            return new Entity(dockedEntity.to.getSegmentController());
        }
        return null;
    }

    public WeaponSystem getWeaponSystem() {
        /**
         * Gets the block's weapon system. Returns null if the block is not part of a weapon system.
         */
        if(getType().getInfo().getFullName().contains("COMPUTER") || getType().getInfo().getFullName().contains("MODULE")) {
            //Todo:Implement a better system of detecting if a block is a weapon other than just by it's name.
            WeaponElementManager weaponElementManager = new WeaponElementManager(internalBlock.getSegmentController());
            List<WeaponCollectionManager> weaponCollections = weaponElementManager.getCollectionManagers();
            for(WeaponCollectionManager weaponCollection : weaponCollections) {
                if(weaponCollection.getControllerPos() == getLocation()) {
                    return new WeaponSystem(weaponCollection);
                }
            }
        }
        return null;
    }

    public SegmentPiece getInternalSegmentPiece() {
        return internalBlock;
    }
    public void setActive(boolean b){
        internalBlock.setActive(b);
    }
    public short getId(){
        return internalBlock.getType();
    }
}
