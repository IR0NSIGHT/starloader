package api.server;

import api.main.GameServer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Universe;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.network.server.ServerController;

import java.util.Collection;

public class Server {

    public Server() {

    }
    public static void executeAdminCommand(String command){
        GameServer.getServerState().executeAdminCommand((String) ServerConfig.SUPER_ADMIN_PASSWORD.getCurrentState(), command, GameServer.getServerState().getAdminLocalClient());
    }
    public static void broadcastMessage(String message){
        for(PlayerState player : getOnlinePlayers()){
            player.sendClientMessage(message, 0);
        }
    }
    public static Collection<PlayerState> getOnlinePlayers(){
        return getServerState().getPlayerStatesByName().values();
    }
    public static GameServerState getServerState(){
        return GameServerState.instance;
    }
}
