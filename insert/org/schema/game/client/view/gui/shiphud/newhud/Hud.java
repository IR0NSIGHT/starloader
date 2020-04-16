//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui.shiphud.newhud;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import api.listener.events.gui.HudCreateEvent;
import api.mod.StarLoader;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.controller.manager.ingame.ship.ShipExternalFlightController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.camera.InShipCamera;
import org.schema.game.client.view.gui.shiphud.HudIndicatorOverlay;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.FireingUnit;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.UsableControllableElementManager;
import org.schema.game.common.controller.elements.effectblock.EffectElementManager.OffensiveEffects;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIStatisticsGraph;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITabbedContent;
import org.schema.schine.graphicsengine.forms.gui.newgui.StatisticsGraphListInterface;
import org.schema.schine.graphicsengine.util.timer.SinusTimerUtil;
import org.schema.schine.network.StateInterface;
import org.schema.schine.physics.PhysicsState;

import java.util.ArrayList;

public class Hud extends GUIElement {
    int x = 0;
    int y = 0;
    float dist = 128.0F;
    long lastTest = 0L;
    private FillableBar powerBar;
    private FillableBar reactorPowerBar;
    private FillableBar speedBarFarRight;
    private FillableBar speedBarRight;
    private FillableBar shieldBarLeft;
    private FillableBar shieldBarRight;
    private FillableBar healthBar;
    private FillableBar shipHPBar;
    private TargetPanel targetPanel;
    private PositiveEffectBar positiveEffectBar;
    private NegativeEffectBar negativeEffectBar;
    private Radar radar;
    private HudIndicatorOverlay indicator;
    private final HudContextHelpManager helpManager;
    private GUIOverlay backgroundCrosshairHUD;
    private GUIOverlay backgroundCrosshair;
    private GUIOverlay hitNotification;
    private GUITextOverlay targetName;
    private SinusTimerUtil sinusTimerUtil = new SinusTimerUtil(10.0F);
    private SinusTimerUtil testSinusTimerUtil = new SinusTimerUtil(4.0F);
    private boolean updateSine;
    private GUIAncor rightBottom;
    private GUIAncor leftBottom;
    private PowerBatteryBar powerBatteryBar;
    private PowerStabilizationBar powerStabilizationBar;
    private PowerConsumptionBar powerConsumptionBar;

    //INSERTED CODE
    public static ArrayList<GUIElement> customElements = new ArrayList<GUIElement>();
    ///

    public Hud(GameClientState state) {
        super(state);
        this.radar = new Radar(state);
        this.powerBar = new PowerBar(state);
        this.reactorPowerBar = new ReactorPowerBar(state);
        this.speedBarFarRight = new SpeedBarFarRight(state);
        this.speedBarRight = new SpeedBarRight(state);
        this.shieldBarLeft = new ShieldBarLeftOld(state);
        this.shieldBarRight = new ShieldBarRightLocal(state);
        this.powerBatteryBar = new PowerBatteryBar(state);
        this.healthBar = new HealthBar(state);
        this.shipHPBar = new ShipHPBar(state);
        this.powerStabilizationBar = new PowerStabilizationBar(state);
        this.powerConsumptionBar = new PowerConsumptionBar(state);
        this.targetPanel = new TargetPanel(state);
        this.positiveEffectBar = new PositiveEffectBar(state);
        this.negativeEffectBar = new NegativeEffectBar(state);
        this.backgroundCrosshairHUD = new GUIOverlay(Controller.getResLoader().getSprite("crosshair-c-gui-"), state);

        //INSERTED CODE
        HudCreateEvent event = new HudCreateEvent(this, state);
        StarLoader.fireEvent(HudCreateEvent.class, event);
        customElements.addAll(event.elements);
        ///
        //this.test = new GUIOverlay(Controller.getResLoader().getSprite("crosshair-c-gui-"), var1);

        this.backgroundCrosshair = new GUIOverlay(Controller.getResLoader().getSprite("crosshair-simple-c-gui-"), state);
        this.hitNotification = new GUIOverlay(Controller.getResLoader().getSprite("crosshair-hit-c-gui-"), state);
        this.helpManager = new HudContextHelpManager(state);
        this.indicator = new HudIndicatorOverlay(state);
    }

    public void cleanUp() {
    }

