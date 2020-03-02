//
// Decompiled by Procyon v0.5.36
//

package org.schema.game.client.view.gui.playerstats;

import api.DebugFile;
import api.mod.StarLoader;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.game.server.data.admin.AdminCommands;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.common.TextCallback;
import org.schema.game.client.controller.PlayerGameTextInput;
import org.schema.game.client.controller.PlayerGameOkCancelInput;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.controller.manager.ingame.PlayerGameControlManager;
import org.schema.game.client.controller.manager.ingame.ship.InShipControlManager;
import org.schema.game.client.controller.manager.ingame.ship.WeaponAssignControllerManager;
import java.util.Locale;
import org.schema.game.common.data.player.catalog.CatalogPermission;
import java.util.Iterator;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITextOverlayTable;
import java.util.Set;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import java.util.Collection;
import java.util.Comparator;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.input.InputState;
import java.util.Observer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList;

public class PlayerStatisticsScrollableListNew extends ScrollableTableList<PlayerState> implements Observer
{
    public PlayerStatisticsScrollableListNew(final InputState inputState, final GUIAncor guiAncor) {
        super(inputState, 100.0f, 100.0f, guiAncor);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initColumns() {
        this.addColumn("Name", 2.0f, new Comparator<PlayerState>() {
            @Override
            public int compare(final PlayerState playerState, final PlayerState playerState2) {
                return playerState.getName().compareToIgnoreCase(playerState2.getName());
            }
        });
        this.addColumn("Faction", 3.0f, new Comparator<PlayerState>() {
            @Override
            public int compare(final PlayerState playerState, final PlayerState playerState2) {
                return playerState.getFactionName().compareToIgnoreCase(playerState2.getFactionName());
            }
        });
        this.addFixedWidthColumn("Ping", 50, new Comparator<PlayerState>() {
            @Override
            public int compare(final PlayerState playerState, final PlayerState playerState2) {
                return playerState.getPing() - playerState2.getPing();
            }
        });
        this.addFixedWidthColumn("Options", 56, new Comparator<PlayerState>() {
            @Override
            public int compare(final PlayerState playerState, final PlayerState playerState2) {
                return 0;
            }
        });
    }

    @Override
    protected Collection<PlayerState> getElementList() {
        return this.getState().getOnlinePlayersLowerCaseMap().values();
    }

    @Override
    public void updateListEntries(final GUIElementList list, final Set<PlayerState> set) {
        list.deleteObservers();
        list.addObserver(this);
        this.getState().getGameState().getFactionManager();
        this.getState().getGameState().getCatalogManager();
        final PlayerState player = this.getState().getPlayer();
        for (final PlayerState playerState : set) {
            DebugFile.log(playerState.getName() + " in sector: " + playerState.getCurrentSector().toString());
            final GUITextOverlayTable guiTextOverlayTable = new GUITextOverlayTable(10, 10, this.getState());
            final GUITextOverlayTable guiTextOverlayTable2 = new GUITextOverlayTable(10, 10, this.getState());
            final GUITextOverlayTable guiTextOverlayTable3 = new GUITextOverlayTable(10, 10, this.getState());
            final GUIElement guiElement;
            (guiElement = new GUIClippedRow(this.getState())).attach(guiTextOverlayTable);
            final GUIElement guiElement2;
            (guiElement2 = new GUIClippedRow(this.getState())).attach(guiTextOverlayTable2);
            final GUIElement guiElement3;
            (guiElement3 = new GUIClippedRow(this.getState())).attach(guiTextOverlayTable3);
            guiTextOverlayTable.getPos().y = 5.0f;
            guiTextOverlayTable2.getPos().y = 5.0f;
            guiTextOverlayTable3.getPos().y = 5.0f;
            guiTextOverlayTable.setTextSimple(playerState.getName());
            guiTextOverlayTable2.setTextSimple(new Object() {
                @Override
                public String toString() {
                    return playerState.getFactionName();
                }
            });
            guiTextOverlayTable3.setTextSimple(new Object() {
                @Override
                public String toString() {
                    return String.valueOf(playerState.getPing());
                }
            });
            assert !guiTextOverlayTable.getText().isEmpty();
            assert !guiTextOverlayTable2.getText().isEmpty();
            assert !guiTextOverlayTable3.getText().isEmpty();
            final GUIAncor guiAncor = new GUIAncor(this.getState(), 50.0f, (float)this.columnsHeight);
            final GUITextButton guiTextButton = new GUITextButton(this.getState(), 50, 20, GUITextButton.ColorPalette.CANCEL, new Object() {
                @Override
                public String toString() {
                    if (PlayerStatisticsScrollableListNew.this.getState().getPlayer().getNetworkObject().isAdminClient.get()) {
                        return "options";
                    }
                    return "-";
                }
            }, new GUICallback() {
                @Override
                public void callback(final GUIElement guiElement, final MouseEvent mouseEvent) {
                    if (mouseEvent.pressedLeftMouse() && PlayerStatisticsScrollableListNew.this.getState().getPlayer().getNetworkObject().isAdminClient.get()) {
                        PlayerStatisticsScrollableListNew.this.pressedAdminOptions(playerState);
                    }
                }

                @Override
                public boolean isOccluded() {
                    return false;
                }
            });
            if (player.getNetworkObject().isAdminClient.get()) {
                guiTextButton.setPos(0.0f, 2.0f, 0.0f);
                guiAncor.attach(guiTextButton);
            }
            final WeaponRow weaponRow;
            (weaponRow = new WeaponRow(this.getState(), playerState, new GUIElement[] { guiElement, guiElement2, guiElement3, guiAncor })).onInit();
            list.addWithoutUpdate(weaponRow);
        }
        list.updateDim();
    }

    public boolean isPlayerAdmin() {
        return this.getState().getPlayer().getNetworkObject().isAdminClient.get();
    }

    public boolean canEdit(final CatalogPermission catalogPermission) {
        return catalogPermission.ownerUID.toLowerCase(Locale.ENGLISH).equals(this.getState().getPlayer().getName().toLowerCase(Locale.ENGLISH)) || this.isPlayerAdmin();
    }

    public WeaponAssignControllerManager getAssignWeaponControllerManager() {
        return this.getPlayerGameControlManager().getWeaponControlManager();
    }

    public InShipControlManager getInShipControlManager() {
        return this.getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager();
    }

    public PlayerGameControlManager getPlayerGameControlManager() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager();
    }

