package api.listener.events;

import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.scanner.ScanAddOn;
import org.schema.game.common.data.player.AbstractOwnerState;

public class EntityScanEvent extends Event {
    private final ScanAddOn scanner;
    private final boolean success;
    private final AbstractOwnerState owner;
    private final SegmentController entity;

    public EntityScanEvent(ScanAddOn scanner, boolean success, AbstractOwnerState owner, SegmentController entity) {
        this.scanner = scanner;
        this.success = success;
        this.owner = owner;
        this.entity = entity;
    }

    public ScanAddOn getScanner() {
        return scanner;
    }

    public boolean isSuccess() {
        return success;
    }

    public AbstractOwnerState getOwner() {
        return owner;
    }

    public SegmentController getEntity() {
        return entity;
    }
}
