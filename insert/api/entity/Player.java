package api.entity;

import api.element.Element;
import api.entity.Entity;
import api.inventory.Inventory;
import api.main.GameClient;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.inventory.InventorySlot;

public class Player extends Entity {

    private PlayerState playerState = new PlayerState(GameClient.getClientState());

    public Player() {

    }

    public int getCredits() {
        return getPlayerState().getCredits();
    }

    public void setCredits(int credits) {
        getPlayerState().setCredits(credits);
    }

    public String getName() {
        return getPlayerState().getName();
    }

    public void setName(String name) {
        getPlayerState().setName(name);
    }
/*
    public Inventory getInventory() {
        org.schema.game.common.data.player.inventory.Inventory inventoryState = Inventory.getPlayerStateInventory(getPlayerState());
        Inventory inventory = new Inventory();
        //Todo: How to get a list of all slots?
        for(int slot = 0; slot < inventoryState.getSlots(); slot ++) {
            InventorySlot invSlot = inventoryState.getSlot(slot);
            Element element = Inventory.getElementFromSlot(invSlot);
            inventory.addElement(element);
        }
        return inventory;
    }
*/
    private PlayerState getPlayerState() {
        return this.playerState;
    }

}
