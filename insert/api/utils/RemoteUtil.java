package api.utils;

import api.main.GameClient;
import org.schema.game.common.Starter;
import org.schema.schine.network.objects.NetworkChat;
import org.schema.schine.network.objects.NetworkObject;

/**
 * Static helper class to get network objects (used for servers to send stuff to players)
 */
public class RemoteUtil {
    public static NetworkChat getNetworkChat(){
        return GameClient.getClientState().getChat().getNetworkChat();
    }
    {
        //Starter
    }
}
