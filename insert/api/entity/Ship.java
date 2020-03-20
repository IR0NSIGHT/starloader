package api.entity;

public class Ship extends Entity {

    private EntityAI entityAI;
    private float maxTurn;
    private float baseTurn;
    private boolean turret;
    private Fleet fleet;
    private org.schema.game.common.controller.Ship internalShip;

    public Ship(org.schema.game.common.controller.Ship controller) {
        super(controller);
        this.setEntityType(EntityType.SHIP);
    }

    private org.schema.game.common.controller.Ship getSMShip() {
        return internalShip;
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

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public Player getPlayerControl(){
        return new Player(internalShip.getAttachedPlayers().get(0));
    }
}
