package api.listener.events.player;

import api.entity.Player;
import api.listener.events.Event;
import org.schema.game.common.controller.EditableSendableSegmentController;

import java.util.Arrays;

public class PlayerCommandEvent extends Event {
    public static int id = idLog++;
    public final String command;
    public final Player player;
    public final String[] args;

    public PlayerCommandEvent(String command, Player player, String[] args){

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
}
