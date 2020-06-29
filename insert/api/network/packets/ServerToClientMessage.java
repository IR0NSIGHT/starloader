package api.network.packets;

import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.game.common.data.player.PlayerState;

import java.io.IOException;

public class ServerToClientMessage extends Packet {
    public ServerToClientMessage(){

    }
    public ServerToClientMessage(String bc){
        this.bc = bc;
    }

    String bc;

    @Override
    public void readPacketData(PacketReadBuffer buf) throws IOException {
        this.bc = buf.readString();
    }

    @Override
    public void writePacketData(PacketWriteBuffer buf) throws IOException {
        buf.writeString(bc);
    }

    @Override
    public void processPacketOnClient() {
    }

    @Override
    public void processPacketOnServer(PlayerState sender) {

    }
}
