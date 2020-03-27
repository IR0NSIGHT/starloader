package api.listener.events;

import api.entity.Entity;
import api.systems.addons.Scanner;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.scanner.ScanAddOn;
import org.schema.game.common.data.player.AbstractOwnerState;

public class EntityScanEvent extends Event {
    public static int id = idLog++;
    private Entity entity;
    private AbstractOwnerState owner;
    private boolean success;
    private Scanner scanner;
    public EntityScanEvent(ScanAddOn scanner, boolean success, AbstractOwnerState owner, SegmentController entity) {
        this.scanner = new Scanner(scanner);
        this.success = success;
        this.owner = owner;
        this.entity = new Entity(entity);
    }

    public Entity getEntity() {
        return entity;
    }

    public AbstractOwnerState getOwner() {
        return owner;
    }

    public boolean isSuccess() {
        return success;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
