package api.network;

import api.main.GameClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerToClientMessage extends Packet {
    public ServerToClientMessage(){

    }
    public ServerToClientMessage(int bc){
        this.bc = bc;
    }
    int bc;

    @Override
    public void readPacketData(DataInputStream buf) throws IOException {
        bc = buf.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream buf) throws IOException {
        buf.writeInt(bc);
    }

    @Override
    public void processPacketOnClient() {
        GameClient.showPopupMessage("WE JUST RECV: " + bc + "!!!!!!!!!!!!", 0);
    }

    @Override
    public void processPacketOnServer() {

    }
}
