//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.shipyard.orders.states;

import api.utils.StaticPlayground;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import org.schema.common.LogUtil;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.*;
import org.schema.game.common.controller.elements.shipyard.ShipyardCollectionManager;
import org.schema.game.common.controller.elements.shipyard.ShipyardElementManager;
import org.schema.game.common.controller.elements.shipyard.orders.ShipyardEntityState;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.meta.VirtualBlueprintMetaItem;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.player.inventory.InventoryChangeMap;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.server.data.EntityRequest;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.ai.stateMachines.FSMException;
import org.schema.schine.ai.stateMachines.Transition;
import org.schema.schine.common.language.Lng;

public class Constructing extends ShipyardState {
    private VirtualBlueprintMetaItem currentDesign;
    private SegmentController currentDocked;
    private long currentFill;
    private Ship newShip;
    private List<Ship> ships = new ObjectArrayList();
    int tickCounter;
    private long lastInventoryFilterStep;
    public boolean testDesign;
    private long spawnedShip;
    private int lastSpawnedId;
    private boolean checkedName;
    private boolean forceRequest;

    public Constructing(ShipyardEntityState var1) {
        super(var1);
    }

    public boolean onEnterS() {
        this.forceRequest = false;
        this.currentDesign = this.getEntityState().getCurrentDesign();
        this.currentDocked = this.getEntityState().getCurrentDocked();
        this.tickCounter = 0;
        this.spawnedShip = 0L;
        if (!this.isLoadedFromTag()) {
            this.getEntityState().currentMapTo.resetAll();
        }

        this.ships.clear();
        this.newShip = null;
        this.getEntityState().currentMapFrom.resetAll();
        if (this.currentDocked != null && this.currentDocked.isVirtualBlueprint()) {
            this.currentDocked.railController.fillElementCountMapRecursive(this.getEntityState().currentMapFrom);
        }

        //
        ElementCountMap resources = StaticPlayground.getRawResources(this.getEntityState().currentMapFrom);
        this.getEntityState().currentMapFrom.resetAll();
        this.getEntityState().currentMapFrom = resources;
        //

        this.currentFill = this.getEntityState().currentMapTo.getTotalAmount();
        this.checkedName = false;
        return false;
    }

    public boolean onExit() {
        return false;
    }

    public boolean onUpdate() throws FSMException {
        boolean var1;
        if (!this.checkedName) {
            this.checkedName = true;
            var1 = ((GameServerState)this.getEntityState().getState()).existsEntity(EntityType.SHIP, this.getEntityState().currentName + (this.getEntityState().spawnDesignWithoutBlocks ? "_SY_TEST" : ""));
            LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": Checking name exists: " + var1);
            if (var1) {
                this.getEntityState().sendShipyardErrorToClient(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_0);
                this.stateTransition(Transition.SY_ERROR);
                return false;
            }
        }

