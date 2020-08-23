//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.graphicsengine.forms.gui.newgui;

import api.listener.events.gui.MainWindowTabAddEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import javax.vecmath.Vector2f;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.MainWindowFramePalette;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.WindowPaletteInterface;
import org.schema.schine.input.InputState;

public class GUIMainWindow extends GUIResizableGrabbableWindow implements GUITabInterface {
    public static final int INNER_FB_INSET = 29;
    public static final int TABS_HEIGHT = 29;
    private final ObjectArrayList<GUIContentPane> tabs = new ObjectArrayList();
    public int xInnerOffset = 8;
    public int yInnerOffset = 8;
    GUIInnerForeground innerForeground;
    boolean init = false;
    GUIInnerBackground innerBackground;
    private int selectedTab = 0;
    private GUIOverlay elements;
    private GUITexDrawableArea top;
    private GUITexDrawableArea bottom;
    private GUITexDrawableArea left;
    private GUITexDrawableArea right;
    private GUITexDrawableArea bg;
    private GUIGradientVerticalStrip lStrip;
    private GUITabPane tabPane;
    private static WindowPaletteInterface w = new WindowPaletteInterface() {
        public final Vector2f getTopModifierOffset() {
            return MainWindowFramePalette.topSizeModifierOffset;
        }

        public final Vector2f getRightModifierOffset() {
            return MainWindowFramePalette.rightSizeModifierOffset;
        }

        public final Vector2f getLeftModifierOffset() {
            return MainWindowFramePalette.leftSizeModifierOffset;
        }

        public final Vector2f getBottomModifierOffset() {
            return MainWindowFramePalette.bottomSizeModifierOffset;
        }

        public final Vector2f getMoveModifierOffset() {
            return MainWindowFramePalette.moveModifierOffset;
        }
    };

    public GUIMainWindow(InputState var1, int var2, int var3, int var4, int var5, String var6) {
        super(var1, var2, var3, var4, var5, var6);
    }

    public GUIMainWindow(InputState var1, int var2, int var3, String var4) {
        super(var1, var2, var3, var4);
    }

    public WindowPaletteInterface getWindowPalette() {
        return w;
    }

    public void cleanUp() {
    }

    public void draw() {
        if (!this.init) {
            this.onInit();
        }

        GlUtil.glPushMatrix();
        this.transform();
        this.checkMouseInside();
        this.checkGrabbedResize();
        GlUtil.glPopMatrix();
        GlUtil.glPushMatrix();
        this.transform();
        this.drawWindow();
        this.innerBackground.draw();

        assert this.getTabs().size() > 0;

        if (this.getTabs().size() == 1) {
            this.innerForeground.cornerDistanceTopY = 29;
            this.innerForeground.cornerDistanceBottomY = 29;
            this.innerForeground.upperCap = true;
            this.innerForeground.draw();
            ((GUIContentPane)this.getTabs().get(0)).draw();
        } else {
            this.innerForeground.cornerDistanceTopY = 29;
            this.innerForeground.cornerDistanceBottomY = 29;
            this.innerForeground.cornerUpperOffsetY = 29;
            this.innerForeground.upperCap = false;
            this.innerForeground.draw();
            this.tabPane.setPos((float)this.getInset(), (float)this.getInset(), 0.0F);
            this.tabPane.draw();
            ((GUIContentPane)this.getTabs().get(this.selectedTab)).draw();
        }

        int var1 = this.getChilds().size();
        if (this.isMouseUpdateEnabled()) {
            this.checkMouseInside();
        }

        for(int var2 = 0; var2 < var1; ++var2) {
            ((AbstractSceneNode)this.getChilds().get(var2)).draw();
        }

        this.drawMouseResizeIndicators();
        GlUtil.glPopMatrix();
    }

