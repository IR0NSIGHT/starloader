package api.entity;

import api.main.GameClient;
import api.main.GameServer;
import api.server.Server;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.fleet.FleetCommandTypes;
import org.schema.game.common.data.fleet.FleetManager;
import org.schema.game.network.objects.remote.FleetCommand;
import org.schema.game.server.data.simulation.npc.NPCFleetManager;

import java.util.ArrayList;

public class Fleet {

    private ArrayList<Ship> fleetMembers;
    private Ship fleetLeader = null;//fleetMembers.get(0);
    private org.schema.game.common.data.fleet.Fleet internalFleet;




    public ArrayList<Ship> getFleetMembers() {
        return fleetMembers;
    }
    public void addMember(org.schema.game.common.controller.Ship ship){
        getServerFleetManager().requestShipAdd(internalFleet, ship);
        //also:
        //internalFleet.addMemberFromEntity(ship);
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
    public Fleet(String name, String owner){
        internalFleet = createNewFleet(name, owner);
    }

    //Server Commands:
    public void moveTo(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MOVE_FLEET, internalFleet, new Vector3i(x,y,z)));
    }
    public void idle(){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MOVE_FLEET, internalFleet));
    }
    public void attack(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MOVE_FLEET, internalFleet, new Vector3i(x,y,z)));
    }
    public void mine(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MOVE_FLEET, internalFleet, new Vector3i(x,y,z)));
    }
    public void delete(){
        internalFleet.removeFleet(true);
    }

    //Trying to avoid these kind of Object arrays as parameters that schema has all over the place
    //Client commands:
    private void sendCommand(FleetCommandTypes types, Object... data){
        internalFleet.sendFleetCommand(types, data);
    }

    //garbage:
    public static org.schema.game.common.data.fleet.Fleet createNewFleet(String fleetName, String playerName){
        getServerFleetManager().requestCreateFleet(fleetName, playerName);
        ObjectArrayList<org.schema.game.common.data.fleet.Fleet> availableFleets = getServerFleetManager().getAvailableFleets(playerName);
        for (org.schema.game.common.data.fleet.Fleet fleet : availableFleets){
            if(fleet.getOwner().equals(playerName)){
                return fleet;
            }
        }
        System.err.println("!!! COULD NOT FIND FLEET THAT WAS JUST CREATED !!!");
        return null;
    }
    //Kindof a problem here that nothing is really defined as 'from client' or 'from server'
    public static Fleet fromShip(Ship ship) {
        //getServerFleetManager().getByEntity(ship.getSegmentController())
        return null;
    }
    public static org.schema.game.common.data.fleet.Fleet fromController(SegmentController controller){
        return controller.getFleet();
    }
    //internal stuff -  public for now
    public static FleetManager getServerFleetManager(){
        return GameServer.getServerState().getFleetManager();
    }
    public static FleetManager getClientFleetManager(){
        return GameClient.getClientState().getFleetManager();
    }
}
