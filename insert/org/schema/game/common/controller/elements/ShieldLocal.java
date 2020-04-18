//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements;

import api.listener.events.calculate.ShieldCapacityCalculateEvent;
import api.listener.events.systems.ShieldHitEvent;
import api.mod.StarLoader;
import api.server.Server;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import javax.vecmath.Vector3f;
import org.schema.common.util.CompareTools;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3fTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.cubes.CubeMeshBufferContainer;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.elements.jumpdrive.JumpAddOn;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer.PowerConsumerCategory;
import org.schema.game.common.controller.elements.shield.capacity.ShieldCapacityUnit;
import org.schema.game.common.controller.elements.shield.regen.ShieldRegenUnit;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SegmentData;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.SerialializationInterface;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.TagSerializable;
import org.schema.schine.resource.tag.Tag.Type;

public class ShieldLocal implements Comparable<ShieldLocal>, PowerConsumer, SerialializationInterface, TagSerializable {
    public static byte VERSION = 0;
    public double shields;
    public double shieldCapacity;
    public double rechargePerSecond;
    public long outputPos;
    public long mainId;
    public final LongArrayList supportIds = new LongArrayList();
    public final LongArrayList supportCoMIds = new LongArrayList();
    public float radius;
    public boolean active = true;
    private float powered;
    private float preventRecharge;
    public ShieldLocalAddOn shieldLocalAddOn;
    private double regenIntegrity;
    private double capacityIntegrity;
    private double preventRechargeNerf;
    private long lastSentUpdate;
    private long lastMgs;

    public ShieldLocal(ShieldLocalAddOn var1) {
        this.shieldLocalAddOn = var1;
    }

    public double getIntegrity() {
        return Math.min(this.regenIntegrity, this.capacityIntegrity);
    }

    public ShieldLocal() {
    }

    public void fromTagStructure(Tag var1) {
        Tag[] var2;
        (var2 = var1.getStruct())[0].getByte();
        this.mainId = var2[1].getLong();
        this.shields = var2[2].getDouble();
        this.shieldCapacity = var2[3].getDouble();
        this.outputPos = var2[4].getLong();
        this.rechargePerSecond = var2[5].getDouble();
        this.active = var2[6].getBoolean();
        this.radius = var2[7].getFloat();
    }

