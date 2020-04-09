package api.listener.events;

import api.systems.weapons.BeamEntity;
import org.schema.game.common.controller.BeamHandlerContainer;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.HitReceiverType;
import org.schema.game.common.controller.damage.beam.DamageBeamHitHandlerSegmentController;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Segment;

import javax.vecmath.Vector3f;
import java.util.Collection;

public class DamageBeamHitEvent extends Event{
    public static int id = 10;
    private final DamageBeamHitHandlerSegmentController inst;
    private final SegmentController hitSegment;
    BeamEntity beamEntity;
    private final int damage;
    private final BeamHandlerContainer<?> unknownVar1;
    private final SegmentPiece unknownVar2;
    private final Vector3f origin;
    private final Vector3f hitPos;
    private final Collection<Segment> idk_lol;

    public DamageBeamHitEvent(DamageBeamHitHandlerSegmentController inst, SegmentController hitSegment, BeamState beam, int damage, BeamHandlerContainer<?> unknownVar1, SegmentPiece unknownVar2, Vector3f origin, Vector3f hitPos, Collection<Segment> idk_lol) {

        this.inst = inst;
        this.hitSegment = hitSegment;
        this.beamEntity = new BeamEntity(beam);
        this.damage = damage;
        this.unknownVar1 = unknownVar1;
        this.unknownVar2 = unknownVar2;
        this.origin = origin;
        this.hitPos = hitPos;
        this.idk_lol = idk_lol;
        hitSegment.getEffectContainer().get(HitReceiverType.BLOCK);
    }
}
