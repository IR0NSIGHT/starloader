package api.listener.events;

public abstract class Event {
    public static int idLog = 11;
    public boolean canceled = false;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
