package api.mod;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.luaj.vm2.ast.Str;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SMDModData {
    //Index all mods from SMD with Name -> [resource id, resource date]
    private static SMDModData instance;
    private final HashMap<String, SMDModInfo> allModData = new HashMap<String, SMDModInfo>();
    public static SMDModData getInstance() {
        if(instance == null){
            instance = new SMDModData();

        }
        return instance;
    }
    public SMDModInfo getModData(String name){
        return allModData.get(name);
    }

    private SMDModData(){
        JsonArray mods = SMDUtils.getSMDMods();
        if(mods == null) {
            return;
        }
        for (JsonElement jsonModElement : mods) {
            JsonObject jsonModObject = jsonModElement.getAsJsonObject();
            SMDModInfo info = SMDModInfo.fromJson(jsonModObject);
            allModData.put(info.getName(), info);
            System.out.println("[StarLoader SMDModData] Fetched mod from SMD: " + info);
        }
    }
    public int getLastUpdateDate(String modName){
        return allModData.get(modName).getResourceDate();
    }

    public static void main(String[] args) throws IOException {
        String hotkey = getInstance().getDownloadURL("Turret Hotkey");
        System.out.println(hotkey);
    }
    public String getDownloadURL(String modName) throws IOException {
        int resId = SMDModData.getInstance().getSMDId(modName);
        HttpURLConnection con = SMDUtils.GET("resources/" + resId);
        JsonParser parser = new JsonParser();
        String raw = IOUtils.toString(con.getInputStream(), "UTF-8");
        JsonObject parse = parser.parse(raw).getAsJsonObject();
        JsonObject resObject = parse.get("resource").getAsJsonObject();
        JsonArray currentFileArray = resObject.get("current_files").getAsJsonArray();
        JsonObject firstDownload = currentFileArray.get(0).getAsJsonObject();
        return firstDownload.get("download_url").getAsString();
    }

    public int getSMDId(String name) {
        return allModData.get(name).getResourceId();
    }

    public Collection<String> getModDataMap() {
        return allModData.keySet();
    }
}
//Scraped info put into a class
class SMDModInfo{
    private int resourceId;
    private int resourceDate;
    private String gameVersion;
    private int downloadCount;
    private float ratingAverage;
    private String tagLine;
    private ArrayList<String> tags;
    private String name;
    private String iconURL;
    public static SMDModInfo fromJson(JsonObject jsonModObject){
        SMDModInfo inst = new SMDModInfo();
        inst.name = jsonModObject.get("title").getAsString();
        inst.resourceId = jsonModObject.get("resource_id").getAsInt();
        inst.resourceDate = jsonModObject.get("last_update").getAsInt();
        JsonElement element = jsonModObject.get("custom_fields").getAsJsonObject().get("Gameversion");
        if(element == null){
            inst.gameVersion = "?";
        }else {
            inst.gameVersion = element.getAsString();
        }
        inst.downloadCount = jsonModObject.get("download_count").getAsInt();
        inst.ratingAverage = jsonModObject.get("rating_avg").getAsFloat();
        inst.tagLine = jsonModObject.get("tag_line").getAsString();
        JsonArray tags = jsonModObject.get("tags").getAsJsonArray();
        ArrayList<String> tagArray = new ArrayList<String>(tags.size());
        for (int i = 0; i < tags.size(); i++) {
            tagArray.add(tags.get(i).getAsString());
        }
        inst.tags = tagArray;
        JsonElement url = jsonModObject.get("icon_url");
        if(!(url instanceof JsonNull)) {
            inst.iconURL = url.getAsString();
        }

        return inst;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getResourceDate() {
        return resourceDate;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public float getRatingAverage() {
        return ratingAverage;
    }

    public String getTagLine() {
        return tagLine;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public String getIconURL() {
        return iconURL;
    }

    @Override
    public String toString() {
        return "SMDModInfo{" +
                "resourceId=" + resourceId +
                ", resourceDate=" + resourceDate +
                ", gameVersion='" + gameVersion + '\'' +
                ", downloadCount=" + downloadCount +
                ", ratingAverage=" + ratingAverage +
                ", tagLine='" + tagLine + '\'' +
                ", tags=" + tags +
                ", name='" + name + '\'' +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
