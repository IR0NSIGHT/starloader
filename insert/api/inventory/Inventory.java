package api.inventory;

import api.element.Element;
import api.element.block.Block;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.inventory.InventorySlot;

import java.util.ArrayList;

public class Inventory {

    private ArrayList<Element> contents;

    public Inventory() {

    }

    public void addElement(Element element) {
        this.contents.add(element);
    }

    public ArrayList<Element> getContents() {
        return contents;
    }

    public Element getElementFromSlot(InventorySlot slot) {
        Element element = Element.getElementFromID(slot.metaId);
        addElement(element);
        return element;
    }

    public static org.schema.game.common.data.player.inventory.Inventory getPlayerStateInventory(PlayerState playerState) {
        return playerState.getInventory();
    }
}
