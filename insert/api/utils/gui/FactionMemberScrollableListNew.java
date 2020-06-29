//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.utils.gui;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Observer;
import java.util.Set;
import org.hsqldb.lib.StringComparator;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.controller.PlayerGameOkCancelInput;
import org.schema.game.client.controller.PlayerMailInputNew;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton.ColorPalette;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.GuiDateFormats;
import org.schema.schine.graphicsengine.forms.gui.newgui.config.PlayerStatusColorPalette;
import org.schema.schine.input.InputState;

public class FactionMemberScrollableListNew extends ScrollableTableList<FactionPermission> implements Observer {
    private Faction faction;

    public FactionMemberScrollableListNew(InputState var1, GUIElement var2, Faction var3) {
        super(var1, 100.0F, 100.0F, var2);
        this.faction = var3;
        this.getState().getFactionManager().addObserver(this);
    }

    public void cleanUp() {
        this.getState().getFactionManager().deleteObserver(this);
        super.cleanUp();
    }

    public void initColumns() {
        new StringComparator();
        this.addColumn("Name", 3.0F, new Comparator<FactionPermission>() {
            public int compare(FactionPermission var1, FactionPermission var2) {
                return var1.playerUID.compareToIgnoreCase(var2.playerUID);
            }
        });
        this.addColumn("Status", 1.0F, new Comparator<FactionPermission>() {
            public int compare(FactionPermission var1, FactionPermission var2) {
                int var3 = FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().containsKey(var1.playerUID.toLowerCase(Locale.ENGLISH)) ? 2 : (var1.isActiveMember() ? 1 : 0);
                int var4 = FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().containsKey(var2.playerUID.toLowerCase(Locale.ENGLISH)) ? 2 : (var2.isActiveMember() ? 1 : 0);
                return var3 - var4;
            }
        });
        this.addColumn("Rank", 1.0F, new Comparator<FactionPermission>() {
            public int compare(FactionPermission var1, FactionPermission var2) {
                return var1.role - var2.role;
            }
        });
        this.addColumn("Pos", 1.0F, new Comparator<FactionPermission>() {
            public int compare(FactionPermission var1, FactionPermission var2) {
                Vector3i var3 = new Vector3i(FactionMemberScrollableListNew.this.getState().getPlayer().getCurrentSector());
                PlayerState var8 = (PlayerState)FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().get(var1.playerUID.toLowerCase(Locale.ENGLISH));
                PlayerState var9 = (PlayerState)FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().get(var2.playerUID.toLowerCase(Locale.ENGLISH));
                double var4 = var8 != null ? (double)Vector3i.getDisatance(var3, var8.getCurrentSector()) : 2.147483547E9D;
                double var6 = var9 != null ? (double)Vector3i.getDisatance(var3, var9.getCurrentSector()) : 2.147483547E9D;
                if (var4 > var6) {
                    return 1;
                } else {
                    return var4 < var6 ? -1 : 0;
                }
            }
        });
        this.addTextFilter(new GUIListFilterText<FactionPermission>() {
            public boolean isOk(String var1, FactionPermission var2) {
                return var2.playerUID.toLowerCase(Locale.ENGLISH).contains(var1.toLowerCase(Locale.ENGLISH));
            }
        }, ControllerElement.FilterRowStyle.FULL);
    }
    //




    ///

    protected Collection<FactionPermission> getElementList() {
        return this.faction.getMembersUID().values();
    }