        SegmentPiece var2;
        if (this.spawnedShip > 0L) {
            if (((GameServerState)this.getEntityState().getState()).getLocalAndRemoteObjectContainer().getLocalObjects().containsKey(this.lastSpawnedId) && (System.currentTimeMillis() - this.spawnedShip > 5000L || this.getEntityState().getCurrentDocked() != null && this.getEntityState().getCurrentDocked().getId() == this.lastSpawnedId)) {
                SegmentController var9;
                var2 = (var9 = (SegmentController)((GameServerState)this.getEntityState().getState()).getLocalAndRemoteObjectContainer().getLocalObjects().get(this.lastSpawnedId)).getSegmentBuffer().getPointUnsave(Ship.core);
                System.err.println("[SERVER][SHIPYARD][CONSTRUCTING] construction done. Core: " + var2);
                LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: waiting for construction DONE: CORE: " + var2);
                if (var2 != null) {
                    if (var2.getType() != 1) {
                        throw new IllegalArgumentException("No Core At Contructed Ship: " + var2);
                    }

                    this.stateTransition(Transition.SY_SPAWN_DONE);
                } else if (!this.forceRequest) {
                    LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: waiting for construction Force request of 0 0 0: " + var2);
                    var9.getSegmentProvider().enqueueHightPrio(0, 0, 0, true);
                    this.forceRequest = true;
                } else {
                    LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: waiting for construction WAITING FOR SPAWN: " + var2);
                }
            } else {
                LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: waiting for construction to be done");
                System.err.println("[SERVER][SHIPYARD][CONSTRUCTING] waiting for constructed ship to be loaded");
            }
        } else if (this.newShip != null && this.getEntityState().getCurrentDocked() == null) {
            var1 = this.getEntityState().getShipyardCollectionManager().createDockingRelation(this.newShip, false);
            LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: ship spawned " + this.newShip + ": creating docking relation: " + var1);
            if (!var1) {
                LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: unable to dock");
                this.getEntityState().sendShipyardErrorToClient(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_1);
                this.stateTransition(Transition.SY_ERROR);
            } else {
                this.getEntityState().getShipyardCollectionManager().setCurrentDesign(-1);
                Iterator var13 = this.ships.iterator();

                while(var13.hasNext()) {
                    Ship var15;
                    (var15 = (Ship)var13.next()).initialize();
                    ((GameServerState)this.getEntityState().getState()).getController().getSynchController().addNewSynchronizedObjectQueued(var15);
                }

                this.getEntityState().getShipyardCollectionManager().sendShipyardStateToClient();
                this.getEntityState().currentMapTo.resetAll();
                this.getEntityState().currentMapFrom.resetAll();
                this.lastSpawnedId = this.newShip.getId();
                this.newShip = null;
                this.ships.clear();
                this.spawnedShip = System.currentTimeMillis();
                LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: ship successfully spawned and docked");
            }

            this.newShip = null;
            if (this.getEntityState().spawnDesignWithoutBlocks) {
                this.getEntityState().lastConstructedDesign = this.currentDesign;
            }

            this.currentDesign = null;
        } else if (this.newShip == null && this.currentDesign != null && this.currentDesign == this.getEntityState().getCurrentDesign() && this.currentDocked != null && this.currentDocked.isVirtualBlueprint() && this.currentDocked == this.getEntityState().getCurrentDocked()) {
            LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: spawning new ship");
            if (!this.getEntityState().spawnDesignWithoutBlocks && !this.getEntityState().currentMapFrom.equals(this.getEntityState().currentMapTo) && !this.getEntityState().getShipyardCollectionManager().isInstantBuild()) {
                if (this.newShip == null) {
                    if (this.currentDocked.isFullyLoadedWithDock()) {
                        LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: spawning new ship: aquiring blocks start step: " + this.currentFill + " / " + this.currentDocked.getTotalElements() + " blocks");
                        int var11 = this.getTicksDone();
                        this.tickCounter += var11;
                        IntOpenHashSet var12 = new IntOpenHashSet();

                        for(Inventory var14 = this.getEntityState().getInventory(); (double)this.tickCounter > ShipyardElementManager.CONSTRUCTION_TICK_IN_SECONDS; this.tickCounter = (int)((double)this.tickCounter - Math.max(1.0D, ShipyardElementManager.CONSTRUCTION_TICK_IN_SECONDS))) {
                            var11 = Math.max(1, ShipyardElementManager.CONSTRUCTION_BLOCKS_TAKEN_PER_TICK);
                            if (this.getEntityState().lastDeconstructedBlockCount > var11 && this.getEntityState().isInRepair) {
                                var11 = this.getEntityState().lastDeconstructedBlockCount;
                            }

                            this.getEntityState().lastDeconstructedBlockCount = 0;
                            Iterator var16 = ElementKeyMap.keySet.iterator();

                            while(var16.hasNext()) {
                                short var5 = (Short)var16.next();
                                if (this.getEntityState().currentMapTo.get(var5) < this.getEntityState().currentMapFrom.get(var5)) {
                                    short var6 = var5;
                                    if (ElementKeyMap.isValidType(var5) && ElementKeyMap.getInfoFast(var5).getSourceReference() != 0) {
                                        var6 = (short)ElementKeyMap.getInfoFast(var5).getSourceReference();
                                    }

                                    int var7 = this.getEntityState().currentMapFrom.get(var5) - this.getEntityState().currentMapTo.get(var5);
                                    int var8 = ShipyardCollectionManager.DEBUG_MODE ? var7 : var14.getOverallQuantity(var6);
                                    var7 = Math.min(var11, Math.min(var7, var8));
                                    if (!ShipyardCollectionManager.DEBUG_MODE) {
                                        var14.decreaseBatch(var6, var7, var12);
                                    }

                                    this.getEntityState().currentMapTo.inc(var5, var7);
                                    var11 -= var7;
                                    this.currentFill += (long)var7;
                                }

                                if (var11 <= 0) {
                                    break;
                                }
                            }

                            if (var12.size() > 0) {
                                var14.sendInventoryModification(var12);
                            }
                        }

                        if (var12.size() > 0) {
                            this.getEntityState().getShipyardCollectionManager().sendShipyardGoalToClient();
                        }

                        this.getEntityState().setCompletionOrderPercentAndSendIfChanged((double)this.currentFill / (double)this.currentDocked.getTotalElements());
                        LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: spawning new ship: aquiring blocks end step: " + this.currentFill + " / " + this.currentDocked.getTotalElements() + " blocks");
                    } else {
                        LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: spawning new ship: aquiring blocks ERROR: Design not fully loaded");
                    }
                } else {
                    LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": new ship exists, but yard is idle: " + this.newShip);
                }
            } else {
                LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction: spawning new ship: blocks all available");
                if (this.currentDocked.isFullyLoadedWithDock()) {
                    assert this.getEntityState().currentName != null;

                    String var10 = EntityRequest.convertShipEntityName(this.getEntityState().currentName);
                    if (EntityRequest.existsIdentifierWOExc(this.getEntityState().getState(), var10)) {
                        this.getEntityState().sendShipyardErrorToClient(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_2);
                        this.stateTransition(Transition.SY_ERROR);

                        assert false;
                    } else if ((var2 = this.getEntityState().getCurrentDocked().getSegmentBuffer().getPointUnsave(Ship.core)) != null) {
                        System.err.println("[SERVER][SHIPYARD][CONSTRUCING] core of design Design : " + var2);
                        if (var2.getType() != 1) {
                            throw new IllegalArgumentException("No Core At Design: " + var2);
                        }

                        this.getEntityState().unloadCurrentDockedVolatile();
                        Transform var3;
                        (var3 = new Transform()).setIdentity();
                        System.err.println("[SERVER] CONSTRUCTION FINISHED: SPAWNING FILE: " + var10);
                        this.ships.clear();
                        var10 = this.getEntityState().currentName + (this.getEntityState().spawnDesignWithoutBlocks ? "_SY_TEST" : "");
                        System.err.println("[SERVER] CONSTRUCTION FINISHED: SPAWNING NAME: " + var10);
                        int var4 = this.getEntityState().getShipyardCollectionManager().isPublicException(this.getEntityState().lastOrderFactionId) ? this.getEntityState().lastOrderFactionId : this.getEntityState().getSegmentController().getFactionId();
                        this.newShip = ((Ship)this.currentDocked).copy(var10, this.currentDocked.getSectorId(), var3, Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_3 + this.getEntityState().getSegmentController().getUniqueIdentifier(), var4, this.ships, new EntityCopyData());
                        System.err.println("[SERVER][SHIPYARD] copy finished!");
                        this.currentDocked.setSegmentBuffer(new SegmentBufferManager(this.currentDocked));
                    }
                }
            }
        } else if (this.newShip == null) {
            LogUtil.sy().fine(this.getEntityState().getSegmentController() + " " + this.getEntityState() + " " + this.getClass().getSimpleName() + ": construction interrupted. design no longer docked!");
            this.getEntityState().sendShipyardErrorToClient(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_4);
            this.stateTransition(Transition.SY_ERROR);
        }

        return false;
    }

    public boolean canCancel() {
        return true;
    }

    public boolean hasBlockGoal() {
        return true;
    }

    public boolean isPullingResources() {
        this.getEntityState().getShipyardCollectionManager().getControllerElement().refresh();
        return this.getEntityState().getShipyardCollectionManager().getControllerElement().isActive();
    }

    public void pullResources() {
        ShipyardCollectionManager var1;
        long var2;
        if ((var2 = (var1 = this.getEntityState().getShipyardCollectionManager()).getSegmentController().getState().getController().getServerRunningTime() / 10000L) > this.lastInventoryFilterStep) {
            Inventory var4 = this.getEntityState().getInventory();
            InventoryChangeMap var5 = new InventoryChangeMap();
            Iterator var6 = ElementKeyMap.keySet.iterator();

            label67:
            while(true) {
                short var8;
                int var9;
                int var16;
                do {
                    short var7;
                    do {
                        if (!var6.hasNext()) {
                            if (var5.size() > 0) {
                                var5.sendAll();
                            }

                            this.lastInventoryFilterStep = var2;
                            return;
                        }

                        var7 = (Short)var6.next();
                    } while(this.getEntityState().currentMapTo.get(var7) >= this.getEntityState().currentMapFrom.get(var7));

                    var8 = var7;
                    var9 = 0;
                    if (ElementKeyMap.isValidType(var7) && ElementKeyMap.getInfoFast(var7).getSourceReference() != 0) {
                        var8 = (short)ElementKeyMap.getInfoFast(var7).getSourceReference();
                        var9 = this.getEntityState().currentMapFrom.get(var8) - this.getEntityState().currentMapTo.get(var8);
                    }

                    var16 = this.getEntityState().currentMapFrom.get(var7) - this.getEntityState().currentMapTo.get(var7) + var9;
                } while((var9 = var4.getOverallQuantity(var8)) >= var16);

                Iterator var10 = ElementKeyMap.inventoryTypes.iterator();

                while(true) {
                    short var11;
                    LongOpenHashSet var17;
                    do {
                        if (!var10.hasNext()) {
                            continue label67;
                        }

                        var11 = (Short)var10.next();
                    } while((var17 = (LongOpenHashSet)var1.getSegmentController().getControlElementMap().getControllingMap().get(var1.getControllerElement().getAbsoluteIndex()).get(var11)) == null);

                    Iterator var18 = var17.iterator();

                    while(var18.hasNext()) {
                        long var14 = (Long)var18.next();
                        int var12;
                        if ((var12 = Math.max(0, var16 - var9)) <= 0 || var9 >= var16) {
                            break;
                        }

                        Inventory var13;
                        if ((var13 = var1.getContainer().getInventory(ElementCollection.getPosIndexFrom4(var14))) != null && (var12 = Math.min(var13.getOverallQuantity(var8), var12)) > 0 && (var12 = var4.canPutInHowMuch(var8, var12, -1)) > 0) {
                            var13.decreaseBatch(var8, var12, var5.getInv(var13));
                            var5.getInv(var4).add(var4.putNextFreeSlotWithoutException(var8, var12, -1));
                            var9 += var12;
                        }
                    }
                }
            }
        }
    }

    public void onShipyardRemoved(Vector3i var1) {
        super.onShipyardRemoved(var1);
        this.getEntityState().currentMapTo.spawnInSpace(this.getEntityState().getSegmentController(), var1);
    }

    public String getClientShortDescription() {
        return StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_SHIPYARD_ORDERS_STATES_CONSTRUCTING_5, new Object[]{ShipyardElementManager.CONSTRUCTION_BLOCKS_TAKEN_PER_TICK, (long)ShipyardElementManager.CONSTRUCTION_TICK_IN_SECONDS});
    }
}
