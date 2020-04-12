//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.network;

import api.DebugFile;
import api.mod.ModInfo;
import api.mod.ModStarter;
import api.mod.ServerModInfo;
import api.server.Server;

public class ServerInfo extends AbstractServerInfo {
    public static long curtime;
    private final Byte infoVersion;
    private final String version;
    private final String name;
    private final String desc;
    private final Long startTime;
    private final Integer playerCount;
    private final Integer maxPlayers;
    private final long ping;
    private final String host;
    private final int port;
    private final String connType;
    public String ip;
    public boolean reachable;

    public ServerInfo(String host, int port, Object[] arr, long var4, String connectionType) {
        //INSERTED CODE
        //Skip all of the info about players and version by starting at 7
        System.err.println("[Starloader][ServerInfo] Registering info for: " + host);
        for (int i = 7; i < arr.length; i++) {
            String serializedInfo = (String) arr[i];
            ServerModInfo.registerModInfo(ServerModInfo.getServerUID(host, port), ModInfo.fromString(serializedInfo));
        }
        //ServerModInfo.dumpModInfos();
        ///
        this.host = host;
        this.port = port;
        this.infoVersion = (Byte)arr[0];
        this.version = arr[1].toString();
        this.name = (String)arr[2];
        this.desc = (String)arr[3];
        this.startTime = (Long)arr[4];
        this.playerCount = (Integer)arr[5];
        this.maxPlayers = (Integer)arr[6];
        this.ping = var4;
        this.connType = connectionType;
    }

    public ServerInfo(String var1, int var2) {
        this.host = var1;
        this.port = var2;
        this.infoVersion = 0;
        this.version = "n/a";
        this.name = "n/a";
        this.desc = "n/a";
        this.startTime = 0L;
        this.playerCount = 0;
        this.maxPlayers = 0;
        this.ping = 99999L;
        this.connType = "n/a";
        this.reachable = false;
    }

    public Byte getInfoVersion() {
        return this.infoVersion;
    }

    public String getVersion() {
        return this.version;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Integer getPlayerCount() {
        return this.playerCount;
    }

    public Integer getMaxPlayers() {
        return this.maxPlayers;
    }

    public long getPing() {
        return this.ping;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getConnType() {
        return this.connType;
    }

    public boolean isResponsive() {
        return this.reachable;
    }
}
