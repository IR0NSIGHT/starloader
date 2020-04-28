//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.beam;

import java.io.IOException;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import api.listener.events.DamageBeamShootEvent;
import api.mod.StarLoader;
import api.systems.modules.custom.CustomShipBeamElement;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.elements.BlockActivationListenerInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.IntegrityBasedInterface;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.controller.elements.UsableCombinableControllableElementManager;
import org.schema.game.common.controller.elements.UsableControllableElementManager;
import org.schema.game.common.controller.elements.ManagerContainer.ReceivedBeamLatch;
import org.schema.game.common.controller.elements.UsableControllableFireingElementManager.ReloadListener;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamUnit;
import org.schema.game.common.controller.elements.combination.BeamCombiSettings;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import org.schema.schine.input.KeyboardMappings;

public abstract class BeamElementManager<E extends BeamUnit<E, CM, EM>, CM extends BeamCollectionManager<E, CM, EM>, EM extends BeamElementManager<E, CM, EM>> extends UsableCombinableControllableElementManager<E, CM, EM, BeamCombiSettings> implements BlockActivationListenerInterface, IntegrityBasedInterface {

    public final ShootContainer shootContainer = new ShootContainer();
    private final BeamCombiSettings combiSettings = new BeamCombiSettings();
    private static GUITextOverlay chargesText;
    public static final Vector4f chargeColor = new Vector4f(0.8F, 0.5F, 0.3F, 0.4F);
    private final BeamElementManager<E, CM, EM>.DrawReloadListener drawReloadListener = new BeamElementManager.DrawReloadListener();

    public BeamElementManager(short var1, short var2, SegmentController var3) {
        super(var1, var2, var3);
    }

    public BeamCombiSettings getCombiSettings() {
        return this.combiSettings;
    }

    public int onActivate(SegmentPiece var1, boolean var2, boolean var3) {
        long var4 = var1.getAbsoluteIndex();

        for(int var8 = 0; var8 < this.getCollectionManagers().size(); ++var8) {
            Iterator var6 = ((BeamCollectionManager)this.getCollectionManagers().get(var8)).getElementCollections().iterator();

            while(var6.hasNext()) {
                BeamUnit var7;
                if ((var7 = (BeamUnit)var6.next()).contains(var4)) {
                    var7.setMainPiece(var1, var3);
                    if (var3) {
                        return 1;
                    }

                    return 0;
                }
            }
        }

        if (var3) {
            return 1;
        } else {
            return 0;
        }
    }

    public void doShot(E c, CM m, ShootContainer var3, PlayerState var4, float var5, Timer var6, boolean var7) {
        ManagerModuleCollection var11 = null;
        short var8;
        if (m.getEffectConnectedElement() != -9223372036854775808L) {
            var8 = (short)ElementCollection.getType(m.getEffectConnectedElement());
            var11 = this.getManagerContainer().getModulesControllerMap().get(var8);
        }

        if (m.getEffectConnectedElement() != -9223372036854775808L) {
            var8 = (short)ElementCollection.getType(m.getEffectConnectedElement());
            var11 = this.getManagerContainer().getModulesControllerMap().get(var8);
            ControlBlockElementCollectionManager var9;
            if ((var9 = CombinationAddOn.getEffect(m.getEffectConnectedElement(), var11, this.getSegmentController())) != null) {
                m.setEffectTotal(var9.getTotalSize());
            }
        }

        ShootingRespose var10;
        if (this.isCombinable() && m.getSlaveConnectedElement() != -9223372036854775808L) {
            var8 = (short)ElementCollection.getType(m.getSlaveConnectedElement());
            ManagerModuleCollection var14 = this.getManagerContainer().getModulesControllerMap().get(var8);
            var10 = this.handleAddOn(this, m, c, var14, var11, var3, (SimpleTransformableSendableObject)null, var4, var6, var5);
            this.handleResponse(var10, c, var3.weapontOutputWorldPos);
        } else {

            Vector3f var12;
            (var12 = new Vector3f()).set(var3.weapontOutputWorldPos);
            var3.shootingDirTemp.scale(c.getDistance());
            var12.add(var3.shootingDirTemp);
            BeamCommand b;
            (b = new BeamCommand()).minEffectiveRange = c.getMinEffectiveRange();
            b.minEffectiveValue = c.getMinEffectiveValue();
            b.maxEffectiveRange = c.getMaxEffectiveRange();
            b.maxEffectiveValue = c.getMaxEffectiveValue();
            b.currentTime = var6.currentTime;
            b.identifier = c.getSignificator();
            b.relativePos.set((float)(c.getOutput().x - 16), (float)(c.getOutput().y - 16), (float)(c.getOutput().z - 16));
            b.reloadCallback = c;
            b.from.set(var3.weapontOutputWorldPos);
            b.to.set(var12);
            b.playerState = var4;
            b.beamTimeout = c.getBurstTime() > 0.0F ? c.getBurstTime() : var5;
            b.tickRate = c.getTickRate();
            b.beamPower = c.getBeamPower();
            b.cooldownSec = c.getCoolDownSec();
            b.bursttime = c.getBurstTime();
            b.initialTicks = c.getInitialTicks();
            b.powerConsumedByTick = c.getPowerConsumption();
            b.latchOn = c.isLatchOn();
            b.checkLatchConnection = c.isCheckLatchConnection();
            b.hitType = c.getHitType();
            b.powerConsumedExtraByTick = 0.0F;
            b.railParent = this.getRailHitMultiplierParent();
            b.railChild = this.getRailHitMultiplierChild();
            if (var4 != null && var4.isKeyDownOrSticky(KeyboardMappings.WALK)) {
                b.dontFade = true;
            }

            b.weaponId = m.getUsableId();
            b.controllerPos = m.getControllerPos();
            b.firendlyFire = c.isFriendlyFire();
            b.penetrating = c.isPenetrating();
            b.acidDamagePercent = c.getAcidDamagePercentage();

            //INSERTED CODE @159
            if(m instanceof DamageBeamCollectionManager) {
                DamageBeamShootEvent event = new DamageBeamShootEvent((DamageBeamUnit) c, b);
                StarLoader.fireEvent(DamageBeamShootEvent.class, event);
                if(event.isCanceled()){
                    return;
                }
            }
            ///

            var10 = m.getHandler().addBeam(b);
            this.handleResponse(var10, c, var3.weapontOutputWorldPos);
        }
    }

