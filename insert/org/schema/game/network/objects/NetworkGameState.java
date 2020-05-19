//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.network.objects;

import org.schema.game.common.data.DebugServerPhysicalObject;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.network.objects.remote.RemoteCatalogEntryBuffer;
import org.schema.game.network.objects.remote.RemoteFactionBuffer;
import org.schema.game.network.objects.remote.RemoteFactionInvitationBuffer;
import org.schema.game.network.objects.remote.RemoteFactionNewsPostBuffer;
import org.schema.game.network.objects.remote.RemoteFactionPointUpdateBuffer;
import org.schema.game.network.objects.remote.RemoteFactionRolesBuffer;
import org.schema.game.network.objects.remote.RemoteFleetBuffer;
import org.schema.game.network.objects.remote.RemoteFleetCommandBuffer;
import org.schema.game.network.objects.remote.RemoteFleetModBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryClientActionBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryMultModBuffer;
import org.schema.game.network.objects.remote.RemoteInventorySlotRemoveBuffer;
import org.schema.game.network.objects.remote.RemoteLeaderboardBuffer;
import org.schema.game.network.objects.remote.RemoteLongBooleanPairBuffer;
import org.schema.game.network.objects.remote.RemoteLongStringBuffer;
import org.schema.game.network.objects.remote.RemoteMetaObjectStateLessBuffer;
import org.schema.game.network.objects.remote.RemoteNPCFactionNewsBuffer;
import org.schema.game.network.objects.remote.RemoteNPCSystemBuffer;
import org.schema.game.network.objects.remote.RemoteRaceBuffer;
import org.schema.game.network.objects.remote.RemoteRaceModBuffer;
import org.schema.game.network.objects.remote.RemoteRuleRulePropertyBuffer;
import org.schema.game.network.objects.remote.RemoteRuleSetManagerBuffer;
import org.schema.game.network.objects.remote.RemoteRuleStateChangeBuffer;
import org.schema.game.network.objects.remote.RemoteShortIntPairBuffer;
import org.schema.game.network.objects.remote.RemoteSimpleCommandBuffer;
import org.schema.game.network.objects.remote.RemoteSystemOwnershipChangeBuffer;
import org.schema.game.network.objects.remote.RemoteTradeActiveBuffer;
import org.schema.game.network.objects.remote.SimpleCommand;
import org.schema.game.network.objects.remote.SimpleCommandFactoryInterface;
import org.schema.game.server.data.simulation.npc.NPCFactionControlCommand;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkEntity;
import org.schema.schine.network.objects.remote.RemoteArrayBuffer;
import org.schema.schine.network.objects.remote.RemoteBoolean;
import org.schema.schine.network.objects.remote.RemoteBooleanPrimitive;
import org.schema.schine.network.objects.remote.RemoteBuffer;
import org.schema.schine.network.objects.remote.RemoteByteArrayDynBuffer;
import org.schema.schine.network.objects.remote.RemoteFloat;
import org.schema.schine.network.objects.remote.RemoteFloatPrimitive;
import org.schema.schine.network.objects.remote.RemoteIntBuffer;
import org.schema.schine.network.objects.remote.RemoteIntPrimitive;
import org.schema.schine.network.objects.remote.RemoteInteger;
import org.schema.schine.network.objects.remote.RemoteIntegerArray;
import org.schema.schine.network.objects.remote.RemoteLong;
import org.schema.schine.network.objects.remote.RemoteLongBuffer;
import org.schema.schine.network.objects.remote.RemoteLongIntPair;
import org.schema.schine.network.objects.remote.RemoteLongPrimitive;
import org.schema.schine.network.objects.remote.RemoteSerializableFactory;
import org.schema.schine.network.objects.remote.RemoteSerializableObjectBuffer;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.network.objects.remote.RemoteStringArray;

