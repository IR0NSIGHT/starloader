//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.damage.projectile;

import api.listener.events.EntityDamageEvent;
import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import org.schema.common.FastMath;
import org.schema.common.util.linAlg.Vector3b;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.effects.ExplosionDrawer;
import org.schema.game.common.controller.EditableSendableSegmentController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.TransientSegmentController;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitReceiverType;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.acid.AcidDamageFormula;
import org.schema.game.common.controller.damage.acid.AcidSetting;
import org.schema.game.common.controller.damage.acid.AcidDamageFormula.AcidFormulaType;
import org.schema.game.common.controller.damage.effects.InterEffectHandler;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.damage.effects.MetaWeaponEffectInterface;
import org.schema.game.common.controller.damage.projectile.ProjectileController.ProjectileHandleState;
import org.schema.game.common.controller.elements.ArmorDamageCalcStyle;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.cargo.CargoCollectionManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.physics.CubeRayCastResult;
import org.schema.game.common.data.physics.InnerSegmentIterator;
import org.schema.game.common.data.physics.ModifiedDynamicsWorld;
import org.schema.game.common.data.physics.RayTraceGridTraverser;
import org.schema.game.common.data.physics.RigidBodyExt;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.world.SectorNotFoundException;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;

public class ProjectileHandlerSegmentController extends ProjectileHandler {
    private final SegmentPiece pTmp = new SegmentPiece();
    private final Vector3f hTmp = new Vector3f();
    private final Vector3f dirTmp = new Vector3f();
    private ProjectileHandlerSegmentController.ShotHandler shotHandler = new ProjectileHandlerSegmentController.ShotHandler();
    private CubeRayCastResult rayCallbackTraverse = new CubeRayCastResult(new Vector3f(), new Vector3f(), (Object)null, new SegmentController[0]) {
        public InnerSegmentIterator newInnerSegmentIterator() {
            return ProjectileHandlerSegmentController.this.new ProjectileTraverseHandler();
        }
    };
    private Vector3f tmpDir = new Vector3f();
    private Vector3f tmpWorldPos = new Vector3f();

    public ProjectileHandlerSegmentController() {
    }

    private boolean processRawHitUnshielded(Segment var1, int var2, short var3, Vector3b var4, Vector3f var5, Transform var6) {
        ElementInformation var8 = ElementKeyMap.getInfoFast(var3);
        this.shotHandler.positionsHit.add(var1.getAbsoluteIndex(var2));
        this.shotHandler.typesHit.add(var8);
        this.shotHandler.segmentsHit.add(var1);
        this.shotHandler.segmentsHitSet.add(var1);
        this.shotHandler.infoIndexHit.add(var2);
        boolean var7 = true;
        if (!this.isAccumulateShot(var8)) {
            var7 = this.processHitsUnshielded(this.shotHandler);
        }

        return var7;
    }

