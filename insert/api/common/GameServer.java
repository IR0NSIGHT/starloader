package api.common;

import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Universe;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.network.RegisteredClientOnServer;

public class GameServer {
    public static GameServerState getServerState(){
        return GameServerState.instance;
    }
    public static Universe getUniverse(){
        return getServerState().getUniverse();
    }

    public static RegisteredClientOnServer getServerClient(PlayerState player){
        return GameServer.getServerState().getClients().get(player.getClientId());
    }
}
