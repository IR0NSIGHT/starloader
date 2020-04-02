package api.listener.events.client;

import api.element.block.Block;
import api.element.block.Blocks;
import api.listener.events.Event;
import api.systems.weapons.Beam;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.world.Segment;

import javax.vecmath.Vector3f;
import java.util.Collection;

public class BlockSalvageOnClientEvent extends Event {
    public static int id = idLog++;
    private Beam beam;
    private final int salvagePower;
    private final Vector3f direction;
    private final Block block;
    private final Collection<Segment> hitSegments;

    public BlockSalvageOnClientEvent(BeamState beamState, int salvagePower, Vector3f direction, SegmentPiece blockInternal, Collection<Segment> hitSegments) {
        this.beam = new Beam(beamState);
        this.salvagePower = salvagePower;
        this.direction = direction;
        this.block = new Block(blockInternal);
        this.hitSegments = hitSegments;

    }

    public Beam getBeam() {
        return beam;
    }

    public int getSalvagePower() {
        return salvagePower;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Block getBlock() {
        return block;
    }

    public Collection<Segment> getHitSegments() {
        return hitSegments;
    }

    public Blocks getType() {
        return block.getType();
    }
}
