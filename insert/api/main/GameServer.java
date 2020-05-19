package api.main;

import api.entity.Player;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;

import java.util.ArrayList;

public class GameServer {
    public static GameServerState getServerState(){
        return GameServerState.instance;
    }

    public static ArrayList<Player> getOnlinePlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for (PlayerState p : GameServer.getServerState().getPlayerStatesByName().values()) {
            players.add(new Player(p));
        }
        return players;
    }
}