    private boolean processHitsUnshielded(ProjectileHandlerSegmentController.ShotHandler var1) {
        int var2 = var1.positionsHit.size();
        boolean var3 = false;
        if (var2 > 1) {
            HitReceiverType var4 = HitReceiverType.ARMOR;
            InterEffectSet var5 = var1.hitSegController.getEffectContainer().get(var4);

            assert var1.damager != null;

            assert var1.damageDealerType != null;

            InterEffectSet var6;
            float var8;
            if ((var6 = var1.damager.getAttackEffectSet(var1.weaponId, var1.damageDealerType)) == null) {
                System.err.println(var1.hitSegController.getState() + " WARNING: hit effect set on " + var1.hitSegController + " by " + var1.damager + " is null for weapon " + var1.weaponId);
                var8 = var1.dmg;
            } else {
                var8 = InterEffectHandler.handleEffects(var1.dmg, var6, var5, var1.hitType, var1.damageDealerType, var4, (short)-1);
            }

            short var14 = 0;
            int var16 = 0;
            float var18 = 0.0F;
            float var7 = 0.0F;

            for(int var9 = 0; var9 < var2; ++var9) {
                ElementInformation var10 = (ElementInformation)var1.typesHit.get(var9);
                Segment var11 = (Segment)var1.segmentsHit.get(var9);
                int var12 = var1.infoIndexHit.get(var9);
                short var13 = var10.id;
                if (!var10.isArmor()) {
                    break;
                }

                if (var16 == 0) {
                    var14 = var13;
                }

                ++var16;
                var18 += var10.getArmorValue() + var10.getArmorValue() * (float)var16 * VoidElementManager.ARMOR_THICKNESS_BONUS;
                var7 += (float)var11.getSegmentData().getHitpointsByte(var12) * 0.007874016F;
            }

            if (var16 > 0) {
                var1.dmg = var1.hitSegController.getHpController().onHullDamage(var1.damager, var8, var14, var1.damageDealerType);
                var3 = true;
                var7 /= (float)var16;
                var1.totalArmorValue = var18 * var7;
                var1.damageToAcidPercent = 0.0F;
                if (var8 < var1.totalArmorValue * VoidElementManager.ACID_DAMAGE_ARMOR_STOPPED_MARGIN) {
                    var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.STOPPED;
                } else if (var8 < var1.totalArmorValue) {
                    var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.STOPPED_ACID;
                    var1.damageToAcidPercent = (var8 - var1.totalArmorValue * VoidElementManager.ACID_DAMAGE_ARMOR_STOPPED_MARGIN) / (1.0F - VoidElementManager.ACID_DAMAGE_ARMOR_STOPPED_MARGIN);
                    this.doDamageReduction(var1, var16);
                } else if (var1.shotStatus == ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION) {
                    if (var8 > var1.totalArmorValue * VoidElementManager.ARMOR_OVER_PENETRATION_MARGIN_MULTIPLICATOR) {
                        var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION;
                    } else {
                        var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.NORMAL;
                    }
                }

                if (var1.shotStatus == ProjectileHandlerSegmentController.ShotStatus.NORMAL || var1.shotStatus == ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION) {
                    this.doDamageReduction(var1, var16);
                }
            } else if (var1.blockIndex == 0 && var1.shotStatus == ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION) {
                if (var1.dmg > VoidElementManager.NON_ARMOR_OVER_PENETRATION_MARGIN) {
                    var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION;
                } else {
                    var1.shotStatus = ProjectileHandlerSegmentController.ShotStatus.NORMAL;
                }
            }
        }

        for(int var15 = 0; var15 < var2 && var1.dmg > 0.0F; ++var15) {
            ElementInformation var17 = (ElementInformation)var1.typesHit.get(var15);
            Segment var19 = (Segment)var1.segmentsHit.get(var15);
            int var20 = var1.infoIndexHit.get(var15);
            short var21 = var17.id;
            if (this.rayCallbackTraverse.isDebug()) {
                System.err.println("HANDLING DAMAGE ON BLOCK: " + var17 + "; " + new SegmentPiece(var19, var20));
            }

            var1.dmg = this.doDamageOnBlock(var21, var17, var19, var20);
            ++var1.blockIndex;
            if (var3 && var1.shotStatus == ProjectileHandlerSegmentController.ShotStatus.STOPPED) {
                var1.dmg = 0.0F;
                break;
            }
        }

        var1.resetHitBuffer();
        return var1.dmg > 0.0F;
    }

    private void doDamageReduction(ProjectileHandlerSegmentController.ShotHandler var1, int var2) {
        if (VoidElementManager.ARMOR_CALC_STYLE == ArmorDamageCalcStyle.EXPONENTIAL) {
            var1.dmg = Math.max(0.0F, FastMath.pow(var1.dmg, VoidElementManager.CANNON_ARMOR_EXPONENTIAL_INCOMING_EXPONENT) / (FastMath.pow(var1.totalArmorValue, VoidElementManager.CANNON_ARMOR_EXPONENTIAL_ARMOR_VALUE_TOTAL_EXPONENT) + FastMath.pow(var1.dmg, VoidElementManager.CANNON_ARMOR_EXPONENTIAL_INCOMING_DAMAGE_ADDED_EXPONENT)));
        } else {
            var1.dmg = Math.max(0.0F, var1.dmg - VoidElementManager.CANNON_ARMOR_FLAT_DAMAGE_REDUCTION * var1.dmg);
            var1.dmg = Math.max(0.0F, var1.dmg - Math.min(VoidElementManager.CANNON_ARMOR_THICKNESS_DAMAGE_REDUCTION_MAX, VoidElementManager.CANNON_ARMOR_THICKNESS_DAMAGE_REDUCTION * (float)var2 * var1.dmg));
        }
    }

