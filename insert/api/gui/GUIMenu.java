package api.gui;

public class GUIMenu {

    private char hotKey;
    private boolean hasHotKey;
    private String name;
    private int[] relativePos = new int[2];
    private int[] preferredSize = new int[2];
    private int[] minSize = new int[2];
    private int[] maxSize = new int[2];

    public GUIMenu(String name, int[] relativePos, int[] preferredSize, int[] minSize, int[] maxSize) {
        this.name = name;
        this.relativePos = relativePos;
        this.preferredSize = preferredSize;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.hasHotKey = false;
    }

    public GUIMenu(String name, int[] relativePos, int[] preferredSize, int[] minSize, int[] maxSize, char hotKey) {
        this.name = name;
        this.relativePos = relativePos;
        this.preferredSize = preferredSize;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.hotKey = hotKey;
        this.hasHotKey = true;
    }

}
