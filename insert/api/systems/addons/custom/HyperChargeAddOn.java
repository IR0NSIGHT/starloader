package api.systems.addons.custom;

import api.element.block.Blocks;
import api.server.Server;
import org.schema.game.common.controller.elements.ManagerContainer;

public class HyperChargeAddOn extends CustomAddOn {
    public HyperChargeAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return 90;
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return 0;
    }

    @Override
    public long getUsableId() {
        return Blocks.VARAT_CHARGED_CIRCUIT.getPlayerUsableId();
    }

    @Override
    public String getWeaponRowName() {
        return "Hyper Charger";
    }

    @Override
    public short getWeaponRowIcon() {
        return Blocks.VARAT_CHARGED_CIRCUIT.getId();
    }

    @Override
    public float getDuration() {
        return 0.7F;
    }

    @Override
    public void onDeactivateFromTime() {
        Server.broadcastMessage("ayo what up im tryna get down");
        entity.getCurrentReactor().setBoost(1F);
    }

    @Override
    public boolean onExecute() {
        entity.getCurrentReactor().setBoost(100F);
        return true;
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onInactive() {

    }

    @Override
    public String getName() {
        return "Hyper Charger";
    }
}