    private float doDamageOnBlock(short var1, ElementInformation var2, Segment var3, int var4) {
        float var14;
        label129: {
            var3.getSegmentData().getOrientation(var4);
            if (this.shotHandler.blockIndex == 0) {
                InterEffectSet var6 = this.shotHandler.defenseEffectSetTmp;
                InterEffectSet var5 = var2.isArmor() ? this.shotHandler.defenseArmor : this.shotHandler.defenseBlock;

                assert this.shotHandler.damager != null;

                assert this.shotHandler.damageDealerType != null;

                var6.setEffect(var5);
                var6.add(var2.effectArmor);
                InterEffectSet var8 = this.shotHandler.damager.getAttackEffectSet(this.shotHandler.weaponId, this.shotHandler.damageDealerType);
                HitReceiverType var9 = var2.isArmor() ? HitReceiverType.ARMOR : HitReceiverType.BLOCK;
                if (var8 != null) {
                    var14 = InterEffectHandler.handleEffects(this.shotHandler.dmg, var8, var6, this.shotHandler.hitType, this.shotHandler.damageDealerType, var9, var1);
                    break label129;
                }

                System.err.println(this.shotHandler.hitSegController.getState() + " WARNING: block hit effect set on " + this.shotHandler.hitSegController + " by " + this.shotHandler.damager + " is null for weapon " + this.shotHandler.weaponId);
            }

            var14 = this.shotHandler.dmg;
        }

        float var15 = var14;
        int var16;
        if ((var16 = Math.round(var14)) == 0) {
            return (float)var16;
        } else {
            if (var3.getSegmentController() instanceof ManagedSegmentController && (var2.isInventory() || var1 == 689)) {
                long var20 = var3.getAbsoluteIndex(var4);
                ManagerContainer var7 = ((ManagedSegmentController)var3.getSegmentController()).getManagerContainer();
                Inventory var10 = null;
                if (var2.isInventory()) {
                    var10 = var7.getInventory(var20);
                } else {
                    Iterator var11 = var7.getCargo().getCollectionManagers().iterator();

                    while(var11.hasNext()) {
                        CargoCollectionManager var12;
                        if ((var12 = (CargoCollectionManager)var11.next()).rawCollection.contains(var20)) {
                            var10 = var7.getInventory(var12.getControllerIndex());
                        }
                    }
                }

                if (var10 != null) {
                    if (!var10.isEmpty()) {
                        var10.getVolume();

                        for(double var25 = 0.0D; var25 < (double)var16 && !var10.isEmpty(); var25 += (double)var16) {
                            if (this.isOnServer()) {
                                var10.spawnVolumeInSpace(var3.getSegmentController(), (double)var16);
                            }

                            var15 = Math.max(0.0F, var15 - (float)var16);
                        }

                        return (float)var16;
                    }
                } else {
                    System.err.println("[SERVER][PROJECTILE] Warning: no connected inventory found when hitting " + var3.getAbsoluteElemPos(var4, new Vector3i()) + " -> " + var2);
                }
            }

            var3.getSegmentData().getHitpointsByte(var4);
            Vector3f var10000;
            EditableSendableSegmentController var21;
            if ((var21 = (EditableSendableSegmentController)this.shotHandler.hitSegController).isExtraAcidDamageOnDecoBlocks() && var2.isDecorative()) {
                if (this.isOnServer()) {
                    SegmentPiece var18 = new SegmentPiece(var3, var4);
                    var21.killBlock(var18);
                    this.shotHandler.acidFormula.getAcidDamageSetting(var1, var16, 40, 40, this.shotHandler.totalArmorValue, this.shotHandler.blockIndex, this.shotHandler.projectileWidth, this.shotHandler.penetrationDepth, this.shotHandler.shotStatus, this.shotHandler.acidSetting);
                    this.shotHandler.totalDmg = (float)this.shotHandler.acidSetting.damage;
                    var21.getAcidDamageManagerServer().inputDamage(var3.getAbsoluteIndex(var4), this.shotHandler.shootingDirRelative, this.shotHandler.acidSetting.damage, this.shotHandler.acidSetting.maxPropagation, this.shotHandler.damager, this.shotHandler.weaponId, true, true);
                } else {
                    ExplosionDrawer var19 = ((GameClientState)this.shotHandler.hitSegController.getState()).getWorldDrawer().getExplosionDrawer();
                    this.pTmp.setByReference(var3, var4);
                    this.pTmp.getAbsolutePos(this.hTmp);
                    var10000 = this.hTmp;
                    var10000.x -= 8.0F;
                    var10000 = this.hTmp;
                    var10000.y -= 8.0F;
                    var10000 = this.hTmp;
                    var10000.z -= 8.0F;
                    var19.addExplosion(this.hTmp);
                }

                return var15;
            } else {
                float var17 = var21.damageElement(var1, var4, var3.getSegmentData(), var16, this.shotHandler.damager, DamageDealerType.PROJECTILE, this.shotHandler.weaponId);
                this.shotHandler.totalDmg = var17;
                short var22 = var3.isEmpty() ? 0 : var3.getSegmentData().getHitpointsByte(var4);
                var15 = Math.max(0.0F, var15 - var17);
                if (var22 > 0) {
                    if (this.isOnServer()) {
                        var21.sendBlockHpByte(var3.getAbsoluteIndex(var4), var22);
                        var21.onBlockDamage(var3.getAbsoluteIndex(var4), var1, var16, this.shotHandler.damageDealerType, this.shotHandler.damager);
                    }

                    var15 = 0.0F;
                } else {
                    if (this.isOnServer()) {
                        var21.sendBlockKill(var3.getAbsoluteIndex(var4));
                        SegmentPiece var23;
                        (var23 = new SegmentPiece(var3, var4)).setType(var1);
                        var21.onBlockKill(var23, this.shotHandler.damager);
                    } else {
                        ExplosionDrawer var24 = ((GameClientState)this.shotHandler.hitSegController.getState()).getWorldDrawer().getExplosionDrawer();
                        this.pTmp.setByReference(var3, var4);
                        this.pTmp.getAbsolutePos(this.hTmp);
                        var10000 = this.hTmp;
                        var10000.x -= 8.0F;
                        var10000 = this.hTmp;
                        var10000.y -= 8.0F;
                        var10000 = this.hTmp;
                        var10000.z -= 8.0F;
                        var24.addExplosion(this.hTmp);
                    }

                    if (var15 > 0.0F && this.shotHandler.shotStatus != ProjectileHandlerSegmentController.ShotStatus.STOPPED) {
                        if (this.shotHandler.shotStatus == ProjectileHandlerSegmentController.ShotStatus.STOPPED_ACID) {
                            var15 *= this.shotHandler.damageToAcidPercent;
                        }

                        this.shotHandler.acidFormula.getAcidDamageSetting(var1, var16, (int)var15, (int)this.shotHandler.initialDamage, this.shotHandler.totalArmorValue, this.shotHandler.blockIndex, this.shotHandler.projectileWidth, this.shotHandler.penetrationDepth, this.shotHandler.shotStatus, this.shotHandler.acidSetting);
                        if (this.isOnServer()) {
                            this.shotHandler.totalDmg = (float)this.shotHandler.acidSetting.damage;
                            var21.getAcidDamageManagerServer().inputDamage(var3.getAbsoluteIndex(var4), this.shotHandler.shootingDirRelative, this.shotHandler.acidSetting.damage, this.shotHandler.acidSetting.maxPropagation, this.shotHandler.damager, this.shotHandler.weaponId, true, false);
                        }

                        var15 = Math.max(var15 - (float)this.shotHandler.acidSetting.damage, 0.0F);
                    }
                }

                return var15;
            }
        }
    }

