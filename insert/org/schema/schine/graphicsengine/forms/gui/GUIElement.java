//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.graphicsengine.forms.gui;

import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observer;
import javax.vecmath.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.schema.common.util.linAlg.TransformTools;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.schine.graphicsengine.OculusVrHelper;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Drawable;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.Positionable;
import org.schema.schine.graphicsengine.forms.Scalable;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIAbstractNewScrollBar;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIActivatableTextBar;
import org.schema.schine.input.InputState;
import org.schema.schine.input.Mouse;

public abstract class GUIElement extends AbstractSceneNode implements Drawable, Positionable, Scalable {
    public static final int RENDER_MODE_NORMAL = 1;
    public static final int RENDER_MODE_SHADOW = 2;
    public static final int ORIENTATION_NONE = 0;
    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;
    public static final int ORIENTATION_TOP = 4;
    public static final int ORIENTATION_BOTTOM = 8;
    public static final int ORIENTATION_VERTICAL_MIDDLE = 16;
    public static final int ORIENTATION_HORIZONTAL_MIDDLE = 32;
    private static final long CALLBACK_DELAY_MS = 200L;
    public static int renderModeSet = 1;
    public static FloatBuffer coord = BufferUtils.createFloatBuffer(3);
    public static GUITextOverlay textOverlay;
    public static boolean deactivateCallbacks;
    private static IntBuffer viewportTemp = BufferUtils.createIntBuffer(16);
    private static DoubleBuffer leftClipPlane = BufferUtils.createDoubleBuffer(4);
    private static DoubleBuffer rightClipPlane = BufferUtils.createDoubleBuffer(4);
    private static DoubleBuffer topClipPlane = BufferUtils.createDoubleBuffer(4);
    private static DoubleBuffer bottomClipPlane = BufferUtils.createDoubleBuffer(4);
    public static boolean translateOnlyMode;
    private final Vector3f relMousePos = new Vector3f();
    public int renderMode = 1;
    private boolean inside;
    protected GUICallback callback;
    private int[] viewportMonitor = new int[4];
    private boolean mouseUpdateEnabled;
    private boolean changed = true;
    private final InputState state;
    private Object userPointer;
    private boolean wasInside;
    private static Vector4f clip = new Vector4f();
    protected static boolean debug;
    private int insideUpdate;
    public static final float x32 = 0.03125F;
    public static final float x16 = 0.0625F;

    public GUIElement(InputState var1) {
        this.state = var1;
        //INSERTED CODE @???
        api.listener.events.gui.GUIElementCreateEvent event = new api.listener.events.gui.GUIElementCreateEvent(this, var1);
        StarLoader.fireEvent(api.listener.events.gui.GUIElementCreateEvent.class, event, false);
        ///
    }

    public static void disableOrthogonal() {
        GlUtil.glMatrixMode(5889);
        GlUtil.glPopMatrix();
        GlUtil.glMatrixMode(5888);
        GlUtil.glPopMatrix();
        GlUtil.glEnable(2929);
        GlUtil.glEnable(2896);
    }

    public String generateToolTip() {
        Iterator var1 = this.getChilds().iterator();

        AbstractSceneNode var2;
        String var3;
        do {
            if (!var1.hasNext()) {
                return null;
            }
        } while(!((var2 = (AbstractSceneNode)var1.next()) instanceof GUIElement) || (var3 = ((GUIElement)var2).generateToolTip()) == null);

        return var3;
    }

    public static boolean isNewHud() {
        return true;
    }

    public static void startStandardDraw() {
        GlUtil.glEnable(3042);
        GlUtil.glBlendFunc(770, 771);
        GlUtil.glEnable(2903);
        GlUtil.glDisable(2929);
        GlUtil.glDisable(2896);
    }

    public static void endStandardDraw() {
        GlUtil.glActiveTexture(33984);
        GlUtil.glBindTexture(3553, 0);
        GlUtil.glDisable(2903);
        GlUtil.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlUtil.glDisable(3042);
        GlUtil.glEnable(2929);
    }

    public static void disableScreenProjection() {
        GlUtil.glMatrixMode(5889);
        GlUtil.glPopMatrix();
        GlUtil.glMatrixMode(5888);
        GlUtil.glPopMatrix();
    }

