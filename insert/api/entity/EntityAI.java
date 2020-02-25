package api.entity;

public class EntityAI {

    private Entity currentTarget;

    public EntityAI() {

    }

    public Entity getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Entity target) {
        this.currentTarget = target;
    }
}
