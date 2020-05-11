//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.world;

import api.listener.events.ShipJumpEngageEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SolverConstraint;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.bytes.ByteArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.TransformTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.data.SectorChange;
import org.schema.game.common.controller.FloatingRock;
import org.schema.game.common.controller.NetworkListenerEntity;
import org.schema.game.common.controller.RemoteTransformable;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitReceiverType;
import org.schema.game.common.controller.damage.effects.InterEffectContainer;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.database.DatabaseEntry;
import org.schema.game.common.controller.database.DatabaseInsertable;
import org.schema.game.common.controller.database.tables.FTLTable;
import org.schema.game.common.controller.elements.EffectManagerContainer;
import org.schema.game.common.controller.elements.StationaryManagerContainer;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.cloaking.StealthAddOn.StealthLvl;
import org.schema.game.common.controller.elements.effectblock.EffectElementManager;
import org.schema.game.common.controller.elements.warpgate.WarpgateCollectionManager;
import org.schema.game.common.controller.rules.rules.RuleEntityManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SimpleGameObject;
import org.schema.game.common.data.blockeffects.config.ConfigEntityManager;
import org.schema.game.common.data.blockeffects.config.ConfigManagerInterface;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.creature.AICreature;
import org.schema.game.common.data.creature.AIPlayer;
import org.schema.game.common.data.mission.spawner.SpawnController;
import org.schema.game.common.data.physics.BoundingSphereObject;
import org.schema.game.common.data.physics.CollisionType;
import org.schema.game.common.data.physics.CubeRayCastResult;
import org.schema.game.common.data.physics.PairCachingGhostObjectAlignable;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.physics.RigidBodySegmentController;
import org.schema.game.common.data.player.AbstractCharacter;
import org.schema.game.common.data.player.AbstractOwnerState;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.common.data.player.faction.FactionRelation.RType;
import org.schema.game.common.data.world.SectorInformation.SectorType;
import org.schema.game.network.objects.NTRuleInterface;
import org.schema.game.server.controller.GameServerController;
import org.schema.game.server.controller.SectorSwitch;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.blueprintnw.BlueprintClassification;
import org.schema.schine.ai.stateMachines.AiInterface;
import org.schema.schine.common.language.Lng;
import org.schema.schine.common.language.Translatable;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.BoundingSphere;
import org.schema.schine.graphicsengine.forms.Light;
import org.schema.schine.network.Identifiable;
import org.schema.schine.network.NetworkGravity;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.TopLevelType;
import org.schema.schine.network.client.ClientState;
import org.schema.schine.network.objects.LocalSectorTransition;
import org.schema.schine.network.objects.NetworkEntity;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.container.PhysicsDataContainer;
import org.schema.schine.network.objects.container.TransformTimed;
import org.schema.schine.network.server.ServerStateInterface;
import org.schema.schine.physics.Physical;
import org.schema.schine.physics.PhysicsState;
import org.schema.schine.resource.DiskWritable;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public abstract class SimpleTransformableSendableObject<E extends NetworkListenerEntity> implements Damager, DatabaseInsertable, SimpleGameObject, BoundingSphereObject, GameTransformable, StealthReconEntity, Sendable, DiskWritable {
    public static final int DEBUG_NT_SMOOTHER = 1;
    private static final Vector3f noGrav = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Transform serverTmp = new Transform();
    private static final Transform clientTmp = new Transform();
    private static ThreadLocal<TransformaleObjectTmpVars> threadLocal = new ThreadLocal<TransformaleObjectTmpVars>() {
        protected final TransformaleObjectTmpVars initialValue() {
            return new TransformaleObjectTmpVars();
        }
    };
    private final Transform worldTransformInverse = new Transform();
    private final InterEffectContainer effectContainer;
    private final BoundingSphere boundingSphere = new BoundingSphere();
    private final BoundingSphere boundingSphereTotal = new BoundingSphere();
    public final VirtualEntityAttachment vServerAttachment;
    public final ObjectArrayFIFOQueue<AICreature<? extends AIPlayer>> attachedAffinityInitial = new ObjectArrayFIFOQueue();
    public final TransformaleObjectTmpVars v;
    private final StateInterface state;
    private final boolean onServer;
    private final Vector3i tagSectorId = new Vector3i(-2147483648, -2147483648, -2147483648);
    private final SpawnController spawnController;
    private final Vector3f tmp = new Vector3f();
    private final GravityState gravity = new GravityState();
    private final ObjectOpenHashSet<AICreature<? extends AIPlayer>> attachedAffinity = new ObjectOpenHashSet();
    private final ByteArrayFIFOQueue graphicsEffectRecBuffer = new ByteArrayFIFOQueue();
    public long sectorChangedTimeOwnClient;
    public Vector3i transientSectorPos = new Vector3i();
    public boolean transientSector;
    public long lastSectorSwitch;
    public CollisionObject clientVirtualObject;
    protected boolean flagGravityUpdate;
    protected NetworkGravity receivedGravity;
    protected Boolean hiddenUpdate;
    protected boolean forcedCheckFlag;
    private boolean warpToken;
    boolean hiddenflag = false;
    private int factionId = 0;
    private int id;
    private RemoteTransformable remoteTransformable;
    private int sectorId = -2;
    private float prohibitingBuildingAroundOrigin;
    private boolean hidden = false;
    private String owner = new String();
    private boolean markedForDelete;
    private boolean flagPhysicsInit;
    private boolean markedForDeleteSent;
    private boolean immediateStuck;
    protected GravityState scheduledGravity;
    private SimpleTransformableSendableObject<E>.TransformTimedSet clientTransform = new SimpleTransformableSendableObject.TransformTimedSet();
    private Vector3f d = new Vector3f();
    private boolean markedForPermanentDelete;
    private long lastSlowdown;
    private long slowdownStart;
    private boolean flagGravityDeligation;
    private boolean writtenForUnload;
    private long lastSearch;
    private long lastWrite;
    private boolean changed = true;
    private byte invisibleNextDraw = 2;
    private Light light;
    private long lastLagSent;
    private long currentLag;
    private long lastLagReceived;
    private long sectorChangedTime;
    public long heatDamageStart;
    public Vector3i heatDamageId;
    private boolean clientCleanedUp;
    private boolean first = true;
    private boolean inClientRange;
    public boolean needsPositionCheckOnLoad;
    protected Vector3f personalGravity = new Vector3f();
    protected boolean personalGravitySwitch = false;
    private final Vector3f minTmp = new Vector3f();
    private final Vector3f maxTmp = new Vector3f();
    private final Vector3f minTarTmp = new Vector3f();
    private final Vector3f maxTarTmp = new Vector3f();
    private boolean adminInvisibility;
    private boolean tracked;

    public RuleEntityManager<?> getRuleEntityManager() {
        return null;
    }

    public SimpleTransformableSendableObject(StateInterface var1) {
        this.state = var1;
        this.v = (TransformaleObjectTmpVars)threadLocal.get();
        this.spawnController = new SpawnController(this);
        this.onServer = var1 instanceof ServerStateInterface;
        this.vServerAttachment = new VirtualEntityAttachment(this);
        this.effectContainer = this.setupEffectContainer();
    }

    public Faction getFaction() {
        return ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId());
    }

    public Vector3i getClientSector() {
        assert !this.isOnServer();

        Sendable var1;
        return (var1 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())) != null && var1 instanceof RemoteSector ? ((RemoteSector)var1).clientPos() : null;
    }

    public Transform getWorldTransformInverse() {
        return this.worldTransformInverse;
    }

    protected InterEffectContainer setupEffectContainer() {
        return new SimpleTransformableSendableObject.GenericEffectSet();
    }

    public void destroy() {
        System.out.println("[SIMPLETRANSFORMABLE] ENTITY " + this + " HAS BEEN DESTROYED... ");
        this.markForPermanentDelete(true);
        this.setMarkedForDeleteVolatile(true);
    }

    public static String getTypeString(SimpleTransformableSendableObject var0) {
        return var0.getType().getName();
    }

    public boolean isPhysicalForDamage() {
        return true;
    }

    public static void calcWaypointSecPos(Vector3i var0, Vector3i var1, Transform var2, GameServerState var3, Vector3i var4) {
        (var4 = new Vector3i(var1)).sub(var0);
        var2.setIdentity();
        float var7 = var3.getGameState().getRotationProgession();
        Vector3f var10 = new Vector3f((float)var4.x * var3.getSectorSize(), (float)var4.y * var3.getSectorSize(), (float)var4.z * var3.getSectorSize());
        Matrix3f var5;
        (var5 = new Matrix3f()).rotX(6.2831855F * var7);
        Sector var8;
        if ((var8 = var3.getUniverse().getSectorWithoutLoading(var1)) != null) {
            try {
                if (var8.getSectorType() == SectorType.PLANET) {
                    var5.invert();
                    Vector3f var9;
                    (var9 = new Vector3f()).add(var10);
                    TransformTools.rotateAroundPoint(var9, var5, var2, new Transform());
                    var2.origin.add(var10);
                    return;
                }
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

        var2.origin.set(var10);
    }

    public ByteBuffer getDataByteBuffer() {
        return this.state.getDataByteBuffer();
    }

    public long getUpdateTime() {
        return this.state.getUpdateTime();
    }

    public void releaseDataByteBuffer(ByteBuffer var1) {
        this.state.releaseDataByteBuffer(var1);
    }

    public static Vector3i getBlockPositionRelativeTo(Vector3f var0, SimpleTransformableSendableObject var1, Vector3i var2) {
        int var3;
        int var4;
        int var5;
        if (var1 == null) {
            var5 = Math.round(var0.x + 16.0F);
            var3 = Math.round(var0.y + 16.0F);
            var4 = Math.round(var0.z + 16.0F);
            var2.set(var5, var3, var4);
        } else {
            Transform var6;
            (var6 = new Transform(var1.getWorldTransform())).inverse();
            Transform var7;
            (var7 = new Transform()).origin.set(var0);
            var6.mul(var7);
            var4 = Math.round(var6.origin.x + 16.0F);
            var3 = Math.round(var6.origin.y + 16.0F);
            var5 = Math.round(var6.origin.z + 16.0F);
            var2.set(var4, var3, var5);
        }

        return var2;
    }

    public abstract void addListener(E var1);

    public abstract List<E> getListeners();

    public static void calcWorldTransformRelative(StateInterface var0, Vector3i var1, Vector3i var2, boolean var3, boolean var4, Transform var5, Transform var6, TransformaleObjectTmpVars var7) {
        if (var1.equals(var2)) {
            var6.set(var5);
        } else {
            float var8 = ((GameStateInterface)var0).getGameState().getRotationProgession();
            StellarSystem.getPosFromSector(var1, var7.systemPos);
            var7.dir.sub(var2, var1);
            var7.otherSecCenter.set((float)var7.dir.x * ((GameStateInterface)var0).getSectorSize(), (float)var7.dir.y * ((GameStateInterface)var0).getSectorSize(), (float)var7.dir.z * ((GameStateInterface)var0).getSectorSize());
            var7.t.set(var5);
            var7.rot.rotX(6.2831855F * var8);
            if (var4) {
                var7.bb.set(var7.otherSecCenter);
                TransformTools.rotateAroundPoint(var7.bb, var7.rot, var7.t, var7.transTmp);
            } else if (var3) {
                var7.rot.invert();
                var7.bb.set(var7.t.origin);
                var7.bb.add(var7.otherSecCenter);
                TransformTools.rotateAroundPoint(var7.bb, var7.rot, var7.t, var7.transTmp);
            }

            var7.t.origin.add(var7.otherSecCenter);
            var6.set(var7.t);
        }
    }

    public static void calcWorldTransformRelative(int var0, Vector3i var1, int var2, Transform var3, StateInterface var4, boolean var5, Transform var6, TransformaleObjectTmpVars var7) {
        SectorType var8;
        Vector3i var14;
        SectorType var15;
        if (!var5) {
            RemoteSector var12 = (RemoteSector)var4.getLocalAndRemoteObjectContainer().getLocalObjects().get(var0);
            RemoteSector var16 = (RemoteSector)var4.getLocalAndRemoteObjectContainer().getLocalObjects().get(var2);
            if (var0 != var2 && (var12 == null || var16 == null)) {
                System.err.println("[ERROR][CLIENT] " + var4 + ": sector yet not loaded: " + var12 + "; " + var16 + " should be: " + var1 + "; fromTo sectorID: " + var0 + " -> " + var2);
                var6.set(var3);
                return;
            }

            var12.clientPos();
            var14 = var16.clientPos();
            var8 = var12.getType();
            var15 = var16.getType();
        } else {
            Sector var10 = ((GameServerState)var4).getUniverse().getSector(var0);
            Sector var9 = ((GameServerState)var4).getUniverse().getSector(var2);
            if (var0 != var2 && (var10 == null || var9 == null)) {
                System.err.println("[ERROR][SERVER] " + var4 + ": sector yet not loaded: " + var10 + "; " + var9 + " should be: " + var1 + "; fromTo sectorID: " + var0 + " -> " + var2);
                var6.set(var3);
                return;
            }

            Vector3i var10000 = var10.pos;
            var14 = var9.pos;

            try {
                var8 = var10.getSectorType();
                var15 = var9.getSectorType();
            } catch (IOException var11) {
                var11.printStackTrace();
                return;
            }
        }

        if (var0 == var2) {
            var6.set(var3);
        } else {
            float var13 = ((GameStateInterface)var4).getGameState().getRotationProgession();
            StellarSystem.getPosFromSector(var1, var7.systemPos);
            var7.dir.sub(var14, var1);
            var7.otherSecCenter.set((float)var7.dir.x * ((GameStateInterface)var4).getSectorSize(), (float)var7.dir.y * ((GameStateInterface)var4).getSectorSize(), (float)var7.dir.z * ((GameStateInterface)var4).getSectorSize());
            var7.t.set(var3);
            if (var8 != SectorType.PLANET && var15 == SectorType.PLANET) {
                var7.rot.rotX(6.2831855F * var13);
                var7.transTmp.origin.set(var7.otherSecCenter);
                var7.transTmp.basis.set(var7.rot);
                var7.transTmp.mul(var7.t);
                var7.t.set(var7.transTmp);
            } else {
                if (var8 == SectorType.PLANET && var15 != SectorType.PLANET) {
                    var7.rot.rotX(6.2831855F * var13);
                    var7.rot.invert();
                    var7.bb.set(var7.t.origin);
                    var7.bb.add(var7.otherSecCenter);
                    TransformTools.rotateAroundPoint(var7.bb, var7.rot, var7.t, var7.transTmp);
                }

                var7.t.origin.add(var7.otherSecCenter);
            }

            var6.set(var7.t);
        }
    }

    public float getSpeedPercentServerLimitCurrent() {
        return this.getSpeedCurrent() / ((GameStateInterface)this.getState()).getGameState().getMaxGalaxySpeed();
    }

    public float getSpeedCurrent() {
        CollisionObject var1;
        return (var1 = this.getPhysicsDataContainer().getObject()) != null && var1 instanceof RigidBody ? ((RigidBody)var1).getLinearVelocity(new Vector3f()).length() : 0.0F;
    }

    public String getTypeString() {
        return getTypeString(this);
    }

    public abstract SimpleTransformableSendableObject.EntityType getType();

    public boolean existsInState() {
        return this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().containsKey(this.getId());
    }

    public final int getSectorId() {
        return this.sectorId;
    }

    public void calcWorldTransformRelative(int var1, Vector3i var2) {
        this.calcWorldTransformRelative(var1, var2, this.getSectorId());
    }

    public Transform getClientTransform() {
        return this.clientTransform;
    }

    public Vector3f getCenterOfMass(Vector3f var1) {
        var1.set(this.getPhysicsDataContainer().lastCenter);
        return var1;
    }

    public Transform getClientTransformCenterOfMass(Transform var1) {
        this.tmp.set(this.getPhysicsDataContainer().lastCenter);
        var1.set(this.clientTransform);
        var1.basis.transform(this.tmp);
        var1.origin.add(this.tmp);
        return var1;
    }

    public Transform getWorldTransformCenterOfMass(Transform var1) {
        this.tmp.set(this.getPhysicsDataContainer().lastCenter);
        var1.set(this.getWorldTransform());
        var1.basis.transform(this.tmp);
        var1.origin.add(this.tmp);
        return var1;
    }

    public Transform getWorldTransformOnClientCenterOfMass(Transform var1) {
        this.tmp.set(this.getPhysicsDataContainer().lastCenter);
        var1.set(this.getWorldTransformOnClient());
        var1.basis.transform(this.tmp);
        var1.origin.add(this.tmp);
        return var1;
    }

    public Vector3f getLinearVelocity(Vector3f var1) {
        return var1;
    }

    public boolean isInPhysics() {
        return this.getPhysicsDataContainer().getObject() != null;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public int getAsTargetId() {
        return this.getId();
    }

    public byte getTargetType() {
        return 0;
    }

    public TransformTimed getWorldTransformOnClient() {
        assert !this.isOnServer();

        GameClientState var1 = (GameClientState)this.getState();
        return (TransformTimed)(this.getSectorId() == var1.getCurrentSectorId() ? this.remoteTransformable.getWorldTransform() : this.clientTransform);
    }

    public void transformAimingAt(Vector3f var1, Damager var2, SimpleGameObject var3, Random var4, float var5) {
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().transformAimingAt(var1, var2, var3, var4, var5);
        } else {
            var1.x = (float)((Math.random() - 0.5D) * (double)var5);
            var1.y = (float)((Math.random() - 0.5D) * (double)var5);
            var1.z = (float)((Math.random() - 0.5D) * (double)var5);
            if (this.isOnServer()) {
                var1.add(var3.getClientTransformCenterOfMass(serverTmp).origin);
            } else {
                var1.add(var3.getClientTransformCenterOfMass(clientTmp).origin);
            }
        }
    }

    public void getAimingAtRelativePos(Vector3f var1, Damager var2, SimpleGameObject var3, Random var4, float var5) {
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().getAimingAtRelative(var1, var2, var3, var4, var5);
        } else {
            var1.x = (var4.nextFloat() - 0.5F) * var5;
            var1.y = (var4.nextFloat() - 0.5F) * var5;
            var1.z = (var4.nextFloat() - 0.5F) * var5;
        }
    }

    public void setHidden(boolean var1) {
        if (this.isOnServer()) {
            this.hidden = var1;
        }

    }

    public void setSectorId(int var1) {
        if (this.isClientOwnObject() && this.sectorId != var1) {
            this.sectorChangedTimeOwnClient = System.currentTimeMillis();
        }

        if (this.sectorId != var1) {
            this.setSectorChangedTime(System.currentTimeMillis());
            if (this.isOnServer()) {
                Sector var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.sectorId);
                Sector var3 = ((GameServerState)this.getState()).getUniverse().getSector(var1);
                if (var2 != null) {
                    var2.removeEntity(this);
                }

                if (var3 != null) {
                    var3.addEntity(this);
                }
            }
        }

        this.sectorId = var1;
    }

    public boolean engageJump(int var1) {
        assert this.isOnServer();

        GameServerState var2 = (GameServerState)this.getState();
        Transform var3;
        (var3 = new Transform()).setIdentity();
        float var4 = var2.getGameState().getRotationProgession();
        Matrix3f var5;
        (var5 = new Matrix3f()).rotX(6.2831855F * var4);
        Vector3f var12;
        GlUtil.getForwardVector(var12 = new Vector3f(), this.getWorldTransform());
        var12.scale(var2.getSectorSize() * (float)var1);
        Sector var6 = var2.getUniverse().getSector(this.getSectorId());
        byte var7 = 0;
        if (var6 != null) {
            try {
                if (var6.getSectorType() == SectorType.PLANET) {
                    var5.invert();
                    Vector3f var8;
                    (var8 = new Vector3f()).add(var12);
                    TransformTools.rotateAroundPoint(var8, var5, var3, new Transform());
                    var3.origin.add(var12);
                } else {
                    var3.origin.set(var12);
                }
            } catch (IOException var11) {
                var11.printStackTrace();
            }
        }

        System.err.println("[JUMPDRIVE] direct forward: " + var3.origin);
        if (this instanceof PlayerControllable && !((PlayerControllable)this).getAttachedPlayers().isEmpty()) {
            Iterator var16 = ((PlayerControllable)this).getAttachedPlayers().iterator();

            while(var16.hasNext()) {
                PlayerState var15;
                Iterator var9 = (var15 = (PlayerState)var16.next()).getControllerState().getUnits().iterator();

                while(var9.hasNext()) {
                    ControllerStateUnit var13;
                    if ((var13 = (ControllerStateUnit)var9.next()).parameter != null && var13.parameter.equals(Ship.core) && !var15.getNetworkObject().waypoint.getVector().equals(PlayerState.NO_WAYPOINT) && var6 != null) {
                        Vector3i var14;
                        (var14 = var15.getNetworkObject().waypoint.getVector()).sub(var6.pos);
                        if ((var12 = new Vector3f((float)var14.x * var2.getSectorSize(), (float)var14.y * var2.getSectorSize(), (float)var14.z * var2.getSectorSize())).length() > var2.getSectorSize() * (float)var1) {
                            var12.normalize();
                            var12.scale(var2.getSectorSize() * (float)var1);
                            var7 = 1;
                        } else {
                            var7 = 2;
                        }

                        var3.origin.set(var12);
                        System.err.println("[JUMPDRIVE] setting dir from waypoint: " + var15.getNetworkObject().waypoint.getVector() + " -> " + var3.origin);
                    }
                }
            }
        }

        if (var6 != null) {
            Vector3i newSecPos = new Vector3i(var6.pos);
            System.err.println("[JUMPDRIVE] scaling: " + var3.origin + " -> " + 1.0F / var2.getSectorSize());
            var3.origin.scale(1.0F / var2.getSectorSize());
            newSecPos.add((int)var3.origin.x, (int)var3.origin.y, (int)var3.origin.z);

            try {
                if (var2.getUniverse().getSector(newSecPos).isNoEntry()) {
                    this.sendControllingPlayersServerMessage(new Object[]{423}, 3);
                } else if (var6.isNoExit()) {
                    this.sendControllingPlayersServerMessage(new Object[]{424}, 3);
                } else {
                    //INSERTED CODE @768
                    if(this instanceof SegmentController) {
                        Vector3i oldSector = this.getSector(new Vector3i());
                        ShipJumpEngageEvent event = new ShipJumpEngageEvent((SegmentController) this, oldSector, newSecPos);
                        StarLoader.fireEvent(ShipJumpEngageEvent.class, event, this.isOnServer());
                        if (event.isCanceled()) {
                            return false;
                        }
                    }else{
                        //how did it even jump
                    }
                    ///
                    System.err.println("[JUMPDRIVE] scaled to secSize: " + var3.origin + " -> " + newSecPos);
                    this.getNetworkObject().graphicsEffectModifier.add((byte)1);
                    SectorSwitch var18;
                    if ((var18 = ((GameServerState)this.getState()).getController().queueSectorSwitch(this, newSecPos, 1, false, true, true)) != null) {
                        var18.delay = System.currentTimeMillis() + 4000L;
                        var18.jumpSpawnPos = new Vector3f(this.getWorldTransform().origin);
                        var18.executionGraphicsEffect = 2;
                        var18.keepJumpBasisWithJumpPos = true;
                        if (var7 == 0) {
                            this.sendControllingPlayersServerMessage(new Object[]{425, newSecPos.toStringPure()}, 1);
                        } else if (var7 == 1) {
                            this.sendControllingPlayersServerMessage(new Object[]{426, newSecPos.toStringPure()}, 1);
                        } else {
                            this.sendControllingPlayersServerMessage(new Object[]{427, newSecPos.toStringPure()}, 1);
                        }

                        return true;
                    }
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            }
        }

        return false;
    }

    public void sendControllingPlayersServerMessage(Object[] var1, int var2) {
        if (this.isOnServer() && this instanceof PlayerControllable && !((PlayerControllable)this).getAttachedPlayers().isEmpty()) {
            ((GameServerState)this.getState()).getController().sendPlayerMessage(((PlayerState)((PlayerControllable)this).getAttachedPlayers().get(0)).getName(), var1, var2);
        }

    }

    public abstract boolean isClientOwnObject();

    protected boolean checkGravityDownwards(SimpleTransformableSendableObject<?> var1) {
        if (this.getSectorId() == var1.getSectorId() && this.getPhysicsDataContainer() != null && this.getPhysicsDataContainer().isInitialized()) {
            this.getGravityAABB(this.minTmp, this.maxTmp);
            var1.getGravityAABB(this.minTarTmp, this.maxTarTmp);
            if (AabbUtil2.testAabbAgainstAabb2(this.minTarTmp, this.maxTarTmp, this.minTmp, this.maxTmp)) {
                Vector3f var2 = new Vector3f(var1.getWorldTransform().origin);
                Vector3f var3 = new Vector3f(var1.getWorldTransform().origin);
                Vector3f var4;
                (var4 = GlUtil.getUpVector(new Vector3f(), this.getWorldTransform())).scale(-64.0F);
                var3.add(var4);
                CubeRayCastResult var5 = new CubeRayCastResult(var2, var3, var1, new SegmentController[0]);
                ClosestRayResultCallback var8;
                if ((var8 = this.getPhysics().testRayCollisionPoint(var2, var3, var5, this.getPhysicsDataContainer().isStatic())) != null && var8.hasHit() && var5.getSegment() != null && var5.getSegment().getSegmentController() == this) {
                    Segment var6;
                    boolean var9 = (var6 = var5.getSegment()).pos.y + 32 <= var6.getSegmentController().getMaxPos().y << 5;
                    boolean var7 = var6.getSegmentController().getSegmentBuffer().containsKey(var6.pos.x, var6.pos.y + 32, var6.pos.z);
                    if (!var9 || var7) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean affectsGravityOf(SimpleTransformableSendableObject<?> var1) {
        if (var1 instanceof AbstractCharacter && ((AbstractCharacter)var1).getOwnerState() != null && ((AbstractCharacter)var1).getOwnerState().isSitting()) {
            return false;
        } else if (FactionManager.isNPCFaction(var1.getFactionId())) {
            return false;
        } else {
            return this.personalGravitySwitch && var1.getSectorId() == this.getSectorId() && (var1.getMass() > 0.0F || var1 instanceof AbstractCharacter) && this.checkGravityDownwards(var1);
        }
    }

    public void aiFromTagStructure(Tag var1) {
        if (this instanceof AiInterface && var1.getType() != Type.BYTE) {
            ((AiInterface)this).getAiConfiguration().fromTagStructure(var1);
        }

    }

    public Tag aiToTagStructure() {
        Tag var1;
        if (this instanceof AiInterface && ((AiInterface)this).getAiConfiguration() != this) {
            assert ((AiInterface)this).getAiConfiguration() != null : this.toString();

            var1 = ((AiInterface)this).getAiConfiguration().toTagStructure();
        } else {
            var1 = new Tag(Type.BYTE, "noAI", (byte)0);
        }

        return var1;
    }

    public void calculateRelToThis(Sector var1, Vector3i var2) {
        this.calcWorldTransformRelative(var2.equals(var1.pos) ? var1.getId() : -1, var2, this.getSectorId());
    }

    public boolean isSpectator() {
        Faction var1;
        if ((var1 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId())) != null && var1.isFactionMode(4)) {
            return true;
        } else {
            return this instanceof PlayerControllable && ((PlayerControllable)this).hasSpectatorPlayers();
        }
    }

    public boolean isInServerClientRange() {
        return this.isOnServer() || this.isInClientRange();
    }

    public int getMiningBonus(SimpleTransformableSendableObject<?> var1) {
        return this instanceof PlayerControllable && ((PlayerControllable)this).getAttachedPlayers().size() > 0 ? ((GameServerState)this.getState()).getUniverse().getSystemOwnerShipType(var1.getSectorId(), ((PlayerState)((PlayerControllable)this).getAttachedPlayers().get(0)).getFactionId()).getMiningBonusMult() * (Integer)ServerConfig.MINING_BONUS.getCurrentState() : ((GameServerState)this.getState()).getUniverse().getSystemOwnerShipType(var1.getSectorId(), this.getFactionId()).getMiningBonusMult() * (Integer)ServerConfig.MINING_BONUS.getCurrentState();
    }

    public boolean hasClientVirtual() {
        return this.clientVirtualObject != null;
    }

    public void calcWorldTransformRelative(int var1, Vector3i var2, int var3) {
        if (this.remoteTransformable == null) {
            throw new NullPointerException("No remote Transformable");
        } else {
            calcWorldTransformRelative(var1, var2, var3, this.remoteTransformable.getWorldTransform(), this.state, this.onServer, this.clientTransform, this.v);
        }
    }

    protected void checkForGravity() {
        this.gravity.setChanged(false);
        if (this.gravityChecksRequired()) {
            if (this.scheduledGravity != null) {
                if (this.scheduledGravity.withBlockBelow) {
                    Vector3f var1 = new Vector3f(this.getWorldTransform().origin);
                    Vector3f var2 = new Vector3f(this.getWorldTransform().origin);
                    Vector3f var3;
                    (var3 = GlUtil.getUpVector(new Vector3f(), this.getWorldTransform())).negate();
                    var3.normalize();
                    var3.scale(10.0F);
                    var2.add(var3);
                    CubeRayCastResult var4;
                    (var4 = new CubeRayCastResult(var1, var2, false, new SegmentController[0])).setIgnoereNotPhysical(true);
                    var4.setOnlyCubeMeshes(true);
                    if (!this.getPhysics().testRayCollisionPoint(var1, var2, var4, false).hasHit()) {
                        this.forcedCheckFlag = true;
                        return;
                    }
                }

                System.err.println(this.getState() + " " + this + " HANDLE SCHEDULED GRAVITY " + this.scheduledGravity.getAcceleration() + ", " + this.scheduledGravity.source);
                this.setGravity(this.scheduledGravity.getAcceleration(), this.scheduledGravity.source, this.scheduledGravity.central, this.scheduledGravity.forcedFromServer);
                this.scheduledGravity = null;
            } else if (this.getState().getUpdateTime() - this.lastSearch > 500L) {
                this.searchForGravity();
                this.lastSearch = this.getState().getUpdateTime();
            }

            this.checkGravityValid();
        }

        if (this.gravity.isChanged()) {
            System.err.println("[GRAVITY] " + this + " changed gravity on " + this.getState() + " " + this.gravity.source + " -> " + this.gravity.getAcceleration());
        }

        if (this.flagGravityUpdate) {
            System.err.println("[GRAVITY] " + this + " FLAG changed gravity on " + this.getState() + " " + this.gravity.source + " -> " + this.gravity.getAcceleration());
            this.gravity.setChanged(true);
            this.flagGravityUpdate = false;
        }

    }

    protected void checkGravityValid() {
        if (!this.isHidden()) {
            if (!this.gravity.isValid(this)) {
                assert !this.getGravity().isAligedOnly() && !this.getGravity().isGravityOn() || this.getGravity().source != null;

                this.removeGravity();
            }

        }
    }

    public void cleanUpOnEntityDelete() {
        if (this.isOnServer()) {
            this.vServerAttachment.clear();
        }

        this.onPhysicsRemove();
        this.attachedAffinity.clear();
    }

    public abstract NetworkEntity getNetworkObject();

    public StateInterface getState() {
        return this.state;
    }

    public Transform getInitialTransform() {
        return this.remoteTransformable.getInitialTransform();
    }

    public void initFromNetworkObject(NetworkObject var1) {
        NetworkEntity var4 = (NetworkEntity)var1;
        this.setId(var4.id.get());
        this.setFactionId(this.getNetworkObject().factionCode.get());
        if (!this.isOnServer()) {
            this.setTracked((Boolean)var4.tracked.get());
            if (this.getSectorId() != var4.sector.get()) {
                this.clientSectorChange(this.getSectorId(), var4.sector.get());
                this.setSectorId(var4.sector.get());
            }

            this.hidden = (Boolean)var4.hidden.get();
        } else {
            this.setSectorId(var4.sector.get());
        }

        if (this.isOnServer() && this.getSectorId() == -2 && var4.sector.get() == -2) {
            Sector var2;
            try {
                var2 = this.getDefaultSector();
            } catch (IOException var3) {
                var3.printStackTrace();
                throw new RuntimeException(var3);
            }

            System.err.println("[SERVER] NO SECTOR INFORMATION PROVIDED ON INIT: ASSIGNING SECTOR " + var2.getId() + " TO " + this);
            this.setSectorId(var2.getId());
        }

        this.getRemoteTransformable().updateFromRemoteInitialTransform(var4);
    }

    public void initialize() {
        if (this.remoteTransformable == null) {
            this.remoteTransformable = new RemoteTransformable(this, this.state) {
                public void createConstraint(Physical var1, Physical var2, Object var3) {
                }

                public StateInterface getState() {
                    return SimpleTransformableSendableObject.this.getState();
                }

                public void getTransformedAABB(Vector3f var1, Vector3f var2, float var3, Vector3f var4, Vector3f var5, Transform var6) {
                    SimpleTransformableSendableObject.this.getTransformedAABB(var1, var2, var3, var4, var5, var6);
                }

                public void initPhysics() {
                    SimpleTransformableSendableObject.this.initPhysics();
                    this.getPhysicsDataContainer().lastTransform.set(SimpleTransformableSendableObject.this.remoteTransformable.getInitialTransform());
                }
            };
        } else {
            System.err.println("[Transformable][WARNING] Remote transformable already exists. skipped creation: " + this);
        }
    }

    public float getMass() {
        return this.getRemoteTransformable().getMass();
    }

    public boolean isMarkedForDeleteVolatile() {
        return this.markedForDelete;
    }

    public void setMarkedForDeleteVolatile(boolean var1) {
        this.markedForDelete = var1;
        Sector var2;
        if (this.isOnServer() && (var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.sectorId)) != null) {
            var2.removeEntity(this);
        }

    }

    public boolean isMarkedForDeleteVolatileSent() {
        return this.markedForDeleteSent;
    }

    public PhysicsDataContainer getPhysicsDataContainer() {
        return this.getRemoteTransformable().getPhysicsDataContainer();
    }

    public void setMarkedForDeleteVolatileSent(boolean var1) {
        this.markedForDeleteSent = var1;
    }

    public boolean isMarkedForPermanentDelete() {
        return this.markedForPermanentDelete;
    }

    public boolean isOkToAdd() {
        return this.isOnServer() ? ((GameServerState)this.getState()).getUniverse().existsSector(this.getSectorId()) : true;
    }

    public boolean isOnServer() {
        return this.onServer;
    }

    public boolean isUpdatable() {
        return true;
    }

    public void markForPermanentDelete(boolean var1) {
        this.markedForPermanentDelete = var1;
        Sector var2;
        if (this.isOnServer() && (var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.sectorId)) != null) {
            var2.removeEntity(this);
        }

    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        this.getRemoteTransformable().updateFromRemoteTransform((NetworkEntity)var1);
        NetworkEntity var7 = (NetworkEntity)var1;
        this.setTracked((Boolean)var7.tracked.get());
        if (!this.isOnServer()) {
            int var4;
            if (!this.getNetworkObject().graphicsEffectModifier.getReceiveBuffer().isEmpty()) {
                synchronized(this.graphicsEffectRecBuffer) {
                    for(var4 = 0; var4 < this.getNetworkObject().graphicsEffectModifier.getReceiveBuffer().size(); ++var4) {
                        this.graphicsEffectRecBuffer.enqueue(this.getNetworkObject().graphicsEffectModifier.getReceiveBuffer().getByte(var4));
                    }
                }
            }

            if (this instanceof SegmentController && ((SegmentController)this).getDockingController().getDelayedDock() != null) {
                this.hiddenUpdate = true;
            } else if (this.hidden != (Boolean)var7.hidden.get()) {
                this.hiddenUpdate = (Boolean)var7.hidden.get();
            }

            if (this.getSectorId() != var7.sector.get()) {
                int var3 = this.getSectorId();
                var4 = var7.sector.get();
                this.clientSectorChange(var3, var4);
            }

            this.setFactionId(this.getNetworkObject().factionCode.get());
        }

        if (var1 instanceof NTRuleInterface) {
            this.getRuleEntityManager().receive((NTRuleInterface)var1);
        }

        if (((NetworkGravity)var7.gravity.get()).gravityReceived) {
            this.receivedGravity = new NetworkGravity((NetworkGravity)var7.gravity.get());
            ((NetworkGravity)var7.gravity.get()).gravityReceived = false;
        }

        for(Iterator var8 = this.getNetworkObject().lagAnnouncement.getReceiveBuffer().iterator(); var8.hasNext(); this.lastLagReceived = System.currentTimeMillis()) {
            long var9 = (Long)var8.next();
            this.currentLag = var9;
        }

    }

    public abstract E createNetworkListenEntity();

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this, "SimpleTransformableSendableObj");
        long var2 = System.currentTimeMillis();
        if (this.first) {
            if (!this.isOnServer()) {
                NetworkListenerEntity var4;
                (var4 = this.createNetworkListenEntity()).setClientId(((GameClientState)this.getState()).getId());
                this.addListener((E) var4);
                var4.setId(this.getId());
                ((GameClientController)this.getState().getController()).getPrivateChannelSynchController().addNewSynchronizedObjectQueued(var4);
            }

            this.first = false;
        }

        if (this.isOnServer()) {
            for(int var10 = 0; var10 < this.getListeners().size(); ++var10) {
                if (((NetworkListenerEntity)this.getListeners().get(var10)).isMarkedForDeleteVolatileSent()) {
                    this.getListeners().remove(var10);
                    --var10;
                }
            }
        }

        this.adminInvisibility = this.getOwnerState() != null && this.getOwnerState() instanceof PlayerState && ((PlayerState)this.getOwnerState()).isInvisibilityMode();
        if (this.invisibleNextDraw < 2) {
            ++this.invisibleNextDraw;
        }

        if (this.hiddenUpdate != null) {
            this.hidden = this.hiddenUpdate;
            this.hiddenUpdate = null;
        }

        if (!this.graphicsEffectRecBuffer.isEmpty()) {
            synchronized(this.graphicsEffectRecBuffer) {
                while(!this.graphicsEffectRecBuffer.isEmpty()) {
                    this.executeGraphicalEffectClient(this.graphicsEffectRecBuffer.dequeueByte());
                }
            }
        }

        if (this.isFlagPhysicsInit() || this.isHidden() != this.hiddenflag) {
            this.onPhysicsRemove();
            if (!this.isHidden() && this.getPhysicsDataContainer().getObject() != null && this.addToPhysicsOnInit()) {
                this.onPhysicsAdd();
            }

            this.hiddenflag = this.isHidden();
            this.getPhysicsDataContainer().updatePhysical(this.getState().getUpdateTime());
            this.setFlagPhysicsInit(false);
        }

        if (this.receivedGravity != null) {
            if (this.getState() instanceof ClientState && this.getRemoteTransformable().isSendFromClient() && !this.receivedGravity.forcedFromServer) {
                this.receivedGravity = null;
            } else {
                label231: {
                    Sendable var11 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.receivedGravity.gravityIdReceive);
                    if (this.receivedGravity.gravityIdReceive > 0 && var11 != null && var11 instanceof SimpleTransformableSendableObject) {
                        if (this.getGravity().source != var11 || this.receivedGravity.gravityReceive.length() > 0.0F && this.getGravity().getAcceleration().length() > 0.0F && !this.getGravity().getAcceleration().equals(this.receivedGravity.gravityReceive)) {
                            this.getGravity().source = (SimpleTransformableSendableObject)var11;
                            this.flagGravityUpdate = true;
                        }

                        this.getGravity().getAcceleration().set(this.receivedGravity.gravityReceive);
                    } else {
                        if (this.receivedGravity.gravityIdReceive != -1) {
                            break label231;
                        }

                        this.getGravity().getAcceleration().set(0.0F, 0.0F, 0.0F);
                        if (this.getGravity().source != null) {
                            this.getGravity().source = null;
                            this.flagGravityUpdate = true;
                        }
                    }

                    this.receivedGravity = null;
                }

                if (this.isOnServer()) {
                    this.flagGravityDeligation = true;
                }
            }
        }

        boolean var13 = this.getPhysicsDataContainer().isInitialized() && this.getPhysicsDataContainer().getObject() != null && !this.getPhysicsDataContainer().getObject().isStaticObject();
        Transform var5;
        if (this.isOnServer()) {
            if (!this.isHidden()) {
                this.spawnController.update(var1);
            }

            this.vServerAttachment.update();
            if (!this.attachedAffinityInitial.isEmpty()) {
                while(true) {
                    if (this.attachedAffinityInitial.isEmpty()) {
                        this.attachedAffinityInitial.clear();
                        break;
                    }

                    AICreature var6 = (AICreature)this.attachedAffinityInitial.dequeue();
                    System.err.println("SPAWNING WITH " + this + ": " + var6);
                    var6.setId(this.state.getNextFreeObjectId());
                    var6.setSectorId(this.getSectorId());

                    assert var6.getUniqueIdentifier() != null;

                    var6.setAffinity(this);
                    ((GameServerState)this.getState()).getController().getSynchController().addNewSynchronizedObjectQueued(var6);
                }
            }

            if (Float.isNaN(this.getWorldTransform().origin.x)) {
                try {
                    throw new IllegalStateException("Exception: NaN position for " + this + "; PObject: " + this.getPhysicsDataContainer().getObject());
                } catch (Exception var8) {
                    var8.printStackTrace();
                    this.getWorldTransform().origin.set(400.0F, 400.0F, 400.0F);
                    if (this.getPhysicsDataContainer().getObject() != null) {
                        ((GameServerController)this.getState().getController()).broadcastMessageAdmin(new Object[]{428, this}, 3);
                        (var5 = new Transform()).setIdentity();
                        var5.origin.set(400.0F, 400.0F, 400.0F);
                        this.getPhysicsDataContainer().updateManually(var5);
                        this.getPhysicsDataContainer().getObject().setWorldTransform(var5);
                        if (!this.getPhysicsDataContainer().getObject().isStaticOrKinematicObject() && this.getPhysicsDataContainer().getObject() instanceof RigidBody) {
                            ((RigidBody)this.getPhysicsDataContainer().getObject()).getMotionState().setWorldTransform(var5);
                        }
                    }
                }
            }
        }

        this.getRemoteTransformable().update(var1);
        if (this.isOnServer()) {
            if (var13 && this.isCheckSectorActive()) {
                ((GameServerState)this.getState()).getUniverse().getSectorBelonging(this);
            }
        } else if (this.isPlayerNeighbor(this.getSectorId())) {
            this.calcWorldTransformRelative(((GameClientState)this.getState()).getCurrentSectorId(), ((GameClientState)this.getState()).getPlayer().getCurrentSector());
            if (((GameClientState)this.getState()).getCurrentSectorId() != this.getSectorId() && this.clientVirtualObject != null) {
                var5 = new Transform(this.getWorldTransformOnClient());
                Vector3f var14 = new Vector3f(this.getPhysicsDataContainer().lastCenter);
                var5.basis.transform(var14);
                var5.origin.add(var14);
                this.clientVirtualObject.setWorldTransform(var5);
                this.clientVirtualObject.setInterpolationWorldTransform(var5);
                ((RigidBody)this.clientVirtualObject).getMotionState().setWorldTransform(var5);
                this.clientVirtualObject.activate(true);
            }
        }

        if (var13) {
            this.checkForGravity();
            this.handleGravity();
        }

        RuleEntityManager var12;
        if ((var12 = this.getRuleEntityManager()) != null) {
            var12.update(var1);
        }

        if (var1.currentTime - this.lastLagReceived > 7000L) {
            this.currentLag = 0L;
        }

        this.updateToNetworkObject();
        long var15;
        if ((var15 = System.currentTimeMillis() - var2) > 200L) {
            System.err.println("[SIMPLETRANSFORMABLE] " + this.getState() + " " + this + " update took " + var15 + " ms");
        }

        this.getState().getDebugTimer().end(this, "SimpleTransformableSendableObj");
    }

    public void updateToFullNetworkObject() {
        this.getNetworkObject().id.set(this.getId());
        this.getNetworkObject().sector.set(this.getSectorId());
        this.getNetworkObject().factionCode.set(this.getFactionId());
        this.getNetworkObject().hidden.set(this.isHidden());
        this.getNetworkObject().tracked.set(this.isTracked());

        assert this.getState().getId() >= 0;

        this.getRemoteTransformable().updateToRemoteInitialTransform(this.getNetworkObject());
        this.updateToNetworkObject();
    }

    public void setAdminTrackedClient(PlayerState var1, boolean var2) {
        if (var1.isAdmin()) {
            this.getNetworkObject().tracked.set(var2, true);
        }

    }

    public void updateToNetworkObject() {
        if (this.isOnServer()) {
            this.getNetworkObject().tracked.set(this.isTracked());
            if (this.getNetworkObject().sector.getInt() != this.getSectorId()) {
                this.getNetworkObject().sector.set(this.getSectorId());
            }

            int var1 = this.getFactionId();
            if (this.getNetworkObject().factionCode.getInt() != var1) {
                this.getNetworkObject().factionCode.set(var1);
            }

            this.getNetworkObject().hidden.set(this.hidden);
        }

        if (this.flagGravityDeligation) {
            System.err.println("[SimpleTransformable][GRAVITY] " + this.getState() + " sending gravity update " + this.getGravity());
            ((NetworkGravity)this.getNetworkObject().gravity.get()).gravity.set(this.getGravity().getAcceleration());
            ((NetworkGravity)this.getNetworkObject().gravity.get()).gravityId = this.getGravity().source != null ? this.getGravity().source.getId() : -1;
            this.getNetworkObject().gravity.setChanged(true);
            this.getNetworkObject().setChanged(true);
            this.flagGravityDeligation = false;
        }

        this.getRemoteTransformable().updateToRemoteTransform(this.getNetworkObject(), this.state);
    }

    public boolean isWrittenForUnload() {
        return this.writtenForUnload;
    }

    public void setWrittenForUnload(boolean var1) {
        this.writtenForUnload = var1;
    }

    public void onWrite() {
    }

    private void clientSectorChange(int var1, int var2) {
        assert !this.isOnServer();

        ((GameClientState)this.getState()).getController().scheduleSectorChange(new SectorChange(this, var1, var2));
    }

    public void flagPhysicsSlowdown() {
        long var1;
        if ((var1 = System.currentTimeMillis()) - this.lastSlowdown > 5000L || this.slowdownStart == 0L) {
            this.slowdownStart = var1;
        }

        this.lastSlowdown = var1;
    }

    public void fromTagStructure(Tag var1) {
        assert var1.getName().equals("transformable");

        Tag[] var6 = (Tag[])var1.getValue();
        this.setMass((Float)var6[0].getValue());
        Tag[] var2;
        float[] var3 = new float[(var2 = (Tag[])var6[1].getValue()).length];

        for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = (Float)var2[var4].getValue();
        }

        if (var6.length > 2 && var6[2].getType() != Type.FINISH && var6[2].getType() != Type.BYTE) {
            this.aiFromTagStructure(var6[2]);
        }

        if (var6.length > 3 && var6[3].getType() == Type.VECTOR3i) {
            this.getTagSectorId().set((Vector3i)var6[3].getValue());
        }

        if (var6.length > 4 && var6[4].getType() == Type.INT && "fid".equals(var6[4].getName())) {
            this.setFactionId((Integer)var6[4].getValue());
        }

        if (var6.length > 5 && var6[5].getType() == Type.STRING && "own".equals(var6[5].getName())) {
            this.owner = (String)var6[5].getValue();
        }

        if (var6.length > 6 && var6[6].getType() != Type.FINISH) {
            this.spawnController.fromTagStructure(var6[6]);
        }

        Matrix4f var7;
        if ((var7 = new Matrix4f(var3)).determinant() == 0.0F) {
            var7.setIdentity();
            (new Transform(var7)).getOpenGLMatrix(var3);

            try {
                throw new NullPointerException("ERROR: Read 0 matrix: " + this + ": catched! continue with standard matrix");
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        this.remoteTransformable.getInitialTransform().setFromOpenGLMatrix(var3);
        this.remoteTransformable.getWorldTransform().setFromOpenGLMatrix(var3);
    }

    public Tag toTagStructure() {
        Tag var1 = new Tag(Type.FLOAT, "mass", this.getMass());
        Tag var2 = new Tag("transform", Type.FLOAT);
        float[] var3 = new float[16];
        this.remoteTransformable.getWorldTransform().getOpenGLMatrix(var3);

        for(int var4 = 0; var4 < 16; ++var4) {
            var2.addTag(new Tag(Type.FLOAT, "matrix", var3[var4]));
        }

        Tag var8;
        if (this instanceof ManagedSegmentController) {
            var8 = new Tag(Type.BYTE, (String)null, (byte)0);
        } else {
            var8 = this.aiToTagStructure();
        }

        Tag var7;
        if (this.getSectorId() >= 0 && ((GameServerState)this.state).getUniverse().getSector(this.getSectorId()) != null) {
            var7 = new Tag(Type.VECTOR3i, "sPos", ((GameServerState)this.state).getUniverse().getSector(this.getSectorId()).pos);
        } else {
            var7 = new Tag(Type.VECTOR3i, "sPos", this.transientSectorPos);
        }

        Tag var5 = new Tag(Type.INT, "fid", this.getOriginalFactionId());

        assert this.spawnController != null;

        Tag var6 = new Tag(Type.STRING, "own", this.owner);
        return new Tag(Type.STRUCT, "transformable", new Tag[]{var1, var2, var8, var7, var5, var6, this.spawnController.toTagStructure(), new Tag(Type.FINISH, "fin", (Tag[])null)});
    }

    public byte getDebugMode() {
        return (Byte)this.getNetworkObject().debugMode.get();
    }

    public void setDebugMode(byte var1) {
        this.getNetworkObject().debugMode.set(var1, true);
    }

    private Sector getDefaultSector() throws IOException {
        return ((GameServerState)this.getState()).getUniverse().getSector(Sector.DEFAULT_SECTOR);
    }

    public int getFactionId() {
        return this.factionId;
    }

    public void setFactionId(int var1) {
        int var2 = this.factionId;
        Faction var3;
        if (this.isOnServer() && var1 != this.factionId && (var3 = ((FactionState)this.getState()).getFactionManager().getFaction(this.factionId)) != null) {
            if (var3.getHomebaseUID().equals(this.getUniqueIdentifier())) {
                ((FactionState)this.getState()).getFactionManager().serverRevokeFactionHome(this.factionId);
            }

            Sector var6;
            if ((var6 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) != null) {
                try {
                    StellarSystem var4;
                    if ((var4 = ((GameServerState)this.getState()).getUniverse().getStellarSystemFromSecPos(var6.pos)).getOwnerFaction() == this.factionId && var4.getOwnerUID() != null && var4.getOwnerUID().equals(this.getUniqueIdentifier())) {
                        ((FactionState)this.getState()).getFactionManager().sendServerSystemFactionOwnerChange("SYSTEM", 0, "", var6.pos, var4.getPos(), var4.getName());
                    }
                } catch (IOException var5) {
                    System.err.println("[SERVER][SimpleTranformableObject] Fatal Exception: system to revoke system ownership could not be retrieved: " + this + ": sid: " + this.getSectorId());
                    var5.printStackTrace();
                }
            } else {
                System.err.println("[SERVER][SimpleTranformableObject] Fatal Exception: sector to revoke system ownership could not be retrieved: " + this + ": sid: " + this.getSectorId());
            }
        }

        this.factionId = var1;
        if (this.isOnServer() && var2 != this.factionId && this.getRuleEntityManager() != null) {
            this.getRuleEntityManager().triggerOnFactionChange();
        }

    }

    public boolean isInExitingFaction() {
        return ((FactionState)this.getState()).getFactionManager().existsFaction(this.factionId);
    }

    public GravityState getGravity() {
        return this.gravity;
    }

    public void getGravityAABB(Vector3f var1, Vector3f var2) {
        Transform var3 = this.getPhysicsDataContainer().addCenterOfMassToTransform(new Transform(this.getWorldTransform()));
        this.getPhysicsDataContainer().getShape().getAabb(var3, var1, var2);
    }

    public void getGravityAABB(Transform var1, Vector3f var2, Vector3f var3) {
        this.getPhysicsDataContainer().getShape().getAabb(var1, var2, var3);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int var1) {
        this.id = var1;
    }

    public long getLastSlowdown() {
        return this.lastSlowdown;
    }

    public PhysicsExt getPhysics() throws SectorNotFoundRuntimeException {
        return (PhysicsExt)this.getPhysicsState().getPhysics();
    }

    public PhysicsState getPhysicsState() throws SectorNotFoundRuntimeException {
        if (this.isOnServer()) {
            Sector var1;
            if ((var1 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) == null) {
                System.err.println("[ERROR][FATAL] Fatal Exception: SECTOR NULL FOR " + this + " " + this.getSectorId());
                throw new SectorNotFoundRuntimeException(this.getSectorId());
            } else {
                return var1;
            }
        } else {
            return (GameClientState)this.getState();
        }
    }

    public void getRelationColor(RType var1, boolean var2, Vector4f var3, float var4, float var5) {
        if (var1 == RType.ENEMY) {
            var3.x = var4 + 1.0F;
            var3.y = var4;
            var3.z = var4;
        } else if (((GameClientState)this.getState()).getPlayer().getFactionId() != this.getFactionId() && ((GameClientState)this.getState()).getFactionManager().existsFaction(this.getFactionId()) && ((GameClientState)this.getState()).getFactionManager().getFaction(this.getFactionId()).getFactionMode() != 0) {
            Faction var6 = ((GameClientState)this.getState()).getFactionManager().getFaction(this.getFactionId());
            var3.x = var6.getColor().x;
            var3.y = var6.getColor().y;
            var3.z = var6.getColor().z;
        } else if (((GameClientState)this.getState()).getPlayer().getFactionId() == this.getFactionId() && ((GameClientState)this.getState()).getFactionManager().existsFaction(this.getFactionId()) && ((GameClientState)this.getState()).getFactionManager().getFaction(this.getFactionId()).isFactionMode(2)) {
            var3.x = var4 + 1.0F;
            var3.y = var4;
            var3.z = var4;
        } else {
            var3.x = var4 + 0.3F;
            var3.y = 0.3F;
            var3.z = var4;
        }
    }

    public Vector3f getRelativeUniverseServerPosition(SimpleTransformableSendableObject var1) {
        assert this.isOnServer();

        Sector var4 = ((GameServerState)this.getState()).getUniverse().getSector(var1.getSectorId());
        Sector var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId());
        Vector3i var3;
        (var3 = new Vector3i()).sub(var4.pos, var2.pos);
        Vector3f var5;
        (var5 = new Vector3f((float)var3.x * ((GameStateInterface)this.state).getSectorSize(), (float)var3.y * ((GameStateInterface)this.state).getSectorSize(), (float)var3.z * ((GameStateInterface)this.state).getSectorSize())).add(this.getWorldTransform().origin);
        return var5;
    }

    public RemoteTransformable getRemoteTransformable() {
        return this.remoteTransformable;
    }

    public void setRemoteTransformable(RemoteTransformable var1) {
        this.remoteTransformable = var1;
    }

    public long getSlowdownStart() {
        return this.slowdownStart;
    }

    public Vector3i getTagSectorId() {
        return this.tagSectorId;
    }

    public TransformTimed getWorldTransform() {
        return this.remoteTransformable.getWorldTransform();
    }

    private boolean gravityChecksRequired() {
        boolean var1 = this.remoteTransformable.isSendFromClient() || this.isOnServer() && (!this.isConrolledByActivePlayer() || this.forcedCheckFlag);
        this.forcedCheckFlag = false;
        return var1;
    }

    protected void handleGravity() {
        if (this.getPhysicsDataContainer().getObject() instanceof RigidBody) {
            RigidBody var1 = (RigidBody)this.getPhysicsDataContainer().getObject();
            float var2 = 1.0F;
            if (this instanceof ConfigManagerInterface) {
                var2 = ((ConfigManagerInterface)this).getConfigManager().apply(StatusEffectType.THRUSTER_ANTI_GRAVITY, 1.0F);
            }

            this.tmp.set(this.gravity.getAcceleration());
            this.tmp.scale(var2);
            this.modivyGravity(this.tmp);
            if (this.gravity.source != null) {
                this.gravity.source.getWorldTransform().basis.transform(this.tmp);
            }

            var1.setGravity(this.tmp);
        }

    }

    public void modivyGravity(Vector3f var1) {
    }

    public int hashCode() {
        assert this.id < 2147483647 && this.id > -2147483648;

        return this.id;
    }

    public boolean equals(Object var1) {
        return var1 instanceof Identifiable && ((Identifiable)var1).getId() == this.getId();
    }

    protected boolean hasVirtual() {
        return ((GameStateInterface)this.getState()).isPhysicalAsteroids() || !(this instanceof FloatingRock);
    }

    public void engageWarp(String var1, boolean var2, long var3, Vector3i var5, int var6) {
        assert this.isOnServer();

        if (var1.equals("none")) {
            this.sendControllingPlayersServerMessage(new Object[]{429}, 3);
        } else if (var1.startsWith("ENTITY_")) {
            synchronized(this.getState()) {
                Sendable var18 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getUidObjectMap().get(var1);
                Sector var7 = null;
                Vector3f var8 = null;
                String var9 = null;
                Sector var10;
                if ((var10 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) == null) {
                    this.sendControllingPlayersServerMessage(new Object[]{430, this.getSectorId()}, 3);
                } else {
                    if (var18 != null) {
                        if (var18 instanceof SimpleTransformableSendableObject && var18 instanceof ManagedSegmentController && ((ManagedSegmentController)var18).getManagerContainer() instanceof StationaryManagerContainer) {
                            StationaryManagerContainer var11 = (StationaryManagerContainer)((ManagedSegmentController)var18).getManagerContainer();
                            boolean var12 = false;

                            for(int var13 = 0; var13 < var11.getWarpgate().getCollectionManagers().size(); ++var13) {
                                if (((WarpgateCollectionManager)var11.getWarpgate().getCollectionManagers().get(var13)).isValid()) {
                                    (var7 = ((GameServerState)this.getState()).getUniverse().getSector(((SimpleTransformableSendableObject)var18).getSectorId())).setActive(true);
                                    var8 = new Vector3f(((SimpleTransformableSendableObject)var18).getWorldTransform().origin);
                                    var9 = ((SimpleTransformableSendableObject)var18).getRealName();
                                    var12 = true;
                                    break;
                                }
                            }

                            if (!var12) {
                                System.err.println("[WARPGATE] Object was loaded but no destination gate is valid!");
                                this.sendControllingPlayersServerMessage(new Object[]{431}, 3);
                            }
                        }
                    } else {
                        try {
                            List var19;
                            if ((var19 = ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getEntityTable().getByUIDExact(DatabaseEntry.removePrefix(var1), -1)).size() > 0) {
                                for(int var21 = 0; var21 < var19.size(); ++var21) {
                                    DatabaseEntry var22;
                                    if ((var22 = (DatabaseEntry)var19.get(var21)).type != SimpleTransformableSendableObject.EntityType.PLANET_SEGMENT.dbTypeId && var22.type != SimpleTransformableSendableObject.EntityType.SPACE_STATION.dbTypeId) {
                                        this.sendControllingPlayersServerMessage(new Object[]{433}, 3);
                                    } else if (StationaryManagerContainer.getActiveWarpGate(var1) != null) {
                                        var7 = ((GameServerState)this.getState()).getUniverse().getSector(var22.sectorPos, true);
                                        var8 = new Vector3f(var22.pos);
                                        var9 = var22.realName;
                                    } else {
                                        this.sendControllingPlayersServerMessage(new Object[]{432}, 3);
                                    }
                                }
                            } else {
                                this.sendControllingPlayersServerMessage(new Object[]{434}, 3);
                            }
                        } catch (SQLException var14) {
                            var14.printStackTrace();
                        } catch (IOException var15) {
                            var15.printStackTrace();
                        }
                    }

                    if (var7 == null) {
                        this.sendControllingPlayersServerMessage(new Object[]{435}, 3);
                    } else {
                        Vector3d var20;
                        if ((var20 = new Vector3d((double)(var7.pos.x - var10.pos.x), (double)(var7.pos.y - var10.pos.y), (double)(var7.pos.z - var10.pos.z))).length() < (double)var6) {
                            this.getNetworkObject().graphicsEffectModifier.add((byte)1);
                            SectorSwitch var23;
                            if ((var23 = ((GameServerState)this.getState()).getController().queueSectorSwitch(this, var7.pos, 1, false, true, true)) != null) {
                                var23.delay = System.currentTimeMillis() + var3;
                                var23.jumpSpawnPos = new Vector3f(var8);
                                var23.keepJumpBasisWithJumpPos = true;
                                var23.executionGraphicsEffect = 2;
                                this.sendControllingPlayersServerMessage(new Object[]{436, var9, var7.pos}, 1);
                            } else {
                                this.sendControllingPlayersServerMessage(new Object[]{437}, 3);
                            }
                        } else {
                            this.sendControllingPlayersServerMessage(new Object[]{438, StringTools.formatPointZero(var20.length()), var6}, 3);
                        }
                    }

                }
            }
        } else {
            if (var1.startsWith(FTLTable.DIRECT_PREFIX)) {
                if (!var2) {
                    this.sendControllingPlayersServerMessage(new Object[]{439}, 3);
                    return;
                }

                Sector var17;
                if ((var17 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) == null) {
                    this.sendControllingPlayersServerMessage(new Object[]{440, this.getSectorId()}, 3);
                    return;
                }

                this.warpArbitrary(var5, var3, var17, var6);
            }

        }
    }

    private void warpArbitrary(Vector3i var1, long var2, Sector var4, int var5) {
        var1 = new Vector3i(var1);
        Vector3d var6;
        if ((var6 = new Vector3d((double)(var1.x - var4.pos.x), (double)(var1.y - var4.pos.y), (double)(var1.z - var4.pos.z))).length() < (double)var5) {
            this.getNetworkObject().graphicsEffectModifier.add((byte)1);

            try {
                Sector var9;
                if ((var9 = ((GameServerState)this.getState()).getUniverse().getSector(var1)).getSectorType() == SectorType.PLANET) {
                    this.sendControllingPlayersServerMessage(new Object[]{441, var1.toStringPure()}, 1);
                    ++var1.y;
                    this.warpArbitrary(var1, var2, var4, var5);
                } else {
                    Iterator var10 = var9.getEntities().iterator();

                    SimpleTransformableSendableObject var7;
                    do {
                        if (!var10.hasNext()) {
                            SectorSwitch var11;
                            if ((var11 = ((GameServerState)this.getState()).getController().queueSectorSwitch(this, var1, 1, false, true, true)) != null) {
                                var11.delay = System.currentTimeMillis() + var2;
                                var11.jumpSpawnPos = new Vector3f(0.0F, 0.0F, 0.0F);
                                var11.keepJumpBasisWithJumpPos = true;
                                var11.executionGraphicsEffect = 2;
                                this.sendControllingPlayersServerMessage(new Object[]{443, var1.toStringPure()}, 1);
                                return;
                            }

                            this.sendControllingPlayersServerMessage(new Object[]{444}, 3);
                            return;
                        }
                    } while(!(var7 = (SimpleTransformableSendableObject)var10.next()).isHomeBase() || var7.isHomeBaseFor(this.getFactionId()));

                    this.sendControllingPlayersServerMessage(new Object[]{442, var1.toStringPure()}, 1);
                    ++var1.y;
                    this.warpArbitrary(var1, var2, var4, var5);
                }
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        } else {
            this.sendControllingPlayersServerMessage(new Object[]{445, StringTools.formatPointZero(var6.length()), var5}, 3);
        }
    }

    protected boolean isCheckSectorActive() {
        return true;
    }

    public boolean isClientSectorIdValidForSpawning(int var1) {
        return var1 == ((GameClientState)this.getState()).getCurrentSectorId() || this.isPlayerNeighbor(var1);
    }

    public boolean isConrolledByActivePlayer() {
        return this instanceof PlayerControllable && !((PlayerControllable)this).getAttachedPlayers().isEmpty();
    }

    public boolean isFlagPhysicsInit() {
        return this.flagPhysicsInit;
    }

    public void setFlagPhysicsInit(boolean var1) {
        this.flagPhysicsInit = var1;
    }

    public boolean isGravitySource() {
        return false;
    }

    public boolean isHomeBase() {
        return this.isHomeBaseFor(this.getFactionId());
    }

    public boolean isHomeBaseFor(int var1) {
        if (this.getFactionId() != 0 && var1 == this.getFactionId()) {
            boolean var2;
            Faction var3;
            if (var2 = (var3 = ((FactionState)this.getState()).getFactionManager().getFaction(var1)) != null && var3.getHomebaseUID().equals(this.getUniqueIdentifier())) {
                if (var3 != null && var3.isNPC()) {
                    return true;
                }

                if (var3 != null && var3.lastSystemSectors.size() == 0 && var3.factionPoints < 0.0F) {
                    return false;
                }
            }

            return var2;
        } else {
            return false;
        }
    }

    public boolean isImmediateStuck() {
        return this.immediateStuck;
    }

    public void setImmediateStuck(boolean var1) {
        this.immediateStuck = var1;
    }

    public boolean isNeighbor(int var1, int var2) {
        if (this.isOnServer()) {
            Sector var5 = ((GameServerState)this.getState()).getUniverse().getSector(var1);
            Sector var6 = ((GameServerState)this.getState()).getUniverse().getSector(var2);
            if (var5 != null && var6 != null) {
                return Sector.isNeighbor(var5.pos, var6.pos);
            } else {
                System.err.println("WARNING while checking neighbor: " + var5 + "; " + var6 + ";    " + var1 + "; " + var2);
                return false;
            }
        } else {
            RemoteSector var3 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var1);
            RemoteSector var4 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var2);
            if (var3 != null && var4 != null) {
                return Sector.isNeighbor(var3.clientPos(), var4.clientPos());
            } else {
                System.err.println("WARNING while checking neighbor: " + var3 + "; " + var4 + ";    " + var1 + "; " + var2);
                return false;
            }
        }
    }

    public boolean isPlayerNeighbor(int var1) {
        RemoteSector var2;
        return (var2 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var1)) != null && Sector.isNeighbor(var2.clientPos(), ((GameClientState)this.getState()).getPlayer().getCurrentSector());
    }

    public void onPhysicsAdd() {
        assert !this.isOnServer() || (Sector)this.getPhysics().getState() == ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId());

        assert this.sectorId != -2;

        this.getPhysicsDataContainer().onPhysicsAdd();

        assert !this.isOnServer() || (Sector)this.getPhysics().getState() == ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId());

        if (this.isOnServer() || this.isClientSectorIdValidForSpawning(this.getSectorId())) {
            if (this.isOnServer()) {
                assert (Sector)this.getPhysics().getState() == ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId());

                assert this.getPhysics().getState() == this.getPhysicsState();

                this.getPhysics().addObject(this.getPhysicsDataContainer().getObject(), this.getPhysicsDataContainer().collisionGroup, this.getPhysicsDataContainer().collisionMask);
                return;
            }

            if (this.getSectorId() == ((GameClientState)this.getState()).getCurrentSectorId()) {
                this.getPhysics().addObject(this.getPhysicsDataContainer().getObject(), this.getPhysicsDataContainer().collisionGroup, this.getPhysicsDataContainer().collisionMask);
                return;
            }

            this.getPhysicsDataContainer().getObject().getWorldTransform(this.getWorldTransform());
            this.calcWorldTransformRelative(((GameClientState)this.getState()).getCurrentSectorId(), ((GameClientState)this.getState()).getPlayer().getCurrentSector());
            RigidBody var1;
            (var1 = this.getPhysics().getBodyFromShape(this.getPhysicsDataContainer().getShape(), 0.0F, new Transform(this.getWorldTransformOnClient()))).setCollisionFlags(2);
            var1.setUserPointer(this.getId());
            this.getPhysics().addObject(var1, this.getPhysicsDataContainer().collisionGroup, this.getPhysicsDataContainer().collisionMask);
            if (var1 instanceof RigidBodySegmentController) {
                ((RigidBodySegmentController)var1).virtualString = "virtC" + ((GameClientState)this.getState()).getPlayer().getCurrentSector() + "{orig" + this.getPhysicsDataContainer().getObject().getWorldTransform(new Transform()).origin + "}";
                ((RigidBodySegmentController)var1).virtualSec = new Vector3i(((GameClientState)this.getState()).getPlayer().getCurrentSector());
            }

            assert this.getPhysics().containsObject(var1);

            this.clientVirtualObject = var1;
            this.clientVirtualObject.setInterpolationWorldTransform(this.getWorldTransformOnClient());
            this.clientVirtualObject.activate(true);
        }

    }

    public void onPhysicsRemove() {
        this.getPhysicsDataContainer().onPhysicsRemove();

        try {
            if (this.isOnServer()) {
                if (((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId()) != null) {
                    this.getPhysics().removeObject(this.getPhysicsDataContainer().getObject());
                    return;
                }
            } else {
                this.getPhysics().removeObject(this.getPhysicsDataContainer().getObject());

                try {
                    if (this.clientVirtualObject != null) {
                        this.getPhysics().removeObject(this.clientVirtualObject);

                        assert !this.getPhysics().containsObject(this.clientVirtualObject) : this.clientVirtualObject;

                        this.clientVirtualObject = null;
                    }

                    return;
                } catch (Exception var1) {
                    var1.printStackTrace();
                }
            }

        } catch (SectorNotFoundRuntimeException var2) {
            var2.printStackTrace();
            System.err.println("[EXCEPTION] OBJECT REMOVAL FROM PHYSICS FAILED -> can continue");
        }
    }

    public void onSectorInactiveClient() {
    }

    public boolean overlapsAABB(SimpleTransformableSendableObject var1, Transform var2, float var3) {
        Vector3f var4 = new Vector3f();
        Vector3f var5 = new Vector3f();
        Vector3f var6 = new Vector3f();
        Vector3f var7 = new Vector3f();
        this.getTransformedAABB(var4, var5, var3, new Vector3f(), new Vector3f(), (Transform)null);
        var1.getTransformedAABB(var6, var7, var3, new Vector3f(), new Vector3f(), var2);
        return AabbUtil2.testAabbAgainstAabb2(var4, var5, var6, var7);
    }

    public void setMass(float var1) {
        this.getRemoteTransformable().setMass(var1);
    }

    public void removeGravity() {
        this.setGravity(noGrav, (SimpleTransformableSendableObject)null, false, false);
    }

    public void setPhysicsDataContainer(PhysicsDataContainer var1) {
        this.getRemoteTransformable().setPhysicsDataContainer(var1);
    }

    public void resetSlowdownStart() {
        this.slowdownStart = 0L;
    }

    public void scheduleGravity(Vector3f var1, SimpleTransformableSendableObject var2) {
        this.scheduledGravity = new GravityState();
        this.scheduledGravity.getAcceleration().set(var1);
        this.scheduledGravity.source = var2;
        this.scheduledGravity.forcedFromServer = false;
    }

    public void scheduleGravityWithBlockBelow(Vector3f var1, SimpleTransformableSendableObject var2) {
        this.scheduledGravity = new GravityState();
        this.scheduledGravity.getAcceleration().set(var1);
        this.scheduledGravity.source = var2;
        this.scheduledGravity.forcedFromServer = false;
        this.scheduledGravity.withBlockBelow = true;
    }

    public void scheduleGravityServerForced(Vector3f var1, SimpleTransformableSendableObject var2) {
        this.scheduledGravity = new GravityState();
        this.scheduledGravity.getAcceleration().set(var1);
        this.scheduledGravity.source = var2;
        this.scheduledGravity.forcedFromServer = true;
        this.forcedCheckFlag = true;
    }

    public void forceGravityCheck() {
        this.forcedCheckFlag = true;
    }

    protected void searchForGravity() {
        if (!this.isHidden() && (!(this instanceof AbstractCharacter) || ((AbstractCharacter)this).getOwnerState() == null || !((AbstractCharacter)this).getOwnerState().isSitting())) {
            Iterator var1 = ((GravityStateInterface)this.getState()).getCurrentGravitySources().iterator();

            SimpleTransformableSendableObject var2;
            do {
                if (!var1.hasNext()) {
                    return;
                }

                var2 = (SimpleTransformableSendableObject)var1.next();
            } while(this.getGravity().source == var2 || !var2.affectsGravityOf(this));

            if (var2.personalGravitySwitch) {
                System.err.println("[GRAVITY] FOUND GRAV FOR " + this + "; " + this.getState());
                if (var2 instanceof SegmentController && ((SegmentController)var2).getConfigManager().apply(StatusEffectType.GRAVITY_OVERRIDE_ENTITY_CENTRAL, 1.0F) != 1.0F) {
                    this.setGravity(new Vector3f(0.0F, 0.0F, 0.1F), var2, true, false);
                } else {
                    this.setGravity(new Vector3f(var2.personalGravity), var2, false, false);
                }
            } else {
                this.setGravity(new Vector3f(0.0F, -9.89F, 0.0F), var2, false, false);
            }
        }
    }

    public void setGravity(Vector3f var1, SimpleTransformableSendableObject<?> var2, boolean var3, boolean var4) {
        if (this.getGravity().source != var2) {
            System.err.println("[GRAVITY] " + this + " " + this.getState() + " SOURCE CHANGE " + this.getGravity().source + " -> " + var2);
            this.getGravity().source = var2;
            this.getGravity().setChanged(true);
        }

        if (this.getGravity().central != var3) {
            System.err.println("[GRAVITY] " + this + " " + this.getState() + " central CHANGE " + this.getGravity().central + " -> " + var3);
            this.getGravity().central = var3;
            this.getGravity().setChanged(true);
        }

        if (!this.getGravity().getAcceleration().equals(var1)) {
            System.err.println("[GRAVITY] " + this + " " + this.getState() + " Acceleration changed to " + var1);
            this.getGravity().getAcceleration().set(var1);
            this.getGravity().setChanged(true);
        }

        if (this.getGravity().isChanged()) {
            ((NetworkGravity)this.getNetworkObject().gravity.get()).gravityId = var2 != null ? var2.getId() : -1;
            ((NetworkGravity)this.getNetworkObject().gravity.get()).gravity.set(var1);
            ((NetworkGravity)this.getNetworkObject().gravity.get()).forcedFromServer = var4;
            this.getNetworkObject().gravity.setChanged(true);
            this.getNetworkObject().setChanged(true);
            System.err.println("[SIMPLETRANSFORMABLE] " + this.getState() + " " + this + " gravity change sent: " + this.getNetworkObject().gravity.get());
        }

    }

    public void setHiddenForced(boolean var1) {
        this.hidden = var1;
    }

    public abstract String toNiceString();

    public final int getOriginalFactionId() {
        return this.factionId;
    }

    protected boolean addToPhysicsOnInit() {
        return true;
    }

    public void warpTransformable(float var1, float var2, float var3, boolean var4, LocalSectorTransition var5) {
        Transform var6;
        (var6 = new Transform(this.getWorldTransform())).origin.set(var1, var2, var3);
        this.warpTransformable(var6, var4, false, (LocalSectorTransition)null);
    }

    public void warpTransformable(Transform var1, boolean var2, boolean var3, LocalSectorTransition var4) {
        if (this.getPhysicsDataContainer().isInitialized()) {
            PairCachingGhostObjectAlignable var5;
            if (this.getPhysicsDataContainer().getObject() instanceof PairCachingGhostObjectAlignable && (var5 = (PairCachingGhostObjectAlignable)this.getPhysicsDataContainer().getObject()).getAttached() != null && var5.localWorldTransform != null) {
                Transform var6;
                (var6 = new Transform(var5.getAttached().getWorldTransform())).inverse();
                Transform var7 = new Transform(var1);
                var6.mul(var7);
                var5.localWorldTransform.set(var6);
            }

            this.getRemoteTransformable().warp(new Transform(var1), var3);
            if (this.isOnServer() && var2) {
                if (this.getPhysicsDataContainer().getObject() instanceof RigidBody) {
                    RigidBody var8 = (RigidBody)this.getPhysicsDataContainer().getObject();
                    Vector3f var9 = new Vector3f();
                    Vector3f var10 = new Vector3f();
                    var8.getLinearVelocity(var9);
                    var8.getAngularVelocity(var10);
                    this.getRemoteTransformable().broadcastTransform(new Transform(var1), var9, var10, var4, this.getNetworkObject());
                    return;
                }

                this.getRemoteTransformable().broadcastTransform(new Transform(var1), new Vector3f(), new Vector3f(), var4, this.getNetworkObject());
            }
        }

    }

    public boolean handleCollision(int var1, RigidBody var2, RigidBody var3, SolverConstraint var4) {
        return false;
    }

    public void onSmootherSet(Transform var1) {
    }

    public abstract String getRealName();

    public long getLastWrite() {
        return this.lastWrite;
    }

    public void setLastWrite(long var1) {
        this.lastWrite = var1;
    }

    public void executeGraphicalEffectServer(byte var1) {
        this.getNetworkObject().graphicsEffectModifier.add(var1);
    }

    public void executeGraphicalEffectClient(byte var1) {
    }

    public SpawnController getSpawnController() {
        return this.spawnController;
    }

    public float getIndicatorDistance() {
        return 0.0F;
    }

    public float getIndicatorMaxDistance(RType var1) {
        return ((GameStateInterface)this.getState()).getSectorSize();
    }

    public boolean hasChangedForDb() {
        return this.changed;
    }

    public void setChangedForDb(boolean var1) {
        this.changed = var1;
    }

    public void setInvisibleNextDraw() {
        this.invisibleNextDraw = -10;
    }

    public String getInfo() {
        return "LoadedEntity [uid=" + this.getUniqueIdentifier() + ", type=" + this.getType().name() + "]";
    }

    public boolean isInvisibleNextDraw() {
        return this.invisibleNextDraw < 2 || this.adminInvisibility;
    }

    public EffectElementManager<?, ?, ?> getEffect(Damager var1, short var2) {
        if (var2 == 0) {
            return null;
        } else if (var1 != null && var1 instanceof ManagedSegmentController) {
            return ((EffectManagerContainer)((ManagedSegmentController)var1).getManagerContainer()).getEffect(var2);
        } else if (this instanceof ManagedSegmentController) {
            return ((EffectManagerContainer)((ManagedSegmentController)this).getManagerContainer()).getEffect(var2);
        } else {
            System.err.println(this.getState() + " SEVERE WARNING:  NO EFFECT FOUND FOR: DAMAGER: " + var1 + " -> " + this + "; ");
            return null;
        }
    }

    public boolean isInAdminInvisibility() {
        return this.adminInvisibility;
    }

    public boolean isChainedToSendFromClient() {
        return false;
    }

    public ObjectOpenHashSet<AICreature<? extends AIPlayer>> getAttachedAffinity() {
        return this.attachedAffinity;
    }

    public float getProhibitingBuildingAroundOrigin() {
        return this.prohibitingBuildingAroundOrigin;
    }

    public void setProhibitingBuildingAroundOrigin(float var1) {
        this.prohibitingBuildingAroundOrigin = var1;
    }

    public String getAdditionalObjectInformation() {
        return null;
    }

    public Light getLight() {
        return this.light;
    }

    public void setLight(Light var1) {
        this.light = var1;
    }

    public void announceLag(long var1) {
        if (System.currentTimeMillis() - this.lastLagSent > 1000L) {
            assert this.getState().isSynched();

            this.getNetworkObject().lagAnnouncement.add(var1);
            this.lastLagSent = System.currentTimeMillis();
        }

    }

    public long getCurrentLag() {
        return this.currentLag;
    }

    public boolean isWarpToken() {
        return this.warpToken;
    }

    public void setWarpToken(boolean var1) {
        this.warpToken = var1;
    }

    public long getSectorChangedTime() {
        return this.sectorChangedTime;
    }

    public void setSectorChangedTime(long var1) {
        this.sectorChangedTime = var1;
    }

    public void onSectorSwitchServer(Sector var1) {
    }

    public boolean isClientCleanedUp() {
        return this.clientCleanedUp;
    }

    public void setClientCleanedUp(boolean var1) {
        this.clientCleanedUp = var1;
    }

    public boolean isInClientRange() {
        boolean var1 = ((GameClientState)this.getState()).getCurrentSectorEntities().containsKey(this.getId());
        boolean var10000 = this.inClientRange;
        return var1;
    }

    public void setInClientRange(boolean var1) {
        this.inClientRange = var1;
    }

    public void setNeedsPositionCheckOnLoad(boolean var1) {
        this.needsPositionCheckOnLoad = var1;
    }

    public float getDamageGivenMultiplier() {
        return 1.0F;
    }

    public boolean isNPCFactionControlledAI() {
        return false;
    }

    public boolean isAIControlled() {
        return false;
    }

    public BoundingSphere getBoundingSphere() {
        return this.boundingSphere;
    }

    public BoundingSphere getBoundingSphereTotal() {
        return this.boundingSphereTotal;
    }

    public boolean isPrivateNetworkObject() {
        return false;
    }

    public String getUniqueIdentifierFull() {
        return this.getType().dbPrefix + this.getUniqueIdentifier();
    }

    public float getReconStrength() {
        return 0.0F;
    }

    public float getStealthStrength() {
        return 0.0F;
    }

    public boolean hasStealth(StealthLvl var1) {
        return false;
    }

    public boolean canSeeStructure(StealthReconEntity var1) {
        if (this.getReconStrength() - var1.getStealthStrength() >= (float)VoidElementManager.RECON_DIFFERENCE_MIN_CLOAKING) {
            return true;
        } else if (var1 instanceof ManagedSegmentController) {
            return !((SegmentController)var1).hasStealth(StealthLvl.CLOAKING);
        } else {
            return false;
        }
    }

    public boolean canSeeIndicator(StealthReconEntity var1) {
        if (this.getReconStrength() - var1.getStealthStrength() >= (float)VoidElementManager.RECON_DIFFERENCE_MIN_JAMMING) {
            return true;
        } else if (var1 instanceof ManagedSegmentController) {
            return !((SegmentController)var1).hasStealth(StealthLvl.JAMMING);
        } else {
            return false;
        }
    }

    public Vector3i getSystem(Vector3i var1) {
        Sendable var2;
        if ((var2 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())) != null && var2 instanceof RemoteSector) {
            RemoteSector var3 = (RemoteSector)var2;
            if (this.isOnServer()) {
                VoidSystem.getContainingSystem(var3.getServerSector().pos, var1);
                return var1;
            } else {
                VoidSystem.getContainingSystem(var3.clientPos(), var1);
                return var1;
            }
        } else {
            return null;
        }
    }

    public Vector3i getSector(Vector3i var1) {
        Sendable var2;
        if ((var2 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())) != null && var2 instanceof RemoteSector) {
            RemoteSector var3 = (RemoteSector)var2;
            if (this.isOnServer()) {
                var1.set(var3.getServerSector().pos);
                return var1;
            } else {
                var1.set(var3.clientPos());
                return var1;
            }
        } else {
            return null;
        }
    }

    public boolean canSeeReactor(SimpleTransformableSendableObject<?> var1) {
        return this.getReconStrength() - var1.getStealthStrength() >= (float)VoidElementManager.RECON_DIFFERENCE_MIN_REACTOR;
    }

    public boolean canSeeChambers(SimpleTransformableSendableObject<?> var1) {
        return this.getReconStrength() - var1.getStealthStrength() >= (float)VoidElementManager.RECON_DIFFERENCE_MIN_CHAMBERS;
    }

    public boolean canSeeWeapons(SimpleTransformableSendableObject<?> var1) {
        return this.getReconStrength() - var1.getStealthStrength() >= (float)VoidElementManager.RECON_DIFFERENCE_MIN_WEAPONS;
    }

    public boolean hasAnyReactors() {
        return false;
    }

    public InterEffectContainer getEffectContainer() {
        return this.effectContainer;
    }

    public long getOwnerId() {
        AbstractOwnerState var1;
        return (var1 = this.getOwnerState()) == null ? -9223372036854775808L : var1.getDbId();
    }

    public abstract CollisionType getCollisionType();

    public boolean isTracked() {
        return this.tracked;
    }

    public void setTracked(boolean var1) {
        if (this.tracked != var1 && !this.isOnServer()) {
            ((GameClientState)this.getState()).getController().flagTrackingChanged();
        }

        this.tracked = var1;
    }

    public RType getRelationTo(SimpleTransformableSendableObject<?> var1) {
        return ((FactionState)this.getState()).getFactionManager().getRelation(this.getFactionId(), var1.getFactionId());
    }

    public static enum EntityType {
        SHIP(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_16;
            }
        }, true, 5, "ENTITY_SHIP_", 10, TopLevelType.SEGMENT_CONTROLLER),
        SHOP(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_17;
            }
        }, true, 1, "ENTITY_SHOP_", 8, TopLevelType.SEGMENT_CONTROLLER),
        DEATH_STAR(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_18;
            }
        }, false, -1, "ENTITY_DEATHSTAR_", 0, TopLevelType.SEGMENT_CONTROLLER),
        ASTEROID(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_28;
            }
        }, true, 3, "ENTITY_FLOATINGROCK_", 0, TopLevelType.SEGMENT_CONTROLLER),
        ASTRONAUT(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_20;
            }
        }, true, -1, "ENTITY_PLAYERCHARACTER_", 0, TopLevelType.ASTRONAUT),
        NPC(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_21;
            }
        }, true, -1, "ENTITY_NPC_", 0, TopLevelType.ASTRONAUT),
        SPACE_STATION(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_22;
            }
        }, true, 2, "ENTITY_SPACESTATION_", 9, TopLevelType.SEGMENT_CONTROLLER),
        PLANET_SEGMENT(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_23;
            }
        }, true, 4, "ENTITY_PLANET_", 0, TopLevelType.SEGMENT_CONTROLLER),
        PLANET_CORE(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_24;
            }
        }, true, -1, "ENTITY_PLANETCORE_", 0, TopLevelType.OTHER_SPACE),
        BLACK_HOLE(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_25;
            }
        }, false, -1, "ENTITY_BLACKHOLE_", 0, TopLevelType.OTHER_SPACE),
        SUN(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_26;
            }
        }, false, -1, "ENTITY_SUN_", 0, TopLevelType.OTHER_SPACE),
        VEHICLE(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_27;
            }
        }, false, -1, "ENTITY_VEHICLE_", 0, TopLevelType.SEGMENT_CONTROLLER),
        ASTEROID_MANAGED(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_19;
            }
        }, true, 6, "ENTITY_FLOATINGROCKMANAGED_", 0, TopLevelType.SEGMENT_CONTROLLER),
        SPACE_CREATURE(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_29;
            }
        }, true, 7, "ENTITY_SPACECREATURE_", 0, TopLevelType.OTHER_SPACE),
        PLANET_ICO(new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SIMPLETRANSFORMABLESENDABLEOBJECT_31;
            }
        }, true, 8, "ENTITY_PLANETICO_", 0, TopLevelType.SEGMENT_CONTROLLER);

        public static SimpleTransformableSendableObject.EntityType[] usedArray;
        public static SimpleTransformableSendableObject.EntityType[] byDbId;
        private final Translatable name;
        public final boolean used;
        public final int dbTypeId;
        public final String dbPrefix;
        public final int mapSprite;
        public final TopLevelType topLevelType;

        public final BlueprintClassification getDefaultClassification() {
            switch(this) {
                case ASTEROID:
                    return BlueprintClassification.NONE_ASTEROID;
                case ASTEROID_MANAGED:
                    return BlueprintClassification.NONE_ASTEROID_MANAGED;
                case PLANET_SEGMENT:
                    return BlueprintClassification.NONE_PLANET;
                case SHOP:
                    return BlueprintClassification.NONE_SHOP;
                case SPACE_STATION:
                    return BlueprintClassification.NONE_STATION;
                case PLANET_ICO:
                    return BlueprintClassification.NONE_ICO;
                default:
                    return BlueprintClassification.NONE;
            }
        }

        private EntityType(Translatable var3, boolean var4, int var5, String var6, int var7, TopLevelType var8) {
            this.name = var3;
            this.used = var4;
            this.dbTypeId = var5;
            this.dbPrefix = var6;
            this.mapSprite = var7;
            this.topLevelType = var8;
        }

        public final String getName() {
            return this.name.getName(this);
        }

        public static SimpleTransformableSendableObject.EntityType[] getUsed() {
            if (usedArray == null) {
                ObjectArrayList var0 = new ObjectArrayList();

                int var1;
                for(var1 = 0; var1 < values().length; ++var1) {
                    if (values()[var1].used) {
                        var0.add(values()[var1]);
                    }
                }

                usedArray = new SimpleTransformableSendableObject.EntityType[var0.size()];

                for(var1 = 0; var1 < usedArray.length; ++var1) {
                    usedArray[var1] = (SimpleTransformableSendableObject.EntityType)var0.get(var1);
                }
            }

            assert usedArray != null;

            return usedArray;
        }

        public static SimpleTransformableSendableObject.EntityType getByDatabaseId(int var0) {
            SimpleTransformableSendableObject.EntityType var1;
            if ((var1 = byDbId[var0]) == null) {
                throw new IllegalArgumentException("Database type doesnt exist: " + var0);
            } else {
                return var1;
            }
        }

        static {
            int var0 = 0;
            SimpleTransformableSendableObject.EntityType[] var1;
            int var2 = (var1 = values()).length;

            int var3;
            SimpleTransformableSendableObject.EntityType var4;
            for(var3 = 0; var3 < var2; ++var3) {
                var4 = var1[var3];
                var0 = Math.max(var0, var4.dbTypeId + 1);
            }

            byDbId = new SimpleTransformableSendableObject.EntityType[var0];
            var2 = (var1 = values()).length;

            for(var3 = 0; var3 < var2; ++var3) {
                if ((var4 = var1[var3]).dbTypeId >= 0) {
                    byDbId[var4.dbTypeId] = var4;
                }
            }

        }
    }

    class TransformTimedSet extends TransformTimed {
        private TransformTimedSet() {
        }

        public void set(Transform var1) {
            if (!var1.equals(this)) {
                this.lastChanged = SimpleTransformableSendableObject.this.getState().getUpdateTime();
            }

            super.set(var1);
        }
    }

    static class GenericEffectSet extends InterEffectContainer {
        private GenericEffectSet() {
        }

        public InterEffectSet[] setupEffectSets() {
            InterEffectSet[] var1 = new InterEffectSet[1];

            for(int var2 = 0; var2 < var1.length; ++var2) {
                var1[var2] = new InterEffectSet();
            }

            return var1;
        }

        public InterEffectSet get(HitReceiverType var1) {
            return this.sets[0];
        }

        public void update(ConfigEntityManager var1) {
        }
    }
}
