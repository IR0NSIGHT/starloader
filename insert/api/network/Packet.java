package api.network;

import api.DebugFile;
import api.entity.Player;
import it.unimi.dsi.fastutil.Hash;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public abstract class Packet {
    public abstract void readPacketData(PacketReadBuffer buf) throws IOException;
    public abstract void writePacketData(PacketWriteBuffer buf) throws IOException;
    public abstract void processPacketOnClient();
    public abstract void processPacketOnServer(Player sender);
    private static HashMap<String, Class<? extends Packet>> packetLookup = new HashMap<String, Class<? extends Packet>>();
    private static HashMap<Class<? extends Packet>, String> reversePacketLookup = new HashMap<Class<? extends Packet>, String>();
    public static void registerPacket(Class<? extends Packet> clazz){
        if(reversePacketLookup.containsKey(clazz)){
            DebugFile.info("Already registered packet, skipping");
            return;
        }
        clazz.getName();
        String id = clazz.getName();
        packetLookup.put(id, clazz);
        reversePacketLookup.put(clazz, id);
    }
    public static Packet newPacket(String id){
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
    private static String getId(Class<? extends Packet> clazz){
        String id = reversePacketLookup.get(clazz);
        if(id == null){
            DebugFile.err("!!! PACKET ID NOT FOUND, DID YOU FORGET TO REGISTER IT? !!!");
            return ""; //will throw another error down the line
        }
        return id;
    }
    public String getId(){
        return getId(getClass());
    }

}
