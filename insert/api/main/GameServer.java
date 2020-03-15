package api.main;

import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;

public class GameServer {
    public static GameServerState getServerState(){
        return GameServerState.instance;
    }
}
