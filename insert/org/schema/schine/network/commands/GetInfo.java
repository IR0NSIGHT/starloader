//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.network.commands;

import java.io.IOException;
import java.util.ArrayList;

import api.DebugFile;
import api.listener.events.ServerPingEvent;
import api.mod.ModInfo;
import api.mod.ServerModInfo;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.schema.schine.network.Command;
import org.schema.schine.network.NetworkProcessor;
import org.schema.schine.network.client.ClientStateInterface;
import org.schema.schine.network.server.ServerProcessor;
import org.schema.schine.network.server.ServerStateInterface;

public class GetInfo extends Command {
    private static final byte INFO_VERSION = 3;
    private long started;

    public GetInfo() {
        this.mode = 1;
    }

    public void clientAnswerProcess(Object[] var1, ClientStateInterface var2, short var3) {
        DebugFile.log("BRUH BRUH BRUH");
        byte infoVersion = (Byte)var1[0];
        System.err.println("Client info version: " + infoVersion);
        String serverVersion = var1[1].toString();
        String serverName = (String)var1[2];
        String serverDescription = (String)var1[3];
        long startTime = (Long)var1[4];
        int players = (Integer)var1[5];
        int maxPlayers = (Integer)var1[6];
        System.currentTimeMillis();
        long var10000 = this.started;
        System.out.println("[CLIENT][INFO]: CLIENT INFO ");
        System.out.println("[CLIENT][INFO]: Version: " + serverVersion);
        System.out.println("[CLIENT][INFO]: Name: " + serverName);
        System.out.println("[CLIENT][INFO]: Description: " + serverDescription);
        System.out.println("[CLIENT][INFO]: Started: " + startTime);
        System.out.println("[CLIENT][INFO]: Players: " + players + "/" + maxPlayers);

    }
    int randInt(int min, int max){
        return min+((int) (Math.random()*(max-min)));

    }
    public void serverProcess(ServerProcessor var1, Object[] var2, ServerStateInterface var3, short var4) throws Exception {
        //INSERTED CODE -- SENDS MOD INFO TO CLIENTS
        String version = String.valueOf(var3.getVersion());
        long var8 = var3.getStartTime();
        int connectedClients = var3.getClients().size();
        int maxClients = var3.getMaxClients();
        ServerPingEvent event = new ServerPingEvent(version, var3.getServerName(), var3.getServerDesc(), connectedClients, maxClients);
        StarLoader.fireEvent(ServerPingEvent.class, event);

        System.err.println("[SERVER] This client is an info ping (server-lists): " + var1.getClientIp() + "; PID: " + var1.id);
        var1.setInfoPinger(true);
        //I am attaching the info to GetInfo, so that later there can be info about if a server is modded or not, and what mods it has.
        //Put all server mods into the return. To be moved later
        ArrayList<ModInfo> serverMods = new ArrayList<>();
        for (StarMod mod : StarLoader.starMods) {
            serverMods.add(mod.getInfo());
        }

        ArrayList<Object> clientReturn = new ArrayList<>();
        clientReturn.add(INFO_VERSION);
        clientReturn.add(event.getVersion());
        clientReturn.add(event.getName());
        clientReturn.add(event.getDescription());
        clientReturn.add(var8);
        clientReturn.add(event.getPlayers());
        clientReturn.add(event.getMaxPlayers());
        for (ModInfo mod : serverMods){
            clientReturn.add(mod.serialize());
        }
        this.createReturnToClient(var3, var1, var4, clientReturn.toArray());
        //this.createReturnToClient(var3, var1, var4, new Object[]{INFO_VERSION, event.getVersion(), event.getName(), event.getDescription(), var8, event.getPlayers(), event.getMaxPlayers()});
        var1.disconnectAfterSent();
    }

    public void writeAndCommitParametriziedCommand(Object[] var1, int var2, int var3, short var4, NetworkProcessor var5) throws IOException {
        this.started = System.currentTimeMillis();
        super.writeAndCommitParametriziedCommand(var1, var2, var3, var4, var5);
    }
}
