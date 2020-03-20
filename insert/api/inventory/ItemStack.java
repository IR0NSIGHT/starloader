package api.inventory;

import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.data.admin.AdminCommandQueueElement;

import java.util.ArrayList;

public class ItemStack {
    private short id;
    private String name;
    private ElementInformation info;
    private int amount;

    public ItemStack(short id){
        this.id = id;
        this.info = ElementKeyMap.getInfo(id);
        this.name = info.getName();
    }
    public ItemStack(String name){
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

    public ItemStack setAmount(int amount) {
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

    public static ArrayList<ItemStack> getAllRawResources(){
        ArrayList<ItemStack> ret = new ArrayList<>();
        for (short type : ElementKeyMap.typeList()) {
            ElementInformation info = ElementKeyMap.getInfo(type);
            String name = info.getName().toLowerCase();
            if(name.contains("ore raw") || name.contains("shard raw")){
                ret.add(new ItemStack(type));
            }
        }
        return ret;
    }
}
