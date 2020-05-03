package api.listener.events;

public abstract class Event {
    public boolean canceled = false;
    public boolean server = true;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isServer() { return server; }
}
