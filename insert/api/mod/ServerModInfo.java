package api.mod;

import api.DebugFile;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerModInfo {
    public static String getServerUID(String name, int port){
        return name + ":" + port;
    }
    private static HashMap<String, ArrayList<String>> serverModInfo = new HashMap<>();
    public static void registerModInfo(String serverUid, String modName){
        DebugFile.log("[Client] Registering server mod: " + modName + ", for: " + serverUid);
        ArrayList<String> infos = serverModInfo.get(serverUid);
        if(infos == null){
            ArrayList<String> emptyModList = new ArrayList<String>();
            serverModInfo.put(serverUid, emptyModList);
            infos = emptyModList;
        }
        for (String allInfo : infos){
            if(allInfo.equals(modName)){
                DebugFile.log("Already registered, likely the user clicked refresh");
                return;
            }
        }
        infos.add(modName);
    }
    public static void dumpModInfos(){
        DebugFile.log("====== Dumping server mod info ======");
        for (String server : serverModInfo.keySet()){
            DebugFile.log("Server: " + server + ":");
            for (String info : serverModInfo.get(server)){
                DebugFile.log(" has mod: " + info);
            }
        }
        DebugFile.log("====== End ======");
    }

    public static ArrayList<String> getServerInfo(String serverUid) {
        return serverModInfo.get(serverUid);
    }
}
