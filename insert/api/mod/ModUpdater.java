package api.mod;

import api.DebugFile;
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
        //Should overwrite existing files in the vm, or so I hope.
        StarMod starMod = SMModLoader.loadModFromJar(new URLClassLoader(url), new JarFile(fileName));
    }
    public static void checkUpdateAll(){
        for (StarMod mod : StarLoader.starMods) {
            SMDModInfo data = SMDModData.getInstance().getModData(mod.getName());
            if(data != null) {
                int resDate = data.getResourceDate();
                int installedResDate = ModDataFile.getInstance().getDownloadDate(mod.getName());
                DebugFile.log(installedResDate + " [Installed] ==> [Remote] " + resDate);
                if(installedResDate < resDate){
                    DebugFile.log("Mod: " + mod.getName() + " out of date. Updating...");
                    try {
                        downloadAndLoadMod(mod.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        DebugFile.err("An error occured while downloading mod, whatever.");
                    }
                }else if (resDate < installedResDate){
                    DebugFile.log("If you are seeing this, something REALLY went wrong");
                    DebugFile.log(installedResDate + " [Installed] ==> [Remote] " + resDate);
                }else{
                    DebugFile.log("Mod up to date");
                }
            }else{
                DebugFile.warn("Could not find mod: " + mod.getName() + " on StarMadeDock, not trying to update it.");
            }
        }
    }
}
