//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example.disruptor;

import api.element.block.Blocks;
import api.systems.modules.custom.CustomShipBeamElement;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.controller.elements.TagModuleUsableInterface;
import org.schema.game.common.controller.elements.beam.BeamCommand;
import org.schema.game.common.controller.elements.beam.BeamElementManager;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamMetaDataDummy;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorUnit;
import org.schema.game.common.controller.elements.combination.BeamCombiSettings;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.config.FloatReactorDualConfigElement;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;

import javax.vecmath.Vector3f;

public class CustomBeamElementManager extends BeamElementManager<CustomBeamUnit, CustomBeamCollectionManager, CustomBeamElementManager> implements TagModuleUsableInterface {
    public static final String TAG_ID = "BRUH";
    private final Blocks moduleId;
    public CustomShipBeamElement customElement;

    //REQIORED CODE SO THE CONFIG DOESNT BREAK
    @ConfigurationElement(
            name = "TractorMassPerBlock"
    )
    public static FloatReactorDualConfigElement TRACTOR_MASS_PER_BLOCK = new FloatReactorDualConfigElement();
    @ConfigurationElement(
            name = "TickRate"
    )
    public static float TICK_RATE = 100.0F;
    @ConfigurationElement(
            name = "Distance"
    )
    public static float DISTANCE = 30.0F;
    @ConfigurationElement(
            name = "CoolDown"
    )
    public static float COOL_DOWN = 3.0F;
    @ConfigurationElement(
            name = "ReactorPowerConsumptionResting"
    )
    public static float REACTOR_POWER_CONSUMPTION_RESTING = 10.0F;
    @ConfigurationElement(
            name = "ReactorPowerConsumptionCharging"
    )
    public static float REACTOR_POWER_CONSUMPTION_CHARGING = 10.0F;
    @ConfigurationElement(
            name = "BurstTime"
    )
    public static float BURST_TIME = 3.0F;
    @ConfigurationElement(
            name = "InitialTicks"
    )
    public static float INITIAL_TICKS = 1.0F;
    @ConfigurationElement(
            name = "ForceToMassMax"
    )
    public static float FORCE_TO_MASS_MAX = 10.0F;
    @ConfigurationElement(
            name = "RailHitMultiplierParent"
    )
    public static double RAIL_HIT_MULTIPLIER_PARENT = 3.0D;
    @ConfigurationElement(
            name = "RailHitMultiplierChild"
    )
    public static double RAIL_HIT_MULTIPLIER_CHILD = 3.0D;

    public double getRailHitMultiplierParent() {
        return customElement.getParentHitMultiplier();
    }

    public double getRailHitMultiplierChild() {
        return customElement.getChildHitMultiplier();
    }

