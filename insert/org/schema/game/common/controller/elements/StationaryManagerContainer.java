//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements;

import api.listener.events.register.ElementRegisterEvent;
import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.common.controller.SegmentBufferInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.ShopInterface;
import org.schema.game.common.controller.ShopNetworkInterface;
import org.schema.game.common.controller.ShoppingAddOn;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.acid.AcidDamageFormula.AcidFormulaType;
import org.schema.game.common.controller.database.DatabaseEntry;
import org.schema.game.common.controller.elements.activation.AbstractUnit;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.controller.elements.activationgate.ActivationGateCollectionManager;
import org.schema.game.common.controller.elements.activationgate.ActivationGateElementManager;
import org.schema.game.common.controller.elements.activationgate.ActivationGateUnit;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamElementManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;
import org.schema.game.common.controller.elements.beam.harvest.SalvageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.harvest.SalvageElementManager;
import org.schema.game.common.controller.elements.beam.harvest.SalvageUnit;
import org.schema.game.common.controller.elements.beam.repair.RepairBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.repair.RepairElementManager;
import org.schema.game.common.controller.elements.beam.repair.RepairUnit;
import org.schema.game.common.controller.elements.cargo.CargoCollectionManager;
import org.schema.game.common.controller.elements.cargo.CargoElementManager;
import org.schema.game.common.controller.elements.cargo.CargoUnit;
import org.schema.game.common.controller.elements.dockingBlock.DockingBlockCollectionManager;
import org.schema.game.common.controller.elements.dockingBlock.DockingBlockElementManager;
import org.schema.game.common.controller.elements.dockingBlock.DockingBlockManagerInterface;
import org.schema.game.common.controller.elements.dockingBlock.DockingBlockUnit;
import org.schema.game.common.controller.elements.dockingBlock.fixed.FixedDockingBlockCollectionManager;
import org.schema.game.common.controller.elements.dockingBlock.fixed.FixedDockingBlockElementManager;
import org.schema.game.common.controller.elements.dockingBlock.fixed.FixedDockingBlockUnit;
import org.schema.game.common.controller.elements.dockingBlock.turret.TurretDockingBlockCollectionManager;
import org.schema.game.common.controller.elements.dockingBlock.turret.TurretDockingBlockElementManager;
import org.schema.game.common.controller.elements.dockingBlock.turret.TurretDockingBlockUnit;
import org.schema.game.common.controller.elements.door.DoorCollectionManager;
import org.schema.game.common.controller.elements.door.DoorUnit;
import org.schema.game.common.controller.elements.effectblock.EffectElementManager;
import org.schema.game.common.controller.elements.effectblock.em.EmEffectCollectionManager;
import org.schema.game.common.controller.elements.effectblock.em.EmEffectElementManager;
import org.schema.game.common.controller.elements.effectblock.em.EmEffectUnit;
import org.schema.game.common.controller.elements.effectblock.heat.HeatEffectCollectionManager;
import org.schema.game.common.controller.elements.effectblock.heat.HeatEffectElementManager;
import org.schema.game.common.controller.elements.effectblock.heat.HeatEffectUnit;
import org.schema.game.common.controller.elements.effectblock.kinetic.KineticEffectCollectionManager;
import org.schema.game.common.controller.elements.effectblock.kinetic.KineticEffectElementManager;
import org.schema.game.common.controller.elements.effectblock.kinetic.KineticEffectUnit;
import org.schema.game.common.controller.elements.explosive.ExplosiveCollectionManager;
import org.schema.game.common.controller.elements.explosive.ExplosiveElementManager;
import org.schema.game.common.controller.elements.explosive.ExplosiveUnit;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorCollectionManager;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorElementManager;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorUnit;
import org.schema.game.common.controller.elements.lift.LiftCollectionManager;
import org.schema.game.common.controller.elements.lift.LiftUnit;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityCollectionManager;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityElementManager;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityUnit;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileCollectionManager;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileElementManager;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileUnit;
import org.schema.game.common.controller.elements.power.PowerAddOn;
import org.schema.game.common.controller.elements.power.PowerCollectionManager;
import org.schema.game.common.controller.elements.power.PowerUnit;
import org.schema.game.common.controller.elements.power.reactor.MainReactorCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.MainReactorElementManager;
import org.schema.game.common.controller.elements.power.reactor.MainReactorUnit;
import org.schema.game.common.controller.elements.power.reactor.StabilizerCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.StabilizerElementManager;
import org.schema.game.common.controller.elements.power.reactor.StabilizerUnit;
import org.schema.game.common.controller.elements.power.reactor.chamber.ConduitCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ConduitElementManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ConduitUnit;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberElementManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberUnit;
import org.schema.game.common.controller.elements.powerbattery.PowerBatteryCollectionManager;
import org.schema.game.common.controller.elements.powerbattery.PowerBatteryUnit;
import org.schema.game.common.controller.elements.powercap.PowerCapacityCollectionManager;
import org.schema.game.common.controller.elements.powercap.PowerCapacityUnit;
import org.schema.game.common.controller.elements.pulse.push.PushPulseCollectionManager;
import org.schema.game.common.controller.elements.pulse.push.PushPulseElementManager;
import org.schema.game.common.controller.elements.pulse.push.PushPulseUnit;
import org.schema.game.common.controller.elements.racegate.RacegateCollectionManager;
import org.schema.game.common.controller.elements.racegate.RacegateElementManager;
import org.schema.game.common.controller.elements.racegate.RacegateUnit;
import org.schema.game.common.controller.elements.rail.inv.RailConnectionCollectionManager;
import org.schema.game.common.controller.elements.rail.inv.RailConnectionElementManager;
import org.schema.game.common.controller.elements.rail.inv.RailConnectionUnit;
import org.schema.game.common.controller.elements.rail.massenhancer.RailMassEnhancerCollectionManager;
import org.schema.game.common.controller.elements.rail.massenhancer.RailMassEnhancerUnit;
import org.schema.game.common.controller.elements.rail.pickup.RailPickupCollectionManager;
import org.schema.game.common.controller.elements.rail.pickup.RailPickupUnit;
import org.schema.game.common.controller.elements.rail.speed.RailSpeedCollectionManager;
import org.schema.game.common.controller.elements.rail.speed.RailSpeedElementManager;
import org.schema.game.common.controller.elements.rail.speed.RailSpeedUnit;
import org.schema.game.common.controller.elements.scanner.ScannerCollectionManager;
import org.schema.game.common.controller.elements.scanner.ScannerElementManager;
import org.schema.game.common.controller.elements.scanner.ScannerUnit;
import org.schema.game.common.controller.elements.sensor.SensorCollectionManager;
import org.schema.game.common.controller.elements.sensor.SensorElementManager;
import org.schema.game.common.controller.elements.sensor.SensorUnit;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityCollectionManager;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityUnit;
import org.schema.game.common.controller.elements.shield.regen.ShieldRegenCollectionManager;
import org.schema.game.common.controller.elements.shield.regen.ShieldRegenUnit;
import org.schema.game.common.controller.elements.shipyard.ShipyardCollectionManager;
import org.schema.game.common.controller.elements.shipyard.ShipyardElementManager;
import org.schema.game.common.controller.elements.shipyard.ShipyardUnit;
import org.schema.game.common.controller.elements.shop.ShopCollectionManager;
import org.schema.game.common.controller.elements.shop.ShopElementManager;
import org.schema.game.common.controller.elements.shop.ShopUnit;
import org.schema.game.common.controller.elements.transporter.TransporterCollectionManager;
import org.schema.game.common.controller.elements.transporter.TransporterElementManager;
import org.schema.game.common.controller.elements.transporter.TransporterUnit;
import org.schema.game.common.controller.elements.trigger.TriggerCollectionManager;
import org.schema.game.common.controller.elements.trigger.TriggerElementManager;
import org.schema.game.common.controller.elements.trigger.TriggerUnit;
import org.schema.game.common.controller.elements.warpgate.WarpgateCollectionManager;
import org.schema.game.common.controller.elements.warpgate.WarpgateElementManager;
import org.schema.game.common.controller.elements.warpgate.WarpgateUnit;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;
import org.schema.game.common.controller.trade.TradeNode;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.ScannerManagerInterface;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.player.inventory.InventoryHolder;
import org.schema.game.common.data.player.inventory.InventoryMultMod;
import org.schema.game.common.data.player.inventory.InventorySlotRemoveMod;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.common.data.player.inventory.NoSlotFreeException;
import org.schema.game.common.data.player.inventory.ShopInventory;
import org.schema.game.common.data.world.Chunk16SegmentData;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.network.objects.NetworkDoorInterface;
import org.schema.game.network.objects.NetworkLiftInterface;
import org.schema.game.network.objects.TradePriceInterface;
import org.schema.game.network.objects.remote.RemoteInventoryMultMod;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.simulation.npc.NPCFaction;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.remote.RemoteVector4i;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class StationaryManagerContainer<E extends SegmentController> extends ManagerContainer<E> implements ShopInterface, ActivationManagerInterface, DoorContainerInterface, EffectManagerContainer, ExplosiveManagerContainerInterface, FactoryAddOnInterface, JumpProhibiterModuleInterface, LiftContainerInterface, MissileModuleInterface, RailManagerInterface, SalvageManagerContainer, ShieldContainerInterface, ShipyardManagerContainerInterface, TransporterModuleInterface, TriggerManagerInterface, WeaponManagerInterface, DockingBlockManagerInterface, ScannerManagerInterface, InventoryHolder {
    private PowerAddOn powerAddOn;
    private ShoppingAddOn shoppingAddOn;
    private ShieldAddOn shieldAddOn;
    private ManagerModuleSingle<ShieldRegenUnit, ShieldRegenCollectionManager, VoidElementManager<ShieldRegenUnit, ShieldRegenCollectionManager>> shields;
    private ManagerModuleSingle<ShieldCapacityUnit, ShieldCapacityCollectionManager, VoidElementManager<ShieldCapacityUnit, ShieldCapacityCollectionManager>> shieldCapacity;
    private ManagerModuleSingle<PowerUnit, PowerCollectionManager, VoidElementManager<PowerUnit, PowerCollectionManager>> power;
    private ManagerModuleSingle<LiftUnit, LiftCollectionManager, VoidElementManager<LiftUnit, LiftCollectionManager>> lift;
    private ManagerModuleSingle<DoorUnit, DoorCollectionManager, VoidElementManager<DoorUnit, DoorCollectionManager>> door;
    private ManagerModuleSingle<RailPickupUnit, RailPickupCollectionManager, VoidElementManager<RailPickupUnit, RailPickupCollectionManager>> railPickup;
    private ManagerModuleCollection<RepairUnit, RepairBeamCollectionManager, RepairElementManager> repair;
    private ManagerModuleCollection<WarpgateUnit, WarpgateCollectionManager, WarpgateElementManager> warpgate;
    private ManagerModuleCollection<RacegateUnit, RacegateCollectionManager, RacegateElementManager> racegate;
    private ManagerModuleCollection<ActivationGateUnit, ActivationGateCollectionManager, ActivationGateElementManager> activationgate;
    private ManagerModuleCollection<TransporterUnit, TransporterCollectionManager, TransporterElementManager> transporter;
    private ManagerModuleSingle<ConduitUnit, ConduitCollectionManager, ConduitElementManager> conduit;
    private ManagerModuleSingle<PowerCapacityUnit, PowerCapacityCollectionManager, VoidElementManager<PowerCapacityUnit, PowerCapacityCollectionManager>> powerCapacity;
    private ManagerModuleSingle<PowerBatteryUnit, PowerBatteryCollectionManager, VoidElementManager<PowerBatteryUnit, PowerBatteryCollectionManager>> powerBattery;
    private ManagerModuleSingle<RailMassEnhancerUnit, RailMassEnhancerCollectionManager, VoidElementManager<RailMassEnhancerUnit, RailMassEnhancerCollectionManager>> railMassEnhancer;
    private ManagerModuleCollection<JumpInhibitorUnit, JumpInhibitorCollectionManager, JumpInhibitorElementManager> jumpProhibiter;
    private ManagerModuleCollection<RailSpeedUnit, RailSpeedCollectionManager, RailSpeedElementManager> railSpeed;
    private ManagerModuleCollection<WeaponUnit, WeaponCollectionManager, WeaponElementManager> weapon;
    private ManagerModuleSingle<MainReactorUnit, MainReactorCollectionManager, MainReactorElementManager> reactor;
    private ManagerModuleSingle<StabilizerUnit, StabilizerCollectionManager, StabilizerElementManager> stabilizer;
    private ManagerModuleCollection<DumbMissileUnit, DumbMissileCollectionManager, DumbMissileElementManager> dumbMissile;
    private ManagerModuleCollection<TurretDockingBlockUnit, TurretDockingBlockCollectionManager, TurretDockingBlockElementManager> turretDockingBlock;
    private ManagerModuleCollection<FixedDockingBlockUnit, FixedDockingBlockCollectionManager, FixedDockingBlockElementManager> fixedDockingBlock;
    private ManagerModuleCollection<TriggerUnit, TriggerCollectionManager, TriggerElementManager> trigger;
    private ManagerModuleCollection<AbstractUnit, ActivationCollectionManager, ActivationElementManager> activation;
    private ManagerModuleCollection<EmEffectUnit, EmEffectCollectionManager, EmEffectElementManager> empEffect;
    private ManagerModuleCollection<CargoUnit, CargoCollectionManager, CargoElementManager> cargo;
    private ManagerModuleCollection<SalvageUnit, SalvageBeamCollectionManager, SalvageElementManager> salvage;
    private ManagerModuleCollection<PushPulseUnit, PushPulseCollectionManager, PushPulseElementManager> pushPulse;
    private ManagerModuleCollection<DamageBeamUnit, DamageBeamCollectionManager, DamageBeamElementManager> damageBeam;
    private List<ManagerModuleSingle<ReactorChamberUnit, ReactorChamberCollectionManager, ReactorChamberElementManager>> chambers;
    private ManagerModuleSingle<MissileCapacityUnit, MissileCapacityCollectionManager, MissileCapacityElementManager> missileCapacity;
    private FactoryAddOn factory;
    private ShopInventory shopInventory;
    private ManagerModuleSingle<ExplosiveUnit, ExplosiveCollectionManager, ExplosiveElementManager> explosive;
    private Long2ObjectOpenHashMap<String> warpDestinationMap;
    private Long2ObjectOpenHashMap<Vector3i> warpDestinationLocalMap;
    private Long2BooleanOpenHashMap warpValidInitialMap = new Long2BooleanOpenHashMap();
    private Long2ObjectOpenHashMap<String> raceDestinationMap;
    private Long2ObjectOpenHashMap<Vector3i> raceDestinationLocalMap;
    private Long2BooleanOpenHashMap raceValidInitialMap = new Long2BooleanOpenHashMap();
    private ManagerModuleCollection<ScannerUnit, ScannerCollectionManager, ScannerElementManager> scanner;
    private ManagerModuleCollection<ShipyardUnit, ShipyardCollectionManager, ShipyardElementManager> shipyard;
    private ManagerModuleCollection<ShopUnit, ShopCollectionManager, ShopElementManager> shopManager;
    private TradeNode tradeNode;
    private ManagerModuleCollection<SensorUnit, SensorCollectionManager, SensorElementManager> sensor;
    private boolean wasValidTradeNode;
    private ManagerModuleCollection<RailConnectionUnit, RailConnectionCollectionManager, RailConnectionElementManager> railSys;
    private boolean flagSendMissileCapacity;
    private static boolean[] specialBlocksStatic;
    private ManagerModuleCollection<HeatEffectUnit, HeatEffectCollectionManager, HeatEffectElementManager> heatEffect;
    private ManagerModuleCollection<KineticEffectUnit, KineticEffectCollectionManager, KineticEffectElementManager> kineticEffect;
    private ManagerModuleCollection<EmEffectUnit, EmEffectCollectionManager, EmEffectElementManager> emEffect;

    public StationaryManagerContainer(StateInterface var1, E var2) {
        super(var1, var2);
    }

    public boolean isPowerBatteryAlwaysOn() {
        return true;
    }

    public static final Vector3i getActiveWarpGate(String var0) {
        FileExt var1 = new FileExt(GameServerState.ENTITY_DATABASE_PATH + var0 + ".ent");

        try {
            if (var1.exists() && (var1.getName().startsWith(EntityType.SPACE_STATION.dbPrefix) || var1.getName().startsWith(EntityType.PLANET_SEGMENT.dbPrefix))) {
                System.err.println("[WARPGATE] CHECKING Destination Gate: " + var1.getName());
                Tag var8 = Tag.readFrom(new BufferedInputStream(new FileInputStream(var1)), true, false);
                Tag var2 = null;
                if (var1.getName().startsWith(EntityType.SPACE_STATION.dbPrefix)) {
                    var2 = ((Tag[])var8.getValue())[1];
                } else if (var1.getName().startsWith(EntityType.PLANET_SEGMENT.dbPrefix)) {
                    var2 = ((Tag[])var8.getValue())[2];
                }

                assert var2 != null;

                byte var9 = 0;
                if (var2.getName().equals("sc")) {
                    var9 = 8;
                }

                Tag[] var11;
                (var11 = (Tag[])var2.getValue())[6].getValue();
                var11 = (Tag[])var11[7].getValue();
                Tag var3 = null;
                if (var11.length > 8 && var11[8].getType() != Type.FINISH) {
                    var3 = var11[8];
                } else {
                    System.err.println("[WARPGATE] Destination Gate: " + var1.getName() + " has no warpgateInfo");
                }

                if (var3 != null && var3.getType() != Type.BYTE) {
                    Tag[] var10 = (Tag[])var3.getValue();

                    for(int var13 = 0; var13 < var10.length - 1; ++var13) {
                        Tag[] var12 = (Tag[])var10[var13].getValue();
                        Vector3i var4 = new Vector3i();
                        if (var12[0].getType() == Type.VECTOR3i) {
                            var4.set(var12[0].getVector3i());
                        } else {
                            ElementCollection.getPosFromIndex(var12[0].getLong(), var4);
                        }

                        var4.add(var9, var9, var9);
                        boolean var5 = (Byte)(var12 = (Tag[])var12[1].getValue())[0].getValue() != 0;
                        var12[1].getValue();
                        if (var5) {
                            System.err.println("[WARPGATE] Destination Gate: " + var13 + " is valid");
                            return var4;
                        }

                        System.err.println("[WARPGATE] Destination Gate: " + var13 + " is not valid");
                    }
                } else {
                    System.err.println("[WARPGATE] Destination Gate: no data");
                }
            } else {
                System.err.println("[WARPGATE] DESTINATION NOT FOUND: " + var0);
            }
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return null;
    }

    public void fillInventory(boolean var1, boolean var2) throws NoSlotFreeException {
        this.shoppingAddOn.fillInventory(var1, var2);
    }

    public long getCredits() {
        return this.shoppingAddOn.getCredits();
    }

    public ShopNetworkInterface getNetworkObject() {
        return (ShopNetworkInterface)this.getSegmentController().getNetworkObject();
    }

    public long getPermissionToPurchase() {
        return this.shoppingAddOn.getPermissionToPurchase();
    }

    public TradePriceInterface getPrice(short var1, boolean var2) {
        return this.shoppingAddOn.getPrice(var1, var2);
    }

    public int getSectorId() {
        return this.getSegmentController().getSectorId();
    }

    public SegmentBufferInterface getSegmentBuffer() {
        return this.getSegmentController().getSegmentBuffer();
    }

    public ShopInventory getShopInventory() {
        Faction var1;
        return (var1 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId())) != null && var1.isNPC() && (EntityType.SPACE_STATION.dbPrefix + DatabaseEntry.removePrefixWOException(var1.getHomebaseUID())).equals(this.getSegmentController().getUniqueIdentifier()) ? (ShopInventory)((NPCFaction)var1).getInventory() : this.shopInventory;
    }

    public Set<String> getShopOwners() {
        return this.shoppingAddOn.getOwnerPlayers();
    }

    public ShoppingAddOn getShoppingAddOn() {
        return this.shoppingAddOn;
    }

    public Transform getWorldTransform() {
        return this.getSegmentController().getWorldTransform();
    }

    public void modCredits(long var1) {
        this.shoppingAddOn.modCredits(var1);
    }

    public int getFactionId() {
        return this.getSegmentController().getFactionId();
    }

    public boolean isInfiniteSupply() {
        return this.shoppingAddOn.isInfiniteSupply();
    }

    public boolean isAiShop() {
        return this.shoppingAddOn.isAIShop();
    }

    protected void fromExtraTag(Tag var1) {
        if (var1.getValue() != null && var1 != null && var1.getType() == Type.STRUCT) {
            Tag[] var2 = (Tag[])var1.getValue();
            this.shopInventory.fromTagStructure(var2[0]);
            this.shoppingAddOn.fromTagStructure(var2[1]);
            if (var2.length > 2 && var2[2].getType() == Type.STRUCT) {
                ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).readFromTag(var2[2]);
            }
        }

    }

    protected Tag getExtraTag() {
        return new Tag(Type.STRUCT, "exS", new Tag[]{this.shopInventory.toTagStructure(), this.shoppingAddOn.toTagStructure(), ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).toTag(), FinishTag.INST});
    }

    public void sendInventoryModification(IntCollection var1, long var2) {
        if (var2 == -9223372036854775808L) {
            InventoryMultMod var4 = new InventoryMultMod(var1, this.shopInventory, var2);
            this.getInventoryInterface().getInventoryMultModBuffer().add(new RemoteInventoryMultMod(var4, this.getSegmentController().getNetworkObject()));
        } else {
            super.sendInventoryModification(var1, var2);
        }
    }

    protected void handleGlobalInventory(InventoryMultMod var1) {
        this.shopInventory.handleReceived(var1, this.getInventoryInterface());
    }

    protected void handleGlobalInventorySlotRemove(InventorySlotRemoveMod var1) {
        boolean var2 = this.isOnServer();
        this.shopInventory.removeSlot(var1.slot, var2);
    }

    public void initFromNetworkObject(NetworkObject var1) {
        super.initFromNetworkObject(var1);
        this.shoppingAddOn.initFromNetwokObject(var1);
    }

    public void initialize(StateInterface var1) {
        this.powerAddOn = new PowerAddOn(this, this.getSegmentController());
        this.shoppingAddOn = new ShoppingAddOn(this);
        this.shieldAddOn = new ShieldAddOn(this, this.getSegmentController());
        this.modules.add(this.explosive = new ManagerModuleSingle(new ExplosiveElementManager(this.getSegmentController()), (short)1, (short)14));
        this.modules.add(this.lift = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), LiftCollectionManager.class), (short)0, (short)113));
        this.modules.add(this.door = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), DoorCollectionManager.class), (short)0, (short)122));
        this.modules.add(this.railPickup = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), RailPickupCollectionManager.class), (short)0, (short)937));
        this.modules.add(this.shields = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), ShieldRegenCollectionManager.class), (short)0, (short)478));
        this.modules.add(this.shieldCapacity = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), ShieldCapacityCollectionManager.class), (short)0, (short)3));
        this.modules.add(this.power = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerCollectionManager.class), (short)0, (short)2));
        this.modules.add(this.missileCapacity = new ManagerModuleSingle(new MissileCapacityElementManager(this.getSegmentController()), (short)1, (short)362));
        this.modules.add(this.reactor = new ManagerModuleSingle(new MainReactorElementManager(this.getSegmentController(), MainReactorCollectionManager.class), (short)0, (short)1008));
        this.modules.add(this.stabilizer = new ManagerModuleSingle(new StabilizerElementManager(this.getSegmentController(), StabilizerCollectionManager.class), (short)0, (short)1009));
        this.modules.add(this.conduit = new ManagerModuleSingle(new ConduitElementManager(this.getSegmentController(), ConduitCollectionManager.class), (short)0, (short)1010));
        this.modules.add(this.railMassEnhancer = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), RailMassEnhancerCollectionManager.class), (short)0, (short)671));
        this.modules.add(this.powerCapacity = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerCapacityCollectionManager.class), (short)0, (short)331));
        this.modules.add(this.powerBattery = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerBatteryCollectionManager.class), (short)0, (short)978));
        this.modules.add(this.turretDockingBlock = new ManagerModuleCollection(new TurretDockingBlockElementManager(this.getSegmentController()), (short)7, (short)88));
        this.modules.add(this.fixedDockingBlock = new ManagerModuleCollection(new FixedDockingBlockElementManager(this.getSegmentController()), (short)289, (short)290));
        this.modules.add(this.repair = new ManagerModuleCollection(new RepairElementManager(this.getSegmentController()), (short)39, (short)30));
        this.modules.add(this.warpgate = new ManagerModuleCollection(new WarpgateElementManager(this.getSegmentController(), this.warpDestinationMap = new Long2ObjectOpenHashMap(), this.warpDestinationLocalMap = new Long2ObjectOpenHashMap()), (short)542, (short)543));
        this.modules.add(this.racegate = new ManagerModuleCollection(new RacegateElementManager(this.getSegmentController(), this.raceDestinationMap = new Long2ObjectOpenHashMap(), this.raceDestinationLocalMap = new Long2ObjectOpenHashMap()), (short)683, (short)684));
        this.modules.add(this.activationgate = new ManagerModuleCollection(new ActivationGateElementManager(this.getSegmentController()), (short)685, (short)686));
        this.modules.add(this.transporter = new ManagerModuleCollection(new TransporterElementManager(this.getSegmentController()), (short)687, (short)688));
        this.modules.add(this.shipyard = new ManagerModuleCollection(new ShipyardElementManager(this.getSegmentController()), (short)677, (short)678));
        this.modules.add(this.cargo = new ManagerModuleCollection(new CargoElementManager(this.getSegmentController()), (short)120, (short)689));
        this.modules.add(this.jumpProhibiter = new ManagerModuleCollection(new JumpInhibitorElementManager(this.getSegmentController()), (short)681, (short)682));
        this.modules.add(this.weapon = new ManagerModuleCollection(new WeaponElementManager(this.getSegmentController()), (short)6, (short)16));
        this.modules.add(this.dumbMissile = new ManagerModuleCollection(new DumbMissileElementManager(this.getSegmentController()), (short)38, (short)32));
        this.modules.add(this.activation = new ManagerModuleCollection(new ActivationElementManager(this.getSegmentController()), (short)30000, (short)32767));
        this.modules.add(this.railSpeed = new ManagerModuleCollection(new RailSpeedElementManager(this.getSegmentController()), (short)672, (short)29999));
        this.modules.add(this.scanner = new ManagerModuleCollection(new ScannerElementManager(this.getSegmentController()), (short)654, (short)655));
        this.modules.add(this.salvage = new ManagerModuleCollection(new SalvageElementManager(this.getSegmentController()), (short)4, (short)24));
        this.modules.add(this.pushPulse = new ManagerModuleCollection(new PushPulseElementManager(this.getSegmentController()), (short)344, (short)345));
        this.modules.add(this.damageBeam = new ManagerModuleCollection(new DamageBeamElementManager(this.getSegmentController()), (short)414, (short)415));
        this.modules.add(this.trigger = new ManagerModuleCollection(new TriggerElementManager(this.getSegmentController()), (short)413, (short)411));
        this.modules.add(this.sensor = new ManagerModuleCollection(new SensorElementManager(this.getSegmentController()), (short)980, (short)405));
        this.modules.add(this.empEffect = new ManagerModuleCollection(new EmEffectElementManager(this.getSegmentController()), (short)424, (short)425));
        this.modules.add(this.shopManager = new ManagerModuleCollection(new ShopElementManager(this.getSegmentController()), (short)347, (short)120));
        this.modules.add(this.railSys = new ManagerModuleCollection(new RailConnectionElementManager(this.getSegmentController()), (short)29998, (short)32767));
        this.modules.add(this.heatEffect = new ManagerModuleCollection(new HeatEffectElementManager(this.getSegmentController()), (short)351, (short)352));
        this.modules.add(this.kineticEffect = new ManagerModuleCollection(new KineticEffectElementManager(this.getSegmentController()), (short)353, (short)354));
        this.modules.add(this.emEffect = new ManagerModuleCollection(new EmEffectElementManager(this.getSegmentController()), (short)349, (short)350));
        this.chambers = new ObjectArrayList();
        Iterator var3 = ElementKeyMap.keySet.iterator();

        while(var3.hasNext()) {
            short var2;
            if (ElementKeyMap.getInfoFast(var2 = (Short)var3.next()).isReactorChamberAny()) {
                ManagerModuleSingle var4 = new ManagerModuleSingle(new ReactorChamberElementManager(var2, this.getSegmentController(), ReactorChamberCollectionManager.class), (short)0, var2);
                this.chambers.add(var4);
                this.modules.add(var4);
            }
        }

        this.factory = new FactoryAddOn();
        this.factory.initialize(this.getModules(), this.getSegmentController());
        this.shopInventory = new ShopInventory(this, -9223372036854775808L);

        //INSERTED CODE @599
        ElementRegisterEvent event = new ElementRegisterEvent(this);
        StarLoader.fireEvent(ElementRegisterEvent.class, event, this.isOnServer());

        for (ManagerModule moduleCollection : event.getRegisteredModules()) {
            this.modules.add(moduleCollection);
        }
        ///
    }

    public boolean isValidShop() {
        return this.shopBlockIndex != -9223372036854775808L || this.isNPCHomeBase();
    }

    public Inventory getInventory(long var1) {
        return (Inventory)(var1 == this.shopBlockIndex ? this.shopInventory : super.getInventory(var1));
    }

    public double getCapacityFor(Inventory var1) {
        long var2;
        if (var1 instanceof ShopInventory) {
            if (!this.isValidShop()) {
                assert false;

                return 0.0D;
            }

            var2 = this.shopBlockIndex;
            if (!this.getCargo().getCollectionManagersMap().containsKey(var2)) {
                return CargoElementManager.INVENTORY_BASE_CAPACITY_STATION;
            }
        } else {
            var2 = var1.getParameterIndex();
        }

        CargoCollectionManager var4;
        return (var4 = (CargoCollectionManager)this.getCargo().getCollectionManagersMap().get(var2)) != null ? var4.getCapacity() : 0.0D;
    }

    public Inventory getInventory(Vector3i var1) {
        return (Inventory)(ElementCollection.getIndex(var1) == this.shopBlockIndex ? this.shopInventory : super.getInventory(var1));
    }

    public void onAction() {
    }

    public void onBlockDamage(long var1, short var3, int var4, DamageDealerType var5, Damager var6) {
        super.onBlockDamage(var1, var3, var4, var5, var6);
        this.shoppingAddOn.onHit(var6);
    }

    public boolean isOnServer() {
        return this.getSegmentController().isOnServer();
    }

    protected void onSpecialTypesRemove(short var1, byte var2, byte var3, byte var4, Segment var5, boolean var6) {
        super.onSpecialTypesRemove(var1, var2, var3, var4, var5, var6);
        if (var1 == 347 && !var6) {
            System.err.println(this.getState() + " RESETTING SHOP!");
            this.shoppingAddOn.reset();
        }

    }

    protected void fromWarpGateTag(Tag var1) {
        if (var1.getType() != Type.BYTE) {
            Tag[] var8 = (Tag[])var1.getValue();

            for(int var2 = 0; var2 < var8.length - 1; ++var2) {
                Tag[] var3;
                Vector3i var4;
                if ((var3 = var8[var2].getStruct())[0].getType() == Type.LONG) {
                    var4 = ElementCollection.getPosFromIndex(var3[0].getLong(), new Vector3i());
                } else {
                    var4 = var3[0].getVector3i();
                }

                if (this.getSegmentController().isLoadedFromChunk16()) {
                    var4.add(Chunk16SegmentData.SHIFT);
                }

                Tag[] var6;
                boolean var7 = (var6 = var3[1].getStruct())[0].getByte() != 0;
                String var9 = var6[1].getString();
                Vector3i var5 = new Vector3i();
                if (var6[2].getType() == Type.VECTOR3i) {
                    var5.set((Vector3i)var6[2].getValue());
                    if (this.getSegmentController().isLoadedFromChunk16()) {
                        var5.add(Chunk16SegmentData.SHIFT);
                    }
                }

                this.warpDestinationMap.put(ElementCollection.getIndex(var4), var9);
                this.warpDestinationLocalMap.put(ElementCollection.getIndex(var4), var5);
                this.warpValidInitialMap.put(ElementCollection.getIndex(var4), var7);
            }

        }
    }

    protected Tag getWarpGateTag() {
        Tag[] var1;
        Tag[] var10000 = var1 = new Tag[((WarpgateElementManager)this.warpgate.getElementManager()).getCollectionManagers().size() + this.warpDestinationMap.size() + 1];
        var10000[var10000.length - 1] = FinishTag.INST;

        int var2;
        for(var2 = 0; var2 < ((WarpgateElementManager)this.warpgate.getElementManager()).getCollectionManagers().size(); ++var2) {
            var1[var2] = ((WarpgateCollectionManager)((WarpgateElementManager)this.warpgate.getElementManager()).getCollectionManagers().get(var2)).toTagStructure();
        }

        for(Iterator var3 = this.warpDestinationMap.entrySet().iterator(); var3.hasNext(); ++var2) {
            Entry var4;
            Vector3i var5 = ElementCollection.getPosFromIndex((Long)(var4 = (Entry)var3.next()).getKey(), new Vector3i());
            Tag var6 = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.warpValidInitialMap.get((Long)var4.getKey()) ? 1 : 0)));
            Tag var7 = new Tag(Type.STRING, (String)null, var4.getValue());
            Tag var8 = new Tag(Type.VECTOR3i, (String)null, this.warpDestinationLocalMap.get((Long)var4.getKey()));
            var8 = new Tag(Type.STRUCT, (String)null, new Tag[]{var6, var7, var8, FinishTag.INST});
            var1[var2] = new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.VECTOR3i, (String)null, var5), var8, FinishTag.INST});
        }

        return new Tag(Type.STRUCT, (String)null, var1);
    }

    protected void fromRaceGateTag(Tag var1) {
        if (var1.getType() != Type.BYTE) {
            Tag[] var8 = (Tag[])var1.getValue();

            for(int var2 = 0; var2 < var8.length - 1; ++var2) {
                Tag[] var3;
                Vector3i var4;
                if ((var3 = (Tag[])var8[var2].getValue())[0].getType() == Type.VECTOR3i) {
                    var4 = var3[0].getVector3i();
                } else {
                    var4 = ElementCollection.getPosFromIndex(var3[0].getLong(), new Vector3i());
                }

                if (this.getSegmentController().isLoadedFromChunk16()) {
                    var4.add(Chunk16SegmentData.SHIFT);
                }

                boolean var5 = (var3 = (Tag[])var3[1].getValue())[0].getByte() != 0;
                String var6 = (String)var3[1].getValue();
                Vector3i var7 = new Vector3i();
                if (var3[2].getType() == Type.VECTOR3i) {
                    var7.set((Vector3i)var3[2].getValue());
                    if (this.getSegmentController().isLoadedFromChunk16()) {
                        var7.add(Chunk16SegmentData.SHIFT);
                    }
                }

                this.raceDestinationMap.put(ElementCollection.getIndex(var4), var6);
                this.raceDestinationLocalMap.put(ElementCollection.getIndex(var4), var7);
                this.raceValidInitialMap.put(ElementCollection.getIndex(var4), var5);
            }

        }
    }

    protected Tag getRaceGateTag() {
        Tag[] var1;
        Tag[] var10000 = var1 = new Tag[((RacegateElementManager)this.racegate.getElementManager()).getCollectionManagers().size() + this.raceDestinationMap.size() + 1];
        var10000[var10000.length - 1] = FinishTag.INST;

        int var2;
        for(var2 = 0; var2 < ((RacegateElementManager)this.racegate.getElementManager()).getCollectionManagers().size(); ++var2) {
            var1[var2] = ((RacegateCollectionManager)((RacegateElementManager)this.racegate.getElementManager()).getCollectionManagers().get(var2)).toTagStructure();
        }

        for(Iterator var3 = this.raceDestinationMap.entrySet().iterator(); var3.hasNext(); ++var2) {
            Entry var4;
            Vector3i var5 = ElementCollection.getPosFromIndex((Long)(var4 = (Entry)var3.next()).getKey(), new Vector3i());
            Tag var6 = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.raceValidInitialMap.get((Long)var4.getKey()) ? 1 : 0)));
            Tag var7 = new Tag(Type.STRING, (String)null, var4.getValue());
            Tag var8 = new Tag(Type.VECTOR3i, (String)null, this.raceDestinationLocalMap.get((Long)var4.getKey()));
            var8 = new Tag(Type.STRUCT, (String)null, new Tag[]{var6, var7, var8, FinishTag.INST});
            var1[var2] = new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.VECTOR3i, (String)null, var5), var8, FinishTag.INST});
        }

        return new Tag(Type.STRUCT, (String)null, var1);
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        super.updateFromNetworkObject(var1, var2);
        this.shoppingAddOn.receivePrices(false);
    }

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this.getSegmentController(), "StationManagerContainerUpdate");
        this.powerAddOn.update(var1);
        super.updateLocal(var1);
        this.factory.update(var1, this.getSegmentController().isOnServer());
        this.shieldAddOn.update(var1);
        this.shoppingAddOn.setActive(this.getSegmentController().getElementClassCountMap().get((short)347) > 0);
        this.shoppingAddOn.update(var1.currentTime);
        if (this.isValidShop()) {
            this.wasValidTradeNode = true;
        }

        if (this.flagSendMissileCapacity) {
            ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).sendMissileCapacity();
            this.flagSendMissileCapacity = false;
        }

        this.getState().getDebugTimer().end(this.getSegmentController(), "StationManagerContainerUpdate");
    }

    protected void handleShopInventoryReceived(ShopInventory var1) {
        System.err.println("[CLIENT] Received shop inventory on " + this.getName());
        this.shopInventory = var1;
    }

    public void updateToFullNetworkObject(NetworkObject var1) {
        super.updateToFullNetworkObject(var1);
        this.shopInventory.sendAll();
        this.shoppingAddOn.updateToFullNT();
    }

    public void updateToNetworkObject(NetworkObject var1) {
        super.updateToNetworkObject(var1);
    }

    public boolean isTargetLocking(SegmentPiece var1) {
        return ((DumbMissileElementManager)this.dumbMissile.getElementManager()).isTargetLocking(var1);
    }

    public ModuleStatistics<E, ? extends ManagerContainer<E>> getStatisticsManager() {
        return null;
    }

    public Collection<ManagerModuleCollection<? extends DockingBlockUnit<?, ?, ?>, ? extends DockingBlockCollectionManager<?, ?, ?>, ? extends DockingBlockElementManager<?, ?, ?>>> getDockingBlock() {
        return this.dockingModules;
    }

    public ManagerModuleSingle<DoorUnit, DoorCollectionManager, VoidElementManager<DoorUnit, DoorCollectionManager>> getDoor() {
        return this.door;
    }

    private NetworkDoorInterface getDoorInterface() {
        return (NetworkDoorInterface)this.getSegmentController().getNetworkObject();
    }

    public DoorCollectionManager getDoorManager() {
        return (DoorCollectionManager)this.door.getCollectionManager();
    }

    public void handleClientRemoteDoor(Vector3i var1) {
        this.getDoorInterface().getDoorActivate().forceClientUpdates();
        Vector4i var2;
        (var2 = new Vector4i(var1)).w = -1;
        this.getDoorInterface().getDoorActivate().add(new RemoteVector4i(var2, this.getSegmentController().getNetworkObject()));
    }

    public ManagerModuleCollection<DumbMissileUnit, DumbMissileCollectionManager, DumbMissileElementManager> getMissile() {
        return this.dumbMissile;
    }

    public FactoryAddOn getFactory() {
        return this.factory;
    }

    public ExplosiveElementManager getExplosiveElementManager() {
        return (ExplosiveElementManager)this.explosive.getElementManager();
    }

    public ExplosiveCollectionManager getExplosiveCollectionManager() {
        return (ExplosiveCollectionManager)this.explosive.getCollectionManager();
    }

    public ManagerModuleSingle<ExplosiveUnit, ExplosiveCollectionManager, ExplosiveElementManager> getExplosive() {
        return this.explosive;
    }

    public ManagerModuleCollection<FixedDockingBlockUnit, FixedDockingBlockCollectionManager, FixedDockingBlockElementManager> getFixedDockingBlock() {
        return this.fixedDockingBlock;
    }

    public NetworkInventoryInterface getInventoryNetworkObject() {
        return (NetworkInventoryInterface)this.getSegmentController().getNetworkObject();
    }

    public int getId() {
        return this.getSegmentController().getId();
    }

    public ManagerModuleSingle<LiftUnit, LiftCollectionManager, VoidElementManager<LiftUnit, LiftCollectionManager>> getLift() {
        return this.lift;
    }

    private NetworkLiftInterface getLiftInterface() {
        return (NetworkLiftInterface)this.getSegmentController().getNetworkObject();
    }

    public LiftCollectionManager getLiftManager() {
        return (LiftCollectionManager)this.lift.getCollectionManager();
    }

    public void handleClientRemoteLift(Vector3i var1) {
        this.getLiftInterface().getLiftActivate().forceClientUpdates();
        Vector4i var2;
        (var2 = new Vector4i(var1)).w = 1;
        this.getLiftInterface().getLiftActivate().add(new RemoteVector4i(var2, this.getSegmentController().getNetworkObject()));
    }

    public ManagerModuleSingle<PowerUnit, PowerCollectionManager, VoidElementManager<PowerUnit, PowerCollectionManager>> getPower() {
        return this.power;
    }

    public PowerAddOn getPowerAddOn() {
        return this.powerAddOn;
    }

    public PowerCapacityCollectionManager getPowerCapacityManager() {
        return (PowerCapacityCollectionManager)this.powerCapacity.getCollectionManager();
    }

    public PowerBatteryCollectionManager getPowerBatteryManager() {
        return (PowerBatteryCollectionManager)this.powerBattery.getCollectionManager();
    }

    public PowerCollectionManager getPowerManager() {
        return (PowerCollectionManager)this.power.getCollectionManager();
    }

    public ManagerModuleCollection<RepairUnit, RepairBeamCollectionManager, RepairElementManager> getRepair() {
        return this.repair;
    }

    public ShieldRegenCollectionManager getShieldRegenManager() {
        return (ShieldRegenCollectionManager)this.shields.getCollectionManager();
    }

    public ShieldCapacityCollectionManager getShieldCapacityManager() {
        return (ShieldCapacityCollectionManager)this.shieldCapacity.getCollectionManager();
    }

    public ShieldAddOn getShieldAddOn() {
        return this.shieldAddOn;
    }

    public ManagerModuleSingle<ShieldRegenUnit, ShieldRegenCollectionManager, VoidElementManager<ShieldRegenUnit, ShieldRegenCollectionManager>> getShields() {
        return this.shields;
    }

    public ManagerModuleCollection<TurretDockingBlockUnit, TurretDockingBlockCollectionManager, TurretDockingBlockElementManager> getTurretDockingBlock() {
        return this.turretDockingBlock;
    }

    public ManagerModuleCollection<WeaponUnit, WeaponCollectionManager, WeaponElementManager> getWeapon() {
        return this.weapon;
    }

    public ManagerModuleCollection<TriggerUnit, TriggerCollectionManager, TriggerElementManager> getTrigger() {
        return this.trigger;
    }

    public ManagerModuleCollection<EmEffectUnit, EmEffectCollectionManager, EmEffectElementManager> getEmpEffect() {
        return this.empEffect;
    }

    public EffectElementManager<?, ?, ?> getEffect(short var1) {
        if (var1 == 0) {
            return null;
        } else if (!this.effectMap.containsKey(var1)) {
            throw new RuntimeException("CRITICAL: invalid weapon effect referenced " + var1 + ": " + this.effectMap);
        } else {
            return (EffectElementManager)this.effectMap.get(var1).getElementManager();
        }
    }

    public ManagerModuleCollection<AbstractUnit, ActivationCollectionManager, ActivationElementManager> getActivation() {
        return this.activation;
    }

    public ManagerModuleCollection<WarpgateUnit, WarpgateCollectionManager, WarpgateElementManager> getWarpgate() {
        return this.warpgate;
    }

    public ManagerModuleCollection<RacegateUnit, RacegateCollectionManager, RacegateElementManager> getRacegate() {
        return this.racegate;
    }

    public ManagerModuleCollection<ScannerUnit, ScannerCollectionManager, ScannerElementManager> getScanner() {
        return this.scanner;
    }

    public ManagerModuleCollection<RailSpeedUnit, RailSpeedCollectionManager, RailSpeedElementManager> getRailSpeed() {
        return this.railSpeed;
    }

    public ManagerModuleSingle<RailMassEnhancerUnit, RailMassEnhancerCollectionManager, VoidElementManager<RailMassEnhancerUnit, RailMassEnhancerCollectionManager>> getRailMassEnhancer() {
        return this.railMassEnhancer;
    }

    public ManagerModuleCollection<ShipyardUnit, ShipyardCollectionManager, ShipyardElementManager> getShipyard() {
        return this.shipyard;
    }

    public ManagerModuleCollection<JumpInhibitorUnit, JumpInhibitorCollectionManager, JumpInhibitorElementManager> getJumpProhibiter() {
        return this.jumpProhibiter;
    }

    public ManagerModuleCollection<TransporterUnit, TransporterCollectionManager, TransporterElementManager> getTransporter() {
        return this.transporter;
    }

    public ManagerModuleCollection<CargoUnit, CargoCollectionManager, CargoElementManager> getCargo() {
        return this.cargo;
    }

    public long getPermissionToTrade() {
        return this.shoppingAddOn.getPermissionToTrade();
    }

    public ManagerModuleSingle<RailPickupUnit, RailPickupCollectionManager, VoidElementManager<RailPickupUnit, RailPickupCollectionManager>> getRailPickup() {
        return this.railPickup;
    }

    public boolean isTradeNode() {
        return this.shoppingAddOn.isTradeNode();
    }

    public TradeNode getTradeNode() {
        if (this.tradeNode == null) {
            this.tradeNode = new TradeNode();
            this.tradeNode.setFromShop(this);
        }

        return this.tradeNode;
    }

    public ManagerModuleCollection<ShopUnit, ShopCollectionManager, ShopElementManager> getShopManager() {
        return this.shopManager;
    }

    public ManagerModuleCollection<SensorUnit, SensorCollectionManager, SensorElementManager> getSensor() {
        return this.sensor;
    }

    public boolean isNPCHomeBase() {
        Faction var1;
        return (var1 = this.getSegmentController().getFaction()) != null && var1.isNPC() && this.getSegmentController().getUniqueIdentifier().equals(((NPCFaction)var1).getHomebaseUID());
    }

    public int getPriceString(ElementInformation var1, boolean var2) {
        return this.shoppingAddOn.getPriceString(var1, var2);
    }

    public boolean wasValidTradeNode() {
        return this.wasValidTradeNode;
    }

    public MainReactorCollectionManager getMainReactor() {
        return (MainReactorCollectionManager)this.reactor.getCollectionManager();
    }

    public StabilizerCollectionManager getStabilizer() {
        return (StabilizerCollectionManager)this.stabilizer.getCollectionManager();
    }

    public List<ManagerModuleSingle<ReactorChamberUnit, ReactorChamberCollectionManager, ReactorChamberElementManager>> getChambers() {
        return this.chambers;
    }

    public ConduitCollectionManager getConduit() {
        return (ConduitCollectionManager)this.conduit.getCollectionManager();
    }

    public ManagerModuleCollection<RailConnectionUnit, RailConnectionCollectionManager, RailConnectionElementManager> getRailSys() {
        return this.railSys;
    }

    protected void afterInitialize() {
        if (specialBlocksStatic == null || specialBlocksStatic.length != ElementKeyMap.highestType) {
            specialBlocksStatic = new boolean[ElementKeyMap.highestType + 1];
            this.getSpecialMap(specialBlocksStatic);
        }

        this.specialBlocks = specialBlocksStatic;
    }

    public float getMissileCapacity() {
        return ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).getMissileCapacity();
    }

    public float getMissileCapacityMax() {
        return ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).getMissileCapacityMax();
    }

    public float getMissileCapacityTimer() {
        return ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).getMissileCapacityTimer();
    }

    public float getMissileCapacityReloadTime() {
        return ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).getMissileCapacityReloadTime();
    }

    public void setMissileCapacity(float var1, float var2, boolean var3) {
        ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).setMissileCapacity(var1);
        ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).setMissileTimer(var2);
        if (var3) {
            this.flagSendMissileCapacity = true;
        }

    }

    public AcidFormulaType getAcidType(long var1) {
        WeaponCollectionManager var3;
        return (var3 = (WeaponCollectionManager)this.getWeapon().getCollectionManagersMap().get(var1)) != null ? var3.getAcidFormula() : AcidFormulaType.EQUAL_DIST;
    }

    public ManagerModuleCollection<DamageBeamUnit, DamageBeamCollectionManager, DamageBeamElementManager> getBeam() {
        return this.damageBeam;
    }

    public ManagerModuleCollection<SalvageUnit, SalvageBeamCollectionManager, SalvageElementManager> getSalvage() {
        return this.salvage;
    }
}
