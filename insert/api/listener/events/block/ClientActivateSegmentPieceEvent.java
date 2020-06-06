package api.listener.events.block;

import api.listener.events.Event;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.common.data.SegmentPiece;

public class ClientActivateSegmentPieceEvent extends Event {
    private final PlayerInteractionControlManager picm;
    private final SegmentPiece piece;

    public ClientActivateSegmentPieceEvent(PlayerInteractionControlManager picm, SegmentPiece piece){

        this.picm = picm;
        this.piece = piece;
    }

    public PlayerInteractionControlManager getPicm() {
        return picm;
    }

    public SegmentPiece getPiece() {
        return piece;
    }
}
