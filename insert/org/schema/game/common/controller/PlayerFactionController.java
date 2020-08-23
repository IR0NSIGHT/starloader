package org.schema.game.common.controller;

import api.listener.events.faction.FactionCreateEvent;
import api.listener.events.faction.FactionShareFOWEvent;
import api.listener.events.player.PlayerJoinFactionEvent;
import api.listener.events.player.PlayerLeaveFactionEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.schema.common.util.StringTools;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.FogOfWarController;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.SimplePlayerCommands;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionInvite;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.common.data.player.faction.FactionNewsPost;
import org.schema.game.common.data.player.faction.FactionNotFoundException;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.game.common.data.player.faction.FactionRelationOffer;
import org.schema.game.common.data.player.faction.FactionPermission.PermType;
import org.schema.game.common.data.player.faction.FactionRelation.RType;
import org.schema.game.common.data.player.faction.config.FactionActivityConfig;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.network.objects.NetworkClientChannel;
import org.schema.game.network.objects.NetworkPlayer;
import org.schema.game.network.objects.remote.RemoteFaction;
import org.schema.game.network.objects.remote.RemoteFactionNewsPost;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ProtectedUplinkName;
import org.schema.schine.common.language.Lng;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.remote.RemoteBoolean;
import org.schema.schine.network.objects.remote.RemoteIntegerArray;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.TagSerializable;
import org.schema.schine.resource.tag.Tag.Type;

public class PlayerFactionController extends Observable implements Observer, TagSerializable {
    public static final int MAX_NEWS_REQUEST_BATCH = 5;
    public static final String factionTagVersion0 = "pFac-v0";
    private static final int ENCO = -9278389;
    private final PlayerState playerState;
    private final List<String> descriptionEdits = new ObjectArrayList();
    private final List<String> chatMsgsRequests = new ObjectArrayList();
    private final List<Faction> factionsToAdd = new ObjectArrayList();
    private final List<int[]> factionChanges = new ObjectArrayList();
    private final Set<FactionInvite> invitesIncomingDisplayed = new HashSet();
    private final List<FactionInvite> invitesIncoming = new ObjectArrayList();
    private final List<FactionInvite> invitesOutgoing = new ObjectArrayList();
    private final List<FactionRelationOffer> relationshipInOffers = new ObjectArrayList();
    private final List<FactionRelationOffer> relationshipOutOffers = new ObjectArrayList();
    private final List<PlayerFactionController.ServerNewsRequest> serverNewsRequests = new ObjectArrayList();
    private final List<FactionChange> changedFactionFrom = new ObjectArrayList();
    private int factionId;
    private int openToJoinRequests = -1;
    private int attackNeutralRequests = -1;
    private int autoDeclareWarRequest = -1;
    private int flagLeave;
    private long lastFactionCreationTime;
    private boolean needsInviteReorganization = true;
    private FactionManager factionManager;
    private int flagJoin;
    private boolean needsRelationshipOfferReorganization = true;
    private String factionString = "";
    private boolean factionChanged;
    private boolean forcedJoin;
    private long lastFacActiveUpdate;
    private byte rank = 0;
    private boolean factionNewsReorganization;
    private long lastPerm;
    private int factionShareFowBuffer;
    private boolean checkedFleets;
    private int currentFactionEnc;
    private int suspended = 0;

    public PlayerFactionController(PlayerState var1) {
        this.playerState = var1;
    }

    public boolean chatClient(String var1) {
        this.playerState.getNetworkObject().factionChatRequests.add(new RemoteString(var1, this.playerState.getNetworkObject()));
        return true;
    }

    public void cleanUp() {
        if (this.playerState.isOnServer()) {
            ((GameServerState)this.playerState.getState()).getGameState().getFactionManager().deleteObserver(this);
        } else {
            ((GameClientState)this.playerState.getState()).getGameState().getFactionManager().deleteObserver(this);
        }
    }

