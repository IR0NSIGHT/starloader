package api.common;

import api.DebugFile;
import org.schema.common.util.StringTools;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Universe;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.schine.network.RegisteredClientInterface;
import org.schema.schine.network.RegisteredClientOnServer;

import java.util.Locale;

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
    public static void executeAdminCommand(String command) {
        RegisteredClientInterface registeredClientInterface = GameServer.getServerState().getAdminLocalClient();
        try {
            final String[] split2;
            final AdminCommands value = AdminCommands.valueOf((split2 = command.split(" "))[0].trim().toUpperCase(Locale.ENGLISH));
            getServerState().getController().enqueueAdminCommand(registeredClientInterface, value, AdminCommands.packParameters(value, StringTools.splitParameters(command.substring(split2[0].length()))));
        } catch (Exception ex3) {
            ex3.printStackTrace();
            DebugFile.err("Failed to execute admin command: " + command);
            registeredClientInterface.executedAdminCommand();
        }
    }
}
