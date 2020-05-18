package api.network;

import api.entity.Player;
import api.faction.Faction;
import api.main.GameClient;
import api.mod.StarLoader;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketReadBuffer {
    private DataInputStream in;

    public PacketReadBuffer(DataInputStream in){
        this.in = in;
    }
    public int readInt() throws IOException {
        return in.readInt();
    }
    public String readString() throws IOException {
        return in.readUTF();
    }
    public double readDouble() throws IOException {
        return in.readDouble();
    }
    public float readFloat() throws IOException {
        return in.readFloat();
    }
    public byte readByte() throws IOException {
        return in.readByte();
    }
    public short readShort() throws IOException {
        return in.readShort();
    }
    public Faction readFaction() throws IOException {
        int facId = in.readInt();
        return new Faction(StarLoader.getGameState().getFactionManager().getFaction(facId));
    }

}
