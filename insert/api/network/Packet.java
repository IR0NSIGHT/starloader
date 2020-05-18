package api.network;

import api.DebugFile;
import api.entity.Player;
import it.unimi.dsi.fastutil.Hash;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public abstract class Packet {
    public abstract void readPacketData(DataInputStream buf) throws IOException;
    public abstract void writePacketData(DataOutputStream buf) throws IOException;
    public abstract void processPacketOnClient();
    public abstract void processPacketOnServer(Player sender);
    private static HashMap<Integer, Class<? extends Packet>> packetLookup = new HashMap<Integer, Class<? extends Packet>>();
    private static HashMap<Class<? extends Packet>, Integer> reversePacketLookup = new HashMap<Class<? extends Packet>, Integer>();
    private static int idLog = 0;
    public static void registerPacket(Class<? extends Packet> clazz){
        if(reversePacketLookup.containsKey(clazz)){
            DebugFile.info("Already registered packet, skipping");
            return;
        }
        int next = idLog++;
        packetLookup.put(next, clazz);
        reversePacketLookup.put(clazz, next);
    }
    public static Packet newPacket(int id){
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
    private static int getId(Class<? extends Packet> clazz){
        Integer integer = reversePacketLookup.get(clazz);
        if(integer == null){
            DebugFile.err("!!! PACKET ID NOT FOUND, DID YOU FORGET TO REGISTER IT? !!!");
            return 0; //will throw another error down the line
        }
        return integer;
    }
    public int getId(){
        return getId(getClass());
    }
}