    public static void enableOrthogonal() {
        GlUtil.glDisable(2896);
        GlUtil.glDisable(2929);
        GlUtil.glPushMatrix();
        GlUtil.glMatrixMode(5889);
        GlUtil.glPushMatrix();
        GlUtil.glLoadIdentity();
        if (Controller.ocMode == 2) {
            GLFrame.getWidth();
            GLFrame.getHeight();
            OculusVrHelper.getScaleFactor();
            OculusVrHelper.getScaleFactor();
            GlUtil.gluOrtho2D(0.0F, (float)(GLFrame.getWidth() / 2) * OculusVrHelper.getScaleFactor(), (float)GLFrame.getHeight(), 0.0F);
        } else if (Controller.ocMode == 1) {
            GLFrame.getWidth();
            GLFrame.getHeight();
            OculusVrHelper.getScaleFactor();
            OculusVrHelper.getScaleFactor();
            GlUtil.gluOrtho2D((float)(GLFrame.getWidth() / 2) - ((float)(GLFrame.getWidth() / 2) * OculusVrHelper.getScaleFactor() - (float)(GLFrame.getWidth() / 2)), (float)GLFrame.getWidth(), (float)GLFrame.getHeight(), 0.0F);
        } else {
            GlUtil.gluOrtho2D(0.0F, (float)GLFrame.getWidth(), (float)GLFrame.getHeight(), 0.0F);
        }

        GlUtil.glMatrixMode(5888);
        GlUtil.glLoadIdentity();
        if (Controller.ocMode != 0) {
            new Transform();
            new Transform();
            Transform var0 = new Transform();
            Transform var1 = new Transform();
            float var2 = OculusVrHelper.getInterpupillaryDistance() * 0.5F;
            var0.setIdentity();
            var0.origin.set(-var2 * (float)GLFrame.getWidth() * 4.0F * (OculusVrHelper.getScaleFactor() + 0.3F), 0.0F, 0.0F);
            var1.setIdentity();
            var1.origin.set(var2 * (float)GLFrame.getWidth() * 4.0F * (OculusVrHelper.getScaleFactor() + 0.3F), 0.0F, 0.0F);
            if (Controller.ocMode == 2) {
                GlUtil.translateModelview(var0.origin);
                return;
            }

            if (Controller.ocMode == 1) {
                GlUtil.translateModelview(var1.origin);
            }
        }

    }

    public static void enableOrthogonal3d() {
        enableOrthogonal3d(GLFrame.getWidth(), GLFrame.getHeight());
    }

    public static void enableOrthogonal3d(int var0, int var1) {
        GlUtil.glDisable(2896);
        GlUtil.glDisable(2929);
        GlUtil.glPushMatrix();
        GlUtil.glMatrixMode(5889);
        GlUtil.glPushMatrix();
        GlUtil.glLoadIdentity();
        GlUtil.glOrtho(0.0F, (float)var0, (float)var1, 0.0F, -1000.0F, 1000.0F);
        GlUtil.glMatrixMode(5888);
        GlUtil.glLoadIdentity();
    }

    public static void enableScreenProjection() {
        GlUtil.glPushMatrix();
        GlUtil.glMatrixMode(5889);
        GlUtil.glPushMatrix();
        GlUtil.glLoadIdentity();
        GlUtil.gluPerspective(45.0F, (float)GLFrame.getWidth() / (float)GLFrame.getHeight(), 0.01F, 100.0F);
        GlUtil.glMatrixMode(5888);
        GlUtil.glLoadIdentity();
    }

    public boolean isRenderable() {
        return GlUtil.isColorMask() && (renderModeSet & this.renderMode) == this.renderMode;
    }

    public void attach(GUIElement var1, int var2) {
        assert var1 != this : "Attaching to self " + var1;

        var1.setParent(this);

        assert this.checkDuplicates(var1) : "DUPLICATE: " + var1 + "; " + this.getChilds();

        this.getChilds().add(var2, var1);
    }

    public void attach(GUIElement var1) {
        assert var1 != this : "Attaching to self " + var1;

        var1.setParent(this);

        assert this.checkDuplicates(var1) : "DUPLICATE: " + var1 + "; " + this.getChilds();

        this.getChilds().add(var1);
    }

    private boolean checkDuplicates(GUIElement var1) {
        Iterator var2 = this.getChilds().iterator();

        do {
            if (!var2.hasNext()) {
                return true;
            }
        } while((AbstractSceneNode)var2.next() != var1);

        return false;
    }

    public void attach(int var1, GUIActivatableTextBar var2) {
        assert var2 != this : "Attaching to self " + var2;

        var2.setParent(this);
        this.getChilds().add(var1, var2);
    }

