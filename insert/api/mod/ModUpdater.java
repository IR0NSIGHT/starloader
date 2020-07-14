package api.mod;

import api.SMModLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.schema.schine.graphicsengine.core.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class ModUpdater {
    public static void downloadAndLoadMod(String modName) throws IOException, InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        StarMod oldMod = ModStarter.fromName(modName);
        StarLoader.starMods.remove(oldMod);

        SMDUtils.downloadMod(modName);
        String fileName = "mods/" + modName + ".jar";
        URL[] url = new URL[]{new File(fileName).toURI().toURL()};
        StarMod starMod = SMModLoader.loadModFromJar(new URLClassLoader(url), new JarFile(fileName));
    }
    public static void checkUpdateAll(){
        for (StarMod mod : StarLoader.starMods) {
            Pair<Integer, Integer> data = SMDModData.getInstance().getModData(mod.getName());
            Integer resDate = data.getRight();
            int installedResDate = ModDataFile.getInstance().getDownloadDate(mod.getName());
            System.out.println(installedResDate + " [Installed] ==> [Remote] " + resDate);
        }
    }
}
