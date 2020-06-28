//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.fleet;

import api.listener.events.controller.fleet.FleetCacheEvent;
import api.listener.events.controller.fleet.FleetUnCacheEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Observable;
import java.util.Map.Entry;
import org.schema.common.util.LogInterface.LogLevel;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.gui.fleet.FleetSelectionInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ai.AIConfiguationElements;
import org.schema.game.common.controller.ai.Types;
import org.schema.game.common.data.fleet.FleetModification.fleetmodType;
import org.schema.game.common.data.fleet.missions.MissionProgram;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.world.Sector;
import org.schema.game.network.objects.NetworkGameState;
import org.schema.game.network.objects.remote.FleetCommand;
import org.schema.game.network.objects.remote.RemoteFleet;
import org.schema.game.network.objects.remote.RemoteFleetBuffer;
import org.schema.game.network.objects.remote.RemoteFleetCommand;
import org.schema.game.network.objects.remote.RemoteFleetCommandBuffer;
import org.schema.game.network.objects.remote.RemoteFleetMod;
import org.schema.game.network.objects.remote.RemoteFleetModBuffer;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.simulation.npc.NPCFaction;
import org.schema.schine.ai.stateMachines.FSMException;
import org.schema.schine.ai.stateMachines.Transition;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.server.ServerStateInterface;

public class FleetManager extends Observable implements FleetSelectionInterface {
    public final Long2ObjectOpenHashMap<Fleet> fleetCache = new Long2ObjectOpenHashMap();
    public final Object2LongOpenHashMap<String> fleetsByName = new Object2LongOpenHashMap();
    public final Long2LongOpenHashMap fleetsByEntityDbId = new Long2LongOpenHashMap();
    public final Object2LongOpenHashMap<String> fleetsByUID = new Object2LongOpenHashMap();
    public final Object2ObjectOpenHashMap<String, LongOpenHashSet> fleetsByOwnerLowerCase = new Object2ObjectOpenHashMap();
    public final StateInterface state;
    private long selectedFleet = -1L;
    private final Long2ObjectOpenHashMap<FleetUnloadedAction> updateActionMap = new Long2ObjectOpenHashMap();
    private final Long2IntOpenHashMap updateActionMapCounter = new Long2IntOpenHashMap();
    private long lastFleetChachingCheck;
    private final LongOpenHashSet serverUncached = new LongOpenHashSet();

    public FleetManager(StateInterface var1) {
        this.state = var1;
        this.fleetsByEntityDbId.defaultReturnValue(-1L);
        this.updateActionMapCounter.defaultReturnValue(0);
    }

    public void update(Timer var1) {
        if (this.isOnServer()) {
            this.updateServer(var1);
        }

    }