    public synchronized void addObserver(Observer var1) {
        super.addObserver(var1);
    }

    public Vector3f getCurrentAbsolutePos(Vector3f var1) {
        Matrix4f var2 = Controller.modelviewMatrix;
        var1.set(var2.m30, var2.m32, var2.m33);
        var1.add(this.getPos());
        return var1;
    }

    protected boolean isCoordsInside(Vector3f var1, float var2, float var3) {
        boolean var5 = var1.x < this.getWidth() * var2 * var2 && var1.x > 0.0F;
        boolean var4 = var1.y < this.getHeight() * var3 * var3 && var1.y > 0.0F;
        return var5 && var4;
    }

    public void checkMouseInside() {
        if (!deactivateCallbacks && !Mouse.isGrabbed() && this.state.getController().getInputController().getCurrentContextPane() == this.state.getController().getInputController().getCurrentContextPaneDrawing()) {
            float var1 = Vector3fTools.length(Controller.modelviewMatrix.m00, Controller.modelviewMatrix.m01, Controller.modelviewMatrix.m02);
            float var2 = ((float)Mouse.getX() - Controller.modelviewMatrix.m30) * var1;
            float var3 = Vector3fTools.length(Controller.modelviewMatrix.m10, Controller.modelviewMatrix.m11, Controller.modelviewMatrix.m12);
            float var4 = ((float)(GLFrame.getHeight() - Mouse.getY()) - Controller.modelviewMatrix.m31) * var3;
            Matrix4f var10000 = Controller.modelviewMatrix;
            GlUtil.getRelMouseX();
            if (debug) {
                System.err.println("MousePos ;; " + var2 + ", " + var4 + "; " + GLFrame.getWidth() + "x" + GLFrame.getHeight() + "; " + Mouse.getX() + "; " + Mouse.getY());
            }

            this.getRelMousePos().set(var2, var4, 0.0F);
            if (!this.getTransform().basis.equals(TransformTools.ident.basis)) {
                rotTmp.invert(this.getTransform().basis);
                rotTmp.transform(this.getRelMousePos());
            }

            if (System.currentTimeMillis() - this.getState().getController().getInputController().getLastDeactivatedMenu() > 200L) {
                this.setInside(this.isCoordsInside(this.getRelMousePos(), var1, var3));
            } else {
                this.setInside(false);
            }

            if (this.isInside() && GlUtil.getClip() != null) {
                javax.vecmath.Vector4f var5;
                javax.vecmath.Vector4f var8 = var5 = new javax.vecmath.Vector4f(GlUtil.getClip());
                var8.x += GlUtil.getClipModelview().m30;
                var5.y += GlUtil.getClipModelview().m30;
                var5.z += GlUtil.getClipModelview().m31;
                var5.w += GlUtil.getClipModelview().m31;
                int var6 = Mouse.getX();
                int var7 = GLFrame.getHeight() - Mouse.getY();
                if (((float)var6 < var5.x || (float)var6 > var5.y || (float)var7 < var5.z || (float)var7 > var5.w) && !(this instanceof GUIAbstractNewScrollBar) && !(this instanceof GUIScrollablePanel)) {
                    this.setInside(false);
                }
            }

            if (this.isInside() && GlUtil.isColorMask()) {
                if (this.callback != null) {
                    if (!this.callback.isOccluded()) {
                        if (this instanceof TooltipProviderCallback && ((TooltipProviderCallback)this).getToolTip() != null) {
                            this.state.getController().getInputController().getGuiCallbackController().addToolTip(((TooltipProviderCallback)this).getToolTip());
                        }

                        this.state.getController().getInputController().getGuiCallbackController().addCallback(this.callback, this);
                    }

                    if (this.callback instanceof GUICallbackBlocking && ((GUICallbackBlocking)this.callback).isBlocking()) {
                        this.state.getController().getInputController().getGuiCallbackController().addBlocking((GUICallbackBlocking)this.callback, this);
                    }
                } else if (this instanceof TooltipProviderCallback && ((TooltipProviderCallback)this).getToolTip() != null) {
                    this.state.getController().getInputController().getGuiCallbackController().addToolTip(((TooltipProviderCallback)this).getToolTip());
                }
            }

            this.wasInside = this.isInside();
        } else {
            this.setInside(false);
        }
    }

