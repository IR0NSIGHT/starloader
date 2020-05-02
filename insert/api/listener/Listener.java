package api.listener;

import api.listener.events.Event;
import api.mod.StarMod;

public abstract class Listener {
    private StarMod mod;
    public abstract void onEvent(Event event);
    public void setMod(StarMod mod) {
        this.mod = mod;
    }

    public StarMod getMod() {
        return mod;
    }
}
