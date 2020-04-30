package api.systems.multiblock;

import java.util.Map;

public class MultiblockSystem {

    private String controllerName;
    private char[][][] layout;
    public Map<Character, String> blocks;

    public MultiblockSystem(String controllerName) {
        this.controllerName = controllerName;
    }

    public void setLayout(char[][][] charLayout) {
        this.layout = layout;
    }

    public void setBlock(char character, String blockName) {
        blocks.put(character, blockName);
    }
}