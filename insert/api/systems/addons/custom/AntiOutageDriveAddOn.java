package api.systems.addons.custom;

import api.element.block.Blocks;
import api.systems.Shield;
import org.schema.game.common.controller.elements.ManagerContainer;

public class AntiOutageDriveAddOn extends CustomAddOn {
    public AntiOutageDriveAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return 10;
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
        return Blocks.BLACK_LIGHT_0.getPlayerUsableId();
    }

    @Override
    public String getWeaponRowName() {
        return "Anti-Outage Drive";
    }

    @Override
    public short getWeaponRowIcon() {
        return Blocks.BLACK_LIGHT_0.getId();
    }

    @Override
    public float getDuration() {
        return 4F;
    }

    @Override
    public void onDeactivateFromTime() {
    }

    @Override
    public boolean onExecute() {
        return true;
    }

    @Override
    public void onActive() {

        Shield shield = this.entity.getLastHitShield();
        if(shield != null){
            shield.setOutageTimeout(0);
            this.getSegmentController().sendServerMessage("!! Anti-Outage Active !!", 1);
        }else{
            this.getSegmentController().sendServerMessage("!! Last hit shield was null !!", 1);
        }
    }

    @Override
    public void onInactive() {

    }

    @Override
    public String getName() {
        return "Anti-Outage Drive";
    }
}
