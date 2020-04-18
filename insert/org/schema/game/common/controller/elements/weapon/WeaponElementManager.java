//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.weapon;

import api.listener.events.CannonShootEvent;
import api.mod.StarLoader;
import api.server.Server;
import api.systems.weapons.Cannon;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import java.io.IOException;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.camera.InShipCamera;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.BlockActivationListenerInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.IntegrityBasedInterface;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.NTReceiveInterface;
import org.schema.game.common.controller.elements.NTSenderInterface;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.controller.elements.UsableCombinableControllableElementManager;
import org.schema.game.common.controller.elements.UsableControllableElementManager;
import org.schema.game.common.controller.elements.WeaponElementManagerInterface;
import org.schema.game.common.controller.elements.WeaponStatisticsData;
import org.schema.game.common.controller.elements.UsableControllableFireingElementManager.ReloadListener;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.combination.WeaponCombiSettings;
import org.schema.game.common.controller.elements.combination.WeaponCombinationAddOn;
import org.schema.game.common.controller.elements.combination.modifier.WeaponUnitModifier;
import org.schema.game.common.controller.elements.config.FloatReactorDualConfigElement;
import org.schema.game.common.controller.elements.effectblock.EffectCollectionManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import org.schema.schine.network.objects.NetworkObject;

