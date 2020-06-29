//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.world;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import obfuscated.K;
import org.schema.common.FastMath;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.EditableSendableSegmentController;
import org.schema.game.common.controller.FloatingRock;
import org.schema.game.common.controller.FloatingRockManaged;
import org.schema.game.common.controller.Planet;
import org.schema.game.common.controller.PlanetIco;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.TeamDeathStar;
import org.schema.game.common.controller.TransientSegmentController;
import org.schema.game.common.controller.SpaceStation.SpaceStationType;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.damage.effects.MetaWeaponEffectInterface;
import org.schema.game.common.controller.damage.effects.InterEffectHandler.InterEffectType;
import org.schema.game.common.controller.damage.projectile.ParticleHitCallback;
import org.schema.game.common.controller.damage.projectile.ProjectileController;
import org.schema.game.common.controller.database.DatabaseEntry;
import org.schema.game.common.controller.database.DatabaseInsertable;
import org.schema.game.common.controller.database.DatabaseEntry.EntityTypeNotFoundException;
import org.schema.game.common.controller.database.tables.SectorItemTable;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ParticleHandler;
import org.schema.game.common.controller.elements.PulseController;
import org.schema.game.common.controller.elements.PulseHandler;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.ShieldLocalAddOn;
import org.schema.game.common.controller.generator.AsteroidCreatorThread.AsteroidType;
import org.schema.game.common.controller.io.IOFileManager;
import org.schema.game.common.controller.rails.RailRequest;
import org.schema.game.common.controller.rails.RailRelation.DockingPermission;
import org.schema.game.common.data.Dodecahedron;
import org.schema.game.common.data.Icosahedron;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.VoidUniqueSegmentPiece;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.explosion.AfterExplosionCallback;
import org.schema.game.common.data.mission.spawner.DefaultSpawner;
import org.schema.game.common.data.mission.spawner.SpawnMarker;
import org.schema.game.common.data.mission.spawner.component.SpawnComponentCreature;
import org.schema.game.common.data.mission.spawner.component.SpawnComponentDestroySpawnerAfterCount;
import org.schema.game.common.data.mission.spawner.condition.SpawnConditionCreatureCountOnAffinity;
import org.schema.game.common.data.mission.spawner.condition.SpawnConditionPlayerProximity;
import org.schema.game.common.data.mission.spawner.condition.SpawnConditionTime;
import org.schema.game.common.data.physics.CubeRayCastResult;
import org.schema.game.common.data.physics.ModifiedDynamicsWorld;
import org.schema.game.common.data.physics.PairCachingGhostObjectAlignable;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.player.AbstractCharacter;
import org.schema.game.common.data.player.AbstractOwnerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.common.data.player.inventory.FreeItem;
import org.schema.game.common.data.player.inventory.NoSlotFreeException;
import org.schema.game.common.data.world.SectorInformation.PlanetType;
import org.schema.game.common.data.world.SectorInformation.SectorType;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.common.data.world.space.PlanetCore;
import org.schema.game.common.util.Collisionable;
import org.schema.game.server.controller.BluePrintController;
import org.schema.game.server.controller.EntityAlreadyExistsException;
import org.schema.game.server.controller.EntityNotFountException;
import org.schema.game.server.controller.SectorSwitch;
import org.schema.game.server.controller.SectorUtil;
import org.schema.game.server.controller.gameConfig.GameConfig;
import org.schema.game.server.data.CreatureType;
import org.schema.game.server.data.Galaxy;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.blueprint.ChildStats;
import org.schema.game.server.data.blueprint.SegmentControllerOutline;
import org.schema.game.server.data.blueprintnw.BlueprintEntry;
import org.schema.game.server.data.simulation.npc.NPCFaction;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.debug.DebugDrawer;
import org.schema.schine.graphicsengine.forms.debug.DebugLine;
import org.schema.schine.graphicsengine.forms.debug.DebugPoint;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.physics.Physical;
import org.schema.schine.physics.Physics;
import org.schema.schine.physics.PhysicsState;
import org.schema.schine.resource.DiskWritable;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.UniqueInterface;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.TagSerializable;
import org.schema.schine.resource.tag.Tag.Type;

public class Sector implements Damager, DatabaseInsertable, ParticleHandler, PulseHandler, PhysicsState, UniqueInterface, TagSerializable {
    public static final AsteroidType[] asteroidsByTemperature = new AsteroidType[AsteroidType.values().length];
    public static final int SECTOR_INITIAL = -2;
    public static final int SECTOR_FREE = -1;
    public static final Vector3i DEFAULT_SECTOR = new Vector3i(2, 2, 2);
    public static final int CLEAR_CODE_NONE = 0;
    public static final int CLEAR_CODE_THREADED = 1;
    public static final int CLEAR_CODE_SEQENTIAL = 2;
    public static final int itemDataSize = 22;
    private static final int DEFAULT_SECTOR_SIZE_WITHOUT_MARGIN = 1000;
    private final Set<SimpleTransformableSendableObject<?>> entities = new ObjectOpenHashSet();
    public static int rockSize = 80;
    public static ByteBuffer buffer = ByteBuffer.allocate(10240);
    private static ObjectArrayFIFOQueue<ProjectileController> particleControllerPool = new ObjectArrayFIFOQueue();
    private static ObjectArrayFIFOQueue<PulseController> pulseControllerPool = new ObjectArrayFIFOQueue();
    final ArrayList<SegmentController> localAdd = new ArrayList();
    private final GameServerState state;
    private final Sector.SimpleTransformableSendableObjectList[] vicinityObjects = new Sector.SimpleTransformableSendableObjectList[27];
    private final ShortOpenHashSet missiles = new ShortOpenHashSet();
    private final Int2ObjectMap<FreeItem> items = new Int2ObjectOpenHashMap();
    private final ParticleHitCallback particleHitCallback = new ParticleHitCallback();
    public Vector3i pos;
    public final Set<EntityUID> entityUids = new ObjectOpenHashSet();
    public boolean terminated;
    Vector3f minOut = new Vector3f();
    Vector3f maxOut = new Vector3f();
    Vector3f minOutOther = new Vector3f();
    Vector3f maxOutOther = new Vector3f();
    Vector3f min;
    Vector3f max;
    float stormTimeAccumulated;
    Transform tmpSecPos;
    Vector3f tmpDir;
    Vector3f tmpOPos;
    Vector3f tmpOUp;
    Vector3f tmpORight;
    ArrayList<EditableSendableSegmentController> tmpL;
    ArrayList<AbstractCharacter<?>> tmpC;
    private float highestSubStep;
    private boolean active;
    private boolean sectorWrittenToDisk;
    private Physics physics;
    private RemoteSector remoteSector;
    private int protectionMode;
    private int id;
    private long lastPing;
    private boolean wasActive;
    private ProjectileController particleController;
    private PulseController pulseController;
    private boolean newCreatedSector;
    private long inactiveTime;
    private float distanceToSun;
    private StellarSystem system;
    private long lastWarning;
    private boolean flagRepair;
    private long dbID;
    private long lastMessage;
    private boolean transientSector;
    private long seed;
    private Random random;
    private PlanetCore planetCore;
    private PlanetType planetTypeCache;
    private SectorType sectorTypeCache;
    private SpaceStationType spaceStationTypeCache;
    private float sunIntensity;
    private Vector3i sunPosRel;
    private Vector3i sunPosRelSecond;
    private Vector3i sunOffset;
    private boolean changed;
    private long lastReplenished;
    private Sector.SectorSunDamager sunDamager;

    public Sector(GameServerState var1) {
        this.min = new Vector3f((float)(-rockSize << 5), (float)(-rockSize << 5), (float)(-rockSize << 5));
        this.max = new Vector3f((float)(rockSize << 5), (float)(rockSize << 5), (float)(rockSize << 5));
        this.stormTimeAccumulated = 0.0F;
        this.tmpSecPos = new Transform();
        this.tmpDir = new Vector3f();
        this.tmpOPos = new Vector3f();
        this.tmpOUp = new Vector3f();
        this.tmpORight = new Vector3f();
        this.tmpL = new ArrayList();
        this.tmpC = new ArrayList();
        this.active = false;
        this.protectionMode = Sector.SectorMode.PROT_NORMAL.code;
        this.distanceToSun = 0.0F;
        this.dbID = -1L;
        this.transientSector = true;
        this.changed = false;
        this.sunDamager = new Sector.SectorSunDamager();
        this.state = var1;
        this.setId(var1.getNextFreeObjectId());
        this.setPhysics(var1.getUniverse().getPhysicsInstance(this));
        this.getPhysics().getDynamicsWorld().setInternalTickCallback(new Sector.PhysicsCallback(), (Object)null);
        this.inactiveTime = System.currentTimeMillis();

        for(int var2 = 0; var2 < this.vicinityObjects.length; ++var2) {
            this.vicinityObjects[var2] = new Sector.SimpleTransformableSendableObjectList();
        }

    }

    public static InputStream getItemBinaryStream(Map<Integer, FreeItem> var0) throws IOException {
        byte[] var1 = SectorItemTable.getItemBinaryString(var0);
        return new ByteArrayInputStream(var1);
    }

    public static boolean isNeighbor(Vector3i var0, Vector3i var1) {
        return Math.abs(var0.x - var1.x) <= 1 && Math.abs(var0.y - var1.y) <= 1 && Math.abs(var0.z - var1.z) <= 1;
    }

    public static boolean isNeighborNotSelf(Vector3i var0, Vector3i var1) {
        return !var0.equals(var1) && Math.abs(var0.x - var1.x) <= 1 && Math.abs(var0.y - var1.y) <= 1 && Math.abs(var0.z - var1.z) <= 1;
    }

    public static boolean isNeighborNotSelf(int var0, int var1, int var2, int var3, int var4, int var5) {
        return (var0 != var3 || var1 != var4 || var2 != var5) && Math.abs(var0 - var3) <= 1 && Math.abs(var1 - var4) <= 1 && Math.abs(var2 - var5) <= 1;
    }

    public static boolean isNeighbor(int var0, int var1, int var2, int var3, int var4, int var5) {
        return Math.abs(var0 - var3) <= 1 && Math.abs(var1 - var4) <= 1 && Math.abs(var2 - var5) <= 1;
    }

