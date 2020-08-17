package api.utils.game;

import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;

import java.util.Set;

public class PlayerUtils {
    public static PlayerControllable getCurrentControl(PlayerState state){
        Set<ControllerStateUnit> units = state.getControllerState().getUnits();
        if(units.isEmpty()) return null;
        ControllerStateUnit unit = units.iterator().next();
        return unit.playerControllable;
    }
}
