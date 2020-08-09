//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.missile;

import api.listener.events.register.RegisterAddonsEvent;
import api.listener.events.weapon.MissileHitByProjectileEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.vecmath.Vector3f;
import org.schema.common.FastMath;
import org.schema.common.util.linAlg.Matrix4fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.WorldDrawer;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.Hittable;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.damage.effects.MetaWeaponEffectInterface;
import org.schema.game.common.controller.damage.projectile.ProjectileController;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileElementManager;
import org.schema.game.common.data.SimpleGameObject;
import org.schema.game.common.data.explosion.ExplosionData;
import org.schema.game.common.data.explosion.ExplosionRunnable;
import org.schema.game.common.data.missile.updates.MissileDeadUpdate;
import org.schema.game.common.data.missile.updates.MissileProjectileHitUpdate;
import org.schema.game.common.data.missile.updates.MissileSpawnUpdate;
import org.schema.game.common.data.missile.updates.MissileUpdate;
import org.schema.game.common.data.missile.updates.MissileSpawnUpdate.MissileType;
import org.schema.game.common.data.physics.CollisionType;
import org.schema.game.common.data.physics.CubeRayCastResult;
import org.schema.game.common.data.physics.PairCachingGhostObjectUncollidable;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.physics.RigidDebrisBody;
import org.schema.game.common.data.player.AbstractOwnerState;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SectorNotFoundRuntimeException;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.TransformaleObjectTmpVars;
import org.schema.game.network.objects.remote.RemoteMissileUpdate;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.container.PhysicsDataContainer;
import org.schema.schine.network.objects.container.TransformTimed;
import org.schema.schine.network.server.ServerState;
import org.schema.schine.physics.Physical;

public abstract class Missile implements Damager, SimpleGameObject, Physical {
    public static final float HIT_RADIUS = 5.0F;
    private static final Transform serverTmp = new Transform();
    public static boolean OLD_EXPLOSION;
    public final Vector3f serverPos = new Vector3f();
    protected final ArrayList<MissileUpdate> pendingUpdates = new ArrayList();
    protected final ArrayList<MissileUpdate> pendingBroadcastUpdates = new ArrayList();
    protected final ArrayList<MissileUpdate> pendingClientUpdates = new ArrayList();
    private final Vector3f direction = new Vector3f();
    private final TransformTimed worldTransform;
    private final StateInterface state;
    private final boolean onServer;
    private final Transform clientTransform = new Transform();
    private final ArrayList<PlayerState> nearPlayers = new ArrayList();
    private final Vector3f nearVector = new Vector3f();
    private final Transform out = new Transform();
    private final Vector3f dist = new Vector3f();
    private final TransformaleObjectTmpVars v = new TransformaleObjectTmpVars();
    private final float[] param = new float[1];
    private final Vector3f hitNormal = new Vector3f();
    public boolean selfDamage;
    protected float distanceMade;
    private int hp;
    long nano;
    float res;
    Vector3f aabbMin = new Vector3f();
    Vector3f aabbMax = new Vector3f();
    private float speed;
    private float blastRadius = 1.0F;
    private Damager owner;
    private short colorType;
    private SphereShape sphere;
    private PairCachingGhostObjectUncollidable ghostObject;
    private boolean alive = true;
    private int damageOnServer = 1;
    private SphereShape sphereBlast;
    private short id = -1231;
    private int sectorId;
    private PhysicsDataContainer physicsDataContainer;
    private Transform initialTransform = new Transform();
    private Transform lastWorldTransformTmp = new Transform();
    private Transform trans = new Transform();
    private Transform transTmp = new Transform();
    private Vector3f absSectorPos = new Vector3f();
    private int lastClientTransCalcSectorId = -1;
    private int hitId = -1;
    private boolean killedByProjectile;
    private Transform oldTransform = new Transform();
    private float distance;
    private Transform std = new Transform();
    private Vector3i otherSecAbs = new Vector3i();
    private Vector3i belogingVector = new Vector3i();
    private SphereShape hitSphere;
    public long spawnTime = -1L;
    public int currentProjectileSector;
    private float lifetime;
    private long weaponId;
    private final DamageDealerType damageType;
    private Vector3f centerOfMass;
    private short aabbCheckNum;
    private IntSet missileHitProjecileSet;
    private float capacityConsumption;
    private int maxHp;

