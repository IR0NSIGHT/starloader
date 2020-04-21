package api.systems.modules.custom;

import api.element.block.Blocks;
import api.systems.modules.custom.example.disruptor.CustomBeamUnit;
import org.schema.game.common.controller.elements.beam.BeamCommand;

public abstract class CustomShipBeamElement {
    public abstract float getPowerConsumption(CustomBeamUnit unit);
    public abstract float getBeamPower(CustomBeamUnit unit);
    public abstract double getRestingPowerConsumption(CustomBeamUnit unit);
    public abstract double getReloadingPowerConsumption(CustomBeamUnit unit);
    public abstract double getParentHitMultiplier();
    public abstract double getChildHitMultiplier();
    public abstract Blocks getControllerBlock();
    public abstract Blocks getModuleBlock();
    public abstract float getTickRate();
    public abstract float getCooldown();
    public abstract float getBurstTime();
    public abstract String getName();

    public abstract float getBaseDistance();
    public abstract boolean isLatch();
    public abstract float getDamage(CustomBeamUnit unit);

    public abstract void fireBeam(BeamCommand beam);
}
