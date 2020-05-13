//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example;

import api.entity.Entity;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.UnitCalcStyle;
import org.schema.game.common.controller.elements.UsableControllableSingleElementManager;
import org.schema.game.common.controller.elements.config.DoubleReactorDualConfigElement;
import org.schema.game.common.controller.elements.effectblock.EffectElementManager.OffensiveEffects;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.controller.observer.DrawerObserver;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.blockeffects.BlockEffect;
import org.schema.game.common.data.blockeffects.BlockEffectTypes;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Sector;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;

public class BatteryElementManager extends UsableControllableSingleElementManager<BatteryUnit, BatteryCollectionManager, BatteryElementManager> implements PowerConsumer {
//    @ConfigurationElement(
//            name = "PowPerThrustCollection"
//    )
//    public static double THRUSTER_BONUS_POW_PER_UNIT = 1.125D;
//    @ConfigurationElement(
//            name = "PowTotalThrust"
//    )
//    public static DoubleReactorDualConfigElement POW_TOTAL = new DoubleReactorDualConfigElement();
//    @ConfigurationElement(
//            name = "MulTotalThrust"
//    )
//    public static DoubleReactorDualConfigElement MUL_TOTAL = new DoubleReactorDualConfigElement();
//    @ConfigurationElement(
//            name = "ReactorPowerPowerConsumptionPerBlockResting"
//    )
//    public static double REACTOR_POWER_CONSUMPTION_PER_BLOCK_RESTING = 1.125D;
//    @ConfigurationElement(
//            name = "ReactorPowerPowerConsumptionPerBlockInUse"
//    )
//    public static double REACTOR_POWER_CONSUMPTION_PER_BLOCK_IN_USE = 1.125D;
//    @ConfigurationElement(
//            name = "ThrustPowerconsumptionPerBlock"
//    )
//    public static double POWER_CONSUMPTION_PER_BLOCK = 1.125D;
//    @ConfigurationElement(
//            name = "UnitCalcMult"
//    )
//    public static DoubleReactorDualConfigElement UNIT_CALC_MULT = new DoubleReactorDualConfigElement();
//    @ConfigurationElement(
//            name = "UnitCalcStyle"
//    )
//    public static UnitCalcStyle UNIT_CALC_STYLE;
//    @ConfigurationElement(
//            name = "MinThrustMassRatio"
//    )
//    public static float MIN_THRUST_MASS_RATIO;
//    @ConfigurationElement(
//            name = "MaxThrustMassRatio"
//    )
//    public static float MAX_THRUST_MASS_RATIO;
//    @ConfigurationElement(
//            name = "MaxThrustToMassAcceleration"
//    )
//    public static float MAX_THRUST_TO_MASS_ACC;
//    @ConfigurationElement(
//            name = "ThrustMassRatioMaxSpeedMultiplier"
//    )
//    public static float THUST_MASS_RATIO_MAX_SPEED_MULTIPLIER;
//    @ConfigurationElement(
//            name = "ThrustMassRatioMaxSpeedAddition"
//    )
//    public static float THUST_MASS_RATIO_MAX_SPEED_ADD;
//    @ConfigurationElement(
//            name = "ThrusterMinReactorPower"
//    )
//    public static float THRUSTER_MIN_REACTOR_POWER;
//    @ConfigurationElement(
//            name = "ThrustRotPercentMult"
//    )
//    public static float THRUST_ROT_PERCENT_MULT;
//    @ConfigurationElement(
//            name = "InertiaPow"
//    )
//    public static float INTERTIA_POW;
//    @ConfigurationElement(
//            name = "MaxRotationalForceX"
//    )
//    public static float MAX_ROTATIONAL_FORCE_X;
//    @ConfigurationElement(
//            name = "MaxRotationalForceY"
//    )
//    public static float MAX_ROTATIONAL_FORCE_Y;
//    @ConfigurationElement(
//            name = "MaxRotationalForceZ"
//    )
//    public static float MAX_ROTATIONAL_FORCE_Z;
//    @ConfigurationElement(
//            name = "BaseRotationalForceX"
//    )
//    public static float BASE_ROTATIONAL_FORCE_X;
//    @ConfigurationElement(
//            name = "BaseRotationalForceY"
//    )
//    public static float BASE_ROTATIONAL_FORCE_Y;
//    @ConfigurationElement(
//            name = "BaseRotationalForceZ"
//    )
//    public static float BASE_ROTATIONAL_FORCE_Z;
//    @ConfigurationElement(
//            name = "ThrustBalanceChangeApplyTimeInSecs"
//    )
//    public static double THRUST_CHANGE_APPLY_TIME_IN_SEC;
public static double THRUSTER_BONUS_POW_PER_UNIT = 1.125D;
    public static DoubleReactorDualConfigElement POW_TOTAL = new DoubleReactorDualConfigElement();
    public static DoubleReactorDualConfigElement MUL_TOTAL = new DoubleReactorDualConfigElement();
    public static double REACTOR_POWER_CONSUMPTION_PER_BLOCK_RESTING = 1.125D;
    public static double REACTOR_POWER_CONSUMPTION_PER_BLOCK_IN_USE = 1.125D;
    public static double POWER_CONSUMPTION_PER_BLOCK = 1.125D;
    public static DoubleReactorDualConfigElement UNIT_CALC_MULT = new DoubleReactorDualConfigElement();
    public static UnitCalcStyle UNIT_CALC_STYLE;
    public static float MIN_THRUST_MASS_RATIO;
    public static float MAX_THRUST_MASS_RATIO;
    public static float MAX_THRUST_TO_MASS_ACC;
    public static float THUST_MASS_RATIO_MAX_SPEED_MULTIPLIER;
    public static float THUST_MASS_RATIO_MAX_SPEED_ADD;
    public static float THRUSTER_MIN_REACTOR_POWER;
    public static float THRUST_ROT_PERCENT_MULT;
    public static float INTERTIA_POW;
    public static float MAX_ROTATIONAL_FORCE_X;
    public static float MAX_ROTATIONAL_FORCE_Y;
    public static float MAX_ROTATIONAL_FORCE_Z;
    public static float BASE_ROTATIONAL_FORCE_X;
    public static float BASE_ROTATIONAL_FORCE_Y;
    public static float BASE_ROTATIONAL_FORCE_Z;
    public static double THRUST_CHANGE_APPLY_TIME_IN_SEC;


