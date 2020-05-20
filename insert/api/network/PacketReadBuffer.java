package api.network;

import api.entity.Entity;
import api.faction.Faction;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.universe.Sector;
import api.universe.Universe;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.world.RemoteSector;

import javax.vecmath.Vector3f;
import java.io.DataInputStream;
import java.io.IOException;

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

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public short readShort() throws IOException {
        return in.readShort();
    }
    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public Sector readServerSector() throws IOException {
        return Universe.getUniverse().getSector(readInt(), readInt(), readInt());
    }

    public RemoteSector readClientSector() throws IOException {
        return GameClient.getClientState().getLoadedSectors().get(new Vector3i(readInt(), readInt(), readInt()));
    }

    public Vector3i readVector() throws IOException {
        return new Vector3i(readInt(), readInt(), readInt());
    }
    public Vector3f readVector3f() throws IOException {
        return new Vector3f(readFloat(), readFloat(), readFloat());
    }

    public Faction readFaction() throws IOException {
        int facId = in.readInt();
        org.schema.game.common.data.player.faction.Faction faction = StarLoader.getGameState().getFactionManager().getFaction(facId);
        if (faction == null) return null;
        return new Faction(faction);
    }
    //FIXME: method broke, find a proper way to get entity by id
    public Entity readEntity() throws IOException {
        Vector3i sector = readVector();
        String entName = readString();
        int entId = readInt();
        if (GameServer.getServerState() != null) {
            return new Entity(GameServer.getServerState().getSegmentControllersByName().get(entName));
        } else if (GameClient.getClientState() != null) {
            return new Entity((SegmentController) GameClient.getClientState().getCurrentSectorEntities().get(entId));
        } else {
            //Title screen
            return null;
        }
    }

}
