//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.listener.events.block.SegmentPieceSalvageEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.manager.ingame.BlockBuffer;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.ai.AIConfiguationElements;
import org.schema.game.common.controller.ai.SegmentControllerAIInterface;
import org.schema.game.common.controller.ai.Types;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.damage.acid.AcidDamageFormula.AcidFormulaType;
import org.schema.game.common.controller.damage.beam.DamageBeamHitHandler;
import org.schema.game.common.controller.damage.beam.DamageBeamHitHandlerSegmentController;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.damage.effects.MetaWeaponEffectInterface;
import org.schema.game.common.controller.damage.projectile.ProjecileDamager;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.controller.elements.InventoryMap;
import org.schema.game.common.controller.elements.PulseHandler;
import org.schema.game.common.controller.elements.beam.harvest.SalvageElementManager;
import org.schema.game.common.controller.elements.cargo.CargoCollectionManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.physics.RigidBodySegmentController;
import org.schema.game.common.data.player.*;
import org.schema.game.common.data.player.faction.FactionInterface;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.player.inventory.InventoryHolder;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.Universe;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.physics.Physical;

import javax.vecmath.Vector3f;
import java.util.*;

public abstract class ManagedUsableSegmentController<E extends ManagedUsableSegmentController<E>> extends EditableSendableSegmentController implements PlayerControllable, Salvager, ShopperInterface, TransientSegmentController, SegmentControllerAIInterface, ProjecileDamager, PulseHandler, ManagedSegmentController<E>, FactionInterface, InventoryHolder, Physical {
    private final List<PlayerState> attachedPlayers = new ObjectArrayList();
    private float salvageDamage;
    private long lastSalvage;
    private boolean transientTouched;
    private Set<ShopInterface> shopsInDistance = new HashSet();
    private BlockBuffer blockKillRecorder = new BlockBuffer();
    private final SegmentPiece segmentPiece = new SegmentPiece();
    private DamageBeamHitHandler damageBeamHitHandler = new DamageBeamHitHandlerSegmentController();

    public ManagedUsableSegmentController(StateInterface var1) {
        super(var1);
    }

    public final List<PlayerState> getAttachedPlayers() {
        return this.attachedPlayers;
    }

    public int getSelectedAIControllerIndex() {
        long var1 = (Long)((GameStateInterface)this.getState()).getGameState().getNetworkObject().serverStartTime.get();
        return (int)(((this.getState().getUpdateTime() - (long)this.getState().getServerTimeDifference() - var1) / ((GameStateInterface)this.getState()).getGameState().getAIWeaponSwitchDelayMS() + (long)this.getId()) % 1000000L);
    }

    public final boolean isClientOwnObject() {
        return !this.isOnServer() && this.attachedPlayers.contains(((GameClientState)this.getState()).getPlayer());
    }

    public final void activateAI(boolean var1, boolean var2) {
        if (this.getElementClassCountMap().get((short)121) > 0) {
            ((AIConfiguationElements)this.getAiConfiguration().get(Types.ACTIVE)).setCurrentState(var1, var2);
            this.getAiConfiguration().applyServerSettings();
        }

    }

    public boolean isAIControlled() {
        return this.getAiConfiguration().isActiveAI();
    }

    public void onDockingChanged(boolean var1) {
        this.getManagerContainer().onDockingChanged(var1);
    }

    public float getDamageTakenMultiplier(DamageDealerType var1) {
        float var3 = this.getConfigManager().apply(StatusEffectType.DAMAGE_TAKEN, var1, 1.0F) - 1.0F;
        float var2 = this.getManagerContainer().getPowerInterface().getExtraDamageTakenFromStabilization();
        return var3 + 1.0F + var2;
    }

    public void sendHitConfirm(byte var1) {
        if (this.getState().getUpdateTime() - this.lastSendHitConfirm > 300L) {
            for(int var2 = 0; var2 < this.getAttachedPlayers().size(); ++var2) {
                ((PlayerState)this.getAttachedPlayers().get(var2)).sendHitConfirm(var1);
            }

            this.lastSendHitConfirm = this.getState().getUpdateTime();
        }

    }

    public void onBlockKill(SegmentPiece var1, Damager var2) {
        super.onBlockKill(var1, var2);
        if (this.isOnServer()) {
            SegmentPiece var3;
            (var3 = new SegmentPiece()).setByValue(var1);

            assert var3.getType() != 0 : var3 + "; " + var1;

            this.blockKillRecorder.recordRemove(var3);
        }

    }

    public void onBlockAddedHandled() {
        this.blockKillRecorder.clear();
    }

