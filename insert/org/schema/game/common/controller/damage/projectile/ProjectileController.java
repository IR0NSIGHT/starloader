//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.damage.projectile;

import api.listener.events.CannonProjectileAddEvent;
import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.util.linAlg.TransformTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.MineInterface;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.mines.Mine;
import org.schema.game.common.data.missile.Missile;
import org.schema.game.common.data.physics.BlockRecorder;
import org.schema.game.common.data.physics.CollisionObjectInterface;
import org.schema.game.common.data.physics.CollisionType;
import org.schema.game.common.data.physics.CubeRayCastResult;
import org.schema.game.common.data.physics.ModifiedDynamicsWorld;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.SectorInformation.SectorType;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.particle.ParticleController;
import org.schema.schine.network.Identifiable;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.server.ServerStateInterface;

public class ProjectileController extends ParticleController<ProjectileParticleContainer> {
    public static final int PROJECTILE_CACHE = 256;
    public static final boolean SORT_PARTICLES = false;
    private final ParticleHitCallback particleHitCallback = new ParticleHitCallback();
    Transform oldTrans = new Transform();
    Transform newTrans = new Transform();
    Transform transTmp = new Transform();
    private StateInterface state;
    private int sectorId;
    private Vector3f velocityHelper = new Vector3f();
    private Vector4f colorHelper = new Vector4f();
    private Vector3f posHelper = new Vector3f();
    private Vector3f userDataHelper = new Vector3f();
    private Vector3f startHelper = new Vector3f();
    private Vector3f posBeforeUpdate = new Vector3f();
    private Vector3f posAfterUpdate = new Vector3f();
    private Vector3i syspos = new Vector3i();
    private final ObjectArrayList<ProjectileBlockHit> blockHitPool;
    private final ObjectArrayList<ProjectileBlockHit> blockHitList;
    private final ProjectileController.BlockSorter blockSorter = new ProjectileController.BlockSorter();
    private final Object2ObjectMap<CollisionType, ProjectileHandler> projectileHandlers;
    private static int projectileIdGen;
    private static ThreadLocal<Object2ObjectMap<CollisionType, ProjectileHandler>> threadHandlers = new ThreadLocal<Object2ObjectMap<CollisionType, ProjectileHandler>>() {
        protected final Object2ObjectMap<CollisionType, ProjectileHandler> initialValue() {
            Object2ObjectOpenHashMap var1 = new Object2ObjectOpenHashMap();
            CollisionType[] var2;
            int var3 = (var2 = CollisionType.values()).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                CollisionType var5 = var2[var4];
                var1.put(var5, var5.projectileHandlerFactory.getInst());
            }

            return var1;
        }
    };
    private static ThreadLocal<ObjectArrayList<ProjectileBlockHit>> threadCache = new ThreadLocal<ObjectArrayList<ProjectileBlockHit>>() {
        protected final ObjectArrayList<ProjectileBlockHit> initialValue() {
            ObjectArrayList var1 = new ObjectArrayList(512);

            for(int var2 = 0; var2 < 128; ++var2) {
                var1.add(new ProjectileBlockHit());
            }

            return var1;
        }
    };
    private static ThreadLocal<ObjectArrayList<ProjectileBlockHit>> threadLocalPool = new ThreadLocal<ObjectArrayList<ProjectileBlockHit>>() {
        protected final ObjectArrayList<ProjectileBlockHit> initialValue() {
            return new ObjectArrayList(512);
        }
    };
    private CubeRayCastResult rayCallbackInitial;
    private Vector3i tmpPos;
    private Int2ObjectOpenHashMap<BlockRecorder> blockHitCache;
    private int blocksAlreadyHit;
    private int blockDeepnessFull;
    private boolean hitMarker;
    private List<SegmentController> hitCon;
    private Int2ObjectOpenHashMap<SegmentController[]> arrayBuffer;
    Vector3f clientPos;
    private PhysicsExt currentPhysics;

    public ProjectileController(StateInterface var1, int var2) {
        super(false);
        this.projectileHandlers = (Object2ObjectMap)threadHandlers.get();
        this.rayCallbackInitial = new CubeRayCastResult(new Vector3f(), new Vector3f(), (Object)null, new SegmentController[0]);
        this.tmpPos = new Vector3i();
        this.blockHitCache = new Int2ObjectOpenHashMap();
        this.hitMarker = false;
        this.hitCon = new ObjectArrayList();
        this.arrayBuffer = new Int2ObjectOpenHashMap();
        this.arrayBuffer.put(0, new SegmentController[0]);
        this.arrayBuffer.put(1, new SegmentController[1]);
        this.arrayBuffer.put(2, new SegmentController[2]);
        this.arrayBuffer.put(3, new SegmentController[3]);
        this.clientPos = new Vector3f();
        this.state = var1;
        this.sectorId = var2;
        this.blockHitPool = (ObjectArrayList)threadLocalPool.get();
        this.blockHitList = (ObjectArrayList)threadCache.get();
    }

    public static void translateSector(StateInterface var0, Sector var1, Sector var2, Vector3f var3, Vector3f var4) {
        Transform var5;
        (var5 = new Transform()).setIdentity();
        var5.origin.set(var3);
        Transform var6;
        (var6 = new Transform()).setIdentity();
        Transform var7;
        (var7 = new Transform()).setIdentity();
        Vector3i var8 = StellarSystem.getPosFromSector(var1.pos, new Vector3i());
        StellarSystem.isStarSystem(var1.pos);
        Vector3i var9 = new Vector3i(var8);
        Vector3i var10 = new Vector3i(var1.pos);
        var9.scale(16);
        var9.add(8, 8, 8);
        var9.sub(var10);
        Vector3f var11;
        (var11 = new Vector3f()).set((float)var9.x * ((GameStateInterface)var0).getSectorSize(), (float)var9.y * ((GameStateInterface)var0).getSectorSize(), (float)var9.z * ((GameStateInterface)var0).getSectorSize());
        var6.setIdentity();
        var6.origin.add(var11);
        var6.basis.rotX(0.0F);
        var6.basis.transform(var6.origin);
        Transform var12;
        (var12 = new Transform()).setIdentity();
        var12.origin.set(var3);
        var12.origin.negate();
        var6.origin.add(var12.origin);
        var9 = new Vector3i(var8);
        var10 = new Vector3i(var2.pos);
        var9.scale(16);
        var9.add(8, 8, 8);
        var9.sub(var10);
        (var11 = new Vector3f()).set((float)var9.x * ((GameStateInterface)var0).getSectorSize(), (float)var9.y * ((GameStateInterface)var0).getSectorSize(), (float)var9.z * ((GameStateInterface)var0).getSectorSize());
        var7.setIdentity();
        var7.origin.add(var11);
        var7.basis.rotX(0.0F);
        var7.basis.transform(var7.origin);
        Matrix4f var14 = new Matrix4f();
        Matrix4f var13 = new Matrix4f();
        var6.getMatrix(var14);
        var7.getMatrix(var13);
        new Transform(var6);
        var7.inverse();
        var6.mul(var7);
        var5.origin.set(var6.origin);
        var5.origin.negate();
        var4.set(var5.origin);
    }

    public void addProjectile(Identifiable owner, Vector3f from, Vector3f toForce, float damage, float distance, int acidFormula, float projectileWidth, int penetrationDepth, float impactForce, long usableId, Vector4f color) {
        int index = addParticle(from, toForce);

        getParticles().setColor(index, color);
        getParticles().setOwnerId(index, owner.getId());
        getParticles().setWeaponId(index, usableId);
        getParticles().setDamage(index, damage);
        getParticles().setDamageInitial(index, damage);
        getParticles().setMaxDistance(index, distance);
        getParticles().setAcidFormulaIndex(index, acidFormula);
        getParticles().setWidth(index, projectileWidth);
        getParticles().setPenetrationDepth(index, penetrationDepth);
        getParticles().setImpactForce(index, impactForce);
        getParticles().setId(index, projectileIdGen++);

        //INSERTED CODE @225
        CannonProjectileAddEvent event = new CannonProjectileAddEvent(this, getParticles(), index);
        StarLoader.fireEvent(event, isOnServer());
        ///

    }

    public void freeBlockHit(ProjectileBlockHit var1) {
        var1.objectId = null;
        var1.setSegmentData((SegmentData)null);
        this.blockHitPool.add(var1);
    }

    public ProjectileBlockHit getBlockHit() {
        return this.blockHitPool.isEmpty() ? new ProjectileBlockHit() : (ProjectileBlockHit)this.blockHitPool.remove(this.blockHitPool.size() - 1);
    }

    public int addParticle(Vector3f var1, Vector3f var2) {
        if (this.particlePointer >= ((ProjectileParticleContainer)this.getParticles()).getCapacity() - 1) {
            ((ProjectileParticleContainer)this.getParticles()).growCapacity();
        }

        int var3 = this.idGen++;
        int var4;
        if (this.isOrderedDelete()) {
            var4 = this.particlePointer % ((ProjectileParticleContainer)this.getParticles()).getCapacity();
            ((ProjectileParticleContainer)this.getParticles()).setPos(var4, var1.x, var1.y, var1.z);
            ((ProjectileParticleContainer)this.getParticles()).setStart(var4, var1.x, var1.y, var1.z);
            ((ProjectileParticleContainer)this.getParticles()).setVelocity(var4, var2.x, var2.y, var2.z);
            ((ProjectileParticleContainer)this.getParticles()).setLifetime(var4, 0.0F);
            ((ProjectileParticleContainer)this.getParticles()).setId(var4, var3);
            ((ProjectileParticleContainer)this.getParticles()).setBlockHitIndex(var4, 0);
            ((ProjectileParticleContainer)this.getParticles()).setShotStatus(var4, 0);
            ++this.particlePointer;
            return var4;
        } else {
            var4 = this.particlePointer % ((ProjectileParticleContainer)this.getParticles()).getCapacity();
            ((ProjectileParticleContainer)this.getParticles()).setPos(var4, var1.x, var1.y, var1.z);
            ((ProjectileParticleContainer)this.getParticles()).setStart(var4, var1.x, var1.y, var1.z);
            ((ProjectileParticleContainer)this.getParticles()).setVelocity(var4, var2.x, var2.y, var2.z);
            ((ProjectileParticleContainer)this.getParticles()).setLifetime(var4, 0.0F);
            ((ProjectileParticleContainer)this.getParticles()).setId(var4, var3);
            ((ProjectileParticleContainer)this.getParticles()).setBlockHitIndex(var4, 0);
            ((ProjectileParticleContainer)this.getParticles()).setShotStatus(var4, 0);
            ++this.particlePointer;
            return var4;
        }
    }

    private boolean canHitMissile(Missile var1, Damager var2) {
        if (ServerConfig.MISSILE_DEFENSE_FRIENDLY_FIRE.isOn()) {
            return true;
        } else {
            return var2 == null || var2.getFactionId() == 0 || var2.getFactionId() != var1.getFactionId();
        }
    }

    public boolean checkCollision(Damager var1, Vector3f var2, Vector3f var3, int var4, boolean var5) {
        this.hitMarker = false;
        this.hitCon.clear();

        do {
            SegmentController[] var6;
            if ((var6 = (SegmentController[])this.arrayBuffer.get(this.hitCon.size())) == null) {
                var6 = new SegmentController[this.hitCon.size()];
                this.arrayBuffer.put(var6.length, var6);
            }

            for(int var7 = 0; var7 < var6.length; ++var7) {
                var6[var7] = (SegmentController)this.hitCon.get(var7);
            }

            if (this.checkCollision(var1, var2, var3, var4, var5, var6)) {
                Arrays.fill(var6, (Object)null);
                return true;
            }

            Arrays.fill(var6, (Object)null);
        } while(this.hitMarker);

        this.hitCon.clear();
        return false;
    }

    public boolean checkCollision(Damager var1, Vector3f var2, Vector3f var3, int var4, boolean var5, SegmentController[] var6) {
        this.rayCallbackInitial.closestHitFraction = 1.0F;
        this.rayCallbackInitial.collisionObject = null;
        this.rayCallbackInitial.setSegment((Segment)null);
        this.rayCallbackInitial.rayFromWorld.set(var2);
        this.rayCallbackInitial.rayToWorld.set(var3);
        this.rayCallbackInitial.filterModeSingleNot = true;
        this.rayCallbackInitial.setFilter(var6);
        this.rayCallbackInitial.setOwner(var1);
        this.rayCallbackInitial.setIgnoereNotPhysical(false);
        this.rayCallbackInitial.setIgnoreDebris(var5);
        this.rayCallbackInitial.setRecordAllBlocks(false);
        this.rayCallbackInitial.setZeroHpPhysical(true);
        this.rayCallbackInitial.setDamageTest(true);
        this.rayCallbackInitial.setCheckStabilizerPaths(true);
        var5 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().get(((ProjectileParticleContainer)this.getParticles()).getOwnerId(var4)) instanceof SegmentController;
        this.rayCallbackInitial.setSimpleRayTest(var5);
        ((ModifiedDynamicsWorld)this.currentPhysics.getDynamicsWorld()).rayTest(var2, var3, this.rayCallbackInitial);
        Vector3f var12;
        (var12 = new Vector3f()).sub(var3, var2);
        var12.normalize();
        if (this.rayCallbackInitial.hasHit()) {
            if (!(this.rayCallbackInitial.collisionObject instanceof CollisionObjectInterface)) {
                assert false : this.rayCallbackInitial.collisionObject;
            } else {
                CollisionType var14 = ((CollisionObjectInterface)this.rayCallbackInitial.collisionObject).getType();
                this.hitMarker = var14 == CollisionType.CUBE_STRUCTURE && this.rayCallbackInitial.getSegment() != null;
                if (this.hitMarker) {
                    this.hitCon.add(this.rayCallbackInitial.getSegment().getSegmentController());
                }

                ProjectileHandler var17 = (ProjectileHandler)this.projectileHandlers.get(var14);

                assert !(var17 instanceof ProjectileHandlerDefault) : "Default handler on type: " + var14.name() + "; " + this.rayCallbackInitial.collisionObject;

                try {
                    if (var17.handleBefore(var1, this, var2, var3, (ProjectileParticleContainer)this.getParticles(), var4, this.rayCallbackInitial).stopParticle) {
                        return true;
                    }

                    if (var17.handle(var1, this, var2, var3, (ProjectileParticleContainer)this.getParticles(), var4, this.rayCallbackInitial).stopParticle) {
                        return true;
                    }

                    if (var17.handleAfterIfNotStopped(var1, this, var2, var3, (ProjectileParticleContainer)this.getParticles(), var4, this.rayCallbackInitial).stopParticle) {
                        return true;
                    }
                } finally {
                    var17.afterHandleAlways(var1, this, var2, var3, (ProjectileParticleContainer)this.getParticles(), var4, this.rayCallbackInitial);
                }
            }
        } else {
            this.hitMarker = false;
            if (this.isOnServer()) {
                GameServerState var13 = (GameServerState)this.getState();
                Sector var15 = (Sector)this.getPhysics().getState();
                int var7 = ((ProjectileParticleContainer)this.getParticles()).getId(var4);
                Iterator var8 = var15.getMissiles().iterator();

                while(var8.hasNext()) {
                    short var9 = (Short)var8.next();
                    Missile var16;
                    if ((var16 = var13.getController().getMissileController().hasHit(var9, var7, var2, var3)) != null && this.canHitMissile(var16, var1)) {
                        float var18 = ((ProjectileParticleContainer)this.getParticles()).getDamage(var4);
                        var16.hitByProjectile(var7, var18);
                    }
                }

                if (!(var1 instanceof Mine)) {
                    var13.getController().getMineController().handleHit(this.sectorId, var2, var3);
                }
            }
        }

        return false;
    }

    public boolean isOnServer() {
        return this.state instanceof ServerStateInterface;
    }

    private void copyAdd(ProjectileController var1, int var2, Vector3f var3) {
        int var4 = var1.addEmptyParticle();
        float[] var5 = ((ProjectileParticleContainer)this.getParticles()).getArrayFloat();
        float[] var6 = ((ProjectileParticleContainer)var1.getParticles()).getArrayFloat();

        int var7;
        for(var7 = 0; var7 < 19; ++var7) {
            var6[var4 * 19 + var7] = var5[var2 * 19 + var7];
        }

        int[] var8 = ((ProjectileParticleContainer)this.getParticles()).getArrayInt();
        int[] var9 = ((ProjectileParticleContainer)var1.getParticles()).getArrayInt();

        for(var7 = 0; var7 < 9; ++var7) {
            var9[var4 * 9 + var7] = var8[var2 * 9 + var7];
        }

        ((ProjectileParticleContainer)var1.getParticles()).setPos(var4, var3.x, var3.y, var3.z);
    }

    public PhysicsExt getPhysics() {
        if (this.state instanceof GameServerState) {
            Sector var1;
            return (var1 = ((GameServerState)this.state).getUniverse().getSector(this.sectorId)) == null ? null : (PhysicsExt)var1.getPhysics();
        } else {
            return (PhysicsExt)((GameClientState)this.state).getPhysics();
        }
    }

    public StateInterface getState() {
        return this.state;
    }

    public void setState(StateInterface var1) {
        this.state = var1;
    }

    public void setSectorId(int var1) {
        this.sectorId = var1;
    }

    public Vector3f secPos(Sector var1, Sector var2, Vector3f var3) throws IOException {
        this.oldTrans.setIdentity();
        this.newTrans.setIdentity();
        float var4 = ((GameStateInterface)this.state).getGameState().getRotationProgession();
        StellarSystem.getPosFromSector(var2.pos, this.syspos);
        Vector3i var5;
        (var5 = new Vector3i()).sub(var1.pos, var2.pos);
        Vector3f var8 = new Vector3f((float)var5.x * ((GameStateInterface)this.state).getSectorSize(), (float)var5.y * ((GameStateInterface)this.state).getSectorSize(), (float)var5.z * ((GameStateInterface)this.state).getSectorSize());
        Transform var6;
        (var6 = new Transform()).setIdentity();
        var6.origin.set(var3);
        Matrix3f var9;
        (var9 = new Matrix3f()).rotX(6.2831855F * var4);
        Transform var10;
        (var10 = new Transform()).setIdentity();
        var10.origin.set(var8);
        Transform var7;
        (var7 = new Transform()).setIdentity();
        var7.origin.set(var8);
        var7.origin.negate();
        if (var2.getSectorType() != SectorType.PLANET && var1.getSectorType() == SectorType.PLANET) {
            TransformTools.rotateAroundPoint(new Vector3f(), var9, var10, this.transTmp);
        } else {
            var10.origin.set(var8);
        }

        var10.inverse();
        var10.mul(var6);
        var6.set(var10);
        return var6.origin;
    }

    private boolean translateSector(Vector3f var1, int var2) {
        assert this.state instanceof GameServerState;

        Vector3f var3 = new Vector3f(var1);
        Sector var4;
        if ((var4 = ((GameServerState)this.state).getUniverse().getSector(this.sectorId)) != null) {
            Vector3i var5 = var4.pos;
            int var6 = -1;
            Vector3f var7 = new Vector3f(var3);
            Vector3i var8 = new Vector3i();

            for(int var9 = 0; var9 < Element.DIRECTIONSi.length; ++var9) {
                (new Vector3i(Element.DIRECTIONSi[var9])).add(var5);
                Transform var10;
                (var10 = new Transform()).setIdentity();
                var10.origin.set((float)Element.DIRECTIONSi[var9].x, (float)Element.DIRECTIONSi[var9].y, (float)Element.DIRECTIONSi[var9].z);
                var10.origin.scale(((GameStateInterface)this.state).getSectorSize());
                Vector3f var11;
                (var11 = new Vector3f()).sub(var3, var10.origin);
                if (var11.lengthSquared() < var7.lengthSquared()) {
                    var7.set(var11);
                    var6 = var9;
                }
            }

            if (var6 < 0) {
                return false;
            }

            var8.set(var5);
            var8.add(Element.DIRECTIONSi[var6]);

            try {
                if (((GameServerState)this.state).getUniverse().isSectorLoaded(var8)) {
                    Sector var14;
                    if ((var14 = ((GameServerState)this.state).getUniverse().getSector(var8, false)) != null && var14.isActive()) {
                        try {
                            if (var4 != null && var14 != null && var4.getSectorType() != SectorType.PLANET && var14.getSectorType() == SectorType.PLANET) {
                                var1.set(this.secPos(var14, var4, var3));
                            } else {
                                translateSector(this.state, var4, var14, var3, var1);
                            }
                        } catch (IOException var12) {
                            var12.printStackTrace();
                        }

                        this.copyAdd(var14.getParticleController(), var2, var1);
                        return true;
                    }

                    if (var14 != null && var14.isActive()) {
                        System.err.println("[SERVER][PROJECTILE] not translating projectile to inactive sector");
                    }

                    return false;
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        } else {
            System.err.println("[SERVER][PROJECTILE] Stopping projectile: out of loaded sector range");
        }

        return true;
    }

    public void deleteParticle(int var1) {
        super.deleteParticle(var1);
    }

    protected void onUpdateStart() {
    }

    public void reset() {
        super.reset();
    }

    public boolean updateParticle(int var1, Timer var2) {
        ((ProjectileParticleContainer)this.getParticles()).getVelocity(var1, this.velocityHelper);
        ((ProjectileParticleContainer)this.getParticles()).getPos(var1, this.posHelper);
        ((ProjectileParticleContainer)this.getParticles()).getStart(var1, this.startHelper);
        this.posBeforeUpdate.set(this.posHelper);
        float var3 = ((ProjectileParticleContainer)this.getParticles()).getLifetime(var1);
        if (this.velocityHelper.length() == 0.0F) {
            ((ProjectileParticleContainer)this.getParticles()).setLifetime(var1, var3 + var2.getDelta());
        } else {
            this.velocityHelper.scale(var2.getDelta());
            ((ProjectileParticleContainer)this.getParticles()).setLifetime(var1, var3 + this.velocityHelper.length());
            this.posHelper.add(this.velocityHelper);
        }

        this.posAfterUpdate.set(this.posHelper);
        boolean var7 = false;

        float var9;
        boolean var11;
        label163: {
            label162: {
                label161: {
                    label160: {
                        try {
                            var7 = true;
                            var9 = ((ProjectileParticleContainer)this.getParticles()).getMaxDistance(var1);
                            int var4 = ((ProjectileParticleContainer)this.getParticles()).getOwnerId(var1);
                            Object var5;
                            if (((ProjectileParticleContainer)this.getParticles()).getWeaponId(var1) == -9223372036854775776L) {
                                var5 = ((MineInterface)this.state.getController()).getMineController().getMine(var4);
                            } else {
                                var5 = (Identifiable)this.state.getLocalAndRemoteObjectContainer().getLocalObjects().get(var4);
                            }

                            if (!(var5 instanceof Damager)) {
                                System.err.println("Exception: No owner for particle found: " + var4 + " -> " + var5);
                                var7 = false;
                                break label160;
                            }

                            ((ProjectileParticleContainer)this.getParticles()).getId(var1);
                            if (var5 instanceof SimpleTransformableSendableObject && !((SimpleTransformableSendableObject)var5).isOnServer() && !((SimpleTransformableSendableObject)var5).isInClientRange()) {
                                var7 = false;
                                break label161;
                            }

                            var11 = this.checkCollision((Damager)var5, this.posBeforeUpdate, this.posAfterUpdate, var1, false);
                            ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
                            if (!var11) {
                                if (var11) {
                                    var7 = false;
                                    break label163;
                                }

                                if (this.state instanceof GameServerState) {
                                    if (Math.abs(this.posHelper.x) <= ((GameStateInterface)this.state).getSectorSize() / 3.0F && Math.abs(this.posHelper.y) <= ((GameStateInterface)this.state).getSectorSize() / 3.0F && Math.abs(this.posHelper.z) <= ((GameStateInterface)this.state).getSectorSize() / 3.0F) {
                                        var7 = false;
                                        break label163;
                                    }

                                    if (!this.translateSector(this.posHelper, var1)) {
                                        var7 = false;
                                        break label163;
                                    }

                                    var7 = false;
                                    break label162;
                                }

                                var7 = false;
                                break label163;
                            }

                            var7 = false;
                        } finally {
                            if (var7) {
                                ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
                            }
                        }

                        ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
                        return false;
                    }

                    ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
                    return false;
                }

                ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
                return false;
            }

            ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
            return false;
        }

        boolean var10 = var3 < var9 && !var11;
        ((ProjectileParticleContainer)this.getParticles()).setPos(var1, this.posAfterUpdate.x, this.posAfterUpdate.y, this.posAfterUpdate.z);
        return var10;
    }

    public void update(Timer var1) {
        this.currentPhysics = null;
        if (this.getParticleCount() > 0) {
            this.currentPhysics = this.getPhysics();
            if (this.currentPhysics != null) {
                if (this.state instanceof GameClientState) {
                    this.clientPos.set(Controller.getCamera().getPos());
                }

                if (this.currentPhysics.getDynamicsWorld().getNumCollisionObjects() > 32) {
                    ((ModifiedDynamicsWorld)this.currentPhysics.getDynamicsWorld()).buildCache();
                }

                super.update(var1);
                ((ModifiedDynamicsWorld)this.currentPhysics.getDynamicsWorld()).cacheValid = false;
                return;
            }

            System.err.println("Exception: Projectile physics null for sector " + this.sectorId);
        }

    }

    public PhysicsExt getCurrentPhysics() {
        return this.currentPhysics;
    }

    public void setCurrentPhysics(PhysicsExt var1) {
        this.currentPhysics = var1;
    }

    protected ProjectileParticleContainer getParticleInstance(int var1) {
        return new ProjectileParticleContainer(var1);
    }

    public int getSectorId() {
        return this.sectorId;
    }

    public static enum ProjectileHandleState {
        PROJECTILE_IGNORE(false),
        PROJECTILE_NO_HIT(false),
        PROJECTILE_HIT_CONTINUE(false),
        PROJECTILE_NO_HIT_STOP(true),
        PROJECTILE_HIT_STOP(true),
        PROJECTILE_HIT_STOP_INVULNERABLE(true);

        final boolean stopParticle;

        private ProjectileHandleState(boolean var3) {
            this.stopParticle = var3;
        }
    }

    class BlockSorter implements Comparator<ProjectileBlockHit> {
        private BlockSorter() {
        }

        public int compare(ProjectileBlockHit var1, ProjectileBlockHit var2) {
            return Float.compare(var1.absPosition.lengthSquared(), var2.absPosition.lengthSquared());
        }
    }
}
