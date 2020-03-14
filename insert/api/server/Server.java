package api.server;

import api.DebugFile;
import api.main.GameServer;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.schema.common.util.StringTools;
import org.schema.game.common.data.chat.AllChannel;
import org.schema.game.common.data.chat.ChatChannel;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Universe;
import org.schema.game.network.objects.ChatMessage;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.schine.network.RegisteredClientInterface;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.server.AdminLocalClient;
import org.schema.schine.network.server.ServerController;
import org.schema.schine.network.server.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

public class Server {

    public Server() {

    }
    /*public static void executeAdminCommand(String command){
        Only works if superadmin is enabled
        GameServer.getServerState().executeAdminCommand((String) ServerConfig.SUPER_ADMIN_PASSWORD.getCurrentState(), command, GameServer.getServerState().getAdminLocalClient());
    }*/
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
        //registeredClientInterface.executedAdminCommand();
    }
    public static void broadcastMessage(String message){
        for (RegisteredClientOnServer client : getClients()) {
            try {
                client.serverMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static RegisteredClientOnServer getClientFromState(PlayerState state){
        try {
            Field f = PlayerState.class.getDeclaredField("serverClient");
            f.setAccessible(true);
            return (RegisteredClientOnServer) f.get(state);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Collection<PlayerState> getOnlinePlayers(){
        return getServerState().getPlayerStatesByName().values();
    }
    public static ObjectCollection<RegisteredClientOnServer> getClients(){
        return getServerState().getClients().values();
    }
    public static GameServerState getServerState(){
        return GameServerState.instance;
    }

    public static PlayerState getPlayer(String name){
        try {
            return getServerState().getPlayerFromName(name);
        } catch (PlayerNotFountException e) {
            return null;
        }
    }
}