    private boolean isOnServer() {
        return this.shotHandler.hitSegController.isOnServer();
    }

    private boolean isAccumulateShot(ElementInformation var1) {
        return var1.isArmor();
    }

    public ProjectileHandleState handle(Damager var1, ProjectileController var2, Vector3f var3, Vector3f var4, ProjectileParticleContainer var5, int var6, CubeRayCastResult var7) {
        Segment var8;
        if ((var8 = var7.getSegment()) == null) {
            System.err.println(var1 + " ERROR: SEGMENT NULL: " + var7);
            return ProjectileHandleState.PROJECTILE_NO_HIT;
        } else {
            SegmentController var9;
            if ((var9 = var8.getSegmentController()) instanceof ProjectileHittable) {
                if (!var9.canBeDamagedBy(var1, DamageDealerType.PROJECTILE)) {
                    return ProjectileHandleState.PROJECTILE_NO_HIT_STOP;
                } else {
                    this.shotHandler.hitSegController = var9;
                    this.shotHandler.posBeforeUpdate.set(var3);
                    this.shotHandler.posAfterUpdate.set(var3);
                    this.shotHandler.shootingDir.sub(var4, var3);
                    FastMath.normalizeCarmack(this.shotHandler.shootingDir);
                    this.shotHandler.shootingDirRelative.set(this.shotHandler.shootingDir);
                    var9.getWorldTransformInverse().basis.transform(this.shotHandler.shootingDirRelative);
                    this.dirTmp.sub(var4, var3);
                    FastMath.normalizeCarmack(this.dirTmp);
                    this.dirTmp.scale(400.0F);
                    var4.add(var3, this.dirTmp);
                    this.rayCallbackTraverse.closestHitFraction = 1.0F;
                    this.rayCallbackTraverse.collisionObject = null;
                    this.rayCallbackTraverse.setSegment((Segment)null);
                    this.rayCallbackTraverse.rayFromWorld.set(var3);
                    this.rayCallbackTraverse.rayToWorld.set(var4);
                    this.rayCallbackTraverse.setFilter(new SegmentController[]{var9});
                    this.rayCallbackTraverse.setOwner(var1);
                    this.rayCallbackTraverse.setIgnoereNotPhysical(false);
                    this.rayCallbackTraverse.setIgnoreDebris(false);
                    this.rayCallbackTraverse.setRecordAllBlocks(false);
                    this.rayCallbackTraverse.setZeroHpPhysical(false);
                    this.rayCallbackTraverse.setDamageTest(true);
                    this.rayCallbackTraverse.setCheckStabilizerPaths(true);
                    this.rayCallbackTraverse.setSimpleRayTest(true);
                    ((ModifiedDynamicsWorld)var2.getCurrentPhysics().getDynamicsWorld()).rayTest(var3, var4, this.rayCallbackTraverse);


                    //INSERTED CODE
                    EntityDamageEvent event = new EntityDamageEvent(this.shotHandler.hitSegController, this.shotHandler, this, this.shotHandler.hitType, var1);
                    StarLoader.fireEvent(EntityDamageEvent.class, event);
                    ///

                    if (this.shotHandler.typesHit.size() > 0) {
                        this.processHitsUnshielded(this.shotHandler);
                    }

                    assert this.shotHandler.typesHit.size() == 0 : "not all hits consumed " + this.shotHandler.typesHit.size();

                    if (this.shotHandler.getResult() == ProjectileHandleState.PROJECTILE_HIT_STOP || this.shotHandler.getResult() == ProjectileHandleState.PROJECTILE_HIT_CONTINUE || this.shotHandler.getResult() == ProjectileHandleState.PROJECTILE_HIT_STOP_INVULNERABLE) {
                        this.shotHandler.hitSegController.sendHitConfirmToDamager(var1, false);
                    }

                    if (this.rayCallbackTraverse.hasHit()) {
                        if (this.rayCallbackTraverse.isDebug()) {
                            System.err.println("UPDATE POSAFTERHIT::: -> " + this.rayCallbackTraverse.hitPointWorld);
                        }

                        var4.set(this.rayCallbackTraverse.hitPointWorld);
                    }

                    return this.shotHandler.getResult();
                }
            } else {
                return ProjectileHandleState.PROJECTILE_NO_HIT;
            }
        }
    }

