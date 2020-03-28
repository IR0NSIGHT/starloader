package api.element.block;

import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;

public enum Blocks {

    SHIP_CORE(1),
    REACTOR(1008),
    STABILIZER(1009);

    private ElementInformation blockInfo;

    private Blocks(int id) {
        Block block = new Block(ElementKeyMap.getInfo(id));
    }

    public ElementInformation getInfo() {
        return blockInfo;
    }
}