    public Missile(StateInterface var1) {
        this.damageType = DamageDealerType.MISSILE;
        this.centerOfMass = new Vector3f();
        this.missileHitProjecileSet = new IntOpenHashSet();
        this.capacityConsumption = 1.0F;
        this.state = var1;
        this.onServer = var1 instanceof ServerState;
        this.physicsDataContainer = new PhysicsDataContainer();
        this.worldTransform = new TransformTimed();
        this.clientTransform.setIdentity();
        this.getWorldTransform().setIdentity();
        if (this.isOnServer()) {
            this.spawnTime = var1.getUpdateTime();
        }

    }

    public InterEffectSet getAttackEffectSet(long var1, DamageDealerType var3) {
        if (this.owner != null) {
            InterEffectSet var4 = this.owner.getAttackEffectSet(var1, var3);

            assert var4 != null : this.owner;

            return var4;
        } else {
            assert false;

            return null;
        }
    }

    public MetaWeaponEffectInterface getMetaWeaponEffect(long var1, DamageDealerType var3) {
        return this.owner != null ? this.owner.getMetaWeaponEffect(var1, var3) : null;
    }

    public abstract float updateTransform(Timer var1, Transform var2, Vector3f var3, Transform var4, boolean var5);

    public boolean existsInState() {
        return ((GameServerState)this.getState()).getController().getMissileController().getMissileManager().getMissiles().containsKey(this.id);
    }

    public int getSectorId() {
        return this.sectorId;
    }