    public void doShot(CustomBeamUnit c, CustomBeamCollectionManager m, ShootContainer var3, PlayerState var4, float var5, Timer var6, boolean var7) {
        ManagerModuleCollection var11 = null;
        short var8;
        if (m.getEffectConnectedElement() != -9223372036854775808L) {
            var8 = (short) ElementCollection.getType(m.getEffectConnectedElement());
            var11 = this.getManagerContainer().getModulesControllerMap().get(var8);
        }

        if (m.getEffectConnectedElement() != -9223372036854775808L) {
            var8 = (short)ElementCollection.getType(m.getEffectConnectedElement());
            var11 = this.getManagerContainer().getModulesControllerMap().get(var8);
            ControlBlockElementCollectionManager var9;
            if ((var9 = CombinationAddOn.getEffect(m.getEffectConnectedElement(), var11, this.getSegmentController())) != null) {
                m.setEffectTotal(var9.getTotalSize());
            }
        }

        ShootingRespose var10;
        if (this.isCombinable() && m.getSlaveConnectedElement() != -9223372036854775808L) {
            var8 = (short)ElementCollection.getType(m.getSlaveConnectedElement());
            ManagerModuleCollection var14 = this.getManagerContainer().getModulesControllerMap().get(var8);
            var10 = this.handleAddOn(this, m, c, var14, var11, var3, (SimpleTransformableSendableObject)null, var4, var6, var5);
            this.handleResponse(var10, c, var3.weapontOutputWorldPos);
        } else {

            Vector3f var12;
            (var12 = new Vector3f()).set(var3.weapontOutputWorldPos);
            var3.shootingDirTemp.scale(c.getDistance());
            var12.add(var3.shootingDirTemp);
            BeamCommand b;
            (b = new BeamCommand()).minEffectiveRange = c.getMinEffectiveRange();
            b.minEffectiveValue = c.getMinEffectiveValue();
            b.maxEffectiveRange = c.getMaxEffectiveRange();
            b.maxEffectiveValue = c.getMaxEffectiveValue();
            b.currentTime = var6.currentTime;
            b.identifier = c.getSignificator();
            b.relativePos.set((float)(c.getOutput().x - 16), (float)(c.getOutput().y - 16), (float)(c.getOutput().z - 16));
            b.reloadCallback = c;
            b.from.set(var3.weapontOutputWorldPos);
            b.to.set(var12);
            b.playerState = var4;
            b.beamTimeout = c.getBurstTime() > 0.0F ? c.getBurstTime() : var5;
            b.tickRate = c.getTickRate();
            b.beamPower = c.getBeamPower();
            b.cooldownSec = c.getCoolDownSec();
            b.bursttime = c.getBurstTime();
            b.initialTicks = c.getInitialTicks();
            b.powerConsumedByTick = c.getPowerConsumption();
            b.latchOn = c.isLatchOn();
            b.checkLatchConnection = c.isCheckLatchConnection();
            b.hitType = c.getHitType();
            b.powerConsumedExtraByTick = 0.0F;
            b.railParent = this.getRailHitMultiplierParent();
            b.railChild = this.getRailHitMultiplierChild();
            if (var4 != null && var4.isKeyDownOrSticky(KeyboardMappings.WALK)) {
                b.dontFade = true;
            }

            b.weaponId = m.getUsableId();
            b.controllerPos = m.getControllerPos();
            b.firendlyFire = c.isFriendlyFire();
            b.penetrating = c.isPenetrating();
            b.acidDamagePercent = c.getAcidDamagePercentage();
            customElement.fireBeam(b);
            var10 = m.getHandler().addBeam(b);
            this.handleResponse(var10, c, var3.weapontOutputWorldPos);
        }
    }

    public CustomBeamElementManager(SegmentController var1, Blocks controllerId, Blocks moduleId) {
        super(controllerId.getId(), moduleId.getId(), var1);
        this.moduleId = moduleId;
    }

    public void updateActivationTypes(ShortOpenHashSet var1) {
        var1.add(moduleId.getId());
    }

    protected String getTag() {
        return "tractorbeam";
    }

    public CustomBeamCollectionManager getNewCollectionManager(SegmentPiece var1, Class<CustomBeamCollectionManager> var2) {
        CustomBeamCollectionManager customBeamCollectionManager = new CustomBeamCollectionManager(var1, this.getSegmentController(), this);
        return customBeamCollectionManager;
    }

    public String getManagerName() {
        return customElement.getName();
    }

    @Override
    protected void playSound(CustomBeamUnit customBeamUnit, Transform transform) {

    }

    public ControllerManagerGUI getGUIUnitValues(TractorUnit var1, TractorBeamCollectionManager var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4) {
        float var5 = var1.getBeamPower();
        float var6 = var1.getTickRate();
        float var7 = var1.getDistance();
        var1.getPowerConsumption();
        return ControllerManagerGUI.create((GameClientState)this.getState(), Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_1, var1, new GUIKeyValueEntry[]{new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_2, StringTools.formatPointZero(var5)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_3, StringTools.formatPointZeroZero(var6)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_4, StringTools.formatPointZero(var7)), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_5, var1.getPowerConsumedPerSecondResting()), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORELEMENTMANAGER_7, var1.getPowerConsumedPerSecondCharging())});
    }

    public float getTickRate() {
        return customElement.getTickRate();
    }

    public float getCoolDown() {
        return customElement.getCooldown();
    }

    public float getBurstTime() {
        return customElement.getBurstTime();
    }

    public String getTagId() {
        return "TRM";
    }

    public float getInitialTicks() {
        return 1F;
    }

    public double getMiningScore() {
        return (double)this.totalSize;
    }

    public CombinationAddOn<CustomBeamUnit, CustomBeamCollectionManager, ? extends CustomBeamElementManager, BeamCombiSettings> getAddOn() {
        return null;
    }

    public TractorBeamMetaDataDummy getDummyInstance() {
        return new TractorBeamMetaDataDummy();
    }

}
