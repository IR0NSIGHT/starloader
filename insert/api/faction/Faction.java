package api.faction;

import api.entity.Player;
import api.entity.Station;
import api.main.GameServer;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.server.data.GameServerState;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Faction {

    private String name;
    private List<Player> members;
    private List<Player> activePlayers;
    private List<Player> inactivePlayers;
    private int memberCount;
    private Map<Player, FactionPermission> ranks;
    private List<Faction> allies;
    private List<Faction> enemies;
    private List<Player> personalEnemies;
    private List<NewsPost> newsPosts;
    private Station homebase;
    private List<Station> claimStations;
    private List<System> ownedSystems;
    private org.schema.game.common.data.player.faction.Faction internalFaction;

    public Faction(org.schema.game.common.data.player.faction.Faction internalFaction) throws IOException {
        this.internalFaction = internalFaction;

        //Name
        name = internalFaction.getName();

        //Member Stuff
        for(String name : internalFaction.getMembersUID().keySet()) {
            Player player = new Player(getPlayerStateFromName(name));
            members.add(player);
            if (internalFaction.getMembersUID().get(name).isActiveMember()) {
                activePlayers.add(player);
            } else {
                inactivePlayers.add(player);
            }
            memberCount = members.size();

            ranks.put(player, internalFaction.getMembersUID().get(name));
        }

        //Allies
        for(org.schema.game.common.data.player.faction.Faction internalAlly : internalFaction.getFriends()) {
            Faction faction = new Faction(internalAlly);
            allies.add(faction);
        }

        //Enemies
        for(org.schema.game.common.data.player.faction.Faction internalEnemy : internalFaction.getEnemies()) {
            Faction faction = new Faction(internalEnemy);
            enemies.add(faction);
        }

        //Personal Enemies
        for(String pName : internalFaction.getPersonalEnemies()) {
            personalEnemies.add(new Player(getPlayerStateFromName(pName)));
        }

        //News Posts
        //Todo:News Post Stuff

        //Home Base
        Vector3i internalHomeCoords = internalFaction.getHomeSector();
        Sector internalSector = GameServer.getServerState().getUniverse().getSector(internalHomeCoords, true);
        for(SimpleTransformableSendableObject internalEntity : internalSector.getEntities()) {
            if(internalEntity.isSegmentController() && internalEntity.getType() == SimpleTransformableSendableObject.EntityType.SPACE_STATION) {
                homebase = new Station((SegmentController) internalEntity);
            }
        }

        //Claim Stations
        //Todo: Claim Stations

        //Owned Systems
        //Todo: Owned Systems
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
}
