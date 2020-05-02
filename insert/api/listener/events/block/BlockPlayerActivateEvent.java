package api.listener.events.block;

import api.element.block.Block;
import api.entity.Entity;
import api.entity.Player;
import api.listener.events.Event;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.activation.ActivationElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;

public class BlockPlayerActivateEvent extends Event {

    private final ActivationElementManager manager;
    private final Block block;
    private final Entity entity;
    private final Player player;
    private ActivationCollectionManager activationCollectionManager;

    public BlockPlayerActivateEvent(ActivationElementManager manager, SegmentPiece segmentPiece, PlayerState playerState, ActivationCollectionManager activationCollectionManager) {
        this.manager = manager;
        this.block = new Block(segmentPiece);
        this.player = new Player(playerState);
        this.activationCollectionManager = activationCollectionManager;
        this.entity = block.getEntity();
    }

    public Player getPlayer() {
        return player;
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

    public ActivationCollectionManager getActivationCollectionManager() {
        return activationCollectionManager;
    }
}