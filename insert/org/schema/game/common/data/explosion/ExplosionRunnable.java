//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.explosion;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.game.common.controller.EditableSendableSegmentController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.TransientSegmentController;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.Hittable;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.ShieldLocalAddOn;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.data.BlockBulkSerialization;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementDocking;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.physics.CubeShape;
import org.schema.game.common.data.physics.PairCachingGhostObjectUncollidable;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.physics.RigidDebrisBody;
import org.schema.game.common.data.player.AbstractCharacter;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SectorNotFoundException;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.network.objects.remote.RemoteBlockBulk;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.remote.RemoteVector4f;

public class ExplosionRunnable implements Runnable {
    private static final ExplosionDataHandler dataHandler = new ExplosionDataHandler();
    private static final List<ExplosionCollisionSegmentCallback> pool = new ObjectArrayList();
    private static ObjectOpenHashSet<Segment> lockedSegments = new ObjectOpenHashSet(512);
    private static int running;
    private static boolean init;
    private SphereShape sphereBlast;
    private ExplosionData explosion;
    private Sector sector;
    private ObjectArrayList<Sendable> hitBuffer = new ObjectArrayList();
    private ExplosionCollisionSegmentCallback callback;
    private static final InterEffectSet voidEffectSet = new InterEffectSet();

    public ExplosionRunnable(ExplosionData var1, Sector var2) {
        this.explosion = var1;
        this.sector = var2;
    }

    public static void initialize() {
        synchronized(pool) {
            if (!init) {
                dataHandler.loadData();

                for(int var1 = 0; var1 < 4; ++var1) {
                    pool.add(new ExplosionCollisionSegmentCallback(dataHandler));
                }

                init = true;
            }

        }
    }

    private static ExplosionCollisionSegmentCallback getCallback() {
        synchronized(pool) {
            return !pool.isEmpty() ? (ExplosionCollisionSegmentCallback)pool.remove(pool.size() - 1) : new ExplosionCollisionSegmentCallback(dataHandler);
        }
    }

    private static void freeCallback(ExplosionCollisionSegmentCallback var0) {
        synchronized(pool) {
            pool.add(var0);
        }
    }

    public GameServerState getState() {
        return this.sector.getState();
    }

    public PhysicsExt getPhysics() {
        return (PhysicsExt)this.sector.getPhysics();
    }

    public boolean canExecute() {
        return running < (Integer)ServerConfig.MAX_SIMULTANEOUS_EXPLOSIONS.getCurrentState();
    }

    public boolean readyToRun() {
        return this.areSegmentsLocked();
    }

    private boolean areSegmentsLocked() {
        synchronized(lockedSegments) {
            return Collections.disjoint(lockedSegments, this.callback.ownLockedSegments);
        }
    }

