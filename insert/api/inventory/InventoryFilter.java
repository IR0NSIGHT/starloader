package api.inventory;

import java.util.Map;

public class InventoryFilter {

    private Map<ItemStack, int[]> blocks;

    public InventoryFilter() {
        this.blocks = null;
    }

    public void addBlock(ItemStack itemStack, int amount) {
        blocks.put(itemStack, new int[] { amount, 0 });
    }

    public void addBlock(ItemStack itemStack, int amount, int upTo) {
        blocks.put(itemStack, new int[] { amount, upTo });
    }

    public void removeBlock(ItemStack itemStack) {
        blocks.remove(itemStack);
    }

    public Map<ItemStack, int[]> getBlocks() {
        return blocks;
    }
}
