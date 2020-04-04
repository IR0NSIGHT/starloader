package api.inventory;

import api.entity.Player;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import org.schema.game.client.view.gui.inventory.inventorynew.InventoryFilterBar;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.inventory.InventoryHolder;

public class Inventory {

    private org.schema.game.common.data.player.inventory.Inventory internalInventory;
    private InventoryType inventoryType;
    private Player inventoryHolder;
    private Block container;

    /**
     * @param internalInventory Internal inventory
     * @param inventoryType Type of inventory
     * @param holder Player who will get updates of modifications (can be null)
     */
    public Inventory(org.schema.game.common.data.player.inventory.Inventory internalInventory, Player holder, InventoryType inventoryType) {
        this.inventoryHolder = holder;
        this.internalInventory = internalInventory;
        this.inventoryType = inventoryType;
        this.container = null;
    }

    public Inventory(org.schema.game.common.data.player.inventory.Inventory internalInventory, Block container, InventoryType inventoryType) {
        this.inventoryHolder = null;
        this.internalInventory = internalInventory;
        this.inventoryType = inventoryType;
        this.container = container;
    }

    public org.schema.game.common.data.player.inventory.Inventory getInternalInventory() {
        return internalInventory;
    }

    public void setInventoryFilter(InventoryFilter inventoryFilter) {
        org.schema.game.common.data.player.inventory.InventoryFilter internalFilter = internalInventory.getFilter();
        for(Block block : inventoryFilter.getBlocks().keySet()) {
            int amount = inventoryFilter.getBlocks().get(block)[0];
            int upTo = inventoryFilter.getBlocks().get(block)[1];
            //internalFilter.filter.put(?);
            //Todo:Figure out how to put block, amount, and upTo values in the internalFilter
        }
    }

    public void addItem(Block block) {
        PlayerState state = inventoryHolder.getPlayerState();
        //what actualy adds the item to the inventory
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
