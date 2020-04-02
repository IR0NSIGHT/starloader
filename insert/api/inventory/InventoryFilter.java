package api.inventory;

import java.util.Map;

public class InventoryFilter {

    private Map<Block, int[]> blocks;

    public InventoryFilter() {
        this.blocks = null;
    }

    public void addBlock(Block block, int amount) {
        blocks.put(block, new int[] { amount, 0 });
    }

    public void addBlock(Block block, int amount, int upTo) {
        blocks.put(block, new int[] { amount, upTo });
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
    }

    public Map<Block, int[]> getBlocks() {
        return blocks;
    }
}
