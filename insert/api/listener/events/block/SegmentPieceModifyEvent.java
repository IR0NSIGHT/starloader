package api.listener.events.block;

import api.listener.events.Event;
import org.schema.game.common.controller.BlockProcessor;
import org.schema.game.common.data.VoidSegmentPiece;

public class SegmentPieceModifyEvent extends Event {

    private final BlockProcessor.PieceList var1;
    private final VoidSegmentPiece var6;

    public SegmentPieceModifyEvent(BlockProcessor.PieceList var1, VoidSegmentPiece var6) {
        this.var1 = var1;
        this.var6 = var6;
    }

    public BlockProcessor.PieceList getVar1() {
        return var1;
    }

    public VoidSegmentPiece getVar6() {
        return var6;
    }
}
