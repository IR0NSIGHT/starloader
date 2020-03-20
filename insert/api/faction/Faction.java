package api.faction;

import api.entity.Player;
import api.entity.Station;
import java.util.ArrayList;
import java.util.List;

public class Faction {

    private String name;
    private List<Player> members;
    private List<Player> activePlayers;
    private List<Player> inactivePlayers;
    private ArrayList<FactionRank> ranks;
    private int memberCount;
    private List<Faction> neutrals;
    private List<Faction> allies;
    private List<Faction> enemies;
    private List<Player> personalEnemies;
    private List<NewsPost> newsPosts;
    private Station homebase;
    private List<Station> claimStations;
    private System ownedSystems;
    private org.schema.game.common.data.player.faction.Faction internalFaction;

    public Faction(org.schema.game.common.data.player.faction.Faction internalFaction){
        this.internalFaction = internalFaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<Player> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public List<Player> getInactivePlayers() {
        return inactivePlayers;
    }

    public void setInactivePlayers(List<Player> inactivePlayers) {
        this.inactivePlayers = inactivePlayers;
    }

    public ArrayList<FactionRank> getRanks() {
        return ranks;
    }

    public void setRanks(ArrayList<FactionRank> ranks) {
        this.ranks = ranks;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public List<Faction> getNeutrals() {
        return neutrals;
    }

    public void setNeutrals(List<Faction> neutrals) {
        this.neutrals = neutrals;
    }

    public List<Faction> getAllies() {
        return allies;
    }

    public void setAllies(List<Faction> allies) {
        this.allies = allies;
    }

    public List<Faction> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Faction> enemies) {
        this.enemies = enemies;
    }

    public List<Player> getPersonalEnemies() {
        return personalEnemies;
    }

    public void setPersonalEnemies(List<Player> personalEnemies) {
        this.personalEnemies = personalEnemies;
    }

    public List<NewsPost> getNewsPosts() {
        return newsPosts;
    }

    public void setNewsPosts(List<NewsPost> newsPosts) {
        this.newsPosts = newsPosts;
    }

    public Station getHomebase() {
        return homebase;
    }

    public void setHomebase(Station homebase) {
        this.homebase = homebase;
    }

    public List<Station> getClaimStations() {
        return claimStations;
    }

    public void setClaimStations(List<Station> claimStations) {
        this.claimStations = claimStations;
    }

    public System getOwnedSystems() {
        return ownedSystems;
    }

    public void setOwnedSystems(System ownedSystems) {
        this.ownedSystems = ownedSystems;
    }
}
