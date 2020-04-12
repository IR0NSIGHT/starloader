//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.player;

import api.listener.events.player.PlayerDamageEvent;
import api.listener.events.player.PlayerDeathEvent;
import api.mod.StarLoader;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipException;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import org.lwjgl.opengl.Display;
import org.schema.common.LogUtil;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3b;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.controller.RoundEndMenu;
import org.schema.game.client.controller.manager.ingame.InventoryControllerManager;
import org.schema.game.client.controller.tutorial.states.TeleportToTutorialSector;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.camera.InShipCamera;
import org.schema.game.client.view.camera.ObjectViewerCamera;
import org.schema.game.client.view.cubes.shapes.BlockShapeAlgorithm;
import org.schema.game.client.view.cubes.shapes.orientcube.Oriencube;
import org.schema.game.client.view.effects.RaisingIndication;
import org.schema.game.client.view.gui.catalog.CatalogEntryPanel;
import org.schema.game.client.view.gui.navigation.navigationnew.SavedCoordinatesScrollableListNew;
import org.schema.game.client.view.gui.shiphud.HudIndicatorOverlay;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.ElementCountMap;
import org.schema.game.common.controller.FactionChange;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.PlayerFactionController;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopInterface;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.ShopperInterface;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.ai.AIGameCreatureConfiguration;
import org.schema.game.common.controller.ai.Types;
import org.schema.game.common.controller.ai.UnloadedAiContainer;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.database.DatabaseEntry;
import org.schema.game.common.controller.database.DatabaseIndex;
import org.schema.game.common.controller.database.FogOfWarReceiver;
import org.schema.game.common.controller.database.DatabaseEntry.EntityTypeNotFoundException;
import org.schema.game.common.controller.database.tables.FTLTable;
import org.schema.game.common.controller.elements.ActivationManagerInterface;
import org.schema.game.common.controller.elements.Cockpit;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.StationaryManagerContainer;
import org.schema.game.common.controller.elements.scanner.ScannerElementManager;
import org.schema.game.common.controller.elements.warpgate.WarpgateCollectionManager;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.controller.rails.RailRequest;
import org.schema.game.common.controller.rails.RailRelation.DockingPermission;
import org.schema.game.common.controller.rules.rules.PlayerRuleEntityManager;
import org.schema.game.common.controller.trade.manualtrade.ManualTrade;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.MetaObjectState;
import org.schema.game.common.data.ScanData;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.VoidUniqueSegmentPiece;
import org.schema.game.common.data.creature.AICreature;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.FixedRecipe;
import org.schema.game.common.data.element.ScannerManagerInterface;
import org.schema.game.common.data.element.meta.BlockStorageMetaItem;
import org.schema.game.common.data.element.meta.BlueprintMetaItem;
import org.schema.game.common.data.element.meta.MetaObject;
import org.schema.game.common.data.element.meta.MetaObjectManager;
import org.schema.game.common.data.element.meta.Recipe;
import org.schema.game.common.data.element.meta.MetaObjectManager.MetaObjectType;
import org.schema.game.common.data.element.meta.weapon.Weapon;
import org.schema.game.common.data.missile.Missile;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.player.catalog.PlayerCatalogManager;
import org.schema.game.common.data.player.dialog.PlayerConversationManager;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionNewsPost;
import org.schema.game.common.data.player.faction.FactionPermission.PermType;
import org.schema.game.common.data.player.faction.FactionRelation.RType;
import org.schema.game.common.data.player.faction.config.FactionPointsGeneralConfig;
import org.schema.game.common.data.player.inventory.CreativeModeInventory;
import org.schema.game.common.data.player.inventory.FreeItem;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.game.common.data.player.inventory.InventoryController;
import org.schema.game.common.data.player.inventory.InventoryHolder;
import org.schema.game.common.data.player.inventory.InventoryMultMod;
import org.schema.game.common.data.player.inventory.InventorySlot;
import org.schema.game.common.data.player.inventory.NetworkInventoryInterface;
import org.schema.game.common.data.player.inventory.NoSlotFreeException;
import org.schema.game.common.data.player.inventory.PersonalFactoryInventory;
import org.schema.game.common.data.player.inventory.PlayerInventory;
import org.schema.game.common.data.player.inventory.StashInventory;
import org.schema.game.common.data.player.inventory.VirtualCreativeModeInventory;
import org.schema.game.common.data.world.Chunk16SegmentData;
import org.schema.game.common.data.world.ClientProximitySector;
import org.schema.game.common.data.world.ClientProximitySystem;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.RuleEntityContainer;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentDataIntArray;
import org.schema.game.common.data.world.SegmentDataWriteException;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.StellarSystem;
import org.schema.game.common.data.world.SectorInformation.SectorType;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.network.objects.CreateDockRequest;
import org.schema.game.network.objects.DragDrop;
import org.schema.game.network.objects.NetworkPlayer;
import org.schema.game.network.objects.remote.RemoteBlockCountMap;
import org.schema.game.network.objects.remote.RemoteBlueprintPlayerRequest;
import org.schema.game.network.objects.remote.RemoteCreatureSpawnRequest;
import org.schema.game.network.objects.remote.RemoteDragDrop;
import org.schema.game.network.objects.remote.RemoteInventoryMultMod;
import org.schema.game.network.objects.remote.RemoteSavedCoordinate;
import org.schema.game.network.objects.remote.RemoteScanData;
import org.schema.game.network.objects.remote.RemoteSegmentControllerBlock;
import org.schema.game.network.objects.remote.RemoteServerMessage;
import org.schema.game.network.objects.remote.RemoteSimpelCommand;
import org.schema.game.network.objects.remote.SimpleCommand;
import org.schema.game.network.objects.remote.SimplePlayerCommand;
import org.schema.game.server.ai.CreatureAIEntity;
import org.schema.game.server.controller.BluePrintController;
import org.schema.game.server.controller.EntityAlreadyExistsException;
import org.schema.game.server.controller.EntityNotFountException;
import org.schema.game.server.controller.NotEnoughCreditsException;
import org.schema.game.server.controller.SectorSwitch;
import org.schema.game.server.controller.SectorUtil;
import org.schema.game.server.data.CatalogState;
import org.schema.game.server.data.CreatureSpawn;
import org.schema.game.server.data.CreatureType;
import org.schema.game.server.data.EntityRequest;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.blueprint.BluePrintSpawnQueueElement;
import org.schema.game.server.data.blueprint.BluePrintWriteQueueElement;
import org.schema.game.server.data.blueprint.ChildStats;
import org.schema.game.server.data.blueprint.SegmentControllerOutline;
import org.schema.game.server.data.blueprintnw.BlueprintClassification;
import org.schema.game.server.data.blueprintnw.BlueprintEntry;
import org.schema.game.server.data.blueprintnw.BlueprintType;
import org.schema.schine.ai.stateMachines.AiInterface;
import org.schema.schine.common.JoystickAxisMapping;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.AbstractScene;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.core.settings.StateParameterNotFoundException;
import org.schema.schine.graphicsengine.forms.BoundingBox;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.input.JoystickMappingFile;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.client.ClientStateInterface;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.container.TransformTimed;
import org.schema.schine.network.objects.remote.RemoteField;
import org.schema.schine.network.objects.remote.RemoteIntegerArray;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.network.objects.remote.RemoteVector3i;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.network.server.ServerStateInterface;
import org.schema.schine.resource.DiskWritable;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.UniqueInterface;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.ListSpawnObjectCallback;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class PlayerState extends AbstractOwnerState implements FogOfWarReceiver, RuleEntityContainer, Sendable, DiskWritable {
    public static final float MAX_HEALTH = 120.0F;
    private static final long DEFAULT_BLUEPRINT_DELAY = 10000L;
    public static ByteBuffer buffer = ByteBuffer.allocate(10240);
    public static Vector3i NO_WAYPOINT = new Vector3i(-2147483648, 2147483647, -2147483648);
    public final PlayerStateSpawnData spawnData;
    private final Vector3f lastForward = new Vector3f(0.0F, 0.0F, 1.0F);
    private final Vector3f lastRight = new Vector3f(1.0F, 0.0F, 0.0F);
    private final Vector3f lastUp = new Vector3f(0.0F, 1.0F, 0.0F);
    private final ByteArrayList keyboardEvents = new ByteArrayList();
    private final ControllerState controllerState;
    private final UploadController shipUploadController;
    private final boolean onServer;
    private final PlayerConversationManager playerConversationManager;
    private final Vector3i currentSector;
    private final Set<ShopInterface> shopsInDistance;
    private final PlayerCatalogManager catalog;
    private final ArrayList<Damager> dieList;
    private final ObjectArrayList<SavedCoordinate> savedCoordinates;
    private final ObjectArrayFIFOQueue<SavedCoordinate> savedCoordinatesToAdd;
    private final SkinUploadController skinUploadController;
    private final SkinManager skinManager;
    private final PlayerFactionController factionController;
    private final long creationTime;
    private final Transform remoteCam;
    private final IntArrayList toDrop;
    private final Vector3f min;
    private final Vector3f max;
    private final Vector3f tmpmin;
    private final Vector3f tmpmax;
    private boolean hasCreativeMode;
    private boolean useCreativeMode;
    private final IntArrayFIFOQueue killerIds;
    private final ObjectArrayFIFOQueue<InventoryMultMod> queuedModifactions;
    private final ObjectArrayFIFOQueue<SimpleCommand<?>> simpleCommandQueue;
    private final ObjectArrayFIFOQueue<CreatureSpawn> requestedreatureSpawns;
    private final PlayerAiManager playerAiManager;
    private final Vector3i currentSystem;
    private final List<ScanData> scanHistory;
    private final PlayerChannelManager playerChannelManager;
    public boolean spawnedOnce;
    public long lastSectorProtectedMsgSent;
    public Tag mainInventoryBackup;
    public Tag capsuleRefinerInventoryBackup;
    public Tag microInventoryBackup;
    public Tag factoryInventoryBackup;
    public long inControlTransition;
    public Vector3i personalSector;
    public int friendlyFireStrikes;
    public long lastFriendlyFireStrike;
    public SavedCoordinatesScrollableListNew savedCoordinatesList;
    public boolean hasSpawnWait;
    boolean[] wasKeyDown;
    boolean[] wasMouseEvent;
    boolean[] wasMouseDown;
    long lastHitConfirm;
    long lastHelmetCommand;
    private float health;
    private int credits;
    private int id;
    private int helmetSlot;
    private NetworkPlayer networkPlayerObject;
    private StateInterface state;
    private InventoryController inventoryController;
    private String name;
    private int clientId;
    private boolean markedForDelete;
    private int ping;
    private ArrayList<BluePrintSpawnQueueElement> bluePrintSpawnQueue;
    private ArrayList<BluePrintWriteQueueElement> bluePrintWriteQueue;
    private List<String> ignored;
    private int currentSectorId;
    private int kills;
    private int deaths;
    private boolean alive;
    private boolean markedForDeleteSent;
    private SimpleTransformableSendableObject<?> aquiredTarget;
    private PlayerCharacter playerCharacter;
    private RegisteredClientOnServer serverClient;
    private ArrayList<ServerMessage> serverToSendMessages;
    private LongArrayList creditModifications;
    private ClientProximitySector proximitySector;
    private boolean sectorChanged;
    private ClientProximitySystem proximitySystem;
    private long sectorBlackHoleEffectStart;
    private long lastInventoryFullMsgSent;
    private boolean markedForPermanentDelete;
    private long lastDeathTime;
    private String ip;
    private long lastBlueprintSpawn;
    private long blueprintDelay;
    private float lastHP;
    private boolean checkForOvercap;
    private Transform transTmp;
    private boolean godMode;
    private boolean invisibilityMode;
    private String lastEnteredEntity;
    private String starmadeName;
    private boolean upgradedAccount;
    private byte clientHitNotifaction;
    private String giveMetaBlueprint;
    private long lastSetProtectionMsg;
    private boolean factionPointProtected;
    private String tutorialCallClient;
    private int oldSectorId;
    private SectorType currentSectorType;
    private boolean basicTutorialStarted;
    private long lastMessage;
    private ClientChannel clientChannel;
    private boolean newPlayerOnServer;
    private long lastDeathNotSuicide;
    private String lastDiedMessage;
    private boolean createdClientChannelOnServer;
    private boolean flagSendUseCreative;
    public Vector3i testSector;
    private final ObjectArrayFIFOQueue<CreateDockRequest> createDockRequests;
    private long lastLagReceived;
    private long currentLag;
    private boolean useCargoInventory;
    private int flagRequestCargoInv;
    private VoidUniqueSegmentPiece cargoInventoryBlock;
    private byte shipControllerSlotClient;
    private float slotChangeUpdateDelay;
    private Vector3i lastSector;
    private ObjectArrayFIFOQueue<Vector3i> resetFowSystem;
    private ObjectArrayFIFOQueue<DragDrop> dropsIntoSpace;
    private int inputBasedSeed;
    private boolean lastCanKeyPressCheckResult;
    private short lastCanKeyPressCheck;
    private ByteArrayList mouseEvents;
    private long lastSentHitDeniedMessage;
    private final BuildModePosition buildModePosition;
    private boolean hasCargoBlockMigratedChunk16;
    private long dbId;
    private final Vector3i tmpSystem;
    private final FogOfWarController fow;
    private LinkedHashSet<Vector3i> lastVisitedSectors;
    public final long[] offlinePermssion;
    public int tempSeed;
    private boolean infiniteInventoryVolume;
    private int mineAutoArmSecs;
    private long lastSpawnedThisSession;
    private Sector forcedEnterSector;
    private String forcedEnterUID;
    private final Cockpit cockpit;
    private final PlayerRuleEntityManager ruleEntityManager;

    public PlayerState(StateInterface var1) {
        this.currentSector = new Vector3i(Sector.DEFAULT_SECTOR);
        this.shopsInDistance = new HashSet();
        this.dieList = new ArrayList();
        this.savedCoordinates = new ObjectArrayList();
        this.savedCoordinatesToAdd = new ObjectArrayFIFOQueue();
        this.remoteCam = new Transform();
        this.toDrop = new IntArrayList();
        this.min = new Vector3f();
        this.max = new Vector3f();
        this.tmpmin = new Vector3f();
        this.tmpmax = new Vector3f();
        this.killerIds = new IntArrayFIFOQueue();
        this.queuedModifactions = new ObjectArrayFIFOQueue();
        this.simpleCommandQueue = new ObjectArrayFIFOQueue();
        this.requestedreatureSpawns = new ObjectArrayFIFOQueue();
        this.currentSystem = new Vector3i(-2147483648, -2147483648, -2147483648);
        this.scanHistory = new ObjectArrayList();
        this.wasKeyDown = new boolean[KeyboardMappings.remoteMappings.length];
        this.wasMouseEvent = new boolean[4];
        this.wasMouseDown = new boolean[4];
        this.health = 120.0F;
        this.helmetSlot = -1;
        this.bluePrintSpawnQueue = new ArrayList();
        this.bluePrintWriteQueue = new ArrayList();
        this.ignored = new ArrayList();
        this.serverToSendMessages = new ArrayList();
        this.creditModifications = new LongArrayList();
        this.sectorChanged = true;
        this.sectorBlackHoleEffectStart = -1L;
        this.blueprintDelay = 10000L;
        this.lastHP = 120.0F;
        this.checkForOvercap = true;
        this.transTmp = new Transform();
        this.oldSectorId = -1;
        this.currentSectorType = SectorType.ASTEROID;
        this.lastDiedMessage = "";
        this.createDockRequests = new ObjectArrayFIFOQueue();
        this.flagRequestCargoInv = -1;
        this.lastSector = new Vector3i();
        this.resetFowSystem = new ObjectArrayFIFOQueue();
        this.dropsIntoSpace = new ObjectArrayFIFOQueue();
        this.mouseEvents = new ByteArrayList();
        this.dbId = -1L;
        this.tmpSystem = new Vector3i();
        this.fow = new FogOfWarController(this);
        this.lastVisitedSectors = new LinkedHashSet();
        this.offlinePermssion = new long[2];
        this.mineAutoArmSecs = -1;
        this.cockpit = new Cockpit(this);
        this.setState(var1);
        this.buildModePosition = new BuildModePosition(this);
        this.spawnData = new PlayerStateSpawnData(this);
        this.playerConversationManager = new PlayerConversationManager(this);
        this.creationTime = System.currentTimeMillis();
        this.onServer = var1 instanceof ServerStateInterface;
        this.controllerState = new ControllerState(this);
        this.playerAiManager = new PlayerAiManager(this);
        this.catalog = new PlayerCatalogManager(this);
        this.proximitySector = new ClientProximitySector(this);
        this.proximitySystem = new ClientProximitySystem(this);
        this.shipUploadController = new ShipUploadController(this);
        this.skinUploadController = new SkinUploadController(this);
        this.skinManager = new SkinManager(this);
        this.factionController = new PlayerFactionController(this);
        this.setInventory(new PlayerInventory(this, 0L));
        this.creativeInventory = new CreativeModeInventory(this, 0L);
        this.virtualCreativeInventory = new VirtualCreativeModeInventory(this, 0L);
        this.setPersonalFactoryInventoryCapsule(new PersonalFactoryInventory(this, 1L, (short)213));
        this.setPersonalFactoryInventoryMicro(new PersonalFactoryInventory(this, 2L, (short)215));
        this.setPersonalFactoryInventoryMacroBlock(new PersonalFactoryInventory(this, 3L, (short)211));
        this.inventoryController = new InventoryController(this);
        this.playerChannelManager = new PlayerChannelManager(this);
        this.ruleEntityManager = new PlayerRuleEntityManager(this);
    }

    public boolean isCreativeModeEnabled() {
        return this.isUseCreativeMode() && this.isHasCreativeMode();
    }

    public Inventory getInventory() {
        Inventory var1;
        if ((var1 = this.getCargoInventoryIfActive()) != null) {
            return var1;
        } else if (this.isCreativeModeEnabled() && !this.isInTutorial()) {
            return this.creativeInventory;
        } else {
            return this.getFirstControlledTransformableWOExc() != null && this.getFirstControlledTransformableWOExc() instanceof SegmentController && ((SegmentController)this.getFirstControlledTransformableWOExc()).isVirtualBlueprint() ? this.virtualCreativeInventory : this.inventory;
        }
    }

    public int getSelectedEntityId() {
        return this.getNetworkObject().selectedEntityId.getInt();
    }

    public void announceKill(Damager var1) {
        if (this.isOnServer()) {
            this.getControllerState().removeAllUnitsFromPlayer(this, false);
            System.err.println(this.getState() + " " + this + " Announcing kill: " + var1 + " killed " + this);
            int var2 = -1;
            if (var1 != null && var1 instanceof Sendable) {
                var2 = ((Sendable)var1).getId();
            }

            if (var1 instanceof Missile) {
                ((Missile)var1).getOwner();
                var2 = ((Sendable)((Missile)var1).getOwner()).getId();
            }

            ((GameServerState)this.getState()).getGameState().announceKill(this, var2);
            this.sendKill(var2);
        } else {
            throw new IllegalArgumentException("Clients may not be here");
        }
    }

    public void putAllInventoryInSpace() {
        this.putInventoryInSpace(-1L);
        this.putInventoryInSpace(1L);
        this.putInventoryInSpace(2L);
        this.putInventoryInSpace(3L);
    }

    public void putInventoryInSpace(long var1) {
        try {
            Inventory var4 = this.getInventory(var1);
            SimpleTransformableSendableObject var3 = this.getFirstControlledTransformable();
            IntOpenHashSet var5 = new IntOpenHashSet(var4.getMap().size());
            var4.spawnInSpace(var3, ElementCollection.getIndex(16, 16, 16), var5);
            this.sendInventoryModification(var5, var1);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void checkIfDiedOnServer() {
        if (!this.dieList.isEmpty()) {
            Damager from = (Damager)this.dieList.get(this.dieList.size() - 1);
            //INSERTED CODE
            PlayerDeathEvent event = new PlayerDeathEvent(this, from);
            StarLoader.fireEvent(PlayerDeathEvent.class, event);
            ///
            System.err.println("[SERVER] PLAYER " + this + " died, removing ALL control units");
            Sector var2;
            if ((var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.getCurrentSectorId())) != null) {
                if (this.isSpawnProtected()) {
                    this.sendServerMessage(new ServerMessage(new Object[]{305}, 3, this.getId()));
                } else if (!var2.isPeace()) {
                    float var7 = (Float)ServerConfig.PLAYER_DEATH_CREDIT_PUNISHMENT.getCurrentState();
                    int var3 = (Integer)ServerConfig.PLAYER_DEATH_PUNISHMENT_TIME.getCurrentState();
                    if (this.lastSpawnedThisSession != 0L && System.currentTimeMillis() - this.lastSpawnedThisSession <= (long)(var3 * 1000)) {
                        long var9 = (this.getState().getUpdateTime() - this.lastSpawnedThisSession) / 1000L;
                        this.sendServerMessage(new ServerMessage(new Object[]{309, var9, var3}, 3, this.getId()));
                    } else {
                        if (var7 < 1.0F) {
                            int var4 = (int)((float)this.getCredits() * var7);
                            if (ServerConfig.PLAYER_DEATH_CREDIT_DROP.isOn()) {
                                this.sendServerMessage(new ServerMessage(new Object[]{306, var4}, 3, this.getId()));
                                this.dropCreditsIntoSpace(var4);
                            } else {
                                this.sendServerMessage(new ServerMessage(new Object[]{307, var4}, 3, this.getId()));
                                this.modCreditsServer((long)(-var4));
                            }
                        }

                        if (ServerConfig.PLAYER_DEATH_BLOCK_PUNISHMENT.isOn()) {
                            this.sendServerMessage(new ServerMessage(new Object[]{308}, 3, this.getId()));
                            this.putAllInventoryInSpace();
                        }
                    }
                } else {
                    this.sendServerMessage(new ServerMessage(new Object[]{310}, 3, this.getId()));
                }
            }

            this.lastDeathTime = this.getState().getUpdateTime();
            this.announceKill(from);
            synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                Iterator var8 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                while(var8.hasNext()) {
                    AbstractCharacter var5;
                    Sendable var10;
                    if ((var10 = (Sendable)var8.next()) instanceof AbstractCharacter && (var5 = (AbstractCharacter)var10).getOwnerState() == this) {
                        var5.destroy(from);
                        break;
                    }
                }
            }

            this.dieList.clear();
        }

    }

    public boolean checkItemInReach(FreeItem var1, Object2ObjectOpenHashMap<PlayerState, IntOpenHashSet> var2) {
        assert this.isOnServer();

        try {
            SimpleTransformableSendableObject var3;
            if ((var3 = this.getFirstControlledTransformable()) != null) {
                Sector var4 = ((GameServerState)this.state).getUniverse().getSector(this.getCurrentSectorId());
                boolean var9;
                if (var3 instanceof SegmentController) {
                    Vector3f var5;
                    (var5 = new Vector3f(var1.getPos())).sub(new Vector3f(0.0F, 0.5F, 0.0F));
                    Vector3f var6;
                    (var6 = new Vector3f(var1.getPos())).add(new Vector3f(0.0F, 0.5F, 0.0F));
                    var9 = ((PhysicsExt)var4.getPhysics()).testRayCollisionPoint(var5, var6, false, (SimpleTransformableSendableObject)null, (SegmentController)var3, false, true, false).hasHit();
                } else {
                    var3.getTransformedAABB(this.min, this.max, 0.5F, this.tmpmin, this.tmpmax, (Transform)null);
                    var9 = BoundingBox.testPointAABB(var1.getPos(), this.min, this.max);
                }

                if (var9) {
                    if (var1.getType() == -2) {
                        this.modCreditsServer((long)var1.getCount());
                        this.sendServerMessage(new ServerMessage(new Object[]{311, var1.getCount()}, 1, this.getId()));
                        var1.setCount(0);
                    } else {
                        int var11 = var1.getCount();
                        if ((var11 = this.getInventory().canPutInHowMuch(var1.getType(), var11, var1.getMetaId())) <= 0) {
                            System.err.println("[SERVER] " + this + " Cannot pick up item: inventory is full");
                            if (System.currentTimeMillis() - this.lastInventoryFullMsgSent > 7000L) {
                                this.sendServerMessage(new ServerMessage(new Object[]{314}, 2, this.getId()));
                                this.lastInventoryFullMsgSent = System.currentTimeMillis();
                            }

                            return false;
                        }

                        System.err.println("[SERVER][PLAYERSTATE] Picked up: Type " + var1.getType() + "; Count " + var11 + "; Meta " + var1.getMetaId());
                        int var12 = this.getInventory((Vector3i)null).incExistingOrNextFreeSlotWithoutException(var1.getType(), var11, var1.getMetaId());
                        IntOpenHashSet var10;
                        if ((var10 = (IntOpenHashSet)var2.get(this)) == null) {
                            var10 = new IntOpenHashSet(24);
                            var2.put(this, var10);
                        }

                        var1.setCount(var1.getCount() - var11);
                        var10.add(var12);
                        if (var1.getType() > 0) {
                            this.sendServerMessage(new ServerMessage(new Object[]{312, var11, ElementKeyMap.getInfo(var1.getType()).getName()}, 1, this.getId()));
                        } else {
                            MetaObject var8;
                            if ((var8 = ((MetaObjectState)this.state).getMetaObjectManager().getObject(var1.getMetaId())) != null) {
                                this.sendServerMessage(new ServerMessage(new Object[]{313, var8.getName()}, 1, this.getId()));
                            }
                        }
                    }

                    var4.getRemoteSector().removeItem(var1.getId());
                    if (var1.getCount() > 0) {
                        var4.getRemoteSector().addItem(var1.getPos(), var1.getType(), var1.getMetaId(), var1.getCount());
                    }

                    return true;
                }
            }
        } catch (PlayerControlledTransformableNotFound var7) {
        }

        return false;
    }

    public void cleanUpOnEntityDelete() {
        this.getControllerState().removeAllUnitsFromPlayer(this, true);
        System.err.println("[PLAYER][CLEANUP] " + this + " removed controlled entities");
        if (!this.isOnServer() && this == ((GameClientState)this.state).getPlayer()) {
            System.err.println("PLAYER SET TO NULL ON " + this.state);
            ((GameClientState)this.state).setPlayer((PlayerState)null);
        }

        this.factionController.cleanUp();
        this.catalog.cleanUp();
        System.err.println("[PLAYER][CLEANUP] " + this + " notified team change");
    }

    public void sendInventoryErrorMessage(Object[] var1, Inventory var2) {
        this.sendServerMessagePlayerError(var1);
    }

    public void destroyPersistent() {
        assert this.isOnServer();

        String var1 = "ENTITY_PLAYERSTATE_" + this.getName();
        (new FileExt(GameServerState.ENTITY_DATABASE_PATH + var1 + ".ent")).delete();
    }

    public void initFromNetworkObject(NetworkObject var1) {
        NetworkPlayer var4 = (NetworkPlayer)var1;
        this.setId(var4.id.getInt());
        this.setClientId(var4.clientId.getInt());
        this.setCredits(var4.credits.getInt());
        this.setCurrentSector(var4.sectorPos.getVector());
        this.setCurrentSectorId(var4.sectorId.getInt());
        this.handleServerHealthAndCheckAliveOnServer(var4.health.getFloat(), (Damager)null);
        this.kills = (Integer)var4.kills.get();
        this.deaths = (Integer)var4.deaths.get();
        this.lastDeathNotSuicide = var4.lastDeathNotSuicide.get();
        this.currentSectorType = SectorType.values()[var4.currentSectorType.getInt()];
        this.setName((String)var4.playerName.get());
        this.personalSector = var4.personalSector.getVector();
        this.testSector = var4.testSector.getVector();
        this.dbId = this.getNetworkObject().dbId.getLong();
        this.setUpgradedAccount(this.getNetworkObject().upgradedAccount.get());
        this.spawnData.initFromNetworkObject();
        this.factionController.initFromNetworkObject(var4);
        this.playerAiManager.initFromNetworkObject(var4);
        this.skinManager.initFromNetworkObject();
        this.ping = var4.ping.get();
        this.sittingOnId = (int)this.getNetworkObject().sittingState.getLongArray()[0];
        ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[1], this.sittingPos);
        ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[2], this.sittingPosTo);
        ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[3], this.sittingPosLegs);

        assert this.getId() >= 0;

        assert this.getClientId() >= 0;

        assert this.getState().getId() >= 0;

        if (!this.isOnServer()) {
            this.lastSpawnedThisSession = this.getNetworkObject().lastSpawnedThisSession.getLong();

            for(int var2 = 0; var2 < this.getNetworkObject().cargoInventoryChange.getReceiveBuffer().size(); ++var2) {
                RemoteSegmentControllerBlock var3 = (RemoteSegmentControllerBlock)this.getNetworkObject().cargoInventoryChange.getReceiveBuffer().get(var2);
                this.cargoInventoryBlock = (VoidUniqueSegmentPiece)var3.get();
            }

            this.infiniteInventoryVolume = this.getNetworkObject().infiniteInventoryVolume.getBoolean();
            this.getNetworkObject().tint.getVector(this.getTint());
            this.setInvisibilityMode(var4.invisibility.get());
            if (this.getClientId() == this.state.getId()) {
                ((GameClientState)this.state).setPlayer(this);
                System.out.println("[PlayerState] Client successfully received player state " + this.getState() + ", owner: " + this.getClientId());
            }
        }

        this.buildModePosition.initFromNetworkObject();
    }

    public void initialize() {
        if (this.isOnServer()) {
            if (GameClientState.singleplayerCreativeMode == 1) {
                this.setHasCreativeMode(true);
                return;
            }

            if (GameClientState.singleplayerCreativeMode == 2) {
                this.setHasCreativeMode(false);
            }
        }

    }

    public boolean isMarkedForDeleteVolatile() {
        return this.markedForDelete;
    }

    public void setMarkedForDeleteVolatile(boolean var1) {
        this.markedForDelete = var1;
    }

    public boolean isMarkedForDeleteVolatileSent() {
        return this.markedForDeleteSent;
    }

    public void setMarkedForDeleteVolatileSent(boolean var1) {
        this.markedForDeleteSent = var1;
    }

    public boolean isMarkedForPermanentDelete() {
        return this.markedForPermanentDelete;
    }

    public boolean isOkToAdd() {
        return true;
    }

    public boolean isUpdatable() {
        return true;
    }

    public void markForPermanentDelete(boolean var1) {
        this.markedForPermanentDelete = var1;
    }

    public void newNetworkObject() {
        this.setNetworkPlayerObject(new NetworkPlayer(this.getState(), this));
    }

    public void updateFromNetworkObject(NetworkObject var1, int var2) {
        NetworkPlayer var3 = (NetworkPlayer)var1;
        this.skinManager.updateFromNetworkObject();
        if (!this.isClientOwnPlayer()) {
            if (!this.isOnServer()) {
                this.inputBasedSeed = var3.inputSeed.getInt();
            }

            if (var2 != 0 && var2 != this.clientId) {
                System.err.println(this.getState() + " " + this + " WARNING: Possible attempt to hack controls of another player. sender: " + var2 + " but was: " + this.clientId);
                System.err.println(this.getState() + " " + this + " debugging what was sent: " + var1.lastDecoded);
            }

            this.getNetworkObject().camOrientation.getMatrix(this.remoteCam.basis);
        }

        int var9;
        for(var9 = 0; var9 < this.getNetworkObject().cargoInventoryChange.getReceiveBuffer().size(); ++var9) {
            RemoteSegmentControllerBlock var4 = (RemoteSegmentControllerBlock)this.getNetworkObject().cargoInventoryChange.getReceiveBuffer().get(var9);
            this.cargoInventoryBlock = (VoidUniqueSegmentPiece)var4.get();
            if (this.isOnServer()) {
                this.getNetworkObject().cargoInventoryChange.add(var4);
            }
        }

        for(var9 = 0; var9 < this.getNetworkObject().resetFowBuffer.getReceiveBuffer().size(); ++var9) {
            this.resetFowSystem.enqueue(((RemoteVector3i)this.getNetworkObject().resetFowBuffer.getReceiveBuffer().get(var9)).getVector());
        }

        this.ruleEntityManager.receive(this.getNetworkObject());
        if (!this.isOnServer()) {
            this.lastSpawnedThisSession = this.getNetworkObject().lastSpawnedThisSession.getLong();
            this.mineAutoArmSecs = this.getNetworkObject().mineArmTimer.getInt();
        } else {
            for(var9 = 0; var9 < this.getNetworkObject().mineArmTimerRequests.getReceiveBuffer().size(); ++var9) {
                this.mineAutoArmSecs = this.getNetworkObject().mineArmTimerRequests.getReceiveBuffer().get(var9);
            }
        }

        this.buildModePosition.updateFromNetworkObject();
        this.playerConversationManager.updateFromNetworkObject();
        int var5;
        boolean var10;
        int var12;
        if (!this.isOnServer()) {
            this.infiniteInventoryVolume = this.getNetworkObject().infiniteInventoryVolume.getBoolean();
            this.setUseCargoInventory(this.getNetworkObject().useCargoMode.getBoolean());
            this.setHasCreativeMode(this.getNetworkObject().hasCreativeMode.getBoolean());
            if (!this.isClientOwnPlayer()) {
                this.setUseCreativeMode(this.getNetworkObject().useCreativeMode.getBoolean());
            }

            for(var9 = 0; var9 < this.getNetworkObject().blockCountMapBuffer.getReceiveBuffer().size(); ++var9) {
                RemoteBlockCountMap var11 = (RemoteBlockCountMap)this.getNetworkObject().blockCountMapBuffer.getReceiveBuffer().get(var9);
                if (this.isClientOwnPlayer()) {
                    CatalogEntryPanel.currentRequestedBlockMap = (ElementCountMap)var11.get();
                }
            }

            this.lastDeathNotSuicide = this.getNetworkObject().lastDeathNotSuicide.get();
            this.currentSectorType = SectorType.values()[var3.currentSectorType.getInt()];
            this.sittingOnId = (int)this.getNetworkObject().sittingState.getLongArray()[0];
            ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[1], this.sittingPos);
            ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[2], this.sittingPosTo);
            ElementCollection.getPosFromIndex(this.getNetworkObject().sittingState.getLongArray()[3], this.sittingPosLegs);
            this.getNetworkObject().tint.getVector(this.getTint());
            this.setInvisibilityMode(var3.invisibility.get());
            var10 = false;
            if (this.credits != var3.credits.getInt()) {
                var10 = true;
            }

            this.setCredits(var3.credits.getInt());
            if (var10) {
                Starter.modManager.onPlayerCreditsChanged(this);
            }

            this.setHelmetSlot(var3.helmetSlot.getInt());
            this.health = var3.health.getFloat();
            var12 = this.getCurrentSectorId();
            if (var3.sectorId.get() != this.currentSectorId && ((GameClientState)this.getState()).getPlayer() == this) {
                ((GameClientState)this.getState()).flagWarped();
            }

            this.setCurrentSector(var3.sectorPos.getVector());
            this.setCurrentSectorId(var3.sectorId.get());
            if (var12 != this.getCurrentSectorId() && this.isClientOwnPlayer()) {
                ((GameClientState)this.getState()).setFlagSectorChange(var12);
            }

            for(var5 = 0; var5 < var3.tutorialCalls.getReceiveBuffer().size(); ++var5) {
                this.tutorialCallClient = (String)((RemoteString)var3.tutorialCalls.getReceiveBuffer().get(var5)).get();
            }

            for(var5 = 0; var5 < var3.messages.getReceiveBuffer().size(); ++var5) {
                ServerMessage var13 = (ServerMessage)((RemoteServerMessage)var3.messages.getReceiveBuffer().get(var5)).get();
                ((GameClientState)this.getState()).getServerMessages().add(var13);
            }
        }

        this.getShipUploadController().handleUploadNT(var2);
        this.getSkinUploadController().handleUploadNT(var2);
        this.kills = (Integer)var3.kills.get();
        this.deaths = (Integer)var3.deaths.get();
        this.factionController.updateFromNetworkObject(var3);
        this.playerAiManager.updateFromNetworkObject(var3);
        this.ping = var3.ping.get();
        this.spawnData.fromNetworkObject();
        if (!this.isClientOwnPlayer()) {
            for(var9 = 0; var9 < var3.keyboardOfControllerBuffer.getReceiveBuffer().size(); ++var9) {
                this.keyboardEvents.add(var3.keyboardOfControllerBuffer.getReceiveBuffer().getByte(var9));
            }
        }

        if (this.isOnServer()) {
            for(var9 = 0; var9 < var3.mouseOfControllerBuffer.getReceiveBuffer().size(); ++var9) {
                ++this.inputBasedSeed;
                this.mouseEvents.add(var3.mouseOfControllerBuffer.getReceiveBuffer().getByte(var9));
            }
        }

        this.handleInventoryStateFromNT(var3);
        super.handleInventoryNT();
        this.handleSpawnRequestFromNT(var3);
        this.controllerState.handleControllerStateFromNT(var3);
        this.handleKilledFromNT(var3);
        this.handleRoundEndFromNT(var3);
        if (this.isOnServer()) {
            var10 = this.isUseCreativeMode();
            this.setUseCreativeMode(this.getNetworkObject().useCreativeMode.getBoolean());
            if (var10 != this.isUseCreativeMode()) {
                this.flagSendUseCreative = true;
            }

            for(var12 = 0; var12 < this.getNetworkObject().requestCargoMode.getReceiveBuffer().size(); ++var12) {
                var5 = this.getNetworkObject().requestCargoMode.getReceiveBuffer().getInt(var12);
                this.flagRequestCargoInv = var5;
            }

            for(var12 = 0; var12 < this.getNetworkObject().creatureSpawnBuffer.getReceiveBuffer().size(); ++var12) {
                RemoteCreatureSpawnRequest var14 = (RemoteCreatureSpawnRequest)this.getNetworkObject().creatureSpawnBuffer.getReceiveBuffer().get(var12);
                synchronized(this.requestedreatureSpawns) {
                    this.requestedreatureSpawns.enqueue(var14.get());
                }
            }

            for(var12 = 0; var12 < this.getNetworkObject().simpleCommandQueue.getReceiveBuffer().size(); ++var12) {
                SimpleCommand var16 = (SimpleCommand)((RemoteSimpelCommand)this.getNetworkObject().simpleCommandQueue.getReceiveBuffer().get(var12)).get();
                synchronized(this.simpleCommandQueue) {
                    this.simpleCommandQueue.enqueue(var16);
                }
            }

            for(var12 = 0; var12 < this.getNetworkObject().creditsDropBuffer.getReceiveBuffer().size(); ++var12) {
                var5 = this.getNetworkObject().creditsDropBuffer.getReceiveBuffer().get(var12);
                synchronized(this.toDrop) {
                    this.toDrop.add(var5);
                }
            }

            if (this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().containsKey(var3.aquiredTargetId.get())) {
                this.aquiredTarget = (SimpleTransformableSendableObject)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get((Integer)var3.aquiredTargetId.get());
            } else {
                this.aquiredTarget = null;
            }
        }

        for(Iterator var15 = this.getNetworkObject().lagAnnouncement.getReceiveBuffer().iterator(); var15.hasNext(); this.lastLagReceived = System.currentTimeMillis()) {
            long var17 = (Long)var15.next();
            this.currentLag = var17;
        }

    }

    public void updateToNetworkObject() {
        if (this.isClientOwnPlayer()) {
            this.inputBasedSeed += this.tempSeed;
            this.tempSeed = 0;
        }

        this.skinManager.updateToNetworkObject();
        this.spawnData.updateToNetworkObject();
        this.buildModePosition.updateToNetworkObject();
        if (this.isOnServer()) {
            this.getNetworkObject().hasCreativeMode.set(this.isHasCreativeMode());
            if (this.flagSendUseCreative) {
                this.getNetworkObject().useCreativeMode.set(this.isUseCreativeMode(), true);
                this.flagSendUseCreative = false;
            }

            this.getNetworkObject().lastSpawnedThisSession.set(this.lastSpawnedThisSession);
            this.getNetworkObject().inputSeed.set(this.inputBasedSeed);
            this.getNetworkObject().useCargoMode.set(this.isUseCargoInventory());
            this.getNetworkObject().invisibility.set(this.invisibilityMode);
            this.getNetworkObject().id.set(this.getId());
            this.getNetworkObject().helmetSlot.set(this.getHelmetSlot());
            this.getNetworkObject().infiniteInventoryVolume.set(this.infiniteInventoryVolume);
            this.getNetworkObject().currentSectorType.set(this.currentSectorType.ordinal());
            this.getNetworkObject().credits.set(this.getCredits());
            this.getNetworkObject().tint.set(this.getTint());
            this.getNetworkObject().mineArmTimer.set(this.mineAutoArmSecs);
            this.getNetworkObject().health.set(this.getHealth());
            this.getNetworkObject().kills.set(this.kills);
            this.getNetworkObject().deaths.set(this.deaths);
            this.getNetworkObject().lastDeathNotSuicide.set(this.lastDeathNotSuicide);
            this.getNetworkObject().ping.set(this.ping);
            this.getNetworkObject().sectorId.set(this.currentSectorId);
            this.getNetworkObject().sectorPos.set(this.currentSector);
            this.getNetworkObject().health.set(this.health);
            boolean var1 = this.getNetworkObject().sittingState.getLongArray()[0] != (long)this.sittingOnId;
            this.getNetworkObject().sittingState.set(0, (long)this.sittingOnId);
            this.getNetworkObject().sittingState.set(1, ElementCollection.getIndex(this.sittingPos));
            this.getNetworkObject().sittingState.set(2, ElementCollection.getIndex(this.sittingPosTo));
            this.getNetworkObject().sittingState.set(3, ElementCollection.getIndex(this.sittingPosLegs));

            assert !var1 || this.getNetworkObject().sittingState.hasChanged() && this.getNetworkObject().isChanged();

            this.getNetworkObject().isAdminClient.set(((GameServerState)this.getState()).getController().isAdmin(this.getName()));
        }

        this.playerAiManager.updateToNetworkObject();
        this.factionController.updateToNetworkObject();
        if (this.isClientOwnPlayer()) {
            this.getNetworkObject().useCreativeMode.set(this.isUseCreativeMode(), true);
            this.getNetworkObject().mouseSwitched.forceClientUpdates();
            this.getNetworkObject().mouseSwitched.set(EngineSettings.C_MOUSE_BUTTON_SWITCH.isOn());
            this.getNetworkObject().selectedEntityId.forceClientUpdates();
            this.getNetworkObject().selectedAITargetId.forceClientUpdates();
            this.getNetworkObject().aquiredTargetId.forceClientUpdates();
            SimpleTransformableSendableObject var3 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedEntity();
            SimpleTransformableSendableObject var2 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSelectedAITarget();
            this.getNetworkObject().selectedEntityId.set(var3 == null ? -1 : var3.getId());
            this.getNetworkObject().selectedAITargetId.set(var2 == null ? -1 : var2.getId());
            this.getNetworkObject().aquiredTargetId.set(this.aquiredTarget == null ? -1 : this.aquiredTarget.getId());
        }

    }

    public boolean controls(ControllerStateUnit var1) {
        return this.controllerState.controls(var1);
    }

    public boolean controls(PlayerControllable var1, Vector3i var2) {
        return this.getControllerState().isOwnerControlling(var1, var2);
    }

    public void damage(float damage, Destroyable destroyable, Damager from) {
        assert this.isOnServer();
        //INSERTED CODE
        PlayerDamageEvent event = new PlayerDamageEvent(damage, destroyable, from, this);
        StarLoader.fireEvent(PlayerDamageEvent.class, event);
        if(event.isCanceled()){
            return;
        }
        ///

        if (!this.isGodMode() && this.isVulnerable() && this.isDamageable(from)) {
            if (damage > 0.0F) {
                from.sendHitConfirm((byte)3);
            }

            this.handleServerHealthAndCheckAliveOnServer(Math.max(0.0F, this.getHealth() - damage), from);
        } else {
            if (from != null && this.getState().getUpdateTime() > this.lastSentHitDeniedMessage + 2000L) {
                if (this.isGodMode()) {
                    from.sendServerMessage(new Object[]{315}, 3);
                    this.lastSentHitDeniedMessage = this.getState().getUpdateTime();
                    return;
                }

                if (this.isSpawnProtected()) {
                    from.sendServerMessage(new Object[]{316}, 3);
                    this.lastSentHitDeniedMessage = this.getState().getUpdateTime();
                }
            }

        }
    }

    public void heal(float var1, Destroyable var2, Damager var3) {
        if (!this.isGodMode() && this.isVulnerable()) {
            this.handleServerHealthAndCheckAliveOnServer(Math.min(this.getMaxHealth(), this.getHealth() + var1), var3);
        }

    }

    public float getMaxHealth() {
        return 120.0F;
    }

    public float getHealth() {
        return this.health;
    }

    public Vector3f getRight(Vector3f var1) {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            if (Controller.getCamera() != null && !Controller.getCamera().isStable()) {
                var1.set(this.lastRight);
                return var1;
            } else if (Controller.getCamera() instanceof InShipCamera) {
                return ((InShipCamera)Controller.getCamera()).getHelperCamera().getRight(var1);
            } else if (Controller.getCamera() != null && !(Controller.getCamera() instanceof ObjectViewerCamera)) {
                return Controller.getCamera().getRight(var1);
            } else {
                var1.set(this.lastRight);
                return var1;
            }
        } else {
            return GlUtil.getRightVector(var1, this.remoteCam);
        }
    }

    public Vector3f getUp(Vector3f var1) {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            if (Controller.getCamera() != null && !Controller.getCamera().isStable()) {
                var1.set(this.lastUp);
                return var1;
            } else if (Controller.getCamera() instanceof InShipCamera) {
                return ((InShipCamera)Controller.getCamera()).getHelperCamera().getUp(var1);
            } else if (Controller.getCamera() != null && !(Controller.getCamera() instanceof ObjectViewerCamera)) {
                return Controller.getCamera().getUp(var1);
            } else {
                var1.set(this.lastUp);
                return var1;
            }
        } else {
            return GlUtil.getUpVector(var1, this.remoteCam);
        }
    }

    public Vector3f getForward(Vector3f var1) {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            if (Controller.getCamera() != null && !Controller.getCamera().isStable()) {
                var1.set(this.lastForward);
                return var1;
            } else if (Controller.getCamera() instanceof InShipCamera) {
                return ((InShipCamera)Controller.getCamera()).getHelperCamera().getForward(var1);
            } else if (Controller.getCamera() != null && !(Controller.getCamera() instanceof ObjectViewerCamera)) {
                return Controller.getCamera().getForward(var1);
            } else {
                var1.set(this.lastForward);
                return var1;
            }
        } else {
            return GlUtil.getForwardVector(var1, this.remoteCam);
        }
    }

    public boolean isInvisibilityMode() {
        return this.invisibilityMode;
    }

    public boolean isOnServer() {
        return this.onServer;
    }

    public boolean isFactoryInUse() {
        return true;
    }

    public NetworkPlayer getNetworkObject() {
        return this.networkPlayerObject;
    }

    public boolean isHarvestingButton() {
        return this.isMouseButtonDown(EngineSettings.C_MOUSE_BUTTON_SWITCH.isOn() ? 0 : 1);
    }

    protected void onNoSlotFree(short var1, int var2) {
        if (this.getState().getUpdateTime() - this.lastMessage > 1000L) {
            this.sendServerMessage(new ServerMessage(new Object[]{317}, 3, this.getId()));
            this.lastMessage = this.getState().getUpdateTime();
        }

    }

    public void sendHitConfirm(byte var1) {
        if (this.isOnServer()) {
            this.getNetworkObject().hitNotifications.set(var1);
            this.lastHitConfirm = this.getState().getUpdateTime();
        }

    }

    public boolean isVulnerable() {
        return !this.isInTutorial() && !this.isGodMode() && !this.isSpawnProtected();
    }

    public void updateLocal(Timer var1) {
        super.updateLocal(var1);
        this.ruleEntityManager.update(var1);
        this.getShopsInDistance().clear();
        SimpleTransformableSendableObject var2;
        if ((var2 = this.getFirstControlledTransformableWOExc()) != null && var2 instanceof ShopperInterface) {
            this.getShopsInDistance().addAll(((ShopperInterface)var2).getShopsInDistance());
        }

        int var32;
        if (!this.isOnServer()) {
            for(var32 = 0; var32 < this.activeManualTrades.size(); ++var32) {
                ((ManualTrade)this.activeManualTrades.get(var32)).updateFromPlayerClient(this);
            }
        }

        while(!this.dropsIntoSpace.isEmpty()) {
            this.handleDragDrop((DragDrop)this.dropsIntoSpace.dequeue());
        }

        Vector3i var33;
        while(!this.isOnServer() && ((GameClientState)this.getState()).getController().getClientChannel() != null && !this.resetFowSystem.isEmpty()) {
            var33 = (Vector3i)this.resetFowSystem.dequeue();
            System.err.println("[CLIENT] Received FOW reset for " + this.getName());
            ((GameClientState)this.getState()).getController().getClientChannel().getGalaxyManagerClient().resetClientVisibilitySystem(var33);
        }

        if (!this.isClientOwnPlayer() && Controller.getCamera() instanceof InShipCamera) {
            this.getNetworkObject().adjustMode.forceClientUpdates();
            this.getNetworkObject().adjustMode.set(((InShipCamera)Controller.getCamera()).isInAdjustMode());
        }

        if (!this.isOnServer() && this.slotChangeUpdateDelay > 0.0F) {
            this.slotChangeUpdateDelay -= var1.getDelta();
            if (this.slotChangeUpdateDelay <= 0.0F) {
                this.getNetworkObject().shipControllerSlot.set(this.shipControllerSlotClient, true);
            }
        }

        this.buildModePosition.update(var1);
        this.handleReceivedSavedCoordinates();
        if (this.isOnServer()) {
            if (this.forcedEnterSector != null && this.getFirstControlledTransformableWOExc() == this.getAssingedPlayerCharacter()) {
                if (this.getSectorId() != this.forcedEnterSector.getSectorId()) {
                    try {
                        (new SectorSwitch(this.getFirstControlledTransformable(), this.forcedEnterSector.pos, 1)).execute((GameServerState)this.getState());
                    } catch (PlayerControlledTransformableNotFound var22) {
                        var22.printStackTrace();
                    } catch (IOException var23) {
                        var23.printStackTrace();
                    }
                }

                this.forcedEnterSector = null;
            }

            Sendable var3;
            SegmentPiece var5;
            if (this.forcedEnterUID != null && this.getAssingedPlayerCharacter() != null && this.getFirstControlledTransformableWOExc() == this.getAssingedPlayerCharacter() && (var3 = (Sendable)((GameServerState)this.getState()).getLocalAndRemoteObjectContainer().getUidObjectMap().get(this.forcedEnterUID)) instanceof Ship && ((Ship)var3).getSectorId() == this.getSectorId() && (var5 = ((Ship)var3).getSegmentBuffer().getPointUnsave(Ship.core)) != null) {
                this.getControllerState().requestControlServerAndSend(this.getAssingedPlayerCharacter(), (PlayerControllable)var5.getSegment().getSegmentController(), new Vector3i(), var5.getAbsolutePos(new Vector3i()), true);
                this.forcedEnterUID = null;
            }

            if (var1.currentTime - this.lastHitConfirm > 370L) {
                this.getNetworkObject().hitNotifications.set((byte)0);
            }

            if (this.createdClientChannelOnServer) {
                this.onClientChannelCreatedOnServer();
                this.createdClientChannelOnServer = false;
            }

            this.handleMetaBlueprintGive();
            this.handleCreateDockRequests();
        } else {
            this.setClientHitNotifaction(this.getNetworkObject().hitNotifications.getByte());
        }

        if (this.flagRequestCargoInv >= 0) {
            if (this.flagRequestCargoInv == 1) {
                this.setUseCargoInventory(true);
                this.getCargoInventoryIfActive();
            } else {
                this.setUseCargoInventory(false);
            }

            this.flagRequestCargoInv = -1;
        }

        this.spawnData.updateLocal(var1);
        if (this.getClientChannel() != null && this.getClientChannel().isConnectionReady()) {
            this.getPlayerChannelManager().update(var1);
        }

        int var35;
        if (this.isOnServer() && this.checkForOvercap && this.getAssingedPlayerCharacter() != null) {
            if (this.getPersonalInventory().isOverCapacity()) {
                BlockStorageMetaItem var34 = (BlockStorageMetaItem)MetaObjectManager.instantiate(MetaObjectType.BLOCK_STORAGE, (short)-1, true);
                this.addAllToStorageMetaItem(var34, this.getPersonalInventory());

                try {
                    var35 = this.getPersonalInventory().getFreeSlot(10, 2147483647);
                    this.getPersonalInventory().put(var35, var34);
                    this.getPersonalInventory().sendInventoryModification(var35);
                } catch (NoSlotFreeException var21) {
                    var21.printStackTrace();
                }

                System.err.println("[SERVER][PLAYERSTATE] " + this + " WAS OVER CAPACITY. ADDED INVENTORY ALL TO META ITEM");
                this.sendServerMessage(new ServerMessage(new Object[]{318}, 5, this.getId()));
            }

            this.checkForOvercap = false;
        }

        if (this.isClientOwnPlayer()) {
            if (Controller.getCamera() != null) {
                this.getNetworkObject().canRotate.forceClientUpdates();
                this.getNetworkObject().canRotate.set(Controller.getCamera().isStable());
            }

            if (this.tutorialCallClient != null) {
                if (this.tutorialCallClient.equals("TutorialBasics00Welcome")) {
                    if (((GameClientState)this.getState()).getController().getTutorialMode() == null || !(((GameClientState)this.getState()).getController().getTutorialMode().getMachine().getFsm().getCurrentState() instanceof TeleportToTutorialSector)) {
                        this.callTutorialClient(this.tutorialCallClient);
                    }
                } else {
                    this.callTutorialClient(this.tutorialCallClient);
                }
            }

            var33 = ((GameClientState)this.getState()).getController().getClientGameData().getWaypoint();
            this.getNetworkObject().waypoint.forceClientUpdates();
            if (var33 != null) {
                this.getNetworkObject().waypoint.set(var33);
            } else {
                this.getNetworkObject().waypoint.set(NO_WAYPOINT);
            }
        }

        this.setLastOrientation();
        if (this.clientChannel != null && this.clientChannel.getPlayerMessageController() != null) {
            this.clientChannel.getPlayerMessageController().update();
        }

        Iterator var4;
        if (this.isOnServer()) {
            this.skinManager.updateServer();
        } else {
            this.skinManager.updateOnClient();
            String var37;
            if (this.getNetworkObject().isAdminClient.get()) {
                var37 = this.controllerState.getUnits().toString();
            } else {
                StringBuilder var36;
                (var36 = new StringBuilder()).append("[");
                var4 = this.controllerState.getUnits().iterator();

                while(var4.hasNext()) {
                    ControllerStateUnit var45 = (ControllerStateUnit)var4.next();
                    var36.append(var45.playerState.getClass().getSimpleName());
                    var36.append(" | ");
                    var36.append(var45.playerControllable.getClass().getSimpleName());
                    if (var4.hasNext()) {
                        var36.append(", ");
                    }
                }

                var36.append("]");
                var37 = var36.toString();
            }

            AbstractScene.infoList.add("|CC " + var37);
        }

        this.handleBeingKilled();
        if (this.isClientOwnPlayer()) {
            if (this.health < this.lastHP) {
                this.onVesselHit((Sendable)null);
                this.lastHP = this.health;
            }

            try {
                this.handleBluePrintQueuesClient();
            } catch (EntityNotFountException var18) {
                var18.printStackTrace();
            } catch (IOException var19) {
                var19.printStackTrace();
            } catch (EntityAlreadyExistsException var20) {
                var20.printStackTrace();
            }
        }

        if (!this.simpleCommandQueue.isEmpty()) {
            synchronized(this.simpleCommandQueue) {
                while(!this.simpleCommandQueue.isEmpty()) {
                    SimpleCommand var38 = (SimpleCommand)this.simpleCommandQueue.dequeue();
                    this.executeSimpleCommand(var38);
                }
            }
        }

        if (!this.requestedreatureSpawns.isEmpty()) {
            synchronized(this.requestedreatureSpawns) {
                while(!this.requestedreatureSpawns.isEmpty()) {
                    CreatureSpawn var39 = (CreatureSpawn)this.requestedreatureSpawns.dequeue();
                    if (this.isOnServer()) {
                        GameServerState var40;
                        if ((var40 = (GameServerState)this.getState()).isAdmin(this.getName())) {
                            try {
                                var39.execute(var40);
                                this.sendServerMessage(new ServerMessage(new Object[]{319}, 1, this.getId()));
                            } catch (IOException var17) {
                                var17.printStackTrace();
                                this.sendServerMessage(new ServerMessage(new Object[]{320}, 3, this.getId()));
                            }
                        } else {
                            this.sendServerMessage(new ServerMessage(new Object[]{321}, 3, this.getId()));
                        }
                    }
                }
            }
        }

        if (!this.queuedModifactions.isEmpty()) {
            synchronized(this.queuedModifactions) {
                while(!this.queuedModifactions.isEmpty()) {
                    InventoryMultMod var41 = (InventoryMultMod)this.queuedModifactions.dequeue();
                    System.err.println("[PLAYER] SENDING QUEUED INVENTORY MOD: " + var41);
                    this.getNetworkObject().getInventoryMultModBuffer().add(new RemoteInventoryMultMod(var41, this.getNetworkObject()));
                }
            }
        }

        this.factionController.update(var1.currentTime);
        this.playerAiManager.update();
        this.playerConversationManager.updateOnActive(var1);
        int var6;
        int var44;
        if (this.isOnServer()) {
            GameServerState var42 = (GameServerState)this.getState();
            if (this.getHelmetSlot() >= 0 && this.getInventory().getType(this.getHelmetSlot()) != MetaObjectType.HELMET.type) {
                this.setHelmetSlot(-1);
            }

            if (var42.getUniverse().existsSector(this.currentSectorId)) {
                var42.activeSectors.add(this.currentSectorId);
            } else {
                System.err.println("[SERVER] Exception (CRITICAL) player " + this + " is in an unloaded sector (" + this.getCurrentSectorId() + ") attempting reload of " + this.getCurrentSector());

                try {
                    Sector var43;
                    if ((var43 = var42.getUniverse().getSector(this.getCurrentSector())) == null) {
                        throw new RuntimeException("Failed loading sector");
                    }

                    System.err.println("[SERVER] reloaded sector successfully. overwriting sector ids to " + var43.getId());
                    var4 = var42.getPlayerStatesByName().values().iterator();

                    while(var4.hasNext()) {
                        PlayerState var48 = (PlayerState)var4.next();
                        var6 = this.getCurrentSectorId();
                        if (var48.getCurrentSectorId() == var6) {
                            System.err.println("[SERVER] reloaded sector successfully. overwriting sector id for " + var48);
                            var48.setCurrentSectorId(var43.getId());
                        }
                    }
                } catch (IOException var28) {
                    var28.printStackTrace();
                    throw new RuntimeException(var28);
                }
            }

            if (!this.toDrop.isEmpty()) {
                synchronized(this.toDrop) {
                    while(!this.toDrop.isEmpty()) {
                        var44 = this.toDrop.removeInt(this.toDrop.size() - 1);
                        this.dropCreditsIntoSpace(var44);
                    }
                }
            }

            boolean var46 = false;
            if (this.serverClient == null) {
                this.serverClient = (RegisteredClientOnServer)var42.getClients().get(this.clientId);
                var46 = true;
            }

            if (this.serverClient != null) {
                this.ping = (int)this.serverClient.getProcessor().getPingTime();
                if (var46) {
                    this.ip = this.serverClient.getProcessor().getIp().substring(0, this.serverClient.getProcessor().getIp().indexOf(":"));
                    long var47 = System.currentTimeMillis();
                    this.getHosts().add(new PlayerInfoHistory(var47, this.ip, this.serverClient.getStarmadeName()));

                    while(this.getHosts().size() > (Integer)ServerConfig.PLAYER_HISTORY_BACKLOG.getCurrentState()) {
                        this.getHosts().remove(0);
                    }

                    this.spawnData.lastLogin = System.currentTimeMillis();
                }
            }

            try {
                this.handleBluePrintQueuesServer();
            } catch (EntityNotFountException var14) {
                var14.printStackTrace();
                throw new RuntimeException(var14);
            } catch (EntityAlreadyExistsException var15) {
                var15.printStackTrace();
                this.sendServerMessage(new ServerMessage(new Object[]{322}, 3, this.getId()));
            } catch (IOException var16) {
                var16.printStackTrace();
                throw new RuntimeException(var16);
            }

            this.checkIfDiedOnServer();
        }

        this.catalog.update();
        if (!this.basicTutorialStarted && this.isOnServer() && Sector.isTutorialSector(this.getCurrentSector()) && this.getAssingedPlayerCharacter() != null) {
            this.callTutorialServer("TutorialBasics00Welcome");
            this.basicTutorialStarted = true;
        }

        if (this.sectorChanged) {
            this.getFogOfWar().onSectorSwitch(this.lastSector, this.currentSector);
            if (this.isOnServer()) {
                try {
                    this.updateProximitySectors();
                } catch (IOException var13) {
                    var13.printStackTrace();
                    throw new RuntimeException(var13);
                }
            } else if (this.isClientOwnPlayer()) {
                this.lastVisitedSectors.add(this.lastSector);
                var32 = -1;

                label422:
                while(true) {
                    if (var32 >= 2) {
                        Iterator var49 = this.lastVisitedSectors.iterator();
                        var35 = this.lastVisitedSectors.size();

                        while(true) {
                            if (!var49.hasNext() || var35 <= 300) {
                                break label422;
                            }

                            var49.next();
                            var49.remove();
                            --var35;
                        }
                    }

                    for(var35 = -1; var35 < 2; ++var35) {
                        for(var44 = -1; var44 < 2; ++var44) {
                            Vector3i var50;
                            (var50 = new Vector3i(this.currentSector)).add(var44, var35, var32);
                            this.lastVisitedSectors.add(var50);
                        }
                    }

                    ++var32;
                }
            }

            var33 = new Vector3i(this.getCurrentSystem());
            StellarSystem.getPosFromSector(this.getCurrentSector(), this.getCurrentSystem());
            if (this.isOnServer() && !this.isInvisibilityMode() && !var33.equals(this.getCurrentSystem())) {
                try {
                    Faction var51;
                    StellarSystem var58;
                    if ((var58 = ((GameServerState)this.getState()).getUniverse().getStellarSystemFromStellarPos(this.getCurrentSystem())).getOwnerFaction() != 0 && var58.getOwnerFaction() != this.getFactionId() && (var51 = ((GameServerState)this.getState()).getFactionManager().getFaction(var58.getOwnerFaction())) != null) {
                        RType var52 = ((GameServerState)this.getState()).getFactionManager().getRelation(var51.getIdFaction(), this.getFactionId());
                        switch(var52) {
                            case ENEMY:
                                String var53 = StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_12, new Object[]{var58.getName(), var58.getPos()});
                                Object[] var7 = new Object[]{323, var58.getName(), var58.getPos()};
                                var51.broadcastMessage(var7, 2, (GameServerState)this.getState());
                                FactionNewsPost var8;
                                (var8 = new FactionNewsPost()).set(var51.getIdFaction(), Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_14, System.currentTimeMillis(), Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_15, var53, 0);
                                ((GameServerState)this.getState()).getFactionManager().addNewsPostServer(var8);
                                break;
                            case FRIEND:
                                var51.broadcastMessage(new Object[]{324, var58.getName(), var58.getPos()}, 1, (GameServerState)this.getState());
                                break;
                            case NEUTRAL:
                                var51.broadcastMessage(new Object[]{325, var58.getName(), var58.getPos()}, 1, (GameServerState)this.getState());
                        }
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

            this.sectorBlackHoleEffectStart = -1L;
            this.sectorChanged = false;
        }

        if (this.isOnServer()) {
            try {
                this.currentSectorType = ((GameServerState)this.getState()).getUniverse().getStellarSystemFromStellarPos(this.currentSystem).getSectorType(this.currentSector);

                assert ((GameServerState)this.getState()).getUniverse().getStellarSystemFromStellarPos(this.currentSystem) == ((GameServerState)this.getState()).getUniverse().getStellarSystemFromSecPos(this.currentSector);
            } catch (IOException var11) {
                var11.printStackTrace();
            }
        }

        this.handleReceivedInputEvents();

        try {
            this.getSkinUploadController().updateLocal();
            this.getShipUploadController().updateLocal();
            this.getControllerState().update(var1);
        } catch (ZipException var25) {
            var25.printStackTrace();
            if (this.isOnServer()) {
                ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{326}, 3);
            }
        } catch (Exception var26) {
            var26.printStackTrace();
            if (!this.isOnServer()) {
                throw new RuntimeException(var26);
            }
        }

        if (this.currentSectorType == SectorType.BLACK_HOLE) {
            try {
                this.getFirstControlledTransformable();
                this.handlePlayerInBlackHoleSystem();
            } catch (PlayerControlledTransformableNotFound var10) {
            }
        }

        if (this.isOnServer()) {
            while(!this.serverToSendMessages.isEmpty()) {
                ServerMessage var54 = (ServerMessage)this.serverToSendMessages.remove(0);
                this.getNetworkObject().messages.add(new RemoteServerMessage(var54, this.getNetworkObject()));
            }

            if (!this.creditModifications.isEmpty()) {
                long var59 = 0L;
                boolean var56;
                synchronized(this.creditModifications) {
                    var56 = !this.creditModifications.isEmpty();
                    var6 = 0;

                    while(true) {
                        if (var6 >= this.creditModifications.size()) {
                            this.creditModifications.clear();
                            break;
                        }

                        long var57 = this.creditModifications.getLong(var6);
                        var59 += var57;
                        ++var6;
                    }
                }

                long var55;
                if ((var55 = (long)this.getCredits() + var59) > 2147483647L) {
                    this.sendServerMessage(new ServerMessage(new Object[]{327}, 3, this.getId()));
                    var55 = 2147483647L;
                }

                this.setCredits((int)var55);
                if (var56) {
                    Starter.modManager.onPlayerCreditsChanged(this);
                }
            }
        }

        Starter.modManager.onPlayerUpdate(this, var1);
        if (this.oldSectorId != this.getCurrentSectorId()) {
            Starter.modManager.onPlayerSectorChanged(this);
            this.oldSectorId = this.getCurrentSectorId();
        }

        super.updateInventory();
        synchronized(this.getNetworkObject()) {
            this.updateToNetworkObject();
        }

        for(var32 = 0; var32 < this.wasMouseDown.length; ++var32) {
            this.wasMouseDown[var32] = this.isMouseButtonDown(var32);
        }

        if (var1.currentTime - this.lastLagReceived > 7000L) {
            this.currentLag = 0L;
        }

    }

    private void handleDragDrop(DragDrop var1) {
        try {
            int var2 = var1.slot;
            int var3 = var1.count;
            short var4 = var1.type;
            int var5 = var1.invId;
            long var6 = var1.parameter;
            Sendable var13;
            if ((var13 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var5)) != null && var13 instanceof InventoryHolder) {
                Inventory var14;
                if ((var14 = ((InventoryHolder)var13).getInventory(var6)).isLockedInventory()) {
                    if (this.isOnServer()) {
                        this.sendServerMessagePlayerError(new Object[]{328});
                    }

                } else {
                    short var19;
                    Sector var20;
                    if ((var19 = var14.getType(var2)) != 0 && (var20 = ((GameServerState)this.state).getUniverse().getSector(this.getCurrentSectorId())) != null) {
                        assert var14.checkVolume();

                        Vector3f var15;
                        Iterator var21;
                        InventorySlot var23;
                        int var25;
                        if (var19 == -32768 && var4 == -32768) {
                            System.err.println("[PLAYER] " + this + " DROPPING MULTISLOT");
                            var21 = var14.getSubSlots(var2).iterator();

                            while(var21.hasNext()) {
                                var25 = (var23 = (InventorySlot)var21.next()).count();
                                int var27 = var23.metaId;
                                SimpleTransformableSendableObject var16 = this.getFirstControlledTransformable();
                                System.err.println("[SERVER][PLAYER] " + this + " dropping item from " + var16);
                                TransformTimed var18 = var16.getWorldTransform();
                                var15 = new Vector3f(var18.origin);
                                Vector3f var17;
                                (var17 = this.getForward(new Vector3f())).scale(2.0F);
                                var15.add(var17);
                                var20.getRemoteSector().addItem(var15, var23.getType(), var27, var25);
                            }

                            assert var14.checkVolume();

                            var14.put(var2, (short)0, 0, -1);
                            var14.sendInventoryModification(var2);

                            assert var14.checkVolume();

                            return;
                        }

                        Vector3f var11;
                        if (var19 == -32768) {
                            assert var14.checkVolume();

                            System.err.println("[PLAYER] " + this + " DROPPING FROM MULTISLOT TYPE: " + var4 + "; count: " + var3);
                            var21 = var14.getSubSlots(var2).iterator();

                            while(var21.hasNext()) {
                                if ((var23 = (InventorySlot)var21.next()).getType() == var4) {
                                    var25 = var23.count();
                                    int var10000 = var23.metaId;
                                    var3 = Math.min(var3, var25);
                                    break;
                                }
                            }

                            var14.getSlot(var2).setMulti(var4, Math.max(0, var14.getCount(var2, var4) - var3));
                            SimpleTransformableSendableObject var22 = this.getFirstControlledTransformable();
                            System.err.println("[SERVER][PLAYER] " + this + " dropping item from " + var22);
                            TransformTimed var24 = var22.getWorldTransform();
                            Vector3f var26 = new Vector3f(var24.origin);
                            (var11 = this.getForward(new Vector3f())).scale(2.0F);
                            var26.add(var11);
                            var20.getRemoteSector().addItem(var26, var4, -1, var3);
                            var14.sendInventoryModification(var2);

                            assert var14.checkVolume();

                            return;
                        }

                        assert var14.checkVolume();

                        int var7 = var3 > 0 ? Math.min(var14.getCount(var2, (short)0), var3) : var14.getCount(var2, (short)0);
                        int var8 = var14.getMeta(var2);
                        if (((MetaObjectState)this.getState()).getMetaObjectManager().getObject(var8) != null && ((MetaObjectState)this.getState()).getMetaObjectManager().getObject(var8).isInventoryLocked(var14)) {
                            this.sendServerMessagePlayerError(new Object[]{329});
                            return;
                        }

                        System.err.println("[SERVER][PLAYER] DROPPING NORMAL SLOT " + ElementKeyMap.toString(var19) + ": " + var7);
                        if (var8 < 0 && var7 < var14.getCount(var2, var4)) {
                            var14.inc(var2, var4, -var7);
                        } else {
                            var14.put(var2, var19, 0, -1);
                        }

                        SimpleTransformableSendableObject var9 = this.getFirstControlledTransformable();
                        System.err.println("[SERVER][PLAYER] " + this + " dropping item from " + var9);
                        TransformTimed var10 = var9.getWorldTransform();
                        var11 = new Vector3f(var10.origin);
                        (var15 = this.getForward(new Vector3f())).scale(2.0F);
                        var11.add(var15);
                        var20.getRemoteSector().addItem(var11, var19, var8, var7);
                        var14.sendInventoryModification(var2);

                        assert var14.checkVolume();
                    }

                }
            } else {
                System.err.println("NO SENDABLE OR INVENTORY HOLDER " + var13);
            }
        } catch (PlayerControlledTransformableNotFound var12) {
            System.err.println("CANNOT DROP ITEM");
            var12.printStackTrace();
        }
    }

    private void handleCreateDockRequests() {
        assert this.isOnServer();

        if (!this.createDockRequests.isEmpty()) {
            synchronized(this.createDockRequests) {
                while(true) {
                    while(true) {
                        while(true) {
                            while(true) {
                                while(true) {
                                    while(!this.createDockRequests.isEmpty()) {
                                        CreateDockRequest var2 = (CreateDockRequest)this.createDockRequests.dequeue();
                                        if (this.getInventory().getOverallQuantity((short)663) > 0) {
                                            if (this.getInventory().getOverallQuantity((short)1) > 0) {
                                                if (!((GameServerState)this.getState()).existsEntity(EntityType.SHIP, var2.name)) {
                                                    if (!this.isInTestSector()) {
                                                        Sendable var3;
                                                        if ((var3 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getUidObjectMap().get(var2.core.uniqueIdentifierSegmentController)) != null && var3 instanceof SegmentController) {
                                                            SegmentController var16 = (SegmentController)var3;
                                                            var2.rail.setSegmentController(var16);
                                                            Iterator var4 = var16.railController.next.iterator();

                                                            while(var4.hasNext()) {
                                                                if (((RailRelation)var4.next()).rail.getAbsolutePos(new Vector3i()).equals(var2.rail.getAbsolutePos(new Vector3i()))) {
                                                                    this.sendServerMessagePlayerError(new Object[]{334});
                                                                    return;
                                                                }
                                                            }

                                                            IntOpenHashSet var17 = new IntOpenHashSet(2);
                                                            this.getInventory().decreaseBatch((short)663, 1, var17);
                                                            this.getInventory().decreaseBatch((short)1, 1, var17);
                                                            this.getInventory().sendInventoryModification(var17);
                                                            Transform var5;
                                                            (var5 = new Transform()).setIdentity();
                                                            float[] var18 = new float[16];
                                                            var5.getOpenGLMatrix(var18);
                                                            Ship var19 = EntityRequest.getNewShip(this.getState(), EntityType.SHIP.dbPrefix + var2.name, var16.getSectorId(), var2.name, var18, -2, -2, -2, 2, 2, 2, this.getUniqueIdentifier(), false);
                                                            var2.core.uniqueIdentifierSegmentController = var19.getUniqueIdentifier();
                                                            var2.docker.uniqueIdentifierSegmentController = var19.getUniqueIdentifier();
                                                            var2.core.setSegmentController(var19);
                                                            var2.docker.setSegmentController(var19);
                                                            Vector3i var20 = new Vector3i();
                                                            var2.docker.getAbsolutePos(var20);
                                                            Vector3i var6;
                                                            (var6 = new Vector3i()).sub(var2.docker.voidPos, var2.core.voidPos);
                                                            Oriencube var7 = (Oriencube)var2.rail.getAlgorithm((short)662);
                                                            Oriencube var8 = (Oriencube)var2.docker.getAlgorithm((short)662);
                                                            Oriencube var9 = (Oriencube)var2.core.getAlgorithm((short)662);
                                                            Matrix3f var10 = var7.getOrientationMatrixSwitched(new Matrix3f());
                                                            Matrix3f var11 = var8.getOrientationMatrixSwitched(new Matrix3f());
                                                            Matrix3f var12 = var9.getOrientationMatrixSwitched(new Matrix3f());
                                                            var7.getOrientationMatrixSwitched(new Matrix3f()).invert();
                                                            var8.getOrientationMatrixSwitched(new Matrix3f()).invert();
                                                            Matrix3f var13;
                                                            (var13 = var9.getOrientationMatrixSwitched(new Matrix3f())).invert();
                                                            System.err.println("CORE MAT: " + var9.getClass().getSimpleName() + "\n" + var12);
                                                            System.err.println("DOCKER MAT: " + var8.getClass().getSimpleName() + "\n" + var11);
                                                            System.err.println("RAIL MAT: " + var7.getClass().getSimpleName() + "\n" + var10);
                                                            Matrix3f var22 = new Matrix3f(var13);
                                                            Vector3f var21 = new Vector3f((float)var6.x, (float)var6.y, (float)var6.z);
                                                            var22.transform(var21);
                                                            Vector3f var23 = new Vector3f(Element.DIRECTIONSf[Element.switchLeftRight(var8.getOrientCubePrimaryOrientation())]);
                                                            Vector3f var25 = new Vector3f(Element.DIRECTIONSf[Element.switchLeftRight(var8.getOrientCubeSecondaryOrientation())]);
                                                            var13.transform(var23);
                                                            var13.transform(var25);
                                                            Vector3i var24 = new Vector3i(Math.round(var23.x), Math.round(var23.y), Math.round(var23.z));
                                                            Vector3i var27 = new Vector3i(Math.round(var25.x), Math.round(var25.y), Math.round(var25.z));
                                                            byte var29 = -1;
                                                            byte var33 = -1;

                                                            for(byte var35 = 0; var35 < 6; ++var35) {
                                                                if (Element.DIRECTIONSi[Element.switchLeftRight(var35)].equals(var24)) {
                                                                    var29 = var35;
                                                                }

                                                                if (Element.DIRECTIONSi[Element.switchLeftRight(var35)].equals(var27)) {
                                                                    var33 = var35;
                                                                }
                                                            }

                                                            assert var29 >= 0 : var24;

                                                            assert var33 >= 0 : var27;

                                                            System.err.println("NEW DIR: " + Element.getSideString(var29) + "; " + Element.getSideString(var33));
                                                            Oriencube var36 = Oriencube.getOrientcube(var29, var33);
                                                            boolean var26 = false;

                                                            for(int var28 = 0; var28 < 24; ++var28) {
                                                                if (BlockShapeAlgorithm.algorithms[5][var28].getClass().equals(var36.getClass())) {
                                                                    var2.docker.setOrientation((byte)var28);

                                                                    assert var2.docker.getAlgorithm().getClass().equals(var36.getClass()) : "Is " + var2.docker.getAlgorithm().getClass().getSimpleName() + "; " + var36.getClass().getSimpleName();

                                                                    var26 = true;
                                                                    break;
                                                                }
                                                            }

                                                            assert var26;

                                                            var27 = new Vector3i(Math.round(var21.x) + 16, Math.round(var21.y) + 16, Math.round(var21.z) + 16);
                                                            var2.docker.voidPos.set(var27);
                                                            var2.core.voidPos.set(16, 16, 16);
                                                            var6 = new Vector3i();
                                                            Segment.getSegmentIndexFromSegmentElement(var27.x, var27.y, var27.z, var6);
                                                            var6.scale(32);
                                                            Vector3b var30 = new Vector3b();
                                                            Segment.getElementIndexFrom(var27.x, var27.y, var27.z, var30);
                                                            RemoteSegment var31;
                                                            (var31 = new RemoteSegment(var19)).setSegmentData(new SegmentDataIntArray(var19.getState() instanceof ClientStateInterface));
                                                            var31.getSegmentData().setSegment(var31);

                                                            try {
                                                                var31.getSegmentData().setInfoElementUnsynched((byte)Ship.core.x, (byte)Ship.core.y, (byte)Ship.core.z, (short)1, true, var31.getAbsoluteIndex((byte)8, (byte)8, (byte)8), var19.getState().getUpdateTime());
                                                                var31.setLastChanged(System.currentTimeMillis());
                                                                if (!var31.pos.equals(var6)) {
                                                                    System.err.println("[SERVER][CREATEDOCKING] adding additional segment for docker(" + var27 + "): " + var6);
                                                                    RemoteSegment var32;
                                                                    (var32 = new RemoteSegment(var19)).setSegmentData(new SegmentDataIntArray(var19.getState() instanceof ClientStateInterface));
                                                                    var32.getSegmentData().setSegment(var32);
                                                                    var32.getSegmentData().setInfoElementUnsynched(var30.x, var30.y, var30.z, (short)663, var2.docker.getOrientation(), (byte)(var2.docker.isActive() ? 1 : 0), true, var32.getAbsoluteIndex(var30.x, var30.y, var30.z), var19.getState().getUpdateTime());
                                                                    var32.setPos(var6);
                                                                    var32.setLastChanged(System.currentTimeMillis());
                                                                    var19.getSegmentBuffer().addImmediate(var32);
                                                                    var19.getSegmentBuffer().updateBB(var32);
                                                                } else {
                                                                    var31.getSegmentData().setInfoElementUnsynched(var30.x, var30.y, var30.z, (short)663, var2.docker.getOrientation(), (byte)(var2.docker.isActive() ? 1 : 0), true, var31.getAbsoluteIndex(var30.x, var30.y, var30.z), var19.getState().getUpdateTime());
                                                                }
                                                            } catch (SegmentDataWriteException var14) {
                                                                throw new RuntimeException("this should be already normal chunk", var14);
                                                            }

                                                            var19.getSegmentBuffer().addImmediate(var31);
                                                            var19.getSegmentBuffer().updateBB(var31);

                                                            assert var2.docker.getType() == 663 && var19.getSegmentBuffer().getPointUnsave(var2.docker.getAbsoluteIndex()).getType() == var2.docker.getType();

                                                            assert var2.rail.uniqueIdentifierSegmentController.equals(var16.getUniqueIdentifier());

                                                            assert var2.rail.getType() != 0 && var16.getSegmentBuffer().getPointUnsave(var2.rail.getAbsoluteIndex()).getType() == var2.rail.getType();

                                                            RailRequest var34;
                                                            (var34 = var19.railController.getRailRequest(var2.docker, var2.rail, var20, (Vector3i)null, DockingPermission.PUBLIC)).fromtag = true;
                                                            var34.sentFromServer = false;
                                                            var19.railController.railRequestCurrent = var34;
                                                            var19.setFactionId(var16.getFactionId());
                                                            var19.setFactionRights(var16.getFactionRights());
                                                            var19.initialize();
                                                            if (var16.isVirtualBlueprint()) {
                                                                var19.setVirtualBlueprintRecursive(true);
                                                            }

                                                            ((GameServerState)this.getState()).getController().getSynchController().addNewSynchronizedObjectQueued(var19);
                                                        } else {
                                                            this.sendServerMessagePlayerError(new Object[]{335});
                                                        }
                                                    } else {
                                                        this.sendServerMessagePlayerError(new Object[]{333});
                                                    }
                                                } else {
                                                    this.sendServerMessagePlayerError(new Object[]{332});
                                                }
                                            } else {
                                                this.sendServerMessagePlayerError(new Object[]{331});
                                            }
                                        } else {
                                            this.sendServerMessagePlayerError(new Object[]{330});
                                        }
                                    }

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        return "PlS[" + this.getName() + " " + (this.starmadeName != null ? "[" + this.starmadeName + "]" + (this.upgradedAccount ? "*" : "") : "") + "; id(" + this.getId() + ")(" + this.clientId + ")f(" + this.getFactionId() + ")]";
    }

    public AbstractCharacter<? extends AbstractOwnerState> getAbstractCharacterObject() {
        return this.getAssingedPlayerCharacter();
    }

    public NetworkInventoryInterface getInventoryNetworkObject() {
        return this.getNetworkObject();
    }

    public void onFiredWeapon(Weapon var1) {
        if (this.isSpawnProtected()) {
            this.sendServerMessage(new ServerMessage(new Object[]{336}, 3, this.getId()));
            this.lastSpawnedThisSession = 0L;
        }

    }

    public void updateToFullNetworkObject() {
        assert this.getId() >= 0;

        assert this.getClientId() >= 0;

        assert this.getState().getId() >= 0;

        this.getNetworkObject().id.set(this.getId());
        this.getNetworkObject().clientId.set(this.getClientId());
        this.getNetworkObject().playerName.set(this.name);
        this.getNetworkObject().credits.set(this.getCredits());
        this.getNetworkObject().health.set(this.getHealth());
        this.getNetworkObject().kills.set(this.kills);
        this.getNetworkObject().deaths.set(this.deaths);
        this.getNetworkObject().lastDeathNotSuicide.set(this.lastDeathNotSuicide);
        this.getNetworkObject().ping.set(this.ping);
        this.getNetworkObject().currentSectorType.set(this.currentSectorType.ordinal());
        this.getNetworkObject().sectorId.set(this.currentSectorId);
        this.getNetworkObject().sectorPos.set(this.currentSector);
        this.getNetworkObject().upgradedAccount.set(this.isUpgradedAccount());
        this.getNetworkObject().tint.set(this.getTint());
        this.getNetworkObject().dbId.set(this.dbId);
        this.getNetworkObject().infiniteInventoryVolume.set(this.infiniteInventoryVolume);
        this.getNetworkObject().personalSector.set(this.personalSector);
        this.getNetworkObject().testSector.set(this.testSector);
        this.getNetworkObject().lastSpawnedThisSession.set(this.lastSpawnedThisSession);
        boolean var1 = this.getNetworkObject().sittingState.getLongArray()[0] != (long)this.sittingOnId;
        this.getNetworkObject().sittingState.set(0, (long)this.sittingOnId);
        this.getNetworkObject().sittingState.set(1, ElementCollection.getIndex(this.sittingPos));
        this.getNetworkObject().sittingState.set(2, ElementCollection.getIndex(this.sittingPosTo));
        this.getNetworkObject().sittingState.set(3, ElementCollection.getIndex(this.sittingPosLegs));
        if (this.cargoInventoryBlock != null) {
            this.getNetworkObject().cargoInventoryChange.add(new RemoteSegmentControllerBlock(this.cargoInventoryBlock, this.isOnServer()));
        }

        assert !var1 || this.getNetworkObject().sittingState.hasChanged() && this.getNetworkObject().isChanged();

        this.getNetworkObject().invisibility.set(this.invisibilityMode);
        this.factionController.updateToFullNetworkObject();
        this.playerAiManager.updateToFullNetworkObject();
        this.skinManager.updateToNetworkObject();
        this.spawnData.updateToFullNetworkObject();
        this.buildModePosition.updateToFullNetworkObject();
        super.updateToFullNetworkObject();
        this.getControllerState().sendAll();
        this.getNetworkObject().setChanged(true);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int var1) {
        assert var1 != 0;

        this.id = var1;
    }

    public String getConversationScript() {
        return null;
    }

    public void setInvisibilityMode(boolean var1) {
        this.invisibilityMode = var1;
    }

    private void dieOnServer(Damager var1) {
        if (this.alive) {
            this.dieList.add(var1);
            Starter.modManager.onPlayerKilled(this, var1);
            if (this.isInTutorial() || this.isInTestSector()) {
                this.loadInventoryBackupServer();

                try {
                    Sector var2 = ((GameServerState)this.getState()).getUniverse().getSector(new Vector3i((Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_X.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Y.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Z.getCurrentState()));
                    SectorSwitch var6 = new SectorSwitch(this.getFirstControlledTransformable(), var2.pos, 1);
                    ((GameServerState)this.getState()).getSectorSwitches().add(var6);
                } catch (IOException var4) {
                    var4.printStackTrace();
                } catch (PlayerControlledTransformableNotFound var5) {
                    var5.printStackTrace();
                }
            }

            Faction var7;
            if ((var7 = ((GameServerState)this.getState()).getFactionManager().getFaction(this.getFactionController().getFactionId())) != null) {
                if (!this.isFactionPointProtected()) {
                    try {
                        if (((GameServerState)this.getState()).getUniverse().getSector(this.currentSector).isNoFPLoss()) {
                            this.sendServerMessage(new ServerMessage(new Object[]{337}, 1, this.getId()));
                        } else {
                            var7.onPlayerDied(this, var1);
                        }
                    } catch (IOException var3) {
                        var3.printStackTrace();
                    }
                } else {
                    System.err.println("[SERVER] Player " + this + " didn't lose faction points. He is protected against faction point loss");
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{338, this.getName()}, 3);
                }
            }

            if (var1 != this && (float)(System.currentTimeMillis() - this.lastDeathNotSuicide) > FactionPointsGeneralConfig.FACTION_POINT_DEATH_PROTECTION_MIN * 60.0F * 1000.0F) {
                System.err.println("[PLAYER][DEATH] last faction point death not suicide " + this.lastDeathNotSuicide + " set to " + System.currentTimeMillis());
                this.lastDeathNotSuicide = System.currentTimeMillis();
            }

            this.alive = false;
            if (this.isOnServer() && !this.alive) {
                System.err.println("Revived PlayerState " + this);
                this.health = 120.0F;
                this.alive = true;
            }

        }
    }

    public void dropCreditsIntoSpace(int var1) {
        assert this.isOnServer();

        if (var1 > 0) {
            try {
                TransformTimed var2 = this.getFirstControlledTransformable().getWorldTransform();
                Sector var3 = ((GameServerState)this.state).getUniverse().getSector(this.getCurrentSectorId());
                Vector3f var6 = new Vector3f(var2.origin);
                Vector3f var4;
                (var4 = this.getForward(new Vector3f())).scale(2.0F);
                var6.add(var4);
                var1 = Math.min(this.getCredits(), var1);
                this.modCreditsServer((long)(-var1));
                var3.getRemoteSector().addItem(var6, (short)-2, -1, var1);
                return;
            } catch (PlayerControlledTransformableNotFound var5) {
                System.err.println("[SERVER][PLAYERSTATE] CANNOT DROP CREDITS: no transformable for player found");
                var5.printStackTrace();
            }
        }

    }

    public boolean spawnCrew() {
        if (this.getPlayerAiManager().getCrew().size() < 5) {
            try {
                CreatureSpawn var1 = new CreatureSpawn(new Vector3i(this.getCurrentSector()), new Transform(this.getFirstControlledTransformable().getWorldTransform()), "NoName", CreatureType.CHARACTER) {
                    public void initAI(AIGameCreatureConfiguration<?, ?> var1) {
                        try {
                            assert var1 != null;

                            var1.get(Types.ORIGIN_X).switchSetting("-2147483648", false);
                            var1.get(Types.ORIGIN_Y).switchSetting("-2147483648", false);
                            var1.get(Types.ORIGIN_Z).switchSetting("-2147483648", false);
                            var1.get(Types.ROAM_X).switchSetting("16", false);
                            var1.get(Types.ROAM_Y).switchSetting("3", false);
                            var1.get(Types.ROAM_Z).switchSetting("16", false);
                            var1.get(Types.ORDER).switchSetting("Idling", false);
                            var1.get(Types.OWNER).switchSetting(PlayerState.this.getUniqueIdentifier(), false);
                        } catch (StateParameterNotFoundException var2) {
                            var2.printStackTrace();
                        }

                        System.err.println("[AISPAWN] adding to crew");
                        PlayerState.this.getPlayerAiManager().addAI(new UnloadedAiContainer((AiInterface)((CreatureAIEntity)var1.getAiEntityState()).getEntity()));
                        ((AICreature)((CreatureAIEntity)var1.getAiEntityState()).getEntity()).setFactionId(PlayerState.this.getFactionId());
                    }
                };
                ((GameServerState)this.state).getController().queueCreatureSpawn(var1);
                System.err.println("[SimpleCommand] [SUCCESS] Spawning creature");
                return true;
            } catch (PlayerControlledTransformableNotFound var2) {
                var2.printStackTrace();
            }
        }

        return false;
    }

    public void loadInventoryBackupServer() {
        assert this.isOnServer();

        if (this.mainInventoryBackup != null) {
            IntOpenHashSet var1 = this.inventory.getAllSlots();
            this.inventory.clear();
            this.inventory.fromTagStructure(this.mainInventoryBackup);
            this.inventory.sendAllWithExtraSlots(var1);
            System.err.println("[SERVER][PLAYERSTATE] " + this + " INVENTORY RESTORED!");
            this.mainInventoryBackup = null;
        }

        if (this.capsuleRefinerInventoryBackup != null) {
            this.personalFactoryInventoryCapsule.clear();
            this.personalFactoryInventoryCapsule.fromTagStructure(this.capsuleRefinerInventoryBackup);
            this.personalFactoryInventoryCapsule.sendAll();
            this.capsuleRefinerInventoryBackup = null;
        }

        if (this.microInventoryBackup != null) {
            this.personalFactoryInventoryMicro.clear();
            this.personalFactoryInventoryMicro.fromTagStructure(this.microInventoryBackup);
            this.personalFactoryInventoryMicro.sendAll();
            this.microInventoryBackup = null;
        }

        if (this.factoryInventoryBackup != null) {
            this.personalFactoryInventoryMacroBlock.clear();
            this.personalFactoryInventoryMacroBlock.fromTagStructure(this.factoryInventoryBackup);
            this.personalFactoryInventoryMacroBlock.sendAll();
            this.factoryInventoryBackup = null;
        }

    }

    public void invBackUpfromTag(Tag var1) {
        if (var1.getType() == Type.STRUCT) {
            Tag[] var2 = (Tag[])var1.getValue();
            this.inventory.clear();
            this.inventory.fromTagStructure(var2[0]);
            this.personalFactoryInventoryCapsule.clear();
            this.personalFactoryInventoryCapsule.fromTagStructure(var2[1]);
            this.personalFactoryInventoryMicro.clear();
            this.personalFactoryInventoryMicro.fromTagStructure(var2[2]);
            this.personalFactoryInventoryMacroBlock.clear();
            this.personalFactoryInventoryMacroBlock.fromTagStructure(var2[3]);
            System.err.println("[SERVER] Player: " + this + " had backup inventory (used for instances) still. restored the backup!");
        }

    }

    public Tag invBackUpToTag() {
        return this.mainInventoryBackup != null ? new Tag(Type.STRUCT, (String)null, new Tag[]{this.mainInventoryBackup, this.capsuleRefinerInventoryBackup, this.microInventoryBackup, this.factoryInventoryBackup, FinishTag.INST}) : new Tag(Type.BYTE, (String)null, (byte)0);
    }

    public void instantiateInventoryServer(boolean var1) {
        assert this.isOnServer();

        this.mainInventoryBackup = this.inventory.toTagStructure();
        this.capsuleRefinerInventoryBackup = this.personalFactoryInventoryCapsule.toTagStructure();
        this.microInventoryBackup = this.personalFactoryInventoryMicro.toTagStructure();
        this.factoryInventoryBackup = this.personalFactoryInventoryMacroBlock.toTagStructure();
        if (var1) {
            IntOpenHashSet var2 = new IntOpenHashSet();
            this.inventory.clear(var2);
            this.inventory.sendInventoryModification(var2);
            var2 = new IntOpenHashSet();
            this.personalFactoryInventoryCapsule.clear(var2);
            this.personalFactoryInventoryCapsule.sendInventoryModification(var2);
            var2 = new IntOpenHashSet();
            this.personalFactoryInventoryMicro.clear(var2);
            this.personalFactoryInventoryMicro.sendInventoryModification(var2);
            var2 = new IntOpenHashSet();
            this.personalFactoryInventoryMacroBlock.clear(var2);
            this.personalFactoryInventoryMacroBlock.sendInventoryModification(var2);
        }

    }

    public PlayerStateSpawnData getSpawn() {
        return this.spawnData;
    }

    private void executeSimpleCommand(SimpleCommand<?> var1) {
        int var2;
        if ((var2 = var1.getCommand()) < SimplePlayerCommands.values().length && var2 >= 0) {
            SimplePlayerCommands var29 = SimplePlayerCommands.values()[var2];
            System.err.println("[SERVER] executing simple command: " + var29.name());
            int var5;
            String var7;
            int var9;
            int var10;
            int var11;
            int var12;
            Sendable var27;
            IntOpenHashSet var28;
            InventoryHolder var32;
            int var35;
            Inventory var38;
            Sendable var40;
            String var43;
            BlockStorageMetaItem var44;
            Sector var47;
            SectorSwitch var50;
            short var51;
            int var53;
            MetaObject var54;
            short var55;
            int var56;
            String var58;
            long var59;
            MetaObject var60;
            Sendable var61;
            Sendable var67;
            int var76;
            long var85;
            Vector3i var88;
            switch(var29) {
                case END_TUTORIAL:
                    if (this.isInTutorial()) {
                        this.loadInventoryBackupServer();

                        try {
                            if (this.spawnData.preSpecialSector == null) {
                                var47 = ((GameServerState)this.getState()).getUniverse().getSector(new Vector3i((Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_X.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Y.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Z.getCurrentState()));
                            } else {
                                var47 = ((GameServerState)this.getState()).getUniverse().getSector(this.spawnData.preSpecialSector);
                            }

                            System.err.println("[SERVER] Ending tutorial. Respawning in sector: " + var47 + ". IsDefault: " + (this.spawnData.preSpecialSector == null));
                            var50 = new SectorSwitch(this.getFirstControlledTransformable(), var47.pos, 1);
                            if (this.spawnData.preSpecialSectorTransform != null) {
                                var50.sectorSpaceTransform = this.spawnData.preSpecialSectorTransform;
                            }

                            ((GameServerState)this.getState()).getSectorSwitches().add(var50);
                            return;
                        } catch (IOException var23) {
                            var23.printStackTrace();
                            return;
                        } catch (PlayerControlledTransformableNotFound var24) {
                            var24.printStackTrace();
                            return;
                        }
                    }
                    break;
                case END_SHIPYARD_TEST:
                    if (this.isInTestSector()) {
                        this.loadInventoryBackupServer();

                        try {
                            if (this.spawnData.preSpecialSector == null) {
                                var47 = ((GameServerState)this.getState()).getUniverse().getSector(new Vector3i((Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_X.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Y.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Z.getCurrentState()));
                            } else {
                                var47 = ((GameServerState)this.getState()).getUniverse().getSector(this.spawnData.preSpecialSector);
                            }

                            System.err.println("[SERVER] Ending shipyard Test. Respawning in sector: " + var47 + ". IsDefault: " + (this.spawnData.preSpecialSector == null));
                            var50 = new SectorSwitch(this.getFirstControlledTransformable(), var47.pos, 1);
                            if (this.spawnData.preSpecialSectorTransform != null) {
                                var50.sectorSpaceTransform = this.spawnData.preSpecialSectorTransform;
                            }

                            ((GameServerState)this.getState()).getSectorSwitches().add(var50);
                            return;
                        } catch (IOException var21) {
                            var21.printStackTrace();
                            return;
                        } catch (PlayerControlledTransformableNotFound var22) {
                            var22.printStackTrace();
                            return;
                        }
                    }
                    break;
                case BACKUP_INVENTORY:
                    Boolean var45 = (Boolean)var1.getArgs()[0];
                    this.instantiateInventoryServer(var45);
                    return;
                case RESTORE_INVENTORY:
                    this.loadInventoryBackupServer();
                    return;
                case DESTROY_TUTORIAL_ENTITY:
                    var43 = (String)var1.getArgs()[0];
                    if (this.isInTutorial()) {
                        Iterator var39 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                        while(var39.hasNext()) {
                            Sendable var91;
                            if ((var91 = (Sendable)var39.next()) instanceof SimpleTransformableSendableObject && ((SimpleTransformableSendableObject)var91).getUniqueIdentifier() != null && ((SimpleTransformableSendableObject)var91).getUniqueIdentifier().startsWith(var43) && ((SimpleTransformableSendableObject)var91).getSectorId() == this.getCurrentSectorId()) {
                                ((SimpleTransformableSendableObject)var91).markForPermanentDelete(true);
                                ((SimpleTransformableSendableObject)var91).setMarkedForDeleteVolatile(true);
                            }
                        }

                        return;
                    }

                    try {
                        throw new IllegalArgumentException("Player " + this + " tried to destroy an entity while not in tutorial");
                    } catch (Exception var20) {
                        var20.printStackTrace();
                        ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{340, this}, 3);
                        return;
                    }
                case WARP_TO_TUTORIAL_SECTOR:
                    Vector3i var37 = new Vector3i();
                    this.spawnData.preSpecialSector = new Vector3i(this.currentSector);

                    try {
                        SimpleTransformableSendableObject var79 = this.getFirstControlledTransformable();
                        this.spawnData.preSpecialSectorTransform = new Transform(var79.getWorldTransform());
                    } catch (PlayerControlledTransformableNotFound var19) {
                        var7 = null;
                        var19.printStackTrace();
                    }

                    for(var53 = 1; var53 < 16; var53 += 2) {
                        for(var56 = 1; var56 < 16; var56 += 2) {
                            for(var9 = 1; var9 < 16; var9 += 2) {
                                var10 = var9 + 2080000000;
                                var11 = var56 + 2080000000;
                                var12 = var53 + 2080000000;
                                var37.set(var10, var11, var12);
                                if (((GameServerState)this.getState()).getUniverse().getSectorWithoutLoading(var37) == null) {
                                    try {
                                        SectorUtil.importSectorFullDir("./data/prefabSectors", "tutorial-v2.0.smsec", var37, (GameServerState)this.state);
                                        SectorSwitch var97 = new SectorSwitch(this.getAssingedPlayerCharacter(), var37, 1);
                                        ((GameServerState)this.getState()).getSectorSwitches().add(var97);
                                        return;
                                    } catch (IOException var17) {
                                        var17.printStackTrace();
                                    } catch (SQLException var18) {
                                        var18.printStackTrace();
                                    }

                                    this.sendServerMessage(new ServerMessage(new Object[]{341}, 3, this.getId()));
                                    return;
                                }
                            }
                        }
                    }

                    return;
                case SEARCH_LAST_ENTERED_SHIP:
                    this.searchForLastEnteredEntity();
                    return;
                case HIRE_CREW:
                    this.spawnCrew();
                    return;
                case SIT_DOWN:
                    this.sittingOnId = (Integer)var1.getArgs()[0];
                    if (this.sittingOnId < 0) {
                        System.err.println("[SERVER] " + this + "standing up");
                        this.sittingStarted = 0L;
                        this.sittingUIDServer = "none";
                        return;
                    }

                    long var71 = (Long)var1.getArgs()[1];
                    long var83 = (Long)var1.getArgs()[2];
                    var85 = (Long)var1.getArgs()[3];
                    this.sittingStarted = System.currentTimeMillis();
                    ElementCollection.getPosFromIndex(var71, this.sittingPos);
                    ElementCollection.getPosFromIndex(var83, this.sittingPosTo);
                    ElementCollection.getPosFromIndex(var85, this.sittingPosLegs);
                    System.err.println("[SERVER] " + this + " sitting down " + this.sittingPos + " " + this.sittingPosTo);
                    return;
                case PUT_ON_HELMET:
                    System.err.println("HELMETCOMMAND: BuildSlot: " + this.getSelectedBuildSlot() + "; cur: " + this.getHelmetSlot());
                    if (System.currentTimeMillis() - this.lastHelmetCommand > 1000L) {
                        if (this.getSelectedBuildSlot() == this.getHelmetSlot() && this.getHelmetSlot() >= 0 && this.getInventory().getType(this.getHelmetSlot()) == MetaObjectType.HELMET.type) {
                            System.err.println("[SERVER] " + this + " TAKE OFF HELMET");
                            this.setHelmetSlot(-1);
                        } else {
                            System.err.println("[SERVER] " + this + " PUT ON HELMET");
                            this.setHelmetSlot(this.getSelectedBuildSlot());
                        }
                    }

                    this.lastHelmetCommand = System.currentTimeMillis();
                    return;
                case REPAIR_STATION:
                    var53 = (Integer)var1.getArgs()[0];
                    var58 = (String)var1.getArgs()[1];
                    if ((var67 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var53)) != null && var67 instanceof SpaceStation) {
                        SpaceStation var84;
                        var85 = (var84 = (SpaceStation)var67).getElementClassCountMap().getPrice();
                        if ((long)this.getCredits() >= var85) {
                            this.modCreditsServer(-var85);
                            var84.setScrap(false);
                            var84.setRealName(var58);
                            return;
                        }

                        this.sendServerMessage(new ServerMessage(new Object[]{342}, 3, this.getId()));
                        return;
                    }
                    break;
                case FAILED_TO_JOIN_CHAT_INVALLID_PASSWD:
                    this.getPlayerChannelManager().handleFailedJoinInvalidPassword((String)var1.getArgs()[0]);
                    return;
                case CLIENT_TO_SERVER_LOG:
                    LogUtil.log().fine((String)var1.getArgs()[0]);
                    return;
                case REBOOT_STRUCTURE:
                    var53 = (Integer)var1.getArgs()[0];
                    boolean var70 = (Boolean)var1.getArgs()[1];
                    if ((var67 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var53)) != null && var67 instanceof SegmentController) {
                        if (var70) {
                            long var80;
                            if ((var80 = ((SegmentController)var67).getHpController().getShopRebootCost()) > (long)this.getCredits()) {
                                this.sendServerMessagePlayerError(new Object[]{343});
                                return;
                            }

                            this.setCredits((int)((long)this.getCredits() - var80));
                        }

                        ((SegmentController)var67).getHpController().reboot(var70);
                        return;
                    }
                    break;
                case REBOOT_STRUCTURE_REQUEST_TIME:
                    var53 = (Integer)var1.getArgs()[0];
                    if ((var61 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var53)) != null && var61 instanceof SegmentController) {
                        ((SegmentController)var61).getHpController().setRebootTimeServerForced(((SegmentController)var61).getHpController().getRebootTimeMS());
                        return;
                    }
                    break;
                case SPAWN_SHOPKEEP:
                    var53 = (Integer)var1.getArgs()[0];
                    if ((var61 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var53)) != null && var61 instanceof ShopSpaceStation) {
                        ShopSpaceStation var72;
                        Iterator var73 = (var72 = (ShopSpaceStation)var61).getAttachedAffinity().iterator();

                        do {
                            if (!var73.hasNext()) {
                                Vector3f var77 = new Vector3f(0.0F, -7.0F, 0.0F);
                                var72.getWorldTransform().transform(var77);
                                Transform var81;
                                (var81 = new Transform()).setIdentity();
                                var81.origin.set(var77);
                                CreatureSpawn var92 = new CreatureSpawn(new Vector3i(this.getCurrentSector()), var81, "ShopKeep", CreatureType.CHARACTER) {
                                    public void initAI(AIGameCreatureConfiguration<?, ?> var1) {
                                        try {
                                            assert var1 != null;

                                            var1.get(Types.ORIGIN_X).switchSetting("0", false);
                                            var1.get(Types.ORIGIN_Y).switchSetting("-7", false);
                                            var1.get(Types.ORIGIN_Z).switchSetting("0", false);
                                            var1.get(Types.ROAM_X).switchSetting("3", false);
                                            var1.get(Types.ROAM_Y).switchSetting("1", false);
                                            var1.get(Types.ROAM_Z).switchSetting("3", false);
                                            var1.get(Types.ORDER).switchSetting("Roaming", false);
                                            ((AICreature)((CreatureAIEntity)var1.getAiEntityState()).getEntity()).setFactionId(-2);
                                        } catch (StateParameterNotFoundException var2) {
                                            var2.printStackTrace();
                                        }
                                    }
                                };
                                ((GameServerState)this.state).getController().queueCreatureSpawn(var92);
                                return;
                            }
                        } while(((AICreature)var73.next()).getFactionId() != -2);

                        this.sendServerMessagePlayerError(new Object[]{345});
                        return;
                    }
                    break;
                case SET_FACTION_RANK_ON_OBJ:
                    var53 = (Integer)var1.getArgs()[0];
                    byte var63 = (Byte)var1.getArgs()[1];
                    if ((var67 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var53)) != null && var67 instanceof SegmentController) {
                        ((SegmentController)var67).setRankRecursive(var63, this, true);
                        return;
                    }
                    break;
                case SCAN:
                    var53 = (Integer)var1.getArgs()[0];
                    if ((var61 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var53)) != null && var61 instanceof ManagedSegmentController && ((ManagedSegmentController)var61).getManagerContainer() instanceof ScannerManagerInterface) {
                        ((ScannerElementManager)((ScannerManagerInterface)((ManagedSegmentController)var61).getManagerContainer()).getScanner().getElementManager()).executeScanOnServer(this);
                        return;
                    }

                    assert false;
                    break;
                case ADD_BLUEPRINT_META_ALL:
                    var53 = (Integer)var1.getArgs()[0];
                    var54 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    if (this.getInventory().isLockedInventory()) {
                        this.sendServerMessagePlayerError(new Object[]{346});
                        return;
                    }

                    if (var54 != null && var54 instanceof BlueprintMetaItem) {
                        BlueprintMetaItem var66 = (BlueprintMetaItem)var54;

                        try {
                            BlueprintEntry var69;
                            if ((var69 = BluePrintController.active.getBlueprint(var66.blueprintName)).getType() == BlueprintType.SPACE_STATION && !ServerConfig.BLUEPRINT_SPAWNABLE_STATIONS.isOn()) {
                                this.sendServerMessagePlayerError(new Object[]{347});
                                return;
                            }

                            if (var69.getType() == BlueprintType.SHIP && !ServerConfig.BLUEPRINT_SPAWNABLE_SHIPS.isOn()) {
                                this.sendServerMessagePlayerError(new Object[]{348});
                                return;
                            }

                            IntOpenHashSet var78 = new IntOpenHashSet();

                            for(var12 = 0; var12 < ElementKeyMap.highestType + 1; ++var12) {
                                short var95 = var51 = (short)var12;
                                if (ElementKeyMap.isValidType(var51) && ElementKeyMap.getInfoFast(var51).getSourceReference() != 0) {
                                    var95 = (short)ElementKeyMap.getInfoFast(var51).getSourceReference();
                                }

                                int var96;
                                if ((var96 = Math.min(this.getInventory().getOverallQuantity(var95), var66.goal.get(var51) - var66.progress.get(var51))) > 0) {
                                    this.getInventory().decreaseBatch(var95, var96, var78);
                                    var66.progress.inc(var51, var96);
                                }
                            }

                            this.getInventory().sendInventoryModification(var78);
                            ((GameServerState)this.state).getGameState().announceMetaObject(var54);
                            return;
                        } catch (EntityNotFountException var16) {
                            this.sendServerMessagePlayerError(new Object[]{349});
                            return;
                        }
                    }
                    break;
                case SEND_ALL_DESTINATIONS_OF_ENTITY:
                    var53 = (Integer)var1.getArgs()[0];
                    if ((var61 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var53)) != null && var61 instanceof ManagedSegmentController && ((ManagedSegmentController)var61).getManagerContainer() instanceof ActivationManagerInterface) {
                        ((SegmentController)var61).railController.getRoot().railController.sendAdditionalBlueprintInfoToClient();
                        return;
                    }
                    break;
                case SET_SPAWN:
                    var53 = (Integer)var1.getArgs()[0];
                    var59 = (Long)var1.getArgs()[1];
                    Sendable var65;
                    if ((var65 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().get(var53)) != null && var65 instanceof SegmentController) {
                        this.spawnData.setDeathSpawnTo((SegmentController)var65, ElementCollection.getPosFromIndex(var59, new Vector3i()));
                        return;
                    }
                    break;
                case ADD_BLUEPRINT_META_SINGLE:
                    var53 = (Integer)var1.getArgs()[0];
                    var55 = (Short)var1.getArgs()[1];
                    var9 = (Integer)var1.getArgs()[2];
                    var60 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    if (this.getInventory().isLockedInventory()) {
                        this.sendServerMessagePlayerError(new Object[]{350});
                        return;
                    }

                    if (var60 != null && var60 instanceof BlueprintMetaItem) {
                        BlueprintMetaItem var74 = (BlueprintMetaItem)var60;

                        try {
                            BlueprintEntry var86;
                            if ((var86 = BluePrintController.active.getBlueprint(var74.blueprintName)).getType() == BlueprintType.SPACE_STATION && !ServerConfig.BLUEPRINT_SPAWNABLE_STATIONS.isOn()) {
                                this.sendServerMessagePlayerError(new Object[]{351});
                                return;
                            }

                            if (var86.getType() == BlueprintType.SHIP && !ServerConfig.BLUEPRINT_SPAWNABLE_SHIPS.isOn()) {
                                this.sendServerMessagePlayerError(new Object[]{352});
                                return;
                            }

                            var51 = var55;
                            if (ElementKeyMap.isValidType(var55) && ElementKeyMap.getInfoFast(var55).getSourceReference() != 0) {
                                var51 = (short)ElementKeyMap.getInfoFast(var55).getSourceReference();
                            }

                            if ((var9 = Math.min(Math.min(var9, var74.goal.get(var55) - var74.progress.get(var55)), this.getInventory().getOverallQuantity(var51))) > 0) {
                                IntOpenHashSet var93 = new IntOpenHashSet();
                                this.getInventory().decreaseBatch(var51, var9, var93);
                                this.getInventory().sendInventoryModification(var93);
                                var74.progress.inc(var55, var9);
                                ((GameServerState)this.state).getGameState().announceMetaObject(var60);
                            }

                            return;
                        } catch (EntityNotFountException var15) {
                            this.sendServerMessagePlayerError(new Object[]{353});
                            return;
                        }
                    }
                    break;
                case VERIFY_FACTION_ID:
                    int var10000 = (Integer)var1.getArgs()[0];
                    boolean var57 = false;
                    if (var10000 != this.getFactionId()) {
                        this.getNetworkObject().factionId.set(this.getFactionId(), true);
                        this.getNetworkObject().factionId.setChanged(true);
                        this.getNetworkObject().setChanged(true);
                        return;
                    }
                    break;
                case SET_FREE_WARP_TARGET:
                    var53 = (Integer)var1.getArgs()[0];
                    var59 = (Long)var1.getArgs()[1];
                    var10 = (Integer)var1.getArgs()[2];
                    var11 = (Integer)var1.getArgs()[3];
                    var12 = (Integer)var1.getArgs()[4];
                    if ((var40 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var53)) != null && var40 instanceof ManagedSegmentController && ((ManagedSegmentController)var40).getManagerContainer() instanceof StationaryManagerContainer) {
                        StationaryManagerContainer var89;
                        WarpgateCollectionManager var94;
                        if ((var94 = (WarpgateCollectionManager)(var89 = (StationaryManagerContainer)((ManagedSegmentController)var40).getManagerContainer()).getWarpgate().getCollectionManagersMap().get(var59)) != null) {
                            var43 = FTLTable.DIRECT_PREFIX + var10 + "_" + var11 + "_" + var12 + "_" + DatabaseEntry.removePrefixWOException(var89.getSegmentController().getUniqueIdentifier());
                            var94.setDestination(var43, new Vector3i(var10, var11, var12));
                            this.sendServerMessagePlayerInfo(new Object[]{354, var10 + ", " + var11 + ", " + var12});
                            return;
                        }

                        this.sendServerMessagePlayerError(new Object[]{355});
                        return;
                    }

                    this.sendServerMessagePlayerError(new Object[]{356});
                    return;
                case SPAWN_BLUEPRINT_META:
                    var53 = (Integer)var1.getArgs()[0];
                    var58 = (String)var1.getArgs()[1];
                    MetaObject var62 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    var10 = (Integer)var1.getArgs()[2];
                    Vector3i var64 = new Vector3i((Integer)var1.getArgs()[3], (Integer)var1.getArgs()[4], (Integer)var1.getArgs()[5]);
                    boolean var75 = (Boolean)var1.getArgs()[6];
                    var35 = (Integer)var1.getArgs()[7];
                    long var87 = (Long)var1.getArgs()[8];
                    SegmentPiece var33 = null;
                    if (var35 > 0) {
                        if (!((var40 = (Sendable)((GameServerState)this.state).getLocalAndRemoteObjectContainer().getLocalObjects().get(var35)) instanceof SegmentController)) {
                            this.sendServerMessagePlayerError(new Object[]{357});
                            return;
                        }

                        SegmentPiece var52;
                        if ((var52 = ((SegmentController)var40).getSegmentBuffer().getPointUnsave(var87)) == null || !var52.isValid() || !var52.getInfo().isRailDockable()) {
                            this.sendServerMessagePlayerError(new Object[]{358});
                            return;
                        }

                        var33 = var52;
                    }

                    Sendable var36 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var10);
                    if (var62 != null && var62 instanceof BlueprintMetaItem && var36 != null && var36 instanceof InventoryHolder && ((InventoryHolder)var36).getInventory(ElementCollection.getIndex(var64)) != null) {
                        if (!(var38 = ((InventoryHolder)var36).getInventory(ElementCollection.getIndex(var64))).conatainsMetaItem(var62)) {
                            this.sendServerMessage(new ServerMessage(new Object[]{359}, 3, this.getId()));
                            return;
                        }

                        BlueprintMetaItem var46;
                        if ((var46 = (BlueprintMetaItem)var62).metGoal()) {
                            var5 = var75 ? this.getFactionId() : 0;
                            BluePrintSpawnQueueElement var41;
                            (var41 = new BluePrintSpawnQueueElement(var46.blueprintName, var58, var5, true, false, false, var33)).metaItem = var53;
                            var41.inv = var38;
                            this.bluePrintSpawnQueue.add(var41);
                            return;
                        }

                        this.sendServerMessage(new ServerMessage(new Object[]{360}, 3, this.getId()));
                        return;
                    }
                    break;
                case ADD_BLOCK_STORAGE_META_SINGLE:
                    var53 = (Integer)var1.getArgs()[0];
                    var55 = (Short)var1.getArgs()[1];
                    var9 = (Integer)var1.getArgs()[2];
                    var60 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    var11 = (Integer)var1.getArgs()[3];
                    var12 = (Integer)var1.getArgs()[4];
                    var35 = (Integer)var1.getArgs()[5];
                    var76 = (Integer)var1.getArgs()[6];
                    var88 = new Vector3i(var12, var35, var76);
                    if ((var27 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var11)) == null || !(var27 instanceof InventoryHolder)) {
                        System.err.println("[PLAYER] ERROR: count find inv holder " + var11);
                        return;
                    }

                    if ((var32 = (InventoryHolder)var27).getInventory(ElementCollection.getIndex(var88)) == null) {
                        System.err.println("[PLAYER] ERROR: count find inv on " + var32 + ": " + var88);
                        return;
                    }

                    var38 = var32.getInventory(ElementCollection.getIndex(var88));
                    if (var60 != null && var60 instanceof BlockStorageMetaItem) {
                        var44 = (BlockStorageMetaItem)var60;
                        short var48 = var55;
                        if (ElementKeyMap.isValidType(var55) && ElementKeyMap.getInfoFast(var55).getSlab() > 0) {
                            var48 = (short)ElementKeyMap.getInfoFast(var55).getSourceReference();
                        }

                        if ((var9 = Math.min(var9, var38.getOverallQuantity(var48))) > 0) {
                            var28 = new IntOpenHashSet();
                            var38.decreaseBatch(var48, var9, var28);
                            var38.sendInventoryModification(var28);
                            var44.storage.inc(var48, var9);
                            ((GameServerState)this.state).getGameState().announceMetaObject(var60);
                        }

                        return;
                    }
                    break;
                case ADD_BLOCK_STORAGE_META_ALL:
                    if (this.isAdmin()) {
                        var53 = (Integer)var1.getArgs()[0];
                        var56 = (Integer)var1.getArgs()[1];
                        var9 = (Integer)var1.getArgs()[2];
                        var10 = (Integer)var1.getArgs()[3];
                        var11 = (Integer)var1.getArgs()[4];
                        Vector3i var68 = new Vector3i(var9, var10, var11);
                        if ((var40 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var56)) != null && var40 instanceof InventoryHolder) {
                            InventoryHolder var82;
                            if ((var82 = (InventoryHolder)var40).getInventory(ElementCollection.getIndex(var68)) == null) {
                                System.err.println("[PLAYER] ERROR: count find inv on " + var82 + ": " + var68);
                                return;
                            }

                            Inventory var90 = var82.getInventory(ElementCollection.getIndex(var68));
                            MetaObject var30 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                            this.addAllToStorageMetaItem(var30, var90);
                            return;
                        }

                        System.err.println("[PLAYER] ERROR: count find inv holder " + var56);
                        return;
                    }
                    break;
                case GET_BLOCK_STORAGE_META_SINGLE:
                    var53 = (Integer)var1.getArgs()[0];
                    var55 = (Short)var1.getArgs()[1];
                    var9 = (Integer)var1.getArgs()[2];
                    var60 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    var11 = (Integer)var1.getArgs()[3];
                    var12 = (Integer)var1.getArgs()[4];
                    var35 = (Integer)var1.getArgs()[5];
                    var76 = (Integer)var1.getArgs()[6];
                    var88 = new Vector3i(var12, var35, var76);
                    if ((var27 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var11)) == null || !(var27 instanceof InventoryHolder)) {
                        System.err.println("[PLAYER] ERROR: count find inv holder " + var11);
                        return;
                    }

                    if ((var32 = (InventoryHolder)var27).getInventory(ElementCollection.getIndex(var88)) == null) {
                        System.err.println("[PLAYER] ERROR: count find inv on " + var32 + ": " + var88);
                        return;
                    }

                    var38 = var32.getInventory(ElementCollection.getIndex(var88));
                    if (var60 != null && var60 instanceof BlockStorageMetaItem) {
                        var44 = (BlockStorageMetaItem)var60;
                        if ((var9 = Math.min(var9 = Math.min(var9, var44.storage.get(var55)), var38.canPutInHowMuch(var55, var9, -1))) > 0 && var38.canPutIn(var55, var9)) {
                            (var28 = new IntOpenHashSet()).add(var38.incExistingOrNextFreeSlotWithoutException(var55, var9));
                            var44.storage.inc(var55, -var9);
                            if (var44.storage.getTotalAmount() <= 0L) {
                                int var49;
                                if ((var49 = var38.getSlotFromMetaId(var44.getId())) >= 0) {
                                    var38.removeMetaItem(var44);
                                    var28.add(var49);
                                }
                            } else {
                                ((GameServerState)this.state).getGameState().announceMetaObject(var60);
                            }

                            var38.sendInventoryModification(var28);
                            return;
                        }

                        this.sendServerMessage(new ServerMessage(new Object[]{361}, 3, this.getId()));
                        return;
                    }
                    break;
                case GET_BLOCK_STORAGE_META_ALL:
                    var53 = (Integer)var1.getArgs()[0];
                    var54 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var53);
                    var9 = (Integer)var1.getArgs()[1];
                    var10 = (Integer)var1.getArgs()[2];
                    var11 = (Integer)var1.getArgs()[3];
                    var12 = (Integer)var1.getArgs()[4];
                    Vector3i var3 = new Vector3i(var10, var11, var12);
                    Sendable var13;
                    if ((var13 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var9)) == null || !(var13 instanceof InventoryHolder)) {
                        System.err.println("[PLAYER] ERROR: count find inv holder " + var9);
                        return;
                    }

                    InventoryHolder var14;
                    if ((var14 = (InventoryHolder)var13).getInventory(ElementCollection.getIndex(var3)) == null) {
                        System.err.println("[PLAYER] ERROR: count find inv on " + var14 + ": " + var3);
                        return;
                    }

                    Inventory var26 = var14.getInventory(ElementCollection.getIndex(var3));
                    if (var54 != null && var54 instanceof BlockStorageMetaItem) {
                        BlockStorageMetaItem var31 = (BlockStorageMetaItem)var54;
                        Iterator var34 = ElementKeyMap.keySet.iterator();

                        while(true) {
                            short var4;
                            do {
                                if (!var34.hasNext()) {
                                    return;
                                }

                                var4 = (Short)var34.next();
                            } while((var5 = var31.storage.get(var4)) <= 0);

                            if ((var5 = Math.min(var5 = Math.min(var5, var31.storage.get(var4)), var26.canPutInHowMuch(var4, var5, -1))) <= 0 || !var26.canPutIn(var4, var5)) {
                                this.sendServerMessage(new ServerMessage(new Object[]{362}, 3, this.getId()));
                                return;
                            }

                            IntOpenHashSet var6;
                            (var6 = new IntOpenHashSet()).add(var26.incExistingOrNextFreeSlotWithoutException(var4, var5));
                            var31.storage.inc(var4, -var5);
                            if (var31.storage.getTotalAmount() <= 0L) {
                                int var42;
                                if ((var42 = this.inventory.getSlotFromMetaId(var31.getId())) >= 0) {
                                    this.inventory.removeMetaItem(var31);
                                    var6.add(var42);
                                }
                            } else {
                                ((GameServerState)this.state).getGameState().announceMetaObject(var54);
                            }

                            var26.sendInventoryModification(var6);
                        }
                    }
                    break;
                case REQUEST_BLUEPRINT_ITEM_LIST:
                    var7 = (String)var1.getArgs()[0];

                    try {
                        BlueprintEntry var8 = BluePrintController.active.getBlueprint(var7);
                        this.getNetworkObject().blockCountMapBuffer.add(new RemoteBlockCountMap(var8.getElementCountMapWithChilds(), this.isOnServer()));
                        return;
                    } catch (EntityNotFountException var25) {
                        var25.printStackTrace();
                    }
            }

        } else {
            this.sendServerMessage(new ServerMessage(new Object[]{339, var2}, 3, this.getId()));
        }
    }

    public void addAllToStorageMetaItem(MetaObject var1, Inventory var2) {
        if (var1 != null && var1 instanceof BlockStorageMetaItem) {
            BlockStorageMetaItem var3 = (BlockStorageMetaItem)var1;
            IntOpenHashSet var4 = new IntOpenHashSet();
            Iterator var5 = ElementKeyMap.keySet.iterator();

            while(var5.hasNext()) {
                short var6;
                short var7 = var6 = (Short)var5.next();
                if (ElementKeyMap.isValidType(var6) && ElementKeyMap.getInfoFast(var6).getSourceReference() != 0) {
                    var7 = (short)ElementKeyMap.getInfoFast(var6).getSourceReference();
                }

                int var8;
                if ((var8 = var2.getOverallQuantity(var7)) > 0) {
                    var2.decreaseBatch(var7, var8, var4);
                    var3.storage.inc(var7, var8);
                }
            }

            var2.sendInventoryModification(var4);
            ((GameServerState)this.state).getGameState().announceMetaObject(var1);
        }

    }

    public boolean isAdmin() {
        return this.isOnServer() ? ((GameServerState)this.getState()).isAdmin(this.getName()) : this.getNetworkObject().isAdminClient.getBoolean();
    }

    public String getFactionOwnRankName() {
        return this.factionController.getFactionOwnRankName();
    }

    public String getFactionRankName(byte var1) {
        return this.factionController.getFactionRankName(var1);
    }

    public boolean isInTutorial() {
        return Sector.isTutorialSector(this.getCurrentSector());
    }

    public boolean isInTestSector() {
        return this.getCurrentSector().equals(this.testSector);
    }

    public boolean isInPersonalSector() {
        return this.getCurrentSector().equals(this.personalSector);
    }

    public void fromTagStructure(Tag var1) {
        this.newPlayerOnServer = false;
        Tag[] var6 = (Tag[])var1.getValue();
        this.setCredits((Integer)var6[0].getValue());
        if (var6[1].getType() == Type.VECTOR3f) {
            this.spawnData.deathSpawn.localPos.set((Vector3f)var6[1].getValue());
        } else if (var6[1].getType() == Type.STRUCT) {
            this.spawnData.fromTagStructure(var6[1]);
        }

        if (var6[3].getType() == Type.VECTOR3i && "sector".equals(var6[3].getName())) {
            this.spawnData.deathSpawn.absoluteSector.set((Vector3i)var6[3].getValue());
        }

        this.getPersonalInventory().fromTagStructure(var6[2]);
        if (var6.length >= 6 && var6[4].getType() == Type.VECTOR3f && "lspawn".equals(var6[4].getName())) {
            this.spawnData.logoutSpawn.localPos.set((Vector3f)var6[4].getValue());
        }

        if (var6.length >= 6 && var6[5].getType() == Type.VECTOR3i && "lsector".equals(var6[5].getName())) {
            this.getCurrentSector().set((Vector3i)var6[5].getValue());
            this.spawnData.logoutSpawn.absoluteSector.set((Vector3i)var6[5].getValue());
        }

        if (var6.length >= 7) {
            this.factionController.fromTagStructure(var6[6]);
        }

        if (var6.length >= 11) {
            this.spawnData.lastLogin = (Long)var6[7].getValue();
            this.spawnData.lastLogout = (Long)var6[8].getValue();
            if ("ips".equals(var6[9].getName())) {
                ArrayList var2;
                Tag.listFromTagStruct(var2 = new ArrayList(), (Tag[])var6[9].getValue());
                Iterator var7 = var2.iterator();

                while(var7.hasNext()) {
                    String var3 = (String)var7.next();
                    this.getHosts().add(new PlayerInfoHistory(0L, var3, ""));
                }
            } else {
                try {
                    Tag.listFromTagStruct(PlayerInfoHistory.class.getConstructor(), this.getHosts(), (Tag[])var6[9].getValue());
                } catch (SecurityException var4) {
                    var4.printStackTrace();
                } catch (NoSuchMethodException var5) {
                    var5.printStackTrace();
                }
            }
        }

        Collections.sort(this.getHosts());
        if (var6.length > 10 && var6[10].getType() == Type.BYTE) {
            this.setHasCreativeMode((Byte)var6[10].getValue() != 0);
        }

        if (var6.length >= 13) {
            this.setLastEnteredEntity((String)var6[11].getValue());
        }

        if (var6.length >= 14 && var6[12].getType() == Type.VECTOR3i) {
            this.testSector = (Vector3i)var6[12].getValue();
        }

        if (var6.length >= 15 && var6[13].getType() == Type.INT) {
            this.setHelmetSlot((Integer)var6[13].getValue());
        }

        if (var6.length >= 16 && var6[14].getType() != Type.FINISH) {
            this.playerAiManager.fromTagStructure(var6[14]);
        }

        if (var6.length >= 17 && var6[15].getType() != Type.FINISH && var6[15].getType() != Type.BYTE) {
            this.personalSector = (Vector3i)var6[15].getValue();
        }

        if (var6.length >= 18 && var6[16].getType() != Type.FINISH) {
            this.getPersonalFactoryInventoryCapsule().fromTagStructure(var6[16]);
        }

        if (var6.length >= 19 && var6[17].getType() != Type.FINISH) {
            this.getPersonalFactoryInventoryMicro().fromTagStructure(var6[17]);
        }

        if (var6.length >= 20 && var6[18].getType() != Type.FINISH) {
            this.getPersonalFactoryInventoryMacroBlock().fromTagStructure(var6[18]);
        }

        if (var6.length >= 21 && var6[19].getType() != Type.FINISH) {
            this.lastDeathNotSuicide = (Long)var6[19].getValue();
        }

        if (var6.length >= 22 && var6[20].getType() != Type.FINISH) {
            Tag.listFromTagStructSP(this.scanHistory, var6[20], new ListSpawnObjectCallback<ScanData>() {
                public ScanData get(Object var1) {
                    ScanData var2;
                    (var2 = new ScanData()).fromTagStructure((Tag)var1);
                    return var2;
                }
            });
        }

        if (var6.length >= 23 && var6[21].getType() != Type.FINISH) {
            this.setFactionPointProtected((Byte)var6[21].getValue() == 1);
        }

        if (var6.length >= 24 && var6[22].getType() != Type.FINISH) {
            this.invBackUpfromTag(var6[22]);
        }

        if (var6.length >= 25 && var6[23].getType() != Type.FINISH) {
            Tag.listFromTagStructSPElimDouble(this.savedCoordinates, var6[23], new ListSpawnObjectCallback<SavedCoordinate>() {
                public SavedCoordinate get(Object var1) {
                    SavedCoordinate var2;
                    (var2 = new SavedCoordinate()).fromTagStructure((Tag)var1);
                    return var2;
                }
            });
        }

        if (var6.length >= 28 && var6[26].getType() == Type.STRUCT) {
            Tag.listFromTagStruct(this.ignored, (Tag[])var6[26].getValue());
        }

        if (var6.length >= 29 && var6[27].getType() == Type.FLOAT) {
            this.health = (Float)var6[27].getValue();
            this.lastHP = this.health;
        }

        if (var6.length >= 30 && var6[28].getType() == Type.STRUCT) {
            this.cargoInventoryBlock = VoidUniqueSegmentPiece.getFromUniqueTag(var6[28], 0);
        }

        if (var6.length > 29 && var6[29].getType() == Type.STRUCT) {
            this.infiniteInventoryVolume = var6[29].getBoolean();
        }

        if (var6.length > 30 && var6[30].getType() == Type.INT) {
            this.mineAutoArmSecs = var6[30].getInt();
        }

    }

    public boolean isControllingCore(ManagedUsableSegmentController<?> var1) {
        return var1 instanceof Ship ? this.getControllerState().isControlling(this, var1, Ship.core) : false;
    }

    public Tag toTagStructure() {
        try {
            ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getPlayerTable().updateOrInsertPlayer(this);
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        Tag[] var1;
        (var1 = new Tag[32])[0] = new Tag(Type.INT, "credits", this.credits);
        var1[1] = this.spawnData.toTagStructure();
        var1[2] = this.getPersonalInventory().toTagStructure();
        var1[3] = new Tag(Type.BYTE, (String)null, (byte)0);
        var1[4] = new Tag(Type.BYTE, (String)null, (byte)0);
        var1[5] = new Tag(Type.BYTE, (String)null, (byte)0);
        var1[6] = this.factionController.toTagStructure();
        var1[7] = new Tag(Type.LONG, (String)null, this.spawnData.lastLogin);
        var1[8] = new Tag(Type.LONG, (String)null, System.currentTimeMillis());
        var1[9] = Tag.listToTagStruct(this.getHosts(), "hist");
        var1[10] = new Tag(Type.BYTE, (String)null, Byte.valueOf((byte)(this.isHasCreativeMode() ? 1 : 0)));
        var1[11] = new Tag(Type.STRING, (String)null, this.getLastEnteredEntity() != null ? this.getLastEnteredEntity() : "none");
        var1[12] = this.testSector == null ? new Tag(Type.BYTE, (String)null, (byte)0) : new Tag(Type.VECTOR3i, (String)null, this.testSector);
        var1[13] = new Tag(Type.INT, (String)null, this.getHelmetSlot());
        var1[14] = this.playerAiManager.toTagStructure();
        var1[15] = this.personalSector == null ? new Tag(Type.BYTE, (String)null, (byte)0) : new Tag(Type.VECTOR3i, (String)null, this.personalSector);
        var1[16] = this.getPersonalFactoryInventoryCapsule().toTagStructure();
        var1[17] = this.getPersonalFactoryInventoryMicro().toTagStructure();
        var1[18] = this.getPersonalFactoryInventoryMacroBlock().toTagStructure();
        var1[19] = new Tag(Type.LONG, (String)null, this.lastDeathNotSuicide);
        var1[20] = Tag.listToTagStruct(this.scanHistory, (String)null);
        var1[21] = this.isFactionPointProtected() ? new Tag(Type.BYTE, (String)null, (byte)1) : new Tag(Type.BYTE, (String)null, (byte)0);
        var1[22] = this.invBackUpToTag();
        var1[23] = Tag.listToTagStruct(this.savedCoordinates, (String)null);
        var1[24] = new Tag(Type.BYTE, (String)null, (byte)0);
        var1[25] = new Tag(Type.BYTE, (String)null, (byte)0);
        var1[26] = Tag.listToTagStruct(this.getIgnored(), Type.STRING, (String)null);
        var1[27] = new Tag(Type.FLOAT, (String)null, this.health);
        var1[28] = this.cargoInventoryBlock != null ? this.cargoInventoryBlock.getUniqueTag() : new Tag(Type.BYTE, (String)null, (byte)0);
        var1[29] = new Tag((String)null, this.infiniteInventoryVolume);
        var1[30] = new Tag(Type.INT, (String)null, this.mineAutoArmSecs);
        var1[31] = FinishTag.INST;
        return new Tag(Type.STRUCT, "PlayerState", var1);
    }

    public SimpleTransformableSendableObject getAquiredTarget() {
        return this.aquiredTarget;
    }

    public void setAquiredTarget(SimpleTransformableSendableObject var1) {
        this.aquiredTarget = var1;
    }

    public PlayerCharacter getAssingedPlayerCharacter() {
        return this.playerCharacter;
    }

    public PlayerCatalogManager getCatalog() {
        return this.catalog;
    }

    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int var1) {
        this.clientId = var1;
    }

    public void callTutorialServer(String var1) {
        assert this.isOnServer();

        this.getNetworkObject().tutorialCalls.add(new RemoteString(var1, true));
    }

    public void callTutorialClient(String var1) {
        assert !this.isOnServer();

        if (((GameClientState)this.getState()).getController().getTutorialMode() != null) {
            ((GameClientState)this.getState()).getController().getTutorialMode().setCurrentMachine(var1);

            assert ((GameClientState)this.getState()).getController().getTutorialMode().getMachine() != null : var1 + " :::: " + ((GameClientState)this.getState()).getController().getTutorialMode().getMachineNames();

            ((GameClientState)this.getState()).getController().getTutorialMode().getMachine().reset();
            this.tutorialCallClient = null;
        }

    }

    private ShopInterface getClosestShopsInDistance() {
        float var1 = -1.0F;
        ShopInterface var2 = null;
        Iterator var3 = this.shopsInDistance.iterator();

        while(true) {
            ShopInterface var4;
            Vector3f var5;
            do {
                if (!var3.hasNext()) {
                    return var2;
                }

                var4 = (ShopInterface)var3.next();
                (var5 = new Vector3f(var4.getWorldTransform().origin)).sub(((SimpleTransformableSendableObject)((ControllerStateUnit)this.getControllerState().getUnits().iterator().next()).playerControllable).getWorldTransform().origin);
            } while(var1 >= 0.0F && var5.lengthSquared() >= var1);

            var2 = var4;
            var1 = var5.lengthSquared();
        }
    }

    public ControllerState getControllerState() {
        return this.controllerState;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int var1) {
        this.credits = Math.min(2147483647, Math.max(0, var1));
        if (this.isOnServer()) {
            this.getRuleEntityManager().triggerPlayerCreditsChanged();
        }

    }

    public Vector3i getCurrentSector() {
        return this.currentSector;
    }

    public void setCurrentSector(Vector3i var1) {
        if (!var1.equals(this.currentSector)) {
            this.lastSector.set(this.currentSector);
            this.sectorChanged = true;
        }

        this.currentSector.set(var1);
    }

    public int getCurrentSectorId() {
        return this.currentSectorId;
    }

    public void setCurrentSectorId(int var1) {
        this.currentSectorId = var1;
    }

    public int getCurrentShipControllerSlot() {
        return this.isClientOwnPlayer() ? this.shipControllerSlotClient : this.getNetworkObject().shipControllerSlot.get();
    }

    public void setCurrentShipControllerSlot(byte var1, float var2) {
        assert !this.isOnServer();

        this.shipControllerSlotClient = var1;
        if (var2 == 0.0F) {
            this.getNetworkObject().shipControllerSlot.set(var1, true);
        }

        this.slotChangeUpdateDelay = var2;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int var1) {
        this.deaths = var1;
    }

    public PlayerConversationManager getPlayerConversationManager() {
        return this.playerConversationManager;
    }

    public PlayerFactionController getFactionController() {
        return this.factionController;
    }

    public int getFactionId() {
        return this.factionController.getFactionId();
    }

    public String getUniqueIdentifier() {
        return "ENTITY_PLAYERSTATE_" + this.name;
    }

    public SimpleTransformableSendableObject getFirstControlledTransformable() throws PlayerControlledTransformableNotFound {
        Iterator var1 = this.getControllerState().getUnits().iterator();

        ControllerStateUnit var2;
        do {
            if (!var1.hasNext()) {
                throw new PlayerControlledTransformableNotFound(this);
            }
        } while(!(var2 = (ControllerStateUnit)var1.next()).playerState.equals(this) || !(var2.playerControllable instanceof SimpleTransformableSendableObject));

        return (SimpleTransformableSendableObject)var2.playerControllable;
    }

    public SimpleTransformableSendableObject getFirstControlledTransformableWOExc() {
        Iterator var1 = this.getControllerState().getUnits().iterator();

        ControllerStateUnit var2;
        do {
            if (!var1.hasNext()) {
                return null;
            }
        } while(!(var2 = (ControllerStateUnit)var1.next()).playerState.equals(this) || !(var2.playerControllable instanceof SimpleTransformableSendableObject));

        return (SimpleTransformableSendableObject)var2.playerControllable;
    }

    public List<PlayerInfoHistory> getHosts() {
        return this.spawnData.hosts;
    }

    public InventoryController getInventoryController() {
        return this.inventoryController;
    }

    public void setInventoryController(InventoryController var1) {
        this.inventoryController = var1;
    }

    public String getIp() {
        return this.ip;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int var1) {
        this.kills = var1;
    }

    public String getLastEnteredEntity() {
        return this.lastEnteredEntity;
    }

    public void setLastEnteredEntity(String var1) {
        this.lastEnteredEntity = var1;
    }

    public long getLastLogin() {
        return this.spawnData.lastLogin;
    }

    public long getLastLogout() {
        return this.spawnData.lastLogout;
    }

    public String getName() {
        return this.name;
    }

    public StateInterface getState() {
        return this.state;
    }

    public void setState(StateInterface var1) {
        this.state = var1;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public int getPing() {
        return this.ping;
    }

    public void setPing(int var1) {
        this.ping = var1;
    }

    public ClientProximitySector getProximitySector() {
        return this.proximitySector;
    }

    public ClientProximitySystem getProximitySystem() {
        return this.proximitySystem;
    }

    public RType getRelation(int var1) {
        return var1 != 0 && var1 == this.getFactionId() ? RType.FRIEND : this.factionController.getRelation(var1);
    }

    public RType getRelation(PlayerState var1) {
        if (var1.getFactionId() != 0 && var1.getFactionId() == this.getFactionId()) {
            return RType.FRIEND;
        } else {
            RType var2 = this.factionController.getRelation(var1);
            RType var3 = var1.factionController.getRelation(this);
            return var2 != RType.ENEMY && var3 != RType.ENEMY ? var2 : RType.ENEMY;
        }
    }

    public UploadController getShipUploadController() {
        return this.shipUploadController;
    }

    public Set<ShopInterface> getShopsInDistance() {
        return this.shopsInDistance;
    }

    public SkinManager getSkinManager() {
        return this.skinManager;
    }

    public SkinUploadController getSkinUploadController() {
        return this.skinUploadController;
    }

    public void getWordTransform(Transform var1) {
        GlUtil.setRightVector(this.getRight(this.transTmp.origin), var1);
        GlUtil.setUpVector(this.getUp(this.transTmp.origin), var1);
        GlUtil.setForwardVector(this.getForward(this.transTmp.origin), var1);
    }

    public void handleBeingKilled() {
        if (!this.killerIds.isEmpty()) {
            synchronized(this.killerIds) {
                while(!this.killerIds.isEmpty()) {
                    int var2 = this.killerIds.dequeueInt();
                    Sendable var3 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var2);
                    if (!this.isOnServer()) {
                        this.getControllerState().removeAllUnitsFromPlayer(this, true);
                        if (this.isClientOwnPlayer()) {
                            ((GameClientState)this.getState()).setCurrentPlayerObject((SimpleTransformableSendableObject)null);
                        }

                        String var4;
                        if (var3 != null) {
                            if (this.getId() == var3.getId()) {
                                var4 = Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_45;
                                ((GameClientState)this.getState()).getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_46, new Object[]{this.getName()}), 0.0F);
                            } else if (var3 instanceof Damager) {
                                var4 = "Cause of last Death:\n" + ((Damager)var3).getName() + " killed you";
                                ((GameClientState)this.getState()).getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_48, new Object[]{((Damager)var3).getName(), this.getName()}), 0.0F);
                            } else {
                                var4 = "Cause of last Death:\n" + var3 + " killed you";
                                ((GameClientState)this.getState()).getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_47, new Object[]{var3, this.getName()}), 0.0F);
                            }
                        } else {
                            var4 = "Cause of last Death:\nunkownObject(" + var2 + ") killed you";
                            ((GameClientState)this.getState()).getController().popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_49, new Object[]{var2, this.getName()}), 0.0F);
                        }

                        this.lastDiedMessage = var4;
                    } else {
                        System.err.println("[SERVER][PLAYER] " + this + " received suicide request: killer: " + var3);
                        LogUtil.log().fine("[DEATH] " + this.getName() + " committed suicide");
                        this.handleServerHealthAndCheckAliveOnServer(0.0F, (Damager)var3);
                    }
                }

            }
        }
    }

    public void enqueueClientBlueprintToWrite(SegmentController var1, String var2, BlueprintClassification var3) {
        BluePrintWriteQueueElement var4 = new BluePrintWriteQueueElement(var1, var2, var3, true);
        this.bluePrintWriteQueue.add(var4);
    }

    public void handleBluePrintQueuesClient() throws EntityNotFountException, IOException, EntityAlreadyExistsException {
        for(int var1 = 0; var1 < this.bluePrintWriteQueue.size(); ++var1) {
            BluePrintWriteQueueElement var2;
            if (!(var2 = (BluePrintWriteQueueElement)this.bluePrintWriteQueue.get(var1)).requestedAdditionalBlueprintData) {
                ((SendableSegmentController)var2.segmentController).getNetworkObject().additionalBlueprintData.set(false, true);
                var2.requestedAdditionalBlueprintData = true;
                this.sendSimpleCommand(SimplePlayerCommands.SEND_ALL_DESTINATIONS_OF_ENTITY, var2.segmentController.getId());
                System.err.println("[CLIENT][PLAYER][BLUEPRINT] " + this + " REQUESTED EXTRA INFO FOR LOCAL BLUEPRINT " + var2.segmentController);
            } else if (((SendableSegmentController)var2.segmentController).railController.getRoot().railController.isAllAdditionalBlueprintInfoReceived()) {
                try {
                    ((CatalogState)this.getState()).getCatalogManager().writeEntryClient(var2, this.getName());
                    System.err.println("[CLIENT][PLAYER][BLUEPRINT] " + this + " SAVED LOCAL BLUEPRINT " + var2.segmentController);
                } catch (IOException var3) {
                    var3.printStackTrace();
                }

                this.bluePrintWriteQueue.remove(var1);
                --var1;
            }
        }

    }

    public void handleBluePrintQueuesServer() throws EntityNotFountException, IOException, EntityAlreadyExistsException {
        while(!this.bluePrintWriteQueue.isEmpty()) {
            BluePrintWriteQueueElement var1 = (BluePrintWriteQueueElement)this.bluePrintWriteQueue.remove(0);

            try {
                ((CatalogState)this.getState()).getCatalogManager().writeEntryServer(var1, this.getName());
                System.err.println("[SERVER][PLAYER][BLUEPRINT] " + this + " SAVED BLUEPRINT " + var1.segmentController);
                LogUtil.log().fine("[BLUEPRINT][SAVE] " + this.getName() + " saved: \"" + var1.name + "\"");
            } catch (IOException var15) {
                var15.printStackTrace();
            }
        }

        while(!this.bluePrintSpawnQueue.isEmpty()) {
            BluePrintSpawnQueueElement var24 = (BluePrintSpawnQueueElement)this.bluePrintSpawnQueue.remove(0);
            Transform var2;
            (var2 = new Transform()).setIdentity();

            try {
                if (System.currentTimeMillis() - this.lastBlueprintSpawn > this.blueprintDelay) {
                    SimpleTransformableSendableObject var3 = this.getFirstControlledTransformable();
                    var2.set(var3.getWorldTransform());
                    long var4 = System.currentTimeMillis();
                    String var28;
                    if (var24.metaItem < 0) {
                        SegmentControllerOutline var27;
                        (var27 = BluePrintController.active.loadBluePrint((GameServerState)this.getState(), var24.catalogName, var24.shipName, var2, var24.infiniteShop ? -1 : this.credits, var24.factionId, this.getCurrentSector(), this.getUniqueIdentifier(), buffer, var24.toDockOn, var24.activeAI, new ChildStats(false))).checkOkName();
                        if (var27.en.getType() == BlueprintType.SHIP) {
                            long var29 = System.currentTimeMillis() - var4;
                            var4 = System.currentTimeMillis();
                            if (!var24.infiniteShop) {
                                this.modCreditsServer(-var27.en.getPrice());
                            } else {
                                this.sendServerMessage(new ServerMessage(new Object[]{367}, 1, this.getId()));
                            }

                            var27.spawnSectorId.set(this.currentSector);
                            var27.checkProspectedBlockCount = true;
                            synchronized(((GameServerState)this.getState()).getBluePrintsToSpawn()) {
                                ((GameServerState)this.getState()).getBluePrintsToSpawn().add(var27);
                            }

                            long var30 = System.currentTimeMillis() - var4;
                            this.lastBlueprintSpawn = System.currentTimeMillis();
                            var28 = "[BLUEPRINT][BUY] " + this.getName() + " bought: \"" + var27.en.getName() + "\" as \"" + var27.realName + "\"; Price: " + var27.en.getPrice() + "; to sector: " + this.getCurrentSector() + " (loadTime: " + var29 + "ms, spawnTime: " + var30 + "ms)";
                            System.err.println(var28);
                            LogUtil.log().fine(var28);
                        } else {
                            this.sendServerMessagePlayerError(new Object[]{368});
                        }
                    } else {
                        MetaObject var26;
                        if ((var26 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var24.metaItem)) == null) {
                            System.err.println("[SERVER][BLUEPRINT][BUY] Exception: Meta Object Blueptinz not found: " + var24.metaItem);
                        } else if (!(var26 instanceof BlueprintMetaItem)) {
                            System.err.println("[SERVER][BLUEPRINT][BUY] Exception: Metaitem not a blueprint: " + var26);
                        } else {
                            BlueprintMetaItem var7;
                            if (!(var7 = (BlueprintMetaItem)var26).metGoal()) {
                                System.err.println("[SERVER][BLUEPRINT][BUY] Exception: Metaitem not a blueprint: " + var26);
                            } else {
                                SegmentControllerOutline var8 = null;

                                try {
                                    (var8 = BluePrintController.active.loadBluePrint((GameServerState)this.getState(), var24.catalogName, var24.shipName, var2, -1, var24.factionId, this.getCurrentSector(), this.getUniqueIdentifier(), buffer, var24.toDockOn, var24.activeAI, new ChildStats(false))).checkOkName();
                                    if (!((GameServerState)this.getState()).getGameConfig().isBBOk(var8.en)) {
                                        this.sendServerMessagePlayerError(new Object[]{363, ((GameServerState)this.getState()).getGameConfig().toStringAllowedSize(var8.en)});
                                        continue;
                                    }
                                } catch (EntityNotFountException var18) {
                                    var18.printStackTrace();
                                }

                                int var9 = (Integer)ServerConfig.ALLOWED_STATIONS_PER_SECTOR.getCurrentState();
                                boolean var10 = true;
                                boolean var25 = true;
                                int var6 = 0;
                                if (var8 != null && var8.en.getType() == BlueprintType.SPACE_STATION) {
                                    if (!ServerConfig.BLUEPRINT_SPAWNABLE_STATIONS.isOn()) {
                                        var25 = false;
                                    }

                                    Iterator var11 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                                    while(var11.hasNext()) {
                                        Sendable var12;
                                        if ((var12 = (Sendable)var11.next()) instanceof SpaceStation && ((SpaceStation)var12).getSectorId() == this.getCurrentSectorId()) {
                                            ++var6;
                                        }
                                    }

                                    var10 = var6 < var9;
                                }

                                if (var8 != null && var8.en.getType() == BlueprintType.SHIP && !ServerConfig.BLUEPRINT_SPAWNABLE_SHIPS.isOn()) {
                                    var25 = false;
                                }

                                if (!var25) {
                                    this.sendServerMessagePlayerError(new Object[]{364});
                                } else if (!var10) {
                                    this.sendServerMessagePlayerError(new Object[]{366, var9});
                                } else {
                                    if (var8 != null && var8.en.getElementCountMapWithChilds().equals(var7.goal)) {
                                        long var31 = System.currentTimeMillis() - var4;
                                        var4 = System.currentTimeMillis();
                                        var8.spawnSectorId.set(this.currentSector);
                                        var8.checkProspectedBlockCount = true;
                                        synchronized(((GameServerState)this.getState()).getBluePrintsToSpawn()) {
                                            ((GameServerState)this.getState()).getBluePrintsToSpawn().add(var8);
                                        }

                                        long var33 = System.currentTimeMillis() - var4;
                                        this.lastBlueprintSpawn = System.currentTimeMillis();
                                        var28 = "[BLUEPRINT][BUY] " + this.getName() + " bought blueprint from metaItem: \"" + var8.en.getName() + "\" as \"" + var8.realName + "\"; Price: " + var8.en.getPrice() + "; to sector: " + this.getCurrentSector() + " (loadTime: " + var31 + "ms, spawnTime: " + var33 + "ms)";
                                        System.err.println(var28);
                                        LogUtil.log().fine(var28);
                                    } else {
                                        this.sendServerMessagePlayerError(new Object[]{365});
                                        if (this.getFirstControlledTransformableWOExc() != null) {
                                            var7.progress.spawnInSpace(this.getFirstControlledTransformableWOExc());
                                        }

                                        if (var8 != null) {
                                            var8.en.getName();
                                        }

                                        String var32 = var8 != null ? var8.realName : "DELETED";
                                        String var13 = "[BLUEPRINT][BUY] " + this.getName() + " failed to buy blueprint from metaItem: \"" + var24.catalogName + "\" as \"" + var32 + "\"; to sector: " + this.getCurrentSector() + "; MetaItem goal for that blueprint differed";
                                        System.err.println(var13);
                                        LogUtil.log().fine(var13);
                                    }

                                    var24.inv.removeMetaItem(var26);
                                }
                            }
                        }
                    }
                } else {
                    this.sendServerMessage(new ServerMessage(new Object[]{369, this.blueprintDelay / 1000L - (System.currentTimeMillis() - this.lastBlueprintSpawn) / 1000L}, 3, this.getId()));
                }
            } catch (NotEnoughCreditsException var19) {
                this.sendServerMessage(new ServerMessage(new Object[]{370}, 3, this.getId()));
                var19.printStackTrace();
            } catch (PlayerControlledTransformableNotFound var20) {
                var20.printStackTrace();
            } catch (EntityNotFountException var21) {
                var21.printStackTrace();
            } catch (IOException var22) {
                var22.printStackTrace();
            } catch (EntityAlreadyExistsException var23) {
                if (this.isOnServer()) {
                    this.sendServerMessage(new ServerMessage(new Object[]{371}, 3, this.getId()));
                }

                var23.printStackTrace();
            }
        }

    }

    public void handleInventoryStateFromNT(NetworkPlayer var1) {
        this.handleNormalShoppingFromNT(var1);
        if (this.isOnServer()) {
            int var4;
            int var7;
            if (!var1.recipeSellRequests.getReceiveBuffer().isEmpty()) {
                IntOpenHashSet var2 = new IntOpenHashSet();
                Iterator var3 = var1.recipeSellRequests.getReceiveBuffer().iterator();

                while(var3.hasNext()) {
                    var4 = (Integer)var3.next();
                    int var5 = this.getInventory((Vector3i)null).getMeta(var4);
                    MetaObject var6;
                    if ((var6 = ((GameServerState)this.getState()).getMetaObjectManager().getObject(var5)) != null) {
                        if (((Recipe)var6).fixedPrice >= 0L) {
                            var7 = (int)((float)((Recipe)var6).fixedPrice * (Float)ServerConfig.RECIPE_REFUND_MULT.getCurrentState());
                            this.modCreditsServer((long)var7);
                        } else {
                            var7 = (int)((float)(Integer)ServerConfig.RECIPE_BLOCK_COST.getCurrentState() * (Float)ServerConfig.RECIPE_REFUND_MULT.getCurrentState());
                            short var8 = ((Recipe)var6).recipeProduct[0].outputResource[0].type;
                            var7 = this.getInventory().incExistingOrNextFreeSlot(var8, var7);
                            var2.add(var7);
                        }

                        this.getInventory((Vector3i)null).put(var4, (short)0, 0, -1);
                        var2.add(var4);
                    }
                }

                this.sendInventoryModification(var2, -9223372036854775808L);
            }

            Iterator var11 = var1.fixedRecipeBuyRequests.getReceiveBuffer().iterator();

            while(true) {
                int var13;
                while(var11.hasNext()) {
                    if ((var13 = (Integer)var11.next()) >= 0 && var13 < ElementKeyMap.fixedRecipes.recipes.size()) {
                        FixedRecipe var15;
                        if ((var15 = (FixedRecipe)ElementKeyMap.fixedRecipes.recipes.get(var13)).canAfford(this)) {
                            IntOpenHashSet var20 = new IntOpenHashSet();
                            boolean var22 = false;
                            if (var15.costType == -1) {
                                this.modCreditsServer((long)(-var15.costAmount));
                                var22 = true;
                            } else if (ElementKeyMap.exists(var15.costType)) {
                                ElementKeyMap.getInfo(var15.costType);
                                Inventory var24;
                                if ((var24 = this.getInventory((Vector3i)null)).getOverallQuantity(var15.costType) >= var15.costAmount) {
                                    var24.decreaseBatch(var15.costType, var15.costAmount, var20);
                                }

                                var22 = true;
                            } else {
                                this.sendServerMessage(new ServerMessage(new Object[]{372, var15.costType, var15.name}, 3, this.getId()));
                            }

                            if (var22) {
                                Recipe var14 = var15.getMetaItem();

                                try {
                                    var7 = this.getInventory((Vector3i)null).getFreeSlot();
                                    this.getInventory((Vector3i)null).put(var7, var14);
                                    var20.add(var7);
                                } catch (NoSlotFreeException var9) {
                                    var9.printStackTrace();
                                    this.sendServerMessage(new ServerMessage(new Object[]{373}, 3, this.getId()));
                                }
                            }

                            if (var20.size() > 0) {
                                this.getInventory((Vector3i)null).sendInventoryModification(var20);
                            }
                        } else {
                            this.sendServerMessage(new ServerMessage(new Object[]{374, var13, var15.name}, 3, this.getId()));
                        }
                    } else {
                        this.sendServerMessage(new ServerMessage(new Object[]{375, var13}, 3, this.getId()));
                    }
                }

                var11 = var1.recipeRequests.getReceiveBuffer().iterator();

                while(var11.hasNext()) {
                    //(Integer)var11.next(); DECOMPILER ARTIFACT
                    this.sendServerMessage(new ServerMessage(new Object[]{376}, 3, this.getId()));
                }

                var11 = var1.dropOrPickupSlots.getReceiveBuffer().iterator();

                while(var11.hasNext()) {
                    DragDrop var17 = (DragDrop)((RemoteDragDrop)var11.next()).get();
                    this.dropsIntoSpace.enqueue(var17);
                }

                var11 = var1.catalogPlayerHandleBuffer.getReceiveBuffer().iterator();

                while(true) {
                    while(true) {
                        while(true) {
                            while(var11.hasNext()) {
                                BlueprintPlayerHandleRequest var19;
                                String var21 = (var19 = (BlueprintPlayerHandleRequest)((RemoteBlueprintPlayerRequest)var11.next()).get()).catalogName;
                                String var23 = var19.entitySpawnName;
                                var13 = var19.toSaveShip;
                                System.err.println("[SERVER][PLAYER] RECEIVED SAVE BUY: " + var21 + " " + var23);
                                if (var19.save) {
                                    Sendable var29;
                                    if ((var29 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var13)) != null && var29 instanceof Ship || var29 instanceof SpaceStation) {
                                        SegmentController var26 = (SegmentController)var29;
                                        if ((Integer)ServerConfig.CATALOG_SLOTS_PER_PLAYER.getCurrentState() >= 0 && this.getCatalog().getPersonalCatalog().size() >= (Integer)ServerConfig.CATALOG_SLOTS_PER_PLAYER.getCurrentState()) {
                                            this.sendServerMessage(new ServerMessage(new Object[]{377, this.getCatalog().getPersonalCatalog().size(), ServerConfig.CATALOG_SLOTS_PER_PLAYER.getCurrentState()}, 3, this.getId()));
                                        } else {
                                            BluePrintWriteQueueElement var30 = new BluePrintWriteQueueElement(var26, var23, var19.classification, false);
                                            this.bluePrintWriteQueue.add(var30);
                                        }
                                    } else {
                                        System.err.println("[SERVER][SAVE][ERROR] COULD NOT FIND SHIP WITH ID " + var13);
                                    }
                                } else if (var19.directBuy) {
                                    SimpleTransformableSendableObject var27;
                                    if ((var27 = this.getFirstControlledTransformableWOExc()) != null && var27 instanceof ShopperInterface) {
                                        ShopperInterface var25 = (ShopperInterface)var27;
                                        boolean var28 = false;
                                        boolean var10 = false;
                                        if (!var25.getShopsInDistance().isEmpty()) {
                                            var28 = true;
                                            var10 = ((ShopInterface)var25.getShopsInDistance().iterator().next()).isInfiniteSupply();
                                        } else if (this.getNetworkObject().isAdminClient.get()) {
                                            var28 = true;
                                            this.sendServerMessage(new ServerMessage(new Object[]{378}, 3, this.getId()));
                                        } else {
                                            this.sendServerMessage(new ServerMessage(new Object[]{379}, 3, this.getId()));
                                        }

                                        if (var28) {
                                            Faction var16 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId());
                                            if (((GameStateInterface)this.getState()).getGameState().allowedToSpawnBBShips(this, var16)) {
                                                SegmentPiece var18 = var19.getToSpawnOnRail(this, (GameServerState)this.state);
                                                var4 = var19.setOwnFaction ? this.getFactionId() : 0;
                                                BluePrintSpawnQueueElement var12 = new BluePrintSpawnQueueElement(var21, var23, var4, var10, false, true, var18);
                                                this.bluePrintSpawnQueue.add(var12);
                                            }
                                        }
                                    }
                                } else {
                                    this.giveMetaBlueprint = var21;
                                }
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    public void handleKilledFromNT(NetworkPlayer var1) {
        Iterator var5 = var1.killedBuffer.getReceiveBuffer().iterator();

        while(var5.hasNext()) {
            int var2 = (Integer)var5.next();
            synchronized(this.killerIds) {
                this.killerIds.enqueue(var2);
            }
        }

    }

    public void suicideOnServer() {
        synchronized(this.killerIds) {
            this.killerIds.enqueue(this.getId());
        }
    }

    public boolean canClientPressKey() {
        assert !this.isOnServer();

        if (this.lastCanKeyPressCheck != this.getState().getNumberOfUpdate()) {
            this.lastCanKeyPressCheckResult = !((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getChatControlManager().isActive() && ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().isActive() && !((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().isAnyMenuActive() && ((GameClientState)this.getState()).getPlayerInputs().isEmpty();
            this.lastCanKeyPressCheck = this.getState().getNumberOfUpdate();
        }

        return this.lastCanKeyPressCheckResult;
    }

    public void handleLocalKeyboardInput() {
        if (this.isClientOwnPlayer() && ((GameClientController)this.getState().getController()).isWindowActive() && this.canClientPressKey()) {
            for(int var1 = 0; var1 < KeyboardMappings.remoteMappings.length; ++var1) {
                KeyboardMappings.remoteMappings[var1].setNTKeyDown(this.getNetworkObject().keyboardOfController, (ClientStateInterface)this.state);
            }
        }

    }

    public void handleLocalMouseInput() {
        if (this.isClientOwnPlayer() && GameClientController.hasGraphics(this.state) && Display.isActive()) {
            GameClientState var1;
            if ((var1 = (GameClientState)this.getState()).getGlobalGameControlManager() != null && ((GameClientController)this.getState().getController()).isWindowActiveOutOfMenu()) {
                this.getNetworkObject().setMouseDown(var1.getController());
            }

            if (Controller.getCamera() != null && Controller.getCamera().isStable()) {
                Transform var3;
                (var3 = new Transform()).setIdentity();
                if (Controller.getCamera() instanceof InShipCamera && this.getState().getUpdateTime() - ((InShipCamera)Controller.getCamera()).getHelperCamera().lastInitialRecoil < Math.max(140L, ((GameClientState)this.getState()).getPing())) {
                    Matrix3f var2 = ((InShipCamera)Controller.getCamera()).getHelperCamera().lastRecoilState;
                    var3.set(var2);
                } else {
                    GlUtil.setRightVector(this.getRight(this.transTmp.origin), var3);
                    GlUtil.setForwardVector(this.getForward(this.transTmp.origin), var3);
                    GlUtil.setUpVector(this.getUp(this.transTmp.origin), var3);
                }

                if (!this.getNetworkObject().camOrientation.equalsMatrix(var3.basis)) {
                    this.getNetworkObject().camOrientation.set(var3.basis, true);
                }
            }
        }

    }

    public void handleNormalShoppingFromNT(NetworkPlayer var1) {
        int var5;
        if (var1.deleteBuffer.getReceiveBuffer().size() > 0) {
            Iterator var2 = var1.deleteBuffer.getReceiveBuffer().iterator();

            while(var2.hasNext()) {
                RemoteIntegerArray var3;
                int var4 = (Integer)((RemoteField[])(var3 = (RemoteIntegerArray)var2.next()).get())[0].get();
                var5 = (Integer)((RemoteField[])var3.get())[1].get();
                int var6 = (Integer)((RemoteField[])var3.get())[2].get();
                short var15;
                ElementKeyMap.getInfo(var15 = (short)var5);
                this.getInventory().inc(var6, var15, -var4);
                this.getInventory().sendInventoryModification(var6);
            }
        }

        if (var1.buyBuffer.getReceiveBuffer().size() > 0 || var1.sellBuffer.getReceiveBuffer().size() > 0) {
            IntOpenHashSet var12 = new IntOpenHashSet();
            Object2ObjectOpenHashMap var13 = new Object2ObjectOpenHashMap();
            ShopInterface var14;
            ServerMessage var20;
            if ((var14 = this.getClosestShopsInDistance()) == null) {
                var20 = new ServerMessage(new Object[]{380}, 3, this.getId());
                System.err.println("[SERVER] " + this + " No Shops in distance: " + this.getShopsInDistance());
                this.sendServerMessage(var20);
                return;
            }

            if (!var14.getShoppingAddOn().hasPermission(this)) {
                var20 = new ServerMessage(new Object[]{381}, 3, this.getId());
                this.sendServerMessage(var20);
                return;
            }

            this.getCredits();
            Iterator var18 = var1.buyBuffer.getReceiveBuffer().iterator();

            int var7;
            RemoteIntegerArray var16;
            while(var18.hasNext()) {
                var7 = (Integer)((RemoteField[])(var16 = (RemoteIntegerArray)var18.next()).get())[0].get();
                short var8;
                ElementInformation var17 = ElementKeyMap.getInfo(var8 = ((Integer) ((RemoteField[]) var16.get())[1].get()).shortValue());
                if (var7 <= 0) {
                    System.err.println("[SERVER] ERROR: invalid quantity Shopping Buy: " + var7 + " of " + var17.getName() + " for " + this);
                }

                System.err.println("[SERVER] Executing Shopping Buy: " + var7 + " of " + var17.getName() + " for " + this);
                if (var14 != null) {
                    IntOpenHashSet var19;
                    if ((var19 = (IntOpenHashSet)var13.get(var14)) == null) {
                        var19 = new IntOpenHashSet();
                        var13.put(var14, var19);
                    }

                    try {
                        var14.getShoppingAddOn().buy(this, var8, var7, var14, var12, var19);
                    } catch (NoSlotFreeException var10) {
                        var10.printStackTrace();
                        var20 = new ServerMessage(new Object[]{382}, 3, this.getId());
                        this.sendServerMessage(var20);
                    }
                } else {
                    System.err.println("Exception no shop in distance found " + this);
                }
            }

            var18 = var1.sellBuffer.getReceiveBuffer().iterator();

            while(var18.hasNext()) {
                var7 = (Integer)((RemoteField[])(var16 = (RemoteIntegerArray)var18.next()).get())[0].get();
                ElementInformation var21;
                if (!(var21 = ElementKeyMap.getInfo((short)(var5 = (Integer)((RemoteField[])var16.get())[1].get()))).isShoppable()) {
                    ((GameServerState)this.getState()).getController().broadcastMessage(new Object[]{383, this.getName()}, 3);
                }

                if (var7 <= 0) {
                    System.err.println("[SERVER] ERROR: invalid quantity Shopping Sell: " + var7 + " of " + var21.getName() + " for " + this);
                }

                System.err.println("[SERVER] Executing Shopping Sell: " + var7 + " of " + var21.getName() + " for " + this);
                if (var14 != null) {
                    IntOpenHashSet var11;
                    if ((var11 = (IntOpenHashSet)var13.get(var14)) == null) {
                        var11 = new IntOpenHashSet();
                        var13.put(var14, var11);
                    }

                    try {
                        var14.getShoppingAddOn().sell(this, (short)var5, var7, var14, var12, var11);
                    } catch (NoSlotFreeException var9) {
                        var9.printStackTrace();
                        var20 = new ServerMessage(new Object[]{384}, 3, this.getId());
                        this.sendServerMessage(var20);
                    }
                } else {
                    System.err.println("Exception no shop in distance found " + this);
                }
            }

            if (!var12.isEmpty()) {
                this.getInventory().sendInventoryModification(var12);
            }

            var18 = var13.entrySet().iterator();

            while(var18.hasNext()) {
                Entry var22;
                ((ShopInterface)(var22 = (Entry)var18.next()).getKey()).getShopInventory().sendInventoryModification((IntCollection)var22.getValue());
            }
        }

    }

    private void handlePlayerInBlackHoleSystem() {
        if (this.sectorBlackHoleEffectStart < 0L) {
            this.sectorBlackHoleEffectStart = System.currentTimeMillis();
            if (this.isClientOwnPlayer()) {
                ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_73, 0.0F);
            }
        }

        long var1 = System.currentTimeMillis();
        if (this.isClientOwnPlayer() && var1 - this.sectorBlackHoleEffectStart > 1000L) {
            ((GameClientState)this.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_74, 0.0F);
        }

    }

    private void handleReceivedInputEvents() {
        Arrays.fill(this.wasKeyDown, false);

        byte var2;
        for(int var1 = 0; var1 < this.keyboardEvents.size(); ++var1) {
            boolean var3 = (var2 = this.keyboardEvents.getByte(var1)) > 0;
            int var6 = Math.abs(var2) - 1;
            KeyboardMappings var7 = KeyboardMappings.values()[var6];

            for(int var4 = 0; var4 < KeyboardMappings.remoteMappings.length; ++var4) {
                if (KeyboardMappings.remoteMappings[var4] == var7) {
                    this.wasKeyDown[var4] = var3;
                    break;
                }
            }

            this.getControllerState().handleKeyEvent(var7);
        }

        this.keyboardEvents.clear();
        Iterator var5 = this.mouseEvents.iterator();

        while(var5.hasNext()) {
            var2 = (Byte)var5.next();
            MouseEvent var8;
            (var8 = new MouseEvent()).button = var2;
            var8.state = true;
            this.getControllerState().handleMouseEvent(var8);
        }

        this.mouseEvents.clear();
    }

    public void handleRoundEndFromNT(NetworkPlayer var1) {
        GameClientState var2;
        if (!this.isOnServer() && (var2 = (GameClientState)this.getState()).getPlayer() == this) {
            Iterator var9 = var1.roundEndBuffer.getReceiveBuffer().iterator();

            while(var9.hasNext()) {
                RemoteIntegerArray var3;
                int var4 = (Integer)(var3 = (RemoteIntegerArray)var9.next()).get(0).get();
                int var5 = (Integer)var3.get(1).get();
                int var10;
                if ((var10 = (Integer)var3.get(2).get()) < 0) {
                    System.out.println("[CLIENT][ROUNDEND] NOBODY HAD THE LAST KILL");
                } else {
                    System.out.println("[CLIENT][ROUNDEND] ENTITY " + var10 + " HAD THE LAST KILL");
                }

                boolean var11 = false;
                synchronized(var2.getPlayerInputs()) {
                    Iterator var7 = var2.getPlayerInputs().iterator();

                    while(true) {
                        if (!var7.hasNext()) {
                            break;
                        }

                        if ((DialogInterface)var7.next() instanceof RoundEndMenu) {
                            var11 = true;
                        }
                    }
                }

                if (!var11) {
                    RoundEndMenu var6 = new RoundEndMenu(var2, var4, var5);
                    var2.getPlayerInputs().add(var6);
                }
            }
        }

    }

    public void handleServerHealthAndCheckAliveOnServer(float var1, Damager var2) {
        if (this.isOnServer()) {
            if (this.isSpawnProtected()) {
                ((GameStateInterface)this.getState()).getGameState().getSpawnProtectionSec();
                System.err.println("[SERVER] player " + this + " was hit but is invulnerable from previous death " + this.getSpawnProtectionTimeLeft() / 1000L + " / " + this.getSpawnProtectionTimeSecsMax() + " sec");
                if (System.currentTimeMillis() - this.lastSetProtectionMsg > 3000L) {
                    this.sendServerMessage(new ServerMessage(new Object[]{385, this.getSpawnProtectionTimeLeft() / 1000L, this.getSpawnProtectionTimeSecsMax()}, 1, this.getId()));
                }

            } else {
                float var3 = this.health;
                this.health = Math.min(120.0F, var1);
                boolean var4 = var2 == this;
                if (var1 <= 0.0F && var3 > 0.0F && this.alive) {
                    String var6 = Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_76;
                    if (var2 != null) {
                        String var7 = "n/a";
                        if (var2 instanceof UniqueInterface) {
                            var7 = ((UniqueInterface)var2).getUniqueIdentifier();
                        }

                        var7 = var2.getName() + "[Faction=" + var2.getFactionId() + ", Owner=" + var2.getOwnerState() + ", UID={" + var7 + "}]";
                        PlayerState var5 = null;
                        if (var2 instanceof PlayerControllable) {
                            if (!((PlayerControllable)var2).getAttachedPlayers().isEmpty()) {
                                var5 = (PlayerState)((PlayerControllable)var2).getAttachedPlayers().get(0);
                            } else {
                                var6 = StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_77, new Object[]{var7});
                            }
                        } else if (var2 instanceof PlayerState) {
                            var5 = (PlayerState)var2;
                        } else {
                            var6 = StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_78, new Object[]{var7});
                        }

                        if (var5 != null) {
                            var6 = StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERSTATE_79, new Object[]{var7, var5.getHealth(), var5.getMaxHealth()});
                        }
                    }

                    LogUtil.log().fine("[DEATH] " + this.getName() + " has been killed by '" + var6 + "'; controllable: " + var2);
                    if (!var4) {
                        this.sendServerMessage(new ServerMessage(new Object[]{386, this.getName(), var6}, 2));
                    }

                    this.dieOnServer(var2);
                }

            }
        }
    }

    public void handleSpawnRequestFromNT(NetworkPlayer var1) {
        if (this.isOnServer() && !var1.spawnRequest.getReceiveBuffer().isEmpty()) {
            GameServerState var4;
            synchronized((var4 = (GameServerState)this.getState()).getSpawnRequests()) {
                var4.getSpawnRequests().add(this);
            }
        }
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object var1) {
        if (var1 != null && var1 instanceof PlayerState) {
            return this.getId() == ((PlayerState)var1).getId();
        } else {
            return false;
        }
    }

    private void hurtClientAnimation(Sendable var1) {
        if (this.isClientOwnPlayer()) {
            ((GameClientState)this.getState()).getController().onHurt(this, var1);
        }

    }

    public boolean isClientOwnPlayer() {
        if (this.isOnServer()) {
            return false;
        } else {
            return ((GameClientState)this.getState()).getPlayer() != null && ((GameClientState)this.getState()).getPlayer().getId() == this.getId();
        }
    }

    public boolean isDamageable(Damager var1) {
        Sector var2;
        if ((var2 = ((GameServerState)this.getState()).getUniverse().getSector(this.getCurrentSectorId())) != null && var2.isProtected()) {
            List var4 = ((PlayerControllable)var1).getAttachedPlayers();

            for(int var5 = 0; var5 < var4.size(); ++var5) {
                PlayerState var3 = (PlayerState)var4.get(var5);
                if (System.currentTimeMillis() - var3.lastSectorProtectedMsgSent > 5000L) {
                    var3.lastSectorProtectedMsgSent = System.currentTimeMillis();
                    var3.sendServerMessage(new ServerMessage(new Object[]{387}, 2, var3.getId()));
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public boolean isEnemy(PlayerState var1) {
        return this.factionController.isEnemy(var1);
    }

    public boolean isFriend(PlayerState var1) {
        return this.factionController.isFriend(var1);
    }

    public boolean isGodMode() {
        return this.godMode;
    }

    public void setGodMode(boolean var1) {
        this.godMode = var1;
    }

    public boolean isKeyDownOrSticky(KeyboardMappings var1) {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            if (((GameClientController)this.getState().getController()).isWindowActive()) {
                return this.canClientPressKey() ? var1.isDownOrSticky(this.getState()) : false;
            } else {
                return false;
            }
        } else {
            boolean var2 = var1.isNTKeyDown((Short)this.getNetworkObject().keyboardOfController.get());
            boolean var3 = this.wasKeyDown(var1);
            return var2 || var3;
        }
    }

    public void handleJoystickDir(Vector3f var1, Vector3f var2, Vector3f var3, Vector3f var4) {
        float var5;
        if (!this.isOnServer()) {
            if (JoystickMappingFile.ok()) {
                var5 = (float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.FORWARD_BACK);
                (var2 = new Vector3f(var2)).scale(var5);
                var1.add(var2);
                var5 = (float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.RIGHT_LEFT);
                (var2 = new Vector3f(var3)).scale(var5);
                var1.add(var2);
                var5 = (float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.UP_DOWN);
                (var2 = new Vector3f(var4)).scale(var5);
                var1.add(var2);
                return;
            }
        } else {
            var5 = this.getNetworkObject().frontBackAxis.getFloat();
            (var2 = new Vector3f(var2)).scale(var5);
            var1.add(var2);
            var5 = this.getNetworkObject().rightLeftAxis.getFloat();
            (var2 = new Vector3f(var3)).scale(var5);
            var1.add(var2);
            var5 = this.getNetworkObject().upDownAxis.getFloat();
            (var2 = new Vector3f(var4)).scale(var5);
            var1.add(var2);
        }

    }

    public void updateNTJoystick() {
        if (this.isClientOwnPlayer()) {
            this.getNetworkObject().frontBackAxis.forceClientUpdates();
            this.getNetworkObject().frontBackAxis.set((float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.FORWARD_BACK));
            this.getNetworkObject().upDownAxis.forceClientUpdates();
            this.getNetworkObject().upDownAxis.set((float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.UP_DOWN));
            this.getNetworkObject().rightLeftAxis.forceClientUpdates();
            this.getNetworkObject().rightLeftAxis.set((float)((GameClientState)this.state).getController().getJoystickAxis(JoystickAxisMapping.RIGHT_LEFT));
        }

    }

    public boolean wasMouseButtonDown(int var1) {
        return var1 >= 0 && var1 < this.wasMouseDown.length && this.wasMouseDown[var1];
    }

    public boolean wasMouseEventServer(int var1) {
        return var1 >= 0 && var1 < this.wasMouseEvent.length && this.wasMouseEvent[var1];
    }

    public boolean isMouseButtonDown(int var1) {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            return ((GameClientController)this.getState().getController()).isWindowActiveOutOfMenu() ? ((GameClientController)this.state.getController()).isMouseButtonDown(var1) : false;
        } else {
            return this.getNetworkObject().isMouseDown(var1) || this.wasMouseEventServer(var1);
        }
    }

    public boolean isMouseSwitched() {
        if (!this.isOnServer() && this.isClientOwnPlayer()) {
            return ((GameClientController)this.getState().getController()).isWindowActiveOutOfMenu() ? EngineSettings.C_MOUSE_BUTTON_SWITCH.isOn() : false;
        } else {
            return this.getNetworkObject().mouseSwitched.get();
        }
    }

    public boolean isNeutral(PlayerState var1) {
        return this.factionController.isNeutral(var1);
    }

    public boolean isVolatile() {
        return false;
    }

    public void modCreditsServer(long var1) {
        synchronized(this.creditModifications) {
            this.creditModifications.add(var1);
        }
    }

    public void notifyRoundEnded(int var1, int var2, Damager var3) {
        System.err.println("SERVER NOTIFYING OF ROUND END");
        RemoteIntegerArray var4;
        (var4 = new RemoteIntegerArray(3, this.getNetworkObject())).set(0, var1);
        var4.set(1, var2);
        var4.set(2, -1);
        this.getNetworkObject().roundEndBuffer.add(var4);
    }

    public void onDestroyedElement(SegmentPiece var1) {
        this.controllerState.onDestroyedElement(var1);
    }

    public void onVesselHit(Sendable var1) {
        this.hurtClientAnimation(var1);
    }

    public void printControllerState() {
        int var1 = 0;

        for(Iterator var2 = this.getControllerState().getUnits().iterator(); var2.hasNext(); ++var1) {
            ControllerStateUnit var3 = (ControllerStateUnit)var2.next();
            AbstractScene.infoList.add("+ " + var1 + " CONTR: " + this.getName() + "): " + var3);
        }

    }

    public void queueInventoryModification(IntOpenHashSet var1, long var2) {
        InventoryMultMod var5 = new InventoryMultMod(var1, this.getInventory(), var2);
        synchronized(this.queuedModifactions) {
            this.queuedModifactions.enqueue(var5);
        }
    }

    public void searchForLastEnteredEntity() {
        assert this.isOnServer();

        if (this.lastEnteredEntity != null && !this.lastEnteredEntity.isEmpty() && this.lastEnteredEntity.startsWith("ENTITY_")) {
            GameServerState var1 = (GameServerState)this.state;

            try {
                DatabaseEntry.getType(this.lastEnteredEntity);
                String var2 = DatabaseEntry.removePrefix(this.lastEnteredEntity);
                if (this.lastEnteredEntity != null && !this.lastEnteredEntity.equals("none")) {
                    try {
                        List var6;
                        if ((var6 = var1.getDatabaseIndex().getTableManager().getEntityTable().getByUIDExact(DatabaseIndex.escape(var2), 20)).isEmpty()) {
                            this.sendServerMessage(new ServerMessage(new Object[]{389, var2}, 3, this.getId()));
                        } else {
                            Iterator var7 = var6.iterator();

                            while(var7.hasNext()) {
                                DatabaseEntry var3 = (DatabaseEntry)var7.next();
                                this.sendServerMessage(new ServerMessage(new Object[]{390, var2, var3.sectorPos}, 1, this.getId()));
                            }

                        }
                    } catch (SQLException var4) {
                        var4.printStackTrace();
                    }
                } else {
                    this.sendServerMessage(new ServerMessage(new Object[]{391}, 3, this.getId()));
                }
            } catch (EntityTypeNotFoundException var5) {
                var5.printStackTrace();
                this.sendServerMessage(new ServerMessage(new Object[]{392}, 3, this.getId()));
            }
        } else {
            this.sendServerMessage(new ServerMessage(new Object[]{388, this.lastEnteredEntity == null ? "Not set" : this.lastEnteredEntity}, 3, this.getId()));
        }
    }

    private void sendKill(int var1) {
        this.getNetworkObject().killedBuffer.add(var1);
    }

    public void sendServerMessage(ServerMessage var1) {
        this.serverToSendMessages.add(var1);
    }

    public void sendServerMessagePlayerError(Object[] var1) {
        this.serverToSendMessages.add(new ServerMessage(var1, 3, this.getId()));
    }

    public void sendServerMessagePlayerInfo(Object[] var1) {
        this.serverToSendMessages.add(new ServerMessage(var1, 1, this.getId()));
    }

    public void sendServerMessagePlayerSimple(Object[] var1) {
        this.serverToSendMessages.add(new ServerMessage(var1, 0, this.getId()));
    }

    public void sendServerMessagePlayerWarning(Object[] var1) {
        this.serverToSendMessages.add(new ServerMessage(var1, 2, this.getId()));
    }

    public void sendSimpleCommand(SimplePlayerCommands var1, Object... var2) {
        this.getNetworkObject().simpleCommandQueue.add(new RemoteSimpelCommand(new SimplePlayerCommand(var1, var2), this.getNetworkObject()));
    }

    public void setAssignedPlayerCharacter(PlayerCharacter var1) {
        this.playerCharacter = var1;
    }

    public void setLastEnteredShip(UniqueInterface var1) {
        this.setLastEnteredEntity(var1.getUniqueIdentifier());
    }

    public void setNetworkPlayerObject(NetworkPlayer var1) {
        this.networkPlayerObject = var1;
    }

    public void suicideOnClient() {
        ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().exitShip(true);
        ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().setActive(false);
        this.sendKill(this.getId());
    }

    public void setLastOrientation() {
        this.getForward(this.lastForward);
        this.getUp(this.lastUp);
        this.getRight(this.lastRight);
    }

    public void setLastOrientation(Transform var1) {
        GlUtil.getForwardVector(this.lastForward, var1);
        GlUtil.getUpVector(this.lastUp, var1);
        GlUtil.getRightVector(this.lastRight, var1);
    }

    private void handleMetaBlueprintGive() {
        if (this.giveMetaBlueprint != null) {
            SimpleTransformableSendableObject var1;
            if ((var1 = this.getFirstControlledTransformableWOExc()) != null && var1 instanceof ShopperInterface) {
                ShopperInterface var6 = (ShopperInterface)var1;
                boolean var2 = false;
                boolean var3 = false;
                if (!var6.getShopsInDistance().isEmpty()) {
                    var2 = true;
                    var3 = ((ShopInterface)var6.getShopsInDistance().iterator().next()).isInfiniteSupply();
                } else if (this.getNetworkObject().isAdminClient.get()) {
                    var2 = true;
                    this.sendServerMessage(new ServerMessage(new Object[]{393}, 3, this.getId()));
                } else {
                    this.sendServerMessage(new ServerMessage(new Object[]{394}, 3, this.getId()));
                }

                if (var2) {
                    Faction var7 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId());
                    if (((GameStateInterface)this.getState()).getGameState().allowedToSpawnBBShips(this, var7)) {
                        try {
                            int var8 = this.getInventory().getFreeSlot();
                            BlueprintEntry var9;
                            if ((var9 = BluePrintController.active.getBlueprint(this.giveMetaBlueprint)).getType() == BlueprintType.SPACE_STATION && this.getCredits() < ((GameStateInterface)this.getState()).getGameState().getStationCost() && !var3) {
                                this.sendServerMessagePlayerError(new Object[]{395});
                            } else {
                                if (var9.getType() == BlueprintType.SPACE_STATION && !var3) {
                                    this.modCreditsServer((long)(-((GameStateInterface)this.getState()).getGameState().getStationCost()));
                                }

                                BlueprintMetaItem var10;
                                (var10 = (BlueprintMetaItem)MetaObjectManager.instantiate(MetaObjectType.BLUEPRINT, (short)-1, true)).blueprintName = this.giveMetaBlueprint;
                                var10.goal = new ElementCountMap(var9.getElementCountMapWithChilds());
                                var10.progress = new ElementCountMap();
                                this.getInventory().put(var8, var10);
                                this.sendInventoryModification(var8, -9223372036854775808L);
                            }
                        } catch (EntityNotFountException var4) {
                            var4.printStackTrace();
                            this.sendServerMessage(new ServerMessage(new Object[]{396, this.giveMetaBlueprint}, 3, this.getId()));
                        } catch (NoSlotFreeException var5) {
                            var5.printStackTrace();
                            this.sendServerMessage(new ServerMessage(new Object[]{397}, 3, this.getId()));
                        }
                    } else {
                        this.sendServerMessagePlayerError(new Object[]{398});
                    }
                }
            }

            this.giveMetaBlueprint = null;
        }

    }

    private void onClientChannelCreatedOnServer() {
        Iterator var1 = this.scanHistory.iterator();

        while(var1.hasNext()) {
            ScanData var2 = (ScanData)var1.next();
            this.getClientChannel().getNetworkObject().scanDataUpdates.add(new RemoteScanData(var2, this.isOnServer()));
        }

        var1 = this.savedCoordinates.iterator();

        while(var1.hasNext()) {
            SavedCoordinate var3 = (SavedCoordinate)var1.next();
            this.getClientChannel().getNetworkObject().savedCoordinates.add(new RemoteSavedCoordinate(var3, this.isOnServer()));
        }

        ((GameServerState)this.getState()).getUniverse().getGalaxyManager().sendAllFtlDataTo(this.getClientChannel().getNetworkObject());
        ((GameServerState)this.getState()).getUniverse().getGalaxyManager().sendAllTradeStubsTo(this.getClientChannel().getNetworkObject());
        ((GameServerState)this.state).getChannelRouter().onLogin(this.getClientChannel());
    }

    private void handleReceivedSavedCoordinates() {
        if (!this.savedCoordinatesToAdd.isEmpty()) {
            synchronized(this.savedCoordinatesToAdd) {
                while(true) {
                    while(true) {
                        while(!this.savedCoordinatesToAdd.isEmpty()) {
                            SavedCoordinate var2 = (SavedCoordinate)this.savedCoordinatesToAdd.dequeue();
                            if (this.isOnServer()) {
                                int var3;
                                if ((var3 = (Integer)ServerConfig.MAX_COORDINATE_BOOKMARKS.getCurrentState()) == 0) {
                                    this.sendServerMessagePlayerError(new Object[]{399});
                                } else if (this.savedCoordinates.size() >= var3 && !var2.isRemoveFlag()) {
                                    this.sendServerMessagePlayerError(new Object[]{400, var3});
                                } else if (var2.isRemoveFlag()) {
                                    this.savedCoordinates.remove(var2);
                                    this.getClientChannel().getNetworkObject().savedCoordinates.add(new RemoteSavedCoordinate(var2, this.isOnServer()));
                                } else if (!this.savedCoordinates.contains(var2)) {
                                    this.savedCoordinates.add(var2);
                                    this.getClientChannel().getNetworkObject().savedCoordinates.add(new RemoteSavedCoordinate(var2, this.isOnServer()));
                                }
                            } else {
                                if (var2.isRemoveFlag()) {
                                    this.savedCoordinates.remove(var2);
                                } else {
                                    this.savedCoordinates.add(var2);
                                }

                                if (this.savedCoordinatesList != null) {
                                    this.savedCoordinatesList.flagDirty();
                                }
                            }
                        }

                        return;
                    }
                }
            }
        }
    }

    public void updateProximitySectors() throws IOException {
        if (this.isOnServer()) {
            this.proximitySector.updateServer();
            this.proximitySystem.updateServer();
        }

    }

    private boolean wasKeyDown(KeyboardMappings var1) {
        for(int var2 = 0; var2 < this.wasKeyDown.length; ++var2) {
            if (var1 == KeyboardMappings.remoteMappings[var2] && this.wasKeyDown[var2]) {
                return true;
            }
        }

        return false;
    }

    public String getStarmadeName() {
        return this.starmadeName;
    }

    public void setStarmadeName(String var1) {
        this.starmadeName = var1;
    }

    public boolean isUpgradedAccount() {
        return this.upgradedAccount;
    }

    public void setUpgradedAccount(boolean var1) {
        this.upgradedAccount = var1;
    }

    public int getHelmetSlot() {
        return this.helmetSlot;
    }

    public void setHelmetSlot(int var1) {
        this.helmetSlot = var1;
    }

    public PlayerAiManager getPlayerAiManager() {
        return this.playerAiManager;
    }

    public ClientChannel getClientChannel() {
        return this.clientChannel;
    }

    public void setClientChannel(ClientChannel var1) {
        this.clientChannel = var1;
        if (var1 != null) {
            this.createdClientChannelOnServer = true;
        }

    }

    public Vector3i getUniqueSpawningSector(int var1) {
        return new Vector3i(2147483615, var1 << 4, 2147483615);
    }

    public Vector3i getUniqueTestingSector(int var1) {
        return new Vector3i(2147483615, (var1 << 4) + 8, 2147483615);
    }

    public boolean isSpectator() {
        Faction var1;
        return (var1 = ((FactionState)this.getState()).getFactionManager().getFaction(this.getFactionId())) != null && var1.isFactionMode(4);
    }

    public boolean isNewPlayerServer() {
        assert this.isOnServer();

        return this.newPlayerOnServer;
    }

    public boolean canRotate() {
        return this.isClientOwnPlayer() && Controller.getCamera() != null ? Controller.getCamera().isStable() : (Boolean)this.getNetworkObject().canRotate.get();
    }

    public byte getClientHitNotifaction() {
        return this.clientHitNotifaction;
    }

    public void setClientHitNotifaction(byte var1) {
        this.clientHitNotifaction = var1;
    }

    public long getThisLogin() {
        return this.spawnData.thisLogin;
    }

    public long getLastDeathNotSuicideFactionProt() {
        return this.lastDeathNotSuicide;
    }

    public Vector3i getCurrentSystem() {
        return this.currentSystem;
    }

    public List<ScanData> getScanHistory() {
        return this.scanHistory;
    }

    public void addScanHistory(ScanData var1) {
        this.scanHistory.add(var1);

        while(this.scanHistory.size() > 5) {
            this.scanHistory.remove(0);
        }

    }

    public boolean isFactionPointProtected() {
        return this.factionPointProtected;
    }

    public void setFactionPointProtected(boolean var1) {
        this.factionPointProtected = var1;
    }

    public void warpToTutorialSectorClient() {
        this.sendSimpleCommand(SimplePlayerCommands.WARP_TO_TUTORIAL_SECTOR);
    }

    public byte getFactionRights() {
        return this.factionController.getFactionRank();
    }

    public ObjectArrayList<SavedCoordinate> getSavedCoordinates() {
        return this.savedCoordinates;
    }

    public ObjectArrayFIFOQueue<SavedCoordinate> getSavedCoordinatesToAdd() {
        return this.savedCoordinatesToAdd;
    }

    public String getLastDiedMessage() {
        return this.lastDiedMessage;
    }

    public String getFactionName() {
        return this.factionController.getFactionName();
    }

    public PlayerChannelManager getPlayerChannelManager() {
        return this.playerChannelManager;
    }

    public boolean hasIgnored() {
        return this.ignored.size() > 0;
    }

    public String[] getIgnored() {
        return (String[])this.ignored.toArray(new String[this.ignored.size()]);
    }

    public boolean isIgnored(String var1) {
        return this.ignored.contains(var1.toLowerCase(Locale.ENGLISH));
    }

    public boolean isIgnored(PlayerState var1) {
        return this.isIgnored(var1.getName());
    }

    public void addIgnore(String var1) {
        if (!this.isIgnored(var1.toLowerCase(Locale.ENGLISH))) {
            this.ignored.add(var1.toLowerCase(Locale.ENGLISH));
        }

    }

    public void removeIgnore(String var1) {
        if (this.isIgnored(var1.toLowerCase(Locale.ENGLISH))) {
            this.ignored.remove(var1.toLowerCase(Locale.ENGLISH));
        }

    }

    public boolean isHasCreativeMode() {
        return this.hasCreativeMode;
    }

    public void setHasCreativeMode(boolean var1) {
        this.hasCreativeMode = var1;
    }

    public boolean isUseCreativeMode() {
        return this.useCreativeMode;
    }

    public void setUseCreativeMode(boolean var1) {
        this.useCreativeMode = var1;
    }

    public void setPersonalSectors() throws IOException {
        if (this.personalSector == null || this.testSector == null) {
            int var1 = 1;
            (new FileExt(GameServerState.DATABASE_PATH)).mkdirs();
            FileExt var2;
            if (!(var2 = new FileExt(GameServerState.DATABASE_PATH + "uniquePlayerCount")).exists()) {
                FileExt var3;
                if ((var3 = new FileExt("./.ipid")).exists()) {
                    FileUtil.copyFile(var3, var2);
                    var3.deleteOnExit();
                } else {
                    var2.createNewFile();
                }
            } else {
                DataInputStream var4;
                var1 = (var4 = new DataInputStream(new FileInputStream(var2))).readInt();
                ++var1;
                var4.close();
            }

            DataOutputStream var5;
            (var5 = new DataOutputStream(new FileOutputStream(var2))).writeInt(var1);
            var5.close();

            assert var1 > 0;

            this.personalSector = this.getUniqueSpawningSector(var1);
            this.testSector = this.getUniqueSpawningSector(var1);
        }

    }

    public ObjectArrayFIFOQueue<CreateDockRequest> getCreateDockRequests() {
        return this.createDockRequests;
    }

    public long getCurrentLag() {
        return this.currentLag;
    }

    public void setAlive(boolean var1) {
        this.alive = var1;
    }

    public boolean isUseCargoInventory() {
        return this.useCargoInventory;
    }

    public void setUseCargoInventory(boolean var1) {
        this.useCargoInventory = var1;
    }

    public void requestCargoInventoryChange(SegmentPiece var1) {
        if (var1 != null) {
            this.getNetworkObject().cargoInventoryChange.add(new RemoteSegmentControllerBlock(new VoidUniqueSegmentPiece(var1), this.getNetworkObject()));
        } else {
            VoidUniqueSegmentPiece var2;
            (var2 = new VoidUniqueSegmentPiece()).uniqueIdentifierSegmentController = "NONE";
            this.getNetworkObject().cargoInventoryChange.add(new RemoteSegmentControllerBlock(var2, this.getNetworkObject()));
        }
    }

    public void requestUseCargoInventory(boolean var1) {
        this.getNetworkObject().requestCargoMode.add(var1 ? 1 : 0);
    }

    private Inventory getCargoInventoryIfActive() {
        if (this.isUseCargoInventory()) {
            if (this.isInTutorial()) {
                if (this.isOnServer()) {
                    this.sendServerMessagePlayerError(new Object[]{401});
                    this.setUseCargoInventory(false);
                }
            } else if (this.cargoInventoryBlock != null) {
                Sendable var1;
                if ((var1 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getUidObjectMap().get(this.cargoInventoryBlock.uniqueIdentifierSegmentController)) != null && var1 instanceof SegmentController) {
                    this.cargoInventoryBlock.setSegmentController((SegmentController)var1);
                    if (!this.hasCargoBlockMigratedChunk16 && this.cargoInventoryBlock.getSegmentController().isLoadedFromChunk16()) {
                        this.cargoInventoryBlock.voidPos.add(Chunk16SegmentData.SHIFT);
                    }

                    this.hasCargoBlockMigratedChunk16 = true;
                    if (this.cargoInventoryBlock.getSegmentController().getSectorId() == this.getCurrentSectorId()) {
                        Inventory var2;
                        if (this.cargoInventoryBlock.getSegmentController() instanceof ManagedSegmentController && (var2 = ((ManagedSegmentController)this.cargoInventoryBlock.getSegmentController()).getManagerContainer().getInventory(this.cargoInventoryBlock.getAbsoluteIndex())) != null) {
                            return var2;
                        }

                        if (this.isOnServer()) {
                            this.sendServerMessagePlayerError(new Object[]{402});
                            this.setUseCargoInventory(false);
                        }
                    } else if (this.isOnServer()) {
                        this.sendServerMessagePlayerError(new Object[]{403});
                        this.setUseCargoInventory(false);
                    }
                } else if (this.isOnServer()) {
                    this.sendServerMessagePlayerError(new Object[]{404});
                    this.setUseCargoInventory(false);
                }
            } else if (this.isOnServer()) {
                this.sendServerMessagePlayerError(new Object[]{405});
                this.setUseCargoInventory(false);
            }
        }

        return null;
    }

    public boolean isInventoryPersonalCargo(StashInventory var1) {
        if (this.cargoInventoryBlock != null && var1.getInventoryHolder() instanceof ManagerContainer) {
            ManagerContainer var2;
            return (var2 = (ManagerContainer)var1.getInventoryHolder()).getSegmentController().getUniqueIdentifier().equals(this.cargoInventoryBlock.uniqueIdentifierSegmentController) && var2.getInventory(this.cargoInventoryBlock.getAbsoluteIndex()) == var1;
        } else {
            return false;
        }
    }

    public void handleReceivedBlockMsg(ManagerContainer<?> var1, ServerMessage var2) {
        assert !this.isOnServer();

        InventoryControllerManager var3;
        Inventory var4 = (var3 = ((GameClientState)this.getState()).getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager()).getSecondInventory();
        Transform var5;
        (var5 = new Transform()).setIdentity();
        Vector3i var6 = ElementCollection.getPosFromIndex(var2.block, new Vector3i());
        var5.origin.set((float)(var6.x - 16), (float)(var6.y - 16), (float)(var6.z - 16));
        var1.getSegmentController().getWorldTransform().transform(var5.origin);
        RaisingIndication var7;
        (var7 = new RaisingIndication(var5, StringTools.getFormatedMessage(var2.getMessage()), 1.0F, 0.3F, 0.3F, 1.0F)).speed = 0.1F;
        var7.lifetime = 4.6F;
        HudIndicatorOverlay.toDrawTexts.add(var7);
        if (var4 != null && var3.isTreeActive() && var4.getInventoryHolder() == var1 && var1.getInventory(var2.block) == var4) {
            var2.type = 3;
            ((GameClientState)this.getState()).getServerMessages().add(var2);
        }

    }

    public String toDetailedString() {
        return this.getName() + ", credits: " + this.getCredits() + "; Sector: " + this.getCurrentSector() + "; Invetory slots filled: " + this.inventory.getCountFilledSlots() + "; last login: " + this.getLastLogin() + "; last logout: " + this.getLastLogout() + "; Spawn data: " + this.spawnData;
    }

    public boolean isVisibleSectorServer(Vector3i var1) {
        return this.getFogOfWar().isVisibleSectorServer(var1);
    }

    public long getFogOfWarId() {
        return this.getDbId();
    }

    public void readDatabase() throws SQLException {
        assert this.isOnServer();

        this.dbId = ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getPlayerTable().getPlayerId(this);
    }

    public void updateDatabase() throws SQLException {
        assert this.isOnServer();

        ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getPlayerTable().getPlayerFactionAndPermission(this, this.offlinePermssion);
        ((GameServerState)this.getState()).getDatabaseIndex().getTableManager().getPlayerTable().updateOrInsertPlayer(this);
    }

    public long getDbId() {
        return this.dbId;
    }

    public void setDbId(long var1) {
        this.dbId = var1;
    }

    public FogOfWarController getFogOfWar() {
        FogOfWarController var1;
        return (var1 = this.factionController.getFactionFow()) != null ? var1 : this.fow;
    }

    public void onChangedFactionServer(FactionChange var1) {
        Faction var2 = ((GameServerState)this.getState()).getFactionManager().getFaction(var1.from);
        Faction var3 = ((GameServerState)this.getState()).getFactionManager().getFaction(var1.to);
        if (var2 != null && (var1.previousPermission & PermType.FOG_OF_WAR_SHARE.value) == PermType.FOG_OF_WAR_SHARE.value) {
            this.fow.merge(var2);
        }

        if (var3 != null) {
            var3.getFogOfWar().merge(this);
        }

        if (var2 == null && var1.from != 0 || var2 != null && var2.getMembersUID().isEmpty()) {
            try {
                ((GameServerState)this.state).getDatabaseIndex().getTableManager().getVisibilityTable().clearVisibility((long)var1.from);
                return;
            } catch (SQLException var4) {
                var4.printStackTrace();
            }
        }

    }

    public long getFactionPermission() {
        return this.factionController.getFactionPermission();
    }

    public Set<Vector3i> getLastVisitedSectors() {
        return this.lastVisitedSectors;
    }

    public void sendFowResetToClient(Vector3i var1) {
        assert this.isOnServer();

        this.getNetworkObject().resetFowBuffer.add(new RemoteVector3i(var1, true));
    }

    public int getInputBasedSeed() {
        return this.inputBasedSeed;
    }

    public boolean isInfiniteInventoryVolume() {
        return this.infiniteInventoryVolume;
    }

    public void setInfiniteInventoryVolume(boolean var1) {
        this.infiniteInventoryVolume = var1;
    }

    public int getSectorId() {
        return this.currentSectorId;
    }

    public int getMineAutoArmSeconds() {
        return this.mineAutoArmSecs;
    }

    public void requestMineArmTimerChange(int var1) {
        this.getNetworkObject().mineArmTimerRequests.add(var1);
    }

    public void lastSpawnedThisSession(long var1) {
        this.lastSpawnedThisSession = var1;
    }

    private int getSpawnProtectionTimeSecsMax() {
        return ((GameStateInterface)this.getState()).getGameState().getSpawnProtectionSec();
    }

    private long getSpawnProtectionTimeLeft() {
        return this.lastSpawnedThisSession + (long)(this.getSpawnProtectionTimeSecsMax() * 1000) - this.getState().getUpdateTime();
    }

    public boolean isSpawnProtected() {
        return this.getSpawnProtectionTimeLeft() > 0L;
    }

    public void sendServerMessage(Object[] var1, int var2) {
        this.sendServerMessage(new ServerMessage(var1, var2));
    }

    public void forcePlayerIntoEntity(Sector var1, String var2) {
        this.forcedEnterSector = var1;
        this.forcedEnterUID = var2;
    }

    public BuildModePosition getBuildModePosition() {
        return this.buildModePosition;
    }

    public Cockpit getCockpit() {
        return this.cockpit;
    }

    public PlayerRuleEntityManager getRuleEntityManager() {
        return this.ruleEntityManager;
    }
}
