//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import api.DebugFile;
import api.ModPlayground;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.schema.game.client.view.gui.inventory.InventorySlotOverlayElement;
import org.schema.game.common.Starter;
import org.schema.game.common.data.player.PlayerState;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.Attributes.Name;

import javax.swing.*;

public class SMModLoader {
    public static final File modFolder = new File("SMMods");

    static {
        if (!modFolder.exists()) {
            modFolder.mkdir();
        }

    }

    public SMModLoader() {
    }

    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, "you have started starmade, press OK to claim your reward");
        DebugFile.clear();
        DebugFile.log("Starting starloader...");
        if (Arrays.asList(args).contains("-nomods")) {
            try {
                Starter.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DebugFile.log("Loading default mod...");
            ModPlayground defaultMod = new ModPlayground();
            defaultMod.register();
            defaultMod.onEnable();

            DebugFile.log("Enabling Mods...");
            File[] files = modFolder.listFiles();
            URL[] urls = new URL[files.length];

            for (int i = 0; i < urls.length; ++i) {
                try {
                    urls[i] = files[i].toURI().toURL();
                } catch (MalformedURLException var11) {
                    var11.printStackTrace();
                }
            }

            URLClassLoader cl = new URLClassLoader(urls);
            File[] var7 = files;
            int var6 = files.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                File f = var7[var5];

                try {
                    JarFile jf = new JarFile(f);
                    DebugFile.log("Loading Mod: " + jf.getName());
                    Class<?> c = cl.loadClass(jf.getManifest().getMainAttributes().getValue(Name.MAIN_CLASS));
                    Object o = c.getConstructors()[0].newInstance();
                    if(!(o instanceof StarMod)){
                        DebugFile.log("Failed to load plugin! not instanceof StarMod! We'll invoke main anyway...");
                    }else {
                        StarMod sMod = ((StarMod) o);
                        DebugFile.log("Enabling mod...");
                        sMod.onEnable();
                        StarLoader.starMods.add(sMod);
                    }
                    jf.close();
                    //c.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugFile.log("MOD LOAD FAILED::::");
                    DebugFile.logError(e, null);
                }
            }
            try {
                DebugFile.log("Starting StarMade...");
                Starter.main(args);
            } catch (IOException e) {
                e.printStackTrace();
                DebugFile.logError(e, null);
            }
        }
    }
}
