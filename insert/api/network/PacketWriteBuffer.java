package api.network;

import api.faction.Faction;
import api.mod.StarLoader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWriteBuffer {
    private DataOutputStream out;

    public PacketWriteBuffer(DataOutputStream out){
        this.out = out;
    }
    public void writeInt(int i) throws IOException {
        out.writeInt(i);
    }
    public void writeString(String s) throws IOException {
        out.writeUTF(s);
    }
    public void writeDouble(double d) throws IOException {
        out.writeDouble(d);
    }
    public void writeByte(byte b) throws IOException {
        out.writeByte(b);
    }
    public void writeFaction(Faction f) throws IOException {
        out.writeInt(f.getID());
    }

}
