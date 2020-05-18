package api.entity;

import api.faction.Faction;
import api.inventory.Inventory;
import api.inventory.InventoryType;
import api.main.GameServer;
import api.network.Packet;
import api.server.Server;
import api.universe.Sector;
import api.universe.Universe;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.server.ServerProcessor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Player {

    private PlayerState playerState;

    public Player(PlayerState state) {
        playerState = state;
    }

    public Faction getFaction() {
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

    public void sendMail(String from, String title, String contents) {
        playerState.getClientChannel().getPlayerMessageController().serverSend(from, playerState.getName(), title,
                contents);
    }
    public RegisteredClientOnServer getServerClient(){
        return GameServer.getServerState().getClients().get(getPlayerState().getClientId());
    }
    public int getId(){
        return getPlayerState().getId();
    }

    public Inventory getInventory() {
        return new Inventory(getPlayerState().getInventory(), this, InventoryType.PLAYER_INVENTORY);
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public Entity getCurrentEntity(){
        Set<ControllerStateUnit> units = getPlayerState().getControllerState().getUnits();
        if(units.isEmpty()){
            return null;
        }
        ControllerStateUnit unit = units.iterator().next();
        if(unit != null && unit.playerControllable instanceof SegmentController){
            return new Entity((SegmentController) unit.playerControllable);
        }
        return null;
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

    public Sector getSector() {
        /**
         * Gets the player's current sector.
         */
        return Universe.getUniverse().getSector(playerState.getCurrentSector());
    }

    public ServerProcessor getServerProcessor(){
        return getServerClient().getProcessor();
    }
    public void sendPacket(Packet apiPacket){
        try {
            DataOutputStream output = new DataOutputStream(getServerProcessor().getOutRaw());
            output.writeInt(-2); //Mod packet ID
            output.writeInt(apiPacket.getId()); //The packet ID we're sending
            apiPacket.writePacketData(output); //The info of the packet
            getServerProcessor().getOutRaw().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
