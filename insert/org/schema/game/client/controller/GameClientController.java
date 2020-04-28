//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.controller;

import api.utils.StarRunnable;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.schema.common.TimeStatistics;
import org.schema.common.XMLTools;
import org.schema.common.util.StringInterface;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.GlobalGameControlManager;
import org.schema.game.client.controller.manager.ingame.InGameControlManager;
import org.schema.game.client.controller.manager.ingame.PlayerGameControlManager;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.controller.manager.ingame.ship.ShipExternalFlightController;
import org.schema.game.client.controller.tutorial.DynamicTutorialStateMachine;
import org.schema.game.client.controller.tutorial.TutorialMode;
import org.schema.game.client.controller.tutorial.newtut.TutorialController;
import org.schema.game.client.data.ClientGameData;
import org.schema.game.client.data.ClientMessageLogEntry;
import org.schema.game.client.data.ClientMessageLogType;
import org.schema.game.client.data.ClientStatics;
import org.schema.game.client.data.CollectionManagerChangeListener;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateControllerInterface;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.data.SectorChange;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.client.view.MainGameGraphics;
import org.schema.game.client.view.WorldDrawer;
import org.schema.game.client.view.gui.BigMessage;
import org.schema.game.client.view.gui.BigTitleMessage;
import org.schema.game.client.view.gui.GUIPopupInterface;
import org.schema.game.client.view.gui.LoadingScreenDetailed;
import org.schema.game.client.view.gui.lagStats.LagDataStatsEntry;
import org.schema.game.client.view.gui.lagStats.LagObject;
import org.schema.game.client.view.gui.shiphud.newhud.PopupMessageNew;
import org.schema.game.client.view.gui.transporter.TransporterDestinations;
import org.schema.game.client.view.shards.ShardDrawer;
import org.schema.game.common.Starter;
import org.schema.game.common.api.SessionNewStyle;
import org.schema.game.common.controller.CreatorThreadControlInterface;
import org.schema.game.common.controller.CreatorThreadController;
import org.schema.game.common.controller.ElementHandlerInterface;
import org.schema.game.common.controller.FloatingRock;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopInterface;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.damage.projectile.ProjectileController;
import org.schema.game.common.controller.elements.ElementCollectionCalculationThreadExecution;
import org.schema.game.common.controller.elements.ElementCollectionCalculationThreadManager;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.PulseController;
import org.schema.game.common.controller.elements.ShipyardManagerContainerInterface;
import org.schema.game.common.controller.elements.TransporterModuleInterface;
import org.schema.game.common.controller.elements.UsableElementManager;
import org.schema.game.common.controller.elements.mines.MineController;
import org.schema.game.common.controller.elements.shipyard.ShipyardCollectionManager;
import org.schema.game.common.controller.elements.transporter.TransporterCollectionManager;
import org.schema.game.common.controller.gamemodes.GameModes;
import org.schema.game.common.controller.io.IOFileManager;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.meta.MetaObject;
import org.schema.game.common.data.missile.ClientMissileManager;
import org.schema.game.common.data.missile.MissileControllerInterface;
import org.schema.game.common.data.missile.MissileManagerInterface;
import org.schema.game.common.data.physics.GamePhysicsObject;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.player.ControllerState;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.SimplePlayerCommands;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionRelation.RType;
import org.schema.game.common.data.player.faction.config.FactionConfig;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentDataIntArray;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.common.util.Collisionable;
import org.schema.game.common.util.GuiErrorHandler;
import org.schema.game.common.util.StarMadeCredentials;
import org.schema.game.common.version.Version;
import org.schema.game.network.StarMadePlayerStats;
import org.schema.game.network.StarMadeServerStats;
import org.schema.game.network.commands.AdminCommand;
import org.schema.game.network.commands.CreateNewShip;
import org.schema.game.network.commands.KillCharacter;
import org.schema.game.network.commands.RequestBlockBehaviour;
import org.schema.game.network.commands.RequestBlockConfig;
import org.schema.game.network.commands.RequestBlockProperties;
import org.schema.game.network.commands.RequestCustomTextures;
import org.schema.game.network.commands.RequestFactionConfig;
import org.schema.game.network.commands.RequestGameMode;
import org.schema.game.network.commands.RequestInventoriesUnblocked;
import org.schema.game.network.commands.RequestPlayerStats;
import org.schema.game.network.commands.RequestServerStats;
import org.schema.game.network.objects.ChatMessage;
import org.schema.game.server.data.Galaxy;
import org.schema.game.server.data.admin.AdminCommandIllegalArgument;
import org.schema.game.server.data.admin.AdminCommandQueueElement;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.schine.ai.stateMachines.FSMException;
import org.schema.schine.common.JoystickAxisMapping;
import org.schema.schine.common.TextCallback;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.AbstractScene;
import org.schema.schine.graphicsengine.core.ChatListener;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.core.settings.StateParameterNotFoundException;
import org.schema.schine.graphicsengine.forms.debug.DebugDrawer;
import org.schema.schine.graphicsengine.forms.gui.ColoredTimedText;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIResizableGrabbableWindow;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.ButtonColorImpl;
import org.schema.schine.graphicsengine.util.WorldToScreenConverterFixedAspect;
import org.schema.schine.input.BasicInputController;
import org.schema.schine.input.JoystickEvent;
import org.schema.schine.input.JoystickMappingFile;
import org.schema.schine.input.KeyEventInterface;
import org.schema.schine.input.Keyboard;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.input.Mouse;
import org.schema.schine.network.ChatSystem;
import org.schema.schine.network.IdGen;
import org.schema.schine.network.NetUtil;
import org.schema.schine.network.ServerInfo;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.SynchronizationContainerController;
import org.schema.schine.network.client.ClientController;
import org.schema.schine.network.client.HostPortLoginName;
import org.schema.schine.network.client.KBMapInterface;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.objects.remote.RemoteBoolean;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.resource.FileExt;
import org.schema.schine.sound.AudioEntity;
import org.schema.schine.sound.pcode.SoundManager;
import org.xml.sax.SAXException;

public class GameClientController extends ClientController implements MineInterface, GameStateControllerInterface, CreatorThreadControlInterface, ElementHandlerInterface, MissileControllerInterface {
    public static final boolean IS_LOCALHOST = false;
    public static final int SHIP_SPAM_PROTECT_TIME_SEC = 5;
    public static final ObjectOpenHashSet<String> fileList = new ObjectOpenHashSet();
    public static WorldToScreenConverterFixedAspect worldToScreenConverter = new WorldToScreenConverterFixedAspect();
    private static boolean started;
    public static boolean availableGUI;
    private final GameClientState state;
    private final SynchronizationContainerController synchController;
    private final SynchronizationContainerController privateChannelSynchController;
    private final HashSet<SectorChange> sectorChanges = new HashSet();
    private final TextureSynchronizer textureSynchronizer;
    private final CreatorThreadController creatorThreadController;
    long lastLagList;
    private final ClientGameData clientGameData;
    private final ArrayList<SendableSegmentController> cleanUps = new ArrayList();
    private final ClientMissileManager clientMissileManager;
    public Object requestLock = new Object();
    public boolean flagWaypointUpdate;
    public Vector3i lastSector = new Vector3i();
    int startUpVolume = 0;
    private TutorialMode tutorialMode;
    private boolean tutorialStarted;
    private GameClientController.ConnectionDialogUpdater connectionDialogUpdater;
    private long lastCleanUp;
    private boolean firstUpdate = true;
    private final MineController mineController;
    private ClientChannel clientChannel;
    private boolean flagRecalc;
    private ObjectArrayList<SimpleTransformableSendableObject> flagSectorCleanup = new ObjectArrayList();
    private long lastMemorySample;
    private boolean resynched;
    public long lastShipSpawn;
    private boolean soundFadeIn;
    private ElementCollectionCalculationThreadManager elementCollectionCalculationThreadManager = new ElementCollectionCalculationThreadManager(false);
    private boolean flagReapplyBlockBehavior;
    private String lastClientStartVersion;
    private Vector3i lastSystem = new Vector3i(-2147483648, -2147483648, -2147483648);
    private SegmentController notSent;
    private Map<String, PlayerState> onlinePlayerMapHelper = new Object2ObjectOpenHashMap();
    private ObjectArrayFIFOQueue<String> queuedDialogs = new ObjectArrayFIFOQueue();
    private final BasicInputController inputController = new BasicInputController();
    private final TutorialController tutorialController;
    private final List<ShopInterface> shopInterfaces = new ObjectArrayList();
    private boolean wasJoystickRightButtonDown = false;
    private short wasJoystickRightButtonDownNum = 0;
    private boolean wasJoystickLeftButtonDown = false;
    private short wasJoystickLeftButtonDownNum = 0;
    private final List<ClientSectorChangeListener> sectorChangeListeners = new ObjectArrayList();
    private final List<ClientSystemChangeListener> systemChangeListeners = new ObjectArrayList();
    private final List<EntitySelectionChangeChangeListener> entitySelectionListeners = new ObjectArrayList();
    private final List<EntityTrackingChangedListener> entityTrackingListeners = new ObjectArrayList();
    private boolean trackingChanged;
    private final List<CollectionManagerChangeListener> collectionManagerChangeListeners = new ObjectArrayList();
    private final List<SendableAddedRemovedListener> sendableAddRemoveListener = new ObjectArrayList();
    public final GameClientController.SectorChangeObservable sectorChangeObservable = new GameClientController.SectorChangeObservable();
    private List<TransporterDestinations> transporterDestinations = new ObjectArrayList();
    private List<SegmentController> possibleFleet = new ObjectArrayList();
    private float zoom = 0.0F;
    public GraphicsContext graphicsContext;
    private boolean wasWindowActive = false;
    private short wasWindowActiveUNum = 0;
    private boolean wasWindowActiveOutOfMenu = false;
    private short wasWindowActiveOutOfMenuUNum = 0;

    public GameClientController(GameClientState var1, GraphicsContext var2, Observer var3) throws NoSuchAlgorithmException, IOException {
        super(var1);
        this.graphicsContext = var2;
        setStarted(true);
        this.state = var1;
        updateFileList();
        this.tutorialController = new TutorialController(var1);
        this.connectionDialogUpdater = new GameClientController.ConnectionDialogUpdater();
        if (var3 != null) {
            System.err.println("[CLIENT] Added observer for GameClientController");
            this.connectionDialogUpdater.addObserver(var3);
        } else {
            System.err.println("[CLIENT] Exception: No Client Dialog Added. Game will probably crash");
        }

        this.clientMissileManager = new ClientMissileManager(var1);
        this.initializeState();
        this.creatorThreadController = new CreatorThreadController(var1);
        if (!var1.isPassive()) {
            this.creatorThreadController.start();
        }

        this.textureSynchronizer = new TextureSynchronizer(var1);
        this.synchController = new SynchronizationContainerController(var1.getLocalAndRemoteObjectContainer(), var1, false);
        this.clientGameData = new ClientGameData(var1);
        if (EngineSettings.PLAYER_SKIN.getCurrentState().toString().trim().length() > 0) {
            this.textureSynchronizer.setModelPath(EngineSettings.PLAYER_SKIN.getCurrentState().toString().trim());
        }

        if (!var1.isPassive()) {
            this.elementCollectionCalculationThreadManager.start();
            this.inputController.initialize();
        }

        this.privateChannelSynchController = new SynchronizationContainerController(var1.getPrivateLocalAndRemoteObjectContainer(), var1, true);
        this.mineController = new MineController(var1);
        EngineSettings.G_DRAW_GUI_ACTIVE.setCurrentState(true);
        EngineSettings.G_DRAW_NO_OVERLAYS.setCurrentState(false);
        FileExt var5 = new FileExt("lastClientStartVerion.txt");
        this.lastClientStartVersion = "0.0.0";
        BufferedWriter var7;
        if (!var5.exists()) {
            (var7 = new BufferedWriter(new FileWriter(var5))).append(String.valueOf(Version.VERSION));
            var7.close();
        } else {
            try {
                BufferedReader var6 = new BufferedReader(new FileReader(var5));
                this.lastClientStartVersion = var6.readLine();
                var6.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            (var7 = new BufferedWriter(new FileWriter(var5))).append(String.valueOf(Version.VERSION));
            var7.close();
        }
    }

    public static boolean exists(String var0) {
        int var1;
        if ((var1 = (var0 = new String(var0)).lastIndexOf(File.separator)) >= 0) {
            var0 = var0.substring(var1 + 1);
        }

        synchronized(fileList) {
            return fileList.contains(var0);
        }
    }

    public static void updateFileList() {
        synchronized(fileList) {
            fileList.clear();
            FileExt var1;
            if (!(var1 = new FileExt(ClientStatics.SEGMENT_DATA_DATABASE_PATH)).exists()) {
                var1.mkdirs();
            }

            String[] var4 = var1.list();

            for(int var2 = 0; var2 < var4.length; ++var2) {
                fileList.add(var4[var2]);
            }

        }
    }

    public static String autocompletePlayer(StateInterface var0, String var1) {
        ArrayList var2 = new ArrayList();
        Iterator var6;
        synchronized(var0.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            var6 = var0.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

            while(var6.hasNext()) {
                Sendable var4;
                if ((var4 = (Sendable)var6.next()) instanceof PlayerState) {
                    var2.add((PlayerState)var4);
                }
            }
        }

        String var3 = StringTools.autoComplete(var1, var2, true, new StringInterface<PlayerState>() {
            public final String get(PlayerState var1) {
                return var1.getName().toLowerCase(Locale.ENGLISH);
            }
        });
        var6 = var2.iterator();

        while(var6.hasNext()) {
            PlayerState var7;
            if ((var7 = (PlayerState)var6.next()).getName().toLowerCase(Locale.ENGLISH).startsWith(var3)) {
                var3 = var7.getName().substring(0, var3.length());
            }
        }

        return var3;
    }

    public static String findCorrectedCommand(String var0) {
        ArrayList var1 = new ArrayList();
        boolean var2 = false;
        AdminCommands[] var3;
        int var4 = (var3 = AdminCommands.values()).length;

        int var5;
        for(var5 = 0; var5 < var4; ++var5) {
            String var7 = var3[var5].name().toLowerCase(Locale.ENGLISH);
            int var6 = StringUtils.getLevenshteinDistance(var0, var7, 4);
            System.err.println(var6 + " " + var0 + " " + var7);
            if (var6 == 0) {
                var2 = true;
            }

            if (var6 > 0) {
                var1.add(new GameClientController.StringDistance(var7, var6));
            }
        }

        if (var2) {
            return "";
        } else if (var1.isEmpty()) {
            return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_48, new Object[]{var0});
        } else {
            Collections.sort(var1, new Comparator<GameClientController.StringDistance>() {
                public final int compare(GameClientController.StringDistance var1, GameClientController.StringDistance var2) {
                    return var1.distance - var2.distance;
                }
            });
            int var8 = Math.min(var1.size(), 5);
            StringBuffer var9;
            (var9 = new StringBuffer()).append(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_49).append(" ");
            var5 = 0;
            Iterator var10 = var1.iterator();

            while(var10.hasNext()) {
                GameClientController.StringDistance var11 = (GameClientController.StringDistance)var10.next();
                var9.append(var11.string);
                if (var5 < var8 - 1) {
                    var9.append(", ");
                }

                ++var5;
                if (var5 >= var8) {
                    break;
                }
            }

            return Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_50 + " " + var9.toString();
        }
    }

