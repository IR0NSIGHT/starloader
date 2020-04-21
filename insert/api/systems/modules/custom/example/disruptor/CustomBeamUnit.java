//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example.disruptor;

import api.systems.modules.custom.CustomShipBeamElement;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.ShipManagerContainer;
import org.schema.game.common.controller.elements.beam.BeamUnit;

public class CustomBeamUnit extends BeamUnit<CustomBeamUnit, CustomBeamCollectionManager, CustomBeamElementManager> {
    public CustomShipBeamElement customElement;
    public CustomBeamUnit(CustomShipBeamElement customElement) {
        this.customElement = customElement;
    }

    public ControllerManagerGUI createUnitGUI(GameClientState var1, ControlBlockElementCollectionManager<?, ?, ?> var2, ControlBlockElementCollectionManager<?, ?, ?> var3) {
        return ((CustomBeamElementManager)((CustomBeamCollectionManager)this.elementCollectionManager).getElementManager()).getGUIUnitValues(this, (CustomBeamCollectionManager)this.elementCollectionManager, var2, var3);
    }

    public float getBeamPowerWithoutEffect() {
        return this.getBeamPower();
    }

    public float getBeamPower() {
        return customElement.getBeamPower(this);
    }

    public float getBaseBeamPower() {
        return 10;//?????
    }

    public float getPowerConsumption() {
        return customElement.getPowerConsumption(this);
    }

    public void flagBeamFiredWithoutTimeout() {
        ((CustomBeamCollectionManager)this.elementCollectionManager).flagBeamFiredWithoutTimeout(this);
    }

    public float getDistanceRaw() {
        return customElement.getBaseDistance() * ((GameStateInterface)this.getSegmentController().getState()).getGameState().getWeaponRangeReference();
    }


    public float getBasePowerConsumption() {
        return 1000.0F;
    }

    public float getPowerConsumptionWithoutEffect() {
        return customElement.getPowerConsumption(this);
    }

    public double getPowerConsumedPerSecondResting() {
        return customElement.getRestingPowerConsumption(this);
    }

    public double getPowerConsumedPerSecondCharging() {
        return customElement.getReloadingPowerConsumption(this);
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.SUPPORT_BEAMS;
    }

    public HitType getHitType() {
        return HitType.WEAPON;
    }

    public boolean isLatchOn() {
        return customElement.isLatch();
    }

    public float getDamage() {
        return customElement.getDamage(this);
    }
}
