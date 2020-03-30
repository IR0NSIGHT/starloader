package api;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.StructureStatsCreateEvent;
import api.listener.events.block.BlockActivateEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.ChamberType;
import api.systems.addons.custom.TacticalJumpAddOn;
import org.newdawn.slick.Color;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.client.view.gui.shiphud.newhud.TargetShieldBar;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;

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
        ElementInformation imp = BlockConfig.newElement("Impervium Armor", new short[]{124, 753, 427, 345, 231, 427});
        imp.setBuildIconNum(234);
        imp.setMaxHitPointsE(100000);
        imp.lightSource = true;
        imp.lightSourceColor.set(new Vector4f(1F,0F,1F, 1F));
        imp.setCanActivate(true);
        BlockConfig.addRecipe(imp, FactoryType.ADVANCED, 5, new FactoryResource(1, Blocks.RED_PAINT.getId()));
        imp.setArmorValue(1000);
        config.add(imp);

        ArrayList<FactoryResource> factoryResources = new ArrayList<>();
        for (Blocks b : Blocks.values()){
            if(b.name().endsWith("PAINT")){
                factoryResources.add(new FactoryResource(1, b.getId()));
            }
        }
        BlockConfig.addRecipe(Blocks.FERTIKEEN_CAPSULE.getInfo(), FactoryType.ADVANCED, 5, factoryResources.toArray(new FactoryResource[0]));


        ElementInformation creative =
                BlockConfig.newChamber("Tactical Drive", ChamberType.MOBILITY.getId(),
                        new short[]{86,23,45,33,99,99}, StatusEffectType.CUSTOM_EFFECT_01);
        config.add(creative);

        ElementInformation c2 =
                BlockConfig.newChamber("Upward Jump", creative.getId(),
                        new short[]{1,1,1,1,1,1}, StatusEffectType.CUSTOM_EFFECT_02);
        config.add(c2);

        ElementInformation bruh =
                BlockConfig.newChamber("Bruh Jump", creative.getId(),
                        new short[]{1,1,1,1,1,1}, StatusEffectType.CUSTOM_EFFECT_03);
        config.add(bruh);
    }

    public static void initBlockData(){
        final BlockConfig config = new BlockConfig();
        for(StarMod mod : StarLoader.starMods){
            mod.onBlockConfigLoad(config);
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
        //File file = config.writeXML();
       // config.loadXML();
        //ElementKeyMap.initializeData(file);
                //config.loadXML();
              //  ElementKeyMap.getInfo(ElementKeyMap.ACTIVAION_BLOCK_ID)

        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                HudCreateEvent ev = (HudCreateEvent) event;
                GUITextOverlay text = new GUITextOverlay(100, 100, FontLibrary.getBlenderProHeavy30(), Color.red, ev.getInputState());
                text.setTextSimple(new Object(){
                    @Override
                    public String toString() {
                        return "GUITextOverlay is here";
                    }
                });
                text.setPos(new Vector3f(100,100,0));
                ev.addElement(text);

                TargetShieldBar panel = new TargetShieldBar(ev.getInputState());
                panel.getPos().set(200,200,0);
                ev.addElement(panel);

            }
        });
        StarLoader.registerListener(BlockActivateEvent.class, new Listener() {
            @Override
            public void onEvent(Event ev) {
                BlockActivateEvent event = (BlockActivateEvent) ev;

                //Server.broadcastMessage("Activated block: " + event.getBlockType().getName());
                //Server.broadcastMessage("At: " + event.getSegmentPiece().getAbsolutePos(new Vector3f()).toString());
            }
        });

        StarLoader.registerListener(RegisterEffectsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterEffectsEvent ev = (RegisterEffectsEvent) event;
                ev.addEffectModifier(StatusEffectType.CUSTOM_EFFECT_01, 100);
                ev.addEffectModifier(StatusEffectType.CUSTOM_EFFECT_02, 4);
                ev.addEffectModifier(StatusEffectType.CUSTOM_EFFECT_03, 4);
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
