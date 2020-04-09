package api.inventory;

import api.entity.Player;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.PlayerState;

public class Inventory {

    private org.schema.game.common.data.player.inventory.Inventory internalInventory;
    private InventoryType inventoryType;
    private Player inventoryHolder;

    /**
     * @param internalInventory Internal inventory
     * @param inventoryType Type of inventory
     * @param holder Player who will get updates of modifications (can be null)
     */
    public Inventory(org.schema.game.common.data.player.inventory.Inventory internalInventory, Player holder, InventoryType inventoryType) {
        this.inventoryHolder = holder;
        this.internalInventory = internalInventory;
        this.inventoryType = inventoryType;
    }

    public Inventory(org.schema.game.common.data.player.inventory.Inventory internalInventory, InventoryType inventoryType) {
        this.inventoryHolder = null;
        this.internalInventory = internalInventory;
        this.inventoryType = inventoryType;
    }

    public org.schema.game.common.data.player.inventory.Inventory getInternalInventory() {
        return internalInventory;
    }

    public void setInventoryFilter(InventoryFilter inventoryFilter) {
        org.schema.game.common.data.player.inventory.InventoryFilter internalFilter = internalInventory.getFilter();
        for(ItemStack block : inventoryFilter.getBlocks().keySet()) {
            int amount = inventoryFilter.getBlocks().get(block)[0];
            int upTo = inventoryFilter.getBlocks().get(block)[1];
            //internalFilter.filter.put(?);
            //Todo:Figure out how to put block, amount, and upTo values in the internalFilter
        }
    }

    public void addItem(ItemStack block) {
        PlayerState state = inventoryHolder.getPlayerState();
        int slot = internalInventory.incExistingOrNextFreeSlot(block.getId(), block.getAmount());
        if(inventoryHolder != null) {
            state.sendInventoryModification(slot, Long.MIN_VALUE);
        }
    }

    public boolean addElementById(short id, int amount) {
        if (ElementKeyMap.isValidType(id)) {
            PlayerState playerState = inventoryHolder.getPlayerState();
            playerState.sendInventoryModification(playerState.getInventory().incExistingOrNextFreeSlot(id, amount), Long.MIN_VALUE);
            return true;
        } else {
            return false;
        }
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }


}
