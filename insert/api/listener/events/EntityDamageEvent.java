package api.listener.events;

import api.entity.Entity;
import api.entity.EntityType;
import api.entity.Player;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.projectile.ProjectileHandlerSegmentController;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

public class EntityDamageEvent extends Event {
    Entity entity;
    private final ProjectileHandlerSegmentController.ShotHandler shotHandler;
    private final ProjectileHandlerSegmentController handler;
    private final HitType hitType;
    private final Damager damager;
    private Entity damagerEntity;
    private Player damagerPlayer;

    public EntityDamageEvent(SegmentController entity, ProjectileHandlerSegmentController.ShotHandler shotHandler, ProjectileHandlerSegmentController handler, HitType hitType, Damager damager) {
        this.entity = new Entity(entity);
        this.shotHandler = shotHandler;
        this.handler = handler;
        this.hitType = hitType;
        this.damager = damager;
        SimpleTransformableSendableObject<?> shooter = damager.getShootingEntity();
        if(shooter instanceof SegmentController){
            this.damagerEntity = new Entity((SegmentController) shooter);
        }else if(shooter instanceof PlayerCharacter){
            this.damagerPlayer = new Player(((PlayerCharacter) shooter).getOwnerState());
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public ProjectileHandlerSegmentController.ShotHandler getShotHandler() {
        return shotHandler;
    }

    public ProjectileHandlerSegmentController getHandler() {
        return handler;
    }

    public HitType getHitType() {
        return hitType;
    }

    public Damager getDamagerRaw() {
        return damager;
    }
    public boolean wasShotByEntity(){
        return damagerEntity != null;
    }
    public boolean wasShotByPlayer(){
        return damagerEntity != null;
    }

    public Entity getDamagerEntity() {
        return damagerEntity;
    }

    public Player getDamagerPlayer() {
        return damagerPlayer;
    }
}
