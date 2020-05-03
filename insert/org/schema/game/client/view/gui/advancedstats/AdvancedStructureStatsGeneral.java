//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui.advancedstats;

import api.listener.events.StructureStatsCreateEvent;
import api.listener.helpers.StructureTab;
import api.mod.StarLoader;
import org.schema.common.util.StringTools;
import org.schema.game.client.controller.PlayerGameTextInput;
import org.schema.game.client.view.gui.advanced.AdvancedGUIElement;
import org.schema.game.client.view.gui.advanced.tools.ButtonCallback;
import org.schema.game.client.view.gui.advanced.tools.ButtonResult;
import org.schema.game.client.view.gui.advanced.tools.LabelResult;
import org.schema.game.client.view.gui.advanced.tools.StatLabelResult;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.player.faction.FactionManager;
import org.schema.game.server.data.EntityRequest;
import org.schema.schine.common.InputChecker;
import org.schema.schine.common.TextCallback;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.font.FontLibrary.FontSize;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIDockableDirtyInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea.HButtonColor;

public class AdvancedStructureStatsGeneral extends AdvancedStructureStatsGUISGroup {
    public AdvancedStructureStatsGeneral(AdvancedGUIElement var1) {
        super(var1);
    }

    public void build(GUIContentPane pane, GUIDockableDirtyInterface var2) {
        pane.setTextBoxHeightLast(30);
        this.addLabel(pane.getContent(0), 0, 0, new LabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_19;
            }

            public FontSize getFontSize() {
                return FontSize.BIG;
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 1, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_20;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return AdvancedStructureStatsGeneral.this.getSegCon().getName();
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 2, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_21;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return FactionManager.getFactionName(AdvancedStructureStatsGeneral.this.getSegCon());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 3, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_22;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return AdvancedStructureStatsGeneral.this.getSegCon().getUniqueIdentifierFull();
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addLabel(pane.getContent(0), 0, 4, new LabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_0;
            }

            public FontSize getFontSize() {
                return FontSize.BIG;
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 5, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_1;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                assert AdvancedStructureStatsGeneral.this.getMan() != null;

                return StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getTotalElements());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 6, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_2;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getTotalPhysicalMass());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 7, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_3;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.massFormat(AdvancedStructureStatsGeneral.this.getMan().getMassFromInventories());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 8, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_4;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().railController.calculateRailMassIncludingSelf());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addLabel(pane.getContent(0), 0, 9, new LabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_5;
            }

            public FontSize getFontSize() {
                return FontSize.BIG;
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 10, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_6;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_11,
                        new Object[]{StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().max.x - AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().min.x - 2.0F)});
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 14, new StatLabelResult() {
            public String getName() {
                return "Blocks (inc. docks)";
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getTotalElementsIncRails());
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 11, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_8;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_9, new Object[]{StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().max.y - AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().min.y - 2.0F)});
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addStatLabel(pane.getContent(0), 0, 12, new StatLabelResult() {
            public String getName() {
                return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_10;
            }

            public FontSize getFontSize() {
                return FontSize.MEDIUM;
            }

            public String getValue() {
                return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_7, new Object[]{StringTools.formatSmallAndBig(AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().max.z - AdvancedStructureStatsGeneral.this.getSegCon().getBoundingBox().min.z - 2.0F)});
            }

            public int getStatDistance() {
                return AdvancedStructureStatsGeneral.this.getTextDist();
            }
        });
        this.addButton(pane.getContent(0), 0, 13, new ButtonResult() {
            public ButtonCallback initCallback() {
                return new ButtonCallback() {
                    public void pressedRightMouse() {
                    }

                    public void pressedLeftMouse() {
                        final SegmentController var1 = AdvancedStructureStatsGeneral.this.getSegCon();
                        if (AdvancedStructureStatsGeneral.this.getState().getPlayer().getFactionId() != 0 && AdvancedStructureStatsGeneral.this.getState().getPlayer().getFactionId() == var1.getFactionId() && !var1.isSufficientFactionRights(AdvancedStructureStatsGeneral.this.getState().getPlayer())) {
                            AdvancedStructureStatsGeneral.this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_13, 0.0F);
                        } else {
                            PlayerGameTextInput var2;
                            (var2 = new PlayerGameTextInput("FactionBlockDialog_CHANGE_NAME", AdvancedStructureStatsGeneral.this.getState(), 50, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_14, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_15, var1.getRealName()) {
                                public String[] getCommandPrefixes() {
                                    return null;
                                }

                                public String handleAutoComplete(String var1x, TextCallback var2, String var3) throws PrefixNotFoundException {
                                    return null;
                                }

                                public void onFailedTextCheck(String var1x) {
                                }

                                public boolean isOccluded() {
                                    return false;
                                }

                                public void onDeactivate() {
                                    AdvancedStructureStatsGeneral.this.getPlayerInteractionControlManager().hinderInteraction(400);
                                    AdvancedStructureStatsGeneral.this.getPlayerInteractionControlManager().suspend(false);
                                }

                                public boolean onInput(String var1x) {
                                    if (!var1.getRealName().equals(var1x.trim())) {
                                        System.err.println("[CLIENT] sending name for object: " + var1 + ": " + var1x.trim());
                                        var1.getNetworkObject().realName.set(var1x.trim(), true);

                                        assert var1.getNetworkObject().realName.hasChanged();

                                        assert var1.getNetworkObject().isChanged();
                                    }

                                    return true;
                                }
                            }).setInputChecker(new InputChecker() {
                                public boolean check(String var1, TextCallback var2) {
                                    if (EntityRequest.isShipNameValid(var1)) {
                                        return true;
                                    } else {
                                        var2.onFailedTextCheck(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_16);
                                        return false;
                                    }
                                }
                            });
                            var2.activate();
                        }
                    }
                };
            }

            public boolean isActive() {
                return super.isActive() && AdvancedStructureStatsGeneral.this.getState() != null && AdvancedStructureStatsGeneral.this.getState().getPlayer() != null && AdvancedStructureStatsGeneral.this.getMan() != null && (AdvancedStructureStatsGeneral.this.getState().getPlayer().getFactionId() == 0 || AdvancedStructureStatsGeneral.this.getSegCon() == null || AdvancedStructureStatsGeneral.this.getState().getPlayer().getFactionId() != AdvancedStructureStatsGeneral.this.getSegCon().getFactionId() || AdvancedStructureStatsGeneral.this.getSegCon().isSufficientFactionRights(AdvancedStructureStatsGeneral.this.getState().getPlayer()));
            }

            public String getName() {
                return StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_17, new Object[]{AdvancedStructureStatsGeneral.this.getSegCon().getType().getName()});
            }

            public String getToolTipText() {
                return this.isActive() ? null : Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_18;
            }

            public HButtonColor getColor() {
                return HButtonColor.BLUE;
            }
        });
        //INSERTED CODE @361
        StarLoader.fireEvent(StructureStatsCreateEvent.class, new StructureStatsCreateEvent(StructureTab.GENERAL, this, pane));
        ///
    }

    private int getTextDist() {
        return 150;
    }

    public String getId() {
        return "ASGENERAL";
    }

    public String getTitle() {
        return Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_ADVANCEDSTATS_ADVANCEDSTRUCTURESTATSGENERAL_12;
    }
}
