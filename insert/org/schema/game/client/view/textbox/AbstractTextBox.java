//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.textbox;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import api.listener.events.draw.DisplayModuleDrawEvent;
import api.mod.StarLoader;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.view.SegmentDrawer.TextBoxSeg;
import org.schema.game.client.view.SegmentDrawer.TextBoxSeg.TextBoxElement;
import org.schema.game.client.view.textbox.Replacements.Type;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.DrawableScene;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.shader.Shader;
import org.schema.schine.graphicsengine.shader.Shaderable;
import org.schema.schine.network.client.ClientState;

public class AbstractTextBox implements Drawable, Shaderable {
    Matrix3f mY = new Matrix3f();
    Matrix3f mYB = new Matrix3f();
    Matrix3f mYC = new Matrix3f();
    Matrix3f mX = new Matrix3f();
    Matrix3f mXB = new Matrix3f();
    Matrix3f mXC = new Matrix3f();
    private GUIOverlay bg;
    private static final float SCALE = -0.00395F;
    private float t;
    private final ClientState state;
    private boolean init;
    private int maxTextDistance;

    public AbstractTextBox(ClientState var1) {
        this.state = var1;
        this.mY.setIdentity();
        this.mY.rotY(1.5707964F);
        this.mYB.setIdentity();
        this.mYB.rotY(-1.5707964F);
        this.mYC.setIdentity();
        this.mYC.rotY(3.1415927F);
        this.mX.setIdentity();
        this.mX.rotX(1.5707964F);
        this.mXB.setIdentity();
        this.mXB.rotX(-1.5707964F);
        this.mXC.setIdentity();
        this.mXC.rotX(3.1415927F);
    }

    public void update(Timer var1) {
        this.t += var1.getDelta() * 2.0F;
    }

    private void draw(TextBoxElement var1) {
        if (Controller.getCamera().isBoundingSphereInFrustrum(var1.worldpos.origin, 2.0F)) {
            float var10000 = Vector3fTools.diffLength(Controller.getCamera().getPos(), var1.worldpos.origin);
            float var2 = 0.0F;
            if (var10000 <= (float)this.maxTextDistance) {

                GlUtil.glPushMatrix();
                GlUtil.glMultMatrix(var1.worldpos);
                GlUtil.scaleModelview(-0.00395F, -0.00395F, -0.00395F);
                var1.text.setPos(8.0F, 8.0F, 0.1F);
                //INSERTED CODE @ ???
                DisplayModuleDrawEvent event = new DisplayModuleDrawEvent(var1, this);
                StarLoader.fireEvent(event, false);
                ///
                try {
                    var1.text.draw();
                } catch (NullPointerException var4) {
                    Object var6 = null;
                    var4.printStackTrace();
                    Iterator var5 = var1.text.getText().iterator();

                    while(var5.hasNext()) {
                        var6 = var5.next();

                        try {
                            System.err.println("PRINTING TEXT: " + var6);
                        } catch (Exception var3) {
                            var3.printStackTrace();
                        }
                    }
                }

                GlUtil.glPopMatrix();
            }
        }
    }

    public void onExit() {
    }

    public void updateShader(DrawableScene var1) {
    }

    public void updateShaderParameters(Shader var1) {
        GlUtil.updateShaderFloat(var1, "uTime", this.t);
        GlUtil.updateShaderVector2f(var1, "uResolution", 20.0F, 1000.0F);
        GlUtil.updateShaderInt(var1, "uDiffuseTexture", 0);
    }

