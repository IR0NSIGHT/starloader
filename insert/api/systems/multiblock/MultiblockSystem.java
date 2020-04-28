package api.systems.multiblock;

import api.element.block.Block;
import org.schema.common.util.linAlg.Vector3i;

public class MultiblockSystem {

    private Block controller;
    private String[][][] blockLayout;
    private Vector3i minPos;
    private Vector3i maxPos;
    private int length = maxPos.z - minPos.z;
    private int width = maxPos.x - minPos.x;
    private int height = maxPos.y - minPos.y;

    public MultiblockSystem(Block controller) {
        this.controller = controller;
    }

    public void setLayout(String[][][] blockLayout) {
        this.blockLayout = blockLayout;
    }

    public boolean checkLayout() {
        String[][][] layout = new String[height][length][width];
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    String ID = "" + controller.getEntity().getBlockAt(minPos.x + x, minPos.y + y, minPos.z + z).getId();
                    if (!blockLayout[y][z][x].contains(ID)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void test() {
        //BlockIDs:
        //CoilBlock = 1
        //CoilCharger = 2
        //HeatAbsorber = 3
        //HeatReflector = 4
        //ControllerBlock = 5
        String[][][] multiBlockLayout =
                {
                        {   //Layer 1
                                {"0", "0", "0/2/5", "0", "0"},
                                {"0", "3", "3", "3", "0"},
                                {"0/2/5", "3", "3", "3", "0/2/5"},
                                {"0", "3", "3", "3", "0"},
                                {"0", "0", "0/2/5", "0", "0"}
                        },
                        {   //Layer 2
                                {"1", "1", "1/2", "1", "1"},
                                {"1", "3", "3", "3", "1"},
                                {"1/2", "3", "3", "3", "1/2"},
                                {"1", "3", "3", "3", "1"},
                                {"1", "1", "1/2", "1", "1"}
                        }
                };
    }
}
