package api.universe;

import api.entity.Entity;
import java.util.List;

public class Sector {

    private System system;
    private int[] coordinates = new int[3];
    private List<Entity> entites;

    public Sector(int[] coordinates, System system) {
        this.coordinates = coordinates;
        this.system = system;
        this.entites = getSMEntitiesFromCoord(coordinates);
    }

    public System getSystem() {
        return system;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public List<Entity> getEntites() {
        return entites;
    }

    private List<Entity> getSMEntitiesFromCoord(int[] coordinates) {
        return null; //Todo: Convert to SM sector, get all entities in said sector and convert them back to API entities.
    }
}