    public Tag toTagStructure() {
        return new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.BYTE, (String)null, VERSION), new Tag(Type.LONG, (String)null, this.mainId), new Tag(Type.DOUBLE, (String)null, this.shields), new Tag(Type.DOUBLE, (String)null, this.getShieldCapacity()), new Tag(Type.LONG, (String)null, this.outputPos), new Tag(Type.DOUBLE, (String)null, this.getRechargeRate()), new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.active ? 1 : 0))), new Tag(Type.FLOAT, (String)null, this.radius), FinishTag.INST});
    }

    public void serialize(DataOutput var1, boolean var2) throws IOException {
        var1.writeLong(this.mainId);
        var1.writeDouble(this.shields);
        var1.writeDouble(this.getShieldCapacity());
        var1.writeLong(this.outputPos);
        var1.writeDouble(this.getRechargeRate());
        var1.writeBoolean(this.active);
        var1.writeFloat(this.radius);
    }

    public void deserialize(DataInput var1, int var2, boolean var3) throws IOException {
        this.mainId = var1.readLong();
        this.shields = var1.readDouble();
        this.shieldCapacity = var1.readDouble();
        this.outputPos = var1.readLong();
        this.rechargePerSecond = var1.readDouble();
        this.active = var1.readBoolean();
        this.radius = var1.readFloat();
    }

    public void updateFrom(ShieldRegenUnit var1) {
        switch(VoidElementManager.SHIELD_LOCAL_RADIUS_CALC_STYLE) {
            case LINEAR:
                this.radius = VoidElementManager.SHIELD_LOCAL_DEFAULT_RADIUS + (float)var1.size() * VoidElementManager.SHIELD_LOCAL_RADIUS_PER_RECHARGE_BLOCK;
                break;
            case EXP:
                this.radius = VoidElementManager.SHIELD_LOCAL_DEFAULT_RADIUS + Math.max(0.0F, (float)Math.pow((double)var1.size(), (double)VoidElementManager.SHIELD_LOCAL_RADIUS_EXP) * VoidElementManager.SHIELD_LOCAL_RADIUS_EXP_MULT);
                break;
            case LOG:
                this.radius = VoidElementManager.SHIELD_LOCAL_DEFAULT_RADIUS + Math.max(0.0F, ((float)Math.log10((double)var1.size()) + VoidElementManager.SHIELD_LOCAL_RADIUS_LOG_OFFSET) * VoidElementManager.SHIELD_LOCAL_RADIUS_LOG_FACTOR);
                break;
            default:
                throw new RuntimeException("Illegal calc style " + VoidElementManager.SHIELD_LOCAL_RADIUS_CALC_STYLE);
        }

        this.outputPos = ElementCollection.getIndex(var1.getCoMOrigin());
        this.rechargePerSecond = (double)((float)var1.size() * VoidElementManager.SHIELD_LOCAL_RECHARGE_PER_BLOCK);
    }

    public void createFrom(ShieldRegenUnit var1) {
        this.mainId = var1.idPos;
        this.regenIntegrity = var1.getIntegrity();
        this.updateFrom(var1);
    }

    public int hashCode() {
        return 31 + (int)(this.mainId ^ this.mainId >>> 32);
    }

    public boolean equals(Object var1) {
        return var1 != null && var1 instanceof ShieldLocal && this.mainId == ((ShieldLocal)var1).mainId;
    }

    public int compareTo(ShieldLocal var1) {
        return CompareTools.compare(var1.radius, this.radius);
    }

    public boolean containsInRadius(float var1, float var2, float var3) {
        float var4 = (float)ElementCollection.getPosX(this.outputPos);
        float var5 = (float)ElementCollection.getPosY(this.outputPos);
        float var6 = (float)ElementCollection.getPosZ(this.outputPos);
        return Vector3fTools.distance(var4, var5, var6, var1, var2, var3) <= this.radius;
    }

    public boolean containsInRadius(ShieldLocal var1) {
        float var2 = (float)ElementCollection.getPosX(var1.outputPos);
        float var3 = (float)ElementCollection.getPosY(var1.outputPos);
        float var4 = (float)ElementCollection.getPosZ(var1.outputPos);
        return this.containsInRadius(var2, var3, var4);
    }

    public boolean addCapacityUnitIfContains(ShieldCapacityUnit var1) {
        boolean var2;
        if (var2 = this.containsInRadius((float)var1.getCoMOrigin().x, (float)var1.getCoMOrigin().y, (float)var1.getCoMOrigin().z)) {
            this.supportIds.add(var1.idPos);
            this.supportCoMIds.add(ElementCollection.getIndex(var1.getCoMOrigin()));
            //INSERTED CODE @177
            this.shieldCapacity += (double)((float)var1.size() * VoidElementManager.SHIELD_LOCAL_CAPACITY_PER_BLOCK);
            ShieldCapacityCalculateEvent event = new ShieldCapacityCalculateEvent(var1, this, this.shieldCapacity);
            StarLoader.fireEvent(ShieldCapacityCalculateEvent.class, event);
            this.shieldCapacity = event.getCapacity();
            ///
            this.capacityIntegrity = Math.min(this.capacityIntegrity, var1.getIntegrity());
        }

        return var2;
    }

    public void process(ShieldHitCallback hit) {
        if (this.shields > 0.0D && this.containsInRadius(hit)) {
            boolean shieldDPS = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_HOTSPOT_DPS, false);
            boolean shieldAlpha = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_HOTSPOT_ALPHA, false);
            float var4 = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_HOTSPOT_PERCENTAGE, 1.0F);
            float var5 = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_HOTSPOT_RANGE, 1.0F);
            boolean var6 = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_HOTSPOT_RECHARGE_MODE, false);
            double damage = hit.getDamage();

            //INSERTED CODE @205
            ShieldHitEvent event = new ShieldHitEvent(this, hit, shieldDPS, shieldAlpha, hit.getDamage());
            StarLoader.fireEvent(ShieldHitEvent.class, event);
            hit.setDamage(event.getDamage());
            if(event.isCanceled()){
                hit.hasHit = false;
                return;
            }
            ///


            double var7 = 0.0D;
            if (shieldAlpha || shieldDPS) {
                if (shieldDPS) {
                    var7 = 1.0D;
                } else if (shieldAlpha) {
                    var7 = -1.0D;
                }

                if (!hit.hasHit) {
                    double dmg = damage - var7 * (double) var5 * damage
                            + var7 * (double) var5 * damage * Math.min(damage / ((double) var4 * (var6 ? this.getRechargeRate() : this.getShieldCapacity())), 2.0D);
                    hit.setDamage(dmg);
                    damage = dmg;
                }
            }

            if (this.shields <= damage) {
                if (!hit.hasHit) {
                    hit.setDamage(damage - this.shields);
                }

                this.shields = 0.0D;
                hit.onShieldOutage(this);
            } else {
                this.shields -= damage;
                if (!hit.hasHit) {
                    hit.setDamage(0.0D);
                }
                hit.onShieldDamage(this);
            }

            hit.hasHit = true;
            //INSERTED TO UPDATE
            hit.setDamage(damage);
            ///
        }

    }

    public void onDamage(double var1) {
        this.preventRecharge = Math.max(this.preventRecharge, this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_UNDER_FIRE_TIMEOUT, VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_MODE_SEC));
        double var3 = this.getShields() / this.getShieldCapacity();
        this.preventRechargeNerf = 1.0D;
        if (var3 <= 1.0E-7D) {
            this.preventRechargeNerf = 0.0D;
        } else if (var3 < (double)VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_START_AT_CHARGED) {
            if (var3 > (double)VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_END_AT_CHARGED) {
                double var5 = var3 - (double)VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_END_AT_CHARGED;
                double var7 = (double)(VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_START_AT_CHARGED - VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_END_AT_CHARGED);

                assert var7 != 0.0D;

                if (var7 == 0.0D) {
                    throw new RuntimeException("Invalid config: shield nerf range zero");
                }

                this.preventRechargeNerf = var5 / var7;
            } else {
                this.preventRechargeNerf = (double)VoidElementManager.SHIELD_LOCAL_RECHARGE_UNDER_FIRE_MIN_PERCENT;
            }
        }

        if (this.shieldLocalAddOn.getSegmentController().isOnServer()) {
            this.shieldLocalAddOn.getManagerContainer().getPowerInterface().onShieldDamageServer(var1);
        }

    }

    public void onOutage(boolean var1) {
        this.preventRecharge = Math.max(this.preventRecharge, this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_ZERO_SHIELDS_TIMEOUT, VoidElementManager.SHIELD_LOCAL_ON_ZERO_SHIELDS_RECHARGE_PREVENTION_SEC));
        this.preventRechargeNerf = 0.0D;
        if (var1 && this.shieldLocalAddOn.getSegmentController().isOnServer()) {
            this.shieldLocalAddOn.sendShieldUpdate(this);
        }

    }

    public String toString() {
        return "ShieldLocal[<" + this.mainId + "> (R: " + this.radius + ") " + StringTools.formatPointZero(this.shields) + "/" + StringTools.formatPointZero(this.getShieldCapacity()) + " -> " + ElementCollection.getPosFromIndex(this.outputPos, new Vector3i()).toStringPure() + "; charge/sec: " + StringTools.formatPointZero(this.getRechargeRate()) + "]";
    }

    public boolean containsInRadius(ShieldHitCallback var1) {
        return this.containsInRadius(var1.xLocalBlock, var1.yLocalBlock, var1.zLocalBlock);
    }

    public boolean containsLocalBlockInRadius(long var1) {
        return this.containsInRadius((float)ElementCollection.getPosX(var1), (float)ElementCollection.getPosY(var1), (float)ElementCollection.getPosZ(var1));
    }

    public double getPowerConsumedPerSecondResting() {
        return this.getRechargeRate() * (double)VoidElementManager.SHIELD_LOCAL_CONSUMPTION_PER_CURRENT_RECHARGE_PER_SECOND_RESTING;
    }

    public double getPowerConsumedPerSecondCharging() {
        return this.getRechargeRate() * (double)VoidElementManager.SHIELD_LOCAL_CONSUMPTION_PER_CURRENT_RECHARGE_PER_SECOND_CHARGING;
    }

    public double getShieldUpkeep() {
        float var1 = this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_CAPACITY_UPKEEP, VoidElementManager.SHIELD_LOCAL_UPKEEP_PER_SECOND_OF_TOTAL_CAPACITY);
        return this.getShieldCapacity() * (double)var1;
    }

    public boolean isPowerCharging(long var1) {
        return this.shields < this.getShieldCapacity() - 1.0E-6D;
    }

    public void setPowered(float var1) {
        this.powered = var1;
    }

    public float getPowered() {
        return this.powered;
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.SHIELDS;
    }

    public void reloadFromReactor(double var1, Timer var3, float var4, boolean var5, float var6) {
        double var7 = this.shields;
        if (this.preventRecharge > 0.0F) {
            this.preventRecharge = Math.max(0.0F, this.preventRecharge - (float)var1);
        } else {
            this.preventRechargeNerf = 1.0D;
        }

        this.getRechargePrevented();
        double var9 = var1 * this.getRechargeRate();
        double var11 = var1 * this.getShieldUpkeep();
        double var13;
        if ((var13 = var9 - var11) >= 0.0D) {
            var13 *= this.preventRechargeNerf;
        } else if (var9 > 0.0D && this.shieldLocalAddOn.getSegmentController().railController.isRoot() && this.shieldLocalAddOn.getSegmentController().isClientOwnObject() && this.shieldLocalAddOn.getState().getUpdateTime() - this.lastMgs > 10000L) {
            Vector3i var15 = ElementCollection.getPosFromIndex(this.mainId, new Vector3i());
            this.shieldLocalAddOn.getSegmentController().sendClientMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIELDLOCAL_1, new Object[]{var15}), 2);
            this.lastMgs = this.shieldLocalAddOn.getState().getUpdateTime();
        }

        this.shields = Math.max(0.0D, Math.min(this.getShieldCapacity(), this.shields + var13));
        if (this.shieldLocalAddOn.getSegmentController().isOnServer()) {
            boolean var16 = var7 < this.shields && this.shields == this.getShieldCapacity();
            boolean var2 = false;

            for(int var17 = 1; var17 < 10 && !var2; ++var17) {
                var2 = var7 < this.getShieldCapacity() * (double)((float)var17) && this.shields >= this.getShieldCapacity() * (double)((float)var17);
            }

            if ((var2 || var16) && (var16 || this.shieldLocalAddOn.getState().getUpdateTime() - this.lastSentUpdate > 1000L)) {
                this.shieldLocalAddOn.sendShieldUpdate(this);
                this.lastSentUpdate = this.shieldLocalAddOn.getState().getUpdateTime();
            }
        }

    }

    public boolean isPowerConsumerActive() {
        return this.active;
    }

    public void resetCapacity() {
        this.supportIds.clear();
        this.supportCoMIds.clear();
        this.capacityIntegrity = 1.0D / 0.0;
        this.shieldCapacity = (double)VoidElementManager.SHIELD_LOCAL_DEFAULT_CAPACITY;
    }

    public boolean markDrawCollectionByBlock(ShieldContainerInterface var1, long var2) {
        Iterator var4 = var1.getShieldRegenManager().getElementCollections().iterator();

        ShieldRegenUnit var5;
        do {
            if (!var4.hasNext()) {
                var4 = var1.getShieldCapacityManager().getElementCollections().iterator();

                ShieldCapacityUnit var6;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }

                    var6 = (ShieldCapacityUnit)var4.next();
                } while(!this.supportIds.contains(var6.idPos) || !var6.getNeighboringCollection().contains(var2));

                this.markDraw(var1);
                return true;
            }

            var5 = (ShieldRegenUnit)var4.next();
        } while(this.mainId != var5.idPos || !var5.getNeighboringCollection().contains(var2));

        this.markDraw(var1);
        return true;
    }

    private void markDraw(ShieldContainerInterface var1) {
        Iterator var2 = var1.getShieldRegenManager().getElementCollections().iterator();

        while(var2.hasNext()) {
            ShieldRegenUnit var3 = (ShieldRegenUnit)var2.next();
            if (this.mainId == var3.idPos) {
                if (this.active) {
                    var3.setDrawColor(0.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    var3.setDrawColor(1.0F, 0.0F, 0.0F, 1.0F);
                }

                var3.markDraw();
            }
        }

        var2 = var1.getShieldCapacityManager().getElementCollections().iterator();

        while(var2.hasNext()) {
            ShieldCapacityUnit var4 = (ShieldCapacityUnit)var2.next();
            if (this.supportIds.contains(var4.idPos)) {
                var4.markDraw();
                var4.setDrawColor(0.0F, 1.0F, 0.0F, 1.0F);
            }
        }

    }

    public boolean containsBlock(ShieldContainerInterface var1, long var2) {
        Iterator var4 = var1.getShieldRegenManager().getElementCollections().iterator();

        ShieldRegenUnit var5;
        do {
            if (!var4.hasNext()) {
                var4 = var1.getShieldCapacityManager().getElementCollections().iterator();

                ShieldCapacityUnit var6;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }

                    var6 = (ShieldCapacityUnit)var4.next();
                } while(!this.supportIds.contains(var6.idPos) || !var6.getNeighboringCollection().contains(var2));

                return true;
            }

            var5 = (ShieldRegenUnit)var4.next();
        } while(this.mainId != var5.idPos || !var5.getNeighboringCollection().contains(var2));

        return true;
    }

    public float getPercentOne() {
        return (float)(this.getShields() / this.getShieldCapacity());
    }

    public double getShieldCapacity() {
        return this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_CAPACITY, this.shieldCapacity);
    }

    public double getShields() {
        return this.shields;
    }

    public String getPosString() {
        return ElementCollection.getPosX(this.mainId) + ", " + ElementCollection.getPosY(this.mainId) + ", " + ElementCollection.getPosZ(this.mainId);
    }

    public String getName() {
        return "ShieldLocal[" + this.getPosString() + "]";
    }

    public double getRechargeRate() {
        double var1 = this.rechargePerSecond * (double)((SendableSegmentController)this.shieldLocalAddOn.getSegmentController()).getBlockEffectManager().status.shieldRegenPercent;
        return this.shieldLocalAddOn.getSegmentController().getConfigManager().apply(StatusEffectType.SHIELD_RECHARGE_RATE, var1);
    }

    public double getRechargeRateIncludingPrevent() {
        return this.getRechargeRate() * this.getRechargePrevented();
    }

    public boolean isPositionInRadiusWorld(Transform var1, Vector3f var2) {
        float var3 = (float)(ElementCollection.getPosX(this.outputPos) - 16);
        float var4 = (float)(ElementCollection.getPosY(this.outputPos) - 16);
        float var5 = (float)(ElementCollection.getPosZ(this.outputPos) - 16);
        Vector3f var6 = new Vector3f(var3, var4, var5);
        var1.transform(var6);
        return Vector3fTools.distance(var2.x, var2.y, var2.z, var6.x, var6.y, var6.z) <= this.radius;
    }

    public void receivedShields(double var1) {
        this.setShieldsAsAction(var1);
    }

    public void setShieldsAsAction(double var1) {
        double var3 = this.shields;
        boolean var5 = false;
        double var6 = 0.0D;
        if (var1 > 0.0D && var1 < this.shields) {
            var5 = true;
            var6 = this.shields - var1;
        }

        this.shields = var1;
        if (var5) {
            this.onDamage(var6);
        }

        if (var1 == 0.0D) {
            this.onOutage(var3 > 0.0D);
        }

        if (this.shieldLocalAddOn.getSegmentController().isOnServer()) {
            boolean var8 = false;

            for(int var2 = 1; var2 < 10 && !var8; ++var2) {
                var8 = var3 > this.getShieldCapacity() * (double)((float)var2) && this.shields <= this.getShieldCapacity() * (double)((float)var2);
            }

            if (var8 && this.shieldLocalAddOn.getState().getUpdateTime() - this.lastSentUpdate > 1000L) {
                this.shieldLocalAddOn.sendShieldUpdate(this);
                this.lastSentUpdate = this.shieldLocalAddOn.getState().getUpdateTime();
            }
        }

    }

    public double getRechargePrevented() {
        return this.preventRecharge > 0.0F ? this.preventRechargeNerf : 1.0D;
    }

    public void dischargeFully() {
    }
}
