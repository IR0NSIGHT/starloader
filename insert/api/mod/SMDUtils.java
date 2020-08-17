package api.mod;

import api.DebugFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//XF refers to "XenForo" the forum software SMD uses
//SMD refers to "StarMade Dock"
public class SMDUtils {
    public static JsonArray getSMDMods() {
        //6 = XF id for category "mods"
        try {
            HttpURLConnection get = GET("resource-categories/6/resources");
            return getJsonArray(IOUtils.toString(get.getInputStream(), "UTF-8"), "resources");
        } catch (IOException e) {
            e.printStackTrace();
            DebugFile.log("Could not get resource-categories/6/resources from server");
        }
        return null;
    }

    private static JsonArray getJsonArray(String raw, String name) {
        JsonParser parser = new JsonParser();
        JsonElement parse = parser.parse(raw);
        JsonObject jObject = parse.getAsJsonObject();
        return jObject.getAsJsonArray(name);
    }

    public static void main(String[] args) throws IOException {
        HttpURLConnection get = GET("resource-categories/6/resources");
        System.out.println(IOUtils.toString(get.getInputStream()));
    }
//    public static JsonObject getSMDMod(String name) throws IOException {
//        int resId = SMDModData.getInstance().getSMDId(name);
//        HttpURLConnection con = GET("resources/" + resId);
//        JsonArray array = getJsonArray(IOUtils.toString(con.getInputStream(), StandardCharsets.UTF_8), "");
//        return array.getAsJsonObject();
//    }
    public static void downloadMod(String name) throws IOException {
        //Get download URL
        DebugFile.log("Attempting to download mod: " + name);
        SMDModData instance = SMDModData.getInstance();
        SMDModInfo data = instance.getModData(name);
        String downloadURL = SMDModData.getInstance().getDownloadURL(name);
        downloadURL = downloadURL.substring("https://starmadedock.net/api/".length());
        //Download to file
        InputStream stream = GET(downloadURL).getInputStream();
        FileUtils.copyInputStreamToFile(stream, new File(name + ".jar"));
        //Put in SMDModData
        ModDataFile.getInstance().onDownloadedMod(name, data.getResourceDate());
    }

    //resource-categories/{id}/
    public static HttpURLConnection GET(String request) throws IOException {
        URL url = new URL("https://starmadedock.net/api/" + request);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.setRequestMethod("GET");
        openConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        openConnection.setRequestProperty("XF-Api-Key", "RSVcV-pNXnzaZgHTths0Qd11WsNJ_EK7");
        openConnection.setRequestProperty("User-Agent", "StarMade-Client");
//        System.out.println("RCode: " + openConnection.getResponseCode());
//        System.out.println(openConnection.getResponseMessage());
        return openConnection;
//        System.out.println(text);
    }
    public static HttpURLConnection getSMFile(String request) throws IOException {
        URL url = new URL("https://files.star-made.org/" + request);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.setRequestMethod("GET");
        openConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        openConnection.setRequestProperty("User-Agent", "StarMade-Client");
//        System.out.println("RCode: " + openConnection.getResponseCode());
//        System.out.println(openConnection.getResponseMessage());
        return openConnection;
//        System.out.println(text);
    }
}
