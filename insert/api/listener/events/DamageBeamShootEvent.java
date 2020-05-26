package api.listener.events;

import org.schema.game.common.controller.elements.beam.BeamCommand;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;

public class DamageBeamShootEvent extends Event {
    DamageBeamUnit beamWeapon;
    private BeamCommand command;

    public DamageBeamShootEvent(DamageBeamUnit unit, BeamCommand command){
        this.beamWeapon = (unit);
        this.command = command;
    }

    public DamageBeamUnit getBeamWeapon() {
        return beamWeapon;
    }

    public BeamCommand getCommand() {
        return command;
    }
}
