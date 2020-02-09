package api.main;

import org.schema.game.client.data.GameClientState;

public class GameClient {
    public static GameClientState getClientState(){
        return GameClientState.instance;
    }
    public static void sendChatMessage(String msg){
        GameClientState inst = getClientState();
        inst.chat(inst.getChat(), "123", msg, true);
    }
}
