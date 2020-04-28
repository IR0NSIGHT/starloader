//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import api.listener.events.block.BlockKillEvent;
import api.mod.StarLoader;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.schema.common.LogUtil;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.effects.RaisingIndication;
import org.schema.game.client.view.effects.ShieldDrawer;
import org.schema.game.client.view.gui.shiphud.HudIndicatorOverlay;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.SegmentController.PullPermission;
import org.schema.game.common.controller.ai.SegmentControllerAIInterface;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.ActivationManagerInterface;
import org.schema.game.common.controller.elements.ExplosiveManagerContainerInterface;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.TransporterModuleInterface;
import org.schema.game.common.controller.elements.activation.ActivationCollectionManager;
import org.schema.game.common.controller.elements.power.PowerAddOn;
import org.schema.game.common.controller.elements.power.PowerManagerInterface;
import org.schema.game.common.controller.elements.sensor.SensorCollectionManager;
import org.schema.game.common.controller.elements.transporter.TransporterCollectionManager;
import org.schema.game.common.controller.io.IOFileManager;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SegmentRetrieveCallback;
import org.schema.game.common.data.VoidUniqueSegmentPiece;
import org.schema.game.common.data.blockeffects.BlockEffectManager;
import org.schema.game.common.data.blockeffects.config.ConfigProviderSource;
import org.schema.game.common.data.element.ActivationTrigger;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementDocking;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.meta.weapon.MarkerBeam;
import org.schema.game.common.data.physics.PairCachingGhostObjectAlignable;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.common.util.FastCopyLongOpenHashSet;
import org.schema.game.network.objects.InterconnectStructureRequest;
import org.schema.game.network.objects.NetworkSegmentController;
import org.schema.game.network.objects.NetworkSegmentProvider;
import org.schema.game.network.objects.remote.RemoteBlockBulk;
import org.schema.game.network.objects.remote.RemoteInterconnectStructure;
import org.schema.game.network.objects.remote.RemoteSegmentPiece;
import org.schema.game.network.objects.remote.RemoteServerMessage;
import org.schema.game.network.objects.remote.RemoteTextBlockPair;
import org.schema.game.network.objects.remote.TextBlockPair;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.client.ClientState;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.remote.RemoteShortBuffer;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.network.objects.remote.RemoteVector3f;
import org.schema.schine.network.objects.remote.RemoteVector4f;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public abstract class SendableSegmentController extends SegmentController implements Sendable {
    public final ObjectArrayFIFOQueue<TextBlockPair> receivedTextBlocks = new ObjectArrayFIFOQueue();
    public final ObjectArrayFIFOQueue<InterconnectStructureRequest> receivedInterconnectedStructure = new ObjectArrayFIFOQueue();
    public final ObjectArrayFIFOQueue<TextBlockPair> receivedTextBlockRequests = new ObjectArrayFIFOQueue();
    public final LongArrayFIFOQueue textBlockChangeInLongRange = new LongArrayFIFOQueue();
    private final LongArrayFIFOQueue blockActivationBuffer = new LongArrayFIFOQueue();
    private final BlockEffectManager blockEffectManager;
    private final BlockProcessor blockProcessor;
    private final ObjectArrayFIFOQueue<Vector4f> remoteHits = new ObjectArrayFIFOQueue();
    private final ObjectArrayFIFOQueue<Vector4f> remoteShieldHits = new ObjectArrayFIFOQueue();
    private final SegmentPiece tmpPiece = new SegmentPiece();
    private final Long2ObjectOpenHashMap<SendableSegmentController.DelayedAct> delayActivationBuffer = new Long2ObjectOpenHashMap();
    private final Long2ObjectOpenHashMap<SendableSegmentController.DelayedAct> delayActivationBufferNonRepeating = new Long2ObjectOpenHashMap();
    private final Long2LongOpenHashMap cooldownBlocks = new Long2LongOpenHashMap();
    private final ObjectArrayList<SendableSegmentProvider> listeners = new ObjectArrayList();
    public SendableSegmentController.SignalQueue signalQueue = new SendableSegmentController.SignalQueue();
    public SignalTrace currentTrace = null;
    public int signalId;
    private NetworkSegmentController networkEntity;
    private int lastModifierId;
    private final SendableSegmentController.DisplayReplace displayReplace = new SendableSegmentController.DisplayReplace();
    private boolean lastModifierChanged;
    private SendableSegmentProvider serverSendableSegmentProvider;
    private Vector3i tmpPos = new Vector3i();
    private SegmentRetrieveCallback tmpRtrv = new SegmentRetrieveCallback();
    private int failedActivating;
    public final ObjectArrayFIFOQueue<ServerMessage> receivedBlockMessages = new ObjectArrayFIFOQueue();
    private final ObjectArrayFIFOQueue<SendableSegmentProvider> flagRemoveCachedTextBoxes = new ObjectArrayFIFOQueue();
    private long warnActivations;
    private long logicCooldown;
    private RemoteSector currentConfigSectorProjection;
    private String ownerChangeRequested;
    private int ownerChangeRequestedClientId;
    private long lastSignalStopMsgSent;
    private static Long2ObjectOpenHashMap<SendableSegmentController.BlockActiveReaction> blockActivations = new Long2ObjectOpenHashMap();
    private static final List<SendableSegmentController.BlockActiveReaction> blockActPool = new ObjectArrayList();
    private static ThreadLocal<SendableSegmentController.SignalTracePool> signalThread = new ThreadLocal<SendableSegmentController.SignalTracePool>() {
        protected final SendableSegmentController.SignalTracePool initialValue() {
            return new SendableSegmentController.SignalTracePool();
        }
    };

    public SendableSegmentController(StateInterface var1) {
        super(var1);
        this.getControlElementMap().setSendableSegmentController(this);
        this.blockEffectManager = new BlockEffectManager(this);
        this.blockProcessor = new BlockProcessor(this);
    }

    public SendableSegmentProvider createNetworkListenEntity() {
        SendableSegmentProvider var1;
        (var1 = new SendableSegmentProvider(this.getState())).initialize();
        var1.setProvidedObject(this);
        ((ClientSegmentProvider)this.getSegmentProvider()).setSendableSegmentProvider(var1);
        return var1;
    }

    public static Tag listToTagStruct(Long2ObjectOpenHashMap<SendableSegmentController.DelayedAct> var0, String var1) {
        Tag[] var2;
        (var2 = new Tag[var0.size() + 1])[var0.size()] = FinishTag.INST;
        int var3 = 0;

        for(Iterator var6 = var0.entrySet().iterator(); var6.hasNext(); ++var3) {
            Entry var4 = (Entry)var6.next();
            Tag[] var5;
            (var5 = new Tag[3])[0] = new Tag(Type.LONG, (String)null, ((SendableSegmentController.DelayedAct)var4.getValue()).encode);
            var5[1] = new Tag(Type.LONG, (String)null, ((SendableSegmentController.DelayedAct)var4.getValue()).time);
            var5[2] = FinishTag.INST;
            var2[var3] = new Tag(Type.STRUCT, (String)null, var5);
        }

        return new Tag(Type.STRUCT, var1, var2);
    }

    public void backUpAllRawFiles() {
        File[] var1 = (new FileExt(GameServerState.SEGMENT_DATA_DATABASE_PATH)).listFiles(new FileFilter() {
            public boolean accept(File var1) {
                return var1.getName().startsWith(SendableSegmentController.this.getUniqueIdentifier());
            }
        });
        long var3 = System.currentTimeMillis();

        for(int var2 = 0; var2 < var1.length; ++var2) {
            FileExt var5;
            if (!(var5 = new FileExt("debugBackupRAW")).exists()) {
                var5.mkdir();
            }

            var5 = new FileExt("debugBackupRAW/" + var3 + "###" + var1[var2].getName());

            try {
                FileUtil.copyFile(var1[var2], var5);
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

    }

    public void onBlockSinglePlacedOnServer() {
    }

    public void destroyPersistent() {
        Starter.modManager.onSegmentControllerDestroyedPermanently(this);
        super.destroyPersistent();

        assert this.isOnServer();

    }

    protected void fireActivation(ActivationTrigger var1) throws IOException {
        if (var1.getType() == 411) {
            for(int var8 = 0; var8 < 6; ++var8) {
                Vector3i var3;
                (var3 = ElementCollection.getPosFromIndex(var1.pos, new Vector3i())).add(Element.DIRECTIONSi[var8]);
                SegmentPiece var4;
                if ((var4 = this.getSegmentBuffer().getPointUnsave(var3)) != null && var4.isValid() && var4.getInfo().isSignal()) {
                    PairCachingGhostObjectAlignable var5;
                    PositionControl var9;
                    if ((var9 = this.getControlElementMap().getControlledElements((short)56, var3)).getControlMap().size() > 0 && var1.obj instanceof PairCachingGhostObjectAlignable && (var5 = (PairCachingGhostObjectAlignable)var1.obj).getObj() instanceof PlayerCharacter) {
                        PlayerCharacter var11 = (PlayerCharacter)var5.getObj();
                        long var6 = var9.getControlPosMap().iterator().nextLong();
                        SegmentPiece var10;
                        if ((var10 = var4.getSegment().getSegmentController().getSegmentBuffer().getPointUnsave(var6)) != null) {
                            var11.activateGravity(var10);
                        }
                    }

                    this.sendBlockActivation(ElementCollection.getEncodeActivation(var4, true, !var4.isActive(), false));
                }
            }

        } else if (var1.getType() == 412) {
            SegmentPiece var2;
            if ((var2 = this.getSegmentBuffer().getPointUnsave(var1.pos)) != null) {
                this.sendBlockActivation(ElementCollection.getEncodeActivation(var2, true, !var2.isActive(), false));
            }

        } else {
            System.err.println("[TRIGGER] Error: Unknown type: " + ElementKeyMap.toString(var1.getType()));
        }
    }

    public int writeAllBufferedSegmentsToDatabase(boolean var1, boolean var2, boolean var3) throws IOException {
        if (this instanceof TransientSegmentController && !((TransientSegmentController)this).isTouched()) {
            if (this instanceof Planet || this instanceof SpaceStation) {
                System.err.println("[SENDABLESEGMENTVONTROLLER][WRITE] " + this.getState() + " skipping writing transient object " + this);
            }

            return 0;
        } else {
            int var4 = 0;
            if (var1) {
                Iterator var5 = this.getDockingController().getDockedOnThis().iterator();

                while(var5.hasNext()) {
                    ElementDocking var6;
                    if ((var6 = (ElementDocking)var5.next()).from.getSegment().getSegmentController() != this) {
                        var4 += var6.from.getSegment().getSegmentController().writeAllBufferedSegmentsToDatabase(var1, var2, var3);
                    }
                }

                var5 = this.railController.next.iterator();

                while(var5.hasNext()) {
                    RailRelation var16;
                    if ((var16 = (RailRelation)var5.next()).docked.getSegmentController() != this) {
                        var4 += var16.docked.getSegmentController().writeAllBufferedSegmentsToDatabase(var1, var2, var3);
                    }
                }
            }

            if (this.isOnServer() && ServerConfig.DEBUG_SEGMENT_WRITING.isOn()) {
                this.backUpAllRawFiles();
            }

            long var15 = System.currentTimeMillis();
            long var7 = 0L;
            synchronized(this.getSegmentBuffer()) {
                SendableSegmentController.Writer var10;
                (var10 = new SendableSegmentController.Writer(false)).forcedTimestamp = var3;
                if (var2) {
                    this.getSegmentBuffer().iterateOverEveryElement(var10, true);
                } else if (this.getSegmentBuffer().getLastBufferChanged() > this.getSegmentBuffer().getLastBufferSaved()) {
                    System.err.println("[SENDABLESEGMENTVONTROLLER][WRITE] " + this.getState() + " WRITING BLOCK DATA " + this + " since it HAS changed: lastChanged: " + this.getSegmentBuffer().getLastBufferChanged() + " / last written " + this.getSegmentBuffer().getLastBufferSaved() + "; NonEmpty: " + this.getSegmentBuffer().getTotalNonEmptySize());
                    this.getSegmentBuffer().iterateOverEveryChangedElement(var10, true);
                }

                this.getSegmentBuffer().setLastBufferSaved(System.currentTimeMillis());
                var4 += var10.writtenSegments;

                try {
                    if (var4 > 0) {
                        long var11 = System.currentTimeMillis();
                        IOFileManager.writeAllHeaders(this.getSegmentProvider().getSegmentDataIO().getManager());
                        var7 = System.currentTimeMillis() - var11;
                    }
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

            if (this.isOnServer() && ServerConfig.FORCE_DISK_WRITE_COMPLETION.isOn()) {
                this.forceAllRawFiles();
            }

            long var9;
            if ((var9 = System.currentTimeMillis() - var15) > 10L) {
                System.err.println("[SENDABLESEGMENTVONTROLLER][WRITE] WARNING: segment writing of " + this + " on " + this.getState() + " took: " + var9 + " ms (file header: " + var7 + "ms)");
            }

            return var4;
        }
    }

    public void cleanUpOnEntityDelete() {
        Starter.modManager.onSegmentControllerDelete(this);
        if (!this.isOnServer()) {
            SendableSegmentProvider var1;
            if ((var1 = ((ClientSegmentProvider)this.getSegmentProvider()).getSendableSegmentProvider()) != null && var1.getNetworkObject() != null) {
                var1.getNetworkObject().signalDelete.set(true, true);
            }
        } else if (this.serverSendableSegmentProvider != null) {
            this.serverSendableSegmentProvider.markForPermanentDelete(true);
            this.serverSendableSegmentProvider = null;
        }

        super.cleanUpOnEntityDelete();
    }

    public NetworkSegmentController getNetworkObject() {
        return this.networkEntity;
    }

    public void setNetworkObject(NetworkSegmentController var1) {
        this.networkEntity = var1;
    }

    public void updateLocal(Timer var1) {
        this.getState().getDebugTimer().start(this, "SendableSegmentController");
        super.updateLocal(var1);
        this.getState().getDebugTimer().start(this, "SendableSegmentController", "ConfigManager");
        this.getConfigManager().updateLocal(var1, this);
        this.getState().getDebugTimer().end(this, "SendableSegmentController", "ConfigManager");
        if (this.currentConfigSectorProjection != this.getRemoteSector()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "ProjectionAddRemove");
            if (this.currentConfigSectorProjection != null) {
                this.currentConfigSectorProjection.onRemovedEntityFromSector(this);
            }

            this.currentConfigSectorProjection = this.getRemoteSector();
            if (this.currentConfigSectorProjection != null) {
                this.currentConfigSectorProjection.onAddedEntityFromSector(this);
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "ProjectionAddRemove");
        }

        if (this.currentConfigSectorProjection != null) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "ProjectionUpdate");
            this.currentConfigSectorProjection.entityUpdateInSector(this);
            this.getState().getDebugTimer().end(this, "SendableSegmentController", "ProjectionUpdate");
        }

        if (!this.flagRemoveCachedTextBoxes.isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "TextBoxes");
            synchronized(this.flagRemoveCachedTextBoxes) {
                while(!this.flagRemoveCachedTextBoxes.isEmpty()) {
                    ((SendableSegmentProvider)this.flagRemoveCachedTextBoxes.dequeue()).clearChangedTextBoxLongRangeIndices();
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "TextBoxes");
        }

        if (this.ownerChangeRequested != null) {
            assert this.isOnServer();

            RegisteredClientOnServer var2;
            if ((var2 = (RegisteredClientOnServer)((GameServerState)this.getState()).getClients().get(this.ownerChangeRequestedClientId)) != null && var2.getPlayerObject() instanceof PlayerState) {
                PlayerState var3 = (PlayerState)var2.getPlayerObject();
                if (this.getFactionId() != 0 && (this.getFactionId() != var3.getFactionId() || !this.isSufficientFactionRights(var3))) {
                    var3.sendServerMessagePlayerError(new Object[]{131});
                } else if (this.ownerChangeRequested.length() == 0) {
                    this.currentOwnerLowerCase = "";
                } else if (var3.isAdmin() || this.ownerChangeRequested.toLowerCase(Locale.ENGLISH).equals(var3.getName().toLowerCase(Locale.ENGLISH))) {
                    this.currentOwnerLowerCase = this.ownerChangeRequested.toLowerCase(Locale.ENGLISH);
                }
            }

            this.ownerChangeRequested = null;
            this.ownerChangeRequestedClientId = 0;
        }

        if (!this.textBlockChangeInLongRange.isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "TextBoxesLR");
            synchronized(this.textBlockChangeInLongRange) {
                while(!this.textBlockChangeInLongRange.isEmpty()) {
                    if (!this.isInClientRange()) {
                        assert !this.isOnServer();

                        this.getTextMap().remove(this.textBlockChangeInLongRange.dequeueLong());
                    }
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "TextBoxesLR");
        }

        Vector4f var14;
        Transform var16;
        if (!this.remoteHits.isEmpty() && !this.isOnServer()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "RemoteHits");

            while(!this.remoteHits.isEmpty()) {
                var14 = (Vector4f)this.remoteHits.dequeue();
                (var16 = new Transform()).setIdentity();
                var16.origin.set(var14.x, var14.y, var14.z);
                int var4 = (int)Math.abs(var14.w);
                HudIndicatorOverlay.toDrawTexts.add(new RaisingIndication(var16, String.valueOf(var4), 1.0F, 0.0F, 0.0F, 1.0F));
                this.getState();
                if (var14.w < 300.0F) {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit small explosion small enemy ship blow up", var16, 2.0F, 50.0F);
                } else if (var14.w < 600.0F) {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit medium explosion medium enemy ship blow up", var16, 2.0F, 100.0F);
                } else {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit large explosion big enemy ship blow up", var16, 2.0F, 150.0F);
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "RemoteHits");
        }

        if (!this.remoteShieldHits.isEmpty() && !this.isOnServer()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "RemoteShieldHits");

            while(!this.remoteShieldHits.isEmpty()) {
                var14 = (Vector4f)this.remoteShieldHits.dequeue();
                (var16 = new Transform()).setIdentity();
                var16.origin.set(var14.x, var14.y, var14.z);
                HudIndicatorOverlay.toDrawTexts.add(new RaisingIndication(var16, String.valueOf((int)var14.w), ShieldAddOn.shieldHitColor.x, ShieldAddOn.shieldHitColor.y, ShieldAddOn.shieldHitColor.z, ShieldAddOn.shieldHitColor.w));
                this.getState();
                if (var14.w < 300.0F) {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit small explosion small enemy ship blow up", var16, 2.0F, 50.0F);
                } else if (var14.w < 600.0F) {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit medium explosion medium enemy ship blow up", var16, 2.0F, 100.0F);
                } else {
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_spaceship enemy - hit large explosion big enemy ship blow up", var16, 2.0F, 150.0F);
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "RemoteShieldHits");
        }

        long var15;
        if (!this.getCooldownBlocks().isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "CooldownBlocks");
            var15 = System.currentTimeMillis();
            LongIterator var19 = this.getCooldownBlocks().values().iterator();

            while(var19.hasNext()) {
                long var5 = var19.nextLong();
                if (var15 - var5 > 20000L) {
                    var19.remove();
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "CooldownBlocks");
        }

        if (!this.receivedInterconnectedStructure.isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "Interconnected");
            synchronized(this.receivedInterconnectedStructure) {
                label234:
                while(true) {
                    while(true) {
                        MarkerBeam var6;
                        Sendable var20;
                        ActivationCollectionManager var23;
                        do {
                            InterconnectStructureRequest var18;
                            VoidUniqueSegmentPiece var21;
                            VoidUniqueSegmentPiece var24;
                            do {
                                do {
                                    do {
                                        do {
                                            if (this.receivedInterconnectedStructure.isEmpty()) {
                                                break label234;
                                            }

                                            var21 = (var18 = (InterconnectStructureRequest)this.receivedInterconnectedStructure.dequeue()).fromPiece;
                                            var24 = var18.toPiece;
                                            var21.setSegmentControllerFromUID(this.getState());
                                            var24.setSegmentControllerFromUID(this.getState());
                                        } while(var21.getSegmentController() == null);
                                    } while(var24.getSegmentController() == null);
                                } while(!(var21.getSegmentController() instanceof ManagedSegmentController));
                            } while(!(((ManagedSegmentController)var21.getSegmentController()).getManagerContainer() instanceof ActivationManagerInterface));

                            var23 = (ActivationCollectionManager)((ActivationManagerInterface)((ManagedSegmentController)var21.getSegmentController()).getManagerContainer()).getActivation().getCollectionManagersMap().get(var21.getAbsoluteIndex());
                            (var6 = new MarkerBeam(0)).marking = var24.getSegmentController().getUniqueIdentifier();
                            var6.markerLocation = var24.getAbsoluteIndex();
                            var6.realName = var24.getSegmentController().getRealName();
                            var20 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var18.playerId);
                        } while(var23 == null);

                        if (var23.getDestination() != null && var23.getDestination().equalsBeam(var6)) {
                            var23.setDestination((MarkerBeam)null);
                            if (var20 != null && var20 instanceof PlayerState) {
                                ((PlayerState)var20).sendServerMessagePlayerInfo(new Object[]{133});
                            }
                        } else {
                            var23.setDestination(var6);
                            if (var20 != null && var20 instanceof PlayerState) {
                                ((PlayerState)var20).sendServerMessagePlayerInfo(new Object[]{132});
                            }
                        }
                    }
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "Interconnected");
        }

        TextBlockPair var22;
        if (!this.receivedTextBlockRequests.isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "TextBoxRequest");
            synchronized(this.receivedTextBlockRequests) {
                for(; !this.receivedTextBlockRequests.isEmpty(); var22.provider.getNetworkObject().textBlockResponsesAndChangeRequests.add(new RemoteTextBlockPair(var22, this.isOnServer()))) {
                    var22 = (TextBlockPair)this.receivedTextBlockRequests.dequeue();

                    assert this.isOnServer();

                    assert var22.provider != null;

                    var22.text = (String)this.getTextMap().get(var22.block);
                    if (var22.text == null) {
                        var22.text = "[no data]";
                    }
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "TextBoxRequest");
        }

        if (!this.receivedTextBlocks.isEmpty()) {
            this.getState().getDebugTimer().start(this, "SendableSegmentController", "TextBoxReceived");
            synchronized(this.receivedTextBlocks) {
                while(!this.receivedTextBlocks.isEmpty()) {
                    var22 = (TextBlockPair)this.receivedTextBlocks.dequeue();
                    this.getTextMap().put(var22.block, var22.text);
                    if (!this.isOnServer()) {
                        synchronized(((ClientSegmentProvider)this.getSegmentProvider()).getSendableSegmentProvider().getRequestedTextBlocks()) {
                            ((ClientSegmentProvider)this.getSegmentProvider()).getSendableSegmentProvider().getRequestedTextBlocks().remove(var22.block);
                        }
                    } else {
                        this.getNetworkObject().textBlockChangeBuffer.add(new RemoteTextBlockPair(var22, this.isOnServer()));
                    }
                }
            }

            this.getState().getDebugTimer().end(this, "SendableSegmentController", "TextBoxReceived");
        }

        if (this.isOnServer() && this.isLastModifierChanged() && this.lastModifierId != 0) {
            try {
                PlayerState var17 = ((GameServerState)this.getState()).getPlayerFromStateId(this.lastModifierId);
                this.setLastModifier(var17.getUniqueIdentifier());
            } catch (Exception var7) {
            }

            this.setLastModifierChanged(false);
        }

        var15 = System.currentTimeMillis();
        this.getState().getDebugTimer().start(this, "SendableSegmentController", "BlockeffectMan");
        if (this.isOnServer()) {
            this.getBlockEffectManager().updateServer(var1);
        } else {
            this.getBlockEffectManager().updateClient(var1);
        }

        this.getState().getDebugTimer().end(this, "SendableSegmentController", "BlockeffectMan");
        this.getState().getDebugTimer().start(this, "SendableSegmentController", "DelayedMods");
        this.getBlockProcessor().handleDelayedMods();
        this.getState().getDebugTimer().end(this, "SendableSegmentController", "DelayedMods");
        this.getState().getDebugTimer().start(this, "SendableSegmentController", "DelayedModsBulk");
        this.getBlockProcessor().handleDelayedBuklkMods();
        this.getState().getDebugTimer().end(this, "SendableSegmentController", "DelayedModsBulk");
        this.getState().getDebugTimer().start(this, "SendableSegmentController", "ActivationsAndControl");
        this.handleActivationsAndControlMap(var1, var15);
        this.getState().getDebugTimer().end(this, "SendableSegmentController", "ActivationsAndControl");
        this.getState().getDebugTimer().end(this, "SendableSegmentController");
    }

    public void sendBlockActivation(long var1) {
        for(int var3 = 0; var3 < this.listeners.size(); ++var3) {
            if (((SendableSegmentProvider)this.listeners.get(var3)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var3)).getNetworkObject().blockActivationBuffer.add(var1);
            }
        }

    }

    public void sendBlockMod(RemoteSegmentPiece var1) {
        for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
            if (((SendableSegmentProvider)this.listeners.get(var2)).isSendTo()) {
                assert this.getState().isSynched();

                ((SendableSegmentProvider)this.listeners.get(var2)).getNetworkObject().modificationBuffer.add(var1);
            }
        }

    }

    public void sendBlockActiveChanged(int var1, int var2, int var3, boolean var4) {
        for(int var5 = 0; var5 < this.listeners.size(); ++var5) {
            if (((SendableSegmentProvider)this.listeners.get(var5)).isSendTo()) {
                RemoteShortBuffer var6;
                if (var4) {
                    var6 = ((SendableSegmentProvider)this.listeners.get(var5)).getNetworkObject().activeChangedTrueBuffer;
                } else {
                    var6 = ((SendableSegmentProvider)this.listeners.get(var5)).getNetworkObject().activeChangedFalseBuffer;
                }

                var6.addCoord((short)var1, (short)var2, (short)var3);
                int var7;
                if ((var7 = (((SendableSegmentProvider)this.listeners.get(var5)).getNetworkObject().activeChangedTrueBuffer.size() + ((SendableSegmentProvider)this.listeners.get(var5)).getNetworkObject().activeChangedFalseBuffer.size()) / 3) > (Integer)ServerConfig.MAX_LOGIC_ACTIVATIONS_AT_ONCE_PER_OBJECT_STOP.getCurrentState()) {
                    assert this.isOnServer();

                    if (System.currentTimeMillis() - this.logicCooldown > 10000L) {
                        ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{134, this.getRealName(), this.getSector(new Vector3i())}, 3);
                        this.logicCooldown = System.currentTimeMillis();
                    }
                } else if (var7 > (Integer)ServerConfig.MAX_LOGIC_ACTIVATIONS_AT_ONCE_PER_OBJECT_WARN.getCurrentState()) {
                    assert this.isOnServer();

                    if (System.currentTimeMillis() - this.warnActivations > 5000L) {
                        ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{135, this.getRealName(), this.getSector(new Vector3i())}, 3);
                        this.warnActivations = System.currentTimeMillis();
                    }
                }
            }
        }

    }

    public void sendBlockHpByte(int var1, int var2, int var3, short var4) {
        for(int var5 = 0; var5 < this.listeners.size(); ++var5) {
            if (((SendableSegmentProvider)this.listeners.get(var5)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var5)).getNetworkObject().killBuffer.addCoord((short)var1, (short)var2, (short)var3, var4);
            }
        }

    }

    public void sendBeamLatchOn(long var1, int var3, long var4) {
        for(int var6 = 0; var6 < this.listeners.size(); ++var6) {
            if (((SendableSegmentProvider)this.listeners.get(var6)).isSendTo()) {
                NetworkSegmentProvider var7;
                (var7 = ((SendableSegmentProvider)this.listeners.get(var6)).getNetworkObject()).beamLatchBuffer.add(var1);
                var7.beamLatchBuffer.add((long)var3);
                var7.beamLatchBuffer.add(var4);
            }
        }

    }

    public void sendExplosionGraphic(Vector3f var1) {
        for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
            if (((SendableSegmentProvider)this.listeners.get(var2)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var2)).getNetworkObject().explosions.add(new RemoteVector3f(this.isOnServer(), var1));
            }
        }

    }

    private void sendTextBlockServerUpdate(SegmentPiece var1, String var2) {
        for(int var3 = 0; var3 < this.listeners.size(); ++var3) {
            SendableSegmentProvider var4;
            if ((var4 = (SendableSegmentProvider)this.listeners.get(var3)).isSendTo()) {
                var4.clearChangedTextBoxLongRangeIndices();
                TextBlockPair var5;
                (var5 = new TextBlockPair()).block = var1.getTextBlockIndex();
                var5.text = var2;
                var5.provider = var4;
                var4.getNetworkObject().textBlockResponsesAndChangeRequests.add(new RemoteTextBlockPair(var5, this.isOnServer()));
            } else {
                var4.sendTextBoxCacheClearIfNotSentYet(var1.getTextBlockIndex());
            }
        }

    }

    public void sendBlockServerMessage(ServerMessage var1) {
        for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
            if (((SendableSegmentProvider)this.listeners.get(var2)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var2)).getNetworkObject().messagesToBlocks.add(new RemoteServerMessage(var1, this.isOnServer()));
            }
        }

    }

    public void sendBlockKill(SegmentPiece var1) {
        this.sendBlockHpByte(var1, (short)0);
    }

    public void sendBlockKill(long var1) {
        this.sendBlockHpByte(var1, (short)0);
    }

    public void sendBlockHpByte(long var1, short var3) {
        this.sendBlockHpByte(ElementCollection.getPosX(var1), ElementCollection.getPosY(var1), ElementCollection.getPosZ(var1), var3);
    }

    public void sendBlockHpByte(SegmentPiece var1, short var2) {
        this.sendBlockHpByte(var1.getSegment().pos.x + var1.x, var1.getSegment().pos.y + var1.y, var1.getSegment().pos.z + var1.z, var2);
    }

    public void sendBlockSalvage(SegmentPiece var1) {
        this.sendBlockSalvage(var1.getSegment().pos.x + var1.x, var1.getSegment().pos.y + var1.y, var1.getSegment().pos.z + var1.z);
    }

    public void sendBlockSalvage(int var1, int var2, int var3) {
        for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
            if (((SendableSegmentProvider)this.listeners.get(var4)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var4)).getNetworkObject().salvageBuffer.addCoord((short)var1, (short)var2, (short)var3);
            }
        }

    }

    public void forceAllRawFiles() {
        IOFileManager.writeAllOpenFiles(this.getSegmentProvider().getSegmentDataIO().getManager());
    }

    public LongArrayFIFOQueue getBlockActivationBuffer() {
        return this.blockActivationBuffer;
    }

    public BlockEffectManager getBlockEffectManager() {
        return this.blockEffectManager;
    }

    public void modivyGravity(Vector3f var1) {
        super.modivyGravity(var1);
        var1.scale(Math.max(0.0F, 1.0F - this.getBlockEffectManager().status.antiGravity));
    }

    public void initFromNetworkObject(NetworkObject var1) {
        super.initFromNetworkObject(var1);
        NetworkSegmentController var6 = (NetworkSegmentController)var1;
        if (!this.isOnServer()) {
            this.getMinPos().set(var6.minSize.getVector());
            this.getMaxPos().set(var6.maxSize.getVector());
            this.setScrap(var6.scrap.get());
            this.setFactionRights(var6.factionRigths.getByte());
            this.setVulnerable(var6.vulnerable.get());
            this.setMinable(var6.minable.get());
            ((SegmentBufferManager)this.getSegmentBuffer()).setExpectedNonEmptySegmentsFromLoad(this.getNetworkObject().expectedNonEmptySegmentsFromLoad.getInt());
            int var3;
            long var4;
            if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof PowerManagerInterface) {
                PowerAddOn var2 = ((PowerManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getPowerAddOn();

                for(var3 = 0; var3 < this.getNetworkObject().initialPower.getReceiveBuffer().size(); ++var3) {
                    var4 = this.getNetworkObject().initialPower.getReceiveBuffer().getLong(var3);
                    var2.setInitialPower((double)var4);
                }

                for(var3 = 0; var3 < this.getNetworkObject().initialBatteryPower.getReceiveBuffer().size(); ++var3) {
                    var4 = this.getNetworkObject().initialBatteryPower.getReceiveBuffer().getLong(var3);
                    var2.setInitialBatteryPower((double)var4);
                }
            }

            if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ShieldContainerInterface) {
                ShieldAddOn var7 = ((ShieldContainerInterface)((ManagedSegmentController)this).getManagerContainer()).getShieldAddOn();

                for(var3 = 0; var3 < this.getNetworkObject().initialShields.getReceiveBuffer().size(); ++var3) {
                    var4 = this.getNetworkObject().initialShields.getReceiveBuffer().getLong(var3);
                    var7.setInitialShields((double)var4);
                }
            }

            this.setCreatorId(var6.creatorId.getByte());
            this.railController.updateFromNetworkObject();
            this.currentOwnerLowerCase = (String)this.getNetworkObject().currentOwner.get();
            this.lastDockerPlayerServerLowerCase = (String)this.getNetworkObject().lastDockerPlayerServerLowerCase.get();
            this.setVirtualBlueprint(this.getNetworkObject().virtualBlueprint.getBoolean());
            this.dbId = var6.dbId.getLong();
        }

        this.lastAllowed = var6.lastAllowed.getLong();
        this.pullPermission = PullPermission.values()[var6.pullPermission.getByte()];
        this.setRealName((String)var6.realName.get());
        this.setUniqueIdentifier((String)this.getNetworkObject().uniqueIdentifier.get());
        this.getDockingController().updateFromNetworkObject(var6);
        this.getHpController().initFromNetwork(var6);
        this.getSlotAssignment().updateFromNetworkObject(var6);
        this.getConfigManager().initFromNetworkObject(this.getNetworkObject());
    }

    public String toNiceString() {
        return null;
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        super.updateFromNetworkObject(var1, var2);
        NetworkSegmentController var3 = (NetworkSegmentController)var1;
        this.getControlElementMap().handleReceived();
        this.getBlockEffectManager().updateFromNetworkObject(var3);
        this.getHpController().updateFromNetworkObject(var3);
        this.railController.updateFromNetworkObject();
        this.getSlotAssignment().updateFromNetworkObject(var3);
        this.getConfigManager().updateFromNetworkObject(this.getNetworkObject());
        int var4;
        int var10;
        if (!this.isOnServer()) {
            this.lastAllowed = var3.lastAllowed.getLong();
            this.pullPermission = PullPermission.values()[var3.pullPermission.getByte()];
            this.setVirtualBlueprint(this.getNetworkObject().virtualBlueprint.getBoolean());
            this.setScrap(this.getNetworkObject().scrap.get());
            this.setVulnerable(var3.vulnerable.get());
            this.setMinable(var3.minable.get());
            this.setFactionRights(var3.factionRigths.getByte());
            this.dbId = var3.dbId.getLong();
            this.currentOwnerLowerCase = (String)this.getNetworkObject().currentOwner.get();
            this.lastDockerPlayerServerLowerCase = (String)this.getNetworkObject().lastDockerPlayerServerLowerCase.get();
            this.coreTimerStarted = ((NetworkSegmentController)var1).coreDestructionStarted.get();
            this.coreTimerDuration = ((NetworkSegmentController)var1).coreDestructionDuration.get();

            for(var10 = 0; var10 < this.getNetworkObject().textBlockChangeBuffer.getReceiveBuffer().size(); ++var10) {
                synchronized(this.receivedTextBlocks) {
                    this.receivedTextBlocks.enqueue(((RemoteTextBlockPair)this.getNetworkObject().textBlockChangeBuffer.getReceiveBuffer().get(var10)).get());
                }
            }

            for(var10 = 0; var10 < this.getNetworkObject().pullPermissionAskAnswerBuffer.getReceiveBuffer().size(); ++var10) {
                this.askForPullClient = this.getNetworkObject().pullPermissionAskAnswerBuffer.getReceiveBuffer().getLong(var10);
            }

            if (!var3.minSize.equalsVector(this.getMinPos())) {
                var3.minSize.getVector(this.getMinPos());
                ((ClientSegmentProvider)this.getSegmentProvider()).flagDimChange();
            }

            if (!var3.maxSize.equalsVector(this.getMaxPos())) {
                var3.maxSize.getVector(this.getMaxPos());
                ((ClientSegmentProvider)this.getSegmentProvider()).flagDimChange();
            }

            long var5;
            if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof PowerManagerInterface) {
                PowerAddOn var11 = ((PowerManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getPowerAddOn();

                for(var4 = 0; var4 < this.getNetworkObject().initialPower.getReceiveBuffer().size(); ++var4) {
                    var5 = this.getNetworkObject().initialPower.getReceiveBuffer().getLong(var4);
                    var11.setInitialPower((double)var5);
                }

                for(var4 = 0; var4 < this.getNetworkObject().initialBatteryPower.getReceiveBuffer().size(); ++var4) {
                    var5 = this.getNetworkObject().initialBatteryPower.getReceiveBuffer().getLong(var4);
                    var11.setInitialBatteryPower((double)var5);
                }
            }

            if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ShieldContainerInterface) {
                ShieldAddOn var12 = ((ShieldContainerInterface)((ManagedSegmentController)this).getManagerContainer()).getShieldAddOn();

                for(var4 = 0; var4 < this.getNetworkObject().initialShields.getReceiveBuffer().size(); ++var4) {
                    var5 = this.getNetworkObject().initialShields.getReceiveBuffer().getLong(var4);
                    var12.setInitialShields((double)var5);
                }
            }

            RemoteVector4f var13;
            for(var10 = 0; var10 < this.getNetworkObject().shieldHits.getReceiveBuffer().size(); ++var10) {
                var13 = (RemoteVector4f)this.getNetworkObject().shieldHits.getReceiveBuffer().get(var10);
                if (((GameClientState)this.getState()).getWorldDrawer() != null) {
                    ShieldDrawer var14 = ((GameClientState)this.getState()).getWorldDrawer().getShieldDrawerManager().get(this);
                    Vector4f var6 = var13.getVector();
                    if (var14 != null) {
                        var14.addHitOld(new Vector3f(var6.x, var6.y, var6.z), var6.w);
                    }

                    this.remoteShieldHits.enqueue(var6);
                }
            }

            for(var10 = 0; var10 < this.getNetworkObject().hits.getReceiveBuffer().size(); ++var10) {
                var13 = (RemoteVector4f)this.getNetworkObject().hits.getReceiveBuffer().get(var10);
                this.remoteHits.enqueue(var13.getVector(new Vector4f()));
            }
        } else {
            for(var10 = 0; var10 < this.getNetworkObject().clientToServerCheckEmptyConnection.getReceiveBuffer().size(); ++var10) {
                long var16 = this.getNetworkObject().clientToServerCheckEmptyConnection.getReceiveBuffer().getLong(var10);
                this.getControlElementMap().checkControllerOnServer(var16);
            }

            for(var10 = 0; var10 < this.getNetworkObject().pullPermissionChangeBuffer.getReceiveBuffer().size(); ++var10) {
                byte var19 = this.getNetworkObject().pullPermissionChangeBuffer.getReceiveBuffer().getByte(var10);
                this.pullPermission = PullPermission.values()[var19];
            }

            for(var10 = 0; var10 < this.getNetworkObject().pullPermissionAskAnswerBuffer.getReceiveBuffer().size(); ++var10) {
                this.lastAllowed = this.getNetworkObject().pullPermissionAskAnswerBuffer.getReceiveBuffer().get(var10);
            }

            ObjectArrayList var17 = var3.currentOwnerChangeRequest.getReceiveBuffer();

            for(var4 = 0; var4 < var17.size(); ++var4) {
                String var15 = (String)((RemoteString)var17.get(var4)).get();
                this.ownerChangeRequested = var15;
                this.ownerChangeRequestedClientId = var2;
            }
        }

        String var20 = null;
        if (this.isOnServer() && !this.getRealName().equals(var3.realName.get())) {
            System.err.println("[SERVER] received name change from client " + var2 + ": " + this.getRealName() + " -> " + (String)var3.realName.get());

            try {
                LogUtil.log().fine("[RENAME] " + ((GameServerState)this.getState()).getPlayerFromStateId(var2).getName() + " changed object name: \"" + this.getRealName() + "\" to \"" + (String)var3.realName.get() + "\"");
            } catch (PlayerNotFountException var7) {
                var7.printStackTrace();
            }

            this.getNetworkObject().realName.setChanged(true);
            var20 = this.getRealName();
        }

        this.setRealName((String)var3.realName.get());
        if (var20 != null) {
            this.onRename(var20, this.getRealName());
        }

        this.getDockingController().updateFromNetworkObject(var3);
        if (!var3.structureInterconnectRequestBuffer.getReceiveBuffer().isEmpty()) {
            synchronized(this.receivedInterconnectedStructure) {
                for(int var18 = 0; var18 < this.getNetworkObject().structureInterconnectRequestBuffer.getReceiveBuffer().size(); ++var18) {
                    this.receivedInterconnectedStructure.enqueue(((RemoteInterconnectStructure)var3.structureInterconnectRequestBuffer.getReceiveBuffer().get(var18)).get());
                }

            }
        }
    }

    public void updateToFullNetworkObject() {
        super.updateToFullNetworkObject();

        assert this.getUniqueIdentifier() != null;

        this.getNetworkObject().factionRigths.set(this.getFactionRights());
        this.getNetworkObject().currentOwner.set(this.currentOwnerLowerCase);
        this.getNetworkObject().lastDockerPlayerServerLowerCase.set(this.lastDockerPlayerServerLowerCase);
        this.getNetworkObject().uniqueIdentifier.set(this.getUniqueIdentifier());
        this.getNetworkObject().scrap.set(this.isScrap());
        this.getNetworkObject().vulnerable.set(this.isVulnerable());
        this.getNetworkObject().minable.set(this.isMinable());
        this.getNetworkObject().creatorId.set((byte)this.getCreatorId());
        this.getNetworkObject().dbId.set(this.dbId);
        this.getNetworkObject().expectedNonEmptySegmentsFromLoad.set(((SegmentBufferManager)this.getSegmentBuffer()).getExpectedNonEmptySegmentsFromLoad());
        if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof PowerManagerInterface) {
            PowerAddOn var1 = ((PowerManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getPowerAddOn();
            this.getNetworkObject().initialPower.add((long)(var1.getInitialPower() + var1.getPowerSimple()));
            this.getNetworkObject().initialBatteryPower.add((long)(var1.getInitialBatteryPower() + var1.getBatteryPower()));
        }

        if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ShieldContainerInterface) {
            ShieldAddOn var2 = ((ShieldContainerInterface)((ManagedSegmentController)this).getManagerContainer()).getShieldAddOn();
            this.getNetworkObject().initialShields.add((long)(var2.getInitialShields() + var2.getShields()));
        }

        this.railController.updateToFullNetworkObject();
        this.getConfigManager().updateToFullNetworkObject(this.getNetworkObject());
        this.getNetworkObject().lastAllowed.set(this.lastAllowed);
        this.getNetworkObject().pullPermission.set((byte)this.pullPermission.ordinal());
        this.getSlotAssignment().sendAll();
        this.getHpController().updateToFullNetworkObject();
        this.getBlockEffectManager().updateToFullNetworkObject(this.getNetworkObject());
        this.updateToNetworkObject();
    }

    public void updateToNetworkObject() {
        super.updateToNetworkObject();

        assert this.getMinPos() != null;

        if (this.isOnServer()) {
            this.getNetworkObject().dbId.set(this.dbId);
            this.getNetworkObject().virtualBlueprint.set(this.isVirtualBlueprint());
            this.railController.updateToNetworkObject();
            this.getNetworkObject().factionRigths.set(this.getFactionRights());
            this.getNetworkObject().currentOwner.set(this.currentOwnerLowerCase);
            this.getNetworkObject().lastDockerPlayerServerLowerCase.set(this.lastDockerPlayerServerLowerCase);
            this.getNetworkObject().scrap.set(this.isScrap());
            this.getNetworkObject().vulnerable.set(this.isVulnerable());
            this.getNetworkObject().minable.set(this.isMinable());
            this.getNetworkObject().minSize.set(this.getMinPos());
            this.getNetworkObject().maxSize.set(this.getMaxPos());
            this.getNetworkObject().coreDestructionStarted.set(this.coreTimerStarted);
            this.getNetworkObject().coreDestructionDuration.set(this.coreTimerDuration);
            this.getNetworkObject().lastAllowed.set(this.lastAllowed);
            this.getNetworkObject().pullPermission.set((byte)this.pullPermission.ordinal());
            if (!this.getRealName().equals(this.getNetworkObject().realName.get())) {
                this.getNetworkObject().realName.set(this.getRealName());
            }
        }

        this.getConfigManager().updateToNetworkObject(this.getNetworkObject());
        this.getHpController().updateToNetworkObject();
    }

    public void handleNTDockChanged() {
        this.getDockingController().onDockChanged(this.getNetworkObject());
    }

    public void handleReceivedBlockActivations(NetworkSegmentProvider var1) {
        for(int var2 = 0; var2 < var1.blockActivationBuffer.getReceiveBuffer().size(); ++var2) {
            long var3 = var1.blockActivationBuffer.getReceiveBuffer().getLong(var2);
            synchronized(this.getBlockActivationBuffer()) {
                this.getBlockActivationBuffer().enqueue(var3);
            }
        }

    }

    private void handleReceivedControllers(NetworkSegmentController var1) {
        this.getControlElementMap().handleReceived();
    }

    private void handleReceivedHarvestConnections(NetworkSegmentController var1) {
    }

    protected void handleReceivedModifications(NetworkSegmentProvider var1) {
        boolean var10000 = this.isOnServer() || ((GameClientState)this.getState()).getCurrentSectorEntities().containsKey(this.getId());
        boolean var2 = false;
        if (var10000) {
            this.getBlockProcessor().receivedMods(var1);

            int var8;
            for(var8 = 0; var8 < var1.explosions.getReceiveBuffer().size(); var8 += 4) {
                Vector3f var4 = ((RemoteVector3f)var1.explosions.getReceiveBuffer().get(var8)).getVector();
                if (((GameClientState)this.getState()).getCurrentSectorId() == this.getSectorId() && ((GameClientState)this.getState()).getWorldDrawer() != null && ((GameClientState)this.getState()).getWorldDrawer().getExplosionDrawer() != null) {
                    ((GameClientState)this.getState()).getWorldDrawer().getExplosionDrawer().addExplosion(var4, 15.0F);
                    Transform var5;
                    (var5 = new Transform()).setIdentity();
                    var5.origin.set(var4);
                    ((ClientState)this.getState()).getController().queueTransformableAudio("0022_explosion_one", var5, 2.0F, 100.0F);
                }
            }

            for(var8 = 0; var8 < var1.beamLatchBuffer.getReceiveBuffer().size(); var8 += 3) {
                if (((GameClientState)this.getState()).getCurrentSectorId() == this.getSectorId()) {
                    long var9 = var1.beamLatchBuffer.getReceiveBuffer().getLong(var8);
                    int var3 = (int)var1.beamLatchBuffer.getReceiveBuffer().getLong(var8 + 1);
                    long var6 = var1.beamLatchBuffer.getReceiveBuffer().getLong(var8 + 2);
                    this.addReceivedBeamLatch(var9, var3, var6);
                }
            }
        }

    }

    protected void addReceivedBeamLatch(long var1, int var3, long var4) {
    }

    public boolean isVolatile() {
        return false;
    }

    public void newNetworkObject() {
    }

    public void onCollision(ManifoldPoint var1, Sendable var2) {
    }

    public void setServerSendableSegmentController(SendableSegmentProvider var1) {
        this.serverSendableSegmentProvider = var1;
    }

    public void onBlockAddedHandled() {
    }

    public void sendBlockBulkMod(RemoteBlockBulk var1) {
        for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
            if (((SendableSegmentProvider)this.listeners.get(var2)).isSendTo()) {
                ((SendableSegmentProvider)this.listeners.get(var2)).getNetworkObject().modificationBulkBuffer.add(var1);
            }
        }

    }

    private void handleActivationsAndControlMap(Timer var1, long var2) {
        if (this.isOnServer() && var1.currentTime - this.logicCooldown < 10000L) {
            this.delayActivationBuffer.clear();
            this.getBlockActivationBuffer().clear();
            this.delayActivationBufferNonRepeating.clear();
        }

        if (this.isOnServer()) {
            this.handleActivationsServer(var1);
        }

        ObjectIterator var4;
        Entry var5;
        if (!this.delayActivationBuffer.isEmpty()) {
            var4 = this.delayActivationBuffer.entrySet().iterator();

            while(var4.hasNext()) {
                var5 = (Entry)var4.next();
                SegmentPiece var6;
                if (var2 > ((SendableSegmentController.DelayedAct)var5.getValue()).time + 500L && (var6 = this.getSegmentBuffer().getPointUnsave((Long)var5.getKey())) != null) {
                    if (ElementKeyMap.isButton(var6.getType())) {
                        long var7 = ElementCollection.getDeactivation(var6.getAbsoluteIndex(), true, false);
                        this.getBlockActivationBuffer().enqueue(var7);
                    } else {
                        synchronized(this.getBlockActivationBuffer()) {
                            this.getBlockActivationBuffer().enqueue(((SendableSegmentController.DelayedAct)var5.getValue()).encode);
                        }
                    }

                    var4.remove();
                }
            }
        }

        if (!this.delayActivationBufferNonRepeating.isEmpty()) {
            var4 = this.delayActivationBufferNonRepeating.entrySet().iterator();

            while(var4.hasNext()) {
                var5 = (Entry)var4.next();
                if (var2 > ((SendableSegmentController.DelayedAct)var5.getValue()).time + 500L) {
                    synchronized(this.getBlockActivationBuffer()) {
                        this.getBlockActivationBuffer().enqueue(((SendableSegmentController.DelayedAct)var5.getValue()).encode);
                    }

                    var4.remove();
                }
            }
        }

        long var11 = System.currentTimeMillis();
        this.getControlElementMap().updateLocal(var1);
        long var12;
        if ((var12 = System.currentTimeMillis() - var11) > 20L) {
            System.err.println("[SENSEGMENTCONTROLLER][" + this.getState() + "] WARNING: getControlElementMap().updateLocal(timer) of " + this + " took " + var12 + " ms");
        }

    }

    public void onBlockKill(SegmentPiece var1, Damager var2) {
        if (var1.getType() == 0) {
            System.err.println(this.getState() + " WARNING: Killed an air block (should not happen) " + var1);
        } else if (this.isOnServer()) {
            this.railController.getRoot().onDamageServerRootObject((float)var1.getInfo().getMaxHitPointsFull(), var2);
        }
        //INSERTED CODE
        BlockKillEvent event = new BlockKillEvent(var1, this, var2);
        StarLoader.fireEvent(BlockKillEvent.class, event);
        if(event.isCanceled()){
            return;
        }
        ///

        this.blockProcessor.onBlockChanged((RemoteSegment)var1.getSegment());
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().onBlockKill(var1.getAbsoluteIndex(), var1.getType(), var2);
        }

    }

    private ReentrantReadWriteLock handleActivationServer(Timer var1, SignalTrace var2, ReentrantReadWriteLock var3, long var4) {
        this.currentTrace = var2;
        long var6 = var2.a;
        SegmentPiece var8 = this.getSegmentBuffer().getPointUnsave(ElementCollection.getPosX(var6), ElementCollection.getPosY(var6), ElementCollection.getPosZ(var6));
        long var9 = ElementCollection.getPosIndexFrom4(var6);
        if (var8 == null) {
            ++this.failedActivating;
            this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var4 + 1000L));
            return var3;
        } else if (!ElementKeyMap.isValidType(var8.getType())) {
            System.err.println("[EXCEPTION][SERVER] tried to handle activation of nonexitent block: " + var8.getType());
            return var3;
        } else {
            long var11 = (long)ElementCollection.getType(var6);
            boolean var13 = false;
            boolean var14 = false;
            if (var11 > 100L) {
                var11 -= 100L;
                var14 = true;
                var6 = ElementCollection.getIndex4(var9, (short)((int)var11));
            }

            if (var11 > 10L) {
                var11 -= 10L;
                var13 = true;
            }

            if (var14 && var2.parent == null && var8.getType() == 668) {
                var2.rootPos = var9;
            }

            var14 = var11 == 3L || var11 == 4L;
            boolean var15 = true;
            if (ElementKeyMap.isButton(var8.getType())) {
                long var17 = ElementCollection.getIndex4(var9, (short)((int)var11));
                if (!var8.isActive() && !this.delayActivationBuffer.containsKey(var17)) {
                    this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var17, var4));
                }
            }

            if (var8.getType() == 683) {
                if (var14) {
                    ((RaceManagerState)this.getState()).getRaceManager().onActivateRaceController(var8);
                }

                System.err.println("[SERVER] ACTIVATED RACE " + var14);
            }

            if (var8.getType() == 980) {
                SensorCollectionManager var30 = (SensorCollectionManager)((ManagedSegmentController)this).getManagerContainer().getSensor().getCollectionManagersMap().get(var8.getAbsoluteIndex());
                if (var14 && var30 != null) {
                    var30.check();
                }

                return var3;
            } else if (var8.getType() == 121) {
                if (this instanceof SegmentControllerAIInterface) {
                    ((SegmentControllerAIInterface)this).activateAI(var14, true);
                }

                return var3;
            } else {
                ActivationCollectionManager var18;
                if (var8.getType() == 668 && this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ActivationManagerInterface) {
                    if ((var18 = (ActivationCollectionManager)((ActivationManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getActivation().getCollectionManagersMap().get(var9)) != null) {
                        if (var18.getDestination() != null) {
                            Sendable var16;
                            if ((var16 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getUidObjectMap().get(var18.getDestination().marking)) instanceof SegmentController) {
                                SendableSegmentController var19;
                                SegmentPiece var20;
                                if ((var20 = (var19 = (SendableSegmentController)var16).getSegmentBuffer().getPointUnsave(var18.getDestination().markerLocation)) == null) {
                                    System.err.println("[SERVER][ACTIVATION][WARNING] wireless logic signal delayed. Recipient block not loaded yet: " + this + " -> " + var16);
                                    ++this.failedActivating;
                                    this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var4 + 1000L));
                                    return var3;
                                }

                                if (var20.getType() == 668) {
                                    long var21;
                                    if (var14) {
                                        var21 = ElementCollection.getActivationWireless(var20.getAbsoluteIndex(), true, false);
                                    } else {
                                        var21 = ElementCollection.getDeactivationWireless(var20.getAbsoluteIndex(), true, false);
                                    }

                                    System.err.println("[SERVER][ACTIVATION] sent '" + var14 + "' signal from " + this + " -> " + var18.getDestination().marking + "; " + ElementCollection.getPosFromIndex(var18.getDestination().markerLocation, new Vector3i()));
                                    if (var20.isActive() != !var8.isActive()) {
                                        if (var8.getAbsoluteIndex() != var2.rootPos) {
                                            var19.getBlockActivationBuffer().enqueue(var21);
                                        } else {
                                            var19.delayActivationBuffer.put(var20.getAbsoluteIndex(), new SendableSegmentController.DelayedAct(var21, var4 + 500L));
                                        }
                                    }
                                }
                            } else {
                                System.err.println("[SERVER][ACTIVATION][WARNING] " + this + " Destination " + var18.getDestination().marking + " not found for wireless block collectionManager at " + var9);
                            }
                        } else {
                            System.err.println("[SERVER][ACTIVATION][WARNING] " + this + " Wireless block collectionManager at " + var9 + " has no destination");
                        }
                    } else {
                        System.err.println("[SERVER][ACTIVATION][ERROR] " + this + " Wireless block collectionManager not found at " + var9);
                    }
                }

                boolean var29;
                if (var13) {
                    var6 = ElementCollection.getIndex4(var9, (short)((int)var11));

                    assert var11 == (long)ElementCollection.getType(var6);

                    if (var8.getType() == 407) {
                        if (!this.delayActivationBuffer.containsKey(var6)) {
                            this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var4));
                        }

                        return var3;
                    }

                    if (var8.getType() == 406) {
                        if (this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ActivationManagerInterface && (var18 = (ActivationCollectionManager)((ActivationManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getActivation().getCollectionManagersMap().get(var9)) != null && this.signalId > var18.currentSignal) {
                            var18.currentSignal = this.signalId;
                            if (!this.delayActivationBufferNonRepeating.containsKey(var6)) {
                                this.delayActivationBufferNonRepeating.put(var9, new SendableSegmentController.DelayedAct(var6, var4));
                            }
                        }

                        return var3;
                    }

                    if (!ElementKeyMap.isValidType(var8.getType()) || !ElementKeyMap.getInfo(var8.getType()).isSignal()) {
                        var15 = false;
                    }
                } else {
                    var29 = true;
                    ActivationCollectionManager var27;
                    if (var8.getType() == 406 && this instanceof ManagedSegmentController && ((ManagedSegmentController)this).getManagerContainer() instanceof ActivationManagerInterface && (var27 = (ActivationCollectionManager)((ActivationManagerInterface)((ManagedSegmentController)this).getManagerContainer()).getActivation().getCollectionManagersMap().get(var9)) != null) {
                        this.signalId = var27.currentSignal;
                        var29 = false;
                    }

                    if (var29) {
                        ++this.signalId;
                    }
                }

                var29 = var15 && ElementCollection.isDeligateFromActivationIndex(var6);

                assert var11 == (long)ElementCollection.getType(var6);

                if (var29 && this.currentTrace.checkLoop()) {
                    return var3;
                } else {
                    boolean var31 = ElementCollection.isActiveFromActivationIndex(var6);

                    assert this.isOnServer();

                    if (var8.getType() == 56 && var8.getSegment().getSegmentController() instanceof Planet) {
                        return var3;
                    } else if (var8.getSegment().getSegmentData() == null) {
                        throw new NullPointerException(var8.getSegment() + " was empty (segment data null) but it's an activation " + var8);
                    } else {
                        if (var3 == null || var3 != var8.getSegment().getSegmentData().rwl) {
                            if (var3 != null) {
                                var3.writeLock().unlock();
                            }

                            var8.getSegment().getSegmentData().rwl.writeLock().lock();
                            var3 = var8.getSegment().getSegmentData().rwl;
                        }

                        boolean var28 = var8.isActive();
                        SendableSegmentController.ReplacementContainer var32 = null;
                        SendableSegmentController.ReplacementContainer var33 = null;
                        if (var31 != var28 || var8.getType() == 410 && var31 == var28) {
                            boolean var34 = var31;
                            if (var8.getType() == 410) {
                                var34 = !var31;
                            }

                            var32 = this.getReplacementConnected(var2, var8, var34, var4, this.railController);
                            var33 = this.getReplacementConnected(var2, var8, var34, var4, this.displayReplace);
                        }

                        if (var31 != var28 && (var32 == null || var33 == null)) {
                            ++this.failedActivating;
                            this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var4 + 1000L));
                            return var3;
                        } else {
                            if (var29 && this instanceof ManagedSegmentController) {
                                if (!((ManagedSegmentController)this).getManagerContainer().handleBlockActivate(var8, var28, var31)) {
                                    ++this.failedActivating;
                                    this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var4 + 3000L));
                                    return var3;
                                }

                                if (!ElementKeyMap.getInfo(var8.getType()).isSignal() && !ElementKeyMap.getInfo(var8.getType()).isRailTrack()) {
                                    var8.setActive(var31);
                                }
                            } else if (ElementKeyMap.isValidType(var8.getType()) && !ElementKeyMap.getInfo(var8.getType()).isRailTrack()) {
                                var8.setActive(var31);
                            }

                            if (var31 && var8.getType() == 663 && this.railController.isDockedAndExecuted()) {
                                this.railController.disconnect();
                                return var3;
                            } else {
                                assert var8.getType() != 0;

                                int var35;
                                try {
                                    var35 = var8.getSegment().getSegmentData().applySegmentData(var8, var4);
                                } catch (SegmentDataWriteException var25) {
                                    try {
                                        SegmentDataWriteException.replaceData(var8.getSegment());
                                        var35 = var8.getSegment().getSegmentData().applySegmentData(var8, var4);
                                    } catch (SegmentDataWriteException var24) {
                                        throw new RuntimeException(var24);
                                    }
                                }

                                long var22;
                                if (this instanceof ManagedSegmentController && var8.getInfo().isSignal()) {
                                    var22 = -9223372036854775808L;
                                    if (var2.parent != null) {
                                        var22 = var2.parent.pos;
                                    }

                                    if (!((ManagedSegmentController)this).getManagerContainer().handleActivateBlockActivate(var8, var22, var28, var1)) {
                                        this.delayActivationBuffer.put(var9, new SendableSegmentController.DelayedAct(var6, var1.currentTime + 3000L));
                                    }
                                }

                                ((RemoteSegment)var8.getSegment()).setLastChanged(var4);
                                if (var35 != 3) {
                                    var8.refresh();

                                    assert var28 != var8.isActive() : var8;

                                    var22 = -9223372036854775808L;
                                    if (var2.parent != null) {
                                        var22 = var2.parent.pos;
                                    }

                                    this.queueBlockChangeReaction(var28, var31, var8, var9, var6, var22);
                                    if (ElementKeyMap.isValidType(var8.getType()) && ElementKeyMap.getInfo(var8.getType()).isSignal()) {
                                        if (var32 != null) {
                                            boolean var26 = this.replaceConnected(var32, var2, var8, var31, var4, this.railController);
                                            if (var31 && !var26) {
                                                this.railController.disconnectFromRailRailIfContact(var8, var4);
                                            }
                                        }

                                        if (var33 != null) {
                                            this.replaceConnected(var33, var2, var8, var31, var4, this.displayReplace);
                                        }
                                    }
                                }

                                return var3;
                            }
                        }
                    }
                }
            }
        }
    }

    private void executeBlockActiveReaction(Timer var1, SendableSegmentController.BlockActiveReaction var2) {
        if (this instanceof ManagedSegmentController) {
            if (var2.active && !this.isVirtualBlueprint() && var2.block.getType() == 14 && ((ManagedSegmentController)this).getManagerContainer() instanceof ExplosiveManagerContainerInterface) {
                ExplosiveManagerContainerInterface var3 = (ExplosiveManagerContainerInterface)((ManagedSegmentController)this).getManagerContainer();
                Vector3i var4 = var2.block.getAbsolutePos(new Vector3i());
                var3.getExplosiveElementManager().addExplosion(var4);
            }

            TransporterCollectionManager var6;
            if (var2.active && !this.isVirtualBlueprint() && var2.block.getType() == 687 && (var6 = (TransporterCollectionManager)((TransporterModuleInterface)((ManagedSegmentController)this).getManagerContainer()).getTransporter().getCollectionManagersMap().get(var2.block.getAbsoluteIndex())) != null && var6.canUse()) {
                var6.sendTransporterUsage();
            }

            if (!var2.block.getInfo().isSignal() && !((ManagedSegmentController)this).getManagerContainer().handleActivateBlockActivate(var2.block, var2.fromActivation, var2.oldActive, var1)) {
                this.delayActivationBuffer.put(var2.posIndex, new SendableSegmentController.DelayedAct(var2.a, var1.currentTime + 3000L));
            }
        }

        long var5 = var2.block.getAbsoluteIndex();
        this.sendBlockActiveChanged(ElementCollection.getPosX(var5), ElementCollection.getPosY(var5), ElementCollection.getPosZ(var5), var2.block.isActive());
        ((RemoteSegment)var2.block.getSegment()).setLastChanged(var1.currentTime);
    }

    private static void freeActReaction(SendableSegmentController.BlockActiveReaction var0) {
        var0.clear();
        blockActPool.add(var0);
    }

    private static SendableSegmentController.BlockActiveReaction getActReaction() {
        return blockActPool.size() > 0 ? (SendableSegmentController.BlockActiveReaction)blockActPool.remove(blockActPool.size() - 1) : new SendableSegmentController.BlockActiveReaction();
    }

    private void queueBlockChangeReaction(boolean var1, boolean var2, SegmentPiece var3, long var4, long var6, long var8) {
        SendableSegmentController.BlockActiveReaction var10;
        (var10 = getActReaction()).oldActive = var1;
        var10.active = var2;
        var10.block = var3;
        var10.posIndex = var4;
        var10.a = var6;
        var10.fromActivation = var8;
        SendableSegmentController.BlockActiveReaction var11;
        if ((var11 = (SendableSegmentController.BlockActiveReaction)blockActivations.put(var3.getAbsoluteIndex(), var10)) != null) {
            var10.counter = var11.counter + 1;
            freeActReaction(var11);
        } else {
            ++var10.counter;
        }
    }

    private void handleBlockActivationsServerAfter(Timer var1) {
        Iterator var2 = blockActivations.values().iterator();

        while(var2.hasNext()) {
            SendableSegmentController.BlockActiveReaction var3 = (SendableSegmentController.BlockActiveReaction)var2.next();
            this.executeBlockActiveReaction(var1, var3);
            freeActReaction(var3);
        }

        blockActivations.clear();
    }

    private SendableSegmentController.ReplacementContainer getReplacementConnected(SignalTrace var1, SegmentPiece var2, boolean var3, long var4, BlockLogicReplaceInterface var6) {
        SendableSegmentController.ReplacementContainer var10 = new SendableSegmentController.ReplacementContainer();
        Vector3i var15 = var2.getAbsolutePos(this.tmpPos);
        FastCopyLongOpenHashSet var11;
        if (var3 && (var11 = (FastCopyLongOpenHashSet)this.getControlElementMap().getControllingMap().getAll().get(var2.getAbsoluteIndex())) != null) {
            for(int var13 = 0; var13 < 6; ++var13) {
                Vector3i var5 = Element.DIRECTIONSi[var13];
                SegmentPiece var7 = this.getSegmentBuffer().getPointUnsave(var15.x + var5.x, var15.y + var5.y, var15.z + var5.z);
                var10.fromBlockSurround = var7;
                if (var7 == null) {
                    System.err.println("FROM SURROUND NULL " + (var15.x + var5.x) + ";" + (var15.y + var5.y) + "; " + (var15.z + var5.z) + "; " + var6.getClass());
                    return null;
                }

                if (var6.fromBlockOk(var7)) {
                    Iterator var12 = var11.iterator();

                    while(var12.hasNext()) {
                        long var8 = (Long)var12.next();
                        SegmentPiece var14;
                        if ((var14 = this.getSegmentBuffer().getPointUnsave(var8)) == null) {
                            System.err.println("FROM TO REPLACE NULL " + (var15.x + var5.x) + ";" + (var15.y + var5.y) + "; " + (var15.z + var5.z) + "; " + var6.getClass());
                            return null;
                        }

                        var10.toReplaceList.add(var14);
                    }

                    var10.ok = true;
                    return var10;
                }
            }
        }

        return var10;
    }

    private boolean replaceConnected(SendableSegmentController.ReplacementContainer var1, SignalTrace var2, SegmentPiece var3, boolean var4, long var5, BlockLogicReplaceInterface var7) {
        if (!var1.ok) {
            return false;
        } else {
            SegmentPiece var21;
            short var22 = (var21 = var1.fromBlockSurround).getType();
            Iterator var20 = var1.toReplaceList.iterator();

            while(true) {
                label67:
                while(true) {
                    short var8;
                    long var12;
                    SegmentPiece var23;
                    do {
                        if (!var20.hasNext()) {
                            return true;
                        }

                        var8 = (var23 = (SegmentPiece)var20.next()).getType();
                        var12 = var23.getAbsoluteIndexWithType4();
                    } while(!var7.isBlockNextToLogicOkTuUse(var21, var23));

                    if (!var7.equalsBlockData(var21, var23)) {
                        Segment var9 = var23.getSegment();
                        var21.getType();
                        var23.getType();
                        SegmentPiece var10 = new SegmentPiece(var21);
                        var7.modifyReplacement(var21, var23);

                        try {
                            var9.getSegmentData().applySegmentData(var23.x, var23.y, var23.z, var21.getData(), 0, false, var23.getAbsoluteIndex(), false, false, var5, true);
                        } catch (SegmentDataWriteException var19) {
                            try {
                                var9.setSegmentData(SegmentDataWriteException.replaceData(var9));
                                var9.getSegmentData().applySegmentData(var23.x, var23.y, var23.z, var21.getData(), 0, false, var23.getAbsoluteIndex(), false, false, var5);
                            } catch (SegmentDataWriteException var18) {
                                throw new RuntimeException(var18);
                            }
                        }

                        ((RemoteSegment)var23.getSegment()).setLastChanged(System.currentTimeMillis());
                        var23.refresh();
                        var7.afterReplaceBlock(var10, var23);

                        assert var21.getType() == this.getSegmentBuffer().getPointUnsave(var21.getAbsoluteIndex()).getType();

                        assert var21.getOrientation() == this.getSegmentBuffer().getPointUnsave(var21.getAbsoluteIndex()).getOrientation();

                        RemoteSegmentPiece var24 = new RemoteSegmentPiece(var23, this.getNetworkObject());
                        this.sendBlockMod(var24);
                        Iterator var25 = this.getControlElementMap().getControllingMap().getAll().long2ObjectEntrySet().iterator();

                        while(true) {
                            SegmentPiece var11;
                            it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry var27;
                            do {
                                do {
                                    if (!var25.hasNext()) {
                                        FastCopyLongOpenHashSet var26;
                                        if ((var26 = (FastCopyLongOpenHashSet)this.getControlElementMap().getControllingMap().getAll().get(var23.getAbsoluteIndex())) != null) {
                                            Iterator var28 = (new LongArrayList(var26)).iterator();

                                            while(var28.hasNext()) {
                                                long var14;
                                                long var16 = ElementCollection.getPosIndexFrom4(var14 = (Long)var28.next());
                                                var8 = (short)ElementCollection.getType(var14);
                                                this.getControlElementMap().removeControllerForElement(var23.getAbsoluteIndex(), var16, var8);
                                                this.getControlElementMap().addControllerForElement(var23.getAbsoluteIndex(), var16, var8);
                                            }
                                        }

                                        ((RemoteSegment)var23.getSegment()).setLastChanged(var5);
                                        continue label67;
                                    }
                                } while(!((FastCopyLongOpenHashSet)(var27 = (it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry)var25.next()).getValue()).contains(var12));

                                this.getControlElementMap().removeControllerForElement(var27.getLongKey(), var23.getAbsoluteIndex(), var8);
                            } while((var11 = this.getSegmentBuffer().getPointUnsave(var27.getLongKey())) != null && !ElementInformation.canBeControlled(var11.getType(), var22));

                            if (var11 == null) {
                                System.err.println("[SERVER] WARNING: block replacement: connection update couldn't load connection starting point to replaced block (To replaced: " + var23.getAbsolutePos(new Vector3i()) + "). From (which failed to load): " + ElementCollection.getPosFromIndex(var27.getLongKey(), new Vector3i()));
                            }

                            this.getControlElementMap().addControllerForElement(var27.getLongKey(), var23.getAbsoluteIndex(), var22);
                        }
                    } else {
                        var23.refresh();
                        var7.afterReplaceBlock(var21, var23);
                    }
                }
            }
        }
    }

    public SendableSegmentController.SignalTracePool getSignalPool() {
        return (SendableSegmentController.SignalTracePool)signalThread.get();
    }

    private void handleActivationsServer(Timer var1) {
        this.failedActivating = 0;
        if (!this.getBlockActivationBuffer().isEmpty()) {
            SendableSegmentController.SignalTracePool var2 = this.getSignalPool();
            long var3 = this.getState().getUpdateTime();

            assert this.isOnServer();

            while(!this.getBlockActivationBuffer().isEmpty()) {
                long var5 = this.getBlockActivationBuffer().dequeue();
                SignalTrace var7;
                (var7 = var2.get()).set(ElementCollection.getPosIndexFrom4(var5), var5, (SignalTrace)null);
                this.signalQueue.enqueue(var7);
                if (this.signalQueue.size() > (Integer)ServerConfig.MAX_LOGIC_SIGNAL_QUEUE_PER_OBJECT.getCurrentState()) {
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{140, this.getRealName(), this.getSector(new Vector3i())}, 3);

                    while(!this.signalQueue.isEmpty()) {
                        var2.free((SignalTrace)this.signalQueue.dequeue());
                    }

                    return;
                }
            }

            ReentrantReadWriteLock var8 = null;

            while(!this.signalQueue.isEmpty()) {
                SignalTrace var6 = (SignalTrace)this.signalQueue.dequeue();
                var8 = this.handleActivationServer(var1, var6, var8, var3);
                var2.markFree(var6);
            }

            var2.freeMarked();
            if (var8 != null) {
                var8.writeLock().unlock();
            }

            if (this.getBlockActivationBuffer().size() > 0) {
                this.handleActivationsServer(var1);
            }
        }

        this.handleBlockActivationsServerAfter(var1);
        if (this.failedActivating > 0) {
            System.err.println("[SERVER] " + this + " Failed Activating " + this.failedActivating + " Blocks ");
        }

    }

    public void activateSwitchSingleServer(long var1) {
        SegmentPiece var3;
        if ((var3 = this.getSegmentBuffer().getPointUnsave(var1)) != null) {
            this.activateSingleServer(!var3.isActive(), var1);
        }

    }

    public void activateSingleServer(boolean var1, long var2) {
        assert this.isOnServer();

        long var4;
        if (var1) {
            var4 = ElementCollection.getActivation(var2, true, false);
        } else {
            var4 = ElementCollection.getDeactivation(var2, true, false);
        }

        this.getBlockActivationBuffer().enqueue(var4);
    }

    public void activateSurroundServer(boolean var1, Vector3i var2, short... var3) {
        assert this.isOnServer();

        for(int var4 = 0; var4 < 6; ++var4) {
            Vector3i var5;
            (var5 = new Vector3i(var2)).add(Element.DIRECTIONSi[var4]);
            SegmentPiece var9;
            if ((var9 = this.getSegmentBuffer().getPointUnsave(var5)) != null) {
                for(int var6 = 0; var6 < var3.length; ++var6) {
                    if (var9.getType() == var3[var6]) {
                        long var7;
                        if (var1) {
                            var7 = ElementCollection.getActivation(var9.getAbsoluteIndex(), true, false);
                        } else {
                            var7 = ElementCollection.getDeactivation(var9.getAbsoluteIndex(), true, false);
                        }

                        this.getBlockActivationBuffer().enqueue(var7);
                    }
                }
            }
        }

    }

    public boolean acivateConnectedSignalsServer(boolean var1, long var2) {
        assert this.isOnServer();

        Short2ObjectOpenHashMap var12 = this.getControlElementMap().getControllingMap().get(var2);
        boolean var3 = false;
        if (var12 != null) {
            short[] var4;
            int var5 = (var4 = ElementKeyMap.signalArray).length;

            for(int var6 = 0; var6 < var5; ++var6) {
                short var7 = var4[var6];
                FastCopyLongOpenHashSet var13;
                if ((var13 = (FastCopyLongOpenHashSet)var12.get(var7)) != null) {
                    for(Iterator var14 = var13.iterator(); var14.hasNext(); var3 = true) {
                        long var8 = (Long)var14.next();
                        long var10;
                        if (var1) {
                            var10 = ElementCollection.getActivation(ElementCollection.getPosIndexFrom4(var8), true, false);
                        } else {
                            var10 = ElementCollection.getDeactivation(ElementCollection.getPosIndexFrom4(var8), true, false);
                        }

                        this.getBlockActivationBuffer().enqueue(var10);
                    }
                }
            }
        }

        return var3;
    }

    public Long2ObjectOpenHashMap<SendableSegmentController.DelayedAct> getDelayActivationBuffer() {
        return this.delayActivationBuffer;
    }

    public Long2ObjectOpenHashMap<SendableSegmentController.DelayedAct> getDelayActivationBufferNonRepeating() {
        return this.delayActivationBufferNonRepeating;
    }

    public void fromActivationStateTag(Tag var1) {
        boolean var2 = false;

        assert "a".equals(var1.getName()) : var1.getName();

        Tag[] var12;
        Tag[] var3 = (Tag[])(var12 = (Tag[])var1.getValue())[0].getValue();

        for(int var4 = 0; var4 < var3.length - 1; ++var4) {
            long var5 = (Long)var3[var4].getValue();
            if (this.isLoadedFromChunk16()) {
                var5 = ElementCollection.shiftIndex4(var5, 8, 8, 8);
            }

            this.blockActivationBuffer.enqueue(var5);
            var2 = true;
        }

        Tag[] var13 = (Tag[])var12[1].getValue();

        for(int var14 = 0; var14 < var13.length - 1; ++var14) {
            Tag[] var6;
            long var7 = (Long)(var6 = (Tag[])var13[var14].getValue())[0].getValue();
            long var9 = (Long)var6[1].getValue();
            if (this.isLoadedFromChunk16()) {
                var7 = ElementCollection.shiftIndex4(var7, 8, 8, 8);
            }

            this.delayActivationBuffer.put(ElementCollection.getPosIndexFrom4(var7), new SendableSegmentController.DelayedAct(var7, var9));
            var2 = true;
        }

        Tag[] var16 = (Tag[])var12[2].getValue();

        for(int var15 = 0; var15 < var16.length - 1; ++var15) {
            Tag[] var17;
            long var8 = (Long)(var17 = (Tag[])var16[var15].getValue())[0].getValue();
            long var10 = (Long)var17[1].getValue();
            if (this.isLoadedFromChunk16()) {
                var8 = ElementCollection.shiftIndex4(var8, 8, 8, 8);
            }

            this.delayActivationBufferNonRepeating.put(ElementCollection.getPosIndexFrom4(var8), new SendableSegmentController.DelayedAct(var8, var10));
            var2 = true;
        }

        if (var2) {
            System.err.println("[SERVER] " + this + " loaded activation state: ActBuffer " + this.blockActivationBuffer.size() + "; Delay " + this.getDelayActivationBuffer().size() + "; DelayNR " + this.getDelayActivationBufferNonRepeating().size());
        }

    }

    public Tag getActivationStateTag() {
        synchronized(this.blockActivationBuffer) {
            LongArrayList var2 = new LongArrayList();

            while(!this.blockActivationBuffer.isEmpty()) {
                var2.add(this.blockActivationBuffer.dequeue());
            }

            for(int var3 = 0; var3 < var2.size(); ++var3) {
                this.blockActivationBuffer.enqueue(var2.get(var3));
            }

            Tag var7 = Tag.listToTagStruct(var2, Type.LONG, (String)null);
            Tag var6 = listToTagStruct(this.getDelayActivationBuffer(), (String)null);
            Tag var4 = listToTagStruct(this.getDelayActivationBufferNonRepeating(), (String)null);
            return new Tag(Type.STRUCT, "a", new Tag[]{var7, var6, var4, FinishTag.INST});
        }
    }

    public Long2LongOpenHashMap getCooldownBlocks() {
        return this.cooldownBlocks;
    }

    public void addListener(SendableSegmentProvider var1) {
        this.listeners.add(var1);
    }

    public List<SendableSegmentProvider> getListeners() {
        return this.listeners;
    }

    public void onRename(String var1, String var2) {
    }

    public void addFlagRemoveCachedTextBoxes(SendableSegmentProvider var1) {
        synchronized(this.flagRemoveCachedTextBoxes) {
            this.flagRemoveCachedTextBoxes.enqueue(var1);
        }
    }

    public void addSectorConfigProjection(Collection<ConfigProviderSource> var1) {
        if (this instanceof ManagedSegmentController) {
            ((ManagedSegmentController)this).getManagerContainer().getPowerInterface().addSectorConfigProjection(var1);
        }

    }

    public boolean isNewPowerSystemNoReactorOverheatingCondition() {
        return false;
    }

    public boolean isNewPowerSystemNoReactor() {
        return false;
    }

    public long getReactorHpMax() {
        return this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getPowerInterface().getCurrentMaxHp() : 1L;
    }

    public long getReactorHp() {
        return this instanceof ManagedSegmentController ? ((ManagedSegmentController)this).getManagerContainer().getPowerInterface().getCurrentHp() : 1L;
    }

    public abstract boolean isStatic();

    public int getLastModifierId() {
        return this.lastModifierId;
    }

    public void setLastModifierId(int var1) {
        this.lastModifierId = var1;
    }

    public boolean isLastModifierChanged() {
        return this.lastModifierChanged;
    }

    public void setLastModifierChanged(boolean var1) {
        this.lastModifierChanged = var1;
    }

    public BlockProcessor getBlockProcessor() {
        return this.blockProcessor;
    }

    public void onClear() {
        this.railController.onClear();
        this.fullyLoadedRailRecChache = false;
    }

    class Writer implements SegmentBufferIteratorEmptyInterface {
        public boolean forcedTimestamp;
        private int writtenSegments;
        private boolean debug;

        public Writer(boolean var2) {
            this.debug = var2;
        }

        public boolean handle(Segment var1, long var2) {
            try {
                if (this.forcedTimestamp) {
                    var2 = SendableSegmentController.this.getState().getUpdateTime();
                }

                boolean var5 = SendableSegmentController.this.getSegmentProvider().getSegmentDataIO().write((RemoteSegment)var1, var2, false, this.debug);
                System.err.println("[SEFMENTWRITER] WRITING " + SendableSegmentController.this + " " + var1.absPos + ": " + var5);
                if (var5) {
                    ++this.writtenSegments;
                }
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return true;
        }

        public boolean handleEmpty(int var1, int var2, int var3, long var4) {
            try {
                SendableSegmentController.this.getSegmentProvider().getSegmentDataIO().writeEmpty(var1, var2, var3, SendableSegmentController.this, var4, false);
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            return true;
        }
    }

    class DelayedAct {
        long encode;
        long time;

        public DelayedAct(long var2, long var4) {
            this.encode = var2;
            this.time = var4;
        }
    }

    public static class SignalTracePool {
        private static ObjectArrayList<SignalTrace> pool = new ObjectArrayList();
        private static ObjectArrayList<SignalTrace> marked = new ObjectArrayList();

        public SignalTracePool() {
        }

        public SignalTrace get() {
            return pool.isEmpty() ? new SignalTrace() : (SignalTrace)pool.remove(pool.size() - 1);
        }

        private void free(SignalTrace var1) {
            var1.reset();
            pool.add(var1);
        }

        public void markFree(SignalTrace var1) {
            marked.add(var1);
        }

        public void freeMarked() {
            int var1 = marked.size();

            for(int var2 = 0; var2 < var1; ++var2) {
                this.free((SignalTrace)marked.get(var2));
            }

            marked.clear();
        }
    }

    class ReplacementContainer {
        SegmentPiece fromBlockSurround;
        List<SegmentPiece> toReplaceList;
        public boolean ok;

        private ReplacementContainer() {
            this.toReplaceList = new ObjectArrayList();
        }
    }

    class DisplayReplace implements BlockLogicReplaceInterface {
        private DisplayReplace() {
        }

        public boolean isBlockNextToLogicOkTuUse(SegmentPiece var1, SegmentPiece var2) {
            return var2 != null && var2.getType() == 479;
        }

        public void afterReplaceBlock(SegmentPiece var1, SegmentPiece var2) {
            String var3;
            if ((var3 = (String)SendableSegmentController.this.getTextMap().get(var1.getTextBlockIndex())) == null) {
                var3 = "[no data]";
            } else {
                String var4;
                if ((var4 = (String)SendableSegmentController.this.getTextMap().get(var2.getTextBlockIndex())) == null) {
                    var4 = "";
                }

                String var5 = "[add]";
                String var6 = "[del]";
                String var7 = "[replacefirst]";
                String var8 = "[replaceall]";
                if (var3.toLowerCase(Locale.ENGLISH).startsWith(var5)) {
                    var3 = StringTools.limit(var4 + var3.substring(var5.length()), 240, 10);
                } else if (var3.toLowerCase(Locale.ENGLISH).startsWith(var6)) {
                    try {
                        int var12 = Integer.parseInt(var3.substring(var5.length()).trim());
                        var3 = StringTools.limit(var4.substring(0, Math.max(0, var4.length() - var12)), 240, 10);
                    } catch (Exception var11) {
                        var11.printStackTrace();
                        SendableSegmentController.this.sendControllingPlayersServerMessage(new Object[]{137, var1.getAbsolutePos(new Vector3f())}, 3);
                        var3 = var4;
                    }
                } else {
                    String[] var13;
                    if (var3.toLowerCase(Locale.ENGLISH).startsWith(var7)) {
                        try {
                            var13 = var3.substring(var7.length()).trim().split("\\[[wW][iI][tT][hH]\\]");
                            var3 = StringTools.limit(var4.replaceFirst(var13[0], var13[1]), 240, 10);
                        } catch (Exception var10) {
                            var10.printStackTrace();
                            SendableSegmentController.this.sendControllingPlayersServerMessage(new Object[]{138, var1.getAbsolutePos(new Vector3f())}, 3);
                            var3 = var4;
                        }
                    } else if (var3.toLowerCase(Locale.ENGLISH).startsWith(var8)) {
                        try {
                            var13 = var3.substring(var8.length()).trim().split("\\[[wW][iI][tT][hH]\\]");
                            var3 = StringTools.limit(var4.replaceAll(var13[0], var13[1]), 240, 10);
                        } catch (Exception var9) {
                            var9.printStackTrace();
                            SendableSegmentController.this.sendControllingPlayersServerMessage(new Object[]{139, var1.getAbsolutePos(new Vector3f())}, 3);
                            var3 = var4;
                        }
                    }
                }
            }

            SendableSegmentController.this.getTextMap().put(var2.getTextBlockIndex(), var3);
            SendableSegmentController.this.sendTextBlockServerUpdate(var2, var3);
        }

        public boolean fromBlockOk(SegmentPiece var1) {
            return var1.getType() == 479;
        }

        public boolean equalsBlockData(SegmentPiece var1, SegmentPiece var2) {
            return false;
        }

        public void modifyReplacement(SegmentPiece var1, SegmentPiece var2) {
            var1.setOrientation(var2.getOrientation());
        }
    }

    static class BlockActiveReaction {
        boolean oldActive;
        boolean active;
        SegmentPiece block;
        long posIndex;
        long a;
        int counter;
        public long fromActivation;

        private BlockActiveReaction() {
            this.counter = 0;
        }

        public int hashCode() {
            long var10000 = this.block.getAbsoluteIndex();
            return (int)(var10000 ^ var10000 >>> 32);
        }

        public boolean equals(Object var1) {
            return this.block.getAbsoluteIndex() == ((SendableSegmentController.BlockActiveReaction)var1).block.getAbsoluteIndex();
        }

        public void clear() {
            this.counter = 0;
            this.block = null;
        }
    }

    public class SignalQueue extends ObjectArrayFIFOQueue<SignalTrace> {
        public SignalQueue() {
        }
    }
}
