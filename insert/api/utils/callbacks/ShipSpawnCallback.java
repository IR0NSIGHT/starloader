package api.utils.callbacks;

import api.utils.StarRunnable;
import org.schema.game.common.controller.SegmentController;

public interface ShipSpawnCallback {
    void onShipSpawn(SegmentController controller);
}
