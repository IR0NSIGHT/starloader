package org.schema.game.common.data.player.inventory;

public class VirtualCreativeModeInventory extends CreativeModeInventory {
    public VirtualCreativeModeInventory(InventoryHolder var1, long var2) {
        super(var1, var2);
    }

    public static int getInventoryType() {
        return 7;
    }

    public boolean isLockedInventory() {
        return false;
    }

    public int getLocalInventoryType() {
        return 7;
    }
}