public class WeaponElementManager extends UsableCombinableControllableElementManager<WeaponUnit, WeaponCollectionManager, WeaponElementManager, WeaponCombiSettings> implements BlockActivationListenerInterface, IntegrityBasedInterface, NTReceiveInterface, NTSenderInterface, WeaponElementManagerInterface {
    static final double[] weightedLookupTable = new double[300];
    @ConfigurationElement(
            name = "AcidFormulaDefault"
    )
    public static int ACID_FORMULA_DEFAULT = 0;
    @ConfigurationElement(
            name = "Damage"
    )
    public static FloatReactorDualConfigElement BASE_DAMAGE = new FloatReactorDualConfigElement();
    @ConfigurationElement(
            name = "Distance"
    )
    public static float BASE_DISTANCE = 1000.0F;
    @ConfigurationElement(
            name = "Speed"
    )
    public static float BASE_SPEED = 10.0F;
    @ConfigurationElement(
            name = "ReloadMs"
    )
    public static float BASE_RELOAD = 1000.0F;
    @ConfigurationElement(
            name = "ImpactForce"
    )
    public static float IMPACT_FORCE = 0.01F;
    @ConfigurationElement(
            name = "Recoil"
    )
    public static float RECOIL = 0.1F;
    @ConfigurationElement(
            name = "CursorRecoilX"
    )
    public static float CURSOR_RECOIL_X = 1.0E-4F;
    @ConfigurationElement(
            name = "CursorRecoilMinX"
    )
    public static float CURSOR_RECOIL_MIN_X = 0.001F;
    @ConfigurationElement(
            name = "CursorRecoilMaxX"
    )
    public static float CURSOR_RECOIL_MAX_X = 0.1F;
    @ConfigurationElement(
            name = "CursorRecoilDirX"
    )
    public static float CURSOR_RECOIL_DIR_X = 0.0F;
    @ConfigurationElement(
            name = "CursorRecoilY"
    )
    public static float CURSOR_RECOIL_Y = 1.0E-4F;
    @ConfigurationElement(
            name = "CursorRecoilMinY"
    )
    public static float CURSOR_RECOIL_MIN_Y = 0.001F;
    @ConfigurationElement(
            name = "CursorRecoilMaxY"
    )
    public static float CURSOR_RECOIL_MAX_Y = 0.1F;
    @ConfigurationElement(
            name = "CursorRecoilDirY"
    )
    public static float CURSOR_RECOIL_DIR_Y = 0.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedIn"
    )
    public static float CURSOR_RECOIL_IN = 1.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedInAddMod"
    )
    public static float CURSOR_RECOIL_IN_ADD = 1.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedInPowMult"
    )
    public static float CURSOR_RECOIL_IN_POW_MULT = 1.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedOut"
    )
    public static float CURSOR_RECOIL_OUT = 5.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedOutAddMod"
    )
    public static float CURSOR_RECOIL_OUT_ADD = 1.0F;
    @ConfigurationElement(
            name = "CursorRecoilSpeedOutPowMult"
    )
    public static float CURSOR_RECOIL_OUT_POW_MULT = 1.0F;
    @ConfigurationElement(
            name = "PowerConsumption"
    )
    public static float BASE_POWER_CONSUMPTION = 10.0F;
    @ConfigurationElement(
            name = "ReactorPowerConsumptionResting"
    )
    public static float REACTOR_POWER_CONSUMPTION_RESTING = 10.0F;
    @ConfigurationElement(
            name = "ReactorPowerConsumptionCharging"
    )
    public static float REACTOR_POWER_CONSUMPTION_CHARGING = 10.0F;
    @ConfigurationElement(
            name = "AdditionalPowerConsumptionPerUnitMult"
    )
    public static float ADDITIONAL_POWER_CONSUMPTION_PER_UNIT_MULT = 0.1F;
    public static boolean debug = false;
    @ConfigurationElement(
            name = "EffectConfiguration"
    )
    public static InterEffectSet basicEffectConfiguration = new InterEffectSet();
    @ConfigurationElement(
            name = "ProjectileWidth"
    )
    public static float PROJECTILE_WIDTH_MULT = 1.0F;
    @ConfigurationElement(
            name = "BasicPenetrationDepth"
    )
    public static int PROJECTILE_PENETRATION_DEPTH_BASIC = 1;
    @ConfigurationElement(
            name = "PenetrationDepthExp"
    )
    public static float PROJECTILE_PENETRATION_DEPTH_EXP = 0.5F;
    @ConfigurationElement(
            name = "PenetrationDepthExpMult"
    )
    public static float PROJECTILE_PENETRATION_DEPTH_EXP_MULT = 0.5F;
    @ConfigurationElement(
            name = "AcidDamageMaxPropagation"
    )
    public static int ACID_DAMAGE_MAX_PROPAGATION = 50;
    @ConfigurationElement(
            name = "AcidDamageFormulaConeStartWideWeight"
    )
    public static float ACID_DAMAGE_FORMULA_CONE_START_WIDE_WEIGHT = 1.8F;
    @ConfigurationElement(
            name = "AcidDamageFormulaConeEndWideWeight"
    )
    public static float ACID_DAMAGE_FORMULA_CONE_END_WIDE_WEIGHT = 0.2F;
    @ConfigurationElement(
            name = "AcidDamageMinOverPenModifier"
    )
    public static float ACID_DAMAGE_MIN_OVER_PEN_MOD = 1.0F;
    @ConfigurationElement(
            name = "AcidDamageMaxOverPenModifier"
    )
    public static float ACID_DAMAGE_MAX_OVER_PEN_MOD = 10.0F;
    @ConfigurationElement(
            name = "AcidDamageMinOverArmorModifier"
    )
    public static float ACID_DAMAGE_MIN_OVER_ARMOR_MOD = 1.0F;
    @ConfigurationElement(
            name = "AcidDamageMaxOverArmorModifier"
    )
    public static float ACID_DAMAGE_MAX_OVER_ARMOR_MOD = 3.0F;
    @ConfigurationElement(
            name = "AcidDamageOverArmorBaseReference"
    )
    public static float ACID_DAMAGE_OVER_ARMOR_BASE = 250.0F;
    @ConfigurationElement(
            name = "DamageChargeMax"
    )
    public static float DAMAGE_CHARGE_MAX = 0.05F;
    @ConfigurationElement(
            name = "DamageChargeSpeed"
    )
    public static float DAMAGE_CHARGE_SPEED = 0.05F;
    @ConfigurationElement(
            name = "PossibleZoom"
    )
    public static float POSSIBLE_ZOOM = 0.0F;
    @ConfigurationElement(
            name = "Aimable"
    )
    public static int AIMABLE = 1;
    private final WeaponStatisticsData tmpOutput = new WeaponStatisticsData();
    private WeaponCombinationAddOn addOn = new WeaponCombinationAddOn(this, (GameStateInterface)this.getState());
    private final ShootContainer shootContainer = new ShootContainer();
    private final WeaponCombiSettings combiSettings = new WeaponCombiSettings();
    private static GUITextOverlay chargesText;
    public static final Vector4f chargeColor = new Vector4f(0.8F, 0.5F, 0.3F, 0.4F);
    private final WeaponElementManager.DrawReloadListener drawReloadListener = new WeaponElementManager.DrawReloadListener();

    public WeaponElementManager(SegmentController var1) {
        super((short)6, (short)16, var1);
    }

    public WeaponCombiSettings getCombiSettings() {
        return this.combiSettings;
    }

    void doShot(WeaponUnit var1, WeaponCollectionManager var2, ShootContainer var3, PlayerState var4, Timer var5) {
        ManagerModuleCollection var6 = null;

        var2.setEffectTotal(0);

        short var7;
        if (var2.getEffectConnectedElement() != -9223372036854775808L) {
            var7 = (short)ElementCollection.getType(var2.getEffectConnectedElement());
            var6 = this.getManagerContainer().getModulesControllerMap().get(var7);
            ControlBlockElementCollectionManager var9;
            if ((var9 = CombinationAddOn.getEffect(var2.getEffectConnectedElement(), var6, this.getSegmentController())) != null) {
                var2.setEffectTotal(var9.getTotalSize());
            }
        }

        if (var2.getSlaveConnectedElement() != -9223372036854775808L) {
            var7 = (short)ElementCollection.getType(var2.getSlaveConnectedElement());
            ManagerModuleCollection var11 = this.getManagerContainer().getModulesControllerMap().get(var7);
            ShootingRespose var10 = this.handleAddOn(this, var2, var1, var11, var6, var3, (SimpleTransformableSendableObject)null, var4, var5, -1.0F);
            this.handleResponse(var10, var1, var3.weapontOutputWorldPos);
        } else if (var1.canUse(var5.currentTime, false)) {
            long var8 = var2.getUsableId();
            if (!this.isUsingPowerReactors() && !this.consumePower(var1.getPowerConsumption())) {
                this.handleResponse(ShootingRespose.NO_POWER, var1, var3.weapontOutputWorldPos);
            } else {

                //INSERTED CODE @249
                CannonShootEvent event = new CannonShootEvent(var1);
                StarLoader.fireEvent(CannonShootEvent.class, event);
                if(event.isCanceled()){
                    return;
                }
                ///

                var1.setStandardShotReloading();
                this.getParticleController().addProjectile(this.getSegmentController(),
                        var3.weapontOutputWorldPos, var3.shootingDirTemp, var1.getDamage(), var1.getDistance(), var2.getAcidFormula().ordinal(),
                        var1.getProjectileWidth(), var1.getPenetrationDepth(var1.getDamage()), var1.getImpactForce(), var8, event.getColor());
                this.handleRecoil(var2, var1, var3.weapontOutputWorldPos, var3.shootingDirTemp, var1.getRecoil(), var1.getDamage());
                var2.damageProduced += var1.getDamage();
                this.handleResponse(ShootingRespose.FIRED, var1, var3.weapontOutputWorldPos);
            }
        } else {
            this.handleResponse(ShootingRespose.RELOADING, var1, var3.weapontOutputWorldPos);
        }
    }

    public void handleRecoil(WeaponCollectionManager var1, WeaponUnit var2, Vector3f var3, Vector3f var4, float var5, float var6) {
        if (this.getSegmentController().railController.getRoot().getPhysicsDataContainer().getObject() instanceof RigidBody) {
            this.getSegmentController().railController.getRoot().getPhysicsDataContainer().getObject();
            if (var5 * var6 == 0.0F) {
                return;
            }

            Vector3f var7;
            (var7 = new Vector3f(var4)).negate();
            this.getSegmentController().railController.getRoot().hitWithPhysicalRecoil(var3, var7, var5 * var6, true);
        }

    }

    public void handleCursorRecoil(WeaponCollectionManager var1, float var2, WeaponCombiSettings var3) {
        if (this.getSegmentController().isClientOwnObject() && Controller.getCamera() instanceof InShipCamera) {
            ((InShipCamera)Controller.getCamera()).addRecoil(Math.min(var3.cursorRecoilMaxX, Math.max(var3.cursorRecoilMinX, var2 * var3.cursorRecoilX)), Math.min(var3.cursorRecoilMaxY, Math.max(var3.cursorRecoilMinY, var2 * var3.cursorRecoilY)), var3.cursorRecoilDirX, var3.cursorRecoilDirY, CURSOR_RECOIL_IN, CURSOR_RECOIL_IN_ADD, CURSOR_RECOIL_IN_POW_MULT, CURSOR_RECOIL_OUT, CURSOR_RECOIL_OUT_ADD, CURSOR_RECOIL_OUT_POW_MULT);
        }

    }

    public int onActivate(SegmentPiece var1, boolean var2, boolean var3) {
        long var4 = var1.getAbsoluteIndex();

        for(int var8 = 0; var8 < this.getCollectionManagers().size(); ++var8) {
            Iterator var6 = ((WeaponCollectionManager)this.getCollectionManagers().get(var8)).getElementCollections().iterator();

            while(var6.hasNext()) {
                WeaponUnit var7;
                if ((var7 = (WeaponUnit)var6.next()).contains(var4)) {
                    var7.setMainPiece(var1, var3);
                    if (var3) {
                        return 1;
                    }

                    return 0;
                }
            }
        }

        if (var3) {
            return 1;
        } else {
            return 0;
        }
    }

    public void updateActivationTypes(ShortOpenHashSet var1) {
        var1.add((short)16);
    }

    public void updateFromNT(NetworkObject var1) {
    }

    public void updateToFullNT(NetworkObject var1) {
    }

    public CombinationAddOn<WeaponUnit, WeaponCollectionManager, WeaponElementManager, WeaponCombiSettings> getAddOn() {
        return this.addOn;
    }

    public ControllerManagerGUI getGUIUnitValues(WeaponUnit var1, WeaponCollectionManager var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4) {
        this.getStatistics(var1, var2, var3, var4, this.tmpOutput);
        return ControllerManagerGUI.create((GameClientState)this.getState(), Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_3, var1, new GUIKeyValueEntry[]{new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_4, StringTools.formatPointZero(this.tmpOutput.damage / this.tmpOutput.split)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_15, var1.getPenetrationDepth(this.tmpOutput.damage)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_24, StringTools.formatPointZero(this.tmpOutput.speed)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_21, StringTools.formatPointZero(this.tmpOutput.distance)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_22, StringTools.formatPointZero(this.tmpOutput.split)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_23, StringTools.formatPointZero(this.tmpOutput.reload)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_5, var1.getPowerConsumedPerSecondResting()), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_6, var1.getPowerConsumedPerSecondCharging()), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_25, StringTools.formatPointZero(this.tmpOutput.effectRatio))});
    }

    protected String getTag() {
        return "cannon";
    }

    public WeaponCollectionManager getNewCollectionManager(SegmentPiece var1, Class<WeaponCollectionManager> var2) {
        return new WeaponCollectionManager(var1, this.getSegmentController(), this);
    }

    public String getManagerName() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_20;
    }

    protected void playSound(WeaponUnit var1, Transform var2) {
        float var3 = Math.max(0.0F, Math.min(1.0F, (float)var1.size() / 100.0F));
        ((GameClientController)this.getState().getController()).queueTransformableAudio("0022_spaceship user - laser gun single fire small", var2, 1.0F * (1.0F - var3));
        ((GameClientController)this.getState().getController()).queueTransformableAudio("0022_spaceship user - laser gun single fire medium", var2, var3, 100.0F);
    }

    public void drawReloads(Vector3i var1, Vector3i var2, long var3) {
        this.handleReload(var1, var2, var3, this.drawReloadListener);
    }

    public void handle(ControllerStateInterface var1, Timer var2) {
        System.currentTimeMillis();
        if (!var1.isFlightControllerActive()) {
            if (debug) {
                System.err.println("NOT ACTIVE");
            }

        } else if (this.getCollectionManagers().isEmpty()) {
            if (debug) {
                System.err.println("NO WEAPONS");
            }

        } else {
            try {
                if (!this.convertDeligateControls(var1, this.shootContainer.controlledFromOrig, this.shootContainer.controlledFrom)) {
                    if (debug) {
                        System.err.println("NO SLOT");
                    }

                    return;
                }
            } catch (IOException var7) {
                var7.printStackTrace();
                return;
            }

            this.getPowerManager().sendNoPowerHitEffectIfNeeded();
            if (debug) {
                System.err.println("FIREING CONTROLLERS: " + this.getState() + ", " + this.getCollectionManagers().size() + " FROM: " + this.shootContainer.controlledFrom);
            }

            for(int var3 = 0; var3 < this.getCollectionManagers().size(); ++var3) {
                WeaponCollectionManager var4 = (WeaponCollectionManager)this.getCollectionManagers().get(var3);
                boolean var5 = var1.isSelected(var4.getControllerElement(), this.shootContainer.controlledFrom);
                boolean var6 = var1.isAISelected(var4.getControllerElement(), this.shootContainer.controlledFrom, var3, this.getCollectionManagers().size(), var4);
                if (var5 && var6) {
                    var5 = this.shootContainer.controlledFromOrig.equals(this.shootContainer.controlledFrom) | this.getControlElementMap().isControlling(this.shootContainer.controlledFromOrig, var4.getControllerPos(), this.controllerId);
                    if (debug) {
                        System.err.println("Controlling " + var5 + " " + this.getState());
                    }

                    if (var5 && var4.allowedOnServerLimit()) {
                        if (this.shootContainer.controlledFromOrig.equals(Ship.core)) {
                            var1.getControlledFrom(this.shootContainer.controlledFromOrig);
                        }

                        if (debug) {
                            System.err.println("Controlling " + var5 + " " + this.getState() + ": " + var4.getElementCollections().size());
                        }

                        var4.handleControlShot(var1, var2);
                    }
                }
            }

            if (this.getCollectionManagers().isEmpty() && this.clientIsOwnShip()) {
                ((GameClientState)this.getState()).getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_WEAPON_WEAPONELEMENTMANAGER_2, 0.0F);
            }

        }
    }

    public boolean isUsingRegisteredActivation() {
        return true;
    }

    public WeaponStatisticsData getStatistics(WeaponUnit var1, WeaponCollectionManager var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4, WeaponStatisticsData var5) {
        if (var4 != null) {
            var2.setEffectTotal(var4.getTotalSize());
        } else {
            var2.setEffectTotal(0);
        }

        var5.damage = var1.getDamage();
        var5.speed = var1.getSpeed();
        var5.distance = var1.getDistance();
        var5.reload = var1.getReloadTimeMs();
        var5.powerConsumption = var1.getPowerConsumption();
        var5.split = 1.0F;
        var5.mode = 1.0F;
        var5.effectRatio = 0.0F;
        if (var3 != null) {
            WeaponUnitModifier var6 = (WeaponUnitModifier)this.getAddOn().getGUI(var2, var1, var3, var4);
            var5.damage = var6.outputDamage;
            var5.speed = var6.outputSpeed;
            var5.distance = var6.outputDistance;
            var5.reload = var6.outputReload;
            var5.powerConsumption = var6.outputPowerConsumption;
        }

        if (var4 != null) {
            var4.getElementManager();
            var5.effectRatio = CombinationAddOn.getRatio(var2, var4);
        }

        return var5;
    }

    public double calculateWeaponDamageIndex() {
        double var1 = 0.0D;
        Iterator var3 = this.getCollectionManagers().iterator();

        while(var3.hasNext()) {
            WeaponCollectionManager var4;
            ControlBlockElementCollectionManager var5 = (var4 = (WeaponCollectionManager)var3.next()).getSupportCollectionManager();
            EffectCollectionManager var6 = var4.getEffectCollectionManager();

            for(Iterator var7 = var4.getElementCollections().iterator(); var7.hasNext(); var1 += (double)this.tmpOutput.damage / ((double)this.tmpOutput.reload / 1000.0D)) {
                WeaponUnit var8 = (WeaponUnit)var7.next();
                this.getStatistics(var8, var4, var5, var6, this.tmpOutput);
            }
        }

        return var1;
    }

    public double calculateWeaponRangeIndex() {
        double var1 = 0.0D;
        double var3 = 0.0D;
        Iterator var5 = this.getCollectionManagers().iterator();

        while(var5.hasNext()) {
            WeaponCollectionManager var6;
            ControlBlockElementCollectionManager var7 = (var6 = (WeaponCollectionManager)var5.next()).getSupportCollectionManager();
            EffectCollectionManager var8 = var6.getEffectCollectionManager();

            for(Iterator var9 = var6.getElementCollections().iterator(); var9.hasNext(); ++var3) {
                WeaponUnit var10 = (WeaponUnit)var9.next();
                this.getStatistics(var10, var6, var7, var8, this.tmpOutput);
                var1 += (double)this.tmpOutput.distance;
            }
        }

        if (var3 > 0.0D) {
            return var1 / var3;
        } else {
            return 0.0D;
        }
    }

    public double calculateWeaponHitPropabilityIndex() {
        double var1 = 0.0D;
        Iterator var3 = this.getCollectionManagers().iterator();

        while(var3.hasNext()) {
            WeaponCollectionManager var4;
            ControlBlockElementCollectionManager var5 = (var4 = (WeaponCollectionManager)var3.next()).getSupportCollectionManager();
            EffectCollectionManager var6 = var4.getEffectCollectionManager();

            for(Iterator var7 = var4.getElementCollections().iterator(); var7.hasNext(); var1 += (double)(BASE_SPEED * this.tmpOutput.split) / ((double)this.tmpOutput.reload / 1000.0D)) {
                WeaponUnit var8 = (WeaponUnit)var7.next();
                this.getStatistics(var8, var4, var5, var6, this.tmpOutput);
            }
        }

        return var1;
    }

    public double calculateWeaponSpecialIndex() {
        Iterator var1 = this.getCollectionManagers().iterator();

        while(var1.hasNext()) {
            WeaponCollectionManager var2;
            ControlBlockElementCollectionManager var3 = (var2 = (WeaponCollectionManager)var1.next()).getSupportCollectionManager();
            EffectCollectionManager var4 = var2.getEffectCollectionManager();
            Iterator var5 = var2.getElementCollections().iterator();

            while(var5.hasNext()) {
                WeaponUnit var6 = (WeaponUnit)var5.next();
                this.getStatistics(var6, var2, var3, var4, this.tmpOutput);
            }
        }

        return 0.0D;
    }

    public double calculateWeaponPowerConsumptionPerSecondIndex() {
        double var1 = 0.0D;
        Iterator var3 = this.getCollectionManagers().iterator();

        while(var3.hasNext()) {
            WeaponCollectionManager var4;
            ControlBlockElementCollectionManager var5 = (var4 = (WeaponCollectionManager)var3.next()).getSupportCollectionManager();
            EffectCollectionManager var6 = var4.getEffectCollectionManager();

            WeaponUnit var8;
            for(Iterator var7 = var4.getElementCollections().iterator(); var7.hasNext(); var1 += (double)var8.getPowerConsumption()) {
                var8 = (WeaponUnit)var7.next();
                this.getStatistics(var8, var4, var5, var6, this.tmpOutput);
            }
        }

        return var1;
    }

    public class DrawReloadListener implements ReloadListener {
        public DrawReloadListener() {
        }

        public String onDischarged(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6) {
            UsableControllableElementManager.drawReload(var1, var2, var3, var4, var5, var6);
            return null;
        }

        public String onReload(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6) {
            UsableControllableElementManager.drawReload(var1, var2, var3, var4, var5, var6);
            return null;
        }

        public String onFull(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6, long var7) {
            return null;
        }

        public void drawForElementCollectionManager(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, long var5) {
            WeaponCollectionManager var7;
            WeaponCombiSettings var8;
            if ((var7 = (WeaponCollectionManager)WeaponElementManager.this.getCollectionManagersMap().get(var5)) != null && (var8 = var7.getWeaponChargeParams()).damageChargeMax > 0.0F && var7.damageCharge > 0.0F) {
                if (WeaponElementManager.chargesText == null) {
                    WeaponElementManager.chargesText = new GUITextOverlay(10, 10, FontSize.MEDIUM.getFont(), (InputState)WeaponElementManager.this.getState());
                    WeaponElementManager.chargesText.onInit();
                }

                float var6 = Math.min(var7.damageCharge / var8.damageChargeMax, 0.99999F);
                UsableControllableElementManager.drawReload(var1, var2, var3, WeaponElementManager.chargeColor, false, var6, true, var7.damageCharge, (int)var8.damageChargeMax, -1L, WeaponElementManager.chargesText);
            }

        }
    }
}
