package api.listener.events.block;

import api.element.block.Block;
import api.entity.Entity;
import api.listener.events.Event;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.SegmentPiece;

public class BlockActivateEvent extends Event {

    public static int id = idLog++;
    private final ActivationElementManager manager;
    private final Block block;
    private final Entity entity;
    private ActivationCollectionManager activationCollectionManager;

    public BlockActivateEvent(ActivationElementManager manager, SegmentPiece segmentPiece, ActivationCollectionManager activationCollectionManager){
        this.manager = manager;
        this.block = new Block(segmentPiece);
        this.activationCollectionManager = activationCollectionManager;
        this.entity = block.getEntity();
    }
    public Entity getEntity(){
        return entity;
    }

    public ActivationElementManager getManager() {
        return manager;
    }

    public Block getBlock() {
        return block;
    }

    public short getBlockId() {
        return block.getType().getId();
    }

//    private boolean isActivatedByPlayer() {
//        if(getManager().getState() instanceof PlayerState) { //Does getState() refer to the player who activated it?
//            return true;
//        }
//        return false;
//    }
//
//    public Player getPlayer() {
//        if(isActivatedByPlayer()) {
//            Player player = new Player((PlayerState) getManager().getState()); //Does getState() refer to the player who activated it?
//            return player;
//        }
//        return null;
//    }

    public ActivationCollectionManager getActivationCollectionManager() {
        return activationCollectionManager;
    }
}
