package api.network;

import api.mod.StarLoader;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.world.RemoteSector;

import javax.vecmath.Vector3f;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PacketReadBuffer {
    private DataInputStream in;

    public PacketReadBuffer(DataInputStream in) {
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
    public long readLong() throws IOException {
        return in.readLong();
    }

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public short readShort() throws IOException {
        return in.readShort();
    }
    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public Vector3i readVector() throws IOException {
        return new Vector3i(readInt(), readInt(), readInt());
    }
    public Vector3f readVector3f() throws IOException {
        return new Vector3f(readFloat(), readFloat(), readFloat());
    }
    public ArrayList<String> readStringList() throws IOException {
        int size = readInt();
        ArrayList<String> r = new ArrayList<String>(size);
        for (int i = 0; i < size; i++) {
            r.add(readString());
        }
        return r;
    }
    public ArrayList<Long> readLongList() throws IOException {
        int size = readInt();
        ArrayList<Long> r = new ArrayList<Long>(size);
        for (int i = 0; i < size; i++) {
            r.add(readLong());
        }
        return r;
    }

}
