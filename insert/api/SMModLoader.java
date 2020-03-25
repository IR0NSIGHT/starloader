package api;

import api.DebugFile;
import api.ModPlayground;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.schema.game.client.view.gui.inventory.InventorySlotOverlayElement;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.elements.scanner.ScanAddOn;
import org.schema.game.common.data.player.PlayerState;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.Attributes.Name;

import javax.swing.*;

public class SMModLoader {
    public static final File modFolder = new File("mods");

    static {
        if (!modFolder.exists()) {
            modFolder.mkdir();
        }
    }

    public SMModLoader() {
    }

    public static StarMod loadModFromJar(ClassLoader loader, JarFile jf) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        DebugFile.log("Loading Mod: " + jf.getName());
        Class<?> c = loader.loadClass(jf.getManifest().getMainAttributes().getValue(Name.MAIN_CLASS));
        Object o = c.getConstructors()[0].newInstance();
        if (!(o instanceof StarMod)) {
            DebugFile.err("Failed to load plugin! not instanceof StarMod.");
            throw new IllegalArgumentException("Main class must be an instance of StarMod");
        } else {
            StarMod sMod = ((StarMod) o);
            DebugFile.log("Registering mod...");
            sMod.onGameStart();
            DebugFile.log("Mod loaded: " + sMod.toString());
            if (!sMod.isValid()) {
                DebugFile.err("MOD LOAD FAILED :: Please set your mod name and version in the onGameStart using the provided setters.");
            } else {
                StarLoader.starMods.add(sMod);
            }
            return sMod;
        }
    }

    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, "you have started starmade, press OK to claim your reward");
        DebugFile.clear();
        DebugFile.log("Starting starloader...");
        DebugFile.log("Loading default mod...");
        ModPlayground defaultMod = new ModPlayground();
        StarLoader.starMods.add(defaultMod);
        defaultMod.onGameStart();
        defaultMod.onEnable();
        defaultMod.flagEnabled(true);

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
                loadModFromJar(cl, jf);
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
