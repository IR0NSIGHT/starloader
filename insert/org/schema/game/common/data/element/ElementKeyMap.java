//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.element;

import api.ModPlayground;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.schema.common.ParseException;
import org.schema.game.client.view.cubes.shapes.BlockStyle;
import org.schema.game.common.Starter;
import org.schema.game.common.data.element.ElementInformation.ResourceInjectionType;
import org.schema.game.common.data.element.ship.ShipElement;
import org.schema.game.common.data.element.spacestation.SpaceStationElement;
import org.schema.game.common.updater.FileUtil;
import org.schema.game.common.util.GuiErrorHandler;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.resource.FileExt;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ElementKeyMap {
    public static final Short2ObjectOpenHashMap<String> nameTranslations = new Short2ObjectOpenHashMap();
    public static final Short2ObjectOpenHashMap<String> descriptionTranslations = new Short2ObjectOpenHashMap();
    public static final short WEAPON_CONTROLLER_ID = 6;
    public static final short WEAPON_ID = 16;
    public static final short CORE_ID = 1;
    public static final short DEATHSTAR_CORE_ID = 65;
    public static final short HULL_ID = 5;
    public static final short GLASS_ID = 63;
    public static final short THRUSTER_ID = 8;
    public static final short TURRET_DOCK_ID = 7;
    public static final short TURRET_DOCK_ENHANCE_ID = 88;
    public static final short POWER_ID_OLD = 2;
    public static final short POWER_CAP_ID = 331;
    public static final short SHIELD_CAP_ID = 3;
    public static final short SHIELD_REGEN_ID = 478;
    public static final short EXPLOSIVE_ID = 14;
    public static final short RADAR_JAMMING_ID = 15;
    public static final short CLOAKING_ID = 22;
    public static final short SALVAGE_ID = 24;
    public static final short MISSILE_DUMB_CONTROLLER_ID = 38;
    public static final short MISSILE_DUMB_ID = 32;
    public static final short SHIELD_DRAIN_CONTROLLER_ID = 46;
    public static final short SHIELD_DRAIN_MODULE_ID = 40;
    public static final short SHIELD_SUPPLY_CONTROLLER_ID = 54;
    public static final short SHIELD_SUPPLY_MODULE_ID = 48;
    public static final short SALVAGE_CONTROLLER_ID = 4;
    public static final short GRAVITY_ID = 56;
    public static final short REPAIR_ID = 30;
    public static final short REPAIR_CONTROLLER_ID = 39;
    public static final short COCKPIT_ID = 47;
    public static final short LIGHT_ID = 55;
    public static final short TERRAIN_ICE_ID = 64;
    public static final short HULL_COLOR_PURPLE_ID = 69;
    public static final short HULL_COLOR_BROWN_ID = 70;
    public static final short HULL_COLOR_BLACK_ID = 75;
    public static final short HULL_COLOR_RED_ID = 76;
    public static final short HULL_COLOR_BLUE_ID = 77;
    public static final short HULL_COLOR_GREEN_ID = 78;
    public static final short HULL_COLOR_YELLOW_ID = 79;
    public static final short HULL_COLOR_WHITE_ID = 81;
    public static final short LANDING_ELEMENT = 112;
    public static final short LIFT_ELEMENT = 113;
    public static final short RECYCLER_ELEMENT = 114;
    public static final short STASH_ELEMENT = 120;
    public static final short AI_ELEMENT = 121;
    public static final short DOOR_ELEMENT = 122;
    public static final short BUILD_BLOCK_ID = 123;
    public static final short TERRAIN_LAVA_ID = 80;
    public static final short TERRAIN_GOLD_ID = 128;
    public static final short TERRAIN_IRIDIUM_ID = 129;
    public static final short TERRAIN_MERCURY_ID = 130;
    public static final short TERRAIN_PALLADIUM_ID = 131;
    public static final short TERRAIN_PLATINUM_ID = 132;
    public static final short TERRAIN_LITHIUM_ID = 133;
    public static final short TERRAIN_MAGNESIUM_ID = 134;
    public static final short TERRAIN_TITANIUM_ID = 135;
    public static final short TERRAIN_URANIUM_ID = 136;
    public static final short TERRAIN_POLONIUM_ID = 137;
    public static final short TERRAIN_EXTRANIUM_ID = 72;
    public static final short TERRAIN_INSANIUNM_ID = 210;
    public static final short TERRAIN_METATE_ID = 209;
    public static final short TERRAIN_NEGAGATE_ID = 208;
    public static final short TERRAIN_QUANTACIDE_ID = 207;
    public static final short TERRAIN_NEGACIDE_ID = 206;
    public static final short TERRAIN_MARS_TOP = 138;
    public static final short TERRAIN_MARS_DIRT = 140;
    public static final short TERRAIN_ROCK_NORMAL = 73;
    public static final short TERRAIN_ROCK_MARS = 139;
    public static final short TERRAIN_ROCK_BLUE = 143;
    public static final short TERRAIN_ROCK_ORANGE = 151;
    public static final short TERRAIN_ROCK_YELLOW = 155;
    public static final short TERRAIN_ROCK_WHITE = 159;
    public static final short TERRAIN_ROCK_PURPLE = 163;
    public static final short TERRAIN_ROCK_RED = 171;
    public static final short TERRAIN_ROCK_GREEN = 179;
    public static final short TERRAIN_ROCK_BLACK = 203;
    public static final short TERRAIN_SAND_ID = 74;
    public static final short TERRAIN_EARTH_TOP_DIRT = 82;
    public static final short TERRAIN_EARTH_TOP_ROCK = 83;
    public static final short TERRAIN_TREE_TRUNK_ID = 84;
    public static final short TERRAIN_TREE_LEAF_ID = 85;
    public static final short TERRAIN_WATER = 86;
    public static final short TERRAIN_DIRT_ID = 87;
    public static final short TERRAIN_VINES_ID = 85;
    public static final short TERRAIN_CACTUS_ID = 89;
    public static final short TERRAIN_PURPLE_ALIEN_TOP = 90;
    public static final short TERRAIN_PURPLE_ALIEN_ROCK = 91;
    public static final short TERRAIN_PURPLE_ALIEN_VINE = 92;
    public static final short WATER = 86;
    public static final short PLAYER_SPAWN_MODULE = 94;
    public static final short LIGHT_BULB_YELLOW = 340;
    public static final short TERRAIN_FLOWERS_BLUE_SPRITE = 93;
    public static final short TERRAIN_GRASS_LONG_SPRITE = 98;
    public static final short TERRAIN_BERRY_BUSH_SPRITE = 102;
    public static final short TERRAIN_FLOWERS_YELLOW_SPRITE = 106;
    public static final short TERRAIN_CACTUS_SMALL_SPRITE = 95;
    public static final short TERRAIN_CACTUS_ARCHED_SPRITE = 103;
    public static final short TERRAIN_FLOWERS_DESERT_SPRITE = 99;
    public static final short TERRAIN_ROCK_SPRITE = 107;
    public static final short TERRAIN_CORAL_RED_SPRITE = 96;
    public static final short TERRAIN_SHROOM_RED_SPRITE = 104;
    public static final short TERRAIN_FUNGAL_GROWTH_SPRITE = 100;
    public static final short TERRAIN_FUNGAL_TRAP_SPRITE = 108;
    public static final short TERRAIN_FLOWER_FAN_PURPLE_SPRITE = 97;
    public static final short TERRAIN_GLOW_TRAP_SPRITE = 101;
    public static final short TERRAIN_WEEDS_PURPLE_SPRITE = 105;
    public static final short TERRAIN_YHOLE_PURPLE_SPRITE = 109;
    public static final short TERRAIN_FAN_FLOWER_ICE_SPRITE = 278;
    public static final short TERRAIN_ICE_CRAG_SPRITE = 279;
    public static final short TERRAIN_CORAL_ICE_SPRITE = 280;
    public static final short TERRAIN_SNOW_BUD_SPRITE = 281;
    public static final short FACTORY_BASIC_ID = 211;
    public static final short FACTORY_STANDARD_ID = 217;
    public static final short FACTORY_ADVANCED_ID = 259;
    public static final short FACTORY_INPUT_ENH_ID = 212;
    public static final short FACTORY_CAPSULE_ASSEMBLER_ID = 213;
    public static final short FACTORY_ENH_UNUSED_ID = 214;
    public static final short FACTORY_MICRO_ASSEMBLER_ID = 215;
    public static final short FACTORY_POWER_COIL_ENH_ID = 216;
    public static final short FACTORY_POWER_BLOCK_ENH_ID = 218;
    public static final short TERRAIN_ICEPLANET_SURFACE = 274;
    public static final short TERRAIN_ICEPLANET_ROCK = 275;
    public static final short TERRAIN_ICEPLANET_WOOD = 276;
    public static final short TERRAIN_ICEPLANET_LEAVES = 277;
    public static final short LIGHT_RED = 282;
    public static final short LIGHT_BLUE = 283;
    public static final short LIGHT_GREEN = 284;
    public static final short LIGHT_YELLOW = 285;
    public static final short TERRAIN_ICEPLANET_CRYSTAL = 286;
    public static final short TERRAIN_REDWOOD = 287;
    public static final short TERRAIN_REDWOOD_LEAVES = 288;
    public static final short FIXED_DOCK_ID = 289;
    public static final short FIXED_DOCK_ID_ENHANCER = 290;
    public static final short FACTION_BLOCK = 291;
    public static final short FACTION_HUB_BLOCK = 292;
    public static final short DECORATIVE_PANEL_1 = 336;
    public static final short DECORATIVE_PANEL_2 = 337;
    public static final short DECORATIVE_PANEL_3 = 338;
    public static final short DECORATIVE_PANEL_4 = 339;
    public static final short POWER_CELL = 219;
    public static final short POWER_COIL = 220;
    public static final short POWER_DRAIN_BEAM_COMPUTER = 332;
    public static final short POWER_DRAIN_BEAM_MODULE = 333;
    public static final short POWER_SUPPLY_BEAM_COMPUTER = 334;
    public static final short POWER_SUPPLY_BEAM_MODULE = 335;
    public static final short PUSH_PULSE_CONTROLLER_ID = 344;
    public static final short PUSH_PULSE_ID = 345;
    public static final short FACTION_PUBLIC_EXCEPTION_ID = 346;
    public static final short FACTION_FACTION_EXCEPTION_ID = 936;
    public static final short SHOP_BLOCK_ID = 347;
    public static final short ACTIVAION_BLOCK_ID = 405;
    public static final short SIGNAL_DELAY_NON_REPEATING_ID = 406;
    public static final short SIGNAL_DELAY_BLOCK_ID = 407;
    public static final short SIGNAL_AND_BLOCK_ID = 408;
    public static final short SIGNAL_OR_BLOCK_ID = 409;
    public static final short SIGNAL_NOT_BLOCK_ID = 410;
    public static final short SIGNAL_TRIGGER_AREA = 411;
    public static final short SIGNAL_TRIGGER_STEPON = 412;
    public static final short SIGNAL_TRIGGER_AREA_CONTROLLER = 413;
    public static final short DAMAGE_BEAM_COMPUTER = 414;
    public static final short DAMAGE_BEAM_MODULE = 415;
    public static final short DAMAGE_PULSE_COMPUTER = 416;
    public static final short DAMAGE_PULSE_MODULE = 417;
    public static final short EFFECT_PIERCING_COMPUTER = 418;
    public static final short EFFECT_PIERCING_MODULE = 419;
    public static final short EFFECT_EXPLOSIVE_COMPUTER = 420;
    public static final short EFFECT_EXPLOSIVE_MODULE = 421;
    public static final short EFFECT_PUNCHTHROUGH_COMPUTER = 422;
    public static final short EFFECT_PUNCHTHROUGH_MODULE = 423;
    public static final short EFFECT_EMP_COMPUTER = 424;
    public static final short EFFECT_EMP_MODULE = 425;
    public static final short EFFECT_STOP_COMPUTER = 460;
    public static final short EFFECT_STOP_MODULE = 461;
    public static final short EFFECT_PUSH_COMPUTER = 462;
    public static final short EFFECT_PUSH_MODULE = 463;
    public static final short EFFECT_PULL_COMPUTER = 464;
    public static final short EFFECT_PULL_MODULE = 465;
    public static final short EFFECT_ION_COMPUTER = 466;
    public static final short EFFECT_ION_MODULE = 467;
    public static final short EFFECT_OVERDRIVE_COMPUTER = 476;
    public static final short EFFECT_OVERDRIVE_MODULE = 477;
    public static final short TEXT_BOX = 479;
    public static final short WARP_GATE_CONTROLLER = 542;
    public static final short WARP_GATE_MODULE = 543;
    public static final short JUMP_DRIVE_CONTROLLER = 544;
    public static final short JUMP_DRIVE_MODULE = 545;
    public static final short MEDICAL_SUPPLIES = 445;
    public static final short MEDICAL_CABINET = 446;
    public static final short SCRAP_ALLOYS = 546;
    public static final short SCRAP_COMPOSITE = 547;
    public static final short SCANNER_COMPUTER = 654;
    public static final short SCANNER_MODULE = 655;
    public static final short METAL_MESH = 440;
    public static final short CRYSTAL_CRIRCUITS = 220;
    public static final short LOGIC_BUTTON_NORM = 666;
    public static final short LOGIC_FLIP_FLOP = 667;
    public static final short LOGIC_WIRELESS = 668;
    public static final short SHIPYARD_COMPUTER = 677;
    public static final short SHIPYARD_MODULE = 678;
    public static final short SHIPYARD_CORE_POSITION = 679;
    public static final short REPULSE_MODULE = 1126;
    public static final short JUMP_INHIBITOR_COMPUTER = 681;
    public static final short JUMP_PROHIBITER_MODULE = 682;
    public static final int Hattel = 1;
    public static final int Sintyr = 2;
    public static final int Mattise = 3;
    public static final int Rammet = 4;
    public static final int Varat = 5;
    public static final int Bastyn = 6;
    public static final int Parsen = 7;
    public static final int Nocx = 8;
    public static final int Threns = 9;
    public static final int Jisper = 10;
    public static final int Zercaner = 11;
    public static final int Sertise = 12;
    public static final int Hital = 13;
    public static final int Fertikeen = 14;
    public static final int Parstun = 15;
    public static final int Nacht = 16;
    public static final short CRYS_RAMMET = 452;
    public static final short CRYS_NOCX = 453;
    public static final short CRYS_PARSEEN = 454;
    public static final short CRYS_HATTEL = 455;
    public static final short CRYS_MATTISE = 456;
    public static final short CRYS_SINTYR = 457;
    public static final short CRYS_BASTYN = 458;
    public static final short CRYS_VARAT = 459;
    public static final short RESS_CRYS_HATTEL = 480;
    public static final short RESS_CRYS_SINTYR = 481;
    public static final short RESS_CRYS_MATTISE = 482;
    public static final short RESS_CRYS_RAMMET = 483;
    public static final short RESS_CRYS_VARAT = 484;
    public static final short RESS_CRYS_BASTYN = 485;
    public static final short RESS_CRYS_PARSEN = 486;
    public static final short RESS_CRYS_NOCX = 487;
    public static final short RESS_ORE_THRENS = 488;
    public static final short RESS_ORE_JISPER = 489;
    public static final short RESS_ORE_ZERCANER = 490;
    public static final short RESS_ORE_SERTISE = 491;
    public static final short RESS_ORE_HITAL = 492;
    public static final short RESS_ORE_FERTIKEEN = 493;
    public static final short RESS_ORE_PARSTUN = 494;
    public static final short RESS_ORE_NACHT = 495;
    public static final short RAIL_BLOCK_BASIC = 662;
    public static final short RAIL_BLOCK_DOCKER = 663;
    public static final short RAIL_BLOCK_CW = 664;
    public static final short RAIL_BLOCK_CCW = 669;
    public static final short RAIL_BLOCK_TURRET_Y_AXIS = 665;
    public static final short RAIL_RAIL_SPEED_CONTROLLER = 672;
    public static final short RAIL_MASS_ENHANCER = 671;
    public static final short RAIL_LOAD = 1104;
    public static final short RAIL_UNLOAD = 1105;
    public static final short LOGIC_REMOTE_INNER = 670;
    public static final short RACE_GATE_CONTROLLER = 683;
    public static final short RACE_GATE_MODULE = 684;
    public static final short ACTIVATION_GATE_CONTROLLER = 685;
    public static final short ACTIVATION_GATE_MODULE = 686;
    public static final short TRANSPORTER_CONTROLLER = 687;
    public static final short TRANSPORTER_MODULE = 688;
    public static final short CARGO_SPACE = 689;
    public static final short POWER_BATTERY = 978;
    public static final short PICKUP_AREA = 937;
    public static final short PICKUP_RAIL = 938;
    public static final short EXIT_SHOOT_RAIL = 939;
    public static final short SIGNAL_RANDOM = 979;
    public static final short SIGNAL_SENSOR = 980;
    public static final short BLUEPRINT_EMPTY = 999;
    public static final short REACTOR_STABILIZER_STREAM_NODE = 66;
    public static final short REACTOR_MAIN = 1008;
    public static final short REACTOR_STABILIZER = 1009;
    public static final short REACTOR_CONDUIT = 1010;
    public static final short REACTOR_CHAMBER_MOBILITY = 1011;
    public static final short REACTOR_CHAMBER_SCANNER = 1012;
    public static final short REACTOR_CHAMBER_JUMP = 1013;
    public static final short REACTOR_CHAMBER_STEALTH = 1014;
    public static final short REACTOR_CHAMBER_LOGISTICS = 1015;
    public static final short REACTOR_CHAMBER_JUMP_DISTANCE_0 = 1100;
    public static final short REACTOR_CHAMBER_JUMP_DISTANCE_1 = 1101;
    public static final short REACTOR_CHAMBER_JUMP_DISTANCE_2 = 1102;
    public static final short REACTOR_CHAMBER_JUMP_DISTANCE_3 = 1103;
    public static final short[] resources = new short[16];
    public static final short[] orientationToResIDMapping = new short[32];
    public static final byte[] resIDToOrientationMapping = new byte[2048];
    public static final short MINE_CORE = 37;
    public static final short MINE_LAYER = 41;
    public static final short EFFECT_EM_COMPUTER = 349;
    public static final short EFFECT_EM = 350;
    public static final short EFFECT_HEAT_COMPUTER = 351;
    public static final short EFFECT_HEAT = 352;
    public static final short EFFECT_KINETIC_COMPUTER = 353;
    public static final short EFFECT_KINETIC = 354;
    public static final short MINE_TYPE_CANNON = 355;
    public static final short MINE_TYPE_MISSILE = 356;
    public static final short MINE_TYPE_PROXIMITY = 358;
    public static final short MINE_TYPE_D = 359;
    public static final short TRACTOR_BEAM_COMPUTER = 360;
    public static final short TRACTOR_BEAM = 361;
    public static final short MINE_MOD_STRENGTH = 363;
    public static final short MINE_MOD_PERSONAL = 364;
    public static final short MINE_MOD_FRIENDS = 365;
    public static final short MINE_MOD_STEALTH = 366;
    public static final short MISSILE_CAPACITY_MODULE = 362;
    public static final int[] orientationToResOverlayMapping = new int[32];
    public static final ShortOpenHashSet keySet = new ShortOpenHashSet(256);
    public static final ShortArrayList doorTypes = new ShortArrayList();
    public static final ShortArrayList inventoryTypes = new ShortArrayList();
    public static final ShortArrayList chamberAnyTypes = new ShortArrayList();
    public static final ShortArrayList chamberGeneralTypes = new ShortArrayList();
    public static final ShortArrayList lightTypes = new ShortArrayList();
    public static final ShortArrayList sourcedTypes = new ShortArrayList();
    private static final Short2ObjectOpenHashMap<ElementInformation> informationKeyMap = new Short2ObjectOpenHashMap();
    private static final ShortOpenHashSet factoryKeySet = new ShortOpenHashSet(256);
    private static final ShortOpenHashSet leveldKeySet = new ShortOpenHashSet(256);
    private static final Short2ObjectOpenHashMap<ElementInformation> projected = new Short2ObjectOpenHashMap();
    public static int highestType = 0;
    public static ElementInformation[] infoArray;
    public static boolean[] factoryInfoArray;
    public static boolean[] validArray;
    public static boolean[] lodShapeArray;
    public static short[] signalArray;
    public static short[] signaledByRailArray;
    public static boolean initialized;
    public static Properties properties;
    public static FixedRecipes fixedRecipes;
    public static FixedRecipe capsuleRecipe;
    public static FixedRecipe microAssemblerRecipe;
    public static FixedRecipe macroBlockRecipe;
    public static ObjectArrayList<ElementInformation> sortedByName;
    public static FixedRecipe personalCapsuleRecipe;
    private static short[] keyArray;
    private static final ShortOpenHashSet signalSet = new ShortOpenHashSet();
    private static final ShortOpenHashSet signaledByRailSet = new ShortOpenHashSet();
    private static ElementCategory categoryHirarchy;
    private static List<String> categoryNames;
    public static String propertiesPath;
    public static File configFile;
    private static boolean loadedForGame;
    public static String propertiesHash = "none";
    public static String configHash = "none";
    public static final short[] HULL_HELPER = new short[]{69, 70, 75, 76, 77, 78, 79, 81};
    public static final byte MAX_HITPOINTS = 127;
    public static final float MAX_HITPOINTS_INV = 0.007874016F;

    public ElementKeyMap() {
    }

    public static boolean isShard(short var0) {
        return var0 == 480 || var0 == 481 || var0 == 482 || var0 == 483 || var0 == 484 || var0 == 485 || var0 == 486 || var0 == 487;
    }

    public static boolean isOre(short var0) {
        return var0 == 488 || var0 == 489 || var0 == 490 || var0 == 491 || var0 == 492 || var0 == 493 || var0 == 494 || var0 == 495;
    }

    public static boolean hasResourceInjected(short var0, byte var1) {
        return isValidType(var0) && getInfo(var0).resourceInjection != ResourceInjectionType.OFF && var1 > 0 && var1 < 17;
    }

    public static void add(short var0, ElementInformation var1) throws ParserConfigurationException {
        if (keySet.contains(var0)) {
            throw new ParserConfigurationException("Duplicate Block ID " + var0 + " (" + var1.getName() + " and " + ((ElementInformation)informationKeyMap.get(var0)).getName() + ")");
        } else {
            keySet.add(var0);
            informationKeyMap.put(var0, var1);
            highestType = Math.max(highestType, var0);
            if (var1.getFactory() != null) {
                factoryKeySet.add(var0);
            }

        }
    }

    public static void addInformationToExisting(ElementInformation var0) throws ParserConfigurationException {
        boolean var1 = categoryHirarchy.insertRecusrive(var0);

        assert var1 : var0.getType();

        add(var0.getId(), var0);
        infoArray = new ElementInformation[highestType + 1];
        factoryInfoArray = new boolean[highestType + 1];
        validArray = new boolean[highestType + 1];
        lodShapeArray = new boolean[highestType + 1];

        Entry var2;
        for(Iterator var4 = informationKeyMap.entrySet().iterator(); var4.hasNext(); validArray[(Short)var2.getKey()] = true) {
            var2 = (Entry)var4.next();
            infoArray[(Short)var2.getKey()] = (ElementInformation)var2.getValue();
        }

        if (factoryKeySet.contains(var0.getId())) {
            factoryInfoArray[var0.getId()] = true;
            var0.getFactory().enhancer = 212;
        }

        if (var0.hasLod()) {
            lodShapeArray[var0.getId()] = true;
        }

        keyArray = new short[keySet.size()];
        int var5 = 0;

        for(Iterator var6 = keySet.iterator(); var6.hasNext(); ++var5) {
            short var3 = (Short)var6.next();
            keyArray[var5] = var3;
        }

    }

    public static void clear() {
        informationKeyMap.clear();
        infoArray = null;
        System.err.println("INFO ARRAY CLEARED");
        highestType = 0;
        factoryKeySet.clear();
        keySet.clear();
        projected.clear();
        leveldKeySet.clear();
        categoryHirarchy.clear();
        categoryNames = null;
        factoryInfoArray = null;
        validArray = null;
        signalArray = null;
        signaledByRailArray = null;
        lodShapeArray = null;
    }

    public static boolean exists(int var0) {
        return var0 > 0 && var0 < infoArray.length && infoArray[var0] != null;
    }

    public static String formatDescString(String var0) {
        StringBuffer var3 = new StringBuffer(var0);
        int var1 = 0;

        for(int var2 = 0; var2 < var3.length() - 1; ++var2) {
            if (var3.charAt(var2) == '\n') {
                var1 = 0;
                ++var2;
            }

            if (var1 > 50) {
                while(var2 > 0 && var3.charAt(var2) != ' ') {
                    --var2;
                }

                var3.deleteCharAt(var2);
                var3.insert(var2, "\n");
                ++var2;
                var1 = 0;
            }

            ++var1;
        }

        return var3.toString();
    }

    public static String[] getCategoryNames(ElementCategory var0) {
        if (categoryNames == null) {
            categoryNames = new ArrayList();
        }

        categoryNames.clear();
        getCategoryNames(var0, categoryNames);
        return (String[])categoryNames.toArray(new String[categoryNames.size()]);
    }

    public static void getCategoryNames(ElementCategory var0, List<String> var1) {
        Iterator var3 = var0.getChildren().iterator();

        while(var3.hasNext()) {
            ElementCategory var2 = (ElementCategory)var3.next();
            var1.add(var2.getCategory());
            getCategoryNames(var2, var1);
        }

    }

    public static ElementCategory getCategoryHirarchy() {
        return categoryHirarchy;
    }

    public static ShortOpenHashSet getFactorykeyset() {
        return factoryKeySet;
    }

    public static void cleanUpUnusedBlockIds() throws IOException {
        Iterator var0 = properties.entrySet().iterator();

        while(var0.hasNext()) {
            Entry var1;
            short var2;
            if ((var2 = Short.parseShort((var1 = (Entry)var0.next()).getValue().toString())) < 1792 && !keySet.contains(var2)) {
                var0.remove();
                System.err.println("REMOVED: " + var1.getKey());
            }
        }

        BufferedReader var9 = new BufferedReader(new FileReader(configFile));
        StringBuffer var8 = new StringBuffer();

        String var11;
        while((var11 = var9.readLine()) != null) {
            var8.append(var11 + "\n");
        }

        var9.close();
        Properties var10 = new Properties();
        Iterator var12 = keySet.iterator();

        while(true) {
            while(var12.hasNext()) {
                short var3;
                ElementInformation var4;
                String var5 = (var4 = getInfo(var3 = (Short)var12.next())).getNameUntranslated().toUpperCase(Locale.ENGLISH).replaceAll("\\s", "_");
                int var6 = 0;

                String var7;
                for(var7 = var5; var10.get(var7) != null && var10.get(var7) != var4 || properties.get(var7) != null && properties.get(var7) != var4; ++var6) {
                    var7 = var5 + "_" + var6;
                }

                var5 = var7;
                Iterator var15 = properties.entrySet().iterator();

                while(var15.hasNext()) {
                    Entry var16;
                    short var17;
                    if ((var17 = Short.parseShort((var16 = (Entry)var15.next()).getValue().toString())) < 1792 && var17 == var3 && !var5.equals(var16.getKey().toString())) {
                        int var14;
                        while((var14 = var8.indexOf("\"" + var16.getKey().toString() + "\"")) >= 0) {
                            var8.replace(var14 + 1, var14 + var16.getKey().toString().length() + 1, var5);
                            System.err.println("REPLACE: " + var14 + "; " + (var14 + var16.getKey().toString().length()) + ": " + var16.getKey().toString() + " -> " + var5);
                        }

                        while((var14 = var8.indexOf(">" + var16.getKey().toString() + "<")) >= 0) {
                            var8.replace(var14 + 1, var14 + var16.getKey().toString().length() + 1, var5);
                            System.err.println("REPLACE: " + var14 + "; " + (var14 + var16.getKey().toString().length()) + ": " + var16.getKey().toString() + " -> " + var5);
                        }

                        var15.remove();
                        System.err.println("RENAMED: " + var16.getKey() + " -> " + var5);
                        var10.put(var5, var16.getValue());
                        break;
                    }
                }
            }

            BufferedWriter var13;
            (var13 = new BufferedWriter(new FileWriter(configFile))).write(var8.toString());
            var13.close();
            properties.putAll(var10);
            writePropertiesOrdered();
            return;
        }
    }

    public static ElementInformation getInfoFast(short var0) {
        return infoArray[var0];
    }

    public static ElementInformation getInfoFast(int var0) {
        return infoArray[var0];
    }

    public static ElementInformation getInfo(short var0) {
        //assert var0 > 0 && var0 < infoArray.length && infoArray[var0] != null : "type " + var0 + " unknown, please check the properties and the xml ";

        if (var0 < 0) {
            throw new NullPointerException("Exception: REQUESTED TYPE " + var0 + " IS NULL");
        } else {
            ElementInformation var1;
            if (infoArray == null) {
                System.err.println("INFO ARRAY IS NULL!!!!!");
            }
            if ((var1 = infoArray[var0]) == null) {
                throw new NullPointerException("Exception: REQUESTED TYPE " + var0 + " IS NULL");
            } else {
                return var1;
            }
        }
    }

    public static ElementInformation getInfo(int var0) {
        assert var0 > 0 && var0 < infoArray.length && infoArray[var0] != null : "type " + var0 + " unknown, please check the properties and the xml ";

        if (var0 < 0) {
            throw new NullPointerException("Exception: REQUESTED TYPE " + var0 + " IS NULL");
        } else {
            ElementInformation var1;
            if ((var1 = infoArray[var0]) == null) {
                throw new NullPointerException("Exception: REQUESTED TYPE " + var0 + " IS NULL");
            } else {
                return var1;
            }
        }
    }

    public static ShortOpenHashSet getLeveldkeyset() {
        return leveldKeySet;
    }

    public static String getNameSave(short var0) {
        return exists(var0) ? getInfo(var0).getName() : "unknown(" + var0 + ")";
    }

    private static void initElements(List<ElementInformation> var0, ElementCategory var1) throws ParserConfigurationException {
        Iterator var13 = var0.iterator();

        while(var13.hasNext()) {
            ElementInformation var2;
            add((var2 = (ElementInformation)var13.next()).getId(), var2);
        }

        categoryHirarchy = var1;
        infoArray = new ElementInformation[highestType + 1];
        factoryInfoArray = new boolean[highestType + 1];
        validArray = new boolean[highestType + 1];
        lodShapeArray = new boolean[highestType + 1];

        Entry var17;
        for(var13 = informationKeyMap.entrySet().iterator(); var13.hasNext(); validArray[(Short)var17.getKey()] = true) {
            var17 = (Entry)var13.next();
            infoArray[(Short)var17.getKey()] = (ElementInformation)var17.getValue();
        }

        ShortArrayList var14 = new ShortArrayList();
        ShortArrayList var18 = new ShortArrayList();

        it.unimi.dsi.fastutil.shorts.Short2ObjectMap.Entry var3;
        Iterator var15;
        for(var15 = informationKeyMap.short2ObjectEntrySet().iterator(); var15.hasNext(); lodShapeArray[var3.getShortKey()] = ((ElementInformation)var3.getValue()).hasLod()) {
            ((ElementInformation)(var3 = (it.unimi.dsi.fastutil.shorts.Short2ObjectMap.Entry)var15.next()).getValue()).onInit();
            if (((ElementInformation)var3.getValue()).isSignal()) {
                var14.add(var3.getShortKey());
            }

            if (((ElementInformation)var3.getValue()).signaledByRail) {
                var18.add(var3.getShortKey());
            }
        }

        var14.toArray(signalArray = new short[var14.size()]);
        var18.toArray(signaledByRailArray = new short[var18.size()]);
        if (projected.size() > 0) {
            projected.trim();
        }

        short var20;
        for(var15 = factoryKeySet.iterator(); var15.hasNext(); getInfo(var20).getFactory().enhancer = 212) {
            var20 = (Short)var15.next();
            factoryInfoArray[var20] = true;
        }

        Collections.sort(sortedByName = new ObjectArrayList(informationKeyMap.values()), new Comparator<ElementInformation>() {
            public final int compare(ElementInformation var1, ElementInformation var2) {
                return var1.getName().toLowerCase(Locale.ENGLISH).compareTo(var2.getName().toLowerCase(Locale.ENGLISH));
            }
        });
        initialized = true;
        var15 = informationKeyMap.entrySet().iterator();

        while(true) {
            int var5;
            Entry var21;
            int var23;
            do {
                if (!var15.hasNext()) {
                    for(var15 = informationKeyMap.keySet().iterator(); var15.hasNext(); getInfo((int)(Short)var15.next()).isSourceBlockTmp = true) {
                    }

                    var15 = informationKeyMap.keySet().iterator();

                    ElementInformation var16;
                    short[] var22;
                    while(var15.hasNext()) {
                        if ((var16 = getInfo((int)(var20 = (Short)var15.next()))).blocktypeIds != null) {
                            var23 = (var22 = var16.blocktypeIds).length;

                            for(var5 = 0; var5 < var23; ++var5) {
                                ElementInformation var24;
                                (var24 = getInfo(var22[var5])).isSourceBlockTmp = false;
                                var24.shoppable = false;
                            }
                        }

                        if (getInfo((int)var20).getType().hasParent("Terrain")) {
                            getInfo((int)var20).setSpecialBlock(false);
                        }

                        getInfo((int)var20).recalcTotalConsistence();
                        getInfo((int)var20).sanatizeReactorValues();
                    }

                    var15 = informationKeyMap.keySet().iterator();

                    while(true) {
                        do {
                            do {
                                if (!var15.hasNext()) {
                                    loadedForGame = false;
                                    return;
                                }
                            } while(!(var16 = getInfo((int)(Short)var15.next())).isSourceBlockTmp);
                        } while(var16.blocktypeIds == null);

                        var23 = (var22 = var16.blocktypeIds).length;

                        for(var5 = 0; var5 < var23; ++var5) {
                            getInfo(var22[var5]).sourceReference = var16.getId();
                        }
                    }
                }

                if (((ElementInformation)(var21 = (Entry)var15.next()).getValue()).resourceInjection != ResourceInjectionType.OFF && ((ElementInformation)var21.getValue()).getIndividualSides() != 1) {
                    try {
                        throw new ParseException("BlockConfig.xml Error: " + var21.getValue() + " cannot have resource injection (resOverlay) and multiple sides");
                    } catch (ParseException var8) {
                        throw new RuntimeException(var8);
                    }
                }

                if (((ElementInformation)var21.getValue()).resourceInjection != ResourceInjectionType.OFF && ((ElementInformation)var21.getValue()).orientatable) {
                    try {
                        throw new ParseException("BlockConfig.xml Error: " + var21.getValue() + " cannot have resource injection (resOverlay) and be orientatable");
                    } catch (ParseException var9) {
                        throw new RuntimeException(var9);
                    }
                }

                if (((ElementInformation)var21.getValue()).getSourceReference() != 0 && ((ElementInformation)var21.getValue()).getSourceReference() != (Short)var21.getKey()) {
                    ((ElementInformation)var21.getValue()).consistence.clear();
                    ((ElementInformation)var21.getValue()).producedInFactory = 0;
                    ((ElementInformation)var21.getValue()).inRecipe = false;
                    ((ElementInformation)var21.getValue()).shoppable = false;
                }

                if (((ElementInformation)var21.getValue()).getHpOldByte() == 0) {
                    ((ElementInformation)var21.getValue()).setHpOldByte((short)((ElementInformation)var21.getValue()).getMaxHitPointsFull());
                }
            } while(((ElementInformation)var21.getValue()).slabIds == null && ((ElementInformation)var21.getValue()).styleIds == null && ((ElementInformation)var21.getValue()).wildcardIds == null);

            var14 = new ShortArrayList();
            int var19 = 1;
            short[] var4;
            int var6;
            short var7;
            if (((ElementInformation)var21.getValue()).wildcardIds != null) {
                var5 = (var4 = ((ElementInformation)var21.getValue()).wildcardIds).length;

                for(var6 = 0; var6 < var5; ++var6) {
                    if (isValidType(var7 = var4[var6])) {
                        if (!var14.contains(var7) && var7 != ((ElementInformation)var21.getValue()).id) {
                            var14.add(var7);
                        }

                        ((ElementInformation)informationKeyMap.get(var7)).wildcardIndex = var19++;
                    } else {
                        try {
                            throw new Exception("WARNING: block type reference invalid: (wildcardIds of " + ((ElementInformation)var21.getValue()).getName() + "): " + var7);
                        } catch (Exception var12) {
                            var12.printStackTrace();
                        }
                    }
                }
            }

            if (((ElementInformation)var21.getValue()).styleIds != null) {
                var5 = (var4 = ((ElementInformation)var21.getValue()).styleIds).length;

                for(var6 = 0; var6 < var5; ++var6) {
                    if (isValidType(var7 = var4[var6])) {
                        if (!var14.contains(var7) && var7 != ((ElementInformation)var21.getValue()).id) {
                            var14.add(var7);
                        }
                    } else {
                        try {
                            throw new Exception("WARNING: block type reference invalid: (styleIds of " + ((ElementInformation)var21.getValue()).getName() + "): " + var7);
                        } catch (Exception var11) {
                            var11.printStackTrace();
                        }
                    }
                }
            }

            if (((ElementInformation)var21.getValue()).slabIds != null) {
                var5 = (var4 = ((ElementInformation)var21.getValue()).slabIds).length;

                for(var6 = 0; var6 < var5; ++var6) {
                    if (isValidType(var7 = var4[var6])) {
                        if (!var14.contains(var7) && var7 != ((ElementInformation)var21.getValue()).id) {
                            var14.add(var7);
                        }
                    } else {
                        try {
                            throw new Exception("WARNING: block type reference invalid (slabIds of " + ((ElementInformation)var21.getValue()).getName() + "): " + var7);
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }
                }
            }

            ((ElementInformation)var21.getValue()).blocktypeIds = new short[var14.size()];

            for(var23 = 0; var23 < ((ElementInformation)var21.getValue()).blocktypeIds.length; ++var23) {
                ((ElementInformation)var21.getValue()).blocktypeIds[var23] = var14.getShort(var23);
            }
        }
    }

    public static synchronized void initDataForGame() {
        if (!loadedForGame) {
            Iterator var0 = informationKeyMap.entrySet().iterator();

            while(var0.hasNext()) {
                Entry var1;
                ((ElementInformation)(var1 = (Entry)var0.next()).getValue()).lodCollision.load();
                ((ElementInformation)var1.getValue()).lodDetailCollision.load();
            }

            loadedForGame = true;
        }

    }

    private static void initFixedRecipePrices(FixedRecipe var0, boolean var1) {
        FixedRecipeProduct[] var12;
        int var2 = (var12 = var0.getRecipeProduct()).length;

        for(int var3 = 0; var3 < var2; ++var3) {
            FixedRecipeProduct var4 = var12[var3];
            int var5 = 0;
            FactoryResource[] var6;
            int var7 = (var6 = var4.getOutputResource()).length;

            int var8;
            FactoryResource var9;
            for(var8 = 0; var8 < var7; ++var8) {
                var9 = var6[var8];
                var5 = (int)((float)var5 + (float)((long)var9.count * getInfo(var9.type).getPrice(var1)) * (Float)ServerConfig.DYNAMIC_RECIPE_PRICE_MODIFIER.getCurrentState());
            }

            var7 = (var6 = var4.getInputResource()).length;

            for(var8 = 0; var8 < var7; ++var8) {
                var9 = var6[var8];
                long var10 = (long)Math.ceil((double)var5 / (double)var9.count);
                if (getInfo(var9.type).dynamicPrice < var10) {
                    getInfo(var9.type).setPrice(var10);
                    getInfo(var9.type).dynamicPrice = var10;
                }
            }
        }

    }

    private static void initFixedRecipes(FixedRecipes var0) {
        fixedRecipes = var0;

        for(int var1 = 0; var1 < var0.recipes.size(); ++var1) {
            if (((FixedRecipe)var0.recipes.get(var1)).name.equals("Make Macro Factory Block")) {
                macroBlockRecipe = (FixedRecipe)var0.recipes.get(var1);
            }

            if (((FixedRecipe)var0.recipes.get(var1)).name.equals("Micro Assembler")) {
                microAssemblerRecipe = (FixedRecipe)var0.recipes.get(var1);
            }

            if (((FixedRecipe)var0.recipes.get(var1)).name.equals("Capsule Refinery")) {
                initFixedRecipePrices(capsuleRecipe = (FixedRecipe)var0.recipes.get(var1), true);
            }

            if (((FixedRecipe)var0.recipes.get(var1)).name.equals("Personal Capsule Refinery")) {
                personalCapsuleRecipe = (FixedRecipe)var0.recipes.get(var1);
            }
        }

    }

    public static void initializeData(File var0) {
        initializeData((File)null, false, (String)null, var0);
    }

    public static void createBlankReactorBlocks(boolean var0) throws ParserConfigurationException {
        ElementCategory var1 = getCategoryHirarchy().getChild("General").getChild("Power").getChild("Chamber");
        Object2ObjectOpenHashMap var2 = new Object2ObjectOpenHashMap();
        Short2ObjectOpenHashMap var3 = new Short2ObjectOpenHashMap();
        Iterator var4 = properties.keySet().iterator();

        while(true) {
            Object var5;
            short var6;
            do {
                do {
                    if (!var4.hasNext()) {
                        var4 = var2.values().iterator();

                        ElementInformation var16;
                        while(var4.hasNext()) {
                            if (!(var16 = (ElementInformation)var4.next()).name.contains("_")) {
                                var16.chamberGeneral = true;
                            }
                        }

                        var4 = var2.values().iterator();

                        ElementInformation var14;
                        while(var4.hasNext()) {
                            if (!(var16 = (ElementInformation)var4.next()).chamberGeneral) {
                                String var18 = var16.name.substring(0, var16.name.indexOf("_"));
                                ElementInformation var19;
                                if ((var19 = (ElementInformation)var2.get(var18)) == null) {
                                    var19 = getInfo(Integer.parseInt(properties.get("REACTOR_CHAMBER_" + var18).toString()));
                                }

                                assert var19 != null : var18;

                                var16.chamberRoot = var19.id;
                                String var20 = var16.name.substring(var16.name.lastIndexOf("_") + 1, var16.name.length());

                                try {
                                    int var10 = Integer.parseInt(var20);
                                    String var13 = var16.name.substring(0, var16.name.lastIndexOf("_"));
                                    if (var10 > 0) {
                                        String var11 = var13 + "_" + (var10 - 1);
                                        if ((var14 = (ElementInformation)var2.get(var11)) == null) {
                                            var14 = getInfo(Integer.parseInt(properties.get("REACTOR_CHAMBER_" + var11).toString()));
                                        }

                                        assert var14 != null : var11;

                                        var16.chamberParent = var14.id;
                                        ((ElementInformation)var3.get((short)var16.chamberParent)).chamberUpgradesTo = var16.id;
                                    } else {
                                        var16.chamberParent = 0;
                                    }
                                } catch (Exception var9) {
                                    System.err.println("NOT UPGRADABLE: " + var16 + "; " + var20);
                                    var16.chamberParent = 0;
                                }
                            }
                        }

                        ObjectArrayList var15;
                        Collections.sort(var15 = new ObjectArrayList(var2.values()), new Comparator<ElementInformation>() {
                            public final int compare(ElementInformation var1, ElementInformation var2) {
                                String var3 = "";
                                String var4 = "";
                                Iterator var5 = ElementKeyMap.properties.entrySet().iterator();

                                while(var5.hasNext()) {
                                    Entry var6;
                                    if (Integer.parseInt((var6 = (Entry)var5.next()).getValue().toString()) == var1.id) {
                                        var3 = var6.getKey().toString();
                                    }

                                    if (Integer.parseInt(var6.getValue().toString()) == var2.id) {
                                        var4 = var6.getKey().toString();
                                    }
                                }

                                return var3.compareTo(var4);
                            }
                        });
                        short var17 = 629;
                        short var21 = 0;
                        Iterator var22 = var15.iterator();

                        ElementInformation var12;
                        while(var22.hasNext()) {
                            if (!(var12 = (ElementInformation)var22.next()).chamberGeneral) {
                                var12.shoppable = false;
                                var12.placable = false;
                                var12.setSourceReference(var12.chamberParent);
                                var12.setTextureId(new short[]{var21, 638, 638, var21, var21, var21});
                                if (var12.chamberParent == 0) {
                                    if ((var14 = (ElementInformation)var3.get((short)var12.chamberRoot)) == null) {
                                        var14 = getInfo(var12.chamberRoot);
                                    }

                                    assert var14 != null : "not found " + var12 + "; " + var12.chamberRoot;

                                    var14.chamberChildren.add(var12.id);
                                }
                            } else {
                                var12.setTextureId(new short[]{var17, var17, var17, var17, var17, var17});
                                var21 = var17++;
                            }

                            if (!var12.chamberGeneral && var12.chamberParent != 0) {
                                if ((var14 = (ElementInformation)var3.get((short)var12.chamberParent)) == null) {
                                    var14 = getInfo(var12.chamberParent);
                                }

                                assert var14 != null : var12 + " no parent: " + var12.chamberParent;

                                var14.chamberChildren.add(var12.id);
                                var12.chamberPrerequisites.add(var14.id);
                            }
                        }

                        var22 = var15.iterator();

                        while(var22.hasNext()) {
                            var12 = (ElementInformation)var22.next();
                            System.err.println("INFO:::: " + var12 + "; INSERT " + !keySet.contains(var12.id));
                            if (!keySet.contains(var12.id)) {
                                System.err.println("ADDING: " + var12);
                                addInformationToExisting(var12);
                            }
                        }

                        return;
                    }

                    var5 = var4.next();
                    var6 = Short.parseShort(properties.get(var5).toString());
                } while(!var5.toString().startsWith("REACTOR_CHAMBER_"));
            } while(!var0 && keySet.contains(var6));

            String var7 = var5.toString().substring(16, var5.toString().length());
            System.err.println("NEW REACTOR CHAMBER: " + var7);
            ElementInformation var8 = new ElementInformation(var6, var7, var1, new short[]{0, 0, 0, 0, 0, 0});
            var2.put(var7, var8);
            var3.put(var6, var8);
        }
    }

    public static void initializeData(File var0, boolean var1, String var2, File var3) {
        while(!initialized) {
            try {
                ElementParser var6;
                initElements((var6 = load(var0, var1, var2, var3)).getInfoElements(), var6.getRootCategory());
                initFixedRecipes(var6.getFixedRecipes());
            } catch (Exception var5) {
                var5.printStackTrace();
                GuiErrorHandler.processErrorDialogException(var5);
                var3 = var3;
                var2 = null;
                var1 = false;
                var0 = null;
                continue;
            }

            Starter.modManager.onInitializeBlockData();
            keyArray = new short[keySet.size()];
            int var7 = 0;

            for(Iterator var8 = keySet.iterator(); var8.hasNext(); ++var7) {
                short var10 = (Short)var8.next();
                keyArray[var7] = var10;
            }

            doorTypes.clear();
            inventoryTypes.clear();
            lightTypes.clear();
            chamberAnyTypes.clear();
            chamberGeneralTypes.clear();
            signalSet.clear();
            signaledByRailSet.clear();
            short[] var9;
            int var11 = (var9 = keyArray).length;

            for(var7 = 0; var7 < var11; ++var7) {
                short var12 = var9[var7];
                ElementInformation var4;
                if ((var4 = infoArray[var12]).isInventory()) {
                    inventoryTypes.add(var4.id);
                }

                if (var4.getSourceReference() != 0) {
                    sourcedTypes.add(var4.id);
                }

                if (var4.isLightSource()) {
                    lightTypes.add(var4.id);
                }

                if (var4.isDoor()) {
                    doorTypes.add(var4.id);
                }

                if (var4.isReactorChamberAny()) {
                    chamberAnyTypes.add(var4.id);
                }

                if (var4.isReactorChamberGeneral()) {
                    chamberGeneralTypes.add(var4.id);
                }

                if (var4.isSignal()) {
                    signalSet.add(var12);
                }

                if (var4.signaledByRail) {
                    signaledByRailSet.add(var12);
                }
            }

            signalArray = new short[signalSet.size()];
            signalArray = signalSet.toArray(signalArray);
            signaledByRailArray = new short[signaledByRailSet.size()];
            signaledByRailArray = signaledByRailSet.toArray(signaledByRailArray);
            inventoryTypes.trim();
            lightTypes.trim();
            doorTypes.trim();

            assert checkConflicts();

            //INSERTED CODE
            ModPlayground.initBlockData();
            //

            return;
        }

    }

    private static boolean checkConflicts() {
        short[] var0;
        int var1 = (var0 = keyArray).length;

        for(int var2 = 0; var2 < var1; ++var2) {
            short var3 = var0[var2];
            short[] var4;
            int var5 = (var4 = keyArray).length;

            for(int var6 = 0; var6 < var5; ++var6) {
                short var7 = var4[var6];
                if (var3 != var7 && !getInfo(var3).isReactorChamberAny() && !getInfo(var3).isDeprecated() && getInfo(var3).isShoppable() && !getInfo(var7).isReactorChamberAny() && getInfo(var3).getSlab() == 0 && getInfo(var7).getSlab() == 0 && getInfo(var3).getBuildIconNum() == getInfo(var7).getBuildIconNum()) {
                    try {
                        throw new Exception("[INFO] BuildIconConflict: " + toString(var3) + " --- " + toString(var7) + "; " + getInfo(var3).getBuildIconNum() + "; " + getInfo(var7).getBuildIconNum());
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        return true;
                    }
                }
            }
        }

        return true;
    }

    public static void initializeDeathStarData() throws ParserConfigurationException {
    }

    public static boolean isValidType(short var0) {
        return var0 >= 0 && var0 < infoArray.length && infoArray[var0] != null;
    }

    public static boolean isValidType(int var0) {
        return var0 >= 0 && var0 < infoArray.length && infoArray[var0] != null;
    }

    public static String list() {
        return keySet.toString();
    }

    private static ElementParser load(File var0, boolean var1, String var2, File var3) throws SAXException, IOException, ParserConfigurationException, ElementParserException {
        ElementParser var4;
        if (var0 == null) {
            (var4 = new ElementParser()).loadAndParseDefault(var3);
            return var4;
        } else {
            (var4 = new ElementParser()).loadAndParseCustomXML(var0, var1, var2, var3);
            return var4;
        }
    }

    public static void reinitializeData(File var0, boolean var1, String var2, File var3) {
        initialized = false;
        categoryHirarchy = null;
        factoryKeySet.clear();
        projected.clear();
        keySet.clear();
        leveldKeySet.clear();
        highestType = 0;
        informationKeyMap.clear();
        fixedRecipes = null;
        infoArray = null;
        System.err.println("INFO ARRAY SET TO NULL");
        factoryInfoArray = null;
        validArray = null;
        signalArray = null;
        lodShapeArray = null;
        keyArray = null;
        signaledByRailArray = null;
        signaledByRailSet.clear();
        signalSet.clear();
        initializeData(var0, var1, var2, var3);
    }

    public static void removeFromExisting(ElementInformation var0) {
        keySet.remove(var0.getId());
        informationKeyMap.remove(var0.getId());
        highestType = 0;

        Iterator var1;
        short var2;
        for(var1 = keySet.iterator(); var1.hasNext(); highestType = Math.max(highestType, var2)) {
            var2 = (Short)var1.next();
        }

        factoryKeySet.remove(var0.getId());
        factoryInfoArray[var0.getId()] = false;
        getLeveldkeyset().remove(var0.getId());
        infoArray = new ElementInformation[highestType + 1];
        validArray = new boolean[highestType + 1];
        lodShapeArray = new boolean[highestType + 1];

        Entry var3;
        for(var1 = informationKeyMap.entrySet().iterator(); var1.hasNext(); lodShapeArray[(Short)var3.getKey()] = ((ElementInformation)var3.getValue()).hasLod()) {
            var3 = (Entry)var1.next();
            infoArray[(Short)var3.getKey()] = (ElementInformation)var3.getValue();
            validArray[(Short)var3.getKey()] = true;
        }

        categoryHirarchy.removeRecursive(var0);
    }

    public static void reparseProperties() throws IOException {
        properties = new Properties();
        FileInputStream var0 = new FileInputStream("./data/config/BlockTypes.properties");
        properties.load(var0);

        try {
            propertiesHash = FileUtil.getSha1Checksum("./data/config/BlockTypes.properties");
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }
    }

    public static void reparseProperties(String var0) throws IOException {
        properties = new Properties();
        FileInputStream var1 = new FileInputStream(var0);
        properties.load(var1);

        try {
            propertiesHash = FileUtil.getSha1Checksum(var0);
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
        }
    }

    public static short[] typeList() {
        return keyArray;
    }

    private static void writeCatToXML(ElementCategory var0, Element var1, Document var2) throws CannotAppendXMLException {
        Element var3 = var2.createElement(var0.getCategory());
        Iterator var4 = var0.getChildren().iterator();

        while(var4.hasNext()) {
            writeCatToXML((ElementCategory)var4.next(), var3, var2);
        }

        var4 = var0.getInfoElements().iterator();

        while(var4.hasNext()) {
            ((ElementInformation)var4.next()).appendXML(var2, var3);
        }

        var1.appendChild(var3);
    }

    public static File writeDocument(File var0, ElementCategory var1, FixedRecipes var2) {
        try {
            Document var3;
            Element var4 = (var3 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()).createElement("Config");
            Element var5 = var3.createElement(var1.getCategory());
            Comment var6 = var3.createComment("autocreated by the starmade block editor");
            var5.appendChild(var6);
            Iterator var9 = var1.getChildren().iterator();

            while(var9.hasNext()) {
                writeCatToXML((ElementCategory)var9.next(), var5, var3);
            }

            Element var10;
            Element var10000 = var10 = var3.createElement("Recipes");
            FixedRecipes var7 = var2;
            Element var12 = var10000;
            var7.appendDoc(var12, var3);
            var4.appendChild(var5);
            var4.appendChild(var10);
            var3.appendChild(var4);
            var3.setXmlVersion("1.0");
            Transformer var11;
            (var11 = TransformerFactory.newInstance().newTransformer()).setOutputProperty("omit-xml-declaration", "yes");
            var11.setOutputProperty("indent", "yes");
            var11.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            new StringWriter();
            StreamResult var13 = new StreamResult(var0);
            DOMSource var14 = new DOMSource(var3);
            var11.transform(var14, var13);
            return var0;
        } catch (Exception var8) {
            var8.printStackTrace();
            GuiErrorHandler.processErrorDialogException(var8);
            return null;
        }
    }

    public static File writeDocument(String var0, ElementCategory var1, FixedRecipes var2) {
        return writeDocument((File)(new FileExt(var0)), var1, var2);
    }

    private static void writeRecipes(Element var0, Document var1, FixedRecipes var2) throws DOMException, CannotAppendXMLException {
        var2.appendDoc(var0, var1);
    }

    public static void removeDuplicateBuildIcons() {
        IntOpenHashSet var0 = new IntOpenHashSet();
        Iterator var1 = informationKeyMap.values().iterator();

        while(var1.hasNext()) {
            ElementInformation var2;
            int var3;
            for(var3 = (var2 = (ElementInformation)var1.next()).getBuildIconNum(); var0.contains(var3); ++var3) {
            }

            var2.setBuildIconNum(var3);
            var0.add(var2.getBuildIconNum());
        }

    }

    public static String toString(int var0) {
        return initialized ? toString((short)var0) : "[" + var0 + "]";
    }

    public static String toString(short var0) {
        if (var0 == 32767) {
            return "TYPE_ALL";
        } else if (var0 == 0) {
            return "TYPE_NONE";
        } else if (var0 == 29999) {
            return "TYPE_RAIL_TRACK";
        } else if (var0 == 30000) {
            return "TYPE_SIGNAL";
        } else if (var0 == -32768) {
            return "TYPE_MULTI_SLOT";
        } else {
            return exists(var0) ? getInfo(var0).toString() : "Unknown(" + var0 + ")";
        }
    }

    public static String toString(Collection<Short> var0) {
        StringBuffer var1;
        (var1 = new StringBuffer()).append("[");
        Iterator var3 = var0.iterator();

        while(var3.hasNext()) {
            Short var2 = (Short)var3.next();
            var1.append(toString(var2) + ";");
        }

        var1.append("}");
        return var1.toString();
    }

    public static short getCollectionType(short var0) {
        return isValidType(var0) && getInfo(var0).isDoor() ? 122 : var0;
    }

    public static boolean isDoor(short var0) {
        return isValidType(var0) && getInfo(var0).isDoor();
    }

    public static boolean isMacroFactory(short var0) {
        return var0 == 211 || var0 == 217 || var0 == 259;
    }

    public static ElementInformation[] getInfoArray() {
        return infoArray;
    }

    public static void setInfoArray(ElementInformation[] var0) {
        infoArray = var0;
    }

    public static boolean isGroupCompatible(short var0, short var1) {
        return isValidType(var0) && isValidType(var1) && getInfo(var0).getInventoryGroup().length() > 0 && getInfo(var0).getInventoryGroup().equals(getInfo(var1).getInventoryGroup());
    }

    public Class<? extends GeneralElement> getTypeFromString(String var1) {
        if (var1.equals("terrain")) {
            return GeneralElement.class;
        } else if (var1.equals("ship")) {
            return ShipElement.class;
        } else if (var1.equals("spacestation")) {
            return SpaceStationElement.class;
        } else if (var1.equals("deathstar")) {
            return SpaceStationElement.class;
        } else if (var1.equals("logic")) {
            return LogicElement.class;
        } else {
            throw new ElementTypeNotFoundException(var1);
        }
    }

    public static boolean isInvisible(short var0) {
        return var0 == 411;
    }

    public static boolean canOpen(short var0) {
        return var0 == 120 || var0 == 114 || var0 == 677 || getFactorykeyset().contains(var0);
    }

    public static void createBlockStyleReferencesFromInvGroup(ElementInformation var0) throws ParseException {
        ObjectArrayList var1 = new ObjectArrayList();

        for(int var2 = 0; var2 < infoArray.length; ++var2) {
            ElementInformation var3;
            if ((var3 = infoArray[var2]) != null && var3 != var0 && var3.slab == 0 && var3.inventoryGroup != null && var3.inventoryGroup.length() > 0 && var3.inventoryGroup.equals(var0.inventoryGroup)) {
                var1.add(var3);
            }
        }

        short[] var6;
        int var7;
        if (var0.wildcardIds != null) {
            var7 = (var6 = var0.wildcardIds).length;

            for(int var4 = 0; var4 < var7; ++var4) {
                short var5 = var6[var4];
                if (!var1.contains(getInfo(var5))) {
                    var1.add(getInfo(var5));
                }
            }
        }

        var6 = new short[var1.size()];

        for(var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = ((ElementInformation)var1.get(var7)).id;
        }

        var0.wildcardIds = var6;
    }

    public static void createBlockStyleReferencesFromName(ElementInformation var0) throws ParseException {
        if (var0.getBlockStyle() == BlockStyle.NORMAL) {
            ObjectArrayList var1 = new ObjectArrayList();

            for(int var2 = 0; var2 < infoArray.length; ++var2) {
                ElementInformation var3;
                if ((var3 = infoArray[var2]) != null && var3 != var0 && var3.getBlockStyle() != BlockStyle.NORMAL && var3.getBlockStyle() != BlockStyle.NORMAL24) {
                    if (var3.name.toLowerCase(Locale.ENGLISH).startsWith(var0.name.toLowerCase(Locale.ENGLISH))) {
                        var3.setSourceReference(var0.id);
                        var1.add(var3);
                    }
                } else if (var3 != null && var3 != var0 && var3.slab == 0 && var3.inventoryGroup != null && var3.inventoryGroup.length() > 0 && var3.inventoryGroup.equals(var0.inventoryGroup)) {
                    var1.add(var3);
                }
            }

            short[] var4 = new short[var1.size()];

            for(int var5 = 0; var5 < var4.length; ++var5) {
                var4[var5] = ((ElementInformation)var1.get(var5)).id;
            }

            var0.styleIds = var4;
        }

    }

    public static void deleteBlockStyleReferences(ElementInformation var0) {
        if (var0.styleIds != null) {
            short[] var1;
            int var2 = (var1 = var0.styleIds).length;

            for(int var3 = 0; var3 < var2; ++var3) {
                short var4;
                if (isValidType(var4 = var1[var3])) {
                    getInfoFast(var4).setSourceReference(0);
                }
            }

            var0.styleIds = null;
        }

    }

    public static void deleteWildCardReferences(ElementInformation var0) {
        if (var0.wildcardIds != null) {
            short[] var1;
            int var2 = (var1 = var0.wildcardIds).length;

            for(int var3 = 0; var3 < var2; ++var3) {
                getInfoFast(var1[var3]).setSourceReference(0);
            }

            var0.wildcardIds = null;
        }

    }

    public static void createBlockSlabs(ElementInformation var0) throws ParseException {
        if (var0.getSlab() != 0) {
            throw new ParseException("Cannot create slab of slab");
        } else {
            assert var0.idName != null;

            String var1 = var0.idName + "_QUARTER_SLAB";
            String var2 = var0.idName + "_HALF_SLAB";
            String var3 = var0.idName + "_THREE_QUARTER_SLAB";
            int var5 = insertIntoProperties(var1);
            int var7 = insertIntoProperties(var2);
            int var9 = insertIntoProperties(var3);
            ElementInformation var6 = new ElementInformation(var0, (short)var5, var0.name + " 1/4");
            ElementInformation var8 = new ElementInformation(var0, (short)var7, var0.name + " 1/2");
            ElementInformation var10;
            (var10 = new ElementInformation(var0, (short)var9, var0.name + " 3/4")).name = var0.name + " 3/4";
            var8.name = var0.name + " 1/2";
            var6.name = var0.name + " 1/4";
            var10.slab = 1;
            var8.slab = 2;
            var6.slab = 3;
            var10.setSourceReference(var0.getId());
            var8.setSourceReference(var0.getId());
            var6.setSourceReference(var0.getId());
            var10.shoppable = false;
            var8.shoppable = false;
            var6.shoppable = false;
            var10.inRecipe = false;
            var8.inRecipe = false;
            var6.inRecipe = false;
            var10.orientatable = true;
            var8.orientatable = true;
            var6.orientatable = true;
            var10.producedInFactory = 0;
            var8.producedInFactory = 0;
            var6.producedInFactory = 0;

            try {
                addInformationToExisting(var6);
                addInformationToExisting(var8);
                addInformationToExisting(var10);
                var0.slabIds = new short[]{var10.getId(), var8.getId(), var6.getId()};
            } catch (ParserConfigurationException var4) {
                var4.printStackTrace();
            }
        }
    }

    public static int insertIntoProperties(String var0) {
        assert var0 != null;

        if (properties.containsKey(var0)) {
            return Integer.parseInt(properties.get(var0).toString());
        } else {
            for(int var1 = 500; var1 < 2048; ++var1) {
                if (!properties.values().contains(String.valueOf(var1))) {
                    properties.put(var0, String.valueOf(var1));
                    writePropertiesOrdered();
                    return var1;
                }
            }

            throw new NullPointerException("No Block ID Free");
        }
    }

    public static void removeByIdName(String var0, boolean var1) {
        if (properties.containsKey(var0)) {
            int var2;
            if (var1) {
                var2 = Integer.parseInt(properties.remove(var0).toString());
                writePropertiesOrdered();
            } else {
                var2 = Integer.parseInt(properties.get(var0).toString());
            }

            if (exists(var2)) {
                removeFromExisting(getInfo(var2));
            }
        }

    }

    public static boolean isLodShape(int var0) {
        return var0 > 0 && var0 < lodShapeArray.length && lodShapeArray[var0];
    }

    public static void main(String[] var0) throws IOException {
        ElementKeyMap.LinkedProperties var7 = new ElementKeyMap.LinkedProperties();
        FileInputStream var1 = new FileInputStream("./data/config/BlockTypes.properties");
        var7.load(var1);
        IntOpenHashSet var8 = new IntOpenHashSet();
        Iterator var2 = var7.values().iterator();

        while(var2.hasNext()) {
            Object var3 = var2.next();

            try {
                var8.add(Integer.parseInt(var3.toString()));
            } catch (NumberFormatException var5) {
            }
        }

        var2 = var7.entrySet().iterator();

        while(var2.hasNext()) {
            Entry var9 = (Entry)var2.next();

            try {
                Integer.parseInt(var9.getValue().toString());
            } catch (NumberFormatException var6) {
                for(int var4 = 100; var4 < 1000000; ++var4) {
                    if (!var8.contains(var4)) {
                        var9.setValue(String.valueOf(var4));
                        var8.add(var4);
                        break;
                    }
                }
            }
        }

        System.err.println("CHK: " + var7.size());
        var7.store(new FileWriter("./data/config/BlockTypes.properties"), "");
    }

    public static void writePropertiesOrdered() {
        try {
            Properties var0;
            (var0 = new Properties() {
                private static final long serialVersionUID = 1L;

                public final synchronized Enumeration<Object> keys() {
                    ArrayList var1 = new ArrayList();
                    Iterator var2 = super.entrySet().iterator();

                    while(var2.hasNext()) {
                        Entry var3 = (Entry)var2.next();
                        var1.add(var3);
                    }

                    Collections.sort(var1, new Comparator<Entry<Object, Object>>() {
                        public int compare(Entry<Object, Object> var1, Entry<Object, Object> var2) {
                            return Integer.parseInt(var1.getValue().toString()) - Integer.parseInt(var2.getValue().toString());
                        }
                    });
                    ArrayList var5 = new ArrayList();
                    Iterator var6 = var1.iterator();

                    while(var6.hasNext()) {
                        Entry var4 = (Entry)var6.next();
                        var5.add(var4.getKey());
                    }

                    return Collections.enumeration(var5);
                }
            }).putAll(properties);
            var0.store(new FileWriter(propertiesPath), "");
        } catch (FileNotFoundException var1) {
            var1.printStackTrace();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public static void deleteBlockSlabs(ElementInformation var0) {
        String var1 = var0.idName + "_QUARTER_SLAB";
        String var2 = var0.idName + "_HALF_SLAB";
        String var3 = var0.idName + "_THREE_QUARTER_SLAB";
        removeByIdName(var1, false);
        removeByIdName(var2, false);
        removeByIdName(var3, false);
    }

    public static boolean isToStashConnectable(short var0) {
        return var0 == 677 || var0 == 347 || var0 == 41 || var0 == 39 || var0 == 4;
    }

    public static boolean isReactor(short var0) {
        return var0 == 1008 || var0 == 1009 || var0 == 1010 || isChamber(var0);
    }

    public static boolean isChamber(short var0) {
        return isValidType(var0) && getInfoFast(var0).isReactorChamberAny();
    }

    public static boolean isInit() {
        return initialized;
    }

    public static boolean isRailLoadOrUnload(short var0) {
        return var0 == 1104 || var0 == 1105;
    }

    public static ElementInformation getMultiBaseType(short var0) {
        if (isValidType(var0)) {
            ElementInformation var1;
            if ((var1 = getInfo(var0)).getSourceReference() != 0) {
                var1 = getInfo(var1.getSourceReference());
            }

            if (var1.blocktypeIds != null) {
                return var1;
            }
        }

        return null;
    }

    public static short convertToByteHP(short var0, int var1) {
        return getInfo(var0).convertToByteHp(var1);
    }

    public static int convertToFullHP(short var0, short var1) {
        return getInfo(var0).convertToFullHp(var1);
    }

    public static short convertSourceReference(short var0) {
        if (isValidType(var0)) {
            short var1;
            return (var1 = (short)getInfoFast(var0).getSourceReference()) != 0 ? var1 : var0;
        } else {
            return var0;
        }
    }

    public static boolean isButton(short var0) {
        return isValidType(var0) && getInfoFast(var0).button;
    }

    public static short[] getSignalTypesActivatedOnSurround() {
        return signaledByRailArray;
    }

    public static boolean isBeacon(short var0) {
        return isValidType(var0) && infoArray[var0].beacon;
    }

    static {
        resources[0] = 480;
        resources[1] = 481;
        resources[2] = 482;
        resources[3] = 483;
        resources[4] = 484;
        resources[5] = 485;
        resources[6] = 486;
        resources[7] = 487;
        resources[8] = 488;
        resources[9] = 489;
        resources[10] = 490;
        resources[11] = 491;
        resources[12] = 492;
        resources[13] = 493;
        resources[14] = 494;
        resources[15] = 495;
        orientationToResIDMapping[1] = 480;
        orientationToResIDMapping[2] = 481;
        orientationToResIDMapping[3] = 482;
        orientationToResIDMapping[4] = 483;
        orientationToResIDMapping[5] = 484;
        orientationToResIDMapping[6] = 485;
        orientationToResIDMapping[7] = 486;
        orientationToResIDMapping[8] = 487;
        orientationToResIDMapping[9] = 488;
        orientationToResIDMapping[10] = 489;
        orientationToResIDMapping[11] = 490;
        orientationToResIDMapping[12] = 491;
        orientationToResIDMapping[13] = 492;
        orientationToResIDMapping[14] = 493;
        orientationToResIDMapping[15] = 494;
        orientationToResIDMapping[16] = 495;
        resIDToOrientationMapping[480] = 1;
        resIDToOrientationMapping[481] = 2;
        resIDToOrientationMapping[482] = 3;
        resIDToOrientationMapping[483] = 4;
        resIDToOrientationMapping[484] = 5;
        resIDToOrientationMapping[485] = 6;
        resIDToOrientationMapping[486] = 7;
        resIDToOrientationMapping[487] = 8;
        resIDToOrientationMapping[488] = 9;
        resIDToOrientationMapping[489] = 10;
        resIDToOrientationMapping[490] = 11;
        resIDToOrientationMapping[491] = 12;
        resIDToOrientationMapping[492] = 13;
        resIDToOrientationMapping[493] = 14;
        resIDToOrientationMapping[494] = 15;
        resIDToOrientationMapping[495] = 16;
        orientationToResOverlayMapping[1] = 1;
        orientationToResOverlayMapping[2] = 2;
        orientationToResOverlayMapping[3] = 3;
        orientationToResOverlayMapping[4] = 4;
        orientationToResOverlayMapping[5] = 5;
        orientationToResOverlayMapping[6] = 6;
        orientationToResOverlayMapping[7] = 7;
        orientationToResOverlayMapping[8] = 8;
        orientationToResOverlayMapping[9] = 9;
        orientationToResOverlayMapping[10] = 10;
        orientationToResOverlayMapping[11] = 11;
        orientationToResOverlayMapping[12] = 12;
        orientationToResOverlayMapping[13] = 13;
        orientationToResOverlayMapping[14] = 14;
        orientationToResOverlayMapping[15] = 15;
        orientationToResOverlayMapping[16] = 16;
    }

    public static class LinkedProperties extends Properties {
        private static final long serialVersionUID = 1L;
        private final Set<Object> keys = new LinkedHashSet();

        public LinkedProperties() {
        }

        public Iterable<Object> orderedKeys() {
            return Collections.list(this.keys());
        }

        public Enumeration<Object> keys() {
            return Collections.enumeration(this.keys);
        }

        public Object put(Object var1, Object var2) {
            this.keys.add(var1);
            return super.put(var1, var2);
        }
    }
}
