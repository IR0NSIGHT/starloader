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


    public BatteryElementManager(SegmentController var1) {
        super(var1, BatteryCollectionManager.class);
        if (!var1.isOnServer()) {
            this.addObserver((DrawerObserver)var1.getState());
        }

    }

    public void onControllerChange() {
    }


    public ControllerManagerGUI getGUIUnitValues(BatteryUnit var1, BatteryCollectionManager var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4) {
        return ControllerManagerGUI.create((GameClientState)this.getState(),
                "PowCap Unit",
                var1,

                new ModuleValueEntry("BU size",
                        var1.size()),
                new ModuleValueEntry("BB size",
                        var1.getBBTotalSize())
        );
    }

    public boolean canHandle(ControllerStateInterface var1) {
        return true;
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
    }


    // api.entity.Ship ent = new api.entity.Ship(var5.docked.getSegmentController());
    //                var3 += ent.getElementManager(BatteryElementManager.class).getSharedConsume(var1);


    public double getPowerConsumedPerSecondResting() {
        return this.totalSize;
    }

    public double getPowerConsumedPerSecondCharging() {
        return this.totalSize*2;
    }

    public boolean isPowerCharging(long var1) {
        return false;
    }
    private float powered = 0;
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

    public void dischargeFully() {
    }
}
