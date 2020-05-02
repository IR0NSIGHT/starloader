package api.mod;

import api.DebugFile;
import api.listener.Listener;
import api.listener.events.Event;
import api.main.GameClient;
import api.main.GameServer;
import api.server.Server;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.game.common.data.SendableGameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StarLoader {
    public static ArrayList<StarMod> starMods = new ArrayList<StarMod>();
    public static HashMap<Class<? extends Event>, ArrayList<Listener>> listeners = new HashMap<Class<? extends Event>, ArrayList<Listener>>();

    public static void clearListeners() {
        listeners.clear();
    }

    public static SendableGameState getGameState() {
        if (GameServer.getServerState() != null) {
            return GameServer.getServerState().getGameState();
        } else if (GameClient.getClientState() != null) {
            return GameClient.getClientState().getGameState();
        }
        //Probably in the main menu or something
        return null;
    }

    public static List<Listener> getListeners(Class<? extends Event> clazz) {
        return listeners.get(clazz);
    }

    public static void enableMod(StarMod mod) {
        DebugFile.log("== Enabling Mod " + mod.getInfo().toString());
        mod.onEnable();
        mod.flagEnabled(true);
    }

    public static void dumpModInfos() {
        for (StarMod mod : StarLoader.starMods) {
            DebugFile.log(mod.toString());
        }
    }
    public static void registerListener(Class<? extends Event> clazz, Listener l) {
        registerListener(clazz, l, null);
    }

    public static void registerListener(Class<? extends Event> clazz, Listener l, StarMod mod) {

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
    }

    //fire event methods:
    public static void fireEvent(Class<? extends Event> clazz, Event ev) {
        //DebugFile.log("Firing Event: " +clazz.getName());
        List<Listener> lstners = getListeners(clazz);
        if (lstners == null) // Avoid iterating on null Event listeners
            return ;
        for (Listener listener : lstners) {
            try {
                listener.onEvent(ev);
            } catch (Exception e) {
                DebugFile.log("Exception during event: " + clazz.getName());
                DebugFile.logError(e, null);
                if (Server.isInitialized()) {
                    Server.broadcastMessage("An error occurred during event: " + clazz);
                }

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
