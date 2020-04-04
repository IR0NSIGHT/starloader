package api.main;

import api.entity.Entity;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.Controller;

import java.util.Collection;
import java.util.Set;

public class GameClient {
    public static GameClientState getClientState(){
        return GameClientState.instance;
    }
    public static GameClientController getClientController(){
        return GameClientState.instance.getController();
    }
    public static PlayerState getClientPlayerState(){
        return getClientState().getPlayer();
    }

    //Lots of internal stuff to be cleaned up later
    public static void makeChatMessage(String msg){
        GameClientState inst = getClientState();
        inst.chat(inst.getChat(), "[ALL]", msg, true);
    }
    public static void sendMessage(String s){
        getClientState().getChat().addToVisibleChat(s, "[ALL]", true);
    }

    public static void showPopupMessage(String str, int time){
        getClientState().getController().popupInfoTextMessage(str, time);
    }
    public static void showBigText(String header, String str, int time){
        getClientState().getController().showBigTitleMessage(header, str, time);
    }
    public static Collection<PlayerState> getConnectedPlayers(){
        return GameClientState.instance.getOnlinePlayersLowerCaseMap().values();
    }
    public static PlayerControllable getCurrentControl(){
        Set<ControllerStateUnit> units = getClientPlayerState().getControllerState().getUnits();
        if(units.isEmpty()) return null;
        ControllerStateUnit unit = units.iterator().next();
        return unit.playerControllable;
    }
    public static Ship getCurrentShip(){
        PlayerControllable con = getCurrentControl();
        if(con instanceof Ship){
            return (Ship) con;
        }else{
            return null;
        }
    }
    public static Entity getCurrentEntity(){
        PlayerControllable con = getCurrentControl();
        if(con instanceof SegmentController){
            return new Entity((SegmentController) con);
        }else{
            return null;
        }
    }
    public static Collection<Fleet> getAvailableFleets(){
        return getClientState().getFleetManager().getAvailableFleetsClient();
    }
    public static void setLoadString(String s){
        Controller.getResLoader().setLoadString(s);
    }
}
