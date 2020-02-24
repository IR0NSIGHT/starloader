package api.inventory;

import api.element.Element;
import org.schema.game.common.data.player.PlayerState;
import java.util.ArrayList;

public class Inventory {

    private ArrayList<Element> contents;
    private boolean locked = false;
    private double storageCapacity;
    private int cargoBlocks;

    public Inventory() {

    }

    public void addElement(Element element) {
        this.contents.add(element);
    }

    public ArrayList<Element> getContents() {
        return contents;
    }

    public static org.schema.game.common.data.player.inventory.Inventory getPlayerStateInventory(PlayerState playerState) {
        return playerState.getInventory();
    }
}