    public ProjectileHandleState handleBefore(Damager var1, ProjectileController var2, Vector3f var3, Vector3f var4, ProjectileParticleContainer var5, int var6, CubeRayCastResult var7) {
        if (var7.getSegment() == null) {
            System.err.println("[PROJECTILE][WARNING] Segment null but collided " + var7.collisionObject);
            return ProjectileHandleState.PROJECTILE_NO_HIT_STOP;
        } else {
            SegmentController var9 = var7.getSegment().getSegmentController();
            this.shotHandler.reset();
            this.shotHandler.dmg = var5.getDamage(var6);
            this.shotHandler.penetrationDepth = var5.getPenetrationDepth(var6);
            this.shotHandler.initialDamage = var5.getDamageInitial(var6);
            this.shotHandler.damager = var1;
            this.shotHandler.hitSegController = var9;
            this.shotHandler.weaponId = var5.getWeaponId(var6);
            this.shotHandler.blockIndex = var5.getBlockHitIndex(var6);
            this.shotHandler.shotStatus = ProjectileHandlerSegmentController.ShotStatus.values()[var5.getShotStatus(var6)];
            this.shotHandler.projectileWidth = var5.getWidth(var6);
            this.shotHandler.projectileId = var5.getId(var6);
            this.shotHandler.wasFirst = this.shotHandler.blockIndex == 0;
            this.shotHandler.defenseArmor.setEffect(this.shotHandler.hitSegController.getEffectContainer().get(HitReceiverType.ARMOR));
            this.shotHandler.defenseArmor.add(VoidElementManager.armorEffectConfiguration);
            this.shotHandler.defenseBlock.setEffect(this.shotHandler.hitSegController.getEffectContainer().get(HitReceiverType.BLOCK));
            this.shotHandler.defenseBlock.add(VoidElementManager.basicEffectConfiguration);
            this.shotHandler.defenseShield.setEffect(this.shotHandler.hitSegController.getEffectContainer().get(HitReceiverType.SHIELD));
            this.shotHandler.defenseShield.add(VoidElementManager.shieldEffectConfiguration);
            this.shotHandler.acidFormula = AcidFormulaType.values()[var5.getAcidFormulaIndex(var6)].formula;
            this.shotHandler.meta = this.shotHandler.damager.getMetaWeaponEffect(this.shotHandler.weaponId, this.shotHandler.damageDealerType);
            if (this.shotHandler.meta != null && var5.getBlockHitIndex(var6) == 0) {
                this.shotHandler.meta.onHit(var9);
            }

            if (!this.isOnServer()) {
                ExplosionDrawer var10000 = ((GameClientState)this.shotHandler.hitSegController.getState()).getWorldDrawer().getExplosionDrawer();
                var4 = null;
                var10000.addExplosion(var7.hitPointWorld, 10.0F, 10, this.shotHandler.dmg);
            }

            if (this.shotHandler.hitSegController.checkAttack(var1, true, true) || !this.isOnServer() && ((GameClientState)this.shotHandler.hitSegController.getState()).getPlayer().isInTutorial()) {
                if (var9 instanceof TransientSegmentController) {
                    ((TransientSegmentController)var9).setTouched(true, true);
                }

                ManagerContainer var11;
                if (var9 instanceof ManagedSegmentController && (var11 = ((ManagedSegmentController)var9).getManagerContainer()) instanceof ShieldContainerInterface) {
                    ShieldContainerInterface var12 = (ShieldContainerInterface)var11;
                    boolean var13 = var9.isUsingLocalShields() && (var12.getShieldAddOn().isUsingLocalShieldsAtLeastOneActive() || var9.railController.isDockedAndExecuted());
                    boolean var15 = var12.getShieldAddOn().getShields() > 0.0D || var9.railController.isDockedAndExecuted();
                    if (var13 || var15) {
                        //this.shotHandler.dmg; ?????????????
                        InterEffectSet var14;
                        (var14 = this.shotHandler.defenseEffectSetTmp).setEffect(this.shotHandler.defenseShield);
                        if (VoidElementManager.individualBlockEffectArmorOnShieldHit) {
                            this.pTmp.setByReference(var7.getSegment(), var7.getCubePos());
                            if (this.pTmp.isValid()) {
                                var14.add(this.pTmp.getInfo().effectArmor);
                            }
                        }

                        try {
                            this.shotHandler.dmg = (float)var12.getShieldAddOn().handleShieldHit(var1, var14, var7.hitPointWorld, var2.getSectorId(), DamageDealerType.PROJECTILE, HitType.WEAPON, (double)this.shotHandler.dmg, this.shotHandler.weaponId);
                        } catch (SectorNotFoundException var8) {
                            var8.printStackTrace();
                            this.shotHandler.dmg = 0.0F;
                        }

                        if (this.shotHandler.dmg <= 0.0F) {
                            var9.sendHitConfirmToDamager(var1, true);
                            return ProjectileHandleState.PROJECTILE_HIT_STOP;
                        } else {
                            return ProjectileHandleState.PROJECTILE_HIT_CONTINUE;
                        }
                    }
                }

                return ProjectileHandleState.PROJECTILE_NO_HIT;
            } else {
                if (!this.isOnServer()) {
                    ExplosionDrawer var10 = ((GameClientState)this.shotHandler.hitSegController.getState()).getWorldDrawer().getExplosionDrawer();
                    this.pTmp.setByReference(var7.getSegment(), var7.getCubePos());
                    this.pTmp.getAbsolutePos(this.hTmp);
                    Vector3f var16 = this.hTmp;
                    var16.x -= 8.0F;
                    var16 = this.hTmp;
                    var16.y -= 8.0F;
                    var16 = this.hTmp;
                    var16.z -= 8.0F;
                    var10.addExplosion(this.hTmp);
                }

                return ProjectileHandleState.PROJECTILE_HIT_STOP_INVULNERABLE;
            }
        }
    }

