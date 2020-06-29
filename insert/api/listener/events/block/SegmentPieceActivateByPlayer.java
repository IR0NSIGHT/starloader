package api.listener.events.block;

import api.listener.events.Event;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;

public class SegmentPieceActivateByPlayer extends Event {


    private final SegmentPiece segmentPiece;


    public SegmentPiece getSegmentPiece() {
        return segmentPiece;
    }

    public SegmentPieceActivateByPlayer(SegmentPiece segmentPiece) {
        this.segmentPiece = segmentPiece;
    }
}