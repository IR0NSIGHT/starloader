//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.network.commands;

import java.io.IOException;

import api.listener.events.ServerPingEvent;
import api.mod.StarLoader;
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
        byte bruh =(Byte)var1[0];
        System.err.println("bruh moment: " + bruh);
        String var9 = var1[1].toString();
        String var10 = (String)var1[2];
        String var4 = (String)var1[3];
        long var6 = (Long)var1[4];
        int var5 = (Integer)var1[5];
        int var8 = (Integer)var1[6];
        System.currentTimeMillis();
        long var10000 = this.started;
        System.out.println("[CLIENT][INFO]: CLIENT INFO ");
        System.out.println("[CLIENT][INFO]: Version: " + var9);
        System.out.println("[CLIENT][INFO]: Name: " + var10);
        System.out.println("[CLIENT][INFO]: Description: " + var4);
        System.out.println("[CLIENT][INFO]: Started: " + var6);
        System.out.println("[CLIENT][INFO]: Players: " + var5 + "/" + var8);
    }
    int randInt(int min, int max){
        return min+((int) (Math.random()*(max-min)));

    }
    public void serverProcess(ServerProcessor var1, Object[] var2, ServerStateInterface var3, short var4) throws Exception {
        String version = String.valueOf(var3.getVersion());
        //String var5 = var3.getServerName();
        //String var5 = "⬛⬛⬛⬛⬛⬛⬛⬛ Briere 2 ⬛⬛⬛⬛⬛⬛⬛⬛ cool squares bro";
        long var8 = var3.getStartTime();
        int connectedClients = var3.getClients().size();
        int maxClients = var3.getMaxClients();
        ServerPingEvent event = new ServerPingEvent(version, var3.getServerName(), var3.getServerDesc(), connectedClients, maxClients);
        StarLoader.fireEvent(ServerPingEvent.class, event);

        System.err.println("[SERVER] This client is an info ping (server-lists): " + var1.getClientIp() + "; PID: " + var1.id);
        var1.setInfoPinger(true);

        this.createReturnToClient(var3, var1, var4, new Object[]{INFO_VERSION, event.getVersion(), event.getName(), event.getDescription(), var8, event.getPlayers(), event.getMaxPlayers()});
        var1.disconnectAfterSent();
    }

    public void writeAndCommitParametriziedCommand(Object[] var1, int var2, int var3, short var4, NetworkProcessor var5) throws IOException {
        this.started = System.currentTimeMillis();
        super.writeAndCommitParametriziedCommand(var1, var2, var3, var4, var5);
    }
}