    public static void applyBlackHoleGrav(StateInterface var0, Vector3i var1, Vector3i var2, Vector3i var3, int var4, Transform var5, Vector3f var6, Vector3f var7, Vector3i var8, Timer var9) {
        synchronized(var0.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var11 = var0.getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

            while(true) {
                while(true) {
                    Sendable var12;
                    Vector3i var15;
                    AbstractCharacter var21;
                    float var24;
                    float var25;
                    float var27;
                    do {
                        label70:
                        do {
                            while(var11.hasNext()) {
                                if ((var12 = (Sendable)var11.next()) instanceof AbstractCharacter && ((AbstractCharacter)var12).getSectorId() == var4) {
                                    continue label70;
                                }

                                if (var12 instanceof SegmentController && ((SegmentController)var12).getMass() > 0.0F && ((SegmentController)var12).getPhysicsDataContainer().getObject() != null && ((SegmentController)var12).getSectorId() == var4) {
                                    RigidBody var13 = (RigidBody)((SegmentController)var12).getPhysicsDataContainer().getObject();
                                    Vector3f var14 = new Vector3f();
                                    var13.getGravity(var14);
                                    var15 = new Vector3i((var1.x << 4) + var2.x - var3.x, (var1.y << 4) + var2.y - var3.y, (var1.z << 4) + var2.z - var3.z);
                                    var5.origin.set((float)var15.x, (float)var15.y, (float)var15.z);
                                    if (var0 instanceof GameServerState && var15.length() == 0.0F) {
                                        FTLConnection var16;
                                        GameServerState var22;
                                        if ((var16 = (var22 = (GameServerState)var0).getDatabaseIndex().getTableManager().getFTLTable().getFtl(var3, "BH_" + var3.x + "_" + var3.y + "_" + var3.z + "_OO_" + var8.x + "_" + var8.y + "_" + var8.z)) != null) {
                                            Vector3i var17;
                                            (var17 = (Vector3i)var16.to.get(0)).add(6, 6, 6);
                                            ((SegmentController)var12).getNetworkObject().graphicsEffectModifier.add((byte)1);
                                            SectorSwitch var23;
                                            if ((var23 = var22.getController().queueSectorSwitch((SegmentController)var12, var17, 1, false, true, true)) != null) {
                                                System.err.println("[SERVER][BlackHole] JUMPING TO: BH_" + var3.x + "_" + var3.y + "_" + var3.z + "; offset: " + var8);
                                                var23.delay = System.currentTimeMillis() + 8000L;
                                                var23.jumpSpawnPos = new Vector3f();
                                                var23.keepJumpBasisWithJumpPos = true;
                                                var23.executionGraphicsEffect = 2;
                                                ((SegmentController)var12).sendControllingPlayersServerMessage(new Object[]{417, var17}, 1);
                                            }
                                        } else {
                                            System.err.println("[SERVER][BlackHole] cannot warp player. no jump route for black hole found: BH_" + var3.x + "_" + var3.y + "_" + var3.z + "_OO_" + var8.x + "_" + var8.y + "_" + var8.z + "; offset: " + var8);
                                        }
                                    }

                                    var5.origin.scale(((GameStateInterface)var0).getSectorSize());
                                    var6.set(((SegmentController)var12).getWorldTransform().origin);
                                    ((SegmentController)var12).getSegmentBuffer().getBoundingBox().getSize();
                                    var7.sub(var5.origin, var6);
                                    if (var7.length() > 0.0F) {
                                        var25 = var7.length();
                                        var27 = ((GameStateInterface)var0).getGameState().getMaxGalaxySpeed() * 0.33F;
                                        var24 = ((GameStateInterface)var0).getSectorSize() * 16.0F / 2.0F;
                                        var7.normalize();
                                        var7.scale(Math.max(0.25F, 1.0F - var25 / var24) * var27);
                                        var13.setGravity(var7);
                                        var13.applyGravity();
                                        Vector3f var20;
                                        if ((var20 = var13.getLinearVelocity(new Vector3f())).length() > ((GameStateInterface)var0).getGameState().getMaxGalaxySpeed() * 3.0F) {
                                            var20.normalize();
                                            var20.scale(((GameStateInterface)var0).getGameState().getMaxGalaxySpeed() * 3.0F);
                                        }
                                    }

                                    var13.setGravity(var14);
                                }
                            }

                            return;
                        } while((var21 = (AbstractCharacter)var12).isHidden());
                    } while(var21.getPhysicsDataContainer().getObject() instanceof PairCachingGhostObjectAlignable && ((PairCachingGhostObjectAlignable)var21.getPhysicsDataContainer().getObject()).getAttached() != null);

                    var21.getGhostObject();
                    new Vector3f();
                    var15 = new Vector3i((var1.x << 4) + var2.x - var3.x, (var1.y << 4) + var2.y - var3.y, (var1.z << 4) + var2.z - var3.z);
                    if (var0 instanceof GameServerState && var15.length() == 0.0F) {
                        try {
                            if (var21.getGhostObject().getAttached() == null) {
                                var21.damage(100000.0F, ((GameServerState)var0).getUniverse().getSector(var3));
                            }
                        } catch (IOException var18) {
                            var18.printStackTrace();
                        }
                    } else {
                        var5.origin.set((float)var15.x, (float)var15.y, (float)var15.z);
                        var5.origin.scale(((GameStateInterface)var0).getSectorSize());
                        var6.set(((AbstractCharacter)var12).getWorldTransform().origin);
                        var7.sub(var5.origin, var6);
                        if (var7.length() > 0.0F && var21.getGhostObject().getAttached() == null) {
                            var24 = var7.length();
                            var25 = ((GameStateInterface)var0).getGameState().getMaxGalaxySpeed() * 0.73F;
                            var27 = ((GameStateInterface)var0).getSectorSize() * 16.0F / 2.0F;
                            var7.normalize();
                            var7.scale(Math.max(0.25F, 1.0F - var24 / var27) * var25 * var9.getDelta());
                            Transform var26 = new Transform();
                            var21.getGhostObject().getWorldTransform(var26);
                            var26.origin.add(var7);
                            var21.getGhostObject().setWorldTransform(var26);
                        }
                    }
                }
            }
        }
    }

    public static boolean isPersonalOrTestSector(Vector3i var0) {
        return var0.x == 2147483615 && var0.z == 2147483615;
    }

    public static boolean isTutorialSector(Vector3i var0) {
        return var0.x >= 2080000000 && var0.y >= 2080000000 && var0.z >= 2080000000 && var0.x < 2080000016 && var0.y < 2080000016 && var0.z < 2080000016;
    }

    public void clearVicinity() {
        for(int var1 = 0; var1 < this.vicinityObjects.length; ++var1) {
            this.vicinityObjects[var1].clear();
        }

    }

    public void updateVicinity(SimpleTransformableSendableObject var1) {
        Sector var2 = this.state.getUniverse().getSector(var1.getSectorId());
        if (isNeighbor(this.pos, var2.pos)) {
            int var3 = this.getVicinityIndex(var2.pos);
            this.vicinityObjects[var3].add(var1);
        }

    }

    public int getVicinityIndex(Vector3i var1) {
        int var2 = var1.x - this.pos.x + 1;
        int var3 = var1.x - this.pos.x + 1;
        return (var1.x - this.pos.x + 1) * 9 + var3 * 3 + var2;
    }

    public void addMetaItems() {
        this.state.getMetaObjectManager().getFromArchive(this.pos, this.items);
    }

    public void addRandomRock(GameServerState var1, long var2, int var4, int var5, int var6, Random var7, int var8) throws IOException {
        Vector3i var9 = new Vector3i();
        this.setRandomPos(var9, var7);
        FloatingRock var10;
        (var10 = new FloatingRock(var1)).setSeed(var2);
        int var18 = var7.nextInt(5) - 2;
        Vector3i var3 = StellarSystem.getPosFromSector(var9, new Vector3i());
        int var11;
        StellarSystem var19;
        if ((var19 = var1.getUniverse().getStellarSystemFromSecPos(var3)).getCenterSectorType() == SectorType.SUN) {
            float var20 = var19.getTemperature(this.pos);

            for(var11 = 0; var11 < AsteroidType.values().length && asteroidsByTemperature[var11].temperature <= var20; ++var11) {
            }

            var11 = Math.min(AsteroidType.values().length - 1, Math.max(0, var11 + var18));
            var10.setCreatorId(asteroidsByTemperature[var11].ordinal());
        } else {
            System.err.println("SET NON SUN " + var9);
            var10.setCreatorId(var7.nextInt(AsteroidType.values().length));
        }

        int var22 = ByteUtil.divSeg(var4 - 1) + 1;
        var11 = ByteUtil.divSeg(var5 - 1) + 1;
        var18 = ByteUtil.divSeg(var6 - 1) + 1;
        var10.setUniqueIdentifier(EntityType.ASTEROID.dbPrefix + System.currentTimeMillis() + "_" + this.pos.x + "_" + this.pos.y + "_" + this.pos.z + "_" + var8);
        var10.getMinPos().set(-var22, -var11, -var18);
        var10.getMaxPos().set(var22 - 1, var11 - 1, var18 - 1);
        var10.loadedMinPos = new Vector3i(var10.getMinPos());
        var10.loadedMaxPos = new Vector3i(var10.getMaxPos());
        var10.loadedGenSize = new Vector3i(var4, var5, var6);
        var10.setId(var1.getNextFreeObjectId());
        var10.setSectorId(this.getId());
        var10.initialize();
        var18 = 0;

        long var12;
        for(var12 = System.currentTimeMillis(); this.checkCollision(var10, var9) != null && var18 < 1000; ++var18) {
            this.setRandomPos(var9, var7);
        }

        long var14;
        if ((var14 = System.currentTimeMillis() - var12) > 10L) {
            System.err.println("[SECTOR] Placing ROCK took " + var14 + "ms");
        }

        if (var18 < 1000) {
            this.localAdd.add(var10);
            var1.getController().getSynchController().addImmediateSynchronizedObject(var10);
        } else {
            try {
                throw new RuntimeException("Could not place rock " + var10.getMinPos() + "; " + var10.getMaxPos());
            } catch (RuntimeException var16) {
                System.out.println("[ERRORLOG][SECTOR] Could not place rock " + var10.getMinPos() + "; " + var10.getMaxPos());
                System.out.println("[ERRORLOG][SECTOR] PRINTING AABB of all objects");
                Iterator var17 = this.localAdd.iterator();

                while(true) {
                    Sendable var21;
                    do {
                        do {
                            if (!var17.hasNext()) {
                                return;
                            }
                        } while(!((var21 = (Sendable)var17.next()) instanceof Physical));
                    } while(var21 instanceof SimpleTransformableSendableObject && ((SimpleTransformableSendableObject)var21).getSectorId() != var10.getSectorId());

                    Physical var23;
                    (var23 = (Physical)var21).getPhysicsDataContainer().getShape().getAabb(var23.getPhysicsDataContainer().getCurrentPhysicsTransform(), this.minOutOther, this.maxOutOther);
                    System.out.println("[ERRORLOG][SECTOR] " + var21 + ": [" + this.minOutOther + " " + this.maxOutOther + "]");
                }
            }
        }
    }

    private Sendable checkCollision(SegmentController var1, Vector3i var2) {
        long var3 = System.currentTimeMillis();
        if (var1 instanceof SegmentController) {
            SegmentController var5 = var1;
            var1.getInitialTransform().setIdentity();
            float var10001 = (float)var2.x;
            float var10002 = (float)var2.y;
            var1.getInitialTransform().origin.set(var10001, var10002, (float)var2.z);
            this.min.set((float)(var1.getMinPos().x - 2 << 5), (float)(var1.getMinPos().y - 2 << 5), (float)(var1.getMinPos().z - 2 << 5));
            this.max.set((float)(var1.getMaxPos().x + 2 << 5), (float)(var1.getMaxPos().y + 2 << 5), (float)(var1.getMaxPos().z + 2 << 5));
            AabbUtil2.transformAabb(this.min, this.max, 100.0F, var1.getInitialTransform(), this.minOut, this.maxOut);
            Iterator var6 = this.localAdd.iterator();

            label41:
            while(true) {
                Sendable var9;
                do {
                    do {
                        if (!var6.hasNext()) {
                            break label41;
                        }
                    } while(!((var9 = (Sendable)var6.next()) instanceof Physical));
                } while(var9 instanceof SimpleTransformableSendableObject && ((SimpleTransformableSendableObject)var9).getSectorId() != var5.getSectorId());

                if (var9 instanceof SegmentController) {
                    SegmentController var7 = (SegmentController)var9;
                    this.minOutOther.set((float)(var7.getMinPos().x - 2 << 5), (float)(var7.getMinPos().y - 2 << 5), (float)(var7.getMinPos().z - 2 << 5));
                    this.maxOutOther.set((float)(var7.getMaxPos().x + 2 << 5), (float)(var7.getMaxPos().y + 2 << 5), (float)(var7.getMaxPos().z + 2 << 5));
                    AabbUtil2.transformAabb(this.minOutOther, this.maxOutOther, 100.0F, var7.getInitialTransform(), this.minOutOther, this.maxOutOther);
                } else {
                    Physical var11;
                    (var11 = (Physical)var9).getPhysicsDataContainer().getShape().getAabb(var11.getInitialTransform(), this.minOutOther, this.maxOutOther);
                }

                if (AabbUtil2.testAabbAgainstAabb2(this.minOut, this.maxOut, this.minOutOther, this.maxOutOther)) {
                    long var12;
                    if ((var12 = System.currentTimeMillis() - var3) > 10L) {
                        System.err.println("[Sector] [Sector] collision test at " + var2 + " is true: trying another pos " + var12 + "ms");
                    }

                    return var9;
                }
            }
        }

        long var10;
        if ((var10 = System.currentTimeMillis() - var3) > 10L) {
            System.err.println("[Sector] No Collission: " + var10 + "ms");
        }

        return null;
    }

