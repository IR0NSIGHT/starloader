package api.listener.events.block;

import api.listener.events.Event;
import api.listener.type.ServerEvent;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Segment;

import javax.vecmath.Vector3f;
import java.util.Collection;

@ServerEvent
public class SegmentPieceSalvageEvent extends Event {

    private final BeamState beamState;
    private final int salvagePower;
    private final Vector3f direction;
    private final SegmentPiece blockInternal;
    private final Collection<Segment> hitSegments;

    public SegmentPieceSalvageEvent(BeamState beamState, int salvagePower, Vector3f direction, SegmentPiece blockInternal, Collection<Segment> hitSegments) {
        this.beamState = beamState;
        this.salvagePower = salvagePower;
        this.direction = direction;
        this.blockInternal = blockInternal;
        this.hitSegments = hitSegments;
    }

    public BeamState getBeamState() {
        return beamState;
    }

    public int getSalvagePower() {
        return salvagePower;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public SegmentPiece getBlockInternal() {
        return blockInternal;
    }

    public Collection<Segment> getHitSegments() {
        return hitSegments;
    }
}
