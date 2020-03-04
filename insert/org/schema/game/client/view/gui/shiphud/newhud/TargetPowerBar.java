package org.schema.game.client.view.gui.shiphud.newhud;

import javax.vecmath.Vector2f;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.power.PowerManagerInterface;
import org.schema.game.common.controller.elements.power.reactor.PowerInterface;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;

import java.awt.*;

public class TargetPowerBar extends FillableHorizontalBar {
    @ConfigurationElement(
            name = "Color"
    )
    public static Vector4i COLOR;
    @ConfigurationElement(
            name = "ColorWarn"
    )
    public static Vector4i COLOR_WARN;
    @ConfigurationElement(
            name = "Offset"
    )
    public static Vector2f OFFSET;
    @ConfigurationElement(
            name = "FlipX"
    )
    public static boolean FLIPX;
    @ConfigurationElement(
            name = "FlipY"
    )
    public static boolean FLIPY;
    @ConfigurationElement(
            name = "FillStatusTextOnTop"
    )
    public static boolean FILL_ON_TOP;
    @ConfigurationElement(
            name = "TextPos"
    )
    public static Vector2f TEXT_POS;
    @ConfigurationElement(
            name = "TextDescPos"
    )
    public static Vector2f TEXT_DESC_POS;
    private float currentUsage;

    public Vector2f getTextPos() {
        return TEXT_POS;
    }

    public Vector2f getTextDescPos() {
        return TEXT_DESC_POS;
    }

    public TargetPowerBar(InputState var1) {
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
        SimpleTransformableSendableObject var1;
        if ((var1 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null && var1 instanceof SegmentController && var1 instanceof ManagedSegmentController && ((ManagedSegmentController)var1).getManagerContainer() instanceof PowerManagerInterface) {
            ManagerContainer var2;
            if ((var2 = ((ManagedSegmentController)var1).getManagerContainer()).isUsingPowerReactors()) {
                this.currentUsage = (float)var2.getPowerInterface().getPowerConsumptionAsPercent();
                return this.currentUsage;
            } else {
                return ((PowerManagerInterface)((ManagedSegmentController)var1).getManagerContainer()).getPowerAddOn().getPercentOne();
            }
        } else {
            return 0.0F;
        }
    }

    public void draw() {
        if (this.currentUsage > 0.0F) {
            Color c = new Color(224, 0, 255);
            this.getColor().set((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)COLOR_WARN.w / 255.0F);
        } else {
            this.getColor().set((float)this.getConfigColor().x / 255.0F, (float)this.getConfigColor().y / 255.0F, (float)this.getConfigColor().z / 255.0F, (float)this.getConfigColor().w / 255.0F);
        }

        super.draw();
    }

    public String getText() {
        SimpleTransformableSendableObject var1;
        if ((var1 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null && var1 instanceof SegmentController && var1 instanceof ManagedSegmentController && ((ManagedSegmentController)var1).getManagerContainer() instanceof PowerManagerInterface) {
            ManagerContainer var2;
            if ((var2 = ((ManagedSegmentController)var1).getManagerContainer()).isUsingPowerReactors()) {
                PowerInterface var4 = var2.getPowerInterface();
                return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPOWERBAR_3, new Object[]{StringTools.formatPointZero(var4.getPowerConsumptionAsPercent() * 100.0D)});
            } else {
                PowerManagerInterface var3 = (PowerManagerInterface)((ManagedSegmentController)var1).getManagerContainer();
                return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
                //return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPOWERBAR_0 + StringTools.massFormat(var3.getPowerAddOn().getPower()) + " / " + StringTools.massFormat(var3.getPowerAddOn().getMaxPower());
            }
        } else {
            return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPOWERBAR_1;
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
        return "TargetPowerBar";
    }
}