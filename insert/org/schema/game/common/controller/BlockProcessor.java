//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.element.block.Block;
import api.listener.events.block.BlockModifyEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;
import org.schema.common.util.ByteUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.shards.ShardDrawer;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.data.BlockBulkSerialization;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SegmentRetrieveCallback;
import org.schema.game.common.data.VoidSegmentPiece;
import org.schema.game.common.data.VoidSegmentPiece.VoidSegmentPiecePool;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.DrawableRemoteSegment;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.data.world.Universe;
import org.schema.game.network.objects.NetworkSegmentProvider;
import org.schema.game.network.objects.remote.RemoteBlockBulk;
import org.schema.game.network.objects.remote.RemoteSegmentPiece;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.forms.BoundingBox;
import org.schema.schine.network.StateInterface;

public class BlockProcessor {
    private final SendableSegmentController con;
    private final List<VoidSegmentPiece> delayedSegmentMods = new ObjectArrayList();
    private final ObjectArrayFIFOQueue<BlockBulkSerialization> delayedBulkMods = new ObjectArrayFIFOQueue();
    private final Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> inventoryMods = new Object2ObjectOpenHashMap();
    private final ObjectArrayList<Segment> emptySegments = new ObjectArrayList();
    private final ObjectArrayList<Segment> segmentsAABBUpdateNeeded = new ObjectArrayList();
    private final Long2ObjectOpenHashMap<RemoteSegment> segmentsChanged = new Long2ObjectOpenHashMap();
    private final ObjectOpenHashSet<RemoteSegment> markNeighborForChanged = new ObjectOpenHashSet();
    public final Long2ObjectOpenHashMap<LongArrayList> connectionsToAddFromPaste = new Long2ObjectOpenHashMap();
    public final Long2ObjectOpenHashMap<String> textToAddFromPaste = new Long2ObjectOpenHashMap();
    private final SegmentRetrieveCallback callBackTmp = new SegmentRetrieveCallback();
    private final BlockProcessor.PieceListProvider plProvider;
    private final BlockProcessor.SegmentChangeContainer pieceMap;
    final BlockProcessor.SegmentBlockProcessor segBlockProc;
    private final SegmentPiece current = new SegmentPiece();
    private final VoidSegmentPiecePool receivedPiecePool;
    private SegmentPiece tmpPiece = new SegmentPiece();
    private static final VoidSegmentPiecePool serverPool = new VoidSegmentPiecePool();
    private static final VoidSegmentPiecePool clientPool = new VoidSegmentPiecePool();
    protected static ThreadLocal<BlockProcessor.PieceListProvider> plProviderTL = new ThreadLocal<BlockProcessor.PieceListProvider>() {
        protected final BlockProcessor.PieceListProvider initialValue() {
            return new BlockProcessor.PieceListProvider();
        }
    };
    protected static ThreadLocal<BlockProcessor.SegmentChangeContainer> sccTL = new ThreadLocal<BlockProcessor.SegmentChangeContainer>() {
        protected final BlockProcessor.SegmentChangeContainer initialValue() {
            return new BlockProcessor.SegmentChangeContainer();
        }
    };
    private List<BlockProcessor.PieceList> locked = new ObjectArrayList();

    public BlockProcessor(SendableSegmentController var1) {
        this.con = var1;
        this.pieceMap = (BlockProcessor.SegmentChangeContainer)sccTL.get();
        this.plProvider = (BlockProcessor.PieceListProvider)plProviderTL.get();
        if (this.isOnServer()) {
            this.receivedPiecePool = serverPool;
            this.segBlockProc = new BlockProcessor.SegmentBlockProcessorServer();
        } else {
            this.receivedPiecePool = clientPool;
            this.segBlockProc = new BlockProcessor.SegmentBlockProcessorClient();
        }
    }

