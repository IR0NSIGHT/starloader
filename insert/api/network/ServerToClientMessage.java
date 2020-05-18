package api.network;

import api.entity.Player;
import api.main.GameClient;
import api.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerToClientMessage extends Packet {
    public ServerToClientMessage(){

    }
    public ServerToClientMessage(String bc){
        this.bc = bc;
    }
    String bc;

    @Override
    public void readPacketData(DataInputStream buf) throws IOException {
        bc = buf.readUTF();
    }

    @Override
    public void writePacketData(DataOutputStream buf) throws IOException {
        buf.writeUTF(bc);
    }

    @Override
    public void processPacketOnClient() {
        GameClient.showPopupMessage("just received: " + bc + " from server", 0);
    }

    @Override
    public void processPacketOnServer(Player sender) {
        Server.broadcastMessage("THANK YOU FOR YOUR PACKET!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