    private void drawWindow() {
        this.elements.setPos(0.0F, 0.0F, 0.0F);
        this.elements.setSpriteSubIndex(0);
        this.elements.draw();
        this.elements.setPos(this.getWidth() - this.elements.getWidth(), 0.0F, 0.0F);
        this.elements.setSpriteSubIndex(1);
        this.elements.draw();
        this.elements.setPos(0.0F, this.getHeight() - this.elements.getHeight(), 0.0F);
        this.elements.setSpriteSubIndex(2);
        this.elements.draw();
        this.elements.setPos(this.getWidth() - this.elements.getWidth(), this.getHeight() - this.elements.getHeight(), 0.0F);
        this.elements.setSpriteSubIndex(3);
        this.elements.draw();
        startStandardDraw();
        this.left.setPos(0.0F, this.elements.getHeight(), 0.0F);
        this.left.setWidth((int)this.elements.getWidth());
        this.left.setHeight((int)Math.max(0.0F, this.getHeight() - 2.0F * this.elements.getHeight()));
        this.left.drawRaw();
        this.right.setPos(this.getWidth() - this.elements.getWidth(), this.elements.getHeight(), 0.0F);
        this.right.setWidth((int)this.elements.getWidth());
        this.right.setHeight((int)Math.max(0.0F, this.getHeight() - 2.0F * this.elements.getHeight()));
        this.right.drawRaw();
        this.top.setPos(this.elements.getWidth(), 0.0F, 0.0F);
        this.top.setHeight((int)this.elements.getHeight());
        this.top.setWidth((int)Math.max(0.0F, this.getWidth() - 2.0F * this.elements.getWidth()));
        this.top.drawRaw();
        this.bottom.setPos(this.elements.getWidth(), this.getHeight() - this.elements.getHeight(), 0.0F);
        this.bottom.setHeight((int)this.elements.getHeight());
        this.bottom.setWidth((int)Math.max(0.0F, this.getWidth() - 2.0F * this.elements.getWidth()));
        this.bottom.drawRaw();
        this.bg.setPos(this.elements.getWidth(), this.elements.getHeight(), 0.0F);
        this.bg.setWidth((int)Math.max(0.0F, this.getWidth() - 2.0F * this.elements.getWidth()));
        this.bg.setHeight((int)Math.max(0.0F, this.getHeight() - 2.0F * this.elements.getHeight()));
        this.bg.drawRaw();
        endStandardDraw();
        this.lStrip.setPos(this.right.getPos().x + 119.0F, this.right.getPos().y, 0.0F);
        this.lStrip.draw();
        this.lStrip.setHeight((int)this.left.getHeight());
        this.lStrip.setPos(this.left.getPos().x + 4.0F, this.left.getPos().y, 0.0F);
        this.lStrip.draw();
        this.drawCross(-58, -8);
        GlUtil.glColor4fForced(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void onInit() {
        super.onInit();
        this.elements = new GUIOverlay(Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainTheme-2x2-gui-"), this.getState());
        this.elements.onInit();
        this.top = new GUITexDrawableArea(this.getState(), Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainThemeTopBottom-2x1-gui-").getMaterial().getTexture(), 0.0F, 0.0F);
        this.bottom = new GUITexDrawableArea(this.getState(), Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainThemeTopBottom-2x1-gui-").getMaterial().getTexture(), 0.0F, 0.0F);
        this.left = new GUITexDrawableArea(this.getState(), Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainThemeLeftRight-1x2-gui-").getMaterial().getTexture(), 0.0F, 0.0F);
        this.right = new GUITexDrawableArea(this.getState(), Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainThemeLeftRight-1x2-gui-").getMaterial().getTexture(), 0.0F, 0.0F);
        this.bg = new GUITexDrawableArea(this.getState(), Controller.getResLoader().getSprite(this.getState().getGUIPath() + "UI_MainThemeBg-gui-").getMaterial().getTexture(), 0.0F, 0.0F);
        this.top.onInit();
        this.bottom.onInit();
        this.left.onInit();
        this.right.onInit();
        this.lStrip = new GUIGradientVerticalStrip(this.getState(), 5.0F, 10.0F, MainWindowFramePalette.windowGradientStripeStart, MainWindowFramePalette.windowGradientStripeMid, MainWindowFramePalette.windowGradientStripeEnd);
        this.lStrip.onInit();
        this.innerBackground = new GUIInnerBackground(this.getState(), this, 24);
        this.innerBackground.onInit();
        this.innerForeground = new GUIInnerForeground(this.getState(), this, 29);
        this.innerForeground.onInit();
        this.tabPane = new GUITabPane(this.getState(), this);
        this.tabPane.onInit();
        this.init = true;
    }

    protected int getMinWidth() {
        return (int)(this.elements.getWidth() * 2.0F);
    }

    protected int getMinHeight() {
        return (int)(this.elements.getHeight() * 2.0F);
    }

    public int getTopDist() {
        return this.getTabs().size() > 1 ? 29 : 0;
    }

    public GUIContentPane addTab(Object var1) {
        GUIContentPane var2;
        (var2 = new GUIContentPane(this.getState(), this, var1)).onInit();
        this.getTabs().add(var2);
        //INSERTED CODE
        MainWindowTabAddEvent event = new MainWindowTabAddEvent(this, var2, var1);
        StarLoader.fireEvent(event, false);
        if(event.isCanceled()) this.getTabs().remove(var2);
        ///
        return var2;
    }

    public void attach(GUIElement var1) {
        assert this.getTabs().size() > 0;

        assert ((GUIContentPane)this.getTabs().get(0)).getTextboxes().size() > 0;

        ((GUIInnerTextbox)((GUIContentPane)this.getTabs().get(0)).getTextboxes().get(0)).getContent().attach(var1);
    }

    public void detach(GUIElement var1) {
        ((GUIInnerTextbox)((GUIContentPane)this.getTabs().get(0)).getTextboxes().get(0)).getContent().detach(var1);
    }

    public ObjectArrayList<GUIContentPane> getTabs() {
        return this.tabs;
    }

    public int getSelectedTab() {
        return this.selectedTab;
    }

    public void setSelectedTab(int var1) {
        this.selectedTab = var1;
    }

    public int getInnerWidthTab() {
        return this.getInnerWidth();
    }

    public int getInnerCornerDistX() {
        return this.innerForeground.cornerDistanceX;
    }

    public int getInnerCornerTopDistY() {
        return this.innerForeground.cornerDistanceTopY;
    }

    public int getInnerCornerBottomDistY() {
        return this.innerForeground.cornerDistanceBottomY;
    }

    public int getInnerHeigth() {
        return (int)this.innerForeground.getHeight();
    }

    public int getInnerWidth() {
        return (int)this.innerForeground.getWidth();
    }

    public int getInnerOffsetX() {
        return this.xInnerOffset;
    }

    public int getInnerOffsetY() {
        return this.yInnerOffset;
    }

    public int getInset() {
        return 29;
    }

    public void clearTabs() {
        this.selectedTab = 0;

        for(int var1 = 0; var1 < this.tabs.size(); ++var1) {
            ((GUIContentPane)this.tabs.get(var1)).cleanUp();
        }

        this.tabs.clear();
    }
}