    private void updateServer(Timer var1) {
        Iterator var2 = this.fleetCache.values().iterator();

        while(var2.hasNext()) {
            Fleet var3;
            (var3 = (Fleet)var2.next()).checkLoadedCommand(this);
            var3.updateDebug(this);

            try {
                var3.getCurrentProgram().update(var1);
            } catch (FSMException var7) {
                var7.printStackTrace();
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        ObjectIterator var10 = this.updateActionMap.entrySet().iterator();

        while(true) {
            while(var10.hasNext()) {
                Entry var11;
                FleetUnloadedAction var4 = (FleetUnloadedAction)(var11 = (Entry)var10.next()).getValue();
                long var5;
                if ((var5 = this.fleetsByEntityDbId.get((Long)var11.getKey())) != var4.fleet.dbid) {
                    if (var5 > 0L) {
                        try {
                            throw new Exception("Action doesn't correspond to a fleet " + var5 + "; " + var4.fleet.dbid);
                        } catch (Exception var9) {
                            var9.printStackTrace();
                        }
                    } else {
                        System.err.println("[SERVER][FLEETMANAGER][WARNING] action for no longer cached fleet (discarding) " + var4.fleet.dbid);
                    }

                    this.modAction(var4.fleet.dbid, -1);
                    var10.remove();
                } else if (this.fleetCache.containsKey(var4.fleet.dbid)) {
                    if (var4.execute(var1)) {
                        var10.remove();
                        this.updateActionMapCounter.get(var4.fleet.dbid);
                        this.modAction(var4.fleet.dbid, -1);
                    }
                } else {
                    this.modAction(var4.fleet.dbid, -1);
                    var10.remove();
                }
            }

            if (var1.currentTime - this.lastFleetChachingCheck > 60000L) {
                this.checkChacheStatus();
                this.lastFleetChachingCheck = var1.currentTime;
            }

            return;
        }
    }

    private void checkChacheStatus() {
        assert this.isOnServer();

        LongArrayList var1 = new LongArrayList();
        Iterator var2 = this.fleetCache.values().iterator();

        while(true) {
            while(true) {
                Fleet var3;
                Faction var4;
                do {
                    do {
                        if (!var2.hasNext()) {
                            var2 = var1.iterator();

                            while(var2.hasNext()) {
                                long var7 = (Long)var2.next();
                                Fleet var9 = (Fleet)this.fleetCache.get(var7);
                                FleetModification var6;
                                (var6 = new FleetModification()).type = fleetmodType.UNCACHE;
                                var6.fleetId = var9.dbid;
                                this.executeMod(var6);
                            }

                            return;
                        }

                        var3 = (Fleet)var2.next();
                    } while(((GameServerState)this.state).getPlayerStatesByNameLowerCase().containsKey(var3.getOwner().toLowerCase(Locale.ENGLISH)));
                } while(var3.isNPCFleet() && (var4 = ((FactionState)this.state).getFactionManager().getFaction(var3.getNpcFaction())) != null && var4 instanceof NPCFaction && ((NPCFaction)var4).isSystemActive(var3.getNpcSystem()));

                if (this.updateActionMapCounter.get(var3.dbid) > 0) {
                    if (var3.isNPCFleet() && (var4 = ((FactionState)this.state).getFactionManager().getFaction(var3.getNpcFaction())) != null && var4 instanceof NPCFaction) {
                        ((NPCFaction)var4).log("Not uncaching Fleet " + var3.getName() + " (existing mission) " + this.updateActionMapCounter.get(var3.dbid), LogLevel.DEBUG);
                    }
                } else {
                    boolean var8 = false;
                    Iterator var5 = var3.getMembers().iterator();

                    while(var5.hasNext()) {
                        if (((FleetMember)var5.next()).isLoaded()) {
                            var8 = true;
                            break;
                        }
                    }

                    if (!var8) {
                        var1.add(var3.dbid);
                    }
                }
            }
        }
    }

    public Fleet getByEntity(SegmentController var1) {
        Fleet var2;
        if ((var2 = this.getCachedByEntity(var1)) == null && this.isOnServer()) {
            if (var1.dbId > 0L && this.serverUncached.contains(var1.dbId)) {
                if ((var2 = ((GameServerState)this.state).getDatabaseIndex().getTableManager().getFleetTable().loadFleetByAnyEntityId(this.state, var1.dbId)) != null) {
                    this.cacheFleet(var2);
                } else {
                    this.serverUncached.add(var1.dbId);
                }

                return var2;
            } else {
                return null;
            }
        } else {
            return var2;
        }
    }

    public Fleet getByEntityDbId(long var1) {
        Fleet var3;
        if ((var3 = this.getCachedByEntityDbId(var1)) == null && this.isOnServer()) {
            if (var1 > 0L && this.serverUncached.contains(var1)) {
                if ((var3 = ((GameServerState)this.state).getDatabaseIndex().getTableManager().getFleetTable().loadFleetByAnyEntityId(this.state, var1)) != null) {
                    this.cacheFleet(var3);
                } else {
                    this.serverUncached.add(var1);
                }

                return var3;
            } else {
                return null;
            }
        } else {
            return var3;
        }
    }

    public Fleet getByFleetDbId(long var1) {
        Fleet var3;
        if ((var3 = (Fleet)this.fleetCache.get(var1)) == null && this.isOnServer()) {
            if (var1 > 0L) {
                if ((var3 = ((GameServerState)this.state).getDatabaseIndex().getTableManager().getFleetTable().loadFleetById(this.state, var1)) != null) {
                    this.cacheFleet(var3);
                }

                return var3;
            } else {
                return null;
            }
        } else {
            return var3;
        }
    }

    private void cacheFleetWithoutMembers(Fleet var1) {
        this.fleetCache.put(var1.dbid, var1);
        this.fleetsByName.put(var1.getName(), var1.dbid);
        LongOpenHashSet var2;
        if ((var2 = (LongOpenHashSet)this.fleetsByOwnerLowerCase.get(var1.getOwner().toLowerCase(Locale.ENGLISH))) == null) {
            var2 = new LongOpenHashSet();
            this.fleetsByOwnerLowerCase.put(var1.getOwner().toLowerCase(Locale.ENGLISH), var2);
        }

        var2.add(var1.dbid);
    }

    public void initializeFleet(Fleet var1) {
        if (var1.getCurrentProgram() == null) {
            var1.setCurrentProgram(new MissionProgram(var1, false));
        }

    }

    private void cacheFleet(Fleet fleet) {
        this.initializeFleet(fleet);
        this.cacheFleetWithoutMembers(fleet);
        Iterator var2 = fleet.getMembers().iterator();

        while(var2.hasNext()) {
            FleetMember var3 = (FleetMember)var2.next();
            this.cacheMember(fleet, var3.UID, var3.entityDbId);
        }

        this.setChanged();
        this.notifyObservers(fleet);
        //INSERTED CODE
        FleetCacheEvent event = new FleetCacheEvent(this, fleet);
        StarLoader.fireEvent(event, isOnServer());
        if(event.isCanceled()) return;
        ///
    }

    private void saveCommandInDb(Fleet var1, FleetCommand var2) {
        assert this.isOnServer();

        var1.setCurrentCommand(var2);

        try {
            ((GameServerState)var1.getState()).getDatabaseIndex().getTableManager().getFleetTable().updateFleetCommand(var1.dbid, var2);
        } catch (SQLException var3) {
            var3.printStackTrace();
        }
    }

    private void cacheMember(Fleet var1, String var2, long var3) {
        this.serverUncached.remove(var3);
        this.fleetsByUID.put(var2, var1.dbid);
        this.fleetsByEntityDbId.put(var3, var1.dbid);
    }

    private void uncacheFleet(Fleet fleet) {
        //INSERTED CODE
        FleetUnCacheEvent event = new FleetUnCacheEvent(this, fleet);
        StarLoader.fireEvent(event, isOnServer());
        if(event.isCanceled()) return;
        ///
        this.uncacheFleetWithoutMembers(fleet);

        for (FleetMember var3 : fleet.getMembers()) {
            this.uncacheMember(var3.UID, var3.entityDbId);
        }

        if (fleet.dbid == this.selectedFleet) {
            this.selectedFleet = -1L;
        }

        Faction var4;
        if (fleet.isNPCFleet() && fleet.isNPCFleet() && (var4 = ((FactionState)this.state).getFactionManager().getFaction(fleet.getNpcFaction())) != null && var4 instanceof NPCFaction) {
            ((NPCFaction)var4).onUncachedFleet(fleet);
        }

        this.setChanged();
        this.notifyObservers();
    }

    private void uncacheFleetWithoutMembers(Fleet var1) {
        this.fleetCache.remove(var1.dbid);
        this.fleetsByName.remove(var1.getName());
        LongOpenHashSet var2;
        if ((var2 = (LongOpenHashSet)this.fleetsByOwnerLowerCase.get(var1.getOwner().toLowerCase(Locale.ENGLISH))) != null) {
            var2.remove(var1.dbid);
            if (var2.isEmpty()) {
                this.fleetsByOwnerLowerCase.remove(var1.getOwner().toLowerCase(Locale.ENGLISH));
            }
        }

    }

    private void uncacheMember(String var1, long var2) {
        this.fleetsByUID.remove(var1);
        this.fleetsByEntityDbId.remove(var2);
    }

    public boolean isOnServer() {
        return this.state instanceof ServerStateInterface;
    }

    public void updateFromNetworkObject(NetworkGameState var1) {
        boolean var2 = false;
        RemoteFleetBuffer var3 = var1.fleetBuffer;

        for(int var4 = 0; var4 < var3.getReceiveBuffer().size(); ++var4) {
            RemoteFleet var5 = (RemoteFleet)var3.getReceiveBuffer().get(var4);
            Fleet var6;
            if ((var6 = (Fleet)this.fleetCache.get(((Fleet)var5.get()).dbid)) == null) {
                this.cacheFleet((Fleet)var5.get());
            } else if (!var6.equals(var5.get())) {
                Iterator var8 = var6.getMembers().iterator();

                while(var8.hasNext()) {
                    FleetMember var7 = (FleetMember)var8.next();
                    this.uncacheMember(var7.UID, var7.entityDbId);
                }

                var6.apply((Fleet)var5.get());
                this.cacheFleet(var6);
            }

            var2 = true;
        }

        RemoteFleetModBuffer var10 = var1.fleetModBuffer;

        for(int var11 = 0; var11 < var10.getReceiveBuffer().size(); ++var11) {
            RemoteFleetMod var13 = (RemoteFleetMod)var10.getReceiveBuffer().get(var11);
            this.executeMod((FleetModification)var13.get());
            var2 = true;
        }

        RemoteFleetCommandBuffer var12 = var1.fleetCommandBuffer;

        for(int var14 = 0; var14 < var12.getReceiveBuffer().size(); ++var14) {
            RemoteFleetCommand var9 = (RemoteFleetCommand)var12.getReceiveBuffer().get(var14);
            this.executeCommand((FleetCommand)var9.get());
            var2 = true;
        }

        if (var2) {
            this.setChanged();
            this.notifyObservers();
        }

    }

    public void sendFleetCommand(FleetCommand var1) {
        ((GameStateInterface)this.state).getGameState().getNetworkObject().fleetCommandBuffer.add(new RemoteFleetCommand(var1, this.isOnServer()));
    }

    public void executeCommand(FleetCommand var1) {
        FleetCommandTypes var2 = FleetCommandTypes.values()[var1.getCommand()];

        assert var1.fleetDbId > -1L;

        Fleet var3;
        if ((var3 = (Fleet)this.fleetCache.get(var1.fleetDbId)) == null) {
            System.err.println("[SERVER][ERROR] Fleet not found: " + var1.fleetDbId + "; " + this.fleetCache);
        } else {
            switch(var2) {
                case IDLE:
                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var19) {
                        var19.printStackTrace();
                    }
                    break;
                case MOVE_FLEET:
                    Vector3i var4;
                    if ((var4 = (Vector3i)var1.getArgs()[0]).x == -2147483648 && var4.y == -2147483648 && var4.z == -2147483648) {
                        var3.removeCurrentMoveTarget();

                        try {
                            var3.getCurrentProgram().getMachine().getFsm().stateTransition(Transition.RESTART);
                        } catch (FSMException var18) {
                            var18.printStackTrace();
                        }
                    } else {
                        var3.setCurrentMoveTarget(var4);

                        try {
                            var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                        } catch (FSMException var17) {
                            var17.printStackTrace();
                        }
                    }
                    break;
                case PATROL_FLEET:
                    var3.setCurrentMoveTarget((Vector3i)var1.getArgs()[0]);

                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var16) {
                        var16.printStackTrace();
                    }
                    break;
                case TRADE_FLEET:
                    var3.setCurrentMoveTarget((Vector3i)var1.getArgs()[0]);

                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var15) {
                        var15.printStackTrace();
                    }
                    break;
                case CALL_TO_CARRIER:
                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var14) {
                        var14.printStackTrace();
                    }
                    break;
                case FLEET_ATTACK:
                    var3.setCurrentMoveTarget((Vector3i)var1.getArgs()[0]);

                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var13) {
                        var13.printStackTrace();
                    }
                    break;
                case FLEET_DEFEND:
                    var3.setCurrentMoveTarget((Vector3i)var1.getArgs()[0]);

                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var12) {
                        var12.printStackTrace();
                    }
                    break;
                case FLEET_IDLE_FORMATION:
                    try {
                        if (ServerConfig.ALLOW_FLEET_FORMATION.isOn()) {
                            var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                        } else {
                            try {
                                ((GameServerState)this.state).getPlayerFromNameIgnoreCase(var3.getOwner()).sendServerMessagePlayerError(new Object[]{191});
                            } catch (PlayerNotFountException var10) {
                                var10.printStackTrace();
                            }
                        }
                    } catch (FSMException var11) {
                        var11.printStackTrace();
                    }
                    break;
                case SENTRY:
                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var9) {
                        var9.printStackTrace();
                    }
                    break;
                case MINE_IN_SECTOR:
                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var8) {
                        var8.printStackTrace();
                    }
                    break;
                case CLOAK:
                case UNCLOAK:
                case JAM:
                case UNJAM:
                    try {
                        var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                    } catch (FSMException var7) {
                        var7.printStackTrace();
                    }
                    break;
                case SENTRY_FORMATION:
                    try {
                        if (ServerConfig.ALLOW_FLEET_FORMATION.isOn()) {
                            var3.getCurrentProgram().getMachine().getFsm().stateTransition(var2.transition);
                        } else {
                            try {
                                ((GameServerState)this.state).getPlayerFromNameIgnoreCase(var3.getOwner()).sendServerMessagePlayerError(new Object[]{192});
                            } catch (PlayerNotFountException var5) {
                                var5.printStackTrace();
                            }
                        }
                    } catch (FSMException var6) {
                        var6.printStackTrace();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unkonwn command: " + var2);
            }

            if (this.isOnServer() && var1 != var3.loadedCommand) {
                this.saveCommandInDb(var3, var1);
            }

        }
    }

    private Fleet getCachedByEntityDbId(long var1) {
        long var3;
        return (var3 = this.fleetsByEntityDbId.get(var1)) > 0L ? (Fleet)this.fleetCache.get(var3) : null;
    }

    private Fleet getCachedByEntity(SegmentController var1) {
        long var2;
        if (var1.dbId < 0L) {
            return (var2 = this.fleetsByUID.getLong(var1.getUniqueIdentifier())) > 0L ? (Fleet)this.fleetCache.get(var2) : null;
        } else {
            return (var2 = this.fleetsByEntityDbId.get(var1.dbId)) > 0L ? (Fleet)this.fleetCache.get(var2) : null;
        }
    }

    public void onJoinedPlayer(PlayerState var1) {
        assert this.isOnServer();

        this.loadByOwner(var1.getName().toLowerCase(Locale.ENGLISH));
        ((FactionState)var1.getState()).getFactionManager();
    }

    public void checkMemberFaction(PlayerState var1) {
        Iterator var2 = this.getAvailableFleets(var1.getName().toLowerCase(Locale.ENGLISH)).iterator();

        while(var2.hasNext()) {
            Fleet var3 = (Fleet)var2.next();
            boolean var4 = false;
            Iterator var5 = (new ObjectArrayList(var3.getMembers())).iterator();

            while(var5.hasNext()) {
                FleetMember var6;
                if ((var6 = (FleetMember)var5.next()).getFactionId() != 0 && var6.getFactionId() != var1.getFactionId()) {
                    var3.removeMemberByDbIdUID(var6.entityDbId, false);
                    var4 = true;
                }
            }

            if (var4) {
                var3.sendFleet();
            }
        }

    }

    public void loadByOwner(String var1) {
        assert this.isOnServer();

        Iterator var3 = ((GameServerState)this.state).getDatabaseIndex().getTableManager().getFleetTable().loadFleetByOwner(this.state, var1.toLowerCase(Locale.ENGLISH)).iterator();

        while(var3.hasNext()) {
            Fleet var2 = (Fleet)var3.next();

            assert var2 != null;

            if (!this.fleetCache.containsKey(var2.dbid)) {
                this.cacheFleet(var2);
                var2.sendFleet();
            }
        }

    }

    public void unloadByOwner(String var1) {
        assert this.isOnServer();

        LongOpenHashSet var5;
        if ((var5 = (LongOpenHashSet)this.fleetsByOwnerLowerCase.get(var1.toLowerCase(Locale.ENGLISH))) != null) {
            Iterator var6 = (new LongOpenHashSet(var5)).iterator();

            while(var6.hasNext()) {
                long var3 = (Long)var6.next();
                Fleet var2;
                (var2 = (Fleet)this.fleetCache.get(var3)).save();
                this.uncacheFleet(var2);
            }
        }

    }

    public void onLeftPlayer(PlayerState var1) {
        assert this.isOnServer();

    }

    public void onAddedEntity(SegmentController var1) {
        assert this.isOnServer();

        assert !(var1 instanceof Ship) || var1.isVirtualBlueprint() || var1.dbId > 0L : var1;

        if (var1.dbId >= 0L) {
            Fleet var2;
            if ((var2 = this.getByEntity(var1)) != null) {
                for(int var3 = 0; var3 < var2.getMembers().size(); ++var3) {
                    if (((FleetMember)var2.getMembers().get(var3)).getLoaded() == var1) {
                        FleetMember var4 = (FleetMember)var2.getMembers().get(var3);
                        Sector var5;
                        if ((var5 = ((GameServerState)this.state).getUniverse().getSector(var1.getSectorId())) != null && !var4.getSector().equals(var5.pos)) {
                            var4.getSector().set(var5.pos);
                            this.submitSectorChangeToClients(var4);
                        }
                        break;
                    }
                }
            }

            if (this.isOnServer() && var1.getRuleEntityManager() != null) {
                var1.getRuleEntityManager().triggerOnFleetChange();
            }

        }
    }

    public void onRemovedEntity(long var1) {
        Sendable var3;
        if ((var3 = (Sendable)this.state.getLocalAndRemoteObjectContainer().getDbObjects().get(var1)) != null && var3 instanceof SegmentController) {
            this.onRemovedEntity((SegmentController)var3);
        } else {
            long var4 = this.fleetsByEntityDbId.get(var1);
            Fleet var7;
            if ((var7 = this.getByEntityDbId(var4)) != null) {
                FleetMember var6;
                if ((var6 = var7.removeMemberByDbIdUID(var1, false)) != null) {
                    if (var6.getLoaded() != null && this.isOnServer() && var6.getLoaded().getRuleEntityManager() != null) {
                        var6.getLoaded().getRuleEntityManager().triggerOnFleetChange();
                    }

                    this.uncacheMember(var6.UID, var6.entityDbId);
                }

                var7.sendFleet();
            }

        }
    }

    public void onRemovedEntity(SegmentController var1) {
        Fleet var2;
        if ((var2 = this.getCachedByEntity(var1)) != null) {
            var2.onUnloadedEntity(var1);
            if (var1.isMarkedForPermanentDelete() && var2 != null) {
                FleetMember var3;
                if ((var3 = var2.removeMemberByUID(var1.getUniqueIdentifier())) != null) {
                    this.uncacheMember(var3.UID, var3.entityDbId);
                }

                var2.sendFleet();
            }

            if (this.isOnServer() && var1.getRuleEntityManager() != null) {
                var1.getRuleEntityManager().triggerOnFleetChange();
            }
        }

    }

    public void updateToFullNetworkObject(NetworkGameState var1) {
        Iterator var2 = this.fleetCache.values().iterator();

        while(var2.hasNext()) {
            Fleet var3 = (Fleet)var2.next();
            var1.fleetBuffer.add(new RemoteFleet(var3, this.isOnServer()));
        }

    }

    private void sendMod(FleetModification var1) {
        ((GameStateInterface)this.state).getGameState().getNetworkObject().fleetModBuffer.add(new RemoteFleetMod(var1, this.isOnServer()));
    }

    public Collection<Fleet> getAvailableFleetsClient() {
        String var1 = ((GameClientState)this.state).getPlayer().getName().toLowerCase(Locale.ENGLISH);
        LongOpenHashSet var6 = (LongOpenHashSet)this.fleetsByOwnerLowerCase.get(var1);
        ObjectArrayList var2 = new ObjectArrayList();
        if (var6 != null) {
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
                long var4 = (Long)var7.next();
                Fleet var3;
                if ((var3 = (Fleet)this.fleetCache.get(var4)) != null) {
                    var2.add(var3);
                }
            }
        }

        return var2;
    }

    public ObjectArrayList<Fleet> getAvailableFleets(String var1) {
        LongOpenHashSet var6 = (LongOpenHashSet)this.fleetsByOwnerLowerCase.get(var1.toLowerCase(Locale.ENGLISH));
        ObjectArrayList var2 = new ObjectArrayList();
        if (var6 != null) {
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
                long var4 = (Long)var7.next();
                Fleet var3;
                if ((var3 = (Fleet)this.fleetCache.get(var4)) != null) {
                    var2.add(var3);
                }
            }
        }

        return var2;
    }

    private void executeMod(FleetModification var1) {
        Fleet var2;
        FleetMember var3;
        Fleet var4;
        Fleet var11;
        switch(var1.type) {
            case ADD_MEMBER:
                assert this.isOnServer();

                var2 = (Fleet)this.fleetCache.get(var1.fleetId);

                assert !this.fleetsByEntityDbId.containsKey(var1.entityDBId) : "Ship already part of fleet: " + this.fleetsByEntityDbId.get(var1.entityDBId) + "; wanted to join: " + var1.fleetId;

                Sendable var14 = (Sendable)this.state.getLocalAndRemoteObjectContainer().getDbObjects().get(var1.entityDBId);
                if (var2 != null) {
                    if (var14 != null && var14 instanceof SegmentController) {
                        System.err.println("[SERVER][FLEETMANAGER] adding (loaded entity) fleet member: " + var14 + " to " + var2);
                        var2.addMemberFromEntity((SegmentController)var14);
                        this.cacheMember(var2, ((SegmentController)var14).getUniqueIdentifier(), ((SegmentController)var14).dbId);
                    } else if ((var3 = var2.addMemberFromDBID(var1.entityDBId)) != null) {
                        System.err.println("[SERVER][FLEETMANAGER] adding (from db entity) fleet member: " + var3 + " to " + var2);
                        this.cacheMember(var2, var3.UID, var1.entityDBId);
                    }

                    var2.save();
                    return;
                }
                break;
            case COMMAND:
                return;
            case CREATE:
                (var11 = new Fleet(this.state)).setName(var1.name);
                var11.setOwner(var1.owner);
                var11.save();
                this.cacheFleet(var11);
                System.err.println("[FLEET][CREATE] " + this.state + " CREATED FLEET: " + var1.name + "; CURRENTLY CACHED FLEETS FOR " + var1.owner + ": " + this.fleetsByOwnerLowerCase.get(var1.owner.toLowerCase(Locale.ENGLISH)));
                var11.sendFleet();
                return;
            case DELETE_FLEET:
                if ((var2 = (Fleet)this.fleetCache.get(var1.fleetId)) != null) {
                    Iterator var13 = var2.getMembers().iterator();

                    while(var13.hasNext()) {
                        ((FleetMember)var13.next()).onRemovedFromFleet();
                    }

                    this.uncacheFleet(var2);
                    if (this.isOnServer()) {
                        this.sendMod(var1);
                    }
                }

                if (this.isOnServer()) {
                    this.deleteFleetFromDatabase(var1.fleetId);
                    return;
                }
                break;
            case UNCACHE:
                if ((var11 = (Fleet)this.fleetCache.get(var1.fleetId)) != null) {
                    if (this.isOnServer()) {
                        var11.save();
                    }

                    this.uncacheFleet(var11);
                    if (this.isOnServer()) {
                        this.sendMod(var1);
                        return;
                    }
                }
                break;
            case MISSION_UPDATE:
                if ((var4 = (Fleet)this.fleetCache.get(var1.fleetId)) != null) {
                    var4.missionString = var1.missionString;
                    this.setChanged();
                    this.notifyObservers();
                    return;
                }
                break;
            case REMOVE_MEMBER:
                System.err.println("[FLEET] REMOVING MEMEBER REQUEST (EXEC ON SERVER): " + var1);
                FleetMember var8;
                if ((var2 = (Fleet)this.fleetCache.get(var1.fleetId)) != null && (var8 = var2.removeMemberByDbIdUID(var1.entityDBId, false)) != null) {
                    this.uncacheMember(var8.UID, var8.entityDbId);
                    if (var8.isLoaded()) {
                        ((AIConfiguationElements<Boolean>) ((Ship)var8.getLoaded()).getAiConfiguration().get(Types.ACTIVE))
                                .setCurrentState(false, true);
                        return;
                    }
                }
                break;
            case RENAME:
                return;
            case SECTOR_CHANGE:
                assert !this.isOnServer();

                long var5 = this.fleetsByEntityDbId.get(var1.entityDBId);
                var2 = (Fleet)this.fleetCache.get(var5);
                boolean var10 = false;
                if (var2 != null) {
                    for(int var12 = 0; var12 < var2.getMembers().size(); ++var12) {
                        if (((FleetMember)var2.getMembers().get(var12)).entityDbId == var1.entityDBId) {
                            ((FleetMember)var2.getMembers().get(var12)).getSector().set(var1.sector);
                            var10 = true;
                            break;
                        }
                    }
                }

                assert !this.isOnServer() || var10 : "Fleet not found by entityDBID: " + var1.entityDBId + "; " + this.fleetsByEntityDbId;
                break;
            case MOVE_MEMBER:
                if ((var4 = (Fleet)this.fleetCache.get(var1.fleetId)) != null) {
                    for(int var9 = 0; var9 < var4.getMembers().size(); ++var9) {
                        if ((var3 = (FleetMember)var4.getMembers().get(var9)).UID.equals(var1.entityUID)) {
                            int var7;
                            if ((var7 = var9 + var1.orderMove) >= 0 && var7 < var4.getMembers().size()) {
                                var4.getMembers().set(var9, var4.getMembers().get(var7));
                                var4.getMembers().set(var7, var3);
                                var4.save();
                                var4.sendFleet();
                                return;
                            }
                            break;
                        }
                    }

                    return;
                }
                break;
            case TARGET_SEC_UDPATE:
                if ((var2 = (Fleet)this.fleetCache.get(var1.fleetId)) != null) {
                    if (var1.target != null) {
                        var2.setCurrentMoveTarget(var1.target);
                        return;
                    }

                    var2.removeCurrentMoveTarget();
                }
        }

    }

    private void deleteFleetFromDatabase(long var1) {
        assert this.isOnServer();

        ((GameServerState)this.state).getDatabaseIndex().getTableManager().getFleetTable().removeFleetCompletely(var1);
    }

    public void requestCreateFleet(String var1, String var2) {
        FleetModification var3;
        (var3 = new FleetModification()).type = fleetmodType.CREATE;
        var3.name = var1;
        var3.owner = var2;
        if (this.isOnServer()) {
            this.executeMod(var3);
        } else {
            this.sendMod(var3);
        }
    }

    public void requestShipAdd(Fleet var1, SegmentController var2) {
        this.requestShipAdd(var1, var2.dbId);
    }

    public void requestShipAdd(Fleet var1, long var2) {
        FleetModification var4;
        (var4 = new FleetModification()).type = fleetmodType.ADD_MEMBER;
        var4.entityDBId = var2;
        var4.fleetId = var1.dbid;
        if (this.isOnServer()) {
            this.executeMod(var4);
        } else {
            this.sendMod(var4);
        }
    }

    public void requestFleetRemove(Fleet var1) {
        if (!this.isOnServer() && !var1.getOwner().toLowerCase(Locale.ENGLISH).equals(((GameClientState)this.state).getPlayer().getName().toLowerCase(Locale.ENGLISH))) {
            ((GameClientState)this.state).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_FLEET_FLEETMANAGER_4, 0.0F);
        } else {
            FleetModification var2;
            (var2 = new FleetModification()).type = fleetmodType.DELETE_FLEET;
            var2.fleetId = var1.dbid;
            if (this.isOnServer()) {
                this.executeMod(var2);
            } else {
                this.sendMod(var2);
            }
        }
    }

    public void requestFleetMemberRemove(Fleet var1, FleetMember var2) {
        if (!this.isOnServer() && !var1.getOwner().toLowerCase(Locale.ENGLISH).equals(((GameClientState)this.state).getPlayer().getName().toLowerCase(Locale.ENGLISH))) {
            ((GameClientState)this.state).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_FLEET_FLEETMANAGER_2, 0.0F);
        } else {
            FleetModification var3;
            (var3 = new FleetModification()).type = fleetmodType.REMOVE_MEMBER;
            var3.fleetId = var1.dbid;
            var3.entityDBId = var2.entityDbId;
            if (this.isOnServer()) {
                this.executeMod(var3);
            } else {
                this.sendMod(var3);
            }
        }
    }

    public void requestFleetOrder(Fleet var1, FleetMember var2, int var3) {
        if (!this.isOnServer() && !var1.getOwner().toLowerCase(Locale.ENGLISH).equals(((GameClientState)this.state).getPlayer().getName().toLowerCase(Locale.ENGLISH))) {
            ((GameClientState)this.state).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_FLEET_FLEETMANAGER_3, 0.0F);
        } else {
            FleetModification var4;
            (var4 = new FleetModification()).type = fleetmodType.MOVE_MEMBER;
            var4.fleetId = var1.dbid;
            var4.entityUID = var2.UID;
            var4.orderMove = (byte)var3;
            if (this.isOnServer()) {
                this.executeMod(var4);
            } else {
                this.sendMod(var4);
            }
        }
    }

    public Fleet getSelected() {
        return (Fleet)this.fleetCache.get(this.selectedFleet);
    }

    public void setSelected(Fleet var1) {
        if (var1 != null) {
            this.selectedFleet = var1.dbid;
        } else {
            this.selectedFleet = -1L;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void submitSectorChangeToClients(FleetMember var1) {
        FleetModification var2;
        (var2 = new FleetModification()).type = fleetmodType.SECTOR_CHANGE;
        var2.entityDBId = var1.entityDbId;
        var2.sector = var1.getSector();
        this.sendMod(var2);
    }

    public void addUpdateAction(FleetUnloadedAction var1) {
        if ((FleetUnloadedAction)this.updateActionMap.put(var1.member.entityDbId, var1) == null) {
            assert var1.fleet.getMembers().contains(var1.member) : var1.member + "; " + var1.fleet;

            this.modAction(var1.fleet.dbid, 1);
        }

    }

    private void modAction(long var1, int var3) {
        int var4 = this.updateActionMapCounter.get(var1);
        var3 = Math.max(0, var4 + var3);
        if (this.fleetCache.containsKey(var1)) {
            this.updateActionMapCounter.put(var1, var3);

            assert var4 <= ((Fleet)this.fleetCache.get(var1)).getMembers().size() : var4 + "; " + ((Fleet)this.fleetCache.get(var1)).getMembers().size();
        } else {
            assert false : var1 + "; " + this.fleetCache + "; \n" + var4 + " -> " + var3;
        }

    }

    public void submitMissionChangeToClients(Fleet var1) {
        FleetModification var2;
        (var2 = new FleetModification()).type = fleetmodType.MISSION_UPDATE;
        var2.fleetId = var1.dbid;
        var2.missionString = var1.missionString;
        this.sendMod(var2);
    }

    public void submitTargetPositionToClients(Fleet var1) {
        FleetModification var2;
        (var2 = new FleetModification()).type = fleetmodType.TARGET_SEC_UDPATE;
        var2.fleetId = var1.dbid;
        if (var1.getCurrentMoveTarget() != null) {
            var2.target = new Vector3i(var1.getCurrentMoveTarget());
        } else {
            var2.target = null;
        }

        this.sendMod(var2);
    }

    public void removeFleet(Fleet var1) {
        assert this.isOnServer();

        FleetModification var2;
        (var2 = new FleetModification()).fleetId = var1.dbid;
        var2.type = fleetmodType.DELETE_FLEET;
        this.executeMod(var2);
    }

    public boolean isCached(Fleet var1) {
        return this.fleetCache.containsKey(var1.dbid);
    }

    public boolean isInFleet(long var1) {
        return this.fleetsByEntityDbId.containsKey(var1);
    }
}