    private final Vector3f velocity = new Vector3f();
    private final Vector3f up = new Vector3f();
    private final Vector3f down = new Vector3f();
    private final Vector3f left = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Vector3f forward = new Vector3f();
    private final Vector3f backward = new Vector3f();
    private final Vector3f dir = new Vector3f();
    private final Vector3f joyDir = new Vector3f();
    private final Vector3f dirApplied = new Vector3f();
    private Vector3f linearVelocityTmp = new Vector3f();
    private short lastUpdate;
    private float timeTracker;
    private long lastSendLimitWarning;
    public final Vector3f thrustBalanceAxis = new Vector3f(0.3333333F, 0.3333333F, 0.3333333F);
    public float rotationBalance = 0.5F;
    private float sharedThrustCache;
    private long lastSharedThrust;
    private boolean usingThrust;
    private float powered;
    private Vector3f rawDir = new Vector3f();
    private long lastConsumeCalc;
    private float sharedRestingConsume;
    private float sharedChargingConsume;
    public float ruleModifierOnThrust = 1.0F;

    public BatteryElementManager(SegmentController var1) {
        super(var1, BatteryCollectionManager.class);
        if (!var1.isOnServer()) {
            this.addObserver((DrawerObserver)var1.getState());
        }

    }

    public float getSingleThrust() {
        float var1;
        return (var1 = ((BatteryCollectionManager)this.getCollection()).getTotalThrust()) == 0.0F ? 0.0F : Math.max(0.5F, var1);
    }

