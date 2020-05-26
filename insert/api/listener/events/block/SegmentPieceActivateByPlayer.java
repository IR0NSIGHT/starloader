package api.listener.events.block;

import api.listener.events.Event;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;

public class SegmentPieceActivateByPlayer extends Event {


    private final ActivationElementManager manager;
    private final SegmentPiece segmentPiece;
    private final PlayerState playerState;
    private final ActivationCollectionManager activationCollectionManager;

    public ActivationElementManager getManager() {
        return manager;
    }

    public SegmentPiece getSegmentPiece() {
        return segmentPiece;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public ActivationCollectionManager getActivationCollectionManager() {
        return activationCollectionManager;
    }

    public SegmentPieceActivateByPlayer(ActivationElementManager manager, SegmentPiece segmentPiece, PlayerState playerState, ActivationCollectionManager activationCollectionManager) {
        this.manager = manager;
        this.segmentPiece = segmentPiece;
        this.playerState = playerState;
        this.activationCollectionManager = activationCollectionManager;
    }
}