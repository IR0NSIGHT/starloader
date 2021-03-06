package api.mod;

import api.DebugFile;
import api.listener.Listener;
import api.listener.events.Event;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.server.data.GameServerState;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StarLoader {
    //TODO Make a hashmap and index via mod name
    public static ArrayList<StarMod> starMods = new ArrayList<StarMod>();
    public static HashMap<Class<? extends Event>, ArrayList<Listener>> listeners = new HashMap<Class<? extends Event>, ArrayList<Listener>>();

    public static void clearListeners() {
        listeners.clear();
    }

    public static SendableGameState getGameState() {
        if (GameServerState.instance != null) {
            return GameServerState.instance.getGameState();
        } else if (GameClientState.instance != null) {
            return GameClientState.instance.getGameState();
        }
        //Probably in the main menu or something
        return null;
    }

    public static List<Listener> getListeners(Class<? extends Event> clazz) {
        return listeners.get(clazz);
    }

    public static void enableMod(StarMod mod) {
        DebugFile.log("== Enabling Mod " + mod.getName());
        mod.onEnable();
        mod.flagEnabled(true);
        DebugFile.log("== Mod " + mod.getName() + " Enabled");
    }

    public static void dumpModInfos() {
        for (StarMod mod : StarLoader.starMods) {
            DebugFile.log(mod.toString());
        }
    }

    public static <T extends Event> void registerListener(Class<T> clazz, Listener<T> l) {
        registerListener(clazz, l, null);
    }

    public static <T extends Event> void registerListener(Class<T> clazz, Listener<T> l, StarMod mod) {

        DebugFile.log("Registering listener " + clazz.getName());
        List<Listener> listeners = StarLoader.getListeners(clazz);
        l.setMod(mod);
        if (listeners == null) {
            ArrayList<Listener> new_listeners = new ArrayList<Listener>();
            new_listeners.add(l);
            StarLoader.listeners.put(clazz, new_listeners);
        } else {
            listeners.add(l);
        }
        DebugFile.log(" = Registered Listener. ");
    }

    //fire event methods:
    public static <T> void fireEvent(Class<? extends Event> clazz, Event ev, boolean isServer) {
        //DebugFile.log("Firing Event: " +clazz.getName());
        ev.server = isServer;
        List<Listener> lstners = getListeners(clazz);
        if (lstners == null) // Avoid iterating on null Event listeners
            return ;
        for (Listener listener : lstners) {
            try {
                listener.onEvent(ev);
            } catch (Exception e) {
                DebugFile.log("Exception during event: " + clazz.getName());
                DebugFile.logError(e, null);

            }
        }
    }
    public static <T> void fireEvent(Event ev, boolean isServer) {
        //DebugFile.log("Firing Event: " +clazz.getName());
        ev.server = isServer;
        List<Listener> lstners = getListeners(ev.getClass());
        if (lstners == null) // Avoid iterating on null Event listeners
            return ;
        for (Listener listener : lstners) {
            try {
                listener.onEvent(ev);
            } catch (Exception e) {
                DebugFile.log("Exception during event: " + ev.getClass().getSimpleName());
                DebugFile.logError(e, null);

            }
        }
    }

    private static ArrayList<ImmutablePair<String, String>> commands = new ArrayList<ImmutablePair<String, String>>();

    public static void registerCommand(String name, String desc) {
        commands.add(new ImmutablePair<String, String>(name, desc));
    }

    public static ArrayList<ImmutablePair<String, String>> getCommands() {
        return commands;
    }

}