    public void onAddedCollection(long var1, CM var3) {
        super.onAddedCollection(var1, var3);
        this.notifyBeamDrawer();
    }

    public void notifyBeamDrawer() {
        if (!this.getSegmentController().isOnServer()) {
            ((GameClientState)this.getSegmentController().getState()).getWorldDrawer().getBeamDrawerManager().update(this, true, (Object)null);
        }

    }

    public void onConnectionRemoved(long var1, CM var3) {
        super.onConnectionRemoved(var1, var3);
        this.notifyBeamDrawer();
    }

    public ControllerManagerGUI getGUIUnitValues(E var1, CM var2, ControlBlockElementCollectionManager<?, ?, ?> var3, ControlBlockElementCollectionManager<?, ?, ?> var4) {
        return null;
    }

    public void handle(ControllerStateInterface var1, Timer var2) {
        if (var1.isFlightControllerActive()) {
            if (!this.getCollectionManagers().isEmpty()) {
                try {
                    if (!this.convertDeligateControls(var1, this.shootContainer.controlledFromOrig, this.shootContainer.controlledFrom)) {
                        return;
                    }
                } catch (IOException var8) {
                    var8.printStackTrace();
                    return;
                }

                int var3 = this.getCollectionManagers().size();

                for(int var4 = 0; var4 < var3; ++var4) {
                    BeamCollectionManager var5 = (BeamCollectionManager)this.getCollectionManagers().get(var4);
                    boolean var6 = var1.isSelected(var5.getControllerElement(), this.shootContainer.controlledFrom);
                    boolean var7 = var1.isAISelected(var5.getControllerElement(), this.shootContainer.controlledFrom, var5 instanceof DamageBeamCollectionManager ? var4 : -2147483648, this.getCollectionManagers().size(), var5);
                    if (var6 && var7 && this.shootContainer.controlledFromOrig.equals(this.shootContainer.controlledFrom) | this.getControlElementMap().isControlling(this.shootContainer.controlledFromOrig, var5.getControllerPos(), this.controllerId) && var5.allowedOnServerLimit()) {
                        if (this.shootContainer.controlledFromOrig.equals(Ship.core)) {
                            var1.getControlledFrom(this.shootContainer.controlledFromOrig);
                        }

                        var5.handleControlShot(var1, var2);
                    }
                }

            }
        }
    }

    public abstract float getTickRate();

    public abstract float getCoolDown();

    public abstract float getBurstTime();

    public abstract float getInitialTicks();

    public abstract double getRailHitMultiplierParent();

    public abstract double getRailHitMultiplierChild();

    public boolean handleBeamLatch(ReceivedBeamLatch var1) {
        Iterator var2 = this.getCollectionManagers().iterator();

        boolean var3;
        do {
            if (!var2.hasNext()) {
                return false;
            }
        } while(!(var3 = ((BeamCollectionManager)var2.next()).handleBeamLatch(var1)));

        return var3;
    }

    public void drawReloads(Vector3i var1, Vector3i var2, long var3) {
        this.handleReload(var1, var2, var3, this.drawReloadListener);
    }

    public boolean isUsingRegisteredActivation() {
        return true;
    }

    public class DrawReloadListener implements ReloadListener {
        public DrawReloadListener() {
        }

        public String onDischarged(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6) {
            UsableControllableElementManager.drawReload(var1, var2, var3, var4, var5, var6);
            return null;
        }

        public String onReload(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6) {
            UsableControllableElementManager.drawReload(var1, var2, var3, var4, var5, var6);
            return null;
        }

        public String onFull(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, boolean var5, float var6, long var7) {
            return null;
        }

        public void drawForElementCollectionManager(InputState var1, Vector3i var2, Vector3i var3, Vector4f var4, long var5) {
            BeamCollectionManager var7;
            BeamCombiSettings var8;
            if ((var7 = (BeamCollectionManager)BeamElementManager.this.getCollectionManagersMap().get(var5)) != null && (var8 = var7.getWeaponChargeParams()).chargeTime > 0.0F && var7.beamCharge > 0.0F) {
                if (BeamElementManager.chargesText == null) {
                    BeamElementManager.chargesText = new GUITextOverlay(10, 10, FontSize.MEDIUM.getFont(), (InputState)BeamElementManager.this.getState());
                    BeamElementManager.chargesText.onInit();
                }

                float var6 = Math.min(var7.beamCharge / var8.chargeTime, 0.99999F);
                UsableControllableElementManager.drawReload(var1, var2, var3, BeamElementManager.chargeColor, false, var6, true, var7.beamCharge, (int)var8.chargeTime, -1L, BeamElementManager.chargesText);
            }

        }
    }
}
