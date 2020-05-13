//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.data.element.ElementCollection;

public class BatteryUnit extends ElementCollection<BatteryUnit, BatteryCollectionManager, BatteryElementManager> {
    private final Long2LongOpenHashMap lastElements = new Long2LongOpenHashMap();
    float thrust;

    public BatteryUnit() {
    }

    public void addElement(long var1, int var3, int var4, int var5) {
        super.addElement(var1, var3, var4, var5);
        this.onAdd(var1, var3, var4, var5);
    }

    protected void significatorUpdate(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
        this.significatorUpdateMin(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
    }

    public ControllerManagerGUI createUnitGUI(GameClientState var1, ControlBlockElementCollectionManager<?, ?, ?> var2, ControlBlockElementCollectionManager<?, ?, ?> var3) {
        return this.elementCollectionManager.getElementManager().getGUIUnitValues(this, this.elementCollectionManager, var2, var3);
    }

    public Long2LongOpenHashMap getLastElements() {
        return this.lastElements;
    }

    protected void onAdd(long var1, int var3, int var4, int var5) {
        long var6 = ElementCollection.getIndex(var3, var4, 0);
        if (!this.getLastElements().containsKey(var6) || var5 < ElementCollection.getPosZ(this.getLastElements().get(var6))) {
            this.getLastElements().put(var6, var1);
        }

    }

}
