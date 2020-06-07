package api.common;

import api.mod.StarLoader;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.world.Universe;
import org.schema.schine.network.objects.Sendable;

public class GameCommon {
    public Sendable getGameObject(int id){
        return StarLoader.getGameState().getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(id);
    }

}