    public void draw() {
        if (this.needsReOrientation()) {
            this.doOrientation();
        }

        GlUtil.glPushMatrix();
        this.hitNotification.orientate(48);
        Vector3f var10000 = this.hitNotification.getPos();
        var10000.x += this.hitNotification.getWidth() / 2.0F;
        var10000 = this.hitNotification.getPos();
        var10000.y += this.hitNotification.getHeight() / 2.0F;
        SegmentController var1;
        if (this.isExternalActive() && Controller.getCamera() instanceof InShipCamera && (var1 = this.getSegmentControllerFromEntered()) != null) {
            InShipCamera var2 = (InShipCamera)Controller.getCamera();
            Vector3f var3;
            (var3 = new Vector3f(var2.getHelperForward())).normalize();
            Vector3f var9 = new Vector3f(var2.getPos());
            GameClientState var4 = (GameClientState)this.getState();
            boolean var5 = false;
            if (System.currentTimeMillis() - this.lastTest > 1000L) {
                try {
                    Vector3i var6;
                    ManagerModuleCollection var11;
                    ControlBlockElementCollectionManager var12;
                    SegmentPiece var13;
                    if ((var6 = var4.getShip().getSlotAssignment().get(var4.getPlayer().getCurrentShipControllerSlot())) != null && (var13 = var4.getShip().getSegmentBuffer().getPointUnsave(var6)) != null && var4.getShip() instanceof ManagedSegmentController && (var11 = var4.getShip().getManagerContainer().getModulesControllerMap().get(var13.getType())) != null && var11.getElementManager() instanceof UsableControllableElementManager && (var12 = (ControlBlockElementCollectionManager)var11.getCollectionManagersMap().get(var13.getAbsoluteIndex())) != null) {
                        for(int var14 = 0; var14 < var12.getElementCollections().size(); ++var14) {
                            ElementCollection var7;
                            if ((var7 = (ElementCollection)var12.getElementCollections().get(var14)) instanceof FireingUnit) {
                                this.dist = Math.max(this.dist, ((FireingUnit)var7).getDistanceFull());
                                var5 = true;
                            }
                        }
                    }
                } catch (Exception var8) {
                }

                if (!var5) {
                    this.dist = 128.0F;
                }

                this.lastTest = System.currentTimeMillis();
            }

            var3.scale(this.dist);
            var9.add(var3);
            Vector3f var15 = ((GameClientState)this.getState()).getScene().getWorldToScreenConverter().convert(var9, new Vector3f(), false);
            this.backgroundCrosshair.getPos().set(var15);
            var10000 = this.backgroundCrosshair.getPos();
            var10000.x += 3.0F;
            var10000 = this.backgroundCrosshair.getPos();
            var10000.y -= 3.0F;
            this.drawSmallCorsair(this.dist, var1);
            this.drawInShipHud(this.dist, var1);
        } else {
            if (this.isExternalActive() && !(Controller.getCamera() instanceof InShipCamera)) {
                System.err.println("WARNING: HudBasic has wrong camera: " + Controller.getCamera().getClass());
            }

            this.backgroundCrosshair.orientate(48);
            var10000 = this.backgroundCrosshair.getPos();
            var10000.x += this.backgroundCrosshair.getWidth() / 2.0F;
            var10000 = this.backgroundCrosshair.getPos();
            var10000.y += this.backgroundCrosshair.getHeight() / 2.0F;
            this.backgroundCrosshair.draw();
            if (((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getPlayerCharacterManager().isTreeActive()) {
                this.radar.orientate(6);
                this.radar.draw();
                this.targetPanel.draw();
            }
        }

        if (((GameClientState)this.getState()).getPlayer() != null && ((GameClientState)this.getState()).getPlayer().getClientHitNotifaction() != 0) {
            byte var10;
            if ((var10 = ((GameClientState)this.getState()).getPlayer().getClientHitNotifaction()) == 2) {
                this.hitNotification.getSprite().getTint().set(0.4F, 0.4F, 1.0F, 0.9F);
            } else if (var10 == 3) {
                this.hitNotification.getSprite().getTint().set(1.0F, 0.4F, 0.4F, 0.9F);
            } else {
                this.hitNotification.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 0.9F);
            }

            this.hitNotification.draw();
        }

        this.drawIndications();
        if (EngineSettings.G_DRAW_LAG_OBJECTS_IN_HUD.isOn()) {
            this.leftBottom.setWidth(GLFrame.getWidth() - (GLFrame.getWidth() / 2 + 354 + 21));
            this.leftBottom.orientate(9);
            var10000 = this.leftBottom.getPos();
            var10000.y -= 100.0F;
            this.leftBottom.draw();
        }

        if (EngineSettings.G_DRAW_NT_STATS_OVERLAY.isOn()) {
            this.rightBottom.setWidth(GLFrame.getWidth() - (GLFrame.getWidth() / 2 + 354 + 21));
            this.rightBottom.orientate(10);
            this.rightBottom.draw();
        }

        this.helpManager.draw();
        //INSERTED CODE
        for (GUIElement element : customElements){
            element.draw();
        }
        ///
        GlUtil.glPopMatrix();
    }

    public void onInit() {
        this.powerBar.onInit();
        this.reactorPowerBar.onInit();
        this.shieldBarLeft.onInit();
        this.shieldBarRight.onInit();
        this.healthBar.onInit();
        this.powerBatteryBar.onInit();
        this.shipHPBar.onInit();
        this.powerStabilizationBar.onInit();
        this.powerConsumptionBar.onInit();
        this.targetPanel.onInit();
        this.speedBarFarRight.onInit();
        this.speedBarRight.onInit();
        this.backgroundCrosshairHUD.onInit();
        this.backgroundCrosshairHUD.getSprite().setTint(new Vector4f(1.0F, 1.0F, 1.0F, 1.0F));

        for (GUIElement element : customElements){
            element.onInit();
        }

        this.backgroundCrosshair.onInit();
        this.backgroundCrosshair.getSprite().setTint(new Vector4f(1.0F, 1.0F, 1.0F, 1.0F));
        this.hitNotification.onInit();
        this.hitNotification.getSprite().setTint(new Vector4f(1.0F, 1.0F, 1.0F, 0.9F));
        this.positiveEffectBar.onInit();
        this.negativeEffectBar.onInit();
        this.indicator.onInit();
        this.radar.onInit();
        this.helpManager.onInit();
        this.targetName = new GUITextOverlay(100, 10, FontLibrary.getCourierNew12White(), this.getState());
        this.targetName.setTextSimple("");
        this.targetName.setPos(300.0F, 300.0F, 0.0F);
        this.rightBottom = new GUIAncor(this.getState(), 300.0F, 80.0F);
        GUIScrollablePanel var1 = new GUIScrollablePanel(100.0F, 100.0F, this.rightBottom, this.getState());
        GUIStatisticsGraph var2;
        (var2 = new GUIStatisticsGraph(this.getState(), (GUITabbedContent)null, this.rightBottom, new StatisticsGraphListInterface[]{((StateInterface)this.getState()).getDataStatsManager().getReceivedData(), ((StateInterface)this.getState()).getDataStatsManager().getSentData()}) {
            public String formatMax(long var1, long var3) {
                return "Max: " + StringTools.readableFileSize(var1) + "; In: " + StringTools.readableFileSize(var3) + (this.ps == null ? " (F12)" : "");
            }
        }).onInit();
        var1.setContent(var2);
        this.rightBottom.attach(var1);
        this.leftBottom = new GUIAncor(this.getState(), 300.0F, 80.0F);
        var1 = new GUIScrollablePanel(100.0F, 100.0F, this.leftBottom, this.getState());
        (var2 = new GUIStatisticsGraph(this.getState(), (GUITabbedContent)null, this.leftBottom, new StatisticsGraphListInterface[]{((GameClientState)this.getState()).lagStats}) {
            public String formatMax(long var1, long var3) {
                return "Max: " + var1 + "ms" + (this.ps == null ? " (F7)" : "");
            }
        }).onInit();
        var1.setContent(var2);
        this.leftBottom.attach(var1);
    }

    private void drawBigCorsair() {
        this.backgroundCrosshairHUD.orientate(48);
        Vector3f var10000 = this.backgroundCrosshairHUD.getPos();
        var10000.x += this.backgroundCrosshairHUD.getWidth() / 2.0F;
        var10000 = this.backgroundCrosshairHUD.getPos();
        var10000.y += this.backgroundCrosshairHUD.getHeight() / 2.0F;
        if (this.getExternalShipMan().getAquire().isTargetMode()) {
            if (((GameClientState)this.getState()).getPlayer().getAquiredTarget() != null) {
                this.backgroundCrosshairHUD.getSprite().getTint().set(this.sinusTimerUtil.getTime(), 1.0F, this.sinusTimerUtil.getTime(), 1.0F);
                this.backgroundCrosshairHUD.getSprite().setScale(1.0F, 1.0F, 1.0F);
                this.updateSine = true;
            } else {
                if (this.getExternalShipMan().getAquire().getTarget() == null || this.getExternalShipMan().getAquire().getTarget() instanceof Ship && ((Ship)this.getExternalShipMan().getAquire().getTarget()).isJammingFor(this.getExternalShipMan().getShip())) {
                    this.targetName.getText().set(0, "");
                    this.backgroundCrosshairHUD.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
                    this.backgroundCrosshairHUD.getSprite().setScale(2.0F, 2.0F, 2.0F);
                    this.backgroundCrosshairHUD.setRot(0.0F, 0.0F, 0.0F);
                } else {
                    float var1 = this.getExternalShipMan().getAquire().getTargetTime();
                    float var2 = this.getExternalShipMan().getAquire().getAcquireTime(this.getExternalShipMan().getShip(), this.getExternalShipMan().getAquire().getTarget());
                    var1 = Math.min(1.0F, var1 / var2);
                    var2 = 1.0F - var1;
                    this.backgroundCrosshairHUD.getSprite().setScale(var2 + 1.0F, var2 + 1.0F, var2 + 1.0F);
                    this.backgroundCrosshairHUD.getSprite().getTint().set(var2, var1, 0.0F, 1.0F);
                    this.backgroundCrosshairHUD.setRot(0.0F, 0.0F, var1 * 360.0F);
                    this.targetName.getText().set(0, this.getExternalShipMan().getAquire().getTarget().toNiceString());
                }

                this.sinusTimerUtil.reset();
                this.updateSine = false;
            }
        } else {
            this.backgroundCrosshairHUD.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
            this.backgroundCrosshairHUD.getSprite().setScale(1.0F, 1.0F, 1.0F);
            this.backgroundCrosshairHUD.setRot(0.0F, 0.0F, 0.0F);
            this.updateSine = false;
        }

        this.backgroundCrosshairHUD.draw();
        //this.test.draw();

        this.targetName.draw();
    }

    public void drawIndications() {
        this.indicator.draw();
    }

    private void drawInShipHud(float var1, SegmentController var2) {
        if (!var2.isUsingPowerReactors()) {
            this.powerBar.draw();
            this.shieldBarLeft.draw();
        } else {
            this.shieldBarRight.draw();
        }

        this.shipHPBar.draw();
        if (var2.isUsingPowerReactors()) {
            this.speedBarRight.draw();
            if (var2.hasAnyReactors()) {
                this.powerStabilizationBar.draw();
                this.powerConsumptionBar.draw();
            } else {
                this.powerStabilizationBar.drawNoReactorText();
            }
        } else {
            this.speedBarFarRight.draw();
            this.powerBatteryBar.draw();
        }

        if (!var2.isUsingPowerReactors()) {
            this.powerBar.drawText();
            this.shieldBarLeft.drawText();
        } else {
            this.shieldBarRight.drawText();
        }

        this.shipHPBar.drawText();
        if (var2.isUsingPowerReactors()) {
            this.powerStabilizationBar.drawText();
            this.powerConsumptionBar.drawText();
            this.speedBarRight.drawText();
        } else {
            this.speedBarFarRight.drawText();
            this.powerBatteryBar.drawText();
        }

        this.targetPanel.draw();
        this.negativeEffectBar.draw();
        this.positiveEffectBar.draw();
        this.radar.orientate(6);
        this.radar.draw();
        this.targetName.getText().set(0, "");
        this.drawBigCorsair();
    }

    private void drawSmallCorsair(float var1, SegmentController var2) {
        InShipCamera var3 = (InShipCamera)Controller.getCamera();
        Vector3f var4 = new Vector3f(Controller.getCamera().getPos());
        Vector3f var6 = new Vector3f(var3.getHelperForward());
        PhysicsExt var5 = (PhysicsExt)((PhysicsState)this.getState()).getPhysics();
        var6.normalize();
        var6.scale(var1);
        var6.add(var4);
        if (var5.testRayCollisionPoint(var4, var6, false, var2, (SegmentController)null, false, true, true).hasHit()) {
            this.backgroundCrosshair.getSprite().getTint().set(0.0F, 1.0F, 0.0F, 1.0F);
        } else {
            this.backgroundCrosshair.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.backgroundCrosshair.draw();
        this.backgroundCrosshair.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ShipExternalFlightController getExternalShipMan() {
        return ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getShipExternalFlightController();
    }

    public float getHeight() {
        return (float)GLFrame.getHeight();
    }

    public float getWidth() {
        return (float)GLFrame.getWidth();
    }

    public boolean isPositionCenter() {
        return false;
    }

    public HudContextHelpManager getHelpManager() {
        return this.helpManager;
    }

    public HudIndicatorOverlay getIndicator() {
        return this.indicator;
    }

    public void setIndicator(HudIndicatorOverlay var1) {
        this.indicator = var1;
    }

    private PlayerInteractionControlManager getInteractionManager() {
        return ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager();
    }

    private SegmentController getSegmentControllerFromEntered() {
        if (this.getInteractionManager().getInShipControlManager().isActive()) {
            return this.getInteractionManager().getInShipControlManager().getEntered().getSegmentController();
        } else {
            return this.getInteractionManager().getSegmentControlManager().getEntered() != null ? this.getInteractionManager().getSegmentControlManager().getEntered().getSegmentController() : null;
        }
    }

    public WeaponCollectionManager getWeaponController() {
        GameClientState var1;
        Ship var2;
        if ((var2 = (var1 = (GameClientState)this.getState()).getShip()) != null) {
            Vector3i var4 = var2.getSlotAssignment().get(var1.getPlayer().getCurrentShipControllerSlot());

            for(int var3 = 0; var3 < var2.getManagerContainer().getWeapon().getCollectionManagers().size(); ++var3) {
                if (((WeaponCollectionManager)var2.getManagerContainer().getWeapon().getCollectionManagers().get(var3)).equalsControllerPos(var4)) {
                    return (WeaponCollectionManager)var2.getManagerContainer().getWeapon().getCollectionManagers().get(var3);
                }
            }
        }

        return null;
    }

    public boolean isExternalActive() {
        return this.getInteractionManager().getInShipControlManager().getShipControlManager().getShipExternalFlightController().isTreeActive() || this.getInteractionManager().getSegmentControlManager().getSegmentExternalController().isTreeActive();
    }

    public void onSectorChange() {
        this.indicator.onSectorChange();
    }

    public void update(Timer var1) {
        this.indicator.update(var1);
        if (this.updateSine) {
            this.sinusTimerUtil.update(var1);
        }

        this.testSinusTimerUtil.update(var1);
        this.positiveEffectBar.updateOrientation();
        this.negativeEffectBar.updateOrientation();
        this.positiveEffectBar.update(var1);
        this.negativeEffectBar.update(var1);
        this.targetPanel.update(var1);
        this.powerBatteryBar.update(var1);
        this.powerBar.update(var1);
        this.reactorPowerBar.update(var1);
        this.speedBarFarRight.update(var1);
        this.speedBarRight.update(var1);
        this.shieldBarLeft.update(var1);
        this.shieldBarRight.update(var1);
        this.healthBar.update(var1);
        this.shipHPBar.update(var1);
        this.powerStabilizationBar.update(var1);
        this.powerConsumptionBar.update(var1);
        this.radar.update(var1);
        //INSERTED CODE
        for (GUIElement element : customElements){
            element.update(var1);
        }
        ///
        this.helpManager.update(var1);
        if (!this.isExternalActive() || !(Controller.getCamera() instanceof InShipCamera)) {
            this.resetDrawnInShip();
        }

    }

    public void resetDrawnHUDAtAll() {
        this.radar.resetDrawn();
    }

    public void notifyEffectHit(SimpleTransformableSendableObject var1, OffensiveEffects var2) {
        HitIconIndex var3;
        if ((var3 = (HitIconIndex)BuffDebuff.mapOffensive.get(var2)) != null) {
            if (var3.isBuff()) {
                this.positiveEffectBar.activate(var3);
            } else {
                this.negativeEffectBar.activate(var3);
            }
        } else {
            EffectIconIndex var4;
            if ((var4 = (EffectIconIndex)BuffDebuff.map.get(var2.getEffect())).isBuff()) {
                this.positiveEffectBar.activate(var4);
            } else {
                this.negativeEffectBar.activate(var4);
            }
        }
    }

    public void resetDrawnInShip() {
        this.powerBatteryBar.resetDrawn();
        this.powerBar.resetDrawn();
        this.reactorPowerBar.resetDrawn();
        this.speedBarRight.resetDrawn();
        this.speedBarFarRight.resetDrawn();
        this.shieldBarLeft.resetDrawn();
        this.shieldBarRight.resetDrawn();
        this.healthBar.resetDrawn();
        this.shipHPBar.resetDrawn();
        this.powerStabilizationBar.resetDrawn();
        this.powerConsumptionBar.resetDrawn();
    }

    public Radar getRadar() {
        return this.radar;
    }

    public void setRadar(Radar var1) {
        this.radar = var1;
    }
}
