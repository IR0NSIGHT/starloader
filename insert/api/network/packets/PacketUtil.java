package api.network.packets;

import api.common.GameClient;
import api.common.GameServer;
import api.network.Packet;
import api.network.PacketWriteBuffer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.network.NetworkProcessor;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.server.ServerProcessor;

import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUtil {
    public static void sendPacketToServer(Packet apiPacket){
        try {
            NetworkProcessor processor = GameClient.getClientState().getProcessor();
            DataOutputStream output = new DataOutputStream(processor.getOutRaw());
            output.writeInt(-2); //Mod packet ID
            output.writeShort(apiPacket.getId()); //The packet ID we're sending
            apiPacket.writePacketData(new PacketWriteBuffer(output)); //The info of the packet
            processor.getOutRaw().flush(); //Send
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerProcessor getServerProcessor(RegisteredClientOnServer clientOnServer){
        return clientOnServer.getProcessor();
    }
    public static void sendPacket(PlayerState player, Packet apiPacket){
        try {
            ServerProcessor processor = getServerProcessor(GameServer.getServerClient(player));
            DataOutputStream output = new DataOutputStream(processor.getOutRaw());
            output.writeInt(-2); //Mod packet ID
            output.writeShort(apiPacket.getId()); //The packet ID we're sending
            apiPacket.writePacketData(new PacketWriteBuffer(output)); //The info of the packet
            processor.getOutRaw().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
