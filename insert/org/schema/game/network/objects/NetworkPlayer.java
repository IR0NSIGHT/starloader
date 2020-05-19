//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.network.objects;

import org.schema.game.client.controller.GameClientController;
import org.schema.game.common.data.player.NetworkPlayerInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.network.objects.remote.RemoteBlockCountMapBuffer;
import org.schema.game.network.objects.remote.RemoteBlueprintPlayerHandleRequestBuffer;
import org.schema.game.network.objects.remote.RemoteCatalogEntryBuffer;
import org.schema.game.network.objects.remote.RemoteCockpit;
import org.schema.game.network.objects.remote.RemoteControlledFileStreamBuffer;
import org.schema.game.network.objects.remote.RemoteControllerUnitRequestBuffer;
import org.schema.game.network.objects.remote.RemoteConversationBuffer;
import org.schema.game.network.objects.remote.RemoteCreatureSpawnBuffer;
import org.schema.game.network.objects.remote.RemoteCrewFleetBuffer;
import org.schema.game.network.objects.remote.RemoteDragDropBuffer;
import org.schema.game.network.objects.remote.RemoteFactionBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryClientActionBuffer;
import org.schema.game.network.objects.remote.RemoteInventoryMultModBuffer;
import org.schema.game.network.objects.remote.RemoteInventorySlotRemoveBuffer;
import org.schema.game.network.objects.remote.RemoteLongStringBuffer;
import org.schema.game.network.objects.remote.RemoteProximitySector;
import org.schema.game.network.objects.remote.RemoteProximitySystem;
import org.schema.game.network.objects.remote.RemoteRuleStateChangeBuffer;
import org.schema.game.network.objects.remote.RemoteSegmentControllerBlockBuffer;
import org.schema.game.network.objects.remote.RemoteServerMessageBuffer;
import org.schema.game.network.objects.remote.RemoteShortIntPairBuffer;
import org.schema.game.network.objects.remote.RemoteSimpleCommandBuffer;
import org.schema.game.network.objects.remote.SimpleCommand;
import org.schema.game.network.objects.remote.SimpleCommandFactoryInterface;
import org.schema.game.network.objects.remote.SimplePlayerCommand;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.input.Mouse;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.remote.RemoteArrayBuffer;
import org.schema.schine.network.objects.remote.RemoteBoolean;
import org.schema.schine.network.objects.remote.RemoteBooleanArray;
import org.schema.schine.network.objects.remote.RemoteBooleanPrimitive;
import org.schema.schine.network.objects.remote.RemoteBuffer;
import org.schema.schine.network.objects.remote.RemoteByteBuffer;
import org.schema.schine.network.objects.remote.RemoteBytePrimitive;
import org.schema.schine.network.objects.remote.RemoteField;
import org.schema.schine.network.objects.remote.RemoteFloatPrimitive;
import org.schema.schine.network.objects.remote.RemoteIntBuffer;
import org.schema.schine.network.objects.remote.RemoteIntPrimitive;
import org.schema.schine.network.objects.remote.RemoteInteger;
import org.schema.schine.network.objects.remote.RemoteIntegerArray;
import org.schema.schine.network.objects.remote.RemoteLongArray;
import org.schema.schine.network.objects.remote.RemoteLongBuffer;
import org.schema.schine.network.objects.remote.RemoteLongIntPair;
import org.schema.schine.network.objects.remote.RemoteLongPrimitive;
import org.schema.schine.network.objects.remote.RemoteLongPrimitiveArray;
import org.schema.schine.network.objects.remote.RemoteMatrix3f;
import org.schema.schine.network.objects.remote.RemoteShort;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.network.objects.remote.RemoteTransformationBuffer;
import org.schema.schine.network.objects.remote.RemoteVector3i;
import org.schema.schine.network.objects.remote.RemoteVector4f;

