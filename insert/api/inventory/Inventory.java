package api.inventory;

import api.DebugFile;
import api.element.Element;
import api.entity.Player;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.PlayerState;

import java.util.Map;

public class Inventory {

    //private Map<Element, Integer> contents;
    //private boolean locked = false;
    //private double storageCapacity;
    //private int cargoBlocks;
    private org.schema.game.common.data.player.inventory.Inventory internalInventory;
    private InventoryType inventoryType;
    private Player inventoryHolder;

    /**
     *
     * @param internalInventory Internal inventory
     * @param inventoryType Type of inventory
     * @param holder Player who will get updates of modifications (can be null)
     */
    public Inventory(org.schema.game.common.data.player.inventory.Inventory internalInventory, InventoryType inventoryType, Player holder) {
        this.inventoryHolder = holder;
        this.internalInventory = internalInventory;
        this.inventoryType = inventoryType;
    }
    public void addItem(ItemStack stack){
        PlayerState state = inventoryHolder.getPlayerState();
        //what actualy adds the item to the inventory
        int slot = internalInventory.incExistingOrNextFreeSlot(stack.getId(), stack.getAmount());
        if(inventoryHolder != null) {
            state.sendInventoryModification(slot, Long.MIN_VALUE);
        }
    }

    public boolean addElementById(short id, int amount){
        if (ElementKeyMap.isValidType(id)) {
            PlayerState playerState = inventoryHolder.getPlayerState();
            playerState.sendInventoryModification(playerState.getInventory().incExistingOrNextFreeSlot(id, amount), Long.MIN_VALUE);
            return true;
        }else{
            return false;
        }
    }

    /*public void addElement(Element element, int count) {
        this.contents.put(element, count);
    }

    public Map<Element, Integer> getContents() {
        return contents;
    }

    public void setContents(Map<Element, Integer> contents) {
        this.contents = contents;
    }*/

    public InventoryType getInventoryType() {
        return inventoryType;
    }


}
