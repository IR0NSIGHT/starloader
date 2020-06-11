package api.listener.events;

import api.entity.Entity;
import api.entity.EntityType;
import api.entity.Player;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.projectile.ProjectileHandlerSegmentController;
import org.schema.game.common.controller.damage.projectile.ProjectileParticleContainer;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

import java.util.ArrayList;

public class EntityDamageEvent extends Event {
    Entity entity;
    private final ProjectileHandlerSegmentController.ShotHandler shotHandler;
    private final ProjectileHandlerSegmentController handler;
    private final HitType hitType;
    private final Damager damager;
    private Entity damagerEntity;
    private Player damagerPlayer;
    private ProjectileParticleContainer projectile;
    private ArrayList<Segment> segmentsHit;

    public EntityDamageEvent(SegmentController entity, ProjectileHandlerSegmentController.ShotHandler shotHandler, ProjectileHandlerSegmentController handler, HitType hitType, Damager damager, ProjectileParticleContainer projectile, ArrayList<Segment> segmentsHit) {
        this.entity = new Entity(entity);
        this.shotHandler = shotHandler;
        this.handler = handler;
        this.hitType = hitType;
        this.damager = damager;
        this.projectile = projectile;
        this.segmentsHit = segmentsHit;
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

    public ProjectileParticleContainer getProjectile() {
        return projectile;
    }

    public ArrayList<Segment> getSegmentsHit() {
        return segmentsHit;
    }
}
