package api;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.entity.Entity;
import api.entity.Player;
import api.gui.custom.examples.BasicInfoGroup;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.listener.events.register.ElementRegisterEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;

import java.util.Locale;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
        this.forceEnable = true;
    }

    public static short xorId = 0;

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        registerComputerModulePair(Blocks.FERTIKEEN_INGOT, Blocks.HYLAT_INGOT);
    }
    public void registerComputerModulePair(Blocks computer, Blocks module){
        ElementInformation comp = computer.getInfo();
        comp.mainCombinationController = true;
        comp.systemBlock = true;
        comp.controlledBy.add(Blocks.SHIP_CORE.getId());
        comp.controlling.add(module.getId());

        module.getInfo().controlledBy.add(computer.getId());

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
                e.addModuleCollection(new ManagerModuleCollection(new WeaponElementManager(e.getSegmentController()), Blocks.FERTIKEEN_INGOT.getId(), Blocks.HYLAT_INGOT.getId()));
            }
        }, this);
        /*StarLoader.registerListener(ShieldCapacityCalculateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                ShieldCapacityCalculateEvent e = (ShieldCapacityCalculateEvent) event;

                long bonusShields = 0;
                ShieldCapacityUnit capacityUnit = ((ShieldCapacityCalculateEvent) event).getUnit();
                Vector3i max = capacityUnit.getMax(new Vector3i());
                Vector3i min = capacityUnit.getMin(new Vector3i());
                int deltaX = Math.abs(max.x-min.x);
                int deltaY = Math.abs(max.y-min.y);
                int deltaZ = Math.abs(max.z-min.z);
                int smallAxes = 0;
                if(deltaX <= 5)
                    smallAxes++;
                if(deltaY <= 5)
                    smallAxes++;
                if(deltaZ <= 5)
                    smallAxes++;
                if(smallAxes >= 2) {
                    e.setShields((long) (e.getCapacity() * 1.2));
                }
            }
        });*/
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
                }
            }
        });

        StarLoader.registerListener(ElementRegisterEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                ElementRegisterEvent ev = (ElementRegisterEvent) event;
                //ev.addModuleCollection(new ManagerModuleCollection());
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
