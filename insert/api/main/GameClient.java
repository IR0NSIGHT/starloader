package api.main;

import org.schema.game.client.data.GameClientState;

public class GameClient {
    public static GameClientState getClientState(){
        return GameClientState.instance;
    }
    public static void makeChatMessage(String msg){
        GameClientState inst = getClientState();
        inst.chat(inst.getChat(), "[ALL]", msg, true);
    }
    public static void sendMessage(String s){
        getClientState().getChat().addToVisibleChat(s, "[ALL]", true);
    }

    public static void showPopupMessage(){

    }
}
