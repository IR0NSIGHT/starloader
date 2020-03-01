package api.main;

import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;

import java.util.Collection;

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
}
