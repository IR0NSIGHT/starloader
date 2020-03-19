package api.inventory;

import api.element.Element;
import java.util.Map;

public class Inventory {

    private Map<Element, Integer> contents;
    private boolean locked = false;
    private double storageCapacity;
    private int cargoBlocks;
    private InventoryType inventoryType;

    public Inventory(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public void addElement(Element element, int count) {
        this.contents.put(element, count);
    }

    public Map<Element, Integer> getContents() {
        return contents;
    }

    public void setContents(Map<Element, Integer> contents) {
        this.contents = contents;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }
}