    public void clientCreateFaction(String var1, String var2) {
        assert !this.isOnServer();

        if (System.currentTimeMillis() - this.lastFactionCreationTime > 10000L) {
            Faction var3 = new Faction(this.getState(), 0, var1, var2);
            this.playerState.getNetworkObject().factionCreateBuffer.add(new RemoteFaction(var3, this.playerState.getNetworkObject()));
            this.lastFactionCreationTime = System.currentTimeMillis();
            //INSERTED CODE @116
            Faction faction = GameServerState.instance.getFactionManager().getFaction(this.factionId);
            FactionCreateEvent factionCreateEvent = new FactionCreateEvent(faction, this.playerState);
            StarLoader.fireEvent(FactionCreateEvent.class, factionCreateEvent, true);
            //
        } else {
            ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_0, 0.0F);
        }
    }

    public boolean editDescriptionClient(String var1) {
        this.playerState.getNetworkObject().factionDescriptionEditRequest.add(new RemoteString(var1, this.playerState.getNetworkObject()));
        return true;
    }

    public void shareFow(int var1) {
        this.playerState.getNetworkObject().factionShareFowBuffer.add(var1);
        //INSERTED CODE @133
        Faction from = GameServerState.instance.getFactionManager().getFaction(this.factionId);
        Faction to = GameServerState.instance.getFactionManager().getFaction(var1);
        FactionShareFOWEvent factionShareFOWEvent = new FactionShareFOWEvent(from, to);
        StarLoader.fireEvent(FactionShareFOWEvent.class, factionShareFOWEvent, this.isOnServer());
        //
    }

    public void forceJoinOnServer(int var1) {
        this.flagJoin = var1;
        this.forcedJoin = true;
    }

    public void unsuspendFaction(GameServerState var1, PlayerState var2) {
        if (this.getSuspended() != 0 && var1.getFactionManager().existsFaction(this.getFactionId())) {
            String var3 = var2.getName() + "|" + this.suspended;
            String var4 = var2.getName();
            Faction var5;
            if ((var5 = var1.getFactionManager().getFaction(this.getSuspended())) == null) {
                System.err.println("[SERVER][UNSUSPENDFACTION] WARNING: faction the player was suspended from does no longer exist");
                this.suspended = 0;
            } else {
                FactionPermission var6;
                if ((var6 = (FactionPermission)var5.getMembersUID().get(var3)) == null) {
                    System.err.println("[SERVER][UNSUSPENDFACTION] WARNING: faction the player was suspended from removed the suspended membership. player needs to rejoin manually");
                    this.suspended = 0;
                } else {
                    Faction var7;
                    if ((var7 = var1.getFactionManager().getFaction(this.getFactionId())) != null) {
                        System.err.println("[SERVER][UNSUSPENDFACTION] Player was still in another faction. Player will be removed from " + var7 + " to rejoin " + var5);
                        var7.removeMember(var2.getName(), var1.getGameState());
                    }

                    var6.playerUID = var4;
                    var5.sendMemberNameChangeMod(var3, var4, var1.getGameState());
                    this.setFactionId(0);
                    this.needsInviteReorganization = true;
                    this.needsRelationshipOfferReorganization = true;
                    this.suspended = var5.getIdFaction();
                }
            }
        } else {
            System.err.println("[SERVER][UNSUSPENDFACTION] ERROR: player " + var2 + " is not suspended from any faction");
        }
    }

    public void suspendFaction(GameServerState var1, PlayerState var2) {
        if (this.getSuspended() == 0 && var1.getFactionManager().existsFaction(this.getFactionId())) {
            String var3 = var2.getName();
            String var5 = var2.getName() + "|" + this.suspended;
            Faction var4;
            ((FactionPermission)(var4 = var1.getFactionManager().getFaction(this.getFactionId())).getMembersUID().get(var3)).playerUID = var5;
            var4.sendMemberNameChangeMod(var3, var5, var1.getGameState());
            this.setFactionId(0);
            this.needsInviteReorganization = true;
            this.needsRelationshipOfferReorganization = true;
            this.suspended = var4.getIdFaction();
        } else {
            System.err.println("[SERVER][SUSPENDFACTION] ERROR: player " + var2 + " already suspended for faction id " + this.suspended);
        }
    }

    private int getSuspended() {
        return this.suspended;
    }

    public void fromTagStructure(Tag var1) {
        if ("pFac-v0".equals(var1.getName())) {
            Tag[] var2 = var1.getStruct();
            this.factionId = var2[0].getInt();
            if (var2[1].getType() == Type.INT) {
                this.suspended = var2[1].getInt();
            }

            System.err.println("[SERVER] loaded faction for " + this.playerState + " -> " + this.factionId);
        } else {
            System.err.println("[Player Tag Controller][ERROR] Unknown tag version " + var1.getName());
        }
    }

    public Tag toTagStructure() {
        Tag[] var1;
        (var1 = new Tag[3])[0] = new Tag(Type.INT, (String)null, this.factionId);
        var1[1] = new Tag(Type.INT, (String)null, this.suspended);
        var1[2] = FinishTag.INST;
        return new Tag(Type.STRUCT, "pFac-v0", var1);
    }

    public int getFactionId() {
        return this.factionId;
    }

    public void setFactionId(int var1) {
        int var2 = this.factionId;
        this.factionId = var1;
        this.currentFactionEnc = var1 + -9278389;
        if (!this.isOnServer() && var2 != this.factionId) {
            GameClientState var3 = (GameClientState)this.getState();
            if (this.playerState != var3.getPlayer() && this.factionId == var3.getPlayer().getFactionId() && this.factionId != 0) {
                var3.getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_1, new Object[]{this.playerState.getName()}), 0.0F);
            }

            if (this.playerState != var3.getPlayer() && var2 == var3.getPlayer().getFactionId() && var2 != 0) {
                var3.getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_2, new Object[]{this.playerState.getName()}), 0.0F);
            }

            this.factionChanged = true;
        } else if (this.isOnServer() && var2 != this.factionId) {
            this.factionChanged = true;
        }

        if (this.playerState.isClientOwnPlayer()) {
            System.err.println("VERIFY FACTION FOR " + this.playerState + ": " + var2 + " -> " + this.factionId);
            this.playerState.sendSimpleCommand(SimplePlayerCommands.VERIFY_FACTION_ID, new Object[]{this.factionId});
        }

        this.needsInviteReorganization = true;
        this.needsRelationshipOfferReorganization = true;
    }

    public String getFactionName() {
        if (this.factionId == 0) {
            return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_3;
        } else {
            return !((FactionState)this.getState()).getFactionManager().existsFaction(this.factionId) ? StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_4, new Object[]{this.factionId}) : ((FactionState)this.getState()).getFactionManager().getFaction(this.factionId).getName();
        }
    }

    public String getFactionString() {
        return this.factionString;
    }

    public List<FactionInvite> getInvitesIncoming() {
        return this.invitesIncoming;
    }

    public List<FactionInvite> getInvitesOutgoing() {
        return this.invitesOutgoing;
    }

    public FactionPermission getLoadedPermission() throws FactionNotFoundException {
        Faction var1;
        if ((var1 = ((GameServerState)this.getState()).getFactionManager().getFaction(this.factionId)) != null) {
            FactionPermission var2;
            if ((var2 = (FactionPermission)var1.getMembersUID().get(this.playerState.getName())) != null) {
                return var2;
            } else {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{87}, 1, this.playerState.getId()));
                throw new FactionNotFoundException(this.factionId);
            }
        } else {
            this.playerState.sendServerMessage(new ServerMessage(new Object[]{88}, 1, this.playerState.getId()));
            throw new FactionNotFoundException(this.factionId);
        }
    }

    public RType getRelation(int var1) {
        return this.factionManager != null ? this.factionManager.getRelation(this.playerState.getName().toLowerCase(Locale.ENGLISH), this.getFactionId(), var1) : RType.NEUTRAL;
    }

    public RType getRelation(PlayerState var1) {
        return this.factionManager != null ? this.getRelation(var1.getFactionId()) : RType.NEUTRAL;
    }

    public List<FactionRelationOffer> getRelationshipInOffers() {
        return this.relationshipInOffers;
    }

    public List<FactionRelationOffer> getRelationshipOutOffers() {
        return this.relationshipOutOffers;
    }

    private StateInterface getState() {
        return this.playerState.getState();
    }

    public void initFromNetworkObject(NetworkPlayer var1) {
        this.factionId = (Integer)var1.factionId.get();
    }

    public boolean isEnemy(PlayerState var1) {
        return this.factionManager != null ? this.factionManager.isEnemy(this.getFactionId(), var1) : false;
    }

    public boolean isFriend(PlayerState var1) {
        return this.factionManager != null ? this.factionManager.isFriend(this.getFactionId(), var1.getFactionId()) : false;
    }

    public boolean isNeutral(PlayerState var1) {
        return this.factionManager != null ? this.factionManager.isNeutral(this.getFactionId(), var1.getFactionId()) : false;
    }

    private boolean isOnServer() {
        return this.playerState.isOnServer();
    }

    public void joinFaction(int var1) {
        //INSERTED CODE @318
        PlayerJoinFactionEvent playerJoinFactionEvent = new PlayerJoinFactionEvent(GameServerState.instance.getFactionManager().getFaction(var1), this.playerState);
        StarLoader.fireEvent(PlayerJoinFactionEvent.class, playerJoinFactionEvent, this.isOnServer());
        //
        this.playerState.getNetworkObject().factionJoinBuffer.add(var1);
    }

    public void leaveFaction() {
        if (this.factionId != 0) {
            //INSERTED CODE @327
            PlayerLeaveFactionEvent playerLeaveFactionEvent = new PlayerLeaveFactionEvent(GameServerState.instance.getFactionManager().getFaction(this.playerState.getFactionId()), this.playerState);
            StarLoader.fireEvent(PlayerLeaveFactionEvent.class, playerLeaveFactionEvent, this.isOnServer());
            //
            this.playerState.getNetworkObject().factionLeaveBuffer.add(this.factionId);
        }

    }

    public boolean postNewsClient(String var1, String var2) {
        if (var1.length() > 0 && var2.length() > 0) {
            FactionNewsPost var3;
            (var3 = new FactionNewsPost()).set(this.getFactionId(), this.playerState.getName(), System.currentTimeMillis(), var1, var2, 0);
            ((GameClientState)this.playerState.getState()).getGameState().getNetworkObject().factionNewsPosts.add(new RemoteFactionNewsPost(var3, ((GameClientState)this.playerState.getState()).getGameState().getNetworkObject()));
            return true;
        } else {
            if (var1.length() == 0) {
                ((GameClientState)this.playerState.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_7, 0.0F);
            }

            if (var2.length() == 0) {
                ((GameClientState)this.playerState.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_8, 0.0F);
            }

            return false;
        }
    }

    public void removeNewsClient(FactionNewsPost var1) {
        var1.setDelete(true);
        ((GameClientState)this.playerState.getState()).getGameState().getNetworkObject().factionNewsPosts.add(new RemoteFactionNewsPost(var1, ((GameClientState)this.playerState.getState()).getGameState().getNetworkObject()));
    }

    public void queueNewsRequestOnServer(NetworkClientChannel var1, long var2) {
        PlayerFactionController.ServerNewsRequest var5 = new PlayerFactionController.ServerNewsRequest(var1, var2);
        synchronized(this.serverNewsRequests) {
            this.serverNewsRequests.add(var5);
        }
    }

    public void reorganizeFactionInvites() {
        this.getInvitesIncoming().clear();
        this.getInvitesOutgoing().clear();
        Iterator var1 = this.factionManager.getFactionInvitations().iterator();

        while(var1.hasNext()) {
            FactionInvite var2;
            if ((var2 = (FactionInvite)var1.next()).getToPlayerName().equals(this.playerState.getName())) {
                this.getInvitesIncoming().add(var2);
                if (this.playerState.isClientOwnPlayer() && !this.invitesIncomingDisplayed.contains(var2)) {
                    Faction var3 = this.factionManager.getFaction(var2.getFactionUID());
                    ((GameClientState)this.getState()).getController().popupInfoTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_9, new Object[]{var2.getFromPlayerName(), var3 != null ? var3.getName() : "ERROR(unknownFaction: " + var2.getFactionUID() + ")\n"}), 0.0F);
                    this.invitesIncomingDisplayed.add(var2);
                }
            }

            if (var2.getFromPlayerName().equals(this.playerState.getName())) {
                if (var2.getFactionUID() == this.factionId) {
                    this.getInvitesOutgoing().add(var2);
                } else {
                    this.factionManager.removeFactionInvitation(var2);
                }
            }
        }

        this.setChanged();
        this.notifyObservers();
    }

    private void reorganizeFactionRelationShipOffers() {
        this.getRelationshipInOffers().clear();
        this.getRelationshipOutOffers().clear();
        Iterator var1 = this.factionManager.getRelationShipOffers().values().iterator();

        while(var1.hasNext()) {
            FactionRelationOffer var2;
            if ((var2 = (FactionRelationOffer)var1.next()).b == this.getFactionId()) {
                this.getRelationshipInOffers().add(var2);
            }

            if (var2.a == this.getFactionId()) {
                this.getRelationshipOutOffers().add(var2);
            }
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void sendEntityFactionIdChangeRequest(int var1, SimpleTransformableSendableObject var2) {
        assert !this.playerState.isOnServer();

        RemoteIntegerArray var3;
        (var3 = new RemoteIntegerArray(2, this.playerState.getNetworkObject())).set(0, var1);
        var3.set(1, var2.getId());
        this.playerState.getNetworkObject().factionEntityIdChangeBuffer.add(var3);
    }

    public void sendEntityOwnerChangeRequest(String var1, SegmentController var2) {
        assert var1 != null : "may not be null. to clear owner, submit empty string";

        var2.getNetworkObject().currentOwnerChangeRequest.add(new RemoteString(var1, this.isOnServer()));
    }

    public void update(long var1) {
        if (this.factionManager == null && ((FactionState)this.playerState.getState()).getFactionManager() != null) {
            this.factionManager = ((FactionState)this.playerState.getState()).getFactionManager();
            this.factionManager.addObserver(this);
        }

        if (this.playerState.isClientOwnPlayer() && this.factionId != 0 && this.currentFactionEnc - -9278389 != this.factionId) {
            this.setFactionId(this.currentFactionEnc - -9278389);
        }

        if (this.factionManager != null && this.needsInviteReorganization) {
            this.reorganizeFactionInvites();
            this.needsInviteReorganization = false;
        }

        if (this.factionManager != null && this.needsRelationshipOfferReorganization) {
            this.reorganizeFactionRelationShipOffers();
            this.needsRelationshipOfferReorganization = false;
        }

        Faction var3;
        if (this.factionChanged && this.factionManager != null && !this.isOnServer()) {
            this.factionChanged = false;
            if ((var3 = this.factionManager.getFaction(this.factionId)) == null) {
                this.rank = 0;
                this.factionString = "";
            } else {
                this.factionString = "[" + var3.getName() + "]";
                if (var3.getMembersUID().containsKey(this.playerState.getName())) {
                    this.rank = ((FactionPermission)var3.getMembersUID().get(this.playerState.getName())).role;
                } else {
                    this.factionChanged = true;
                }
            }
        }

        if ((var3 = this.factionManager.getFaction(this.factionId)) != null && var3.getMembersUID().containsKey(this.playerState.getName())) {
            FactionPermission var5;
            (var5 = (FactionPermission)var3.getMembersUID().get(this.playerState.getName())).lastSeenPosition.set(this.playerState.getCurrentSector());
            var5.lastSeenTime = var1;
            this.rank = var5.role;
        }

        if (this.isOnServer()) {
            this.serverUpdate();
        } else {
            boolean var6 = (this.lastPerm & PermType.FOG_OF_WAR_SHARE.value) == PermType.FOG_OF_WAR_SHARE.value;
            boolean var4 = (this.getFactionPermission() & PermType.FOG_OF_WAR_SHARE.value) == PermType.FOG_OF_WAR_SHARE.value;
            if (this.factionNewsReorganization) {
                this.setChanged();
                this.notifyObservers();
                this.factionNewsReorganization = false;
            }

            if (!this.isOnServer() && this.getFactionId() != 0 && this.lastPerm != this.getFactionPermission() && (!var6 && var4 || var6 && !var4)) {
                ((GameClientState)this.getState()).getController().getClientChannel().getGalaxyManagerClient().resetClientVisibility();
            }

            if (this.factionShareFowBuffer != 0) {
                ((GameClientState)this.getState()).getController().getClientChannel().getGalaxyManagerClient().resetClientVisibility();
                this.factionShareFowBuffer = 0;
            }

            this.lastPerm = this.getFactionPermission();
        }
    }

    private void serverUpdate() {
        this.lastPerm = this.getFactionPermission();
        if (this.playerState.getClientChannel() != null && this.playerState.getClientChannel().isConnectionReady()) {
            while(!this.changedFactionFrom.isEmpty()) {
                this.onChangedFactionServer((FactionChange)this.changedFactionFrom.remove(0));
            }
        }

        FactionPermission var3;
        if (this.factionShareFowBuffer != 0) {
            if (!this.hasPermissionEditPermission()) {
                this.playerState.sendServerMessagePlayerError(new Object[]{89});
            } else {
                Faction var1;
                if ((var1 = ((FactionState)this.getState()).getFactionManager().getFaction(this.factionShareFowBuffer)) != null && this.getFactionFow() != null) {
                    var1.getFogOfWar().merge(this.factionManager.getFaction(this.factionId));
                    Iterator var2 = var1.getMembersUID().values().iterator();

                    while(var2.hasNext()) {
                        var3 = (FactionPermission)var2.next();

                        try {
                            ((GameServerState)this.getState()).getPlayerFromNameIgnoreCase(var3.playerUID).getNetworkObject().factionShareFowBuffer.add(this.factionShareFowBuffer);
                        } catch (PlayerNotFountException var9) {
                        }
                    }

                    FactionNewsPost var16;
                    (var16 = new FactionNewsPost()).set(this.getFactionId(), "Fog of war shared", System.currentTimeMillis(), "Fog of war shared", "Faction member " + this.playerState.getName() + " shared fog of war with faction " + var1.getName(), 0);
                    ((GameServerState)this.playerState.getState()).getFactionManager().addNewsPostServer(var16);
                    (var16 = new FactionNewsPost()).set(var1.getIdFaction(), "Fog of war information received", System.currentTimeMillis(), "Fog of war information received", "Your faction received fog of war information from faction " + this.getFactionName(), 0);
                    ((GameServerState)this.playerState.getState()).getFactionManager().addNewsPostServer(var16);
                }
            }

            this.factionShareFowBuffer = 0;
        }

        assert this.isOnServer();

        if (this.playerState.getAssingedPlayerCharacter() != null && this.playerState.getAssingedPlayerCharacter().getFactionId() != this.factionId) {
            this.playerState.getAssingedPlayerCharacter().setFactionId(this.factionId);
        }

        int var4;
        if (!this.factionChanges.isEmpty()) {
            synchronized(this.factionChanges) {
                while(true) {
                    while(!this.factionChanges.isEmpty()) {
                        int[] var17;
                        int var18 = (var17 = (int[])this.factionChanges.remove(0))[0];
                        var4 = var17[1];
                        if (var18 != this.getFactionId() && var18 != 0) {
                            System.err.println("[PlayerFactionController][SERVER][ERROR] cant change faction id of entity " + var18 + "/" + this.getFactionId());
                        } else {
                            Sendable var5 = (Sendable)this.playerState.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var4);
                            System.err.println("[SERVER] received object faction change request " + var18 + " for object " + var5);
                            String var6 = "Neutral";
                            Faction var7 = this.factionManager.getFaction(var18);
                            if (var18 != 0 && var7 == null) {
                                System.err.println("[PlayerFactionController][SERVER][ERROR] target factionId dos not exist " + var5 + "; target: " + var18);
                                var18 = 0;
                            }

                            if (var7 != null) {
                                var6 = var7.getName();
                            }

                            if (var5 != null && var5 instanceof SimpleTransformableSendableObject) {
                                SimpleTransformableSendableObject var19;
                                boolean var22;
                                if (!(var22 = (var19 = (SimpleTransformableSendableObject)var5) instanceof SegmentController && ((SegmentController)var19).isOwnerSpecific(this.playerState)) && var19 instanceof SegmentController && !((SegmentController)var19).isRankAllowedToChangeFaction(var18, this.playerState, this.rank)) {
                                    if (var19 instanceof Planet && var18 == 0) {
                                        this.playerState.sendServerMessagePlayerError(new Object[]{90});
                                    } else {
                                        this.playerState.sendServerMessagePlayerError(new Object[]{91});
                                    }
                                } else if (var19.getFactionId() == 0 || this.getFactionId() == var19.getFactionId() || !this.factionManager.existsFaction(var19.getFactionId()) || var22) {
                                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{92, var19.toNiceString(), var6}, 1, this.playerState.getId()));
                                    if (var19.getFactionId() != 0 && var18 == 0 && var19 instanceof SegmentController) {
                                        ((SegmentController)var19).railController.resetFactionForEntitiesWithoutFactionBlock(var19.getFactionId());
                                    } else {
                                        var19.setFactionId(var18);
                                    }

                                    if (var19 instanceof SegmentController && var18 == 0) {
                                        ((SegmentController)var19).currentOwnerLowerCase = "";
                                    }
                                }
                            } else {
                                System.err.println("[PlayerFactionController][SERVER][ERROR] cant change faction id of etity " + var5);
                            }
                        }
                    }
                }
            }
        }

        Iterator var25;
        if (!this.serverNewsRequests.isEmpty()) {
            synchronized(this.serverNewsRequests) {
                while(true) {
                    while(!this.serverNewsRequests.isEmpty()) {
                        PlayerFactionController.ServerNewsRequest var20 = (PlayerFactionController.ServerNewsRequest)this.serverNewsRequests.remove(0);
                        TreeSet var24 = (TreeSet)this.factionManager.getNews().get(this.factionId);
                        var4 = 0;
                        if (var24 != null && var24.size() > 0) {
                            var25 = var24.descendingSet().iterator();

                            while(var25.hasNext()) {
                                FactionNewsPost var36 = (FactionNewsPost)var25.next();
                                if (var4 >= 5) {
                                    break;
                                }

                                if (var20.req < 0L) {
                                    var20.networkClientChannel.factionNewsPosts.add(new RemoteFactionNewsPost(var36, var20.networkClientChannel));
                                } else if (var36.getDate() < var20.req) {
                                    var20.networkClientChannel.factionNewsPosts.add(new RemoteFactionNewsPost(var36, var20.networkClientChannel));
                                    ++var4;
                                }
                            }
                        } else {
                            System.err.println("[SERVER] " + this.playerState + " FactionNews for " + this.factionId + " not found!");
                        }
                    }
                }
            }
        }

        GameServerState var15 = (GameServerState)this.getState();
        int var21;
        Faction var27;
        if (this.factionId != 0) {
            var21 = this.factionId;
            this.rank = 0;
            if ((var27 = ((GameServerState)this.getState()).getFactionManager().getFaction(this.factionId)) == null) {
                System.err.println("[SERVER][PlayerFactionController][ERROR] faction of player " + this.playerState + " not found. setting to 0 from " + var21);
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{93}, 3, this.playerState.getId()));
                this.setFactionId(0);
            } else if (!var27.getMembersUID().containsKey(this.playerState.getName())) {
                System.err.println("[SERVER][PlayerFactionController][ERROR] faction " + var27 + " does not contain member " + this.playerState + ". setting to 0 from " + var21);
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{94}, 3, this.playerState.getId()));
                this.setFactionId(0);
            } else {
                this.rank = ((FactionPermission)var27.getMembersUID().get(this.playerState.getName())).role;
            }

            if (var21 != this.factionId) {
                FactionChange var26;
                (var26 = new FactionChange()).from = var21;
                var26.to = this.getFactionId();
                var26.previousPermission = this.lastPerm;
                this.changedFactionFrom.add(var26);
            }
        }

        if (!this.descriptionEdits.isEmpty()) {
            synchronized(this.descriptionEdits) {
                while(!this.descriptionEdits.isEmpty()) {
                    String var30 = (String)this.descriptionEdits.remove(0);

                    try {
                        if (this.getLoadedPermission().hasDescriptionAndNewsPostPermission(var15.getFactionManager().getFaction(this.factionId))) {
                            Faction var28;
                            (var28 = var15.getFactionManager().getFaction(this.factionId)).setDescription(var30);
                            var28.sendDescriptionMod(this.playerState.getName(), var30, var15.getGameState());
                        }
                    } catch (FactionNotFoundException var8) {
                        var8.printStackTrace();
                    }
                }
            }
        }

        if (!this.chatMsgsRequests.isEmpty()) {
            synchronized(this.chatMsgsRequests) {
                while(!this.chatMsgsRequests.isEmpty()) {
                    this.chatMsgsRequests.remove(0);
                }
            }
        }

        if (!this.factionsToAdd.isEmpty()) {
            var21 = this.factionId;
            Faction var29;
            synchronized(this.factionsToAdd) {
                for(; !this.factionsToAdd.isEmpty(); this.setFactionId(var29.getIdFaction())) {
                    (var29 = (Faction)this.factionsToAdd.remove(0)).setIdFaction(FactionManager.getNewId());
                    var15.getGameState().getFactionManager().addFaction(var29);
                    if (this.getFactionId() != 0) {
                        var15.getGameState().getFactionManager().removeMemberOfFaction(this.getFactionId(), this.playerState);
                    }
                }
            }

            if (var21 != this.factionId) {
                FactionChange var33;
                (var33 = new FactionChange()).from = var21;
                var33.to = this.getFactionId();
                var33.previousPermission = this.lastPerm;
                this.changedFactionFrom.add(var33);
            }
        }

        Faction var23;
        if (this.openToJoinRequests >= 0) {
            if ((var23 = var15.getGameState().getFactionManager().getFaction(this.factionId)) != null) {
                if ((var3 = (FactionPermission)var23.getMembersUID().get(this.playerState.getName())) != null) {
                    if (var3.hasInvitePermission(var23)) {
                        var23.setOpenToJoin(this.openToJoinRequests == 1);
                        var23.sendOpenToJoinMod(this.playerState.getName(), this.openToJoinRequests == 1, ((GameServerState)this.getState()).getGameState());
                    } else {
                        this.playerState.sendServerMessage(new ServerMessage(new Object[]{95}, 1, this.playerState.getId()));
                        System.err.println(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_16, new Object[]{this.playerState}));
                    }
                } else {
                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{96}, 1, this.playerState.getId()));
                    System.err.println("[SERVER][PlayerFaction] cannot change otj setting. not a member: " + this.playerState);
                }
            } else {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{97}, 1, this.playerState.getId()));
                System.err.println("[SERVER][PlayerFaction] cannot change otj setting. no faction for " + this.playerState);
            }

            this.openToJoinRequests = -1;
        }

        if (this.attackNeutralRequests >= 0) {
            if ((var23 = var15.getGameState().getFactionManager().getFaction(this.factionId)) != null) {
                if ((var3 = (FactionPermission)var23.getMembersUID().get(this.playerState.getName())) != null) {
                    if (var3.hasRelationshipPermission(var23)) {
                        var23.setAttackNeutral(this.attackNeutralRequests == 1);
                        var23.sendAttackNeutralMod(this.playerState.getName(), this.attackNeutralRequests == 1, ((GameServerState)this.getState()).getGameState());
                    } else {
                        this.playerState.sendServerMessage(new ServerMessage(new Object[]{98}, 1, this.playerState.getId()));
                        System.err.println("[SERVER][PlayerFaction] cannot change otj setting. no permission for " + this.playerState);
                    }
                } else {
                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{99}, 1, this.playerState.getId()));
                    System.err.println("[SERVER][PlayerFaction] cannot change otj setting. not a member: " + this.playerState);
                }
            } else {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{100}, 1, this.playerState.getId()));
                System.err.println("[SERVER][PlayerFaction] cannot change otj setting. no faction for " + this.playerState);
            }

            this.attackNeutralRequests = -1;
        }

        if (this.autoDeclareWarRequest >= 0) {
            if ((var23 = var15.getGameState().getFactionManager().getFaction(this.factionId)) != null) {
                if ((var3 = (FactionPermission)var23.getMembersUID().get(this.playerState.getName())) != null) {
                    if (var3.hasRelationshipPermission(var23)) {
                        var23.setAutoDeclareWar(this.autoDeclareWarRequest == 1);
                        var23.sendAutoDeclareWar(this.playerState.getName(), this.autoDeclareWarRequest == 1, ((GameServerState)this.getState()).getGameState());
                    } else {
                        this.playerState.sendServerMessage(new ServerMessage(new Object[]{101}, 1, this.playerState.getId()));
                        System.err.println("[SERVER][PlayerFaction] cannot change otj setting. no permission for " + this.playerState);
                    }
                } else {
                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{102}, 1, this.playerState.getId()));
                    System.err.println("[SERVER][PlayerFaction] cannot change otj setting. not a member: " + this.playerState);
                }
            } else {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{103}, 1, this.playerState.getId()));
                System.err.println("[SERVER][PlayerFaction] cannot change otj setting. no faction for " + this.playerState);
            }

            this.autoDeclareWarRequest = -1;
        }

        FactionChange var32;
        if (this.flagLeave != 0) {
            var21 = this.getFactionId();
            System.err.println("[FACTION] Player " + this.playerState + " is changing faction (leave) to " + this.flagJoin + "; current faction: " + this.factionId);
            String var31 = (var27 = this.factionManager.getFaction(this.flagLeave)) != null ? var27.getName() : "UNKNOWN(" + this.flagLeave + ")";
            this.playerState.sendServerMessage(new ServerMessage(new Object[]{104, var31}, 1, this.playerState.getId()));

            assert this.flagLeave == this.factionId : this.flagLeave + ", should be " + this.factionId;

            var15.getFactionManager().removeMemberOfFaction(this.factionId, this.playerState);
            this.setFactionId(0);
            this.flagLeave = 0;
            (var32 = new FactionChange()).from = var21;
            var32.to = this.getFactionId();
            var32.previousPermission = this.lastPerm;
            this.changedFactionFrom.add(var32);
        }

        if (this.flagJoin != 0) {
            var21 = this.getFactionId();
            System.err.println("[FACTION] Player " + this.playerState + " is changing faction (join) to " + this.flagJoin);
            var27 = this.factionManager.getFaction(this.flagJoin);
            FactionInvite var34 = null;
            if (var27 == null) {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{108}, 3, this.playerState.getId()));
            } else {
                boolean var35 = true;
                if (!var27.isOpenToJoin()) {
                    var35 = false;
                }

                Iterator var37 = this.getInvitesIncoming().iterator();

                while(var37.hasNext()) {
                    FactionInvite var38;
                    if ((var38 = (FactionInvite)var37.next()).getFactionUID() == var27.getIdFaction()) {
                        var35 = true;
                        var34 = var38;
                        System.err.println("[PlayerFactionController] FOUND INVITATION TO JOIN " + var38);
                        break;
                    }
                }

                if (!var35 && !this.forcedJoin) {
                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{107}, 3, this.playerState.getId()));
                } else {
                    if (this.flagJoin != this.getFactionId()) {
                        int var40 = this.getFactionId();
                        var27.addOrModifyMember(this.playerState.getName(), this.playerState.getName(), (byte)0, System.currentTimeMillis(), this.factionManager.getGameState(), true);
                        this.setFactionId(this.flagJoin);
                        if (var34 != null) {
                            System.err.println("[SERVER][PlayerFactionManager] removing taken invitation " + var34);
                            this.factionManager.removeFactionInvitation(var34);
                        }

                        if (var40 != 0) {
                            var15.getGameState().getFactionManager().removeMemberOfFaction(var40, this.playerState);
                        }
                    } else {
                        this.playerState.sendServerMessage(new ServerMessage(new Object[]{105}, 3, this.playerState.getId()));
                        if (var34 != null) {
                            this.factionManager.removeFactionInvitation(var34);
                        }
                    }

                    this.playerState.sendServerMessage(new ServerMessage(new Object[]{106, var27.getName()}, 1, this.playerState.getId()));
                }
            }

            this.flagJoin = 0;
            this.forcedJoin = false;
            (var32 = new FactionChange()).from = var21;
            var32.to = this.getFactionId();
            var32.previousPermission = this.lastPerm;
            this.changedFactionFrom.add(var32);
        }

        if (var15.getFactionManager().existsFaction((int)this.playerState.offlinePermssion[0]) && ((int)this.playerState.offlinePermssion[0] != this.getFactionId() || (this.playerState.offlinePermssion[1] & PermType.FOG_OF_WAR_SHARE.value) == PermType.FOG_OF_WAR_SHARE.value && (this.getFactionPermission() & PermType.FOG_OF_WAR_SHARE.value) != PermType.FOG_OF_WAR_SHARE.value)) {
            System.err.println("[SERVER] Merged Fog of War since rank/faction changed offline");
            this.playerState.getFogOfWar().merge(var15.getFactionManager().getFaction((int)this.playerState.offlinePermssion[0]));
        }

        this.playerState.offlinePermssion[0] = 0L;
        if (this.getFactionId() != 0 && (var23 = var15.getFactionManager().getFaction(this.getFactionId())) != null && (var3 = (FactionPermission)var23.getMembersUID().get(this.playerState.getName())) != null && (float)(System.currentTimeMillis() - this.playerState.getThisLogin()) > FactionActivityConfig.SET_ACTIVE_AFTER_ONLINE_FOR_MIN * 60.0F * 1000.0F && (!var3.isActiveMember() || System.currentTimeMillis() - this.lastFacActiveUpdate > 300000L)) {
            if (!var3.isActiveMember()) {
                this.playerState.sendServerMessage(new ServerMessage(new Object[]{109, FactionActivityConfig.SET_INACTIVE_AFTER_HOURS}, 1, this.playerState.getId()));
            }

            var3.activeMemberTime = System.currentTimeMillis();
            var23.addOrModifyMember("ADMIN", this.playerState.getName(), var3.role, var3.activeMemberTime, var15.getGameState(), true);
            this.lastFacActiveUpdate = System.currentTimeMillis();
            GameServerState var39 = (GameServerState)this.playerState.getState();
            if (this.playerState.getStarmadeName() != null && this.playerState.getStarmadeName().length() > 0) {
                var25 = var39.getProtectedUsers().entrySet().iterator();

                label319:
                while(true) {
                    Entry var42;
                    do {
                        do {
                            if (!var25.hasNext()) {
                                break label319;
                            }
                        } while(((String)(var42 = (Entry)var25.next()).getKey()).toLowerCase(Locale.ENGLISH).equals(this.playerState.getName().toLowerCase(Locale.ENGLISH)));
                    } while(!((ProtectedUplinkName)var42.getValue()).uplinkname.toLowerCase(Locale.ENGLISH).equals(this.playerState.getName().toLowerCase(Locale.ENGLISH)));

                    Iterator var41 = var15.getFactionManager().getFactionCollection().iterator();

                    while(var41.hasNext()) {
                        var41.next();
                        FactionPermission var43;
                        if ((var43 = (FactionPermission)var23.getMembersUID().get(var42.getKey())) != null && var43.isActiveMember()) {
                            var43.activeMemberTime = 0L;
                            var23.addOrModifyMember("ADMIN", var43.playerUID, var43.role, var43.activeMemberTime, var15.getGameState(), true);
                            this.playerState.sendServerMessage(new ServerMessage(new Object[]{110, var42.getKey()}, 3, this.playerState.getId()));
                        }
                    }
                }
            }
        }

        if (!this.checkedFleets) {
            ((GameServerState)this.playerState.getState()).getFleetManager().checkMemberFaction(this.playerState);
            this.checkedFleets = true;
        }

    }

    private void onChangedFactionServer(FactionChange var1) {
        ((GameServerState)this.playerState.getState()).getGameState().onFactionChangedServer(this.playerState, var1);
        ((GameServerState)this.playerState.getState()).getChannelRouter().onFactionChangedServer(this.playerState.getClientChannel());
        this.playerState.onChangedFactionServer(var1);
    }

    public void update(Observable var1, Object var2) {
        if ("INVITATION_UPDATE".equals(var2)) {
            this.needsInviteReorganization = true;
        } else if ("RELATIONSHIP_OFFER".equals(var2)) {
            this.needsRelationshipOfferReorganization = true;
        } else {
            if ("FACTION_NEWS_DELETED".equals(var2)) {
                this.factionNewsReorganization = true;
            }

        }
    }

    public void updateFromNetworkObject(NetworkPlayer var1) {
        int var8;
        if (!this.isOnServer()) {
            var8 = (Integer)var1.factionId.get();
            if (this.factionId != var8) {
                this.setFactionId(var8);
            }

        } else {
            int var2;
            if (this.factionId != 0) {
                for(var8 = 0; var8 < this.playerState.getNetworkObject().factionLeaveBuffer.getReceiveBuffer().size(); ++var8) {
                    var2 = this.playerState.getNetworkObject().factionLeaveBuffer.getReceiveBuffer().get(var8);
                    this.flagLeave = var2;
                }
            }

            boolean var9;
            for(var8 = 0; var8 < this.playerState.getNetworkObject().requestFactionOpenToJoin.getReceiveBuffer().size(); ++var8) {
                var9 = (Boolean)((RemoteBoolean)this.playerState.getNetworkObject().requestFactionOpenToJoin.getReceiveBuffer().get(var8)).get();
                this.openToJoinRequests = var9 ? 1 : 0;
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().requestAttackNeutral.getReceiveBuffer().size(); ++var8) {
                var9 = (Boolean)((RemoteBoolean)this.playerState.getNetworkObject().requestAttackNeutral.getReceiveBuffer().get(var8)).get();
                this.attackNeutralRequests = var9 ? 1 : 0;
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().requestAutoDeclareWar.getReceiveBuffer().size(); ++var8) {
                var9 = (Boolean)((RemoteBoolean)this.playerState.getNetworkObject().requestAutoDeclareWar.getReceiveBuffer().get(var8)).get();
                this.autoDeclareWarRequest = var9 ? 1 : 0;
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionJoinBuffer.getReceiveBuffer().size(); ++var8) {
                var2 = this.playerState.getNetworkObject().factionJoinBuffer.getReceiveBuffer().get(var8);
                this.flagJoin = var2;
            }

            String var10;
            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionChatRequests.getReceiveBuffer().size(); ++var8) {
                var10 = (String)((RemoteString)this.playerState.getNetworkObject().factionChatRequests.getReceiveBuffer().get(var8)).get();
                synchronized(this.chatMsgsRequests) {
                    this.chatMsgsRequests.add(var10);
                }
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionDescriptionEditRequest.getReceiveBuffer().size(); ++var8) {
                var10 = (String)((RemoteString)this.playerState.getNetworkObject().factionDescriptionEditRequest.getReceiveBuffer().get(var8)).get();
                synchronized(this.descriptionEdits) {
                    this.descriptionEdits.add(var10);
                }
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionShareFowBuffer.getReceiveBuffer().size(); ++var8) {
                var2 = this.playerState.getNetworkObject().factionShareFowBuffer.getReceiveBuffer().getInt(var8);
                this.factionShareFowBuffer = var2;
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionCreateBuffer.getReceiveBuffer().size(); ++var8) {
                Faction var3;
                (var3 = (Faction)((RemoteFaction)this.playerState.getNetworkObject().factionCreateBuffer.getReceiveBuffer().get(var8)).get()).getMembersUID().put(this.playerState.getName(), new FactionPermission(this.playerState, (byte)4, System.currentTimeMillis()));
                synchronized(this.factionsToAdd) {
                    this.factionsToAdd.add(var3);
                }
            }

            for(var8 = 0; var8 < this.playerState.getNetworkObject().factionEntityIdChangeBuffer.getReceiveBuffer().size(); ++var8) {
                RemoteIntegerArray var11 = (RemoteIntegerArray)this.playerState.getNetworkObject().factionEntityIdChangeBuffer.getReceiveBuffer().get(var8);
                synchronized(this.factionChanges) {
                    this.factionChanges.add(new int[]{(Integer)var11.get(0).get(), (Integer)var11.get(1).get()});
                }
            }

        }
    }

    public void updateToFullNetworkObject() {
        this.playerState.getNetworkObject().factionId.set(this.factionId);
    }

    public void updateToNetworkObject() {
        if (this.isOnServer()) {
            this.playerState.getNetworkObject().factionId.set(this.factionId);
        }

    }

    public byte getFactionRank() {
        return this.rank;
    }

    public String getFactionRankName(byte var1) {
        Faction var2;
        if ((var2 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId())) != null) {
            if (var1 >= 0 && this.getFactionId() > 0) {
                return var2.getRoles().getRoles()[var1].name;
            }

            if (var1 == -1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_32;
            }
        }

        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_PLAYERFACTIONCONTROLLER_33;
    }

    public long getFactionPermission() {
        Faction var1;
        if ((var1 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId())) != null) {
            if (this.rank >= 0 && this.getFactionId() > 0) {
                return var1.getRoles().getRoles()[this.rank].role;
            }

            if (this.rank == -1) {
                return FactionPermission.ADMIN_PERMISSIONS;
            }
        }

        return 0L;
    }

    public String getFactionOwnRankName() {
        return this.getFactionRankName(this.rank);
    }

    public float getFactionPoints() {
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) ? this.factionManager.getFaction(this.factionId).factionPoints : 0.0F;
    }

    public boolean hasRelationshipPermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasRelationshipPermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasDescriptionAndNewsPostPermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasRelationshipPermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasInvitePermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasInvitePermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasKickPermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasKickPermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasPermissionEditPermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasPermissionEditPermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasHomebaseEditPermission() {
        FactionPermission var1;
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) && (var1 = (FactionPermission)this.factionManager.getFaction(this.factionId).getMembersUID().get(this.playerState.getName())) != null ? var1.hasHomebasePermission(this.factionManager.getFaction(this.factionId)) : false;
    }

    public boolean hasHomebase() {
        if (this.factionManager != null && this.factionManager.existsFaction(this.factionId)) {
            return this.factionManager.getFaction(this.factionId).getHomebaseUID().length() > 0;
        } else {
            return false;
        }
    }

    public boolean isHomebase(SimpleTransformableSendableObject var1) {
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) ? this.factionManager.getFaction(this.factionId).getHomebaseUID().equals(var1.getUniqueIdentifier()) : false;
    }

    public FogOfWarController getFactionFow() {
        return this.factionManager != null && this.factionManager.existsFaction(this.factionId) ? this.factionManager.getFaction(this.factionId).getFogOfWar() : null;
    }

    class ServerNewsRequest {
        NetworkClientChannel networkClientChannel;
        long req;

        public ServerNewsRequest(NetworkClientChannel var2, long var3) {
            this.networkClientChannel = var2;
            this.req = var3;
        }
    }
}