public class NetworkGameState extends NetworkEntity implements NetworkInventoryInterface, NTRuleInterface {
    public RemoteLong universeDayDuration = new RemoteLong(this);
    public RemoteLong serverStartTime = new RemoteLong(this);
    public RemoteLong serverModTime = new RemoteLong(this);
    public RemoteLongPrimitive lastFactionPointTurn = new RemoteLongPrimitive(0L, this);
    public RemoteIntBuffer frozenSectorRequests = new RemoteIntBuffer(this);
    public RemoteLeaderboardBuffer leaderBoardBuffer = new RemoteLeaderboardBuffer(this);
    public RemoteFactionBuffer factionDel;
    public RemoteFactionBuffer factionAdd;
    public RemoteFactionNewsPostBuffer factionNewsPosts = new RemoteFactionNewsPostBuffer(this);
    public RemoteFactionInvitationBuffer factionInviteAdd = new RemoteFactionInvitationBuffer(this);
    public RemoteFactionInvitationBuffer factionInviteDel = new RemoteFactionInvitationBuffer(this);
    public RemoteRaceBuffer raceBuffer = new RemoteRaceBuffer(this);
    public RemoteRaceModBuffer raceModBuffer = new RemoteRaceModBuffer(this);
    public RemoteArrayBuffer<RemoteStringArray> personalElemiesAdd = new RemoteArrayBuffer(3, RemoteStringArray.class, this);
    public RemoteArrayBuffer<RemoteStringArray> personalElemiesDel = new RemoteArrayBuffer(3, RemoteStringArray.class, this);
    public RemoteMetaObjectStateLessBuffer metaObjectStateLessBuffer = new RemoteMetaObjectStateLessBuffer(this);
    public RemoteCatalogEntryBuffer catalogBuffer = new RemoteCatalogEntryBuffer(this);
    public RemoteCatalogEntryBuffer catalogDeleteBuffer = new RemoteCatalogEntryBuffer(this);
    public RemoteCatalogEntryBuffer catalogChangeRequestBuffer = new RemoteCatalogEntryBuffer(this);
    public RemoteCatalogEntryBuffer catalogDeleteRequestBuffer = new RemoteCatalogEntryBuffer(this);
    public RemoteArrayBuffer<RemoteStringArray> catalogRatingBuffer = new RemoteArrayBuffer(3, RemoteStringArray.class, this);
    public RemoteFloat serverShutdown = new RemoteFloat(-1.0F, this);
    public RemoteFloat serverCountdownTime = new RemoteFloat(-1.0F, this);
    public RemoteString serverCountdownMessage = new RemoteString("", this);
    public RemoteFloat serverMaxSpeed = new RemoteFloat(50.0F, this);
    public RemoteFloat sectorSize = new RemoteFloat(1000.0F, this);
    public RemoteFloat turnSpeedDivisor = new RemoteFloat(1.1F, this);
    public RemoteFloat linearDamping = new RemoteFloat(0.09F, this);
    public RemoteFloat rotationalDamping = new RemoteFloat(0.09F, this);
    public RemoteInteger recipeBlockCost = new RemoteInteger(10000, this);
    public RemoteInteger maxChainDocking = new RemoteInteger(10000, this);
    public RemoteInteger spawnProtection = new RemoteInteger(0, this);
    public RemoteBoolean additiveProjectiles = new RemoteBoolean(false, this);
    public RemoteBoolean dynamicPrices = new RemoteBoolean(false, this);
    public RemoteBoolean buyBBWIthCredits = new RemoteBoolean(false, this);
    public RemoteFloat relativeProjectiles = new RemoteFloat(1.0F, this);
    public RemoteFloat weaponRangeReference = new RemoteFloat(1.0F, this);
    public RemoteBoolean ignoreDockingArea = new RemoteBoolean(false, this);
    public RemoteArrayBuffer<RemoteStringArray> deployBlockBehaviorChecksum = new RemoteArrayBuffer(2, RemoteStringArray.class, this);
    public RemoteString serverMessage = new RemoteString("", this);
    public RemoteString gameModeMessage = new RemoteString("", this);
    public RemoteInteger saveSlotsAllowed = new RemoteInteger(0, this);
    public RemoteInteger maxBuildArea = new RemoteInteger(10, this);
    public RemoteString battlemodeInfo = new RemoteString("", this);
    public RemoteTradeActiveBuffer tradeActiveBuffer = new RemoteTradeActiveBuffer(this);
    public RemoteShortIntPairBuffer inventoryFillBuffer = new RemoteShortIntPairBuffer(this);
    public RemoteBuffer<RemoteLongIntPair> inventoryProductionLimitBuffer = new RemoteBuffer(RemoteLongIntPair.class, this);
    public RemoteByteArrayDynBuffer catalogBufferDeflated = new RemoteByteArrayDynBuffer(this);
    public RemoteSerializableObjectBuffer<DebugServerPhysicalObject> debugPhysical = new RemoteSerializableObjectBuffer(this, new RemoteSerializableFactory<DebugServerPhysicalObject>() {
        public DebugServerPhysicalObject instantiate() {
            return new DebugServerPhysicalObject();
        }
    });
    public RemoteArrayBuffer<RemoteStringArray> factionRelationshipAcceptBuffer = new RemoteArrayBuffer(3, RemoteStringArray.class, this);
    public RemoteArrayBuffer<RemoteStringArray> factionMod = new RemoteArrayBuffer(4, RemoteStringArray.class, this);
    public RemoteFactionPointUpdateBuffer factionPointMod = new RemoteFactionPointUpdateBuffer(this);
    public RemoteArrayBuffer<RemoteStringArray> factionMemberMod = new RemoteArrayBuffer(5, RemoteStringArray.class, this);
    public RemoteArrayBuffer<RemoteStringArray> factionkickMemberRequests = new RemoteArrayBuffer(3, RemoteStringArray.class, this);
    public RemoteArrayBuffer<RemoteIntegerArray> factionRelationships = new RemoteArrayBuffer(3, RemoteIntegerArray.class, this);
    //INSERTED CODE
    public RemoteArrayBuffer<RemoteStringArray> clientToServerBuffer = new RemoteArrayBuffer(7, RemoteStringArray.class, this);
    ///
    public RemoteArrayBuffer<RemoteStringArray> factionHomeBaseChangeBuffer = new RemoteArrayBuffer(7, RemoteStringArray.class, this);
    public RemoteSystemOwnershipChangeBuffer factionClientSystemOwnerChangeBuffer = new RemoteSystemOwnershipChangeBuffer(this);
    public RemoteArrayBuffer<RemoteStringArray> factionRelationshipOffer = new RemoteArrayBuffer(6, RemoteStringArray.class, this);
    public RemoteFactionRolesBuffer factionRolesBuffer = new RemoteFactionRolesBuffer(this);
    public RemoteArrayBuffer<RemoteStringArray> serverConfig = new RemoteArrayBuffer(2, RemoteStringArray.class, this);
    public RemoteLongPrimitive seed = new RemoteLongPrimitive(0L, this);
    public RemoteIntPrimitive stationCost = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive sectorsToExploreForSystemScan = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive factionKickInactiveTimeLimit = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive aiWeaponSwitchDelay = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive clientUploadBlockSize = new RemoteIntPrimitive(128, this);
    public RemoteFloatPrimitive planetSizeMean = new RemoteFloatPrimitive(0.0F, this);
    public RemoteFloatPrimitive planetSizeDeviation = new RemoteFloatPrimitive(0.0F, this);
    public RemoteFloatPrimitive shopArmorRepairPerSecond = new RemoteFloatPrimitive(0.0F, this);
    public RemoteFloatPrimitive shopRebootCostPerSecond = new RemoteFloatPrimitive(0.0F, this);
    public RemoteBooleanPrimitive allowOldDockingBeam = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive allowOldPowerSystem = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive weightedCenterOfMass = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive lockFactionShips = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive allowPersonalInvOverCap = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive onlyAddFactionToFleet = new RemoteBooleanPrimitive(false, this);
    public RemoteFloatPrimitive massLimitShip = new RemoteFloatPrimitive(-1.0F, this);
    public RemoteFloatPrimitive massLimitPlanet = new RemoteFloatPrimitive(-1.0F, this);
    public RemoteFloatPrimitive massLimitStation = new RemoteFloatPrimitive(-1.0F, this);
    public RemoteIntPrimitive blockLimitShip = new RemoteIntPrimitive(-1, this);
    public RemoteIntPrimitive blockLimitPlanet = new RemoteIntPrimitive(-1, this);
    public RemoteIntPrimitive blockLimitStation = new RemoteIntPrimitive(-1, this);
    public RemoteNPCFactionNewsBuffer npcFactionNewsBuffer = new RemoteNPCFactionNewsBuffer(this);
    public RemoteFleetBuffer fleetBuffer = new RemoteFleetBuffer(this, this.getState());
    public RemoteFleetModBuffer fleetModBuffer = new RemoteFleetModBuffer(this);
    public RemoteFleetCommandBuffer fleetCommandBuffer = new RemoteFleetCommandBuffer(this);
    public RemoteInventoryClientActionBuffer inventoryClientActionBuffer = new RemoteInventoryClientActionBuffer(this);
    public RemoteInventoryMultModBuffer inventoryMultModBuffer = new RemoteInventoryMultModBuffer(this);
    public RemoteLongBuffer inventoryProductionBuffer = new RemoteLongBuffer(this);
    public RemoteShortIntPairBuffer inventoryFilterBuffer = new RemoteShortIntPairBuffer(this);
    public RemoteInventorySlotRemoveBuffer inventorySlotRemoveRequestBuffer = new RemoteInventorySlotRemoveBuffer(this);
    public RemoteInventoryBuffer inventoryChangeBuffer;
    public RemoteNPCSystemBuffer npcSystemBuffer = new RemoteNPCSystemBuffer(this);
    public RemoteBooleanPrimitive npcDebug = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive fow = new RemoteBooleanPrimitive(false, this);
    public RemoteSimpleCommandBuffer simpleCommandQueue = new RemoteSimpleCommandBuffer(this, new SimpleCommandFactoryInterface() {
        public SimpleCommand<?> getNewCommand() {
            return new NPCFactionControlCommand();
        }
    });
    public RemoteFloatPrimitive npcFleetSpeedLoaded = new RemoteFloatPrimitive(1.0F, this);
    public RemoteString npcShopOwnersDebug = new RemoteString(this);
    public RemoteBooleanPrimitive manCalcCancelOn = new RemoteBooleanPrimitive(false, this);
    public RemoteLongBooleanPairBuffer modulesEnabledByDefault = new RemoteLongBooleanPairBuffer(this);
    public RemoteRuleRulePropertyBuffer rulePropertyBuffer = new RemoteRuleRulePropertyBuffer(this);
    public RemoteRuleSetManagerBuffer ruleSetManagerBuffer = new RemoteRuleSetManagerBuffer(this);
    public RemoteBooleanPrimitive allowFactoriesOnShip = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive shipyardIgnoreStructure = new RemoteBooleanPrimitive(false, this);
    public RemoteRuleStateChangeBuffer ruleChangeBuffer = new RemoteRuleStateChangeBuffer(this);
    public RemoteIntBuffer ruleStateRequestBuffer = new RemoteIntBuffer(this);
    public RemoteBuffer<RemoteString> ruleIndividualAddRemoveBuffer = new RemoteBuffer(RemoteString.class, this);