    public void updateListEntries(GUIElementList list, Set<FactionPermission> entries) {
        list.deleteObservers();
        list.addObserver(this);

        for (final FactionPermission player : entries) {
            GUITextOverlayTable var4 = new GUITextOverlayTable(10, 10, this.getState());
            GUITextOverlayTable var5 = new GUITextOverlayTable(10, 10, this.getState()) {
                public void draw() {
                    if (FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().containsKey(player.playerUID.toLowerCase(Locale.ENGLISH))) {
                        if (player.isActiveMember()) {
                            this.setColor(PlayerStatusColorPalette.onlineActive);
                        } else {
                            this.setColor(PlayerStatusColorPalette.onlineInactive);
                        }
                    } else if (player.isActiveMember()) {
                        this.setColor(PlayerStatusColorPalette.offlineActive);
                    } else {
                        this.setColor(PlayerStatusColorPalette.offlineInactive);
                    }

                    super.draw();
                }
            };
            GUITextOverlayTable var6 = new GUITextOverlayTable(10, 10, this.getState());
            GUITextOverlayTable var7 = new GUITextOverlayTable(10, 10, this.getState());
            var4.setTextSimple(player.playerUID);
            var5.setTextSimple(new Object() {
                public String toString() {
                    if (FactionMemberScrollableListNew.this.getState().getOnlinePlayersLowerCaseMap().containsKey(player.playerUID.toLowerCase(Locale.ENGLISH))) {
                        return player.isActiveMember() ? Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_4 : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_5;
                    } else {
                        return player.isActiveMember() ? Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_6 : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_7;
                    }
                }
            });
            var6.setTextSimple(new Object() {
                public String toString() {
                    return FactionMemberScrollableListNew.this.faction.getRoles().getRoles()[player.role].name;
                }
            });
            var7.setTextSimple(new Object() {
                public String toString() {
                    return player.lastSeenPosition.x + ", " + player.lastSeenPosition.y + ", " + player.lastSeenPosition.z;
                }
            });
            var4.getPos().y = 5.0F;
            var5.getPos().y = 5.0F;
            var6.getPos().y = 5.0F;
            var7.getPos().y = 5.0F;
            FactionRow row;
            (row = new FactionRow(this.getState(), player, new GUIElement[]{var4, var5, var6, var7})).expanded = new GUIElementList(this.getState());
            GUITextOverlayTableInnerDescription var14;
            (var14 = new GUITextOverlayTableInnerDescription(10, 10, this.getState())).setTextSimple(new Object() {
                public String toString() {
                    return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_29 + GuiDateFormats.factionMemberLastSeenTime.format(player.lastSeenTime);
                }
            });
            GUIAncor anchor = new GUIAncor(this.getState(), 100.0F, 30.0F);
            GUITextButton var16 = new GUITextButton(this.getState(), 50, 24, ColorPalette.OK, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_8, new GUICallback() {
                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        (new PlayerMailInputNew(FactionMemberScrollableListNew.this.getState(), player.playerUID, "")).activate();
                    }

                }

                public boolean isOccluded() {
                    return !FactionMemberScrollableListNew.this.isActive();
                }
            });
            GUITextButton var8 = new GUITextButton(this.getState(), 60, 24, ColorPalette.OK, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_30, new GUICallback() {
                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        FactionMemberScrollableListNew.this.getState().getController().getClientGameData().setWaypoint(player.lastSeenPosition);
                    }

                }

