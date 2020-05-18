package api;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.entity.Entity;
import api.entity.Player;
import api.gui.custom.examples.BasicInfoGroup;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.KeyPressEvent;
import api.listener.events.calculate.MaxPowerCalculateEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.listener.events.register.ElementRegisterEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.network.Packet;
import api.network.ServerToClientMessage;
import api.server.Server;
import api.systems.modules.custom.example.BatteryElementManager;
import api.universe.Universe;
import api.utils.StarRunnable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.ManagerModuleSingle;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.network.client.ClientProcessor;
import org.schema.schine.network.objects.remote.RemoteIntBuffer;
import org.schema.schine.network.objects.remote.RemoteStringArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
        Packet.registerPacket(ServerToClientMessage.class);
    }

    public static short newCapId = 0;

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
    }
    public void registerComputerModulePair(Blocks computer, Blocks module){
        ElementInformation comp = computer.getInfo();
        comp.mainCombinationController = true;
        comp.systemBlock = true;
        comp.controlledBy.add(Blocks.SHIP_CORE.getId());
        comp.controlling.add(module.getId());

        module.getInfo().controlledBy.add(computer.getId());

    }
    public void registerElementBlock(ElementInformation info){
        info.systemBlock = true;
        info.controlledBy.add(Blocks.SHIP_CORE.getId());

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

    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");
        new StarRunnable(){
            @Override
            public void run() {
                GameServerState server = GameServer.getServerState();
            }
        }.runTimer(50);
        StarLoader.registerListener(KeyPressEvent.class, new Listener() {
            int timesPressed = 0;
            int total = 0;
            @Override
            public void onEvent(Event event) {
                KeyPressEvent e = (KeyPressEvent) event;
                if(e.getChar() == 'o'){
                    GameClient.showPopupMessage("yooooo", 1);
                    GameClient.sendPacketToServer(new ServerToClientMessage("eyy"));
                }
            }
        });

        //HELP COMMAND
        StarLoader.registerListener(PlayerCommandEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                PlayerCommandEvent e = (PlayerCommandEvent) event;
                if(e.command.toLowerCase(Locale.ENGLISH).equals("help")){
                    Player player = e.player;
                    player.sendServerMessage("### COMMANDS: ###");
                    for (ImmutablePair<String, String> command : StarLoader.getCommands()) {
                        player.sendServerMessage(command.left + ": " + command.right);
                    }
                }
            }
        }, this);

        StarLoader.registerListener(ElementRegisterEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                ElementRegisterEvent e = (ElementRegisterEvent) event;
               // e.addModuleCollection(new ManagerModuleSingle(new BatteryElementManager(e.getSegmentController()), Blocks.SHIP_CORE.getId(), newCapId));
          }
        }, this);
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

        StarLoader.registerListener(PlayerCommandEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                PlayerCommandEvent e = (PlayerCommandEvent) event;
                Player p = e.player;
                if(e.command.toLowerCase(Locale.ENGLISH).equals("test")){
                    DebugFile.log("Test called", getMod());
                    Entity currentEntity = p.getCurrentEntity();
                    if(currentEntity == null){
                        p.sendServerMessage("You are in: nothing, thanks for playing");
                    }else{
                        p.sendServerMessage("You are in: " + currentEntity.getUID());
                    }
                }else if(e.command.equals("thr")){
                    Entity currentEntity = p.getCurrentEntity();
                    if(currentEntity == null){
                        p.sendServerMessage("no");
                        return;
                    }
                    BatteryElementManager elementManager = currentEntity.getElementManager(BatteryElementManager.class);
                    float actualThrust = elementManager.totalSize;
                    Server.broadcastMessage("The total thrust of this object is: " + actualThrust);
                }else if(e.command.equals("a")){
                    p.sendPacket(new ServerToClientMessage("hi"));
                }
            }
        });


        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                HudCreateEvent ev = (HudCreateEvent) event;
                BasicInfoGroup bar = new BasicInfoGroup(ev);
            }
        });


//        final int[] t = {0};
//        new StarRunnable() {
//            @Override
//            public void run() {
//                t[0] += 4;
//            }
//        }.runTimer(1);
//        StarLoader.registerListener(CannonShootEvent.class, new Listener() {
//            @Override
//            public void onEvent(Event event) {
//                CannonShootEvent e = (CannonShootEvent) event;
//                Color hsb = Color.getHSBColor(((float) t[0] % 360) / 360F, 1F, 1F);
//                Vector4f tuple4f = new Vector4f(hsb.getRed() / 255F, hsb.getGreen() / 255F, hsb.getBlue() / 255F, 1F);
//                e.setColor(tuple4f);
//            }
//        });

        StarLoader.registerListener(RegisterEffectsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterEffectsEvent ev = (RegisterEffectsEvent) event;
                for (StatusEffectType types : StatusEffectType.values()) {
                    if (types.name().contains("CUSTOM")) {
                        ev.addEffectModifier(types, 10F);
                    }
                }
            }
        });
    }
}
