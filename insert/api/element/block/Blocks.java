package api.element.block;

public class Blocks {

    public Block getBlock(Block block) {
        return BlockConfigReader.getBlockFromID(block.getID());
    }
}
