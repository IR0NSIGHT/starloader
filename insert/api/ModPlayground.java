package api;

import api.config.BlockConfig;
import api.element.block.Block;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import api.entity.Entity;
import api.gui.custom.CustomShieldTargetBar;
import api.gui.custom.CustomShipHPBar;
import api.gui.custom.EntityShieldBar;
import api.listener.Listener;
import api.listener.events.Event;
import api.listener.events.StructureStatsCreateEvent;
import api.listener.events.block.BlockActivateEvent;
import api.listener.events.block.BlockSalvageEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.main.GameClient;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.ChamberType;
import api.systems.addons.custom.TacticalJumpAddOn;
import api.utils.StarRunnable;
import api.utils.VecUtil;
import com.bulletphysics.linearmath.Transform;
import org.newdawn.slick.Color;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.activation.AbstractUnit;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.physics.Vector3fb;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
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
    public static short xorId = 0;

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        ElementInformation imp = BlockConfig.newElement("Impervium Armor", new short[]{124, 753, 427, 345, 231, 427});
        imp.setBuildIconNum(234);
        imp.setMaxHitPointsE(100000);
        imp.lightSource = true;
        imp.lightSourceColor.set(new Vector4f(1F, 0F, 1F, 1F));
        imp.setCanActivate(true);
        BlockConfig.addRecipe(imp, FactoryType.ADVANCED, 5, new FactoryResource(1, Blocks.RED_PAINT.getId()));
        imp.setArmorValue(1000);
        config.add(imp);

        ArrayList<FactoryResource> factoryResources = new ArrayList<>();
        for (Blocks b : Blocks.values()) {
            if (b.name().endsWith("PAINT")) {
                factoryResources.add(new FactoryResource(1, b.getId()));
            }
        }
        BlockConfig.addRecipe(Blocks.FERTIKEEN_CAPSULE.getInfo(), FactoryType.ADVANCED, 5, factoryResources.toArray(new FactoryResource[0]));

        ElementInformation creative =
                BlockConfig.newChamber("Tactical Drive", ChamberType.MOBILITY.getId(),
                        new short[]{86, 23, 45, 33, 99, 99}, StatusEffectType.CUSTOM_EFFECT_01);
        config.add(creative);

        ElementInformation c2 =
                BlockConfig.newChamber("Upward Jump", creative.getId(),
                        new short[]{231}, StatusEffectType.CUSTOM_EFFECT_02);
        config.add(c2);

        ElementInformation bruh =
                BlockConfig.newChamber("Bruh Jump", creative.getId(),
                        new short[]{Blocks.CHABAZ_CAPSULE.getId()}, StatusEffectType.CUSTOM_EFFECT_03);
        config.add(bruh);

        ElementInformation info = Blocks.THRUSTER_MODULE.getInfo();
        //info.signal = true;



        //Doesnt work (not sure why
        ElementInformation xor = BlockConfig.newElement("XOR gate", new short[]{745});
        xor.signal = true;
        xor.setBuildIconNum(745);
        xor.setHasActivationTexure(true);
        config.add(xor);

        xorId = xor.getId();

    }

    public static void initBlockData() {
        final BlockConfig config = new BlockConfig();
        for (StarMod mod : StarLoader.starMods) {
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
    public static void spawnBlockParticle(short id, Vector3f pos){
        GameClientState state = GameClient.getClientState();
        if(state == null){
            return;
        }
        final Vector3fb vector3fb = new Vector3fb(pos);
        final Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set(vector3fb);
        state.getWorldDrawer().getShards().voronoiBBShatter((PhysicsExt)state.getPhysics(), transform, id, state.getCurrentSectorId(), transform.origin, null);
    }
    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");

        StarLoader.registerListener(BlockSalvageEvent.class, new Listener() {
            @Override
            public void onEvent(Event e) {
                BlockSalvageEvent event = (BlockSalvageEvent) e;
                //spawnBlockParticle(event.getBlock().getType().getId(), event.getBeam().getInternalBeam().lastHitTrans.origin);
                //spawnBlockParticle(event.getBlock().getType().getId(), event.getBlock().getInternalSegmentPiece().getWorldPos(new Vector3f(), event.getBlock().getInternalSegmentPiece().getSegmentController().getSectorId()));
                spawnBlockParticle(event.getBlock().getType().getId(), event.getBeam().getInternalBeam().hitPoint);
            }
        });

        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                HudCreateEvent ev = (HudCreateEvent) event;
                EntityShieldBar bar = new EntityShieldBar();
                ev.addElement(bar);
                /*GUITextOverlay text = new GUITextOverlay(150, 10, FontLibrary.getBlenderProHeavy30(), Color.red, ev.getInputState());

                text.setTextSimple(new Object() {
                    @Override
                    public String toString() {
                        SimpleTransformableSendableObject<?> selectedEntity = GameClient.getClientState().getSelectedEntity();
                        if (selectedEntity != null) {
                            return "Selected: " + selectedEntity.toNiceString();
                        } else {
                            return "No entity selected";
                        }
                    }
                });
                text.setPos(new Vector3f(100, 100, 0));
                ev.addElement(text);

                CustomShieldTargetBar bar = new CustomShieldTargetBar() {
                    @Override
                    public void onUpdate() {
                        Entity currentEntity = GameClient.getCurrentEntity();
                        this.setEntity(currentEntity);
                    }
                };
                ev.addElement(bar);

                CustomShipHPBar customShipHPBar = new CustomShipHPBar(ev.getInputState()) {
                    @Override
                    public void onUpdate() {
                        Entity currentEntity = GameClient.getCurrentEntity();
                        this.setEntity(currentEntity);
                    }
                };
                ev.addElement(customShipHPBar);*/

            }
        });
        StarLoader.registerListener(BlockActivateEvent.class, new Listener() {
            @Override
            public void onEvent(Event ev) {
                final BlockActivateEvent event = (BlockActivateEvent) ev;
                //Server.broadcastMessage("Activated block: " + event.getBlockType().getName());
                //Server.broadcastMessage("At: " + event.getSegmentPiece().getAbsolutePos(new Vector3f()).toString());
                if(event.getBlockId() == xorId){
                    ActivationElementManager var1 = event.getManager();
                    Block block = event.getBlock();
                    long var6 = block.getInternalSegmentPiece().getAbsoluteIndex();
                    int activeSignals = 0;
                    for(int i = 0; i < var1.getCollectionManagers().size(); ++i) {
                        ActivationCollectionManager var8 = (ActivationCollectionManager)var1.getCollectionManagers().get(i);
                        for(int j = 0; j < var8.getElementCollections().size(); ++j) {
                            if (((AbstractUnit)var8.getElementCollections().get(j)).contains(var6)) {
                                var8.getControllerElement().refresh();
                                if(var8.getControllerElement().isActive()){
                                    activeSignals++;
                                }
                            }
                        }
                    }
                    if(activeSignals == 2){
                        block.getInternalSegmentPiece().setActive(true);
                    }else{
                        block.getInternalSegmentPiece().setActive(false);
                    }
                }
                if (event.getBlockId() == Blocks.THRUSTER_MODULE.getId()) {
                    final Entity entity = event.getEntity();
                    //entity.internalEntity.getPhysicsObject().applyCentralForce(new Vector3f(10,10,10));
                    final Vector3f v = entity.getVelocity();
                    v.add(VecUtil.scale(entity.getDirection(), 15F));
                    entity.setVelocity(v);

                    new StarRunnable() {
                        @Override
                        public void run() {
                            entity.setVelocity(v);
                        }
                    }.runLater(1);

                }

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
                        if (ship != null) {
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
