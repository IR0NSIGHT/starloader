package api.mod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class SMDModData {
    //Index all mods from SMD with Name -> [resource id, resource date]
    private static SMDModData instance;
    private HashMap<String, Pair<Integer, Integer>> allModData = new HashMap<>();
    public static SMDModData getInstance() {
        if(instance == null){
            instance = new SMDModData();

        }
        return instance;
    }
    public Pair<Integer, Integer> getModData(String name){
        return allModData.get(name);
    }

    private SMDModData(){
        JsonArray mods = SMDUtils.getSMDMods();
        for (JsonElement jsonModElement : mods) {
            JsonObject jsonModObject = jsonModElement.getAsJsonObject();
            String modTitle = jsonModObject.get("title").getAsString();
            int modResourceId = jsonModObject.get("resource_id").getAsInt();
            int modResourceDate = jsonModObject.get("last_update").getAsInt();
//            int modResourceDate = jsonModObject.get("resource_date").getAsInt();
            allModData.put(modTitle, new ImmutablePair<Integer, Integer>(modResourceId, modResourceDate));
            System.out.println("[StarLoader SMDModData] Fetched mod from SMD: " + modTitle + ", " + modResourceId + "");
        }
    }
    public int getLastUpdateDate(String modName){
        return allModData.get(modName).getRight();
    }

    public static void main(String[] args) throws IOException {
        String hotkey = getInstance().getDownloadURL("Turret Hotkey");
        System.out.println(hotkey);
    }
    public String getDownloadURL(String modName) throws IOException {
        int resId = SMDModData.getInstance().getSMDId(modName);
        HttpURLConnection con = SMDUtils.GET("resources/" + resId);
        JsonParser parser = new JsonParser();
        String raw = IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8);
        JsonObject parse = parser.parse(raw).getAsJsonObject();
        JsonObject resObject = parse.get("resource").getAsJsonObject();
        JsonArray currentFileArray = resObject.get("current_files").getAsJsonArray();
        JsonObject firstDownload = currentFileArray.get(0).getAsJsonObject();
        return firstDownload.get("download_url").getAsString();
    }

    public int getSMDId(String name) {
        return allModData.get(name).getLeft();
    }
}
