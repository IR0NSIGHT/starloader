package api.entity;

import api.inventory.Inventory;
import api.inventory.InventoryType;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;
import java.util.ArrayList;
import java.util.Map;

public class Station extends Entity {

    private SpaceStation internalStation;

    public Station(SegmentController controller) {
        super(controller);
        internalStation = (org.schema.game.common.controller.SpaceStation) controller;
    }

    public SpaceStation getInternalStation() {
        /**
         * Gets the game's internal version of the station. Don't use unless you know what you're doing!
         */
        return internalStation;
    }

    public boolean isHomeBase() {
        /**
         * Checks if the station is a home base for the faction controlling it. Returns false if it has no faction controlling it.
         */
        if(internalStation.isInExitingFaction()) {
            return internalStation.isHomeBase();
        }
        return false;
    }

    public boolean hasShop() {
        /**
         * Checks if the station has a shop.
         */
        return internalStation.isValidShop();
    }

    public Inventory getShopInventory() {
        /**
         * Returns the station's shop inventory. Returns null if the station doesn't have a shop.
         */
        if(hasShop()) {
            return new Inventory(internalStation.getShopInventory(), InventoryType.SHOP_INVENTORY);
        }
        return null;
    }

    public ArrayList<Player> getShopOwners() {
        /**
         * Gets an arraylist of players that own the station's shop. Returns null if the station doesn't have a shop or the shop has no owners.
         */
        if(hasShop()) {
            ArrayList<Player> shopOwners = new ArrayList<Player>();
            for(String internalPlayerName : internalStation.getShopOwners()) {
                shopOwners.add(getPlayerFromName(internalPlayerName));
            }
            return shopOwners;
        }
        return null;
    }

    private Player getPlayerFromName(String playerName) {
        GameServerState gameServerState = GameServerState.instance;
        Map<String, PlayerState> playerStates = gameServerState.getPlayerStatesByName();
        PlayerState pState = null;
        try {
            pState = playerStates.get(playerName);
        } catch(Exception e) {
            System.err.println("[StarLoader API]: Tried to get a PlayerState from name, but specified player was not found on server!");
            e.printStackTrace();
        }
        return new Player(pState);
    }

    public boolean takeControlOfSystem() {
        if(this.getFaction() == null){
            return false;
        }
        getSector().getSystem().claim(this);
        return true;
    }
}
