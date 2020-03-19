package api.entity;

import api.main.GameClient;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;

import javax.vecmath.Vector3f;
import java.util.Map;

public class Player {

    private PlayerState playerState = new PlayerState(GameClient.getClientState());

    private Entity currentEntity;

    public Player() {

    }

    public Entity getCurrentEntity() {
        //ToDo:set the currentEntity as the players current entered entity
        return currentEntity;
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
        this.playerState = getPlayerStateFromName(this.getName());
        return this.playerState;
    }

    private PlayerState getPlayerStateFromName(String playerName) {
        GameServerState gameServerState = GameServerState.instance;
        Map<String, PlayerState> playerStates = gameServerState.getPlayerStatesByName();
        PlayerState pState = null;
        try {
            pState = playerStates.get(playerName);
        } catch(Exception e) {
            System.err.println("[StarLoader API]: Tried to get a PlayerState from name, but specified player was not found on server!");
            e.printStackTrace();
        }
        this.playerState = pState;
        return playerState;
    }
}
