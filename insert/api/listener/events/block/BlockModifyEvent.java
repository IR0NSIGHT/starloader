package api.listener.events.block;

import api.element.block.Block;
import api.listener.events.Event;
import org.schema.game.common.controller.BlockProcessor;
import org.schema.game.common.data.VoidSegmentPiece;

public class BlockModifyEvent extends Event {
    public static int id = idLog++;
    private Block block;
    private BlockProcessor.PieceList pieces;

    public BlockModifyEvent(BlockProcessor.PieceList var1, VoidSegmentPiece var6) {
        this.pieces = var1;
        this.block = new Block(var6);
    }

    public Block getBlock() {
        return block;
    }

    public BlockProcessor.PieceList getPieces() {
        return pieces;
    }
}