public class NetworkPlayer extends NetworkObject implements NetworkPlayerInterface, NetworkInventoryInterface, NTRuleInterface {
    public RemoteBooleanPrimitive upgradedAccount = new RemoteBooleanPrimitive(false, this);
    public RemoteFloatPrimitive health = new RemoteFloatPrimitive(1.0F, this);
    public RemoteIntPrimitive clientId = new RemoteIntPrimitive(-777777, this);
    public RemoteIntPrimitive currentSectorType = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive sectorId = new RemoteIntPrimitive(-2, this);
    public RemoteVector3i sectorPos = new RemoteVector3i(this);
    public RemoteVector3i waypoint = new RemoteVector3i(this);
    public RemoteIntPrimitive credits = new RemoteIntPrimitive(0, this);
    public RemoteInventoryClientActionBuffer inventoryClientActionBuffer = new RemoteInventoryClientActionBuffer(this);
    public RemoteInteger kills = new RemoteInteger(this);
    public RemoteIntPrimitive helmetSlot = new RemoteIntPrimitive(0, this);
    public RemoteBooleanPrimitive infiniteInventoryVolume = new RemoteBooleanPrimitive(false, this);
    public RemoteLongPrimitiveArray sittingState = new RemoteLongPrimitiveArray(4, this);
    public RemoteSimpleCommandBuffer simpleCommandQueue = new RemoteSimpleCommandBuffer(this, new SimpleCommandFactoryInterface() {
        public SimpleCommand getNewCommand() {
            return new SimplePlayerCommand();
        }
    });
    public RemoteInteger deaths = new RemoteInteger(this);
    public RemoteLongPrimitive lastDeathNotSuicide = new RemoteLongPrimitive(0L, this);
    public RemoteBooleanPrimitive isAdminClient = new RemoteBooleanPrimitive(this);
    public RemoteBooleanPrimitive hasCreativeMode = new RemoteBooleanPrimitive(this);
    public RemoteBooleanPrimitive useCreativeMode = new RemoteBooleanPrimitive(this);
    public RemoteIntBuffer requestCargoMode = new RemoteIntBuffer(this);
    public RemoteBooleanPrimitive useCargoMode = new RemoteBooleanPrimitive(this);
    public RemoteInteger aquiredTargetId = new RemoteInteger(-1, this);
    public RemoteBlockCountMapBuffer blockCountMapBuffer = new RemoteBlockCountMapBuffer(this);
    public RemoteSegmentControllerBlockBuffer cargoInventoryChange = new RemoteSegmentControllerBlockBuffer(this);
    public RemoteArrayBuffer<RemoteIntegerArray> factionEntityIdChangeBuffer = new RemoteArrayBuffer(2, RemoteIntegerArray.class, this);
    public RemoteIntPrimitive selectedEntityId = new RemoteIntPrimitive(-1, this);
    public RemoteIntPrimitive selectedAITargetId = new RemoteIntPrimitive(-1, this);
    public RemoteIntPrimitive ping = new RemoteIntPrimitive(0, this);
    public RemoteIntPrimitive playerFaceId = new RemoteIntPrimitive(1, this);
    public RemoteBytePrimitive shipControllerSlot = new RemoteBytePrimitive((byte)0, this);
    public RemoteBytePrimitive buildSlot = new RemoteBytePrimitive((byte)0, this);
    public RemoteBoolean canRotate = new RemoteBoolean(true, this);
    public RemoteString skinName = new RemoteString(this);
    public RemoteBooleanPrimitive invisibility = new RemoteBooleanPrimitive(this);
    public RemoteString playerName = new RemoteString(this);
    public RemoteInteger factionId = new RemoteInteger(this);
    public RemoteFactionBuffer factionCreateBuffer;
    public RemoteIntBuffer factionLeaveBuffer = new RemoteIntBuffer(this);
    public RemoteIntBuffer factionJoinBuffer = new RemoteIntBuffer(this);
    public RemoteVector4f tint = new RemoteVector4f(this);
    public RemoteCrewFleetBuffer crewRequest = new RemoteCrewFleetBuffer(this);
    public RemoteBuffer<RemoteString> factionDescriptionEditRequest = new RemoteBuffer(RemoteString.class, this);
    public RemoteBuffer<RemoteString> factionChatRequests = new RemoteBuffer(RemoteString.class, this);
    public RemoteArrayBuffer<RemoteIntegerArray> roundEndBuffer = new RemoteArrayBuffer(3, RemoteIntegerArray.class, this);
    public RemoteIntBuffer killedBuffer = new RemoteIntBuffer(this);
    public RemoteControlledFileStreamBuffer shipUploadBuffer;
    public RemoteControlledFileStreamBuffer skinUploadBuffer;
    public RemoteControlledFileStreamBuffer skinDownloadBuffer;
    public RemoteBooleanArray activeControllerMask = new RemoteBooleanArray(4, this);
    public RemoteCreatureSpawnBuffer creatureSpawnBuffer = new RemoteCreatureSpawnBuffer(this);
    public RemoteConversationBuffer converationBuffer = new RemoteConversationBuffer(this);
    public RemoteControllerUnitRequestBuffer controlRequestParameterBuffer = new RemoteControllerUnitRequestBuffer(this);
    public RemoteIntBuffer factionShareFowBuffer = new RemoteIntBuffer(this);
    public RemoteBuffer<RemoteVector3i> resetFowBuffer = new RemoteBuffer(RemoteVector3i.class, this);
    public RemoteIntBuffer creditTransactionBuffer = new RemoteIntBuffer(this);
    public RemoteDragDropBuffer dropOrPickupSlots = new RemoteDragDropBuffer(this);
    public RemoteIntBuffer recipeRequests = new RemoteIntBuffer(this);
    public RemoteIntBuffer recipeSellRequests = new RemoteIntBuffer(this);
    public RemoteIntBuffer fixedRecipeBuyRequests = new RemoteIntBuffer(this);
    public RemoteServerMessageBuffer messages = new RemoteServerMessageBuffer(this);
    public RemoteFloatPrimitive frontBackAxis = new RemoteFloatPrimitive(0.0F, this);
    public RemoteFloatPrimitive rightLeftAxis = new RemoteFloatPrimitive(0.0F, this);
    public RemoteFloatPrimitive upDownAxis = new RemoteFloatPrimitive(0.0F, this);
    public RemoteBuffer<RemoteString> skinRequestBuffer = new RemoteBuffer(RemoteString.class, this);
    public RemoteBlueprintPlayerHandleRequestBuffer catalogPlayerHandleBuffer = new RemoteBlueprintPlayerHandleRequestBuffer(this);
    public RemoteArrayBuffer<RemoteIntegerArray> buyBuffer = new RemoteArrayBuffer(2, RemoteIntegerArray.class, this);
    public RemoteArrayBuffer<RemoteIntegerArray> sellBuffer = new RemoteArrayBuffer(2, RemoteIntegerArray.class, this);
    public RemoteArrayBuffer<RemoteIntegerArray> deleteBuffer = new RemoteArrayBuffer(3, RemoteIntegerArray.class, this);
    public RemoteBuffer<RemoteBoolean> spawnRequest = new RemoteBuffer(RemoteBoolean.class, this);
    public RemoteInventoryBuffer inventoryBuffer;
    public RemoteInventoryMultModBuffer inventoryMultModBuffer = new RemoteInventoryMultModBuffer(this);
    public RemoteProximitySector proximitySector;
    public RemoteProximitySystem proximitySystem;
    public RemoteIntBuffer creditsDropBuffer = new RemoteIntBuffer(this);
    public RemoteCatalogEntryBuffer catalogBuffer = new RemoteCatalogEntryBuffer(this);
    public RemoteShort keyboardOfController = new RemoteShort(this);
    public RemoteBooleanArray mouseOfController = new RemoteBooleanArray(4, this);
    public RemoteByteBuffer keyboardOfControllerBuffer = new RemoteByteBuffer(this, 16);
    public RemoteByteBuffer mouseOfControllerBuffer = new RemoteByteBuffer(this);
    public RemoteMatrix3f camOrientation = new RemoteMatrix3f(this);
    public RemoteCockpit cockpit;
    public RemoteArrayBuffer<RemoteLongArray> textureChangedBroadcastBuffer = new RemoteArrayBuffer(2, RemoteLongArray.class, this);
    public RemoteBuffer<RemoteBoolean> requestFactionOpenToJoin = new RemoteBuffer(RemoteBoolean.class, this);
    public RemoteBuffer<RemoteBoolean> requestAttackNeutral = new RemoteBuffer(RemoteBoolean.class, this);
    public RemoteBuffer<RemoteBoolean> requestAutoDeclareWar = new RemoteBuffer(RemoteBoolean.class, this);
    public RemoteBooleanPrimitive mouseSwitched = new RemoteBooleanPrimitive(false, this);
    public RemoteShortIntPairBuffer inventoryFilterBuffer = new RemoteShortIntPairBuffer(this);
    public RemoteLongBuffer inventoryProductionBuffer = new RemoteLongBuffer(this);
    public RemoteBytePrimitive hitNotifications = new RemoteBytePrimitive((byte)0, this);
    public RemoteBuffer<RemoteString> tutorialCalls = new RemoteBuffer(RemoteString.class, this);
    public RemoteVector3i personalSector = new RemoteVector3i(this);
    public RemoteVector3i testSector = new RemoteVector3i(this);
    public RemoteInventorySlotRemoveBuffer inventorySlotRemoveRequestBuffer = new RemoteInventorySlotRemoveBuffer(this);
    public RemoteLongPrimitive dbId = new RemoteLongPrimitive(0L, this);
    public RemoteIntPrimitive inputSeed = new RemoteIntPrimitive(0, this);
    public RemoteShortIntPairBuffer inventoryFillBuffer = new RemoteShortIntPairBuffer(this);
    public RemoteBuffer<RemoteLongIntPair> inventoryProductionLimitBuffer = new RemoteBuffer(RemoteLongIntPair.class, this);
    public RemoteIntBuffer mineArmTimerRequests = new RemoteIntBuffer(this);
    public RemoteIntPrimitive mineArmTimer = new RemoteIntPrimitive(0, this);
    public RemoteLongPrimitive lastSpawnedThisSession = new RemoteLongPrimitive(0L, this);
    public RemoteTransformationBuffer buildModePositionBuffer = new RemoteTransformationBuffer(this);
    public RemoteBooleanPrimitive isInBuildMode = new RemoteBooleanPrimitive(false, this);
    public RemoteBooleanPrimitive isBuildModeSpotlight = new RemoteBooleanPrimitive(true, this);
    public RemoteBuffer<RemoteString> ruleIndividualAddRemoveBuffer = new RemoteBuffer(RemoteString.class, this);
    public RemoteRuleStateChangeBuffer ruleChangeBuffer = new RemoteRuleStateChangeBuffer(this);
    public RemoteIntBuffer ruleStateRequestBuffer = new RemoteIntBuffer(this);
    public RemoteBooleanPrimitive adjustMode = new RemoteBooleanPrimitive(false, this);

