package api;

import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.*;
import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.ChamberType;
import api.systems.addons.custom.TacticalJumpAddOn;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.data.blockeffects.config.ConfigPool;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;

import javax.xml.parsers.ParserConfigurationException;

public class ModPlayground extends StarMod {
    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        setModName("DefaultMod").setModAuthor("Jake").setModDescription("test").setModVersion("1.0").setModSMVersion("0.202");
        setModDescription("Default mod that is always loaded");
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        ElementInformation imp = BlockConfig.newElement("Impervium Armor", new short[]{66,66,66,66,66,66});
        imp.setBuildIconNum(234);
        imp.setMaxHitPointsE(100000);
        imp.setArmorValue(1000);
        config.add(imp);


        ElementInformation creative =
                BlockConfig.newChamber("Tactical Drive", ChamberType.MOBILITY.getId(),
                        new short[]{1,1,1,1,1,1}, StatusEffectType.CUSTOM_EFFECT_01);
        config.add(creative);

    }
    public static void initBlockData(){
        final BlockConfig config = new BlockConfig();
        for(StarMod mod : StarLoader.starMods){
            mod.onBlockConfigLoad(config);
        }
        for (ElementInformation element : config.getElements()) {
            try {
                //ElementKeyMap.addInformationToExisting(new ElementInformation(element.getId(), element.getName(), ElementKeyMap.getCategoryHirarchy(), element.getTextureIds()));
                ElementKeyMap.addInformationToExisting(element);
            } catch (ParserConfigurationException e) {
                DebugFile.logError(e, null);
                e.printStackTrace();
            }
        }
        /*try {
            int aaaaa = ElementKeyMap.insertIntoProperties("aaaaa");
            ElementKeyMap.addInformationToExisting(new ElementInformation((short) aaaaa, "aaaaa", ElementKeyMap.getCategoryHirarchy(), new short[]{1,2,3,4,5,6}));
            Server.broadcastMessage("Key: " + aaaaa);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");
        //File file = config.writeXML();
       // config.loadXML();
        //ElementKeyMap.initializeData(file);
                //config.loadXML();
              //  ElementKeyMap.getInfo(ElementKeyMap.ACTIVAION_BLOCK_ID)
        StarLoader.registerListener(RegisterEffectsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterEffectsEvent ev = (RegisterEffectsEvent) event;
                ev.addEffectModifier(StatusEffectType.CUSTOM_EFFECT_01, 100);
            }
        });
        StarLoader.registerListener(RegisterAddonsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterAddonsEvent ev = (RegisterAddonsEvent) event;
                //ev.getContainer().getSegmentController().getConfigManager().apply()
                ev.addAddOn(new TacticalJumpAddOn(ev.getContainer()));
            }
        });

        /*StarLoader.registerListener(EntityScanEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                EntityScanEvent e = ((EntityScanEvent) event);
                SegmentController internalEntity = e.getEntity().internalEntity;
                Server.broadcastMessage("1");
                if(internalEntity instanceof ManagedSegmentController){
                    Server.broadcastMessage("2");
                    ManagerContainer manager = ((ManagedSegmentController) internalEntity).getManagerContainer();
                    ActivationBeamElementManager activationBeamElementManager = new ActivationBeamElementManager(internalEntity);
                    manager.addUpdatable(activationBeamElementManager);
                    Server.broadcastMessage("3");
                }
            }
        });*/
        //This is to register a new listener to listen for StructureStatsCreateEvent
        StarLoader.registerListener(StructureStatsCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                final StructureStatsCreateEvent e = ((StructureStatsCreateEvent) event);
                e.addStatLabel(20, new StatLabelResult() {
                    @Override
                    public String getValue() {
                        //SegmentControllers are ships or stations.
                        //It's going to take a while to get used to naming, dont worry about it.
                        SegmentController ship = e.getCurrentShip();
                        if(ship != null){
                            return String.valueOf(ship.railController.getDockedCount());
                        }
                        return "No ship";
                    }

                    @Override
                    public int getStatDistance() {
                        //Controls how far to the right it is.
                        return 30;
                    }

                    @Override
                    public String getName() {
                        return "Docked Entities: ";
                    }
                });
            }
        });
    }
}
