package api.network;

import api.DebugFile;
import org.schema.game.common.data.player.PlayerState;

import java.io.IOException;
import java.util.HashMap;

public abstract class Packet {
    public abstract void readPacketData(PacketReadBuffer buf) throws IOException;
    public abstract void writePacketData(PacketWriteBuffer buf) throws IOException;
    public abstract void processPacketOnClient();
    public abstract void processPacketOnServer(PlayerState sender);
    private static HashMap<Short, Class<? extends Packet>> packetLookup = new HashMap<Short, Class<? extends Packet>>();
    private static HashMap<Class<? extends Packet>, Short> reversePacketLookup = new HashMap<Class<? extends Packet>, Short>();
    private static short idLog = Short.MIN_VALUE;
    public static void registerPacket(Class<? extends Packet> clazz){
        if(reversePacketLookup.containsKey(clazz)){
            DebugFile.info("Already registered packet, skipping");
            return;
        }
        short id = idLog++;
        packetLookup.put(id, clazz);
        reversePacketLookup.put(clazz, id);
    }
    public static void clearPackets(){
        packetLookup.clear();
        reversePacketLookup.clear();
        idLog = Short.MIN_VALUE;
    }
    public static Packet newPacket(short id){
        Class<? extends Packet> clazz = packetLookup.get(id);
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            //how?????
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static short getId(Class<? extends Packet> clazz){
        return reversePacketLookup.get(clazz);
    }
    public short getId(){
        return getId(getClass());
    }

}
