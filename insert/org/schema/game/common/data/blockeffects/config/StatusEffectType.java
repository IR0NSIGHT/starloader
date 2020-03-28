//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.blockeffects.config;

import java.util.Locale;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectBooleanValue;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectFloatValue;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectIntValue;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectParameter;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectWeaponType;
import org.schema.schine.common.language.Lng;

public enum StatusEffectType {
    SHIELD_RECHARGE_RATE(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_0;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_CAPACITY(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_1;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_CAPACITY_UPKEEP(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_147;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_UNDER_FIRE_RECHARGE_NERF(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_2;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_UNDER_FIRE_TIMEOUT(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_3;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_ZERO_SHIELDS_TIMEOUT(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_117;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_DAMAGE_RESISTANCE(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_4;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    SHIELD_HEAT_DAMAGE_TAKEN(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_74;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_HOTSPOT_ALPHA(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_118;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    SHIELD_HOTSPOT_DPS(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_119;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    SHIELD_HOTSPOT_PERCENTAGE(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_120;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_HOTSPOT_RANGE(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_81;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_HOTSPOT_RECHARGE_MODE(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_148;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    SHIELD_DEFENSE_KINETIC(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_137;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_DEFENSE_HEAT(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_138;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIELD_DEFENSE_EM(StatusEffectCategory.SHIELDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_141;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_TOP_SPEED(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_5;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_ACCELERATION(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_6;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_BRAKING(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_7;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_TURN_RATE(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_8;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_ANTI_GRAVITY(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_9;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_POWER_CONSUMPTION(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_10;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_DAMPENING(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_11;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_CONFIG_CHANGE_TIMEOUT(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_12;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_BLAST_STRENGTH(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_107;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_BLAST_POWER_CONSUMPTION_CHARGING(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_127;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_BLAST_COOLDOWN(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_128;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    THRUSTER_BLAST(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_14;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    THRUSTER_EVADE(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_15;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    THRUSTER_BLAST_MULTI_CHARGE_COUNT(StatusEffectCategory.THRUSTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_20;
        }
    }, true, new Class[]{StatusEffectIntValue.class}),
    POWER_RECHARGE_EFFICIENCY(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_13;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    POWER_MODULE_CHARGING_RATE_MOD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_22;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    POWER_TOP_OFF_RATE_MOD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_23;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    POWER_STABILIZER_DISTANCE(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_16;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    POWER_CONDUIT_POWER_USAGE(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_17;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_FAILSAFE(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_42;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    REACTOR_FAILSAFE_THRESHOLD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_110;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_FAILSAFE_HPPERCENT_MIN_TARGET_THRESHOLD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_136;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_BOOST(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_100;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    REACTOR_BOOST_STRENGTH(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_111;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_BOOST_DURATION(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_112;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_BOOST_COOLDOWN(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_113;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_SHIELD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_101;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    CHAMBER_SHIELD(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_102;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    REACTOR_EXPLOSIVENESS(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_103;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_SHIELD_CAPACITY(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_31;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    REACTOR_SHIELD_POWER_CONSUMPTION(StatusEffectCategory.POWER, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_104;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    JUMP_DISTANCE(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_44;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    JUMP_CHARGE_TIME(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_79;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    JUMP_AUTO_CHARGE(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_27;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    JUMP_POWER_CHARGE_RATE(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_80;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    JUMP_POWER_TOPOFF_RATE(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_84;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    JUMP_MULTI_CHARGE_COUNT(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_105;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    JUMP_DRIVE_ENABLE(StatusEffectCategory.JUMP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_134;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    WARP_DISTANCE(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_18;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    WARP_POWER_EFFICIENCY(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_30;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARP_FREE_TARGET(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_33;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    WARP_INTERDICTION(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_21;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    WARP_INTERDICTION_DISTANCE(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_106;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    WARP_INTERDICTION_COOLDOWN(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_114;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    WARP_INTERDICTION_STRENGTH(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_115;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    WARP_INTERDICTION_POWER_CONSUMPTION(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_116;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    WARP_INTERDICTION_ACTIVE_RESTING_POWER_CONS(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_121;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARP_INTERDICTION_INACTIVE_RESTING_POWER_CONS(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_122;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARP_INTERDICTION_ACTIVE(StatusEffectCategory.WARP, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_129;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    SCAN_STRENGTH(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_34;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    SCAN_POWER_CHARGE_RATE(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_92;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SCAN_POWER_TOPOFF_RATE(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_47;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SCAN_USAGE_TIME(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_67;
        }
    }, true, true, new Class[]{StatusEffectFloatValue.class}),
    SCAN_CHARGE_TIME(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_25;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SCAN_ACTIVE_RESTING_POWER_CONS(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_123;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    SCAN_INACTIVE_RESTING_POWER_CONS(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_124;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    SCAN_ACTIVE_RESTING_POWER_CONS_MULT(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_130;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SCAN_INACTIVE_RESTING_POWER_CONS_MULT(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_131;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ORE_SCANNER(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_66;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    CARGO_SCANNER(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_69;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    SCAN_LONG_RANGE_DISTANCE(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_63;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    SCAN_LONG_RANGE_SCANNER(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_65;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    SCAN_SHORT_RANGE_SCANNER_ENABLE(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_135;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    ANOMALY_SCANNER(StatusEffectCategory.SCANNERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_72;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    JAM_POWER(StatusEffectCategory.OLD_RADARJAM, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_29;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    JAM_POWER_EFFICIENCY(StatusEffectCategory.OLD_RADARJAM, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_26;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CARGO_WEIGHT(StatusEffectCategory.CARGO, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_36;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CARGO_VOLUME(StatusEffectCategory.CARGO, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_43;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CARGO_DAMAGE_RESISTANCE(StatusEffectCategory.CARGO, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_38;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_STRENGTH(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_61;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_POWER_CHARGE_RATE(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_32;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_POWER_TOPOFF_RATE(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_41;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_USAGE_TIME(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_35;
        }
    }, true, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_CHARGE_TIME(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_40;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_MISSILE_LOCK_ON_TIME(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_70;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_JAMMER_CAPABILITY(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_71;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    STEALTH_CLOAK_CAPABILITY(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_73;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    STEALTH_ACTIVE_RESTING_POWER_CONS(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_125;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    STEALTH_INACTIVE_RESTING_POWER_CONS(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_126;
        }
    }, true, new Class[]{StatusEffectBooleanValue.class}),
    STEALTH_ACTIVE_RESTING_POWER_CONS_MULT(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_132;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    STEALTH_INACTIVE_RESTING_POWER_CONS_MULT(StatusEffectCategory.STEALTH, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_133;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CLOAK_TIME(StatusEffectCategory.OLD_CLOAKERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_39;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CLOAK_CHARGE_TIME(StatusEffectCategory.OLD_CLOAKERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_19;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CLOAK_POWER_CHARGE_RATE(StatusEffectCategory.OLD_CLOAKERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_46;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CLOAK_POWER_TOPOFF_RATE(StatusEffectCategory.OLD_CLOAKERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_78;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CLOAK_STRENGTH(StatusEffectCategory.OLD_CLOAKERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_37;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    TRANSPORTER_DISTANCE(StatusEffectCategory.TRANSPORTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_24;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    TRANSPORTER_NO_SHIELD_DOWN(StatusEffectCategory.TRANSPORTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_45;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    TRANSPORTER_POWER_CHARGE_RATE(StatusEffectCategory.TRANSPORTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_108;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    TRANSPORTER_POWER_TOPOFF_RATE(StatusEffectCategory.TRANSPORTERS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_82;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIPYARD_POWER_EFFICIENCY(StatusEffectCategory.SHIPYARDS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_98;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    FACTORIES_POWER_CHARGE_RATE(StatusEffectCategory.FACTORIES, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_75;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    FACTORIES_POWER_TOPOFF_RATE(StatusEffectCategory.FACTORIES, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_109;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    FACTORY_BAKE_TIME_MULT(StatusEffectCategory.FACTORIES, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_48;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    SHIP_FACTORY(StatusEffectCategory.FACTORIES, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_85;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    MINING_BONUS_ACTIVE(StatusEffectCategory.MINING, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_49;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    MINING_BONUS_PASSIVE(StatusEffectCategory.MINING, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_50;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    MINERAL_TO_RESOURCE(StatusEffectCategory.MINING, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_86;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARHEAD_RADIUS(StatusEffectCategory.WARHEADS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_51;
        }
    }, false, new Class[]{StatusEffectIntValue.class}),
    WARHEAD_DAMAGE(StatusEffectCategory.WARHEADS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_64;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARHEAD_CHANCE_FOR_EXPLOSION_ON_HIT(StatusEffectCategory.WARHEADS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_53;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WARHEAD_CHANCE_FOR_SPONTANIOUS_EXPLODE(StatusEffectCategory.WARHEADS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_54;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    AI_DISABLE(StatusEffectCategory.AI, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_55;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    AI_ACCURACY_TURRET(StatusEffectCategory.AI, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_56;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    AI_ACCURACY_DRONE(StatusEffectCategory.AI, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_57;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    AI_ACCURACY_POINT_DEFENSE(StatusEffectCategory.AI, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_58;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    RAIL_SPEED(StatusEffectCategory.RAILS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_59;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    RAIL_ENHANCER_POWER_EFFICIENCY(StatusEffectCategory.RAILS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_99;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    CAMERA_INTERFERENCE(StatusEffectCategory.VISIBILITY, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_60;
        }
    }, false, new Class[]{StatusEffectFloatValue.class}),
    DAMAGE_TAKEN(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_62;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    HULL_HEAT_DAMAGE_TAKEN(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_87;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    DAMAGE_PHYSICS_DAMAGE_EFFECT_TAKEN(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_88;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    DAMAGE_EMP_DAMAGE_TAKEN(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_89;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    DAMAGE_OUTPUT_MULTIPLIER(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_90;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    GENERAL_DEFENSE_KINETIC(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_139;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    GENERAL_DEFENSE_HEAT(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_140;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    GENERAL_DEFENSE_EM(StatusEffectCategory.DAMAGE, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_142;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    WEAPON_RANGE(StatusEffectCategory.WEAPON, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_28;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    WEAPON_DAMAGE(StatusEffectCategory.WEAPON, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_52;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    WEAPON_TOP_OFF_RATE(StatusEffectCategory.WEAPON, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_91;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    WEAPON_CHARGE_RATE(StatusEffectCategory.WEAPON, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_83;
        }
    }, true, new Class[]{StatusEffectWeaponType.class, StatusEffectFloatValue.class}),
    MASS_MOD(StatusEffectCategory.MASS, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_68;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_HP_EFFICIENCY(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_93;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_HP_ABSORPTION(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_94;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_HP_REGENERATION(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_95;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_DEFENSE_KINETIC(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_143;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_DEFENSE_HEAT(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_144;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_DEFENSE_EM(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_145;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    ARMOR_DEFENSE_ENVIRONMENTAL(StatusEffectCategory.ARMOR, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_146;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    GRAVITY_OVERRIDE_ENTITY_DIR(StatusEffectCategory.GRAVITY, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_76;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    GRAVITY_OVERRIDE_ENTITY_CENTRAL(StatusEffectCategory.GRAVITY, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_97;
        }
    }, true, new Class[]{StatusEffectFloatValue.class}),
    GRAVITY_OVERRIDE_ENTITY_SWITCH(StatusEffectCategory.GRAVITY, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_77;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    BUILD_INHIBITOR(StatusEffectCategory.BUILDING, new Object() {
        public final String toString() {
            return Lng.ORG_SCHEMA_GAME_COMMON_DATA_BLOCKEFFECTS_CONFIG_STATUSEFFECTTYPE_96;
        }
    }, false, new Class[]{StatusEffectBooleanValue.class}),
    //TODO
    // I know this is a really messy way to do this, but i think its the best option for now
    //What i've done before:
    // Turned it into a class, with a bunch of public static variables
    // You could add stuff to it on the fly, all of the enums (values(), valueOf(), etc) worked
    // but switch statements wont work
    // I could go and change all the switch statements, but that is going to be 100000x easier once I get the real source
    CUSTOM_EFFECT_01(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_02(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect 2";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_03(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_04(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_05(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_06(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_07(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_08(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_09(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_10(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_11(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_12(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_13(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_14(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_15(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_16(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_17(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_18(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_19(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_20(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),
    CUSTOM_EFFECT_21(StatusEffectCategory.POWER, new Object(){
        public String toString() {
            return "Custom Effect";
        }
    }, false, StatusEffectFloatValue.class),

    ;

    ///
    public static void register(StatusEffectType type){

    }


    ///

    private StatusEffectCategory category;
    private final boolean percentage;
    public final Object nameObj;
    public final Class<? extends StatusEffectParameter>[] effectParameters;
    public final StatusEffectParameter[] effectParameterInstances;
    public final boolean usageTime;

    StatusEffectType(StatusEffectCategory var3, Object var4, boolean var5, Class<? extends StatusEffectParameter>... var6) {
        this(var3, var4, var5, false, var6);
    }

    private StatusEffectType(StatusEffectCategory var3, Object var4, boolean var5, boolean var6, Class<? extends StatusEffectParameter>... var7) {
        this.category = var3;
        this.nameObj = var4;
        this.effectParameters = var7;
        this.usageTime = var6;
        this.effectParameterInstances = new StatusEffectParameter[var7.length];
        this.percentage = var5;

        for(int var10 = 0; var10 < var7.length; ++var10) {
            Class var11 = var7[var10];

            try {
                this.effectParameterInstances[var10] = (StatusEffectParameter)var11.newInstance();
            } catch (InstantiationException var8) {
                var8.printStackTrace();
            } catch (IllegalAccessException var9) {
                var9.printStackTrace();
            }
        }

    }

    public final String toString() {
        return "[" + this.category.getName() + "]" + this.getName();
    }

    public final String getName() {
        return this.nameObj.toString();
    }

    public static String getAll() {
        StringBuffer var0 = new StringBuffer();

        for(int var1 = 0; var1 < values().length; ++var1) {
            StatusEffectType var2 = values()[var1];
            var0.append(var2.name().toLowerCase(Locale.ENGLISH));
            if (var1 < values().length - 1) {
                var0.append(", ");
            }
        }

        return var0.toString();
    }

    public final StatusEffectCategory getCategory() {
        return this.category;
    }

    public final StatusEffectParameter getInstance(Class<? extends StatusEffectParameter> var1) {
        for(int var2 = 0; var2 < this.effectParameters.length; ++var2) {
            if (this.effectParameters[var2] == var1) {
                return this.effectParameterInstances[var2];
            }
        }

        throw new RuntimeException("no effect Class found " + var1.getName());
    }

    public final String getFullName() {
        return "[" + this.getCategory() + "] " + this.getName();
    }

    public final boolean isPercentage() {
        return this.percentage;
    }

    public final boolean isTimed() {
        return this.usageTime;
    }

    public final boolean respectOnePointZero() {
        return this == REACTOR_BOOST_STRENGTH;
    }

    static {
        assert values().length < 256 : values().length;

    }
}
