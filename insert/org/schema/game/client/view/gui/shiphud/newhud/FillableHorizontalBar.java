//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui.shiphud.newhud;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import org.schema.common.util.StringTools;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.ShaderLibrary;
import org.schema.schine.graphicsengine.shader.Shaderable;
import org.schema.schine.input.InputState;

public abstract class FillableHorizontalBar extends HudConfig implements Shaderable {
    protected GUITextOverlay textDesc;
    private float glowIntensity = 0.0F;
    private Sprite barSprite;
    private Vector4f color;
    private GUITextOverlay text;

    public FillableHorizontalBar(InputState var1) {
        super(var1);
    }

    public abstract boolean isBarFlippedX();

    public abstract boolean isBarFlippedY();

    public abstract boolean isFillStatusTextOnTop();

    public abstract Vector2f getTextPos();

    public abstract Vector2f getTextDescPos();

    public void cleanUp() {
    }

    public void draw() {
        GlUtil.glPushMatrix();
        this.transform();
        this.text.getText().set(0, StringTools.formatPointZero(this.getFilled() * 100.0F) + "%");
        this.textDesc.getText().set(0, this.getText());
        if (this.isLongerBar()) {
            this.textDesc.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.text.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.text.setPos(this.getTextPos().x, this.getTextPos().y, 0.0F);
            this.textDesc.setPos(this.getTextDescPos().x, this.getTextDescPos().y, 0.0F);
        } else {
            this.textDesc.setColor(this.getColor());
            this.text.setColor(this.getColor());
            this.text.setPos(this.getTextPos().x, this.getTextPos().y, 0.0F);
            this.textDesc.setPos(this.getTextDescPos().x, this.getTextDescPos().y, 0.0F);
        }

        ShaderLibrary.powerBarShaderHor.setShaderInterface(this);
        ShaderLibrary.powerBarShaderHor.load();
        this.barSprite.draw();
        ShaderLibrary.powerBarShaderHor.unload();
        this.text.draw();
        this.textDesc.draw();
        GlUtil.glPopMatrix();
    }

    public void onInit() {
        if (this.isLongerBar()) {
            this.barSprite = Controller.getResLoader().getSprite("HUD_PlayerHP-1x4-gui-");
        } else {
            this.barSprite = Controller.getResLoader().getSprite("HUD_HorizontalBar-1x4-gui-");
        }

        this.barSprite.setSelectedMultiSprite(1);
        this.width = (float)this.barSprite.getWidth();
        this.height = (float)this.barSprite.getHeight();
        this.color = new Vector4f((float)this.getConfigColor().x / 255.0F, (float)this.getConfigColor().y / 255.0F, (float)this.getConfigColor().z / 255.0F, (float)this.getConfigColor().w / 255.0F);
        this.text = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium14(), this.getState());
        this.textDesc = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium14(), this.getState());
        this.text.setTextSimple("n/a");
        this.textDesc.setTextSimple("n/a");
    }

    protected boolean isLongerBar() {
        return false;
    }

    public void onExit() {
        GlUtil.glActiveTexture(33984);
        GlUtil.glBindTexture(3553, 0);
    }

    public void updateShader(DrawableScene var1) {
    }

    public void updateShaderParameters(Shader var1) {
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.glActiveTexture(33984);
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.glBindTexture(3553, this.barSprite.getMaterial().getTexture().getTextureId());
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderInt(var1, "barTex", 0);
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderFloat(var1, "filled", this.getFilled());
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderFloat(var1, "glowIntensity", this.glowIntensity);
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderBoolean(var1, "flippedX", this.isBarFlippedX());
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderBoolean(var1, "flippedY", this.isBarFlippedY());
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderFloat(var1, "minTexCoord", 0.005F);
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderFloat(var1, "maxTexCoord", 0.995F);
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        GlUtil.updateShaderVector4f(var1, "barColor", this.getColor());
        if (var1.recompiled) {
            GlUtil.printGlErrorCritical();
        }

        var1.recompiled = false;
    }

    public final Vector4f getColor() {
        return this.color;
    }

    public abstract float getFilled();

    public abstract String getText();

    public float getGlowIntensity() {
        return this.glowIntensity;
    }

    public void setGlowIntensity(float var1) {
        this.glowIntensity = var1;
    }
}