    public ProjectileHandleState handleAfterIfNotStopped(Damager var1, ProjectileController var2, Vector3f var3, Vector3f var4, ProjectileParticleContainer var5, int var6, CubeRayCastResult var7) {
        var5.setBlockHitIndex(var6, this.shotHandler.blockIndex);
        var5.setShotStatus(var6, this.shotHandler.shotStatus.ordinal());
        return ProjectileHandleState.PROJECTILE_IGNORE;
    }

    public void afterHandleAlways(Damager var1, ProjectileController var2, Vector3f var3, Vector3f var4, ProjectileParticleContainer var5, int var6, CubeRayCastResult var7) {
        if (this.shotHandler.wasFirst && this.shotHandler.blockIndex > 0 && this.shotHandler.hitSegController != null && this.shotHandler.hitSegController.railController.getRoot().getPhysicsDataContainer().getObject() instanceof RigidBodyExt) {
            this.tmpDir.sub(var4, var3);
            if (this.tmpDir.lengthSquared() > 0.0F) {
                this.tmpWorldPos.set(var7.hitPointWorld);
                this.applyRecoil(this.tmpWorldPos, this.tmpDir, this.shotHandler.initialDamage);
            }
        }

    }

    private void applyRecoil(Vector3f var1, Vector3f var2, float var3) {
        this.shotHandler.hitSegController.railController.getRoot();
        this.shotHandler.hitSegController.railController.getRoot().hitWithPhysicalRecoil(var1, var2, var3 * 0.1F, false);
    }

