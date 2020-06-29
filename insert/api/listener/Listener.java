package api.listener;

import api.mod.StarMod;

public abstract class Listener<T> {
    private StarMod mod;
    public abstract void onEvent(T event);
    public void setMod(StarMod mod) {
        this.mod = mod;
    }
    public StarMod getMod() {
        return mod;
    }

}
