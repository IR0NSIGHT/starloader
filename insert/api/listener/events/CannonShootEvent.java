package api.listener.events;

import org.schema.game.common.controller.elements.weapon.WeaponUnit;

public class CannonShootEvent extends Event {
    WeaponUnit cannon;

    public CannonShootEvent(WeaponUnit unit){
        this.cannon = (unit);
    }

    public WeaponUnit getCannon() {
        return cannon;
    }

}