    public static enum ShotStatus {
        OVER_PENETRATION,
        STOPPED,
        STOPPED_ACID,
        NORMAL;

        private ShotStatus() {
        }
    }

    class ProjectileTraverseHandler extends InnerSegmentIterator {
        private ProjectileTraverseHandler() {
        }

        public boolean onOuterSegmentHitTest(Segment var1, boolean var2) {
            if (this.debug) {
                System.err.println("OUTER HIT::: " + var1);
            }

            boolean var3 = true;
            if (!var2 && ProjectileHandlerSegmentController.this.shotHandler.typesHit.size() > 0) {
                var3 = ProjectileHandlerSegmentController.this.processHitsUnshielded(ProjectileHandlerSegmentController.this.shotHandler);
            }

            return var3;
        }

        public boolean handle(int var1, int var2, int var3, RayTraceGridTraverser var4) {
            SegmentController var5 = this.getContextObj();
            int var6 = var1 - this.currentSeg.pos.x + 16;
            int var7 = var2 - this.currentSeg.pos.y + 16;
            int var8 = var3 - this.currentSeg.pos.z + 16;
            if (this.debug) {
                var4.drawDebug(var1 + 16, var2 + 16, var3 + 16, this.tests, var5.getWorldTransform());
            }

            ++this.tests;
            SegmentData var10 = this.currentSeg.getSegmentData();
            if (var6 >= 0 && var6 < 32 && var7 >= 0 && var7 < 32 && var8 >= 0 && var8 < 32) {
                this.v.elemA.set((byte)var6, (byte)var7, (byte)var8);
                this.v.elemPosA.set((float)(this.v.elemA.x - 16), (float)(this.v.elemA.y - 16), (float)(this.v.elemA.z - 16));
                Vector3f var10000 = this.v.elemPosA;
                var10000.x += (float)this.currentSeg.pos.x;
                var10000 = this.v.elemPosA;
                var10000.y += (float)this.currentSeg.pos.y;
                var10000 = this.v.elemPosA;
                var10000.z += (float)this.currentSeg.pos.z;
                this.v.nA.set(this.v.elemPosA);
                this.v.tmpTrans3.set(this.testCubes);
                this.v.tmpTrans3.basis.transform(this.v.nA);
                this.v.tmpTrans3.origin.add(this.v.nA);
                float var12 = Vector3fTools.length(this.fromA.origin, this.v.tmpTrans3.origin) / Vector3fTools.length(this.fromA.origin, this.toA.origin);
                this.rayResult.closestHitFraction = var12;
                this.rayResult.setSegment(var10.getSegment());
                this.rayResult.getCubePos().set(this.v.elemA);
                this.rayResult.hitPointWorld.set(this.v.tmpTrans3.origin);
                this.rayResult.hitNormalWorld.sub(this.fromA.origin, this.toA.origin);
                FastMath.normalizeCarmack(this.rayResult.hitNormalWorld);
                int var9;
                boolean var11;
                short var13;
                if ((var13 = var10.getType(var9 = SegmentData.getInfoIndex((byte)var6, (byte)var7, (byte)var8))) > 0 && ElementInformation.isPhysicalRayTests(var13, var10, var9) && this.isZeroHpPhysical(var10, var9)) {
                    if (this.rayResult.isDebug()) {
                        System.err.println("HIT BLOCK: " + var6 + ", " + var7 + "; " + var8 + "; BLOCK: " + (var1 - 16) + ", " + (var2 - 16) + ", " + (var3 - 16));
                    }

                    this.rayResult.collisionObject = this.collisionObject;
                    var11 = ProjectileHandlerSegmentController.this.processRawHitUnshielded(this.currentSeg, var9, var13, this.v.elemA, this.v.elemPosA, this.testCubes);
                    if (this.rayResult.isDebug()) {
                        System.err.println("HIT BLOCK: " + var6 + ", " + var7 + "; " + var8 + "; BLOCK: " + (var1 - 16) + ", " + (var2 - 16) + ", " + (var3 - 16) + " -CONTINUE: " + var11);
                    }

                    if (!var11) {
                        this.hitSignal = true;
                    }

                    return var11;
                }

                if (ProjectileHandlerSegmentController.this.shotHandler.typesHit.size() > 0) {
                    this.hitSignal = true;
                    var11 = ProjectileHandlerSegmentController.this.processHitsUnshielded(ProjectileHandlerSegmentController.this.shotHandler);
                    if (this.rayResult.isDebug()) {
                        System.err.println("*AIR* HIT BLOCK ACCUMULATED (air block): " + var6 + ", " + var7 + "; " + var8 + "; BLOCK: " + (var1 - 16) + ", " + (var2 - 16) + ", " + (var3 - 16) + "; type: " + var13 + "; continue: " + var11);
                    }

                    return var11;
                }
            }

            return true;
        }
    }

