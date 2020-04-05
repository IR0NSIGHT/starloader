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

public abstract class CustomHudBar extends FillableHorizontalBar {
    public Vector4i COLOR = new Vector4i(0, 255, 109, 255);
    public static Vector2f OFFSET = new Vector2f(100,100);
    public static boolean FLIPX = false;
    public static boolean FLIPY = false;
    public static boolean FILL_ON_TOP = true;
    public Vector2f TEXT_POS = new Vector2f(210,-7);
    public Vector2f TEXT_DESC_POS = new Vector2f(0,-7);

    public abstract boolean drawBar();

    @Override
    public void draw() {
        if(drawBar()){
            super.draw();
        }
    }

    @Override
    public void onInit() {
        create();
        super.onInit();
    }

    public abstract void create();

    public void setTextPosition(int x, int y){
        TEXT_DESC_POS = new Vector2f(x, y);
    }
    public void setPercentPosition(int x, int y){
        TEXT_POS = new Vector2f(x, y);
    }

    public Vector2f getTextPos() {
        return TEXT_POS;
    }

    public Vector2f getTextDescPos() {
        return TEXT_DESC_POS;
    }

    public CustomHudBar() {
        super(GameClient.getClientState());
        setGlowIntensity(500);
    }
    public abstract void onUpdate();

    @Override
    public void update(Timer timer) {
        super.update(timer);
        onUpdate();
    }

    public void setColor(int r, int g, int b, int a) {
        this.COLOR.set(r,g,b,a);
    }
    public void setColor(java.awt.Color color) {
        this.COLOR.set(color.getRed(),color.getGreen(),color.getBlue(),255);
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


    public abstract float getFilled();

    public abstract String getText();

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
