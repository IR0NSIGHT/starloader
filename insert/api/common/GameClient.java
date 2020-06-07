package api.common;

import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.data.ClientGameData;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.Controller;

import java.util.Set;

public class GameClient {

    public static PlayerInteractionControlManager getControlManager(){
        return GameClient.getClientState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager();
    }
    public static ClientGameData getGameData(){
        return getClientController().getClientGameData();
    }

    public static GameClientState getClientState(){
        return GameClientState.instance;
    }
    public static GameClientController getClientController(){
        return GameClientState.instance.getController();
    }
    public static PlayerState getClientPlayerState(){
        return getClientState().getPlayer();
    }

    public static void sendMessage(String s){
        getClientState().getChat().addToVisibleChat(s, "[ALL]", true);
    }

    public static void showPopupMessage(String str, int delay){
        getClientState().getController().popupInfoTextMessage(str, delay);
    }


    public static PlayerControllable getCurrentControl(){
        Set<ControllerStateUnit> units = getClientPlayerState().getControllerState().getUnits();
        if(units.isEmpty()) return null;
        ControllerStateUnit unit = units.iterator().next();
        return unit.playerControllable;
    }



    public static void setLoadString(String s){
        Controller.getResLoader().setLoadString(s);
    }

}
