package api.listener.events.block;

import api.entity.Entity;
import api.inventory.ItemStack;
import api.listener.events.Event;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.SegmentPiece;

public class BlockActivateEvent extends Event {
    public static int id = idLog++;
    private final ActivationElementManager manager;
    private final SegmentPiece segmentPiece;
    private final ItemStack blockType;
    private boolean canceled = false;

    public BlockActivateEvent(ActivationElementManager manager, SegmentPiece segmentPiece, ItemStack blockType){
        this.manager = manager;
        this.segmentPiece = segmentPiece;
        this.blockType = blockType;
    }
    public Entity getEntity(){
        return new Entity(this.getSegmentPiece().getSegmentController());
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }


    public boolean isCanceled() {
        return canceled;
    }

    public ActivationElementManager getManager() {
        return manager;
    }

    public SegmentPiece getSegmentPiece() {
        return segmentPiece;
    }

    public ItemStack getBlockType() {
        return blockType;
    }
}