    public boolean beforeExplosion() {
        PhysicsExt var1;
        if ((var1 = this.getPhysics()) == null) {
            System.err.println("[SERVER][WARNING] not spawned missile in unloaded sector: " + this.sector);
            return false;
        } else {
            DynamicsWorld var12;
            if ((var12 = var1.getDynamicsWorld()) == null) {
                System.err.println("[SERVER][WARNING] not spawned missile in unloaded sector (dynWorld): " + this.sector);
                return false;
            } else {
                synchronized(pool) {
                    ++running;
                }

                this.callback = getCallback();
                this.callback.reset();
                this.hitBuffer.clear();
                Vector3f var2 = new Vector3f();
                Vector3f var3 = new Vector3f();
                Vector3f var4 = new Vector3f();
                Vector3f var5 = new Vector3f();
                this.sphereBlast = new SphereShape(this.explosion.radius);
                this.sphereBlast.getAabb(this.explosion.centerOfExplosion, var2, var3);
                com.bulletphysics.util.ObjectArrayList var13 = var12.getCollisionObjectArray();
                ObjectArrayList var6 = new ObjectArrayList();
                Transform var7 = new Transform();

                int var8;
                for(var8 = 0; var8 < var13.size(); ++var8) {
                    CollisionObject var9;
                    if (!((var9 = (CollisionObject)var13.get(var8)) instanceof PairCachingGhostObjectUncollidable) && !(var9 instanceof RigidDebrisBody)) {
                        var9.getCollisionShape().getAabb(var9.getWorldTransform(var7), var4, var5);
                        if (AabbUtil2.testAabbAgainstAabb2(var2, var3, var4, var5)) {
                            var6.add(var9);
                        }
                    }
                }

                for(var8 = 0; var8 < var6.size(); ++var8) {
                    CollisionObject var14;
                    if ((var14 = (CollisionObject)var6.get(var8)).getUserPointer() != null && var14.getUserPointer() instanceof Integer) {
                        int var15 = (Integer)var14.getUserPointer();
                        Sendable var16 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var15);
                        Damager var20 = this.explosion.from;
                        if (this.explosion.hitsFromSelf || var16 != var20) {
                            SegmentController var23;
                            if (var16 != null && var20 != null && var16 instanceof SegmentController && var20 instanceof SegmentController) {
                                var23 = (SegmentController)var20;
                                SegmentController var24 = (SegmentController)var16;
                                if (var23.railController.isInAnyRailRelationWith(var24)) {
                                    if (!this.explosion.hitsFromSelf) {
                                        continue;
                                    }

                                    if (this.explosion.ignoreShieldsSelf) {
                                        this.callback.entitiesToIgnoreShieldsOn.add(var24.getId());
                                    }
                                }
                            }

                            if (var16 != null && var16 instanceof Hittable && ((Hittable)var16).isPhysicalForDamage()) {
                                if (var16 instanceof SegmentController && var20 != null && var20 instanceof SegmentController) {
                                    var23 = (SegmentController)var16;
                                    if (!this.explosion.hitsFromSelf && var23.getDockingController().isDocked() && var23.getDockingController().isInAnyDockingRelation((SegmentController)var20) || !this.explosion.hitsFromSelf && var23.railController.isInAnyRailRelationWith((SegmentController)var20)) {
                                        continue;
                                    }
                                }

                                if (((Hittable)var16).canBeDamagedBy(this.explosion.from, DamageDealerType.MISSILE) && ((Hittable)var16).checkAttack(this.explosion.from, true, this.explosion.damageType != DamageDealerType.EXPLOSIVE)) {
                                    this.hitBuffer.add(var16);
                                    if (var16 instanceof SegmentController) {
                                        Iterator var26 = (var23 = (SegmentController)var16).getDockingController().getDockedOnThis().iterator();

                                        while(var26.hasNext()) {
                                            ElementDocking var18 = (ElementDocking)var26.next();
                                            this.hitBuffer.add(var18.from.getSegment().getSegmentController());
                                        }

                                        var23.railController.getRoot().railController.getAll(this.hitBuffer);
                                    }
                                }
                            }
                        }
                    }
                }

                ExplosionPhysicsSegemtsChecker var28 = new ExplosionPhysicsSegemtsChecker();
                this.callback.centerOfExplosion.set(this.explosion.centerOfExplosion.origin);
                this.callback.explosionRadius = this.explosion.radius;
                this.callback.hitType = this.explosion.hitType;
                this.callback.attack = this.explosion.attackEffectSet;

                assert this.explosion.attackEffectSet != null;

                this.callback.ignoreShieldsGlobal = this.explosion.ignoreShields;
                this.callback.useLocalShields = true;
                this.callback.damageType = this.explosion.damageType;
                this.callback.weaponId = this.explosion.weaponId;
                this.callback.shieldDamageBonus = VoidElementManager.EXPLOSION_SHIELD_DAMAGE_BONUS;

                assert this.callback.cubeCallbackPointer == 0;

                Iterator var29 = this.hitBuffer.iterator();

                while(true) {
                    while(true) {
                        while(true) {
                            Sendable var19;
                            do {
                                do {
                                    do {
                                        if (!var29.hasNext()) {
                                            try {
                                                this.callback.sortInsertShieldAndArmorValues(this.explosion.centerOfExplosion.origin, this.explosion.sectorId, this.hitBuffer);
                                            } catch (SectorNotFoundException var10) {
                                                var10.printStackTrace();
                                                this.explosion.damageInitial = 0.0F;
                                            }

                                            return true;
                                        }
                                    } while(!((var19 = (Sendable)var29.next()) instanceof Hittable));
                                } while(!((Hittable)var19).isVulnerable());
                            } while(!((Hittable)var19).checkAttack(this.explosion.from, true, this.explosion.damageType != DamageDealerType.EXPLOSIVE));

                            if (var19 instanceof SegmentController) {
                                SegmentController var21;
                                CubeShape var17 = (CubeShape)(var21 = (SegmentController)var19).getPhysicsDataContainer().getShapeChild().childShape;
                                Object var22 = var21.getWorldTransform();
                                if (this.explosion.sectorId != var21.getSectorId()) {
                                    Sector var25 = ((GameServerState)var21.getState()).getUniverse().getSector(this.explosion.sectorId);
                                    Sector var27 = ((GameServerState)var21.getState()).getUniverse().getSector(var21.getSectorId());
                                    if (var25 == null || var27 == null) {
                                        System.err.println("[EXPLOSION] ERROR: SECTOR NOT LOADED: " + this.explosion.sectorId + ": " + var25 + "; " + var21.getSectorId() + ": " + var27);
                                        continue;
                                    }

                                    var21.calcWorldTransformRelative(var25.getSectorId(), var25.pos);
                                    var22 = var21.getClientTransform();
                                }

                                var28.processCollision(var17, (Transform)var22, this.sphereBlast, this.explosion.centerOfExplosion, this.callback);
                            } else if (var19 instanceof AbstractCharacter) {
                                this.callback.addCharacterHittable((AbstractCharacter)var19);
                            }
                        }
                    }
                }
            }
        }
    }

    public void run() {
        synchronized(lockedSegments) {
            boolean var2 = false;

            while(true) {
                if (this.readyToRun()) {
                    if (var2) {
                        this.callback.updateCallbacks();
                    }

                    lockedSegments.addAll(this.callback.ownLockedSegments);
                    break;
                }

                var2 = true;

                try {
                    lockedSegments.wait();
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }
        }

        if (!this.hitBuffer.isEmpty()) {
            this.callback.sortAndInsertCallbackCache();
            float var1 = this.explosion.damageInitial * (this.explosion.from != null ? this.explosion.from.getDamageGivenMultiplier() : 1.0F);
            dataHandler.applyDamage(this.callback, (int)this.explosion.radius, var1);
        }

        synchronized(this.getState().getExplosionOrdersFinished()) {
            this.getState().getExplosionOrdersFinished().enqueue(this);
        }
    }

    public void afterExplosion() {
        this.applyCallbackDamage(this.callback);
        synchronized(lockedSegments) {
            lockedSegments.removeAll(this.callback.ownLockedSegments);
            lockedSegments.notifyAll();
        }

        freeCallback(this.callback);
        this.callback = null;
        synchronized(pool) {
            --running;
        }
    }

    private void applyCallbackDamage(ExplosionCollisionSegmentCallback var1) {
        ExplosionCubeConvexBlockCallback[] var2 = var1.callbackCache;
        SegmentData var3 = null;
        var1.hitSegments.clear();
        var1.hitSendSegments.clear();
        long var4 = System.currentTimeMillis();
        ByteArrayList var6 = null;
        int var8 = 0;
        var1.sentDamage.clear();

        Sendable var11;
        SegmentController var16;
        try {
            for(int var9 = 0; var9 < var1.cubeCallbackPointer; ++var9) {
                ExplosionCubeConvexBlockCallback var10;
                if ((var10 = var2[var9]).type == 1) {
                    if ((var11 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var10.segEntityId)) != null && var11 instanceof AbstractCharacter) {
                        AbstractCharacter var12 = (AbstractCharacter)var11;
                        if (var10.blockHpOrig > var10.blockHp) {
                            var12.getOwnerState().damage((float)(var10.blockHpOrig - var10.blockHp), var12, this.explosion.from);
                        }
                    }
                } else if (var10.type == 0) {
                    try {
                        var10.data.checkWritable();
                    } catch (SegmentDataWriteException var30) {
                        var30.printStackTrace();

                        assert var10.data.getSegment().getSegmentData() == var10.data : var10.data.getSegment().getSegmentData() + "; " + var10.data;

                        var10.data = SegmentDataWriteException.replaceDataOnServer(var10.data);
                    }

                    if (var10.data.getSegment() == null) {
                        Segment var13;
                        if ((var11 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var10.segEntityId)) == null || !(var11 instanceof SegmentController) || (var13 = ((SegmentController)var11).getSegmentBuffer().get(var10.segmentPos)).isEmpty()) {
                            continue;
                        }

                        var10.data = var13.getSegmentData();
                    }

                    try {
                        var10.data.checkWritable();
                    } catch (SegmentDataWriteException var29) {
                        var29.printStackTrace();
                        var10.data = SegmentDataWriteException.replaceDataOnServer(var10.data);
                    }

                    if (var3 != var10.data) {
                        if (var3 != null) {
                            var3.rwl.writeLock().unlock();
                        }

                        (var3 = var10.data).rwl.writeLock().lock();
                        var1.hitSegments.add(var10.data.getSegment());
                        Long2ObjectOpenHashMap var7;
                        if ((var7 = (Long2ObjectOpenHashMap)var1.hitSendSegments.get(var10.data.getSegmentController())) == null) {
                            var7 = new Long2ObjectOpenHashMap();
                            var1.hitSendSegments.put(var10.data.getSegmentController(), var7);
                        }

                        long var42 = ElementCollection.getIndex(var10.data.getSegment().pos);
                        if ((var6 = (ByteArrayList)var7.get(var42)) == null) {
                            var6 = BlockBulkSerialization.getBufferServer();
                            var7.put(var42, var6);
                        }

                        var1.hitSegments.add(var10.data.getSegment());
                    }

                    if (!var10.data.getSegment().isEmpty()) {
                        short var44 = var10.data.getType(var10.segDataIndex);
                        int var50 = var10.blockHpOrig;
                        if (var44 != 0 && var10.blockHp != var50) {
                            int var14;
                            if ((var14 = Math.max(0, var50 - var10.blockHp)) > 0) {
                                var14 = (int)((float)var14 + (float)var14 * VoidElementManager.EXPLOSION_HULL_DAMAGE_BONUS);
                                var10.blockHp = Math.max(0, Math.min(var50, var50 - var14));
                            }

                            short var15;
                            if (var50 - var14 <= 0) {
                                var15 = 0;
                                boolean var35 = var10.data.isActive(var10.segDataIndex);
                                byte var43 = var10.data.getOrientation(var10.segDataIndex);
                                if (var44 != 1) {
                                    var10.data.setType(var10.segDataIndex, (short)0);
                                }

                                var10.data.setHitpointsByte(var10.segDataIndex, 0);
                                var16 = var10.data.getSegmentController();
                                if (var44 != 1) {
                                    var10.data.onRemovingElement(var10.segDataIndex, var10.segPosX, var10.segPosY, var10.segPosZ, var44, false, false, var43, var35, false, var4, false);
                                }

                                if (this.explosion.chain) {
                                    SegmentPiece var45;
                                    (var45 = new SegmentPiece(var10.data.getSegment(), var10.segPosX, var10.segPosY, var10.segPosZ)).setType(var44);
                                    var16.onBlockKill(var45, this.explosion.from);
                                }

                                var16.getHpController().onElementDestroyed(this.explosion.from, ElementKeyMap.getInfo(var44), DamageDealerType.MISSILE, this.explosion.weaponId);
                            } else {
                                var15 = ElementKeyMap.convertToByteHP(var44, var50 - var14);
                                var10.data.setHitpointsByte(var10.segDataIndex, (byte)var15);
                            }

                            var6.add(var10.segPosX);
                            var6.add(var10.segPosY);
                            var6.add(var10.segPosZ);
                            var6.add((byte)var15);
                            if (!var1.sentDamage.contains(var10.data.getSegmentController().getId())) {
                                if (this.explosion.from == null || !(this.explosion.from instanceof SegmentController) || !((SegmentController)this.explosion.from).railController.isInAnyRailRelationWith(var10.data.getSegmentController())) {
                                    var10.data.getSegmentController().sendHitConfirmToDamager(this.explosion.from, false);
                                }

                                var1.sentDamage.add(var10.data.getSegmentController().getId());
                            }

                            var8 += var14;
                        }
                    } else {
                        var6.clear();
                        var6.add((byte)0);
                    }

                    var10.data = null;
                }
            }
        } catch (SegmentDataWriteException var31) {
            var31.printStackTrace();
            throw new RuntimeException("SegmentData should already be normal version", var31);
        } finally {
            if (var3 != null) {
                var3.rwl.writeLock().unlock();
            }

        }

        Iterator var37 = var1.hitSegments.iterator();

        while(true) {
            while(var37.hasNext()) {
                Segment var38;
                ((RemoteSegment)(var38 = (Segment)var37.next())).setLastChanged(var4);
                if (var38.isEmpty() && var38.getSegmentData() != null) {
                    ((RemoteSegment)var38).getSegmentData().getSegmentController().getSegmentProvider().addToFreeSegmentDataFast(var38.getSegmentData());
                } else if (var38.getSegmentData() != null) {
                    var38.getSegmentController().getSegmentProvider().enqueueAABBChange(var38);
                }
            }

            var37 = var1.shieldMap.keySet().iterator();

            while(true) {
                double var17;
                ExplosionCubeConvexBlockCallback var36;
                ShieldAddOn var47;
                do {
                    long var19;
                    ShieldLocalAddOn var33;
                    ShieldLocal var34;
                    double var46;
                    do {
                        while(true) {
                            int var39;
                            double var53;
                            do {
                                do {
                                    do {
                                        do {
                                            do {
                                                if (!var37.hasNext()) {
                                                    var37 = var1.hitPosMap.keySet().iterator();

                                                    while(true) {
                                                        ExplosionCubeConvexBlockCallback var48;
                                                        Sendable var51;
                                                        do {
                                                            do {
                                                                do {
                                                                    do {
                                                                        do {
                                                                            if (!var37.hasNext()) {
                                                                                ObjectIterator var40 = var1.hitSendSegments.entrySet().iterator();

                                                                                while(var40.hasNext()) {
                                                                                    Entry var41;
                                                                                    SendableSegmentController var49 = (SendableSegmentController)(var41 = (Entry)var40.next()).getKey();
                                                                                    if (var8 > 0 && var49 instanceof TransientSegmentController) {
                                                                                        ((TransientSegmentController)var49).setTouched(true, true);
                                                                                    }

                                                                                    if (var8 > 0) {
                                                                                        ((EditableSendableSegmentController)var49).onDamageServerRootObject((float)var8, this.explosion.from);
                                                                                    }

                                                                                    if (var49 instanceof ManagedSegmentController && ((ManagedSegmentController)var49).getManagerContainer() instanceof ShieldContainerInterface) {
                                                                                        ((ShieldContainerInterface)((ManagedSegmentController)var49).getManagerContainer()).getShieldAddOn().getShields();
                                                                                    }

                                                                                    var49.getSegmentBuffer().restructBB();
                                                                                    BlockBulkSerialization var52 = new BlockBulkSerialization();
                                                                                    Long2ObjectOpenHashMap var55 = (Long2ObjectOpenHashMap)var41.getValue();
                                                                                    var52.buffer = var55;
                                                                                    var49.sendBlockBulkMod(new RemoteBlockBulk(var52, true));
                                                                                    var40.remove();
                                                                                }

                                                                                if (this.explosion.afterExplosionHook != null) {
                                                                                    this.explosion.afterExplosionHook.onExplosionDone();
                                                                                }
                                                                                return;
                                                                            }

                                                                            var39 = (Integer)var37.next();
                                                                        } while(!var1.appliedDamageMap.containsKey(var39));
                                                                    } while(!var1.hitPosMap.containsKey(var39));

                                                                    var1.hitPosMap.get(var39);
                                                                    var48 = (ExplosionCubeConvexBlockCallback)var1.hitPosMap.get(var39);
                                                                } while((var51 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var39)) == null);
                                                            } while(!(var51 instanceof SegmentController));
                                                        } while(var1.appliedDamageMap.get(var39) <= 0.0F);

                                                        SegmentController var54 = (SegmentController)var51;
                                                        boolean var56 = this.explosion.from != null && this.explosion.from instanceof SegmentController && ((SegmentController)this.explosion.from).railController.isInAnyRailRelationWith(var54);
                                                        var54.getNetworkObject().hits.add(new RemoteVector4f(new Vector4f(var48.boxTransform.origin.x, var48.boxTransform.origin.y, var48.boxTransform.origin.z, var56 ? -var1.appliedDamageMap.get(var39) : var1.appliedDamageMap.get(var39)), true));
                                                    }
                                                }

                                                var39 = (Integer)var37.next();
                                                var46 = var1.shieldMap.get(var39);
                                                var53 = var1.shieldMapBef.get(var39);
                                            } while(var46 == var53);

                                            var36 = (ExplosionCubeConvexBlockCallback)var1.shieldHitPosMap.get(var39);
                                        } while((var11 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var39)) == null);
                                    } while(!(var11 instanceof SegmentController));

                                    var47 = ((ShieldContainerInterface)((ManagedSegmentController)(var16 = (SegmentController)var11)).getManagerContainer()).getShieldAddOn();
                                    var17 = Math.max(0.0D, var53 - var46);
                                } while(var46 == var53);
                            } while(var36 == null);

                            if (var47.isUsingPowerReactors()) {
                                var33 = var47.getShieldLocalAddOn();
                                var19 = var1.shieldLocalMap.get(var39);
                                break;
                            }

                            if (var46 <= 0.0D) {
                                var17 = Math.ceil(var47.getShields());
                            }

                            try {
                                var47.handleShieldHit(this.explosion.from, voidEffectSet, var36.boxTransform.origin, this.explosion.sectorId, this.explosion.damageType, this.explosion.hitType, var17, this.explosion.weaponId);
                            } catch (SectorNotFoundException var27) {
                                var27.printStackTrace();
                                return;
                            }

                            var47.sendShieldUpdate();
                            var47.getShields();
                            if (var36 != null) {
                                if (this.explosion.from == null || !(this.explosion.from instanceof SegmentController) || !((SegmentController)this.explosion.from).railController.isInAnyRailRelationWith(var16)) {
                                    var16.sendHitConfirmToDamager(this.explosion.from, true);
                                }

                                var47.sendShieldHit(var36.boxTransform.origin, (float)((int)var17));
                            }
                        }
                    } while((var34 = (ShieldLocal)var33.getLocalShieldMap().get(var19)) == null);

                    if (var46 <= 0.0D) {
                        var17 = Math.ceil(var34.getShields());
                    }

                    try {
                        var47.handleShieldHit(this.explosion.from, voidEffectSet, var36.boxTransform.origin, this.explosion.sectorId, this.explosion.damageType, this.explosion.hitType, var17, this.explosion.weaponId);
                    } catch (SectorNotFoundException var28) {
                        var28.printStackTrace();
                        return;
                    }

                    System.err.println("LOCAL SHIELDS HIT:::: " + var34);
                    var33.sendShieldUpdate(var34);
                    var34.getShields();
                } while(var36 == null);

                if (this.explosion.from == null || !(this.explosion.from instanceof SegmentController) || !((SegmentController)this.explosion.from).railController.isInAnyRailRelationWith(var16)) {
                    var16.sendHitConfirmToDamager(this.explosion.from, true);
                }

                var47.sendShieldHit(var36.boxTransform.origin, (float)((int)var17));
            }
        }
    }
}
