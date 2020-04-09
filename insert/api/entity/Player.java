package api.entity;

import api.faction.Faction;
import api.inventory.Inventory;
import api.inventory.InventoryType;
import api.server.Server;
import api.universe.Sector;
import api.universe.Universe;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;
import java.io.IOException;
import java.util.Map;

public class Player {

    private PlayerState playerState;

    public Player(PlayerState state) {
        playerState = state;
    }

    public Faction getFaction() throws IOException {
        GameServerState gameServerState = GameServerState.instance;
        int factionID = playerState.getFactionId();
        return new Faction(gameServerState.getFactionManager().getFaction(factionID));
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

    public void sendServerMessage(String message){
        Server.sendMessage(this.getPlayerState(), message);
    }

    public void sendMail(String from, String to, String title, String contents) {
        playerState.getClientChannel().getPlayerMessageController().serverSend(from, to, title,
                contents);
    }

    public Inventory getInventory() {
        return new Inventory(getPlayerState().getInventory(), this, InventoryType.PLAYER_INVENTORY);
    }

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

    public void openInventory(Inventory inventory) {

    }

    public Sector getSector() throws IOException {
        /**
         * Gets the player's current sector.
         */
        return new Sector(Universe.getUniverse().getSector(playerState.getCurrentSector()));
    }
}