    public void handleDelayedMods() {
        boolean var1 = false;
        this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Total");
        this.getState().getDebugTimer().setMeta(this.con, "SendableSegmentController", "DelayedMods", "Total", this.delayedSegmentMods.size() + " Blocks");
        if (!this.delayedSegmentMods.isEmpty()) {
            if (this.isOnServer()) {
                this.con.onBlockSinglePlacedOnServer();
            }

            if (this.isAccessible()) {
                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "PreProcess");
                BlockProcessor.SegmentChangeContainer var2 = this.preProcess(this.delayedSegmentMods);
                this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "PreProcess");
                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Aquire");
                boolean var3 = true;
                Iterator var4 = var2.values().iterator();

                BlockProcessor.PieceList var5;
                while(var4.hasNext()) {
                    var5 = (BlockProcessor.PieceList)var4.next();
                    if (!this.aquireSegmentLock(var5)) {
                        var3 = false;
                        break;
                    }

                    this.locked.add(var5);
                }

                this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Aquire");
                if (!var3) {
                    for(var4 = this.locked.iterator(); var4.hasNext(); this.plProvider.free(var5)) {
                        if ((var5 = (BlockProcessor.PieceList)var4.next()).segmentData != null) {
                            var5.segmentData.rwl.writeLock().unlock();
                        }
                    }

                    this.locked.clear();
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "ClearBuffers");
                    this.clearBuffers();
                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "ClearBuffers");
                    return;
                }

                this.locked.clear();
                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle");
                var4 = var2.values().iterator();

                while(var4.hasNext()) {
                    String var6 = String.valueOf((var5 = (BlockProcessor.PieceList)var4.next()).absIndex);
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var6);
                    this.handleSegment(var5);
                    this.segmentsChanged.put(ElementCollection.getIndex(var5.segment.pos), var5.segment);
                    if (var5.segment.isEmpty() && var5.segment.getSegmentData() != null) {
                        this.emptySegments.add(var5.segment);
                    }

                    if (this.segBlockProc.removes > 0) {
                        this.segmentsAABBUpdateNeeded.add(var5.segment);
                    }

                    if (this.segBlockProc.total > 0) {
                        var1 = true;
                    }

                    this.segBlockProc.reset();
                    this.plProvider.free(var5);
                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var6);
                }

                this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle");
                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "FreeEmpty");
                this.freeEmptySegs();
                this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "FreeEmpty");
                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "MarkNeighbor");
                var4 = this.markNeighborForChanged.iterator();

                while(true) {
                    if (!var4.hasNext()) {
                        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "MarkNeighbor");
                        this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "UpdateAABB");
                        this.updateAABBs();
                        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "UpdateAABB");
                        break;
                    }

                    RemoteSegment var16 = (RemoteSegment)var4.next();

                    for(int var17 = -1; var17 <= 1; ++var17) {
                        for(int var12 = -1; var12 <= 1; ++var12) {
                            for(int var14 = -1; var14 <= 1; ++var14) {
                                int var7 = var16.pos.x + (var14 << 5);
                                int var8 = var16.pos.y + (var12 << 5);
                                int var9 = var16.pos.z + (var17 << 5);
                                long var10 = ElementCollection.getIndex(var7, var8, var9);
                                if (!this.segmentsChanged.containsKey(var10)) {
                                    this.con.getSegmentBuffer().get(var7, var8, var9, this.callBackTmp);
                                    if (this.callBackTmp.segment != null) {
                                        this.segmentsChanged.put(var10, (RemoteSegment)this.callBackTmp.segment);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "SendInventoryMods");
            this.sendInventoryModifications();
            this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "SendInventoryMods");
            if (var1) {
                this.con.setAllTouched(true);
            }
        }

        this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "ClearBuffers");
        this.clearDelayedSegmentMods();
        this.clearBuffers();
        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "ClearBuffers");
        if (!this.segmentsChanged.isEmpty()) {
            this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "UpdateTimestamps");
            Iterator var13 = this.segmentsChanged.values().iterator();

            while(var13.hasNext()) {
                RemoteSegment var15;
                (var15 = (RemoteSegment)var13.next()).dataChanged(true);
                var15.setLastChanged(this.getState().getUpdateTime());
            }

            this.segmentsChanged.clear();
            this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "UpdateTimestamps");
        }

        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Total");
    }

    private boolean isAccessible() {
        return this.isOnServer() || ((GameClientState)this.getState()).getCurrentSectorEntities().containsKey(this.con.getId());
    }

    private BlockProcessor.SegmentChangeContainer preProcess(List<VoidSegmentPiece> var1) {
        int var2 = var1.size();

        for(int var3 = 0; var3 < var2; ++var3) {
            VoidSegmentPiece var4;
            long var5;
            if ((var4 = (VoidSegmentPiece)var1.get(var3)).controllerPos != -9223372036854775808L) {
                var5 = var4.controllerPos;
                if ((SegmentPiece)this.pieceMap.controllers.get(var5) == null) {
                    SegmentPiece var7 = this.con.getSegmentBuffer().getPointUnsave(var5, this.plProvider.getPiece());
                    this.pieceMap.controllers.put(var5, var7);
                }
            }

            var5 = var4.getSegmentAbsoluteIndex();
            BlockProcessor.PieceList var8;
            if ((var8 = (BlockProcessor.PieceList)this.pieceMap.get(var5)) == null) {
                (var8 = this.plProvider.get()).absIndex = var5;
                var8.x = ByteUtil.divUSeg(var4.voidPos.x) << 5;
                var8.y = ByteUtil.divUSeg(var4.voidPos.y) << 5;
                var8.z = ByteUtil.divUSeg(var4.voidPos.z) << 5;
                this.con.getSegmentBuffer().get(var8.x, var8.y, var8.z, this.callBackTmp);
                if (this.isOnServer() || this.callBackTmp.state != -2 || var4.forceClientSegmentAdd) {
                    if (this.callBackTmp.state < 0) {
                        var8.segment = (RemoteSegment)(this.isOnServer() ? new RemoteSegment(this.con) : new DrawableRemoteSegment(this.con));
                        var8.segment.setPos(var8.x, var8.y, var8.z);
                        System.err.println("[BLOCKPROCESSOR] NEW SEGMENT FROM ADDED PIECE: " + this.con.getState() + "; " + var8.segment.pos);
                        var8.newSegment = true;
                    } else {
                        assert this.callBackTmp.segment != null;

                        var8.segment = (RemoteSegment)this.callBackTmp.segment;
                    }

                    this.pieceMap.put(var5, var8);
                }
            }

            if (ElementKeyMap.isValidType(var4.getType())) {
                ++var8.blocksModOrAdd;
            } else {
                ++var8.blocksRemoved;
            }

            var8.add(var4);
        }

        return this.pieceMap;
    }

    private void clearDelayedSegmentMods() {
        int var1 = this.delayedSegmentMods.size();

        for(int var2 = 0; var2 < var1; ++var2) {
            this.receivedPiecePool.free((VoidSegmentPiece)this.delayedSegmentMods.get(var2));
        }

        this.delayedSegmentMods.clear();
    }

    private void clearBuffers() {
        this.inventoryMods.clear();
        this.segmentsAABBUpdateNeeded.clear();
        this.emptySegments.clear();
        this.pieceMap.clear();
        this.plProvider.clearProcessingData();
        this.plProvider.freeAllPieces();
    }

    private void sendInventoryModifications() {
        Iterator var1 = this.inventoryMods.entrySet().iterator();

        while(var1.hasNext()) {
            Entry var2;
            ((PlayerState)(var2 = (Entry)var1.next()).getKey()).sendInventoryModification((IntCollection)var2.getValue(), -9223372036854775808L);
        }

    }

    private void updateAABBs() {
        if (!this.segmentsAABBUpdateNeeded.isEmpty()) {
            Iterator var1 = this.segmentsAABBUpdateNeeded.iterator();

            while(var1.hasNext()) {
                Segment var2 = (Segment)var1.next();
                this.con.getSegmentProvider().enqueueAABBChange(var2);
            }
        }

    }

    private void freeEmptySegs() {
        int var1 = this.emptySegments.size();

        for(int var2 = 0; var2 < var1; ++var2) {
            Segment var3;
            SegmentData var4;
            if ((var4 = (var3 = (Segment)this.emptySegments.get(var2)).getSegmentData()) != null) {
                if (!this.isOnServer()) {
                    ((GameClientState)this.getState()).getWorldDrawer().getFlareDrawerManager().clearSegment((DrawableRemoteSegment)var3);
                }

                try {
                    var4.rwl.writeLock().lock();

                    assert var4.getSegment() == null || var4.getSegment().isEmpty();

                    assert var4.getSize() == 0;

                    this.con.getSegmentProvider().addToFreeSegmentDataFast(var4);
                } finally {
                    var4.rwl.writeLock().unlock();
                }
            }
        }

    }

    private boolean aquireSegmentLock(BlockProcessor.PieceList var1) {
        String var2 = String.valueOf(var1.absIndex);
        if (var1.blocksModOrAdd > 0 && var1.segment.isEmpty()) {
            this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Aquire", var2, "assign");
            this.con.getSegmentProvider().getFreeSegmentData().assignData(var1.segment);
            this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Aquire", var2, "assign");
        }

        int var3 = var1.size();
        SegmentData var4;
        if ((var4 = var1.segment.getSegmentData()) == null) {
            assert var1.blocksModOrAdd == 0;

            assert var1.blocksRemoved == var3;

            this.plProvider.connectionsFrom.clear();
            this.plProvider.connectionsTo.clear();
            this.plProvider.oldTypes.clear();
            System.err.println("[BLOCKPROCESSOR] Segment already empty. nothing to do");

            assert var1.segmentData == null;

            return true;
        } else {
            var1.segmentData = var4;
            this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Aquire", var2, "lock");
            boolean var5 = var4.rwl.writeLock().tryLock();
            this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Aquire", var2, "lock");
            return var5;
        }
    }
    public static void p(String s){
        System.err.println("[BP] " + s);
    }

    private void handleSegment(BlockProcessor.PieceList e) {
        String var2 = String.valueOf(e.absIndex);
        if (e.segmentData != null) {
            this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "handleSeg");
            int size = e.size();
            SegmentData var4 = e.segmentData;
            p(var4.toString());
            try {
                if (e.blocksModOrAdd > 0) {
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "onBlockAddedHandled");
                    this.con.onBlockAddedHandled();
                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "onBlockAddedHandled");
                    p(var2);
                }

                int i;
                for(i = 0; i < size; ++i) {
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i);
                    VoidSegmentPiece segmentPiece = (VoidSegmentPiece)e.get(i);
                    //INSERTED CODE @550
                    BlockModifyEvent event = new BlockModifyEvent(e, segmentPiece);
                    StarLoader.fireEvent(BlockModifyEvent.class, event);
                    ///

                    assert var4 != null : e.segment + "; " + e.blocksModOrAdd + "; " + e.size();

                    this.current.setByReference(e.segment, (byte)ByteUtil.modUSeg(segmentPiece.voidPos.x), (byte)ByteUtil.modUSeg(segmentPiece.voidPos.y), (byte)ByteUtil.modUSeg(segmentPiece.voidPos.z));
                    segmentPiece.setSegment(this.current.getSegment());
                    segmentPiece.x = this.current.x;
                    segmentPiece.y = this.current.y;
                    segmentPiece.z = this.current.z;
                    short var7 = this.current.getType();

                    try {
                        this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i + "HandleChange");
                        if (segmentPiece.onlyHitpointsChanged && (segmentPiece.isAlive() || this.current.getType() == 1)) {
                            short var23 = segmentPiece.getHitpointsByte();
                            segmentPiece.setDataByReference(this.current.getData());
                            segmentPiece.setHitpointsByte(var23);
                            segmentPiece.onlyHitpointsChanged = false;
                        } else if (segmentPiece.onlyActiveChanged) {
                            boolean var8 = segmentPiece.isActive();
                            segmentPiece.setDataByReference(this.current.getData());
                            segmentPiece.setActive(var8);
                            segmentPiece.onlyHitpointsChanged = false;
                        }

                        if (!this.isOnServer() && segmentPiece.getType() == 0 && segmentPiece.isDead()) {
                            float var25 = 1.0F / (float)Math.max(1, ShardDrawer.shardsAddedFromNTBlocks);
                            if (Math.random() < (double)var25) {
                                Vector3f var9;
                                (var9 = new Vector3f()).set((float)segmentPiece.voidPos.x, (float)segmentPiece.voidPos.y, (float)segmentPiece.voidPos.z);
                                var9.x -= 16.0F;
                                var9.y -= 16.0F;
                                var9.z -= 16.0F;
                                ((GameClientState)this.getState()).getWorldDrawer().getShards().voronoiBBShatterDelayed((PhysicsExt)((GameClientState)this.getState()).getPhysics(), var9, this.current.getType(), this.con, this.con.getGravity().source);
                                ++ShardDrawer.shardsAddedFromNTBlocks;
                            }

                            this.con.onBlockKill(this.current, (Damager)null);
                        } else {
                            int var24;
                            if ((var24 = (this.current.getType() != 0 ? this.current.getHitpointsFull() : 0) - segmentPiece.getHitpointsFull()) > 0) {
                                this.con.onBlockDamage(this.current.getAbsoluteIndex(), this.current.getType(), var24, DamageDealerType.GENERAL, (Damager)null);
                            }
                        }

                        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i + "HandleChange");

                        assert segmentPiece.getSegment() != null;

                        this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i + "ProcessPiece");
                        boolean var26 = this.processPiece(segmentPiece, segmentPiece.senderId, segmentPiece.controllerPos, var7, this.inventoryMods, false, this.emptySegments, this.segmentsAABBUpdateNeeded, this.con.getUpdateTime());
                        this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i + "ProcessPiece");
                        if (this.isOnServer()) {
                            SegmentPiece var10;
                            if (!var26) {
                                if ((var10 = new SegmentPiece(this.current)).isDead()) {
                                    var10.setHitpointsByte(1);
                                }

                                SegmentPiece var11 = new SegmentPiece(var10);
                                this.con.sendBlockMod(new RemoteSegmentPiece(var11, this.isOnServer()));
                            } else {
                                segmentPiece.forceClientSegmentAdd = e.newSegment;
                                (var10 = new SegmentPiece(segmentPiece)).forceClientSegmentAdd = e.newSegment;
                                RemoteSegmentPiece var28 = new RemoteSegmentPiece(var10, this.isOnServer());
                                this.con.sendBlockMod(var28);
                            }
                        }

                        if (var26 && segmentPiece.getType() != 0 && segmentPiece.getType() != var7) {
                            this.plProvider.oldTypes.add(var7);
                            if (ElementKeyMap.getInfoFast(segmentPiece.getType()).getControlledBy().contains(Short.valueOf((short)1)) && this.con instanceof Ship) {
                                this.plProvider.connectionsFrom.add(ElementCollection.getIndex(Ship.core));
                                this.plProvider.connectionsTo.add(segmentPiece.getAbsoluteIndex());
                            } else if (segmentPiece.controllerPos != -9223372036854775808L) {
                                this.plProvider.connectionsFrom.add(segmentPiece.controllerPos);
                                this.plProvider.connectionsTo.add(segmentPiece.getAbsoluteIndex());
                            }
                        }
                    } catch (IOException var17) {
                        var17.printStackTrace();
                    } catch (InterruptedException var18) {
                        var18.printStackTrace();
                    }

                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "BLOCK" + i);
                }

                if (e.newSegment) {
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "addImmediate");
                    this.con.getSegmentBuffer().addImmediate(e.segment);
                    this.con.getSegmentBuffer().updateBB(e.segment);
                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "addImmediate");
                }

                this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "controllerUpdate");
                i = this.plProvider.connectionsFrom.size();

                for(int var21 = 0; var21 < i; ++var21) {
                    long var22 = this.plProvider.connectionsFrom.getLong(var21);
                    long var27 = this.plProvider.connectionsTo.getLong(var21);
                    short var29 = this.plProvider.oldTypes.getShort(var21);
                    SegmentPiece var20;
                    if ((var20 = (SegmentPiece)this.pieceMap.controllers.get(var22)) == null && var22 == ElementCollection.getIndex(Ship.core)) {
                        var20 = this.con.getSegmentBuffer().getPointUnsave(var22);
                        this.pieceMap.controllers.put(var22, var20);
                    }

                    if (var20 != null) {
                        if (ElementKeyMap.isValidType(var20.getType())) {
                            try {
                                if (!this.isOnServer()) {
                                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "removedControlledFromAll" + var21);
                                    this.con.getControlElementMap().removeControlledFromAll(this.current.getAbsoluteIndex(), var29, false);
                                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "removedControlledFromAll" + var21);
                                }

                                this.con.setCurrentBlockController(var20, this.tmpPiece, var27);
                            } catch (CannotBeControlledException var16) {
                                var16.printStackTrace();
                            }
                        } else {
                            System.err.println("Exception: Client sent controller, that is type 0: " + var20 + " for " + ElementCollection.getPosFromIndex(var27, new Vector3i()));
                        }
                    } else {
                        System.err.println("WARNING: NOT CONNECTION " + ElementCollection.getPosFromIndex(var22, new Vector3i()) + " to " + ElementCollection.getPosFromIndex(var27, new Vector3i()));
                    }
                }

                this.plProvider.connectionsFrom.clear();
                this.plProvider.connectionsTo.clear();
                this.plProvider.oldTypes.clear();
                this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "controllerUpdate");
            } finally {
                if (var4 != null) {
                    this.getState().getDebugTimer().start(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "unlock");
                    var4.rwl.writeLock().unlock();
                    this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", var2, "unlock");
                }

            }

            this.getState().getDebugTimer().end(this.con, "SendableSegmentController", "DelayedMods", "Handle", String.valueOf(e.absIndex), "handleSeg");
        }
    }

    private boolean processPiece(VoidSegmentPiece var1, int var2, long var3, short var5, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var6, boolean var7, List<Segment> var8, List<Segment> var9, long var10) throws InterruptedException, IOException {
        if (this instanceof TransientSegmentController) {
            ((TransientSegmentController)this).setTouched(true, false);
        }

        boolean var16;
        if (var16 = this.segBlockProc.handleSegmentPiece(var1.getSegment(), var1, var5, var2, var6, var7, var10)) {
            long var12 = ElementCollection.getIndex(var1.voidPos);
            LongArrayList var17 = (LongArrayList)this.connectionsToAddFromPaste.remove(var12);
            if (var1.getType() != 0) {
                if (var17 != null) {
                    for(int var4 = 0; var4 < var17.size(); ++var4) {
                        long var14 = var17.getLong(var4);
                        this.con.getControlElementMap().addControllerForElement(var14, var12, var1.getType());
                    }
                }

                String var18;
                if ((var18 = (String)this.textToAddFromPaste.remove(var12)) != null) {
                    this.con.getTextMap().put(ElementCollection.getIndex4(var1.voidPos, (short)var1.getOrientation()), var18);
                }
            }
        }

        return var16;
    }

    public void handleDelayedBuklkMods() {
        while(!this.delayedBulkMods.isEmpty()) {
            BlockBulkSerialization var1 = (BlockBulkSerialization)this.delayedBulkMods.dequeue();
            this.handleReceivedBulk(var1);
        }

    }

    private void handleReceivedBulk(BlockBulkSerialization var1) {
        long var2 = System.currentTimeMillis();
        Iterator var17 = var1.buffer.entrySet().iterator();

        while(var17.hasNext()) {
            Entry var4;
            long var6 = (Long)(var4 = (Entry)var17.next()).getKey();
            ByteArrayList var18 = (ByteArrayList)var4.getValue();
            this.con.getSegmentBuffer().get(ElementCollection.getPosX(var6), ElementCollection.getPosY(var6), ElementCollection.getPosZ(var6), this.callBackTmp);
            if (this.callBackTmp.state == 1) {
                Segment var5 = this.callBackTmp.segment;

                try {
                    var5.getSegmentData().checkWritable();
                } catch (SegmentDataWriteException var14) {
                    SegmentDataWriteException.replaceData(var5);
                }

                try {
                    int var20 = 0;
                    if (var18.size() == 1) {
                        assert var18.get(0) == 0;

                        try {
                            var5.getSegmentData().reset(var2);
                        } catch (SegmentDataWriteException var15) {
                            throw new RuntimeException("Chunk should be normal here", var15);
                        }
                    } else {
                        boolean var7 = false;

                        while(true) {
                            if (var20 >= var18.size()) {
                                if (var7 && ((RemoteSegment)var5).getSegmentData().getSize() == 0) {
                                    SegmentData var21 = ((RemoteSegment)var5).getSegmentData();
                                    this.con.getSegmentProvider().addToFreeSegmentDataFast(var21);
                                }

                                ((RemoteSegment)var5).dataChanged(var7);
                                break;
                            }

                            byte var8 = (byte)var18.get(var20);
                            byte var9 = (byte)var18.get(var20 + 1);
                            byte var10 = (byte)var18.get(var20 + 2);
                            int var11 = var18.get(var20 + 3) & 255;
                            int var12 = SegmentData.getInfoIndex(var8, var9, var10);
                            short var13 = var5.getSegmentData().getType(var12);
                            if (var11 <= 0 && var13 != 1) {
                                var7 = true;
                                ((RemoteSegment)var5).getSegmentData().setInfoElement(var8, var9, var10, (short)0, false, (long)var12, var2);
                                if (((GameClientState)this.getState()).getWorldDrawer() != null && (var8 % 2 == 0 && var9 % 2 == 0 && var10 % 2 == 0 || Math.random() > 0.9D)) {
                                    Vector3f var22 = new Vector3f();
                                    var5.getAbsoluteElemPos(var8, var9, var10, var22);
                                    var22.x -= 16.0F;
                                    var22.y -= 16.0F;
                                    var22.z -= 16.0F;
                                    ((GameClientState)this.getState()).getWorldDrawer().getShards().voronoiBBShatterDelayed((PhysicsExt)((GameClientState)this.getState()).getPhysics(), var22, var13, this.con, this.con.getGravity().source);
                                }
                            } else {
                                ((RemoteSegment)var5).getSegmentData().setHitpointsByte(var12, var11);
                            }

                            var20 += 4;
                        }
                    }
                } catch (SegmentDataWriteException var16) {
                    throw new RuntimeException("Should be already changed before this", var16);
                }
            }

            BlockBulkSerialization.freeBufferClient(var18);
            CollisionObject var19;
            if ((var19 = this.con.getPhysicsDataContainer().getObject()) != null) {
                var19.activate(true);
                ((RigidBody)var19).applyGravity();
            }
        }

    }

    private StateInterface getState() {
        return this.con.getState();
    }

    public boolean isOnServer() {
        return this.con.isOnServer();
    }

    public void received(BlockBulkSerialization var1) {
        this.delayedBulkMods.enqueue(var1);
    }

    public void receivedMods(NetworkSegmentProvider var1) {
        int var2;
        for(var2 = 0; var2 < var1.modificationBuffer.getReceiveBuffer().size(); ++var2) {
            VoidSegmentPiece var3;
            (var3 = this.receivedPiecePool.get()).setByValue((VoidSegmentPiece)((RemoteSegmentPiece)var1.modificationBuffer.getReceiveBuffer().get(var2)).get());

            assert !this.con.isOnServer() || var3.senderId != 0;

            this.delayedSegmentMods.add(var3);
        }

        short var4;
        short var5;
        VoidSegmentPiece var7;
        short var8;
        for(var2 = 0; var2 < var1.killBuffer.getReceiveBuffer().size(); var2 += 4) {
            var8 = var1.killBuffer.getReceiveBuffer().getShort(var2);
            var4 = var1.killBuffer.getReceiveBuffer().getShort(var2 + 1);
            var5 = var1.killBuffer.getReceiveBuffer().getShort(var2 + 2);
            short var6 = var1.killBuffer.getReceiveBuffer().getShort(var2 + 3);
            (var7 = this.receivedPiecePool.get()).voidPos.set(var8, var4, var5);
            var7.onlyHitpointsChanged = true;
            var7.setType((short)0);
            var7.setHitpointsByte(var6);
            this.delayedSegmentMods.add(var7);
        }

        for(var2 = 0; var2 < var1.activeChangedTrueBuffer.getReceiveBuffer().size(); var2 += 3) {
            var8 = var1.activeChangedTrueBuffer.getReceiveBuffer().getShort(var2);
            var4 = var1.activeChangedTrueBuffer.getReceiveBuffer().getShort(var2 + 1);
            var5 = var1.activeChangedTrueBuffer.getReceiveBuffer().getShort(var2 + 2);
            (var7 = this.receivedPiecePool.get()).voidPos.set(var8, var4, var5);
            var7.setActive(true);
            var7.onlyActiveChanged = true;
            this.delayedSegmentMods.add(var7);
        }

        for(var2 = 0; var2 < var1.activeChangedFalseBuffer.getReceiveBuffer().size(); var2 += 3) {
            var8 = var1.activeChangedFalseBuffer.getReceiveBuffer().getShort(var2);
            var4 = var1.activeChangedFalseBuffer.getReceiveBuffer().getShort(var2 + 1);
            var5 = var1.activeChangedFalseBuffer.getReceiveBuffer().getShort(var2 + 2);
            (var7 = this.receivedPiecePool.get()).voidPos.set(var8, var4, var5);
            var7.setActive(false);
            var7.onlyActiveChanged = true;
            this.delayedSegmentMods.add(var7);
        }

        for(var2 = 0; var2 < var1.salvageBuffer.getReceiveBuffer().size(); var2 += 3) {
            var8 = var1.salvageBuffer.getReceiveBuffer().getShort(var2);
            var4 = var1.salvageBuffer.getReceiveBuffer().getShort(var2 + 1);
            var5 = var1.salvageBuffer.getReceiveBuffer().getShort(var2 + 2);
            VoidSegmentPiece var9;
            (var9 = this.receivedPiecePool.get()).voidPos.set(var8, var4, var5);
            var9.setType((short)0);
            var9.setHitpointsByte(1);
            this.delayedSegmentMods.add(var9);
        }

        for(var2 = 0; var2 < var1.modificationBulkBuffer.getReceiveBuffer().size(); ++var2) {
            this.received((BlockBulkSerialization)((RemoteBlockBulk)var1.modificationBulkBuffer.getReceiveBuffer().get(var2)).get());
        }

    }

    public void onBlockChanged(RemoteSegment var1) {
        this.segmentsChanged.put(ElementCollection.getIndex(var1.pos), var1);
    }

    public void markNeighbors(Segment var1) {
        this.markNeighborForChanged.add((RemoteSegment)var1);
    }

    public class SegmentBlockProcessorClient extends BlockProcessor.SegmentBlockProcessor {
        public SegmentBlockProcessorClient() {
            super();
        }

        public boolean checkPermission(SegmentPiece var1, short var2, int var3) {
            return true;
        }

        public boolean handleSegmentPiece(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5, boolean var6, long var7) throws InterruptedException {
            boolean var9;
            if (var9 = super.handleSegmentPiece(var1, var2, var3, var4, var5, var6, var7)) {
                BlockProcessor.this.markNeighbors(var1);
            }

            return var9;
        }

        protected void checkSurround(Segment var1, SegmentPiece var2, short var3) {
            if (!var1.isEmpty() && !ElementInformation.isAlwaysPhysical(var2.getType()) && var2.isEdgeOfSegment()) {
                byte var8 = var2.getCornerSegmentsDiffs();
                int var9 = 0;
                byte var4 = 0;
                byte var5 = 0;
                byte var6 = 0;

                for(int var7 = 0; var7 < 6; ++var7) {
                    if ((Element.SIDE_FLAG[var7] & var8) == Element.SIDE_FLAG[var7]) {
                        var1.setChangedSurround(Element.DIRECTIONSb[var7].x, Element.DIRECTIONSb[var7].y, Element.DIRECTIONSb[var7].z);
                        ++var9;
                        var4 += Element.DIRECTIONSb[var7].x;
                        var5 += Element.DIRECTIONSb[var7].y;
                        var6 += Element.DIRECTIONSb[var7].z;
                    }
                }

                if (var9 > 2) {
                    var1.setChangedSurround(var4, var5, 0);
                    var1.setChangedSurround(var4, 0, var6);
                    var1.setChangedSurround(0, var5, var6);
                    var1.setChangedSurround(var4, var5, var6);
                    return;
                }

                if (var9 > 1) {
                    var1.setChangedSurround(var4, var5, var6);
                }
            }

        }

        protected void onPieceActiveChanged(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5) {
            ++this.changes;
            ++this.total;

            assert var2.getType() != 0;

            if (BlockProcessor.this.con instanceof ManagedSegmentController && ((ManagedSegmentController)BlockProcessor.this.con).getManagerContainer().getTypesThatNeedActivation().contains(var2.getType()) || ElementKeyMap.getInfo(var2.getType()).isController()) {
                SegmentPiece var6 = new SegmentPiece(var2);
                BlockProcessor.this.con.getNeedsActiveUpdateClient().enqueue(var6);
            }

        }

        protected void onPieceRemoved(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5) {
            ++this.removes;
            ++this.total;
        }
    }

    public class SegmentBlockProcessorServer extends BlockProcessor.SegmentBlockProcessor {
        private final GameServerState state;

        public SegmentBlockProcessorServer() {
            super();
            this.state = (GameServerState)BlockProcessor.this.con.getState();
        }

        public boolean handleSegmentPiece(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5, boolean var6, long var7) throws InterruptedException {
            if (BlockProcessor.this.con.getLastModifierId() != var4) {
                BlockProcessor.this.con.setLastModifierChanged(true);
                BlockProcessor.this.con.setLastModifierId(var4);
            }

            return super.handleSegmentPiece(var1, var2, var3, var4, var5, var6, var7);
        }

        protected void onPieceAdded(Segment var1, SegmentPiece var2, short var3, short var4, int var5, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var6) {
            super.onPieceAdded(var1, var2, var3, var4, var5, var6);
            if (var3 == 291) {
                System.err.println("[SERVER] FACTION BLOCK ADDED TO " + this + "; resetting faction!");
                BlockProcessor.this.con.railController.resetFactionForEntitiesWithoutFactionBlock(BlockProcessor.this.con.getFactionId());
            }

            try {
                short var8;
                if (ElementKeyMap.isValidType(var8 = var2.getType())) {
                    if (ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var8).getSourceReference())) {
                        var8 = (short)ElementKeyMap.getInfoFast(var8).getSourceReference();
                    }

                    BlockProcessor.this.con.getHpController().onManualAddBlock(ElementKeyMap.getInfo(var8));
                }

                if (!var2.getSegmentController().isVirtualBlueprint()) {
                    PlayerState var9 = this.state.getPlayerFromStateId(var5);
                    IntOpenHashSet var10;
                    if ((var10 = (IntOpenHashSet)var6.get(var9)) == null) {
                        var10 = new IntOpenHashSet();
                        var6.put(var9, var10);
                    }

                    int var11 = var9.getSelectedBuildSlot();
                    if (var9.getInventory((Vector3i)null).getType(var11) == var8) {
                        var9.getInventory((Vector3i)null).inc(var11, var8, -1);
                        var10.add(var11);
                        return;
                    }

                    var9.getInventory((Vector3i)null).decreaseBatch(var8, 1, var10);
                }

            } catch (PlayerNotFountException var7) {
                var7.printStackTrace();
            }
        }

        public boolean checkPermission(SegmentPiece var1, short var2, int var3) {
            assert var3 != 0 : "sender " + var3 + " was server but also received on server";

            BoundingBox var4 = BlockProcessor.this.con.getSegmentBuffer().getBoundingBox();

            try {
                PlayerState var5 = this.state.getPlayerFromStateId(var3);
                if (var2 != 0 && this.state.getGameConfig().isHasMaxDim() && !this.state.getGameConfig().isOk(var4, BlockProcessor.this.con)) {
                    var5.sendServerMessagePlayerError(new Object[]{12, var4.toStringSize(-2), this.state.getGameConfig().toStringAllowedSize(BlockProcessor.this.con)});
                    return false;
                } else {
                    boolean var12 = this.state.isAdmin(var5.getName());
                    int var6;
                    int var7;
                    if (var2 != 0 && (var6 = var5.getInventory((Vector3i)null).getCount(var5.getSelectedBuildSlot(), var2)) <= 0 && (var7 = var5.getInventory((Vector3i)null).getOverallQuantity(var2)) <= 0) {
                        System.err.println("[SERVER] place-before: Player " + var5 + " doesnt have enough in the slot he wants to build with: " + var1 + "; slot " + var6 + "; overall " + var7);
                        if (!var12 || !Segment.ALLOW_ADMIN_OVERRIDE) {
                            return false;
                        }

                        System.err.println("[SERVER] overwritten by admin rights");
                    }

                    if (!var12 && !BlockProcessor.this.con.allowedToEdit(var5)) {
                        Object[] var10 = new Object[]{13, var5.getName(), BlockProcessor.this.con.toNiceString()};
                        System.err.println(var10);
                        if (System.currentTimeMillis() - GameServerState.lastBCMessage > 2000L) {
                            this.state.getController().broadcastMessage(var10, 3);
                        }

                        return false;
                    } else {
                        if (ElementKeyMap.isValidType(var2) && !var1.getSegmentController().isVirtualBlueprint()) {
                            int var10000 = var5.getInventory((Vector3i)null).getFirstSlot(var2, true);
                            boolean var9 = false;
                            if (var10000 == -1) {
                                System.err.println("[SERVER] place-after: Player " + var5 + " doesnt have enough in the slot he wants to build with: " + var1);
                                if (!var12 || !Segment.ALLOW_ADMIN_OVERRIDE) {
                                    return false;
                                }

                                System.err.println("[SERVER] overwritten by admin rights");
                            }
                        }

                        return true;
                    }
                }
            } catch (PlayerNotFountException var8) {
                var8.printStackTrace();
                Object[] var11 = new Object[]{14, var3, BlockProcessor.this.con.toNiceString()};
                System.err.printf("SERVER-WARNING: unknown(%s)\ntried to edit\n%s\n(faction access denied)\n", var3, BlockProcessor.this.con.toNiceString());
                if (System.currentTimeMillis() - GameServerState.lastBCMessage > 2000L) {
                    this.state.getController().broadcastMessage(var11, 3);
                }

                return false;
            }
        }

        protected void checkSurround(Segment var1, SegmentPiece var2, short var3) {
        }

        protected void onPieceActiveChanged(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5) {
            ++this.changes;
            ++this.total;
        }

        protected void onPieceRemoved(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5) {
            ++this.removes;
            ++this.total;
            if (ElementKeyMap.isValidType(var3)) {
                if (ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var3).getSourceReference())) {
                    var3 = (short)ElementKeyMap.getInfoFast(var3).getSourceReference();
                }

                BlockProcessor.this.con.getHpController().onManualRemoveBlock(ElementKeyMap.getInfo(var3));
            }

            if (!BlockProcessor.this.con.isVirtualBlueprint() && var4 != 0) {
                try {
                    PlayerState var7 = this.state.getPlayerFromStateId(var4);
                    IntOpenHashSet var8;
                    if ((var8 = (IntOpenHashSet)var5.get(var7)) == null) {
                        var8 = new IntOpenHashSet();
                        var5.put(var7, var8);
                    }

                    if (BlockProcessor.this.con.isScrap()) {
                        if (Universe.getRandom().nextFloat() > 0.5F) {
                            var3 = 546;
                        } else {
                            var3 = 547;
                        }
                    }

                    var8.add(var7.getInventory().incExistingOrNextFreeSlot(var3, 1));
                    if (ServerConfig.ENABLE_BREAK_OFF.isOn()) {
                        ((EditableSendableSegmentController)BlockProcessor.this.con).checkCore(var2);
                    }

                    return;
                } catch (PlayerNotFountException var6) {
                    var6.printStackTrace();
                }
            }

        }
    }

    public abstract class SegmentBlockProcessor {
        protected int removes;
        protected int changes;
        protected int adds;
        protected int total;

        public SegmentBlockProcessor() {
        }

        public void reset() {
            this.removes = 0;
            this.changes = 0;
            this.adds = 0;
            this.total = 0;
        }

        public abstract boolean checkPermission(SegmentPiece var1, short var2, int var3);

        public boolean handleSegmentPiece(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5, boolean var6, long var7) throws InterruptedException {
            assert var1 != null : var2 + "; " + BlockProcessor.this.con;

            short var9;
            if (ElementKeyMap.isValidType(var9 = var2.getType()) && ElementKeyMap.isValidType(ElementKeyMap.getInfoFast(var9).getSourceReference())) {
                var9 = (short)ElementKeyMap.getInfoFast(var9).getSourceReference();
            }

            if (var4 != 0 && !this.checkPermission(var2, var9, var4)) {
                return false;
            } else {
                int var10;
                try {
                    var10 = var1.getSegmentData().applySegmentData(var2.x, var2.y, var2.z, var2.getData(), 0, var6, var2.getAbsoluteIndex(), false, true, var7);
                } catch (SegmentDataWriteException var12) {
                    SegmentDataWriteException.replaceData(var1);

                    try {
                        var10 = var1.getSegmentData().applySegmentData(var2.x, var2.y, var2.z, var2.getData(), 0, var6, var2.getAbsoluteIndex(), false, true, var7);
                    } catch (SegmentDataWriteException var11) {
                        throw new RuntimeException(var11);
                    }
                }

                switch(var10) {
                    case 0:
                        this.onPieceAdded(var1, var2, var9, var3, var4, var5);
                        break;
                    case 1:
                        this.onPieceRemoved(var1, var2, var3, var4, var5);
                    case 2:
                    case 3:
                        break;
                    case 4:
                        this.onPieceActiveChanged(var1, var2, var3, var4, var5);
                        break;
                    default:
                        throw new RuntimeException("unknown change id: " + var10);
                }

                this.checkSurround(var1, var2, var9);
                return true;
            }
        }

        protected abstract void checkSurround(Segment var1, SegmentPiece var2, short var3);

        protected abstract void onPieceRemoved(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5);

        protected abstract void onPieceActiveChanged(Segment var1, SegmentPiece var2, short var3, int var4, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var5);

        protected void onPieceAdded(Segment var1, SegmentPiece var2, short var3, short var4, int var5, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var6) {
            ++this.adds;
            ++this.total;
        }
    }

    public static class PieceList extends ObjectArrayList<VoidSegmentPiece> {
        private static final long serialVersionUID = 2296926034557584508L;
        public RemoteSegment segment;
        public long absIndex;
        public int x;
        public int y;
        public int z;
        private boolean newSegment;
        public int blocksModOrAdd;
        public int blocksRemoved;
        public SegmentData segmentData;

        private PieceList() {
            this.absIndex = -9223372036854775808L;
            this.x = -2147483648;
            this.y = -2147483648;
            this.z = -2147483648;
        }

        public void clear() {
            super.clear();
            this.newSegment = false;
            this.segment = null;
            this.absIndex = -9223372036854775808L;
            this.x = -2147483648;
            this.y = -2147483648;
            this.z = -2147483648;
            this.blocksRemoved = 0;
            this.blocksModOrAdd = 0;
            this.segmentData = null;
        }

        public boolean add(VoidSegmentPiece var1) {
            return super.add(var1);
        }
    }

    static class SegmentChangeContainer extends Long2ObjectOpenHashMap<BlockProcessor.PieceList> {
        private static final long serialVersionUID = -802243669446537813L;
        private Long2ObjectOpenHashMap<SegmentPiece> controllers;

        private SegmentChangeContainer() {
            this.controllers = new Long2ObjectOpenHashMap();
        }

        public void clear() {
            super.clear();
            this.controllers.clear();
        }
    }

    static class PieceListProvider {
        private final List<SegmentPiece> sg;
        private final List<SegmentPiece> addwd;
        private final List<BlockProcessor.PieceList> pl;
        private final LongArrayList connectionsFrom;
        private final LongArrayList connectionsTo;
        private final ShortArrayList oldTypes;

        private PieceListProvider() {
            this.sg = new ObjectArrayList();
            this.addwd = new ObjectArrayList();
            this.pl = new ObjectArrayList();
            this.connectionsFrom = new LongArrayList();
            this.connectionsTo = new LongArrayList();
            this.oldTypes = new ShortArrayList();
        }

        public SegmentPiece getPiece() {
            SegmentPiece var1;
            if (this.sg.isEmpty()) {
                var1 = new SegmentPiece();
            } else {
                var1 = (SegmentPiece)this.sg.remove(this.sg.size() - 1);
            }

            this.addwd.add(var1);
            return var1;
        }

        public void freeAllPieces() {
            Iterator var1 = this.addwd.iterator();

            while(var1.hasNext()) {
                SegmentPiece var2 = (SegmentPiece)var1.next();
                this.freePiece(var2);
            }

            this.addwd.clear();
        }

        public void clearProcessingData() {
            this.connectionsFrom.clear();
            this.connectionsTo.clear();
            this.oldTypes.clear();
        }

        private void freePiece(SegmentPiece var1) {
            var1.reset();
            this.sg.add(var1);
        }

        public BlockProcessor.PieceList get() {
            return this.pl.isEmpty() ? new BlockProcessor.PieceList() : (BlockProcessor.PieceList)this.pl.remove(this.pl.size() - 1);
        }

        public void free(BlockProcessor.PieceList var1) {
            var1.clear();
            this.pl.add(var1);
        }
    }
}
