//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui.shiphud.newhud;

import java.util.Iterator;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

import api.listener.events.TargetPanelDrawEvent;
import api.mod.StarLoader;
import org.newdawn.slick.Color;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector4i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.HudIndicatorOverlay;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.player.AbstractCharacter;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.AbstractSceneNode;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIDrawnTimerInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITextOverlayAutoScroll;
import org.schema.schine.input.InputState;

public class TargetPanel extends HudConfig {
    @ConfigurationElement(
            name = "Position"
    )
    public static GUIPosition POSITION;
    @ConfigurationElement(
            name = "Offset"
    )
    public static Vector2f OFFSET;
    @ConfigurationElement(
            name = "NameOffset"
    )
    public static Vector2f nameOffset;
    @ConfigurationElement(
            name = "MassOffset"
    )
    public static Vector2f massOffset;
    @ConfigurationElement(
            name = "SpeedOffset"
    )
    public static Vector2f speedOffset;
    @ConfigurationElement(
            name = "FactionOffset"
    )
    public static Vector2f factionOffset;
    private GUIOverlay background;
    private TargetPlayerHealthBar playerHealthBar;
    private TargetPowerBar powerBar;
    private TargetShieldBar shieldBar;
    private TargetShipHPBar shipHPBar;
    private GUITextOverlay name;
    private GUITextOverlay faction;
    private GUITextOverlay mass;
    private GUITextOverlay speed;
    private float timeDrawn;
    private GUIScrollablePanel nameScroller;
    private GUIScrollablePanel factionScroller;

    public TargetPanel(InputState var1) {
        super(var1);
    }

    public Vector4i getConfigColor() {
        return null;
    }

    public GUIPosition getConfigPosition() {
        return POSITION;
    }

    public Vector2f getConfigOffset() {
        return OFFSET;
    }

    protected String getTag() {
        return "TargetPanel";
    }