    public void afterFullResynchronize() {
        synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var2 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

            while(var2.hasNext()) {
                Sendable var3;
                if ((var3 = (Sendable)var2.next()) instanceof SendableGameState) {
                    this.getState().setGameState((SendableGameState)var3);
                }
            }

        }
    }

    public void onShutDown() {
        System.out.println("[CLIENT] CLIENT SHUTDOWN. Dumping client data!");

        try {
            this.writeSegmentDataToDatabase(false);
            System.out.println("[CLIENT] CLIENT SHUTDOWN. client data saved!");
            EngineSettings.write();
            GUIResizableGrabbableWindow.write();
            this.getState().getThreadPoolLogins().shutdown();
            this.getState().getThreadPoolLogins().awaitTermination(3L, TimeUnit.SECONDS);
            System.out.println("[CLIENT] CLIENT SHUTDOWN. thread pool terminated!");
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    protected void onLogin() throws IOException, InterruptedException {
        try {
            System.out.println("[CLIENT] executing login hooks...");
            this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_0);
            this.getState().setGameMode(this.requestGameMode());
            boolean var1 = false;
            boolean var2 = false;
            boolean var3 = false;
            boolean var4 = false;
            boolean var5 = false;
            String var6 = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + ".xml";
            String var7 = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + ".properties";
            String var8 = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + "-faction.xml";
            String var9 = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + "-block-behavior.xml";
            String var10 = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + "-custom-textures" + File.separator;
            ClientStatics.SEGMENT_DATA_DATABASE_PATH = ClientStatics.ENTITY_DATABASE_PATH + this.getConnection().getHost() + File.separator + "DATA" + File.separator;
            File var10000 = new File(ClientStatics.SEGMENT_DATA_DATABASE_PATH);
            FileExt var11 = null;
            var10000.mkdirs();
            String var12;
            boolean var13;
            byte[] var23;
            if (!this.getState().receivedBlockConfigChecksum.equals(this.getState().getBlockConfigCheckSum())) {
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_41);
                }

                System.err.println("[Client] Config Checksum check failed: ");
                System.err.println("Remote: " + this.getState().receivedBlockConfigChecksum);
                System.err.println("Local:  " + this.getState().getBlockConfigCheckSum());
                var11 = new FileExt(var6);
                var12 = null;
                var13 = false;

                try {
                    var13 = !var11.exists() || !(var12 = FileUtil.getSha1ChecksumZipped(var6)).equals(this.getState().receivedBlockConfigChecksum);
                } catch (Exception var14) {
                    var14.printStackTrace();
                }

                if (var13) {
                    if (var11.exists()) {
                        System.err.println("Cached block config File existed. Checksums failed");
                        System.err.println("Remote: " + this.getState().receivedBlockConfigChecksum);
                        System.err.println("Chached:  " + var12);
                        System.err.println("Local-default:  " + this.getState().getBlockConfigCheckSum());
                        var11.delete();
                    }

                    this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_1);
                    System.err.println("[CLIENT] Detected modified server config");
                    var23 = this.requestBlockConfig();
                    GZIPOutputStream var26;
                    (var26 = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(var6)))).write(var23);
                    var26.flush();
                    var26.close();
                } else {
                    System.err.println("[Client] Found valid blockConfig cache file for this server in " + var11.getAbsolutePath());
                }

                var1 = true;
            }

            BufferedOutputStream var29;
            if (!this.getState().receivedFactionConfigChecksum.equals(this.getState().getFactionConfigCheckSum())) {
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_44);
                }

                System.err.println("[Client] Faction Config Checksum check failed: ");
                System.err.println("Remote: " + this.getState().receivedFactionConfigChecksum);
                System.err.println("Local:  " + this.getState().getFactionConfigCheckSum());
                var11 = new FileExt(var8);
                var12 = null;

                try {
                    var13 = !var11.exists() || !(var12 = FileUtil.getSha1ChecksumZipped(var8)).equals(this.getState().receivedFactionConfigChecksum);
                } catch (Exception var16) {
                    var16.printStackTrace();
                    System.err.println("[Client][FactionConfig] TRYING UNZIPPED CHECKSUM");

                    try {
                        var13 = !var11.exists() || !(var12 = FileUtil.getSha1Checksum(var8)).equals(this.getState().receivedFactionConfigChecksum);
                    } catch (Exception var15) {
                        var15.printStackTrace();
                        var13 = true;
                    }
                }

                if (var13) {
                    if (var11.exists()) {
                        System.err.println("Cached faction config File existed. Checksums failed");
                        System.err.println("Remote: " + this.getState().receivedFactionConfigChecksum);
                        System.err.println("Chached:  " + var12);
                        System.err.println("Local-default:  " + this.getState().getFactionConfigCheckSum());
                        var11.delete();
                    }

                    this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_34);
                    System.err.println("[CLIENT] Detected modified server faction config");
                    var23 = this.requestFactionConfig();
                    (var29 = new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(var8)))).write(var23);
                    var29.flush();
                    var29.close();
                } else {
                    System.err.println("[Client] Found valid blockConfig cache file for this server in " + var11.getAbsolutePath());
                }

                var2 = true;
            }

            byte[] var27;
            if (!this.getState().receivedBlockConfigPropertiesChecksum.equals(this.getState().getConfigPropertiesCheckSum())) {
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_45);
                }

                System.err.println("[Client] Config Checksum check failed: ");
                System.err.println("Remote: " + this.getState().receivedBlockConfigPropertiesChecksum);
                System.err.println("Local:  " + this.getState().getConfigPropertiesCheckSum());
                if ((var11 = new FileExt(var7)).exists() && FileUtil.getSha1Checksum(var7).equals(this.getState().receivedBlockConfigPropertiesChecksum)) {
                    System.err.println("[Client] Found valid blockProperties cache file for this server in " + var11.getAbsolutePath());
                } else {
                    if (var11.exists()) {
                        System.err.println("Cached properties File existed. Checksums failed ");
                        System.err.println("Remote: " + FileUtil.getSha1Checksum(var7));
                        System.err.println("Local:  " + this.getState().getConfigPropertiesCheckSum());
                        var11.delete();
                    }

                    this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_3);
                    System.err.println("[CLIENT] Detected modified server block properties");
                    var27 = this.requestBlockProperties();
                    (var29 = new BufferedOutputStream(new FileOutputStream(var7))).write(var27);
                    var29.flush();
                    var29.close();
                }

                var3 = true;
            }

            this.parseBlockBehavior("./data/config/blockBehaviorConfig.xml");
            if (!this.getState().receivedBlockBehaviorChecksum.equals(this.getState().getBlockBehaviorCheckSum())) {
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_46);
                }

                System.err.println("[Client] Block behavior Checksum check failed: ");
                System.err.println("Remote: " + this.getState().receivedBlockBehaviorChecksum);
                System.err.println("Local:  " + this.getState().getBlockBehaviorCheckSum());
                if ((var11 = new FileExt(var9)).exists() && FileUtil.getSha1Checksum(var9).equals(this.getState().receivedBlockBehaviorChecksum)) {
                    System.err.println("[Client] Found valid blockProperties cache file for this server in " + var11.getAbsolutePath());
                } else {
                    if (var11.exists()) {
                        System.err.println("Cached Block behavior File existed. Checksums failed ");
                        System.err.println("Remote: " + FileUtil.getSha1Checksum(var9));
                        System.err.println("Local:  " + this.getState().getBlockBehaviorCheckSum());
                        var11.delete();
                    }

                    this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_4);
                    System.err.println("[CLIENT] Detected modified server Block behavior ");
                    var27 = this.requestBlockBehavior();
                    (var29 = new BufferedOutputStream(new FileOutputStream(var9))).write(var27);
                    var29.flush();
                    var29.close();
                }

                var4 = true;
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_47);
                }
            }

            if (!this.getState().receivedCustomTexturesChecksum.equals(this.getState().getCustomTexturesCheckSum())) {
                System.err.println("[Client] Custom textures Checksum check failed: ");
                System.err.println("Remote: " + this.getState().receivedCustomTexturesChecksum);
                System.err.println("Local:  " + this.getState().getCustomTexturesCheckSum());
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_42);
                }

                String var24 = FileUtil.createFilesHashRecursively(var10, new FileFilter() {
                    public boolean accept(File var1) {
                        return var1.isDirectory() || var1.getName().toLowerCase(Locale.ENGLISH).endsWith(".png");
                    }
                });
                FileExt var28;
                if ((var28 = new FileExt(var10 + "pack.zip")).exists() && var24.equals(this.getState().receivedCustomTexturesChecksum)) {
                    System.err.println("[Client] Found valid custom textures cache file for this server in " + var28.getAbsolutePath());
                } else {
                    if (var28.exists()) {
                        System.err.println("Cached Custom textures File existed. Checksums failed ");
                        System.err.println("Remote: " + this.getState().receivedCustomTexturesChecksum);
                        System.err.println("Local:  " + var24);
                        var28.delete();
                    } else {
                        (new FileExt(var10)).mkdirs();
                    }

                    this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_5);
                    if (this.graphicsContext != null) {
                        this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_36);
                    }

                    System.err.println("[CLIENT] Detected modified server custom textures");
                    byte[] var30 = this.requestCustomTextures();
                    System.err.println("[CLIENT] Custom Block Textures successfully received (bytes: " + var30.length + ")");
                    BufferedOutputStream var25;
                    (var25 = new BufferedOutputStream(new FileOutputStream(var10 + "pack.zip"))).write(var30);
                    var25.flush();
                    var25.close();
                }

                var5 = true;
                if (this.graphicsContext != null) {
                    this.graphicsContext.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_40);
                }
            }

            if (var5) {
                FileUtil.extract(new FileExt(var10 + "pack.zip"), var10);
                EngineSettings.CLIENT_CUSTOM_TEXTURE_PATH.setCurrentState(var10 + GameResourceLoader.CUSTOM_TEXTURE_PATH);
                System.err.println("[CLIENT] set custom texture path to: " + EngineSettings.CLIENT_CUSTOM_TEXTURE_PATH.getCurrentState());
            }

            if (this.getConnection().getHost().toLowerCase(Locale.ENGLISH).equals("localhost")) {
                System.err.println("[CLIENT] RESET TO LOCAL CUSTOM TEXTURES");
                EngineSettings.CLIENT_CUSTOM_TEXTURE_PATH.setCurrentState("./customBlockTextures");
            }

            if (var4) {
                this.state.getController().parseBlockBehavior(var9);
            } else {
                System.err.println("[CLIENT] Server is using default block behavior: CSUM: remote: " + this.getState().receivedBlockConfigPropertiesChecksum + " ::: local: " + this.getState().getConfigPropertiesCheckSum());
            }

            if (var3) {
                ElementKeyMap.reparseProperties(var7);
            } else {
                System.err.println("[CLIENT] Server is using default block properties");
            }

            if (var2) {
                FactionConfig.load(this.state, var8);
            }

            if (var1) {
                ElementKeyMap.reinitializeData(new FileExt(var6), true, var3 ? var7 : null, (File)null);
            } else {
                System.err.println("[CLIENT] Server is using default block config");
            }
        } catch (Exception var17) {
            throw new RuntimeException(var17);
        }

        FileExt var18;
        if (!(var18 = new FileExt(ClientStatics.ENTITY_DATABASE_PATH + "version")).exists()) {
            System.out.println("[CLIENT] PURGE NEEDED: No Database Info");
            this.purgeCompleteDB();
        } else {
            BufferedReader var19;
            String var21 = (var19 = new BufferedReader(new FileReader(var18))).readLine();
            var19.close();
            if (var21 != null && !var21.equals(this.getConnection().getHost())) {
                System.out.println("[CLIENT] PURGE NEEDED: Playing on another host: was: " + var21 + "; now: " + this.getConnection().getHost());
                this.purgeCompleteDB();
            }
        }

        FileExt var20;
        if (!(var20 = new FileExt(ClientStatics.ENTITY_DATABASE_PATH)).exists()) {
            var20.mkdir();
        }

        BufferedWriter var22;
        (var22 = new BufferedWriter(new FileWriter(var18))).append(this.getConnection().getHost());
        var22.flush();
        var22.close();
        System.out.println("[CLIENT] finished executing client hooks: " + this.getState().getBlockBehaviorConfig());

        assert this.getState().getBlockBehaviorConfig() != null;

    }

    protected void onResynchRequest() {
        this.resynched = true;
        System.err.println("[CLIENT] ################################# RESYNCHRONIZED");
    }

    public void setGuiConnectionState(String var1) {
        this.connectionDialogUpdater.update(var1);
    }

    public void setGuiConnectionDispose() {
        System.err.println("[CLIENT] DISPOSING CONNECTION DIALOG");
        this.connectionDialogUpdater.dispose();
    }

    public void alertMessage(String var1) {
        this.popupAlertTextMessage(var1, 0.0F);
    }

    public void kick(String var1) {
        GLFrame.processErrorDialogException(new LogoutException(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_43, new Object[]{var1})), this.getState());
        if (this.getState().getGlFrame() != null) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }

            this.getState().getGlFrame().dispose();
        }

    }

    public boolean isJoystickKeyboardButtonDown(KBMapInterface var1) {
        return this.inputController.getJoystick().isKeyboardButtonDown((KeyboardMappings)var1);
    }

    public boolean isJoystickOk() {
        return JoystickMappingFile.ok();
    }

    public void flagTrackingChanged() {
        this.trackingChanged = true;
    }

    public void removeCollectionManagerChangeListener(CollectionManagerChangeListener var1) {
        this.collectionManagerChangeListeners.remove(var1);
    }

    public void addCollectionManagerChangeListener(CollectionManagerChangeListener var1) {
        this.collectionManagerChangeListeners.add(var1);
    }

    public void notifyCollectionManagerChanged(ElementCollectionManager<?, ?, ?> var1) {
        for(int var2 = 0; var2 < this.collectionManagerChangeListeners.size(); ++var2) {
            ((CollectionManagerChangeListener)this.collectionManagerChangeListeners.get(var2)).onChange(var1);
        }

    }

    public void removeSendableAddedRemovedListener(SendableAddedRemovedListener var1) {
        this.sendableAddRemoveListener.remove(var1);
    }

    public void addSendableAddedRemovedListener(SendableAddedRemovedListener var1) {
        this.sendableAddRemoveListener.add(var1);
    }

    public void removeEntityTrackingListener(EntityTrackingChangedListener var1) {
        this.entityTrackingListeners.remove(var1);
    }

    public void addEntityTrackingListener(EntityTrackingChangedListener var1) {
        this.entityTrackingListeners.add(var1);
    }

    public void addSectorChangeListener(ClientSectorChangeListener var1) {
        this.sectorChangeListeners.add(var1);
    }

    public void addSystemChangeListener(ClientSystemChangeListener var1) {
        this.systemChangeListeners.add(var1);
    }

    public void removeEntitySelectionChangeListener(EntitySelectionChangeChangeListener var1) {
        this.entitySelectionListeners.remove(var1);
    }

    public void addEntitySelectionChangeListener(EntitySelectionChangeChangeListener var1) {
        this.entitySelectionListeners.add(var1);
    }

    public boolean isJoystickMouseRigthButtonDown() {
        if (this.wasJoystickRightButtonDownNum != this.getState().getNumberOfUpdate()) {
            this.wasJoystickRightButtonDown = this.inputController.getJoystick().getRightMouse().isDown();
            this.wasJoystickRightButtonDownNum = this.getState().getNumberOfUpdate();
        }

        return this.wasJoystickRightButtonDown;
    }

    public boolean isJoystickMouseLeftButtonDown() {
        if (this.wasJoystickLeftButtonDownNum != this.getState().getNumberOfUpdate()) {
            this.wasJoystickLeftButtonDown = this.inputController.getJoystick().getLeftMouse().isDown();
            this.wasJoystickLeftButtonDownNum = this.getState().getNumberOfUpdate();
        }

        return this.wasJoystickLeftButtonDown;
    }

    public double getJoystickAxis(JoystickAxisMapping var1) {
        return this.inputController.getJoystick().getAxis(var1);
    }

    public void update(Timer var1) {
        this.getState().getDebugTimer().start(this.getState());
        ShardDrawer.shardsAddedFromNTBlocks = Math.max(0, ShardDrawer.shardsAddedFromNTBlocks - 1);
        this.getState().updateTime = var1.currentTime;
        ServerInfo.curtime = this.getState().updateTime;
        GameClientState.requestQueue = 0;
        GameClientState.requestedSegments = 0;
        GameClientState.returnedRequests = 0;
        if (GameClientState.allocatedSegmentData > 0) {
            GameClientState.lastAllocatedSegmentData = GameClientState.allocatedSegmentData;
        }

        GameClientState.lastFreeSegmentData = this.getState().getSegmentDataManager().sizeFree();
        GameClientState.allocatedSegmentData = 0;
        this.getState().incUpdateNumber();
        GameClientState.collectionUpdates = 0;
        this.state.getParticleSystemManager().update(this.getState().getPhysics(), var1);
        if (this.getState().isFlagRequestServerTime()) {
            try {
                this.requestServerTime();
            } catch (IOException var24) {
                var24.printStackTrace();
                throw new RuntimeException(var24);
            }

            this.getState().resetFlagRequestServerTime();
            this.getState().setLastServerTimeRequest(System.currentTimeMillis());
        }

        PlayerGameOkCancelInput var3;
        if (this.getState().getCharacter() != null && this.getState().getController().getPlayerInputs().isEmpty()) {
            while(!this.queuedDialogs.isEmpty()) {
                String var2 = (String)this.queuedDialogs.dequeue();
                (var3 = new PlayerGameOkCancelInput("DASASS", this.getState(), 500, 300, "Server Message", var2) {
                    public void pressedOK() {
                        this.deactivate();
                    }

                    public void onDeactivate() {
                    }
                }).getInputPanel().setCancelButton(false);
                var3.getInputPanel().setOkButton(true);
                var3.activate();
            }
        }

        if (this.getState().isFlagPlayerReceived()) {
            this.getState().setFlagPlayerReceived(false);
            ClientChannel var34;
            (var34 = new ClientChannel(this.getState())).initialize();
            var34.setId(0);
            var34.setPlayerId(this.getState().getPlayer().getId());
            this.setClientChannel(var34);
            this.getPrivateChannelSynchController().addNewSynchronizedObjectQueued(var34);
        }

        if (this.getClientChannel().isConnectionReady()) {
            synchronized(this.getState()) {
                this.getState().setSynched();
                this.getTextureSynchronizer().synchronize();
                this.getClientChannel().updateLocal(var1);
                this.getState().getChannelRouter().update(var1);
                this.getState().setUnsynched();
            }
        }

        String var37;
        if ((Float)this.getState().getGameState().getNetworkObject().serverShutdown.get() > 0.0F) {
            var37 = StringTools.formatCountdown(this.getState().getGameState().getNetworkObject().serverShutdown.get().intValue());
            this.showBigTitleMessage("shutdown", StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_51, new Object[]{var37}), 0.0F);
        }

        if ((Float)this.getState().getGameState().getNetworkObject().serverCountdownTime.get() > 0.0F) {
            var37 = StringTools.formatCountdown(this.getState().getGameState().getNetworkObject().serverCountdownTime.get().intValue());
            this.showBigTitleMessage("countdown", (String)this.getState().getGameState().getNetworkObject().serverCountdownMessage.get() + " " + var37, 0.0F);
        }

        if (this.resynched) {
            this.getState().getWorldDrawer().clearAll();
        }

        if (!this.soundFadeIn) {
            if (this.startUpVolume == 0) {
                this.startUpVolume = (int)SoundManager.musicVolume;
                SoundManager.musicVolume = 0.0F;
            } else if (SoundManager.musicVolume < (float)this.startUpVolume) {
                ++SoundManager.musicVolume;
            } else {
                this.soundFadeIn = true;
            }
        }

        if (Controller.soundChanged) {
            System.err.println("[CLIENT] Sound settings changed");
            Controller.getAudioManager().onSoundOptionsChanged();
            Controller.soundChanged = false;
        }

        SimpleTransformableSendableObject var4;
        Iterator var40;
        if (!this.state.isPassive() && EngineSettings.S_SOUND_SYS_ENABLED.isOn()) {
            if (Controller.getAudioManager().isRecalc()) {
                Iterator var35 = Controller.getAudioManager().getCurrentEntities().iterator();

                while(var35.hasNext()) {
                    AudioEntity var39 = (AudioEntity)var35.next();
                    if (!this.getState().getCurrentSectorEntities().containsValue(var39)) {
                        Controller.getAudioManager().stopEntitySound(var39);
                        var35.remove();
                    }
                }

                var40 = this.getState().getCurrentSectorEntities().values().iterator();

                while(var40.hasNext()) {
                    if ((var4 = (SimpleTransformableSendableObject)var40.next()) instanceof AudioEntity && !Controller.getAudioManager().getCurrentEntities().contains((AudioEntity)var4)) {
                        Controller.getAudioManager().getCurrentEntities().add((AudioEntity)var4);
                        Controller.getAudioManager().startEntitySound((AudioEntity)var4);
                    }
                }

                Controller.getAudioManager().setRecalc(false);
            }

            Controller.getAudioManager().update(var1);
        } else if (Controller.getAudioManager() != null) {
            Controller.getAudioManager().getCurrentEntities().size();
        }

        if (!this.state.isPassive() && Controller.getCamera() != null) {
            Controller.getAudioManager().setListener(Controller.getCamera(), 1.0F);
        }

        long var5;
        if (!this.flagSectorCleanup.isEmpty()) {
            ObjectArrayList var36;
            (var36 = new ObjectArrayList()).addAll(this.flagSectorCleanup);
            long var42 = System.currentTimeMillis();
            this.scheduleWriteDataPush(var36);
            if ((var5 = System.currentTimeMillis() - var42) > 50L) {
                System.err.println("[CLIENT] WARNING: Sector Clean ups schedule data push took " + var5);
            }

            this.flagSectorCleanup.clear();
        }

        if (this.flagWaypointUpdate) {
            this.flagWaypointUpdate = false;
            this.clientGameData.updateNearest(this.getState().getCurrentSectorId());
        }

        long var38;
        if (!this.cleanUps.isEmpty() && System.currentTimeMillis() - this.lastCleanUp > 100L) {
            var38 = System.currentTimeMillis();
            synchronized(this.cleanUps) {
                SendableSegmentController var41 = (SendableSegmentController)this.cleanUps.remove(this.cleanUps.size() - 1);
                if (!this.getState().getCurrentSectorEntities().containsKey(var41.getId())) {
                    var41.getSegmentBuffer().clear(false);
                    if (var41 instanceof ManagedSegmentController) {
                        ((ManagedSegmentController)var41).getManagerContainer().clear();
                    }

                    ((ClientSegmentProvider)var41.getSegmentProvider()).clearRequestedBuffers();
                }
            }

            this.lastCleanUp = System.currentTimeMillis();
            if ((var5 = System.currentTimeMillis() - var38) > 50L) {
                System.err.println("[CLIENT] WARNING: Sector Clean ups took " + var5);
            }
        }

        var38 = System.currentTimeMillis();
        DebugDrawer.clear();
        long var43;
        if ((var43 = System.currentTimeMillis() - var38) > 50L) {
            System.err.println("[CLIENT] WARNING: DebugCleanup Took " + var43);
        }

        if (!this.getState().isNetworkSynchronized()) {
            this.popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_6, 0.0F);
        }

        var38 = System.currentTimeMillis();
        synchronized(this.getState()) {
            this.getState().setSynched();
            this.getSynchController().handleQueuedSynchronizedObjects();
            this.getPrivateChannelSynchController().handleQueuedSynchronizedObjects();
            this.getState().setUnsynched();
        }

        if ((var43 = System.currentTimeMillis() - var38) > 50L) {
            System.err.println("[CLIENT] queued synchronized objects " + var43);
        }

        if (System.currentTimeMillis() - this.lastMemorySample > 300L) {
            GameClientState.totalMemory = Runtime.getRuntime().totalMemory();
            GameClientState.freeMemory = Runtime.getRuntime().freeMemory();
            GameClientState.takenMemory = GameClientState.totalMemory - GameClientState.freeMemory;
            this.lastMemorySample = System.currentTimeMillis();
        }

        this.updateAmbientSound(var1);
        if (!this.getState().getServerMessages().isEmpty()) {
            synchronized(this.getState().getServerMessages()) {
                label1000:
                while(true) {
                    ServerMessage var44;
                    do {
                        if (this.getState().getServerMessages().isEmpty()) {
                            break label1000;
                        }
                    } while((var44 = (ServerMessage)this.getState().getServerMessages().remove(0)).receiverPlayerId > 0 && var44.receiverPlayerId != this.getState().getPlayer().getId());

                    switch(var44.type) {
                        case 0:
                            this.getState().chat(this.getState().getChat(), StringTools.getFormatedMessage(var44.getMessage()), "[MESSAGE]", false);
                            break;
                        case 1:
                            this.popupInfoTextMessage(StringTools.getFormatedMessage(var44.getMessage()), 0.0F);
                            break;
                        case 2:
                            this.popupGameTextMessage(StringTools.getFormatedMessage(var44.getMessage()), 0.0F);
                            break;
                        case 3:
                            this.popupAlertTextMessage(StringTools.getFormatedMessage(var44.getMessage()), 0.0F);
                        case 4:
                        default:
                            break;
                        case 5:
                            this.popupDialogMessage(StringTools.getFormatedMessage(var44.getMessage()));
                    }
                }
            }
        }

        if (this.firstUpdate) {
            this.firstUpdate = false;
        }

        if (!this.state.isPassive()) {
            this.getState().getWorldDrawer().getBuildModeDrawer().update(var1);
        }

        this.getState().getDebugTimer().start("STATE");
        TimeStatistics.reset("#state");
        synchronized(this.getState()) {
            try {
                this.getState().setSynched();
                this.getTutorialController().update(var1);
                if (this.getState().exportingShip != null && this.getState().exportingShip.done) {
                    FileExt var45 = new FileExt(this.getState().exportingShip.path + "/" + this.getState().exportingShip.name);
                    this.popupGameTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_39, new Object[]{var45.getAbsolutePath()}), 0.0F);
                    this.getState().exportingShip = null;
                }

                if (this.trackingChanged) {
                    var40 = this.entityTrackingListeners.iterator();

                    while(var40.hasNext()) {
                        ((EntityTrackingChangedListener)var40.next()).onTrackingChanged();
                    }

                    this.trackingChanged = false;
                }

                Iterator var46;
                Sendable var47;
                if (var1.currentTime - this.lastLagList > 1000L) {
                    this.getState().laggyList.clear();
                    ObjectArrayList var49 = new ObjectArrayList();
                    var46 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                    while(var46.hasNext()) {
                        if ((var47 = (Sendable)var46.next()).getCurrentLag() > 0L) {
                            this.getState().laggyList.add(var47);
                            var49.add(new LagObject(var47));
                        }
                    }

                    this.getState().lagStats.add(0, new LagDataStatsEntry(var1.currentTime, var49));
                    this.getState().lagStats.clearAllBefore(var1.currentTime - 240000L);
                    this.getState().notifyLaggyListChanged();
                    this.lastLagList = var1.currentTime;
                }

                this.updateActiveControllers();

                int var48;
                try {
                    ColoredTimedText.blink.update(var1);
                    synchronized(this.getState().getVisibleChatLog()) {
                        for(var48 = 0; var48 < this.getState().getVisibleChatLog().size(); ++var48) {
                            ((ColoredTimedText)this.getState().getVisibleChatLog().get(var48)).update(var1);
                        }
                    }
                } catch (IndexOutOfBoundsException var27) {
                    var3 = null;
                    var27.printStackTrace();
                }

                if (this.getState().getCharacter() != null && !this.lastSystem.equals(this.getState().getPlayer().getCurrentSystem()) && this.getState().getCurrentClientSystem() != null) {
                    this.onSystemChange(this.lastSystem, this.getState().getPlayer().getCurrentSystem());
                }

                GameClientState.staticSector = this.state.getCurrentSectorId();
                if (!this.getState().getToRequestMetaObjects().isEmpty() && this.getClientChannel() != null && this.getClientChannel().isConnectionReady()) {
                    synchronized(this.getState().getToRequestMetaObjects()) {
                        while(!this.getState().getToRequestMetaObjects().isEmpty()) {
                            var48 = this.getState().getToRequestMetaObjects().dequeueInt();

                            assert var48 >= 0 : var48;

                            this.getClientChannel().requestMetaObject(var48);
                        }
                    }
                }

                synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                    while(!this.state.unloadedInventoryUpdates.isEmpty()) {
                        ((ManagerContainer)this.state.unloadedInventoryUpdates.dequeue()).handleInventoryReceivedNT();
                    }

                    this.applySectorChanges();
                    if (this.getState().isFlagSectorChange() >= 0) {
                        this.onSectorChangeSelf(this.getState().getCurrentSectorId(), this.getState().isFlagSectorChange());
                        this.getState().setFlagSectorChange(-1);
                    }

                    if (this.flagRecalc) {
                        var43 = System.currentTimeMillis();
                        this.recalcCurrentEntities();
                        long var6;
                        if ((var6 = System.currentTimeMillis() - var43) > 50L) {
                            System.err.println("[CLIENT] WARNING: Sector Entity recalc took " + var6);
                        }
                    }

                    if (this.getClientChannel().isConnectionReady()) {
                        this.getClientMissileManager().updateClient(var1, this.getClientChannel());
                    }

                    this.getState().doUpdates();
                    this.getState().getParticleController().update(var1);
                    this.getState().getPulseController().update(var1);
                    AbstractScene.infoList.add("# UParticles: " + this.getState().getParticleController().getParticleCount());
                    if (this.getState().isWaitingForPlayerActivate()) {
                        var46 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

                        while(var46.hasNext()) {
                            PlayerCharacter var51;
                            if ((var47 = (Sendable)var46.next()) instanceof PlayerCharacter && (var51 = (PlayerCharacter)var47).getClientOwnerId() == this.getState().getId()) {
                                if (var51.checkClintSpawnSanity(this.state.getCurrentSectorEntities())) {
                                    this.getState().setCharacter(var51);
                                    this.getState().getPlayer().setAssignedPlayerCharacter(var51);
                                    this.getState().setShip((Ship)null);
                                    this.requestControlChange((PlayerControllable)null, this.getState().getCharacter(), (Vector3i)null, new Vector3i(), false);
                                    this.getState().getGlobalGameControlManager().getIngameControlManager().getFreeRoamController().setActive(false);
                                    this.getState().getGlobalGameControlManager().getIngameControlManager().getAutoRoamController().setActive(false);
                                    this.getState().setWaitingForPlayerActivate(false);
                                    if (!this.getState().getPlayer().getNetworkObject().isAdminClient.get() && EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn()) {
                                        EngineSettings.P_PHYSICS_DEBUG_ACTIVE.changeBooleanSetting(false);
                                        System.err.println("physics debug: " + EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn());
                                        this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_7, new Object[]{EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn()}), 0.0F);
                                    }

                                    this.tutorialStarted = true;
                                    this.getState().setPlayerSpawned(true);
                                    System.err.println("[CLIENT] client spawned character and player: " + var51 + "; " + this.state.getPlayer());
                                } else {
                                    this.showBigMessage("WA", Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_60, StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_61, new Object[]{var51.waitingForToSpawn.size(), this.state.getPlayer().getCurrentSector().toStringPure()}), 0.0F);
                                    var51.waitingForToSpawn.clear();
                                }
                                break;
                            }
                        }
                    }

                    this.state.spotlights.clear();
                    this.mineController.updateLocal(var1);
                    this.getState().getMetaObjectManager().updateLocal(var1);
                    var46 = this.getState().getCurrentSectorEntities().values().iterator();

                    while(var46.hasNext()) {
                        SimpleTransformableSendableObject var50 = (SimpleTransformableSendableObject)var46.next();
                        this.getState().getDebugTimer().start(var50);
                        if (var50 instanceof SegmentController) {
                            ((SegmentController)var50).getSegmentBuffer().updateNumber();
                        }

                        label950: {
                            if (var50.getSectorId() == this.getState().getCurrentSectorId()) {
                                if (var50 instanceof PlayerCharacter && !var50.isHidden() && ((PlayerCharacter)var50).getFlashLightActive() != null) {
                                    this.state.spotlights.add((PlayerCharacter)var50);
                                }

                                if (var50.getPhysicsDataContainer().getObject() != null && var50.getPhysicsDataContainer().getObject() instanceof RigidBody) {
                                    if (var50 instanceof SegmentController) {
                                        ((RigidBody)var50.getPhysicsDataContainer().getObject()).setDamping(((SegmentController)var50).getLinearDamping(), ((SegmentController)var50).getRotationalDamping());
                                    } else {
                                        ((RigidBody)var50.getPhysicsDataContainer().getObject()).setDamping(this.getState().getLinearDamping(), this.getState().getRotationalDamping());
                                    }
                                }

                                assert var50.getPhysicsDataContainer().getObject() == null || var50.getPhysicsDataContainer().getObject() instanceof GamePhysicsObject;

                                if (!var50.getRemoteTransformable().isSnapped()) {
                                    break label950;
                                }

                                System.err.println("[CLIENT] applied server-snap to " + var50);
                            } else {
                                if (!var50.getRemoteTransformable().isSnapped()) {
                                    break label950;
                                }

                                System.err.println("[CLIENT] applying server-snap to " + var50 + ": NOT IN SECTOR; Out of reach of physics");
                                if (var50.getPhysicsDataContainer().isInitialized()) {
                                    var50.getPhysicsDataContainer().updatePhysical(this.getState().getUpdateTime());
                                }
                            }

                            var50.getRemoteTransformable().setSnapped(false);
                        }

                        this.getState().getDebugTimer().start(var50, "update");
                        var50.updateLocal(var1);
                        this.getState().getDebugTimer().end(var50, "update");
                        this.getState().getDebugTimer().end(var50);
                    }

                    this.getState().getDebugTimer().start("OTHERS");
                    this.updateOthers(var1);
                    this.getState().getDebugTimer().end("OTHERS");
                    if (this.flagReapplyBlockBehavior) {
                        this.reapplyBlockConfigInstantly();
                        this.flagReapplyBlockBehavior = false;
                    }
                }

                this.getState().getDebugTimer().start("STATETONT");
                this.getState().getGameState().updateToNetworkObject();
                this.getState().getDebugTimer().end("STATETONT");
                var40 = this.getState().getCurrentSectorEntities().values().iterator();

                while(var40.hasNext()) {
                    (var4 = (SimpleTransformableSendableObject)var40.next()).getPhysicsDataContainer().lastTransform.set(var4.getPhysicsDataContainer().thisTransform);
                    var4.getPhysicsDataContainer().thisTransform.set(var4.getWorldTransform());
                }
            } finally {
                this.getState().setUnsynched();
            }
        }

        this.getState().getDebugTimer().end("STATE");
        this.getState().getDebugTimer().start("PHYSICS");
        if (!this.state.isPassive()) {
            TimeStatistics.reset("#Physics");
            this.getState().getPhysics().update(var1, this.getState().getHighestSubStep());
            TimeStatistics.set("#Physics");
            if (this.getState().getCharacter() != null && !this.getState().getCharacter().actionUpdate) {
                this.getState().getCharacter().inPlaceAttachedUpdate(this.getState().getUpdateTime());
            }
        }

        this.getState().getDebugTimer().end("PHYSICS");
        this.getState().getDebugTimer().start("CAMERA");
        synchronized(this.getState()) {
            this.getState().setSynched();
            boolean var10000 = !this.state.isPassive() && this.getState().getWorldDrawer().getGameMapDrawer().updateCamera(var1);
            boolean var52 = false;
            if (!var10000 && Controller.getCamera() != null && this.getState().getScene() != null) {
                this.getState().getScene().updateCurrentCamera(var1);
                Controller.getCamera().lookAt(false);
            }

            if (!this.state.isPassive()) {
                this.getState().getGlobalGameControlManager().update(var1);
            }

            this.getState().setUnsynched();
        }

        this.getState().getDebugTimer().end("CAMERA");
        TimeStatistics.set("#state");
        if (TimeStatistics.get("#state") > 50L) {
            System.err.println("[CLIENT-CONTROLLER][WARNING] state update took " + TimeStatistics.get("#state") + "ms");
        }

        this.getState().setHighestSubStep(0.0F);
        if (!this.state.isPassive()) {
            if (this.state.getWorldDrawer() != null && TimeStatistics.get("#Physics") > (long)(Integer)EngineSettings.G_DEBRIS_THRESHOLD_SLOW_MS.getCurrentState()) {
                this.state.getWorldDrawer().getShards().slow = Math.min((Integer)EngineSettings.G_DEBRIS_THRESHOLD_SLOW_MS.getCurrentState(), (int)Math.ceil((double)TimeStatistics.get("#Physics") / 2.0D));
            } else {
                this.state.getWorldDrawer().getShards().slow = 0;
            }
        }

        if (TimeStatistics.get("#Physics") > 30L) {
            System.err.println("[CLIENT] WARNING: Physics took " + TimeStatistics.get("#Physics"));
        }

        this.getState().getDebugTimer().start("ADDANDREMOVE");
        this.getState().handleFlaggedAddedOrRemovedObjects();
        this.getState().handleNeedsNotifyObjects();
        this.getState().getDebugTimer().end("ADDANDREMOVE");
        this.getState().getDebugTimer().start("SYNCH");

        try {
            super.updateSynchronization();
        } catch (IOException var21) {
            var21.printStackTrace();
            throw new RuntimeException(var21);
        }

        this.getState().getDebugTimer().end("SYNCH");
        if (System.currentTimeMillis() - this.getState().getLastServerTimeRequest() > (long)(Integer)EngineSettings.N_SERVERTIME_UPDATE_FREQUENCY.getCurrentState()) {
            this.getState().flagRequestServerTime();
        }

        if (this.getState().isDbPurgeRequested()) {
            this.purgeDB();
            this.getState().setDbPurgeRequested(false);
        }

        if (this.getState().isInWarp() && Math.abs(this.getState().getWarpedUpdateNr() - this.getState().getNumberOfUpdate()) > 2) {
            this.getState().setWarped(false);
        }

        //INSERTED CODE @1502
        //Prioritize server ticks on localhost
        if(!isLocalHost()) {
            StarRunnable.tickAll();
        }
        ///

        this.getState().getDebugTimer().end();
    }

    public void onSystemChange(Vector3i var1, Vector3i var2) {
        VoidSystem var3;
        String var4;
        if ((var3 = this.getState().getCurrentClientSystem()).getOwnerFaction() == 0) {
            var4 = Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_52;
        } else if (this.state.getFactionManager().existsFaction(var3.getOwnerFaction())) {
            Faction var7 = this.state.getFactionManager().getFaction(var3.getOwnerFaction());
            int var5 = this.getState().getPlayer().getFactionId();
            RType var6 = this.getState().getFactionManager().getRelation(var5, var7.getIdFaction());
            if (var7.getIdFaction() == var5) {
                var4 = StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_53, new Object[]{var7.getName()});
            } else if (var6 == RType.NEUTRAL) {
                var4 = StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_54, new Object[]{var7.getName()});
            } else if (var6 == RType.FRIEND) {
                var4 = StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_55, new Object[]{var7.getName()});
            } else if (var6 == RType.ENEMY) {
                var4 = StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_56, new Object[]{var7.getName()});
            } else {
                var4 = Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_57;
            }
        } else {
            var4 = Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_58;
        }

        if (!this.getState().getPlayer().isInTutorial() && !this.getState().getPlayer().isInTestSector() && !this.getState().getPlayer().isInPersonalSector()) {
            this.showBigMessage("SystemChanged" + var3.getPos(), StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_59, new Object[]{var3.getPos().toStringPure()}), var4, 0.0F);
        }

        Iterator var8 = this.systemChangeListeners.iterator();

        while(var8.hasNext()) {
            ((ClientSystemChangeListener)var8.next()).onSystemChanged(var1, var2);
        }

        this.lastSystem.set(this.getState().getPlayer().getCurrentSystem());
        this.lastSector.set(this.getState().getPlayer().getCurrentSector());
    }

    private void popupDialogMessage(String var1) {
        this.queuedDialogs.enqueue(var1);
    }

    public void flagCurrentEntitiesRecalc() {
        this.flagRecalc = true;
    }

    public void updateStateInput(Timer var1) {
        synchronized(this.getState()) {
            this.getState().setSynched();
            this.inputController.updateInput(this, var1);
            if (hasGraphics(this.state) && Controller.getCamera() != null) {
                this.getState().getWorldDrawer().getGameMapDrawer().updateMouse();
            }

            this.getState().setUnsynched();
        }
    }

    public boolean allowedToActivate(SegmentPiece var1) {
        SegmentController var2;
        if ((var2 = var1.getSegmentController()).isOwnerSpecific(this.getState().getPlayer())) {
            return true;
        } else {
            Vector3i var3 = new Vector3i();
            Vector3i var4 = new Vector3i();
            var1.getAbsolutePos(var3);
            boolean var5 = false;

            for(int var6 = 0; var6 < 6; ++var6) {
                var4.add(var3, Element.DIRECTIONSi[var6]);
                SegmentPiece var7;
                if ((var7 = var2.getSegmentBuffer().getPointUnsave(var4)) != null && (var7.getType() == 346 || var7.getType() == 936 && var7.getSegmentController().getFactionId() == this.getState().getPlayer().getFactionId())) {
                    var5 = true;
                    break;
                }
            }

            if (!var5 && this.state.getGameState().getLockFactionShips() && var2.getFactionId() != 0 && var2.getFactionId() != this.getState().getPlayer().getFactionId() && this.getState().getFactionManager().getFaction(var2.getFactionId()) != null) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_8, 0.0F);
                return false;
            } else if (!var5 && this.state.getGameState().getLockFactionShips() && var2.getElementClassCountMap().get((short)291) > 0 && var2.getFactionId() == this.getState().getPlayer().getFactionId() && !var1.getSegmentController().isSufficientFactionRights(this.state.getPlayer())) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_35, 0.0F);
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean allowedToConnect(SegmentController var1) {
        if (var1.getFactionId() != 0 && var1.getFactionId() != this.getState().getPlayer().getFactionId() && this.getState().getFactionManager().getFaction(var1.getFactionId()) != null) {
            this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_9, "sbtaf", 0.0F);
            return false;
        } else if (!this.getState().isAdmin() && !var1.allowedToEdit(this.getState().getPlayer())) {
            if (var1.getFactionId() != this.getState().getPlayer().getFactionId() || this.getState().getPlayer().getFactionRights() >= var1.getFactionRights() && var1.getFactionRights() != -1) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_11, "alrmovSSr1", 0.0F);
            } else {
                this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_2, new Object[]{this.getState().getPlayer().getFactionRankName(var1.getFactionRights())}), "alrmovSSr0", 0.0F);
            }

            return false;
        } else {
            if (this.getState().isAdmin() && !var1.allowedToEdit(this.getState().getPlayer())) {
                if (!Segment.ALLOW_ADMIN_OVERRIDE) {
                    if (var1.getFactionId() != this.getState().getPlayer().getFactionId() || this.getState().getPlayer().getFactionRights() >= var1.getFactionRights() && var1.getFactionRights() != -1) {
                        this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_70, "alrmovSSr1", 0.0F);
                    } else {
                        this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_10, new Object[]{this.getState().getPlayer().getFactionRankName(var1.getFactionRights())}), "alrmovSSr0", 0.0F);
                    }

                    return false;
                }

                if (this.notSent != var1) {
                    this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_12, "alrmovr2", 0.0F);
                    this.notSent = var1;
                }
            }

            return true;
        }
    }

    public boolean allowedToEdit(SegmentController var1) {
        if (var1.isOwnerSpecific(this.getState().getPlayer())) {
            return true;
        } else if (!this.getState().isAdmin() && !var1.allowedToEdit(this.getState().getPlayer())) {
            if (var1.getFactionId() != this.getState().getPlayer().getFactionId() || this.getState().getPlayer().getFactionRights() >= var1.getFactionRights() && var1.getFactionRights() != -1) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_67, "alrmovSSr1", 0.0F);
            } else {
                this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_69, new Object[]{this.getState().getPlayer().getFactionRankName(var1.getFactionRights())}), "alrmovSSr0", 0.0F);
            }

            return false;
        } else {
            if (this.getState().isAdmin() && !var1.allowedToEdit(this.getState().getPlayer())) {
                if (!Segment.ALLOW_ADMIN_OVERRIDE) {
                    if (var1.getFactionId() != this.getState().getPlayer().getFactionId() || this.getState().getPlayer().getFactionRights() >= var1.getFactionRights() && var1.getFactionRights() != -1) {
                        this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_33, "alrmovSSr1", 0.0F);
                    } else {
                        this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_66, new Object[]{this.getState().getPlayer().getFactionRankName(var1.getFactionRights())}), "alrmovSSr0", 0.0F);
                    }

                    return false;
                }

                if (this.notSent != var1) {
                    this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_68, "alrmovr2", 0.0F);
                    this.notSent = var1;
                }
            }

            if (var1.getFactionId() != 0 && var1.getFactionId() != this.getState().getPlayer().getFactionId() && this.getState().getFactionManager().getFaction(var1.getFactionId()) != null) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_13, "ceatt", 0.0F);
                return false;
            } else if (var1.railController.isDockedAndExecuted() && var1.railController.isShipyardDockedRecursive()) {
                if (var1.railController.getRoot() instanceof ManagedSegmentController && ((ManagedSegmentController)var1.railController.getRoot()).getManagerContainer() instanceof ShipyardManagerContainerInterface) {
                    Iterator var2 = ((ShipyardManagerContainerInterface)((ManagedSegmentController)var1.railController.getRoot()).getManagerContainer()).getShipyard().getCollectionManagers().iterator();

                    ShipyardCollectionManager var3;
                    SegmentController var4;
                    do {
                        if (!var2.hasNext()) {
                            this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_15, 0.0F);
                            return false;
                        }
                    } while((var4 = (var3 = (ShipyardCollectionManager)var2.next()).getCurrentDocked()) == null || !var1.railController.isAnyChildOf(var4));

                    boolean var5;
                    if (!(var5 = var3.isDockedInEditableState())) {
                        this.getState().getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_14, new Object[]{var3.getStateDescription()}), 0.0F);
                    }

                    return var5;
                } else {
                    this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_16, 0.0F);
                    return false;
                }
            } else if (var1 instanceof ShopSpaceStation) {
                this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_17, 0.0F);
                return false;
            } else if (var1 instanceof Ship && ((Ship)var1).getAttachedPlayers().size() > 0 && !((Ship)var1).getAttachedPlayers().contains(this.getState().getPlayer())) {
                this.getState().getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_18, 0.0F);
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isNeighborToClientSector(int var1) {
        RemoteSector var2;
        return (var2 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var1)) != null && this.getState().getCurrentRemoteSector() != null && Sector.isNeighbor(var2.clientPos(), this.getState().getCurrentRemoteSector().clientPos());
    }

    public void applySectorChanges() {
        if (!this.sectorChanges.isEmpty()) {
            synchronized(this.sectorChanges) {
                Iterator var2 = this.sectorChanges.iterator();

                while(true) {
                    while(true) {
                        if (!var2.hasNext()) {
                            this.sectorChanges.clear();
                            return;
                        }

                        SectorChange var3;
                        boolean var4 = (var3 = (SectorChange)var2.next()).what.isInClientRange();
                        var3.what.setSectorId(var3.to);
                        if (!var3.what.isHidden()) {
                            var3.what.setWarpToken(true);
                        }

                        this.updateSector(var3.what, var3.from);
                        RemoteSector var5;
                        if ((var5 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var3.to)) != null && this.getState().getCurrentRemoteSector() != null) {
                            if (Sector.isNeighbor(var5.clientPos(), this.getState().getCurrentRemoteSector().clientPos()) || var4) {
                                break;
                            }
                        } else if (var3.from >= 0) {
                            RemoteSector var7 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var3.from);
                            if (this.getState().getCurrentRemoteSector() == null || var7 != null && Sector.isNeighbor(var7.clientPos(), this.getState().getCurrentRemoteSector().clientPos())) {
                                break;
                            }
                        }
                    }

                    this.flagRecalc = true;
                }
            }
        }
    }

    private void bigMessage(String var1, String var2, String var3, float var4, Color var5, String var6) {
        synchronized(this.getState().getWorldDrawer().getGuiDrawer().bigMessages) {
            Iterator var8 = this.getState().getWorldDrawer().getGuiDrawer().bigMessages.iterator();

            BigMessage var9;
            do {
                if (!var8.hasNext()) {
                    BigMessage var11 = new BigMessage(var1, this.getState(), var2, var3, var5);
                    this.getState().getWorldDrawer().getGuiDrawer().bigMessages.addFirst(var11);
                    var11.startPopupMessage(var4);
                    this.getState().getController().queueUIAudio(var6);
                    return;
                }
            } while(!(var9 = (BigMessage)var8.next()).getId().equals(var1));

            var9.setMessage(var2);
            var9.restartPopupMessage();
        }
    }

    public long getServerRunningTime() {
        long var1 = (Long)this.getState().getGameState().getNetworkObject().serverStartTime.get();
        long var3 = (long)this.getState().getServerTimeDifference();
        return System.currentTimeMillis() - (var1 - var3);
    }

    public long calculateStartTime() {
        if ((Long)this.getState().getGameState().getNetworkObject().universeDayDuration.get() == -1L) {
            return -1L;
        } else {
            long var1 = (Long)this.getState().getGameState().getNetworkObject().universeDayDuration.get();
            long var3 = (Long)this.getState().getGameState().getNetworkObject().serverModTime.get();
            long var5 = (Long)this.getState().getGameState().getNetworkObject().serverStartTime.get();
            long var7 = (long)this.getState().getServerTimeDifference();
            return var5 - var7 + var3 - var1;
        }
    }

    public long getUniverseDayInMs() {
        return (Long)this.getState().getGameState().getNetworkObject().universeDayDuration.get();
    }

    public void onRemoveEntity(Sendable var1) {
        SimpleTransformableSendableObject var2;
        if (var1 instanceof SimpleTransformableSendableObject && ((var2 = (SimpleTransformableSendableObject)var1).getSectorId() == this.getState().getCurrentSectorId() || var2.isNeighbor(var2.getSectorId(), this.getState().getCurrentSectorId()))) {
            this.flagRecalc = true;
        }

    }

    public GUIPopupInterface changePopupMessage(String var1, String var2) {
        Iterator var3 = this.getState().getWorldDrawer().getGuiDrawer().popupMessages.iterator();

        GUIPopupInterface var4;
        do {
            if (!var3.hasNext()) {
                return null;
            }
        } while(!(var4 = (GUIPopupInterface)var3.next()).getMessage().equals(var1));

        var4.setMessage(var2);
        var4.restartPopupMessage();
        return var4;
    }

    public void characterCommitSuicide() throws IOException, InterruptedException {
        if (this.getState().getCharacter() != null) {
            this.getConnection().sendCommand(NetUtil.RECEIVER_SERVER[0], KillCharacter.class, new Object[]{this.getState().getCharacter().getId()});
        } else {
            System.err.println("No character to kill");
        }
    }

    public void clearAllSegmentBuffers() {
        System.err.println("[CLIENT] clearing all buffered data");
        synchronized(this.getState()) {
            synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                Iterator var3 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

                while(var3.hasNext()) {
                    Sendable var4;
                    if ((var4 = (Sendable)var3.next()) instanceof SendableSegmentController) {
                        ((SendableSegmentController)var4).onClear();
                        synchronized(((SendableSegmentController)var4).getSegmentBuffer()) {
                            ((SendableSegmentController)var4).getSegmentBuffer().clear(false);
                        }

                        if (var4 instanceof ManagedSegmentController) {
                            ((ManagedSegmentController)var4).getManagerContainer().clear();
                        }

                        ((ClientSegmentProvider)((SendableSegmentController)var4).getSegmentProvider()).clearRequestedBuffers();
                        if (((SendableSegmentController)var4).getPhysicsDataContainer().getObject() != null && ((SendableSegmentController)var4).getPhysicsDataContainer().getObject() instanceof RigidBody) {
                            ((SendableSegmentController)var4).getPhysicsDataContainer().getObject().setWorldTransform(((SendableSegmentController)var4).getWorldTransformOnClient());
                            ((RigidBody)((SendableSegmentController)var4).getPhysicsDataContainer().getObject()).setInterpolationWorldTransform(((SendableSegmentController)var4).getWorldTransformOnClient());
                        }
                    }
                }
            }

        }
    }

    public void connect(HostPortLoginName var1) throws Exception {
        System.out.println("[CLIENT] trying to connect to " + var1.host + ":" + var1.port);
        String var2;
        if ((var2 = var1.loginName) == null) {
            try {
                FileExt var3 = new FileExt("./debugPlayerLock.lock");
                DataInputStream var6;
                int var5 = (var6 = new DataInputStream(new FileInputStream(var3))).readInt();
                var6.close();
                var2 = "player" + (var5 + 1);
            } catch (FileNotFoundException var4) {
                var2 = "player1";
            }
        }

        if ((Starter.currentSession == null || !Starter.currentSession.isValid()) && (!EngineSettings.N_IGNORE_SAVED_UPLINK_CREDENTIALS_IN_SINGLEPLAYER.isOn() || !var1.host.equals("localhost")) && StarMadeCredentials.exists()) {
            System.out.println("Found Credentials");
            this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_19);
            StarMadeCredentials var7 = StarMadeCredentials.read();
            System.out.println(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_20);
            this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_21);
            (new SessionNewStyle(var1.host + ":" + var1.port)).login(var7.getUser(), var7.getPasswd());
            this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_22);
            System.out.println(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_23);
        }

        this.connect(var1.host, var1.port, var1.userAgent, var2, Starter.currentSession);
        System.out.println("[CLIENT] connected to server " + var1.host + ":" + var1.port);
        if (Starter.currentSession != null) {
            System.err.println("UPDATING AFTER LOGIN");
            Starter.currentSession.afterLogin();
        }

    }

    public GUIPopupInterface endPopupMessage(String var1) {
        Iterator var2 = this.getState().getWorldDrawer().getGuiDrawer().popupMessages.iterator();

        GUIPopupInterface var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }
        } while(!(var3 = (GUIPopupInterface)var2.next()).getMessage().equals(var1));

        var3.timeOut();
        return var3;
    }

    public <E extends ElementCollection<E, EC, EM>, EC extends ElementCollectionManager<E, EC, EM>, EM extends UsableElementManager<E, EC, EM>> void enqueueElementCollectionUpdate(ElementCollectionManager<E, EC, EM> var1) {
        this.elementCollectionCalculationThreadManager.enqueue(new ElementCollectionCalculationThreadExecution(var1));
    }

    public ClientChannel getClientChannel() {
        return this.clientChannel;
    }

    public void setClientChannel(ClientChannel var1) {
        this.clientChannel = var1;
    }

    public ClientGameData getClientGameData() {
        return this.clientGameData;
    }

    public ClientMissileManager getClientMissileManager() {
        return this.clientMissileManager;
    }

    public CreatorThreadController getCreatorThreadController() {
        return this.creatorThreadController;
    }

    public SynchronizationContainerController getPrivateChannelSynchController() {
        return this.privateChannelSynchController;
    }

    public SynchronizationContainerController getSynchController() {
        return this.synchController;
    }

    public TextureSynchronizer getTextureSynchronizer() {
        return this.textureSynchronizer;
    }

    public TutorialMode getTutorialMode() {
        return this.tutorialMode;
    }

    public void setTutorialMode(TutorialMode var1) {
        this.tutorialMode = var1;
    }

    public void initialize() throws IOException, NoSuchAlgorithmException {
        System.err.println("[CLIENT] State initializing");
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_24);
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_25);
        System.err.println("[CLIENT] Chat initializing");
        ChatSystem var1;
        (var1 = new ChatSystem(this.getState())).setId(this.getState().getNextFreeObjectId());
        var1.setOwnerStateId(this.getState().getId());
        var1.initialize();
        this.getState().setChat(var1);
        this.getSynchController().addNewSynchronizedObjectQueued(var1);
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_26);
        System.err.println("[CLIENT] Control Manager initializing");
        synchronized(this.getState()) {
            this.getState().setSynched();
            this.setupControlManager();
            this.getState().setUnsynched();
        }

        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_27);
        System.err.println("[CLIENT] InternalCallback initializing");
        this.getState().getPhysics().getDynamicsWorld().setInternalTickCallback(new GameClientController.PhysicsCallback(), (Object)null);
        (ClientSegmentProvider.dummySegment = new RemoteSegment(new FloatingRock(this.getState()))).setSegmentData(new SegmentDataIntArray(false));
        System.out.println("[CLIENT] State initialized. GameMode: " + this.getState().getGameMode().toString());
    }

    public void initializeState() throws NoSuchAlgorithmException, IOException {
        this.getState().setBlockBehaviorCheckSum(FileUtil.getSha1Checksum("./data/config/blockBehaviorConfig.xml"));
        this.getState().setConfigCheckSum(FileUtil.getSha1Checksum("./data/config/BlockConfig.xml"));
        this.getState().setFactionConfigCheckSum(FileUtil.getSha1Checksum("./data/config/FactionConfig.xml"));
        this.getState().setConfigPropertiesCheckSum(FileUtil.getSha1Checksum("./data/config/BlockTypes.properties"));
        if (!ElementKeyMap.configHash.equals("none") && !this.getState().getBlockConfigCheckSum().equals(ElementKeyMap.configHash)) {
            System.err.println("[CLIENT][CONFIG] DIFFERENT BLOCK CONFIG TO COMPARE TO. REFRESHING BLOCK CONFIG TO MATCH HASH OF ORIGINAL BLOCK CONFIG SO IT CAN BE COMPARED TO THE SERVER'S HASH AFTER LOGIN");
            ElementKeyMap.reinitializeData(new FileExt("./data/config/BlockConfig.xml"), false, (String)null, (File)null);
        }

        this.getState().setCustomTexturesCheckSum(FileUtil.createFilesHashRecursively(GameResourceLoader.CUSTOM_TEXTURE_PATH, new FileFilter() {
            public boolean accept(File var1) {
                return var1.isDirectory() || var1.getName().toLowerCase(Locale.ENGLISH).endsWith(".png");
            }
        }));
        this.getState().setController(this);

        try {
            this.getState().setParticleController(new ProjectileController(this.getState(), -1));
            this.getState().setPulseController(new PulseController(this.getState(), -1));
            this.getState().setPhysics(new PhysicsExt(this.getState()));
            this.getState().setGUIController(new GUIController(this.getState()));
            if (!this.state.isPassive()) {
                this.getState().setWorldDrawer(new WorldDrawer(this.getState()));
            }

            this.getState().setReady(true);
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public boolean isTutorialStarted() {
        return this.tutorialStarted;
    }

    public String onAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
        System.err.println("AUTOCOMPLETE: " + var1 + "; PREFIX: " + var3);
        String var10;
        Iterator var17;
        if (var3.equals(this.getState().getCommandPrefixes()[0])) {
            var10 = EngineSettings.autoCompleteString(var1);
            if (var1.equals(var10)) {
                ArrayList var10000 = EngineSettings.list(var10);
                var3 = null;
                var17 = var10000.iterator();

                while(var17.hasNext()) {
                    EngineSettings var16 = (EngineSettings)var17.next();
                    var2.onTextEnter(var16.toString(), false, true);
                }
            }

            return var10;
        } else {
            int var5;
            if (var3.equals(this.getState().getCommandPrefixes()[1])) {
                var10 = AdminCommands.autoCompleteString(var1);
                if (!var1.isEmpty()) {
                    boolean var9 = false;
                    ArrayList var12;
                    if (var1.equals(var10) && !var1.isEmpty() && (var12 = AdminCommands.list(var10)).size() > 1) {
                        var9 = true;
                        StringBuffer var6;
                        (var6 = new StringBuffer()).append(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_28);

                        for(int var7 = 0; var7 < var12.size(); ++var7) {
                            String var8 = ((AdminCommands)var12.get(var7)).name().toLowerCase(Locale.ENGLISH);
                            var6.append(var8);
                            if (var7 < var12.size() - 1) {
                                var6.append(", ");
                            }
                        }

                        var2.onTextEnter(var6.toString(), false, true);
                    }

                    String var13;
                    if (var1.equals(var10) && !var9 && !(var13 = findCorrectedCommand(var1)).isEmpty()) {
                        var2.onTextEnter(var13, false, true);
                    }
                }

                ArrayList var11;
                (var11 = new ArrayList()).add("pm");
                var11.add("f");

                for(var5 = 0; var5 < AdminCommands.values().length; ++var5) {
                    AdminCommands var14 = AdminCommands.values()[var5];
                    var11.add(var14.name());
                }

                if (EngineSettings.P_PHYSICS_DEBUG_ACTIVE.isOn() && this.getState().getPlayer() != null && !this.getState().getPlayer().getNetworkObject().isAdminClient.get()) {
                    EngineSettings.P_PHYSICS_DEBUG_ACTIVE.setCurrentState(false);
                }

                var17 = var11.iterator();

                while(var17.hasNext()) {
                    String var15 = (String)var17.next();
                    if (var1.startsWith(var15.toLowerCase(Locale.ENGLISH) + " ")) {
                        System.err.println("ADMIN COMMAND SET: " + var15);
                        String[] var18;
                        if ((var18 = var1.split(" ")).length > 1) {
                            var10 = "";

                            for(int var19 = 0; var19 < var18.length - 1; ++var19) {
                                var10 = var10 + var18[var19] + " ";
                            }

                            var10 = var10 + autocompletePlayer(this.state, var18[var18.length - 1]);
                        }
                        break;
                    }
                }

                return var10;
            } else {
                String[] var4;
                if (var3.equals("#") && (var4 = var1.split(" ")).length > 0) {
                    var3 = "";

                    for(var5 = 0; var5 < var4.length - 1; ++var5) {
                        var3 = var3 + var4[var5] + " ";
                    }

                    return var3 + autocompletePlayer(this.state, var4[var4.length - 1]);
                } else {
                    throw new PrefixNotFoundException(var3);
                }
            }
        }
    }

    public void onEndFrame() {
    }

    public void onHurt(PlayerState var1, Sendable var2) {
        if (var1.isClientOwnPlayer()) {
            this.getState().getWorldDrawer().getGuiDrawer().startHurtAnimation(var2);
        }

    }

    public void onPrivateSendableAdded(Sendable var1) {
    }

    public void onPrivateSendableRemoved(Sendable var1) {
    }

    public void onSectorChangeSelf(int var1, int var2) {
        long var3 = System.currentTimeMillis();
        if (this.getState().getShip() != null && this.getState().getShip().getSectorId() != var1) {
            this.getState().getShip().setSectorId(var1);
            this.updateSector(this.getState().getShip(), var2);
        }

        Iterator var5 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

        while(true) {
            Sendable var6;
            SimpleTransformableSendableObject var8;
            do {
                do {
                    do {
                        if (!var5.hasNext()) {
                            this.flagRecalc = true;
                            var5 = this.sectorChangeListeners.iterator();

                            while(var5.hasNext()) {
                                ((ClientSectorChangeListener)var5.next()).onSectorChangeSelf(var1, var2);
                            }

                            this.getClientGameData().updateNearest(var1);
                            long var7;
                            if ((var7 = System.currentTimeMillis() - var3) > 50L) {
                                System.err.println("[CLIENT] WARNING: Sector change took " + var7 + " ms");
                            }

                            return;
                        }
                    } while(!((var6 = (Sendable)var5.next()) instanceof SimpleTransformableSendableObject));
                } while((var8 = (SimpleTransformableSendableObject)var6).isClientOwnObject());
            } while(var8 instanceof SegmentController && (((SegmentController)var8).getDockingController().isDocked() || ((SegmentController)var8).railController.isDockedOrDirty()));

            var8.onPhysicsRemove();
            if (!var8.isHidden()) {
                var8.onPhysicsAdd();
            }
        }
    }

    public void onSendableAdded(Sendable var1) {
        if (var1 instanceof SegmentController) {
            Starter.modManager.onSegmentControllerSpawn((SegmentController)var1);
        }

        if (var1 instanceof ShopInterface) {
            this.shopInterfaces.add((ShopInterface)var1);
        }

        if (var1 instanceof SimpleTransformableSendableObject) {
            int var2 = ((SimpleTransformableSendableObject)var1).getSectorId();
            if (var1 instanceof PlayerCharacter) {
                synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                    Iterator var4 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                    while(var4.hasNext()) {
                        Sendable var5;
                        if ((var5 = (Sendable)var4.next()) instanceof PlayerState && ((PlayerState)var5).getClientId() == ((PlayerCharacter)var1).getClientOwnerId()) {
                            ((PlayerState)var5).setAssignedPlayerCharacter((PlayerCharacter)var1);
                        }
                    }
                }
            }

            RemoteSector var3 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var2);
            RemoteSector var8 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getState().getCurrentSectorId());
            if (var3 != null && var8 != null && Sector.isNeighbor(var3.clientPos(), var8.clientPos())) {
                this.flagRecalc = true;
            }
        } else {
            this.getState().getOtherSendables().add(var1);
            if (var1 instanceof RemoteSector) {
                this.state.getLoadedSectors().put(((RemoteSector)var1).clientPos(), (RemoteSector)var1);
            }
        }

        Iterator var7 = this.sendableAddRemoveListener.iterator();

        while(var7.hasNext()) {
            ((SendableAddedRemovedListener)var7.next()).onAddedSendable(var1);
        }

        this.flagTrackingChanged();
        if (var1 instanceof PlayerState) {
            Starter.modManager.onPlayerCreated((PlayerState)var1);
        }

    }

    public void onSendableRemoved(Sendable var1) {
        if (var1 instanceof SegmentController) {
            this.creatorThreadController.removeCreatorThread((SegmentController)var1);
        }

        Iterator var3;
        if (var1 instanceof SimpleTransformableSendableObject) {
            if (var1 instanceof PlayerCharacter) {
                synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                    var3 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                    while(var3.hasNext()) {
                        Sendable var4;
                        if ((var4 = (Sendable)var3.next()) instanceof PlayerState && ((PlayerState)var4).getAssingedPlayerCharacter() == var1) {
                            ((PlayerState)var4).setAssignedPlayerCharacter((PlayerCharacter)null);
                        }
                    }
                }
            }

            int var2 = ((SimpleTransformableSendableObject)var1).getSectorId();
            RemoteSector var7 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var2);
            RemoteSector var8 = (RemoteSector)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(this.getState().getCurrentSectorId());
            if (var7 != null && var8 != null && Sector.isNeighbor(var7.clientPos(), var8.clientPos())) {
                this.flagRecalc = true;
            }
        }

        if (var1 instanceof ShopInterface) {
            this.shopInterfaces.remove((ShopInterface)var1);
        }

        this.getState().getOtherSendables().remove(var1);
        if (var1 instanceof RemoteSector) {
            this.state.getLoadedSectors().remove(((RemoteSector)var1).clientPos());
        }

        PlayerInteractionControlManager var6;
        if ((var6 = this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager()).getSelectedEntity() == var1) {
            var6.setSelectedEntity((SimpleTransformableSendableObject)null);
        }

        if (var6.getSelectedAITarget() == var1) {
            var6.setSelectedAITarget((SimpleTransformableSendableObject)null);
        }

        if (var1 == this.getState().getCurrentPlayerObject()) {
            System.err.println("[CLIENT][ONENTITYREMOVE] reset player object");
            this.getState().setCurrentPlayerObject((SimpleTransformableSendableObject)null);
        }

        if (var1 == this.getState().getShip() && this.getState().getShip() != null) {
            System.err.println("[CLIENT][ONENTITYREMOVE] Removing own ship");
            this.getState().setShip((Ship)null);
        }

        if (var1 == this.getState().getCharacter()) {
            System.err.println("[CLIENT][ONENTITYREMOVE] Removing own character");
            this.getState().setCharacter((PlayerCharacter)null);
        }

        var3 = this.sendableAddRemoveListener.iterator();

        while(var3.hasNext()) {
            ((SendableAddedRemovedListener)var3.next()).onRemovedSendable(var1);
        }

        this.flagTrackingChanged();
        if (var1 instanceof PlayerState) {
            Starter.modManager.onPlayerRemoved((PlayerState)var1);
        }

    }

    public void onStartFrame() {
    }

    public void onStringCommand(String var1, TextCallback var2, String var3) {
        System.err.println("HANDLING COMMAND " + var1 + "::: " + this.getState().getCommandPrefixes()[0].substring(1) + "; " + this.getState().getCommandPrefixes()[1].substring(1));
        if (var1.length() > var3.length()) {
            String[] var4 = (var1 = var1.substring(var3.length() - 1)).split("\\s+");

            try {
                String var5;
                if (var3.equals(this.getState().getCommandPrefixes()[0])) {
                    if (var4.length > 1 && var4.length <= 2) {
                        EngineSettings var13 = (EngineSettings)Enum.valueOf(EngineSettings.class, var4[0].toUpperCase(Locale.ENGLISH));
                        var5 = var4[1];

                        try {
                            var13.switchSetting(var5);
                            var2.onTextEnter("[COMMAND] \"" + var1 + "\" successful: " + var13.name() + " = " + var13.getCurrentState(), false, false);
                        } catch (StateParameterNotFoundException var6) {
                            throw new IllegalArgumentException("[ERROR] STATE NOT KNOWN: " + var6.getMessage());
                        }
                    } else {
                        throw new IndexOutOfBoundsException("[ERROR] Invalid argument count " + var4.length + ": " + Arrays.toString(var4));
                    }
                } else if (var3.equals(this.getState().getCommandPrefixes()[1])) {
                    AdminCommands var11 = (AdminCommands)Enum.valueOf(AdminCommands.class, var4[0].toUpperCase(Locale.ENGLISH));
                    if ((var5 = var1.substring(var1.indexOf(var4[0]) + var4[0].length()).trim()).length() > 0) {
                        String[] var10 = StringTools.splitParameters(var5);
                        Object[] var12 = AdminCommands.packParameters(var11, var10);
                        this.sendAdminCommand(var11, var12);
                    } else if (var11.getTotalParameterCount() > 0) {
                        var1 = "need ";
                        if (var11.getRequiredParameterCount() != var11.getTotalParameterCount()) {
                            var1 = var1 + "minimum of " + var11.getRequiredParameterCount();
                        } else {
                            var1 = var1 + var11.getTotalParameterCount();
                        }

                        throw new AdminCommandIllegalArgument(var11, (String[])null, "No parameters provided: " + var1);
                    } else {
                        this.sendAdminCommand(var11);
                    }
                } else {
                    throw new IllegalArgumentException("[ERROR] PREFIX NOT KNOWN: \"" + var3 + "\"; use one of " + Arrays.toString(this.getState().getCommandPrefixes()));
                }
            } catch (IllegalArgumentException var7) {
                if (!var7.getMessage().startsWith("[ERROR]")) {
                    var2.onTextEnter("[ERROR] UNKNOWN COMMAND: " + var4[0], false, false);
                } else {
                    var2.onTextEnter(var7.getMessage(), false, false);
                }
            } catch (IndexOutOfBoundsException var8) {
                var2.onTextEnter(var8.getMessage(), false, false);
            } catch (AdminCommandIllegalArgument var9) {
                if (var9.getMsg() != null) {
                    var2.onTextEnter("[ERROR] " + var9.getCommand() + ": " + var9.getMsg(), false, false);
                    var2.onTextEnter("[ERROR] usage: " + var9.getCommand().getDescription(), false, false);
                } else {
                    var2.onTextEnter(var9.getMessage(), false, false);
                }
            }
        }
    }

    public GUIPopupInterface popupAlertTextMessage(String var1, String var2, float var3) {
        return this.popupTextMessage(var1, var2, var3, ClientMessageLogType.ERROR, "0022_menu_ui - error 1");
    }

    public void popupAlertTextMessage(String var1) {
        this.popupAlertTextMessage(var1, 0.0F);
    }

    public GUIPopupInterface popupAlertTextMessage(String var1, float var2) {
        return this.popupTextMessage(var1, var1, var2, ClientMessageLogType.ERROR, "0022_menu_ui - error 1");
    }

    public GUIPopupInterface popupGameTextMessage(String var1, String var2, float var3) {
        return this.popupTextMessage(var1, var2, var3, ClientMessageLogType.GAME, "0022_menu_ui - highlight 1");
    }

    public GUIPopupInterface popupInfoTextMessage(String var1, String var2, float var3) {
        return this.popupTextMessage(var1, var2, var3, ClientMessageLogType.INFO, "0022_menu_ui - highlight 3");
    }

    public GUIPopupInterface popupGameTextMessage(String var1, float var2) {
        return this.popupTextMessage(var1, var1, var2, ClientMessageLogType.GAME, "0022_menu_ui - highlight 1");
    }

    public GUIPopupInterface popupInfoTextMessage(String var1, float var2) {
        return this.popupTextMessage(var1, var1, var2, ClientMessageLogType.INFO, "0022_menu_ui - highlight 3");
    }

    private GUIPopupInterface popupTextMessage(String var1, String var2, float var3, ClientMessageLogType var4, String var5) {
        if (!this.state.isPassive()) {
            synchronized(this.getState().getWorldDrawer().getGuiDrawer().popupMessages) {
                Iterator var10 = this.getState().getWorldDrawer().getGuiDrawer().popupMessages.iterator();

                GUIPopupInterface var12;
                do {
                    if (!var10.hasNext()) {
                        System.err.println("[CLIENT][POPUP] " + var4.name() + ": " + var1);
                        this.getState().getMessageLog().log(new ClientMessageLogEntry("SYSTEM", this.getState().getPlayerName(), var1, System.currentTimeMillis(), var4));
                        PopupMessageNew var11;
                        (var11 = new PopupMessageNew(this.getState(), var2, var1, var4.color)).setFlashing(var4 == ClientMessageLogType.FLASHING);
                        this.getState().getWorldDrawer().getGuiDrawer().popupMessages.addFirst(var11);
                        var11.startPopupMessage(var3);
                        this.getState().getController().queueUIAudio(var5);
                        return var11;
                    }
                } while(!(var12 = (GUIPopupInterface)var10.next()).getId().equals(var2));

                var12.setMessage(var1);
                var12.restartPopupMessage();
                return var12;
            }
        } else {
            Iterator var6 = this.getState().getChatListeners().iterator();

            while(var6.hasNext()) {
                ChatListener var7 = (ChatListener)var6.next();
                ChatMessage var8;
                (var8 = new ChatMessage()).sender = "[POPUP]";
                var8.receiver = "[CLIENT]";
                var8.text = var1;
                var7.notifyOfChat(var8);
            }

            return new PopupMessageNew(this.getState(), var2, var1, var4.color);
        }
    }

    public GUIPopupInterface popupTipTextMessage(String var1, float var2) {
        return this.popupTextMessage(var1, var1, var2, ClientMessageLogType.TIP, "0022_menu_ui - highlight 3");
    }

    public GUIPopupInterface popupFlashingTextMessage(String var1, float var2) {
        return this.popupTextMessage(var1, var1, var2, ClientMessageLogType.FLASHING, "0022_menu_ui - highlight 3");
    }

    private void purgeCompleteDB() {
        System.out.println("[CLIENT] PURGING CLIENT DATABASE CACHE");
        FileExt var1;
        if ((var1 = new FileExt(ClientStatics.SEGMENT_DATA_DATABASE_PATH)).exists()) {
            FileUtil.deleteDir(var1);
            var1.mkdir();
        }

    }

    private void purgeDB() {
        synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var2 = this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

            while(var2.hasNext()) {
                Sendable var3;
                if ((var3 = (Sendable)var2.next()) instanceof SegmentController) {
                    ((SegmentController)var3).getSegmentProvider().purgeDB();
                }
            }

        }
    }

    public void readjustControllers(Collection<Element> var1, SegmentController var2, Segment var3) {
    }

    private void recalcCurrentEntities() {
        try {
            Iterator var1 = this.getState().getCurrentSectorEntities().values().iterator();

            SimpleTransformableSendableObject var2;
            while(var1.hasNext()) {
                var2 = (SimpleTransformableSendableObject)var1.next();
                this.flagSectorCleanup.add(var2);
            }

            this.getState().getCurrentGravitySources().clear();
            this.getState().getCurrentSectorEntities().clear();
            var1 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

            label56:
            while(true) {
                while(true) {
                    Sendable var5;
                    do {
                        if (!var1.hasNext()) {
                            for(int var4 = 0; var4 < this.flagSectorCleanup.size(); ++var4) {
                                var2 = (SimpleTransformableSendableObject)this.flagSectorCleanup.get(var4);
                                if (this.getState().getCurrentSectorEntities().containsKey(var2.getId())) {
                                    this.flagSectorCleanup.remove(var4);
                                    --var4;
                                } else {
                                    var2.setInClientRange(false);
                                }
                            }

                            this.flagRecalc = false;
                            break label56;
                        }
                    } while(!((var5 = (Sendable)var1.next()) instanceof SimpleTransformableSendableObject));

                    (var2 = (SimpleTransformableSendableObject)var5).setInClientRange(true);
                    if (var2.getSectorId() != this.getState().getCurrentSectorId() && !var2.isNeighbor(var2.getSectorId(), this.getState().getCurrentSectorId())) {
                        var2.onSectorInactiveClient();
                    } else {
                        var2.setClientCleanedUp(false);
                        if (var2.isGravitySource()) {
                            this.getState().getCurrentGravitySources().add(var2);
                        }

                        this.getState().getCurrentSectorEntities().put(var2.getId(), var2);
                    }
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            System.err.println("Exception: Delayed recalc");
            this.flagRecalc = true;
        }

        if (!this.state.isPassive()) {
            this.getState().getWorldDrawer().setFlagSegmentControllerUpdate(true);
            this.getState().getWorldDrawer().setFlagCharacterUpdate(true);
            this.getState().getWorldDrawer().setFlagManagedSegmentControllerUpdate(true);
            this.getState().getWorldDrawer().setFlagPlanetCoreUpdate(true);
        }

        this.sectorChangeObservable.note();
    }

    public byte[] requestBlockConfig() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            return (byte[])this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestBlockConfig.class, new Object[0])[0];
        }
    }

    public byte[] requestFactionConfig() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            return (byte[])this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestFactionConfig.class, new Object[0])[0];
        }
    }

    public byte[] requestBlockProperties() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            return (byte[])this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestBlockProperties.class, new Object[0])[0];
        }
    }

    public byte[] requestBlockBehavior() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            return (byte[])this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestBlockBehaviour.class, new Object[0])[0];
        }
    }

    public byte[] requestCustomTextures() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            System.err.println("[CLIENT] REQUESTING CUSTOM TEXTURES COMMAND NOW EXECUTING!");
            return (byte[])this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestCustomTextures.class, new Object[0])[0];
        }
    }

    public void requestControlChange(PlayerControllable var1, PlayerControllable var2, Vector3i var3, Vector3i var4, boolean var5) {
        this.getState().getPlayer().getControllerState().requestControl(var1, var2, var3, var4, var5);
    }

    public GameModes requestGameMode() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            Object[] var4 = this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestGameMode.class, new Object[0]);
            this.getState().setInitialSectorId((Integer)var4[1]);
            this.getState().getInitialSectorPos().set((Integer)var4[2], (Integer)var4[3], (Integer)var4[4]);
            this.getState().receivedBlockConfigChecksum = (String)var4[5];
            this.getState().receivedBlockConfigPropertiesChecksum = (String)var4[6];
            this.getState().setPhysicalAsteroids((Boolean)var4[7]);
            this.getState().receivedBlockBehaviorChecksum = (String)var4[8];
            this.getState().receivedCustomTexturesChecksum = (String)var4[9];
            Galaxy.USE_GALAXY = (Boolean)var4[10];
            this.getState().receivedFactionConfigChecksum = (String)var4[11];
            GameClientState.SERVER_BLOCK_QUEUE_SIZE = (Integer)var4[12];
            System.err.println("[CLIENT] RECEIVED STARTING SECTOR: " + this.getState().getInitialSectorPos());
            return GameModes.valueOf(var4[0].toString());
        }
    }

    public void requestInvetoriesUnblocked(int var1) throws IOException {
        this.getConnection().sendCommand(0, RequestInventoriesUnblocked.class, new Object[]{var1});
    }

    public void requestNewShip(Transform var1, Vector3i var2, Vector3i var3, PlayerState var4, String var5, String var6) {
        if (System.currentTimeMillis() - this.lastShipSpawn <= 5000L) {
            this.popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_29, new Object[]{5 - (int)Math.ceil((double)((float)(System.currentTimeMillis() - this.lastShipSpawn) / 1000.0F))}), 0.0F);
        } else {
            Object[] var7 = new Object[26];
            float[] var8 = new float[16];
            var1.getOpenGLMatrix(var8);
            int var11 = 0;
            System.err.println("[RequestNewShip] Ship pos: " + Arrays.toString(var8));

            for(int var9 = 0; var9 < 16; ++var9) {
                var7[var11++] = var8[var9];
            }

            assert var2.x <= var3.x;

            assert var2.y <= var3.y;

            assert var2.z <= var3.z;

            var7[var11++] = var2.x;
            var7[var11++] = var2.y;
            var7[var11++] = var2.z;
            var7[var11++] = var3.x;
            var7[var11++] = var3.y;
            var7[var11++] = var3.z;
            var7[var11++] = var4.getId();
            var7[var11++] = var5;
            var7[var11++] = var6;
            var7[var11] = "Ship";

            try {
                this.getConnection().sendCommand(0, CreateNewShip.class, var7);
            } catch (IOException var10) {
                var10.printStackTrace();
                throw new RuntimeException(var10);
            }

            this.lastShipSpawn = System.currentTimeMillis();
        }
    }

    public void requestNewStation(Transform var1, PlayerState var2, String var3, String var4) throws IOException, InterruptedException {
        Object[] var5 = new Object[26];
        float[] var6 = new float[16];
        var1.getOpenGLMatrix(var6);
        int var8 = 0;
        System.err.println("[RequestNewStation] Station pos: " + Arrays.toString(var6));

        for(int var7 = 0; var7 < 16; ++var7) {
            var5[var8++] = var6[var7];
        }

        var5[var8++] = -4;
        var5[var8++] = -4;
        var5[var8++] = -4;
        var5[var8++] = 4;
        var5[var8++] = 4;
        var5[var8++] = 4;
        var5[var8++] = var2.getId();
        var5[var8++] = var3;
        var5[var8++] = var4;
        var5[var8] = "Station";
        this.getConnection().sendCommand(0, CreateNewShip.class, var5);
    }

    public void requestNewVehicle(Transform var1, Vector3i var2, Vector3i var3, PlayerState var4, String var5, String var6) throws IOException, InterruptedException {
        Object[] var7 = new Object[26];
        float[] var8 = new float[16];
        var1.getOpenGLMatrix(var8);
        int var10 = 0;
        System.err.println("[RequestNewShip] Ship pos: " + Arrays.toString(var8));

        for(int var9 = 0; var9 < 16; ++var9) {
            var7[var10++] = var8[var9];
        }

        assert var2.x <= var3.x;

        assert var2.y <= var3.y;

        assert var2.z <= var3.z;

        var7[var10++] = var2.x;
        var7[var10++] = var2.y;
        var7[var10++] = var2.z;
        var7[var10++] = var3.x;
        var7[var10++] = var3.y;
        var7[var10++] = var3.z;
        var7[var10++] = var4.getId();
        var7[var10++] = var5;
        var7[var10++] = var6;
        var7[var10] = "Vehicle";
        this.getConnection().sendCommand(0, CreateNewShip.class, var7);
    }

    public StarMadePlayerStats requestPlayerStats(int var1) throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var3 = IdGen.getNewPacketId();
            return StarMadePlayerStats.decode(this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var3, RequestPlayerStats.class, new Object[]{var1}), var1);
        }
    }

    public StarMadeServerStats requestServerStats() throws IOException, InterruptedException {
        synchronized(this.requestLock) {
            short var2 = IdGen.getNewPacketId();
            long var3 = System.currentTimeMillis();
            StarMadeServerStats var8 = StarMadeServerStats.decode(this.getConnection().sendReturnedCommand(NetUtil.RECEIVER_SERVER[0], var2, RequestServerStats.class, new Object[0]));
            long var5 = System.currentTimeMillis() - var3;
            var8.ping = var5;
            return var8;
        }
    }

    public void scheduleSectorChange(SectorChange var1) {
        if (!this.sectorChanges.contains(var1)) {
            synchronized(this.sectorChanges) {
                this.sectorChanges.add(var1);
            }
        }
    }

    private void scheduleWriteDataPush(final ObjectArrayList<SimpleTransformableSendableObject> var1) {
        Iterator var2 = var1.iterator();

        while(var2.hasNext()) {
            ((SimpleTransformableSendableObject)var2.next()).setClientCleanedUp(true);
        }

        final Object var4 = new Object();
        Runnable var3 = new Runnable() {
            public void run() {
                synchronized(var4) {
                    Iterator var2 = var1.iterator();

                    while(true) {
                        if (!var2.hasNext()) {
                            break;
                        }

                        SimpleTransformableSendableObject var3;
                        if ((var3 = (SimpleTransformableSendableObject)var2.next()) instanceof SendableSegmentController) {
                            try {
                                ((SendableSegmentController)var3).writeAllBufferedSegmentsToDatabase(false, false, false);
                            } catch (IOException var6) {
                                var6.printStackTrace();
                            }

                            synchronized(GameClientController.this.cleanUps) {
                                GameClientController.this.cleanUps.add((SendableSegmentController)var3);
                            }
                        }
                    }
                }

                GameClientController.updateFileList();
            }
        };
        this.getState().getThreadPoolLogins().execute(var3);
    }

    public void sendAdminCommand(AdminCommands var1, Object... var2) {
        Object[] var3;
        (var3 = new Object[var2.length + 1])[0] = var1.ordinal();

        for(int var5 = 0; var5 < var2.length; ++var5) {
            var3[var5 + 1] = var2[var5];
        }

        try {
            this.getConnection().sendCommand(0, AdminCommand.class, var3);
        } catch (IOException var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }
    }

    public void setSpawnPoint(SegmentPiece var1) {
        this.getState().getPlayer().sendSimpleCommand(SimplePlayerCommands.SET_SPAWN, new Object[]{var1.getSegmentController().getId(), var1.getAbsoluteIndex()});
    }

    private void setupControlManager() {
        this.getState().setGlobalGameControlManager(new GlobalGameControlManager(this.getState()));
        this.getState().getGlobalGameControlManager().initialize();
        this.getState().getGlobalGameControlManager().setActive(true);
    }

    public void showBigMessage(String var1, String var2, String var3, float var4) {
        this.bigMessage(var1, var2, var3, var4, ClientMessageLogType.GAME.color, "0022_menu_ui - highlight 1");
    }

    public void showServerMessage() {
        String var1;
        if (this.getState().getGameState() != null && !this.state.isPassive() && (var1 = (String)this.getState().getGameState().getNetworkObject().serverMessage.get()).length() > 0) {
            this.getState().getController().getPlayerInputs().add(new GUIMessageDialog(this.getState(), var1, Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_62, false));
        }

    }

    public void showBigTitleMessage(String var1, String var2, float var3) {
        this.titleMessage(var1, var2, var3, ClientMessageLogType.GAME.color, "0022_menu_ui - highlight 1");
    }

    public void showSmallBigTitleMessage(String var1, String var2, float var3) {
        this.titleMessage(var1, var2, var3, ClientMessageLogType.GAME.color, "0022_menu_ui - highlight 1");
    }

    public void spawnAndActivatePlayerCharacter() throws PlayerNotYetInitializedException {
        if (this.getState().getPlayer() != null && this.getState().getPlayer().getNetworkObject() != null) {
            if (!this.getState().isWaitingForPlayerActivate()) {
                this.getState().getPlayer().getNetworkObject().spawnRequest.add(new RemoteBoolean(this.getState().getPlayer().getNetworkObject()));
                this.getState().getPlayer().hasSpawnWait = true;
                this.getState().setWaitingForPlayerActivate(true);
            }
        } else {
            throw new PlayerNotYetInitializedException();
        }
    }

    public boolean startGraphics(GraphicsContext var1) throws Exception {
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_63);
        MainGameGraphics var2 = new MainGameGraphics(this.state);
        this.getState().setScene(var2);
        if (currentGLFrame == null) {
            currentGLFrame = new GLFrame();
        }

        this.getState().setGlFrame(currentGLFrame);
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_30);
        this.setGuiConnectionState(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_31);
        this.setGuiConnectionDispose();
        System.err.println("[CLIENT] GRAPHICS CONTEXT: " + var1);
        String var3 = "StarMade alpha v" + Version.VERSION + " (" + Version.build + ") [" + this.getState().getGameMode().name() + "]; " + (Version.is64Bit() ? "64bit" : "32bit");
        if (!currentGLFrame.existing) {
            if (var1 == null) {
                var1 = new GraphicsContext();
                if (GUITextButton.cp == null) {
                    GUITextButton.cp = new ButtonColorImpl();
                }

                if (Controller.getResLoader() == null) {
                    Controller.initResLoader(new GameResourceLoader());
                }

                var1.initializeOpenGL(var3);
                var1.initializeLoadingScreen(new LoadingScreenDetailed());
            }

            var1.setFrame(currentGLFrame, (Boolean)null);
            var1.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_64);
            currentGLFrame.startGraphicsWithState(var3, this.getState(), var2, var1);
            this.graphicsContext = var1;
            return true;
        } else {
            System.err.println("[STARTUP] SETTING GRAPHICS FRAME. WHICH RUNS IN DIFFERENT THREAT");
            var1.setLoadMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_65);
            currentGLFrame.setGraphicsContext(this.state, var2);
            if (var1 != null) {
                var1.setFrame(currentGLFrame, (Boolean)null);
            }

            this.graphicsContext = var1;
            return false;
        }
    }

    public boolean suicide() {
        if (this.getState().getPlayer() != null) {
            this.getState().getPlayer().suicideOnClient();
            return true;
        } else {
            return false;
        }
    }

    public void timeOutBigMessage(String var1) {
        synchronized(this.getState().getWorldDrawer().getGuiDrawer().bigMessages){}

        Throwable var10000;
        label98: {
            boolean var10001;
            Iterator var3;
            try {
                var3 = this.getState().getWorldDrawer().getGuiDrawer().bigMessages.iterator();
            } catch (Throwable var10) {
                var10000 = var10;
                var10001 = false;
                break label98;
            }

            while(true) {
                try {
                    if (var3.hasNext()) {
                        BigMessage var4;
                        if (!(var4 = (BigMessage)var3.next()).getId().equals(var1)) {
                            continue;
                        }

                        var4.timeOut();
                        return;
                    }
                } catch (Throwable var9) {
                    var10000 = var9;
                    var10001 = false;
                    break;
                }

                return;
            }
        }

        Throwable var11 = var10000;
        try {
            throw var11;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void timeOutBigTitleMessage(String var1) {
        synchronized(this.getState().getWorldDrawer().getGuiDrawer().titleMessages){}

        Throwable var10000;
        label98: {
            boolean var10001;
            Iterator var3;
            try {
                var3 = this.getState().getWorldDrawer().getGuiDrawer().titleMessages.iterator();
            } catch (Throwable var10) {
                var10000 = var10;
                var10001 = false;
                break label98;
            }

            while(true) {
                try {
                    if (var3.hasNext()) {
                        BigTitleMessage var4;
                        if (!(var4 = (BigTitleMessage)var3.next()).getId().equals(var1)) {
                            continue;
                        }

                        var4.timeOut();
                        return;
                    }
                } catch (Throwable var9) {
                    var10000 = var9;
                    var10001 = false;
                    break;
                }

                return;
            }
        }

        try {
            throw var10000;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void titleMessage(String var1, String var2, float var3, Color var4, String var5) {
        synchronized(this.getState().getWorldDrawer().getGuiDrawer().titleMessages) {
            Iterator var7 = this.getState().getWorldDrawer().getGuiDrawer().titleMessages.iterator();

            BigTitleMessage var8;
            do {
                if (!var7.hasNext()) {
                    BigTitleMessage var10 = new BigTitleMessage(var1, this.getState(), var2, var4);
                    this.getState().getWorldDrawer().getGuiDrawer().titleMessages.addFirst(var10);
                    var10.startPopupMessage(var3);
                    this.getState().getController().queueUIAudio(var5);
                    return;
                }
            } while(!(var8 = (BigTitleMessage)var7.next()).getId().equals(var1));

            var8.setMessage(var2);
            var8.restartPopupMessage();
        }
    }

    public void reapplyBlockConfigInstantly() {
        System.err.println("[CLIENT] a new block behavior has been received. Applying to all entities");
        Iterator var1 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

        while(var1.hasNext()) {
            Sendable var2;
            if ((var2 = (Sendable)var1.next()) instanceof ManagedSegmentController) {
                ((ManagedSegmentController)var2).getManagerContainer().reparseBlockBehavior(true);
            }
        }

        this.popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_CONTROLLER_GAMECLIENTCONTROLLER_32, 0.0F);
    }

    public void updateActiveControllers() {
        InGameControlManager var1;
        PlayerGameControlManager var2 = (var1 = this.getState().getGlobalGameControlManager().getIngameControlManager()).getPlayerGameControlManager();
        boolean var3 = this.state.getUpdateTime() - this.getState().getController().getInputController().getLastDeactivatedMenu() < 200L;
        boolean var4 = this.state.getUpdateTime() - this.state.getHinderedInputTime() > (long)this.state.getHinderedInput();
        boolean var5 = this.getState().getController().getPlayerInputs().isEmpty();
        this.getState().getPlayer().getNetworkObject().activeControllerMask.forceClientUpdates();
        this.getState().getPlayer().getNetworkObject().activeControllerMask.set(0, !var1.isAnyMenuOrChatActive() && !var3 && var2.getPlayerIntercationManager().getPlayerCharacterManager().isTreeActiveAndNotSuspended() && var5 && var4);
        PlayerInteractionControlManager var6;
        ShipExternalFlightController var7 = (var6 = var2.getPlayerIntercationManager()).getInShipControlManager().getShipControlManager().getShipExternalFlightController();
        this.getState().getPlayer().getNetworkObject().activeControllerMask.set(1, !var1.isAnyMenuOrChatActive() && !var3 && (var7.isTreeActiveAndNotSuspended() && var7.isTreeActiveInFlight() || var6.getSegmentControlManager().getSegmentExternalController().isTreeActiveAndNotSuspended()) && var5 && var4);
        this.getState().getPlayer().getNetworkObject().activeControllerMask.set(2, !var3 && var2.getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getSegmentBuildController().isTreeActiveAndNotSuspended() && var5 && var4);
    }

    private void updateAmbientSound(Timer var1) {
        this.getState().getShip();
    }

    public void updateCurrentControlledEntity(ControllerState var1) {
        assert var1 != null;

        assert var1.getOwner() != null;

        if (var1.getOwner().getClientId() == this.getState().getId()) {
            this.getState().setCurrentPlayerObject((SimpleTransformableSendableObject)null);
            Iterator var3 = var1.getUnits().iterator();

            while(var3.hasNext()) {
                ControllerStateUnit var2;
                if ((var2 = (ControllerStateUnit)var3.next()).playerControllable instanceof SimpleTransformableSendableObject) {
                    this.getState().setCurrentPlayerObject((SimpleTransformableSendableObject)var2.playerControllable);
                    System.err.println("[CLIENT] CURRENT MAIN CONTROLLING IS NOW: " + this.getState().getCurrentPlayerObject() + "; at: " + this.getState().getCurrentPlayerObject().getWorldTransform().origin);
                    return;
                }
            }
        }

    }

    public void updateMouseGrabbed() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getChatControlManager().isActive();
        this.getState().getGlobalGameControlManager().getIngameControlManager().getFreeRoamController().isActive();
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().isActive();
    }

    private void updateOthers(Timer var1) {
        this.onlinePlayerMapHelper.putAll(this.getState().getOnlinePlayersLowerCaseMap());
        boolean var2 = false;
        Iterator var3 = this.shopInterfaces.iterator();

        while(var3.hasNext()) {
            ((ShopInterface)var3.next()).getShoppingAddOn().update(var1.currentTime);
        }

        var3 = this.getState().getOtherSendables().iterator();

        while(var3.hasNext()) {
            Sendable var4 = (Sendable)var3.next();
            long var5 = System.currentTimeMillis();

            assert !(var4 instanceof SimpleTransformableSendableObject) : var4;

            var4.updateLocal(var1);
            if (var4 instanceof PlayerState) {
                if ((PlayerState)this.getState().getOnlinePlayersLowerCaseMap().put(((PlayerState)var4).getName().toLowerCase(Locale.ENGLISH), (PlayerState)var4) == null) {
                    var2 = true;
                }

                this.onlinePlayerMapHelper.remove(((PlayerState)var4).getName().toLowerCase(Locale.ENGLISH));
                if (((PlayerState)var4).getBuildModePosition().isSpotLightOn()) {
                    this.getState().spotlights.add(((PlayerState)var4).getBuildModePosition());
                }
            }

            long var7;
            if ((var7 = System.currentTimeMillis() - var5) > 50L) {
                System.err.println("[CLIENT] WARNING: UPDATE OF (OTHER) " + var4 + " took " + var7);
            }
        }

        if (this.onlinePlayerMapHelper.size() > 0) {
            var2 = true;
            var3 = this.onlinePlayerMapHelper.keySet().iterator();

            while(var3.hasNext()) {
                String var9 = (String)var3.next();
                this.getState().getOnlinePlayersLowerCaseMap().remove(var9);
            }
        }

        this.onlinePlayerMapHelper.clear();
        if (var2) {
            this.onOnlinePlayersChanged();
        }

    }

    private void onOnlinePlayersChanged() {
        if (this.getState().getWorldDrawer() != null && this.getState().getWorldDrawer().getGuiDrawer() != null && this.getState().getWorldDrawer().getGuiDrawer().getPlayerStatisticsPanel() != null) {
            this.getState().getWorldDrawer().getGuiDrawer().getPlayerStatisticsPanel().playerListUpdated();
        }

    }

    public void updateSector(SimpleTransformableSendableObject<?> var1, int var2) {
        if (var1.getPhysicsDataContainer().getObject() != null && var1.getPhysicsDataContainer().isInitialized()) {
            var1.onPhysicsRemove();
            if (!var1.isHidden()) {
                var1.onPhysicsAdd();
            }
        }

    }

    public void updateTutorialMode(Timer var1) {
        if (Controller.getResLoader().isLoaded() && this.tutorialStarted && this.tutorialMode == null) {
            this.getState().getTutorialAIState().setCurrentProgram(this.tutorialMode = new TutorialMode(this.getState().getTutorialAIState()));
        }

        if (this.tutorialMode != null && (EngineSettings.TUTORIAL_NEW.isOn() || this.tutorialMode.getMachine() instanceof DynamicTutorialStateMachine && !this.tutorialMode.isInStartMachine())) {
            this.tutorialMode.suspend(!this.getState().getGlobalGameControlManager().getIngameControlManager().isActive());
            if (this.getState().isPlayerSpawned()) {
                try {
                    this.getState().getTutorialAIState().updateOnActive(var1);
                    return;
                } catch (FSMException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }

    public void writeSegmentDataToDatabase(boolean var1) throws Exception {
        final ArrayList var2 = new ArrayList();
        synchronized(this.getState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var4 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

            while(true) {
                if (!var4.hasNext()) {
                    break;
                }

                Sendable var5;
                if ((var5 = (Sendable)var4.next()) instanceof SendableSegmentController) {
                    ((SendableSegmentController)var5).writeAllBufferedSegmentsToDatabase(false, true, var1);
                    var2.add((SendableSegmentController)var5);
                }
            }
        }

        this.getState().getThreadPoolLogins().execute(new Runnable() {
            public void run() {
                Iterator var1 = var2.iterator();

                while(var1.hasNext()) {
                    IOFileManager.writeAllOpenFiles(((SendableSegmentController)var1.next()).getSegmentProvider().getSegmentDataIO().getManager());
                }

            }
        });
        updateFileList();
    }

    public GameClientState getState() {
        return this.state;
    }

    public boolean isMouseButtonDown(int var1) {
        if (Mouse.isButtonDown(var1)) {
            return true;
        } else {
            if (this.isJoystickOk()) {
                if (var1 == 0) {
                    return this.isJoystickMouseLeftButtonDown();
                }

                if (var1 == 1) {
                    return this.isJoystickMouseRigthButtonDown();
                }
            }

            return false;
        }
    }

    public void parseBlockBehavior(String var1) {
        try {
            this.state.setBlockBehaviorConfig(XMLTools.loadXML(new FileExt(var1)));
            this.state.setBlockBehaviorCheckSum(FileUtil.getSha1Checksum(var1));
        } catch (SAXException var2) {
            var2.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var2);
        } catch (IOException var3) {
            var3.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var3);
        } catch (ParserConfigurationException var4) {
            var4.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var4);
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        }
    }

    public void reapplyBlockBehavior() {
        this.flagReapplyBlockBehavior = true;
    }

    public List<TransporterDestinations> getActiveTransporterDestinations(SegmentController var1) {
        this.transporterDestinations.clear();
        Iterator var2 = this.getState().getCurrentSectorEntities().values().iterator();

        label47:
        while(true) {
            Sendable var3;
            do {
                do {
                    if (!var2.hasNext()) {
                        return this.transporterDestinations;
                    }
                } while(!((var3 = (Sendable)var2.next()) instanceof ManagedSegmentController));
            } while(!(((ManagedSegmentController)var3).getManagerContainer() instanceof TransporterModuleInterface));

            Iterator var6 = ((TransporterModuleInterface)((ManagedSegmentController)var3).getManagerContainer()).getTransporter().getCollectionManagers().iterator();

            while(true) {
                TransporterCollectionManager var4;
                do {
                    do {
                        if (!var6.hasNext()) {
                            continue label47;
                        }
                    } while(!(var4 = (TransporterCollectionManager)var6.next()).isValid());
                } while(!var4.isPublicAccess() && (!var4.isFactionAccess() || var4.getFactionId() != var1.getFactionId()) && !var4.getSegmentController().railController.isInAnyRailRelationWith(var1));

                TransporterDestinations var5;
                (var5 = new TransporterDestinations()).name = var4.getTransporterName();
                var5.pos = new Vector3i(var4.getControllerPos());
                var5.target = var4.getSegmentController();
                this.transporterDestinations.add(var5);
            }
        }
    }

    public List<SegmentController> getPossibleFleetAdd() {
        this.possibleFleet.clear();
        Iterator var1 = this.getState().getCurrentSectorEntities().values().iterator();

        while(true) {
            Ship var3;
            do {
                do {
                    Sendable var2;
                    do {
                        if (!var1.hasNext()) {
                            return this.possibleFleet;
                        }
                    } while(!((var2 = (Sendable)var1.next()) instanceof Ship));

                    var3 = (Ship)var2;
                } while(this.getState().getFleetManager().getByEntity(var3) != null);
            } while(this.state.getGameState().isOnlyAddFactionToFleet() && (var3.getFactionId() == 0 || this.state.getPlayer().getFactionId() != var3.getFactionId()));

            if (this.state.getController().isNeighborToClientSector(var3.getSectorId()) && var3.allowedToEdit(this.getState().getPlayer())) {
                this.possibleFleet.add(var3);
            }
        }
    }

    public void onStopClient() {
        this.creatorThreadController.onStopClient();
        this.elementCollectionCalculationThreadManager.onStop();
        if (this.clientChannel != null) {
            this.clientChannel.onStopClient();
        }

        if (this.state.getThreadedSegmentWriter() != null) {
            this.state.getThreadedSegmentWriter().shutdown();
        }

        if (this.state.getWorldDrawer() != null) {
            this.state.getWorldDrawer().onStopClient();
        }

        if (this.state.getGameState() != null) {
            this.state.getGameState().onStop();
        }

        this.state.setDoNotDisplayIOException(true);
        this.state.setExitApplicationOnDisconnect(false);
        if (this.getConnection() != null) {
            this.getConnection().disconnect();
        }

        IOFileManager.cleanUp(false);
    }

    public List<DialogInterface> getPlayerInputs() {
        return this.getInputController().getPlayerInputs();
    }

    public boolean isChatActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getChatControlManager().isActive();
    }

    public void handleKeyEvent(KeyEventInterface var1) {
        this.getState().getGlobalGameControlManager().handleKeyEvent(var1);
        this.getTutorialController().handleKeyEvent(var1);
        if (this.getState().getPlayer() != null) {
            KeyboardMappings[] var2;
            int var3 = (var2 = KeyboardMappings.values()).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                KeyboardMappings var5;
                if ((var5 = var2[var4]).getMapping() == var1.getKey()) {
                    this.getState().getPlayer().getControllerState().handleKeyEvent(var5);
                }
            }
        }

    }

    public void handleJoystickEventButton(JoystickEvent var1) {
        this.getState().getGlobalGameControlManager().handleJoystickInputPanels(var1);
    }

    public void handleMouseEvent(MouseEvent var1) {
        this.getState().getGlobalGameControlManager().handleMouseEvent(var1);
        if (this.getState().getPlayer() != null) {
            this.getState().getPlayer().getControllerState().handleMouseEvent(var1);
        }

    }

    public void handleLocalMouseInput() {
        this.getState().getPlayer().handleLocalMouseInput();
    }

    public void onMouseEvent(MouseEvent var1) {
        if (GLFrame.activeForInput) {
            this.handleMouseEvent(var1);
        }

        if (this.zoom > 0.0F && var1.pressedSecondary()) {
            boolean var2 = !this.getState().getWorldDrawer().getGameMapDrawer().isMapActive();
            if (AbstractScene.getZoomFactorUnchecked() == this.zoom) {
                AbstractScene.setZoomFactorForRender(var2, 1.0F);
                return;
            }

            AbstractScene.setZoomFactorForRender(var2, this.zoom);
        }

    }

    public boolean beforeInputUpdate() {
        if (!hasGraphics(this.state)) {
            return false;
        } else {
            boolean var1 = this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getPlayerCharacterManager().isTreeActive() && !this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getPlayerCharacterManager().isSuspended();
            this.zoom = -1.0F;
            int var2;
            int var4;
            if (var1 && this.state.getPlayer() != null) {
                var4 = this.state.getPlayer().getSelectedBuildSlot();
                MetaObject var3;
                if ((var2 = this.state.getPlayer().getInventory().getMeta(var4)) != -1 && (var3 = this.state.getMetaObjectManager().getObject(var2)) != null) {
                    this.zoom = var3.hasZoomFunction();
                }
            } else if (this.getState().isInFlightMode() && this.getState().getCurrentPlayerObject() instanceof ManagedSegmentController) {
                this.zoom = ((ManagedSegmentController)this.getState().getCurrentPlayerObject()).getManagerContainer().getSelectedWeaponZoom(this.state.getPlayer());
            }

            if (this.zoom <= 0.0F) {
                AbstractScene.setZoomFactorForRender(!this.state.getWorldDrawer().getGameMapDrawer().isMapActive(), 1.0F);
            }

            this.state.getPlayer().updateNTJoystick();
            var4 = this.state.getPlayerInputs().size() - 1;

            for(var2 = 0; var2 < this.state.getPlayerInputs().size(); ++var2) {
                if (this.state.getPlayerInputs().get(var2) instanceof RoundEndMenu) {
                    var4 = var2;
                    break;
                }
            }

            var2 = this.state.getPlayerInputs().size() - 1;
            if (!this.state.getPlayerInputs().isEmpty() && this.state.getPlayerInputs().get(var2) instanceof MainMenu) {
                --var2;
            }

            if (var4 < var2) {
                DialogInterface var5 = (DialogInterface)this.state.getPlayerInputs().get(var2);
                this.state.getPlayerInputs().set(var2, this.state.getPlayerInputs().get(var4));
                this.state.getPlayerInputs().set(var4, var5);
            }

            this.state.getGlobalGameControlManager().activateDelayed();
            PlayerGameControlManager var6 = this.state.getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager();

            assert !var6.getPlayerIntercationManager().getInShipControlManager().isActive() || !var6.getPlayerIntercationManager().getPlayerCharacterManager().isActive();

            return true;
        }
    }

    public BasicInputController getInputController() {
        return this.inputController;
    }

    public JoystickMappingFile getJoystick() {
        return this.inputController.getJoystick();
    }

    public TutorialController getTutorialController() {
        return this.tutorialController;
    }

    public static boolean isStarted() {
        return started;
    }

    public static void setStarted(boolean var0) {
        started = var0;
    }

    public void mouseButtonEventNetworktransmission(MouseEvent var1) {
        if (this.getState().getPlayer() != null && var1.state) {
            ++this.getState().getPlayer().tempSeed;
            this.getState().getPlayer().getNetworkObject().handleMouseEventButton(var1.state, var1.button);
        }

    }

    public boolean isWindowActive() {
        if (this.getState().getNumberOfUpdate() != this.wasWindowActiveUNum) {
            this.wasWindowActive = hasGraphics(this.state) && Display.isActive() && Keyboard.isCreated();
            this.wasWindowActiveUNum = this.getState().getNumberOfUpdate();
        }

        return this.wasWindowActive;
    }

    public boolean isWindowActiveOutOfMenu() {
        if (this.getState().getNumberOfUpdate() != this.wasWindowActiveOutOfMenuUNum) {
            this.wasWindowActiveOutOfMenu = this.isWindowActive() && this.outOfMenu();
            this.wasWindowActiveOutOfMenuUNum = this.getState().getNumberOfUpdate();
        }

        return this.wasWindowActiveOutOfMenu;
    }

    public boolean outOfMenu() {
        PlayerInteractionControlManager var1;
        return !(var1 = this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager()).isSuspended() && var1.isActive() && System.currentTimeMillis() - var1.getSuspentionFreedTime() > 400L;
    }

    public MineController getMineController() {
        return this.mineController;
    }

    public MissileManagerInterface getMissileManager() {
        return this.clientMissileManager;
    }

    public void onSelectedEntityChanged(SimpleTransformableSendableObject<?> var1, SimpleTransformableSendableObject var2) {
        Iterator var3 = this.entitySelectionListeners.iterator();

        while(var3.hasNext()) {
            ((EntitySelectionChangeChangeListener)var3.next()).onEntityChanged(var1, var2);
        }

    }

    class PhysicsCallback extends InternalTickCallback {
        private PhysicsCallback() {
        }

        public void internalTick(DynamicsWorld var1, float var2) {
            int var17 = GameClientController.this.getState().getPhysics().getDynamicsWorld().getDispatcher().getNumManifolds();

            for(int var18 = 0; var18 < var17; ++var18) {
                PersistentManifold var3;
                CollisionObject var4 = (CollisionObject)(var3 = GameClientController.this.getState().getPhysics().getDynamicsWorld().getDispatcher().getManifoldByIndexInternal(var18)).getBody0();
                CollisionObject var5 = (CollisionObject)var3.getBody1();
                int var6 = var3.getNumContacts();
                if (var4 != null && var5 != null && var4.getUserPointer() instanceof Integer && var5.getUserPointer() instanceof Integer) {
                    int var7 = (Integer)var4.getUserPointer();
                    int var8 = (Integer)var5.getUserPointer();
                    Sendable var19 = (Sendable)GameClientController.this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var7);
                    Sendable var20 = (Sendable)GameClientController.this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var8);
                    Vector3f var9 = new Vector3f();
                    Vector3f var10 = new Vector3f();
                    if (var19 instanceof Collisionable && var20 instanceof Collisionable) {
                        Collisionable var11 = (Collisionable)var19;
                        Collisionable var12 = (Collisionable)var20;
                        boolean var13 = var11.needsManifoldCollision();
                        boolean var14 = var12.needsManifoldCollision();
                        if (var13 || var14) {
                            for(int var15 = 0; var15 < var6; ++var15) {
                                ManifoldPoint var16;
                                if ((var16 = var3.getContactPoint(var15)).getDistance() < 0.0F && var4.getUserPointer() != null && var5.getUserPointer() != null) {
                                    var16.getPositionWorldOnA(var9);
                                    var16.getPositionWorldOnB(var10);
                                    Vector3f var10000 = var16.normalWorldOnB;
                                    if (var13) {
                                        var11.onCollision(var16, var20);
                                    }

                                    if (var14) {
                                        var12.onCollision(var16, var19);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    class ConnectionDialogUpdater extends Observable {
        private ConnectionDialogUpdater() {
        }

        public void update(String var1) {
            this.setChanged();
            this.notifyObservers(var1);
        }

        public void dispose() {
            this.setChanged();
            this.notifyObservers();
        }
    }

    static class StringDistance {
        String string;
        int distance;

        StringDistance(String var1, int var2) {
            this.string = var1;
            this.distance = var2;
        }
    }

    public class SectorChangeObservable extends Observable {
        public SectorChangeObservable() {
        }

        public void note() {
            this.setChanged();
            this.notifyObservers();
        }
    }
}
