//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common;

import api.DebugFile;
import api.mod.ModStarter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import org.hsqldb.HsqlException;
import org.schema.common.CallInterace;
import org.schema.common.LogUtil;
import org.schema.common.ParseException;
import org.schema.common.util.StringTools;
import org.schema.common.util.data.ResourceUtil;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.common.util.security.OperatingSystem;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.controller.GameMainMenuController;
import org.schema.game.client.controller.manager.ingame.BlockBuffer.BlockBufferFactory;
import org.schema.game.client.data.ClientStatics;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.client.view.cubes.shapes.BlockShapeAlgorithm;
import org.schema.game.common.api.SessionNewStyle;
import org.schema.game.common.controller.FloatingRock;
import org.schema.game.common.controller.FloatingRockManaged;
import org.schema.game.common.controller.Planet;
import org.schema.game.common.controller.PlanetIco;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.ShopSpaceStation;
import org.schema.game.common.controller.SpaceCreature;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.Vehicle;
import org.schema.game.common.controller.database.DatabaseIndex;
import org.schema.game.common.controller.database.tables.EntityTable.Despawn;
import org.schema.game.common.controller.elements.VoidElementManager;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.blockeffects.config.ConfigPool;
import org.schema.game.common.data.creature.AICharacter;
import org.schema.game.common.data.creature.AIRandomCompositeCreature;
import org.schema.game.common.data.element.ControlElementMapperFactory;
import org.schema.game.common.data.element.ElementCountMapFactory;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.player.CharacterProvider;
import org.schema.game.common.data.player.FixedSpaceEntityProvider;
import org.schema.game.common.data.player.PlayerCharacter;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.SpaceCreatureProvider;
import org.schema.game.common.data.player.faction.config.FactionConfig;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject.EntityType;
import org.schema.game.common.data.world.migration.StationAndShipTransienceMigration;
import org.schema.game.common.data.world.space.PlanetCore;
import org.schema.game.common.gui.ClientDialog;
import org.schema.game.common.gui.ServerGUI;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.common.util.FolderZipper;
import org.schema.game.common.util.GuiErrorHandler;
import org.schema.game.common.version.Version;
import org.schema.game.mod.ModManager;
import org.schema.game.server.controller.BluePrintController;
import org.schema.game.server.controller.GameServerController;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerConfig;
import org.schema.game.server.data.simulation.npc.news.NPCFactionNewsEventFactory;
import org.schema.schine.auth.Session;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import org.schema.schine.graphicsengine.core.ResourceException;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIResizableGrabbableWindow;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.network.ChatSystem;
import org.schema.schine.network.NetUtil;
import org.schema.schine.network.SendableFactory;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.client.HostPortLoginName;
import org.schema.schine.network.commands.LoginRequest;
import org.schema.schine.network.exception.ServerPortNotAvailableException;
import org.schema.schine.network.server.ServerController;
import org.schema.schine.network.server.ServerState;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.MeshLoader;
import org.schema.schine.resource.ResourceLoader;
import org.schema.schine.resource.tag.SerializableTagFactory;
import org.schema.schine.resource.tag.SerializableTagRegister;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.TagSerializableLong2TransformMapFactory;
import org.schema.schine.resource.tag.TagSerializableLong2Vector3fMapFactory;
import org.schema.schine.resource.tag.TagSerializableLongSetFactory;
import org.xml.sax.SAXException;

public class Starter implements Observer {
    public static final ModManager modManager = new ModManager();
    protected static boolean cleanUpDBForStations = false;
    public static boolean DEDICATED_SERVER_ARGUMENT = false;
    public static Session currentSession;
    public static boolean hasGUI = true;
    public static boolean serverInitFinished = false;
    public static ServerGUI sGUI;
    public static boolean serverUp;
    public static final Object serverLock = new Object();
    public static boolean loggedIn;
    private static int authStyle = -1;
    private static boolean forceSimClean;
    private static String uniqueSessionId;
    private static ClientRunnable clientRunnable;
    private static boolean importedCustom;
    private static boolean registered;
    public static Exception startupException;
    private ArrayList<HostPortLoginName> history = new ArrayList();

    public Starter() {
    }

    public static synchronized String getUniqueSessionId() {
        if (uniqueSessionId == null) {
            uniqueSessionId = createUniqueSessionId();
        }

        return uniqueSessionId;
    }

    public static void writeDefaultConfigs() {
        EngineSettings.writeDefault();
        KeyboardMappings.writeDefault();
    }

    public static String createUniqueSessionId() {
        return System.currentTimeMillis() + "-" + (int)(Math.random() * 40000.0D);
    }

