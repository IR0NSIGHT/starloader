//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements;

import api.listener.events.register.ElementRegisterEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ThrustConfiguration;
import org.schema.game.common.controller.damage.acid.AcidDamageFormula.AcidFormulaType;
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
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorElementManager;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorUnit;
import org.schema.game.common.controller.elements.cargo.CargoCollectionManager;
import org.schema.game.common.controller.elements.cargo.CargoElementManager;
import org.schema.game.common.controller.elements.cargo.CargoUnit;
import org.schema.game.common.controller.elements.cloaking.CloakingCollectionManager;
import org.schema.game.common.controller.elements.cloaking.CloakingElementManager;
import org.schema.game.common.controller.elements.cloaking.CloakingUnit;
import org.schema.game.common.controller.elements.dockingBeam.ActivationBeamElementManager;
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
import org.schema.game.common.controller.elements.jamming.JammingCollectionManager;
import org.schema.game.common.controller.elements.jamming.JammingElementManager;
import org.schema.game.common.controller.elements.jamming.JammingUnit;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;
import org.schema.game.common.controller.elements.jumpdrive.JumpDriveCollectionManager;
import org.schema.game.common.controller.elements.jumpdrive.JumpDriveElementManager;
import org.schema.game.common.controller.elements.jumpdrive.JumpDriveUnit;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorCollectionManager;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorElementManager;
import org.schema.game.common.controller.elements.jumpprohibiter.JumpInhibitorUnit;
import org.schema.game.common.controller.elements.mines.MineLayerCollectionManager;
import org.schema.game.common.controller.elements.mines.MineLayerElementManager;
import org.schema.game.common.controller.elements.mines.MineLayerUnit;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityCollectionManager;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityElementManager;
import org.schema.game.common.controller.elements.missile.capacity.MissileCapacityUnit;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileCollectionManager;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileElementManager;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileUnit;
import org.schema.game.common.controller.elements.power.PowerAddOn;
import org.schema.game.common.controller.elements.power.PowerCollectionManager;
import org.schema.game.common.controller.elements.power.PowerUnit;
import org.schema.game.common.controller.elements.power.reactor.*;
import org.schema.game.common.controller.elements.power.reactor.chamber.*;
import org.schema.game.common.controller.elements.powerbattery.PowerBatteryCollectionManager;
import org.schema.game.common.controller.elements.powerbattery.PowerBatteryUnit;
import org.schema.game.common.controller.elements.powercap.PowerCapacityCollectionManager;
import org.schema.game.common.controller.elements.powercap.PowerCapacityUnit;
import org.schema.game.common.controller.elements.pulse.push.PushPulseCollectionManager;
import org.schema.game.common.controller.elements.pulse.push.PushPulseElementManager;
import org.schema.game.common.controller.elements.pulse.push.PushPulseUnit;
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
import org.schema.game.common.controller.elements.railbeam.RailBeamElementManager;
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
import org.schema.game.common.controller.elements.thrust.ThrusterCollectionManager;
import org.schema.game.common.controller.elements.thrust.ThrusterElementManager;
import org.schema.game.common.controller.elements.thrust.ThrusterUnit;
import org.schema.game.common.controller.elements.transporter.TransporterCollectionManager;
import org.schema.game.common.controller.elements.transporter.TransporterElementManager;
import org.schema.game.common.controller.elements.transporter.TransporterUnit;
import org.schema.game.common.controller.elements.trigger.TriggerCollectionManager;
import org.schema.game.common.controller.elements.trigger.TriggerElementManager;
import org.schema.game.common.controller.elements.trigger.TriggerUnit;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.ScannerManagerInterface;
import org.schema.game.common.data.physics.RepulseHandler;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.common.data.world.Segment;
import org.schema.game.network.objects.NetworkDoorInterface;
import org.schema.game.network.objects.NetworkShip;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.remote.RemoteBoolean;
import org.schema.schine.network.objects.remote.RemoteVector4i;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ShipManagerContainer extends ManagerContainer<Ship> implements ActivationManagerInterface, DoorContainerInterface, EffectManagerContainer, ExplosiveManagerContainerInterface, JumpProhibiterModuleInterface, ManagerThrustInterface, MissileModuleInterface, PulseManagerInterface, RailManagerInterface, SalvageManagerContainer, ShieldContainerInterface, TransporterModuleInterface, TriggerManagerInterface, WeaponManagerInterface, DockingBlockManagerInterface, ScannerManagerInterface {
    private PowerAddOn powerAddOn;
    private ShieldAddOn shieldAddOn;
    private final ShipManagerModuleStatistics statisticsManager;
    private ActivationBeamElementManager dockingBeam;
    private RailBeamElementManager railBeam;
    private ManagerModuleSingle<ShieldRegenUnit, ShieldRegenCollectionManager, VoidElementManager<ShieldRegenUnit, ShieldRegenCollectionManager>> shields;
    private ManagerModuleSingle<ShieldCapacityUnit, ShieldCapacityCollectionManager, VoidElementManager<ShieldCapacityUnit, ShieldCapacityCollectionManager>> shieldCapacity;
    private ManagerModuleSingle<ThrusterUnit, ThrusterCollectionManager, ThrusterElementManager> thrust;
    private ManagerModuleSingle<CloakingUnit, CloakingCollectionManager, CloakingElementManager> cloaking;
    private ManagerModuleSingle<ExplosiveUnit, ExplosiveCollectionManager, ExplosiveElementManager> explosive;
    private ManagerModuleSingle<JammingUnit, JammingCollectionManager, JammingElementManager> jamming;
    private ManagerModuleSingle<RailPickupUnit, RailPickupCollectionManager, VoidElementManager<RailPickupUnit, RailPickupCollectionManager>> railPickup;
    private ManagerModuleCollection<ActivationGateUnit, ActivationGateCollectionManager, ActivationGateElementManager> activationgate;
    private ManagerModuleCollection<TransporterUnit, TransporterCollectionManager, TransporterElementManager> transporter;
    private ManagerModuleSingle<PowerUnit, PowerCollectionManager, VoidElementManager<PowerUnit, PowerCollectionManager>> power;
    private ManagerModuleSingle<MainReactorUnit, MainReactorCollectionManager, MainReactorElementManager> reactor;
    private ManagerModuleSingle<StabilizerUnit, StabilizerCollectionManager, StabilizerElementManager> stabilizer;
    private ManagerModuleSingle<ConduitUnit, ConduitCollectionManager, ConduitElementManager> conduit;
    private ManagerModuleSingle<RailMassEnhancerUnit, RailMassEnhancerCollectionManager, VoidElementManager<RailMassEnhancerUnit, RailMassEnhancerCollectionManager>> railMassEnhancer;
    private ManagerModuleSingle<PowerCapacityUnit, PowerCapacityCollectionManager, VoidElementManager<PowerCapacityUnit, PowerCapacityCollectionManager>> powerCapacity;
    private ManagerModuleSingle<PowerBatteryUnit, PowerBatteryCollectionManager, VoidElementManager<PowerBatteryUnit, PowerBatteryCollectionManager>> powerBattery;
    private ManagerModuleSingle<DoorUnit, DoorCollectionManager, VoidElementManager<DoorUnit, DoorCollectionManager>> door;
    private ManagerModuleCollection<SalvageUnit, SalvageBeamCollectionManager, SalvageElementManager> salvage;
    private ManagerModuleCollection<AbstractUnit, ActivationCollectionManager, ActivationElementManager> activation;
    private ManagerModuleCollection<RailSpeedUnit, RailSpeedCollectionManager, RailSpeedElementManager> railSpeed;
    private ManagerModuleCollection<TurretDockingBlockUnit, TurretDockingBlockCollectionManager, TurretDockingBlockElementManager> turretDockingBlock;
    private ManagerModuleCollection<FixedDockingBlockUnit, FixedDockingBlockCollectionManager, FixedDockingBlockElementManager> fixedDockingBlock;
    private ManagerModuleCollection<RepairUnit, RepairBeamCollectionManager, RepairElementManager> repair;
    private ManagerModuleCollection<TractorUnit, TractorBeamCollectionManager, TractorElementManager> tractorBeam;
    private ManagerModuleCollection<WeaponUnit, WeaponCollectionManager, WeaponElementManager> weapon;
    private ManagerModuleCollection<JumpDriveUnit, JumpDriveCollectionManager, JumpDriveElementManager> jumpDrive;
    private ManagerModuleCollection<JumpInhibitorUnit, JumpInhibitorCollectionManager, JumpInhibitorElementManager> jumpProhibiter;
    private ManagerModuleCollection<ScannerUnit, ScannerCollectionManager, ScannerElementManager> scanner;
    private ManagerModuleCollection<CargoUnit, CargoCollectionManager, CargoElementManager> cargo;
    private ManagerModuleCollection<PushPulseUnit, PushPulseCollectionManager, PushPulseElementManager> pushPulse;
    private ManagerModuleCollection<DamageBeamUnit, DamageBeamCollectionManager, DamageBeamElementManager> damageBeam;
    private ManagerModuleCollection<TriggerUnit, TriggerCollectionManager, TriggerElementManager> trigger;
    private ManagerModuleCollection<DumbMissileUnit, DumbMissileCollectionManager, DumbMissileElementManager> dumbMissile;
    private ManagerModuleSingle<MissileCapacityUnit, MissileCapacityCollectionManager, MissileCapacityElementManager> missileCapacity;
    private ManagerModuleCollection<SensorUnit, SensorCollectionManager, SensorElementManager> sensor;
    private ManagerModuleCollection<RailConnectionUnit, RailConnectionCollectionManager, RailConnectionElementManager> railSys;
    private ManagerModuleCollection<MineLayerUnit, MineLayerCollectionManager, MineLayerElementManager> mineLayer;
    private ManagerModuleCollection<HeatEffectUnit, HeatEffectCollectionManager, HeatEffectElementManager> heatEffect;
    private ManagerModuleCollection<KineticEffectUnit, KineticEffectCollectionManager, KineticEffectElementManager> kineticEffect;
    private ManagerModuleCollection<EmEffectUnit, EmEffectCollectionManager, EmEffectElementManager> emEffect;
    private List<ManagerModuleSingle<ReactorChamberUnit, ReactorChamberCollectionManager, ReactorChamberElementManager>> chambers;
    public final ThrustConfiguration thrustConfiguration;
    private JumpAddOn jumpAddOn;
    private RepulseHandler repulseManager;
    private boolean flagSendMissileCapacity;
    private final CockpitManager cockpitManager;
    private static boolean[] specialBlocksStatic;
    private FactoryAddOn factory;

    public ShipManagerContainer(StateInterface var1, Ship var2) {
        super(var1, var2);
        this.statisticsManager = new ShipManagerModuleStatistics(var2, this);
        this.thrustConfiguration = new ThrustConfiguration(var2);
        this.cockpitManager = new CockpitManager(this);
    }

    protected Tag getExtraTag() {
        return new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.LONG, (String)null, ((Ship)this.getSegmentController()).lastPickupAreaUsed), this.thrustConfiguration.toTag(), ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).toTag(), this.cockpitManager.toTagStructure(), FinishTag.INST});
    }

    protected void fromExtraTag(Tag var1) {
        if (var1.getType() == Type.STRUCT) {
            Tag[] var2;
            if ((var2 = (Tag[])var1.getValue())[0].getType() == Type.LONG) {
                ((Ship)this.getSegmentController()).lastPickupAreaUsed = (Long)var2[0].getValue();
                if (((Ship)this.getSegmentController()).lastPickupAreaUsed != -9223372036854775808L && ((Ship)this.getSegmentController()).isLoadedFromChunk16()) {
                    ((Ship)this.getSegmentController()).lastPickupAreaUsed = ElementCollection.shiftIndex(((Ship)this.getSegmentController()).lastPickupAreaUsed, 8, 8, 8);
                }
            }

            if (var2.length > 1 && var2[1].getType() == Type.STRUCT) {
                this.thrustConfiguration.readFromTag(var2[1]);
            }

            if (var2.length > 2 && var2[2].getType() == Type.STRUCT) {
                ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).readFromTag(var2[2]);
            }

            if (var2.length > 3 && var2[3].getType() == Type.STRUCT) {
                this.cockpitManager.fromTagStructure(var2[3]);
            }
        }

    }

    public CloakingElementManager getCloakElementManager() {
        return (CloakingElementManager)this.cloaking.getElementManager();
    }

    public ManagerModuleSingle<CloakingUnit, CloakingCollectionManager, CloakingElementManager> getCloaking() {
        return this.cloaking;
    }

    public ActivationBeamElementManager getDockingBeam() {
        return this.dockingBeam;
    }

    public RailBeamElementManager getRailBeam() {
        return this.railBeam;
    }

    public Collection<ManagerModuleCollection<? extends DockingBlockUnit<?, ?, ?>, ? extends DockingBlockCollectionManager<?, ?, ?>, ? extends DockingBlockElementManager<?, ?, ?>>> getDockingBlock() {
        return this.dockingModules;
    }

    public ManagerModuleSingle<DoorUnit, DoorCollectionManager, VoidElementManager<DoorUnit, DoorCollectionManager>> getDoor() {
        return this.door;
    }

    private NetworkDoorInterface getDoorInterface() {
        return ((Ship)this.getSegmentController()).getNetworkObject();
    }

    public DoorCollectionManager getDoorManager() {
        return (DoorCollectionManager)this.door.getCollectionManager();
    }

    public void handleClientRemoteDoor(Vector3i var1) {
        this.getDoorInterface().getDoorActivate().forceClientUpdates();
        Vector4i var2;
        (var2 = new Vector4i(var1)).w = -1;
        this.getDoorInterface().getDoorActivate().add(new RemoteVector4i(var2, ((Ship)this.getSegmentController()).getNetworkObject()));
    }

    public ManagerModuleCollection<DumbMissileUnit, DumbMissileCollectionManager, DumbMissileElementManager> getMissile() {
        return this.dumbMissile;
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
        return ((Ship)this.getSegmentController()).getNetworkObject();
    }

    public int getId() {
        return ((Ship)this.getSegmentController()).getId();
    }

    public ManagerModuleSingle<JammingUnit, JammingCollectionManager, JammingElementManager> getJamming() {
        return this.jamming;
    }

    public JammingElementManager getJammingElementManager() {
        return (JammingElementManager)this.jamming.getElementManager();
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

    public ManagerModuleCollection<PushPulseUnit, PushPulseCollectionManager, PushPulseElementManager> getPushPulse() {
        return this.pushPulse;
    }

    public ManagerModuleCollection<RepairUnit, RepairBeamCollectionManager, RepairElementManager> getRepair() {
        return this.repair;
    }

    public ManagerModuleCollection<SalvageUnit, SalvageBeamCollectionManager, SalvageElementManager> getSalvage() {
        return this.salvage;
    }

    public ShieldRegenCollectionManager getShieldRegenManager() {
        assert this.shields != null;

        assert this.shields.getCollectionManager() != null;

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

    public ManagerModuleSingle<ThrusterUnit, ThrusterCollectionManager, ThrusterElementManager> getThrust() {
        return this.thrust;
    }

    public ThrusterElementManager getThrusterElementManager() {
        return (ThrusterElementManager)this.thrust.getElementManager();
    }

    public ManagerModuleCollection<TurretDockingBlockUnit, TurretDockingBlockCollectionManager, TurretDockingBlockElementManager> getTurretDockingBlock() {
        return this.turretDockingBlock;
    }

    public ManagerModuleCollection<WeaponUnit, WeaponCollectionManager, WeaponElementManager> getWeapon() {
        return this.weapon;
    }

    public void initialize(StateInterface var1) {
        this.jumpAddOn = new JumpAddOn(this);
        this.powerAddOn = new PowerAddOn(this, this.getSegmentController());
        this.shieldAddOn = new ShieldAddOn(this, this.getSegmentController());
        this.repulseManager = new RepulseHandler((Ship)this.getSegmentController());
        this.addConsumer(this.repulseManager);
        this.dockingBeam = new ActivationBeamElementManager(this.getSegmentController());
        this.addUpdatable(this.dockingBeam);
        this.railBeam = new RailBeamElementManager(this.getSegmentController());
        this.beamInteracesSingle.add(this.railBeam.getBeamHandler());
        this.addUpdatable(this.railBeam);
        this.modules.add(this.shields = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), ShieldRegenCollectionManager.class), (short)0, (short)478));
        this.modules.add(this.shieldCapacity = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), ShieldCapacityCollectionManager.class), (short)0, (short)3));
        this.modules.add(this.railPickup = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), RailPickupCollectionManager.class), (short)0, (short)937));
        this.modules.add(this.thrust = new ManagerModuleSingle(new ThrusterElementManager(this.getSegmentController()), (short)1, (short)8));
        this.modules.add(this.missileCapacity = new ManagerModuleSingle(new MissileCapacityElementManager(this.getSegmentController()), (short)1, (short)362));
        this.modules.add(this.door = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), DoorCollectionManager.class), (short)0, (short)122));
        this.modules.add(this.cloaking = new ManagerModuleSingle(new CloakingElementManager(this.getSegmentController()), (short)1, (short)22));
        this.modules.add(this.explosive = new ManagerModuleSingle(new ExplosiveElementManager(this.getSegmentController()), (short)1, (short)14));
        this.modules.add(this.jamming = new ManagerModuleSingle(new JammingElementManager(this.getSegmentController()), (short)1, (short)15));
        this.modules.add(this.power = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerCollectionManager.class), (short)0, (short)2));
        this.modules.add(this.reactor = new ManagerModuleSingle(new MainReactorElementManager(this.getSegmentController(), MainReactorCollectionManager.class), (short)0, (short)1008));
        this.modules.add(this.stabilizer = new ManagerModuleSingle(new StabilizerElementManager(this.getSegmentController(), StabilizerCollectionManager.class), (short)0, (short)1009));
        this.modules.add(this.conduit = new ManagerModuleSingle(new ConduitElementManager(this.getSegmentController(), ConduitCollectionManager.class), (short)0, (short)1010));
        this.modules.add(this.railMassEnhancer = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), RailMassEnhancerCollectionManager.class), (short)0, (short)671));
        this.modules.add(this.powerCapacity = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerCapacityCollectionManager.class), (short)0, (short)331));
        this.modules.add(this.powerBattery = new ManagerModuleSingle(new VoidElementManager(this.getSegmentController(), PowerBatteryCollectionManager.class), (short)0, (short)978));
        this.modules.add(this.salvage = new ManagerModuleCollection(new SalvageElementManager(this.getSegmentController()), (short)4, (short)24));
        this.modules.add(this.activation = new ManagerModuleCollection(new ActivationElementManager(this.getSegmentController()), (short)30000, (short)32767));
        this.modules.add(this.railSpeed = new ManagerModuleCollection(new RailSpeedElementManager(this.getSegmentController()), (short)672, (short)29999));
        this.modules.add(this.activationgate = new ManagerModuleCollection(new ActivationGateElementManager(this.getSegmentController()), (short)685, (short)686));
        this.modules.add(this.transporter = new ManagerModuleCollection(new TransporterElementManager(this.getSegmentController()), (short)687, (short)688));
        this.modules.add(this.turretDockingBlock = new ManagerModuleCollection(new TurretDockingBlockElementManager(this.getSegmentController()), (short)7, (short)88));
        this.modules.add(this.fixedDockingBlock = new ManagerModuleCollection(new FixedDockingBlockElementManager(this.getSegmentController()), (short)289, (short)290));
        this.modules.add(this.repair = new ManagerModuleCollection(new RepairElementManager(this.getSegmentController()), (short)39, (short)30));
        this.modules.add(this.tractorBeam = new ManagerModuleCollection(new TractorElementManager(this.getSegmentController()), (short)360, (short)361));

        //INSERTED CODE @600
        ElementRegisterEvent event = new ElementRegisterEvent(this);
        StarLoader.fireEvent(ElementRegisterEvent.class, event);

        for (UsableControllableElementManager<?, ?, ?> internalManager : event.internalManagers) {
            this.modules.add(new ManagerModuleCollection(internalManager, internalManager.controllerId, internalManager.controllingId));
        }
        ///

        this.modules.add(this.weapon = new ManagerModuleCollection(new WeaponElementManager(this.getSegmentController()), (short)6, (short)16));
        this.modules.add(this.jumpDrive = new ManagerModuleCollection(new JumpDriveElementManager(this.getSegmentController()), (short)544, (short)545));
        this.modules.add(this.jumpProhibiter = new ManagerModuleCollection(new JumpInhibitorElementManager(this.getSegmentController()), (short)681, (short)682));
        this.modules.add(this.scanner = new ManagerModuleCollection(new ScannerElementManager(this.getSegmentController()), (short)654, (short)655));
        this.modules.add(this.cargo = new ManagerModuleCollection(new CargoElementManager(this.getSegmentController()), (short)120, (short)689));
        this.modules.add(this.pushPulse = new ManagerModuleCollection(new PushPulseElementManager(this.getSegmentController()), (short)344, (short)345));
        this.modules.add(this.damageBeam = new ManagerModuleCollection(new DamageBeamElementManager(this.getSegmentController()), (short)414, (short)415));
        this.modules.add(this.trigger = new ManagerModuleCollection(new TriggerElementManager(this.getSegmentController()), (short)413, (short)411));
        this.modules.add(this.sensor = new ManagerModuleCollection(new SensorElementManager(this.getSegmentController()), (short)980, (short)405));
        this.modules.add(this.dumbMissile = new ManagerModuleCollection(new DumbMissileElementManager(this.getSegmentController()), (short)38, (short)32));
        this.modules.add(this.heatEffect = new ManagerModuleCollection(new HeatEffectElementManager(this.getSegmentController()), (short)351, (short)352));
        this.modules.add(this.kineticEffect = new ManagerModuleCollection(new KineticEffectElementManager(this.getSegmentController()), (short)353, (short)354));
        this.modules.add(this.emEffect = new ManagerModuleCollection(new EmEffectElementManager(this.getSegmentController()), (short)349, (short)350));
        this.modules.add(this.railSys = new ManagerModuleCollection(new RailConnectionElementManager(this.getSegmentController()), (short)29998, (short)32767));
        this.modules.add(this.mineLayer = new ManagerModuleCollection(new MineLayerElementManager(this.getSegmentController()), (short)41, (short)37));
        this.chambers = new ObjectArrayList();
        ElementKeyMap.typeList();

        for(int var3 = 0; var3 < ElementKeyMap.chamberAnyTypes.size(); ++var3) {
            short var2;
            ElementKeyMap.getInfoFast(var2 = ElementKeyMap.chamberAnyTypes.getShort(var3));
            ManagerModuleSingle var4 = new ManagerModuleSingle(new ReactorChamberElementManager(var2, this.getSegmentController(), ReactorChamberCollectionManager.class), (short)0, var2);
            this.chambers.add(var4);
            this.modules.add(var4);
        }

        if (((GameStateInterface)this.getState()).getGameState() != null && ((GameStateInterface)this.getState()).getGameState().isAllowFactoryOnShips()) {
            this.factory = new FactoryAddOn();
            this.factory.initialize(this.getModules(), this.getSegmentController());
        }

    }

    public void onAction() {
        if (((Ship)this.getSegmentController()).railController.getRoot() != this.getSegmentController() && ((Ship)this.getSegmentController()).railController.getRoot() instanceof ManagedSegmentController) {
            ((ManagedSegmentController)((Ship)this.getSegmentController()).railController.getRoot()).getManagerContainer().onAction();
        }

        if (CloakingElementManager.REUSE_DELAY_ON_ACTION_MS >= 0) {
            ((CloakingElementManager)this.cloaking.getElementManager()).stopCloak(CloakingElementManager.REUSE_DELAY_ON_ACTION_MS);
        }

        if (JammingElementManager.REUSE_DELAY_ON_ACTION_MS >= 0) {
            ((JammingElementManager)this.jamming.getElementManager()).stopJamming(JammingElementManager.REUSE_DELAY_ON_ACTION_MS);
        }

        this.onRevealingAction();
    }

    public void onAddedElementSynched(short var1, Segment var2, long var3, long var5, boolean var7) {
        super.onAddedElementSynched(var1, var2, var3, var5, var7);
        switch(var1) {
            case 47:
                this.cockpitManager.addCockpit(var3);
                return;
            case 121:
                ((Ship)this.getSegmentController()).getAiConfiguration().setControllerBlock(new SegmentPiece(var2, (byte)ByteUtil.modUSeg(ElementCollection.getPosX(var3)), (byte)ByteUtil.modUSeg(ElementCollection.getPosY(var3)), (byte)ByteUtil.modUSeg(ElementCollection.getPosZ(var3))));
            default:
                return;
            case 663:
                this.railBeam.addRailDocker(ElementCollection.getIndex4(var3, var1));
                return;
            case 1126:
                int var9 = ByteUtil.modUSeg(ElementCollection.getPosX(var3));
                int var8 = ByteUtil.modUSeg(ElementCollection.getPosY(var3));
                int var6 = ByteUtil.modUSeg(ElementCollection.getPosZ(var3));
                byte var10 = var2.getSegmentData().getOrientation((byte)var9, (byte)var8, (byte)var6);
                this.repulseManager.add(var3, var10);
        }
    }

    public void onRemovedElementSynched(short var1, int var2, byte var3, byte var4, byte var5, Segment var6, boolean var7) {
        super.onRemovedElementSynched(var1, var2, var3, var4, var5, var6, var7);
        switch(var1) {
            case 47:
                this.cockpitManager.removeCockpit(var6.getAbsoluteIndex(var3, var4, var5));
                return;
            case 121:
                ((Ship)this.getSegmentController()).getAiConfiguration().setControllerBlock((SegmentPiece)null);
            default:
                return;
            case 663:
                this.railBeam.removeRailDockers(ElementCollection.getIndex4(var6.getAbsoluteIndex(var3, var4, var5), var1));
                return;
            case 1126:
                this.repulseManager.remove(var6.getAbsoluteIndex(var3, var4, var5));
        }
    }

    public void onHitNotice() {
        if (((Ship)this.getSegmentController()).isOnServer()) {
            if ((this.isCloaked() || this.isJamming()) && ((Ship)this.getSegmentController()).getNetworkObject().onHitNotices.isEmpty()) {
                ((CloakingElementManager)this.cloaking.getElementManager()).onHit();
                ((JammingElementManager)this.jamming.getElementManager()).onHit();
                ((Ship)this.getSegmentController()).getNetworkObject().onHitNotices.add(new RemoteBoolean(true, ((Ship)this.getSegmentController()).getNetworkObject()));
                return;
            }
        } else {
            ((CloakingElementManager)this.cloaking.getElementManager()).onHit();
            ((JammingElementManager)this.jamming.getElementManager()).onHit();
        }

    }

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this.getSegmentController(), "ShipManagerContainerUpdate");
        this.powerAddOn.update(var1);
        super.updateLocal(var1);
        this.thrustConfiguration.updateLocal(var1);
        if (this.factory != null) {
            this.factory.update(var1, this.isOnServer());
        } else if (((GameStateInterface)this.getState()).getGameState().isAllowFactoryOnShips()) {
            this.factory = new FactoryAddOn();
            this.factory.initialize(this.getModules(), this.getSegmentController());
            Iterator var2 = this.factory.map.values().iterator();

            while(var2.hasNext()) {
                ManagerModuleCollection var3 = (ManagerModuleCollection)var2.next();
                this.getModulesMap().put(var3.getElementID(), var3);
            }
        }

        if (this.flagSendMissileCapacity) {
            ((MissileCapacityElementManager)this.missileCapacity.getElementManager()).sendMissileCapacity();
            this.flagSendMissileCapacity = false;
        }

        this.repulseManager.handle(var1);
        this.getState().getDebugTimer().end(this.getSegmentController(), "ShipManagerContainerUpdate");
    }

    public boolean isTargetLocking(SegmentPiece var1) {
        return ((DumbMissileElementManager)this.dumbMissile.getElementManager()).isTargetLocking(var1);
    }

    public ShipManagerModuleStatistics getStatisticsManager() {
        return this.statisticsManager;
    }

    public boolean isCloaked() {
        return this.getCloakElementManager().isCloaked();
    }

    public boolean isJamming() {
        return this.getJammingElementManager().isJamming();
    }

    public boolean hasRepulsors() {
        return this.getRepulseManager().hasRepulsors();
    }

    public ManagerModuleCollection<AbstractUnit, ActivationCollectionManager, ActivationElementManager> getActivation() {
        return this.activation;
    }

    public void setActivation(ManagerModuleCollection<AbstractUnit, ActivationCollectionManager, ActivationElementManager> var1) {
        this.activation = var1;
    }

    public ManagerModuleCollection<TriggerUnit, TriggerCollectionManager, TriggerElementManager> getTrigger() {
        return this.trigger;
    }

    public EffectElementManager<?, ?, ?> getEffect(short var1) {
        if (var1 == 0) {
            return null;
        } else if (!this.effectMap.containsKey(var1)) {
            throw new RuntimeException("CRITICAL: invalid weapon effect referenced " + ElementKeyMap.toString(var1) + ": " + this.effectMap + "\non entity " + this.getSegmentController() + " (" + this.getState() + ")");
        } else {
            return (EffectElementManager)this.effectMap.get(var1).getElementManager();
        }
    }

    public ManagerModuleCollection<JumpDriveUnit, JumpDriveCollectionManager, JumpDriveElementManager> getJumpDrive() {
        return this.jumpDrive;
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

    public ManagerModuleCollection<JumpInhibitorUnit, JumpInhibitorCollectionManager, JumpInhibitorElementManager> getJumpProhibiter() {
        return this.jumpProhibiter;
    }

    public ManagerModuleCollection<TransporterUnit, TransporterCollectionManager, TransporterElementManager> getTransporter() {
        return this.transporter;
    }

    public ManagerModuleCollection<CargoUnit, CargoCollectionManager, CargoElementManager> getCargo() {
        return this.cargo;
    }

    public ManagerModuleSingle<RailPickupUnit, RailPickupCollectionManager, VoidElementManager<RailPickupUnit, RailPickupCollectionManager>> getRailPickup() {
        return this.railPickup;
    }

    public ManagerModuleCollection<SensorUnit, SensorCollectionManager, SensorElementManager> getSensor() {
        return this.sensor;
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

    public JumpAddOn getJumpAddOn() {
        return this.jumpAddOn;
    }

    public RepulseHandler getRepulseManager() {
        return this.repulseManager;
    }

    public void initFromNetworkObject(NetworkObject var1) {
        super.initFromNetworkObject(var1);
        NetworkShip var2 = (NetworkShip)var1;
        this.thrustConfiguration.initFromNetworkObject(var2);
        this.cockpitManager.initFromNetworkObject(var2);
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        super.updateFromNetworkObject(var1, var2);
        this.thrustConfiguration.updateFromNetworkObject((NetworkShip)var1, var2);
        this.cockpitManager.updateFromNetworkObject();
    }

    public void updateToFullNetworkObject(NetworkObject var1) {
        super.updateToFullNetworkObject(var1);
        this.thrustConfiguration.updateToFullNetworkObject((NetworkShip)var1);
        this.cockpitManager.updateToFullNetworkObject((NetworkShip)var1);
    }

    public void updateToNetworkObject(NetworkObject var1) {
        super.updateToNetworkObject(var1);
        this.thrustConfiguration.updateToNetworkObject((NetworkShip)var1);
    }

    protected void afterInitialize() {
        if (specialBlocksStatic == null || specialBlocksStatic.length != ElementKeyMap.highestType) {
            specialBlocksStatic = new boolean[ElementKeyMap.highestType + 1];
            this.getSpecialMap(specialBlocksStatic);
        }

        this.specialBlocks = specialBlocksStatic;
    }

    public ManagerModuleCollection<MineLayerUnit, MineLayerCollectionManager, MineLayerElementManager> getMineLayer() {
        return this.mineLayer;
    }

    public ManagerModuleCollection<TractorUnit, TractorBeamCollectionManager, TractorElementManager> getTractorBeam() {
        return this.tractorBeam;
    }

    public void setTractorBeam(ManagerModuleCollection<TractorUnit, TractorBeamCollectionManager, TractorElementManager> var1) {
        this.tractorBeam = var1;
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

    public CockpitManager getCockpitManager() {
        return this.cockpitManager;
    }

    public ManagerModuleCollection<DamageBeamUnit, DamageBeamCollectionManager, DamageBeamElementManager> getBeam() {
        return this.damageBeam;
    }
}
