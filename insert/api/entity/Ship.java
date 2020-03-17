package api.entity;

import org.schema.game.common.controller.SegmentController;

public class Ship extends Entity {

    private EntityAI entityAI;
    private float maxTurn;
    private float baseTurn;
    private boolean turret;
    private org.schema.game.common.controller.Ship internalShip;

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

    public Fleet getFleet(){
        return Fleet.fromShip(this);
    }
    //temporary
    public static Ship fromSMShip(org.schema.game.common.controller.Ship controller){
        Ship ship = new Ship();
        ship.internalShip = controller;
        return ship;
    }

}