    public void draw() {
        SimpleTransformableSendableObject var1;
        if ((var1 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null) {
            Vector2f var10000 = PopupMessageNew.targetPanel;
            var10000.x += this.getPos().x;
            var10000 = PopupMessageNew.targetPanel;
            var10000.y += this.getPos().y;
            GlUtil.glPushMatrix();
            this.transform();
            this.background.draw();
            var10000 = PopupMessageNew.targetPanel;
            var10000.x += this.background.getPos().x;
            var10000 = PopupMessageNew.targetPanel;
            var10000.y += this.background.getPos().y;
            var10000 = PopupMessageNew.targetPanel;
            var10000.x += this.background.getWidth();
            var10000 = PopupMessageNew.targetPanel;
            var10000.y += this.background.getHeight();
            this.nameScroller.draw();
            this.factionScroller.draw();
            this.mass.draw();
            this.speed.draw();
            if (var1 instanceof SegmentController) {
                if (((SegmentController)var1).hasStructureAndArmorHP()) {
                    this.shipHPBar.draw();
                }

                if (var1 instanceof ManagedSegmentController) {
                    this.shieldBar.draw();
                    this.powerBar.draw();
                }
            } else if (var1 instanceof AbstractCharacter) {
                this.playerHealthBar.draw();
            }

            Iterator var2 = this.getChilds().iterator();

            while(var2.hasNext()) {
                ((AbstractSceneNode)var2.next()).draw();
            }

            if (this.isRenderable() && this.isMouseUpdateEnabled()) {
                this.checkMouseInside();
            }

            GlUtil.glPopMatrix();
        }
        //INSERTED CODE
        StarLoader.fireEvent(TargetPanelDrawEvent.class, new TargetPanelDrawEvent(this));
        ///
    }

    public void onInit() {
        super.onInit();
        this.background = new GUIOverlay(Controller.getResLoader().getSprite("HUD_Target-2x4-gui-"), this.getState());
        this.powerBar = new TargetPowerBar(this.getState()) {
            public void draw() {
                ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity();
                super.draw();
            }
        };
        this.playerHealthBar = new TargetPlayerHealthBar(this.getState());
        this.shieldBar = new TargetShieldBar(this.getState());
        this.shipHPBar = new TargetShipHPBar(this.getState());
        this.background.onInit();
        this.powerBar.onInit();
        this.shieldBar.onInit();
        this.playerHealthBar.onInit();
        this.shipHPBar.onInit();
        this.nameScroller = new GUIScrollablePanel(this.background.getWidth() - nameOffset.x * 2.0F, 24.0F, this.getState());
        this.nameScroller.setLeftRightClipOnly = true;
        this.nameScroller.setScrollable(0);
        this.name = new GUITextOverlayAutoScroll(10, 10, FontLibrary.getBlenderProMedium15(), this.nameScroller, new GUIDrawnTimerInterface() {
            public float getTimeDrawn() {
                return TargetPanel.this.timeDrawn;
            }

            public void setTimeDrawn(float var1) {
                TargetPanel.this.timeDrawn = var1;
            }
        }, this.getState());
        this.name.setTextSimple(new Object() {
            public String toString() {
                SimpleTransformableSendableObject var1;
                if ((var1 = ((GameClientState)TargetPanel.this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null) {
                    String var2 = var1.getRealName();
                    return var1.getAdditionalObjectInformation() != null ? var2 + var1.getAdditionalObjectInformation() : var2;
                } else {
                    return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_4;
                }
            }
        });
        this.nameScroller.setPos(nameOffset.x, nameOffset.y, 0.0F);
        this.factionScroller = new GUIScrollablePanel(this.background.getWidth() - factionOffset.x * 2.0F, 24.0F, this.getState());
        this.faction = new GUITextOverlayAutoScroll(10, 10, FontLibrary.getBlenderProMedium15(), this.factionScroller, new GUIDrawnTimerInterface() {
            public void setTimeDrawn(float var1) {
                TargetPanel.this.timeDrawn = var1;
            }

            public float getTimeDrawn() {
                return TargetPanel.this.timeDrawn;
            }
        }, this.getState()) {
            Vector4f c = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);

            public void draw() {
                SimpleTransformableSendableObject var1;
                if ((var1 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null) {
                    HudIndicatorOverlay.getColor(var1, this.c, false, (GameClientState)this.getState());
                    this.setColor(this.c);
                    super.draw();
                }

            }
        };
        this.faction.setTextSimple(new Object() {
            public String toString() {
                SimpleTransformableSendableObject var1;
                if ((var1 = ((GameClientState)TargetPanel.this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null) {
                    FactionManager var2;
                    return (var2 = ((GameClientState)TargetPanel.this.getState()).getFactionManager()).existsFaction(var1.getFactionId()) ? var2.getFaction(var1.getFactionId()).getName() : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_1;
                } else {
                    return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_0;
                }
            }
        });
        this.factionScroller.setPos(factionOffset.x, factionOffset.y, 0.0F);
        this.mass = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium15(), new Color(215, 224, 236, 255), this.getState());
        this.mass.setTextSimple(new Object() {
            public String toString() {
                SimpleTransformableSendableObject var1;
                return (var1 = ((GameClientState)TargetPanel.this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null ? StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_3, new Object[]{StringTools.formatSeperated(Math.ceil((double)var1.getMass()))}) : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_2;
            }
        });
        this.mass.setPos(massOffset.x, massOffset.y, 0.0F);
        this.speed = new GUITextOverlay(10, 10, FontLibrary.getBlenderProMedium15(), new Color(215, 224, 236, 255), this.getState());
        this.speed.setTextSimple(new Object() {
            public String toString() {
                SimpleTransformableSendableObject var1;
                return (var1 = ((GameClientState)TargetPanel.this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity()) != null ? StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_5, new Object[]{StringTools.formatSeperated(Math.round(var1.getSpeedCurrent()))}) : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_SHIPHUD_NEWHUD_TARGETPANEL_6;
            }
        });
        this.speed.setPos(speedOffset.x, speedOffset.y, 0.0F);
        this.width = this.background.getWidth();
        this.height = this.background.getHeight();
        this.powerBar.getPos().set(TargetPowerBar.OFFSET.x, TargetPowerBar.OFFSET.y, 0.0F);
        this.shieldBar.getPos().set(TargetShieldBar.OFFSET.x, TargetShieldBar.OFFSET.y, 0.0F);
        this.playerHealthBar.getPos().set(PlayerHealthBar.OFFSET.x, PlayerHealthBar.OFFSET.y, 0.0F);
        this.shipHPBar.getPos().set(TargetShipHPBar.OFFSET.x, TargetShipHPBar.OFFSET.y, 0.0F);
    }

    public void update(Timer var1) {
        this.updateOrientation();
        this.timeDrawn += var1.getDelta();
    }
}
