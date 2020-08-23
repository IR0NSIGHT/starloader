package api.listener.events.player;

import api.listener.events.Event;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;

public class PlayerLeaveFactionEvent extends Event {

    private Faction faction;
    private PlayerState player;

    public PlayerLeaveFactionEvent(Faction faction, PlayerState player) {
        this.faction = faction;
        this.player = player;
    }

    public Faction getFaction() {
        return faction;
    }

    public PlayerState getPlayer() {
        return player;
    }
}
