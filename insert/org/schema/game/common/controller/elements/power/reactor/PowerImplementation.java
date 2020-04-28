//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.power.reactor;

import api.listener.events.calculate.CurrentPowerCalculateEvent;
import api.listener.events.calculate.MaxPowerCalculateEvent;
import api.mod.StarLoader;
import api.server.Server;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.Long2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import javax.vecmath.Matrix3f;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PowerChangeListener.PowerChangeType;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ManagerModuleSingle;
import org.schema.game.common.controller.elements.ReactorLevelCalcStyle;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ConduitCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberElementManager;
import org.schema.game.common.controller.elements.power.reactor.chamber.ReactorChamberUnit;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorBonusMatrixUpdate;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorElement;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorSet;
import org.schema.game.common.controller.elements.power.reactor.tree.ReactorTree;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.blockeffects.config.ConfigPool;
import org.schema.game.common.data.blockeffects.config.ConfigProviderSource;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.network.objects.PowerInterfaceNetworkObject;
import org.schema.game.network.objects.remote.RemoteReactorBonusMatrix;
import org.schema.game.network.objects.remote.RemoteReactorSet;
import org.schema.game.network.objects.remote.RemoteSegmentPiece;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.container.UpdateWithoutPhysicsObjectInterface;
import org.schema.schine.network.objects.remote.LongIntPair;
import org.schema.schine.network.objects.remote.RemoteLongIntPair;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class PowerImplementation extends Observable implements PowerInterface, UpdateWithoutPhysicsObjectInterface {
    private static final byte TAG_VERSION = 1;
    private double power;
    private final ReactorSet reactorSet;
    private MainReactorUnit biggestReactor;
    public final ManagerContainer<? extends SegmentController> container;
    private final List<PowerConsumer> powerConsumerList = new ObjectArrayList();
    private final Set<PowerConsumer> powerConsumers = new ObjectOpenHashSet();
    private boolean consumersChanged;
    private double currentConsumption;
    private double currentLocalConsumption;
    private double currentPowerGain;
    private double currentConsumptionPerSec;
    private final LongArrayFIFOQueue chamberConvertRequests = new LongArrayFIFOQueue();
    private final LongArrayFIFOQueue treeBootRequests = new LongArrayFIFOQueue();
    private long selectedReactor = -9223372036854775808L;
    private float reactorSwitchCooldown;
    private ObjectArrayFIFOQueue<ReactorSet> receivedReactorSets = new ObjectArrayFIFOQueue();
    private boolean requestedRecalibrate;
    private final Long2IntOpenHashMap changedModuleSet = new Long2IntOpenHashMap();
    private long lastChangeSend;
    private final ReactorPriorityQueue priorityQueue;
    private final ObjectArrayFIFOQueue<ReactorTree> receivedTrees = new ObjectArrayFIFOQueue();
    private int waitingForPool;
    private boolean usingReactorsFromTag;
    private double maxPower;
    private boolean hadReactor;
    private float reactorBoost;
    private final Long2ObjectOpenHashMap<ConfigProviderSource> configProjectionSources = new Long2ObjectOpenHashMap();
    private double currentMaxPower;
    private final List<StabilizerPath> stabilizerPaths = new ObjectArrayList();
    private boolean flagStabPathCalc;
    private float reactorRebootCooldown;
    private long firstTreeCreate = -1L;
    private float accumulated;
    private float currentEnergyStreamCooldown;
    private float lastEnergyStreamCooldown;
    private double currentLocalConsumptionPerSec;
    private long lastStabReactor = -9223372036854775808L;
    private final ObjectArrayFIFOQueue<ReactorBonusMatrixUpdate> receivedBonusMatrixUpdates = new ObjectArrayFIFOQueue();
    private double injectedPowerPerSec;
    private float injectedPowerTimoutSec;
    private long selectedReactorToSet = -9223372036854775808L;
    private boolean energyStream;
    static int blocksKilled;
    static int blocksNotKilled;

    public PowerImplementation(ManagerContainer<? extends SegmentController> var1) {
        this.container = var1;
        this.reactorSet = new ReactorSet(this);
        this.priorityQueue = new ReactorPriorityQueue(this);
        var1.addUpdatable(this);
        var1.addEffectSource(this);
    }

    public double getPower() {
        return this.power;
    }

    public double getMaxPower() {
        //MODIFIED CODE
        return this.maxPower;
        //return Math.max((double)VoidElementManager.REACTOR_POWER_CAPACITY_MULTIPLIER, this.maxPower);
    }

    public void flagStabilizersDirty() {
        this.getStabilizer().flagDirty();
    }

    public MainReactorCollectionManager getMainReactor() {
        return this.container.getMainReactor();
    }

    public StabilizerCollectionManager getStabilizer() {
        return this.container.getStabilizer();
    }

    public List<MainReactorUnit> getMainReactors() {
        return this.getMainReactor().getElementCollections();
    }

    public Set<ReactorChamberUnit> getConnectedChambersToConduit(long var1) {
        return this.getConduits().getConnected(var1);
    }

    public List<ManagerModuleSingle<ReactorChamberUnit, ReactorChamberCollectionManager, ReactorChamberElementManager>> getChambers() {
        return this.container.getChambers();
    }

    public ReactorChamberUnit getReactorChamber(long var1) {
        Iterator var3 = this.getChambers().iterator();

        while(var3.hasNext()) {
            Iterator var4 = ((ReactorChamberCollectionManager)((ManagerModuleSingle)var3.next()).getCollectionManager()).getElementCollections().iterator();

            while(var4.hasNext()) {
                ReactorChamberUnit var5;
                if ((var5 = (ReactorChamberUnit)var4.next()).getNeighboringCollection().contains(var1)) {
                    return var5;
                }
            }
        }

        return null;
    }

    public MainReactorUnit getReactor(long var1) {
        Iterator var3 = this.getMainReactors().iterator();

        MainReactorUnit var4;
        do {
            if (!var3.hasNext()) {
                return null;
            }
        } while(!(var4 = (MainReactorUnit)var3.next()).getNeighboringCollection().contains(var1));

        return var4;
    }

    public ConduitCollectionManager getConduits() {
        return this.container.getConduit();
    }

    public void createPowerTree() {
        if (this.isOnServer()) {
            this.reactorSet.build();
            boolean var1 = false;
            Iterator var2 = this.reactorSet.getTrees().iterator();

            while(var2.hasNext()) {
                if (((ReactorTree)var2.next()).getId() == this.selectedReactor) {
                    var1 = true;
                    break;
                }
            }

            if (!var1 && this.getSegmentController().isFullyLoaded()) {
                if (this.biggestReactor != null) {
                    var2 = this.reactorSet.getTrees().iterator();

                    while(var2.hasNext()) {
                        if (((ReactorTree)var2.next()).getId() == this.biggestReactor.idPos) {
                            this.selectedReactor = this.biggestReactor.idPos;
                            break;
                        }
                    }
                } else if (this.reactorSet.size() > 0) {
                    this.selectedReactor = ((ReactorTree)this.reactorSet.getTrees().get(0)).getId();
                } else {
                    this.selectedReactor = -9223372036854775808L;
                }
            }

            this.getNetworkObject().getReactorSetBuffer().add(new RemoteReactorSet(this.reactorSet, this.isOnServer()));
            if (this.getSegmentController().isFullyLoaded()) {
                if (this.firstTreeCreate > 0L && System.currentTimeMillis() - this.firstTreeCreate > 10000L) {
                    this.dischargeAllPowerConsumers();
                    this.reactorSet.dischargeAll = true;
                }

                if (this.firstTreeCreate <= 0L) {
                    this.firstTreeCreate = System.currentTimeMillis();
                }
            }

            this.setChanged();
            this.notifyObservers(this.reactorSet);
        }
    }

    public void dischargeAllPowerConsumers() {
        Iterator var1 = this.powerConsumerList.iterator();

        while(var1.hasNext()) {
            ((PowerConsumer)var1.next()).dischargeFully();
        }

    }

    public PowerInterfaceNetworkObject getNetworkObject() {
        return (PowerInterfaceNetworkObject)((SendableSegmentController)this.getSegmentController()).getNetworkObject();
    }

    public ReactorSet getReactorSet() {
        return this.reactorSet;
    }

    public void calcBiggestAndActiveReactor() {
        this.maxPower = 0.0D;
        this.biggestReactor = null;
        int var1 = 0;
        Iterator var2 = this.getMainReactor().getElementCollections().iterator();

        while(var2.hasNext()) {
            MainReactorUnit var3;
            if ((var3 = (MainReactorUnit)var2.next()).size() > var1) {
                var1 = var3.size();
                this.biggestReactor = var3;
            }
        }

    }

    public static double getStabilization(double var0, boolean var2) {
        return ((double)(var2 ? VoidElementManager.REACTOR_STABILIZER_FREE_MAIN_REACTOR_BLOCKS : 0) + var0) * (double)VoidElementManager.REACTOR_STABILIZATION_MULTIPLIER;
    }

    public double getStabilization() {
        return getStabilization(this.getStabilizer().getStabilization(), true);
    }

    public double getBiggestReactorSize() {
        return this.biggestReactor == null ? 0.0D : (double)this.biggestReactor.size() * (double)VoidElementManager.REACTOR_MAIN_COUNT_MULTIPLIER;
    }

    public double getActiveReactorInitialSize() {
        ReactorTree var1;
        return (var1 = (ReactorTree)this.getReactorSet().getTreeMap().get(this.selectedReactor)) == null ? 0.0D : (double)var1.getSize() * (double)VoidElementManager.REACTOR_MAIN_COUNT_MULTIPLIER;
    }

    public double getActiveReactorCurrentSize() {
        ReactorTree var1;
        return (var1 = (ReactorTree)this.getReactorSet().getTreeMap().get(this.selectedReactor)) == null ? 0.0D : (double)var1.getActualSize() * (double)VoidElementManager.REACTOR_MAIN_COUNT_MULTIPLIER;
    }

    public void onFinishedStabilizerChange() {
        if (this.getActiveReactor() != null) {
            this.getStabilizerCollectionManager().calculateStabilization(this.getActiveReactor().getId(), this.getActiveReactor().getCenterOfMass());
            this.lastStabReactor = this.getActiveReactor().getId();
        } else {
            this.lastStabReactor = -9223372036854775808L;
        }

        this.maxPower = this.calculatInitialMaxPower();
        this.currentMaxPower = this.calculatCurrentMaxPower();

        this.flagStabilizerPathCalc();
        if (this.isOnServer()) {
            this.getSegmentController().getRuleEntityManager().triggerOnReactorActivityChange();
        }

    }

    public StabilizerCollectionManager getStabilizerCollectionManager() {
        return this.container.getStabilizer();
    }

    //REPLACE METHOD
    private double calculatInitialMaxPower() {
        double stabilization = this.getStabilizationPowerEfficiency();
        double v = Math.min(this.getActiveReactorInitialSize(), stabilization) * (double) VoidElementManager.REACTOR_POWER_CAPACITY_MULTIPLIER;
        MaxPowerCalculateEvent event = new MaxPowerCalculateEvent(this, v);
        StarLoader.fireEvent(MaxPowerCalculateEvent.class, event);
        v = event.getPower();
        return v;
    }
    //
    //REPLACE METHOD
    private double calculatCurrentMaxPower() {
        double stabilization = this.getStabilizationPowerEfficiency();
        double v = Math.min(this.getActiveReactorCurrentSize(), stabilization) * (double) VoidElementManager.REACTOR_POWER_CAPACITY_MULTIPLIER;
        CurrentPowerCalculateEvent event = new CurrentPowerCalculateEvent(this, v);
        StarLoader.fireEvent(CurrentPowerCalculateEvent.class, event);
        v = event.getPower();
        return v;
    }
    //

    public double getStabilizationPowerEfficiency() {
        return this.getStabilization() * (1.0D / (double)VoidElementManager.REACTOR_STABILIZATION_POWER_EFFECTIVE_FULL);
    }

    public static int getMaxStabilizerCount() {
        return VoidElementManager.REACTOR_STABILIZER_GROUPS_MAX < 0 ? 2147483647 : VoidElementManager.REACTOR_STABILIZER_GROUPS_MAX;
    }

    public double getReactorOptimalDistance() {
        int var1 = Math.max(1, (int)this.getBiggestReactorSize() - VoidElementManager.REACTOR_STABILIZER_FREE_MAIN_REACTOR_BLOCKS);
        double var2;
        switch(VoidElementManager.REACTOR_CALC_STYLE) {
            case LINEAR:
                var2 = Math.max(0.0D, (double)VoidElementManager.REACTOR_STABILIZER_STARTING_DISTANCE + (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_PER_MAIN_REACTOR_BLOCK * (double)var1);
                break;
            case EXP:
                var2 = Math.max(0.0D, (double)VoidElementManager.REACTOR_STABILIZER_STARTING_DISTANCE + Math.pow((double)var1, (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP) * (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP_MULT);
                if ((float)var1 >= VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP_SOFTCAP_BLOCKS_START) {
                    var2 *= 1.0D + Math.pow(Math.max((double)((float)var1 / VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP_SOFTCAP_BLOCKS_START - 1.0F), 0.0D), (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP_SOFTCAP_EXP) * (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_EXP_SOFTCAP_MULT;
                }
                break;
            case LOG_LEVELED:
                var2 = getLogLeveled(var1);
                break;
            case LOG:
                var2 = Math.max(0.0D, (double)VoidElementManager.REACTOR_STABILIZER_STARTING_DISTANCE + Math.max(0.0D, Math.max(0.0D, Math.log10((double)var1)) + (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_OFFSET) * (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_FACTOR);
                break;
            default:
                throw new RuntimeException("Illegal calc style " + VoidElementManager.REACTOR_CALC_STYLE);
        }

        assert !Double.isNaN(var2);

        var2 *= (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_TOTAL_MULT;
        var2 = this.getSegmentController().getConfigManager().apply(StatusEffectType.POWER_STABILIZER_DISTANCE, var2);

        assert !Double.isNaN(var2);

        return var2;
    }

    private static double getLogLeveled(int var0) {
        int var3 = getReactorLevel(var0);
        double var1 = Math.max(0.0D, (double)VoidElementManager.REACTOR_STABILIZER_STARTING_DISTANCE + Math.pow((double)var3, (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_LEVELED_EXP) * (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_LEVELED_MULTIPLIER);
        if (!VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_LEVELED_STEPS) {
            double var4 = Math.max(0.0D, (double)VoidElementManager.REACTOR_STABILIZER_STARTING_DISTANCE + Math.pow((double)(var3 + 1), (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_LEVELED_EXP) * (double)VoidElementManager.REACTOR_STABILIZER_DISTANCE_LOG_LEVELED_MULTIPLIER);
            int var6 = getMinNeededFromReactorLevel(var3, VoidElementManager.REACTOR_CHAMBER_BLOCKS_PER_MAIN_REACTOR_AND_LEVEL);
            var3 = getMinNeededFromReactorLevel(var3 + 1, VoidElementManager.REACTOR_CHAMBER_BLOCKS_PER_MAIN_REACTOR_AND_LEVEL);
            var0 -= var6;
            var3 -= var6;
            float var9 = (float)var0 / (float)var3;
            double var7 = (var4 - var1) * (double)var9;
            var1 += var7;
        }

        return var1;
    }

    public static void main(String[] var0) {
        for(int var1 = 0; var1 < 250000; var1 += 1000) {
            System.err.println(printReactorLevel(var1) + " -> DISTANCE " + getLogLeveled(var1));
        }

    }

    public double calcStabilization(double var1, float var3) {
        assert !Double.isNaN(var1);

        assert !Double.isNaN((double)var3);

        return calcStabilizationStatic(var1, var3);
    }

    public static double calcStabilizationStatic(double var0, float var2) {
        if (var0 <= 0.0D) {
            return 1.0D;
        } else {
            double var3;
            if ((var3 = (double)var2 / var0) >= (double)VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ONE) {
                return 1.0D;
            } else if (var3 <= (double)VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ZERO) {
                return 0.0D;
            } else {
                double var5 = (double)(VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ONE - VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ZERO);
                double var7;
                double var9 = (var7 = var3 - (double)VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ZERO) / var5;

                assert !Double.isNaN(var9) : var5 + " / " + var7 + " = NaN; optDist: " + var0 + "; ractorDist: " + var2 + "; perc: " + var3;

                return var9;
            }
        }
    }

    public static double calcStabilizationDistanceForStabilizationPercent(double var0, float var2) {
        double var3 = var0 * (double)VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ZERO;
        double var5 = var0 * (double)VoidElementManager.REACTOR_STABILIZER_LINEAR_FALLOFF_ONE - var3;
        return var3 + (double)var2 * var5;
    }

    public double getRechargeRatePercentPerSec() {
        float var10000 = VoidElementManager.REACTOR_RECHARGE_PERCENT_PER_SECOND;
        float var1 = var10000 + var10000 * this.getReactorBoost();
        return (double)this.getSegmentController().getConfigManager().apply(StatusEffectType.POWER_RECHARGE_EFFICIENCY, var1);
    }

    public double getRechargeRatePowerPerSec() {
        double var1 = this.getRechargeRatePercentPerSec();
        if (this.power <= 1.0E-8D) {
            var1 *= (double)VoidElementManager.REACTOR_RECHARGE_EMPTY_MULTIPLIER;
        }

        if (this.isStabilizerPathHit()) {
            var1 *= (double)VoidElementManager.REACTOR_STABILIZATION_ENERGY_STREAM_HIT_COOLDOWN_REACTOR_EFFICIENCY;
        }

        return var1 * this.currentMaxPower;
    }

    public void sendBonusMatrixUpdate(ReactorTree var1, Matrix3f var2) {
        ReactorBonusMatrixUpdate var3;
        (var3 = new ReactorBonusMatrixUpdate()).id = var1.getId();
        var3.bonusMatrix = new Matrix3f(var2);
        this.getNetworkObject().getReactorBonusMatrixUpdateBuffer().add(new RemoteReactorBonusMatrix(var3, this.isOnServer()));
    }

    public void update(Timer var1) {
        if (!this.isOnServer()) {
            this.selectedReactor = this.selectedReactorToSet;
        }

        if (this.getConfigPool() == null) {
            ++this.waitingForPool;
            if (this.waitingForPool % 1000 == 0) {
                System.err.println("[CONFIGPOOL] " + this.getState() + " WAITING FOR CONFIG POOL (" + this.waitingForPool + ")");
            }

        } else {
            if (this.flagStabPathCalc) {
                this.calculateStabilizerPaths();
                this.flagStabPathCalc = false;
            }

            this.getSegmentController().getPhysicsDataContainer().onPhysicsObjectUpdateEnergyBeamInterface = this;
            Iterator var2 = this.stabilizerPaths.iterator();

            while(var2.hasNext()) {
                ((StabilizerPath)var2.next()).update(var1);
            }

            while(!this.receivedBonusMatrixUpdates.isEmpty()) {
                ReactorBonusMatrixUpdate var12 = (ReactorBonusMatrixUpdate)this.receivedBonusMatrixUpdates.dequeue();
                ReactorTree var3;
                if ((var3 = (ReactorTree)this.getReactorSet().getTreeMap().get(var12.id)) != null) {
                    var3.getBonusMatrix().set(var12.bonusMatrix);
                    if (this.isOnServer()) {
                        this.sendBonusMatrixUpdate(var3, var12.bonusMatrix);
                    }

                    this.flagStabilizersDirty();
                }
            }

            while(!this.receivedTrees.isEmpty()) {
                ((ReactorTree)this.receivedTrees.dequeue()).onConfigPoolReceived();
            }

            if (this.getActiveReactor() != null && this.lastStabReactor != this.getActiveReactor().getId()) {
                this.getStabilizerCollectionManager().calculateStabilization(this.getActiveReactor().getId(), this.getActiveReactor().getCenterOfMass());
                this.lastStabReactor = this.getActiveReactor().getId();
            }

            this.maxPower = this.calculatInitialMaxPower();
            this.currentMaxPower = this.calculatCurrentMaxPower();

            this.priorityQueue.updateLocal(var1, this);
            this.reactorSwitchCooldown = Math.max(0.0F, this.reactorSwitchCooldown - var1.getDelta());
            this.reactorRebootCooldown = Math.max(0.0F, this.reactorRebootCooldown - var1.getDelta());
            if (this.requestedRecalibrate) {
                this.createPowerTree();
                this.requestedRecalibrate = false;
                if (this.getSegmentController().isOnServer()) {
                    this.getSegmentController().getRuleEntityManager().triggerOnReactorActivityChange();
                }
            }

            if (this.isOnServer() && (this.currentEnergyStreamCooldown > this.lastEnergyStreamCooldown || this.lastEnergyStreamCooldown > 0.0F && this.currentEnergyStreamCooldown <= 0.0F)) {
                this.getNetworkObject().getEnergyStreamCooldownBuffer().add(this.currentEnergyStreamCooldown);
            }

            this.lastEnergyStreamCooldown = this.currentEnergyStreamCooldown;
            if (this.injectedPowerTimoutSec > 0.0F) {
                this.injectedPowerTimoutSec = Math.max(0.0F, this.injectedPowerTimoutSec - var1.getDelta());
            } else {
                this.injectedPowerPerSec = 0.0D;
            }

            if (!this.isDocked()) {
                this.currentPowerGain = (double)var1.getDelta() * (this.getRechargeRatePowerPerSec() + this.injectedPowerPerSec);
                if (this.isStabilizerPathHit()) {
                    this.currentEnergyStreamCooldown = Math.max(0.0F, this.currentEnergyStreamCooldown - var1.getDelta());
                    this.getSegmentController().popupOwnClientMessage("LPESHHIT", StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_POWER_REACTOR_POWERIMPLEMENTATION_0, new Object[]{StringTools.formatPointZero(VoidElementManager.REACTOR_STABILIZATION_ENERGY_STREAM_HIT_COOLDOWN_REACTOR_EFFICIENCY * 100.0F), StringTools.formatPointZero(this.currentEnergyStreamCooldown)}), 3);
                }

                this.consumePower(var1, this);
                this.power = Math.min(this.getMaxPower(), this.power + this.currentPowerGain);
            } else {
                this.currentPowerGain = 0.0D;
            }

            if (this.getSegmentController().getElementClassCountMap().get((short)1008) > 0) {
                this.hadReactor = true;
            } else if (this.hadReactor) {
                if (this.isOnServer() && !this.isAnyDamaged()) {
                    this.requestRecalibrate();
                }

                this.hadReactor = false;
            }

            long var4;
            Entry var14;
            int var15;
            if (this.isOnServer()) {
                if (!this.changedModuleSet.isEmpty() && var1.currentTime - this.lastChangeSend > 700L) {
                    var2 = this.changedModuleSet.long2IntEntrySet().iterator();

                    while(var2.hasNext()) {
                        var4 = (var14 = (Entry)var2.next()).getLongKey();
                        var15 = var14.getIntValue();
                        LongIntPair var6;
                        (var6 = new LongIntPair()).l = var4;
                        var6.i = var15;
                        this.getNetworkObject().getReactorChangeBuffer().add(new RemoteLongIntPair(var6, this.isOnServer()));
                    }

                    this.changedModuleSet.clear();
                    this.lastChangeSend = var1.currentTime;
                    this.getSegmentController().getRuleEntityManager().triggerOnReactorActivityChange();
                }
            } else if (!this.changedModuleSet.isEmpty()) {
                var2 = this.changedModuleSet.long2IntEntrySet().iterator();

                while(var2.hasNext()) {
                    var4 = (var14 = (Entry)var2.next()).getLongKey();
                    var15 = var14.getIntValue();
                    if (this.reactorSet.applyReceivedSizeChange(var4, var15)) {
                        this.setReactorRebootCooldown();
                    }
                }

                this.changedModuleSet.clear();
            }

            while(!this.receivedReactorSets.isEmpty()) {
                ReactorSet var13 = (ReactorSet)this.receivedReactorSets.dequeue();
                var15 = this.reactorSet.getTrees().size();
                this.reactorSet.apply(var13);
                if (var15 > 0 && this.reactorSet.getTrees().size() == 0) {
                    this.onLastReactorRemoved();
                }

                this.setChanged();
                this.notifyObservers(this.reactorSet);
                if (var13.dischargeAll) {
                    this.dischargeAllPowerConsumers();
                }
            }

            long var16;
            while(!this.treeBootRequests.isEmpty()) {
                var16 = this.treeBootRequests.dequeueLong();
                Iterator var17 = this.getReactorSet().getTrees().iterator();

                while(var17.hasNext()) {
                    ReactorTree var5;
                    if ((var5 = (ReactorTree)var17.next()).getId() == var16) {
                        this.doBoot(var5);
                        this.dischargeAllPowerConsumers();
                    }
                }

                if (this.isOnServer()) {
                    this.getNetworkObject().getBootRequestBuffer().add(var16);
                }
            }

            while(true) {
                while(!this.chamberConvertRequests.isEmpty()) {
                    var4 = ElementCollection.getPosIndexFrom4(var16 = this.chamberConvertRequests.dequeueLong());
                    short var19 = (short)ElementCollection.getType(var16);
                    System.err.println("REQUEST TO CONVERT RECEIVED: " + var4 + " -> " + ElementKeyMap.toString(var19));
                    ReactorChamberUnit var23 = null;
                    var2 = this.getChambers().iterator();

                    while(var2.hasNext()) {
                        Iterator var8 = ((ReactorChamberCollectionManager)((ReactorChamberElementManager)((ManagerModuleSingle)var2.next()).getElementManager()).getCollection()).getElementCollections().iterator();

                        while(var8.hasNext()) {
                            ReactorChamberUnit var9;
                            if ((var9 = (ReactorChamberUnit)var8.next()).idPos == var4) {
                                var23 = var9;
                                break;
                            }
                        }

                        if (var23 != null) {
                            break;
                        }
                    }

                    if (var23 != null) {
                        boolean var18 = false;
                        ObjectOpenHashSet var20 = new ObjectOpenHashSet();
                        Iterator var7 = var23.getNeighboringCollection().iterator();

                        while(var7.hasNext()) {
                            long var24 = (Long)var7.next();
                            SegmentPiece var21;
                            if ((var21 = ((SendableSegmentController)this.getSegmentController()).getSegmentBuffer().getPointUnsave(var24)).getType() != var19) {
                                var18 = true;
                                var21.setType(var19);

                                try {
                                    var21.getSegment().getSegmentData().applySegmentData(var21.x, var21.y, var21.z, var21.getData(), 0, false, var21.getAbsoluteIndex(), false, false, var1.currentTime);
                                    var20.add((RemoteSegment)var21.getSegment());
                                } catch (SegmentDataWriteException var11) {
                                    try {
                                        SegmentDataWriteException.replaceData(var21.getSegment());
                                        var21.getSegment().getSegmentData().applySegmentData(var21.x, var21.y, var21.z, var21.getData(), 0, false, var21.getAbsoluteIndex(), false, false, var1.currentTime);
                                    } catch (SegmentDataWriteException var10) {
                                        throw new RuntimeException(var10);
                                    }
                                }

                                RemoteSegmentPiece var22 = new RemoteSegmentPiece(var21, this.isOnServer());
                                ((SendableSegmentController)this.getSegmentController()).sendBlockMod(var22);
                            }
                        }

                        var7 = var20.iterator();

                        while(var7.hasNext()) {
                            ((RemoteSegment)var7.next()).setLastChanged(var1.currentTime);
                        }

                        if (var18) {
                            var23.clear();
                        }
                    } else {
                        System.err.println("[SERVER][POWER][ERROR] Chamber unit to convert not found! sig " + var4 + "; to type " + var19);
                    }
                }

                this.reactorSet.update(var1, this.selectedReactor);
                return;
            }
        }
    }

    private boolean isDocked() {
        return this.getSegmentController().railController.isDocked();
    }

    public void flagConsumersChanged() {
        this.consumersChanged = true;
    }

    public void consumePower(Timer var1, PowerImplementation var2) {
        this.accumulated += var1.getDelta();
        if (this.accumulated > 0.05F) {
            float var3 = (float)((int)(this.accumulated / 0.05F)) * 0.05F;
            this.consumePowerTick(var3, var1, var2, 1.0F);
            this.accumulated -= var3;
        }

    }

    public void consumePowerTick(float var1, Timer var2, PowerImplementation var3, float var4) {
        if (this.consumersChanged) {
            this.powerConsumerList.clear();
            this.powerConsumerList.addAll(this.powerConsumers);
            Collections.sort(this.powerConsumerList, this.priorityQueue);
            this.consumersChanged = false;
        }

        this.currentConsumptionPerSec = 0.0D;
        this.currentConsumption = 0.0D;
        this.currentLocalConsumption = 0.0D;
        this.currentLocalConsumptionPerSec = 0.0D;
        if (!this.getSegmentController().isUsingOldPower()) {
            int var5 = this.powerConsumerList.size();
            this.priorityQueue.resetStats();

            for(int var6 = 0; var6 < var5; ++var6) {
                PowerConsumer var7 = (PowerConsumer)this.powerConsumerList.get(var6);
                this.consumePowerFromConsumer(var2, var1, var7, var3, var4);
            }

        }
    }

    private void consumePowerFromConsumer(Timer var1, float var2, PowerConsumer var3, PowerImplementation var4, float var5) {
        if (var3.isPowerConsumerActive()) {
            this.priorityQueue.addAmount(var3.getPowerConsumerCategory());
            if (var3 instanceof RailPowerConsumer) {
                Iterator var6 = this.getSegmentController().railController.next.iterator();

                while(var6.hasNext()) {
                    SegmentController var8;
                    if ((var8 = ((RailRelation)var6.next()).docked.getSegmentController()) instanceof ManagedSegmentController) {
                        ManagerContainer var9 = ((ManagedSegmentController)var8).getManagerContainer();
                        if (!var8.railController.isPowered()) {
                            var5 = 0.0F;
                        }

                        var9.getPowerInterface().consumePowerTick(var2, var1, var4, var5);
                        this.priorityQueue.addConsumption(var3.getPowerConsumerCategory(), var9.getPowerInterface().getCurrentConsumptionPerSec());
                        var3.setPowered(var9.getPowerInterface().getPowerConsumerPriorityQueue().getTotalPercent());
                    }
                }
            } else {
                boolean var12 = var3.isPowerCharging(var1.currentTime);
                double var17 = var3.getPowerConsumedPerSecondCharging();
                var17 = this.getSegmentController().getConfigManager().apply(StatusEffectType.POWER_MODULE_CHARGING_RATE_MOD, var17);
                double var19 = var3.getPowerConsumedPerSecondResting();
                var19 = this.getSegmentController().getConfigManager().apply(StatusEffectType.POWER_TOP_OFF_RATE_MOD, var19);
                double var10;
                if (var12) {
                    var10 = var17;
                } else {
                    var10 = var19;
                }

                this.priorityQueue.addConsumption(var3.getPowerConsumerCategory(), var10);
                var4.currentConsumptionPerSec += var10;
                if (var4 != this) {
                    this.currentConsumptionPerSec += var10;
                }

                this.currentLocalConsumptionPerSec += var10;
                double var13 = var10 * (double)var2;
                var4.currentConsumption += var13;
                if (var4 != this) {
                    this.currentConsumption += var13;
                }

                this.currentLocalConsumption += var13;
                double var15 = var19 * (double)var2;
                if (var4.power * (double)var5 <= 0.0D) {
                    var3.setPowered(0.0F);
                    var3.reloadFromReactor(0.0D, var1, var2, var12, 0.0F);
                } else {
                    float var18;
                    if (var15 > 0.0D && var4.power * (double)var5 < var15) {
                        var18 = (float)(var4.power * (double)var5 / var15);
                    } else {
                        var18 = 1.0F;
                    }

                    if (var13 > 0.0D && var4.power * (double)var5 < var13) {
                        var5 = (float)(var4.power * (double)var5 / var13);
                        var4.power = 0.0D;
                    } else {
                        var4.power -= var13;
                        var5 = 1.0F;
                    }

                    var3.setPowered(var5);
                    var3.reloadFromReactor((double)(var5 * var2), var1, var2, var12, var18);
                }
            }

            this.priorityQueue.addPercent(var3.getPowerConsumerCategory(), (double)var3.getPowered());
            this.priorityQueue.addTotalPercent(var3.getPowered());
        }
    }

    public SegmentController getSegmentController() {
        return this.container.getSegmentController();
    }

    public boolean isClientOwnObject() {
        return this.container.getSegmentController().isClientOwnObject();
    }

    public boolean isOnServer() {
        return this.container.isOnServer();
    }

    public boolean isUsingPowerReactors() {
        if (!this.getSegmentController().isFullyLoaded()) {
            return this.usingReactorsFromTag;
        } else {
            return !this.getSegmentController().isUsingOldPower();
        }
    }

    public double getPowerAsPercent() {
        SegmentController var1;
        if (this.getSegmentController().railController.isDockedAndExecuted() && (var1 = this.getSegmentController().railController.getRoot()) instanceof ManagedSegmentController) {
            return ((ManagedSegmentController)var1).getManagerContainer().getPowerInterface().getPowerAsPercent();
        } else {
            return this.maxPower > 0.0D ? this.power / this.maxPower : 0.0D;
        }
    }

    public void addConsumer(PowerConsumer var1) {
        this.powerConsumers.add(var1);
        this.consumersChanged = true;
    }

    public void removeConsumer(PowerConsumer var1) {
        this.powerConsumers.remove(var1);
        this.consumersChanged = true;
    }

    public double getCurrentConsumption() {
        return this.currentConsumption;
    }

    public double getCurrentLocalConsumption() {
        return this.currentLocalConsumption;
    }

    public double getCurrentLocalConsumptionPerSec() {
        return this.currentLocalConsumptionPerSec;
    }

    public double getCurrentPowerGain() {
        return this.currentPowerGain;
    }

    public double getCurrentConsumptionPerSec() {
        return this.currentConsumptionPerSec;
    }

    public void convertRequest(long var1, short var3) {
        this.getNetworkObject().getConvertRequestBuffer().add(ElementCollection.getIndex4(var1, var3));
    }

    public LongArrayFIFOQueue getChamberConvertRequests() {
        return this.chamberConvertRequests;
    }

    public LongArrayFIFOQueue getTreeBootRequests() {
        return this.treeBootRequests;
    }

    public boolean isInAnyTree(ReactorChamberUnit var1) {
        return this.getReactorSet().isInAnyTree(var1);
    }

    private void doBoot(ReactorTree var1) {
        long var2 = this.selectedReactor;
        this.selectedReactor = var1.getId();
        this.reactorSwitchCooldown = VoidElementManager.REACTOR_SWITCH_COOLDOWN_SEC;
        var1.resetBootedRecursive();
        this.flagStabilizerPathCalc();
        System.err.println(this.getState() + "[REACTOR] Reactor switched: " + var2 + " -> " + this.selectedReactor);
    }

    public void flagStabilizerPathCalc() {
        this.flagStabPathCalc = true;
    }

    public StateInterface getState() {
        return this.getSegmentController().getState();
    }

    public void boot(ReactorTree var1) {
        if (this.isOnServer()) {
            this.doBoot(var1);
        } else {
            this.getNetworkObject().getBootRequestBuffer().add(var1.getId());
        }
    }

    public void onLastReactorRemoved() {
        this.flagStabilizerPathCalc();
    }

    public boolean isActiveReactor(ReactorTree var1) {
        if (this.isDocked()) {
            return false;
        } else {
            return this.selectedReactor == var1.getId();
        }
    }

    public float getReactorSwitchCooldown() {
        return this.reactorSwitchCooldown;
    }

    public float getReactorRebootCooldown() {
        return this.reactorRebootCooldown;
    }

    public double getActiveReactorIntegrity() {
        return this.getActiveReactor() != null ? this.getActiveReactor().getIntegrity() : 0.0D;
    }

    public void fromTagStructure(Tag var1) {
        Tag[] var4;
        byte var2 = (var4 = var1.getStruct())[0].getByte();
        this.selectedReactor = var4[1].getLong();
        ReactorSet var3;
        (var3 = new ReactorSet(this)).fromTagStructure(var4[2]);
        this.reactorSet.apply(var3);
        this.priorityQueue.fromTagStructure(var4[3]);
        if (var4.length > 4 && var4[4].getType() == Type.BYTE) {
            this.usingReactorsFromTag = var4[4].getByte() != 0;
        }

        if (var2 > 0) {
            this.reactorSwitchCooldown = var4[5].getFloat();
            this.reactorRebootCooldown = var4[6].getFloat();
        }

    }

    public Tag toTagStructure() {
        Tag var1 = new Tag(Type.BYTE, (String)null, (byte)1);
        Tag var2 = new Tag(Type.LONG, (String)null, this.selectedReactor);
        boolean var3 = this.getSegmentController().isFullyLoaded() ? this.isUsingPowerReactors() : this.usingReactorsFromTag;
        return new Tag(Type.STRUCT, (String)null, new Tag[]{var1, var2, this.reactorSet.toTagStructure(), this.priorityQueue.toTagStructure(), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(var3 ? 1 : 0))), new Tag(Type.FLOAT, (String)null, this.reactorSwitchCooldown), new Tag(Type.FLOAT, (String)null, this.reactorRebootCooldown), FinishTag.INST});
    }

    public void updateFromNetworkObject(NetworkObject var1) {
        ObjectArrayList var2 = ((PowerInterfaceNetworkObject)var1).getReactorSetBuffer().getReceiveBuffer();

        int var3;
        for(var3 = 0; var3 < var2.size(); ++var3) {
            ReactorSet var4 = (ReactorSet)((RemoteReactorSet)var2.get(var3)).get();
            this.receivedReactorSets.enqueue(var4);
        }

        ShortArrayList var5 = ((PowerInterfaceNetworkObject)var1).getRecalibrateRequestBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var5.size(); ++var3) {
            this.requestedRecalibrate = true;
        }

        var2 = ((PowerInterfaceNetworkObject)var1).getReactorBonusMatrixUpdateBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var2.size(); ++var3) {
            this.receivedBonusMatrixUpdates.enqueue(((RemoteReactorBonusMatrix)var2.get(var3)).get());
        }

        var2 = this.getNetworkObject().getReactorChangeBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var2.size(); ++var3) {
            LongIntPair var8 = (LongIntPair)((RemoteLongIntPair)var2.get(var3)).get();
            this.changedModuleSet.put(var8.l, var8.i);
        }

        LongArrayList var6 = ((PowerInterfaceNetworkObject)var1).getConvertRequestBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var6.size(); ++var3) {
            this.getChamberConvertRequests().enqueue(var6.getLong(var3));
        }

        FloatArrayList var7 = ((PowerInterfaceNetworkObject)var1).getReactorCooldownBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var7.size(); var3 += 2) {
            if (var3 <= var7.size() - 2) {
                this.reactorSwitchCooldown = var7.getFloat(var3);
                this.reactorRebootCooldown = var7.getFloat(var3 + 1);
            }
        }

        var7 = ((PowerInterfaceNetworkObject)var1).getEnergyStreamCooldownBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var7.size(); ++var3) {
            this.currentEnergyStreamCooldown = var7.getFloat(var3);
        }

        var6 = ((PowerInterfaceNetworkObject)var1).getBootRequestBuffer().getReceiveBuffer();

        for(var3 = 0; var3 < var6.size(); ++var3) {
            this.getTreeBootRequests().enqueue(var6.getLong(var3));
        }

        if (!this.isOnServer()) {
            this.selectedReactorToSet = ((PowerInterfaceNetworkObject)var1).getActiveReactor().getLong();
        }

        this.priorityQueue.receive((PowerInterfaceNetworkObject)var1);
    }

    public void updateToFullNetworkObject(NetworkObject var1) {
        ((PowerInterfaceNetworkObject)var1).getActiveReactor().set(this.selectedReactor);
        this.getNetworkObject().getReactorSetBuffer().add(new RemoteReactorSet(this.reactorSet, this.isOnServer()));
        this.priorityQueue.send((PowerInterfaceNetworkObject)var1);
        this.sendCooldowns();
    }

    public void sendCooldowns() {
        if (this.reactorSwitchCooldown > 0.0F || this.reactorRebootCooldown > 0.0F) {
            this.getNetworkObject().getReactorCooldownBuffer().add(this.reactorSwitchCooldown);
            this.getNetworkObject().getReactorCooldownBuffer().add(this.reactorRebootCooldown);
        }

        if (this.currentEnergyStreamCooldown > 0.0F) {
            this.getNetworkObject().getEnergyStreamCooldownBuffer().add(this.currentEnergyStreamCooldown);
        }

    }

    public void updateToNetworkObject(NetworkObject var1) {
        if (this.isOnServer()) {
            ((PowerInterfaceNetworkObject)var1).getActiveReactor().set(this.selectedReactor);
        }

    }

    public void initFromNetworkObject(NetworkObject var1) {
        this.updateFromNetworkObject(var1);
        this.selectedReactor = ((PowerInterfaceNetworkObject)var1).getActiveReactor().getLong();
    }

    public String toString() {
        return "[POWERINTERFACE: " + this.getSegmentController() + "]";
    }

    public void onBlockKilledServer(Damager var1, short var2, long var3) {
        assert this.isOnServer();

        if (this.reactorSet.onBlockKilledServer(var1, var2, var3, this.changedModuleSet)) {
            ++blocksKilled;
            this.setReactorRebootCooldown();
        } else {
            ++blocksNotKilled;
        }
    }

    public void setReactorRebootCooldown() {
        this.reactorRebootCooldown = this.getRebootTimeSec();
    }

    public float getRebootTimeSec() {
        return VoidElementManager.REACTOR_REBOOT_MIN_COOLDOWN_SEC + (float)Math.max(0.0D, (Math.log10((double)this.getSegmentController().getMassWithDocks()) + (double)VoidElementManager.REACTOR_REBOOT_LOG_OFFSET) * (double)VoidElementManager.REACTOR_REBOOT_LOG_FACTOR) * (float)(1.0D - (double)this.getCurrentHp() / (double)this.getCurrentMaxHp()) * VoidElementManager.REACTOR_REBOOT_SEC_PER_HP_PERCENT;
    }

    public void onShieldDamageServer(double var1) {
    }

    public void onBlockDamageServer(Damager var1, int var2, short var3, long var4) {
    }

    public boolean isAnyDamaged() {
        return this.reactorSet.isAnyDamaged();
    }

    public void requestRecalibrate() {
        if (this.isOnServer()) {
            this.requestedRecalibrate = true;
        } else {
            this.getNetworkObject().getRecalibrateRequestBuffer().add((short)0);
        }
    }

    public void onAnyReactorModulesChanged() {
        if (!this.reactorSet.isAnyDamaged()) {
            this.requestedRecalibrate = true;
        }

    }

    public int updatePrio() {
        return 1000;
    }

    public static int getReactorLevel(int var0) {
        switch(VoidElementManager.REACTOR_LEVEL_CALC_STYLE) {
            case LINEAR:
                return Math.max(0, var0 / VoidElementManager.REACTOR_LEVEL_CALC_LINEAR_BLOCKS_NEEDED_PER_LEVEL);
            case LOG10:
                int var1 = Math.max(0, (int)Math.log10((double)var0));
                int var2 = Math.max(10, (int)Math.pow(10.0D, (double)var1));
                var0 /= var2;
                var0 += Math.max(0, var1) * 10;
                return Math.max(0, var0 - 10);
            default:
                throw new RuntimeException("Unknown Reactor Calc Style: " + VoidElementManager.REACTOR_LEVEL_CALC_STYLE.name());
        }
    }

    public static int convertLinearLvl(int var0) {
        return VoidElementManager.REACTOR_LEVEL_CALC_STYLE == ReactorLevelCalcStyle.LINEAR ? var0 : var0 - var0 / 10;
    }

    public static String printReactorLevel(int var0) {
        int var1 = getReactorLevel(var0);
        return "blocks: " + var0 + "; Level: " + convertLinearLvl(var1) + "; Min Blocks: " + getMinNeededFromReactorLevelRaw(var1) + "; Max Blocks: " + getReactorMaxFromLevel(var1);
    }

    public static int getMinNeededFromReactorLevelRaw(int var0) {
        if (var0 == 0) {
            return 1;
        } else {
            switch(VoidElementManager.REACTOR_LEVEL_CALC_STYLE) {
                case LINEAR:
                    return Math.max(0, var0 * VoidElementManager.REACTOR_LEVEL_CALC_LINEAR_BLOCKS_NEEDED_PER_LEVEL);
                case LOG10:
                    int var1 = var0 / 10;
                    var0 = Math.max(0, var0 % 10 - 1);
                    return (var1 = (int)Math.pow(10.0D, (double)var1)) * 10 + var0 * var1 * 10;
                default:
                    throw new RuntimeException("Unknown Reactor Calc Style: " + VoidElementManager.REACTOR_LEVEL_CALC_STYLE.name());
            }
        }
    }

    public float getReactorToChamberSizeRelation() {
        return VoidElementManager.REACTOR_CHAMBER_BLOCKS_PER_MAIN_REACTOR_AND_LEVEL;
    }

    public boolean isChamberValid(int var1, int var2) {
        return isChamberValid(var1, var2, this.getReactorToChamberSizeRelation());
    }

    public int getNeededMinForReactorLevel(int var1) {
        return getMinNeededFromReactorLevel(getReactorLevel(var1), this.getReactorToChamberSizeRelation());
    }

    public int getNeededMaxForReactorLevel(int var1) {
        return getMinNeededFromReactorLevel(getReactorLevel(var1) + 1, this.getReactorToChamberSizeRelation());
    }

    public int getNeededMinForReactorLevelByLevel(int var1) {
        return getMinNeededFromReactorLevel(var1, this.getReactorToChamberSizeRelation());
    }

    public int getNeededMaxForReactorLevelByLevel(int var1) {
        return getMinNeededFromReactorLevel(var1 + 1, this.getReactorToChamberSizeRelation());
    }

    public static int getReactorMaxFromLevel(int var0) {
        return getMinNeededFromReactorLevelRaw(var0 + 1);
    }

    public static int getMinNeededFromReactorLevel(int var0, float var1) {
        var0 = getMinNeededFromReactorLevelRaw(var0);
        return (int)(var1 * (float)var0);
    }

    public static boolean isChamberValid(int var0, int var1, float var2) {
        var0 = getMinNeededFromReactorLevel(getReactorLevel(var0), var2);
        return var1 >= var0;
    }

    public ShortList getAppliedConfigGroups(ShortList var1) {
        if (this.isDocked()) {
            if (this.getSegmentController().railController.getRoot() instanceof ManagedSegmentController) {
                ((ManagedSegmentController)this.getSegmentController().railController.getRoot()).getManagerContainer().getPowerInterface().getAppliedConfigGroups(var1);
            }
        } else {
            this.reactorSet.getAppliedConfigGroups(var1);
        }

        return var1;
    }

    public ReactorPriorityQueue getPowerConsumerPriorityQueue() {
        return this.priorityQueue;
    }

    public ConfigPool getConfigPool() {
        return this.container.getSegmentController().getConfigManager().getConfigPool();
    }

    public void reactorTreeReceived(ReactorTree var1) {
        this.receivedTrees.enqueue(var1);
    }

    public void checkRemovedChamber(List<ReactorChamberUnit> var1) {
        this.getConduits().checkRemovedChamber(var1);
    }

    public boolean isInstable() {
        return this.getReactorBoost() > 0.0F || this.getStabilizerEfficiencyTotal() < VoidElementManager.REACTOR_EXPLOSION_STABILITY;
    }

    public double getStabilizerEfficiencyTotal() {
        double var1 = this.getStabilization();
        double var3 = this.getActiveReactorCurrentSize();
        return getStabilizerEfficiency(var1, var3);
    }

    public static double getStabilizerEfficiency(double var0, double var2) {
        if (var0 > var2) {
            return 1.0D;
        } else if (var0 <= 0.0D) {
            return 0.0D;
        } else {
            return var2 <= 0.0D ? 0.0D : var0 / var2;
        }
    }

    public double getStabilizerEfficiencyExtra() {
        double var1 = this.getStabilization();
        double var3 = this.getActiveReactorCurrentSize();
        return var1 > var3 ? (var1 - var3) / var3 : 0.0D;
    }

    public long getCurrentHp() {
        if (this.getSegmentController().railController.isDockedAndExecuted()) {
            return this.getSegmentController().railController.getRoot() instanceof ShopSpaceStation ? 1L : ((ManagedSegmentController)this.getSegmentController().railController.getRoot()).getManagerContainer().getPowerInterface().getCurrentHp();
        } else {
            return this.getCurrentHpRaw();
        }
    }

    public long getCurrentHpRaw() {
        ReactorTree var1;
        if ((var1 = this.getActiveReactor()) != null) {
            return var1.getHp();
        } else {
            SegmentPiece var2;
            return this.getSegmentController() instanceof Ship && (var2 = this.getSegmentController().getSegmentBuffer().getPointUnsave(Ship.core)) != null && var2.getHitpointsByte() == 0 ? 0L : 1L;
        }
    }

    public long getCurrentMaxHpRaw() {
        ReactorTree var1;
        return (var1 = this.getActiveReactor()) != null ? var1.getMaxHp() : 1L;
    }

    public ReactorTree getActiveReactor() {
        return this.reactorSet.getActiveReactor();
    }

    public float getChamberCapacity() {
        return this.getActiveReactor() != null ? this.getActiveReactor().getChamberCapacity() : 0.0F;
    }

    public long getCurrentMaxHp() {
        if (this.getSegmentController().railController.isDockedAndExecuted()) {
            return this.getSegmentController().railController.getRoot() instanceof ShopSpaceStation ? 1L : ((ManagedSegmentController)this.getSegmentController().railController.getRoot()).getManagerContainer().getPowerInterface().getCurrentMaxHp();
        } else {
            return this.getCurrentMaxHpRaw();
        }
    }

    public ReactorElement getChamber(long var1) {
        return this.reactorSet.getChamber(var1);
    }

    public double getPowerConsumptionAsPercent() {
        SegmentController var1;
        if (this.getSegmentController().railController.isDockedAndExecuted() && (var1 = this.getSegmentController().railController.getRoot()) instanceof ManagedSegmentController) {
            return ((ManagedSegmentController)var1).getManagerContainer().getPowerInterface().getPowerConsumptionAsPercent();
        } else {
            return this.getRechargeRatePowerPerSec() == 0.0D ? 0.0D : this.getCurrentConsumptionPerSec() / this.getRechargeRatePowerPerSec();
        }
    }

    public List<PowerConsumer> getPowerConsumerList() {
        return this.powerConsumerList;
    }

    public boolean hasActiveReactors() {
        return this.isUsingPowerReactors() && !this.getSegmentController().railController.isDockedAndExecuted() && this.getActiveReactor() != null;
    }

    public boolean hasAnyReactors() {
        return this.isUsingPowerReactors() && this.reactorSet.getTrees().size() > 0;
    }

    public ManagerContainer<? extends SegmentController> getManagerContainer() {
        return this.container;
    }

    public void switchActiveReacorToMostHp(ReactorTree var1) {
        if (this.treeBootRequests.isEmpty() && this.reactorSwitchCooldown == 0.0F) {
            ReactorTree var2 = null;
            Iterator var3 = this.reactorSet.getTrees().iterator();

            while(true) {
                ReactorTree var4;
                do {
                    do {
                        if (!var3.hasNext()) {
                            if (var2 != null) {
                                System.err.println("[SERVER][REACTOR] FAILSAVE REACTOR SWITCH TO " + var2.getName());
                                this.treeBootRequests.enqueue(var2.getId());
                            }

                            return;
                        }
                    } while((var4 = (ReactorTree)var3.next()) == var1);
                } while(var2 != null && (var4.getHp() <= var2.getHp() || var4.getHpPercent() <= (double)this.getSegmentController().getConfigManager().apply(StatusEffectType.REACTOR_FAILSAFE_HPPERCENT_MIN_TARGET_THRESHOLD, 1.0F)));

                var2 = var4;
            }
        }
    }

    public boolean canUpdate() {
        return true;
    }

    public void onNoUpdate(Timer var1) {
    }

    public void setReactorBoost(float var1) {
        this.reactorBoost = var1;
    }

    public float getReactorBoost() {
        return this.reactorBoost;
    }

    public long getSourceId() {
        return 0L;
    }

    public void registerProjectionConfigurationSource(ConfigProviderSource var1) {
        this.configProjectionSources.put(var1.getSourceId(), var1);
    }

    public void unregisterProjectionConfigurationSource(ConfigProviderSource var1) {
        this.configProjectionSources.remove(var1.getSourceId());
    }

    public void addSectorConfigProjection(Collection<ConfigProviderSource> var1) {
        var1.addAll(this.configProjectionSources.values());
    }

    public long getActiveReactorId() {
        return this.selectedReactor;
    }

    public boolean isActiveReactor(long var1) {
        return var1 == this.selectedReactor;
    }

    public boolean isAnyRebooting() {
        return this.reactorSet.getTrees().size() > 0 && (this.getReactorSwitchCooldown() > 0.0F || this.getReactorRebootCooldown() > 0.0F || this.getActiveReactor() != null && this.getActiveReactor().isAnyChamberBootingUp());
    }

    public double getStabilizerIntegrity() {
        return this.getStabilizer().getIntegrity();
    }

    public static boolean hasEnergyStreamDocked(SimpleTransformableSendableObject<?> var0) {
        return var0 instanceof ManagedSegmentController && !((SegmentController)var0).isUsingOldPower() && !((ManagedSegmentController)var0).getManagerContainer().getPowerInterface().getStabilizerPaths().isEmpty();
    }

    public static boolean hasEnergyStream(SimpleTransformableSendableObject<?> var0) {
        return var0 instanceof ManagedSegmentController && ((SegmentController)var0).railController.getRoot() == var0 && hasEnergyStreamDocked(var0);
    }

    public void powerChanged(PowerChangeType var1) {
        if (!this.isOnServer()) {
            ((GameClientState)this.getState()).onPowerChanged(this.getSegmentController(), var1);
        }

    }

    private void calculateStabilizerPaths() {
        this.onPhysicsRemove();
        if (!this.energyStream) {
            this.stabilizerPaths.clear();
        } else {
            this.stabilizerPaths.clear();
            if (this.getActiveReactor() != null) {
                this.getStabilizer().calculatePaths(this.getActiveReactor(), this.stabilizerPaths);
            }

            this.powerChanged(PowerChangeType.STABILIZER_PATH);
            if (this.getActiveReactor() != null) {
                this.onPhysicsAdd();
            }

        }
    }

    public List<StabilizerPath> getStabilizerPaths() {
        return this.stabilizerPaths;
    }

    public void onPhysicsAdd() {
        PhysicsExt var1;
        if (this.isOnServer()) {
            if (!((GameServerState)this.getState()).getUniverse().existsSector(this.getSegmentController().getSectorId())) {
                return;
            }

            var1 = this.getSegmentController().getPhysics();
        } else {
            if (!this.getSegmentController().isClientSectorIdValidForSpawning(this.getSegmentController().getSectorId())) {
                return;
            }

            var1 = this.getSegmentController().getPhysics();
        }

        Iterator var2 = this.getStabilizerPaths().iterator();

        while(var2.hasNext()) {
            ((StabilizerPath)var2.next()).onPhysicsAdd(this.getSegmentController(), var1);
        }

    }

    public void onPhysicsRemove() {
        if (!this.isOnServer() || ((GameServerState)this.getState()).getUniverse().existsSector(this.getSegmentController().getSectorId())) {
            PhysicsExt var1 = this.getSegmentController().getPhysics();
            Iterator var2 = this.getStabilizerPaths().iterator();

            while(var2.hasNext()) {
                ((StabilizerPath)var2.next()).onPhysicsRemove(this.getSegmentController(), var1);
            }

        }
    }

    public void updateWithoutPhysicsObject() {
        Iterator var1 = this.stabilizerPaths.iterator();

        while(var1.hasNext()) {
            StabilizerPath var2 = (StabilizerPath)var1.next();
            if (this.isOnServer()) {
                var2.updateTransform(this.getSegmentController().getWorldTransform());
            } else {
                var2.updateTransform(this.getSegmentController().getWorldTransformOnClient());
            }
        }

    }

    public void checkRootIntegrity() {
    }

    public void drawDebugEnergyStream() {
        Iterator var1 = this.stabilizerPaths.iterator();

        while(var1.hasNext()) {
            ((StabilizerPath)var1.next()).drawDebug(this.getSegmentController());
        }

    }

    public float getStabilzerPathRadius() {
        return VoidElementManager.REACTOR_STABILIZER_PATH_RADIUS_DEFAULT + (float)this.getActiveReactorLevel() * VoidElementManager.REACTOR_STABILIZER_PATH_RADIUS_PER_LEVEL;
    }

    private int getActiveReactorLevel() {
        ReactorTree var1;
        return (var1 = (ReactorTree)this.getReactorSet().getTreeMap().get(this.selectedReactor)) == null ? 0 : var1.getLevel();
    }

    public boolean isStabilizerPathHit() {
        return this.currentEnergyStreamCooldown > 0.0F;
    }

    public void destroyStabilizersBasedOnReactorSize(Damager var1) {
    }

    public float getExtraDamageTakenFromStabilization() {
        double var1 = (double)VoidElementManager.REACTOR_LOW_STABILIZATION_EXTRA_DAMAGE_START;
        double var3 = (double)VoidElementManager.REACTOR_LOW_STABILIZATION_EXTRA_DAMAGE_END;
        double var5 = (double)VoidElementManager.REACTOR_LOW_STABILIZATION_EXTRA_DAMAGE_START_DAMAGE;
        double var7 = (double)VoidElementManager.REACTOR_LOW_STABILIZATION_EXTRA_DAMAGE_END_DAMAGE;
        double var9;
        if (var1 > var3 && var7 > var5 && (var9 = this.getStabilizerEfficiencyTotal()) < var1) {
            double var11 = var1 - var3;
            double var13 = 0.0D;
            if (var9 > var3) {
                var13 = (var9 - var3) / var11;
            }

            double var15 = var7 - var5;
            return (float)((1.0D - var13) * var15 + var5);
        } else {
            return 0.0F;
        }
    }

    public void doEnergyStreamCooldownOnHit(Damager var1, float var2, long var3) {
        if (this.energyStream) {
            if (this.isOnServer()) {
                float var5 = var2 * VoidElementManager.REACTOR_STABILIZATION_ENERGY_STREAM_HIT_COOLDOWN_PER_DAMAGE_IN_SEC / (float)Math.max(1, this.getReactorSet().getActiveReactor().getLevel());
                System.out.println("energyStreamCooldown: level " + this.getReactorSet().getActiveReactor().getLevel() + " damage " + var2 + " => " + var5);
                var5 = Math.min(Math.max(var5, VoidElementManager.REACTOR_STABILIZATION_ENERGY_STREAM_HIT_MIN_COOLDOWN_IN_SEC), VoidElementManager.REACTOR_STABILIZATION_ENERGY_STREAM_HIT_MAX_COOLDOWN_IN_SEC);
                this.currentEnergyStreamCooldown = Math.max(var5, this.currentEnergyStreamCooldown);
            }

        }
    }

    public float getCurrentEnergyStreamDamageCooldown() {
        return this.currentEnergyStreamCooldown;
    }

    public void injectPower(Damager var1, double var2) {
        this.injectedPowerPerSec = var2;
        this.injectedPowerTimoutSec = 5.0F;
    }

    public void setEnergyStreamEnabled(boolean var1) {
        if (this.energyStream != var1) {
            this.flagStabPathCalc = true;
        }

        this.energyStream = var1;
    }
}
