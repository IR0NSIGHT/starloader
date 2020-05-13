//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example;

import java.util.Iterator;

import api.ModPlayground;
import api.element.block.Blocks;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer.Hos;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.BlockKillInterface;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.ElementCollectionManager.CollectionShape;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;

public class BatteryCollectionManager extends ElementCollectionManager<BatteryUnit, BatteryCollectionManager, BatteryElementManager> implements PlayerUsableInterface, BlockKillInterface {
    private float totalThrust;
    private float totalThrustRaw;

    public BatteryCollectionManager(SegmentController var1, BatteryElementManager var2) {
        super(ModPlayground.newCapId, var1, var2);
    }

    public int getMargin() {
        return 0;
    }

    protected Class<BatteryUnit> getType() {
        return BatteryUnit.class;
    }

    public boolean needsUpdate() {
        return false;
    }

    public BatteryUnit getInstance() {
        return new BatteryUnit();
    }

    public void onLogicActivate(SegmentPiece var1, boolean var2, Timer var3) {
    }

    protected void onChangedCollection() {

    }

    public void handleMouseEvent(ControllerStateUnit var1, MouseEvent var2) {
    }

    public boolean isDetailedElementCollections() {
        return false;
    }

    public GUIKeyValueEntry[] getGUICollectionStats() {
        return new GUIKeyValueEntry[]{
                new ModuleValueEntry("Total Size", this.getTotalSize())};
    }

    public String getModuleName() {
        return "PowCap System";
    }

    public float getSensorValue(SegmentPiece var1) {
        return 1.0F;
    }

    public WeaponRowElementInterface getWeaponRow() {
        return null;
    }

    public boolean isControllerConnectedTo(long var1, short var3) {
        return true;
    }

    public boolean isPlayerUsable() {
        return true;
    }

    public long getUsableId() {
        //If you set this to an existing usable id, it will BREAK the original
        return Blocks.FERTIKEEN_INGOT.getPlayerUsableId();
    }

    public void handleControl(ControllerStateInterface var1, Timer var2) {
        this.getElementManager().handle(var1, var2);
    }

    public CollectionShape requiredNeigborsPerBlock() {
        return CollectionShape.ALL_IN_ONE;
    }

    public void onKilledBlock(long var1, short var3, Damager var4) {
        this.checkIntegrity(var1, var3, var4);
    }

    public void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2) {
    }

    public void addHudConext(ControllerStateUnit var1, HudContextHelpManager var2, Hos var3) {
    }
}
