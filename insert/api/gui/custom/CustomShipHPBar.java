//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.gui.custom;

import javax.vecmath.Vector2f;

import api.entity.Entity;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.FillableHorizontalBar;
import org.schema.game.client.view.gui.shiphud.newhud.GUIPosition;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SegmentControllerHpControllerInterface;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.InputState;

public abstract class CustomShipHPBar extends FillableHorizontalBar {
    public static Vector4i COLOR = new Vector4i(40,255,10, 255);
    public static Vector2f OFFSET = new Vector2f(200,200);
    public static boolean FLIPX = false;
    public static boolean FLIPY = false;
    public static boolean FILL_ON_TOP = true;
    public static Vector2f TEXT_POS = new Vector2f(200,200);
    public static Vector2f TEXT_DESC_POS = new Vector2f(220,220);;

    public Vector2f getTextPos() {
        return TEXT_POS;
    }

    public Vector2f getTextDescPos() {
        return TEXT_DESC_POS;
    }
    public SegmentController entity = null;
    public CustomShipHPBar(InputState var1) {
        super(var1);
    }

    public boolean isBarFlippedX() {
        return FLIPX;
    }

    public boolean isBarFlippedY() {
        return FLIPY;
    }

    public boolean isFillStatusTextOnTop() {
        return FILL_ON_TOP;
    }

    public float getFilled() {
        if(this.entity != null) {
            return (float) this.entity.getHpController().getHpPercent();
        }else{
            return 0;
        }
    }

    @Override
    public void onInit() {
        super.onInit();
        getPos().set(200,200, 1F);
    }

    public abstract void onUpdate();

    @Override
    public void update(Timer timer) {
        super.update(timer);
        onUpdate();

    }

    public String getText() {
        if(this.entity != null){
            SegmentController var3;
            SegmentControllerHpControllerInterface var2 = (var3 = this.entity).getHpController();
            return (var3.isUsingPowerReactors() ? Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIPHPBAR_2 : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIPHPBAR_0) + StringTools.massFormat(var2.getHp()) + " / " + StringTools.massFormat(var2.getMaxHp());
        } else {
            return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIPHPBAR_1;
        }
    }

    public Vector4i getConfigColor() {
        return COLOR;
    }

    public GUIPosition getConfigPosition() {
        return null;
    }

    public Vector2f getConfigOffset() {
        return OFFSET;
    }

    protected String getTag() {
        return "TargetStructureHPBar";
    }

    public void setEntity(Entity currentEntity){
        if(currentEntity == null){
            entity = null;
        }else {
            this.entity = currentEntity.internalEntity;
        }
    }
}