    public static void checkDatabase(boolean var0) {
        FileExt var5;
        if ((var5 = new FileExt(GameServerState.ENTITY_DATABASE_PATH)).exists() && var5.list().length > 1 && !DatabaseIndex.existsDB()) {
            try {
                System.err.println("DATABASE STARTING");
                System.err.println("INFO SET");
                DatabaseIndex var6 = new DatabaseIndex();
                System.err.println("DATABASE CREATING");
                var6.createDatabase();
                System.err.println("DATABASE CREATED");
                var6.fillCompleteDatabase(new PercentCallbackInterface() {
                    public final void update(String var1) {
                    }
                });
                System.err.println("DATABASE FILLED");
                var6.getTableManager().getEntityTable().optimizeDatabase(new PercentCallbackInterface() {
                    public final void update(String var1) {
                    }
                });
                System.err.println("DATABASE OPTIMIZED");
                var6.destroy();
                System.err.println("DATABASE CLOSED");
            } catch (SQLException var1) {
                var1.printStackTrace();
                JOptionPane.showOptionDialog((Component)null, Lng.ORG_SCHEMA_GAME_COMMON_STARTER_4, "ERROR", 0, 0, (Icon)null, new String[]{"Ok"}, "Ok");
            } catch (IOException var2) {
                var2.printStackTrace();
                JOptionPane.showOptionDialog((Component)null, Lng.ORG_SCHEMA_GAME_COMMON_STARTER_5, "ERROR", 0, 0, (Icon)null, new String[]{"Ok"}, "Ok");
            }

            try {
                LogUtil.setUp(20, new CallInterace() {
                    public final void call() {
                    }
                });
                return;
            } catch (SecurityException var3) {
                var3.printStackTrace();
                return;
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }

    private static void cleanSimulatedMobs() throws IOException, SQLException {
        DatabaseIndex var0;
        (var0 = new DatabaseIndex()).getTableManager().getEntityTable().removeAll(DatabaseIndex.escape("MOB_SIM") + "%", Despawn.ALL, (Vector3i)null, false);
        File[] var1;
        int var2 = (var1 = (new FileExt(GameServerState.ENTITY_DATABASE_PATH)).listFiles(new FilenameFilter() {
            public final boolean accept(File var1, String var2) {
                return var2.startsWith("ENTITY_SHIP_MOB_SIM");
            }
        })).length;

        int var3;
        for(var3 = 0; var3 < var2; ++var3) {
            var1[var3].delete();
        }

        (new FileExt(GameServerState.ENTITY_DATABASE_PATH + "/SIMULATION_STATE.sim")).delete();
        var2 = (var1 = (new FileExt(GameServerState.SEGMENT_DATA_DATABASE_PATH)).listFiles(new FilenameFilter() {
            public final boolean accept(File var1, String var2) {
                return var2.startsWith("ENTITY_SHIP_MOB_SIM");
            }
        })).length;

        for(var3 = 0; var3 < var2; ++var3) {
            var1[var3].delete();
        }

        var0.destroy();
    }

    public static void cleanUniverseWithBackup() throws IOException {
        long var0 = System.currentTimeMillis();
        (new FileExt("./backup/")).mkdir();
        FileExt var2 = new FileExt(GameServerState.ENTITY_DATABASE_PATH);
        FileExt var3 = new FileExt(ClientStatics.ENTITY_DATABASE_PATH);
        if (var2.exists()) {
            FolderZipper.zipFolder(GameServerState.ENTITY_DATABASE_PATH, "./backup/server-database-backup-" + var0 + ".zip", (String)null, (FileFilter)null);
            FileUtil.deleteDir(var2);
            var2.delete();
        }

        if (var3.exists()) {
            FolderZipper.zipFolder(ClientStatics.ENTITY_DATABASE_PATH, "./backup/client-database-backup-" + var0 + ".zip", (String)null, (FileFilter)null);
            FileUtil.deleteDir(var3);
            var3.delete();
        }

    }

    public static void cleanUniverseWithoutBackup() {
        System.out.println("RESETTING DB");
        FileUtil.deleteDir(new FileExt(GameServerState.ENTITY_DATABASE_PATH));
        (new FileExt(GameServerState.ENTITY_DATABASE_PATH)).delete();
        FileUtil.deleteDir(new FileExt(ClientStatics.ENTITY_DATABASE_PATH));
        (new FileExt(ClientStatics.ENTITY_DATABASE_PATH)).delete();
    }

    public static void cleanClientCacheWithoutBackup() {
        System.out.println("RESETTING CLIENT DB");
        FileUtil.deleteDir(new FileExt(ClientStatics.ENTITY_DATABASE_PATH));
        (new FileExt(ClientStatics.ENTITY_DATABASE_PATH)).delete();
        FileUtil.deleteDir(new FileExt(ClientStatics.ENTITY_DATABASE_PATH));
        (new FileExt(ClientStatics.ENTITY_DATABASE_PATH)).delete();
    }

    static void clientStartup() {
        String var0 = UIManager.getSystemLookAndFeelClassName();
        System.err.println("[LookAndFeel] NATIVE LF " + var0);
        GameClientState.instanced = true;
        if ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(var0)) {
            try {
                UIManager.setLookAndFeel(var0);
            } catch (InstantiationException var22) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException var18) {
                    var18.printStackTrace();
                } catch (InstantiationException var19) {
                    var19.printStackTrace();
                } catch (IllegalAccessException var20) {
                    var20.printStackTrace();
                } catch (UnsupportedLookAndFeelException var21) {
                    var21.printStackTrace();
                }
            } catch (ClassNotFoundException var23) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException var14) {
                    var14.printStackTrace();
                } catch (InstantiationException var15) {
                    var15.printStackTrace();
                } catch (IllegalAccessException var16) {
                    var16.printStackTrace();
                } catch (UnsupportedLookAndFeelException var17) {
                    var17.printStackTrace();
                }
            } catch (UnsupportedLookAndFeelException var24) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException var10) {
                    var10.printStackTrace();
                } catch (InstantiationException var11) {
                    var11.printStackTrace();
                } catch (IllegalAccessException var12) {
                    var12.printStackTrace();
                } catch (UnsupportedLookAndFeelException var13) {
                    var13.printStackTrace();
                }
            } catch (IllegalAccessException var25) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException var6) {
                    var6.printStackTrace();
                } catch (InstantiationException var7) {
                    var7.printStackTrace();
                } catch (IllegalAccessException var8) {
                    var8.printStackTrace();
                } catch (UnsupportedLookAndFeelException var9) {
                    var9.printStackTrace();
                }
            }
        } else {
            System.err.println("[ERROR] Not applying lok and feel as not all can guarantee all decoration working, resulting in a startup crash");
        }

        System.out.println("[CLIENT][GUI] Starting Client Login&Settings Dialog");

        try {
            (new JPanel()).setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Game", 4, 2, (Font)null, new Color(0, 0, 0)));
        } catch (Exception var5) {
            var5.printStackTrace();
            System.err.println("Look And Feel Exception catched. Reverting to default");

            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (InstantiationException var1) {
            } catch (ClassNotFoundException var2) {
            } catch (UnsupportedLookAndFeelException var3) {
            } catch (IllegalAccessException var4) {
            }
        }

        (new Starter()).getFromHostAndPortConnectionDialog();
    }

    public static void doMigration(JDialog var0, boolean var1) {
        FileExt var14;
        if (!(var14 = new FileExt(BluePrintController.active.entityBluePrintPath)).exists()) {
            var14.mkdir();

            try {
                FileUtil.copyDirectory(new FileExt(BluePrintController.defaultBB.entityBluePrintPath), new FileExt(BluePrintController.active.entityBluePrintPath));
                BluePrintController.active.setImportedByDefault(true);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        try {
            BluePrintController.active.convert(BluePrintController.active.entityBluePrintPath, true);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        checkDatabase(var0 == null);
        if ((var14 = new FileExt(GameServerState.ENTITY_DATABASE_PATH)).exists() && var14.list().length > 1) {
            FileExt var12;
            var1 = !(var12 = new FileExt("lpversion")).exists();
            boolean var2 = !var12.exists();
            boolean var3 = !var12.exists();
            if (var12.exists()) {
                try {
                    String var4;
                    BufferedReader var13;
                    if ((var4 = (var13 = new BufferedReader(new FileReader(var12))).readLine()).length() > 0 && !var4.equals(Version.VERSION + ";" + Version.build)) {
                        var3 = true;
                    }

                    String[] var15;
                    if (var4.length() > 0 && (var15 = var4.split(";"))[0].split("\\.").length < 3) {
                        float var16;
                        if ((var16 = Float.parseFloat(var15[0])) < 0.0934F) {
                            System.out.println("Old Version Found: " + var16);
                            var1 = true;
                        }

                        if (var16 < 0.09444F) {
                            System.out.println("Database size Migration needed : " + var16);
                            var2 = true;
                        }
                    }

                    var13.close();
                } catch (IOException var6) {
                    var6.printStackTrace();
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            }

            if (var3 && BluePrintController.active.readBluePrints().isEmpty()) {
                BluePrintController.active.setDirty(true);
            }

            if (var2) {
                try {
                    System.err.println("DOING TAG DATABASE MIGRATION (this can take a little) [DO NOT KILL THE PROCESS]");
                    dbShopAndTagMigration();
                } catch (IOException var5) {
                    var5.printStackTrace();
                }
            }

            if (var1 || forceSimClean) {
                try {
                    cleanSimulatedMobs();
                    return;
                } catch (IOException var10) {
                    var10.printStackTrace();
                    return;
                } catch (SQLException var11) {
                    var11.printStackTrace();
                }
            }
        }

    }

    private static void dbShopAndTagMigration() throws IOException {
        File[] var0 = (new FileExt(GameServerState.SEGMENT_DATA_DATABASE_PATH)).listFiles(new FilenameFilter() {
            public final boolean accept(File var1, String var2) {
                return var2.startsWith(EntityType.SHOP.dbPrefix) && var2.endsWith(".smd2");
            }
        });
        System.err.println("[MIGRATION] REMOVING UNNECESSARY SHOP RAW BLOCK DATA. FILES FOUND: " + var0.length);

        int var1;
        for(var1 = 0; var1 < var0.length; ++var1) {
            try {
                var0[var1].delete();
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        System.err.println("[MIGRATION] REMOVING UNNECESSARY SHOP RAW BLOCK DATA DONE");
        System.err.println("[MIGRATION] MIGRATING META-DATA STRUCTURE (can take very long)");
        var0 = (new FileExt(GameServerState.ENTITY_DATABASE_PATH)).listFiles(new FilenameFilter() {
            public final boolean accept(File var1, String var2) {
                return var2.startsWith("ENTITY_") && var2.endsWith(".ent");
            }
        });
        long var4 = 0L;

        for(var1 = 0; var1 < var0.length; ++var1) {
            try {
                long var6 = var0[var1].length();
                Tag var2 = Tag.readFrom(new FileInputStream(var0[var1]), true, false);
                var0[var1].delete();
                FileOutputStream var3 = new FileOutputStream(var0[var1]);
                var2.writeTo(var3, true);
                Tag.readFrom(new FileInputStream(var0[var1]), true, true);
                var4 = var6 - var0[var1].length();
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        System.err.println("[MIGRATION] MIGRATING META-DATA STRUCTURE DONE; files migrated: " + var0.length + "; Bytes cleaned: " + var4);
    }

    public static void handlePortError(String var0) {
        while(true) {
            var0 = (String)JOptionPane.showInputDialog(new JFrame(), var0 + "Some other program is blocking port " + ServerController.port + ". Please end that program or choose another port for starmade (Type a number from 1024 to 65535)", "Port Problem", -1, (Icon)null, (Object[])null, ServerController.port);

            try {
                if (var0 != null && var0.length() > 0) {
                    ServerController.port = Integer.parseInt(var0);
                    return;
                }

                handlePortError("invalid Port: '" + var0 + "'. ");
                return;
            } catch (NumberFormatException var1) {
                var0 = "invalid Port: '" + var0 + "'. ";
            }
        }
    }

    public static void initializeServer(boolean var0) throws SecurityException, IOException {
        DatabaseIndex.registerDriver();
        importCustom(var0);
    }

    public static void initialize(boolean var0) throws SecurityException, IOException {
        try {
            BlockShapeAlgorithm.initialize();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (var0) {
                GuiErrorHandler.processErrorDialogException(var3);
            } else {
                try {
                    throw new Exception("System.exit() called");
                } catch (Exception var2) {
                    var2.printStackTrace();
                    System.exit(0);
                }
            }
        }

        System.setProperty("hsqldb.reconfig_logging", "false");
        registerSerializableFactories();
        registerRemoteClasses();
        System.out.println("[INITIALIZE] REMOTE CLASSES REGISTERED");
        ResourceLoader.resourceUtil = new ResourceUtil();
        System.out.println("[INITIALIZE] RESOURCE MANAGER INITIALIZED");
        NetUtil.addCommandPath("org.schema.game.network.commands");
        System.out.println("[INITIALIZE] REMOTE COMMANDS REGISTERED");
        System.setSecurityManager((SecurityManager)null);
        System.out.println("[INITIALIZE] SECURITY MANAGER SET");
        NetUtil.assignCommandIds();
        System.out.println("[INITIALIZE] COMMAND ID's ASSIGNED. NOW READING BLOCK CONFIG");
        ElementKeyMap.initializeData(GameResourceLoader.getConfigInputFile());
        System.out.println("[INITIALIZE] BLOCK CONFIGURATION READ");
    }

    public static void importCustom(boolean var0) throws IOException {
        if (!importedCustom) {
            if (!(new FileExt(GameResourceLoader.CUSTOM_TEXTURE_PATH)).exists()) {
                GameResourceLoader.copyDefaultCustomTexturesTo(GameResourceLoader.CUSTOM_TEXTURE_PATH);
            }

            createCustomFactionConfig();
            createCustomBlockBehaviorConfig();
            createCustomEffectConfig();
            GameResourceLoader.createCustomTextureZip();
            if (!(new FileExt(GameResourceLoader.CUSTOM_CONFIG_IMPORT_PATH)).exists()) {
                GameResourceLoader.copyCustomConfig(GameResourceLoader.CUSTOM_CONFIG_IMPORT_PATH);
            }

            importedCustom = true;
        }

    }

    public static void main(String[] var0) throws IOException {
        try {
            EngineSettings.read();
        } catch (Exception var13) {
        }

        try {
            LogUtil.setUp((Integer)EngineSettings.LOG_FILE_COUNT.getCurrentState(), new CallInterace() {
                public final void call() {
                }
            });
        } catch (SecurityException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        OperatingSystem var1 = OperatingSystem.getOS();
        Version.loadVersion();
        System.out.println("#################### StarMade #######################");
        System.out.println("# version " + Version.VERSION + " - build " + Version.build + " #");
        System.out.println("#####################################################");
        System.out.println("[SERIAL] \"" + var1.serial + "\"");
        System.out.println("[INSTALLATION_DIRECTORY] \"" + (new FileExt("./")).getAbsolutePath() + "\"");
        if (Version.build.equals("latest")) {
            writeDefaultConfigs();
        }

        try {
            List var2 = ManagementFactory.getRuntimeMXBean().getInputArguments();
            System.out.println("[JVM-ARGUMENTS] " + var2);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        try {
            ObjectArrayList var19 = new ObjectArrayList();

            for(int var20 = 0; var20 < var0.length; ++var20) {
                if (var0[var20].startsWith("-auth")) {
                    try {
                        String[] var3 = var0[var20].split("\\s+");
                        var19.add(var3[0].trim());
                        var19.add(var3[1].trim());
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }
                } else {
                    var19.add(var0[var20]);
                }
            }

            String var4;
            String var5;
            File var21;
            if ((var21 = new File("testcredentials.properties")).exists() && !var19.contains("-auth")) {
                Properties var22 = new Properties();

                try {
                    FileInputStream var23 = new FileInputStream(var21);
                    var22.load(var23);
                    var4 = var22.getProperty("user");
                    var5 = var22.getProperty("password");
                    SessionNewStyle var25;
                    (var25 = new SessionNewStyle("test")).login(var4, var5);
                    var5 = var25.getToken();
                    var19.add("-auth");
                    var19.add(var5);
                    System.err.println("[STARTER] Test token used from test credentials (" + var4 + ")");
                    var23.close();
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
            }

            int var26 = var19.indexOf("-auth");
            boolean var24 = var19.contains("-gui");
            if (var19.contains("-cleanuptransient")) {
                System.out.println("WARNING: PLEASE CREATE A BACKUP BEFORE DOING THIS! DO YOU WANT TO PROCEED Y/N?");
                if ((new BufferedReader(new InputStreamReader(System.in))).readLine().equals("Y")) {
                    cleanUpDBForStations = true;
                }
            }

            EngineSettings.read();
            KeyboardMappings.read();
            if (var26 >= 0 && var26 + 1 < var19.size()) {
                var4 = (String)var19.get(var26 + 1);
                System.err.println("[STARTER] AUTH TOKEN SUBMITTED VIA ARGUMENT");
                SessionNewStyle var29;
                (var29 = new SessionNewStyle("general")).loginWithExistingToken(var4);
                currentSession = var29;
                System.err.println("[STARTER] RETRIEVED REGISTRY NAME: " + var29.getRegistryName() + "; ID: " + var29.getUserId());
                if (LoginRequest.isLoginNameValid(var29.getRegistryName())) {
                    if (EngineSettings.ONLINE_PLAYER_NAME.getCurrentState().toString().trim().isEmpty()) {
                        EngineSettings.ONLINE_PLAYER_NAME.setCurrentState(var29.getRegistryName());
                    }

                    if (EngineSettings.OFFLINE_PLAYER_NAME.getCurrentState().toString().trim().isEmpty()) {
                        EngineSettings.OFFLINE_PLAYER_NAME.setCurrentState(var29.getRegistryName());
                    }
                }
            }

            if (var19.contains("-server")) {
                importCustom(var24);
                LanguageManager.loadCurrentLanguage(true);
            }

            if (var19.contains("-server") && !var24) {
                hasGUI = false;
            }

            if (EngineSettings.FIRST_START.isOn()) {
                EngineSettings.FIRST_START.setCurrentState(false);
                EngineSettings.AUTOSET_RESOLUTION.setCurrentState(!(new FileExt("lpversion")).exists());
            }

            GameServerState.readDatabasePosition(hasGUI);
            if (EngineSettings.DELETE_SEVER_DATABASE_ON_STARTUP.isOn()) {
                cleanUniverseWithoutBackup();
            }

            if (!var19.contains("-server")) {
                loadLastUsedSkin();
            }

            GUIResizableGrabbableWindow.read();
            int var28 = -1;
            int var30;
            if ((var30 = var19.indexOf("-simd")) >= 0 && var30 + 1 < var19.size()) {
                try {
                    var28 = Integer.parseInt((String)var19.get(var30 + 1));
                } catch (NumberFormatException var7) {
                    System.out.println("WARNING: Error parsing -simd parameter");
                    var28 = -1;
                }
            }

            LibLoader.loadNativeLibs(var19.contains("-server"), var28, EngineSettings.USE_OPEN_AL_SOUND.isOn());
            if (var0.length == 0) {
                try {
                    throw new IllegalStateException("Sorry, it doesn't work this way!\nplease start the game with the StarMade-Starter you downloaded");
                } catch (Exception var6) {
                    GLFrame.processErrorDialogException(var6, (StateInterface)null);
                }
            } else {
                Iterator var27 = var19.iterator();

                while(true) {
                    do {
                        if (!var27.hasNext()) {
                            if (var19.contains("-cleansim")) {
                                forceSimClean = true;
                            }

                            System.out.println("[MAIN] CHECKING IF MIGRATION NECESSARY");
                            System.out.println("[MAIN] MIGRATION PROCESS DONE");
                            copyDefaultBB(var24);
                            if (var0.length > 0) {
                                System.out.println("[MAIN] STARTING WITH ARGUMENTS: " + Arrays.toString(var0));
                                if (var19.contains("-server")) {
                                    System.out.println("[MAIN] LOADED ENGINE SETTINGS");
                                    initializeServer(var24);
                                    initialize(var24);
                                    doMigration((JDialog)null, false);
                                    System.out.println("[MAIN] INITIALIZATION COMPLETED");
                                    DEDICATED_SERVER_ARGUMENT = true;
                                    MeshLoader.loadVertexBufferObject = false;
                                    if (Controller.getResLoader() == null) {
                                        Controller.initResLoader(new GameResourceLoader());
                                        Controller.getResLoader();
                                        ResourceLoader.dedicatedServer = true;
                                    }

                                    Controller.getResLoader().loadServer();
                                    startServer(var19.contains("-gui"));
                                    return;
                                }

                                if (var19.contains("-oldmenu")) {
                                    System.out.println("[MAIN] LOADED ENGINE SETTINGS");
                                    initializeServer(var24);
                                    initialize(var24);
                                    doMigration(new JDialog(), false);
                                    System.out.println("[MAIN] INITIALIZATION COMPLETED");
                                    LanguageManager.loadCurrentLanguage(false);
                                    clientStartup();
                                    return;
                                }

                                if (var19.contains("-locallastsettings")) {
                                    System.out.println("[MAIN] LOADED ENGINE SETTINGS");
                                    initializeServer(var24);
                                    initialize(var24);
                                    doMigration(new JDialog(), false);
                                    System.out.println("[MAIN] INITIALIZATION COMPLETED");
                                    LanguageManager.loadCurrentLanguage(false);
                                    if (var19.contains("-exitonesc")) {
                                        EngineSettings.S_EXIT_ON_ESC.setCurrentState(true);
                                    }

                                    startLocal();
                                    return;
                                }

                                if (var19.contains("-force")) {
                                    LanguageManager.loadCurrentLanguage(false);
                                    startMainMenu();
                                    return;
                                }

                                try {
                                    throw new IllegalStateException("Sorry, it doesn't work this way!\nplease start the game with the StarMade-Starter you downloaded.\nFor starting a GUI-less dedicated server, start with \"-server\"");
                                } catch (Exception var15) {
                                    if (var24) {
                                        GLFrame.processErrorDialogException(var15, (StateInterface)null);
                                        return;
                                    }

                                    var15.printStackTrace();
                                    return;
                                }
                            }

                            try {
                                throw new IllegalStateException("Sorry, it doesn't work this way!\nplease start the game with the StarMade-Starter you downloaded.\nFor starting a GUI-less dedicated server, start with \"-server\"");
                            } catch (Exception var14) {
                                if (var24) {
                                    GLFrame.processErrorDialogException(var14, (StateInterface)null);
                                    return;
                                }

                                var14.printStackTrace();
                                return;
                            }
                        }
                    } while(!(var5 = (String)var27.next()).startsWith("-port:"));

                    var5 = var5.replaceFirst("-port:", "");

                    try {
                        ServerController.port = Integer.parseInt(var5.trim());
                    } catch (NumberFormatException var17) {
                        NumberFormatException var31 = var17;

                        try {
                            throw new Exception("\"-port\" parameter malformed. please use a port number. (Example: -port:4242)", var31);
                        } catch (Exception var16) {
                            GLFrame.processErrorDialogException(var16, (StateInterface)null);
                        }
                    }
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        }
    }

    public static void copyDefaultBB(boolean var0) {
        (new FileExt(GameServerState.SEGMENT_DATA_DATABASE_PATH)).mkdirs();
        FileExt var1 = new FileExt("." + File.separator + "blueprints");
        FileExt var2;
        int var8;
        File[] var17;
        if ((var2 = new FileExt(GameServerState.ENTITY_BLUEPRINT_PATH_DEFAULT)).exists()) {
            File[] var4;
            int var5 = (var4 = var2.listFiles()).length;

            for(int var3 = 0; var3 < var5; ++var3) {
                File var6;
                if ((var6 = var4[var3]).isDirectory() && (var6.getName().equals("Isanth-VI") || var6.getName().equals("Isanth Type-Zero Mm") || var6.getName().equals("Isanth Type-Zero Bb") || var6.getName().equals("Isanth Type-Zero Bp") || var6.getName().equals("Isanth Type-Zero Cp") || var6.getName().equals("Isanth Type-Zero Mp"))) {
                    System.err.println("[START] Default blueprint migration. REMOVING DEFAULT INSANTH VARIANT " + var6.getName());
                    FileUtil.deleteDir(var6);
                } else if (var6.isDirectory()) {
                    File[] var7;
                    var8 = (var7 = var6.listFiles()).length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        if ((var6 = var7[var9]).isDirectory() && var6.getName().equals("DATA")) {
                            int var10 = (var17 = var6.listFiles()).length;

                            for(int var11 = 0; var11 < var10; ++var11) {
                                File var12;
                                if (!(var12 = var17[var11]).getName().startsWith("ENTITY")) {
                                    System.err.println("[START] Default blueprint migration. Deleting data file: " + var12.getName());
                                    var12.delete();
                                }
                            }
                        }
                    }
                }
            }
        }

        FileExt var14 = new FileExt("." + File.separator + "blueprints" + File.separator + "Isanth Type-Zero B-" + File.separator + "header.smbph");
        FileExt var15 = new FileExt("." + File.separator + GameServerState.ENTITY_BLUEPRINT_PATH_DEFAULT + File.separator + "Isanth Type-Zero B-" + File.separator + "header.smbph");
        boolean var16 = false;
        if (var14.exists() && var15.exists() && var14.length() != var15.length()) {
            var16 = true;
        }

        if ((new FileExt("." + File.separator + "blueprints" + File.separator + "Isanth Type-Zero B-" + File.separator + "DATA" + File.separator + "Isanth Type-Zero B-.0.0.0.smd3")).exists()) {
            var16 = true;
        }

        if (!var1.exists() || var16) {
            if (var1.exists() && var16) {
                int var18 = (var17 = var1.listFiles()).length;

                for(var8 = 0; var8 < var18; ++var8) {
                    File var19;
                    if ((var19 = var17[var8]).isDirectory() && (var19.getName().equals("Isanth-VI") || var19.getName().equals("Isanth Type-Zero Mm") || var19.getName().equals("Isanth Type-Zero Bb") || var19.getName().equals("Isanth Type-Zero Bp") || var19.getName().equals("Isanth Type-Zero Cp") || var19.getName().equals("Isanth Type-Zero Mp"))) {
                        System.err.println("[START] REMOVING INSANTH VARIANT " + var19.getName());
                        FileUtil.deleteDir(var19);
                    }

                    if (var16 && (var19.getName().equals("Isanth Type-Zero B-") || var19.getName().equals("Isanth Type-Zero Bc") || var19.getName().equals("Isanth Type-Zero Bm") || var19.getName().equals("Isanth Type-Zero C-") || var19.getName().equals("Isanth Type-Zero Cc") || var19.getName().equals("Isanth Type-Zero Cb") || var19.getName().equals("Isanth Type-Zero Cm") || var19.getName().equals("Isanth Type-Zero M-") || var19.getName().equals("Isanth Type-Zero Mb") || var19.getName().equals("Isanth Type-Zero Mc"))) {
                        System.err.println("[START] REMOVING INSANTH VARIANT (will be replaced) " + var19.getName());
                        FileUtil.deleteDir(var19);
                    }
                }
            }

            var1.mkdirs();
            if (var2.exists()) {
                try {
                    System.out.println("[START] no Blueprint files detected: COPYING DEFAULT BLUEPRINTS");
                    FileUtil.copyDirectory(new FileExt(GameServerState.ENTITY_BLUEPRINT_PATH_DEFAULT), new FileExt(GameServerState.ENTITY_BLUEPRINT_PATH));
                    return;
                } catch (IOException var13) {
                    var13.printStackTrace();
                    if (var0) {
                        GuiErrorHandler.processErrorDialogException(var13);
                    }

                    return;
                }
            }

            System.err.println("[START] no default blueprints detected");
        }

    }

    private static void createCustomEffectConfig() throws IOException {
        FileExt var0;
        if (!(var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH)).exists()) {
            System.err.println("[STARTER] custom effect config config dir does not exist. copying templates");
            var0.mkdir();
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfigTemplate.xml")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(ConfigPool.configPathTemplate), var0);
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfigHOWTO.txt")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(ConfigPool.configPathHOWTO), var0);
        } else {
            System.err.println("[STARTER] template dir exists. overwriting template with current data");
        }

        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfigTemplate.xml")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(ConfigPool.configPathTemplate), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfigHOWTO.txt")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(ConfigPool.configPathHOWTO), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfig.xml")).exists()) {
            (new FileExt(ConfigPool.configPathTemplate)).delete();
            FileUtil.copyFile(var0, new FileExt(ConfigPool.configPathTemplate));
            System.err.println("[STARTER] custom config file has been copied to config location" + (new FileExt(ConfigPool.configPathTemplate)).getAbsolutePath());
        }

    }

    private static void createCustomBlockBehaviorConfig() throws IOException {
        FileExt var0;
        if (!(var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH)).exists()) {
            System.err.println("[STARTER] custom block behavior config dir does not exist. copying templates");
            var0.mkdir();
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfigTemplate.xml")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(VoidElementManager.configPath), var0);
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfigHOWTO.txt")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(VoidElementManager.configPathHOWTO), var0);
        } else {
            System.err.println("[STARTER] template dir exists. overwriting template with current data");
        }

        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfigTemplate.xml")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(VoidElementManager.configPath), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfigHOWTO.txt")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(VoidElementManager.configPathHOWTO), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_BLOCK_BEHAVIOR_CONFIG_PATH + "customBlockBehaviorConfig.xml")).exists()) {
            (new FileExt(VoidElementManager.configPath)).delete();
            FileUtil.copyFile(var0, new FileExt(VoidElementManager.configPath));
            System.err.println("[STARTER] custom config file has been copied to config location" + (new FileExt(VoidElementManager.configPath)).getAbsolutePath());
        }

    }

    private static void createCustomFactionConfig() throws IOException {
        FileExt var0;
        if (!(var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH)).exists()) {
            System.err.println("[STARTER] custom faction config dir does not exist. copying templates");
            var0.mkdir();
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH + "FactionConfigTemplate.xml")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(FactionConfig.factionConfigPath), var0);
            if ((var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH + "customFactionConfigHOWTO.txt")).exists()) {
                var0.delete();
            }

            FileUtil.copyFile(new FileExt(FactionConfig.factionConfigPathHOWTO), var0);
        } else {
            System.err.println("[STARTER] template dir exists. overwriting template with current data");
        }

        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH + "FactionConfigTemplate.xml")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(FactionConfig.factionConfigPath), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH + "customFactionConfigHOWTO.txt")).exists()) {
            var0.delete();
        }

        FileUtil.copyFile(new FileExt(FactionConfig.factionConfigPathHOWTO), var0);
        if ((var0 = new FileExt(GameResourceLoader.CUSTOM_FACTION_CONFIG_PATH + "FactionConfig.xml")).exists()) {
            (new FileExt(FactionConfig.factionConfigPath)).delete();
            FileUtil.copyFile(var0, new FileExt(FactionConfig.factionConfigPath));
            System.err.println("[STARTER] custom config file has been copied to config location" + (new FileExt(FactionConfig.factionConfigPath)).getAbsolutePath());
        }

    }

    public static void registerRemoteClasses() {
        if (!registered) {
            ObjectArrayList var0;
            (var0 = new ObjectArrayList()).add(new SendableFactory<Ship>() {
                public final Ship getInstance(StateInterface var1) {
                    return new Ship(var1);
                }

                public final Class<Ship> getClazz() {
                    return Ship.class;
                }
            });
            var0.add(new SendableFactory<PlayerState>() {
                public final PlayerState getInstance(StateInterface var1) {
                    return new PlayerState(var1);
                }

                public final Class<PlayerState> getClazz() {
                    return PlayerState.class;
                }
            });
            var0.add(new SendableFactory<ChatSystem>() {
                public final ChatSystem getInstance(StateInterface var1) {
                    return new ChatSystem(var1);
                }

                public final Class<ChatSystem> getClazz() {
                    return ChatSystem.class;
                }
            });
            var0.add(new SendableFactory<PlayerCharacter>() {
                public final PlayerCharacter getInstance(StateInterface var1) {
                    return new PlayerCharacter(var1);
                }

                public final Class<PlayerCharacter> getClazz() {
                    return PlayerCharacter.class;
                }
            });
            var0.add(new SendableFactory<FloatingRockManaged>() {
                public final FloatingRockManaged getInstance(StateInterface var1) {
                    return new FloatingRockManaged(var1);
                }

                public final Class<FloatingRockManaged> getClazz() {
                    return FloatingRockManaged.class;
                }
            });
            var0.add(new SendableFactory<FloatingRock>() {
                public final FloatingRock getInstance(StateInterface var1) {
                    return new FloatingRock(var1);
                }

                public final Class<FloatingRock> getClazz() {
                    return FloatingRock.class;
                }
            });
            var0.add(new SendableFactory<ShopSpaceStation>() {
                public final ShopSpaceStation getInstance(StateInterface var1) {
                    return new ShopSpaceStation(var1);
                }

                public final Class<ShopSpaceStation> getClazz() {
                    return ShopSpaceStation.class;
                }
            });
            var0.add(new SendableFactory<SendableSegmentProvider>() {
                public final SendableSegmentProvider getInstance(StateInterface var1) {
                    return new SendableSegmentProvider(var1);
                }

                public final Class<SendableSegmentProvider> getClazz() {
                    return SendableSegmentProvider.class;
                }
            });
            var0.add(new SendableFactory<CharacterProvider>() {
                public final CharacterProvider getInstance(StateInterface var1) {
                    return new CharacterProvider(var1);
                }

                public final Class<CharacterProvider> getClazz() {
                    return CharacterProvider.class;
                }
            });
            var0.add(new SendableFactory<SpaceCreatureProvider>() {
                public final SpaceCreatureProvider getInstance(StateInterface var1) {
                    return new SpaceCreatureProvider(var1);
                }

                public final Class<SpaceCreatureProvider> getClazz() {
                    return SpaceCreatureProvider.class;
                }
            });
            var0.add(new SendableFactory<FixedSpaceEntityProvider>() {
                public final FixedSpaceEntityProvider getInstance(StateInterface var1) {
                    return new FixedSpaceEntityProvider(var1);
                }

                public final Class<FixedSpaceEntityProvider> getClazz() {
                    return FixedSpaceEntityProvider.class;
                }
            });
            var0.add(new SendableFactory<SendableSegmentProvider>() {
                public final SendableSegmentProvider getInstance(StateInterface var1) {
                    return new SendableSegmentProvider(var1);
                }

                public final Class<SendableSegmentProvider> getClazz() {
                    return SendableSegmentProvider.class;
                }
            });
            var0.add(new SendableFactory<SendableGameState>() {
                public final SendableGameState getInstance(StateInterface var1) {
                    return new SendableGameState(var1);
                }

                public final Class<SendableGameState> getClazz() {
                    return SendableGameState.class;
                }
            });
            var0.add(new SendableFactory<SpaceStation>() {
                public final SpaceStation getInstance(StateInterface var1) {
                    return new SpaceStation(var1);
                }

                public final Class<SpaceStation> getClazz() {
                    return SpaceStation.class;
                }
            });
            var0.add(new SendableFactory<Vehicle>() {
                public final Vehicle getInstance(StateInterface var1) {
                    return new Vehicle(var1);
                }

                public final Class<Vehicle> getClazz() {
                    return Vehicle.class;
                }
            });
            var0.add(new SendableFactory<Planet>() {
                public final Planet getInstance(StateInterface var1) {
                    return new Planet(var1);
                }

                public final Class<Planet> getClazz() {
                    return Planet.class;
                }
            });
            var0.add(new SendableFactory<RemoteSector>() {
                public final RemoteSector getInstance(StateInterface var1) {
                    return new RemoteSector(var1);
                }

                public final Class<RemoteSector> getClazz() {
                    return RemoteSector.class;
                }
            });
            var0.add(new SendableFactory<ClientChannel>() {
                public final ClientChannel getInstance(StateInterface var1) {
                    return new ClientChannel(var1);
                }

                public final Class<ClientChannel> getClazz() {
                    return ClientChannel.class;
                }
            });
            var0.add(new SendableFactory<AICharacter>() {
                public final AICharacter getInstance(StateInterface var1) {
                    return new AICharacter(var1);
                }

                public final Class<AICharacter> getClazz() {
                    return AICharacter.class;
                }
            });
            var0.add(new SendableFactory<AIRandomCompositeCreature>() {
                public final AIRandomCompositeCreature getInstance(StateInterface var1) {
                    return new AIRandomCompositeCreature(var1);
                }

                public final Class<AIRandomCompositeCreature> getClazz() {
                    return AIRandomCompositeCreature.class;
                }
            });
            var0.add(new SendableFactory<PlanetCore>() {
                public final PlanetCore getInstance(StateInterface var1) {
                    return new PlanetCore(var1);
                }

                public final Class<PlanetCore> getClazz() {
                    return PlanetCore.class;
                }
            });
            var0.add(new SendableFactory<SpaceCreature>() {
                public final SpaceCreature getInstance(StateInterface var1) {
                    return new SpaceCreature(var1);
                }

                public final Class<SpaceCreature> getClazz() {
                    return SpaceCreature.class;
                }
            });
            var0.add(new SendableFactory<PlanetIco>() {
                public final PlanetIco getInstance(StateInterface var1) {
                    return new PlanetIco(var1);
                }

                public final Class<PlanetIco> getClazz() {
                    return PlanetIco.class;
                }
            });
            Iterator var1 = var0.iterator();

            while(var1.hasNext()) {
                NetUtil.addSendableClass((SendableFactory)var1.next());
            }

            modManager.registerNetworkClasses();
            modManager.registerRemoteClasses();
            registered = true;
        }

    }

    public static void registerSerializableFactories() {
        SerializableTagRegister.register = new SerializableTagFactory[]{new ControlElementMapperFactory(), new ElementCountMapFactory(), new NPCFactionNewsEventFactory(), new TagSerializableLongSetFactory(), new BlockBufferFactory(), new TagSerializableLong2Vector3fMapFactory(), new TagSerializableLong2TransformMapFactory()};
    }

    public static void stopClient(GraphicsContext var0) {
        if (clientRunnable != null) {
            if (var0 != null) {
                var0.setLoadMessage("Stopping current client");
            }

            clientRunnable.stopClient();
            clientRunnable = null;
        }

    }

    //REPLACE METHOD
    public static void startClient(HostPortLoginName server, boolean startConnectDialog, GraphicsContext context) {
        String loginName = server.host;
        DebugFile.log("Connecting to server: " + loginName);
        boolean allUptoDate = ModStarter.preClientConnect(loginName, server.port);
        if(allUptoDate) {
            stopClient(context);
            clientRunnable = new ClientRunnable(server, startConnectDialog, context);
            Thread var3;

            (var3 = new Thread(clientRunnable, "ClientThread")).setPriority(8);
            GameClientController.availableGUI = true;
            System.err.println("[Starloader] Start client thread");
            var3.start();
        }else{
            System.err.println("[Starloader] Not all mods up to date, not starting");
        }
    }
    ///

    public static void startLocal() {
        startServer(false);
        startClient(new HostPortLoginName("localhost", 4242, (byte)0, (String)null), true, (GraphicsContext)null);
    }

    public static void startMainMenu() {
        System.err.println("[CLIENT][STARTUP] Starting main menu");
        GameMainMenuController var0 = new GameMainMenuController();

        try {
            System.err.println("[CLIENT][STARTUP] Starting Graphics");
            var0.startGraphics();
        } catch (FileNotFoundException var1) {
            var1.printStackTrace();
        } catch (ResourceException var2) {
            var2.printStackTrace();
        } catch (ParseException var3) {
            var3.printStackTrace();
        } catch (SAXException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        } catch (ParserConfigurationException var6) {
            var6.printStackTrace();
        }
    }

    public static void startServer(boolean var0) {
        serverInitFinished = false;
        //INSERTED CODE @1613
        ModStarter.preServerStart();
        ///
        (new Thread(getServerRunnable(var0), "ServerThread")).start();
    }

    public static Runnable getServerRunnable(final boolean var0) {
        return new Runnable() {
            public final void run() {
                boolean var1 = false;
                boolean var18 = false;

                label390: {
                    try {
                        Object var2;
                        try {
                            var18 = true;
                            System.err.println("[SERVER] initializing ");
                            ServerState.setShutdown(false);
                            Controller.initResLoader(new GameResourceLoader());
                            GameServerState var44 = new GameServerState();
                            final GameServerController var3 = new GameServerController(var44);
                            if (Starter.cleanUpDBForStations) {
                                try {
                                    System.out.println("--------------------------------------");
                                    System.out.println("--------DATABASE CLEANUP START--------");
                                    System.out.println("--------------------------------------");
                                    System.out.println("CLEANUP TARGET " + GameServerState.ENTITY_DATABASE_PATH);
                                    (new StationAndShipTransienceMigration()).convertDatabase(GameServerState.ENTITY_DATABASE_PATH);
                                    System.out.println("--------------------------------------");
                                    System.out.println("--------DATABASE CLEANUP FINISHED-----");
                                    System.out.println("------------SYSTEM WILL EXIT----------");
                                    System.out.println("--------------------------------------");
                                } catch (Exception var39) {
                                    var39.printStackTrace();
                                } finally {
                                    try {
                                        throw new Exception("System.exit() called");
                                    } catch (Exception var35) {
                                        var35.printStackTrace();
                                        System.exit(0);
                                    }
                                }
                            }

                            Starter.sGUI = null;
                            if (var0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        (Starter.sGUI = new ServerGUI(var3)).setVisible(true);
                                    }
                                });
                            }

                            Starter.serverUp = false;
                            var3.startServerAndListen();

                            while(!var3.isListenting()) {
                                try {
                                    Thread.sleep(30L);
                                } catch (InterruptedException var38) {
                                    var38.printStackTrace();
                                }
                            }

                            Starter.serverUp = true;
                            if (Starter.DEDICATED_SERVER_ARGUMENT && (Boolean)ServerConfig.SECURE_UPLINK_ENABLED.getCurrentState()) {
                                ServerConfig.SECURE_UPLINK_TOKEN.getCurrentState();
                            }

                            FileExt var4;
                            if ((var4 = new FileExt("./debugPlayerLock.lock")).exists()) {
                                var4.delete();
                            }

                            var4.createNewFile();
                            DataOutputStream var43 = new DataOutputStream(new FileOutputStream(var4));
                            synchronized(var44.getClients()) {
                                var43.writeInt(var44.getClients().size());
                            }

                            var43.close();
                            var18 = false;
                            break label390;
                        } catch (Exception var41) {
                            var2 = var41;
                            if (Starter.sGUI != null) {
                                Starter.sGUI.setVisible(false);
                            }
                            if (var41 instanceof ServerPortNotAvailableException) {
                                var2 = new Exception(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_STARTER_8, new Object[]{ServerController.port}), var41);
                            }
                        }



                        if ((var2 instanceof SQLException || var2 instanceof HsqlException) && ((Exception)var2).getMessage() != null && ((Exception)var2).getMessage().contains("data file is modified but backup file does not exist")) {
                            var2 = new SQLException("You last session of StarMade before updating to the new hsql library wasn't shutdown correctly, so the restore files are incompatible.\nPlease download http://files.star-made.org/build/starmade-build_20160924_005306/lib/hsqldb.jar and place it in /lib. Run the game once and exit without a forced shutdown.\nThen do a starmade update again to revert the lib to the new one again.", (Throwable)var2);
                        }

                        Starter.startupException = (Exception)var2;
                        var1 = true;
                        ((Exception)var2).printStackTrace();
                        var18 = false;
                    } finally {
                        if (var18) {
                            if (!var1) {
                                System.err.println("[SERVER] SERVER INIT FINISHED");
                                Starter.serverInitFinished = true;
                                synchronized(Starter.serverLock) {
                                    Starter.serverLock.notify();
                                }
                            } else {
                                ServerState.setShutdown(true);
                                synchronized(Starter.serverLock) {
                                    Starter.serverLock.notify();
                                }
                            }

                        }
                    }

                    ServerState.setShutdown(true);
                    synchronized(Starter.serverLock) {
                        Starter.serverLock.notify();
                        return;
                    }
                }

                System.err.println("[SERVER] SERVER INIT FINISHED");
                Starter.serverInitFinished = true;
                synchronized(Starter.serverLock) {
                    Starter.serverLock.notify();
                }
            }
        };
    }

    public static void loadLastUsedSkin() throws IOException {
        FileExt var0;
        if ((var0 = new FileExt("./.skin")).exists()) {
            BufferedReader var3 = new BufferedReader(new FileReader(var0));

            String var1;
            String var2;
            for(var2 = null; (var1 = var3.readLine()) != null; var2 = new String(var1)) {
            }

            var3.close();
            if (var2 != null) {
                EngineSettings.PLAYER_SKIN.setCurrentState(var2);
            }
        }

    }

    public static int getAuthStyle() {
        if (authStyle < 0) {
            if ((Integer)EngineSettings.A_FORCE_AUTHENTICATION_METHOD.getCurrentState() >= 0) {
                authStyle = (Integer)EngineSettings.A_FORCE_AUTHENTICATION_METHOD.getCurrentState();
                System.err.println("[INIT] set forced auth style: " + authStyle);
            } else {
                HttpURLConnection var0;
                try {
                    URL var10000 = new URL("http://files.star-made.org/auth_method");
                    var0 = null;
                    (var0 = (HttpURLConnection)var10000.openConnection()).setReadTimeout(10000);
                    BufferedReader var4 = new BufferedReader(new InputStreamReader(var0.getInputStream()));
                    StringBuffer var2 = new StringBuffer();

                    String var1;
                    while((var1 = var4.readLine()) != null) {
                        var2.append(var1);
                    }

                    var4.close();
                    authStyle = Integer.parseInt(var2.toString());
                } catch (Exception var3) {
                    var0 = null;
                    var3.printStackTrace();
                    authStyle = 1;
                }

                System.err.println("[INIT] Retrieved auth style: " + authStyle);
            }
        }

        return authStyle;
    }

    private void getFromHostAndPortConnectionDialog() {
        try {
            this.getLastFromSavedList();
        } catch (Exception var2) {
            System.err.println("Catched Exception loading server entries from last");
            var2.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ClientDialog var1;
                (var1 = new ClientDialog(Starter.this, Starter.this.history)).setDefaultCloseOperation(3);
                var1.setVisible(true);
                String[] var2 = (new FileExt("./")).list();
                boolean var3 = false;
                int var4 = (var2 = var2).length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String var6;
                    if ((var6 = var2[var5]).startsWith("hs_err_pid") && var6.endsWith(".log")) {
                        var3 = true;
                        (new FileExt(var6)).renameTo(new FileExt("./logs/" + var6));
                    }
                }

                if (var3) {
                    String var9 = System.getProperty("sun.arch.data.model");
                    String var10 = System.getProperty("os.arch");
                    if (var9 != null && var9.contains("32")) {
                        if (var10 != null && var10.contains("64")) {
                            if (JOptionPane.showOptionDialog(var1, "You are running 32-bit java. Memory will be very limited, and will likely cause issues.\nPlease upgrade to 64-bit java!", "JVM Crash detected", 0, 1, (Icon)null, new String[]{"Download", "Ok"}, "Download") == 0) {
                                try {
                                    Desktop.getDesktop().browse(URI.create("https://java.com/download"));
                                    return;
                                } catch (IOException var7) {
                                    var7.printStackTrace();
                                }
                            }

                            return;
                        }
                    } else if (JOptionPane.showOptionDialog(var1, "If you have another graphics-card in your computer, tell your computer to execute java via GPU only. (In the case of NVidia cards you can do this in the \"NVidia Control center\")\nAdding \"javaw.exe\" to program list in control center and setting it to 'GPU only' usually fixes this.\n\nYou may also visit the site of the manufacturer of your graphics-card and get the latest beta drivers for your card.\n\nAlso please install the 64bit Java Development Kit (JDK) some intel drivers seem\nto link against extended java functions the default Java Runtime Environment (JRE) does not support.", "JVM Crash detected", 0, 1, (Icon)null, new String[]{"Ok", "Download JDK"}, "Ok") == 1) {
                        try {
                            Desktop.getDesktop().browse(new URI("http://tiny.cc/66t12x"));
                            return;
                        } catch (Exception var8) {
                            var8.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    private void getLastFromSavedList() throws Exception {
        FileExt var1;
        if (!(var1 = new FileExt("./.sessions")).exists()) {
            throw new FileNotFoundException();
        } else {
            String var2;
            BufferedReader var5;
            HostPortLoginName var8;
            for(var5 = new BufferedReader(new FileReader(var1)); (var2 = var5.readLine()) != null; this.history.add(var8)) {
                String[] var6;
                String var3 = (var6 = var2.split(",", 21))[0];
                String var4 = (var6 = var6[1].split(":", 2))[0];
                int var7 = Integer.parseInt(var6[1]);
                var8 = new HostPortLoginName(var4, var7, (byte)0, var3);
                if (this.history.contains(var8)) {
                    this.history.remove(var8);
                }
            }

            var5.close();
        }
    }

    public void update(Observable var1, Object var2) {
        if (var2 instanceof HostPortLoginName) {
            HostPortLoginName var3 = (HostPortLoginName)var2;
            if (this.history.contains(var3)) {
                this.history.remove(var3);
            }

            this.history.add(var3);
            this.updateSavedList(this.history);
            startClient(var3, true, (GraphicsContext)null);
        } else if (var2 instanceof Exception) {
            System.err.println("CATCHED EXCEPTION");
        } else {
            if (var2 == null) {
                serverInitFinished = false;
                startServer(false);
            }

        }
    }

    private void updateSavedList(ArrayList<HostPortLoginName> var1) {
        try {
            FileExt var2;
            if (!(var2 = new FileExt("./.sessions")).exists()) {
                var2.createNewFile();
            }

            BufferedWriter var6 = new BufferedWriter(new FileWriter(var2));
            Iterator var5 = var1.iterator();

            while(var5.hasNext()) {
                HostPortLoginName var3 = (HostPortLoginName)var5.next();
                var6.append(var3.loginName + "," + var3.host + ":" + var3.port + "\n");
            }

            var6.flush();
            var6.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
