package api.entity;

import java.util.ArrayList;

public class Fleet {

    private ArrayList<Ship> fleetMembers;
    private Ship fleetLeader = fleetMembers.get(0);

    public ArrayList<Ship> getFleetMembers() {
        return fleetMembers;
    }

    public void setFleetMembers(ArrayList<Ship> fleetMembers) {
        this.fleetMembers = fleetMembers;
    }

    public Ship getFleetLeader() {
        return fleetLeader;
    }

    public void setFleetLeader(Ship fleetLeader) {
        this.fleetLeader = fleetLeader;
    }
}