    public float getSingleThrustRaw() {
        float var1;
        return (var1 = ((BatteryCollectionManager)this.getCollection()).getTotalThrustRaw()) == 0.0F ? 0.0F : Math.max(0.5F, var1);
    }

    public float getActualThrust() {
        if (this.getSegmentController() instanceof Ship && ((Ship)this.getSegmentController()).getManagerContainer().thrustConfiguration.thrustSharing) {
            long var1;
            if ((var1 = System.currentTimeMillis()) - this.lastSharedThrust > 700L) {
                this.sharedThrustCache = this.getSharedThrust();
                this.lastSharedThrust = var1;
            }

            return this.sharedThrustCache;
        } else {
            return this.getSingleThrust();
        }
    }

    private float getSharedThrust() {
        return (float)(Math.pow((double)this.getSharedThrustRaw(), POW_TOTAL.get(this.isUsingPowerReactors())) * MUL_TOTAL.get(this.isUsingPowerReactors()));
    }

    float getSharedThrustRaw() {
        float var1 = this.getSingleThrustRaw();
        Iterator var2 = this.getSegmentController().railController.next.iterator();

        while(var2.hasNext()) {
            RailRelation var3;
            if ((var3 = (RailRelation)var2.next()).rail.getSegmentController() instanceof Ship) {
                Entity ent = new Entity(var3.docked.getSegmentController());
                var1 += ent.getElementManager(BatteryElementManager.class).getSharedThrustRaw();
            }
        }

        return var1;
    }

    public short getLastUpdateNum() {
        return this.lastUpdate;
    }

    public float getThrustMassRatio() {
        return Math.max(MIN_THRUST_MASS_RATIO, Math.min(MAX_THRUST_MASS_RATIO, this.getActualThrust() / Math.max(1.0E-5F, this.getSegmentController().getMass())));
    }

