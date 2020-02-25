package api.gui;

public class GUIPanel {

    private GUIMenu parentMenu;
    private int[] relativePos = new int[2];
    private int[] preferredSize = new int[2];
    private int[] minSize = new int[2];
    private int[] maxSize = new int[2];

    public GUIPanel(GUIMenu parentMenu, int[] relativePos, int[] preferredSize, int[] minSize, int[] maxSize) {
        this.parentMenu = parentMenu;
        this.relativePos = relativePos;
        this.preferredSize = preferredSize;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }
}
