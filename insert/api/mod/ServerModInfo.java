package api.mod;

import api.DebugFile;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerModInfo {
    private static HashMap<String, ArrayList<ModInfo>> serverModInfo = new HashMap<>();
    public static void registerModInfo(String server, ModInfo info){
        DebugFile.log("[Client] Registering info: " + info.toString());
        ArrayList<ModInfo> infos = serverModInfo.get(server);
        if(infos == null){
            ArrayList<ModInfo> emptyModList = new ArrayList<>();
            serverModInfo.put(server, emptyModList);
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

    public static ArrayList<ModInfo> getServerInfo(String serverName) {
        return serverModInfo.get(serverName);
    }
}