    public float getMaxVelocity(Vector3f var1) {
        if (this.getSegmentController() instanceof PlayerControllable && !((PlayerControllable)this.getSegmentController()).getAttachedPlayers().isEmpty() && Sector.isTutorialSector(((PlayerState)((PlayerControllable)this.getSegmentController()).getAttachedPlayers().get(0)).getCurrentSector())) {
            if (this.getSegmentController().isClientOwnObject()) {
                ((GameClientState)this.getState()).getController().popupInfoTextMessage("uhh err", 0.0F);
            }

            return 15.0F;
        } else {
            float var2 = this.getMaxSpeedAbsolute();
            var2 = this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_TOP_SPEED, var2);
            float var3;
            return (var3 = var1.length()) > var2 ? var3 - 0.05F : var2;
        }
    }

    public float getMaxSpeedAbsolute() {
        float var1 = ((SendableSegmentController)this.getSegmentController()).getBlockEffectManager().status.topSpeed;
        float var2 = ((GameStateInterface)this.getSegmentController().getState()).getGameState().getMaxGalaxySpeed() * ((SendableSegmentController)this.getSegmentController()).getBlockEffectManager().status.thrustPercent;
        float var3 = (this.getThrustMassRatio() + THUST_MASS_RATIO_MAX_SPEED_ADD) * THUST_MASS_RATIO_MAX_SPEED_MULTIPLIER;
        return (var2 *= var3) + var1 * var2;
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    public Transform getPlayerTrans(Transform var1) {
        var1.setIdentity();
        GlUtil.setForwardVector(this.forward, var1);
        GlUtil.setUpVector(this.up, var1);
        GlUtil.setRightVector(this.right, var1);
        return var1;
    }

    public void onControllerChange() {
    }

    public void orientate(float var1) {
        Vector3f var2;
        (var2 = ((Ship)this.getSegmentController()).getOrientationForce()).scale(this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_TURN_RATE, 1.0F));
        if (!this.getAttachedPlayers().isEmpty() && (this.isOnServer() || ((GameClientState)this.getState()).getCurrentSectorEntities().containsKey(this.getSegmentController().getId()))) {
            this.getSegmentController().getPhysics().orientate(this.getSegmentController(), this.forward, this.up, this.right, var2.x, var2.y, var2.z, var1);
        }

    }

    public ControllerManagerGUI getGUIUnitValues(BatteryUnit var1, BatteryCollectionManager var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4) {
        return ControllerManagerGUI.create((GameClientState)this.getState(),
                "PowCap Unit",
                var1,
                new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_6,
                        StringTools.formatPointZero(var1.thrust)),
                new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_7,
                        StringTools.formatPointZero(var1.getPowerConsumption() * getUpdateFrequency())));
    }

    public boolean canHandle(ControllerStateInterface var1) {
        if(true) return false;
        if (!this.getSegmentController().checkBlockMassServerLimitOk()) {
            if (!this.getSegmentController().isOnServer() && System.currentTimeMillis() - this.lastSendLimitWarning > 5000L) {
                int var4 = ((GameStateInterface)this.getState()).getGameState().getBlockLimit(this.getSegmentController());
                double var2 = (double)Math.round(((GameStateInterface)this.getState()).getGameState().getMassLimit(this.getSegmentController()) * 100.0D) / 100.0D;
                String var5 = var4 > 0 ? var4 + " " + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_9 : "";
                var5 = var5 + (var5.length() > 0 && var2 > 0.0D ? " " + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_10 + " " : "");
                var5 = var5 + (var2 > 0.0D ? var2 + " " + Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_11 : "");
                this.getSegmentController().popupOwnClientMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_8, new Object[]{var5}), 3);
                this.lastSendLimitWarning = System.currentTimeMillis();
            }

            return false;
        } else {
            return !this.getSegmentController().railController.isDocked() || this.getSegmentController().railController.isDockedAndExecuted() && this.getSegmentController().railController.isTurretDocked();
        }
    }

    protected String getTag() {
        //Uses mainreactor because it has nothing
        //not even case sensitive lol
        return "mainreactor";
    }

    public BatteryCollectionManager getNewCollectionManager(SegmentPiece var1, Class<BatteryCollectionManager> var2) {
        return new BatteryCollectionManager(this.getSegmentController(), this);
    }

    protected void playSound(BatteryUnit var1, Transform var2) {
    }

    public void handle(ControllerStateInterface var1, Timer var2) {
        if(true) return;
        if (this.getSegmentController().getMass() > 0.0F) {
            if (var1.isFlightControllerActive()) {
                if (var1.canFlyShip()) {
                    float var3 = Math.min(this.getActualThrust(), Math.max(1.0E-5F, this.getSegmentController().getMass() * MAX_THRUST_TO_MASS_ACC));
                    if (this.getSegmentController().railController.isDockedOrDirty()) {
                        if (this.getSegmentController().railController.isDocked()) {
                            var1.getUp(this.up);
                            this.down.set(this.up);
                            this.down.negate();
                            var1.getRight(this.right);
                            this.left.set(this.right);
                            this.left.negate();
                            var1.getForward(this.forward);
                            this.backward.set(this.forward);
                            this.backward.negate();
                            if ((var1.getPlayerState() == null || System.currentTimeMillis() - var1.getPlayerState().inControlTransition >= 500L) && var1.getPlayerState().canRotate()) {
                                this.getSegmentController().getPhysics().orientate(this.getSegmentController(), this.forward, this.up, this.right, 0.0F, 0.0F, 0.0F, var2.getDelta());
                            }
                        }

                    } else if (this.getSegmentController().getDockingController().isDocked()) {
                        var1.getUp(this.up);
                        this.down.set(this.up);
                        this.down.negate();
                        var1.getRight(this.right);
                        this.left.set(this.right);
                        this.left.negate();
                        var1.getForward(this.forward);
                        this.backward.set(this.forward);
                        this.backward.negate();
                        if ((var1.getPlayerState() == null || System.currentTimeMillis() - var1.getPlayerState().inControlTransition >= 500L) && var1.getPlayerState().canRotate()) {
                            this.getSegmentController().getPhysics().orientate(this.getSegmentController(), this.forward, this.up, this.right, 0.0F, 0.0F, 0.0F, var2.getDelta());
                        }

                    } else {
                        RigidBody var4;
                        if ((var4 = (RigidBody)this.getPhysicsDataContainer().getObject()) != null) {
                            var4.getLinearVelocity(this.linearVelocityTmp);
                            float var5 = this.linearVelocityTmp.length();
                            float var6 = this.getMaxVelocity(this.linearVelocityTmp);
                            if (((SendableSegmentController)this.getSegmentController()).getBlockEffectManager().hasEffect(BlockEffectTypes.CONTROLLESS)) {
                                if (var5 > var6) {
                                    this.linearVelocityTmp.normalize();
                                    this.linearVelocityTmp.scale(var6);
                                    var4.setLinearVelocity(this.linearVelocityTmp);
                                }

                                if (this.getSegmentController().isClientOwnObject()) {
                                    BlockEffect var17 = ((SendableSegmentController)this.getSegmentController()).getBlockEffectManager().getEffect(BlockEffectTypes.CONTROLLESS);
                                    long var14 = System.currentTimeMillis() - var17.getStart();
                                    var14 = var17.getDuration() - var14;
                                    ((GameClientState)this.getSegmentController().getState()).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_1, new Object[]{var14 / 1000L}), "C", 0.0F);
                                }

                            } else {
                                var1.getUp(this.up);
                                this.down.set(this.up);
                                this.down.negate();
                                var1.getRight(this.right);
                                this.left.set(this.right);
                                this.left.negate();
                                var1.getForward(this.forward);
                                this.backward.set(this.forward);
                                this.backward.negate();
                                var6 = 1.0F - ((Ship)this.getSegmentController()).getManagerContainer().getRepulseManager().getThrustToRepul();
                                float var8 = 1.0F - this.rotationBalance;
                                float var9 = Math.max(0.0F, Math.min(1.0F, (var6 + var8) / 2.0F));
                                this.left.scale(var9 * this.thrustBalanceAxis.x * 3.0F * 2.0F);
                                this.right.scale(var9 * this.thrustBalanceAxis.x * 3.0F * 2.0F);
                                this.up.scale(var9 * this.thrustBalanceAxis.y * 3.0F * 2.0F);
                                this.down.scale(var9 * this.thrustBalanceAxis.y * 3.0F * 2.0F);
                                this.forward.scale(var9 * this.thrustBalanceAxis.z * 3.0F * 2.0F);
                                this.backward.scale(var9 * this.thrustBalanceAxis.z * 3.0F * 2.0F);
                                this.dir.set(0.0F, 0.0F, 0.0F);
                                if (var1.isKeyDown(KeyboardMappings.FORWARD_SHIP)) {
                                    ++this.rawDir.z;
                                    this.dir.add(this.forward);
                                }

                                if (var1.isKeyDown(KeyboardMappings.BACKWARDS_SHIP)) {
                                    --this.rawDir.z;
                                    this.dir.add(this.backward);
                                }

                                if (var1.isKeyDown(KeyboardMappings.STRAFE_LEFT_SHIP)) {
                                    --this.rawDir.x;
                                    this.dir.add(this.right);
                                }

                                if (var1.isKeyDown(KeyboardMappings.STRAFE_RIGHT_SHIP)) {
                                    ++this.rawDir.x;
                                    this.dir.add(this.left);
                                }

                                if (var1.isKeyDown(KeyboardMappings.UP_SHIP)) {
                                    ++this.rawDir.y;
                                    this.dir.add(this.up);
                                }

                                if (var1.isKeyDown(KeyboardMappings.DOWN_SHIP)) {
                                    --this.rawDir.y;
                                    this.dir.add(this.down);
                                }

                                this.usingThrust = this.dir.length() > 0.0F || var1.isKeyDown(KeyboardMappings.BRAKE) && (double)var5 > 0.01D;
                                var1.getUp(this.up);
                                this.down.set(this.up);
                                this.down.negate();
                                var1.getRight(this.right);
                                this.left.set(this.right);
                                this.left.negate();
                                var1.getForward(this.forward);
                                this.backward.set(this.forward);
                                this.backward.negate();
                                if ((double)var3 < 0.1D) {
                                    if (this.clientIsOwnShip() && this.getSegmentController().getTotalElements() > 1 && this.dir.length() > 0.0F && ((GameClientState)this.getState()).getWorldDrawer() != null) {
                                        ((GameClientState)this.getState()).getWorldDrawer().getGuiDrawer().notifyEffectHit(this.getSegmentController(), OffensiveEffects.NO_THRUST);
                                    }

                                    var3 = 0.1F;
                                } else if (var3 <= 0.5F && this.clientIsOwnShip()) {
                                    ((GameClientState)this.getState()).getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_2, 0.0F);
                                }

                                this.joyDir.set(0.0F, 0.0F, 0.0F);
                                var1.handleJoystickDir(this.joyDir, new Vector3f(this.forward), new Vector3f(this.left), new Vector3f(this.up));
                                if (this.joyDir.length() > 0.0F) {
                                    this.dir.add(this.joyDir);
                                    this.dir.normalize();
                                }

                                var5 = getUpdateFrequency();
                                this.timeTracker += var2.getDelta();
                                this.timeTracker = Math.min(var5 * 100.0F, this.timeTracker);

                                while(this.timeTracker >= var5) {
                                    this.dirApplied.set(this.dir);
                                    this.timeTracker -= var5;
                                    if (this.dirApplied.length() > 0.0F) {
                                        var6 = var3;
                                        float var13;
                                        if (this.isUsingPowerReactors()) {
                                            var13 = Math.max(THRUSTER_MIN_REACTOR_POWER, this.getPowered());
                                            var6 = Math.max(0.1F, var3 * var13);
                                        } else {
                                            var13 = this.getPowerConsumption();
                                            if (this.getPowerManager().getPower() < (double)var13) {
                                                if (this.getPowerManager().getPower() <= 0.0D) {
                                                    var6 = 0.001F;
                                                } else {
                                                    double var16 = this.getPowerManager().getPower();
                                                    this.getPowerManager().consumePowerInstantly(var16);
                                                    var6 = var3 * ((float)var16 / var13);
                                                }
                                            } else if (!this.getPowerManager().consumePowerInstantly((double)var13)) {
                                                if (this.clientIsOwnShip()) {
                                                    ((GameClientState)this.getState()).getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_3, 0.0F);
                                                }

                                                var6 = 0.01F;
                                            }
                                        }

                                        var6 = this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_ACCELERATION, var6);
                                        applyThrust(this.dirApplied, var6, var4, this.getSegmentController(), 1.0F, this.linearVelocityTmp);
                                        this.lastUpdate = this.getState().getNumberOfUpdate();
                                        var4.getLinearVelocity(this.getVelocity());
                                    } else if (var1.isKeyDown(KeyboardMappings.BRAKE)) {
                                        label168: {
                                            var6 = Math.max(0.1F, var3 * var8);
                                            Vector3f var7;
                                            if ((var7 = var4.getLinearVelocity(new Vector3f())).length() > 1.0F) {
                                                if (this.isUsingPowerReactors()) {
                                                    var6 = Math.max(0.1F, var6 * Math.max(THRUSTER_MIN_REACTOR_POWER, this.getPowered()));
                                                } else {
                                                    label219: {
                                                        float var10 = this.getPowerConsumption();
                                                        if (this.getPowerManager().getPower() < (double)var10) {
                                                            if (this.getPowerManager().getPower() > 0.0D) {
                                                                double var11 = this.getPowerManager().getPower();
                                                                this.getPowerManager().consumePowerInstantly(var11);
                                                                var6 = (float)var11;
                                                                break label219;
                                                            }
                                                        } else {
                                                            if (this.getPowerManager().consumePowerInstantly((double)var10)) {
                                                                break label219;
                                                            }

                                                            if (this.clientIsOwnShip()) {
                                                                ((GameClientState)this.getState()).getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_THRUST_THRUSTERELEMENTMANAGER_4, 0.0F);
                                                            }
                                                        }

                                                        var6 = 0.1F;
                                                    }
                                                }

                                                var6 = this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_BRAKING, var6);
                                                Vector3f var15;
                                                (var15 = new Vector3f(var7)).normalize();
                                                var15.negate();
                                                var15.scale(var6 * 0.5F);
                                                Vector3f var18;
                                                (var18 = new Vector3f(var15)).scale(var4.getInvMass());
                                                if (var7.length() >= var18.length()) {
                                                    var4.applyCentralImpulse(var15);
                                                    break label168;
                                                }
                                            }

                                            var7.set(0.0F, 0.0F, 0.0F);
                                            var4.setLinearVelocity(var7);
                                        }

                                        this.getVelocity().x = 0.0F;
                                        this.getVelocity().y = 0.0F;
                                        this.getVelocity().z = 0.0F;
                                    }

                                    if (var1.canRotateShip()) {
                                        this.orientate(var2.getDelta());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public static void applyThrust(Vector3f var0, float var1, RigidBody var2, SegmentController var3, float var4, Vector3f var5) {
        applyThrustForce(var0, var1, var2, var3, var4, var5, false);
    }

    public static void applyThrustForce(Vector3f var0, float var1, RigidBody var2, SegmentController var3, float var4, Vector3f var5, boolean var6) {
        var2.activate();
        var0.scale(var1 * 0.3F);
        Vector3f var8;
        (var8 = new Vector3f(var0)).scale(var2.getInvMass());
        var2.getLinearVelocity(var5);
        Vector3f var7;
        (var7 = new Vector3f(var5)).add(var8);
        if (var6 || !var3.getHpController().isRebooting()) {
            if (var3 instanceof Ship) {
                var1 = ((Ship)var3).getManagerContainer().getThrusterElementManager().getMaxVelocity(var5) * var4;
            } else {
                var1 = ((GameStateInterface)var3.getState()).getGameState().getMaxGalaxySpeed();
            }

            float var9 = var1 * var1;
            if (var7.lengthSquared() > var9) {
                if (var5.lengthSquared() > var9) {
                    var5.normalize();
                    var5.scale(var1);
                }

                var7.normalize();
                var7.scale(var1);
                var7.sub(var5);
                var7.scale(1.0F / var2.getInvMass());
                var2.applyCentralImpulse(var7);
                return;
            }

            var2.applyCentralImpulse(var0);
        }

    }

    public float getPowerConsumption() {
        float var1;
        if (POWER_CONSUMPTION_PER_BLOCK <= 0.0D) {
            var1 = (var1 = this.getActualThrust()) <= 0.5F ? 0.0F : var1;
        } else if (this.getSegmentController() instanceof Ship && ((Ship)this.getSegmentController()).getManagerContainer().thrustConfiguration.thrustSharing) {
            var1 = this.getSharedConsume(POWER_CONSUMPTION_PER_BLOCK);
        } else {
            var1 = (float)(POWER_CONSUMPTION_PER_BLOCK * (double)((BatteryCollectionManager)this.getCollection()).getTotalSize());
        }

        return var1;
    }

    private float getSharedConsume(double var1) {
        float var3 = (float)(var1 * (double)this.totalSize);
        Iterator var4 = this.getSegmentController().railController.next.iterator();

        while(var4.hasNext()) {
            RailRelation var5;
            if ((var5 = (RailRelation)var4.next()).rail.getSegmentController() instanceof Ship) {
                //My way
                api.entity.Ship ent = new api.entity.Ship(var5.docked.getSegmentController());
                var3 += ent.getElementManager(BatteryElementManager.class).getSharedConsume(var1);
                //Real way
                //var3 += ((Ship)var5.docked.getSegmentController()).getManagerContainer().getThrusterElementManager().getSharedConsume(var1);
            }
        }

        return var3;
    }

    public static final float getUpdateFrequency() {
        return 0.03F;
    }

    public boolean isUsingThrust() {
        return this.usingThrust;
    }

    public void setUsingThrust(boolean var1) {
        this.usingThrust = var1;
    }

    public double getPowerConsumedPerSecondResting() {
        double var1;
        if (this.getSegmentController() instanceof Ship && ((Ship)this.getSegmentController()).getManagerContainer().thrustConfiguration.thrustSharing) {
            if (this.getState().getUpdateTime() - this.lastConsumeCalc > 1000L) {
                this.sharedRestingConsume = this.getSharedConsume(REACTOR_POWER_CONSUMPTION_PER_BLOCK_RESTING);
                this.sharedChargingConsume = this.getSharedConsume(REACTOR_POWER_CONSUMPTION_PER_BLOCK_IN_USE);
                this.lastConsumeCalc = this.getState().getUpdateTime();
            }

            var1 = (double)this.sharedRestingConsume;
        } else {
            var1 = (double)this.totalSize * REACTOR_POWER_CONSUMPTION_PER_BLOCK_RESTING;
        }

        return this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_POWER_CONSUMPTION, var1);
    }

    public double getPowerConsumedPerSecondCharging() {
        double var1;
        if (this.getSegmentController() instanceof Ship && ((Ship)this.getSegmentController()).getManagerContainer().thrustConfiguration.thrustSharing) {
            if (this.getState().getUpdateTime() - this.lastConsumeCalc > 1000L) {
                this.sharedRestingConsume = this.getSharedConsume(REACTOR_POWER_CONSUMPTION_PER_BLOCK_RESTING);
                this.sharedChargingConsume = this.getSharedConsume(REACTOR_POWER_CONSUMPTION_PER_BLOCK_IN_USE);
                this.lastConsumeCalc = this.getState().getUpdateTime();
            }

            var1 = (double)this.sharedChargingConsume;
        } else {
            var1 = (double)this.totalSize * REACTOR_POWER_CONSUMPTION_PER_BLOCK_IN_USE;
        }

        return this.getSegmentController().getConfigManager().apply(StatusEffectType.THRUSTER_POWER_CONSUMPTION, var1);
    }

    public boolean isPowerCharging(long var1) {
        return this.isUsingThrust();
    }

    public void setPowered(float var1) {
        this.powered = var1;
    }

    public float getPowered() {
        return this.powered;
    }

    public void reloadFromReactor(double var1, Timer var3, float var4, boolean var5, float var6) {
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.THRUST;
    }

    public boolean isPowerConsumerActive() {
        return true;
    }

    public String getName() {
        return "BatteryElementManager";
    }

    public Vector3f getInputVectorNormalize(Vector3f var1) {
        var1.set(this.dir);
        if (var1.lengthSquared() > 0.0F) {
            var1.normalize();
        }

        return var1;
    }

    public void dischargeFully() {
    }

    static {
        UNIT_CALC_STYLE = UnitCalcStyle.BOX_DIM_MULT;
        MIN_THRUST_MASS_RATIO = 0.0F;
        MAX_THRUST_MASS_RATIO = 3.0F;
        MAX_THRUST_TO_MASS_ACC = 5.0F;
        THUST_MASS_RATIO_MAX_SPEED_MULTIPLIER = 1.0F;
        THUST_MASS_RATIO_MAX_SPEED_ADD = 0.5F;
        THRUSTER_MIN_REACTOR_POWER = 0.5F;
        THRUST_ROT_PERCENT_MULT = 1.0F;
        INTERTIA_POW = 0.5F;
        MAX_ROTATIONAL_FORCE_X = 0.0F;
        MAX_ROTATIONAL_FORCE_Y = 0.0F;
        MAX_ROTATIONAL_FORCE_Z = 0.0F;
        BASE_ROTATIONAL_FORCE_X = 0.0F;
        BASE_ROTATIONAL_FORCE_Y = 0.0F;
        BASE_ROTATIONAL_FORCE_Z = 0.0F;
        THRUST_CHANGE_APPLY_TIME_IN_SEC = 1.125D;
    }
}