    public void onPlayerDetachedFromThis(PlayerState var1, PlayerControllable var2) {
        if (this.railController.getRoot() == this) {
            this.getManagerContainer().onPlayerDetachedFromThisOrADock(this, var1, var2);
        } else {
            if (this.railController.getRoot() instanceof ManagedSegmentController) {
                ((ManagedSegmentController)this.railController.getRoot()).getManagerContainer().onPlayerDetachedFromThisOrADock(this, var1, var2);
            }

        }
    }

    public void onPhysicsAdd() {
        super.onPhysicsAdd();
        this.getManagerContainer().getPowerInterface().onPhysicsAdd();
    }

    public void onPhysicsRemove() {
        super.onPhysicsRemove();
        this.getManagerContainer().getPowerInterface().onPhysicsRemove();
    }

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this, "ManagedUsableSegControllerUpdate");
        super.updateLocal(var1);
        this.getManagerContainer().updateLocal(var1);
        long var2 = System.currentTimeMillis();
        long var4;
        if ((var4 = System.currentTimeMillis() - var2) > 40L) {
            System.err.println("[SEGMENTCONTROLLER] " + this.getState() + " " + this + " manager update took " + var4 + " ms");
        }

        var2 = System.currentTimeMillis();
        this.getAiConfiguration().update(var1);
        long var6;
        if ((var6 = System.currentTimeMillis() - var2) > 21L) {
            System.err.println("[SHIP] " + this.getState() + " " + this + " AI udpate took " + var6);
        }

        this.getState().getDebugTimer().end(this, "ManagedUsableSegControllerUpdate");
    }

    public void onAttachPlayer(PlayerState var1, Sendable var2, Vector3i var3, Vector3i var4) {
        this.refreshNameTag();
        if (var2 instanceof AbstractCharacter && ((AbstractCharacter)var2).getGravity().source != null && ((AbstractCharacter)var2).getGravity().source != this) {
            System.err.println("[SHIP] removing gravity due to entering a ship != current gravity entity " + var2 + " -> " + this + "; current: " + ((AbstractCharacter)var2).getGravity().source);
            ((AbstractCharacter)var2).removeGravity();
        }

        if (this.isOnServer() && var1.getCurrentSectorId() != this.getSectorId()) {
            System.err.println("[SERVER][ONATTACHPLAYER] entering! " + this + " in a different sector");
            Vector3i var5;
            if (this.isOnServer()) {
                var5 = ((GameServerState)this.getState()).getUniverse().getSector(this.getSectorId()).pos;
            } else {
                var5 = ((RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getSectorId())).clientPos();
            }

            var1.setCurrentSector(new Vector3i(var5));
            var1.setCurrentSectorId(this.getSectorId());
            PlayerCharacter var6;
            if ((var6 = var1.getAssingedPlayerCharacter()) != null) {
                System.err.println("[SERVER][SEGMENTCONTROLLER][ONATTACHPLAYER] entering! Moving along playercharacter " + this + " in a different sector");
                var6.setSectorId(this.getSectorId());
            } else {
                System.err.println("[SERVER][SEGMENTCONTROLLER] WARNING NO PLAYER CHARACTER ATTACHED TO " + var1);
            }
        }

        Starter.modManager.onSegmentControllerPlayerAttached(this);
    }

    public final void handleMouseEvent(ControllerStateUnit var1, MouseEvent var2) {
        if (var1.parameter instanceof Vector3i && this.getPhysicsDataContainer().isInitialized()) {
            this.getManagerContainer().handleMouseEvent(var1, var2);
        }

    }

    public void handleControl(Timer var1, ControllerStateInterface var2) {
        if (var2 instanceof ControllerStateUnit) {
            ControllerStateUnit var4 = (ControllerStateUnit)var2;
            if (((GameStateInterface)this.getState()).getGameState().getFrozenSectors().contains(this.getSectorId())) {
                CollisionObject var3;
                if ((var3 = this.getPhysicsDataContainer().getObject()) != null && var3 instanceof RigidBodySegmentController) {
                    ((RigidBodySegmentController)var3).setLinearVelocity(new Vector3f());
                    ((RigidBodySegmentController)var3).setAngularVelocity(new Vector3f());
                }

                if (this.isClientOwnObject()) {
                    ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_MANAGEDUSABLESEGMENTCONTROLLER_0, 0.0F);
                }

            } else {
                if (var4.parameter instanceof Vector3i && this.getPhysicsDataContainer().isInitialized()) {
                    assert var4.getPlayerState() != null;

                    this.getManagerContainer().handleControl(var4, var1);
                }

            }
        }
    }

    public void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2) {
        if (var1.parameter instanceof Vector3i && this.getPhysicsDataContainer().isInitialized()) {
            this.getManagerContainer().handleKeyEvent(var1, var2);
        }

    }

    public final Set<ShopInterface> getShopsInDistance() {
        return this.shopsInDistance;
    }

    public final boolean hasSpectatorPlayers() {
        Iterator var1 = this.attachedPlayers.iterator();

        do {
            if (!var1.hasNext()) {
                return false;
            }
        } while(!((PlayerState)var1.next()).isSpectator());

        return true;
    }

    public void refreshNameTag() {
    }

    public void setTouched(boolean var1, boolean var2) {
        if (var1 != this.transientTouched) {
            this.setChangedForDb(true);
        }

        if (var1 && this.isLoadByBlueprint()) {
            this.blueprintIdentifier = null;
            this.blueprintSegmentDataPath = null;
        }

        this.transientTouched = var1;
    }

    public boolean isEmptyOnServer() {
        return !this.transientTouched ? false : super.isEmptyOnServer();
    }

    public boolean isTouched() {
        return this.transientTouched;
    }

    public int handleSalvage(BeamState beam, int beamHits, BeamHandlerContainer<?> var3, Vector3f to, SegmentPiece segmentPiece, Timer var6, Collection<Segment> updatedSegments) {
        this.segmentPiece.setByReference(segmentPiece.getSegment(), segmentPiece.x, segmentPiece.y, segmentPiece.z);
        float var16 = (float)beamHits * beam.getPower();
        if (System.currentTimeMillis() - this.lastSalvage > 10000L) {
            this.salvageDamage = 0.0F;
        }

        this.salvageDamage += var16;
        this.lastSalvage = System.currentTimeMillis();
        if (this.isOnServer() && beamHits > 0 && this.salvageDamage >= SalvageElementManager.SALVAGE_DAMAGE_NEEDED_PER_BLOCK) {
            if (this instanceof TransientSegmentController) {
                this.setTouched(true, true);
            }

            this.salvageDamage -= SalvageElementManager.SALVAGE_DAMAGE_NEEDED_PER_BLOCK;
            short var17;
            if (ElementKeyMap.isValidType(var17 = this.segmentPiece.getType()) && ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var17).getSourceReference())) {
                var17 = (short)ElementKeyMap.getInfoFast(var17).getSourceReference();
            }

            byte var18 = this.segmentPiece.getOrientation();
            if (this.segmentPiece.getSegment().removeElement(this.segmentPiece.getPos(this.tmpLocalPos), false)) {

                //INSERTED CODE @379
                //Note that this currently only calls for the server.
                SegmentPieceSalvageEvent event = new SegmentPieceSalvageEvent(beam, (int) salvageDamage, to, segmentPiece, updatedSegments);
                StarLoader.fireEvent(SegmentPieceSalvageEvent.class, event, this.isOnServer());
                if(event.isCanceled()){
                    return beamHits;
                }
                ///
                if(this.isOnServer()) {
                    this.onSalvaged(var3);
                    if (ElementKeyMap.isValidType(var17)) {
                        this.segmentPiece.getSegmentController().getHpController().onManualRemoveBlock(ElementKeyMap.getInfo(var17));
                        if (ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var17).getSourceReference())) {
                            var17 = (short) ElementKeyMap.getInfoFast(var17).getSourceReference();
                        }
                    }


                    updatedSegments.add(segmentPiece.getSegment());
                    ((RemoteSegment) this.segmentPiece.getSegment()).setLastChanged(System.currentTimeMillis());
                    this.segmentPiece.refresh();

                    assert this.segmentPiece.getType() == 0;

                    if (this.segmentPiece.getSegment().getSegmentController().isScrap()) {
                        if (Universe.getRandom().nextFloat() > 0.5F) {
                            var17 = 546;
                        } else {
                            var17 = 547;
                        }
                    }

                    this.segmentPiece.setHitpointsByte(1);
                    this.segmentPiece.getSegment().getSegmentController().sendBlockSalvage(this.segmentPiece);
                    Short2ObjectOpenHashMap var10;
                    LongOpenHashSet var11;
                    if ((var10 = this.getControlElementMap().getControllingMap().get(ElementCollection.getIndex(beam.controllerPos))) != null && (var11 = (LongOpenHashSet) var10.get((short) 120)) != null && var11.size() > 0) {
                        LongIterator var13 = var11.iterator();

                        while (var13.hasNext()) {
                            long var19 = var13.nextLong();
                            Inventory var15;
                            if ((var15 = this.getManagerContainer().getInventory(ElementCollection.getPosIndexFrom4(var19))) != null && var15.canPutIn(var17, 1)) {
                                int var14 = var15.incExistingOrNextFreeSlot(var17, 1);
                                this.getManagerContainer().sendInventoryDelayed(var15, var14);
                                var14 = this.getMiningBonus(this.segmentPiece.getSegment().getSegmentController());
                                if (ElementKeyMap.hasResourceInjected(var17, var18) && var15.canPutIn(ElementKeyMap.orientationToResIDMapping[var18], 1 * var14)) {
                                    var14 = var15.incExistingOrNextFreeSlot(ElementKeyMap.orientationToResIDMapping[var18], 1 * var14);
                                    this.getManagerContainer().sendInventoryDelayed(var15, var14);
                                }
                                break;
                            }
                        }
                    } else if (this.getAttachedPlayers().size() > 0) {
                        PlayerState var12;
                        (var12 = (PlayerState) this.getAttachedPlayers().get(0)).modDelayPersonalInventory(var17, 1);
                        if (ElementKeyMap.hasResourceInjected(var17, var18)) {
                            int var8 = this.getMiningBonus(this.segmentPiece.getSegment().getSegmentController());
                            var12.modDelayPersonalInventory(ElementKeyMap.orientationToResIDMapping[var18], 1 * var8);
                        }
                    }
                }
            }
        }

        this.segmentPiece.reset();
        return beamHits;
    }

    public void initFromNetworkObject(NetworkObject var1) {
        super.initFromNetworkObject(var1);
        this.getManagerContainer().initFromNetworkObject(var1);
        this.getAiConfiguration().initFromNetworkObject(var1);
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        super.updateFromNetworkObject(var1, var2);
        this.getManagerContainer().updateFromNetworkObject(var1, var2);
        this.getAiConfiguration().updateFromNetworkObject(var1);
    }

    public void updateToFullNetworkObject() {
        super.updateToFullNetworkObject();
        this.getManagerContainer().updateToFullNetworkObject(this.getNetworkObject());
        this.getAiConfiguration().updateToFullNetworkObject(this.getNetworkObject());
    }

    public void updateToNetworkObject() {
        super.updateToNetworkObject();
        this.getManagerContainer().updateToNetworkObject(this.getNetworkObject());
        this.getAiConfiguration().updateToNetworkObject(this.getNetworkObject());
    }

    public final boolean needsTagSave() {
        return true;
    }

    public final InventoryMap getInventories() {
        return this.getManagerContainer().getInventories();
    }

    public final Inventory getInventory(long var1) {
        return this.getManagerContainer().getInventory(var1);
    }

    public final void volumeChanged(double var1, double var3) {
        this.getManagerContainer().volumeChanged(var1, var3);
    }

    public final void sendInventoryErrorMessage(Object[] var1, Inventory var2) {
        this.getManagerContainer().sendInventoryErrorMessage(var1, var2);
    }

    public final double getCapacityFor(Inventory var1) {
        CargoCollectionManager var2;
        return (var2 = (CargoCollectionManager)this.getManagerContainer().getCargo().getCollectionManagersMap().get(var1.getParameterIndex())) != null ? var2.getCapacity() : 0.0D;
    }

    public final NetworkInventoryInterface getInventoryNetworkObject() {
        return this.getManagerContainer().getInventoryNetworkObject();
    }

    public final String printInventories() {
        return this.getManagerContainer().printInventories();
    }

    public final void sendInventoryModification(IntCollection var1, long var2) {
        this.getManagerContainer().sendInventoryModification(var1, var2);
    }

    public final void sendInventoryModification(int var1, long var2) {
        this.getManagerContainer().sendInventoryModification(var1, var2);
    }

    public final void sendInventorySlotRemove(int var1, long var2) {
        this.getManagerContainer().sendInventorySlotRemove(var1, var2);
    }

    public final AbstractOwnerState getOwnerState() {
        AbstractOwnerState var1 = null;

        for(int var2 = 0; var2 < this.getAttachedPlayers().size(); ++var2) {
            AbstractOwnerState var3 = (AbstractOwnerState)this.getAttachedPlayers().get(var2);
            if (var1 == null || var3.isControllingCore()) {
                var1 = var3;
            }
        }

        return var1;
    }

    public InterEffectSet getAttackEffectSet(long var1, DamageDealerType var3) {
        return this.getManagerContainer().getAttackEffectSet(var1, var3);
    }

    public MetaWeaponEffectInterface getMetaWeaponEffect(long var1, DamageDealerType var3) {
        return this.getManagerContainer().getMetaWeaponEffect(var1, var3);
    }

    public DamageBeamHitHandler getDamageBeamHitHandler() {
        return this.damageBeamHitHandler;
    }

    protected void addReceivedBeamLatch(long var1, int var3, long var4) {
        this.getManagerContainer().addReceivedBeamLatch(var1, var3, var4);
    }

    public BlockBuffer getBlockKillRecorder() {
        return this.blockKillRecorder;
    }

    public void setBlockKillRecorder(BlockBuffer var1) {
        this.blockKillRecorder = var1;
    }

    public float getMissileCapacity() {
        return this.getManagerContainer().getMissileCapacity();
    }

    public float getMissileCapacityMax() {
        return this.getManagerContainer().getMissileCapacityMax();
    }

    public AcidFormulaType getAcidType(long var1) {
        return this.getManagerContainer().getAcidType(var1);
    }
}
