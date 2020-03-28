package api.element.block;

import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;

import javax.xml.parsers.ParserConfigurationException;

public class Block {

    private ElementInformation blockInfo;

    Block(ElementInformation blockInfo) {
        this.blockInfo = blockInfo;
    }

    public Block() {

    }

    public void registerBlock(ElementInformation blockInfo) {
        this.blockInfo = blockInfo;

    }

    private void registerBlock() throws ParserConfigurationException {
        ElementKeyMap.add(blockInfo.id, blockInfo);
    }
}