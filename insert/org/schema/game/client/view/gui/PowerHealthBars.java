package org.schema.game.client.view.gui;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.Ship;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.util.timer.SinusTimerUtil;

public class PowerHealthBars extends GUIElement {
    int x = 0;
    int y = 0;
    private GUIOverlay healthBarFrame;
    private GUIOverlay healthBar;
    private GUIOverlay powerBarFrame;
    private GUIOverlay powerBar;
    private GUITextOverlay shieldNerf;
    private GUITextOverlay powerCons;
    private SinusTimerUtil sinusTime = new SinusTimerUtil();
    private float distance = 32.0F;
    private boolean recovery;
    private GUIOverlay shieldBar;
    private GUIOverlay shieldBarFrame;

    public PowerHealthBars(GameClientState var1) {
        super(var1);
        String var2 = "powerbar-1x4-gui-";
        this.healthBar = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.healthBar.setSpriteSubIndex(0);
        this.healthBar.setPos(66.0F, -11.0F, 11F);
        this.healthBarFrame = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.healthBarFrame.setSpriteSubIndex(2);
        this.healthBarFrame.setPos(66.0F, -11.0F, 0.0F);
        this.shieldBar = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.shieldBar.setSpriteSubIndex(1);
        this.shieldBar.setPos(66.0F, -11.0F, 0.0F);
        this.shieldBarFrame = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.shieldBarFrame.setSpriteSubIndex(2);
        this.shieldBarFrame.setPos(66.0F, -11.0F, 0.0F);
        this.powerBar = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.powerBar.setSpriteSubIndex(1);
       // this.powerBar.setPos(549.0F, -11.0F, 0.0F);
        this.powerBar.setPos(549.0F, -100.0F, 0.0F);
        this.powerBarFrame = new GUIOverlay(Controller.getResLoader().getSprite(var2), this.getState());
        this.powerBarFrame.setSpriteSubIndex(3);
        this.powerBarFrame.setPos(10.0F, -11.0F, 0.0F);
        if (Controller.getResLoader().getSprite(var2).getTint() == null) {
            Controller.getResLoader().getSprite(var2).setTint(new Vector4f(1.0F, 1.0F, 1.0F, 1.0F));
        }

    }

    public void cleanUp() {
    }