    @Override
    public GameClientState getState() {
        return (GameClientState)super.getState();
    }

    protected void pressedAdminOptions(final PlayerState playerState) {
        final GameClientState state = this.getState();
        final PlayerGameOkCancelInput playerGameOkCancelInput;
        (playerGameOkCancelInput = new PlayerGameOkCancelInput("PlayerStatisticsPanel_PLAYER_ADMIN_OPTIONS", state, (Object)("Player Admin Options: " + playerState.getName()), (Object)"") {
            @Override
            public boolean isOccluded() {
                return false;
            }

            @Override
            public void onDeactivate() {
            }

            @Override
            public void pressedOK() {
                this.deactivate();
            }
        }).getInputPanel().setOkButton(false);
        playerGameOkCancelInput.getInputPanel().onInit();
        final GUITextButton guiTextButton = new GUITextButton(state, 120, 20, GUITextButton.ColorPalette.CANCEL, new Object() {
            @Override
            public String toString() {
                if (state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    return "Kick";
                }
                return "-";
            }
        }, new GUICallback() {
            final /* synthetic */ PlayerGameOkCancelInput val$c = playerGameOkCancelInput;

            @Override
            public void callback(final GUIElement guiElement, final MouseEvent mouseEvent) {
                if (mouseEvent.pressedLeftMouse() && state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    this.val$c.deactivate();
                    new PlayerGameTextInput("PlayerStatisticsPanel_KICK", state, 100, (Object)"Kick", (Object)"Enter Reason") {
                        @Override
                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        @Override
                        public boolean isOccluded() {
                            return false;
                        }

                        @Override
                        public String handleAutoComplete(final String s, final TextCallback textCallback, final String s2) throws PrefixNotFoundException {
                            return null;
                        }

                        @Override
                        public void onFailedTextCheck(final String s) {
                        }

                        @Override
                        public void onDeactivate() {
                            val$c.activate();
                        }

                        @Override
                        public boolean onInput(final String s) {
                            state.getController().sendAdminCommand(AdminCommands.KICK_REASON, playerState.getName(), s);
                            return true;
                        }
                    }.activate();
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        });
        final GUITextButton guiTextButton2 = new GUITextButton(state, 140, 20, FontLibrary.getBoldArial12White(), new Object() {
            @Override
            public String toString() {
                if (state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    return "Ban StarMade Account";
                }
                return "-";
            }
        }, new GUICallback() {
            @Override
            public void callback(final GUIElement guiElement, final MouseEvent mouseEvent) {
                if (mouseEvent.pressedLeftMouse() && state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    state.getController().sendAdminCommand(AdminCommands.BAN_ACCOUNT_BY_PLAYERNAME, playerState.getName());
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        });
        final GUITextButton guiTextButton3 = new GUITextButton(state, 140, 20, FontLibrary.getBoldArial12White(), new Object() {
            @Override
            public String toString() {
                if (state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    return "Ban Player Name";
                }
                return "-";
            }
        }, new GUICallback() {
            @Override
            public void callback(final GUIElement guiElement, final MouseEvent mouseEvent) {
                if (mouseEvent.pressedLeftMouse() && state.getPlayer().getNetworkObject().isAdminClient.get()) {

                    state.getController().sendAdminCommand(AdminCommands.BAN, playerState.getName(), false);
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        });
        final GUITextButton guiTextButton4 = new GUITextButton(state, 140, 20, FontLibrary.getBoldArial12White(), new Object() {
            @Override
            public String toString() {
                if (state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    return "Ban IP";
                }
                return "-";
            }
        }, new GUICallback() {
            @Override
            public void callback(final GUIElement guiElement, final MouseEvent mouseEvent) {
                if (mouseEvent.pressedLeftMouse() && state.getPlayer().getNetworkObject().isAdminClient.get()) {
                    state.getController().sendAdminCommand(AdminCommands.BAN_IP_BY_PLAYERNAME, playerState.getName());
                }
            }

            @Override
            public boolean isOccluded() {
                return false;
            }
        });
        playerGameOkCancelInput.getInputPanel().getContent().attach(guiTextButton);
        guiTextButton3.getPos().y = 24.0f;
        playerGameOkCancelInput.getInputPanel().getContent().attach(guiTextButton3);
        guiTextButton2.getPos().y = 48.0f;
        playerGameOkCancelInput.getInputPanel().getContent().attach(guiTextButton2);
        guiTextButton4.getPos().y = 72.0f;
        playerGameOkCancelInput.getInputPanel().getContent().attach(guiTextButton4);
        playerGameOkCancelInput.activate();
    }

    class WeaponRow extends Row
    {
        public WeaponRow(final InputState inputState, final PlayerState playerState, final GUIElement... array) {
            PlayerStatisticsScrollableListNew.this.super(inputState, playerState, array);
            this.highlightSelect = true;
        }
    }
}
