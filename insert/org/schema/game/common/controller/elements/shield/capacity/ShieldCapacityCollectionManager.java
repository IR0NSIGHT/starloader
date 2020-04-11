//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.shield.capacity;

import api.listener.events.calculate.ShieldCapacityCalculateEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Iterator;
import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.ShieldHitCallback;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;

public class ShieldCapacityCollectionManager extends ElementCollectionManager<ShieldCapacityUnit, ShieldCapacityCollectionManager, VoidElementManager<ShieldCapacityUnit, ShieldCapacityCollectionManager>> implements PowerConsumer {
    private float powered;
    private final Long2ObjectOpenHashMap<ShieldLocal> lastLocalShieldCache = new Long2ObjectOpenHashMap();
    private long lastLocalShieldGet;

    public ShieldCapacityCollectionManager(SegmentController var1, VoidElementManager<ShieldCapacityUnit, ShieldCapacityCollectionManager> var2) {
        super((short)3, var1, var2);
    }

    private void updateCapabilities() {
        long var1 = 0L;
        Iterator var3 = this.getElementCollections().iterator();
        ArrayList<ShieldCapacityUnit> units = new ArrayList<>();
        while(var3.hasNext()) {
            ShieldCapacityUnit var4 = (ShieldCapacityUnit)var3.next();
            units.add(var4);
            var1 = (long)((float)var1 + (float)var4.size() * VoidElementManager.SHIELD_EXTRA_CAPACITY_MULT_PER_UNIT);
        }

        var1 = (long)(Math.pow((double)var1 * VoidElementManager.SHIELD_CAPACITY_PRE_POW_MUL, VoidElementManager.SHIELD_CAPACITY_POW) * VoidElementManager.SHIELD_CAPACITY_TOTAL_MUL);
        ShieldAddOn var5 = ((ShieldContainerInterface)((ManagedSegmentController)this.getSegmentController()).getManagerContainer()).getShieldAddOn();

        //INSERTED CODE
        //Turns out setting shields max is literaly overwritten later....
//        ShieldCapacityCalculateEvent event = new ShieldCapacityCalculateEvent(this, units, var1);
//        StarLoader.fireEvent(ShieldCapacityCalculateEvent.class, event);
//        var1 = event.getCapacity();

        ///

        var5.setShieldCapacityHP((double)var1);
        var5.setShields(Math.min(var5.getShields(), (double)var1));
    }

    public int getMargin() {
        return 0;
    }

    public boolean isDetailedElementCollections() {
        return true;
    }

    protected Class<ShieldCapacityUnit> getType() {
        return ShieldCapacityUnit.class;
    }

    public boolean needsUpdate() {
        return false;
    }

    public ShieldCapacityUnit getInstance() {
        return new ShieldCapacityUnit();
    }

    protected void onChangedCollection() {
        this.updateCapabilities();
        if (this.getSegmentController().isUsingPowerReactors()) {
            ((ShieldContainerInterface)this.getContainer()).getShieldAddOn().getShieldLocalAddOn().flagCalcLocalShields();
        }

    }

    public GUIKeyValueEntry[] getGUICollectionStats() {
        ShieldAddOn var1 = ((ShieldContainerInterface)((ManagedSegmentController)this.getSegmentController()).getManagerContainer()).getShieldAddOn();
        return new GUIKeyValueEntry[]{new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIELD_CAPACITY_SHIELDCAPACITYCOLLECTIONMANAGER_0, StringTools.formatPointZero(var1.getShieldCapacity())), new ModuleValueEntry(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIELD_CAPACITY_SHIELDCAPACITYCOLLECTIONMANAGER_1, StringTools.formatPointZero(var1.getShieldRechargeRate()))};
    }

    public String getModuleName() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIELD_CAPACITY_SHIELDCAPACITYCOLLECTIONMANAGER_2;
    }

    public void clear() {
        super.clear();
        this.lastLocalShieldCache.clear();
    }

    public float getSensorValue(SegmentPiece var1) {
        ShieldAddOn var2;
        if ((var2 = ((ShieldContainerInterface)((ManagedSegmentController)this.getSegmentController()).getManagerContainer()).getShieldAddOn()).isUsingLocalShields()) {
            ShieldLocal var3 = (ShieldLocal)this.lastLocalShieldCache.get(var1.getAbsoluteIndex());
            if (this.lastLocalShieldGet + (long)(var3 == null ? 500 : 5000) < this.getSegmentController().getState().getUpdateTime()) {
                this.lastLocalShieldCache.put(var1.getAbsoluteIndex(), var2.getShieldLocalAddOn().getContainingShield((ShieldContainerInterface)((ManagedSegmentController)this.getSegmentController()).getManagerContainer(), var1.getAbsoluteIndex()));
                this.lastLocalShieldGet = this.getSegmentController().getState().getUpdateTime();
            }

            ShieldLocal var4;
            return (var4 = (ShieldLocal)this.lastLocalShieldCache.get(var1.getAbsoluteIndex())) != null ? (float)Math.min(1.0D, var4.getShields() / Math.max(9.999999747378752E-5D, var4.getShieldCapacity())) : 0.0F;
        } else {
            return (float)Math.min(1.0D, var2.getShields() / Math.max(9.999999747378752E-5D, var2.getShieldCapacity()));
        }
    }

    public double getPowerConsumedPerSecondResting() {
        return 0.0D;
    }

    public double getPowerConsumedPerSecondCharging() {
        return 0.0D;
    }

    public ShieldAddOn getShieldAddOn() {
        return ((ShieldContainerInterface)((ManagedSegmentController)this.getSegmentController()).getManagerContainer()).getShieldAddOn();
    }

    public boolean isPowerCharging(long var1) {
        return this.getShieldAddOn().getPercentOne() < 0.9999F;
    }

    public void setPowered(float var1) {
        this.powered = var1;
    }

    public float getPowered() {
        return this.powered;
    }

    public void reloadFromReactor(double var1, Timer var3, float var4, boolean var5, float var6) {
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.SHIELDS;
    }

    public boolean isPowerConsumerActive() {
        return true;
    }

    public void dischargeFully() {
    }

    public void shieldHit(ShieldHitCallback var1) {
        if (this.getSegmentController().isOnServer()) {
            this.checkIntegrityForced(var1.damager);
        }

    }
}
