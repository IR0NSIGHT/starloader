package api.utils.callbacks;

import api.entity.Entity;
import api.utils.StarRunnable;
import org.schema.game.common.controller.SegmentController;

public interface EntitySpawnCallback {
    void onEntitySpawn(Entity controller);
}
