package api.universe;

import api.faction.Faction;
import java.util.List;

public class System {

    private List<Sector> sectors;
    private Faction systemOwner = null;
    private int[] coordinates = new int[3];

    public System(int[] coordinates) {
        this.coordinates = coordinates;
        this.sectors = getSMSectorsFromCoord(coordinates);
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public Faction getSystemOwner() {
        return systemOwner;
    }

    public void setSystemOwner(Faction systemOwner) {
        this.systemOwner = systemOwner;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    private List<Sector> getSMSectorsFromCoord(int[] coordinates) {
        return null; //Todo: Convert to SM system, then get all sectors from it and then convert them back to API sectors.
    }
}
