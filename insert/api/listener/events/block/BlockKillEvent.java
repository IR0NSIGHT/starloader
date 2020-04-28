package api.listener.events.block;

import api.element.block.Block;
import api.entity.Entity;
import api.listener.events.Event;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.data.SegmentPiece;

public class BlockKillEvent extends Event {
    public static int id = idLog++;
    private final Damager damager;
    private final Entity entity;
    private final Block block;

    public BlockKillEvent(SegmentPiece piece, SegmentController controller, Damager damager){
        this.damager = damager;
        this.entity = new Entity(controller);
        this.block = new Block(piece);
    }

    public Damager getDamager() {
        return damager;
    }

    public Entity getEntity() {
        return entity;
    }

    public Block getBlock() {
        return block;
    }
}
