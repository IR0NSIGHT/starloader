package api.listener.events.systems;

import api.entity.Entity;
import api.listener.events.Event;
import api.systems.Shield;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.elements.ShieldHitCallback;
import org.schema.game.common.controller.elements.ShieldLocal;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

import javax.vecmath.Vector3f;

public class ShieldHitEvent extends Event {
    public static int id = idLog++;
    private Shield shield;
    ShieldHitCallback shieldHit;
    private final boolean isLowDamage;
    private final boolean isHighDamage;
    private double damage;

    public ShieldHitEvent(ShieldLocal local, ShieldHitCallback shieldHit, boolean isLowDamage, boolean isHighDamage,
                          double damage){
        this.shield = new Shield(local);
        this.shieldHit = shieldHit;
        this.isLowDamage = isLowDamage;
        this.isHighDamage = isHighDamage;
        this.damage = damage;
    }
    public Entity getEntity(){
        return shield.getEntity();
    }

    public Entity getShootingEntity(){
        SimpleTransformableSendableObject<?> shootingEntity = shieldHit.damager.getShootingEntity();
        if(shootingEntity instanceof SegmentController){
            return new Entity((SegmentController) shootingEntity);
        }
        return null;
    }
    public Vector3f getWorldHit(){
        return new Vector3f(shieldHit.xWorld,shieldHit.yWorld,shieldHit.zWorld);
    }

    public Vector3f getLocalHit(){
        return new Vector3f(shieldHit.xLocalBlock, shieldHit.yLocalBlock, shieldHit.zLocalBlock);
    }
    public DamageDealerType getDamageType(){
        return shieldHit.damageType;
    }

    public void setDamage(double damage){
        this.damage = damage;
    }
    public void addDamage(double damage){
        this.damage += damage;
    }

    public Shield getShield() {
        return shield;
    }

    public ShieldHitCallback getShieldHit() {
        return shieldHit;
    }

    public double getDamage() {
        return damage;
    }

    public boolean isHighDamage() {
        return isHighDamage;
    }

    public boolean isLowDamage() {
        return isLowDamage;
    }
}