    public void checkBlockingOnly() {
        if (!deactivateCallbacks && !Mouse.isGrabbed() && this.state.getController().getInputController().getCurrentContextPane() == this.state.getController().getInputController().getCurrentContextPaneDrawing()) {
            Matrix4f var1 = Controller.modelviewMatrix;
            float var2 = (new Vector3f(var1.m00, var1.m01, var1.m02)).length();
            float var3 = (new Vector3f(var1.m10, var1.m11, var1.m12)).length();
            float var4 = ((float)Mouse.getX() - var1.m30) * var2;
            float var5 = ((float)(GLFrame.getHeight() - Mouse.getY()) - var1.m31) * var3;
            boolean var7 = var4 < this.getWidth() * var2 * var2 && var4 > 0.0F;
            boolean var6 = var5 < this.getHeight() * var3 * var3 && var5 > 0.0F;
            if (var7 && var6 && GlUtil.isColorMask() && this.callback != null && this.callback instanceof GUICallbackBlocking && ((GUICallbackBlocking)this.callback).isBlocking()) {
                this.state.getController().getInputController().getGuiCallbackController().addBlocking((GUICallbackBlocking)this.callback, this);
            }

        }
    }

    public void checkMouseInsideWithTransform() {
        if (translateOnlyMode) {
            this.translate();
            this.checkMouseInside();
            this.translateBack();
        } else {
            GlUtil.glPushMatrix();
            this.transform();
            this.checkMouseInside();
            GlUtil.glPopMatrix();
        }
    }

    public AbstractSceneNode clone() {
        return null;
    }

    public void transform() {
        this.transformTranslation();
        GlUtil.scaleModelview(this.getScale().x, this.getScale().y, this.getScale().z);
    }

    public void detach(GUIElement var1) {
        var1.setParent((AbstractSceneNode)null);
        this.getChilds().remove(var1);
    }

    public void detachAll() {
        while(0 < this.getChilds().size()) {
            AbstractSceneNode var1;
            if ((var1 = (AbstractSceneNode)this.getChilds().get(0)) instanceof GUIElement) {
                this.detach((GUIElement)var1);
            } else {
                this.detach(var1);
            }
        }

    }

    public void doOrientation() {
    }

    public void drawAttached() {
        if (!this.isInvisible()) {
            if (translateOnlyMode) {
                this.translate();
            } else {
                GlUtil.glPushMatrix();
                this.transform();
            }

            this.setInside(false);
            if (this.isMouseUpdateEnabled()) {
                this.checkMouseInside();
            }

            int var1 = this.childs.size();

            for(int var2 = 0; var2 < var1; ++var2) {
                assert this.childs.get(var2) != this : this;

                ((AbstractSceneNode)this.childs.get(var2)).draw();
            }

            if (translateOnlyMode) {
                this.translateBack();
            } else {
                GlUtil.glPopMatrix();
            }
        }
    }

    public void drawClipped(float var1, float var2, float var3, float var4) {
        var1 /= var2;
        var2 = var3 / var4;
        clip.set(this.getPos().x, this.getPos().x + var1 * this.getWidth(), this.getPos().y, this.getPos().y + var2 * this.getHeight());
        this.drawClipped(clip);
    }

    public void drawClipped(Vector4f var1) {
        leftClipPlane.put(new double[]{1.0D, 0.0D, 0.0D, (double)(-var1.x)}).rewind();
        GL11.glClipPlane(12288, leftClipPlane);
        GlUtil.glEnable(12288);
        rightClipPlane.put(new double[]{-1.0D, 0.0D, 0.0D, (double)var1.y}).rewind();
        GL11.glClipPlane(12289, rightClipPlane);
        GlUtil.glEnable(12289);
        topClipPlane.put(new double[]{0.0D, 1.0D, 0.0D, (double)(-var1.z)}).rewind();
        GL11.glClipPlane(12290, topClipPlane);
        GlUtil.glEnable(12290);
        bottomClipPlane.put(new double[]{0.0D, -1.0D, 0.0D, (double)var1.w}).rewind();
        GL11.glClipPlane(12291, bottomClipPlane);
        GlUtil.glEnable(12291);
        this.draw();
        GlUtil.glDisable(12288);
        GlUtil.glDisable(12289);
        GlUtil.glDisable(12290);
        GlUtil.glDisable(12291);
    }

