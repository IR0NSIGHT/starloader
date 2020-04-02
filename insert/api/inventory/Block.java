package api.inventory;

import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import java.util.ArrayList;

public class Block {

    private short id;
    private String name;
    private ElementInformation info;
    private int amount;

    public Block(short id) {
        this.id = id;
        this.info = ElementKeyMap.getInfo(id);
        this.name = info.getName();
    }

    public Block(String name){
        for (short type : ElementKeyMap.typeList()) {
            ElementInformation info = ElementKeyMap.getInfo(type);
            if(info.getName().toLowerCase().contains(name.toLowerCase())){
                this.id = type;
                this.info = info;
                break;
            }
        }
        this.name = name;
    }

    public Block setAmount(int amount) {
        this.amount = amount;
        return this;

    }

    public short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ElementInformation getInfo() {
        return info;
    }

    public int getAmount() {
        return amount;
    }

    public static ArrayList<Block> getAllRawResources(){
        ArrayList<Block> ret = new ArrayList<>();
        for (short type : ElementKeyMap.typeList()) {
            ElementInformation info = ElementKeyMap.getInfo(type);
            String name = info.getName().toLowerCase();
            if(name.contains("ore raw") || name.contains("shard raw")){
                ret.add(new Block(type));
            }
        }
        return ret;
    }
}