    public void draw(TextBoxSeg var1) {
        this.maxTextDistance = (Integer)EngineSettings.MAX_DISPLAY_MODULE_TEXT_DRAW_DISTANCE.getCurrentState();

        int var2;
        TextBoxElement var3;
        for(var2 = 0; var2 < var1.pointer; ++var2) {
            SegmentController var4 = (var3 = var1.v[var2]).c;
            long var5 = var3.v;
            String var7;
            if ((var7 = (String)var4.getTextMap().get(var5)) == null) {
                var3.color.set(1.0F, 1.0F, 1.0F, 1.0F);
                var3.font = FontSize.MEDIUM.getFont();
                var3.replacements.clear();
                var3.rawText = "loading...";
                var3.realText = "loading...";
            } else if (!var7.equals(var3.rawText)) {
                var3.rawText = var7;
                var3.realText = var7;
                String var8 = var7;
                var3.color.set(1.0F, 1.0F, 1.0F, 1.0F);
                var3.font = FontSize.MEDIUM.getFont();
                var3.replacements.clear();

                try {
                    int var9;
                    int var12;
                    if (var8.startsWith("<style>") && (var9 = var8.indexOf("</style>")) > 0) {
                        var3.realText = var8.substring(var9 + 8);
                        if (var3.realText.startsWith("\n")) {
                            var3.realText = var8.substring(var9 + 9);
                        }

                        String[] var19;
                        int var11 = (var19 = var8.substring(7, var9).split(",")).length;

                        for(var12 = 0; var12 < var11; ++var12) {
                            String[] var14;
                            if ((var14 = var19[var12].split("=")).length == 2) {
                                if (var14[0].equals("c")) {
                                    Color var16 = Color.decode(var14[1]);
                                    var3.color.set((float)var16.getRed() / 255.0F, (float)var16.getGreen() / 255.0F, (float)var16.getBlue() / 255.0F, (float)var16.getAlpha() / 255.0F);
                                } else if (var14[0].equals("f")) {
                                    switch(Integer.parseInt(var14[1])) {
                                        case 0:
                                            var3.font = FontLibrary.getBoldArial16WhiteNoOutline();
                                            break;
                                        case 1:
                                            var3.font = FontLibrary.getBoldArial18NoOutline();
                                            break;
                                        case 2:
                                            var3.font = FontLibrary.getBoldArial20WhiteNoOutline();
                                    }
                                }
                            }
                        }
                    }

                    List var20 = StringTools.tokenize(var3.realText, "[", "]");
                    StringBuffer var10 = new StringBuffer(var3.realText);
                    Iterator var17 = var20.iterator();

                    while(true) {
                        if (!var17.hasNext()) {
                            var3.buffer = var10;
                            var3.realText = var10.toString();
                            break;
                        }

                        String var21 = (String)var17.next();
                        Type[] var23;
                        var12 = (var23 = Type.values()).length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                            Type var24 = var23[var13];
                            this.check(var21, var10, var24, var3, var4);
                        }
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                    var3.realText = "style error!";
                }
            }

            var3.text.setFont(var3.font);
            var3.text.setColor(var3.color);
            if (var3.replacements.size() > 0) {
                for(int var18 = 0; var18 < var3.replacements.size(); ++var18) {
                    String var22 = ((Replacement)var3.replacements.get(var18)).get();
                    var3.realText = var3.realText.replaceFirst(Pattern.quote("%&$"), var22);
                }
            }

            var3.text.getText().set(0, var3.realText != null ? var3.realText : "<ERROR>");
            if (var7 == null) {
                ((ClientSegmentProvider)var4.getSegmentProvider()).getSendableSegmentProvider().clientTextBlockRequest(var5);
            }

            ElementCollection.getPosFromIndex(var5, var3.posBuffer);
            Vector3f var10000 = var3.posBuffer;
            var10000.x -= 16.0F;
            var10000 = var3.posBuffer;
            var10000.y -= 16.0F;
            var10000 = var3.posBuffer;
            var10000.z -= 16.0F;
            var3.worldpos.basis.setIdentity();
            var3.worldpos.basis.set(var4.getWorldTransformOnClient().basis);
            switch(ElementCollection.getType(var5)) {
                case 0:
                    var3.worldpos.basis.mul(this.mYC);
                    var10000 = var3.posBuffer;
                    var10000.x -= 0.5F;
                    var10000 = var3.posBuffer;
                    var10000.y += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z += 0.51F;
                    break;
                case 1:
                    var10000 = var3.posBuffer;
                    var10000.x += 0.5F;
                    var10000 = var3.posBuffer;
                    var10000.y += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z -= 0.51F;
                    break;
                case 2:
                    var3.worldpos.basis.mul(this.mX);
                    var10000 = var3.posBuffer;
                    var10000.x += 0.5F;
                    var10000 = var3.posBuffer;
                    var10000.y += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z += 0.51F;
                    break;
                case 3:
                    var3.worldpos.basis.mul(this.mYC);
                    var3.worldpos.basis.mul(this.mXB);
                    var10000 = var3.posBuffer;
                    var10000.x -= 0.5F;
                    var10000 = var3.posBuffer;
                    var10000.y -= 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z += 0.51F;
                    break;
                case 4:
                    var3.worldpos.basis.mul(this.mY);
                    var10000 = var3.posBuffer;
                    var10000.x -= 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.y += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z -= 0.5F;
                    break;
                case 5:
                    var3.worldpos.basis.mul(this.mYB);
                    var10000 = var3.posBuffer;
                    var10000.x += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.y += 0.51F;
                    var10000 = var3.posBuffer;
                    var10000.z += 0.5F;
            }

            var4.getWorldTransformOnClient().transform(var3.posBuffer);
            var3.worldpos.origin.set(var3.posBuffer);
        }

        if (var1.pointer > 0) {
            this.bg.getSprite().setBillboard(false);
            Sprite.draw3D(this.bg.getSprite(), var1.v, var1.pointer, Controller.getCamera());
        }


        for(var2 = 0; var2 < var1.pointer; ++var2) {
            SegmentController var25 = (var3 = var1.v[var2]).c;
            this.draw(var3);
            if (var3.replacements.size() > 0) {
                var3.realText = var3.buffer.toString();
            }
        }

    }

