package api.listener.events;

import api.systems.weapons.DamageBeam;
import org.schema.game.common.controller.elements.beam.BeamCommand;
import org.schema.game.common.controller.elements.beam.BeamUnit;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;

public class DamageBeamShootEvent extends Event {
    DamageBeam beamWeapon;
    private BeamCommand command;

    public DamageBeamShootEvent(DamageBeamUnit unit, BeamCommand command){
        this.beamWeapon = new DamageBeam(unit);
        this.command = command;
    }

    public DamageBeam getBeamWeapon() {
        return beamWeapon;
    }

    public BeamCommand getCommand() {
        return command;
    }
}
