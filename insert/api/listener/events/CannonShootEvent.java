package api.listener.events;

import api.systems.weapons.Cannon;
import api.systems.weapons.DamageBeam;
import org.schema.game.common.controller.elements.beam.BeamCommand;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;

import javax.vecmath.Vector4f;

public class CannonShootEvent extends Event {
    Cannon cannon;

    public CannonShootEvent(WeaponUnit unit){
        this.cannon = new Cannon(unit);
        this.color = cannon.getCollectionManager().getColor();
    }

    public Cannon getCannon() {
        return cannon;
    }

    private Vector4f color;

    public void setColor(Vector4f c){
        this.color = c;
    }
    public Vector4f getColor() {
        return color;
    }
}