    public RemoteShortIntPairBuffer getInventoryFillBuffer() {
        return this.inventoryFillBuffer;
    }

    public RemoteBuffer<RemoteLongIntPair> getInventoryProductionLimitBuffer() {
        return this.inventoryProductionLimitBuffer;
    }

    public NetworkGameState(SendableGameState var1, StateInterface var2) {
        super(var2);
        this.factionDel = new RemoteFactionBuffer(this, var2);
        this.factionAdd = new RemoteFactionBuffer(this, var2);
        this.inventoryChangeBuffer = new RemoteInventoryBuffer(var1, this);
    }

    public RemoteInventoryClientActionBuffer getInventoryClientActionBuffer() {
        return this.inventoryClientActionBuffer;
    }

    public RemoteInventoryMultModBuffer getInventoryMultModBuffer() {
        return this.inventoryMultModBuffer;
    }

    public RemoteLongBuffer getInventoryProductionBuffer() {
        return this.inventoryProductionBuffer;
    }

    public RemoteShortIntPairBuffer getInventoryFilterBuffer() {
        return this.inventoryFilterBuffer;
    }

    public RemoteLongStringBuffer getInventoryCustomNameModBuffer() {
        return null;
    }

    public void onDelete(StateInterface var1) {
    }

    public void onInit(StateInterface var1) {
    }

    public RemoteInventoryBuffer getInventoriesChangeBuffer() {
        return this.inventoryChangeBuffer;
    }

    public RemoteInventorySlotRemoveBuffer getInventorySlotRemoveRequestBuffer() {
        return this.inventorySlotRemoveRequestBuffer;
    }

    public RemoteRuleStateChangeBuffer getRuleStateChangeBuffer() {
        return this.ruleChangeBuffer;
    }

    public RemoteIntBuffer getRuleStateRequestBuffer() {
        return this.ruleStateRequestBuffer;
    }

    public RemoteBuffer<RemoteString> getRuleIndividualAddRemoveBuffer() {
        return this.ruleIndividualAddRemoveBuffer;
    }
}
