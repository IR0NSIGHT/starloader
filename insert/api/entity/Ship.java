package api.entity;

public class Ship extends Entity {

    private EntityAI entityAI;
    private float maxTurn;
    private float baseTurn;
    private boolean turret;

    public Ship() {

    }

    private org.schema.game.common.controller.Ship getSMShip(Ship ship) {
        //ToDo:Figure out how to get a list of all entities on server and have this convert api ship entity to game ship entity
        return null;
    }

    public EntityAI getAI() {
        return entityAI;
    }

    public void setAI(EntityAI entityAI) {
        this.entityAI = entityAI;
    }

    public float getMaxTurn() {
        return maxTurn;
    }

    public void setMaxTurn(float maxTurn) {
        this.maxTurn = maxTurn;
    }

    public float getBaseTurn() {
        return baseTurn;
    }

    public void setBaseTurn(float baseTurn) {
        this.baseTurn = baseTurn;
    }

    public boolean isTurret() {
        return turret;
    }

    public void setTurret(boolean turret) {
        this.turret = turret;
    }

}
