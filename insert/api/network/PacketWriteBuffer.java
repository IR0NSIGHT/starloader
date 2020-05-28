package api.network;

import org.schema.common.util.linAlg.Vector3i;

import javax.vecmath.Vector3f;
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
    public void writeBoolean(boolean b) throws IOException {
        out.writeBoolean(b);
    }
    public void writeFloat(float charge) throws IOException {
        out.writeFloat(charge);
    }
    public void writeVector(Vector3i sec) throws IOException {
        out.writeInt(sec.x);
        out.writeInt(sec.y);
        out.writeInt(sec.z);
    }
    public void writeVector3f(Vector3f sec) throws IOException {
        out.writeFloat(sec.x);
        out.writeFloat(sec.y);
        out.writeFloat(sec.z);
    }
}
