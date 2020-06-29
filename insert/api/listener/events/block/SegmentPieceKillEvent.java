package api.listener.events.block;

import api.listener.events.Event;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.data.SegmentPiece;

public class SegmentPieceKillEvent extends Event {

    private final SegmentPiece piece;
    private final SegmentController controller;
    private final Damager damager;

    public SegmentPieceKillEvent(SegmentPiece piece, SegmentController controller, Damager damager){
        this.piece = piece;
        this.controller = controller;
        this.damager = damager;
    }

    public SegmentPiece getPiece() {
        return piece;
    }

    public SegmentController getController() {
        return controller;
    }

    public Damager getDamager() {
        return damager;
    }
}
