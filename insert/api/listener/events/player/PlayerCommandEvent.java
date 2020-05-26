package api.listener.events.player;

import api.listener.events.Event;
import org.schema.game.common.data.player.PlayerState;

import java.util.Arrays;

public class PlayerCommandEvent extends Event {
    public final String command;
    public final PlayerState player;
    public final String[] args;

    public PlayerCommandEvent(String command, PlayerState player, String[] args){

        this.command = command;
        this.player = player;
        this.args = args;
    }

    @Override
    public String toString() {
        return "PlayerCommandEvent{" +
                "command='" + command + '\'' +
                ", player=" + player +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public String getCommand() {
        return command;
    }

    public PlayerState getPlayer() {
        return player;
    }

    public String[] getArgs() {
        return args;
    }
}
