package api.entity;

import api.inventory.Inventory;
import api.inventory.InventoryType;
import api.server.Server;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;
import java.util.ArrayList;
import java.util.Map;

public class Player {

    private PlayerState playerState;
    private Entity currentEntity;
    private ArrayList<Fleet> fleets;

    public Player(PlayerState state) {
        playerState = state;
    }

    public Entity getCurrentEntity() {
        //ToDo:set the currentEntity as the players current entered entity
        return currentEntity;
    }

    public ArrayList<Fleet> getFleets() {
        return fleets;
    }

    public void addFleet(Fleet fleet) {
        fleets.add(fleet);
    }

    public void removeFleet(Fleet fleet) {
        fleets.add(fleet);
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

    public Vector3i getSector(){
        //Duplicate vector so nothing weird happens
        return new Vector3i(getPlayerState().getCurrentSector());
    }
    public void sendServerMessage(String message){
        Server.sendMessage(this.getPlayerState(), message);
    }
    public Inventory getInventory(){
        return new Inventory(playerState.getInventory(), InventoryType.PLAYER_INVENTORY, this);
    }
    public void sendMail(String from, String to, String title, String contents){
        playerState.getClientChannel().getPlayerMessageController().serverSend(from, to, title,
                contents);
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
    public PlayerState getPlayerState() {
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
