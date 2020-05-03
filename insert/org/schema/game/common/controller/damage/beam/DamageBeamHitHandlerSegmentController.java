//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.damage.beam;

import api.DebugFile;
import api.listener.Listener;
import api.listener.events.DamageBeamHitEvent;
import api.listener.events.Event;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import java.util.Collection;
import javax.vecmath.Vector3f;
import org.schema.common.FastMath;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.BeamHandlerContainer;
import org.schema.game.common.controller.EditableSendableSegmentController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.TransientSegmentController;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitReceiverType;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.effects.InterEffectHandler;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.beam.BeamElementManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.world.SectorNotFoundException;
import org.schema.game.common.data.world.Segment;
import org.schema.schine.graphicsengine.core.Timer;

public class DamageBeamHitHandlerSegmentController implements DamageBeamHitHandler {
    private SegmentPiece segmentPiece = new SegmentPiece();
    private final InterEffectSet defenseShield = new InterEffectSet();
    private final InterEffectSet defenseBlock = new InterEffectSet();
    private final InterEffectSet defenseArmor = new InterEffectSet();
    private Damager damager;
    private final DamageDealerType damageDealerType;
    private long weaponId;
    private float dam;
    private HitType hitType;
    private SegmentController hitController;
    private final InterEffectSet defense;

    public DamageBeamHitHandlerSegmentController() {
        this.damageDealerType = DamageDealerType.BEAM;
        this.weaponId = -9223372036854775808L;
        this.defense = new InterEffectSet();
    }

    public void reset() {
        this.segmentPiece.reset();
        this.defenseShield.reset();
        this.defenseBlock.reset();
        this.defenseArmor.reset();
        this.damager = null;
        this.hitType = null;
        this.hitController = null;
        this.weaponId = -9223372036854775808L;
        this.dam = 0.0F;
    }

    public int onBeamDamage(BeamState hittingBeam, int hits, BeamHandlerContainer<?> container, SegmentPiece segmentPiece, Vector3f from, Vector3f to, Timer var7, Collection<Segment> updatedSegments) {
        this.segmentPiece.setByReference(segmentPiece);

        //hitController.getEffectContainer().get(HitReceiverType.SHIELD).getStrength(InterEffectHandler.InterEffectType.EM);
        if (!this.segmentPiece.isValid()) {
            System.err.println(this.segmentPiece.getSegmentController().getState() + " HITTTING INVALID PIECE");
            return 0;
        } else {
            this.hitController = segmentPiece.getSegmentController();
            if (this.hitController instanceof TransientSegmentController) {
                ((TransientSegmentController)this.hitController).setTouched(true, true);
            }

            Starter.modManager.onSegmentControllerHitByBeam(this.hitController);



            this.segmentPiece.getType();
            ElementInformation var11 = this.segmentPiece.getInfo();
            this.damager = hittingBeam.getHandler().getBeamShooter();
            if (!this.hitController.checkAttack(this.damager, true, true)) {
                return 0;
            } else {
                this.defenseShield.setEffect(this.hitController.getEffectContainer().get(HitReceiverType.SHIELD));
                this.defenseShield.add(VoidElementManager.shieldEffectConfiguration);
                if (VoidElementManager.individualBlockEffectArmorOnShieldHit) {
                    this.defenseShield.add(var11.effectArmor);
                }

                this.defenseArmor.setEffect(this.hitController.getEffectContainer().get(HitReceiverType.ARMOR));
                this.defenseArmor.add(VoidElementManager.armorEffectConfiguration);
                this.defenseBlock.setEffect(this.hitController.getEffectContainer().get(HitReceiverType.BLOCK));
                this.defenseBlock.add(VoidElementManager.basicEffectConfiguration);
                this.weaponId = hittingBeam.weaponId;
                float var12 = (float)hits * hittingBeam.getPowerByBeamLength();
                this.dam = var12;
                if (hittingBeam.beamType == 6) {
                    this.dam = FastMath.ceil((float)var11.getMaxHitPointsFull() / (float)var11.getMaxHitPointsByte()) * (float)((int)((float)hits * hittingBeam.getPowerByBeamLength()));
                }

                this.hitType = hittingBeam.hitType;
                this.dam *= this.hitController.getDamageTakenMultiplier(this.damageDealerType);
                if (this.damager != null) {
                    this.dam *= this.damager.getDamageGivenMultiplier();
                }

                boolean var13 = false;
                if (!hittingBeam.ignoreShield && this.hitController instanceof ManagedSegmentController && ((ManagedSegmentController)this.hitController).getManagerContainer() instanceof ShieldContainerInterface) {
                    ShieldContainerInterface var14;
                    ShieldAddOn var15 = (var14 = (ShieldContainerInterface)((ManagedSegmentController)this.hitController).getManagerContainer()).getShieldAddOn();
                    if (this.hitController.isUsingLocalShields()) {
                        if (var15.isUsingLocalShieldsAtLeastOneActive() || this.hitController.railController.isDockedAndExecuted()) {
                            try {
                                float var10000 = this.dam;
                                this.dam = (float)var15.handleShieldHit(this.damager, this.defenseShield, hittingBeam.hitPoint, hittingBeam.hitSectorId, this.damageDealerType, this.hitType, (double)this.dam, this.weaponId);
                            } catch (SectorNotFoundException var10) {
                                var10.printStackTrace();
                                this.dam = 0.0F;
                            }

                            if (this.dam <= 0.0F) {
                                this.hitController.sendHitConfirmToDamager(this.damager, true);
                                return 0;
                            }
                        }
                    } else {
                        if (var15.getShields() > 0.0D || this.hitController.railController.isDockedAndExecuted()) {
                            try {
                                this.dam = (float)var15.handleShieldHit(this.damager, this.defenseShield, hittingBeam.hitPoint, hittingBeam.hitSectorId, this.damageDealerType, this.hitType, (double)this.dam, this.weaponId);
                            } catch (SectorNotFoundException var9) {
                                var9.printStackTrace();
                                this.dam = 0.0F;
                            }

                            if (this.dam <= 0.0F) {
                                this.hitController.sendHitConfirmToDamager(this.damager, true);
                                return 0;
                            }
                        }

                        var13 = var14.getShieldAddOn().getShields() > 0.0D;
                    }
                }

                this.hitController.sendHitConfirmToDamager(this.damager, var13);
                this.dam = (float)hittingBeam.calcPreviousArmorDamageReduction(this.dam);
                this.dam = this.hitController.getHpController().onHullDamage(this.damager, this.dam, this.segmentPiece.getType(), this.damageDealerType);
                if (this.doDamageOnBlock(this.segmentPiece, hittingBeam) && var11.isArmor()) {
                    hittingBeam.getHandler().onArmorBlockKilled(hittingBeam, var11.getArmorValue());
                }

                CollisionObject var16;
                if ((var16 = this.hitController.getPhysicsDataContainer().getObject()) != null) {
                    var16.activate(true);
                }

                //INSERTED CODE @179
                DamageBeamHitEvent event = new DamageBeamHitEvent(this, hitController, hittingBeam, hits, container, segmentPiece, from, to, updatedSegments);
                StarLoader.fireEvent(DamageBeamHitEvent.class, event, this.isOnServer());
                ///

                Starter.modManager.onSegmentControllerDamageTaken(this.hitController);
                return hits;
            }
        }
    }

