package api.utils;

import org.schema.game.common.controller.ElementCountMap;
import org.schema.game.common.data.element.ElementKeyMap;

public class StaticPlayground {
    public static ElementCountMap getRawResources(ElementCountMap blockMap){
        ElementCountMap resourceMap = new ElementCountMap();
        for (short type : ElementKeyMap.keySet) {
            int count = blockMap.get(type);
            if (count > 0 && ElementKeyMap.isValidType(type)) {
                ElementCountMap infoRawBlocks = new ElementCountMap(ElementKeyMap.getInfo(type).getRawBlocks());
                infoRawBlocks.mult(count);
                resourceMap.add(infoRawBlocks);
            }
        }
        return resourceMap;
    }
}
