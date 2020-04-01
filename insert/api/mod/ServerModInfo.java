package api.mod;

import api.DebugFile;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerModInfo {
    public static String getServerUID(String name, int port){
        return name + ":" + port;
    }
    private static HashMap<String, ArrayList<ModInfo>> serverModInfo = new HashMap<>();
    public static void registerModInfo(String serverUid, ModInfo info){
        DebugFile.log("[Client] Registering server mod info: " + info.toString() + ", for: " + serverUid);
        ArrayList<ModInfo> infos = serverModInfo.get(serverUid);
        if(infos == null){
            ArrayList<ModInfo> emptyModList = new ArrayList<>();
            serverModInfo.put(serverUid, emptyModList);
            infos = emptyModList;
        }
        for (ModInfo allInfo : infos){
            if(allInfo.name.equals(info.name)){
                DebugFile.log("Already registered, likely the user clicked refresh");
                return;
            }
        }
        infos.add(info);
    }
    public static void dumpModInfos(){
        DebugFile.log("====== Dumping server mod info ======");
        for (String server : serverModInfo.keySet()){
            DebugFile.log("Server: " + server + ":");
            for (ModInfo info : serverModInfo.get(server)){
                DebugFile.log(" has mod: " + info.toString());
            }
        }
        DebugFile.log("====== End ======");
    }

    public static ArrayList<ModInfo> getServerInfo(String serverUid) {
        return serverModInfo.get(serverUid);
    }
}