    public void calcWorldTransformRelative(int var1, Vector3i var2) {
        this.lastClientTransCalcSectorId = -1;
        if (this.getSectorId() == var1) {
            this.clientTransform.set(this.getWorldTransform());
        } else {
            boolean var3 = false;
            if (this.lastClientTransCalcSectorId != var1) {
                var3 = true;
                new Vector3i();
                Vector3i var6;
                if (this.isOnServer()) {
                    Sector var4;
                    if ((var4 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) == null) {
                        return;
                    }

                    var6 = var4.pos;
                } else {
                    RemoteSector var7;
                    if ((var7 = (RemoteSector)this.state.getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())) == null) {
                        System.err.println("Exception: Sector Not Found: " + this.getSectorId() + " for " + this + "; from sector: " + var1);
                        this.clientTransform.set(this.getWorldTransform());
                        this.clientTransform.origin.set(10000.0F, 10000.0F, 1000.0F);
                        return;
                    }

                    var6 = var7.clientPos();
                }

                StellarSystem.getPosFromSector(var2, new Vector3i());
                Vector3i var5;
                (var5 = new Vector3i()).sub(var6, var2);
                this.absSectorPos.set((float)var5.x * ((GameStateInterface)this.getState()).getSectorSize(), (float)var5.y * ((GameStateInterface)this.getState()).getSectorSize(), (float)var5.z * ((GameStateInterface)this.getState()).getSectorSize());
                this.trans.setIdentity();
                this.trans.basis.setIdentity();
                this.trans.origin.set(this.absSectorPos);
            }

            if (this.lastClientTransCalcSectorId != var1 || !this.lastWorldTransformTmp.equals(this.getWorldTransform())) {
                this.transTmp.set(this.trans);
                Matrix4fTools.transformMul(this.transTmp, this.getWorldTransform());
                this.clientTransform.set(this.getWorldTransform());
                this.clientTransform.origin.set(this.transTmp.origin);
                this.lastWorldTransformTmp.set(this.getWorldTransform());
            }

            if (var3) {
                this.lastClientTransCalcSectorId = var1;
            }

        }
    }

    public Transform getClientTransform() {
        return this.clientTransform;
    }

    public Vector3f getCenterOfMass(Vector3f var1) {
        var1.set(this.centerOfMass);
        return var1;
    }

    public Transform getClientTransformCenterOfMass(Transform var1) {
        return this.clientTransform;
    }

    public Vector3f getLinearVelocity(Vector3f var1) {
        var1.set(this.direction);
        return var1;
    }

    public SimpleTransformableSendableObject<?> getShootingEntity() {
        return this.getOwner().getShootingEntity();
    }

    public boolean isInPhysics() {
        return true;
    }

    public boolean isHidden() {
        return false;
    }

    public int getAsTargetId() {
        return this.id;
    }

    public byte getTargetType() {
        return 1;
    }

    public Transform getWorldTransformOnClient() {
        return this.clientTransform;
    }

    public void transformAimingAt(Vector3f var1, Damager var2, SimpleGameObject var3, Random var4, float var5) {
        var1.set(0.0F, 0.0F, 0.0F);
        var1.add(this.getClientTransformCenterOfMass(serverTmp).origin);
    }

    public void sendHitConfirm(byte var1) {
        if (this.getOwner() != null) {
            this.getOwner().sendHitConfirm(var1);
        }

    }

    public boolean isSegmentController() {
        return true;
    }

    public int getFactionId() {
        return this.owner != null ? this.owner.getFactionId() : 0;
    }

    public String getName() {
        return "Missile<" + this.owner.getName() + ">";
    }

    public AbstractOwnerState getOwnerState() {
        return this.owner.getOwnerState();
    }

    private Vector3i getSectorBelonging(Sector var1) {
        Vector3i var2 = var1.pos;
        this.nearVector.set(this.getWorldTransform().origin);
        StellarSystem.isStarSystem(var1.pos);
        int var9 = -10;
        int var3 = -10;
        int var4 = -10;

        for(int var5 = -1; var5 < 2; ++var5) {
            for(int var6 = -1; var6 < 2; ++var6) {
                for(int var7 = -1; var7 < 2; ++var7) {
                    this.std.setIdentity();
                    this.otherSecAbs.set(var2);
                    this.otherSecAbs.add(var7, var6, var5);
                    Sector var8;
                    if ((var8 = ((GameServerState)this.getState()).getUniverse().getSectorWithoutLoading(this.otherSecAbs)) != null) {
                        SimpleTransformableSendableObject.calcWorldTransformRelative(this.getSectorId(), var2, var8.getId(), this.std, this.state, true, this.out, this.v);
                        this.dist.sub(this.getWorldTransform().origin, this.out.origin);
                        if (this.dist.lengthSquared() < this.nearVector.lengthSquared()) {
                            this.nearVector.set(this.dist);
                            var9 = var7;
                            var3 = var6;
                            var4 = var5;
                        }
                    }
                }
            }
        }

        if (var9 > -10) {
            this.belogingVector.set(var2);
            this.belogingVector.add(var9, var3, var4);
            return this.belogingVector;
        } else {
            return var2;
        }
    }

    public void cleanUpOnEntityDelete() {
        if (!this.isOnServer()) {
            System.err.println("[CLIENT] Ending Trail for missile");
            GameClientState var1 = (GameClientState)this.getState();
            if (!this.state.isPassive()) {
                var1.getWorldDrawer().getTrailDrawer().endTrail(this);
                if (this.getSectorId() == var1.getCurrentSectorId()) {
                    ((GameClientController)this.getState().getController()).queueTransformableAudio("0022_explosion_one", this.getWorldTransform(), 20.0F);
                    ((GameClientState)this.getState()).getWorldDrawer().getExplosionDrawer().addExplosion(this.getWorldTransform().origin, 10.0F, this.weaponId);
                }
            }
        }

    }

    public void clearAllUpdates() {
        this.pendingUpdates.clear();
        this.pendingBroadcastUpdates.clear();
    }

    public void createConstraint(Physical var1, Physical var2, Object var3) {
    }

    public Transform getInitialTransform() {
        return this.initialTransform;
    }

    public float getMass() {
        return 0.0F;
    }

    public PhysicsDataContainer getPhysicsDataContainer() {
        return this.physicsDataContainer;
    }

    public void setPhysicsDataContainer(PhysicsDataContainer var1) {
        this.physicsDataContainer = var1;
    }

    public StateInterface getState() {
        return this.state;
    }

    public void getTransformedAABB(Vector3f var1, Vector3f var2, float var3, Vector3f var4, Vector3f var5, Transform var6) {
    }

    public void initPhysics() {
        this.ghostObject = new PairCachingGhostObjectUncollidable(CollisionType.MISSILE, this.getPhysicsDataContainer());
        this.ghostObject.setWorldTransform(this.getInitialTransform());
        this.oldTransform.set(this.getInitialTransform());
        this.sphereBlast = new SphereShape(this.getBlastRadius());
        this.sphere = new SphereShape(0.5F);
        this.sphere.setMargin(0.1F);
        this.ghostObject.setCollisionShape(this.sphere);
        this.ghostObject.setUserPointer((Object)null);
        this.hitSphere = new SphereShape(5.0F);
        this.hitSphere.getAabb(this.getInitialTransform(), this.aabbMin, this.aabbMax);
        this.getPhysicsDataContainer().setObject(this.ghostObject);
        this.getPhysicsDataContainer().setShape(this.sphere);
        this.getPhysicsDataContainer().updatePhysical(this.getState().getUpdateTime());
        this.ghostObject.setCollisionFlags(this.ghostObject.getCollisionFlags() | 4);
        if (this.isOnServer()) {
            this.currentProjectileSector = this.getSectorId();
            this.addToSectorPhysicsForProjectileCheck(this.currentProjectileSector);
        }

    }

    public void endTrail() {
        GameClientState var1;
        if ((var1 = (GameClientState)this.getState()).getWorldDrawer() != null && var1.getWorldDrawer().getTrailDrawer() != null) {
            var1.getWorldDrawer().getTrailDrawer().endTrail(this);
        }

    }

    public float getBlastRadius() {
        return this.blastRadius;
    }

    public int getDamage() {
        return this.damageOnServer;
    }

    public void setDamage(int var1) {
        this.damageOnServer = var1;
        this.blastRadius = FastMath.pow((float)var1 / (Float)ServerConfig.MISSILE_RADIUS_HP_BASE.getCurrentState() / 4.1887903F, 0.33333334F);
    }

    public Vector3f getDirection(Vector3f var1) {
        var1.set(this.direction);
        return var1;
    }

    public short getId() {
        return this.id;
    }

    public void setId(short var1) {
        this.id = var1;
    }

    public Damager getOwner() {
        return this.owner;
    }

    public void setOwner(Damager var1) {
        this.owner = var1;
    }

    public Sector getSector(int var1) {
        assert this.isOnServer();

        return ((GameServerState)this.getState()).getUniverse().getSector(var1);
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float var1) {
        this.speed = var1;
    }

    public abstract MissileType getType();

    public TransformTimed getWorldTransform() {
        return this.worldTransform;
    }

    public boolean hasPendingBroadcastUpdates() {
        return !this.pendingBroadcastUpdates.isEmpty();
    }

    public boolean hasPendingUpdates() {
        return !this.pendingUpdates.isEmpty();
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean var1) {
        this.alive = var1;
    }

    public boolean isOnServer() {
        return this.onServer;
    }

    public List<PlayerState> nearPlayers() {
        return this.nearPlayers;
    }

    public void addToSectorPhysicsForProjectileCheck(int var1) {
        this.ghostObject.setCollisionFlags(4);
        Sector var2;
        if ((var2 = this.getSector(var1)) != null) {
            var2.addMissile(this.id);
        }

    }

    public void removeFromSectorPhysicsForProjectileCheck(int var1) {
        Sector var2;
        if ((var2 = this.getSector(var1)) != null) {
            var2.removeMissile(this.id);
        }

    }

    public abstract void onSpawn();

    public void sendPendingBroadcastUpdates(ClientChannel var1) {
        for(int var2 = 0; var2 < this.pendingBroadcastUpdates.size(); ++var2) {
            MissileUpdate var3 = (MissileUpdate)this.pendingBroadcastUpdates.get(var2);
            var1.getNetworkObject().missileUpdateBuffer.add(new RemoteMissileUpdate(var3, this.isOnServer()));
        }

    }

    public void sendPendingUpdates(ClientChannel var1) {
        for(int var2 = 0; var2 < this.pendingUpdates.size(); ++var2) {
            MissileUpdate var3 = (MissileUpdate)this.pendingUpdates.get(var2);
            var1.getNetworkObject().missileUpdateBuffer.add(new RemoteMissileUpdate(var3, this.isOnServer()));
        }

    }

    public void setDirection(Vector3f var1) {
        assert !Float.isNaN(var1.x) && !Float.isNaN(var1.y) && !Float.isNaN(var1.z) : var1 + " became NaN";

        this.direction.set(var1);
    }

    public void setFromSpawnUpdate(MissileSpawnUpdate var1) {
        this.getWorldTransform().setIdentity();
        this.id = var1.id;
        this.sectorId = var1.sectorId;

        assert var1.missileType == this.getType() : var1.missileType + " --- " + this.getType();

        this.setDirection(var1.dir);
        this.getWorldTransform().origin.set(var1.position);
        this.weaponId = var1.weaponId;
        this.speed = var1.speed;
        this.colorType = var1.colorType;
        this.spawnTime = var1.spawnTime;
        if (this instanceof ActivationMissileInterface) {
            ((ActivationMissileInterface)this).setActivationTimer(var1.bombActivationTime);
        }

    }

    public void setSectorId(int var1, boolean var2) {
        this.sectorId = var1;
    }

    protected void setTransformMissile(Transform var1) {
        this.oldTransform.set(this.getWorldTransform());
        this.getWorldTransform().set(var1);
        if (this.isOnServer()) {
            this.ghostObject.setWorldTransform(var1);
        }

    }

    public void startTrail() {
        GameClientState var1;
        if ((var1 = (GameClientState)this.getState()).getController().isNeighborToClientSector(this.getSectorId())) {
            if (var1.getWorldDrawer() != null && var1.getWorldDrawer().getTrailDrawer() != null) {
                var1.getWorldDrawer().getTrailDrawer().startTrail(this);
                return;
            }

            System.err.println("[CLIENT] Cannot add Trail for missile (drawer not initialized)");
        }

    }

    public void testCollision(Sector var1, Sector var2) {
        assert this.isOnServer();

        if (this.owner == null) {
            System.err.println("[MISSILE] Exception: OWNER IS NULL");
        }

        Transform var3 = new Transform(this.oldTransform);
        Transform var4 = new Transform(this.getWorldTransform());
        Vector3f var5;
        if (var1 != var2) {
            var5 = new Vector3f(var4.origin);
            Vector3f var6 = new Vector3f();
            ProjectileController.translateSector(this.state, var1, var2, var5, var6);
            var4.origin.set(var6);
            var5.set(var3.origin);
            ProjectileController.translateSector(this.state, var1, var2, var5, var6);
            var3.origin.set(var6);
            if (this.currentProjectileSector != var2.getId()) {
                this.removeFromSectorPhysicsForProjectileCheck(this.currentProjectileSector);
                this.currentProjectileSector = var2.getId();
                this.addToSectorPhysicsForProjectileCheck(this.currentProjectileSector);
            }
        }

        this.hitSphere.getAabb(var4, this.aabbMin, this.aabbMax);
        PhysicsExt var19 = (PhysicsExt)var2.getPhysics();
        (var5 = new Vector3f()).sub(var4.origin, var3.origin);
        if (var4.basis.determinant() != 0.0F && var5.lengthSquared() > 0.0F) {
            assert var19 != null;

            assert this.ghostObject != null;

            Damager var18 = null;
            if (!this.canHitSelf()) {
                var18 = this.getOwner();
            }

            CubeRayCastResult var20;
            (var20 = new CubeRayCastResult(var3.origin, var4.origin, var18, new SegmentController[0])).setDamageTest(true);
            var19.testRayCollisionPoint(var3.origin, var4.origin, var20, false);
            if (var20.hasHit()) {
                Transform var7;
                (var7 = new Transform(var4)).origin.set(var20.hitPointWorld);
                Vector3f var8 = new Vector3f();
                Vector3f var9 = new Vector3f();
                Vector3f var10 = new Vector3f();
                Vector3f var11 = new Vector3f();
                this.sphereBlast.getAabb(var7, var8, var9);
                ObjectArrayList var21 = var19.getDynamicsWorld().getCollisionObjectArray();
                ObjectArrayList var12 = new ObjectArrayList();
                Transform var13 = new Transform();

                for(int var14 = 0; var14 < var21.size(); ++var14) {
                    CollisionObject var15;
                    if (!((var15 = (CollisionObject)var21.get(var14)) instanceof PairCachingGhostObjectUncollidable) && !(var15 instanceof RigidDebrisBody)) {
                        var15.getCollisionShape().getAabb(var15.getWorldTransform(var13), var10, var11);
                        if (AabbUtil2.testAabbAgainstAabb2(var8, var9, var10, var11)) {
                            var12.add(var15);
                        }
                    }
                }

                if (var20.hasHit()) {
                    ExplosionData var22;
                    (var22 = new ExplosionData()).damageType = this.damageType;
                    var22.centerOfExplosion = new Transform(var7);
                    var22.fromPos = new Vector3f(var3.origin);
                    var22.toPos = new Vector3f(var4.origin);
                    Vector3f var23;
                    (var23 = new Vector3f()).sub(var22.fromPos, var22.toPos);
                    var23.normalize();
                    var23.scale(0.48F);
                    var22.centerOfExplosion.origin.add(var23);
                    var22.ignoreShields = this.isIgnoreShields();
                    var22.ignoreShieldsSelf = this.isIgnoreShieldsSelf();
                    var22.radius = this.getBlastRadius();
                    var22.damageInitial = (float)this.damageOnServer;
                    var22.damageBeforeShields = 0.0F;
                    var22.originSectorId = var1.getId();
                    var22.sectorId = var2.getId();
                    var22.hitsFromSelf = this.selfDamage;
                    var22.from = this.getOwner();
                    var22.weaponId = this.weaponId;
                    var22.hitType = HitType.WEAPON;
                    var22.attackEffectSet = this.getAttackEffectSet(this.weaponId, var22.damageType);
                    if (this.getOwner() != null && this.getOwner() instanceof SegmentController) {
                        ((SendableSegmentController)this.getOwner()).sendExplosionGraphic(var22.centerOfExplosion.origin);
                    }

                    if (!this.isDud()) {
                        ExplosionRunnable var17 = new ExplosionRunnable(var22, var2);
                        ((GameServerState)this.getState()).enqueueExplosion(var17);
                    } else {
                        this.onDud();
                    }

                    this.setAlive(false);
                }
            }

        } else {
            if (var5.lengthSquared() != 0.0F) {
                try {
                    throw new IllegalStateException("[MISSILE] WORLD TRANSFORM (DIR: " + var5 + "; len " + var5.length() + ") IS STRANGE OR PHYSICS NOT INITIALIZED \n" + var4.basis + ";\n" + this);
                } catch (Exception var16) {
                    var16.printStackTrace();
                    this.setAlive(false);
                }
            }

        }
    }

    public void onDud() {
    }

    protected boolean isIgnoreShieldsSelf() {
        return false;
    }

    protected boolean isIgnoreShields() {
        return false;
    }

    protected boolean canHitSelf() {
        return false;
    }

    protected boolean isDud() {
        return false;
    }

    public String toString() {
        return "Missile(" + this.getId() + " s[" + this.getSectorId() + "] OWN: " + this.owner + ")";
    }

    public void translateTrail() {
        GameClientState var1;
        if ((var1 = (GameClientState)this.getState()).getWorldDrawer() != null && var1.getWorldDrawer().getTrailDrawer() != null) {
            this.calcWorldTransformRelative(var1.getCurrentSectorId(), var1.getPlayer().getCurrentSector());
            var1.getWorldDrawer().getTrailDrawer().translateTrail(this);
        }

    }

    public abstract void updateClient(Timer var1);

    public void updateServer(Timer var1) {
        this.addLifetime(var1);
        Sector var5;
        if ((var5 = ((GameServerState)this.state).getUniverse().getSector(this.sectorId)) != null) {
            if (this.isAlive()) {
                try {
                    try {
                        Vector3i var2 = this.getSectorBelonging(var5);
                        Sector var6;
                        if ((var6 = ((GameServerState)this.state).getUniverse().getSectorWithoutLoading(var2)) != null) {
                            this.testCollision(var5, var6);
                        }
                    } catch (SectorNotFoundRuntimeException var3) {
                        System.err.println("Exception catched: sector not found. Ending missile " + this.id + ". (Ignore fatal exception output)");
                        var3.printStackTrace();
                        this.setAlive(false);
                    }
                } catch (IllegalStateException var4) {
                    var4.printStackTrace();
                }
            }
        } else {
            System.err.println("[MISSILE] WARNING: Missile sector is no longer loaded: " + this.sectorId + "; ending missile!");
            this.setAlive(false);
        }

        if (this.isAlive() && this.distanceMade > this.getDistance()) {
            this.setAlive(false);
            System.err.println("[SERVER] MISSILE DIED FROM LIFETIME DISTANCE");
        }

        if (this.isAlive() && this.getMissileTimeoutMs() > 0L && this.state.getUpdateTime() - this.spawnTime > this.getMissileTimeoutMs()) {
            this.setAlive(false);
            System.err.println("[SERVER] MISSILE DIED FROM LIFETIME TIMEOUT (" + this.getMissileTimeoutMs() + ")");
        }

        if (this.isKilledByProjectile()) {
            this.setAlive(false);
        }

        if (!this.isAlive()) {
            MissileDeadUpdate var7 = new MissileDeadUpdate(this.getId());
            this.onDeadServer();
            var7.setHitId(this.hitId);
            this.pendingBroadcastUpdates.add(var7);
        }

    }

    protected long getMissileTimeoutMs() {
        return -1L;
    }

    protected void onDeadServer() {
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float var1) {
        this.distance = var1;
    }

    public short getColorType() {
        return this.colorType;
    }

    public void setColorType(short var1) {
        this.colorType = var1;
    }

    public Transform getOldTransform() {
        return this.oldTransform;
    }

    public boolean hasHit(Vector3f var1, Vector3f var2) {
        this.param[0] = 1.0F;
        this.hitNormal.set(0.0F, 0.0F, 0.0F);
        return AabbUtil2.rayAabb(var1, var2, this.aabbMin, this.aabbMax, this.param, this.hitNormal);
    }

    public boolean isKilledByProjectile() {
        return this.killedByProjectile;
    }

    public void setKilledByProjectile(boolean var1) {
        this.killedByProjectile = var1;
    }

    public Transform getWorldTransformRelativeToSector(int var1, Transform var2) {
        Sector var3;
        if ((var3 = ((GameServerState)this.getState()).getUniverse().getSector(var1)) != null) {
            SimpleTransformableSendableObject.calcWorldTransformRelative(var1, var3.pos, this.getSectorId(), this.getWorldTransform(), this.state, true, var2, this.v);
            return var2;
        } else {
            return null;
        }
    }

    public int getHp() {
        return this.hp;
    }

    public void setupHp(float var1) {
        switch(DumbMissileElementManager.MISSILE_HP_CALC_STYLE) {
            case LINEAR:
                var1 = DumbMissileElementManager.MISSILE_HP_MIN + var1 * DumbMissileElementManager.MISSILE_HP_PER_DAMAGE;
                break;
            case EXP:
                var1 = DumbMissileElementManager.MISSILE_HP_MIN + Math.max(0.0F, (float)Math.pow((double)var1, (double)DumbMissileElementManager.MISSILE_HP_EXP) * DumbMissileElementManager.MISSILE_HP_EXP_MULT);
                break;
            case LOG:
                var1 = DumbMissileElementManager.MISSILE_HP_MIN + Math.max(0.0F, ((float)Math.log10((double)var1) + DumbMissileElementManager.MISSILE_HP_LOG_OFFSET) * DumbMissileElementManager.MISSILE_HP_LOG_FACTOR);
                break;
            default:
                this.hp = 1;
                throw new RuntimeException("Illegal calc style " + DumbMissileElementManager.MISSILE_HP_CALC_STYLE);
        }

        this.maxHp = Math.round(var1);
        this.hp = Math.round(var1);
    }

    public void hitByProjectile(int projectileId, float projectileDamage) {
        this.missileHitProjecileSet.add(projectileId);
        int var10000 = this.hp;
        this.hp = (int)Math.max(0.0F, (float)this.hp - projectileDamage);
        if (this.hp <= 0) {
            this.setKilledByProjectile(true);
        }

        //INSERTED CODE
        MissileHitByProjectileEvent event = new MissileHitByProjectileEvent(this, projectileId, projectileDamage);
        StarLoader.fireEvent(event, isOnServer());
        ///

        MissileProjectileHitUpdate var3;
        (var3 = new MissileProjectileHitUpdate(this.getId())).percent = this.maxHp > 0 ? (float)this.hp / (float)this.maxHp : 0.0F;
        this.pendingBroadcastUpdates.add(var3);
    }

    public void sendClientMessage(String var1, int var2) {
        if (this.owner != null) {
            this.owner.sendClientMessage(var1, var2);
        }

    }

    public void sendServerMessage(Object[] var1, int var2) {
        if (this.owner != null) {
            this.owner.sendServerMessage(var1, var2);
        }

    }

    public float getDamageGivenMultiplier() {
        return this.owner != null ? this.owner.getDamageGivenMultiplier() : 1.0F;
    }

    public void addLifetime(Timer var1) {
        this.lifetime += var1.getDelta();
    }

    public float getLifetime() {
        return this.lifetime;
    }

    public long getWeaponId() {
        return this.weaponId;
    }

    public void setWeaponId(long var1) {
        this.weaponId = var1;
    }

    public long getOwnerId() {
        return this.owner.getOwnerState() != null ? this.owner.getOwnerState().getDbId() : -9223372036854775808L;
    }

    public void onClientDie(int var1) {
        this.endTrail();
        Sendable var2;
        if ((var2 = (Sendable)this.state.getLocalAndRemoteObjectContainer().getLocalObjects().get(var1)) != null && var2 instanceof Hittable) {
            System.err.println("[CLIENT] missile dead update. HIT " + var2);
        }

    }

    public boolean hadHit(int var1) {
        return this.missileHitProjecileSet.contains(var1);
    }

    public void onClientProjectileHit(float var1) {
        WorldDrawer var2;
        if ((var2 = ((GameClientState)this.getState()).getWorldDrawer()) != null) {
            var2.getExplosionDrawer().addShieldBubbleHit(this.getWorldTransformOnClient().origin, var1);
        }

    }

    public boolean isInNeighborSecorToServer(Vector3i var1) {
        assert this.isOnServer();

        Sector var2;
        return (var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId())) != null && Sector.isNeighbor(var1, var2.pos);
    }

    public float getCapacityConsumption() {
        return this.capacityConsumption;
    }

    public void setCapacityConsumption(float var1) {
        this.capacityConsumption = var1;
    }
}