    public class ShotHandler {
        public SegmentController hitSegController;
        public long weaponId;
        public Damager damager;
        public Vector3f posBeforeUpdate = new Vector3f();
        public Vector3f posAfterUpdate = new Vector3f();
        public Vector3f shootingDir = new Vector3f();
        public Vector3f shootingDirRelative = new Vector3f();
        public HitType hitType;
        public DamageDealerType damageDealerType;
        private float dmg;
        private float totalArmorValue;
        private float totalDmg;
        private LongArrayList positionsHit;
        private ObjectArrayList<ElementInformation> typesHit;
        private ObjectArrayList<Segment> segmentsHit;
        private ObjectOpenHashSet<Segment> segmentsHitSet;
        private IntArrayList infoIndexHit;
        public InterEffectSet defenseArmor;
        public InterEffectSet defenseShield;
        public InterEffectSet defenseBlock;
        public MetaWeaponEffectInterface meta;
        public ProjectileHandlerSegmentController.ShotStatus shotStatus;
        public final AcidSetting acidSetting;
        public AcidDamageFormula acidFormula;
        public int blockIndex;
        public int penetrationDepth;
        public float projectileWidth;
        public float initialDamage;
        public InterEffectSet defenseEffectSetTmp;
        public ProjectileHandleState forcedResult;
        public boolean wasFirst;
        public float damageToAcidPercent;
        public int projectileId;

        public ShotHandler() {
            this.hitType = HitType.WEAPON;
            this.damageDealerType = DamageDealerType.PROJECTILE;
            this.positionsHit = new LongArrayList();
            this.typesHit = new ObjectArrayList();
            this.segmentsHit = new ObjectArrayList();
            this.segmentsHitSet = new ObjectOpenHashSet();
            this.infoIndexHit = new IntArrayList();
            this.defenseArmor = new InterEffectSet();
            this.defenseShield = new InterEffectSet();
            this.defenseBlock = new InterEffectSet();
            this.shotStatus = ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION;
            this.acidSetting = new AcidSetting();
            this.defenseEffectSetTmp = new InterEffectSet();
        }

        public void reset() {
            this.meta = null;
            this.weaponId = -9223372036854775808L;
            this.damager = null;
            this.hitSegController = null;
            this.dmg = 0.0F;
            this.totalDmg = 0.0F;
            this.totalArmorValue = 0.0F;
            this.initialDamage = 0.0F;
            this.acidFormula = null;
            this.forcedResult = null;
            this.shotStatus = ProjectileHandlerSegmentController.ShotStatus.OVER_PENETRATION;
            this.forcedResult = null;
            this.resetHitBuffer();
        }

        public void resetHitBuffer() {
            this.positionsHit.clear();
            this.typesHit.clear();
            this.segmentsHit.clear();
            this.segmentsHitSet.clear();
            this.infoIndexHit.clear();
        }

        public ProjectileHandleState getResult() {
            if (this.forcedResult != null) {
                return this.forcedResult;
            } else {
                return this.dmg > 0.0F ? ProjectileHandleState.PROJECTILE_HIT_CONTINUE : ProjectileHandleState.PROJECTILE_HIT_STOP;
            }
        }
    }
}