    public void draw() {
        if(true) return;
        GlUtil.glPushMatrix();
        this.transform();
        GameClientState var1 = (GameClientState)this.getState();
        this.healthBarFrame.getSprite().getTint().w = 0.0F;
        this.powerBarFrame.getSprite().getTint().w = 0.0F;
        this.healthBarFrame.draw();
        this.healthBar.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
        float var2 = 1.0F - var1.getPlayer().getHealth() / 120.0F;
        org.lwjgl.util.vector.Vector4f var3 = new org.lwjgl.util.vector.Vector4f(this.healthBar.getPos().x + var2 * 402.0F, this.healthBar.getPos().x + 402.0F, this.healthBar.getPos().y, this.healthBar.getPos().y + this.healthBar.getHeight());
        this.healthBar.drawClipped(var3);
        if (var1.getShip() != null) {
            Ship var11 = var1.getShip();
            this.shieldBar.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
            float var12 = (float)(1.0D - var11.getManagerContainer().getShieldAddOn().getShields() / var11.getManagerContainer().getShieldAddOn().getShieldCapacity());
            org.lwjgl.util.vector.Vector4f var10 = new org.lwjgl.util.vector.Vector4f(this.shieldBar.getPos().x + var12 * 402.0F, this.shieldBar.getPos().x + 402.0F, this.shieldBar.getPos().y, this.shieldBar.getPos().y + this.shieldBar.getHeight());
            this.shieldBar.drawClipped(var10);
            this.powerBarFrame.getSprite().getTint().w = 0.0F;
            if (this.recovery != var11.getManagerContainer().getPowerAddOn().isInRecovery()) {
                this.recovery = var11.getManagerContainer().getPowerAddOn().isInRecovery();
            }

            if (this.recovery || var11.getManagerContainer().getPowerAddOn().getPowerRailed() <= 0.0D) {
                this.powerBarFrame.getSprite().getTint().x = 1.0F;
                this.powerBarFrame.getSprite().getTint().y = 0.0F;
                this.powerBarFrame.getSprite().getTint().z = 0.0F;
                this.powerBarFrame.getSprite().getTint().w = this.sinusTime.getTime() * 0.3F;
            }

            this.powerBarFrame.draw();
            this.powerBar.getSprite().getTint().set(1.0F, 1.0F, 1.0F, 1.0F);
            this.powerBar.drawClipped((float)var11.getManagerContainer().getPowerAddOn().getPower() * 0.785F, (float)var11.getManagerContainer().getPowerAddOn().getMaxPower(), 1.0F, 1.0F);
            Vector3f var10000;
            double var4;
            if (var11.getManagerContainer().getPowerAddOn().getPowerConsumedPerSecond() > 0.0D) {
                var4 = var11.getManagerContainer().getPowerAddOn().getPowerConsumedPerSecond();
                this.powerCons.getPos().set(this.powerBar.getPos());
                var10000 = this.powerCons.getPos();
                var10000.y += 115.0F;
                var10000 = this.powerCons.getPos();
                var10000.x += 200.0F;
                String var6 = "Cons: " + StringTools.formatPointZero(var4) + "/sec";
                if (var11.getManagerContainer().getPowerAddOn().isInRecovery()) {
                    var6 = var6 + "   Outage Recovery!";
                }

                this.powerCons.getText().set(0, var6);
                this.powerCons.draw();
            }

            if (var11.getManagerContainer().getShieldAddOn().getShieldCapacity() > 0.0D) {
                var4 = var11.getManagerContainer().getShieldAddOn().getShields() / var11.getManagerContainer().getShieldAddOn().getShieldCapacity() * 100.0D;
                this.shieldNerf.getPos().set(this.shieldBar.getPos());
                var10000 = this.shieldNerf.getPos();
                var10000.y += 115.0F;
                var10000 = this.shieldNerf.getPos();
                var10000.x += 10.0F;
                double var13 = var11.getManagerContainer().getShieldAddOn().getRecovery();
                double var8 = var11.getManagerContainer().getShieldAddOn().getRecoveryOut();
                this.shieldNerf.getText().set(0, "Shield: " + (int)Math.ceil(var4) + "%     Recharge: " + (int)Math.ceil(var11.getManagerContainer().getShieldAddOn().getNerf() * 100.0D) + "% " + (var13 > 0.0D ? "(under fire)" : "     " + (var8 > 0.0D ? "Recovery: " + StringTools.formatPointZero(var8) + "sec" : "")));
                this.shieldNerf.draw();
            }
        }

        GlUtil.glPopMatrix();
    }

    public void onInit() {
        this.healthBarFrame.onInit();
        this.healthBar.onInit();
        this.powerBarFrame.onInit();
        this.powerBar.onInit();
        this.shieldNerf = new GUITextOverlay(300, 30, FontLibrary.getBoldArial12White(), this.getState());
        this.shieldNerf.onInit();
        this.shieldNerf.setTextSimple("ShieldSystems: 100%");
        this.powerCons = new GUITextOverlay(300, 30, FontLibrary.getBoldArial12White(), this.getState());
        this.powerCons.onInit();
        this.powerCons.setTextSimple("Power Consumption: 0");
    }

    public float getHeight() {
        return this.healthBarFrame.getHeight();
    }

    public float getWidth() {
        return this.healthBarFrame.getWidth() * 2.0F + this.distance;
    }

    public boolean isPositionCenter() {
        return false;
    }

    public boolean isExternalShipActive() {
        return ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getShipExternalFlightController().isTreeActive();
    }

    public void update(Timer var1) {
        this.sinusTime.update(var1);
    }
}