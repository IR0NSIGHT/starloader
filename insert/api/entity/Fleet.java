package api.entity;

import api.main.GameClient;
import api.main.GameServer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.fleet.FleetCommandTypes;
import org.schema.game.common.data.fleet.FleetManager;
import org.schema.game.common.data.fleet.FleetMember;
import org.schema.game.network.objects.remote.FleetCommand;
import org.schema.game.server.data.GameServerState;

import java.util.List;

public class Fleet {

    private org.schema.game.common.data.fleet.Fleet internalFleet;

    public Fleet(org.schema.game.common.data.fleet.Fleet internalFleet) {
        this.internalFleet = internalFleet;
    }

    public void addMember(Ship ship) {
        getServerFleetManager().requestShipAdd(internalFleet, ship.getInternalShip());
    }

    public void removeMember(Ship ship) {
        getServerFleetManager().requestFleetMemberRemove(internalFleet, new FleetMember(ship.getInternalShip());
    }

    public List<Ship> getMembers() {
        GameServerState gameServerState = GameServerState.instance;

        List<Ship> members = null;
        for(FleetMember fleetMember : internalFleet.getMembers()) {
            SegmentController internalShip = gameServerState.getSegmentControllersByName().get(fleetMember.name);
            members.add(new Ship(internalShip));
        }
        return members;
    }

    //Server Commands:
    public void moveTo(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MOVE_FLEET, internalFleet, new Vector3i(x,y,z)));
    }

    public void idle(){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.IDLE, internalFleet));
    }

    public void attack(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.FLEET_ATTACK, internalFleet, new Vector3i(x,y,z)));
    }

    public void mine(int x, int y, int z){
        getServerFleetManager().executeCommand(new FleetCommand(FleetCommandTypes.MINE_IN_SECTOR, internalFleet, new Vector3i(x,y,z)));
    }

    //Todo:Add more fleet commands

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

    //internal stuff -  public for now
    public static FleetManager getServerFleetManager(){
        return GameServer.getServerState().getFleetManager();
    }

    public static FleetManager getClientFleetManager(){
        return GameClient.getClientState().getFleetManager();
    }
}
