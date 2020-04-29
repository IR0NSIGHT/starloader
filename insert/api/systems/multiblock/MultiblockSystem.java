package api.systems.multiblock;

import api.element.block.Blocks;
import java.util.ArrayList;

public class MultiblockSystem {

    private String controllerName;
    private ArrayList<Blocks[][][]> layouts;

    public MultiblockSystem(String controllerName) {
        this.controllerName = controllerName;
    }

    public void setLayout(int level, Blocks[][][] layout) {
        layouts.set(level, layout);
    }
}