    public Sendable checkSectorCollision(SimpleTransformableSendableObject var1, Vector3f var2) {
        long var3 = System.currentTimeMillis();
        Transform var5;
        (var5 = new Transform()).basis.set(var1.getWorldTransform().basis);
        var5.origin.set(var2);
        var1.getRemoteTransformable().getPhysicsDataContainer().getShape().getAabb(var5, this.minOut, this.maxOut);
        synchronized(this.state.getLocalAndRemoteObjectContainer().getLocalObjects()){}

        label141: {
            Throwable var10000;
            label140: {
                Iterator var7;
                boolean var10001;
                try {
                    var7 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();
                } catch (Throwable var16) {
                    var10000 = var16;
                    var10001 = false;
                    break label140;
                }

                while(true) {
                    try {
                        while(true) {
                            if (!var7.hasNext()) {
                                break label141;
                            }

                            Sendable var18;
                            if ((var18 = (Sendable)var7.next()) instanceof Physical && (!(var18 instanceof SimpleTransformableSendableObject) || ((SimpleTransformableSendableObject)var18).getSectorId() == var1.getSectorId())) {
                                Physical var8;
                                (var8 = (Physical)var18).getPhysicsDataContainer().getShape().getAabb(var8.getPhysicsDataContainer().getCurrentPhysicsTransform(), this.minOutOther, this.maxOutOther);
                                if (AabbUtil2.testAabbAgainstAabb2(this.minOut, this.maxOut, this.minOutOther, this.maxOutOther)) {
                                    long var9;
                                    if ((var9 = System.currentTimeMillis() - var3) > 10L) {
                                        System.err.println("[Sector] collision test at " + var2 + " is true: trying another pos " + this.minOut + ", " + this.maxOut + " ---> " + this.minOutOther + ", " + this.maxOutOther + ": " + var9 + "ms");
                                    }

                                    return var18;
                                }
                            }
                        }
                    } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break;
                    }
                }
            }

            Throwable var17 = var10000;
            try {
                throw var17;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        long var6;
        if ((var6 = System.currentTimeMillis() - var3) > 10L) {
            System.err.println("[Sector] No Collission: " + var6 + "ms");
        }

        return null;
    }

    public void cleanUp() {
        synchronized(this.state.getLocalAndRemoteObjectContainer()) {
            ((ModifiedDynamicsWorld)this.getPhysics().getDynamicsWorld()).clean();
            this.state.getUniverse().addToFreePhysics(this.getPhysics(), this);
        }
    }

    private void doSunstorm(Timer var1, float var2) {
        this.stormTimeAccumulated += var1.getDelta();
        GameConfig var3 = this.state.getGameConfig();
        var2 = Math.max(0.1F, var2 * var3.sunMaxDelayBetweenHits);
        if (this.stormTimeAccumulated > var2) {
            this.tmpSecPos.setIdentity();
            Vector3i var12 = new Vector3i((this.system.getPos().x << 4) + this.sunPosRel.x - this.pos.x, (this.system.getPos().y << 4) + this.sunPosRel.y - this.pos.y, (this.system.getPos().z << 4) + this.sunPosRel.z - this.pos.z);
            Vector3i var4;
            if (this.sunPosRelSecond != null && (var4 = new Vector3i((this.system.getPos().x << 4) + this.sunPosRelSecond.x - this.pos.x, (this.system.getPos().y << 4) + this.sunPosRelSecond.y - this.pos.y, (this.system.getPos().z << 4) + this.sunPosRelSecond.z - this.pos.z)).length() < var12.length()) {
                var12 = var4;
            }

            this.tmpSecPos.origin.set((float)var12.x, (float)var12.y, (float)var12.z);
            this.tmpSecPos.origin.scale(this.state.getSectorSize());
            this.tmpL.clear();
            this.tmpC.clear();
            Iterator var17 = this.state.getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

            while(true) {
                while(var17.hasNext()) {
                    Sendable var14;
                    if ((var14 = (Sendable)var17.next()) instanceof Ship && ((SegmentController)var14).getSectorId() == this.getId()) {
                        this.tmpL.add((EditableSendableSegmentController)var14);
                    } else if (var14 instanceof AbstractCharacter && !((AbstractCharacter)var14).isHidden() && ((AbstractCharacter)var14).getSectorId() == this.getId()) {
                        this.tmpC.add((AbstractCharacter)var14);
                    }
                }

                for(int var18 = 0; var18 < this.tmpC.size(); ++var18) {
                    AbstractCharacter var16 = (AbstractCharacter)this.tmpC.get(var18);
                    if (Math.random() > (this.distanceToSun == 0.0F ? 0.001D : 5.0E-5D) && var16.getGravity().source == null) {
                        var16.damage(1.0F, this);
                        var16.sendControllingPlayersServerMessage(new Object[]{418}, 3);
                    }
                }

                if (this.tmpL.isEmpty()) {
                    return;
                }

                EditableSendableSegmentController var20;
                if ((var20 = (EditableSendableSegmentController)this.tmpL.get(this.getRandom().nextInt(this.tmpL.size()))).heatDamageId == null || !var20.heatDamageId.equals(this.system.getPos())) {
                    var20.heatDamageId = this.system.getPos();
                    var20.heatDamageStart = var1.currentTime;
                    var20.sendControllingPlayersServerMessage(new Object[]{419, (int)var3.sunDamageDelay}, 3);
                    return;
                }

                if ((double)(var1.currentTime - var20.heatDamageStart) < (double)(var3.sunDamageDelay * 1000.0F)) {
                    return;
                }

                this.tmpOPos.set(var20.getWorldTransform().origin);
                var2 = var20.getSegmentBuffer().getBoundingBox().getSize() / 3.0F;
                this.tmpDir.sub(this.tmpOPos, this.tmpSecPos.origin);
                float var8 = this.tmpDir.length();
                this.tmpDir.normalize();
                this.tmpOUp.set(0.0F, 1.0F, 0.0F);
                this.tmpORight.cross(this.tmpDir, this.tmpOUp);
                this.tmpORight.normalize();
                this.tmpOUp.cross(this.tmpORight, this.tmpDir);
                this.tmpOUp.normalize();
                this.tmpDir.scale(var8 + var2);
                this.tmpOPos.add(this.tmpSecPos.origin, this.tmpDir);
                this.tmpOUp.scale(var2 * (this.getRandom().nextFloat() - 0.5F) * 2.0F);
                this.tmpORight.scale(var2 * (this.getRandom().nextFloat() - 0.5F) * 2.0F);
                this.tmpOPos.add(this.tmpOUp);
                this.tmpOPos.add(this.tmpORight);
                if (EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn()) {
                    DebugPoint var9 = new DebugPoint(new Vector3f(this.tmpOPos), new Vector4f(1.0F, 1.0F, 0.0F, 1.0F), 10.0F);
                    DebugDrawer.points.add(var9);
                    DebugLine var19 = new DebugLine(this.tmpSecPos.origin, this.tmpOPos);
                    DebugDrawer.lines.add(var19);
                }

                ClosestRayResultCallback var10;
                if ((var10 = ((PhysicsExt)this.getPhysics()).testRayCollisionPoint(this.tmpSecPos.origin, this.tmpOPos, false, (SimpleTransformableSendableObject)null, (SegmentController)null, false, true, false)) != null && var10.hasHit() && var10 instanceof CubeRayCastResult && ((CubeRayCastResult)var10).getSegment() != null && ((CubeRayCastResult)var10).getSegment().getSegmentController().railController.isInAnyRailRelationWith(var20)) {
                    var20 = (EditableSendableSegmentController)((CubeRayCastResult)var10).getSegment().getSegmentController();
                    var2 = Math.max(0.1F, this.sunIntensity * var3.sunDamagePerBlock * (float)var20.railController.getRoot().getTotalElementsIncRails());
                    var2 = Math.min(var3.sunDamageMax, Math.max(var2, var3.sunDamageMin));
                    this.particleHitCallback.reset();
                    CubeRayCastResult var11 = (CubeRayCastResult)var10;
                    Transform var5;
                    (var5 = new Transform()).setIdentity();
                    var5.origin.set(var11.hitPointWorld);
                    var8 = var3.sunDamageRadius;
                    ManagerContainer var13;
                    if (var20 instanceof ManagedSegmentController && (var13 = ((ManagedSegmentController)var20).getManagerContainer()) instanceof ShieldContainerInterface) {
                        ShieldLocalAddOn var6 = ((ShieldContainerInterface)var13).getShieldAddOn().getShieldLocalAddOn();
                        Vector3f var7 = new Vector3f(var5.origin);
                        var20.getWorldTransformInverse().transform(var7);
                        var7.x += 16.0F;
                        var7.y += 16.0F;
                        var7.z += 16.0F;
                        ShieldLocal var15;
                        if ((var15 = var6.getShieldInRadius((ShieldContainerInterface)var13, var7)) != null && var15.active) {
                            var15.getShields();
                        }
                    }

                    var2 = var20.getConfigManager().apply(StatusEffectType.HULL_HEAT_DAMAGE_TAKEN, var2);
                    var20.addExplosion(this.sunDamager, DamageDealerType.EXPLOSIVE, HitType.ENVIROMENTAL, -9223372036854775808L, var5, var8, var2, true, new AfterExplosionCallback() {
                        public void onExplosionDone() {
                        }
                    }, 3);
                    if (var20 instanceof PlayerControllable && System.currentTimeMillis() - this.lastMessage > 3000L) {
                        var20.sendControllingPlayersServerMessage(new Object[]{420, var2}, 3);
                        this.lastMessage = System.currentTimeMillis();
                    }
                }

                this.stormTimeAccumulated = 0.0F;
                break;
            }
        }

    }

    public void fromTagStructure(Tag var1) {
        Tag[] var7 = (Tag[])var1.getValue();
        this.pos = (Vector3i)var7[0].getValue();

        assert this.pos != null;

        Tag[] var2;
        int var3 = (var2 = (Tag[])var7[1].getValue()).length;

        Tag var5;
        for(int var4 = 0; var4 < var3 && (var5 = var2[var4]).getType() != Type.FINISH; ++var4) {
            String var9 = (String)var5.getValue();

            try {
                this.entityUids.add(new EntityUID(var9, DatabaseEntry.getEntityType(var9), -1L));
            } catch (EntityTypeNotFoundException var6) {
                var6.printStackTrace();
            }
        }

        if (var7.length > 2 && var7[2].getType() == Type.INT) {
            this.protectionMode = (Integer)var7[2].getValue();
        }

        if (var7.length > 3 && var7[3].getType() == Type.STRUCT) {
            var2 = (Tag[])var7[3].getValue();

            for(var3 = 0; var3 < var2.length - 1; ++var3) {
                FreeItem var8;
                (var8 = new FreeItem()).fromTagStructure(var2[var3], this.state);
                var8.setId(GameServerState.itemIds++);
                if (var8.getType() != 0) {
                    this.items.put(var8.getId(), var8);
                }
            }
        }

        System.err.println("Read From Disk: " + this);
    }

    public Tag toTagStructure() {
        ObjectArrayList var1;
        Tag[] var2 = new Tag[(var1 = this.updateEntities()).size() + 1];
        int var3 = 0;

        for(int var4 = 0; var4 < var1.size(); ++var4) {
            var2[var3] = new Tag(Type.STRING, (String)null, ((SimpleTransformableSendableObject)var1.get(var4)).getUniqueIdentifier());
            ++var3;
        }

        var3 = 0;
        Tag[] var7 = new Tag[this.getItems().size() + 1];

        for(Iterator var5 = this.getItems().values().iterator(); var5.hasNext(); ++var3) {
            FreeItem var6 = (FreeItem)var5.next();
            var7[var3] = var6.toTagStructure(this.state);
        }

        var7[var7.length - 1] = FinishTag.INST;
        var2[var1.size()] = FinishTag.INST;
        return new Tag(Type.STRUCT, "SECTOR", new Tag[]{new Tag(Type.VECTOR3i, "POS", this.pos), new Tag(Type.STRUCT, "idents", var2), new Tag(Type.INT, "PROT", this.protectionMode), new Tag(Type.STRUCT, "items", var7), FinishTag.INST});
    }

    public long getDBId() {
        return this.dbID;
    }

    public void setDBId(long var1) {
        this.dbID = var1;
    }

    public float getHighestSubStep() {
        return this.highestSubStep;
    }

    public void setHighestSubStep(float var1) {
        this.highestSubStep = var1;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int var1) {
        this.id = var1;
    }

    public InputStream getItemBinaryStream() throws IOException {
        return getItemBinaryStream(this.items);
    }

    public Int2ObjectMap<FreeItem> getItems() {
        return this.remoteSector != null ? this.remoteSector.getItems() : this.items;
    }

    public long getLastPing() {
        return this.lastPing;
    }

    public float getLinearDamping() {
        if (this.state.getGameState() != null) {
            return this.state.getGameState().getLinearDamping();
        } else {
            throw new NullPointerException();
        }
    }

    public Physics getPhysics() {
        return this.physics;
    }

    public String getPhysicsSlowMsg() {
        return "[PHYSICS][SERVER] WARNING: PHYSICS SYNC IN DANGER. SECTOR: " + this.pos + " [" + this.getId() + "]";
    }

    public float getRotationalDamping() {
        if (this.state.getGameState() != null) {
            return this.state.getGameState().getRotationalDamping();
        } else {
            throw new NullPointerException();
        }
    }

    public void handleNextPhysicsSubstep(float var1) {
        this.setHighestSubStep(Math.max(this.getHighestSubStep(), var1));
    }

    public String toStringDebug() {
        return "Sector[" + this.getId() + "]" + this.pos + "; @" + this.getPhysics();
    }

    public short getNumberOfUpdate() {
        return this.state.getNumberOfUpdate();
    }

    public void setPhysics(Physics var1) {
        this.physics = var1;
    }

    public ProjectileController getParticleController() {
        return this.particleController;
    }

    private void getParticleControllerFromPool() {
        if (particleControllerPool.isEmpty()) {
            this.particleController = new ProjectileController(this.state, this.id);
        } else {
            ProjectileController var1;
            (var1 = (ProjectileController)particleControllerPool.dequeue()).setSectorId(this.id);
            this.particleController = var1;
        }
    }

    public PlanetType getPlanetType() throws IOException {
        if (this.planetTypeCache == null) {
            StellarSystem var1 = this.state.getUniverse().getStellarSystemFromSecPos(this.pos);
            this.planetTypeCache = var1.getPlanetType(this.pos);
        }

        return this.planetTypeCache;
    }

    public int getProtectionMode() {
        return this.protectionMode;
    }

    public void setProtectionMode(int var1) {
        this.protectionMode = var1;
    }

    public PulseController getPulseController() {
        return this.pulseController;
    }

    public void setPulseController(PulseController var1) {
        this.pulseController = var1;
    }

    private void getPulseControllerFromPool() {
        if (pulseControllerPool.isEmpty()) {
            this.pulseController = new PulseController(this.state, this.id);
        } else {
            PulseController var1;
            (var1 = (PulseController)pulseControllerPool.dequeue()).setSectorId(this.id);
            this.pulseController = var1;
        }
    }

    public RemoteSector getRemoteSector() {
        return this.remoteSector;
    }

    public void setRemoteSector(RemoteSector var1) {
        this.remoteSector = var1;
    }

    public SectorType getSectorType() throws IOException {
        if (this.sectorTypeCache == null) {
            StellarSystem var1 = this.state.getUniverse().getStellarSystemFromSecPos(this.pos);
            this.sectorTypeCache = var1.getSectorType(this.pos);
        }

        return this.sectorTypeCache;
    }

    public SpaceStationType getStationType() throws IOException {
        if (this.spaceStationTypeCache == null) {
            StellarSystem var1 = this.state.getUniverse().getStellarSystemFromSecPos(this.pos);
            this.spaceStationTypeCache = var1.getSpaceStationTypeType(this.pos);
        }

        return this.spaceStationTypeCache;
    }

    public String getUniqueIdentifier() {
        return "SECTOR_" + this.pos.x + "." + this.pos.y + "." + this.pos.z;
    }

    public boolean isVolatile() {
        return false;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object var1) {
        return this.id == ((Sector)var1).id;
    }

    public String toString() {
        return "Sector[" + this.getId() + "]" + this.pos;
    }

    public boolean hasSectorRemoveTimeout(long var1) {
        int var3;
        return (var3 = (Integer)ServerConfig.SECTOR_INACTIVE_CLEANUP_TIMEOUT.getCurrentState()) >= 0 && var1 > this.inactiveTime + (long)(var3 * 1000);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean var1) {
        if (!this.active && var1) {
            synchronized(this.state.getLocalAndRemoteObjectContainer()) {
                Iterator var3 = this.updateEntities().iterator();

                while(var3.hasNext()) {
                    SimpleTransformableSendableObject var4;
                    if ((var4 = (SimpleTransformableSendableObject)var3.next()).isWrittenForUnload()) {
                        System.err.println("[SERVER][DEBUG] Reactivated entity " + var4 + ", so it will be saved again (to prevent rollback bug). Sector: " + this);
                        var4.setWrittenForUnload(false);
                    }
                }

                this.sectorWrittenToDisk = false;
                this.wasActive = true;
                this.ping();
                this.getParticleControllerFromPool();
                this.getPulseControllerFromPool();
                this.active = var1;
            }
        } else {
            if (this.active && !var1) {
                this.releaseParticleControllerFromPool();
                this.releasePulseControllerFromPool();
                this.inactiveTime = System.currentTimeMillis();
            }

            this.active = var1;
        }
    }

    public boolean isNoIndications() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.NO_INDICATIONS);
    }

    public boolean isNoFPLoss() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.NO_FP_LOSS);
    }

    public boolean isPeace() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.PROT_NO_SPAWN);
    }

    public static boolean isMode(int var0, Sector.SectorMode var1) {
        return isMode(var0, var1.code);
    }

    public static boolean isMode(int var0, int var1) {
        return (var0 & var1) == var1;
    }

    public boolean isProtected() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.PROT_NO_ATTACK);
    }

    public boolean isNoEntry() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.LOCK_NO_ENTER);
    }

    public boolean isNoExit() {
        return isMode(this.getProtectionMode(), Sector.SectorMode.LOCK_NO_EXIT);
    }

    public boolean isSectorWrittenToDisk() {
        return this.sectorWrittenToDisk;
    }

    public void loadEntities(GameServerState var1) throws IOException, SQLException {
        ObjectArrayList var2 = new ObjectArrayList(this.entityUids.size());
        int var3 = 0;
        Iterator var4 = this.entityUids.iterator();

        while(true) {
            while(var4.hasNext()) {
                EntityUID var5;
                if ((var5 = (EntityUID)var4.next()).type == EntityType.SHIP && var5.spawnedOnlyInDb) {
                    this.getState().getUniverse().scheduleDefferedLoad(this, var5, 2);
                    System.err.println("[SERVER][SECTOR] Deferred loading of database spawned entity: " + var5);
                } else {
                    Sendable var6;
                    if ((var6 = this.loadEntitiy(var1, var5)) != null) {
                        var2.add(var6);
                    }

                    if (var6 != null && (var6 instanceof FloatingRock || var6 instanceof FloatingRockManaged)) {
                        ++var3;
                    }
                }
            }

            if (!this.isTransientSector() && var3 == this.entityUids.size() && (Integer)ServerConfig.ASTEROID_SECTOR_REPLENISH_TIME_SEC.getCurrentState() >= 0) {
                int var7 = this.getRockCount(this.getSectorType());
                long var8 = (long)((Integer)ServerConfig.ASTEROID_SECTOR_REPLENISH_TIME_SEC.getCurrentState() * 1000);
                if (var7 > var3 && System.currentTimeMillis() - this.getLastReplenished() > var8 && (var7 > 0 || var3 == 1)) {
                    System.err.println("[SECTOR] REPLENISHING ASTEROIDS IN SECTOR " + this.pos + ": LAST REPLENISHED: " + new Date(this.getLastReplenished()));
                    this.setTransientSector(true);
                    this.setLastReplenished(System.currentTimeMillis());
                }
            }

            if (this.isTransientSector() && !EngineSettings.SECRET.getCurrentState().toString().toLowerCase(Locale.ENGLISH).contains("noasteroids")) {
                this.populateAsteroids(this.getSectorType(), this.random);
            }

            return;
        }
    }

    public SegmentController loadSingleEntitiyWithDock(GameServerState var1, EntityUID var2, boolean var3) throws SQLException {
        Sendable var6;
        if ((var6 = this.loadEntitiy(var1, var2)) != null && var6 instanceof SegmentController) {
            SegmentController var4;
            (var4 = (SegmentController)var6).setVirtualBlueprint(var3);
            Iterator var7 = var4.railController.getExpectedToDock().iterator();

            while(var7.hasNext()) {
                String var5 = ((RailRequest)var7.next()).docked.uniqueIdentifierSegmentController;
                this.loadSingleEntitiyWithDock(var1, new EntityUID(var5, DatabaseEntry.getEntityType(var5), -1L), var3);
            }

            return (SegmentController)var6;
        } else {
            return null;
        }
    }

    public Sendable loadEntitiy(GameServerState var1, EntityUID var2) throws SQLException {
        String var3 = var2.uid;
        long var4;
        if ((var4 = var2.id) < 0L) {
            var4 = var1.getDatabaseIndex().getTableManager().getEntityTable().getIdForFullUID(var3);
        }

        long var6 = System.currentTimeMillis();
        synchronized(var1.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Sendable var10;
            if ((var10 = (Sendable)var1.getLocalAndRemoteObjectContainer().getUidObjectMap().get(var3)) != null) {
                if (var10 instanceof SimpleTransformableSendableObject) {
                    System.err.println("[SECTOR] entity " + var3 + " is still active -> not loaded from disk again: Loaded: " + var10 + "; currently in " + var1.getUniverse().getSector(((SimpleTransformableSendableObject)var10).getSectorId()));
                } else {
                    System.err.println("[SECTOR] entity " + var3 + " is still active -> not loaded from disk again: Loaded: " + var10 + ";");
                }

                return var10;
            }
        }

        EntityType var8;
        if ((var8 = DatabaseEntry.getEntityType(var3)) == EntityType.ASTEROID || var8 == EntityType.ASTEROID_MANAGED) {
            this.setTransientSector(false);
            DatabaseEntry.removePrefixWOException(var3);
            DatabaseEntry var17;
            if ((var17 = var1.getDatabaseIndex().getTableManager().getEntityTable().getById(var4)) != null && !var17.touched) {
                return Universe.loadUntouchedAsteroid(var1, var17, this);
            }
        }

        File var9 = SectorUtil.getEntityPath(var3);

        try {
            SimpleTransformableSendableObject var19;
            (var19 = this.loadEntityForThisSector(var8, var9, var4)).setTracked(var2.tracked);
            var19.setNeedsPositionCheckOnLoad(var2.spawnedOnlyInDb);
            long var20;
            if ((var20 = System.currentTimeMillis() - var6) > 50L) {
                System.err.println("[SERVER][SECTOR][LOADING_ENTITY] WARNING: " + this + " loading entity: " + var3 + " took long: " + var20 + "ms");
            }

            return var19;
        } catch (EntityNotFountException var14) {
            EntityNotFountException var18 = var14;

            try {
                var18.printStackTrace();
                System.err.println("[SERVER][ERROR] Exception Cannot load  " + var9.getName() + "; Removing entry from database");
                boolean var11 = var1.getDatabaseIndex().getTableManager().getEntityTable().removeSegmentController(DatabaseEntry.removePrefix(var3), var1);

                assert var11 : DatabaseEntry.removePrefix(var3);
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        } catch (Exception var15) {
            System.err.println("[SERVER][ERROR] Exception Loading Sector " + var9.getName() + ";");
            var15.printStackTrace();
        }

        return null;
    }

    public SimpleTransformableSendableObject<?> loadEntityForThisSector(EntityType var1, File var2, long var3) throws IOException, EntityNotFountException {
        return Universe.loadEntity(this.state, var1, var2, this, var3);
    }

    public void loadItems(byte[] var1) throws IOException {
        DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1));
        int var5 = var1.length / 22;

        for(int var3 = 0; var3 < var5; ++var3) {
            FreeItem var4;
            (var4 = new FreeItem()).setId(GameServerState.getItemId());
            var4.setType(var2.readShort());
            var4.setCount(var2.readInt());
            var4.setPos(new Vector3f(var2.readFloat(), var2.readFloat(), var2.readFloat()));
            var4.setMetaId(var2.readInt());
            if (var4.getType() != 0) {
                this.items.put(var4.getId(), var4);
            }
        }

    }

    public void loadUIDs(GameServerState var1) {
        this.entityUids.addAll(var1.getDatabaseIndex().getTableManager().getEntityTable().loadSectorEntities(this.pos));
        System.err.println("[SERVER][SECTOR] " + this.pos + " LOADED UIDs: " + uidToString(this.entityUids));
    }

    private static String uidToString(Set<EntityUID> var0) {
        StringBuffer var1 = new StringBuffer();
        Iterator var3 = var0.iterator();

        while(var3.hasNext()) {
            EntityUID var2 = (EntityUID)var3.next();
            var1.append(var2.uid + "(dbId " + var2.id + ")");
            if (var3.hasNext()) {
                var1.append("; ");
            }
        }

        return var1.toString();
    }

    public void mode(Sector.SectorMode var1, boolean var2) {
        this.mode(var1.code, var2);
        if (this.getRemoteSector() != null) {
            this.getRemoteSector().getRuleEntityManager().triggerSectorChmod();
        }

    }

    public void mode(int var1, boolean var2) {
        if (var2) {
            this.protectionMode |= var1;
        } else {
            this.protectionMode &= ~var1;
        }

        if (this.getRemoteSector() != null && this.getRemoteSector().getNetworkObject() != null) {
            this.getRemoteSector().getNetworkObject().mode.set(this.protectionMode, true);
        }

        this.setChangedForDb(true);
    }

    public void onAddedSector() throws IOException {
        assert this.getRemoteSector() == null;

        this.setRemoteSector(new RemoteSector(this.state));
        this.getRemoteSector().setSector(this);
        this.getRemoteSector().setItems(this.items);
        StellarSystem var1 = this.state.getUniverse().getStellarSystemFromSecPos(this.pos);
        this.system = var1;
        Vector3i var2 = Galaxy.getContainingGalaxyFromSystemPos(var1.getPos(), new Vector3i());
        Galaxy var4 = this.state.getUniverse().getGalaxy(var2);
        this.distanceToSun = var4.getSunDistance(this.pos);
        this.sunIntensity = var4.getSunIntensityFromSec(this.pos);
        Vector3i var3 = Galaxy.getLocalCoordinatesFromSystem(var1.getPos(), new Vector3i());
        this.sunOffset = var4.getSunPositionOffset(var3, new Vector3i());
        this.sunPosRel = new Vector3i(8, 8, 8);
        this.sunPosRel.add(this.sunOffset);
        if (var1.getCenterSectorType() == SectorType.DOUBLE_STAR) {
            this.sunPosRelSecond = new Vector3i();
            VoidSystem.getSecond(this.sunPosRel, this.sunOffset, this.sunPosRelSecond);
        }

        var1.getSectorType(this.pos);
        this.state.getController().getSynchController().addNewSynchronizedObjectQueued(this.getRemoteSector());
    }

    private void onNewCreatedSector() throws IOException {
        this.setChangedForDb(true);
        SectorType var1 = this.state.getUniverse().getStellarSystemFromSecPos(this.pos).getSectorType(this.pos);
        if (this.pos.equals(DEFAULT_SECTOR) && ServerConfig.PROTECT_STARTING_SECTOR.isOn()) {
            this.protect(true);
        }

        if (!this.isPeace() && ServerConfig.ENEMY_SPAWNING.isOn()) {
            if (var1 == SectorType.ASTEROID && !EngineSettings.SECRET.getCurrentState().toString().toLowerCase(Locale.ENGLISH).contains("nomobs") && !VoidSystem.getContainingSystem(this.pos, new Vector3i()).equals(0, 0, 0) && this.getRandom().nextInt(100) == 0) {
                try {
                    this.state.getController().initiateWave(this.getRandom().nextInt(8) + 3, -1, 1, 3, BluePrintController.active, new Vector3i(this.pos));
                    return;
                } catch (EntityNotFountException var2) {
                    var2.printStackTrace();
                    return;
                } catch (EntityAlreadyExistsException var3) {
                    var3.printStackTrace();
                }
            }

        } else {
            System.err.println("[SECTOR] NEW SECTOR IS PROTECTED FROM SPAWNING ANY ENEMIES");
        }
    }

    public void peace(boolean var1) {
        this.mode(Sector.SectorMode.PROT_NO_SPAWN, var1);
    }

    public void noEnter(boolean var1) {
        this.mode(Sector.SectorMode.LOCK_NO_ENTER, var1);
    }

    public void noExit(boolean var1) {
        this.mode(Sector.SectorMode.LOCK_NO_EXIT, var1);
    }

    public void noIndications(boolean var1) {
        this.mode(Sector.SectorMode.NO_INDICATIONS, var1);
    }

    public void noFpLoss(boolean var1) {
        this.mode(Sector.SectorMode.NO_FP_LOSS, var1);
    }

    public void ping() {
        this.lastPing = System.currentTimeMillis();
    }

    public void pingShort() {
        long var1;
        if ((var1 = System.currentTimeMillis() - (long)((Integer)ServerConfig.SECTOR_INACTIVE_TIMEOUT.getCurrentState() * 1000) + 1000L) < this.lastPing) {
            this.lastPing = var1;
        }

    }

    public void populate(GameServerState var1) throws IOException {
        StellarSystem var2 = var1.getUniverse().getStellarSystemFromSecPos(this.pos);
        if (ServerConfig.USE_PERSONAL_SECTORS.isOn() && this.pos.x == 16000 && this.pos.z == 16000 && this.pos.y > 1 && this.pos.y % 16 == 0) {
            this.populatePersonalSector(var1);
        } else {
            SectorType var3 = var2.getSectorType(this.pos);
            Faction var4;
            switch(var3) {
                case ASTEROID:
                    if (this.getFactionId() < 0 && (var4 = this.getState().getFactionManager().getFaction(this.getFactionId())) != null && var4 instanceof NPCFaction) {
                        ((NPCFaction)var4).populateAfterAsteroids(this, var3, this.random);
                        return;
                    }

                    this.addShopToAsteroidSector(var1);
                    return;
                case SPACE_STATION:
                    if (var2.getOwnerFaction() == -1) {
                        this.popuplateSpaceStationSector(var1, SpaceStationType.PIRATE);
                        return;
                    }

                    this.popuplateSpaceStationSector(var1, var2.getSpaceStationTypeType(this.pos));
                    return;
                case PLANET:
                    if (this.getFactionId() < 0 && (var4 = this.getState().getFactionManager().getFaction(this.getFactionId())) != null && var4 instanceof NPCFaction) {
                        ((NPCFaction)var4).populatePlanet(this, var3, var2.getPlanetType(this.pos), this.random);
                        return;
                    }

                    this.populatePlanetSector(var1, var2.getPlanetType(this.pos));
                    return;
                case MAIN:
                    if (EngineSettings.SECRET.getCurrentState().toString().toLowerCase(Locale.ENGLISH).contains("nuplanet")) {
                        this.populatePlanetIcoSector(var1);
                        return;
                    }

                    this.populateMainSector(var1);
                    return;
                case DOUBLE_STAR:
                case GIANT:
                case SUN:
                    return;
                case BLACK_HOLE:
                    return;
                case VOID:
                    return;
                case LOW_ASTEROID:
                    this.addShopToAsteroidSector(var1);
                    return;
                default:
                    assert false : "unknown sector type " + var2.getSectorType(this.pos);

            }
        }
    }

    private int getRockCount(SectorType var1) throws IOException {
        if (EngineSettings.SECRET.getCurrentState().toString().toLowerCase(Locale.ENGLISH).contains("noasteroids")) {
            return 0;
        } else {
            Random var2 = new Random(this.getSeed());
            int var3;
            if ((var3 = var1.getAsteroidCountMax()) > 0) {
                int var4 = var2.nextInt(var3);
                if (Galaxy.USE_GALAXY && var1 == SectorType.ASTEROID) {
                    ++var4;
                }

                return var4;
            } else {
                return 0;
            }
        }
    }

    private void addShopToAsteroidSector(GameServerState var1) {
        if (this.getRandom().nextFloat() < (Float)ServerConfig.SHOP_SPAWNING_PROBABILITY.getCurrentState()) {
            ShopSpaceStation var2 = new ShopSpaceStation(var1);
            int var3 = var1.getNextFreeObjectId();
            var2.setSeed((long)this.getRandom().nextInt());
            var2.setUniqueIdentifier(EntityType.SHOP.dbPrefix + this.seed + "_" + var3);
            var2.getMinPos().set(new Vector3i(0, -6, 0));
            var2.getMaxPos().set(new Vector3i(0, 6, 0));
            var2.setId(var3);
            var2.setSectorId(this.getId());
            var2.initialize();

            try {
                var2.fillInventory(false, false);
            } catch (NoSlotFreeException var4) {
                var4.printStackTrace();
            }

            var2.getInitialTransform().setIdentity();
            var2.getInitialTransform().origin.set((float)(this.getSectorSizeWithoutMargin() / 2), (float)this.getRandom().nextInt(this.getSectorSizeWithoutMargin() / 4), (float)(this.getSectorSizeWithoutMargin() / 2));
            var1.getController().getSynchController().addImmediateSynchronizedObject(var2);
            this.localAdd.add(var2);
        }

    }

    private void populateAsteroids(SectorType var1, Random var2) throws IOException {
        Faction var3;
        if (this.getFactionId() < 0 && (var3 = this.getState().getFactionManager().getFaction(this.getFactionId())) != null && var3 instanceof NPCFaction) {
            ((NPCFaction)var3).populateAsteroids(this, var1, var2);
        } else {
            var2 = new Random(this.getSeed());
            Random var12 = new Random(this.getSeed());
            int var4;
            if ((var4 = var1.getAsteroidCountMax()) > 0) {
                var4 = var2.nextInt(var4);
                if (Galaxy.USE_GALAXY && var1 == SectorType.ASTEROID) {
                    ++var4;
                }

                int var11 = (Integer)ServerConfig.ASTEROID_RADIUS_MAX.getCurrentState();

                for(int var5 = 0; var5 < var4; ++var5) {
                    long var9 = var2.nextLong();
                    var12.setSeed(var9);
                    int var6 = var12.nextInt(var11) + rockSize;
                    int var7 = var12.nextInt(var11) + rockSize;
                    int var8 = var12.nextInt(var11) + rockSize;
                    this.addRandomRock(this.state, var9, var6, var7, var8, var12, var5);
                }
            }

        }
    }

    private void populateBlackHoleSector(GameServerState var1) {
    }

    private void populateEmptySector(GameServerState var1) {
    }

    private void populateMainSector(GameServerState var1) {
        ShopSpaceStation var2;
        (var2 = new ShopSpaceStation(var1)).setSeed(0L);
        var2.setUniqueIdentifier(EntityType.SHOP.dbPrefix + System.currentTimeMillis());
        var2.getMinPos().set(new Vector3i(0, -10, 0));
        var2.getMaxPos().set(new Vector3i(0, 10, 0));
        var2.setId(var1.getNextFreeObjectId());
        var2.setSectorId(this.getId());
        var2.initialize();

        try {
            var2.fillInventory(false, false);
        } catch (NoSlotFreeException var3) {
            var3.printStackTrace();
        }

        var2.getInitialTransform().setIdentity();
        var2.getInitialTransform().origin.set(0.0F, 0.0F, 0.0F);
        var1.getController().getSynchController().addImmediateSynchronizedObject(var2);
        this.localAdd.add(var2);
    }

    private void populatePersonalSector(GameServerState var1) {
        ShopSpaceStation var2;
        (var2 = new ShopSpaceStation(var1)).setSeed(0L);
        var2.setUniqueIdentifier(EntityType.SHOP.dbPrefix + System.currentTimeMillis());
        var2.getMinPos().set(new Vector3i(0, -10, 0));
        var2.getMaxPos().set(new Vector3i(0, 10, 0));
        var2.setId(var1.getNextFreeObjectId());
        var2.setSectorId(this.getId());
        var2.getShoppingAddOn().setInfiniteSupply(true);
        var2.initialize();

        try {
            var2.fillInventory(false, false);
        } catch (NoSlotFreeException var3) {
            var3.printStackTrace();
        }

        var2.getInitialTransform().setIdentity();
        var2.getInitialTransform().origin.set(0.0F, 0.0F, 0.0F);
        var1.getController().getSynchController().addImmediateSynchronizedObject(var2);
        this.localAdd.add(var2);
    }

    private void populatePlanetIcoSector(GameServerState var1) {
        int var2 = FastMath.fastRound(12.5F);
        int var3 = Icosahedron.segmentProviderXMinMax(400.0F);
        Vector3i var4 = new Vector3i(-var3, var2 - 2, Icosahedron.segmentProviderZMin(400.0F));
        Vector3i var10 = new Vector3i(var3, var2 + 1, Icosahedron.segmentProviderZMax(400.0F));
        K var11 = new K(400.0F);
        PlanetIco var5;
        (var5 = new PlanetIco(var1)).setTerrainGenerator(var11);
        var5.setId(var1.getNextFreeObjectId());
        var5.initialize();
        var5.getInitialTransform().setIdentity();
        var5.setSeed(1337L);
        var5.setSectorId(this.getId());
        var5.setUniqueIdentifier(EntityType.PLANET_ICO.dbPrefix + "CORE_" + this.pos.x + "_" + this.pos.y + "_" + this.pos.z);
        var1.getController().getSynchController().addNewSynchronizedObjectQueued(var5);

        for(int var6 = 0; var6 < 20; ++var6) {
            PlanetIco var7;
            (var7 = new PlanetIco(var1)).setTerrainGenerator(var11);
            var7.setId(var1.getNextFreeObjectId());
            var7.setSeed(23452345L);
            var7.setSectorId(this.getId());
            var7.getMinPos().set(var4);
            var7.getMaxPos().set(var10);
            var7.setSideId((byte)var6);
            var7.initialize();
            var7.setUniqueIdentifier(EntityType.PLANET_ICO.dbPrefix + "SIDE_" + var6 + "_" + this.pos.x + "_" + this.pos.y + "_" + this.pos.z);
            VoidUniqueSegmentPiece var8;
            (var8 = new VoidUniqueSegmentPiece()).setSegmentController(var7);
            var8.setType((short)663);
            var8.voidPos.set(0, 0, 0);
            var8.uniqueIdentifierSegmentController = var7.getUniqueIdentifier();
            VoidUniqueSegmentPiece var9;
            (var9 = new VoidUniqueSegmentPiece()).setSegmentController(var5);
            var9.voidPos.set(0, 0, 0);
            var9.setType((short)662);
            var9.uniqueIdentifierSegmentController = var5.getUniqueIdentifier();
            RailRequest var12;
            (var12 = var7.railController.getRailRequest(var8, var9, new Vector3i(), (Vector3i)null, DockingPermission.PUBLIC)).fromtag = true;
            var12.ignoreCollision = true;
            var12.sentFromServer = false;
            var12.movedTransform.set(Icosahedron.getSideTransform((byte)var6));
            var12.railMovingLocalAtDockTransform.setIdentity();
            var7.railController.railRequestCurrent = var12;
            var1.getController().getSynchController().addNewSynchronizedObjectQueued(var7);
        }

    }

    public void populatePlanetSector(GameServerState var1, PlanetType var2) {
        float var3 = (Float)ServerConfig.PLANET_SIZE_MEAN_VALUE.getCurrentState();
        float var4 = (Float)ServerConfig.PLANET_SIZE_DEVIATION_VALUE.getCurrentState();
        double var5 = (double)var3 + this.random.nextGaussian() * ((double)var4 / 3.0D);
        int var9 = Math.min(2000, Math.max(50, (int)var5));
        new PlanetCore(var1);
        PlanetCore var11;
        (var11 = new PlanetCore(var1)).setId(var1.getNextFreeObjectId());
        var11.setUniqueIdentifier(EntityType.PLANET_CORE.dbPrefix + this.pos.x + "_" + this.pos.y + "_" + this.pos.z);
        var11.setSectorId(this.id);
        var11.setRadius((float)var9);
        var11.initialize();
        Dodecahedron var10;
        (var10 = new Dodecahedron(var11.getRadius())).create();
        this.setPlanetCore(var11);
        var11.getInitialTransform().setIdentity();
        var1.getController().getSynchController().addNewSynchronizedObjectQueued(var11);

        for(int var12 = 0; var12 < 12; ++var12) {
            Planet var6;
            (var6 = new Planet(var1)).setCreatorId(var2.ordinal());
            int var7 = (int)(var11.getRadius() / 32.0F);
            var6.setSeed(this.getRandom().nextLong());
            var6.setUniqueIdentifier(EntityType.PLANET_SEGMENT.dbPrefix + this.pos.x + "_" + this.pos.y + "_" + this.pos.z + "_" + var12 + "_" + System.currentTimeMillis());
            var6.getMinPos().set(new Vector3i(-var7, 0, -var7));
            var6.getMaxPos().set(new Vector3i(var7, 3, var7));
            var6.setId(var1.getNextFreeObjectId());
            var6.setSectorId(this.getId());
            var6.initialize();
            var6.setPlanetCoreUID(var11.getUniqueIdentifier());
            var6.setPlanetCore(var11);
            var6.fragmentId = var12;
            Transform var13 = var10.getTransform(var12, new Transform(), 0.5F, 0.5F);
            Vector3f var8;
            (var8 = new Vector3f(var13.origin)).normalize();
            var8.scale(7.6F);
            var13.origin.add(var8);
            var6.getInitialTransform().set(var13);
            var1.getController().getSynchController().addImmediateSynchronizedObject(var6);
            this.localAdd.add(var6);
        }

    }

    private void populateSunSector(GameServerState var1) {
    }

    private int getSectorSizeWithoutMargin() {
        return 1000;
    }

    private void popuplateNPCSpaceStationSector(GameServerState var1) {
        Faction var2;
        if (this.getFactionId() < 0 && (var2 = this.getState().getFactionManager().getFaction(this.getFactionId())) != null && var2 instanceof NPCFaction) {
            ((NPCFaction)var2).populateSpaceStation(this, this.random);
        } else {
            this.popuplateDefaultNeutralSpaceStationSector(var1);
        }
    }

    private void popuplateRandomSpaceStationSector(GameServerState var1) {
        BluePrintController var2;
        byte var3;
        if (this.random.nextInt(3) == 0) {
            var2 = BluePrintController.stationsTradingGuild;
            var3 = -2;
        } else {
            var2 = BluePrintController.stationsNeutral;
            var3 = 0;
        }

        List var4;
        if (!(var4 = var2.readBluePrints()).isEmpty() && this.random.nextInt(60) > 0) {
            BlueprintEntry var5 = (BlueprintEntry)var4.get(this.random.nextInt(var4.size()));
            Transform var6;
            (var6 = new Transform()).setIdentity();

            try {
                SegmentControllerOutline var11;
                (var11 = var2.loadBluePrint(var1, var5.getName(), "Station_" + var5.getName() + "_" + this.pos.x + "_" + this.pos.y + "_" + this.pos.z + "_" + System.currentTimeMillis(), var6, -1, var3, var4, this.pos, (List)null, "<system>", buffer, true, (SegmentPiece)null, new ChildStats(true))).scrap = var2 == BluePrintController.stationsNeutral;
                var11.shop = var3 == -2;
                var11.spawnSectorId = new Vector3i(this.pos);
                synchronized(var1.getBluePrintsToSpawn()) {
                    var1.getBluePrintsToSpawn().add(var11);
                }
            } catch (EntityNotFountException var8) {
                var8.printStackTrace();
            } catch (IOException var9) {
                var9.printStackTrace();
            } catch (EntityAlreadyExistsException var10) {
                var10.printStackTrace();
            }
        } else {
            this.popuplateDefaultNeutralSpaceStationSector(var1);
        }
    }

    private void popuplateDefaultNeutralSpaceStationSector(GameServerState var1) {
        SpaceStation var2;
        (var2 = new SpaceStation(var1)).setSeed(this.getRandom().nextLong());
        var2.setUniqueIdentifier(EntityType.SPACE_STATION.dbPrefix + System.currentTimeMillis());
        var2.setRealName("Station " + this.getId());
        var2.getMinPos().set(new Vector3i(-3, -3, -3));
        var2.getMaxPos().set(new Vector3i(3, 3, 3));
        var2.setCreatorId(SpaceStationType.RANDOM.ordinal());
        var2.setId(var1.getNextFreeObjectId());
        var2.setSectorId(this.getId());
        var2.initialize();
        var2.setScrap(true);
        var2.getInitialTransform().setIdentity();
        var1.getController().getSynchController().addImmediateSynchronizedObject(var2);
        DefaultSpawner var5 = new DefaultSpawner();
        SpawnMarker var3 = new SpawnMarker(new Vector3i(1, (int)(Math.random() * 10.0D - 5.0D), 1), var2, var5);
        SpawnComponentCreature var4;
        (var4 = new SpawnComponentCreature()).setBottom("LegsArag");
        var4.setMiddle("TorsoShell");
        var4.setName("Spider");
        var4.setCreatureType(CreatureType.CREATURE_SPECIFIC);
        var4.setFactionId(FactionManager.FAUNA_GROUP_ENEMY[0]);
        var5.getComponents().add(var4);
        var5.getComponents().add(new SpawnComponentDestroySpawnerAfterCount(5));
        var5.getConditions().add(new SpawnConditionTime(25000L));
        var5.getConditions().add(new SpawnConditionCreatureCountOnAffinity(1));
        var5.getConditions().add(new SpawnConditionPlayerProximity(64.0F));
        var2.getSpawnController().getSpawnMarker().add(var3);
        this.localAdd.add(var2);
    }

    private void popuplatePirateSpaceStationSector(GameServerState var1) {
        BluePrintController var2;
        List var3;
        if (!(var3 = (var2 = BluePrintController.stationsPirate).readBluePrints()).isEmpty() && (!ServerConfig.USE_OLD_GENERATED_PIRATE_STATIONS.isOn() || this.random.nextInt(5) > 0)) {
            BlueprintEntry var4 = (BlueprintEntry)var3.get(this.random.nextInt(var3.size()));
            Transform var5;
            (var5 = new Transform()).setIdentity();

            try {
                SegmentControllerOutline var10;
                (var10 = var2.loadBluePrint(var1, var4.getName(), "Station_" + var4.getName() + "_" + this.pos.x + "_" + this.pos.y + "_" + this.pos.z + "_" + System.currentTimeMillis(), var5, -1, -1, var3, this.pos, (List)null, "<system>", buffer, true, (SegmentPiece)null, new ChildStats(true))).scrap = var2 == BluePrintController.stationsNeutral;
                var10.spawnSectorId = new Vector3i(this.pos);
                synchronized(var1.getBluePrintsToSpawn()) {
                    var1.getBluePrintsToSpawn().add(var10);
                    return;
                }
            } catch (EntityNotFountException var7) {
                var7.printStackTrace();
                return;
            } catch (IOException var8) {
                var8.printStackTrace();
                return;
            } catch (EntityAlreadyExistsException var9) {
                var9.printStackTrace();
            }
        }

    }

    private void popuplateSpaceStationSector(GameServerState var1, SpaceStationType var2) {
        switch(var2) {
            case EMPTY:
                return;
            case RANDOM:
                this.popuplateRandomSpaceStationSector(var1);
                return;
            case PIRATE:
                this.popuplatePirateSpaceStationSector(var1);
                return;
            case FACTION:
                this.popuplateNPCSpaceStationSector(var1);
            default:
        }
    }

    public void protect(boolean var1) {
        this.mode(Sector.SectorMode.PROT_NO_ATTACK, var1);
    }

    public void queueRepairRequest() {
        this.flagRepair = true;
    }

    public void releaseParticleControllerFromPool() {
        assert this.particleController != null;

        particleControllerPool.enqueue(this.particleController);
        this.particleController = null;
    }

    public void releasePulseControllerFromPool() {
        assert this.pulseController != null;

        pulseControllerPool.enqueue(this.pulseController);
        this.pulseController = null;
    }

    private void repair() {
        this.setChangedForDb(true);

        Iterator var1;
        try {
            List var10000 = this.state.getDatabaseIndex().getTableManager().getEntityTable().getBySector(this.pos, 0);
            var1 = null;
            var1 = var10000.iterator();

            while(var1.hasNext()) {
                DatabaseEntry var2 = (DatabaseEntry)var1.next();

                try {
                    if (!this.state.getSegmentControllersByName().containsKey(var2.uid.trim())) {
                        String var3 = var2.uid.split("_", 3)[1];
                        System.err.println("[REPAIR] FOUND SECTOR ENTITY: " + var2.uid + " [" + var3 + "]");
                        this.loadEntityForThisSector(var2.getEntityType(), new FileExt(var2.uid + ".ent"), -1L);
                    }
                } catch (IOException var4) {
                    var4.printStackTrace();
                } catch (EntityNotFountException var5) {
                    var5.printStackTrace();
                }
            }
        } catch (SQLException var6) {
            var1 = null;
            var6.printStackTrace();
        }

        (new FileExt(GameServerState.ENTITY_DATABASE_PATH)).listFiles();
    }

    public void setNew() {
        this.newCreatedSector = true;
    }

    private void setRandomPos(Vector3i var1, Random var2) {
        var1.set(0, 0, 0);
        int var3 = this.getSectorSizeWithoutMargin();
        int var4 = (int)(((float)var2.nextInt(var3) - (float)var3 / 2.0F) * 0.7F);
        int var5 = (int)(((float)var2.nextInt(var3) - (float)var3 / 2.0F) * 0.7F);
        int var6 = (int)(((float)var2.nextInt(var3) - (float)var3 / 2.0F) * 0.7F);
        var1.set(var4, var5, var6);
    }

    public void testPopulate() throws Exception {
        new Vector3i();
        TeamDeathStar var1;
        (var1 = new TeamDeathStar(this.state)).setSeed(this.getRandom().nextLong());
        var1.setUniqueIdentifier("ENTITY_DEATHSTAR_GREEN_" + System.currentTimeMillis());
        var1.getMinPos().set(new Vector3i(-2, -2, -2));
        var1.getMaxPos().set(new Vector3i(2, 2, 2));
        var1.setId(this.state.getNextFreeObjectId());
        var1.setSectorId(this.getId());
        var1.initialize();
        var1.getInitialTransform().setIdentity();
        var1.getInitialTransform().origin.set(0.0F, -64.0F, 0.0F);
        this.localAdd.add(var1);
        this.state.getController().getSynchController().addImmediateSynchronizedObject(var1);
    }

    public String toDetailString() {
        String var1 = "unknown(IOError)";

        try {
            var1 = this.getSectorType().name();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        return "Sector[" + this.getId() + "]" + this.pos + "; Permission" + this.getPermissionString() + "; Seed: " + this.getSeed() + "; Type: " + var1 + ";";
    }

    public String getPermissionString() {
        return getPermissionString(this.getProtectionMode());
    }

    public static String getPermissionString(int var0) {
        return "[Peace,Protected,NoEnter,NoExit,NoIndication,NoFpLoss]: " + getPermissionBit(var0, Sector.SectorMode.PROT_NO_SPAWN) + getPermissionBit(var0, Sector.SectorMode.PROT_NO_ATTACK) + getPermissionBit(var0, Sector.SectorMode.LOCK_NO_ENTER) + getPermissionBit(var0, Sector.SectorMode.LOCK_NO_EXIT) + getPermissionBit(var0, Sector.SectorMode.NO_INDICATIONS) + getPermissionBit(var0, Sector.SectorMode.NO_FP_LOSS);
    }

    public int getPermissionBit(Sector.SectorMode var1) {
        return this.getPermissionBit(var1.code);
    }

    public int getPermissionBit(int var1) {
        return isMode(this.getProtectionMode(), var1) ? 1 : 0;
    }

    public static int getPermissionBit(int var0, Sector.SectorMode var1) {
        return getPermissionBit(var0, var1.code);
    }

    public static int getPermissionBit(int var0, int var1) {
        return isMode(var0, var1) ? 1 : 0;
    }

    private boolean checkSec() {
        Vector3i var1 = Galaxy.getContainingGalaxyFromSystemPos(VoidSystem.getContainingSystem(this.pos, new Vector3i()), new Vector3i());
        Galaxy var3 = this.state.getUniverse().getGalaxy(var1);
        Vector3i var2 = Galaxy.getLocalCoordinatesFromSystem(VoidSystem.getContainingSystem(this.pos, new Vector3i()), new Vector3i());
        var1 = var3.getSunPositionOffset(var2, new Vector3i());
        return this.sunOffset.equals(var1);
    }

    public void update(Timer var1) throws IOException {
        assert !this.terminated;

        ++GameServerState.totalSectorCountTmp;
        System.currentTimeMillis();
        long var2 = System.currentTimeMillis();
        long var4 = 0L;
        long var6 = 0L;
        if (this.flagRepair) {
            this.repair();
            this.flagRepair = false;
        }

        long var8;
        if (this.isActive()) {
            ++GameServerState.activeSectorCountTmp;

            assert this.getParticleController() != null;

            var8 = System.currentTimeMillis();
            this.getParticleController().update(var1);
            this.getPulseController().update(var1);
            if ((var6 = System.currentTimeMillis() - var8) > 150L) {
                System.err.println("[SERVER] '''WARNING''' PATICLE UPDATE: " + this.pos + " ms TOOK " + var6 + "; Count: " + this.getParticleController().getParticleCount());
            }

            if (this.newCreatedSector) {
                this.onNewCreatedSector();
                this.newCreatedSector = false;
            }

            var8 = System.currentTimeMillis();
            this.getPhysics().update(var1, this.getHighestSubStep());
            var4 = System.currentTimeMillis() - var8;
            this.sectorWrittenToDisk = false;
            this.setHighestSubStep(0.0F);
            float var10 = this.state.getGameConfig().sunMinIntensityDamageRange;
            if (this.system.isHeatDamage(this.pos, this.sunIntensity, this.distanceToSun, var10)) {
                float var11 = (var10 - this.distanceToSun / Math.max(1.0F, this.sunIntensity)) / var10;
                this.doSunstorm(var1, var11);
            }

            if (this.system.getCenterSectorType() == SectorType.BLACK_HOLE) {
                assert this.checkSec();

                if (!this.system.getPos().equals(0, 0, 0) && !this.isTutorialSector() && !this.isPersonalOrTestSector()) {
                    applyBlackHoleGrav(this.state, this.system.getPos(), this.sunPosRel, this.pos, this.getId(), this.tmpSecPos, this.tmpOPos, this.tmpDir, this.sunOffset, var1);
                }
            }
        } else if (this.state.delayAutosave < System.currentTimeMillis()) {
            if (this.wasActive) {
                var8 = System.currentTimeMillis();
                this.writeToDisk(2, false, true, this.state.getUniverse());
                long var12;
                if ((var12 = System.currentTimeMillis() - var8) > 20L) {
                    System.err.println("[SERVER][SECTOR] WRITING SECTOR ID " + this.id + " -> " + this.pos + " TOOK " + var12 + "ms");
                }
            }

            this.wasActive = false;
        } else {
            System.err.println("[SERVER] Delay of unloading sector: delay issued by admin " + (this.state.delayAutosave - System.currentTimeMillis()) / 1000L + " secs");
        }

        if ((var8 = System.currentTimeMillis() - var2) > 130L && System.currentTimeMillis() - this.lastWarning > 100000L) {
            System.err.println("WARNING: sector update of single sector " + this.pos + " took " + var8 + " ms; Physics " + var4 + " ms");
            System.err.println("WARNING: sector info: " + this.pos + "; id " + this.id + "; type " + this.getSectorType().name() + "; ");
            if (!GameClientController.isStarted()) {
                this.state.getController().broadcastMessageAdmin(new Object[]{421, this.pos, var8, var4}, 3);
            }

            this.lastWarning = System.currentTimeMillis();
        }

    }

    public boolean isPersonalOrTestSector() {
        return isPersonalOrTestSector(this.pos);
    }

    public ObjectArrayList<SimpleTransformableSendableObject> updateEntities() {
        ObjectArrayList var1 = new ObjectArrayList();

        try {
            Iterator var2 = this.state.getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

            while(var2.hasNext()) {
                Sendable var3;
                if ((var3 = (Sendable)var2.next()) instanceof SimpleTransformableSendableObject && ((SimpleTransformableSendableObject)var3).getSectorId() == this.getId()) {
                    SimpleTransformableSendableObject var5;
                    (var5 = (SimpleTransformableSendableObject)var3).getWorldTransform();
                    var1.add(var5);
                }
            }

            return var1;
        } catch (ConcurrentModificationException var4) {
            var4.printStackTrace();
            System.err.println("CATCHED EXCEPTION!!!!!!!!!!!!!!!!!! (sector entity calc)");
            return this.updateEntities();
        }
    }

    private void writeEntity(Sendable var1, int var2, Universe var3) throws Exception {
        if (var1 instanceof TagSerializable) {
            long var4 = System.currentTimeMillis();
            if (var1 instanceof SimpleTransformableSendableObject) {
                ((SimpleTransformableSendableObject)var1).transientSectorPos.set(this.pos);
                ((SimpleTransformableSendableObject)var1).transientSector = this.isTransientSector();
            }

            long var6 = var4 - System.currentTimeMillis();
            writeSingle(this.state, var1);
            if (var2 == 1) {
                System.err.println("[SERVER] Terminate: DONE Writing Entity: " + this + ": " + var1);
            }

            if (var1 instanceof SendableSegmentController) {
                SendableSegmentController var8 = (SendableSegmentController)var1;
                if (var2 == 1) {
                    var8.getSegmentBuffer().size();
                    var8.getSegmentBuffer().clear(true);
                } else if (var2 == 2) {
                    var3.addToClear((SendableSegmentController)var1);
                }
            }

            long var10;
            if ((var10 = var4 - System.currentTimeMillis()) > 10L) {
                System.err.println("[SECTOR] WARNING: Writing entity: " + var1 + " took " + var10 + " ms. write: " + var6 + "; segments " + (var10 - var6));
            }
        }

    }

    public static void writeSingle(GameServerState var0, Sendable var1) throws IOException, SQLException {
        var0.getController().writeEntity((DiskWritable)var1, true);
        if (var1 instanceof SendableSegmentController) {
            SendableSegmentController var3;
            int var2 = (var3 = (SendableSegmentController)var1).writeAllBufferedSegmentsToDatabase(false, false, false);
            if (var1 instanceof Ship || var1 instanceof SpaceStation) {
                System.err.println("[SERVER][DEBUG] WRITTEN " + var1 + "; lastWrite " + var3.getLastWrite() + "; written segments: " + var2);
            }

            IOFileManager.writeAllOpenFiles(((SendableSegmentController)var1).getSegmentProvider().getSegmentDataIO().getManager());
        }

    }

    public void writeToDisk(final int var1, boolean var2, final boolean var3, final Universe var4) throws IOException {
        Iterator var10 = this.getItems().values().iterator();

        while(var10.hasNext()) {
            FreeItem var5;
            if ((var5 = (FreeItem)var10.next()).getType() < 0) {
                this.state.getMetaObjectManager().archive(this.pos, var5);
            }
        }

        final ObjectArrayList var11;
        if ((var11 = this.updateEntities()).isEmpty()) {
            this.setTransientSector(false);
        }

        boolean var12 = false;
        boolean var6 = false;

        for(int var7 = 0; var7 < var11.size(); ++var7) {
            SimpleTransformableSendableObject var8;
            if ((var8 = (SimpleTransformableSendableObject)var11.get(var7)) instanceof TransientSegmentController && (((TransientSegmentController)var8).isMoved() || ((TransientSegmentController)var8).isTouched())) {
                var8.transientSector = false;
                this.setTransientSector(false);
                if (this.isTransientSector()) {
                    System.err.println("SECTOR " + this.pos + " is Transient sector no longer because of " + var8);
                }
            }

            if (var8 instanceof SpaceStation) {
                var12 = true;
            }

            if (var8 instanceof Planet && !"none".equals(((Planet)var8).getPlanetCoreUID())) {
                var6 = true;
            }
        }

        if (this.getSectorType() == SectorType.SPACE_STATION && !var12 || this.getSectorType() == SectorType.PLANET && !var6) {
            StellarSystem var14 = this.getState().getUniverse().getStellarSystemFromSecPos(this.pos);
            Vector3i var16 = VoidSystem.getLocalCoordinates(this.pos, new Vector3i());
            int var13 = var14.getIndex(var16);
            var14.setSectorType(var13, SectorType.VOID);
        }

        if (var1 == 1) {
            System.err.println("[SERVER] Terminate: Writing Entity: " + this + "; trans: " + this.isTransientSector() + "; " + var11);
        }

        final GameServerState var15 = this.state;
        Runnable var17 = new Runnable() {
            private String threatstate = "not started";

            public String toString() {
                return "WriterRunnable (ID: " + Sector.this.getId() + "; state: " + this.threatstate + ")";
            }

            public void run() {
                this.threatstate = "waiting on synch";
                synchronized(var15) {
                    synchronized(var15.getLocalAndRemoteObjectContainer()) {
                        try {
                            try {
                                this.threatstate = "synched - database update";

                                assert var15 != null;

                                assert var15.getDatabaseIndex() != null;

                                assert Sector.this != null;

                                var15.getDatabaseIndex().getTableManager().getSectorTable().updateOrInsertSector(Sector.this);
                                if (Sector.this.getRemoteSector() != null) {
                                    Sector.this.getRemoteSector().getConfigManager().saveToDatabase(Sector.this.getState());
                                }

                                long var3x = System.currentTimeMillis();
                                this.threatstate = "synched - write";

                                for(int var5 = 0; var5 < var11.size(); ++var5) {
                                    try {
                                        if (!((SimpleTransformableSendableObject)var11.get(var5)).isWrittenForUnload()) {
                                            if (!((SimpleTransformableSendableObject)var11.get(var5)).isMarkedForDeleteVolatile()) {
                                                Sector.this.writeEntity((Sendable)var11.get(var5), var1, var4);
                                            }

                                            ((SimpleTransformableSendableObject)var11.get(var5)).setWrittenForUnload(var3);
                                        } else {
                                            System.err.println("[SERVER][DEBUG] NOT writing " + var11.get(var5) + " (already written for unload)");
                                        }
                                    } catch (IOException var13) {
                                        var13.printStackTrace();
                                    } catch (Exception var14) {
                                        var14.printStackTrace();
                                    }
                                }

                                if (System.currentTimeMillis() - var3x > 100L) {
                                    System.err.println("[SERVER] (>100) SERVER SECTOR WRITING FINSISHED #" + Sector.this.getId() + " " + Sector.this.pos + " (ents: " + var11.size() + ") took: " + (System.currentTimeMillis() - var3x) + " ms");
                                }

                                Sector.this.sectorWrittenToDisk = true;
                                this.threatstate = "synched - DONE";
                            } catch (IOException var15x) {
                                var15x.printStackTrace();
                            } catch (SQLException var16) {
                                var16.printStackTrace();
                            }
                        } catch (Throwable var17) {
                            //Decompiler weirdness
                            var17.printStackTrace();
                        }
                    }
                }

                this.threatstate = "terminated";
            }
        };

        try {
            var15.getThreadQueue().enqueue(var17);
        } catch (RejectedExecutionException var9) {
            var9.printStackTrace();
            System.err.println(var15.getThreadPoolLogins().getActiveCount() + "/" + var15.getThreadPoolLogins().getMaximumPoolSize());
        }
    }

    public boolean isTransientSector() {
        return this.transientSector;
    }

    public void setTransientSector(boolean var1) {
        if (this.transientSector && !var1) {
            this.setChangedForDb(true);
        }

        this.transientSector = var1;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long var1) {
        this.seed = var1;
    }

    public Random getRandom() {
        return this.random;
    }

    public void setRandom(Random var1) {
        this.random = var1;
    }

    public PlanetCore getPlanetCore() {
        return this.planetCore;
    }

    public void setPlanetCore(PlanetCore var1) {
        this.planetCore = var1;
    }

    public GameServerState getState() {
        return this.state;
    }

    public void sendHitConfirm(byte var1) {
    }

    public boolean isSegmentController() {
        return false;
    }

    public int getFactionId() {
        try {
            return this.state.getUniverse().getStellarSystemFromSecPos(this.pos).getOwnerFaction();
        } catch (IOException var1) {
            var1.printStackTrace();

            assert false;

            return 0;
        }
    }

    public String getName() {
        try {
            return this.getSectorType() == SectorType.BLACK_HOLE ? "Black Hole" : "Sun";
        } catch (IOException var1) {
            var1.printStackTrace();
            return "n/a";
        }
    }

    public AbstractOwnerState getOwnerState() {
        return null;
    }

    public void removeMissile(short var1) {
        this.getMissiles().remove(var1);
    }

    public void addMissile(short var1) {
        this.getMissiles().add(var1);
    }

    public ShortOpenHashSet getMissiles() {
        return this.missiles;
    }

    public boolean isTutorialSector() {
        return isTutorialSector(this.pos);
    }

    public boolean hasChangedForDb() {
        return this.changed;
    }

    public void setChangedForDb(boolean var1) {
        this.changed = var1;
    }

    public void sendServerMessage(Object[] var1, int var2) {
    }

    public long getLastReplenished() {
        return this.lastReplenished;
    }

    public void setLastReplenished(long var1) {
        this.lastReplenished = var1;
    }

    public void addEntity(SimpleTransformableSendableObject var1) {
        this.getEntities().add(var1);
        this.getState().getController().onEntityAddedToSector(this, var1);
    }

    public void removeEntity(SimpleTransformableSendableObject var1) {
        this.getEntities().remove(var1);
        this.getState().getController().onEntityRemoveFromSector(this, var1);
    }

    public Set<SimpleTransformableSendableObject<?>> getEntities() {
        return this.entities;
    }

    public void sendClientMessage(String var1, int var2) {
    }

    public float getDamageGivenMultiplier() {
        return 1.0F;
    }

    public boolean isInterdicting(SegmentController var1, Sector var2) {
        if (!this.getRemoteSector().getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_ACTIVE, false)) {
            return false;
        } else {
            int var3 = Math.abs(var2.pos.x - this.pos.x);
            int var4 = Math.abs(var2.pos.y - this.pos.y);
            int var7 = Math.abs(var2.pos.z - this.pos.z);
            int var6 = this.getRemoteSector().getConfigManager().apply(StatusEffectType.WARP_INTERDICTION_STRENGTH, 1);
            int var5 = 2;
            if (var3 + var4 + var7 <= var5) {
                if(var1.hasActiveReactors()) {
                    int reactorLevel = ((ManagedSegmentController<?>) var1).getManagerContainer().getPowerInterface().getActiveReactor().getLevel();
                    boolean b = var1.hasActiveReactors() && reactorLevel <= var6;
                    return b;
                }else{
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public SimpleTransformableSendableObject<?> getShootingEntity() {
        return null;
    }

    public InterEffectSet getAttackEffectSet(long var1, DamageDealerType var3) {
        return null;
    }

    public MetaWeaponEffectInterface getMetaWeaponEffect(long var1, DamageDealerType var3) {
        return null;
    }

    public int getSectorId() {
        return this.id;
    }

    static {
        int var0;
        for(var0 = 0; var0 < asteroidsByTemperature.length; ++var0) {
            asteroidsByTemperature[var0] = AsteroidType.values()[var0];
        }

        Arrays.sort(asteroidsByTemperature, new Comparator<AsteroidType>() {
            public final int compare(AsteroidType var1, AsteroidType var2) {
                return (int)(var1.temperature * 100000.0F - var2.temperature * 100000.0F);
            }
        });

        for(var0 = 0; var0 < asteroidsByTemperature.length; ++var0) {
        }

    }

    class SimpleTransformableSendableObjectList extends ArrayList<SimpleTransformableSendableObject> {
        private static final long serialVersionUID = 1L;

        private SimpleTransformableSendableObjectList() {
        }
    }

    class PhysicsCallback extends InternalTickCallback {
        private PhysicsCallback() {
        }

        public void internalTick(DynamicsWorld var1, float var2) {
            int var17 = Sector.this.getPhysics().getDynamicsWorld().getDispatcher().getNumManifolds();

            for(int var18 = 0; var18 < var17; ++var18) {
                PersistentManifold var3;
                CollisionObject var4 = (CollisionObject)(var3 = Sector.this.getPhysics().getDynamicsWorld().getDispatcher().getManifoldByIndexInternal(var18)).getBody0();
                CollisionObject var5 = (CollisionObject)var3.getBody1();
                if (var4 != null && var5 != null && !(var4 instanceof GhostObject) && !(var5 instanceof GhostObject) && var5.getUserPointer() != null && var5.getUserPointer() != null) {
                    if (var4.getUserPointer() instanceof Integer && var5.getUserPointer() instanceof Integer) {
                        int var6 = var3.getNumContacts();
                        int var7 = (Integer)var4.getUserPointer();
                        int var8 = (Integer)var5.getUserPointer();
                        Sendable var19 = (Sendable)Sector.this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var7);
                        Sendable var20 = (Sendable)Sector.this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var8);
                        Vector3f var9 = new Vector3f();
                        Vector3f var10 = new Vector3f();
                        if (var19 instanceof Collisionable && var20 instanceof Collisionable) {
                            Collisionable var11 = (Collisionable)var19;
                            Collisionable var12 = (Collisionable)var20;
                            boolean var13 = var11.needsManifoldCollision();
                            boolean var14 = var12.needsManifoldCollision();
                            if (var13 || var14) {
                                for(int var15 = 0; var15 < var6; ++var15) {
                                    ManifoldPoint var16;
                                    if ((var16 = var3.getContactPoint(var15)).getDistance() < 0.0F && var4.getUserPointer() != null && var5.getUserPointer() != null) {
                                        var16.getPositionWorldOnA(var9);
                                        var16.getPositionWorldOnB(var10);
                                        Vector3f var10000 = var16.normalWorldOnB;
                                        if (var13) {
                                            var11.onCollision(var16, var20);
                                        }

                                        if (var14) {
                                            var12.onCollision(var16, var19);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Sector.this.getState().getController().broadcastMessage(new Object[]{422, var4.getUserPointer() != null ? var4.getUserPointer().toString() : "null", var5.getUserPointer() != null ? var5.getUserPointer().toString() : null}, 3);
                    }
                }
            }

        }
    }

    class SectorSunDamager implements Damager {
        InterEffectSet damageSet = new InterEffectSet();

        public SectorSunDamager() {
            this.damageSet.setStrength(InterEffectType.HEAT, 1.0F);
        }

        public void sendServerMessage(Object[] var1, int var2) {
        }

        public StateInterface getState() {
            return Sector.this.getState();
        }

        public void sendHitConfirm(byte var1) {
        }

        public boolean isSegmentController() {
            return false;
        }

        public SimpleTransformableSendableObject<?> getShootingEntity() {
            return null;
        }

        public int getFactionId() {
            return 0;
        }

        public String getName() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_6;
        }

        public AbstractOwnerState getOwnerState() {
            return null;
        }

        public void sendClientMessage(String var1, int var2) {
        }

        public float getDamageGivenMultiplier() {
            return 1.0F;
        }

        public InterEffectSet getAttackEffectSet(long var1, DamageDealerType var3) {
            return this.damageSet;
        }

        public MetaWeaponEffectInterface getMetaWeaponEffect(long var1, DamageDealerType var3) {
            return null;
        }

        public int getSectorId() {
            return Sector.this.getSectorId();
        }
    }

    public static enum SectorMode {
        PROT_NORMAL(0),
        PROT_NO_SPAWN(1),
        PROT_NO_ATTACK(2),
        LOCK_NO_ENTER(4),
        LOCK_NO_EXIT(8),
        NO_INDICATIONS(16),
        NO_FP_LOSS(32);

        public final int code;

        private SectorMode(int var3) {
            this.code = var3;
        }

        public final String getName() {
            switch(this) {
                case LOCK_NO_ENTER:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_7;
                case LOCK_NO_EXIT:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_8;
                case NO_FP_LOSS:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_9;
                case NO_INDICATIONS:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_10;
                case PROT_NORMAL:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_11;
                case PROT_NO_ATTACK:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_12;
                case PROT_NO_SPAWN:
                    return Lng.ORG_SCHEMA_GAME_COMMON_DATA_WORLD_SECTOR_13;
                default:
                    return "UNKNOWNSECTORMODE";
            }
        }
    }
}
