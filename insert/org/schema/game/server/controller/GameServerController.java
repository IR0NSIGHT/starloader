//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.server.controller;

import api.listener.events.EntitySpawnEvent;
import api.listener.events.player.PlayerSpawnEvent;
import api.mod.StarLoader;
import api.server.Server;
import api.utils.StarRunnable;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import obfuscated.av;
import org.apache.commons.io.FileUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.schema.common.LogUtil;
import org.schema.common.ParseException;
import org.schema.common.XMLTools;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.controller.MineInterface;
import org.schema.game.client.data.GameStateControllerInterface;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.common.Starter;
import org.schema.game.common.controller.CreatorThreadControlInterface;
import org.schema.game.common.controller.CreatorThreadController;
import org.schema.game.common.controller.ElementHandlerInterface;
import org.schema.game.common.controller.Planet;
import org.schema.game.common.controller.PlanetIco;
import org.schema.game.common.controller.SegmentBufferIteratorEmptyInterface;
import org.schema.game.common.controller.SegmentBufferManager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SegmentOutOfBoundsException;
import org.schema.game.common.controller.SegmentProvider;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.TransientSegmentController;
import org.schema.game.common.controller.Vehicle;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.database.DatabaseEntry;
import org.schema.game.common.controller.database.DatabaseIndex;
import org.schema.game.common.controller.database.tables.EntityTable.Despawn;
import org.schema.game.common.controller.elements.ElementCollectionCalculationThreadExecution;
import org.schema.game.common.controller.elements.ElementCollectionCalculationThreadManager;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.controller.elements.UsableElementManager;
import org.schema.game.common.controller.elements.mines.MineController;
import org.schema.game.common.controller.elements.missile.MissileController;
import org.schema.game.common.controller.io.IOFileManager;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.crashreporter.CrashReporter;
import org.schema.game.common.data.BlockBulkSerialization;
import org.schema.game.common.data.EntityFileTools;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SegmentRetrieveCallback;
import org.schema.game.common.data.SegmentSignature;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.UploadInProgressException;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.explosion.ExplosionRunnable;
import org.schema.game.common.data.gamemode.AbstractGameMode;
import org.schema.game.common.data.gamemode.GameModeException;
import org.schema.game.common.data.gamemode.battle.BattleMode;
import org.schema.game.common.data.missile.MissileControllerInterface;
import org.schema.game.common.data.missile.MissileManagerInterface;
import org.schema.game.common.data.physics.GamePhysicsObject;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.catalog.CatalogPermission;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.game.common.data.player.inventory.NoSlotFreeException;
import org.schema.game.common.data.world.DeserializationException;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.RemoteSegment;
import org.schema.game.common.data.world.Sector;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.common.data.world.Universe;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.common.util.GuiErrorHandler;
import org.schema.game.common.version.Version;
import org.schema.game.network.objects.BitsetResponse;
import org.schema.game.network.objects.NetworkSegmentProvider;
import org.schema.game.network.objects.remote.RemoteBitset;
import org.schema.game.network.objects.remote.RemoteSegmentRemoteObj;
import org.schema.game.server.controller.pathfinding.AbstractPathFindingHandler;
import org.schema.game.server.controller.pathfinding.BreakTestRequest;
import org.schema.game.server.controller.pathfinding.SegmentBreaker;
import org.schema.game.server.controller.pathfinding.SegmentPathFindingHandler;
import org.schema.game.server.controller.pathfinding.SegmentPathGroundFindingHandler;
import org.schema.game.server.controller.pathfinding.SegmentPathRequest;
import org.schema.game.server.data.Admin;
import org.schema.game.server.data.CreatureSpawn;
import org.schema.game.server.data.EntityRequest;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerAccountEntry;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.game.server.data.ProtectedUplinkName;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.ServerExecutionJob;
import org.schema.game.server.data.ShipSpawnWave;
import org.schema.game.server.data.GameServerState.FileRequest;
import org.schema.game.server.data.admin.AdminCommandNotFoundException;
import org.schema.game.server.data.admin.AdminCommandQueueElement;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.game.server.data.blueprint.ChildStats;
import org.schema.game.server.data.blueprint.SegmentControllerOutline;
import org.schema.game.server.data.blueprint.SegmentControllerSpawnCallbackDirect;
import org.schema.game.server.data.simulation.npc.NPCFaction;
import org.schema.schine.auth.SessionCallback;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import org.schema.schine.graphicsengine.core.ResourceException;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.BoundingBox;
import org.schema.schine.graphicsengine.util.WorldToScreenConverterFixedAspect;
import org.schema.schine.network.ChatSystem;
import org.schema.schine.network.RegisteredClientInterface;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.ServerInfo;
import org.schema.schine.network.SynchronizationContainerController;
import org.schema.schine.network.client.ClientState;
import org.schema.schine.network.commands.Login.LoginCode;
import org.schema.schine.network.objects.Sendable;
import org.schema.schine.network.server.AuthenticationRequiredException;
import org.schema.schine.network.server.ServerController;
import org.schema.schine.network.server.ServerMessage;
import org.schema.schine.network.server.ServerState;
import org.schema.schine.physics.Physical;
import org.schema.schine.resource.DiskWritable;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.ResourceLoader;
import org.schema.schine.resource.ResourceMap;
import org.schema.schine.resource.tag.Tag;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GameServerController extends ServerController implements MineInterface, GameStateControllerInterface, CreatorThreadControlInterface, ElementHandlerInterface, MissileControllerInterface {
    public static final String BLOCK_BEHAVIOR_DEFAULT_PATH = "./data/config/blockBehaviorConfig.xml";
    public static WorldToScreenConverterFixedAspect worldToScreenConverter = new WorldToScreenConverterFixedAspect();
    public static Matrix4f projectionMatrix = new Matrix4f();
    public final ObjectArrayFIFOQueue<RegisteredClientOnServer> initialGameModeRequests = new ObjectArrayFIFOQueue();
    private final SynchronizationContainerController synchController;
    private final MissileController missileController;
    private final CreatorThreadController creatorThreadController;
    private final ObjectOpenHashSet<RemoteSector> sectorsToUpdate = new ObjectOpenHashSet();
    private final SegmentBreaker segmentBreaker;
    private final SegmentPathFindingHandler segmentPathFinder;
    private final SegmentPathGroundFindingHandler segmentPathGroundFinder;
    long lastSynchronize;
    private GameServerState state;
    private long endRoundMode;
    private long lastSave;
    private ServerSegmentRequestThread serverSegmentRequestThread;
    private Set<RegisteredClientOnServer> clientCopy = new HashSet();
    private ElementCollectionCalculationThreadManager elementCollectionCalculationThreadManager = new ElementCollectionCalculationThreadManager(true);
    private byte[] blockBehaviorChanged = null;
    private String lastMessage = "";
    private long lastExceptionTime;
    private ObjectOpenHashSet<Class<Exception>> exceptionSet = new ObjectOpenHashSet();
    private final Int2LongOpenHashMap sectorLag = new Int2LongOpenHashMap();
    private long lastDelayAutoSaveMsgSent;
    private Object reqLock = new Object();
    private boolean flaggedShutdown;
    private final MineController mineController;
    private final List<SectorListener> sectorListeners = new ObjectArrayList();

    public GameServerController(GameServerState var1) {
        super(var1);
        var1.logbookEntries = readLogbookEntries();
        this.missileController = new MissileController(var1);
        this.sectorListeners.add(this.mineController = new MineController(var1));
        this.state = var1;
        this.getTimer().initialize(true);
        this.lastSave = System.currentTimeMillis();
        this.synchController = new SynchronizationContainerController(var1.getLocalAndRemoteObjectContainer(), var1, false);
        this.creatorThreadController = new CreatorThreadController(var1);
        this.creatorThreadController.start();
        this.serverSegmentRequestThread = new ServerSegmentRequestThread(var1);
        this.serverSegmentRequestThread.start();
        this.segmentBreaker = new SegmentBreaker(this.getState());
        this.segmentPathFinder = new SegmentPathFindingHandler(this.getState());
        this.segmentPathGroundFinder = new SegmentPathGroundFindingHandler(this.getState());
        ExplosionRunnable.initialize();
        FileExt var5;
        (var5 = new FileExt(GameServerState.ENTITY_DATABASE_PATH + "tmp/")).mkdirs();
        (new FileExt(GameServerState.ENTITY_DATABASE_PATH + "backupCorrupted")).mkdir();
        if (var5.exists()) {
            File[] var6 = var5.listFiles();

            for(int var2 = 0; var2 < var6.length; ++var2) {
                if (var6[var2].getName().endsWith(".tmp")) {
                    FileExt var3 = new FileExt(GameServerState.ENTITY_DATABASE_PATH + "backupCorrupted/" + var6[var2].getName());

                    try {
                        FileUtil.copyFile(var6[var2], var3);
                    } catch (IOException var4) {
                        var4.printStackTrace();
                    }

                    var6[var2].delete();
                }
            }
        }

        this.elementCollectionCalculationThreadManager.start();
    }

    public static void checkIP(String var0) throws NoIPException {
        try {
            String[] var1 = var0.split("\\.");

            for(int var2 = 0; var2 < 4; ++var2) {
                int var3;
                if ((var3 = Integer.parseInt(var1[var2])) < 0 || var3 > 255) {
                    throw new NoIPException(var0);
                }
            }

        } catch (Exception var4) {
            var4.printStackTrace();
            throw new NoIPException(var0);
        }
    }

    public static void main(String[] var0) {
    }

    public static String[] readLogbookEntries() {
        BufferedReader var1;
        String[] var24;
        label203: {
            FileExt var0 = new FileExt("./data/config/logbookEntriesGen.txt");
            var1 = null;
            if (var0.exists()) {
                boolean var12 = false;

                label214: {
                    label215: {
                        try {
                            var12 = true;
                            var1 = new BufferedReader(new FileReader(var0));
                            ArrayList var21 = new ArrayList();
                            StringBuffer var2 = new StringBuffer();

                            String var3;
                            while((var3 = var1.readLine()) != null) {
                                if (var3.trim().equals("-")) {
                                    if (var2.length() > 0) {
                                        var2.deleteCharAt(var2.length() - 1);
                                    }

                                    if (var2.length() >= 512) {
                                        var2.substring(0, 511);
                                    }

                                    var21.add(var2.toString());
                                    var2 = new StringBuffer();
                                } else {
                                    var2.append(var3 + "\n");
                                }
                            }

                            if (var2.length() > 0) {
                                var21.add(var2.toString());
                            }

                            if (var21.size() > 0) {
                                String[] var22 = new String[var21.size()];

                                for(int var23 = 0; var23 < var22.length; ++var23) {
                                    var22[var23] = (String)var21.get(var23);
                                }

                                var24 = var22;
                                var12 = false;
                                break label203;
                            }

                            var12 = false;
                            break label215;
                        } catch (FileNotFoundException var18) {
                            var18.printStackTrace();
                            var12 = false;
                        } catch (IOException var19) {
                            var19.printStackTrace();
                            var12 = false;
                            break label214;
                        } finally {
                            if (var12) {
                                if (var1 != null) {
                                    try {
                                        var1.close();
                                    } catch (IOException var13) {
                                        var13.printStackTrace();
                                    }
                                }

                            }
                        }

                        if (var1 != null) {
                            try {
                                var1.close();
                            } catch (IOException var16) {
                                var16.printStackTrace();
                            }

                            return new String[]{"Er! Ror ha, pe need! No en! Tries!"};
                        }

                        return new String[]{"Er! Ror ha, pe need! No en! Tries!"};
                    }

                    try {
                        var1.close();
                    } catch (IOException var17) {
                        var17.printStackTrace();
                    }

                    return new String[]{"Er! Ror ha, pe need! No en! Tries!"};
                }

                if (var1 != null) {
                    try {
                        var1.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }
                }
            }

            return new String[]{"Er! Ror ha, pe need! No en! Tries!"};
        }

        try {
            var1.close();
        } catch (IOException var14) {
            var14.printStackTrace();
        }

        return var24;
    }

    public static void handleSegmentRequest(NetworkSegmentProvider var0, RemoteSegment var1, long var2, long var4, int var6) {
        if (var0 != null) {
            synchronized(var0) {
                if (var1.getLastChanged() <= var4 && var1.getSize() == var6) {
                    var0.signatureOkBuffer.add(var2);
                } else if (var1.isEmpty()) {
                    var0.signatureEmptyBuffer.add(var2);
                } else {
                    SegmentSignature var9;
                    (var9 = new SegmentSignature(new Vector3i(var1.pos), (long)var1.getSegmentController().getId(), var1.isEmpty(), (short)(var1.isEmpty() ? -1 : var1.getSize()))).context = var1.getSegmentController();
                    var0.segmentBuffer.add(new RemoteSegmentRemoteObj(var1, var9, var0));
                }

            }
        }
    }

    public void addAdminDeniedCommand(String var1, String var2, AdminCommands var3) {
        boolean var4 = false;
        synchronized(this.state.getAdmins()) {
            String var6 = var2.trim().toLowerCase(Locale.ENGLISH);
            Admin var9;
            if ((var9 = (Admin)this.state.getAdmins().get(var6)) != null) {
                var4 = var9.deniedCommands.add(var3);
            }
        }

        if (var4) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' added a denied command to admin: '" + var2 + "': " + var3.name());

            try {
                this.writeAdminsToDisk();
                return;
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void removeAdminDeniedCommand(String var1, String var2, AdminCommands var3) {
        boolean var4 = false;
        synchronized(this.state.getAdmins()) {
            String var6 = var2.trim().toLowerCase(Locale.ENGLISH);
            Admin var9;
            if ((var9 = (Admin)this.state.getAdmins().get(var6)) != null) {
                var4 = var9.deniedCommands.remove(var3);
            }
        }

        if (var4) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' removed a denied command from admin: '" + var2 + "': " + var3.name());

            try {
                this.writeAdminsToDisk();
                return;
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void addAdmin(String var1, String var2) {
        synchronized(this.state.getAdmins()) {
            String var5 = var2.trim().toLowerCase(Locale.ENGLISH);
            Admin var3 = new Admin(var5);
            this.state.getAdmins().put(var5, var3);
        }

        LogUtil.log().fine("[ADMIN] '" + var1 + "' added to admins: '" + var2 + "'");

        try {
            this.writeAdminsToDisk();
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public void addAdminsFromDisk() throws IOException {
        FileExt var1 = new FileExt("./admins.txt");
        BufferedReader var10 = new BufferedReader(new FileReader(var1));
        String var2 = null;

        while((var2 = var10.readLine()) != null) {
            synchronized(this.state.getAdmins()) {
                if (var2.contains("#")) {
                    String[] var11 = var2.trim().split("#", 2);
                    Admin var4 = new Admin(var11[0].trim().toLowerCase(Locale.ENGLISH));
                    this.state.getAdmins().put(var4.name, var4);
                    int var5 = (var11 = var11[1].trim().split(",")).length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        String var7 = var11[var6];

                        try {
                            AdminCommands var12 = AdminCommands.valueOf(var7.toUpperCase(Locale.ENGLISH).trim());
                            var4.deniedCommands.add(var12);
                        } catch (Exception var8) {
                            var8.printStackTrace();
                        }
                    }
                } else {
                    this.state.getAdmins().put(var2.trim().toLowerCase(Locale.ENGLISH), new Admin(var2.trim().toLowerCase(Locale.ENGLISH)));
                }
            }
        }

        var10.close();
    }

    public void addBannedIp(String var1, String var2, long var3) throws NoIPException {
        checkIP(var2);
        boolean var5 = false;
        synchronized(this.state.getBlackListedIps()) {
            var5 = this.state.getBlackListedIps().add(new PlayerAccountEntry(var3, var2.trim()));
        }

        if (var5) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' banned ip: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
                return;
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

    }

    public void addBannedName(String var1, String var2, long var3) {
        boolean var5 = false;
        synchronized(this.state.getBlackListedNames()) {
            var5 = this.state.getBlackListedNames().add(new PlayerAccountEntry(var3, var2.trim().toLowerCase(Locale.ENGLISH)));
        }

        if (var5) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' banned playerName: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
                return;
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

    }

    public void addBannedAccount(String var1, String var2, long var3) {
        boolean var5 = false;
        synchronized(this.state.getBlackListedAccounts()) {
            var5 = this.state.getBlackListedAccounts().add(new PlayerAccountEntry(var3, var2.trim()));
        }

        if (var5) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' banned StarMade Account: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
                return;
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

    }

    private void addBlacklistFromDisk() throws IOException {
        FileExt var1 = new FileExt("./blacklist.txt");
        BufferedReader var15 = new BufferedReader(new FileReader(var1));
        String var2 = null;

        while((var2 = var15.readLine()) != null) {
            if (var2.startsWith("nm:")) {
                synchronized(this.state.getBlackListedNames()) {
                    this.state.getBlackListedNames().add(new PlayerAccountEntry(var2.substring(3).trim().toLowerCase(Locale.ENGLISH)));
                }
            } else if (var2.startsWith("ip:")) {
                synchronized(this.state.getBlackListedIps()) {
                    String var5 = var2.substring(3).trim();

                    try {
                        checkIP(var5);
                        this.state.getBlackListedIps().add(new PlayerAccountEntry(var5));
                    } catch (NoIPException var13) {
                        var13.printStackTrace();
                    }
                }
            } else if (var2.startsWith("ac:")) {
                synchronized(this.state.getBlackListedAccounts()) {
                    this.state.getBlackListedAccounts().add(new PlayerAccountEntry(var2.substring(3).trim()));
                }
            } else {
                long var18;
                String[] var3;
                PlayerAccountEntry var4;
                if (var2.startsWith("nmt:")) {
                    var18 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                    synchronized(this.state.getBlackListedNames()) {
                        if ((var4 = new PlayerAccountEntry(var18, var3[2].trim().toLowerCase(Locale.ENGLISH))).isValid(System.currentTimeMillis())) {
                            this.state.getBlackListedNames().add(var4);
                        }
                    }
                } else if (var2.startsWith("ipt:")) {
                    var18 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                    synchronized(this.state.getBlackListedIps()) {
                        String var17 = var3[2].trim();

                        try {
                            checkIP(var17);
                            PlayerAccountEntry var16;
                            if ((var16 = new PlayerAccountEntry(var18, var17)).isValid(System.currentTimeMillis())) {
                                this.state.getBlackListedIps().add(var16);
                            }
                        } catch (NoIPException var9) {
                            var9.printStackTrace();
                        }
                    }
                } else if (var2.startsWith("act:")) {
                    var18 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                    synchronized(this.state.getBlackListedAccounts()) {
                        if ((var4 = new PlayerAccountEntry(var18, var3[2].trim())).isValid(System.currentTimeMillis())) {
                            this.state.getBlackListedAccounts().add(var4);
                        }
                    }
                }
            }
        }

        var15.close();
    }

    public ProtectedUplinkName removeProtectedUser(String var1) {
        ProtectedUplinkName var2 = null;
        synchronized(this.state.getProtectedUsers()) {
            var2 = (ProtectedUplinkName)this.state.getProtectedUsers().remove(var1.trim());
        }

        try {
            this.writeProtectedUsersToDisk();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return var2;
    }

    public void addProtectedUser(String var1, String var2) {
        System.err.println("[AUTH] PROTECTING USER " + var2 + " under uplink id " + var1.trim());
        LogUtil.log().fine("[AUTH] PROTECTING USER " + var2 + " under uplink id " + var1.trim());
        synchronized(this.state.getProtectedUsers()) {
            int var4 = Math.max(0, (Integer)ServerConfig.PROTECTED_NAMES_BY_ACCOUNT.getCurrentState());
            ArrayList var5 = new ArrayList();
            Iterator var6 = this.state.getProtectedUsers().entrySet().iterator();

            while(var6.hasNext()) {
                Entry var7;
                if (((ProtectedUplinkName)(var7 = (Entry)var6.next()).getValue()).equals(var1) && !this.isAdmin(((ProtectedUplinkName)var7.getValue()).playername)) {
                    var5.add(var7.getValue());
                }
            }

            ProtectedUplinkName var10;
            if (var5.size() > var4) {
                Collections.sort(var5);

                while(var5.size() > var4) {
                    var10 = (ProtectedUplinkName)var5.remove(0);
                    System.err.println("[AUTH] removing protection oldest used username of account " + var1 + ": " + var10.playername);
                    LogUtil.log().fine("[AUTH] removing protection oldest used username of account " + var1 + ": " + var10.playername);
                    this.state.getProtectedUsers().remove(var10.playername);
                }
            }

            var10 = new ProtectedUplinkName(var1, var2.trim(), System.currentTimeMillis());
            this.state.getProtectedUsers().put(var2.trim(), var10);
        }

        try {
            this.writeProtectedUsersToDisk();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    public void addProtectedUsersFromDisk() throws IOException {
        FileExt var1 = new FileExt("./protected.txt");
        BufferedReader var11 = new BufferedReader(new FileReader(var1));
        String var2 = null;

        while((var2 = var11.readLine()) != null) {
            synchronized(this.state.getProtectedUsers()) {
                String[] var4;
                try {
                    var4 = var2.split(";");
                    long var6 = System.currentTimeMillis();
                    if (var4.length > 2) {
                        try {
                            var6 = Long.parseLong(var4[2]);
                        } catch (NumberFormatException var8) {
                            var8.printStackTrace();
                        }
                    }

                    ProtectedUplinkName var5 = new ProtectedUplinkName(var4[1].trim(), var4[0], var6);
                    this.state.getProtectedUsers().put(var4[0].toLowerCase(Locale.ENGLISH), var5);
                } catch (Exception var9) {
                    var4 = null;
                    var9.printStackTrace();
                    System.err.println("EXCEPTION: Could not protect " + var2 + " invalid format");
                }
            }
        }

        var11.close();
    }

    private void addWhitelistFromDisk() throws IOException {
        FileExt var1 = new FileExt("./whitelist.txt");
        BufferedReader var22 = new BufferedReader(new FileReader(var1));
        String var2 = null;

        while((var2 = var22.readLine()) != null) {
            if (var2.startsWith("nm:")) {
                synchronized(this.state.getWhiteListedNames()) {
                    this.state.getWhiteListedNames().add(new PlayerAccountEntry(var2.substring(3).trim().toLowerCase(Locale.ENGLISH)));
                }
            } else if (var2.startsWith("ip:")) {
                synchronized(this.state.getWhiteListedIps()) {
                    String var5 = var2.substring(3).trim();

                    try {
                        checkIP(var5);
                        this.state.getWhiteListedIps().add(new PlayerAccountEntry(var5));
                    } catch (NoIPException var20) {
                        var20.printStackTrace();
                    }
                }
            } else if (var2.startsWith("ac:")) {
                synchronized(this.state.getWhiteListedAccounts()) {
                    this.state.getWhiteListedAccounts().add(new PlayerAccountEntry(var2.substring(3).trim()));
                }
            } else {
                String[] var3;
                PlayerAccountEntry var7;
                long var24;
                if (var2.startsWith("nmt:")) {
                    try {
                        var24 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                        synchronized(this.state.getWhiteListedNames()) {
                            if ((var7 = new PlayerAccountEntry(var24, var3[2].trim().toLowerCase(Locale.ENGLISH))).isValid(System.currentTimeMillis())) {
                                this.state.getWhiteListedNames().add(var7);
                            }
                        }
                    } catch (NumberFormatException var17) {
                        var17.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Valid until must be a date in ms. Line: " + var2);
                    } catch (Exception var18) {
                        var18.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Wrong format. Line: " + var2);
                    }
                } else if (var2.startsWith("ipt:")) {
                    try {
                        var24 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                        synchronized(this.state.getWhiteListedIps()) {
                            String var25 = var3[2].trim();

                            try {
                                checkIP(var25);
                                PlayerAccountEntry var23;
                                if ((var23 = new PlayerAccountEntry(var24, var25)).isValid(System.currentTimeMillis())) {
                                    this.state.getWhiteListedIps().add(var23);
                                }
                            } catch (NoIPException var12) {
                                var12.printStackTrace();
                            }
                        }
                    } catch (NumberFormatException var14) {
                        var14.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Valid until must be a date in ms. Line: " + var2);
                    } catch (Exception var15) {
                        var15.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Wrong format. Line: " + var2);
                    }
                } else if (var2.startsWith("act:")) {
                    try {
                        var24 = Long.parseLong((var3 = var2.split(":", 3))[1]);
                        synchronized(this.state.getWhiteListedAccounts()) {
                            if ((var7 = new PlayerAccountEntry(var24, var3[2].trim())).isValid(System.currentTimeMillis())) {
                                this.state.getWhiteListedAccounts().add(var7);
                            }
                        }
                    } catch (NumberFormatException var10) {
                        var10.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Valid until must be a date in ms. Line: " + var2);
                    } catch (Exception var11) {
                        var11.printStackTrace();
                        System.err.println("ERROR IN WHITELIST ENTRY. Wrong format. Line: " + var2);
                    }
                }
            }
        }

        var22.close();
    }

    public void addWitelistedIp(String var1, long var2) throws NoIPException {
        checkIP(var1);
        boolean var4 = false;
        synchronized(this.state.getWhiteListedIps()) {
            var4 = this.state.getWhiteListedIps().add(new PlayerAccountEntry(var2, var1.trim()));
        }

        if (var4) {
            try {
                this.writeWhiteListToDisk();
                return;
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void addWitelistedName(String var1, long var2) {
        boolean var4 = false;
        synchronized(this.state.getWhiteListedNames()) {
            var4 = this.state.getWhiteListedNames().add(new PlayerAccountEntry(var2, var1.trim().toLowerCase(Locale.ENGLISH)));
        }

        if (var4) {
            try {
                this.writeWhiteListToDisk();
                return;
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void addWitelistedAccount(String var1, long var2) {
        boolean var4 = false;
        synchronized(this.state.getWhiteListedAccounts()) {
            var4 = this.state.getWhiteListedAccounts().add(new PlayerAccountEntry(var2, var1.trim()));
        }

        if (var4) {
            try {
                this.writeWhiteListToDisk();
                return;
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public boolean authenticate(String var1, SessionCallback var2) throws AuthenticationRequiredException {
        System.err.println("[AUTH] authenticating " + var1 + "; useAuth: " + ServerConfig.USE_STARMADE_AUTHENTICATION.isOn() + "; requireAuth: " + ServerConfig.REQUIRE_STARMADE_AUTHENTICATION.isOn());
        LogUtil.log().fine("[AUTH] authenticating " + var1 + "; useAuth: " + ServerConfig.USE_STARMADE_AUTHENTICATION.isOn() + "; requireAuth: " + ServerConfig.REQUIRE_STARMADE_AUTHENTICATION.isOn());
        if (!this.authSession(var1, var2, ServerConfig.REQUIRE_STARMADE_AUTHENTICATION.isOn(), ServerConfig.USE_STARMADE_AUTHENTICATION.isOn(), this.isUserProtected(var1))) {
            throw new IllegalArgumentException("AUTH FAILED");
        } else {
            return true;
        }
    }

    public void protectUserName(String var1, SessionCallback var2) {
        if (var2.getStarMadeUserName() != null && var2.getStarMadeUserName().length() > 0) {
            this.addProtectedUser(var2.getStarMadeUserName(), var1.toLowerCase(Locale.ENGLISH));
        }

    }

    public void update(Timer var1) throws IOException, AdminCommandNotFoundException, SQLException {

        //long currentTime = System.currentTimeMillis();
        //long timeDelta = currentTime - lastTime;
        //lastTime = currentTime;
        //Server.broadcastMessage("Update: " + timeDelta);
        //Determined that the server runs at about 25TPS, average of 38mspt

        this.getState().udpateTime = System.currentTimeMillis();
        ServerInfo.curtime = this.getState().udpateTime;
        GameServerState.totalDockingChecks = SegmentController.dockingChecks;
        SegmentController.dockingChecks = 0;
        this.state.incUpdateNumber();

        assert !this.flaggedShutdown;

        if (ServerState.isFlagShutdown()) {
            ServerState.setFlagShutdown(false);
            boolean var142 = ClientState.setFinishedFrameAfterLocalServerShutdown;
            this.flaggedShutdown = true;
            LogUtil.log().fine("[SHUTDOWN] Shutting down server");
            System.out.println("[SERVER] now executing regular shutdown");
            ServerState.setShutdown(true);
            this.onShutDown(false);
            if (var142 && !GraphicsContext.isFinished()) {
                LogUtil.closeAll();
            }

            ServerState.serverIsOkToShutdown = true;
            if (!GameClientController.isStarted()) {
                try {
                    throw new Exception("System.exit() called");
                } catch (Exception var99) {
                    var99.printStackTrace();
                    System.exit(0);
                }
            } else {
                if (var142) {
                    GLFrame.setFinished(true);
                }

            }
        } else {
            if (this.state.getTimedShutdownStart() > 0L) {
                long var2 = System.currentTimeMillis() - this.state.getTimedShutdownStart();
                float var4;
                if ((var4 = (float)((long)(this.state.getTimedShutdownSeconds() * 1000) - var2) / 1000.0F) <= 0.0F) {
                    System.err.println("[SERVER] SCHEDULED SHUTDOWN NOW EXECUTING. EXITING SERVER!");
                    ServerState.setFlagShutdown(true);
                    return;
                }

                synchronized(this.state) {
                    this.state.setSynched();

                    try {
                        //if (var4 > 60.0F && (int)(Float)this.state.getGameState().getNetworkObject().serverShutdown.get() != (int)var4) {
                        if (var4 > 60.0F && this.state.getGameState().getNetworkObject().serverShutdown.get().intValue() != (int)var4) {
                            this.state.getGameState().getNetworkObject().serverShutdown.set(var4);
                        } else {
                            this.state.getGameState().getNetworkObject().serverShutdown.set(var4);
                        }
                    } finally {
                        this.state.setUnsynched();
                    }
                }
            }

            long var3;
            if (this.state.getTimedMessageStart() > 0L) {
                synchronized(this.state) {
                    label2542: {
                        this.state.setSynched();

                        try {
                            var3 = System.currentTimeMillis() - this.state.getTimedMessageStart();
                            float var5;
                            if ((var5 = (float)((long)(this.state.getTimedMessageSeconds() * 1000) - var3) / 1000.0F) <= 0.0F) {
                                this.state.setTimedMessageStart(-1L);
                                this.state.getGameState().getNetworkObject().serverCountdownTime.set(-1.0F);
                                this.state.getGameState().getNetworkObject().serverCountdownMessage.set("");
                            } else {
                                this.state.getGameState().getNetworkObject().serverCountdownMessage.set(this.state.getTimedMessage());
                                if (var5 > 60.0F && this.state.getGameState().getNetworkObject().serverCountdownTime.get().intValue() != (int)var5) {
                                    this.state.getGameState().getNetworkObject().serverCountdownTime.set(var5);
                                } else {
                                    this.state.getGameState().getNetworkObject().serverCountdownTime.set(var5);
                                }
                                break label2542;
                            }
                        } finally {
                            this.state.setUnsynched();
                        }

                        return;
                    }
                }
            }

            if (GameServerState.allocatedSegmentData > 0) {
                GameServerState.lastAllocatedSegmentData = GameServerState.allocatedSegmentData;
            }

            GameServerState.lastFreeSegmentData = this.getServerState().getSegmentDataManager().sizeFree();
            GameServerState.allocatedSegmentData = 0;
            GameServerState.collectionUpdates = 0;
            GameServerState.segmentRequestQueue = this.state.getSegmentRequests().size();
            GameServerState.dayTime = this.state.getGameState().getRotationProgession();
            if (this.endRoundMode > 0L) {
                this.handleEndRound();
            }

            GlUtil.gluPerspective(projectionMatrix, 75.0F, 1.3333333F, 0.1F, 1500.0F, false);
            int var141 = (Integer)ServerConfig.SECTOR_AUTOSAVE_SEC.getCurrentState() * 1000;
            if (System.currentTimeMillis() > this.state.delayAutosave && var141 > 0 && System.currentTimeMillis() - this.lastSave > (long)var141) {
                System.out.println("[SERVER] SERVER AUTOSAVE START. Dumping server State!");
                this.broadcastMessage(new Object[]{457}, 2);
                this.writeEntitiesToDatabaseAtAutosaveOrShutdown(false, false, this.state.getUniverse());
                System.out.println("[SERVER] SERVER AUTOSAVE END. ServerState saved!");
                this.lastSave = System.currentTimeMillis();
            } else if (System.currentTimeMillis() < this.state.delayAutosave && System.currentTimeMillis() - this.lastDelayAutoSaveMsgSent > 60000L) {
                this.broadcastMessage(new Object[]{458, (this.state.delayAutosave - System.currentTimeMillis()) / 1000L}, 2);
                this.lastDelayAutoSaveMsgSent = System.currentTimeMillis();
            }

            if (!this.state.getFileRequests().isEmpty()) {
                synchronized(this.state) {
                    this.state.setSynched();
                    synchronized(this.state.getFileRequests()) {
                        if (!this.state.getFileRequests().isEmpty()) {
                            FileRequest var148 = (FileRequest)this.state.getFileRequests().remove(0);
                            if (this.state.getLocalAndRemoteObjectContainer().getLocalObjects().containsKey(var148.channel.getPlayerId())) {
                                try {
                                    String var6 = "./server-skins/" + var148.req;
                                    System.err.println("[SERVER] transferring file to client " + var148.channel.getId() + ": " + var6);
                                    var148.channel.getClientFileDownloadController().upload(var6);
                                    this.state.getActiveFileRequests().add(var148);
                                } catch (UploadInProgressException var112) {
                                    System.err.println("[SERVER] Cannot upload " + var148.req + ": an upload already is in progress for channel " + var148.channel.getId());
                                    this.state.getFileRequests().add(var148);
                                } catch (IOException var113) {
                                    var113.printStackTrace();
                                }
                            }
                        }
                    }

                    this.state.setUnsynched();
                }
            }

            int var150;
            if (!this.state.getActiveFileRequests().isEmpty()) {
                synchronized(this.state) {
                    this.state.setSynched();
                    synchronized(this.state.getActiveFileRequests()) {
                        for(var150 = 0; var150 < this.state.getActiveFileRequests().size(); ++var150) {
                            FileRequest var151 = (FileRequest)this.state.getActiveFileRequests().get(var150);
                            if (this.getState().getPlayerStatesByName().values().contains(var151.channel.getPlayer()) && var151.channel.getClientFileDownloadController().isNeedsUpdate()) {
                                var151.channel.updateLocal(var1);
                            } else {
                                this.state.getActiveFileRequests().remove(var150);
                                --var150;
                            }
                        }
                    }

                    this.state.setUnsynched();
                }
            }

            if (!this.state.getServerExecutionJobs().isEmpty()) {
                synchronized(this.state.getServerExecutionJobs()) {
                    while(!this.state.getServerExecutionJobs().isEmpty()) {
                        ((ServerExecutionJob)this.state.getServerExecutionJobs().dequeue()).execute(this.state);
                    }
                }
            }

            if (!this.state.pathFindingCallbacks.isEmpty()) {
                synchronized(this.state.pathFindingCallbacks) {
                    while(!this.state.pathFindingCallbacks.isEmpty()) {
                        ((AbstractPathFindingHandler)this.state.pathFindingCallbacks.dequeue()).handleReturn();
                    }

                    this.state.pathFindingCallbacks.notifyAll();
                }
            }

            if (!this.state.creatureSpawns.isEmpty()) {
                synchronized(this.state.creatureSpawns) {
                    while(!this.state.creatureSpawns.isEmpty()) {
                        ((CreatureSpawn)this.state.creatureSpawns.dequeue()).execute(this.state);
                    }
                }
            }

            if (!this.getSystemInQueue().isEmpty()) {
                synchronized(this.getSystemInQueue()) {
                    while(!this.getSystemInQueue().isEmpty()) {
                        this.handleSystemIn((String)this.getSystemInQueue().dequeue());
                    }
                }
            }

            if (!this.state.getAdminCommands().isEmpty()) {
                ArrayList var143 = new ArrayList(this.state.getAdminCommands().size());
                synchronized(this.state.getAdminCommands()) {
                    var143.addAll(this.state.getAdminCommands());
                    this.state.getAdminCommands().clear();
                }

                while(!var143.isEmpty()) {
                    ((AdminCommandQueueElement)var143.remove(0)).execute(this.state);
                }
            }

            if (this.state.toLoadSectorsQueue != null && !this.state.toLoadSectorsQueue.isEmpty()) {
                for(int var144 = 0; var144 < 8 && !this.state.toLoadSectorsQueue.isEmpty(); ++var144) {
                    this.state.getUniverse().getSector((Vector3i)this.state.toLoadSectorsQueue.dequeue());
                }
            }

            int var11;
            Iterator var12;
            Sendable var13;
            long var145;
            long var152;
            if (!this.state.getEntityRequests().isEmpty()) {
                synchronized(this.state) {
                    this.state.setSynched();
                    var145 = System.currentTimeMillis();
                    synchronized(this.state.getEntityRequests()) {
                        while(!this.state.getEntityRequests().isEmpty()) {
                            try {
                                EntityRequest var7 = (EntityRequest)this.state.getEntityRequests().remove(0);
                                System.err.println("[SERVER] HANDLING Entity REQUEST: " + var7.getEntityClass().getSimpleName());
                                PlayerState var8;
                                if (var7.getEntityClass() == Ship.class) {
                                    var8 = this.state.getPlayerFromStateId(var7.getClientId());

                                    try {
                                        Ship var161 = var7.getShip(this.state, true);
                                        if (var8.getInventory((Vector3i)null).existsInInventory((short)1)) {
                                            this.getSynchController().addNewSynchronizedObjectQueued(var161);
                                            var8.getInventory((Vector3i)null).incExistingAndSend((short)1, -1, var8.getNetworkObject());

                                            LogUtil.log().fine("[SPAWN] " + var8.getName() + " spawned new ship: \"" + var161.getRealName() + "\"");
                                            //INSERTED CODE
                                            EntitySpawnEvent event = new EntitySpawnEvent(var161.getRemoteSector().getServerSector().pos, var161);
                                            StarLoader.fireEvent(EntitySpawnEvent.class, event, true);
                                            ///
                                        }
                                    } catch (EntityAlreadyExistsException var109) {
                                        var109.printStackTrace();
                                        ((RegisteredClientOnServer)this.getServerState().getClients().get(var8.getClientId())).serverMessage("[ERROR] An Entity with that name already exists");
                                    } catch (EntityNotFountException var110) {
                                        var110.printStackTrace();
                                        ((RegisteredClientOnServer)this.getServerState().getClients().get(var8.getClientId())).serverMessage("[ERROR] Entity not found");
                                    }
                                } else if (var7.getEntityClass() == Vehicle.class) {
                                    var8 = this.state.getPlayerFromStateId(var7.getClientId());

                                    try {
                                        Vehicle var9 = var7.getVehicle(this.state);
                                        this.getSynchController().addNewSynchronizedObjectQueued(var9);
                                        //INSERTED CODE
                                        EntitySpawnEvent event = new EntitySpawnEvent(var9.getRemoteSector().getServerSector().pos, var9);
                                        StarLoader.fireEvent(EntitySpawnEvent.class, event, true);
                                        ///
                                    } catch (EntityAlreadyExistsException var108) {
                                        var108.printStackTrace();
                                        ((RegisteredClientOnServer)this.getServerState().getClients().get(var8.getClientId())).serverMessage("[ERROR] An Entity with that name already exists");
                                    }
                                } else {
                                    if (var7.getEntityClass() != SpaceStation.class) {
                                        throw new IllegalArgumentException("Unknown class to spawn: " + var7.getEntityClass());
                                    }

                                    var8 = this.state.getPlayerFromStateId(var7.getClientId());

                                    try {
                                        if (var8.getCredits() < this.getState().getGameState().getStationCost()) {
                                            var8.sendServerMessage(new ServerMessage(new Object[]{459, StringTools.formatSeperated(var8.getCredits()), StringTools.formatSeperated(this.getState().getGameState().getStationCost())}, 3, var8.getId()));
                                        } else {
                                            int var10 = (Integer)ServerConfig.ALLOWED_STATIONS_PER_SECTOR.getCurrentState();
                                            var11 = 0;
                                            var12 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                                            while(var12.hasNext()) {
                                                if ((var13 = (Sendable)var12.next()) instanceof SpaceStation && ((SpaceStation)var13).getSectorId() == var8.getCurrentSectorId()) {
                                                    ++var11;
                                                    break;
                                                }
                                            }

                                            if (var11 < var10 && !var8.isInPersonalSector() && !var8.isInTestSector() && !var8.isInTutorial()) {
                                                SpaceStation var181 = var7.getSpaceStation(this.state, true);
                                                var8.modCreditsServer((long)(-this.getState().getGameState().getStationCost()));
                                                this.getSynchController().addNewSynchronizedObjectQueued(var181);
                                                LogUtil.log().fine("[SPAWN] " + var8.getName() + " spawned new station: \"" + var181.getRealName() + "\"");
                                                //INSERTED CODE
                                                EntitySpawnEvent event = new EntitySpawnEvent(var181.getRemoteSector().getServerSector().pos, var181);
                                                StarLoader.fireEvent(EntitySpawnEvent.class, event, true);
                                                ///
                                            } else if (var11 >= var10) {
                                                var8.sendServerMessagePlayerError(new Object[]{460, var10});
                                            } else {
                                                var8.sendServerMessagePlayerError(new Object[]{461});
                                            }
                                        }
                                    } catch (EntityAlreadyExistsException var124) {
                                        var124.printStackTrace();
                                        ((RegisteredClientOnServer)this.getServerState().getClients().get(var8.getClientId())).serverMessage("[ERROR] An Entity with that name already exists");
                                    }
                                }
                            } catch (PlayerNotFountException var125) {
                                var125.printStackTrace();
                            } catch (NoSlotFreeException var126) {
                                var126.printStackTrace();
                            }
                        }
                    }

                    if ((var152 = System.currentTimeMillis() - var145) > 30L) {
                        System.err.println("[SERVER][UPDATE] WARNING: entitiesRequest update took " + var152);
                    }

                    this.state.setUnsynched();
                }
            }

            Iterator var153;
            long var154;
            int var155;
            if (!this.toRemoveClients.isEmpty()) {
                var3 = System.currentTimeMillis();
                synchronized(this.toRemoveClients) {
                    var153 = this.toRemoveClients.iterator();

                    while(true) {
                        if (!var153.hasNext()) {
                            this.toRemoveClients.clear();
                            break;
                        }

                        var155 = (Integer)var153.next();
                        System.err.println("[SERVER] logging out client " + var155);
                        synchronized(this.state.getClients()) {
                            RegisteredClientOnServer var163;
                            if ((var163 = (RegisteredClientOnServer)this.state.getClients().remove(var155)) == null) {
                                System.err.println("[SERVER][WARNING] Exception cound NOT remove client with ID " + var155 + ": " + this.state.getClients());
                            } else {
                                System.err.println("[SERVER] successfully removed client with ID " + var155);
                                var163.getProcessor().disconnect();
                                this.onLoggedout(var163);
                            }
                        }

                        this.setChanged();
                        this.notifyObservers();
                    }
                }

                if ((var154 = System.currentTimeMillis() - var3) > 30L) {
                    System.err.println("[SERVER][UPDATE] WARNING: handleRemoveClients update took " + var154);
                }
            }

            this.getClientCopy();
            Iterator var146 = this.clientCopy.iterator();

            while(var146.hasNext()) {
                RegisteredClientOnServer var147;
                (var147 = (RegisteredClientOnServer)var146.next()).getSynchController().handleQueuedSynchronizedObjects();
                if (!var147.checkConnection()) {
                    System.err.println("[SERVER][WARNING] #### client not connected anymore: removing " + var147.getPlayerName() + "; ID: " + var147.getId() + "; IP: " + var147.getIp());
                    this.toRemoveClients.add(var147.getId());
                }
            }

            this.sendMessages(this.clientCopy);
            synchronized(this.state) {
                this.state.setSynched();
                this.getState().getChannelRouter().update(var1);
                this.state.handleAddedAndRemovedObjects();
                this.state.handleNeedNotifyObjects();
                this.checkExplosionOrders();
                this.mineController.updateLocal(var1);
                this.state.getSimulationManager().update(var1);
                this.state.getFleetManager().update(var1);
                if (!this.state.getBluePrintsToSpawn().isEmpty()) {
                    var145 = System.currentTimeMillis();
                    synchronized(this.state.getBluePrintsToSpawn()) {
                        while(!this.state.getBluePrintsToSpawn().isEmpty()) {
                            final SegmentControllerOutline outline = (SegmentControllerOutline)this.state.getBluePrintsToSpawn().remove(0);

                            try {
                                outline.checkOkName();
                                String var159 = "[BLUEPRINT][LOAD] " + outline.playerUID + " loaded " + outline.en.getName() + " as \"" + outline.realName + "\" in " + outline.spawnSectorId + " as faction " + outline.getFactionId();
                                LogUtil.log().fine(var159);
                                System.err.println(var159);
                                SegmentController spawn = outline.spawn(outline.spawnSectorId, outline.checkProspectedBlockCount, new ChildStats(false), new SegmentControllerSpawnCallbackDirect(this.state, outline.spawnSectorId) {
                                    public void onNoDocker() {
                                        PlayerState var1;
                                        if ((var1 = GameServerController.this.state.getPlayerFromNameIgnoreCaseWOException(outline.playerUID)) != null) {
                                            var1.sendServerMessagePlayerError(new Object[]{462});
                                            System.err.println("[BLUEPRINT] no docker blocks on blueprint " + outline);
                                        } else {
                                            System.err.println("[BLUEPRINT] no docker blocks on blueprint (not a player spawner " + outline.playerUID + ") " + outline);
                                        }
                                    }
                                });
                                //INSERTED CODE @1234
                                EntitySpawnEvent event = new EntitySpawnEvent(outline.spawnSectorId, spawn);
                                StarLoader.fireEvent(EntitySpawnEvent.class, event, true);
                                ///
                            } catch (EntityAlreadyExistsException var105) {
                                var105.printStackTrace();

                                try {
                                    PlayerState var166;
                                    (var166 = this.getState().getPlayerFromName(outline.playerUID)).sendServerMessage(new ServerMessage(new Object[]{463}, 3, var166.getId()));
                                } catch (PlayerNotFountException var104) {
                                    var104.printStackTrace();
                                }
                            } catch (Exception var106) {
                                var106.printStackTrace();
                            }
                        }
                    }

                    if ((var152 = System.currentTimeMillis() - var145) > 30L) {
                        System.err.println("[SERVER][UPDATE] WARNING: blueprintstoSpawn update took " + var152);
                    }
                }

                if (!this.state.getSegmentRequestsLoaded().isEmpty()) {
                    for(var150 = 0; !this.state.getSegmentRequestsLoaded().isEmpty() && var150 < 1000; ++var150) {
                        synchronized(this.state.getSegmentRequestsLoaded()) {
                            ServerSegmentRequest var157 = (ServerSegmentRequest)this.state.getSegmentRequestsLoaded().dequeue();

                            try {
                                this.handleLoadedRequest(var157);
                            } catch (ArrayIndexOutOfBoundsException var102) {
                                var102.printStackTrace();
                                this.state.getSegmentRequestsLoaded().enqueue(var157);
                            }
                        }
                    }
                }

                this.getSynchController().handleQueuedSynchronizedObjects();
                this.state.handleLoginReuests();
                this.state.getGameMapProvider().updateServer();
                if (!this.state.getScheduledUpdates().isEmpty()) {
                    synchronized(this.state.getScheduledUpdates()) {
                        while(!this.state.getScheduledUpdates().isEmpty()) {
                            ((Sendable)this.state.getScheduledUpdates().remove(0)).updateLocal(var1);
                        }
                    }
                }

                boolean var149 = false;
                var154 = System.currentTimeMillis();

                for(var155 = 0; var155 < this.getServerState().getSectorSwitches().size(); ++var155) {
                    SectorSwitch var162;
                    if ((var162 = (SectorSwitch)this.getServerState().getSectorSwitches().get(var155)).delay <= 0L || this.getState().getUpdateTime() > var162.delay) {
                        var162.execute(this.getServerState());
                        this.getServerState().getSectorSwitches().remove(var155);
                        --var155;
                        var149 = true;
                    }
                }

                long var164;
                if ((var164 = System.currentTimeMillis() - var154) > 30L) {
                    System.err.println("[SERVER][UPDATE] WARNING: sectorSwitchExecutions update took " + var164);
                }

                PlayerState var167;
                if (!this.state.getSpawnRequestsReady().isEmpty()) {
                    synchronized(this.state.getSpawnRequestsReady()) {
                        var153 = this.state.getSpawnRequestsReady().iterator();

                        while(var153.hasNext()) {
                            var167 = (PlayerState)var153.next();
                            this.spawnPlayerCharacter(var167);
                        }

                        this.state.getSpawnRequestsReady().clear();
                    }
                }

                if (!this.state.getSpawnRequests().isEmpty()) {
                    synchronized(this.state.getSpawnRequests()) {
                        var153 = this.state.getSpawnRequests().iterator();

                        while(var153.hasNext()) {
                            var167 = (PlayerState)var153.next();
                            this.spawnPlayerCharacterPrepare(var167);
                        }

                        this.state.getSpawnRequests().clear();
                    }
                }

                var154 = System.currentTimeMillis();
                this.getServerState().getUniverse().update(var1, this.sectorLag);
                this.sectorLag.clear();
                if ((var164 = System.currentTimeMillis() - var154) > 30L) {
                    System.err.println("[SERVER][UPDATE] WARNING: UNIVERSE update took " + var164);
                }

                synchronized(this.getServerState().getLocalAndRemoteObjectContainer().getLocalObjects()) {
                    this.getState().getMetaObjectManager().updateLocal(var1);
                    this.executeWaves();
                    this.getMissileController().updateServer(var1);
                    Iterator var165;
                    if (this.getServerState().isFactionReinstitudeFlag()) {
                        var153 = this.getServerState().getFactionManager().getFactionCollection().iterator();

                        while(var153.hasNext()) {
                            Faction var173;
                            var165 = (var173 = (Faction)var153.next()).getMembersUID().values().iterator();

                            while(var165.hasNext()) {
                                FactionPermission var169 = (FactionPermission)var165.next();

                                try {
                                    PlayerState var170;
                                    if ((var170 = this.getServerState().getPlayerFromName(var169.playerUID)) != null) {
                                        var170.getFactionController().setFactionId(var173.getIdFaction());
                                    }
                                } catch (PlayerNotFountException var101) {
                                    var101.printStackTrace();
                                }
                            }
                        }

                        this.getServerState().setFactionReinstitudeFlag(false);
                    }

                    var152 = System.currentTimeMillis();
                    ObjectCollection var168 = this.getServerState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values();
                    this.getState().activeSectors.clear();
                    Iterator var171 = var168.iterator();

                    label2225:
                    while(true) {
                        if (!var171.hasNext()) {
                            IntArrayList var174 = new IntArrayList();
                            Iterator var175 = this.getState().activeSectors.iterator();

                            while(true) {
                                while(var175.hasNext()) {
                                    var11 = (Integer)var175.next();
                                    Sendable var184;
                                    if ((var184 = (Sendable)this.getState().getLocalAndRemoteObjectContainer().getLocalObjects().get(var11)) != null && var184 instanceof RemoteSector) {
                                        var184.updateLocal(var1);
                                    } else {
                                        System.err.println("[SERVER] Exception: active sector " + var11 + " could not be updated. not loaded: " + var184 + "; Removing sector id from active sector set...");
                                        var174.add(var11);
                                    }
                                }

                                long var176;
                                if ((var176 = System.currentTimeMillis() - var152) > 50L) {
                                    System.err.println("[SERVER][UPDATE] WARNING: object local update of " + var168.size() + " objects took " + var176);
                                }

                                this.getState().activeSectors.removeAll(var174);
                                var12 = var168.iterator();

                                while(var12.hasNext()) {
                                    if ((var13 = (Sendable)var12.next()) instanceof SimpleTransformableSendableObject) {
                                        ((SimpleTransformableSendableObject)var13).getPhysicsDataContainer().lastTransform.set(((SimpleTransformableSendableObject)var13).getPhysicsDataContainer().thisTransform);
                                        ((SimpleTransformableSendableObject)var13).getPhysicsDataContainer().thisTransform.set(((SimpleTransformableSendableObject)var13).getWorldTransform());
                                    }
                                }

                                if (this.getBlockBehaviorChanged() != null) {
                                    FileExt var160;
                                    if ((var160 = new FileExt("./data/config/blockBehaviorConfig.xml")).exists()) {
                                        FileUtil.backupFile(var160);
                                    }

                                    BufferedOutputStream var177;
                                    (var177 = new BufferedOutputStream(new FileOutputStream("./data/config/blockBehaviorConfig.xml"))).write(this.getBlockBehaviorChanged());
                                    var177.flush();
                                    var177.close();
                                    this.parseBlockBehavior("./data/config/blockBehaviorConfig.xml");
                                    System.err.println("[SERVER] a new block behavior has been received. Applying to all entities, and deligating to all clients");
                                    var165 = this.getState().getLocalAndRemoteObjectContainer().getLocalUpdatableObjects().values().iterator();

                                    while(var165.hasNext()) {
                                        Sendable var178;
                                        if ((var178 = (Sendable)var165.next()) instanceof ManagedSegmentController) {
                                            ((ManagedSegmentController)var178).getManagerContainer().reparseBlockBehavior(true);
                                        }
                                    }

                                    this.getServerState().getGameState().setServerDeployedBlockBehavior(true);
                                    this.setBlockBehaviorChanged((byte[])null);
                                }
                                break label2225;
                            }
                        }

                        Sendable var172 = (Sendable)var171.next();

                        assert var172 != null : var172;

                        if (var172 == null) {
                            throw new NullPointerException();
                        }

                        try {
                            if (var172 instanceof SegmentController) {
                                SegmentController var180;
                                (var180 = (SegmentController)var172).getSegmentBuffer().updateNumber();
                                if (var149) {
                                    var180.getRuleEntityManager().triggerAnyEntitySectorSwitched();
                                    var180.getRuleEntityManager().triggerSectorEntitiesChanged();
                                }
                            }

                            if (!(var172 instanceof SimpleTransformableSendableObject) || this.state.getUniverse().getIsSectorActive(((SimpleTransformableSendableObject)var172).getSectorId())) {
                                assert !(var172 instanceof Physical) || ((Physical)var172).getPhysicsDataContainer().getObject() == null || ((Physical)var172).getPhysicsDataContainer().getObject() instanceof GamePhysicsObject : ((Physical)var172).getPhysicsDataContainer().getObject();

                                long var182 = System.currentTimeMillis();
                                var172.updateLocal(var1);
                                long var186 = System.currentTimeMillis() - var182;
                                if (var172 instanceof SimpleTransformableSendableObject) {
                                    this.sectorLag.add(((SimpleTransformableSendableObject)var172).getSectorId(), var186);
                                }

                                if (var186 > 50L) {
                                    System.err.println("[SERVER][UPDATE] WARNING: object local update of " + var172 + " took " + var186);
                                    var172.announceLag(var186);
                                }
                            }
                        } catch (Exception var120) {
                            var120.printStackTrace();
                            if (this.exceptionSet.contains(var120.getClass())) {
                                int var183 = 0;
                                String var185 = "./logs/exceptionlog0.txt";

                                while((new FileExt(var185)).exists()) {
                                    ++var183;
                                    var185 = "./logs/exceptionlog" + var183 + ".txt";
                                    System.err.println("Finding exception file " + var185);
                                }

                                FileUtil.copyFile(new FileExt("./logs/log.txt.0"), new FileExt(var185));
                            }

                            System.err.println("[SERVER] Exception catched ");
                            if (System.currentTimeMillis() - this.lastExceptionTime > 5000L || !this.lastMessage.equals(var120.getClass().getSimpleName())) {
                                this.broadcastMessage(new Object[]{464, var120.getClass().getSimpleName()}, 3);
                                this.lastExceptionTime = System.currentTimeMillis();
                                this.lastMessage = var120.getClass().getSimpleName();
                            }
                        }
                    }
                }

                if (this.state.getGameState().getGameModes().size() <= 0) {
                    this.state.getGameState().getNetworkObject().gameModeMessage.set("");
                } else {
                    String var158 = "";
                    var153 = this.state.getGameState().getGameModes().iterator();

                    while(true) {
                        if (!var153.hasNext()) {
                            this.state.getGameState().getNetworkObject().gameModeMessage.set(var158);
                            break;
                        }

                        AbstractGameMode var179 = (AbstractGameMode)var153.next();

                        try {
                            var179.update(var1);
                            var158 = var158 + var179.getCurrentOutput() + "\n";
                        } catch (GameModeException var100) {
                            throw new RuntimeException(var100);
                        }
                    }
                }

                this.getState().getGameState().updateToNetworkObject();
                this.state.setUnsynched();
            }

            if (!this.state.getCreatorHooks().isEmpty()) {
                synchronized(this.state) {
                    this.state.setSynched();
                    synchronized(this.state.getCreatorHooks()) {
                        var150 = 0;

                        while(true) {
                            if (var150 >= this.state.getCreatorHooks().size()) {
                                this.state.getCreatorHooks().clear();
                                break;
                            }

                            ((av)this.state.getCreatorHooks().get(var150)).a();
                            ++var150;
                        }
                    }

                    this.state.setUnsynched();
                }
            }

            this.synchronize(this.clientCopy);
            BlockBulkSerialization.freeUsedServerPool();
            this.lastSynchronize = System.currentTimeMillis();
            GameServerState.updateAllShopPricesFlag = false;


        }
        //INSERTED CODE @1437
        StarRunnable.tickAll();
        ///
    }
    long lastTime = 0;

    private boolean authSession(String var1, SessionCallback var2, boolean var3, boolean var4, boolean var5) throws AuthenticationRequiredException {
        return var2.authorize(var1, this, var3, var4, var5);
    }

    public long getServerRunningTime() {
        long var1 = this.state.getServerStartTime();
        return System.currentTimeMillis() - var1;
    }

    public long calculateStartTime() {
        return (Integer)ServerConfig.UNIVERSE_DAY_IN_MS.getCurrentState() == -1 ? -1L : this.state.getServerStartTime() - (long)(Integer)ServerConfig.UNIVERSE_DAY_IN_MS.getCurrentState() + this.state.getServerTimeMod() - (long)this.getServerState().getServerTimeDifference();
    }

    public long getUniverseDayInMs() {
        return (long)(Integer)ServerConfig.UNIVERSE_DAY_IN_MS.getCurrentState();
    }

    public void onRemoveEntity(Sendable var1) {
    }

    private boolean clearWorld() {
        System.err.println("SERVER CLEARING WORLD");
        boolean var1 = true;
        this.purgeDB();
        synchronized(this.state.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var3 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

            while(var3.hasNext()) {
                Sendable var4;
                if ((var4 = (Sendable)var3.next()) instanceof PlayerState) {
                    ((PlayerState)var4).handleServerHealthAndCheckAliveOnServer(0.0F, (PlayerState)var4);
                }

                if (var4 instanceof SegmentController) {
                    var1 = false;
                    var4.setMarkedForDeleteVolatile(true);
                }
            }

            return var1;
        }
    }

    private void createAdminFile() throws IOException {
        FileExt var1;
        if (!(var1 = new FileExt("./admins.txt")).exists()) {
            var1.createNewFile();
        }

    }

    private void createBlacklistFile() throws IOException {
        FileExt var1;
        if (!(var1 = new FileExt("./blacklist.txt")).exists()) {
            var1.createNewFile();
        }

    }

    public void despawn(String var1) throws SQLException {
        System.err.println("[DESPAWN] despawning: " + var1);
        Iterator var2 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

        Sendable var3;
        do {
            if (!var2.hasNext()) {
                String var4 = DatabaseIndex.escape(var1) + "%";
                int var5;
                if ((var5 = this.state.getDatabaseIndex().getTableManager().getEntityTable().despawn(var4, Despawn.ALL, (Vector3i)null, (EntityType)null)) > 0) {
                    System.err.println("[DESPAWN] despawned " + var5 + " from DB using escaped matching string '" + var4 + "'");
                }

                return;
            }
        } while(!((var3 = (Sendable)var2.next()) instanceof SegmentController) || !DatabaseEntry.removePrefixWOException(((SegmentController)var3).getUniqueIdentifier()).equals(var1));

        var3.markForPermanentDelete(true);
        var3.setMarkedForDeleteVolatile(true);
        System.err.println("[DESPAWN] despawning loaded: " + var1);
    }

    private void createProtectedUserFile() throws IOException {
        FileExt var1;
        if (!(var1 = new FileExt("./protected.txt")).exists()) {
            var1.createNewFile();
        }

    }

    public void createThreadDump() {
        try {
            CrashReporter.createThreadDump("threadDumpFrozen");
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }

    protected void displayError(Exception var1) {
        if (Starter.sGUI != null) {
            GuiErrorHandler.processErrorDialogException(var1);
        }

    }

    public GameServerState getServerState() {
        return (GameServerState)super.getServerState();
    }

    public void initializeServerState() throws IOException, SQLException, NoSuchAlgorithmException, ResourceException, ParseException, SAXException, ParserConfigurationException {
        this.createVersionFile();
        this.parseBlockBehavior("./data/config/blockBehaviorConfig.xml");
        this.state.setConfigCheckSum(FileUtil.getSha1Checksum("./data/config/BlockConfig.xml"));
        this.state.setFactionConfigCheckSum(FileUtil.getSha1Checksum("./data/config/FactionConfig.xml"));
        this.state.setConfigPropertiesCheckSum(FileUtil.getSha1Checksum("./data/config/BlockTypes.properties"));
        this.state.setCustomTexturesChecksum(FileUtil.createFilesHashRecursively(GameResourceLoader.CUSTOM_TEXTURE_PATH, new FileFilter() {
            public boolean accept(File var1) {
                return var1.isDirectory() || var1.getName().toLowerCase(Locale.ENGLISH).endsWith(".png");
            }
        }));
        RandomAccessFile var1;
        byte[] var2 = new byte[(int)(var1 = new RandomAccessFile(GameResourceLoader.CUSTOM_TEXTURE_PATH + "pack.zip", "r")).length()];
        var1.read(var2);
        var1.close();
        this.state.setCustomTexturesFile(var2);
        ObjectArrayList var4;
        ResourceLoader.loadModelConfig(var4 = new ObjectArrayList());
        this.state.setResourceMap(new ResourceMap());
        this.state.getResourceMap().initForServer(var4);
        var2 = new byte[(int)(var1 = new RandomAccessFile("./data/config/FactionConfig.xml", "r")).length()];
        var1.read(var2);
        var1.close();
        this.state.setFactionConfigFile(var2);
        var2 = new byte[(int)(var1 = new RandomAccessFile("./data/config/BlockConfig.xml", "r")).length()];
        var1.read(var2);
        var1.close();
        this.state.setBlockConfigFile(var2);
        var2 = new byte[(int)(var1 = new RandomAccessFile("./data/config/BlockTypes.properties", "r")).length()];
        var1.read(var2);
        var1.close();
        this.state.setBlockPropertiesFile(var2);

        try {
            this.addAdminsFromDisk();
            this.addBlacklistFromDisk();
            this.addWhitelistFromDisk();
            this.addProtectedUsersFromDisk();
        } catch (IOException var3) {
            this.createAdminFile();
            this.createBlacklistFile();
            this.createWhitelistFile();
            this.createProtectedUserFile();
        }

        ChatSystem var5;
        (var5 = new ChatSystem(this.state)).setId(this.state.getNextFreeObjectId());
        var5.initialize();
        this.state.setChat(var5);
        this.getSynchController().addNewSynchronizedObjectQueued(var5);
        this.state.setGameState(new SendableGameState(this.state));
        this.state.getGameState().setId(this.state.getNextFreeObjectId());
        this.state.getGameState().initialize();
        this.state.getSimulationManager().initialize();
        this.getSynchController().addNewSynchronizedObjectQueued(this.state.getGameState());
        this.loadUniverse();
        if (ServerConfig.BATTLE_MODE.isOn()) {
            this.state.getGameState().getGameModes().add(new BattleMode(this.state));
        }

    }

    public boolean isAdmin(RegisteredClientOnServer var1) {
        return var1 != null && var1.getPlayerObject() != null && (this.state.getAdmins().isEmpty() || this.isAdmin(var1.getPlayerName().trim().toLowerCase(Locale.ENGLISH)));
    }

    public boolean isBanned(RegisteredClientOnServer var1, StringBuffer var2) {
        System.err.println("[SERVER] checking ip ban: " + var1.getIp());
        if (var1.getIp().trim().equals("127.0.0.1")) {
            System.err.println("[SERVER] Local connection detected. No ban");
            return false;
        } else {
            return this.state.getBlackListedNames().containsAndIsValid(var1.getPlayerName().toLowerCase(Locale.ENGLISH), var2) || this.state.getBlackListedIps().containsAndIsValid(var1.getIp(), var2) || var1.getStarmadeName() != null && this.state.getBlackListedAccounts().containsAndIsValid(var1.getStarmadeName(), var2);
        }
    }

    public boolean isWhiteListed(RegisteredClientOnServer var1) {
        if (var1.getIp().trim().equals("127.0.0.1")) {
            return true;
        } else if (!ServerConfig.USE_WHITELIST.isOn()) {
            return true;
        } else {
            System.err.println("[SERVER] checking whitelist: Name: " + var1.getPlayerName() + "; IP: " + var1.getIp() + "; SM-Account: " + var1.getStarmadeName());
            return this.state.getWhiteListedNames().containsAndIsValid(var1.getPlayerName().toLowerCase(Locale.ENGLISH)) || this.state.getWhiteListedIps().containsAndIsValid(var1.getIp()) || var1.getStarmadeName() != null && this.state.getWhiteListedAccounts().containsAndIsValid(var1.getStarmadeName());
        }
    }

    public int onLoggedIn(RegisteredClientOnServer var1) throws Exception {
        PlayerState var2;
        (var2 = new PlayerState(this.state)).setName(var1.getPlayerName());
        var2.setStarmadeName(var1.getStarmadeName());
        var2.setUpgradedAccount(var1.isUpgradedAccount());
        boolean var3 = false;

        int var9;
        while((var9 = this.state.getNextFreeObjectId()) < 0) {
        }

        assert var9 > 0;

        var2.setId(var9);
        var2.setClientId(var1.getId());
        Sector var4 = ((GameServerState)var2.getState()).getUniverse().getSector(new Vector3i((Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_X.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Y.getCurrentState(), (Integer)ServerConfig.DEFAULT_SPAWN_SECTOR_Z.getCurrentState()));
        var2.setCurrentSectorId(var4.getId());
        var2.readDatabase();

        try {
            Tag var11 = this.readEntity(var2.getUniqueIdentifier());
            var2.fromTagStructure(var11);
            var2.setPersonalSectors();
            var2.spawnData.setFromLoadedSpawnPoint();
            System.err.println("[SERVER] player LOADED FROM DISK " + var1.getPlayerName() + ", credits: " + var2.getCredits() + "; Sector: " + var2.getCurrentSector() + "; Invetory slots filled: " + var2.getInventory().getCountFilledSlots() + "; last login: " + var2.getLastLogin() + "; last logout: " + var2.getLastLogout() + "; ");
        } catch (EntityNotFountException var6) {
            var2.setPersonalSectors();
            var2.spawnData.setForNewPlayerOnLogin();
            var2.getInventoryController().fillInventory();
        } catch (Exception var7) {
            Exception var10 = var7;
            var7.printStackTrace();

            try {
                GuiErrorHandler.processErrorDialogException(new Exception("Sorry :(, the system failed to load player " + var1 + ". Possible data corruption detected.\nYou may need to remove your player state from the StarMade/server-database directory, or reset the universe", var10));
            } catch (Exception var5) {
            }

            throw var7;
        }

        var2.updateDatabase();
        var2.initialize();
        System.err.println("[SERVER] adding player to synch queue (ID: " + var9 + ") (SECTOR: " + var2.getCurrentSector() + " [" + var2.getCurrentSectorId() + "])");
        this.getSynchController().addNewSynchronizedObjectQueued(var2);
        var1.setPlayerObject(var2);
        Iterator var12 = this.getState().getFactionManager().getFactionCollection().iterator();

        while(var12.hasNext()) {
            Faction var8;
            if ((var8 = (Faction)var12.next()).isNPC()) {
                ((NPCFaction)var8).getDiplomacy().onPlayerJoined(var2);
            }
        }

        Starter.modManager.onPlayerCreated(var2);

        assert var2.getCurrentSectorId() != 0;

        return LoginCode.SUCCESS_LOGGED_IN.code;
    }

    public void onLoggedout(RegisteredClientOnServer var1) {
        if (debugLogoutOnShutdown || !this.flaggedShutdown) {
            System.err.println("[SERVER] onLoggedOut starting for " + var1);
            LogUtil.log().fine("[LOGOUT] logging out client ID " + var1);
            if (var1 != null) {
                synchronized(this.state.getLocalAndRemoteObjectContainer().getLocalObjects()) {
                    Iterator var3 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

                    label52:
                    while(true) {
                        while(true) {
                            if (!var3.hasNext()) {
                                break label52;
                            }

                            Sendable var4;
                            if ((var4 = (Sendable)var3.next()) instanceof PlayerState && ((PlayerState)var4).getClientId() == var1.getId()) {
                                System.err.println("[SERVER][ClientLogout] saving and removing Player " + var4);
                                ((PlayerState)var4).spawnData.onLoggedOut();
                                var4.setMarkedForDeleteVolatile(true);
                                Starter.modManager.onPlayerRemoved((PlayerState)var4);
                                this.state.getChannelRouter().onLogoff((PlayerState)var4);

                                try {
                                    this.writeEntity((PlayerState)var4, true);
                                } catch (IOException var5) {
                                    var5.printStackTrace();
                                } catch (SQLException var6) {
                                    var6.printStackTrace();
                                }
                            } else if (var4 instanceof ChatSystem && ((ChatSystem)var4).getOwnerStateId() == var1.getId()) {
                                System.err.println("[SERVER][ClientLogout] deleting CHATSYSTEM " + var4);
                                var4.setMarkedForDeleteVolatile(true);
                            } else if (var4 instanceof PlayerCharacter && ((PlayerCharacter)var4).getClientOwnerId() == var1.getId()) {
                                System.err.println("[SERVER][ClientLogout] deleting PLAYERCHARACTER " + var4);
                                var4.setMarkedForDeleteVolatile(true);
                            }
                        }
                    }
                }
            } else {
                System.err.println("Skipping logout procedure for null client");
            }

            System.err.println("[SERVER] onLoggedOut DONE for " + var1);
        }
    }

    protected void onShutDown(boolean var1) throws IOException {
        Iterator var2;
        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down element collection thread");
            System.out.println("[SERVER][SHUTDOWN] shutting down element collection thread");
            this.elementCollectionCalculationThreadManager.onStop();
        } catch (Exception var17) {
            var2 = null;
            var17.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down pathfinding threads");
            System.out.println("[SERVER][SHUTDOWN] shutting down pathfinding threads");
            this.segmentBreaker.shutdown();
            this.segmentPathFinder.shutdown();
            this.segmentPathGroundFinder.shutdown();
        } catch (Exception var16) {
            var2 = null;
            var16.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down universe");
            System.out.println("[SERVER][SHUTDOWN] shutting down universe");
            this.getServerState().getUniverse().onShutdown();
        } catch (Exception var15) {
            var2 = null;
            var15.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down segment request thread");
            System.out.println("[SERVER][SHUTDOWN] shutting segment request thread");
            this.serverSegmentRequestThread.shutdown();
        } catch (Exception var14) {
            var2 = null;
            var14.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down simulation");
            System.out.println("[SERVER][SHUTDOWN] shutting down simulation");
            this.getState().getSimulationManager().shutdown();
        } catch (Exception var13) {
            var2 = null;
            var13.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down active checker");
            System.out.println("[SERVER][SHUTDOWN] shutting down active checker");
            this.serverActiveChecker.shutdown();
        } catch (Exception var12) {
            var2 = null;
            var12.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting sysin listener");
            System.out.println("[SERVER][SHUTDOWN] shutting down sysin listener");
            systemInListener.setState((ServerState)null);
        } catch (Exception var11) {
            var2 = null;
            var11.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down mob thread");
            System.out.println("[SERVER][SHUTDOWN] shutting down mob thread");
            this.state.getMobSpawnThread().shutdown();
        } catch (Exception var10) {
            var2 = null;
            var10.printStackTrace();
        }

        try {
            LogUtil.log().fine("[SHUTDOWN] shutting down game map provider");
            System.out.println("[SERVER][SHUTDOWN] shutting down game map provider");
            this.state.getGameMapProvider().shutdown();
        } catch (Exception var9) {
            var2 = null;
            var9.printStackTrace();
        }

        LogUtil.log().fine("[SHUTDOWN] server stop listening");
        System.out.println("[SERVER][SHUTDOWN] Stopping to listen!");

        try {
            this.stopListening();
        } catch (Exception var8) {
            var2 = null;
            var8.printStackTrace();
        }

        LogUtil.log().fine("[SHUTDOWN] disconnecting all clients");
        System.out.println("[SERVER][SHUTDOWN] disconnecting all clients!");

        try {
            var2 = this.getServerState().getClients().values().iterator();

            while(var2.hasNext()) {
                RegisteredClientOnServer var3 = (RegisteredClientOnServer)var2.next();

                try {
                    var3.getProcessor().disconnect();
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            }
        } catch (Exception var19) {
            var2 = null;
            var19.printStackTrace();
        }

        LogUtil.log().fine("[SHUTDOWN] writing current universe STARTED");
        System.out.println("[SERVER][SHUTDOWN] Dumping server State!");

        try {
            this.writeEntitiesToDatabaseAtAutosaveOrShutdown(true, false, this.getServerState().getUniverse());
        } catch (IOException var5) {
            var5.printStackTrace();
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        try {
            int var20 = 0;

            while(this.getState().getThreadQueue().getActiveCount() > 0) {
                System.out.println("[SERVER][SHUTDOWN] waiting for " + this.getState().getThreadQueue().getActiveCount() + " queued writing jobs to finish");

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }

                ++var20;
                if (var20 * 10 > 300) {
                    System.err.println("Exception: WAITED LONGER THAN 5 minutes for final write. Something went wrong!");
                    break;
                }
            }

            System.out.println("[SERVER][SHUTDOWN] all queued writing jobs finished. closing database");
            this.getState().getDatabaseIndex().commit();
            if (var1) {
                System.out.println("[SERVER][SHUTDOWN] Emergency shudown: cannot close database is an ordered mannor (threads might be interrupted unexpected)");
                System.out.println("[SERVER][SHUTDOWN] Emergency shudown: leaving database to recover on next server start");
            } else {
                this.getState().getDatabaseIndex().destroy();
            }

            LogUtil.log().fine("[SHUTDOWN] database closed successfully");
            System.out.println("[SERVER][SHUTDOWN] database closed successfully");
        } catch (SQLException var18) {
            var2 = null;
            var18.printStackTrace();
        }

        LogUtil.log().fine("[SHUTDOWN] writing current universe FINISHED");
        System.out.println("[SERVER][SHUTDOWN] ServerState saved!");
        if (this.state.getThreadedSegmentWriter() != null) {
            this.state.getThreadedSegmentWriter().shutdown();
        }

        this.state.getGameState().onStop();
        this.state.getThreadQueue().shutdown();
        IOFileManager.cleanUp(true);
        ServerState.setCreated(false);
        System.out.println("[SERVER][SHUTDOWN] ServerState.created set to " + ServerState.isCreated());
    }

    public boolean isUserProtectionAuthenticated(String var1, String var2) {
        ProtectedUplinkName var3 = (ProtectedUplinkName)this.state.getProtectedUsers().get(var1.trim().toLowerCase(Locale.ENGLISH));
        System.err.println("[AUTH] Protection status of " + var1 + " is " + var3 + " -> protected = " + (var3 != null && var3.equals(var2)));
        LogUtil.log().fine("[AUTH] Protection status of " + var1 + " is " + var3 + " -> protected = " + (var3 != null && var3.equals(var2)));
        return var3 != null && var3.equals(var2);
    }

    private void createVersionFile() {
        FileExt var1;
        if ((var1 = new FileExt("lpversion")).exists()) {
            var1.delete();
        }

        try {
            BufferedWriter var3;
            (var3 = new BufferedWriter(new FileWriter(var1))).append(Version.VERSION + ";" + Version.build);
            var3.flush();
            var3.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    private void createWhitelistFile() throws IOException {
        FileExt var1;
        if (!(var1 = new FileExt("./whitelist.txt")).exists()) {
            var1.createNewFile();
        }

    }

    public void endRound(int var1, int var2, Damager var3) {
        Iterator var4 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

        while(var4.hasNext()) {
            Sendable var5;
            if ((var5 = (Sendable)var4.next()) instanceof PlayerState) {
                ((PlayerState)var5).notifyRoundEnded(var1, var2, var3);
            }
        }

        this.endRoundMode = System.currentTimeMillis();
    }

    public void enqueueAdminCommand(RegisteredClientInterface var1, AdminCommands var2, Object[] var3) {
        synchronized(this.state.getAdminCommands()) {
            this.state.getAdminCommands().add(new AdminCommandQueueElement(var1, var2, var3));
        }
    }

    public <E extends ElementCollection<E, EC, EM>, EC extends ElementCollectionManager<E, EC, EM>, EM extends UsableElementManager<E, EC, EM>> void enqueueElementCollectionUpdate(ElementCollectionManager<E, EC, EM> var1) {
        if (this.state.getLocalAndRemoteObjectContainer().getLocalObjects().containsKey(var1.getSegmentController().getId())) {
            this.elementCollectionCalculationThreadManager.enqueue(new ElementCollectionCalculationThreadExecution(var1));
        } else {
            System.err.println("[SERVER] Exception tried to schedule update for nonexisting controller: " + var1);
        }
    }

    public void executeWave(ShipSpawnWave var1) throws Exception {
        if (var1.getPrintsToSpawn() != null && !var1.getPrintsToSpawn().isEmpty()) {
            Transform var2;
            (var2 = new Transform()).setIdentity();
            Vector3f var3;
            if ((var3 = new Vector3f((float)Math.random() - 0.5F, (float)Math.random() - 0.5F, (float)Math.random() - 0.5F)).length() == 0.0F) {
                var3.x = 1.0F;
            }

            var3.normalize();
            var3.scale((float)(100.0D + Math.random() * 50.0D));
            var2.origin.add(var3);
            Iterator var5 = var1.getPrintsToSpawn().iterator();

            while(var5.hasNext()) {
                CatalogPermission var4 = (CatalogPermission)var5.next();
                this.state.spawnMobs(1, var4.getUid(), var1.sectorId, var2, var1.getWaveTeam(), var1.bluePrintController);
            }

        } else {
            System.err.println("[SERVER][WAVE] not spawning empty list: " + var1.getPrintsToSpawn());
        }
    }

    private void executeWaves() {
        if (!this.state.waves.isEmpty()) {
            for(int var1 = 0; var1 < this.state.waves.size(); ++var1) {
                ShipSpawnWave var2 = (ShipSpawnWave)this.state.waves.get(var1);
                if (System.currentTimeMillis() - var2.getTimeInitiatedInMS() > (long)(var2.getTimeInSecs() * 1000)) {
                    try {
                        if (this.getState().getUniverse().isSectorActive(var2.sectorId)) {
                            this.executeWave(var2);
                        } else {
                            System.err.println("[SERVER][WAVE] WARNING: Could not load wave. sector not loaded " + var2.sectorId);
                        }
                    } catch (EntityNotFountException var3) {
                        var3.printStackTrace();
                    } catch (IOException var4) {
                        var4.printStackTrace();
                    } catch (EntityAlreadyExistsException var5) {
                        var5.printStackTrace();
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }

                    this.state.waves.remove(var1);
                    --var1;
                } else {
                    var2.getTimeInSecs();
                    System.currentTimeMillis();
                    var2.getTimeInitiatedInMS();
                }
            }
        }

    }

    private void getClientCopy() {
        while(true) {
            try {
                this.clientCopy.clear();
                this.clientCopy.addAll(this.state.getClients().values());
                return;
            } catch (ConcurrentModificationException var1) {
                var1.printStackTrace();
            }
        }
    }

    public CreatorThreadController getCreatorThreadController() {
        return this.creatorThreadController;
    }

    public MissileController getMissileController() {
        return this.missileController;
    }

    public MineController getMineController() {
        return this.mineController;
    }

    public GameServerState getState() {
        return this.state;
    }

    public SynchronizationContainerController getSynchController() {
        return this.synchController;
    }

    private void handleEndRound() {
        if (this.clearWorld()) {
            this.state.getUniverse().resetAllSectors();
            this.endRoundMode = 0L;
        }

    }

    public void handleEmpty(NetworkSegmentProvider var1, SegmentController var2, Vector3i var3, long var4, long var6) {
        if (var2.getSegmentBuffer().getLastChanged(var3) > var6) {
            var1.signatureEmptyBuffer.add(var4);
        }

    }

    public void handleLoadedRequest(ServerSegmentRequest var1) {
        assert var1.segment != null;

        RemoteSegment var2 = var1.segment;
        ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).addToBufferIfNecessary(var2, var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
        if (!var1.sigatureOfSegmentBuffer) {
            handleSegmentRequest(var1.getNetworkSegmentProvider(), var2, var2.getIndex(), var1.getLocalTimestamp(), var1.getSizeOnClient());
        } else {
            long var3 = SegmentBufferManager.getBufferIndexFromAbsolute(var1.getSegmentPos());
            if (var1.bitMap != null) {
                var1.bitMap = var1.getSegmentController().getSegmentBuffer().applyBitMap(var3, var1.bitMap);
            }

            if (var1.bitMap != null) {
                var1.getSegmentController().getSegmentBuffer().insertFromBitset(var1.getSegmentPos(), var3, var1.bitMap, new SegmentBufferIteratorEmptyInterface() {
                    public boolean handle(Segment var1, long var2) {
                        return false;
                    }

                    public boolean handleEmpty(int var1, int var2, int var3, long var4) {
                        return false;
                    }
                });
            }

            assert !(var1.getSegmentController() instanceof ShopSpaceStation) || var1.bitMap == null;

            synchronized(var1.getNetworkSegmentProvider()) {
                var1.getNetworkSegmentProvider().segmentBufferAwnserBuffer.add(new RemoteBitset(new BitsetResponse(var3, var1.bitMap, new Vector3i(var1.getSegmentPos())), var1.getNetworkSegmentProvider()));
            }
        }
    }

    public void handleSegmentRequest(ServerSegmentRequest var1) throws IOException, InterruptedException, SegmentOutOfBoundsException {
        RemoteSegment var2 = null;
        RequestData var5;
        if (var1.getSegmentController() instanceof Planet && !((Planet)var1.getSegmentController()).isTouched()) {
            ObjectArrayList var3 = new ObjectArrayList(3);
            if (var1.getSegmentPos().y < 96) {
                synchronized(var1.getSegmentController()) {
                    synchronized(this.state) {
                        SegmentRetrieveCallback var6 = new SegmentRetrieveCallback();
                        var1.getSegmentController().getSegmentBuffer().get(var1.getSegmentPos(), var6);
                        if (var6.state == -1) {
                            (var2 = new RemoteSegment(var1.getSegmentController())).setPos(var1.getSegmentPos());
                            var2.setSize(0);
                            var2.setLastChanged(System.currentTimeMillis());
                        } else if (var6.state != -2) {
                            var2 = (RemoteSegment)var6.segment;
                        }
                    }

                    if (var2 == null) {
                        var5 = var1.getSegmentController().getCreatorThread().allocateRequestData(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);

                        RemoteSegment var19;
                        for(int var16 = 0; var16 < 96; var16 += 32) {
                            long var7 = ElementCollection.getIndex(var1.getSegmentPos().x, var16, var1.getSegmentPos().z);
                            SegmentProvider.buildRevalidationIndex(var19 = ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).doRequest(var7, var5), var1.getSegmentController().isStatic(), true);
                            if (var19.getSegmentData() != null) {
                                var19.getSegmentData().setNeedsRevalidate(true);
                            }

                            if (var19.pos.equals(var1.getSegmentPos())) {
                                var2 = var19;
                            } else {
                                var3.add(var19);
                            }
                        }

                        var1.getSegmentController().getCreatorThread().freeRequestData(var5, var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
                        Iterator var17 = var3.iterator();

                        while(var17.hasNext()) {
                            SegmentProvider.buildRevalidationIndex(var19 = (RemoteSegment)var17.next(), var1.getSegmentController().isStatic(), true);
                            if (var19.getSegmentData() != null) {
                                var19.getSegmentData().setNeedsRevalidate(true);
                            }

                            synchronized(this.state) {
                                ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).addToBufferIfNecessary(var19, var19.pos.x, var19.pos.y, var19.pos.z);
                            }
                        }
                    }
                }
            } else {
                (var2 = new RemoteSegment(var1.getSegmentController())).setPos(var1.getSegmentPos());
                var2.setSize(0);
                var2.setLastChanged(System.currentTimeMillis());
            }
        }

        if (var1.getSegmentController() instanceof PlanetIco && !((PlanetIco)var1.getSegmentController()).isPlanetCore()) {
            ((PlanetIco)var1.getSegmentController()).getCreatorThread();
            var1.getSegmentController().getSegmentProvider();
            var5 = var1.getSegmentController().getCreatorThread().allocateRequestData(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            long var18 = ElementCollection.getIndex(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            var2 = ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).doRequestStaged(var18, (RequestDataIcoPlanet)var5);
            var1.getSegmentController().getCreatorThread().freeRequestData(var5, var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);

            assert var2 != null;
        }

        boolean var14 = true;
        if (var2 == null) {
            RequestData var4 = var1.getSegmentController().getCreatorThread().allocateRequestData(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            long var15 = ElementCollection.getIndex(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            var2 = ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).doRequest(var15, var4);
            var1.getSegmentController().getCreatorThread().freeRequestData(var4, var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            var14 = false;
        }

        var1.segment = var2;
        if (var1.sigatureOfSegmentBuffer) {
            try {
                var1.bitMap = ((ServerSegmentProvider)var1.getSegmentController().getSegmentProvider()).getSegmentDataIO().requestSignature(var1.getSegmentPos().x, var1.getSegmentPos().y, var1.getSegmentPos().z);
            } catch (DeserializationException var10) {
                var10.printStackTrace();
            }
        }

        SegmentProvider.buildRevalidationIndex(var1.segment, var1.getSegmentController().isStatic(), var14);
        if (var1.segment.getSegmentData() != null) {
            var1.segment.getSegmentData().setNeedsRevalidate(true);
        }

        synchronized(this.state.getSegmentRequestsLoaded()) {
            assert var1.segment.getSegmentData() == null || var1.segment.getSegmentData().needsRevalidate();

            this.state.getSegmentRequestsLoaded().enqueue(var1);
        }
    }

    private void handleSystemIn(String var1) {
        this.state.executeAdminCommand((String)ServerConfig.SUPER_ADMIN_PASSWORD.getCurrentState(), var1, this.state.getAdminLocalClient());
    }

    public void initiateWave(int var1, int var2, int var3, int var4, BluePrintController var5, Vector3i var6) throws EntityNotFountException, IOException, EntityAlreadyExistsException {
        ShipSpawnWave var7;
        (var7 = new ShipSpawnWave(var2, var3, var5, var4, var6)).createWave(this.state, var1);
        if (var7.getPrintsToSpawn() != null && !var7.getPrintsToSpawn().isEmpty()) {
            this.getState().waves.add(var7);
        }

    }

    public boolean isAdmin(String var1) {
        return this.state.getAdmins().isEmpty() || this.state.getAdmins().containsKey(var1.trim().toLowerCase(Locale.ENGLISH));
    }

    public boolean isUserProtected(String var1) {
        return this.state.getProtectedUsers().containsKey(var1.trim().toLowerCase(Locale.ENGLISH));
    }

    private void loadUniverse() throws IOException, SQLException {
        try {
            this.state.getMetaObjectManager().load();
        } catch (FileNotFoundException var1) {
        }
    }

    private void purgeDB() {
        synchronized(this.state.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var2 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

            while(var2.hasNext()) {
                Sendable var3;
                if ((var3 = (Sendable)var2.next()) instanceof SegmentController) {
                    ((SegmentController)var3).getSegmentProvider().purgeDB();
                }
            }

        }
    }

    public SectorSwitch queueSectorSwitch(SimpleTransformableSendableObject var1, Vector3i var2, int var3, boolean var4) {
        return this.queueSectorSwitch(var1, var2, var3, var4, false, false);
    }

    public SectorSwitch queueSectorSwitch(SimpleTransformableSendableObject var1, Vector3i var2, int var3, boolean var4, boolean var5, boolean var6) {
        if (var5 || System.currentTimeMillis() - var1.lastSectorSwitch > 4000L) {
            SectorSwitch var7;
            (var7 = new SectorSwitch(var1, new Vector3i(var2), var3)).makeCopy = var4;
            var7.elminateGravity = var6;
            if (!this.state.getSectorSwitches().contains(var7)) {
                var1.lastSectorSwitch = System.currentTimeMillis();
                this.state.getSectorSwitches().add(var7);
                return var7;
            }
        }

        return null;
    }

    public Tag readEntity(String var1) throws IOException, EntityNotFountException {
        return this.readEntity(var1, ".ent");
    }

    public Tag readEntity(String var1, String var2) throws IOException, EntityNotFountException {
        System.currentTimeMillis();
        if (var2.length() > 0 && !var2.startsWith(".")) {
            var2 = "." + var2;
        }

        String var3;
        for(var3 = GameServerState.ENTITY_DATABASE_PATH + var1 + var2; var3.endsWith("."); var3 = var2.substring(0, var3.length() - 2)) {
            System.err.println("Replacing point at end!");
        }

        Object var7;
        synchronized(GameServerState.fileLocks) {
            if ((var7 = GameServerState.fileLocks.get(var3)) == null) {
                var7 = new Object();
                GameServerState.fileLocks.put(var3, var7);
            }
        }

        synchronized(var7) {
            Tag var10000 = this.readTag(var3, var1);
            var1 = null;
            return var10000;
        }
    }

    public void readjustControllers(Collection<Element> var1, SegmentController var2, Segment var3) {
    }

    private Tag readTag(String var1, String var2) throws IOException, EntityNotFountException {
        FileExt var3;
        if ((var3 = new FileExt(var1)).exists()) {
            try {
                BufferedInputStream var4;
                Tag var5 = Tag.readFrom(var4 = new BufferedInputStream(new FileInputStream(var3), 4096), true, false);
                var4.close();
                return var5;
            } catch (EOFException var7) {
                var7.printStackTrace();
                this.broadcastMessage(new Object[]{465, var3.getName()}, 3);
                System.err.println("Exception: File corrupt! creating backup, and regenerating new sector please report this bug: " + var3.getName());

                try {
                    FileUtils.copyFile(var3, new FileExt(var1 + ".corrupt"));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

                throw new EntityNotFountException(var2 + "; file: " + var3.getAbsolutePath() + "; pathString: " + var1);
            }
        } else {
            System.err.println("[FILE NOT FOUND] File for " + var2 + " does not exist: " + var3.getAbsolutePath());
            throw new EntityNotFountException(var2 + "; file: " + var3.getAbsolutePath() + "; pathString: " + var1);
        }
    }

    public boolean removeAdmin(String var1, String var2) {
        Admin var3 = null;
        synchronized(this.state.getAdmins()) {
            var3 = (Admin)this.state.getAdmins().remove(var2.trim().toLowerCase(Locale.ENGLISH));
        }

        if (var3 != null) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' removed from admins: '" + var2 + "'");

            try {
                this.writeAdminsToDisk();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return var3 != null;
    }

    public boolean removeBannedIp(String var1, String var2) {
        boolean var3 = false;
        synchronized(this.state.getBlackListedIps()) {
            var3 = this.state.getBlackListedIps().remove(var2.trim());
        }

        if (var3) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' unbanned ip: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return var3;
    }

    public boolean removeBannedName(String var1, String var2) {
        boolean var3 = false;
        synchronized(this.state.getBlackListedNames()) {
            var3 = this.state.getBlackListedNames().remove(var2.trim().toLowerCase(Locale.ENGLISH));
        }

        if (var3) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' unbanned playerName: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return var3;
    }

    public boolean removeBannedAccount(String var1, String var2) {
        boolean var3;
        synchronized(this.state.getBlackListedAccounts()) {
            var3 = this.state.getBlackListedAccounts().remove(var2.trim());
        }

        if (var3) {
            LogUtil.log().fine("[ADMIN] '" + var1 + "' unbanned StarMade account: '" + var2 + "'");

            try {
                this.writeBlackListToDisk();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return var3;
    }

    public void scheduleSegmentRequest(SegmentController var1, Vector3i var2, NetworkSegmentProvider var3, long var4, short var6, boolean var7, boolean var8) {
        ServerSegmentRequest var10;
        (var10 = new ServerSegmentRequest(var1, var2, var3, var4, var6)).highPrio = var8;
        var10.sigatureOfSegmentBuffer = var7;
        synchronized(this.state.getSegmentRequests()) {
            this.state.getSegmentRequests().enqueue(var10);
            this.state.getSegmentRequests().notify();
        }
    }

    public void sendPlayerMessage(String var1, Object[] var2, int var3) {
        try {
            System.err.println("[SERVER] sending message to " + var1 + ": Param: " + Arrays.toString(var2));
            PlayerState var4;
            (var4 = this.getState().getPlayerFromName(var1)).sendServerMessage(new ServerMessage(var2, var3, var4.getId()));
        } catch (PlayerNotFountException var5) {
            System.err.println("CANNOT SEND MESSAGE TO " + var1 + ": " + Arrays.toString(var2));
            var5.printStackTrace();
        }
    }

    public void spawnKillBonus(SimpleTransformableSendableObject var1) {
        float var2 = (Float)ServerConfig.AI_DESTRUCTION_LOOT_COUNT_MULTIPLIER.getCurrentState();
        float var3 = (Float)ServerConfig.AI_DESTRUCTION_LOOT_STACK_MULTIPLIER.getCurrentState();
        if (var2 > 0.0F && var3 > 0.0F) {
            if (var1 instanceof Ship) {
                Ship var4 = (Ship)var1;
                int var10 = (int)((float)Universe.getRandom().nextInt(Math.min(5000, var4.getTotalElements()) / 10 + 1) * var2);
                Sector var11 = this.getState().getUniverse().getSector(var1.getSectorId());
                Vector3f var9 = new Vector3f(var1.getWorldTransform().origin);
                System.err.println("[SERVER] SPAWNING BONUS IN sector: " + var11 + "; amount: " + var10);
                if (var11 != null) {
                    for(int var5 = 0; var5 < var10; ++var5) {
                        int var6 = Math.max(1, (int)Math.floor(Universe.getRandom().nextGaussian() * 500.0D * (double)var3) + 10);
                        short var7;
                        ElementInformation var10000 = ElementKeyMap.getInfo(var7 = ElementKeyMap.typeList()[Universe.getRandom().nextInt(ElementKeyMap.typeList().length)]);
                        Vector3f var8 = null;
                        if (var10000.isShoppable()) {
                            Vector3f var12 = var8 = new Vector3f(var9);
                            var12.x = (float)((double)var12.x + 5.0D * (Math.random() - 0.5D));
                            var8.y = (float)((double)var8.y + 5.0D * (Math.random() - 0.5D));
                            var8.z = (float)((double)var8.z + 5.0D * (Math.random() - 0.5D));
                            var11.getRemoteSector().addItem(var8, var7, -1, var6);
                        }
                    }
                }
            }

        }
    }

    private void spawnPlayerCharacterPrepare(PlayerState var1) throws IOException {
        var1.spawnData.onSpawnPreparePlayerCharacter(var1.spawnedOnce);
    }

    private void spawnPlayerCharacter(PlayerState var1) throws IOException {
        String var2 = "ENTITY_PLAYERCHARACTER_" + var1.getName();
        System.err.println("[SERVER][SPAWN] SPAWNING NEW CHARACTER FOR " + var1);
        var1.getControllerState().setLastTransform((Transform)null);
        PlayerCharacter var3;
        (var3 = new PlayerCharacter(this.state)).initialize();
        var1.spawnData.onSpawnPlayerCharacter(var3, var1.spawnedOnce);
        System.err.println("[SERVER][PlayerCharacter] " + this.getState() + " Set initial transform to " + var3.getInitialTransform().origin);
        var3.setFactionId(var1.getFactionId());
        var3.setId(this.state.getNextFreeObjectId());
        var3.setUniqueIdentifier(var2);
        var3.setClientOwnerId(var1.getClientId());
        this.getSynchController().addNewSynchronizedObjectQueued(var3);
        var1.setAssignedPlayerCharacter(var3);
        var1.spawnedOnce = true;
        var1.setAlive(true);
        var1.lastSpawnedThisSession(System.currentTimeMillis());
        //INSERTED CODE
        PlayerSpawnEvent event = new PlayerSpawnEvent(var1.getCurrentSector(), var3);
        StarLoader.fireEvent(PlayerSpawnEvent.class, event, true);
        ///
    }

    public void triggerForcedSave() {
        this.lastSave = 0L;
    }

    private void checkExplosionOrders() {
        ExplosionRunnable var2;
        for(; 0 < this.state.getExplosionOrdersQueued().size() && (var2 = (ExplosionRunnable)this.state.getExplosionOrdersQueued().get(0)).canExecute(); this.state.getExplosionOrdersQueued().remove(0)) {
            if (var2.beforeExplosion()) {
                this.getState().getTheadPoolExplosions().execute(var2);
            } else {
                System.err.println("[EXPLOSION][WARNING] NOT EXECUTING EXPLOSION ORDER");
            }
        }

        if (!this.state.getExplosionOrdersFinished().isEmpty()) {
            synchronized(this.state.getExplosionOrdersFinished()) {
                while(!this.state.getExplosionOrdersFinished().isEmpty()) {
                    ((ExplosionRunnable)this.state.getExplosionOrdersFinished().dequeue()).afterExplosion();
                }

            }
        }
    }

    private void writeAdminsToDisk() throws IOException {
        FileExt var1;
        (var1 = new FileExt("./admins.txt")).delete();
        this.createAdminFile();
        BufferedWriter var7 = new BufferedWriter(new FileWriter(var1));
        synchronized(this.state.getAdmins()) {
            Iterator var3 = this.state.getAdmins().values().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                Admin var4 = (Admin)var3.next();
                var7.append(var4.name.trim().toLowerCase(Locale.ENGLISH));
                if (var4.deniedCommands.size() > 0) {
                    var7.append("#");
                    Iterator var8 = var4.deniedCommands.iterator();

                    while(var8.hasNext()) {
                        AdminCommands var5 = (AdminCommands)var8.next();
                        var7.append(var5.name());
                        if (var8.hasNext()) {
                            var7.append(",");
                        }
                    }
                }

                var7.newLine();
            }
        }

        var7.close();
    }

    private void writeBlackListToDisk() throws IOException {
        FileExt var1;
        (var1 = new FileExt("./blacklist.txt")).delete();
        this.createBlacklistFile();
        BufferedWriter var8 = new BufferedWriter(new FileWriter(var1));
        Iterator var3;
        PlayerAccountEntry var4;
        synchronized(this.state.getBlackListedNames()) {
            var3 = this.state.getBlackListedNames().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                if ((var4 = (PlayerAccountEntry)var3.next()).isValid(System.currentTimeMillis())) {
                    var8.append(var4.fileLineName());
                    var8.newLine();
                }
            }
        }

        synchronized(this.state.getBlackListedIps()) {
            var3 = this.state.getBlackListedIps().iterator();

            while(var3.hasNext()) {
                if ((var4 = (PlayerAccountEntry)var3.next()).isValid(System.currentTimeMillis())) {
                    var8.append(var4.fileLineIP());
                    var8.newLine();
                }
            }
        }

        synchronized(this.state.getBlackListedAccounts()) {
            var3 = this.state.getBlackListedAccounts().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                if ((var4 = (PlayerAccountEntry)var3.next()).isValid(System.currentTimeMillis())) {
                    var8.append(var4.fileLineAccount());
                    var8.newLine();
                }
            }
        }

        var8.close();
    }

    public void writeEntitiesToDatabaseAtAutosaveOrShutdown(boolean var1, boolean var2, Universe var3) throws IOException, SQLException {
        assert this.getServerState() != null;

        assert this.getServerState().getUniverse() != null;

        synchronized(this.state.getLocalAndRemoteObjectContainer().getLocalObjects()) {
            Iterator var5 = this.state.getPlayerStatesByName().values().iterator();

            while(true) {
                if (!var5.hasNext()) {
                    break;
                }

                PlayerState var6 = (PlayerState)var5.next();
                this.writeEntity(var6, true);
            }
        }

        var3.writeToDatabase(var1, var2);
    }

    public void writeSingleEntityWithDock(Sendable var1) throws IOException, SQLException {
        Sector.writeSingle(this.state, var1);
        if (var1 instanceof SegmentController) {
            Iterator var3 = ((SegmentController)var1).railController.next.iterator();

            while(var3.hasNext()) {
                RailRelation var2 = (RailRelation)var3.next();
                this.writeSingleEntityWithDock(var2.docked.getSegmentController());
            }
        }

    }

    public void writeEntity(DiskWritable var1, boolean var2) throws IOException, SQLException {
        if (!var1.isVolatile()) {
            if (var1 instanceof TransientSegmentController && !((TransientSegmentController)var1).isTouched() && !((TransientSegmentController)var1).needsTagSave()) {
                this.state.getDatabaseIndex().getTableManager().getEntityTable().updateOrInsertSegmentController((SegmentController)var1);
            } else {
                long var3 = System.currentTimeMillis();
                boolean var5 = true;
                if (var1 instanceof TransientSegmentController && !((TransientSegmentController)var1).isTouched() && !((TransientSegmentController)var1).needsTagSave()) {
                    var5 = false;
                }

                if (var1 instanceof PlayerState) {
                    System.err.println("[SERVER] WRITING PLAYER TO DISK: " + var1 + " DD: " + var5);
                }

                if (var5) {
                    assert var1.getUniqueIdentifier() != null : "no ident for " + var1 + " on SERVER";

                    String var9 = var1.getUniqueIdentifier() + ".ent";
                    String var10 = GameServerState.ENTITY_DATABASE_PATH;
                    EntityFileTools.write(GameServerState.fileLocks, var1, var10, var9);
                } else {
                    System.err.println("[SERVER] NOT WRITING ENTITY TAG: " + var1 + " DOESNT NEED SAVE");
                }

                long var7 = System.currentTimeMillis();
                if (var2 && var1 instanceof SegmentController) {
                    if (!((SegmentController)var1).isVirtualBlueprint()) {
                        this.state.getDatabaseIndex().getTableManager().getEntityTable().updateOrInsertSegmentController((SegmentController)var1);
                    } else {
                        this.state.getDatabaseIndex().getTableManager().getEntityTable().removeSegmentController((SegmentController)var1);
                        if (((SegmentController)var1).getDbId() > 0L) {
                            this.state.getDatabaseIndex().getTableManager().getTradeNodeTable().removeTradeNode(((SegmentController)var1).getDbId());
                            this.state.getUniverse().tradeNodesDirty.enqueue(((SegmentController)var1).getDbId());
                        }
                    }
                }

                long var11 = 0L + (System.currentTimeMillis() - var7);
                long var12;
                if ((var12 = System.currentTimeMillis() - var3) > 20L) {
                    System.err.println("[SERVER] WARNING: WRITING ENTITY TAG: " + var1 + " FINISHED: " + var12 + "ms -> tagcreate: 0; tagwrite: 0; rename: 0; delete: 0; DB: " + var11);
                }

            }
        }
    }

    private void writeProtectedUsersToDisk() throws IOException {
        FileExt var1;
        (var1 = new FileExt("./protected.txt")).delete();
        this.createProtectedUserFile();
        BufferedWriter var6 = new BufferedWriter(new FileWriter(var1));
        synchronized(this.state.getProtectedUsers()) {
            Iterator var3 = this.state.getProtectedUsers().entrySet().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                Entry var4 = (Entry)var3.next();
                var6.append(((String)var4.getKey()).toLowerCase(Locale.ENGLISH) + ";" + ((ProtectedUplinkName)var4.getValue()).uplinkname + ";" + ((ProtectedUplinkName)var4.getValue()).timeProtected);
                var6.newLine();
            }
        }

        var6.close();
    }

    private void writeWhiteListToDisk() throws IOException {
        FileExt var1;
        (var1 = new FileExt("./whitelist.txt")).delete();
        this.createWhitelistFile();
        BufferedWriter var8 = new BufferedWriter(new FileWriter(var1));
        Iterator var3;
        PlayerAccountEntry var4;
        synchronized(this.state.getWhiteListedNames()) {
            var3 = this.state.getWhiteListedNames().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                var4 = (PlayerAccountEntry)var3.next();
                var8.append(var4.fileLineName());
                var8.newLine();
            }
        }

        synchronized(this.state.getWhiteListedIps()) {
            var3 = this.state.getWhiteListedIps().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                var4 = (PlayerAccountEntry)var3.next();
                var8.append(var4.fileLineIP());
                var8.newLine();
            }
        }

        synchronized(this.state.getWhiteListedAccounts()) {
            var3 = this.state.getWhiteListedAccounts().iterator();

            while(true) {
                if (!var3.hasNext()) {
                    break;
                }

                var4 = (PlayerAccountEntry)var3.next();
                var8.append(var4.fileLineAccount());
                var8.newLine();
            }
        }

        var8.close();
    }

    public void queueSegmentControllerBreak(SegmentPiece var1) {
        this.segmentBreaker.enqueue(new BreakTestRequest(var1));
    }

    public void queueSegmentPath(SegmentPiece var1, SegmentPiece var2, SegmentPathCallback var3) {
        this.getSegmentPathFinder().enqueue(new SegmentPathRequest(var1, var2, var3));
    }

    public void queueSegmentGroundPath(SegmentPiece var1, SegmentPiece var2, SegmentPathCallback var3) {
        this.getSegmentPathGroundFinder().enqueue(new SegmentPathRequest(var1, var2, var3));
    }

    public void queueSegmentPath(Vector3i var1, Vector3i var2, SegmentController var3, SegmentPathCallback var4) {
        this.getSegmentPathFinder().enqueue(new SegmentPathRequest(var1, var2, var3, var4));
    }

    public void queueSegmentGroundPath(Vector3i var1, Vector3i var2, SegmentController var3, SegmentPathCallback var4) {
        this.getSegmentPathGroundFinder().enqueue(new SegmentPathRequest(var1, var2, var3, var4));
    }

    public void queueSegmentRandomPath(SegmentPiece var1, Vector3i var2, BoundingBox var3, Vector3i var4, SegmentPathCallback var5) {
        this.getSegmentPathFinder().enqueue(new SegmentPathRequest(var1, var2, var3, var4, var5));
    }

    public void queueSegmentRandomGroundPath(SegmentPiece var1, Vector3i var2, BoundingBox var3, Vector3i var4, SegmentPathCallback var5) {
        this.getSegmentPathGroundFinder().enqueue(new SegmentPathRequest(var1, var2, var3, var4, var5));
    }

    public void queueCreatureSpawn(CreatureSpawn var1) {
        synchronized(this.state.creatureSpawns) {
            this.state.creatureSpawns.enqueue(var1);
        }
    }

    public SegmentPathFindingHandler getSegmentPathFinder() {
        return this.segmentPathFinder;
    }

    public SegmentPathGroundFindingHandler getSegmentPathGroundFinder() {
        return this.segmentPathGroundFinder;
    }

    public void parseBlockBehavior(String var1) {
        try {
            FileExt var9;
            Document var2 = XMLTools.loadXML(var9 = new FileExt(var1));
            FileExt var3;
            if ((var3 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfig.xml")).exists()) {
                Document var11 = XMLTools.loadXML(var3);
                System.err.println("[SERVER] Custom block behavior config found");
                XMLTools.mergeDocument(var2, var11);

                try {
                    XMLTools.writeDocument(new FileExt("blockBehaviorConfigMergeResult.xml"), var2);
                    var9 = new FileExt("blockBehaviorConfigMergeResult.xml");
                } catch (TransformerException var4) {
                    var4.printStackTrace();
                }
            }

            this.state.setBlockBehaviorConfig(var2);
            RandomAccessFile var12;
            byte[] var10 = new byte[(int)(var12 = new RandomAccessFile(var9, "r")).length()];
            var12.read(var10);
            var12.close();
            this.state.setBlockBehaviorBytes(var10);
            this.state.setBlockBehaviorChecksum(FileUtil.getSha1Checksum(var9));
        } catch (SAXException var5) {
            var5.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var5);
        } catch (IOException var6) {
            var6.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var6);
        } catch (ParserConfigurationException var7) {
            var7.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var7);
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        }
    }

    public byte[] getBlockBehaviorChanged() {
        return this.blockBehaviorChanged;
    }

    public void setBlockBehaviorChanged(byte[] var1) {
        this.blockBehaviorChanged = var1;
    }

    public boolean allowedToExecuteAdminCommand(RegisteredClientOnServer var1, AdminCommands var2) {
        if (this.state.getAdmins().size() == 0) {
            return true;
        } else if (this.state.getAdmins().containsKey(var1.getPlayerName().toLowerCase(Locale.ENGLISH))) {
            return !((Admin)this.state.getAdmins().get(var1.getPlayerName().toLowerCase(Locale.ENGLISH))).deniedCommands.contains(var2);
        } else {
            return false;
        }
    }

    public void broadcastMessageSector(Object[] var1, int var2, int var3) {
        Iterator var4 = this.state.getLocalAndRemoteObjectContainer().getLocalObjects().values().iterator();

        while(var4.hasNext()) {
            Sendable var5;
            if ((var5 = (Sendable)var4.next()) instanceof PlayerState && ((PlayerState)var5).getCurrentSectorId() == var3) {
                ((PlayerState)var5).sendServerMessage(new ServerMessage(var1, var2));
            }
        }

    }

    public void onEntityAddedToSector(Sector var1, SimpleTransformableSendableObject var2) {
        int var3 = this.sectorListeners.size();

        for(int var4 = 0; var4 < var3; ++var4) {
            ((SectorListener)this.sectorListeners.get(var4)).onSectorEntityAdded(var2, var1);
        }

    }

    public void onEntityRemoveFromSector(Sector var1, SimpleTransformableSendableObject var2) {
        int var3 = this.sectorListeners.size();

        for(int var4 = 0; var4 < var3; ++var4) {
            ((SectorListener)this.sectorListeners.get(var4)).onSectorEntityRemoved(var2, var1);
        }

    }

    public void onSectorAddedSynch(Sector var1) {
        int var2 = this.sectorListeners.size();

        for(int var3 = 0; var3 < var2; ++var3) {
            ((SectorListener)this.sectorListeners.get(var3)).onSectorAdded(var1);
        }

    }

    public void onSectorRemovedSynch(Sector var1) {
        int var2 = this.sectorListeners.size();

        for(int var3 = 0; var3 < var2; ++var3) {
            ((SectorListener)this.sectorListeners.get(var3)).onSectorRemoved(var1);
        }

    }

    public List<SectorListener> getSectorListeners() {
        return this.sectorListeners;
    }

    public MissileManagerInterface getMissileManager() {
        return this.missileController.getMissileManager();
    }
}
