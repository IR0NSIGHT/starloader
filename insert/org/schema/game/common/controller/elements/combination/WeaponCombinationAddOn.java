//
// Decompiled by Procyon v0.5.36
//

package org.schema.game.common.controller.elements.combination;

import api.listener.events.CannonProjectileAddEvent;
import api.mod.StarLoader;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.game.common.data.player.PlayerState;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.client.data.GameStateInterface;
import org.schema.common.config.ConfigurationElement;
import org.schema.game.common.controller.elements.combination.modifier.WeaponUnitModifier;
import org.schema.game.common.controller.elements.combination.modifier.MultiConfigModifier;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;

public class WeaponCombinationAddOn extends CombinationAddOn<WeaponUnit, WeaponCollectionManager, WeaponElementManager, WeaponCombiSettings>
{
    @ConfigurationElement(name = "cannon")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponCannonUnitModifier;
    @ConfigurationElement(name = "beam")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponBeamUnitModifier;
    @ConfigurationElement(name = "missile")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponMissileUnitModifier;

    public WeaponCombinationAddOn(final WeaponElementManager weaponElementManager, final GameStateInterface gameStateInterface) {
        super(weaponElementManager, gameStateInterface);
    }

    public ShootingRespose handle(final WeaponUnitModifier mod, final WeaponCollectionManager fireingCollection, final WeaponUnit firingUnit, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager2, final ShootContainer shootContainer, final SimpleTransformableSendableObject simpleTransformableSendableObject) {
        mod.handle(firingUnit, controlBlockElementCollectionManager, CombinationAddOn.getRatio(fireingCollection, controlBlockElementCollectionManager));
        final long weaponId = fireingCollection.getUsableId();
        firingUnit.setShotReloading((long)mod.outputReload);
        final Vector3f dir = new Vector3f(shootContainer.shootingDirTemp);
        if (mod.outputAimable) {
            dir.set((Tuple3f)shootContainer.shootingDirTemp);
        }
        else {
            dir.set((Tuple3f)shootContainer.shootingDirStraightTemp);
        }
        dir.normalize();
        dir.scale(mod.outputSpeed);

        ((WeaponElementManager)this.elementManager).getParticleController()
                .addProjectile(((WeaponElementManager)this.elementManager).getSegmentController(),
                        shootContainer.weapontOutputWorldPos, dir,
                        mod.outputDamage, mod.outputDistance,
                        mod.outputAcidType, mod.outputProjectileWidth,
                        firingUnit.getPenetrationDepth(mod.outputDamage),
                        mod.outputImpactForce, weaponId, fireingCollection.getColor());
        fireingCollection.damageProduced += mod.outputDamage;
        fireingCollection.getElementManager().handleRecoil(fireingCollection, firingUnit, shootContainer.weapontOutputWorldPos, shootContainer.shootingDirTemp, mod.outputRecoil, mod.outputDamage);
        return ShootingRespose.FIRED;
    }

    @Override
    protected String getTag() {
        return "cannon";
    }

    @Override
    public ShootingRespose handleCannonCombi(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final WeaponCollectionManager weaponCollectionManager2, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, final ShootContainer shootContainer, final SimpleTransformableSendableObject simpleTransformableSendableObject, final PlayerState playerState, final Timer timer, final float n) {
        return this.handle(WeaponCombinationAddOn.weaponCannonUnitModifier.get(weaponCollectionManager2), weaponCollectionManager, weaponUnit, weaponCollectionManager2, controlBlockElementCollectionManager, shootContainer, simpleTransformableSendableObject);
    }

    @Override
    public ShootingRespose handleBeamCombi(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DamageBeamCollectionManager damageBeamCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, final ShootContainer shootContainer, final SimpleTransformableSendableObject simpleTransformableSendableObject, final PlayerState playerState, final Timer timer, final float n) {
        return this.handle(WeaponCombinationAddOn.weaponBeamUnitModifier.get(damageBeamCollectionManager), weaponCollectionManager, weaponUnit, damageBeamCollectionManager, controlBlockElementCollectionManager, shootContainer, simpleTransformableSendableObject);
    }

    @Override
    public ShootingRespose handleMissileCombi(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DumbMissileCollectionManager dumbMissileCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, final ShootContainer shootContainer, final SimpleTransformableSendableObject simpleTransformableSendableObject, final PlayerState playerState, final Timer timer, final float n) {
        return this.handle(WeaponCombinationAddOn.weaponMissileUnitModifier.get(dumbMissileCollectionManager), weaponCollectionManager, weaponUnit, dumbMissileCollectionManager, controlBlockElementCollectionManager, shootContainer, simpleTransformableSendableObject);
    }

    @Override
    public WeaponUnitModifier getGUI(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final WeaponCollectionManager weaponCollectionManager2, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        final WeaponUnitModifier weaponUnitModifier;
        (weaponUnitModifier = WeaponCombinationAddOn.weaponCannonUnitModifier.get(weaponCollectionManager2)).handle(weaponUnit, weaponCollectionManager2, CombinationAddOn.getRatio(weaponCollectionManager, weaponCollectionManager2));
        return weaponUnitModifier;
    }

