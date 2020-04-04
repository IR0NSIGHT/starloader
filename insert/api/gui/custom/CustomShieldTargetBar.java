//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.gui.custom;

import javax.vecmath.Vector2f;

import api.entity.Entity;
import api.main.GameClient;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.FillableHorizontalBar;
import org.schema.game.client.view.gui.shiphud.newhud.GUIPosition;
import org.schema.game.client.view.gui.shiphud.newhud.TargetPowerBar;
import org.schema.game.client.view.gui.shiphud.newhud.TargetShipHPBar;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.ShieldLocalAddOn;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.InputState;

import java.awt.*;

public abstract class CustomShieldTargetBar extends FillableHorizontalBar {
    @ConfigurationElement(
            name = "Color"
    )
    public static Vector4i COLOR = new Vector4i(0, 255, 109, 255);
    @ConfigurationElement(
            name = "Offset"
    )
    public static Vector2f OFFSET = new Vector2f(100,100);
    public static boolean FLIPX = false;
    @ConfigurationElement(
            name = "FlipY"
    )
    public static boolean FLIPY = false;
    @ConfigurationElement(
            name = "FillStatusTextOnTop"
    )
    public static boolean FILL_ON_TOP = true;
    @ConfigurationElement(
            name = "TextPos"
    )
    public static Vector2f TEXT_POS = new Vector2f(120,120);
    @ConfigurationElement(
            name = "TextDescPos"
    )
    public static Vector2f TEXT_DESC_POS = new Vector2f(140,140);

    @Override
    public void draw() {
        if(selectedController != null) {
            super.draw();
        }
    }


    @Override
    public void onInit() {

        super.onInit();
    }

    public Vector2f getTextPos() {
        return TEXT_POS;
    }

    public Vector2f getTextDescPos() {
        return TEXT_DESC_POS;
    }
    public  SegmentController selectedController;
    public void setEntity(Entity e){
        if(e == null){
            selectedController = null;
        }else {
            this.selectedController = e.internalEntity;
        }
    }
    
    public CustomShieldTargetBar() {
        super(GameClient.getClientState());
    }
    public abstract void onUpdate();

    @Override
    public void update(Timer timer) {
        super.update(timer);
        onUpdate();
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
        SimpleTransformableSendableObject var1;
        if (this.selectedController != null && ((ManagedSegmentController)this.selectedController).getManagerContainer() instanceof ShieldContainerInterface) {
            ManagerContainer var3;
            ShieldContainerInterface var2 = (ShieldContainerInterface)(var3 = ((ManagedSegmentController)this.selectedController).getManagerContainer());
            if (var3.isUsingPowerReactors()) {
                ShieldLocalAddOn var4;
                return (var4 = var2.getShieldAddOn().getShieldLocalAddOn()).isAtLeastOneActive() ? var4.getLastHitShield().getPercentOne() : 0.0F;
            } else {
                return var2.getShieldAddOn().getPercentOne();
            }
        } else {
            return 0.0F;
        }
    }

    public String getText() {
        if (this.selectedController != null && this.selectedController instanceof ManagedSegmentController && ((ManagedSegmentController)this.selectedController).getManagerContainer() instanceof ShieldContainerInterface) {
            ManagerContainer var3;
            ShieldContainerInterface var2 = (ShieldContainerInterface)(var3 = ((ManagedSegmentController)this.selectedController).getManagerContainer());
            if (var3.isUsingPowerReactors()) {
                ShieldLocalAddOn var4;
                if ((var4 = var2.getShieldAddOn().getShieldLocalAddOn()).isAtLeastOneActive()) {
                    ShieldLocal var5 = var4.getLastHitShield();
                    return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIELDBAR_2, new Object[]{var5.getPosString()}) + StringTools.massFormat(var5.getShields()) + " / " + StringTools.massFormat(var5.getShieldCapacity());
                } else {
                    return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIELDBAR_1;
                }
            } else {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIELDBAR_0 + StringTools.massFormat(var2.getShieldAddOn().getShields()) + " / " + StringTools.massFormat(var2.getShieldAddOn().getShieldCapacity());
            }
        } else {
            return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETSHIELDBAR_3;
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
        return "TargetShieldBar";
    }
}