                public boolean isOccluded() {
                    return !FactionMemberScrollableListNew.this.isActive();
                }
            });
            GUITextButton var9 = new GUITextButton(this.getState(), 60, 24, ColorPalette.OK, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_9, new GUICallback() {
                public boolean isOccluded() {
                    return !FactionMemberScrollableListNew.this.isActive();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        FactionPermission var3x;
                        if (!(var3x = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(FactionMemberScrollableListNew.this.getState().getPlayer().getName())).hasPermissionEditPermission(FactionMemberScrollableListNew.this.faction)) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_17, 0.0F);
                            return;
                        }

                        if (var3x.role == player.role) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_11, 0.0F);
                            return;
                        }

                        if (var3x.role < player.role) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_18, 0.0F);
                            return;
                        }

                        if (player.role >= FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_13, 0.0F);
                            return;
                        }

                        if (player.role + 1 >= FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1) {
                            (new PlayerGameOkCancelInput("CONFIRM", FactionMemberScrollableListNew.this.getState(), Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_14, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_15) {
                                public void onDeactivate() {
                                }

                                public void pressedOK() {
                                    FactionMemberScrollableListNew.this.faction.addOrModifyMemberClientRequest(this.getState().getPlayer().getName(), player.playerUID, (byte) (player.role + 1), this.getState().getGameState());
                                    this.deactivate();
                                }
                            }).activate();
                            return;
                        }

                        FactionMemberScrollableListNew.this.faction.addOrModifyMemberClientRequest(FactionMemberScrollableListNew.this.getState().getPlayer().getName(), player.playerUID, (byte) (player.role + 1), FactionMemberScrollableListNew.this.getState().getGameState());
                    }

                }
            }) {
                public void draw() {
                    FactionPermission var1;
                    if ((var1 = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(((GameClientState) this.getState()).getPlayer().getName())).hasPermissionEditPermission(FactionMemberScrollableListNew.this.faction) && var1.role > player.role && player.role < FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1) {
                        super.draw();
                    }

                }
            };
            GUITextButton var10 = new GUITextButton(this.getState(), 60, 24, ColorPalette.CANCEL, "DEmote", new GUICallback() {
                public boolean isOccluded() {
                    return !FactionMemberScrollableListNew.this.isActive();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        FactionPermission var3x;
                        if ((var3x = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(FactionMemberScrollableListNew.this.getState().getPlayer().getName())) == null || !var3x.hasPermissionEditPermission(FactionMemberScrollableListNew.this.faction)) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_10, 0.0F);
                            return;
                        }

                        if (var3x.role < player.role) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_12, 0.0F);
                            return;
                        }

                        if (player.role == 0) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_19, 0.0F);
                            return;
                        }

                        if (player.role >= FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1 && (var3x != player || player.isOverInactiveLimit(FactionMemberScrollableListNew.this.getState()))) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_20, 0.0F);
                            return;
                        }

                        if (player.role >= FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1) {
                            (new PlayerGameOkCancelInput("CONFIRM", FactionMemberScrollableListNew.this.getState(), Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_27, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_22) {
                                public void pressedOK() {
                                    FactionMemberScrollableListNew.this.faction.addOrModifyMemberClientRequest(this.getState().getPlayer().getName(), player.playerUID, (byte) (player.role - 1), this.getState().getGameState());
                                    this.deactivate();
                                }

                                public void onDeactivate() {
                                }
                            }).activate();
                            return;
                        }

                        FactionMemberScrollableListNew.this.faction.addOrModifyMemberClientRequest(FactionMemberScrollableListNew.this.getState().getPlayer().getName(), player.playerUID, (byte) (player.role - 1), FactionMemberScrollableListNew.this.getState().getGameState());
                    }

                }
            }) {
                public void draw() {
                    FactionPermission var1;
                    if ((var1 = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(((GameClientState) this.getState()).getPlayer().getName())) != null && var1.hasPermissionEditPermission(FactionMemberScrollableListNew.this.faction) && (player.role >= FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1 || var1.role > player.role) && player.role != 0 && (player.role < FactionMemberScrollableListNew.this.faction.getRoles().getRoles().length - 1 || var1 == player && !player.isOverInactiveLimit((GameStateInterface) this.getState()))) {
                        super.draw();
                    }

                }
            };
            GUITextButton var12 = new GUITextButton(this.getState(), 60, 24, ColorPalette.CANCEL, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_23, new GUICallback() {
                public boolean isOccluded() {
                    return !FactionMemberScrollableListNew.this.isActive();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        FactionPermission var3x = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(FactionMemberScrollableListNew.this.getState().getPlayer().getName());
                        System.err.println("[CLIENT] PERMISSION (will be rechecked on server): " + var3x.toString(FactionMemberScrollableListNew.this.faction));
                        if (var3x == null || !var3x.hasKickPermission(FactionMemberScrollableListNew.this.faction)) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_24, 0.0F);
                            return;
                        }

                        if (var3x.role <= player.role) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_25, 0.0F);
                            return;
                        }

                        if (player.isFounder(FactionMemberScrollableListNew.this.faction)) {
                            FactionMemberScrollableListNew.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_26, 0.0F);
                            return;
                        }

                        (new PlayerGameOkCancelInput("CONFIRM", FactionMemberScrollableListNew.this.getState(), Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_21, StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_FACTION_NEWFACTION_FACTIONMEMBERSCROLLABLELISTNEW_28, new Object[]{player.playerUID})) {
                            public void pressedOK() {
                                FactionMemberScrollableListNew.this.faction.kickMemberClientRequest(this.getState().getPlayer().getName(), player.playerUID, this.getState().getGameState());
                                this.deactivate();
                            }

                            public void onDeactivate() {
                            }
                        }).activate();
                    }

                }
            }) {
                public void draw() {
                    FactionPermission var1;
                    if ((var1 = (FactionPermission) FactionMemberScrollableListNew.this.faction.getMembersUID().get(((GameClientState) this.getState()).getPlayer().getName())) != null && var1.hasKickPermission(FactionMemberScrollableListNew.this.faction) && var1.role > player.role && !player.isFounder(FactionMemberScrollableListNew.this.faction)) {
                        super.draw();
                    }

                }
            };
            anchor.attach(var16);
            anchor.attach(var8);
            anchor.attach(var9);
            anchor.attach(var10);
            anchor.attach(var12);
            var16.setPos(0.0F, anchor.getHeight(), 0.0F);
            var8.setPos(var16.getWidth() + 10.0F, anchor.getHeight(), 0.0F);
            var9.setPos(var16.getWidth() + 10.0F + var8.getWidth() + 10.0F, anchor.getHeight(), 0.0F);
            var10.setPos(var16.getWidth() + 10.0F + var9.getWidth() + 10.0F + var8.getWidth() + 10.0F, anchor.getHeight(), 0.0F);
            var12.setPos(var8.getWidth() + 10.0F + var16.getWidth() + 10.0F + var9.getWidth() + 10.0F + var10.getWidth() + 10.0F, anchor.getHeight(), 0.0F);
            anchor.attach(var14);

            row.expanded.add(new GUIListElement(anchor, anchor, this.getState()));
            row.onInit();
            list.addWithoutUpdate(row);
        }

        list.updateDim();
    }

    public GameClientState getState() {
        return (GameClientState)super.getState();
    }

    class FactionRow extends ScrollableTableList<FactionPermission>.Row {
        public FactionRow(InputState var2, FactionPermission var3, GUIElement... var4) {
            super(var2, var3, var4);
            this.highlightSelect = true;
        }
    }
}