    private void check(String var1, StringBuffer var2, Type var3, TextBoxElement var4, SegmentController var5) {
        if (var1.toLowerCase(Locale.ENGLISH).equals("password")) {
            var2.delete(0, var2.length());
        } else {
            int var6;
            if (var3.takesIndex == 0 && var1.toLowerCase(Locale.ENGLISH).equals(var3.var.toLowerCase(Locale.ENGLISH))) {
                var6 = var2.indexOf("[" + var1 + "]");
                var2.delete(var6, var6 + ("[" + var1 + "]").length());
                var2.insert(var6, "%&$");
                this.addRepl(var4, var3, var5, var6);
            } else {
                Matcher var11;
                if (var3.takesIndex > 0 && var1.toLowerCase(Locale.ENGLISH).matches(var3.var.toLowerCase(Locale.ENGLISH) + "[0-9]+") && (var11 = Pattern.compile("\\[" + var3.var.toLowerCase(Locale.ENGLISH) + "[0-9]+\\]").matcher(var2)).find()) {
                    var6 = var11.start();
                    String var7;
                    int var8 = (var7 = var2.substring(var6, var11.end())).toLowerCase(Locale.ENGLISH).indexOf(var3.var.toLowerCase(Locale.ENGLISH)) + var3.var.length();
                    int var9 = var7.indexOf("]");

                    try {
                        int var12 = Integer.parseInt(var7.substring(var8, var9));
                        var2.delete(var6, var11.end());
                        var2.insert(var6, "%&$");
                        this.addRepl(var4, var3, var5, var6, var12);
                        return;
                    } catch (NumberFormatException var10) {
                        System.err.println("VAR: " + var3.name() + "; " + var3.var);
                        var10.printStackTrace();
                    }
                }

            }
        }
    }

    private void addRepl(TextBoxElement var1, Type var2, SegmentController var3, int var4) {
        this.addRepl(var1, var2, var3, var4, 0);
    }

    private void addRepl(TextBoxElement var1, final Type var2, final SegmentController var3, int var4, int var5) {
        if (var2.fac.ok(var3)) {
            var1.replacements.add(new Replacement(var4, var5) {
                public String get() {
                    try {
                        return var2.fac.getValue(var3, this.index);
                    } catch (Exception var2x) {
                        var2x.printStackTrace();
                        return "ERROR(" + var2x.getClass().getSimpleName() + ")";
                    }
                }
            });
        }

    }

    public void cleanUp() {
    }

    public void draw() {
    }

    public boolean isInvisible() {
        return false;
    }

    public void onInit() {
        if (!this.init) {
            this.bg = new GUIOverlay(Controller.getResLoader().getSprite("screen-gui-"), this.state);
            this.init = true;
        }
    }
}
