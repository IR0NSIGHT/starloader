package api.network.packets;

import api.entity.Entity;
import api.entity.Player;
import api.main.GameClient;
import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.common.util.linAlg.Vector3i;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class UpdateCurrentVelocityPacket extends Packet {
    Vector3f velocity;
    public UpdateCurrentVelocityPacket(){

    }
    public UpdateCurrentVelocityPacket(Vector3f v){
        this.velocity = v;

    }
    @Override
    public void readPacketData(PacketReadBuffer buf) throws IOException {
        this.velocity = buf.readVector3f();
    }

    @Override
    public void writePacketData(PacketWriteBuffer buf) throws IOException {
        buf.writeVector3f(velocity);

    }

    @Override
    public void processPacketOnClient() {
        Entity currentEntity = GameClient.getCurrentEntity();
        if(currentEntity != null) {
            currentEntity.setVelocity(velocity);
        }
    }

    @Override
    public void processPacketOnServer(Player sender) {
        sender.getCurrentEntity().setVelocity(velocity);
    }
}
