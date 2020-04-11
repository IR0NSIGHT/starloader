//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller;

import it.unimi.dsi.fastutil.longs.Long2ShortOpenHashMap;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer.Hos;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.controller.elements.BlockMetaDataDummy;
import org.schema.game.common.controller.elements.ManagerActivityInterface;
import org.schema.game.common.controller.elements.ManagerReloadInterface;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;

public interface PlayerUsableInterface extends HandleControlInterface {
    long MIN_USABLE = -9223372036854774808L;
    long USABLE_ID_CLOAK = -9223372036854775807L;
    long USABLE_ID_JAM = -9223372036854775806L;
    long USABLE_ID_POWER_BATTERY = -9223372036854775805L;
    long USABLE_ID_ACTIVATION_BEAM = -9223372036854775804L;
    long USABLE_ID_THRUSTER = -9223372036854775803L;
    long USABLE_ID_JUMP = -9223372036854775802L;
    long USABLE_ID_SCAN_REACTOR = -9223372036854775801L;
    long USABLE_ID_STEALTH_REACTOR = -9223372036854775800L;
    long USABLE_ID_REPULSOR = -9223372036854775799L;
    long USABLE_ID_NULL_EFFECT = -9223372036854775798L;
    long USABLE_ID_CONTROLLESS = -9223372036854775797L;
    long USABLE_ID_THRUSTER_OUTAGE = -9223372036854775796L;
    long USABLE_ID_NO_POWER_RECHARGE = -9223372036854775795L;
    long USABLE_ID_NO_SHIELD_RECHARGE = -9223372036854775794L;
    long USABLE_ID_PUSH = -9223372036854775793L;
    long USABLE_ID_PULL = -9223372036854775792L;
    long USABLE_ID_STOP = -9223372036854775791L;
    long USABLE_ID_STATUS_ARMOR_HARDEN = -9223372036854775790L;
    long USABLE_ID_STATUS_PIERCING_PROTECTION = -9223372036854775789L;
    long USABLE_ID_STATUS_POWER_SHIELD = -9223372036854775788L;
    long USABLE_ID_STATUS_SHIELD_HARDEN = -9223372036854775787L;
    long USABLE_ID_STATUS_ARMOR_HP_DEDUCTION_BONUS = -9223372036854775786L;
    long USABLE_ID_STATUS_ARMOR_HP_ABSORPTION_BONUS = -9223372036854775785L;
    long USABLE_ID_STATUS_STATUS_TOP_SPEED = -9223372036854775784L;
    long USABLE_ID_STATUS_STATUS_ANTI_GRAVITY = -9223372036854775783L;
    long USABLE_ID_STATUS_GRAVITY_EFFECT_IGNORANCE = -9223372036854775782L;
    long USABLE_ID_TAKE_OFF = -9223372036854775781L;
    long USABLE_ID_EVADE = -9223372036854775780L;
    long USABLE_ID_SHOOT_TURRETS = -9223372036854775779L;
    long USABLE_ID_REACTOR_BOOST = -9223372036854775778L;
    long USABLE_ID_INTERDICTION = -9223372036854775777L;
    long USABLE_ID_MINE_SHOOTER = -9223372036854775776L;
    long USABLE_ID_UNDOCK = -9223372036854775775L;
    long[] ALWAYS_SELECTED = new long[]{-9223372036854775803L};
    Long2ShortOpenHashMap ICONS = new Long2ShortOpenHashMap(
            new long[]{-9223372036854775807L, -9223372036854775806L, -9223372036854775805L, -9223372036854775804L, -9223372036854775802L, -9223372036854775801L, -9223372036854775800L, -9223372036854775799L, -9223372036854775779L, -9223372036854775778L, -9223372036854775777L, -9223372036854775781L, -9223372036854775780L, -9223372036854775775L},
            new short[]{22, 15, 978, 405, 544, 654, 22, 1126, 665, 1008, 681, 8, 463, 663});
    WeaponRowElementInterface getWeaponRow();

    boolean isControllerConnectedTo(long var1, short var3);

    boolean isPlayerUsable();

    long getUsableId();

    ManagerReloadInterface getReloadInterface();

    ManagerActivityInterface getActivityInterface();

    String getName();

    boolean isAddToPlayerUsable();

    void onPlayerDetachedFromThisOrADock(ManagedUsableSegmentController<?> var1, PlayerState var2, PlayerControllable var3);

    void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2);

    void addHudConext(ControllerStateUnit var1, HudContextHelpManager var2, Hos var3);

    float getWeaponSpeed();

    float getWeaponDistance();

    void handleMouseEvent(ControllerStateUnit var1, MouseEvent var2);

    void onSwitched(boolean var1);

    void onLogicActivate(SegmentPiece var1, boolean var2, Timer var3);
}
