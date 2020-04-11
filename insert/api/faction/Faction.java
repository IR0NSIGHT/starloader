package api.faction;

import api.entity.Player;
import api.entity.Station;
import api.main.GameServer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.data.GameServerState;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Faction {

    private org.schema.game.common.data.player.faction.Faction internalFaction;

    public Faction(org.schema.game.common.data.player.faction.Faction internalFaction) {
        this.internalFaction = internalFaction;
    }

    public org.schema.game.common.data.player.faction.Faction getInternalFaction() {
        return internalFaction;
    }

    public int getID() {
        return internalFaction.getIdFaction();
    }

    public String getName() {
        return internalFaction.getName();
    }

    public void setName(String name) {
        internalFaction.setName(name);
    }

    public List<Player> getMembers() {
        List<Player> members = null;
        GameServerState gameServerState = GameServerState.instance;
        for(String uid : internalFaction.getMembersUID().keySet()) {
            Player player = new Player(getPlayerStateFromUID(uid));
            members.add(player);
        }
        return members;
    }

    public void addMember(Player player) {
        player.getPlayerState().getFactionController().forceJoinOnServer(internalFaction.getIdFaction());
    }

    public void removeMember(Player player) {
        player.getPlayerState().getFactionController().leaveFaction();
    }

    public List<Player> getActiveMembers() {
        List<Player> activeMembers = null;
        for(Player player : getMembers()) {
            if(internalFaction.getMembersUID().get(player.getPlayerState().getUniqueIdentifier()).isActiveMember()) {
                activeMembers.add(player);
            }
        }
        return activeMembers;
    }

    public List<Player> getInactiveMembers() {
        List<Player> inactiveMembers = null;
        for(Player player : getMembers()) {
            if(!internalFaction.getMembersUID().get(player.getPlayerState().getUniqueIdentifier()).isActiveMember()) {
                inactiveMembers.add(player);
            }
        }
        return inactiveMembers;
    }

    public List<Faction> getAllies() throws IOException {
        List<Faction> allies = null;
        for(org.schema.game.common.data.player.faction.Faction internalAlly : internalFaction.getFriends()) {
            Faction faction = new Faction(internalAlly);
            allies.add(faction);
        }
        return allies;
    }

    public List<Faction> getEnemies() throws IOException {
        List<Faction> enemies = null;
        for(org.schema.game.common.data.player.faction.Faction internalEnemy : internalFaction.getEnemies()) {
            Faction faction = new Faction(internalEnemy);
            enemies.add(faction);
        }
        return enemies;
    }

    public void setAlly(Faction faction) {
        internalFaction.getFriends().add(faction.internalFaction);
    }

    public void setEnemy(Faction faction) {
        internalFaction.getEnemies().add(faction.internalFaction);
    }

    public List<Player> getPersonalEnemies() {
        List<Player> personalEnemies = null;
        for(String uid : internalFaction.getPersonalEnemies()) {
            personalEnemies.add(new Player(getPlayerStateFromUID(uid)));
        }

        return personalEnemies;
    }

    public Station getHomebase() throws IOException {
        Station homebase = null;
        Vector3i internalHomeCoords = internalFaction.getHomeSector();
        Sector internalSector = GameServer.getServerState().getUniverse().getSector(internalHomeCoords, true);
        for(SimpleTransformableSendableObject internalEntity : internalSector.getEntities()) {
            if(internalEntity.isSegmentController() && internalEntity.getType() == SimpleTransformableSendableObject.EntityType.SPACE_STATION) {
                homebase = new Station((SegmentController) internalEntity);
            }
        }
        return homebase;
    }

    private PlayerState getPlayerStateFromName(String playerName) {
        GameServerState gameServerState = GameServerState.instance;
        Map<String, PlayerState> playerStates = gameServerState.getPlayerStatesByName();
        PlayerState pState = null;
        try {
            pState = playerStates.get(playerName);
        } catch(Exception e) {
            System.err.println("[StarLoader API]: Tried to get a PlayerState from name, but specified player was not found on server!");
            e.printStackTrace();
        }
        return pState;
    }

    private PlayerState getPlayerStateFromUID(String UID) {
        GameServerState gameServerState = GameServerState.instance;
        Map<String, PlayerState> playerStates = gameServerState.getPlayerStatesByName();
        PlayerState pState = null;
        for(PlayerState playerState : playerStates.values()) {
            if(playerState.getUniqueIdentifier().equals(UID)) {
                pState = playerState;
            }
        }
        return pState;
    }
}