    //INSERTED CODE
    //Structure: HEADER_ID, DATA...
    public RemoteBooleanPrimitive test = new RemoteBooleanPrimitive(this);
    public RemoteIntBuffer clientToServerBuffer = new RemoteIntBuffer(this);
    ///

    public RemoteShortIntPairBuffer getInventoryFillBuffer() {
        return this.inventoryFillBuffer;
    }

    public RemoteBuffer<RemoteLongIntPair> getInventoryProductionLimitBuffer() {
        return this.inventoryProductionLimitBuffer;
    }

    public RemoteInventorySlotRemoveBuffer getInventorySlotRemoveRequestBuffer() {
        return this.inventorySlotRemoveRequestBuffer;
    }

    public NetworkPlayer(StateInterface var1, PlayerState var2) {
        super(var1);
        this.inventoryBuffer = new RemoteInventoryBuffer(var2, this);
        this.proximitySector = new RemoteProximitySector(var2.getProximitySector(), this);
        this.proximitySystem = new RemoteProximitySystem(var2.getProximitySystem(), this);
        this.shipUploadBuffer = new RemoteControlledFileStreamBuffer(this, (int)var1.getUploadBlockSize());
        this.skinUploadBuffer = new RemoteControlledFileStreamBuffer(this, (int)var1.getUploadBlockSize());
        this.skinDownloadBuffer = new RemoteControlledFileStreamBuffer(this, (int)var1.getUploadBlockSize());
        this.factionCreateBuffer = new RemoteFactionBuffer(this, var1);
        this.cockpit = new RemoteCockpit(var2.getCockpit(), this);
    }

