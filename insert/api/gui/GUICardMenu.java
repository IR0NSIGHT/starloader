package api.gui;

import java.util.ArrayList;

public class GUICardMenu extends GUIMenu {

    private ArrayList<GUICard> elements;

    public GUICardMenu(String displayName) {
        super(displayName);
    }

    public void addElement(GUICard element) {
        elements.add(element);
    }

    public void removeElement(GUICard element) {
        elements.remove(element);
    }

    public void setElements(ArrayList<GUICard> elements) {
        this.elements = elements;
    }

    public ArrayList<GUICard> getElements() {
        return elements;
    }
}
