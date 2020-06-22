//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.CompoundShapeChild;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.FastMath;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3b;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.BlockBuffer;
import org.schema.game.client.controller.manager.ingame.BuildCallback;
import org.schema.game.client.controller.manager.ingame.BuildInstruction;
import org.schema.game.client.controller.manager.ingame.BuildRemoveCallback;
import org.schema.game.client.controller.manager.ingame.SymmetryPlanes;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.buildhelper.BuildHelper;
import org.schema.game.client.view.camera.InShipCamera;
import org.schema.game.client.view.camera.SegmentControllerCamera;
import org.schema.game.common.controller.HpTrigger.HpTriggerType;
import org.schema.game.common.controller.ai.AIGameSegmentControllerConfiguration;
import org.schema.game.common.controller.ai.SegmentControllerAIInterface;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitReceiverType;
import org.schema.game.common.controller.damage.Hittable;
import org.schema.game.common.controller.damage.effects.InterEffectContainer;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShieldHitCallback;
import org.schema.game.common.controller.elements.StationaryManagerContainer;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.cloaking.StealthAddOn.StealthLvl;
import org.schema.game.common.controller.elements.shipyard.ShipyardCollectionManager;
import org.schema.game.common.controller.generator.CreatorThread;
import org.schema.game.common.controller.io.SegmentDataFileUtils;
import org.schema.game.common.controller.io.UniqueIdentifierInterface;
import org.schema.game.common.controller.rails.RailController;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.controller.rails.RailController.RailTrigger;
import org.schema.game.common.controller.rules.rules.SegmentControllerRuleEntityManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.blockeffects.config.ConfigManagerInterface;
import org.schema.game.common.data.blockeffects.config.ConfigPoolProvider;
import org.schema.game.common.data.blockeffects.config.ConfigProviderSource;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager.EffectEntityType;
import org.schema.game.common.data.creature.AICreature;
import org.schema.game.common.data.creature.CannotInstantiateAICreatureException;
import org.schema.game.common.data.element.ActivationTrigger;
import org.schema.game.common.data.element.ControlElementMap;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementDocking;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.ElementInformation.ResourceInjectionType;
import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.common.data.fleet.FleetStateInterface;
import org.schema.game.common.data.physics.CollisionType;
import org.schema.game.common.data.physics.CubeShape;
import org.schema.game.common.data.physics.CubesCompoundShape;
import org.schema.game.common.data.physics.RigidBodySegmentController;
import org.schema.game.common.data.player.AbstractOwnerState;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.common.data.world.EntityUID;
import org.schema.game.common.data.world.GravityStateInterface;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.RuleEntityContainer;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.Universe;
import org.schema.game.common.util.Collisionable;
import org.schema.game.network.objects.NetworkSegmentController;
import org.schema.game.network.objects.remote.RemoteSegmentPiece;
import org.schema.game.server.controller.ServerSegmentProvider;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.simulation.npc.geo.NPCSystem;
import org.schema.game.server.data.simulation.npc.geo.NPCEntityContingent.NPCEntitySpecification;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.BoundingBox;
import org.schema.schine.graphicsengine.forms.DebugBox;
import org.schema.schine.graphicsengine.forms.debug.DebugDrawer;
import org.schema.schine.graphicsengine.forms.debug.DebugLine;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.input.InputState;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.TopLevelType;
import org.schema.schine.network.UniqueLongIDInterface;
import org.schema.schine.network.objects.LocalSectorTransition;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.container.TransformTimed;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.network.server.ServerStateInterface;
import org.schema.schine.physics.Physical;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public abstract class SegmentController extends SimpleTransformableSendableObject<SendableSegmentProvider> implements Hittable, UniqueIdentifierInterface, ConfigManagerInterface, RuleEntityContainer, Collisionable, UniqueLongIDInterface {
    private static final byte TAG_VERSION = 1;
    public byte tagVersion;
    public static int dockingChecks;
    public long lastEditBlocks;
    public long lastDamageTaken;
    public final RailController railController;
    protected final Vector3b tmpLocalPos = new Vector3b();
    protected final Vector3i posTmp = new Vector3i();
    private final Vector3f centerOfMassUnweighted = new Vector3f();
    private final SegmentControllerHpControllerInterface hpController;
    private final DockingController dockingController;
    private final Vector3iSegment maxPos = new Vector3iSegment();
    private final Vector3iSegment minPos = new Vector3iSegment();
    private final SegmentControllerRuleEntityManager ruleEntityManager;
    private final ElementCountMap elementClassCountMap = new ElementCountMap();
    private final SegmentControllerElementCollisionChecker collisionChecker;
    private final Vector3i testPos = new Vector3i();
    private final Vector3f camPosLocal = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    private final Vector3f camForwLocal = new Vector3f();
    private final Vector3f camLeftLocal = new Vector3f();
    private final Vector3f camUpLocal = new Vector3f();
    private final ControlElementMap controlElementMap;
    private final SlotAssignment slotAssignment;
    private final ObjectArrayFIFOQueue<SegmentPiece> needsActiveUpdateClient = new ObjectArrayFIFOQueue();
    private final LongArrayList textBlocks = new LongArrayList();
    private final Long2ObjectOpenHashMap<String> textMap = new Long2ObjectOpenHashMap();
    private final Transform clientTransformInverse = new Transform();
    private final Vector3f camLocalTmp = new Vector3f();
    private final ObjectOpenHashSet<ActivationTrigger> triggers = new ObjectOpenHashSet();
    public boolean flagPhysicsAABBUpdate;
    public CollisionObject stuckFrom;
    public long stuckFromTime;
    public boolean forceSpecialRegion;
    protected float totalPhysicalMass;
    protected long coreTimerStarted = -1L;
    protected long coreTimerDuration = -1L;
    protected boolean flagUpdateMass;
    private final BlockTypeSearchRunnableManager blockTypeSearchManager;
    private final IntSet proximityObjects = new IntOpenHashSet();
    private boolean scrap;
    private boolean vulnerable = true;
    private boolean minable = true;
    private SegmentProvider segmentProvider;
    private int creatorId;
    private CreatorThread creatorThread;
    private int id = -1234;
    private String realName = "undef";
    private int totalElements = 0;
    private SegmentBufferInterface segmentBuffer;
    private byte factionRights = 0;
    private String uniqueIdentifier;
    private long timeCreated;
    private boolean aabbRecalcFlag;
    private boolean flagCheckDocking;
    private boolean flagSegmentBufferAABBUpdate;
    private String spawner = "";
    private String lastModifier = "";
    private long seed;
    private long delayDockingCheck;
    private int lastSector;
    private boolean newlyCreated = true;
    public NPCSystem npcSystem;
    private boolean virtualBlueprint;
    private ConfigEntityManager configManager;
    public float percentageDrawn = 1.0F;
    private boolean spawnedInDatabaseAsChunk16;
    private boolean onFullyLoadedExcecuted;
    public boolean hadAtLeastOneElement;
    private long checkVirtualDock;
    public String blueprintSegmentDataPath;
    public String blueprintIdentifier;
    public ElementCountMap itemsToSpawnWith;
    public SegmentController.PullPermission pullPermission;
    protected long lastAsked;
    protected long askForPullClient;
    public int oldPowerBlocksFromBlueprint;
    private Vector3i checkSector;
    private boolean onMassUpdate;
    protected boolean fullyLoadedRailRecChache;
    public final Matrix3f tensor;
    private final Matrix3f j;
    private final Vector3f bPos;
    public String currentOwnerLowerCase;
    public String lastDockerPlayerServerLowerCase;
    public long dbId;
    public final Vector3f proximityVector;
    public long blinkTime;
    public Shader blinkShader;
    private boolean loadedFromChunk16;
    public NPCEntitySpecification npcSpec;
    private long lastStuck;
    private long stuckTime;
    private int stuckCount;
    private boolean factionSetFromBlueprint;
    private boolean flagLongStuck;
    private long lastExceptionPrint;
    private int updateCounter;
    private boolean checkedRootDb;
    public long lastAllowed;
    private float massMod;
    public boolean usedOldPowerFromTag;
    public boolean usedOldPowerFromTagForcedWrite;
    protected long lastSendHitConfirm;
    private long lastControlledMsgUpdate;
    private boolean blockAdded;
    private boolean blockRemoved;
    public long lastAdminCheckFlag;
    public long lastAnyDamageTakenServer;
    private boolean flagAnyDamageTakenServer;
    private long lastAttackTrigger;

    public boolean isInTestSector() {
        Vector3i var1;
        return (var1 = this.getSector(this.checkSector)) != null && Sector.isPersonalOrTestSector(var1);
    }

    public SegmentControllerRuleEntityManager getRuleEntityManager() {
        return this.ruleEntityManager;
    }

    protected InterEffectContainer setupEffectContainer() {
        return new SegmentController.SegmentControllerEffectSet();
    }

    public SegmentController(StateInterface var1) {
        super(var1);
        this.pullPermission = SegmentController.PullPermission.ASK;
        this.checkSector = new Vector3i();
        this.fullyLoadedRailRecChache = false;
        this.tensor = new Matrix3f();
        this.tensor.setZero();
        this.j = new Matrix3f();
        this.bPos = new Vector3f();
        this.currentOwnerLowerCase = "";
        this.lastDockerPlayerServerLowerCase = "";
        this.dbId = -1L;
        this.proximityVector = new Vector3f();
        this.usedOldPowerFromTag = false;
        this.usedOldPowerFromTagForcedWrite = false;
        this.collisionChecker = new SegmentControllerElementCollisionChecker(this);
        if (var1 instanceof ServerStateInterface) {
            this.segmentProvider = new ServerSegmentProvider((SendableSegmentController)this);
        } else {
            this.segmentProvider = new ClientSegmentProvider((SendableSegmentController)this);
        }

        this.segmentBuffer = new SegmentBufferManager(this);
        this.controlElementMap = new ControlElementMap();
        this.railController = new RailController(this);
        this.blockTypeSearchManager = new BlockTypeSearchRunnableManager(this);
        this.setTimeCreated(System.currentTimeMillis());
        this.dockingController = new DockingController(this);
        this.slotAssignment = new SlotAssignment((SendableSegmentController)this);
        this.hpController = new SegmentControllerHpController((SendableSegmentController)this);
        this.ruleEntityManager = new SegmentControllerRuleEntityManager(this);
    }

    public SimpleTransformableSendableObject<?> getShootingEntity() {
        return this;
    }

    public float getDamageTakenMultiplier(DamageDealerType var1) {
        return this.getConfigManager().apply(StatusEffectType.DAMAGE_TAKEN, var1, 1.0F) - 1.0F + 1.0F;
    }

    public float getDamageGivenMultiplier() {
        return 1.0F;
    }

    public abstract void onDamageServerRootObject(float var1, Damager var2);

    public void onBlockDamage(long var1, short var3, int var4, DamageDealerType var5, Damager var6) {
        if (this.isOnServer()) {
            this.railController.getRoot().onDamageServerRootObject((float)var4, var6);
            this.onAnyDamageTakenServer((double)var4, var6, var5);
        }

        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().onBlockDamage(var1, var3, var4, var5, var6);
        }

    }

    public abstract void onBlockKill(SegmentPiece var1, Damager var2);

    public String getObfuscationString() {
        return !this.isOnServer() ? ((GameClientState)this.getState()).getController().getConnection().getHost() : null;
    }

    public static void setConstraintFrameOrientation(byte var0, Transform var1, Vector3f var2, Vector3f var3, Vector3f var4) {
        switch(var0) {
            case 0:
            default:
                var1.basis.setRow(0, var2.x, var3.x, var4.x);
                var1.basis.setRow(1, var2.y, var3.y, var4.y);
                var1.basis.setRow(2, var2.z, var3.z, var4.z);
                return;
            case 1:
                var1.basis.setRow(0, -var2.x, -var3.x, -var4.x);
                var1.basis.setRow(1, -var2.y, -var3.y, -var4.y);
                var1.basis.setRow(2, -var2.z, -var3.z, -var4.z);
                return;
            case 2:
                var1.basis.setRow(0, var4.x, var2.x, var3.x);
                var1.basis.setRow(1, var4.y, var2.y, var3.y);
                var1.basis.setRow(2, var4.z, var2.z, var3.z);
                return;
            case 3:
                var1.basis.setRow(0, -var4.x, var2.x, -var3.x);
                var1.basis.setRow(1, -var4.y, var2.y, -var3.y);
                var1.basis.setRow(2, -var4.z, var2.z, -var3.z);
                return;
            case 4:
                var1.basis.setRow(0, -var4.x, -var3.x, -var2.x);
                var1.basis.setRow(1, -var4.y, -var3.y, -var2.y);
                var1.basis.setRow(2, -var4.z, -var3.z, -var2.z);
                return;
            case 5:
                var1.basis.setRow(0, var4.x, -var3.x, var2.x);
                var1.basis.setRow(1, var4.y, -var3.y, var2.y);
                var1.basis.setRow(2, var4.z, -var3.z, var2.z);
        }
    }

    public static boolean isPublicException(SegmentPiece var0, int var1) {
        if (var0 != null) {
            SegmentController var2 = var0.getSegmentController();
            Vector3i var3 = new Vector3i();
            var0.getAbsolutePos(var3);
            if (var0.getType() == 679) {
                Iterator var4 = ((StationaryManagerContainer)((ManagedSegmentController)var2).getManagerContainer()).getShipyard().getCollectionManagers().iterator();

                while(var4.hasNext()) {
                    ShipyardCollectionManager var5;
                    if ((var5 = (ShipyardCollectionManager)var4.next()).getConnectedCorePositionBlock4() == var0.getAbsoluteIndexWithType4() && var5.isPublicException(var1)) {
                        return true;
                    }
                }
            }

            if (isBlockPublicException(var2, var3, var1)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBlockPublicException(SegmentController var0, Vector3i var1, int var2) {
        Vector3i var3 = new Vector3i();

        for(int var4 = 0; var4 < 6; ++var4) {
            var3.add(var1, Element.DIRECTIONSi[var4]);
            SegmentPiece var5;
            if ((var5 = var0.getSegmentBuffer().getPointUnsave(var3)) == null) {
                return true;
            }

            if (var5.getType() == 346 || var5.getType() == 936 && var0.getFactionId() == var2) {
                return true;
            }
        }

        return false;
    }

    public boolean isScrap() {
        return this.scrap;
    }

    public void setScrap(boolean var1) {
        this.scrap = var1;
    }

    public float getMassWithoutDockIncludingStation() {
        double var1 = this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getMassFromInventories() : 0.0D;
        return (float)((double)this.getTotalPhysicalMass() + var1);
    }

    public float getMassWithDocks() {
        double var1 = this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getMassFromInventories() : 0.0D;
        return (float)((double)this.railController.calculateRailMassIncludingSelf() + var1);
    }

    public void sendHitConfirmToDamager(Damager var1, boolean var2) {
        if (var1 != null && (var1.isSegmentController() || !(this instanceof Planet) && !(this instanceof SpaceStation))) {
            var1.sendHitConfirm((byte)(var2 ? 2 : 1));
        }

    }

    public boolean isIgnorePhysics() {
        return this.getDockingController().getDelayedDock() != null;
    }

    public boolean isFullyLoaded() {
        return this.segmentBuffer.isFullyLoaded();
    }

    public boolean isFullyLoadedWithDock() {
        return this.railController.getRoot().railController.isFullyLoadedRecursive();
    }

    protected boolean canObjectOverlap(Physical var1) {
        return true;
    }

    public int getMiningBonus(SimpleTransformableSendableObject<?> var1) {
        return this.getConfigManager().apply(StatusEffectType.MINING_BONUS_ACTIVE, super.getMiningBonus(var1));
    }

    public boolean isOverlapping() {
        Vector3f var1 = new Vector3f();
        Vector3f var2 = new Vector3f();
        Vector3f var3 = new Vector3f();
        Vector3f var4 = new Vector3f();
        Vector3f var5 = new Vector3f();
        Vector3f var6 = new Vector3f();
        var1.set((float)(this.getMinPos().x << 5), (float)(this.getMinPos().y << 5), (float)(this.getMinPos().z << 5));
        var2.set((float)(this.getMaxPos().x << 5), (float)(this.getMaxPos().y << 5), (float)(this.getMaxPos().z << 5));
        AabbUtil2.transformAabb(var1, var2, 100.0F, this.getWorldTransform(), var3, var4);
        Iterator var7 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

        while(true) {
            Sendable var8;
            do {
                do {
                    do {
                        if (!var7.hasNext()) {
                            return false;
                        }
                    } while(!((var8 = (Sendable)var7.next()) instanceof Physical));
                } while(var8 == this);
            } while(var8 instanceof SimpleTransformableSendableObject && ((SimpleTransformableSendableObject)var8).getSectorId() != this.getSectorId());

            Physical var9 = (Physical)var8;
            if (this.canObjectOverlap(var9)) {
                var9.getPhysicsDataContainer().getShape().getAabb(var9.getPhysicsDataContainer().getCurrentPhysicsTransform(), var5, var6);
                if (AabbUtil2.testAabbAgainstAabb2(var3, var4, var5, var6)) {
                    return true;
                }
            }
        }
    }

    public boolean isCoreOverheating() {
        return this.coreTimerStarted > 0L;
    }

    public long getCoreOverheatingTimeLeftMS(long var1) {
        long var3 = var1 - this.coreTimerStarted;
        return this.coreTimerDuration - var3;
    }

    public void stopCoreOverheating() {
        if (this.coreTimerStarted != -1L || this.coreTimerDuration != -1L) {
            System.err.println(this.getState() + " " + this + " STOPPED OVERHEATING");
        }

        this.coreTimerStarted = -1L;
        this.coreTimerDuration = -1L;
    }

    public long getCoreTimerDuration() {
        return this.coreTimerDuration;
    }

    public long getCoreTimerStarted() {
        return this.coreTimerStarted;
    }

    public void startCoreOverheating(Damager var1) {
        if (this.isOnServer()) {
            if (this.coreTimerStarted < 0L) {
                Faction var2;
                if ((var2 = this.getFaction()) != null) {
                    var2.onEntityOverheatingServer(this);
                }

                this.coreTimerStarted = System.currentTimeMillis();
                this.coreTimerDuration = 1000L * Math.min(VoidElementManager.OVERHEAT_TIMER_MAX, Math.max(VoidElementManager.OVERHEAT_TIMER_MIN + (long)((float)this.getTotalElements() * VoidElementManager.OVERHEAT_TIMER_ADDED_PER_BLOCK), VoidElementManager.OVERHEAT_TIMER_MIN));
                this.kickAllPlayersOutServer();
                if (this instanceof SegmentControllerAIInterface) {
                    ((AIGameSegmentControllerConfiguration)((SegmentControllerAIInterface)this).getAiConfiguration()).onStartOverheating(var1);
                }

                System.err.println("[SERVER] MAIN CORE STARTED DESTRUCTION [" + this.uniqueIdentifier + "] " + this.getSector(new Vector3i()) + " in " + this.coreTimerDuration / 1000L + " seconds - started " + this.coreTimerStarted + " caused by " + (var1 != null ? var1.getOwnerState() : ""));
            }

            this.railController.resetFactionForEntitiesWithoutFactionBlock(this.getFactionId());
        }

    }

    public boolean isHandleHpCondition(HpTriggerType var1) {
        return false;
    }

    public boolean hasStructureAndArmorHP() {
        return true;
    }

    public void aabbRecalcFlag() {
        this.aabbRecalcFlag = true;
    }

    public abstract boolean allowedToEdit(PlayerState var1);

    public void createConstraint(Physical var1, Physical var2, Object var3) {
    }

    public void getTransformedAABB(Vector3f var1, Vector3f var2, float var3, Vector3f var4, Vector3f var5, Transform var6) {
        var4.set(this.getSegmentBuffer().getBoundingBox().min);
        var5.set(this.getSegmentBuffer().getBoundingBox().max);
        if (this.getSegmentBuffer().getTotalNonEmptySize() == 0) {
            var4.set(0.0F, 0.0F, 0.0F);
            var5.set(0.0F, 0.0F, 0.0F);
        } else {
            if (var4.x > var5.x || var4.y > var5.y || var4.z > var5.z) {
                var4.set(0.0F, 0.0F, 0.0F);
                var5.set(0.0F, 0.0F, 0.0F);
            }

            if (var6 == null) {
                AabbUtil2.transformAabb(var4, var5, var3, this.getWorldTransform(), var1, var2);
            } else {
                AabbUtil2.transformAabb(var4, var5, var3, var6, var1, var2);
            }
        }
    }

    public void checkInitialPositionServer(Transform var1) {
    }

    public void avoid(Transform var1, boolean var2) {
        ObjectArrayList var3 = new ObjectArrayList();
        Iterator var4 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

        while(var4.hasNext()) {
            Sendable var5;
            SimpleTransformableSendableObject var7;
            if ((var5 = (Sendable)var4.next()) instanceof SimpleTransformableSendableObject && (var7 = (SimpleTransformableSendableObject)var5) != this && var7.getSectorId() == this.getSectorId()) {
                var3.add(var7);
            }
        }

        Vector3i var6;
        (var6 = new Vector3i()).set((int)var1.origin.x, (int)var1.origin.y, (int)var1.origin.z);

        for(int var8 = 30; this.checkCollision(this, var1, var2, var3, var6) != null; var8 += 30) {
            if (Universe.getRandom().nextBoolean()) {
                var6.x += Universe.getRandom().nextBoolean() ? var8 : -var8;
            }

            if (Universe.getRandom().nextBoolean()) {
                var6.y += Universe.getRandom().nextBoolean() ? var8 : -var8;
            }

            if (Universe.getRandom().nextBoolean()) {
                var6.z += Universe.getRandom().nextBoolean() ? var8 : -var8;
            }
        }

        System.err.println("[SERVER][SHIP] Collision Avoidance for: " + this + ": " + var6);
    }

    private Sendable checkCollision(SegmentController var1, Transform var2, boolean var3, List<SimpleTransformableSendableObject<?>> var4, Vector3i var5) {
        long var6 = System.currentTimeMillis();
        Vector3f var8 = new Vector3f();
        Vector3f var9 = new Vector3f();
        Vector3f var10 = new Vector3f();
        Vector3f var11 = new Vector3f();
        Vector3f var12 = new Vector3f();
        Vector3f var13 = new Vector3f();
        var2.setIdentity();
        var2.origin.set((float)var5.x, (float)var5.y, (float)var5.z);
        var8.set((float)(var1.getMinPos().x - 2 << 5), (float)(var1.getMinPos().y - 2 << 5), (float)(var1.getMinPos().z - 2 << 5));
        var9.set((float)(var1.getMaxPos().x + 2 << 5), (float)(var1.getMaxPos().y + 2 << 5), (float)(var1.getMaxPos().z + 2 << 5));
        AabbUtil2.transformAabb(var8, var9, 100.0F, var2, var12, var13);
        Iterator var14 = var4.iterator();

        while(var14.hasNext()) {
            SimpleTransformableSendableObject var15;
            if ((var15 = (SimpleTransformableSendableObject)var14.next()) instanceof Physical) {
                Object var17;
                if (var15 instanceof SegmentController) {
                    SegmentController var16 = (SegmentController)var15;
                    var10.set((float)(var16.getMinPos().x - 2 << 5), (float)(var16.getMinPos().y - 2 << 5), (float)(var16.getMinPos().z - 2 << 5));
                    var11.set((float)(var16.getMaxPos().x + 2 << 5), (float)(var16.getMaxPos().y + 2 << 5), (float)(var16.getMaxPos().z + 2 << 5));
                    var17 = var3 ? var16.getInitialTransform() : var16.getWorldTransform();
                    AabbUtil2.transformAabb(var10, var11, 100.0F, (Transform)var17, var10, var11);
                } else {
                    if (var3) {
                        var15.getInitialTransform();
                    } else {
                        var15.getPhysicsDataContainer().getCurrentPhysicsTransform();
                    }

                    var17 = null;
                    var15.getPhysicsDataContainer().getShape().getAabb(var15.getInitialTransform(), var10, var11);
                }

                if (AabbUtil2.testAabbAgainstAabb2(var12, var13, var10, var11)) {
                    long var19;
                    if ((var19 = System.currentTimeMillis() - var6) > 10L) {
                        System.err.println("[Sector] [Sector] collision test at " + var5 + " is true: trying another pos " + var19 + "ms");
                    }

                    return var15;
                }
            }
        }

        long var18;
        if ((var18 = System.currentTimeMillis() - var6) > 10L) {
            System.err.println("[Sector] No Collission: " + var18 + "ms");
        }

        return null;
    }

    public void initPhysics() {
        if (this.getPhysicsDataContainer().getObject() == null) {
            Transform var1 = this.getRemoteTransformable().getInitialTransform();
            if (this.isOnServer() && this.needsPositionCheckOnLoad) {
                this.checkInitialPositionServer(var1);
            }

            this.needsPositionCheckOnLoad = false;
            CubeShape var2 = new CubeShape(this.getSegmentBuffer());
            CubesCompoundShape var3 = new CubesCompoundShape(this);
            Transform var4;
            (var4 = new Transform()).setIdentity();
            var3.addChildShape(var4, var2);
            int var5 = var3.getChildList().size() - 1;
            this.getPhysicsDataContainer().setShapeChield((CompoundShapeChild)var3.getChildList().get(var5), var5);
            var3.recalculateLocalAabb();
            this.getPhysicsDataContainer().setShape(var3);
            this.getPhysicsDataContainer().setInitial(var1);
            RigidBody var6;
            (var6 = this.getPhysics().getBodyFromShape(var3, this.getMass(), this.getPhysicsDataContainer().initialTransform)).setUserPointer(this.getId());
            this.getPhysicsDataContainer().setObject(var6);
            this.getWorldTransform().set(var1);

            assert this.getPhysicsDataContainer().getObject() != null;
        } else {
            System.err.println("[SegmentController][WARNING] not adding initial physics object. it already exists");
        }

        this.setFlagPhysicsInit(true);
    }

    public void decTotalElements() {
        this.setTotalElements(this.totalElements - 1);
    }

    private List<String> getDataFilesListOld() {
        final String var1 = this.getUniqueIdentifier() + ".";
        FilenameFilter var4 = new FilenameFilter() {
            public boolean accept(File var1x, String var2) {
                return var2.startsWith(var1);
            }
        };
        String[] var5 = (new FileExt(GameServerState.SEGMENT_DATA_DATABASE_PATH)).list(var4);
        ObjectArrayList var2 = new ObjectArrayList();

        for(int var3 = 0; var3 < var5.length; ++var3) {
            var2.add(var5[var3]);
        }

        return var2;
    }

    private List<String> getFileNames(List<String> var1) {
        ObjectArrayList var2 = new ObjectArrayList();
        Iterator var4 = var1.iterator();

        while(var4.hasNext()) {
            String var3 = (String)var4.next();
            var2.add((new FileExt(var3)).getName());
        }

        return var2;
    }

    public void destroyPersistent() {
        assert this.isOnServer();

        GameServerState var1 = (GameServerState)this.getState();
        (new StringBuilder()).append(GameServerState.ENTITY_DATABASE_PATH).append(this.getUniqueIdentifier()).toString();

        try {
            var1.getDatabaseIndex().getTableManager().getEntityTable().removeSegmentController(this);
            FileExt var4 = new FileExt(GameServerState.ENTITY_DATABASE_PATH + this.getUniqueIdentifier() + ".ent");
            System.err.println("[SERVER][SEGMENTCONTROLLER] PERMANENTLY DELETING ENTITY: " + var4.getName());
            var4.delete();
            List var5 = SegmentDataFileUtils.getAllFiles(this.getMinPos(), this.getMaxPos(), this.getUniqueIdentifier(), this);

            assert this.getFileNames(var5).containsAll(this.getDataFilesListOld()) : this.getMinPos() + "; " + this.getMaxPos() + "\n" + this.getFileNames(var5) + ";\n\n" + this.getDataFilesListOld();

            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
                String var2 = (String)var6.next();
                FileExt var7;
                if ((var7 = new FileExt(var2)).exists()) {
                    System.err.println("[SERVER][SEGMENTCONTROLLER] PERMANENTLY DELETING ENTITY DATA: " + var7.getName() + " (exists: " + var7.exists() + ")");
                    var7.delete();
                }
            }

        } catch (SQLException var3) {
            var3.printStackTrace();
        }
    }

    public boolean existsNeighborSegment(Vector3i var1, int var2) {
        this.getNeighborSegmentPos(var1, var2, this.testPos);
        return this.segmentBuffer.containsKey(this.testPos);
    }

    public final void flagUpdateDocking() {
        this.flagCheckDocking = true;
    }

    public final void flagupdateMass() {
        if (this.getDockingController().isDocked()) {
            this.getDockingController().getDockedOn().to.getSegment().getSegmentController().flagupdateMass();
        }

        if (this.railController.isDockedAndExecuted()) {
            this.railController.getRoot().flagupdateMass();
        }

        this.flagUpdateMass = true;
        this.onMassUpdate = true;
    }

    public boolean isSegmentBufferFullyLoadedServer() {
        assert this.isOnServer();

        return ((SegmentBufferManager)this.segmentBuffer).isFullyLoaded();
    }

    public boolean isSegmentBufferFullyLoadedServerRailRec() {
        assert this.isOnServer();

        if (this.fullyLoadedRailRecChache) {
            return true;
        } else if (((SegmentBufferManager)this.segmentBuffer).isFullyLoaded() && (this.getState().getUpdateTime() - this.getTimeCreated() > 30000L || this.railController.getExpectedToDock().isEmpty())) {
            Iterator var1 = this.railController.next.iterator();

            do {
                if (!var1.hasNext()) {
                    this.fullyLoadedRailRecChache = true;
                    return true;
                }
            } while(((RailRelation)var1.next()).docked.getSegmentController().isSegmentBufferFullyLoadedServerRailRec());

            return false;
        } else {
            return false;
        }
    }

    public void readTextBlockData(Tag var1) {
        Tag[] var6 = (Tag[])var1.getValue();

        for(int var2 = 0; var2 < var6.length - 1; ++var2) {
            Tag[] var3;
            long var4 = (Long)(var3 = (Tag[])var6[var2].getValue())[0].getValue();
            String var7 = (String)var3[1].getValue();
            if (this.isLoadedFromChunk16()) {
                var4 = ElementCollection.shiftIndex4(var4, 8, 8, 8);
            }

            this.textMap.put(var4, var7);
        }

    }

    private Tag getNPCTagData() {
        ObjectArrayList var1;
        (var1 = new ObjectArrayList()).addAll(this.getAttachedAffinity());
        Tag[] var2;
        (var2 = new Tag[var1.size() + 1])[var1.size()] = FinishTag.INST;

        for(int var3 = 0; var3 < var1.size(); ++var3) {
            assert var1.get(var3) != null;

            var2[var3] = AICreature.toTagNPC((AICreature)var1.get(var3));
        }

        return new Tag(Type.STRUCT, var1.toString(), var2);
    }

    private void readNPCData(Tag var1) {
        Tag[] var5;
        if ((var5 = (Tag[])var1.getValue()).length > 1) {
            for(int var2 = 0; var2 < var5.length - 1; ++var2) {
                AICreature var3;
                try {
                    var3 = AICreature.getNPCFromTag(var5[var2], this.getState());
                    this.attachedAffinityInitial.enqueue(var3);
                } catch (CannotInstantiateAICreatureException var4) {
                    var3 = null;
                    var4.printStackTrace();
                }
            }
        }

    }

    public Vector3f getAbsoluteElementWorldPosition(Vector3i var1, Vector3f var2) {
        if (this.isOnServer()) {
            this.getAbsoluteElementWorldPositionLocal(var1, var2);
        } else {
            var2.set((float)var1.x, (float)var1.y, (float)var1.z);
            this.getWorldTransformOnClient().basis.transform(var2);
            var2.add(this.getWorldTransformOnClient().origin);
        }

        return var2;
    }

    public Vector3f getAbsoluteElementWorldPositionShifted(Vector3i var1, Vector3f var2) {
        if (this.isOnServer()) {
            this.getAbsoluteElementWorldPositionLocalShifted(var1, var2);
        } else {
            var2.set((float)(var1.x - 16), (float)(var1.y - 16), (float)(var1.z - 16));
            this.getWorldTransformOnClient().basis.transform(var2);
            var2.add(this.getWorldTransformOnClient().origin);
        }

        return var2;
    }

    public Vector3f getAbsoluteElementWorldPositionLocal(Vector3i var1, Vector3f var2) {
        var2.set((float)var1.x, (float)var1.y, (float)var1.z);
        this.getWorldTransform().basis.transform(var2);
        var2.add(this.getWorldTransform().origin);
        return var2;
    }

    public Vector3f getAbsoluteElementWorldPositionLocalShifted(Vector3i var1, Vector3f var2) {
        var2.set((float)(var1.x - 16), (float)(var1.y - 16), (float)(var1.z - 16));
        this.getWorldTransform().basis.transform(var2);
        var2.add(this.getWorldTransform().origin);
        return var2;
    }

    public Vector3f getAbsoluteElementWorldPositionShifted(int var1, int var2, int var3, int var4, Vector3f var5) {
        if (this.isOnServer()) {
            this.getAbsoluteElementWorldPositionLocalShifted(var1, var2, var3, var4, var5);
        } else {
            var5.set((float)(var1 - 16), (float)(var2 - 16), (float)(var3 - 16));
            this.getWorldTransformOnClient().basis.transform(var5);
            var5.add(this.getWorldTransformOnClient().origin);
        }

        return var5;
    }

    public Vector3f getAbsoluteElementWorldPositionLocalShifted(int var1, int var2, int var3, int var4, Vector3f var5) {
        assert this.isOnServer();

        var5.set((float)(var1 - 16), (float)(var2 - 16), (float)(var3 - 16));
        this.getWorldTransform().basis.transform(var5);
        var5.add(this.getWorldTransform().origin);
        Sector var6;
        if ((var6 = ((GameServerState)this.getState()).getUniverse().getSector(var4)) != null) {
            this.v.inT.origin.set(var5);
            SimpleTransformableSendableObject.calcWorldTransformRelative(var6.getId(), var6.pos, this.getSectorId(), this.v.inT, this.getState(), true, this.v.outT, this.v);
            var5.set(this.v.outT.origin);
        }

        return var5;
    }

    public Vector3f getAbsoluteElementWorldPosition(int var1, int var2, int var3, Vector3f var4) {
        if (this.isOnServer()) {
            this.getAbsoluteElementWorldPositionLocal(var1, var2, var3, var4);
        } else {
            var4.set((float)var1, (float)var2, (float)var3);
            this.getWorldTransformOnClient().basis.transform(var4);
            var4.add(this.getWorldTransformOnClient().origin);
        }

        return var4;
    }

    public Vector3f getAbsoluteElementWorldPositionLocal(int var1, int var2, int var3, Vector3f var4) {
        var4.set((float)var1, (float)var2, (float)var3);
        this.getWorldTransform().basis.transform(var4);
        var4.add(this.getWorldTransform().origin);
        return var4;
    }

    public void getAbsoluteSegmentWorldPositionClient(Segment var1, Vector3f var2) {
        var2.set((float)var1.pos.x, (float)var1.pos.y, (float)var1.pos.z);
        TransformTimed var3;
        (var3 = this.getWorldTransformOnClient()).basis.transform(var2);
        var2.add(var3.origin);
    }

    public BoundingBox getBoundingBox() {
        return this.getSegmentBuffer().getBoundingBox();
    }

    public Vector3f getCamForwLocal() {
        return this.camForwLocal;
    }

    public Vector3f getCamLeftLocal() {
        return this.camLeftLocal;
    }

    public Vector3f getCamUpLocal() {
        return this.camUpLocal;
    }

    public Transform getClientTransformInverse() {
        return this.clientTransformInverse;
    }

    public SegmentControllerElementCollisionChecker getCollisionChecker() {
        return this.collisionChecker;
    }

    public ControlElementMap getControlElementMap() {
        return this.controlElementMap;
    }

    public int getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(int var1) {
        if (var1 != this.creatorId) {
            this.setChangedForDb(true);
        }

        this.creatorId = var1;
    }

    public CreatorThread getCreatorThread() {
        return this.creatorThread;
    }

    public void setCreatorThread(CreatorThread var1) {
        this.creatorThread = var1;
    }

    public DockingController getDockingController() {
        return this.dockingController;
    }

    public ElementCountMap getElementClassCountMap() {
        return this.elementClassCountMap;
    }

    protected Tag getExtraTagData() {
        return new Tag(Type.BYTE, (String)null, (byte)0);
    }

    public String getLastModifier() {
        return this.lastModifier;
    }

    public void setLastModifier(String var1) {
        if (!var1.equals(this.lastModifier)) {
            this.setChangedForDb(true);
        }

        this.lastModifier = var1;
    }

    public Vector3f getLocalCamPos() {
        return this.camPosLocal;
    }

    public Vector3iSegment getMaxPos() {
        return this.maxPos;
    }

    public Vector3iSegment getMinPos() {
        return this.minPos;
    }

    public abstract void getNearestIntersectingElementPosition(Vector3f var1, Vector3f var2, Vector3i var3, float var4, BuildRemoveCallback var5, SymmetryPlanes var6, short var7, short var8, BuildHelper var9, BuildInstruction var10, Set<Segment> var11) throws IOException, InterruptedException;

    public abstract int getNearestIntersection(short var1, Vector3f var2, Vector3f var3, BuildCallback var4, int var5, boolean var6, DimensionFilter var7, Vector3i var8, int var9, float var10, SymmetryPlanes var11, BuildHelper var12, BuildInstruction var13) throws ElementPositionBlockedException, BlockedByDockedElementException, BlockNotBuildTooFast;

    public ObjectArrayFIFOQueue<SegmentPiece> getNeedsActiveUpdateClient() {
        return this.needsActiveUpdateClient;
    }

    public SegmentPiece[] getNeighborElements(Vector3i var1, short var2, SegmentPiece[] var3) throws IOException, InterruptedException {
        assert var3.length == 6;

        for(int var4 = 0; var4 < 6; ++var4) {
            this.posTmp.set(var1);
            this.posTmp.add(Element.DIRECTIONSi[var4]);
            SegmentPiece var5;
            if ((var5 = this.getSegmentBuffer().getPointUnsave(this.posTmp)) == null) {
                return null;
            }

            if (var2 != 32767 && var2 != var5.getType()) {
                var3[var4] = null;
            } else {
                var3[var4] = var5;
            }
        }

        return var3;
    }

    public Segment getNeighboringSegment(Vector3b var1, Segment var2, Vector3i var3) {
        assert var2 != null : this + ", " + this.getState() + " has null seg";

        var3.set(var2.pos);
        if (SegmentData.valid(var1.x, var1.y, var1.z)) {
            return var2;
        } else {
            int var6 = ByteUtil.divUSeg(var1.x);
            int var4 = ByteUtil.divUSeg(var1.y);
            int var5 = ByteUtil.divUSeg(var1.z);
            var3.add(var6 << 5, var4 << 5, var5 << 5);
            var1.x = (byte)ByteUtil.modUSeg(var1.x);
            var1.y = (byte)ByteUtil.modUSeg(var1.y);
            var1.z = (byte)ByteUtil.modUSeg(var1.z);
            return this.segmentBuffer.getSegmentState(var3) >= 0 ? this.segmentBuffer.get(var3) : null;
        }
    }

    public Segment getNeighboringSegmentFast(Segment var1, byte var2, byte var3, byte var4) {
        assert var1 != null : this + ", " + this.getState() + " has null seg";

        if (SegmentData.valid(var2, var3, var4)) {
            return var1;
        } else {
            int var6 = var1.pos.x + (ByteUtil.divUSeg(var2) << 5);
            int var7 = var1.pos.y + (ByteUtil.divUSeg(var3) << 5);
            int var5 = var1.pos.z + (ByteUtil.divUSeg(var4) << 5);
            return this.segmentBuffer.get(var6, var7, var5);
        }
    }

    public Vector3i getNeighboringSegmentPosUnsave(Vector3b var1, Segment var2, Vector3i var3, Vector3i var4) {
        int var5 = var1.x >> 4;
        int var6 = var1.y >> 4;
        int var7 = var1.z >> 4;
        var4.x = var2.absPos.x + var5;
        var4.y = var2.absPos.y + var6;
        var4.z = var2.absPos.z + var7;
        var3.x = var2.pos.x + (var5 << 5);
        var3.y = var2.pos.y + (var6 << 5);
        var3.z = var2.pos.z + (var7 << 5);
        var1.x = (byte)(var1.x & 15);
        var1.y = (byte)(var1.y & 15);
        var1.z = (byte)(var1.z & 15);
        return var3;
    }

    public Vector3i getNeighborSegmentPos(Vector3i var1, int var2, Vector3i var3) {
        var3.set(var1);
        switch(var2) {
            case 0:
                var3.z += 32;
                break;
            case 1:
                var3.z -= 32;
                break;
            case 2:
                var3.y += 32;
                break;
            case 3:
                var3.y -= 32;
                break;
            case 4:
                var3.x += 32;
                break;
            case 5:
                var3.x -= 32;
                break;
            default:
                assert false;
        }

        return var3;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long var1) {
        this.seed = var1;
    }

    public SegmentBufferInterface getSegmentBuffer() {
        return this.segmentBuffer;
    }

    public void setSegmentBuffer(SegmentBufferInterface var1) {
        this.segmentBuffer = var1;
    }

    public Segment getSegmentFromCache(int var1, int var2, int var3) {
        return this.segmentBuffer.get(var1, var2, var3);
    }

    public SegmentProvider getSegmentProvider() {
        return this.segmentProvider;
    }

    public String getSpawner() {
        return this.spawner;
    }

    public void setSpawner(String var1) {
        if (var1.equals(this.spawner)) {
            this.setChangedForDb(true);
        }

        this.spawner = var1;
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

    public void setTimeCreated(long var1) {
        this.timeCreated = var1;
    }

    public int getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(int var1) {
        this.totalElements = var1;
        this.flagupdateMass();
        this.flagCheckDocking = true;
    }

    public String getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }

    public void setUniqueIdentifier(String var1) {
        this.uniqueIdentifier = var1;
    }

    public boolean hasNeighborElements(Segment var1, byte var2, byte var3, byte var4) {
        int var5;
        byte var6;
        byte var7;
        byte var8;
        if (SegmentData.allNeighborsInside(var2, var3, var4)) {
            for(var5 = 0; var5 < 6; ++var5) {
                var6 = (byte)ByteUtil.modUSeg(var2 + Element.DIRECTIONSb[var5].x);
                var7 = (byte)ByteUtil.modUSeg(var3 + Element.DIRECTIONSb[var5].y);
                var8 = (byte)ByteUtil.modUSeg(var4 + Element.DIRECTIONSb[var5].z);
                if (var1.getSegmentData().containsUnsave(var6, var7, var8)) {
                    return true;
                }
            }
        } else {
            for(var5 = 0; var5 < 6; ++var5) {
                var6 = (byte)(var2 + Element.DIRECTIONSb[var5].x);
                var7 = (byte)(var3 + Element.DIRECTIONSb[var5].y);
                var8 = (byte)(var4 + Element.DIRECTIONSb[var5].z);
                Segment var9;
                if ((var9 = this.getNeighboringSegmentFast(var1, var6, var7, var8)) != null && !var9.isEmpty()) {
                    var6 = (byte)ByteUtil.modUSeg(var6);
                    var7 = (byte)ByteUtil.modUSeg(var7);
                    var8 = (byte)ByteUtil.modUSeg(var8);
                    if (var9.getSegmentData().containsUnsave(var6, var7, var8)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void incTotalElements() {
        this.setTotalElements(this.totalElements + 1);
    }

    public boolean isFlagSegmentBufferAABBUpdate() {
        return this.flagSegmentBufferAABBUpdate;
    }

    public void setFlagSegmentBufferAABBUpdate(boolean var1) {
        this.flagSegmentBufferAABBUpdate = var1;
    }

    public boolean isInbound(int var1, int var2, int var3) {
        return var1 <= this.maxPos.x && var2 <= this.maxPos.y && var3 <= this.maxPos.z && var1 >= this.minPos.x && var2 >= this.minPos.y && var3 >= this.minPos.z;
    }

    public boolean isInbound(Vector3i var1) {
        return this.isInbound(var1.x, var1.y, var1.z);
    }

    public boolean isInboundAbs(int var1, int var2, int var3) {
        var1 = ByteUtil.divSeg(var1);
        var2 = ByteUtil.divSeg(var2);
        var3 = ByteUtil.divSeg(var3);
        return var1 <= this.getMaxPos().x && var2 <= this.getMaxPos().y && var3 <= this.getMaxPos().z && var1 >= this.getMinPos().x && var2 >= this.getMinPos().y && var3 >= this.getMinPos().z;
    }

    public boolean isInboundAbs(Vector3i var1) {
        return this.isInboundAbs(var1.x, var1.y, var1.z);
    }

    public boolean isInboundCoord(int var1, Vector3i var2) {
        return var2.getCoord(var1) <= this.maxPos.getCoord(var1) && var2.getCoord(var1) >= this.minPos.getCoord(var1);
    }

    public boolean isInboundCoord(int var1, int var2) {
        return var2 <= this.maxPos.getCoord(var1) && var2 >= this.minPos.getCoord(var1);
    }

    public boolean mayActivateOnThis(SegmentController var1, SegmentPiece var2) {
        return isPublicException(var2, var1.getFactionId()) || !((FactionState)this.getState()).getFactionManager().existsFaction(this.getFactionId()) || var1.getFactionId() == this.getFactionId() || var1 instanceof PlayerControllable && !((PlayerControllable)var1).getAttachedPlayers().isEmpty() && ((PlayerState)((PlayerControllable)var1).getAttachedPlayers().get(0)).getFactionId() == this.getFactionId();
    }

    public void onAddedElementSynched(short var1, byte var2, byte var3, byte var4, byte var5, Segment var6, boolean var7, long var8, long var10, boolean var12) {
        this.hpController.onAddedElementSynched(var1);
        this.bPos.set((float)(var6.pos.x + var3 - 16), (float)(var6.pos.y + var4 - 16), (float)(var6.pos.z + var5 - 16));
        ElementInformation var15;
        float var16 = (var15 = ElementKeyMap.getInfoFast(var1)).getMass();
        Vector3f var10000 = this.centerOfMassUnweighted;
        var10000.x += this.bPos.x * var16;
        var10000 = this.centerOfMassUnweighted;
        var10000.y += this.bPos.y * var16;
        var10000 = this.centerOfMassUnweighted;
        var10000.z += this.bPos.z * var16;
        this.totalPhysicalMass += var16;
        float var17 = this.bPos.lengthSquared();
        this.j.m00 = var17;
        this.j.m01 = 0.0F;
        this.j.m02 = 0.0F;
        this.j.m10 = 0.0F;
        this.j.m11 = var17;
        this.j.m12 = 0.0F;
        this.j.m20 = 0.0F;
        this.j.m21 = 0.0F;
        this.j.m22 = var17;
        Matrix3f var19 = this.j;
        var19.m00 += this.bPos.x * -this.bPos.x;
        var19 = this.j;
        var19.m01 += this.bPos.y * -this.bPos.x;
        var19 = this.j;
        var19.m02 += this.bPos.z * -this.bPos.x;
        var19 = this.j;
        var19.m10 += this.bPos.x * -this.bPos.y;
        var19 = this.j;
        var19.m11 += this.bPos.y * -this.bPos.y;
        var19 = this.j;
        var19.m12 += this.bPos.z * -this.bPos.y;
        var19 = this.j;
        var19.m20 += this.bPos.x * -this.bPos.z;
        var19 = this.j;
        var19.m21 += this.bPos.y * -this.bPos.z;
        var19 = this.j;
        var19.m22 += this.bPos.z * -this.bPos.z;
        var19 = this.tensor;
        var19.m00 += var16 * this.j.m00;
        var19 = this.tensor;
        var19.m01 += var16 * this.j.m01;
        var19 = this.tensor;
        var19.m02 += var16 * this.j.m02;
        var19 = this.tensor;
        var19.m10 += var16 * this.j.m10;
        var19 = this.tensor;
        var19.m11 += var16 * this.j.m11;
        var19 = this.tensor;
        var19.m12 += var16 * this.j.m12;
        var19 = this.tensor;
        var19.m20 += var16 * this.j.m20;
        var19 = this.tensor;
        var19.m21 += var16 * this.j.m21;
        var19 = this.tensor;
        var19.m22 += var16 * this.j.m22;
        this.blockAdded = true;
        //INSERTED CODE
        StarLoader.fireEvent(new SegmentPieceAddEvent(this, var1,var2,var3,var4,var5,var6,var7, var8), this.isOnServer());
        ///
        if (var1 == 479) {
            long var13 = ElementCollection.getIndex4(var8, (short)var2);
            this.getTextBlocks().add(var13);
        }

        this.getElementClassCountMap().inc(var1);
        if (var15.resourceInjection == ResourceInjectionType.ORE && var2 > 0 && var2 <= 16) {
            int var18 = var2 - 1;
            this.elementClassCountMap.addOre(var18);
        }

        this.incTotalElements();
    }

    public void addFromMeta(float var1, Vector3f var2, int var3, int[] var4, int[] var5, LongArrayList var6, Matrix3f var7) {
        this.totalPhysicalMass += var1;
        this.centerOfMassUnweighted.add(var2);
        this.totalElements += var3;
        if (this.isOnServer() && this.elementClassCountMap.restrictedBlocks(var4)) {
            this.setScrap(true);
            ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{122, this.toString(), this.getSector(new Vector3i()).toString(), this.spawner != null ? this.spawner : "unknown"}, 3);
        }

        this.getElementClassCountMap().add(var4, var5);
        this.getTextBlocks().addAll(var6);
        this.tensor.add(var7);
        this.blockAdded = true;
        this.hpController.onAddedElementsSynched(var4, var5);
    }

    public void onProximity(SegmentController var1) {
        if (!this.railController.isInAnyRailRelationWith(var1) && (!this.getDockingController().isDocked() || this.getDockingController().getDockedOn().from.getSegment().getSegmentController() != var1)) {
            if (!this.getDockingController().getDockedOnThis().isEmpty()) {
                Iterator var2 = this.getDockingController().getDockedOnThis().iterator();

                while(var2.hasNext()) {
                    if (((ElementDocking)var2.next()).from.getSegment().getSegmentController() == var1) {
                        return;
                    }
                }
            }

            this.getProximityObjects().add(var1.getId());
        }
    }

    public void onRemovedElementSynched(short var1, int var2, byte var3, byte var4, byte var5, byte var6, Segment var7, boolean var8, long var9) {
        if (!this.isOnServer()) {
            this.segmentBuffer.onRemovedElementClient(var1, var2, var3, var4, var5, var7, var9);
        }

        ElementInformation var13;
        float var14 = (var13 = ElementKeyMap.getInfoFast(var1)).getMass();
        this.bPos.set((float)(var7.pos.x + var3 - 16), (float)(var7.pos.y + var4 - 16), (float)(var7.pos.z + var5 - 16));
        Vector3f var10000 = this.centerOfMassUnweighted;
        var10000.x -= this.bPos.x * var14;
        var10000 = this.centerOfMassUnweighted;
        var10000.y -= this.bPos.y * var14;
        var10000 = this.centerOfMassUnweighted;
        var10000.z -= this.bPos.z * var14;
        this.totalPhysicalMass -= var14;
        float var10 = this.bPos.lengthSquared();
        this.j.setRow(0, var10, 0.0F, 0.0F);
        this.j.setRow(1, 0.0F, var10, 0.0F);
        this.j.setRow(2, 0.0F, 0.0F, var10);
        Matrix3f var15 = this.j;
        var15.m00 += this.bPos.x * -this.bPos.x;
        var15 = this.j;
        var15.m01 += this.bPos.y * -this.bPos.x;
        var15 = this.j;
        var15.m02 += this.bPos.z * -this.bPos.x;
        var15 = this.j;
        var15.m10 += this.bPos.x * -this.bPos.y;
        var15 = this.j;
        var15.m11 += this.bPos.y * -this.bPos.y;
        var15 = this.j;
        var15.m12 += this.bPos.z * -this.bPos.y;
        var15 = this.j;
        var15.m20 += this.bPos.x * -this.bPos.z;
        var15 = this.j;
        var15.m21 += this.bPos.y * -this.bPos.z;
        var15 = this.j;
        var15.m22 += this.bPos.z * -this.bPos.z;
        var15 = this.tensor;
        var15.m00 -= var14 * this.j.m00;
        var15 = this.tensor;
        var15.m01 -= var14 * this.j.m01;
        var15 = this.tensor;
        var15.m02 -= var14 * this.j.m02;
        var15 = this.tensor;
        var15.m10 -= var14 * this.j.m10;
        var15 = this.tensor;
        var15.m11 -= var14 * this.j.m11;
        var15 = this.tensor;
        var15.m12 -= var14 * this.j.m12;
        var15 = this.tensor;
        var15.m20 -= var14 * this.j.m20;
        var15 = this.tensor;
        var15.m21 -= var14 * this.j.m21;
        var15 = this.tensor;
        var15.m22 -= var14 * this.j.m22;
        this.hpController.onRemovedElementSynched(var1);
        this.blockRemoved = true;
        this.getElementClassCountMap().dec(var1);
        if (var13.resourceInjection == ResourceInjectionType.ORE && var6 > 0 && var6 <= 16) {
            var2 = var6 - 1;
            this.elementClassCountMap.decOre(var2);
        }

        this.decTotalElements();
        if (var7.isEmpty()) {
            this.getSegmentBuffer().onSegmentBecameEmpty(var7);
        }
        //INSERTED CODE @???
        StarLoader.fireEvent(new SegmentPieceRemoveEvent(var1, var2, var3, var4, var5, var6, var7, var8), this.isOnServer());
        ///

        if (!var8) {
            this.getControlElementMap().onRemoveElement(var7.getAbsoluteIndex(var3, var4, var5), var1);
            if (this.isOnServer() && var1 == 291) {
                System.err.println("[SERVER] FACTION BLOCK REMOVED FROM " + this + "; resetting faction !!!!!!!!!!!!!!");
                this.railController.resetFactionForEntitiesWithoutFactionBlock(this.getFactionId());
            }

            if (var1 == 479 || var1 == 670) {
                var7.getAbsoluteIndex(var3, var4, var5);
                long var11 = ElementCollection.getIndex4(var7.getAbsoluteIndex(var3, var4, var5), var1 == 670 ? 670 : (short)var6);
                this.getTextBlocks().remove(var11);
                this.getTextMap().remove(var11);
            }
        }

    }

    public boolean hasAnyReactors() {
        return this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer().hasAnyReactors();
    }

    public boolean isInAdminInvisibility() {
        if (this.railController.isRoot()) {
            return super.isInAdminInvisibility();
        } else {
            return super.isInAdminInvisibility() || this.railController.getRoot().isInAdminInvisibility();
        }
    }

    protected void readExtraTagData(Tag var1) {
    }

    public PlayerState isInGodmode() {
        if (this instanceof PlayerControllable) {
            Iterator var1 = ((PlayerControllable)this).getAttachedPlayers().iterator();

            while(var1.hasNext()) {
                PlayerState var2;
                if ((var2 = (PlayerState)var1.next()).isGodMode()) {
                    return var2;
                }
            }
        }

        if (this.getDockingController().isDocked()) {
            return this.getDockingController().getDockedOn().to.getSegment().getSegmentController().isInGodmode();
        } else {
            return this.railController.isDockedAndExecuted() ? this.railController.previous.rail.getSegmentController().isInGodmode() : null;
        }
    }

    public void resetTotalElements() {
        this.setTotalElements(0);
        this.totalPhysicalMass = 0.0F;
        this.centerOfMassUnweighted.set(0.0F, 0.0F, 0.0F);
        this.getPhysicsDataContainer().lastCenter.set(0.0F, 0.0F, 0.0F);
    }

    public void setCurrentBlockController(SegmentPiece var1, SegmentPiece var2, long var3) throws CannotBeControlledException {
        this.setCurrentBlockController(var1, var2, var3, 0);
    }

    public boolean isSegmentController() {
        return true;
    }

    public String getName() {
        return this.toNiceString();
    }

    public void setCurrentBlockController(SegmentPiece var1, SegmentPiece var2, long var3, int var5) throws CannotBeControlledException {
        if ((var2 = this.getSegmentBuffer().getPointUnsave(var3, var2)) != null && var1 != null) {
            var1.refresh();

            assert var1.getSegment().getSegmentData() != null : var1;

            assert var2.getSegment().getSegmentData() != null : "Exception: " + this.getState() + " " + this + " controlled piece is null " + var3 + "; " + var2.getSegment();

            if (var2.getSegment().getSegmentData() == null) {
                System.err.println("Exception: " + this.getState() + " " + this + " controlled piece SegmentData is null " + var3 + "; " + var2.getSegment());
                return;
            }

            short var6 = var1.getType();
            short var7 = var2.getType();

            assert var6 > 0 : var7 + "; " + var1 + "; " + this;

            assert var7 > 0 : var7 + "; " + var2 + "; " + this;

            if (var7 <= 0 || var6 <= 0) {
                System.err.println("Exception: set controller for invalid type: " + var6 + " for: " + var1 + " -> " + var3 + "; ");
                System.err.println("Exception (Cnt): set controller for invalid type: " + var7 + " for: " + var1 + " -> " + var3 + "; ");
                return;
            }

            if (var1.getAbsoluteIndex() == var3) {
                if (!this.isOnServer()) {
                    ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_6, 0.0F);
                }

                return;
            }

            ElementInformation var11 = ElementKeyMap.getInfo(var6);
            ElementInformation var8 = ElementKeyMap.getInfo(var7);
            if (this instanceof ManagedSegmentController && !((ManagedSegmentController)this).getManagerContainer().canBeControlled(var6, var7)) {
                System.err.println("[SEGMENTCONTROLLER] This cant be controlled by " + this + ": " + ElementKeyMap.toString(var6) + " -> " + ElementKeyMap.toString(var7) + "; ManagerContainer.canBeController(from, to) failed.");
                throw new CannotBeControlledException(var11, var8);
            }

            if (var8.getControlledBy().contains(var6) || var11.controlsAll() || var11.isCombiConnectAny(var7) || ElementInformation.canBeControlled(var6, var7)) {
                ControlElementMap var12 = var1.getSegmentController().getControlElementMap();
                long var9 = var1.getAbsoluteIndex();
                if (var5 == 0) {
                    var12.switchControllerForElement(var9, var3, var7);
                    return;
                }

                if (var5 == 1) {
                    if (!var12.isControlling(var9, var3, var7)) {
                        var12.switchControllerForElement(var9, var3, var7);
                        return;
                    }
                } else if (var12.isControlling(var9, var3, var7)) {
                    var12.switchControllerForElement(var9, var3, var7);
                }

                return;
            }

            if (this.lastControlledMsgUpdate != this.getState().getUpdateTime()) {
                System.err.println(this.getState() + " [SegmentController][setCurrentBlockController]  " + ElementKeyMap.toString(var7) + " CANNOT BE CONTROLLED BY " + ElementKeyMap.toString(var6) + "; controlled by: " + var8.getControlledBy() + "; controlling " + ElementKeyMap.getInfo(var6).getControlling() + "; " + ElementKeyMap.getInfo(var6) + ":  -> " + var3 + " (message will display only once per update)");
                this.lastControlledMsgUpdate = this.getState().getUpdateTime();
            }

            if (!this.isOnServer() && var8.getControlledBy().size() > 0) {
                ((GameClientState)this.getState()).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_0, new Object[]{ElementKeyMap.getInfo(var7).getName(), ElementKeyMap.getInfo(var6).getName()}), 0.0F);
            }
        }

    }

    public abstract void startCreatorThread();

    public Tag getTextBlockTag() {
        return Tag.listToTagStruct(this.textMap, (String)null);
    }

    public DebugLine[] getCenterOfMassCross() {
        assert !this.isOnServer();

        return DebugLine.getCross(this.getClientTransform(), this.getPhysicsDataContainer().lastCenter, this.getBoundingBox().getSize() / 2.0F, this.getBoundingBox().getSize() / 2.0F, this.getBoundingBox().getSize() / 2.0F, false);
    }

    public void drawDebugTransform() {
        Object var1;
        if (this.isOnServer() && GameClientState.staticSector == this.getSectorId()) {
            var1 = this.getWorldTransform();
        } else {
            var1 = this.getClientTransform();
        }

        Vector3f var2 = GlUtil.getUpVector(new Vector3f(), (Transform)var1);
        Vector3f var3 = GlUtil.getRightVector(new Vector3f(), (Transform)var1);
        Vector3f var4 = GlUtil.getForwardVector(new Vector3f(), (Transform)var1);
        Vector3f var5;
        if (this.isOnServer()) {
            (var5 = new Vector3f()).add(var2);
            var5.add(var3);
            var5.add(var4);
            var5.normalize();
            var5.scale(3.0F);
            var5.add(((Transform)var1).origin);
            DebugLine var6 = new DebugLine(new Vector3f(((Transform)var1).origin), var5, new Vector4f(1.0F, 0.0F, 1.0F, 1.0F));
            if (!DebugDrawer.lines.contains(var6)) {
                DebugDrawer.lines.add(var6);
            }
        }

        var5 = new Vector3f(var2);
        Vector3f var14 = new Vector3f(var3);
        Vector3f var7 = new Vector3f(var4);
        var2.set(0.0F, 0.0F, 0.0F);
        var3.set(0.0F, 0.0F, 0.0F);
        var4.set(0.0F, 0.0F, 0.0F);
        var5.scale(3.0F);
        var14.scale(3.0F);
        var7.scale(3.0F);
        var2.add(((Transform)var1).origin);
        var3.add(((Transform)var1).origin);
        var4.add(((Transform)var1).origin);
        var5.add(((Transform)var1).origin);
        var14.add(((Transform)var1).origin);
        var7.add(((Transform)var1).origin);
        Vector4f var10 = new Vector4f(0.0F, 1.0F, 0.0F, 1.0F);
        Vector4f var8 = new Vector4f(1.0F, 0.0F, 0.0F, 1.0F);
        Vector4f var9 = new Vector4f(0.0F, 0.0F, 1.0F, 1.0F);
        if (this.isOnServer()) {
            var10.x += 0.8F;
            var8.z += 0.8F;
            var9.y += 0.8F;
        }

        DebugLine var11 = new DebugLine(var2, var5, var10);
        DebugLine var12 = new DebugLine(var3, var14, var8);
        DebugLine var13 = new DebugLine(var4, var7, var9);
        if (!DebugDrawer.lines.contains(var11)) {
            DebugDrawer.lines.add(var11);
        }

        if (!DebugDrawer.lines.contains(var12)) {
            DebugDrawer.lines.add(var12);
        }

        if (!DebugDrawer.lines.contains(var13)) {
            DebugDrawer.lines.add(var13);
        }

    }

    private void handleTriggers(long var1) {
        if (!this.triggers.isEmpty()) {
            ObjectIterator var3 = this.triggers.iterator();

            while(var3.hasNext()) {
                ActivationTrigger var4;
                if (!(var4 = (ActivationTrigger)var3.next()).fired) {
                    try {
                        this.fireActivation(var4);
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }

                    var4.fired = true;
                }

                if (var1 - var4.ping > 700L) {
                    var3.remove();
                }
            }
        }

    }

    public abstract InterEffectSet getAttackEffectSet(long var1, DamageDealerType var3);

    private void handleSlowdown(long var1) {
        if (var1 - this.getLastSlowdown() >= 5000L && !this.isImmediateStuck()) {
            this.resetSlowdownStart();
        } else if (this.getSlowdownStart() > 0L && var1 - this.getSlowdownStart() > 10000L || this.isImmediateStuck()) {
            System.err.println("[SEGCON] " + this.getState() + " stuck physics detected on " + this);
            if (this.isOnServer()) {
                if (this.isImmediateStuck()) {
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{123, this}, 3);
                } else {
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{124, this}, 3);
                }
            }

            this.warpOutOfCollision();
            this.getPhysicsDataContainer().updatePhysical(this.getState().getUpdateTime());
            this.resetSlowdownStart();
            this.setImmediateStuck(false);
            return;
        }

    }

    public void destroy() {
        System.out.println("[SEGMENTCONTROLLER] ENTITY " + this + " HAS BEEN DESTROYED... ");
        Faction var1;
        if ((var1 = this.getFaction()) != null) {
            var1.onEntityDestroyedServer(this);
        }

        this.markForPermanentDelete(true);
        this.setMarkedForDeleteVolatile(true);
    }

    private void updateCoreOverheating(Timer var1, long var2) {
        if (this.isCoreOverheating()) {
            long var4 = var2 - this.coreTimerStarted;
            long var6 = this.coreTimerDuration - var4;
            int var13;
            if (this.isOnServer() && !ServerConfig.USE_STRUCTURE_HP.isOn() && var4 > 1000L && this instanceof Ship && ((Ship)this).getAttachedPlayers().size() > 0) {
                this.stopCoreOverheating();
                SegmentPiece var11;
                if ((var11 = this.getSegmentBuffer().getPointUnsave(Ship.core)) != null) {
                    var13 = var11.getInfoIndex();
                    short var3 = var11.getSegment().getSegmentData().getHitpointsByte(var13);

                    try {
                        var11.getSegment().getSegmentData().setHitpointsByte(var13, (short)Math.min(127, var3 + 1));
                    } catch (SegmentDataWriteException var9) {
                        SegmentDataWriteException.replaceData(var11.getSegment());

                        try {
                            var11.getSegment().getSegmentData().setHitpointsByte(var13, (short)Math.min(127, var3 + 1));
                        } catch (SegmentDataWriteException var8) {
                            throw new RuntimeException(var8);
                        }
                    }

                    this.sendBlockMod(new RemoteSegmentPiece(var11, this.isOnServer()));
                }
            } else if (var4 > this.coreTimerDuration) {
                if (this.isOnServer()) {
                    System.err.println("[SERVER][DESTROY] CORE OVERHEATED COMPLETELY: KILLING ALL SHIP CREW " + this);
                    if (this instanceof PlayerControllable) {
                        List var10 = ((PlayerControllable)this).getAttachedPlayers();

                        for(var13 = 0; var13 < var10.size(); ++var13) {
                            ((PlayerState)var10.get(var13)).handleServerHealthAndCheckAliveOnServer(0.0F, this);
                        }
                    }

                    this.railController.undockAllServer();
                    this.destroy();
                }

                this.coreTimerStarted = -1L;
                this.coreTimerDuration = -1L;
            }

            if (!this.isOnServer()) {
                float var12 = 3.0E-5F;
                if (var6 < 4000L) {
                    var12 = 0.1F;
                } else if (var6 < 30000L) {
                    var12 = 0.02F;
                } else if (var6 < 120000L) {
                    var12 = 0.003F;
                } else if (var6 < 240000L) {
                    var12 = 5.0E-4F;
                }

                if (this.isClientOwnObject()) {
                    ((GameClientState)this.getState()).getController().showBigTitleMessage("overheat", "SYSTEMS OVERHEATING\n" + StringTools.formatTimeFromMS(var6) + " until destruction. Reboot Systems to Stop ['" + KeyboardMappings.REBOOT_SYSTEMS.getKeyChar() + "']", 0.0F);
                }

                if (Math.random() < (double)var12) {
                    GameClientState var14 = (GameClientState)this.getState();
                    Vector3f var16 = new Vector3f();
                    this.getSegmentBuffer().getBoundingBox().calculateHalfSize(var16);
                    float var15 = var16.length();
                    Vector3f var17 = new Vector3f((float)(Math.random() - 0.5D), (float)(Math.random() - 0.5D), (float)(Math.random() - 0.5D));

                    while(var17.lengthSquared() == 0.0F) {
                        var17.set((float)(Math.random() - 0.5D), (float)(Math.random() - 0.5D), (float)(Math.random() - 0.5D));
                    }

                    var17.normalize();
                    var17.scale((float)((double)var15 * (1.0D + Math.random())));
                    var16.set(this.getWorldTransform().origin);
                    var16.add(var17);
                    var14.getWorldDrawer().getExplosionDrawer().addExplosion(var16, (float)(2.0D + Math.random() * 40.0D));
                }
            }
        }

    }

    protected abstract void fireActivation(ActivationTrigger var1) throws IOException;

    public boolean updateMassServer() {
        return true;
    }

    private void warpOutOfCollision() {
        if (this.isOnServer() && this.getMass() > 0.0F) {
            Vector3f var1 = new Vector3f();
            Vector3f var2 = new Vector3f();
            Transform var3 = new Transform(this.getWorldTransform());
            boolean var4 = false;

            do {
                this.getTransformedAABB(var1, var2, 1.0F, new Vector3f(), new Vector3f(), (Transform)null);
                var4 = false;
                Iterator var5 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                while(var5.hasNext()) {
                    Sendable var6;
                    SimpleTransformableSendableObject var10;
                    if ((var6 = (Sendable)var5.next()) instanceof SimpleTransformableSendableObject && (var10 = (SimpleTransformableSendableObject)var6) != this && var10.getSectorId() == this.getSectorId() && !var10.isHidden()) {
                        Vector3f var7 = new Vector3f();
                        Vector3f var8 = new Vector3f();
                        var10.calcWorldTransformRelative(this.getSectorId(), ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId()).pos);
                        var10.getTransformedAABB(var7, var8, 1.0F, new Vector3f(), new Vector3f(), var10.getClientTransform());
                        if (AabbUtil2.testAabbAgainstAabb2(var1, var2, var7, var8)) {
                            var4 = true;
                            break;
                        }
                    }
                }

                if (var4) {
                    Vector3f var10000 = this.getWorldTransform().origin;
                    var10000.y += 20.0F;
                }
            } while(var4);

            System.err.println("[SEREVR][SEGMENTCONTROLLER] WARNING: COLLISION RECOVER: " + this + " warped from " + var3.origin + " to " + this.getWorldTransform().origin);
            Transform var9 = new Transform(this.getWorldTransform());
            this.getWorldTransform().set(var3);
            this.warpTransformable(var9, true, false, (LocalSectorTransition)null);
        }

    }

    public void kickAllPlayersOutServer() {
        this.kickPlayerOutServer((PlayerState)null);
    }

    public void kickPlayerOutServer(PlayerState var1) {
        assert this.isOnServer();

        if (this instanceof PlayerControllable) {
            Iterator var2 = ((PlayerControllable)this).getAttachedPlayers().iterator();

            while(true) {
                PlayerState var3;
                do {
                    if (!var2.hasNext()) {
                        return;
                    }

                    var3 = (PlayerState)var2.next();
                } while(var1 != null && var3 != var1);

                var3.getControllerState().forcePlayerOutOfSegmentControllers();
            }
        }
    }

    public abstract int writeAllBufferedSegmentsToDatabase(boolean var1, boolean var2, boolean var3) throws IOException;

    public boolean isNewlyCreated() {
        return this.newlyCreated;
    }

    public void onSegmentAddedSynchronized(Segment var1) {
    }

    public ObjectOpenHashSet<ActivationTrigger> getTriggers() {
        return this.triggers;
    }

    public boolean checkClientLoadedOverlap(PlayerCharacter var1) {
        boolean var2 = true;
        Vector3f var3 = new Vector3f((float)(this.minPos.x << 5), (float)(this.minPos.y << 5), (float)(this.minPos.z << 5));
        Vector3f var4 = new Vector3f((float)(this.maxPos.x << 5), (float)(this.maxPos.y << 5), (float)(this.maxPos.z << 5));

        assert var3.x <= var4.x : var3 + "; " + var4;

        assert var3.y <= var4.y : var3 + "; " + var4;

        assert var3.z <= var4.z : var3 + "; " + var4;

        Vector3f var5 = new Vector3f();
        Vector3f var6 = new Vector3f();
        Vector3f var7 = new Vector3f();
        Vector3f var8 = new Vector3f();
        AabbUtil2.transformAabb(var3, var4, 0.0F, this.getWorldTransformOnClient(), var5, var6);
        var1.getPhysicsDataContainer().getShape().getAabb(var1.getWorldTransformOnClient(), var7, var6);
        if (AabbUtil2.testAabbAgainstAabb2(var5, var6, var7, var6)) {
            Transform var12 = new Transform();
            Transform var14;
            (var14 = new Transform(this.getWorldTransformOnClient())).inverse();
            var12.set(var14);
            var12.mul(var1.getWorldTransformOnClient());
            var1.getPhysicsDataContainer().getShape().getAabb(var12, var7, var8);
            BoundingBox var9 = new BoundingBox(var3, var4);
            var9 = (new BoundingBox(var7, var8)).getIntersection(var9, new BoundingBox());
            Vector3i var11 = new Vector3i();
            Vector3i var13 = new Vector3i();
            if (var9 != null && var9.isValid()) {
                var11.x = ByteUtil.divSeg((int)(var9.min.x - 16.0F)) << 5;
                var11.y = ByteUtil.divSeg((int)(var9.min.y - 16.0F)) << 5;
                var11.z = ByteUtil.divSeg((int)(var9.min.z - 16.0F)) << 5;
                var13.x = FastMath.fastCeil((var9.max.x + 16.0F) / 32.0F) << 5;
                var13.y = FastMath.fastCeil((var9.max.y + 16.0F) / 32.0F) << 5;
                var13.z = FastMath.fastCeil((var9.max.z + 16.0F) / 32.0F) << 5;

                for(int var10 = var11.z; var10 < var13.z; var10 += 32) {
                    for(int var15 = var11.y; var15 < var13.y; var15 += 32) {
                        for(int var16 = var11.x; var16 < var13.x; var16 += 32) {
                            if (this.getSegmentBuffer().getSegmentState(var16, var15, var10) == -2) {
                                if (!((ClientSegmentProvider)this.getSegmentProvider()).existsOrIsInRequest(var16, var15, var10)) {
                                    ((ClientSegmentProvider)this.getSegmentProvider()).enqueueHightPrio(var16, var15, var10, false);
                                }

                                var1.waitingForToSpawn.add(ElementCollection.getIndex(var16, var15, var10));
                                var2 = false;
                            }
                        }
                    }
                }
            }
        }

        return var2;
    }

    public RigidBody getPhysicsObject() {
        if (this.getPhysicsDataContainer().getObject() != null && this.getPhysicsDataContainer().getObject() instanceof RigidBody) {
            return (RigidBody)this.getPhysicsDataContainer().getObject();
        } else {
            RigidBody var1;
            return this.getDockingController().isDocked() && (var1 = this.getDockingController().getDockedOn().to.getSegment().getSegmentController().getPhysicsObject()) != null ? var1 : null;
        }
    }

    public boolean isDocked() {
        if (this.getDockingController().isDocked()) {
            return true;
        } else {
            return this.railController.isDockedAndExecuted();
        }
    }

    public LongArrayList getTextBlocks() {
        return this.textBlocks;
    }

    public Long2ObjectOpenHashMap<String> getTextMap() {
        return this.textMap;
    }

    public void popupOwnClientMessage(String var1, int var2) {
        if (this.isClientOwnObject()) {
            GameClientController var3 = ((GameClientState)this.getState()).getController();
            switch(var2) {
                case 0:
                    var3.getState().chat(var3.getState().getChat(), var1, "[MESSAGE]", false);
                    return;
                case 1:
                    var3.popupInfoTextMessage(var1, 0.0F);
                    return;
                case 2:
                    var3.popupGameTextMessage(var1, 0.0F);
                    return;
                case 3:
                    var3.popupAlertTextMessage(var1, 0.0F);
                    return;
                default:
                    assert false;
            }
        }

    }

    public void popupOwnClientMessage(String var1, String var2, int var3) {
        if (this.isClientOwnObject()) {
            GameClientController var4 = ((GameClientState)this.getState()).getController();
            switch(var3) {
                case 0:
                    var4.getState().chat(var4.getState().getChat(), var2, "[MESSAGE]", false);
                    return;
                case 1:
                    var4.popupInfoTextMessage(var2, var1, 0.0F);
                    return;
                case 2:
                    var4.popupGameTextMessage(var2, var1, 0.0F);
                    return;
                case 3:
                    var4.popupAlertTextMessage(var2, var1, 0.0F);
                    return;
                default:
                    assert false;
            }
        }

    }

    public void scan() {
        assert this.isOnServer();

        System.err.println("[SCAN] doing scan on server");
    }

    public float getSpeedCurrent() {
        CollisionObject var1;
        return (var1 = this.getDockingController().getAbsoluteMother().getPhysicsDataContainer().getObject()) != null && var1 instanceof RigidBody ? ((RigidBody)var1).getLinearVelocity(new Vector3f()).length() : 0.0F;
    }

    public Vector3f getLinearVelocity(Vector3f var1) {
        var1.set(0.0F, 0.0F, 0.0F);
        if (this.getPhysicsDataContainer().getObject() != null) {
            var1 = ((RigidBody)this.getPhysicsDataContainer().getObject()).getLinearVelocity(var1);
        }

        return var1;
    }

    public boolean isClientOwnObject() {
        return !this.isOnServer() && ((GameClientState)this.getState()).getCurrentPlayerObject() == this;
    }

    public void cleanUpOnEntityDelete() {
        super.cleanUpOnEntityDelete();

        try {
            this.getSegmentProvider().releaseFileHandles();
        } catch (IOException var1) {
            var1.printStackTrace();
        }

        this.getCreatorThread().terminate();
    }

    public void fromTagStructure(Tag var1) {
        this.newlyCreated = false;
        if ("sc".equals(var1.getName())) {
            this.setLoadedFromChunk16(true);
        }

        Tag[] var6;
        if ((var6 = (Tag[])var1.getValue()).length > 40 && var6[40].getType() == Type.LONG) {
            this.tagVersion = var6[40].getByte();
        }

        this.setUniqueIdentifier((String)var6[0].getValue());
        Vector3i var2 = (Vector3i)var6[1].getValue();
        Vector3i var3 = (Vector3i)var6[2].getValue();
        this.getMinPos().set(var2);
        this.getMaxPos().set(var3);
        this.getDockingController().fromTagStructure(var6[3]);
        this.getControlElementMap().setLoadedFromChunk16(true);
        this.getControlElementMap().fromTagStructure(var6[4]);
        this.setRealName((String)var6[5].getValue());
        super.fromTagStructure(var6[6]);
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().fromTagStructure(var6[7]);
        }

        this.setCreatorId((Integer)var6[8].getValue());
        this.setSpawner((String)var6[9].getValue());
        this.setLastModifier((String)var6[10].getValue());
        if (var6.length > 11 && var6[11].getType() == Type.LONG && var6[11].getType() != Type.NOTHING) {
            this.seed = (Long)var6[11].getValue();
        } else {
            this.seed = Universe.getRandom().nextLong();
        }

        if (var6.length > 12 && var6[12].getType() == Type.BYTE && var6[12].getType() != Type.NOTHING) {
            if (this instanceof TransientSegmentController) {
                ((TransientSegmentController)this).setTouched((Byte)var6[12].getValue() == 1, false);
            }
        } else if (this instanceof TransientSegmentController) {
            ((TransientSegmentController)this).setTouched(true, false);
        }

        if (var6.length > 13 && var6[13].getType() != Type.FINISH && var6[13].getType() != Type.NOTHING) {
            this.readExtraTagData(var6[13]);
        }

        if (var6.length > 14 && var6[14].getType() != Type.FINISH && var6[14].getType() != Type.NOTHING) {
            this.readNPCData(var6[14]);
        }

        if (var6.length > 15 && var6[15].getType() != Type.FINISH && var6[15].getType() != Type.BYTE) {
            this.readTextBlockData(var6[15]);
        } else if (var6.length > 15 && var6[15].getType() != Type.FINISH && var6[15].getType() == Type.BYTE) {
            this.setScrap((Byte)var6[15].getValue() > 0);
        }

        if (var6.length > 16 && var6[16].getType() != Type.FINISH && var6[16].getType() == Type.BYTE) {
            this.setVulnerable((Byte)var6[16].getValue() > 0);
        }

        if (var6.length > 17 && var6[17].getType() != Type.FINISH && var6[17].getType() == Type.BYTE) {
            this.setMinable((Byte)var6[17].getValue() > 0);
        }

        byte var7 = -2;
        if (var6.length > 18 && var6[18].getType() != Type.FINISH && var6[18].getType() == Type.BYTE) {
            var7 = (Byte)var6[18].getValue();
        }

        if (var6.length > 19 && var6[19].getType() != Type.FINISH && var6[19].getType() != Type.NOTHING) {
            this.railController.fromTag(var6[19], this.isLoadedFromChunk16() ? 8 : 0, true);
        }

        if (var6.length > 20 && var6[20].getType() != Type.FINISH && var6[20].getType() != Type.NOTHING) {
            int var8 = (Integer)var6[20].getValue();
            if (this.isLoadedFromChunk16()) {
                var8 = (int)Math.ceil((double)((float)var8 / 27.0F));
            }

            ((SegmentBufferManager)this.segmentBuffer).setExpectedNonEmptySegmentsFromLoad(var8);
        }

        if (var6.length > 21 && var6[21].getType() != Type.FINISH && var6[21].getType() != Type.NOTHING) {
            this.getHpController().fromTagStructure(var6[21]);
        }

        long var4;
        if (var6.length > 22 && var6[22].getType() != Type.FINISH && var6[22].getType() != Type.NOTHING && (var4 = (Long)((Tag[])var6[22].getValue())[0].getValue()) >= 0L) {
            this.coreTimerStarted = System.currentTimeMillis();
            this.coreTimerDuration = var4;
        }

        if (var6.length > 23 && var6[23].getType() != Type.FINISH && var6[23].getType() != Type.NOTHING) {
            byte var9 = (Byte)var6[23].getValue();
            this.virtualBlueprint = var9 != 0;
        }

        if (var6.length > 24 && var6[24].getType() != Type.FINISH && var6[24].getType() == Type.STRUCT) {
            this.blueprintSegmentDataPath = (String)((Tag[])var6[24].getValue())[0].getValue();
            this.blueprintIdentifier = (String)((Tag[])var6[24].getValue())[1].getValue();
        }

        if (var6.length > 25 && var6[25].getType() != Type.FINISH) {
            this.currentOwnerLowerCase = (String)var6[25].getValue();
        }

        if (var6.length > 26 && var6[26].getType() != Type.FINISH) {
            this.lastDockerPlayerServerLowerCase = (String)var6[26].getValue();
        }

        Type var10000;
        if (var6.length > 27) {
            var6[27].getType();
            var10000 = Type.FINISH;
        }

        if (var6.length > 28 && var6[28].getType() == Type.BYTE_ARRAY) {
            this.itemsToSpawnWith = new ElementCountMap();
            this.itemsToSpawnWith.readByteArray(var6[28].getByteArray());
        }

        if (var6.length > 29 && var6[29].getType() == Type.BYTE && var6[29].getByte() != 0) {
            this.setLoadedFromChunk16(true);
        }

        if (var6.length > 30 && var6[30].getType() == Type.BYTE) {
            this.factionSetFromBlueprint = var6[30].getByte() != 0;
        }

        if (this.tagVersion <= 0 && var6.length > 31) {
            var6[31].getType();
            var10000 = Type.BYTE;
        }

        if (var6.length > 32 && var6[32].getType() == Type.LONG) {
            this.lastAsked = var6[32].getLong();
        }

        if (var6.length > 33 && var6[33].getType() == Type.LONG) {
            this.lastAllowed = var6[33].getLong();
        }

        if (var6.length > 34 && var6[34].getType() == Type.BYTE) {
            this.usedOldPowerFromTag = var6[34].getBoolean();
        } else {
            this.usedOldPowerFromTag = true;
        }

        if (var6.length > 35 && var6[35].getType() == Type.INT) {
            this.oldPowerBlocksFromBlueprint = var6[35].getInt();
        }

        if (var6.length > 36 && var6[36].getType() == Type.SERIALIZABLE && this instanceof ManagedUsableSegmentController) {
            ((ManagedUsableSegmentController)this).setBlockKillRecorder(BlockBuffer.fromTag(var6[36]));
        }

        if (var6.length > 37 && var6[37].getType() == Type.LONG) {
            this.lastEditBlocks = var6[37].getLong();
        }

        if (var6.length > 38 && var6[38].getType() == Type.LONG) {
            this.lastDamageTaken = var6[38].getLong();
        }

        if (var6.length > 39 && var6[39].getType() == Type.LONG) {
            this.lastAdminCheckFlag = var6[39].getLong();
        }

        this.setChangedForDb(false);
        if (var7 == -1 && this.currentOwnerLowerCase.trim().length() == 0) {
            this.setFactionRights((byte)-2);
        } else {
            this.setFactionRights(var7);
        }

        ((SegmentControllerHpController)this.getHpController()).checkOneHp();
    }

    public boolean hasIntegrityStructures() {
        return false;
    }

    public int getFactionId() {
        if (this.railController.isDockedAndExecuted()) {
            int var1 = this.railController.getDockedFactionId(super.getFactionId());
            return ((FactionState)this.getState()).getFactionManager().existsFaction(super.getFactionId()) && !((FactionState)this.getState()).getFactionManager().existsFaction(var1) ? super.getFactionId() : var1;
        } else {
            return super.getFactionId();
        }
    }

    public void setFactionId(int var1) {
        if (this.isOnServer() && var1 != this.getFactionId() && this.getFactionId() != 0) {
            this.setFactionRights((byte)-2);
        }

        super.setFactionId(var1);
    }

    public final int getId() {
        return this.id;
    }

    public abstract NetworkSegmentController getNetworkObject();

    public boolean isHomeBaseFor(int var1) {
        if (this.getDockingController().isDocked()) {
            return this.getDockingController().getAbsoluteMother().isHomeBaseFor(this.getDockingController().getLocalMother().getFactionId());
        } else if (this.railController.isDockedAndExecuted()) {
            return this.railController.getRoot() instanceof ShopSpaceStation ? true : this.railController.getRoot().isHomeBaseFor(var1);
        } else {
            return super.isHomeBaseFor(var1);
        }
    }

    public void onPhysicsAdd() {
        super.onPhysicsAdd();
        if (this.isOnServer()) {
            this.vServerAttachment.update();
        }

    }

    public void onPhysicsRemove() {
        super.onPhysicsRemove();
        if (this.isOnServer()) {
            this.vServerAttachment.update();
        }

    }

    public void setId(int var1) {
        this.id = var1;
    }

    public Tag toTagStructure() {
        Tag var1 = new Tag(Type.STRING, "uniqueId", this.getUniqueIdentifier());
        Tag var2 = new Tag(Type.INT, "creatoreId", this.getCreatorId());
        Tag var3 = new Tag(Type.VECTOR3i, "minPos", this.getMinPos());
        Tag var4 = new Tag(Type.VECTOR3i, "maxPos", this.getMaxPos());
        Tag var5 = new Tag(Type.STRING, "realname", this.getRealName());
        Tag var6 = this.dockingController.toTagStructure();
        Tag var7;
        if (this instanceof ManagedSegmentController) {
            var7 = ((ManagedSegmentController)this).getManagerContainer().toTagStructure();
        } else {
            var7 = new Tag(Type.BYTE, "dummy", (byte)0);
        }

        Tag var8 = this.getControlElementMap().toTagStructure();
        Tag var9 = new Tag(Type.STRING, (String)null, this.spawner != null ? this.spawner : "");
        Tag var10 = new Tag(Type.STRING, (String)null, this.lastModifier != null ? this.lastModifier : "");
        Tag var11 = new Tag(Type.LONG, (String)null, this.getSeed());
        Tag var12;
        if (this instanceof TransientSegmentController) {
            var12 = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(((TransientSegmentController)this).isTouched() ? 1 : 0)));
        } else {
            var12 = new Tag(Type.BYTE, (String)null, (byte)1);
        }

        Tag var13 = this.railController.getTag();
        Tag var14 = new Tag(Type.INT, (String)null, this.segmentBuffer.getTotalNonEmptySize());
        long var15 = -1L;
        if (this.coreTimerStarted > 0L) {
            long var17 = System.currentTimeMillis() - this.coreTimerStarted;
            var15 = this.coreTimerDuration - var17;
        }

        Tag var20 = new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.LONG, (String)null, var15), FinishTag.INST});
        Tag var18 = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.virtualBlueprint ? 1 : 0)));
        Tag var19 = this.isLoadByBlueprint() ? new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.STRING, (String)null, this.blueprintSegmentDataPath), new Tag(Type.STRING, (String)null, this.blueprintIdentifier), FinishTag.INST}) : new Tag(Type.BYTE, (String)null, (byte)1);
        return new Tag(Type.STRUCT, "s3", new Tag[]{var1, var3, var4, var6, var8, var5, super.toTagStructure(), var7, var2, var9, var10, var11, var12, this.getExtraTagData(), this.getNPCTagData(), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.isScrap() ? 1 : 0))), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.isVulnerable() ? 1 : 0))), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.isMinable() ? 1 : 0))), new Tag(Type.BYTE, (String)null, this.getFactionRights()), var13, var14, this.getHpController().toTagStructure(), var20, var18, var19, new Tag(Type.STRING, (String)null, this.currentOwnerLowerCase), new Tag(Type.STRING, (String)null, this.lastDockerPlayerServerLowerCase), new Tag(Type.BYTE, (String)null, (byte)0), this.itemsToSpawnWith != null ? new Tag(Type.BYTE_ARRAY, (String)null, this.itemsToSpawnWith.getByteArray()) : new Tag(Type.BYTE, (String)null, (byte)0), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.isSpawnedInDatabaseAsChunk16() ? 1 : 0))), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.factionSetFromBlueprint ? 1 : 0))), new Tag(Type.BYTE, (String)null, (byte)0), new Tag(Type.LONG, (String)null, this.lastAsked), new Tag(Type.LONG, (String)null, this.lastAllowed), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(!this.isUsingOldPower() && !this.usedOldPowerFromTagForcedWrite ? 0 : 1))), new Tag(Type.INT, (String)null, this.oldPowerBlocksFromBlueprint), this instanceof ManagedUsableSegmentController ? ((ManagedUsableSegmentController)this).getBlockKillRecorder().getTag() : new Tag(Type.BYTE, (String)null, (byte)0), new Tag(Type.LONG, (String)null, this.lastEditBlocks), new Tag(Type.LONG, (String)null, this.lastDamageTaken), new Tag(Type.LONG, (String)null, this.lastAdminCheckFlag), new Tag(Type.BYTE, (String)null, (byte)1), FinishTag.INST});
    }

    public boolean isGravitySource() {
        return this.personalGravitySwitch;
    }

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this, "SegmentController");
        if (EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn()) {
            this.drawDebugTransform();
        }

        boolean var2;
        if ((var2 = this.getConfigManager().apply(StatusEffectType.GRAVITY_OVERRIDE_ENTITY_SWITCH, false)) != this.personalGravitySwitch) {
            this.personalGravity.x = 0.0F;
            this.personalGravity.y = 1.0F;
            this.personalGravity.z = 0.0F;
            if (var2) {
                this.personalGravity.y = this.getConfigManager().apply(StatusEffectType.GRAVITY_OVERRIDE_ENTITY_DIR, 1.0F);
            } else {
                this.personalGravity.x = 0.0F;
                this.personalGravity.y = 0.0F;
                this.personalGravity.z = 0.0F;
            }

            this.personalGravitySwitch = var2;
            ((GravityStateInterface)this.getState()).getCurrentGravitySources().remove(this);
            if (this.isGravitySource()) {
                ((GravityStateInterface)this.getState()).getCurrentGravitySources().add(this);
            }
        }

        if (this.flagAnyDamageTakenServer && this.getState().getUpdateTime() - this.lastAttackTrigger > 5000L) {
            this.getRuleEntityManager().triggerOnAttack();
            this.flagAnyDamageTakenServer = false;
            this.lastAttackTrigger = this.getState().getUpdateTime();
        }

        float var10;
        if ((var10 = this.getConfigManager().apply(StatusEffectType.MASS_MOD, 1.0F)) != this.massMod) {
            this.massMod = var10;
            this.flagupdateMass();
        }

        super.updateLocal(var1);
        this.getEffectContainer().reset();
        this.getEffectContainer().update(this.getConfigManager());
        this.updateCoreOverheating(var1, this.getState().getUpdateTime());
        this.blockTypeSearchManager.update(var1);
        this.railController.update(var1);
        this.railController.updateChildPhysics(var1);
        if (this.blockAdded) {
            this.onAfterBlockAddedOnUpdateLocal();
            this.blockAdded = false;
        }

        if (this.blockRemoved) {
            this.onAfterBlockRemovedOnUpdateLocal();
            this.blockRemoved = false;
        }

        if (this.onMassUpdate) {
            if (this.isOnServer()) {
                this.getRuleEntityManager().triggerOnMassUpdate();
            }

            this.onMassUpdate = false;
        }

        if (!this.onFullyLoadedExcecuted && this.getSegmentBuffer().isFullyLoaded()) {
            this.onFullyLoaded();
            this.onFullyLoadedExcecuted = true;
        }

        if (this.flagLongStuck) {
            this.handleLongStuck();
            this.flagLongStuck = false;
        }

        this.handleTriggers(var1.currentTime);
        this.handleSlowdown(var1.currentTime);
        if (this.isOnServer()) {
            try {
                ((GameServerState)this.getState()).debugController.check(this);
            } catch (Exception var9) {
                if (((GameServerState)this.getState()).getTimedShutdownStart() <= 0L) {
                    var9.printStackTrace();
                    System.err.println("[SERVER] Exception: TERMINATE! SHUTTING DOWN: DEBUG OPERATION REQUESTED SHUTDOWN TO PRESERVE LOG!");
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{125}, 0);
                    ((GameServerState)this.getState()).addTimedShutdown(120);
                    this.railController.setDebugFlag(true);
                } else if (System.currentTimeMillis() - this.lastExceptionPrint > 3000L) {
                    System.err.println("(only showing 1 per 3 sec) FOLLOWUP EXCEPTION: " + var9.getMessage());
                    this.lastExceptionPrint = System.currentTimeMillis();
                    this.railController.setDebugFlag(true);
                }
            }

            if (!this.checkedRootDb && this.updateCounter > 100 && this.dbId > 0L && this instanceof Ship) {
                this.checkedRootDb = true;
                if (this.railController.isRoot()) {
                    Sector var4 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId());
                    List var5;
                    if ((var5 = ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getEntityTable().loadByDockedEntity(this.dbId)) != null && !var5.isEmpty()) {
                        int var11 = 0;
                        Iterator var3 = var5.iterator();

                        label237:
                        while(true) {
                            EntityUID var14;
                            do {
                                if (!var3.hasNext()) {
                                    break label237;
                                }

                                var14 = (EntityUID)var3.next();
                            } while(((GameServerState)this.getState()).getLocalAndRemoteObjectContainer().getDbObjects().containsKey(var14.id));

                            try {
                                ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getEntityTable().changeSectorForEntity(var14.id, var4.pos, new Vector3f(this.getWorldTransform().origin), true);
                                Sendable var15 = var4.loadEntitiy((GameServerState)this.getState(), var14);
                                ((GameServerState)this.getState()).getController().broadcastMessageAdmin(new Object[]{126, this.toNiceString()}, 3);

                                try {
                                    throw new Exception("WARNING (non critical): Docked entity of " + this + " UID: " + this.getUniqueIdentifier() + "; SECTOR: " + var4.pos + " WAS NOT LOADED PHYSICALLY. Loaded (and possibly moved) it forcefully! DOCK #" + var11 + ": " + var15 + "; UID: " + ((SegmentController)var15).getUniqueIdentifier());
                                } catch (Exception var7) {
                                    var7.printStackTrace();

                                    assert var15 instanceof SegmentController && ((SegmentController)var15).getSectorId() == this.getSectorId() : ((SegmentController)var15).getSectorId() + " != " + this.getSectorId();
                                }
                            } catch (SQLException var8) {
                                var8.printStackTrace();
                            }

                            ++var11;
                        }
                    }
                }
            }
        }

        if (this.isOnServer() && this.isVirtualBlueprint()) {
            if (this.checkVirtualDock == 0L) {
                this.checkVirtualDock = var1.currentTime;
            } else if (this.checkVirtualDock > 0L && var1.currentTime - this.checkVirtualDock > 60000L) {
                if (!this.railController.isDockedAndExecuted()) {
                    this.sendControllingPlayersServerMessage(new Object[]{127}, 3);
                    this.setMarkedForDeleteVolatile(true);
                    this.markForPermanentDelete(true);
                }

                this.checkVirtualDock = -1L;
            }
        }

        if (this.isOnServer() && this.factionSetFromBlueprint) {
            label197: {
                if (!this.railController.isDockedAndExecuted()) {
                    if (!this.railController.isFullyLoadedRecursive()) {
                        break label197;
                    }

                    if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer().getFactionBlockPos() == -9223372036854775808L && this.getFactionId() > 0 && !FactionManager.isNPCFaction(this.getFactionId())) {
                        this.railController.setFactionIdForEntitiesWithoutFactionBlock(0);
                    }
                }

                this.factionSetFromBlueprint = false;
            }
        }

        if (!this.isOnServer() && this.askForPullClient != 0L) {
            if (this.isClientOwnObject()) {
                final long var12 = this.askForPullClient;
                (new PlayerOkCancelInput("CONFIRM", (InputState)this.getState(), 300, 100, Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_11, StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_12, new Object[]{((Sendable)this.getState().getLocalAndRemoteObjectContainer().getDbObjects().get(var12)).toString()})) {
                    public void pressedOK() {
                        SegmentController.this.getNetworkObject().pullPermissionAskAnswerBuffer.add(var12);
                        this.deactivate();
                    }

                    public void cancel() {
                        SegmentController.this.getNetworkObject().pullPermissionAskAnswerBuffer.add(-var12);
                        super.cancel();
                    }

                    public void onDeactivate() {
                    }
                }).activate();
            }

            this.askForPullClient = 0L;
        }

        this.getDockingController().updateLocal(var1);

        assert this.getUniqueIdentifier() != null;

        this.updateInverseTransform();
        if (this.flagCheckDocking && !(this instanceof FloatingRock) && this.getPhysicsDataContainer().isInitialized() && var1.currentTime - this.delayDockingCheck > 2000L) {
            try {
                if (this.getDockingController().isDocked() && this.isOnServer()) {
                    ++dockingChecks;
                }

                this.getDockingController().checkDockingValid();
                this.flagCheckDocking = false;
                this.delayDockingCheck = var1.currentTime;
            } catch (CollectionNotLoadedException var6) {
                this.delayDockingCheck = var1.currentTime;
            }
        }

        if (!this.isOnServer()) {
            while(!this.getNeedsActiveUpdateClient().isEmpty()) {
                SegmentPiece var13 = (SegmentPiece)this.getNeedsActiveUpdateClient().dequeue();
                ((ManagedSegmentController)this).getManagerContainer().handleActivateBlockActivate(var13, -9223372036854775808L, !var13.isActive(), var1);
            }
        }

        this.hpController.updateLocal(var1);
        if (this.aabbRecalcFlag && this.getPhysicsDataContainer().isInitialized()) {
            if (this.railController.isDocked()) {
                this.railController.getRoot().aabbRecalcFlag();
            }

            this.flagupdateMass();
            ((CompoundShape)this.getPhysicsDataContainer().getShape()).recalculateLocalAabb();
            this.recalcBoundingSphere();
            this.aabbRecalcFlag = false;
            if (this.isOnServer()) {
                this.getRuleEntityManager().triggerOnBBUpdate();
            }
        }

        if (this.isFlagSegmentBufferAABBUpdate() && this.getPhysicsDataContainer().isInitialized()) {
            if (this.getPhysicsDataContainer().getObject() != null) {
                this.getPhysicsDataContainer().getObject().activate(true);
            }

            this.aabbRecalcFlag();
            this.setFlagSegmentBufferAABBUpdate(false);
        }

        if (!this.isOnServer() && Controller.getCamera() != null) {
            this.camLocalTmp.set(Controller.getCamera().getPos());
            this.getClientTransformInverse().set(this.getWorldTransformOnClient());
            this.getClientTransformInverse().inverse();
            this.getClientTransformInverse().transform(this.camLocalTmp);
            this.camPosLocal.set(this.camLocalTmp);
            if (Controller.getCamera() instanceof SegmentControllerCamera && ((SegmentControllerCamera)Controller.getCamera()).getSegmentController() == this) {
                this.getCamForwLocal().set(Controller.getCamera().getCachedForward());
                this.getCamLeftLocal().set(Controller.getCamera().getCachedRight());
                this.getCamLeftLocal().negate();
                this.getCamUpLocal().set(Controller.getCamera().getCachedUp());
                this.getWorldTransformInverse().basis.transform(this.getCamForwLocal());
                this.getWorldTransformInverse().basis.transform(this.getCamLeftLocal());
                this.getWorldTransformInverse().basis.transform(this.getCamUpLocal());
            }
        }

        this.segmentProvider.update(this.isOnServer() ? null : ((GameClientState)this.getState()).getController().getCreatorThreadController().clientQueueManager);
        this.getPhysicsState().handleNextPhysicsSubstep(0.0F);
        this.getSegmentBuffer().update();
        this.lastSector = this.getSectorId();
        ++this.updateCounter;
        this.getState().getDebugTimer().end(this, "SegmentController");
    }

    private void onAfterBlockAddedOnUpdateLocal() {
        if (this.isOnServer()) {
            this.getRuleEntityManager().triggerOnBlockBuild();
        }

    }

    private void onAfterBlockRemovedOnUpdateLocal() {
        if (this.isOnServer()) {
            this.getRuleEntityManager().triggerOnRemoveBlock();
        }

    }

    public void updateInverseTransform() {
        this.getWorldTransformInverse().set(this.getWorldTransform());
        this.getWorldTransformInverse().inverse();
    }

    private void recalcBoundingSphere() {
        this.getBoundingSphere().radius = 0.0F;
        this.getBoundingSphereTotal().radius = 0.0F;
        this.getBoundingSphere().setFrom(this.getSegmentBuffer().getBoundingBox());
        if (this.railController.isRoot()) {
            this.railController.calcBoundingSphereTotal(this.getBoundingSphereTotal());
        }

    }

    protected void onFullyLoaded() {
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().onFullyLoaded();
        }

    }

    protected boolean addToPhysicsOnInit() {
        return !this.railController.hasActiveDockingRequest();
    }

    public void onSmootherSet(Transform var1) {
        this.getDockingController().onSmootherSet(var1);
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String var1) {
        if (!var1.equals(this.realName)) {
            this.setChangedForDb(true);
        }

        this.realName = var1;
    }

    public boolean isPhysicalForDamage() {
        return !this.isVirtualBlueprint();
    }

    public void executeGraphicalEffectClient(byte var1) {
        assert !this.isOnServer();

        GameClientState var2;
        if ((var2 = (GameClientState)this.getState()).getWorldDrawer() != null) {
            var2.getWorldDrawer().getSegmentControllerEffectDrawer().startEffect(this, var1);
        }

    }

    public String getInfo() {
        return "LoadedEntity [uid=" + this.getUniqueIdentifier() + ", type=" + this.getType().name() + ", seed=" + this.seed + ", lastModifier=" + this.lastModifier + ", spawner=" + this.spawner + ", realName=" + this.realName + ", touched=" + (this instanceof TransientSegmentController ? ((TransientSegmentController)this).isTouched() : "true") + ", faction=" + this.getFactionId() + ", pos=" + this.getWorldTransform().origin + ", minPos=" + this.minPos + ", maxPos=" + this.maxPos + ", creatorID=" + this.getCreatorId() + ", emptyObject=" + this.isEmptyOnServer() + "]";
    }

    public boolean isChainedToSendFromClient() {
        return this.railController.isChainSendFromClient();
    }

    public boolean isVulnerable() {
        return this.vulnerable;
    }

    public void setVulnerable(boolean var1) {
        this.vulnerable = var1;
    }

    public boolean isMinable() {
        return this.minable;
    }

    public void setMinable(boolean var1) {
        this.minable = var1;
    }

    public byte getFactionRights() {
        return this.factionRights;
    }

    public byte getOwnerFactionRights() {
        AbstractOwnerState var1;
        return (var1 = this.getOwnerState()) != null ? var1.getFactionRights() : this.factionRights;
    }

    public void setFactionRights(byte var1) {
        this.factionRights = var1;
    }

    public abstract boolean isEmptyOnServer();

    public abstract void sendBlockActivation(long var1);

    public abstract void sendBlockMod(RemoteSegmentPiece var1);

    public abstract void sendBeamLatchOn(long var1, int var3, long var4);

    public abstract void sendBlockHpByte(int var1, int var2, int var3, short var4);

    public abstract void sendBlockSalvage(int var1, int var2, int var3);

    public abstract void sendBlockKill(SegmentPiece var1);

    public abstract void sendBlockHpByte(SegmentPiece var1, short var2);

    public abstract void sendBlockServerMessage(ServerMessage var1);

    public abstract void sendBlockSalvage(SegmentPiece var1);

    public void drawPosition(Vector3i var1, float var2, Vector4f var3) {
        DebugBox var4 = new DebugBox(new Vector3f((float)(var1.x - 16) - 0.5F - var2, (float)(var1.y - 16) - 0.5F - var2, (float)(var1.z - 16) - 0.5F - var2), new Vector3f((float)(var1.x - 16) + 0.5F + var2, (float)(var1.y - 16) + 0.5F + var2, (float)(var1.z - 16) + 0.5F + var2), this.isOnServer() ? this.getWorldTransform() : this.getWorldTransformOnClient(), var3.x, var3.y, var3.z, var3.w);
        DebugDrawer.boxes.add(var4);
    }

    public SlotAssignment getSlotAssignment() {
        return this.slotAssignment;
    }

    public boolean isCloakedFor(SimpleTransformableSendableObject<?> var1) {
        return false;
    }

    public boolean isJammingFor(SimpleTransformableSendableObject<?> var1) {
        return false;
    }

    public Vector3f getCenterOfMassUnweighted() {
        return this.centerOfMassUnweighted;
    }

    public float getTotalPhysicalMass() {
        return this.getConfigManager().apply(StatusEffectType.MASS_MOD, this.totalPhysicalMass);
    }

    public SegmentControllerHpControllerInterface getHpController() {
        return this.hpController;
    }

    public boolean isRankAllowedToChangeFaction(int var1, PlayerState var2, byte var3) {
        return !((FactionState)this.getState()).getFactionManager().existsFaction(this.getFactionId()) || this.isSufficientFactionRights(var2);
    }

    public boolean isVirtualBlueprint() {
        return this.virtualBlueprint;
    }

    public void setVirtualBlueprint(boolean var1) {
        this.virtualBlueprint = var1;
    }

    public void setVirtualBlueprintRecursive(boolean var1) {
        this.virtualBlueprint = var1;
        Iterator var2 = this.railController.next.iterator();

        while(var2.hasNext()) {
            ((RailRelation)var2.next()).docked.getSegmentController().setVirtualBlueprintRecursive(var1);
        }

    }

    public void setMarkedForDeleteVolatileIncludingDocks(boolean var1) {
        this.setMarkedForDeleteVolatile(var1);
        Iterator var2 = this.railController.next.iterator();

        while(var2.hasNext()) {
            ((RailRelation)var2.next()).docked.getSegmentController().setMarkedForDeleteVolatileIncludingDocks(var1);
        }

    }

    public void setMarkedForDeletePermanentIncludingDocks(boolean var1) {
        this.setMarkedForDeleteVolatile(var1);
        this.markForPermanentDelete(var1);
        Iterator var2 = this.railController.next.iterator();

        while(var2.hasNext()) {
            ((RailRelation)var2.next()).docked.getSegmentController().setMarkedForDeletePermanentIncludingDocks(var1);
        }

    }

    public boolean checkBlockMassServerLimitOk() {
        return ((GameStateInterface)this.getState()).getGameState().isBlocksOk(this, this.getTotalElements()) && ((GameStateInterface)this.getState()).getGameState().isMassOk(this, (double)this.getMassWithoutDockIncludingStation());
    }

    public String getWriteUniqueIdentifier() {
        return this.getUniqueIdentifier();
    }

    public String getReadUniqueIdentifier() {
        return this.isLoadByBlueprint() ? this.blueprintIdentifier : this.getUniqueIdentifier();
    }

    public boolean isLoadByBlueprint() {
        return this.blueprintSegmentDataPath != null && this.blueprintIdentifier != null;
    }

    public String getBlueprintSegmentDataPath() {
        return this.blueprintSegmentDataPath;
    }

    public void sendSectorBroadcast(Object[] var1, int var2) {
        assert this.isOnServer();

        ((GameServerState)this.getState()).getController().broadcastMessageSector(var1, var2, this.getSectorId());
    }

    public void setAllTouched(boolean var1) {
        if (this instanceof TransientSegmentController && !((TransientSegmentController)this).isTouched()) {
            ((TransientSegmentController)this).setTouched(var1, false);
        }

        Iterator var2 = this.railController.next.iterator();

        while(var2.hasNext()) {
            ((RailRelation)var2.next()).docked.getSegmentController().setAllTouched(var1);
        }

    }

    public float getLinearDamping() {
        return this.getPhysics().getState().getLinearDamping();
    }

    public float getRotationalDamping() {
        return this.getPhysics().getState().getRotationalDamping();
    }

    public boolean isOwnerSpecific(PlayerState var1) {
        return this.getFactionRights() == -1 && this.currentOwnerLowerCase.equals(var1.getName().toLowerCase(Locale.ENGLISH));
    }

    public boolean isOwnerWithoutFactionCheck(PlayerState var1) {
        return this.getFactionRights() == -1 && (this.currentOwnerLowerCase.length() == 0 || this.isOwnerSpecific(var1));
    }

    public boolean isSufficientFactionRights(PlayerState var1) {
        return this.getFactionRights() == -2 || this.getFactionRights() >= 0 && var1.getFactionRights() >= this.getFactionRights() || this.isOwnerWithoutFactionCheck(var1);
    }

    public void setRankRecursive(byte var1, PlayerState var2, boolean var3) {
        this.setRankRecursive(var1, var2, var3, 0);
    }

    private void setRankRecursive(byte var1, PlayerState var2, boolean var3, int var4) {
        boolean var5 = false;
        if (var1 >= 0 && this.getFactionRights() == -1 && var4 == 0 && this.isOwnerSpecific(var2)) {
            var5 = true;
        } else if (this.getFactionRights() == -2) {
            var5 = true;
        } else if (this.getFactionRights() >= 0 && var2.getFactionRights() >= this.getFactionRights()) {
            var5 = true;
        }

        if (!this.isOwnerSpecific(var2) && this.getFactionId() != 0 && var2.getFactionId() != this.getFactionId()) {
            var5 = false;
        }

        if (var5) {
            this.setFactionRights(var1);
            this.currentOwnerLowerCase = var2.getName().toLowerCase(Locale.ENGLISH);
            if (var3) {
                var2.sendServerMessagePlayerInfo(new Object[]{128, var2.getFactionRankName(var1), var1 < 4 ? "or better " : ""});
            }
        } else if (var1 < 0 || this.getFactionRights() != -1 || var4 <= 0 || !this.isOwnerSpecific(var2)) {
            var2.sendServerMessagePlayerError(new Object[]{129, this.getName()});
        }

        if (var5) {
            Iterator var6 = this.railController.next.iterator();

            while(var6.hasNext()) {
                ((RailRelation)var6.next()).docked.getSegmentController().setRankRecursive(var1, var2, false, var4 + 1);
            }
        }

    }

    public int getTotalElementsIncRails() {
        int var1 = this.getTotalElements();

        RailRelation var3;
        for(Iterator var2 = this.railController.next.iterator(); var2.hasNext(); var1 += var3.docked.getSegmentController().getTotalElementsIncRails()) {
            var3 = (RailRelation)var2.next();
        }

        return var1;
    }

    public boolean canBeRequestedOnClient(int var1, int var2, int var3) {
        return this.isInboundAbs(var1, var2, var3);
    }

    public IntSet getProximityObjects() {
        return this.proximityObjects;
    }

    public boolean isInFleet() {
        return this.getFleet() != null;
    }

    public Fleet getFleet() {
        return ((FleetStateInterface)this.getState()).getFleetManager().getByEntity(this);
    }

    public long getDbId() {
        return this.dbId;
    }

    public void initialize() {
        super.initialize();
        this.configManager = new ConfigEntityManager(this.getDbId(), EffectEntityType.STRUCTURE, (ConfigPoolProvider)this.getState());

        try {
            this.configManager.entityName = this.toNiceString();
        } catch (Exception var1) {
            this.configManager.entityName = this.toString();
        }

        if (this.isOnServer()) {
            this.configManager.loadFromDatabase((GameServerState)this.getState());
        }

    }

    public boolean isLoadedFromChunk16() {
        return this.loadedFromChunk16;
    }

    public void setLoadedFromChunk16(boolean var1) {
        this.loadedFromChunk16 = var1;
    }

    public void sendServerMessage(Object[] var1, int var2) {
        if (this.isOnServer() && this instanceof PlayerControllable) {
            if (((PlayerControllable)this).getAttachedPlayers().size() == 0 && this.railController.isDockedAndExecuted()) {
                this.railController.getRoot().sendServerMessage(var1, var2);
                return;
            }

            Iterator var3 = ((PlayerControllable)this).getAttachedPlayers().iterator();

            while(var3.hasNext()) {
                ((PlayerState)var3.next()).sendServerMessage(new ServerMessage(var1, var2));
            }
        }

    }

    public void sendServerMessage(String var1, int var2) {
        this.sendServerMessage(new Object[]{var1}, var2);
    }

    public void sendClientMessage(String var1, int var2) {
        if (!this.isOnServer() && this instanceof PlayerControllable) {
            if (((PlayerControllable)this).getAttachedPlayers().size() == 0 && this.railController.isDockedAndExecuted()) {
                this.railController.getRoot().sendClientMessage(var1, var2);
                return;
            }

            Iterator var3 = ((PlayerControllable)this).getAttachedPlayers().iterator();

            while(var3.hasNext()) {
                if (((PlayerState)var3.next()).isClientOwnPlayer()) {
                    switch(var2) {
                        case 1:
                            ((GameClientState)this.getState()).getController().popupInfoTextMessage(var1, 0.0F);
                            return;
                        default:
                            ((GameClientState)this.getState()).getController().popupAlertTextMessage(var1, 0.0F);
                            return;
                    }
                }
            }
        }

    }

    public boolean isSpawnedInDatabaseAsChunk16() {
        return this.spawnedInDatabaseAsChunk16;
    }

    public void setSpawnedInDatabaseAsChunk16(boolean var1) {
        this.spawnedInDatabaseAsChunk16 = var1;
    }

    public void onStuck() {
        if (this.isOnServer()) {
            if (System.currentTimeMillis() - this.lastStuck > 15000L) {
                this.stuckTime = System.currentTimeMillis();
                this.stuckCount = 0;
            } else {
                this.stuckCount = (int)((long)this.stuckCount + (System.currentTimeMillis() - this.stuckTime));
                System.err.println("############### OBJECT STUCK FOR SECONDS: " + (long)this.stuckCount / 1000L);
            }

            if (this.stuckCount > 10000) {
                this.lastStuck = 0L;
                this.stuckCount = 0;
                this.flagLongStuck = true;
                return;
            }

            this.lastStuck = System.currentTimeMillis();
        }

    }

    public void saveDebugRail() {
        assert this.isOnServer();

        ((GameServerState)this.getState()).debugController.saveRail(this);
    }

    private void handleLongStuck() {
        if (this.isOnServer()) {
            System.err.println("[SERVER][PHYSICS] LAG HANDLING ############### START " + this);
            Transform var1 = new Transform(this.getWorldTransform());
            this.avoid(var1, false);
            System.err.println("[SERVER][PHYSICS] LAG HANDLING ############### WARPING " + this + " to a position without a collision: " + this.getWorldTransform().origin + " -> " + var1.origin);
            this.warpTransformable(var1, true, true, (LocalSectorTransition)null);
        }

    }

    public void registerTransientEffects(List<ConfigProviderSource> var1) {
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().registerTransientEffetcs(var1);
        }

        RemoteSector var2;
        if ((var2 = this.getRemoteSector()) != null) {
            var1.add(var2);
        }

    }

    public RemoteSector getRemoteSector() {
        Sendable var1;
        return (var1 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())) instanceof RemoteSector ? (RemoteSector)var1 : null;
    }

    public void setFactionFromBlueprint(boolean var1) {
        this.factionSetFromBlueprint = var1;
    }

    public void onRevealingAction() {
    }

    public TopLevelType getTopLevelType() {
        return TopLevelType.SEGMENT_CONTROLLER;
    }

    public boolean isAllowedToTakeItemsByRail(SegmentController var1) {
        switch(this.pullPermission) {
            case ALWAYS:
                return true;
            case FACTION:
                if (this.getFactionId() != 0 && this.getFactionId() == var1.getFactionId()) {
                    return true;
                }
            case ASK:
                if (this.lastAsked != var1.dbId) {
                    this.getNetworkObject().pullPermissionAskAnswerBuffer.add(var1.dbId);
                    this.lastAsked = var1.dbId;
                    this.railController.onUndock(new RailTrigger() {
                        public void handle(RailRelation var1) {
                            SegmentController.this.lastAsked = 0L;
                        }
                    });
                }

                if (this.lastAllowed == var1.dbId) {
                    return true;
                }

                return false;
            case NEVER:
                return false;
            default:
                throw new IllegalArgumentException("Unknown pull permission " + this.pullPermission.name());
        }
    }

    public ConfigEntityManager getConfigManager() {
        return this.configManager;
    }

    public boolean hasActiveReactors() {
        return this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer().hasActiveReactors();
    }

    public boolean isUsingPowerReactors() {
        return !this.isUsingOldPower() || this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer().isUsingPowerReactors();
    }

    public float getReconStrength() {
        return this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getScanAddOn().getActiveStrength() : 0.0F;
    }

    public float getStealthStrength() {
        return this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getStealthAddOn().getActiveStrength() : 0.0F;
    }

    public boolean hasStealth(StealthLvl var1) {
        return this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getStealthAddOn().hasStealth(var1) : false;
    }

    public boolean isUsingLocalShields() {
        return this instanceof ManagedSegmentController && ((ManagedSegmentController)this.railController.getRoot()).getManagerContainer() instanceof ShieldContainerInterface ? ((ShieldContainerInterface)((ManagedSegmentController)this.railController.getRoot()).getManagerContainer()).getShieldAddOn().isUsingLocalShields() : false;
    }

    public boolean isUsingOldPower() {
        if (!((GameStateInterface)this.getState()).getGameState().isAllowOldPowerSystem()) {
            return false;
        } else if (this.railController.getRoot() != this) {
            return this.railController.getRoot().isUsingOldPower();
        } else if ((!this.isFullyLoaded() || this.railController.hasActiveDockingRequest() || this.oldPowerBlocksFromBlueprint > 0) && (this.usedOldPowerFromTag || this.getHpController().hadOldPowerBlocks())) {
            if (this.getElementClassCountMap().get((short)2) >= this.oldPowerBlocksFromBlueprint) {
                this.oldPowerBlocksFromBlueprint = 0;
            }

            return true;
        } else {
            return this.getElementClassCountMap().get((short)2) > 0;
        }
    }

    public abstract boolean isStatic();

    public BlockTypeSearchRunnableManager getBlockTypeSearchManager() {
        return this.blockTypeSearchManager;
    }

    public float getMissileCapacity() {
        return 0.0F;
    }

    public float getMissileCapacityMax() {
        return 1.0F;
    }

    public float getRootMissileCapacity() {
        return this.railController.getRoot().getMissileCapacity();
    }

    public float getRootMissileCapacityMax() {
        return this.railController.getRoot().getMissileCapacityMax();
    }

    public void hitWithPhysicalRecoil(Vector3f var1, Vector3f var2, float var3, boolean var4) {
        assert var3 > 0.0F;

        SegmentController var5;
        CollisionObject var6;
        if ((var6 = (var5 = this.railController.getRoot()).getPhysicsDataContainer().getObject()) instanceof RigidBodySegmentController) {
            var1 = new Vector3f(var1);
            var2 = new Vector3f(var2);
            RigidBodySegmentController var7 = (RigidBodySegmentController)var6;
            if (var2.lengthSquared() > 0.0F) {
                if (this.isOnServer()) {
                    var5.getWorldTransformInverse().transform(var1);
                } else {
                    var5.getClientTransformInverse().transform(var1);
                }

                var2.normalize();
                var2.scale(var3);

                assert !Vector3fTools.isNan(var2) : var2;

                var7.applyCentralForce(var2);
                if (var4) {
                    var2.negate();
                }

                Vector3f var8;
                (var8 = new Vector3f()).cross(var1, var2);
                if ((!this.isClientOwnObject() || !(Controller.getCamera() instanceof InShipCamera) || ((InShipCamera)Controller.getCamera()).isInAdjustMode() || KeyboardMappings.FREE_CAM.isDown(this.getState())) && (!this.isOnServer() || !this.getRemoteTransformable().isSendFromClient())) {
                    var7.applyTorque(var8);
                }

                var7.hadRecoil = true;
            }

            var6.activate(true);
        }

    }

    public CollisionType getCollisionType() {
        return CollisionType.CUBE_STRUCTURE;
    }

    public void onDockingChanged(boolean var1) {
        if (this.isOnServer()) {
            this.getRuleEntityManager().triggerOnDockingChange();
        }

    }

    public float getMaxServerSpeed() {
        return this.isStatic() ? 0.0F : ((GameStateInterface)this.getState()).getGameState().getMaxGalaxySpeed();
    }

    public boolean canBeDamagedBy(Damager var1, DamageDealerType var2) {
        return true;
    }

    public void onShieldDamageServer(ShieldHitCallback var1) {
        this.onAnyDamageTakenServer(var1.getDamage(), var1.damager, var1.damageType);
    }

    public void onAnyDamageTakenServer(double var1, Damager var3, DamageDealerType var4) {
        this.lastAnyDamageTakenServer = this.getState().getUpdateTime();
        this.flagAnyDamageTakenServer = true;
    }

    static class SegmentControllerEffectSet extends InterEffectContainer {
        private SegmentControllerEffectSet() {
        }

        public InterEffectSet[] setupEffectSets() {
            InterEffectSet[] var1 = new InterEffectSet[3];

            for(int var2 = 0; var2 < var1.length; ++var2) {
                var1[var2] = new InterEffectSet();
            }

            return var1;
        }

        public InterEffectSet get(HitReceiverType var1) {
            if (var1 != HitReceiverType.BLOCK && var1 != HitReceiverType.SHIELD && var1 != HitReceiverType.ARMOR) {
                throw new RuntimeException("illegal hit received " + var1.name());
            } else if (var1 == HitReceiverType.BLOCK) {
                return this.sets[0];
            } else {
                return var1 == HitReceiverType.ARMOR ? this.sets[1] : this.sets[2];
            }
        }

        public void update(ConfigEntityManager var1) {
            this.update(var1, HitReceiverType.BLOCK);
            this.update(var1, HitReceiverType.SHIELD);
            this.update(var1, HitReceiverType.ARMOR);
        }

        private void update(ConfigEntityManager var1, HitReceiverType var2) {
            InterEffectSet var3 = this.get(var2);
            this.addGeneral(var1, var3);
            if (var2 == HitReceiverType.ARMOR) {
                this.addArmor(var1, var3);
            } else {
                if (var2 == HitReceiverType.SHIELD) {
                    this.addShield(var1, var3);
                }

            }
        }
    }

    public static enum PullPermission {
        NEVER(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_9, 0),
        ASK(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_10, 1),
        FACTION(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_13, 2),
        ALWAYS(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_SEGMENTCONTROLLER_14, 3);

        public final String desc;

        private PullPermission(String var3, int var4) {
            this.desc = var3;
        }
    }
}