    public RemoteInventoryBuffer getInventoriesChangeBuffer() {
        return this.inventoryBuffer;
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

    public void handleKeyEvent(boolean var1, int var2) {
        for(int var3 = 0; var3 < KeyboardMappings.remoteMappings.length; ++var3) {
            if (KeyboardMappings.remoteMappings[var3].getMapping() == var2) {
                KeyboardMappings.remoteMappings[var3].sendEvent(this.keyboardOfControllerBuffer, var1, this.isOnServer());
            }
        }

    }

    public void handleMouseEventButton(boolean var1, int var2) {
        byte var3 = (byte)(var1 ? var2 : -var2 - 1);
        this.mouseOfControllerBuffer.add(var3);
    }

    public boolean isMouseDown(int var1) {
        if (var1 >= 0 && var1 < ((RemoteField[])this.mouseOfController.get()).length) {
            return (Boolean)((RemoteField[])this.mouseOfController.get())[var1].get();
        } else {
            System.err.println("[WARNING] Mouse button not registered! " + var1);
            return false;
        }
    }

    public void onDelete(StateInterface var1) {
    }

    public void onInit(StateInterface var1) {
    }

    public void setMouseDown(GameClientController var1) {
        if (Mouse.isCreated()) {
            for(int var2 = 0; var2 < ((RemoteField[])this.mouseOfController.get()).length; ++var2) {
                if ((Boolean)((RemoteField[])this.mouseOfController.get())[var2].get() != var1.isMouseButtonDown(var2)) {
                    ((RemoteField[])this.mouseOfController.get())[var2].set(var1.isMouseButtonDown(var2), true);
                }
            }

        }
    }

    public RemoteBytePrimitive getBuildSlot() {
        return this.buildSlot;
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
