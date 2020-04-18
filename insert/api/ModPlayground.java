package api;

import api.config.BlockConfig;
import api.element.block.Block;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import api.entity.Entity;
import api.gui.custom.examples.*;
import api.listener.Listener;
import api.listener.events.CannonShootEvent;
import api.listener.events.DamageBeamShootEvent;
import api.listener.events.Event;
import api.listener.events.StructureStatsCreateEvent;
import api.listener.events.block.BlockActivateEvent;
import api.listener.events.block.BlockModifyEvent;
import api.listener.events.block.BlockSalvageEvent;
import api.listener.events.calculate.MaxPowerCalculateEvent;
import api.listener.events.calculate.ShieldCapacityCalculateEvent;
import api.listener.events.gui.HudCreateEvent;
import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.register.RegisterEffectsEvent;
import api.listener.events.systems.InterdictionCheckEvent;
import api.listener.events.systems.ShieldHitEvent;
import api.main.GameClient;
import api.main.GameServer;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.server.Server;
import api.systems.ChamberType;
import api.systems.addons.JumpInterdictor;
import api.systems.addons.custom.*;
import api.utils.StarRunnable;
import api.utils.VecUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.activation.AbstractUnit;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.controller.elements.jumpprohibiter.InterdictionAddOn;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityCollectionManager;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityUnit;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.Universe;
import org.schema.game.server.data.GameServerState;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

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
        //Create a new block called 'Impervium Armor'
        //The short list is the block texture ids, make sure to give it a list of size 1,3 or 6.
        final ElementInformation imp = BlockConfig.newElement("Impervium Armor", new short[]{124});
        imp.setBuildIconNum(Blocks.GREY_ADVANCED_ARMOR.getId());
        //Give it lotss of health
        imp.setMaxHitPointsE(100000);
        imp.setArmorValue(1000);
        //Make it emit light
        imp.lightSource = true;
        imp.lightSourceColor.set(new Vector4f(1F, 0F, 1F, 1F));

        /*imp.blended = true;
        new StarRunnable(){
            @Override
            public void run() {
                Color hsb = Color.getHSBColor(((float)ticksRan%360)/360F, 1F, 1F);
                Vector4f tuple4f = new Vector4f(hsb.getRed()/255F, hsb.getGreen()/255F, hsb.getBlue()/255F, 1F);
                Server.broadcastMessage(tuple4f.toString());
                imp.lightSourceColor.set(tuple4f);
            }
        }.runTimer(1);*/
        //Make it activatable,
        imp.setCanActivate(true);

        //Give it a recipe that uses red paint
        BlockConfig.addRecipe(imp, FactoryType.ADVANCED, 5, new FactoryResource(1, Blocks.RED_PAINT.getId()));
        //Add it to the config.
        config.add(imp);

        ArrayList<FactoryResource> factoryResources = new ArrayList<FactoryResource>();
        for (Blocks b : Blocks.values()) {
            if (b.name().endsWith("PAINT")) {
                factoryResources.add(new FactoryResource(1, b.getId()));
            }
        }
        BlockConfig.addRecipe(Blocks.FERTIKEEN_CAPSULE.getInfo(), FactoryType.ADVANCED, 5, factoryResources.toArray(new FactoryResource[0]));

        ElementInformation sh =
                BlockConfig.newChamber("Shield Hardener", ChamberType.DEFENCE.getId(),
                        new short[]{12, 42, 111, 86, 222, 333}, StatusEffectType.CUSTOM_EFFECT_05);
        config.add(sh);

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

    @Override
    public void onEnable() {
        DebugFile.log("Loading default mod...");

        StarLoader.registerListener(ShieldHitEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                ShieldHitEvent e = (ShieldHitEvent) event;
                CustomAddOn customAddon = e.getEntity().getCustomAddon(ShieldHardenAddOn.class);
                if (customAddon != null && customAddon.isActive()) {
                    Server.broadcastMessage("damage reduced from " + e.getDamage() + " to " + (e.getDamage() * 0.25));
                    e.setDamage(e.getDamage() * 0.25);
                    //e.setCanceled(true);
                } else {
                    if (customAddon == null) {
                        Server.broadcastMessage("rip");
                    }
                }
            }
        });

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

        StarLoader.registerListener(DamageBeamShootEvent.class, new Listener() {

            @Override
            public void onEvent(Event event) {
                DamageBeamShootEvent e = (DamageBeamShootEvent) event;
                //e.getBeamWeapon().getUnit().elementCollectionManager.getColor()
            }
        });

        StarLoader.registerListener(BlockSalvageEvent.class, new Listener() {
            @Override
            public void onEvent(Event e) {
                BlockSalvageEvent event = (BlockSalvageEvent) e;
                GameClient.spawnBlockParticle(event.getBlock().getType().getId(), event.getBeamEntity().getInternalBeam().hitPoint);
            }
        });

        StarLoader.registerListener(HudCreateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                HudCreateEvent ev = (HudCreateEvent) event;
                BasicInfoGroup bar = new BasicInfoGroup(ev);
            }
        });
        final int[] t = {0};
        new StarRunnable() {
            @Override
            public void run() {
                t[0] += 4;
            }
        }.runTimer(1);
        StarLoader.registerListener(CannonShootEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                CannonShootEvent e = (CannonShootEvent) event;
                Color hsb = Color.getHSBColor(((float) t[0] % 360) / 360F, 1F, 1F);
                Vector4f tuple4f = new Vector4f(hsb.getRed() / 255F, hsb.getGreen() / 255F, hsb.getBlue() / 255F, 1F);
                e.setColor(tuple4f);
            }
        });

        StarLoader.registerListener(BlockActivateEvent.class, new Listener() {
            @Override
            public void onEvent(Event ev) {
                final BlockActivateEvent event = (BlockActivateEvent) ev;
                //Server.broadcastMessage("Activated block: " + event.getBlockType().getName());
                //Server.broadcastMessage("At: " + event.getSegmentPiece().getAbsolutePos(new Vector3f()).toString());
                if (event.getBlockId() == xorId) {
                    ActivationElementManager var1 = event.getManager();
                    Block block = event.getBlock();
                    long var6 = block.getInternalSegmentPiece().getAbsoluteIndex();
                    int activeSignals = 0;
                    for (int i = 0; i < var1.getCollectionManagers().size(); ++i) {
                        ActivationCollectionManager var8 = (ActivationCollectionManager) var1.getCollectionManagers().get(i);
                        for (int j = 0; j < var8.getElementCollections().size(); ++j) {
                            if (((AbstractUnit) var8.getElementCollections().get(j)).contains(var6)) {
                                var8.getControllerElement().refresh();
                                if (var8.getControllerElement().isActive()) {
                                    activeSignals++;
                                }
                            }
                        }
                    }
                    if (activeSignals == 2) {
                        block.getInternalSegmentPiece().setActive(true);
                    } else {
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

        StarLoader.registerListener(MaxPowerCalculateEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                MaxPowerCalculateEvent e = (MaxPowerCalculateEvent) event;
                float apply = e.getEntity().getConfigManager().apply(StatusEffectType.POWER_RECHARGE_EFFICIENCY, 1F);
                if(apply != 1F) {
                    e.setPower(e.getPower()*100);
                }
            }
        });/*

        StarLoader.registerListener(InterdictionCheckEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                InterdictionCheckEvent e = (InterdictionCheckEvent) event;
                //Loop through all piloted ships, check if they have interdiction

                GameServerState server = GameServer.getServerState();
                if (server != null) {
                    Universe universe = server.getUniverse();
                    for (int x = -3; x <= 3; ++x) {
                        for (int y = -3; y <= 3; ++y) {
                            for (int z = -3; z <= 3; ++z) {
                                Vector3i v = e.getEntity().getSectorPosition();
                                v.add(x, y, z);
                                Sector sector = universe.getSectorWithoutLoading(v);
                                if(sector != null) {
                                    for (SimpleTransformableSendableObject<?> entity : sector.getEntities()) {
                                        if (entity instanceof SegmentController) {
                                            Entity ship = new Entity((SegmentController) entity);
                                            JumpInterdictor inter = ship.getInterdictionAddOn();
                                            if (inter.isActive()) {
                                                int strength = inter.getStrength();
                                                int distance = inter.getDistance();
                                                int inderdictorLevel = ship.getCurrentReactor().getLevel();
                                                int jumperMax = e.getEntity().getCurrentReactor().getLevel();
                                                int extraDelta = strength*20;
                                                //50 = max,
                                                //Interdiction condition: If Interdictor_max + (50*20) > Jumper_max
                                                Server.broadcastMessage("Strength: " + inter.getStrength() + "Dist, " + distance + "IL, " + inderdictorLevel + ", jmax: " + jumperMax);
                                                Vector3i sectorPosition = e.getEntity().getSectorPosition();
                                                sectorPosition.sub(ship.getSectorPosition());
                                                double d = sectorPosition.lengthSquared();
                                                Server.broadcastMessage("D " + d);
                                                if(d <= distance) {
                                                    if (inderdictorLevel + extraDelta > jumperMax) {
                                                        e.setInterdicted(true);
                                                    } else {
                                                        //nope
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });*/

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
        StarLoader.registerListener(RegisterAddonsEvent.class, new Listener() {
            @Override
            public void onEvent(Event event) {
                RegisterAddonsEvent ev = (RegisterAddonsEvent) event;
                //ev.getContainer().getSegmentController().getConfigManager().apply()
                ev.addAddOn(new TacticalJumpAddOn(ev.getContainer()));
                ev.addAddOn(new ShieldHardenAddOn(ev.getContainer()));
                ev.addAddOn(new SystemScannerAddOn(ev.getContainer()));
                ev.addAddOn(new HyperChargeAddOn(ev.getContainer()));
                ev.addAddOn(new AntiOutageDriveAddOn(ev.getContainer()));
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