    @Override
    public WeaponUnitModifier getGUI(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DamageBeamCollectionManager damageBeamCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        final WeaponUnitModifier weaponUnitModifier;
        (weaponUnitModifier = WeaponCombinationAddOn.weaponBeamUnitModifier.get(damageBeamCollectionManager)).handle(weaponUnit, damageBeamCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, damageBeamCollectionManager));
        return weaponUnitModifier;
    }

    @Override
    public WeaponUnitModifier getGUI(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DumbMissileCollectionManager dumbMissileCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        final WeaponUnitModifier weaponUnitModifier;
        (weaponUnitModifier = WeaponCombinationAddOn.weaponMissileUnitModifier.get(dumbMissileCollectionManager)).handle(weaponUnit, dumbMissileCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, dumbMissileCollectionManager));
        return weaponUnitModifier;
    }

    @Override
    public double calcCannonCombiPowerConsumption(final double n, final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final WeaponCollectionManager weaponCollectionManager2, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponCannonUnitModifier.get(weaponCollectionManager2).calculatePowerConsumption(n, weaponUnit, weaponCollectionManager2, CombinationAddOn.getRatio(weaponCollectionManager, weaponCollectionManager2));
    }

    @Override
    public double calcBeamCombiPowerConsumption(final double n, final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DamageBeamCollectionManager damageBeamCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponBeamUnitModifier.get(damageBeamCollectionManager).calculatePowerConsumption(n, weaponUnit, damageBeamCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, damageBeamCollectionManager));
    }

    @Override
    public double calcMissileCombiPowerConsumption(final double n, final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DumbMissileCollectionManager dumbMissileCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponMissileUnitModifier.get(dumbMissileCollectionManager).calculatePowerConsumption(n, weaponUnit, dumbMissileCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, dumbMissileCollectionManager));
    }

    @Override
    public double calcCannonCombiReload(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final WeaponCollectionManager weaponCollectionManager2, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponCannonUnitModifier.get(weaponCollectionManager2).calculateReload(weaponUnit, weaponCollectionManager2, CombinationAddOn.getRatio(weaponCollectionManager, weaponCollectionManager2));
    }

    @Override
    public double calcBeamCombiReload(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DamageBeamCollectionManager damageBeamCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponBeamUnitModifier.get(damageBeamCollectionManager).calculateReload(weaponUnit, damageBeamCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, damageBeamCollectionManager));
    }

    @Override
    public double calcMissileCombiReload(final WeaponCollectionManager weaponCollectionManager, final WeaponUnit weaponUnit, final DumbMissileCollectionManager dumbMissileCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return WeaponCombinationAddOn.weaponMissileUnitModifier.get(dumbMissileCollectionManager).calculateReload(weaponUnit, dumbMissileCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, dumbMissileCollectionManager));
    }

    @Override
    public void calcCannonCombiSettings(final WeaponCombiSettings weaponCombiSettings, final WeaponCollectionManager weaponCollectionManager, final WeaponCollectionManager weaponCollectionManager2, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        WeaponCombinationAddOn.weaponCannonUnitModifier.get(weaponCollectionManager2).calcCombiSettings(weaponCombiSettings, weaponCollectionManager, weaponCollectionManager2, CombinationAddOn.getRatio(weaponCollectionManager, weaponCollectionManager2));
    }

    @Override
    public void calcBeamCombiSettings(final WeaponCombiSettings weaponCombiSettings, final WeaponCollectionManager weaponCollectionManager, final DamageBeamCollectionManager damageBeamCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        WeaponCombinationAddOn.weaponBeamUnitModifier.get(damageBeamCollectionManager).calcCombiSettings(weaponCombiSettings, weaponCollectionManager, damageBeamCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, damageBeamCollectionManager));
    }

    @Override
    public void calcMissileCombiPowerSettings(final WeaponCombiSettings weaponCombiSettings, final WeaponCollectionManager weaponCollectionManager, final DumbMissileCollectionManager dumbMissileCollectionManager, final ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        WeaponCombinationAddOn.weaponMissileUnitModifier.get(dumbMissileCollectionManager).calcCombiSettings(weaponCombiSettings, weaponCollectionManager, dumbMissileCollectionManager, CombinationAddOn.getRatio(weaponCollectionManager, dumbMissileCollectionManager));
    }

    static {
        weaponCannonUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {
            @Override
            public final WeaponUnitModifier instance() {
                return new WeaponUnitModifier();
            }
        };
        weaponBeamUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {
            @Override
            public final WeaponUnitModifier instance() {
                return new WeaponUnitModifier();
            }
        };
        weaponMissileUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {
            @Override
            public final WeaponUnitModifier instance() {
                return new WeaponUnitModifier();
            }
        };
    }
}
