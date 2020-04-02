//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.listener.events.block.BlockSalvageEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector3f;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.elements.BeamState;
import org.schema.game.common.controller.elements.FloatingRockManagerContainer;
import org.schema.game.common.controller.elements.InventoryMap;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.PulseHandler;
import org.schema.game.common.controller.elements.beam.harvest.SalvageElementManager;
import org.schema.game.common.controller.elements.cargo.CargoCollectionManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.player.inventory.InventoryHolder;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.Universe;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.network.objects.NetworkSpaceStation;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class FloatingRockManaged extends FloatingRock implements PlayerControllable, Salvager, PulseHandler, ManagedSegmentController<FloatingRockManaged>, InventoryHolder {
    private final ArrayList<PlayerState> attachedPlayers = new ArrayList();
    private final FloatingRockManagerContainer asteroidManagerContainer;
    private long lastSalvage;
    private float salvageDamage;

    public FloatingRockManaged(StateInterface var1) {
        super(var1);
        this.asteroidManagerContainer = new FloatingRockManagerContainer(var1, this);
    }

    public ManagerContainer<FloatingRockManaged> getManagerContainer() {
        return this.asteroidManagerContainer;
    }

    public EntityType getType() {
        return EntityType.ASTEROID_MANAGED;
    }

    public SegmentController getSegmentController() {
        return this;
    }

    public void sendHitConfirm(byte var1) {
        if (this instanceof PlayerControllable && this.getState().getUpdateTime() - this.lastSendHitConfirm > 300L) {
            for(int var2 = 0; var2 < this.getAttachedPlayers().size(); ++var2) {
                ((PlayerState)this.getAttachedPlayers().get(var2)).sendHitConfirm(var1);
            }

            this.lastSendHitConfirm = this.getState().getUpdateTime();
        }

    }

    public void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2) {
    }

    public void handleControl(Timer var1, ControllerStateInterface var2) {
        if (var2 instanceof ControllerStateUnit) {
            ControllerStateUnit var3;
            if ((var3 = (ControllerStateUnit)var2).parameter instanceof Vector3i && this.getPhysicsDataContainer().isInitialized()) {
                this.getManagerContainer().handleControl(var3, var1);
            }

        }
    }

    public void onAddedElementSynched(short var1, byte var2, byte var3, byte var4, byte var5, Segment var6, boolean var7, long var8, long var10, boolean var12) {
        this.getManagerContainer().onAddedElementSynched(var1, var6, var8, var10, var12);
        super.onAddedElementSynched(var1, var2, var3, var4, var5, var6, var7, var8, var10, var12);
    }

    public String toString() {
        return "ManagedAsteroid(" + this.getId() + ")sec[" + this.getSectorId() + "]" + (this.isTouched() ? "(!)" : "");
    }

    public void updateLocal(Timer var1) {
        super.updateLocal(var1);
        this.isOnServer();
        this.getManagerContainer().updateLocal(var1);
    }

    public boolean needsTagSave() {
        return true;
    }

    public void initFromNetworkObject(NetworkObject var1) {
        super.initFromNetworkObject(var1);
        this.getManagerContainer().initFromNetworkObject(this.getNetworkObject());
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        super.updateFromNetworkObject(var1, var2);
        this.getManagerContainer().updateFromNetworkObject(var1, var2);
    }

    public void updateToFullNetworkObject() {
        super.updateToFullNetworkObject();
        this.getManagerContainer().updateToFullNetworkObject(this.getNetworkObject());
    }

    public void updateToNetworkObject() {
        super.updateToNetworkObject();
        this.getManagerContainer().updateToNetworkObject(this.getNetworkObject());
    }

    public void onRemovedElementSynched(short var1, int var2, byte var3, byte var4, byte var5, byte var6, Segment var7, boolean var8, long var9) {
        this.getManagerContainer().onRemovedElementSynched(var1, var2, var3, var4, var5, var7, var8);
        super.onRemovedElementSynched(var1, var2, var3, var4, var5, var6, var7, var8, var9);
    }

    public void fromTagStructure(Tag var1) {
        assert var1.getName().equals("FloatingRockManaged");

        Tag[] var2 = (Tag[])var1.getValue();
        super.fromTagStructure(var2[1]);
    }

    public Tag toTagStructure() {
        return new Tag(Type.STRUCT, "FloatingRockManaged", new Tag[]{this.getManagerContainer().toTagStructure(), super.toTagStructure(), new Tag(Type.FINISH, (String)null, (Tag[])null)});
    }

    public void newNetworkObject() {
        this.setNetworkObject(new NetworkSpaceStation(this.getState(), this));
    }

    public int handleSalvage(BeamState var1, int var2, BeamHandlerContainer<?> var3, Vector3f var4, SegmentPiece var5, Timer var6, Collection<Segment> var7) {
        float var16 = (float)var2 * var1.getPower();
        if (System.currentTimeMillis() - this.lastSalvage > 10000L) {
            this.salvageDamage = 0.0F;
        }

        this.salvageDamage += var16;
        this.lastSalvage = System.currentTimeMillis();
        if (this.isOnServer() && var2 > 0 && this.salvageDamage >= SalvageElementManager.SALVAGE_DAMAGE_NEEDED_PER_BLOCK) {
            this.setTouched(true, true);
            short var17;
            if (ElementKeyMap.isValidType(var17 = var5.getType()) && ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var17).getSourceReference())) {
                var17 = (short)ElementKeyMap.getInfoFast(var17).getSourceReference();
            }

            byte var18 = var5.getOrientation();

            //INSERTED CODE
            //Note that this currently only calls for the server.
            BlockSalvageEvent event = new BlockSalvageEvent(var1, var2, var4, var5, var7, var17);
            StarLoader.fireEvent(BlockSalvageEvent.class, event);
            if(event.isCanceled()){
                return var2;
            }
            ///


            if (var5.getSegment().removeElement(var5.getPos(this.tmpLocalPos), false)) {
                this.onSalvaged(var3);
                var7.add(var5.getSegment());
                ((RemoteSegment)var5.getSegment()).setLastChanged(System.currentTimeMillis());
                var5.refresh();

                assert var5.getType() == 0;

                if (var5.getSegment().getSegmentController().isScrap()) {
                    if (Universe.getRandom().nextFloat() > 0.5F) {
                        var17 = 546;
                    } else {
                        var17 = 547;
                    }
                }

                var5.setHitpointsByte(1);
                var5.getSegment().getSegmentController().sendBlockSalvage(var5);
                Short2ObjectOpenHashMap var10;
                LongOpenHashSet var11;
                if ((var10 = this.getControlElementMap().getControllingMap().get(ElementCollection.getIndex(var1.controllerPos))) != null && (var11 = (LongOpenHashSet)var10.get((short)120)) != null && var11.size() > 0) {
                    LongIterator var13 = var11.iterator();

                    while(var13.hasNext()) {
                        long var19 = var13.nextLong();
                        Inventory var15;
                        if ((var15 = this.getManagerContainer().getInventory(ElementCollection.getPosFromIndex(var19, new Vector3i()))) != null && var15.canPutIn(var17, 1)) {
                            int var14 = var15.incExistingOrNextFreeSlot(var17, 1);
                            this.getManagerContainer().sendInventoryDelayed(var15, var14);
                            var14 = this.getMiningBonus(var5.getSegment().getSegmentController());
                            if (ElementKeyMap.hasResourceInjected(var17, var18) && var15.canPutIn(ElementKeyMap.orientationToResIDMapping[var18], var14)) {
                                var14 = var15.incExistingOrNextFreeSlot(ElementKeyMap.orientationToResIDMapping[var18], var14);
                                this.getManagerContainer().sendInventoryDelayed(var15, var14);
                            }
                            break;
                        }
                    }
                } else if (this.getAttachedPlayers().size() > 0) {
                    PlayerState var12;
                    (var12 = (PlayerState)this.getAttachedPlayers().get(0)).modDelayPersonalInventory(var17, 1);
                    if (ElementKeyMap.hasResourceInjected(var17, var18)) {
                        int var8 = this.getMiningBonus(var5.getSegment().getSegmentController());
                        var12.modDelayPersonalInventory(ElementKeyMap.orientationToResIDMapping[var18], var8);
                    }
                }
            }
        }

        return var2;
    }

    public List<PlayerState> getAttachedPlayers() {
        return this.attachedPlayers;
    }

    public void handleMouseEvent(ControllerStateUnit var1, MouseEvent var2) {
        if (var1.parameter instanceof Vector3i && this.getPhysicsDataContainer().isInitialized()) {
            this.getManagerContainer().handleMouseEvent(var1, var2);
        }

    }

    public void onAttachPlayer(PlayerState var1, Sendable var2, Vector3i var3, Vector3i var4) {
        GameClientState var5;
        if (!this.isOnServer() && ((GameClientState)this.getState()).getPlayer() == var1 && (var5 = (GameClientState)this.getState()).getPlayer() == var1) {
            var5.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSegmentControlManager().setActive(true);
            System.err.println("Entering asteroid");
        }

        Starter.modManager.onSegmentControllerPlayerAttached(this);
    }

    public void onDetachPlayer(PlayerState var1, boolean var2, Vector3i var3) {
        GameClientState var4;
        if (!this.isOnServer() && (var4 = (GameClientState)this.getState()).getPlayer() == var1 && ((GameClientState)this.getState()).getPlayer() == var1) {
            var4.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSegmentControlManager().setActive(false);
        }

        Starter.modManager.onSegmentControllerPlayerDetached(this);
    }

    public boolean hasSpectatorPlayers() {
        Iterator var1 = this.attachedPlayers.iterator();

        do {
            if (!var1.hasNext()) {
                return false;
            }
        } while(!((PlayerState)var1.next()).isSpectator());

        return true;
    }

    public boolean isTouched() {
        return true;
    }

    public boolean isMoved() {
        return true;
    }

    public void onPlayerDetachedFromThis(PlayerState var1, PlayerControllable var2) {
    }

    public InventoryMap getInventories() {
        return this.getManagerContainer().getInventories();
    }

    public Inventory getInventory(long var1) {
        return this.getManagerContainer().getInventory(var1);
    }

    public double getCapacityFor(Inventory var1) {
        CargoCollectionManager var2;
        return (var2 = (CargoCollectionManager)this.getManagerContainer().getCargo().getCollectionManagersMap().get(var1.getParameterIndex())) != null ? var2.getCapacity() : 0.0D;
    }

    public void volumeChanged(double var1, double var3) {
        this.getManagerContainer().volumeChanged(var1, var3);
    }

    public void sendInventoryErrorMessage(Object[] var1, Inventory var2) {
        this.getManagerContainer().sendInventoryErrorMessage(var1, var2);
    }

    public NetworkInventoryInterface getInventoryNetworkObject() {
        return this.getManagerContainer().getInventoryNetworkObject();
    }

    public String printInventories() {
        return this.getManagerContainer().printInventories();
    }

    public void sendInventoryModification(IntCollection var1, long var2) {
        this.getManagerContainer().sendInventoryModification(var1, var2);
    }

    public void sendInventoryModification(int var1, long var2) {
        this.getManagerContainer().sendInventoryModification(var1, var2);
    }

    public void sendInventorySlotRemove(int var1, long var2) {
        this.getManagerContainer().sendInventorySlotRemove(var1, var2);
    }
}
