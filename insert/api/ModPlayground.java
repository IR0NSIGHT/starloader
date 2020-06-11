package api;

import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.KeyPressEvent;
import api.listener.events.gui.ControlManagerActivateEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.listener.events.register.ElementRegisterEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.network.Packet;
import api.network.packets.ServerToClientMessage;
import api.utils.StarRunnable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.PlayerPanel;
import org.schema.game.client.view.gui.faction.newfaction.FactionPanelNew;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.network.RegisteredClientOnServer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
        this.forceEnable = true;
    }

    public static short newCapId = 0;

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
    }
    public void registerComputerModulePair(short computer, short module){

        ElementInformation comp = ElementKeyMap.infoArray[computer];
        comp.mainCombinationController = true;
        comp.systemBlock = true;
        comp.controlledBy.add((short) 1);
        comp.controlling.add(module);

        ElementKeyMap.infoArray[module].controlledBy.add(computer);

    }
    public void registerElementBlock(ElementInformation info){
        info.systemBlock = true;
        info.controlledBy.add((short) 1);

    }

    public static void initBlockData() {
        DebugFile.log("Initializing block data for enabled mods: ");
        final BlockConfig config = new BlockConfig();
        for (StarMod mod : StarLoader.starMods) {
            if(mod.isEnabled()) {
                DebugFile.log("Initializing block for mod: " + mod.modName);
                mod.onBlockConfigLoad(config);
            }
        }

        //Regenerate LOD shapes/Factory enhancers, rather than just obliterating the list in addElementToExisting
        for (Map.Entry<Short, ElementInformation> next : ElementKeyMap.informationKeyMap.entrySet()) {
            Short keyId = next.getKey();
            ElementKeyMap.lodShapeArray[keyId] = next.getValue().hasLod();
            ElementKeyMap.factoryInfoArray[keyId] = ElementKeyMap.getFactorykeyset().contains(keyId);
        }
        /*for (ElementInformation element : config.getElements()) {
            try {
                ElementKeyMap.addInformationToExisting(element);
            } catch (ParserConfigurationException e) {
                DebugFile.logError(e, null);
                e.printStackTrace();
            }
        }*/
    }
    public static void broadcastMessage(String message) {
        for (RegisteredClientOnServer client : GameServerState.instance.getClients().values()) {
            try {
                client.serverMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");

        Packet.registerPacket(ServerToClientMessage.class);

//        StarLoader.registerListener(MaxPowerCalculateEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                MaxPowerCalculateEvent e = (MaxPowerCalculateEvent) event;
//                DebugFile.info("Original Power: " + e.getPower());
//                BatteryElementManager elementManager = e.getEntity().getElementManager(BatteryElementManager.class);
//                double extraPower = (double) elementManager.totalSize*100;
//                DebugFile.info("Extra Power: " + e.getPower());
//                e.setPower(e.getPower()+extraPower);
//            }
//        });
        getConfig().saveDefault("this is a: test");
        StarLoader.registerListener(ControlManagerActivateEvent.class, new Listener<ControlManagerActivateEvent>() {
            @Override
            public void onEvent(ControlManagerActivateEvent event) {
                broadcastMessage("Activated: " + event.getControlManager().getClass().getName() + " >>> " + event.isActive());
                PlayerPanel playerPanel = GameClientState.instance.getWorldDrawer().getGuiDrawer().getPlayerPanel();
                try {
                    Field panel = PlayerPanel.class.getDeclaredField("factionPanelNew");
                    panel.setAccessible(true);
                    FactionPanelNew p = (FactionPanelNew) panel.get(playerPanel);
                    if(!(p instanceof MyFactionPanelNew)) {
                        GameClientState state = playerPanel.getState();
                        panel.set(playerPanel, new MyFactionPanelNew(state));
                    }
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }, null);
//        StarLoader.registerListener(PlayerCommandEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                PlayerCommandEvent e = (PlayerCommandEvent) event;
//                Player p = e.player;
//                if(e.command.toLowerCase(Locale.ENGLISH).equals("test")){
//                    DebugFile.log("Test called", getMod());
//                    Entity currentEntity = p.getCurrentEntity();
//                    if(currentEntity == null){
//                        p.sendServerMessage("You are in: nothing, thanks for playing");
//                    }else{
//                        p.sendServerMessage("You are in: " + currentEntity.getUID());
//                    }
//                }else if(e.command.equals("thr")){
//                    Entity currentEntity = p.getCurrentEntity();
//                    if(currentEntity == null){
//                        p.sendServerMessage("no");
//                        return;
//                    }
//                    BatteryElementManager elementManager = currentEntity.getElementManager(BatteryElementManager.class);
//                    float actualThrust = elementManager.totalSize;
//                    Server.broadcastMessage("The total thrust of this object is: " + actualThrust);
//                }else if(e.command.equals("a")){
//                    p.sendPacket(new ServerToClientMessage(p.getFaction(), "hi"));
//                }
//            }
//        });
//
//
//        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                HudCreateEvent ev = (HudCreateEvent) event;
//                BasicInfoGroup bar = new BasicInfoGroup(ev);
//            }
//        });
//
//
////        final int[] t = {0};
////        new StarRunnable() {
////            @Override
////            public void run() {
////                t[0] += 4;
////            }
////        }.runTimer(1);
////        StarLoader.registerListener(CannonShootEvent.class, new Listener() {
////            @Override
////            public void onEvent(Event event) {
////                CannonShootEvent e = (CannonShootEvent) event;
////                Color hsb = Color.getHSBColor(((float) t[0] % 360) / 360F, 1F, 1F);
////                Vector4f tuple4f = new Vector4f(hsb.getRed() / 255F, hsb.getGreen() / 255F, hsb.getBlue() / 255F, 1F);
////                e.setColor(tuple4f);
////            }
////        });
//
//        StarLoader.registerListener(RegisterEffectsEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                RegisterEffectsEvent ev = (RegisterEffectsEvent) event;
//                for (StatusEffectType types : StatusEffectType.values()) {
//                    if (types.name().contains("CUSTOM")) {
//                        ev.addEffectModifier(types, 10F);
//                    }
//                }
//            }
//        });
    }
}
