package api.network;

import api.entity.Player;
import api.faction.Faction;
import api.main.GameClient;
import api.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerToClientMessage extends Packet {
    public ServerToClientMessage(){

    }
    public ServerToClientMessage(Faction fac, String bc){
        this.fac = fac;
        this.bc = bc;
    }

    private Faction fac;
    String bc;

    @Override
    public void readPacketData(PacketReadBuffer buf) throws IOException {
        this.bc = buf.readString();
        this.fac = buf.readFaction();
    }

    @Override
    public void writePacketData(PacketWriteBuffer buf) throws IOException {
        buf.writeString(bc);
        buf.writeFaction(fac);
    }

    @Override
    public void processPacketOnClient() {
        GameClient.showPopupMessage(this.fac.getName() + "[fac] just received: " + bc + " from server", 0);
    }

    @Override
    public void processPacketOnServer(Player sender) {
        if(fac == null){
            Server.broadcastMessage("no fac!");
            return;
        }
        Server.broadcastMessage("facc: " + fac.getName());
    }
}
