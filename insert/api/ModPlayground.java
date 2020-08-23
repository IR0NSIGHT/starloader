package api;

import api.common.GameClient;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.gui.MainWindowTabAddEvent;
import api.listener.events.player.PlayerChatEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.listener.events.register.ElementRegisterEvent;
import api.listener.events.register.RegisterAddonsEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.network.Packet;
import api.network.packets.ServerToClientMessage;
import api.utils.PlayerUsableHelper;
import api.utils.addon.SimpleAddOn;
import api.utils.game.SegmentControllerUtils;
import api.utils.gui.SimpleGUIBuilder;
import org.newdawn.slick.Color;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerModuleControllable;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.network.objects.ChatMessage;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.network.RegisteredClientOnServer;

import java.io.IOException;
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

    public static void initBlockData() {
        DebugFile.log("Initializing block data for enabled mods: ");
        final BlockConfig config = new BlockConfig();
        for (StarMod mod : StarLoader.starMods) {
            if(mod.isEnabled()) {
                DebugFile.log("Initializing block for mod: " + mod.getName());
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
    private static SimpleGUIBuilder builder;
    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");
        Packet.registerPacket(ServerToClientMessage.class);
        StarLoader.registerListener(MainWindowTabAddEvent.class, new Listener<MainWindowTabAddEvent>() {
            @Override
            public void onEvent(MainWindowTabAddEvent event) {
                if(event.getTitle().equals(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONPANELNEW_9)){
                    GUIContentPane funny = event.createTab("this is a tab");
                    GUITextOverlay t = new GUITextOverlay(100, 100, event.getPane().getState());
                    t.setFont(FontLibrary.getBlenderProMedium20());
                    t.setColor(Color.cyan);
                    t.setTextSimple("jkdlsjfa sj kjdas skljklj dfasdfklj kl dfkljdfskljkljf kljdas klklj dfsklj dfskl ");
                    funny.getContent(0).attach(t);
                }
            }
        });
        StarLoader.registerListener(RegisterAddonsEvent.class, new Listener<RegisterAddonsEvent>() {
            @Override
            public void onEvent(RegisterAddonsEvent event) {
                event.addModule(new SimpleAddOn(event.getContainer()) {
                    @Override
                    public float getChargeRateFull() {
                        return 10;
                    }

                    @Override
                    public double getPowerConsumedPerSecondResting() {
                        return 0;
                    }

                    @Override
                    public double getPowerConsumedPerSecondCharging() {
                        return 0;
                    }

                    @Override
                    public long getUsableId() {
                        return PlayerUsableHelper.getPlayerUsableId(ElementKeyMap.SHIELD_CAP_ID);
                    }

                    @Override
                    public String getWeaponRowName() {
                        return "Bruhhhhhh";
                    }

                    @Override
                    public short getWeaponRowIcon() {
                        return ElementKeyMap.SHIELD_CAP_ID;
                    }

                    @Override
                    public float getDuration() {
                        return 3;
                    }

                    @Override
                    public boolean onExecute() {
                        ModPlayground.broadcastMessage("cringe!!!!");
                        return true;
                    }

                    @Override
                    public void onActive() {
                        ModPlayground.broadcastMessage("epic!!!!");
                    }

                    @Override
                    public void onInactive() {

                    }

                    @Override
                    public String getName() {
                        return "The NAME";
                    }
                });
            }
        });
        StarLoader.registerListener(PlayerCommandEvent.class, new Listener<PlayerCommandEvent>() {
            @Override
            public void onEvent(PlayerCommandEvent event) {
                PlayerControllable control = GameClient.getCurrentControl();
                if(control instanceof SegmentController){
                    ManagedUsableSegmentController<?> seg = (ManagedUsableSegmentController<?>) control;
                    ReactorElement jumpChamber = SegmentControllerUtils.getChamberFromElement(seg, ElementKeyMap.getInfo(ElementKeyMap.REACTOR_CHAMBER_JUMP_DISTANCE_3));
                    if(jumpChamber != null && jumpChamber.isAllValid()){
                        ModPlayground.broadcastMessage("yes!!!");
                    }else{
                        ModPlayground.broadcastMessage("no");
                    }
                }
            }
        });

//        StarLoader.registerListener(ControlManagerActivateEvent.class, new Listener<ControlManagerActivateEvent>() {
//            @Override
//            public void onEvent(ControlManagerActivateEvent event) {
//                if(!event.isActive()){
//                    builder.setVisible(false);
//                }
//            }
//        });
//
//        StarLoader.registerListener(KeyPressEvent.class, new Listener<KeyPressEvent>() {
//            @Override
//            public void onEvent(KeyPressEvent event) {
//                if(event.getChar() == 'b'){
//                    boolean visibility = !builder.isVisible();
//                    builder.setVisible(visibility);
//                    GameClient.getClientState().getGlobalGameControlManager().getIngameControlManager().getChatControlManager().setActive(visibility);
//                }
//            }
//        });
//
//        StarLoader.registerListener(PlayerGUICreateEvent.class, new Listener<PlayerGUICreateEvent>() {
//            @Override
//            public void onEvent(PlayerGUICreateEvent event) {
//
//                builder = SimpleGUIBuilder.newBuilder("Tab1")
//                        .fixedText("BR uh", Color.white, FontLibrary.getBlenderProMedium20())
//                        .newLine()
//                        .button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        }).button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        }).button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        }).fixedText("yes", Color.blue, FontLibrary.getBoldArial24())
//                        .newLine().button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        }).button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        }).newLine().button("yo", new GUICallback() {
//                            @Override
//                            public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
//
//                            }
//
//                            @Override
//                            public boolean isOccluded() {
//                                return false;
//                            }
//                        });
//                SimpleGUIList<Faction> guiBuilder = new SimpleGUIList<Faction>(GameClient.getClientState(), builder) {
//                    @Override
//                    public void initColumns() {
//                        addSimpleColumn("NAME", 8F, new RowStringCreator<Faction>() {
//                            @Override
//                            public String update(Faction entry) {
//                                return entry.getName();
//                            }
//                        });
//                        addSimpleColumn("yyyyy", 6F, new RowStringCreator<Faction>() {
//                            @Override
//                            public String update(Faction entry) {
//                                return String.valueOf(entry.getIdFaction());
//                            }
//                        });
//                        addSimpleColumn("online", 16F, new RowStringCreator<Faction>() {
//                            @Override
//                            public String update(Faction entry) {
//                                return String.valueOf(entry.getOnlinePlayers().size());
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected Collection<Faction> getElementList() {
//                        return StarLoader.getGameState().getFactionManager().getFactionMap().values();
//                    }
//                };
//                guiBuilder.createEntryButton(GUITextButton.ColorPalette.NEUTRAL, "bruhhh", new EntryCallback<Faction>() {
//                    @Override
//                    public void on(GUIElement self, Faction entry, MouseEvent event) {
//
//                    }
//                });
//                guiBuilder.addDefaultFilter();
//
//                builder.addSimpleGUIList(guiBuilder);
//                builder.scaleCurrentLine();
//            }
//        });
//        StarLoader.registerListener(PlayerGUIDrawEvent.class, new Listener<PlayerGUIDrawEvent>() {
//            @Override
//            public void onEvent(PlayerGUIDrawEvent event) {
//                if(builder == null){
//                    DebugFile.warn("builder null oh boy");
//                    System.err.println("BUILDER NULL");
//                }else{
//                    builder.draw();
//                }
//            }
//        });

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
        getConfig("config").saveDefault("this is a: test");

//        StarLoader.registerListener(DisplayModuleDrawEvent.class, new Listener<DisplayModuleDrawEvent>() {
//            @Override
//            public void onEvent(DisplayModuleDrawEvent event) {
//                event.getBoxElement().text.getText().set(0, "███████•\n" +
//                        "◘\n" +
//                        "○\n" +
//                        "◙\n" +
//                        "►\n" +
//                        "◄\n" +
//                        "↕\n" +
//                        "↑\n" +
//                        "↓\n" +
//                        "▬\n" +
//                        "↔\n" +
//                        "→\n" +
//                        "←\n" +
//                        "■\n" +
//                        "Ω\n" +
//                        "╪    \n" +
//                        "╤\n" +
//                        "╫\n" +
//                        "┬\n" +
//                        "«███\n███████████████████████████████\n███████████████████████████████████████████████████████████████████");
//            }
//        });

//        StarLoader.registerListener(ControlManagerActivateEvent.class, new Listener<ControlManagerActivateEvent>() {
//            @Override
//            public void onEvent(ControlManagerActivateEvent event) {
//                broadcastMessage("Activated: " + event.getControlManager().getClass().getName() + " >>> " + event.isActive());
//                PlayerPanel playerPanel = GameClientState.instance.getWorldDrawer().getGuiDrawer().getPlayerPanel();
//                try {
//                    Field panel = PlayerPanel.class.getDeclaredField("factionPanelNew");
//                    panel.setAccessible(true);
//                    FactionPanelNew p = (FactionPanelNew) panel.get(playerPanel);
//                    if(!(p instanceof MyFactionPanelNew)) {
//                        GameClientState state = playerPanel.getState();
//                        panel.set(playerPanel, new MyFactionPanelNew(state));
//                    }
//                } catch (NoSuchFieldException ex) {
//                    ex.printStackTrace();
//                } catch (IllegalAccessException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, null);


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
