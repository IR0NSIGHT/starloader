package api.listener.events.controller.asteroid;

import api.listener.events.Event;
import org.schema.game.common.controller.generator.AsteroidCreatorThread;
import org.schema.game.server.controller.RequestData;

public class AsteroidGenerateEvent extends Event {
    private final AsteroidCreatorThread asteroidCreatorThread;
    private final RequestData requestData;

    public AsteroidGenerateEvent(AsteroidCreatorThread asteroidCreatorThread, RequestData requestData) {

        this.asteroidCreatorThread = asteroidCreatorThread;
        this.requestData = requestData;
    }

    public AsteroidCreatorThread getAsteroidCreatorThread() {
        return asteroidCreatorThread;
    }

    public RequestData getRequestData() {
        return requestData;
    }
}
