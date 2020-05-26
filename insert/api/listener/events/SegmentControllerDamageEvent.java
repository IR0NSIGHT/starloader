package api.listener.events;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.HitType;
import org.schema.game.common.controller.damage.projectile.ProjectileHandlerSegmentController;

public class SegmentControllerDamageEvent extends Event {
    private SegmentController entity;
    private final ProjectileHandlerSegmentController.ShotHandler shotHandler;
    private final ProjectileHandlerSegmentController handler;
    private final HitType hitType;
    private final Damager damager;

    public SegmentControllerDamageEvent(SegmentController entity, ProjectileHandlerSegmentController.ShotHandler shotHandler, ProjectileHandlerSegmentController handler, HitType hitType, Damager damager) {
        this.entity = entity;
        this.shotHandler = shotHandler;
        this.handler = handler;
        this.hitType = hitType;
        this.damager = damager;
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

    public SegmentController getEntity() {
        return entity;
    }
}
