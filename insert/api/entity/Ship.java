package api.entity;

import api.universe.Universe;

public class Ship extends Entity {

    private Universe universe;
    private EntityAI entityAI;
    private float maxTurn;
    private float baseTurn;

    public Ship() {

    }

    private org.schema.game.common.controller.Ship getSMShip(Ship ship) {
        //ToDo:Figure out how to get a list of all entities on server and have this convert api ship entity to game ship entity
        return null;
    }

    public EntityAI getAI() {
        return entityAI;
    }

    public Universe getUniverse() {
        return universe;
    }

}