    private boolean doDamageOnBlock(SegmentPiece var1, BeamState var2) {
        var1.getOrientation();
        short var3 = var1.getType();
        ElementInformation var4;
        HitReceiverType var5 = (var4 = var1.getInfo()).isArmor() ? HitReceiverType.ARMOR : HitReceiverType.BLOCK;
        InterEffectSet var6 = var4.isArmor() ? this.defenseArmor : this.defenseBlock;

        assert this.damager != null;

        assert this.damageDealerType != null;

        InterEffectSet var7 = this.damager.getAttackEffectSet(this.weaponId, this.damageDealerType);
        this.defense.setEffect(var6);
        this.defense.scaleAdd(var4.effectArmor, 1.0F);
        float var10;
        float var12 = var10 = InterEffectHandler.handleEffects(this.dam, var7, this.defense, this.hitType, this.damageDealerType, var5, var3);
        int var11 = Math.round(var10);
        int var13 = var1.getHitpointsFull();
        EditableSendableSegmentController var14;
        float var8 = (var14 = (EditableSendableSegmentController)this.hitController).damageElement(var3, var1.getInfoIndex(), var1.getSegment().getSegmentData(), var11, this.damager, this.damageDealerType, this.weaponId);
        var13 = (int)((float)var13 - var8);
        var12 = Math.max(0.0F, var12 - var8);
        if (var13 > 0) {
            if (this.isOnServer()) {
                var14.sendBlockHpByte(var1.getAbsoluteIndex(), ElementKeyMap.convertToByteHP(var3, var13));
                var14.onBlockDamage(var1.getAbsoluteIndex(), var3, var11, this.damageDealerType, this.damager);
            }
        } else {
            if (this.isOnServer()) {
                var14.sendBlockKill(var1.getAbsoluteIndex());
                var14.onBlockKill(var1, this.damager);
            }

            if (this.isOnServer() && var2.acidDamagePercent > 0.0F) {
                int var9 = (int)(var2.acidDamagePercent * var12);
                var14.getAcidDamageManagerServer().inputDamage(var1.getAbsoluteIndex(), var2.hitNormalRelative, var9, 16, var2.getHandler().getBeamShooter(), var2.weaponId, true, false);
                Math.max(var12 - (float)var9, 0.0F);
            }
        }

        return var13 <= 0;
    }

    private boolean isOnServer() {
        return this.hitController.isOnServer();
    }
}