    public void drawInfo() {
        if (this.isInside()) {
            enableOrthogonal();
            if (textOverlay == null) {
                (textOverlay = new GUITextOverlay(300, 300, this.state)).setText(new ArrayList());
                textOverlay.getText().add("NONE");
            }

            textOverlay.getPos().set((float)(Mouse.getX() + 10), (float)(GLFrame.getHeight() - Mouse.getY()), 0.0F);
            String var1 = this.getName() + this.hashCode() + " " + (this.isInside() ? "(+) " : "(-) ") + (int)this.getRelMousePos().x + ", " + (int)this.getRelMousePos().y;
            textOverlay.getText().set(0, var1);
            textOverlay.drawText();
            disableOrthogonal();
        }
    }

    public GUICallback getCallback() {
        return this.callback;
    }

    public void setCallback(GUICallback var1) {
        this.callback = var1;
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public Vector3f getRelMousePos() {
        return this.relMousePos;
    }

    public InputState getState() {
        return this.state;
    }

    public Object getUserPointer() {
        return this.userPointer;
    }

    public void setUserPointer(Object var1) {
        this.userPointer = var1;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean var1) {
        this.changed = var1;
    }

    public boolean isInside() {
        if (Math.abs(this.insideUpdate - this.state.getNumberOfUpdate()) > 1) {
            this.inside = false;
        }

        return this.inside;
    }

    public void setInside(boolean var1) {
        this.inside = var1;
        this.insideUpdate = this.state.getNumberOfUpdate();
    }

    public boolean isMouseUpdateEnabled() {
        return this.mouseUpdateEnabled;
    }

    public void setMouseUpdateEnabled(boolean var1) {
        this.mouseUpdateEnabled = var1;
    }

    public boolean isOnScreen() {
        if (this.getPos().x > (float)GLFrame.getWidth()) {
            return false;
        } else if (this.getPos().y > (float)GLFrame.getHeight()) {
            return false;
        } else if (this.getPos().x + this.getWidth() * this.getScale().x < 0.0F) {
            return false;
        } else {
            return this.getPos().y + this.getHeight() * this.getScale().y >= 0.0F;
        }
    }

    public boolean isPositionCenter() {
        return false;
    }

    public boolean needsReOrientation() {
        for(int var1 = 0; var1 < 4; ++var1) {
            if (this.viewportMonitor[var1] != Controller.viewport.get(var1)) {
                for(var1 = 0; var1 < 4; ++var1) {
                    this.viewportMonitor[var1] = Controller.viewport.get(var1);
                }

                return true;
            }
        }

        return false;
    }

    public void orientate(int var1) {
        this.orientate(var1, Controller.viewport.get(0), Controller.viewport.get(1), Controller.viewport.get(2), Controller.viewport.get(3));
    }

    public void orientate(int var1, int var2, int var3, int var4, int var5) {
        this.getPos().set(0.0F, 0.0F, 0.0F);
        if ((var1 & 16) == 16) {
            this.getPos().y = (float)((int)((float)var5 - this.getHeight() * this.getScale().y - (float)var3) / 2);
        }

        if ((var1 & 32) == 32) {
            this.getPos().x = (float)((int)((float)var4 - this.getWidth() * this.getScale().x - (float)var2) / 2);
        }

        if ((var1 & 1) == 1) {
            this.getPos().x = (float)var2;
        }

        if ((var1 & 2) == 2) {
            this.getPos().x = (float)((int)((float)var4 - this.getWidth() * this.getScale().x));
        }

        if ((var1 & 4) == 4) {
            this.getPos().y = (float)var3;
        }

        if ((var1 & 8) == 8) {
            this.getPos().y = (float)((int)((float)var5 - this.getHeight() * this.getScale().y));
        }

    }

    public void orientateInsideFrame() {
        if (this.getPos().x < 0.0F) {
            this.getPos().x = 0.0F;
        }

        if (this.getPos().y < 0.0F) {
            this.getPos().y = 0.0F;
        }

        if (this.getPos().x + this.getWidth() > (float)GLFrame.getWidth()) {
            this.getPos().x = (float)GLFrame.getWidth() - this.getWidth();
        }

        if (this.getPos().y + this.getHeight() > (float)GLFrame.getHeight()) {
            this.getPos().y = (float)GLFrame.getHeight() - this.getHeight();
        }

    }

    protected void transformTranslation() {
        GlUtil.glMultMatrix(this.getTransform());
    }

    public boolean wasInside() {
        return this.wasInside;
    }

    public boolean isActive() {
        return true;
    }

    public void resetToolTip() {
    }
}
