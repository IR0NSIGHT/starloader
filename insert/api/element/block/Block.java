package api.element.block;

import api.entity.Entity;
import api.utils.StarVector;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;

public class Block {
    private SegmentPiece internalBlock;

    public Block(SegmentPiece internalBlock) {
        this.internalBlock = internalBlock;
    }
    public Blocks getType(){
        return Blocks.fromId(internalBlock.getType());
    }
    public Vector3i getLocation(){
        return new Vector3i(internalBlock.x, internalBlock.y, internalBlock.z);
    }
    public float distance(Block other){
        return SegmentPiece.getWorldDistance(internalBlock, other.internalBlock);
    }
    public Vector3i getWorldLocation(){
        return internalBlock.getAbsolutePos(new Vector3i());
    }
    public Entity getEntity(){
        return new Entity(internalBlock.getSegmentController());
    }
    public int getHp(){
        return internalBlock.getHitpointsFull();
    }
    public void setHp(int i){
        internalBlock.setHitpointsFull(i);
    }
    public int getMaxHp(){
        return getType().getInfo().maxHitPointsFull;
    }

    public SegmentPiece getInternalSegmentPiece() {
        return internalBlock;
    }
}
