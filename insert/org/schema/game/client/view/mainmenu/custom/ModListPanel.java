//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.mainmenu.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.schema.game.client.controller.PlayerDropDownInput;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.PlayerTextInput;
import org.schema.game.client.view.mainmenu.DialogInput;
import org.schema.game.client.view.mainmenu.MainMenuGUI;
import org.schema.game.client.view.mainmenu.gui.effectconfig.*;
import org.schema.game.common.data.blockeffects.config.ConfigGroup;
import org.schema.game.common.data.blockeffects.config.ConfigManagerInterface;
import org.schema.game.common.data.blockeffects.config.ConfigPool;
import org.schema.game.common.data.blockeffects.config.EffectConfigElement;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.schine.common.TextCallback;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIListElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIActiveInterface;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalButtonTablePane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITextOverlayTable;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea.HButtonColor;
import org.schema.schine.input.InputState;

public class ModListPanel extends GUIElement implements GUIActiveInterface {
    public GUIMainWindow mainPanel;
    private GUIContentPane mainTab;
    private DialogInput diag;
    private List<GUIElement> toCleanUp = new ObjectArrayList();
    private GUIEffectStat stat;
    private boolean init;
    private ConfigManagerInterface specifiedMan;

    public ModListPanel(InputState var1, ConfigManagerInterface var2, GUIEffectStat var3, DialogInput var4) {
        super(var1);
        this.diag = var4;
        this.stat = var3;
        this.specifiedMan = var2;
    }

    public void cleanUp() {
        Iterator var1 = this.toCleanUp.iterator();

        while(var1.hasNext()) {
            ((GUIElement)var1.next()).cleanUp();
        }

        this.toCleanUp.clear();
    }

    public void draw() {
        if (!this.init) {
            this.onInit();
        }

        GlUtil.glPushMatrix();
        this.transform();
        this.mainPanel.draw();
        GlUtil.glPopMatrix();
    }

    public void onInit() {
        if (!this.init) {
            this.mainPanel = new GUIMainWindow(this.getState(), GLFrame.getWidth() - 410, GLFrame.getHeight() - 20, 400, 10, "UniversePanelWindow");
            this.mainPanel.onInit();
            this.mainPanel.setPos(435.0F, 35.0F, 0.0F);
            this.mainPanel.setWidth((float)(GLFrame.getWidth() - 470));
            this.mainPanel.setHeight((float)(GLFrame.getHeight() - 70));
            this.mainPanel.clearTabs();
            if (this.specifiedMan != null) {
                this.createTestTab();
                this.mainTab = this.createEffectsTab();
            } else {
                this.mainTab = this.createEffectsTab();
                this.createTestTab();
            }

            this.mainPanel.activeInterface = this;
            this.mainPanel.setCloseCallback(new GUICallback() {
                public boolean isOccluded() {
                    return !ModListPanel.this.isActive();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        ModListPanel.this.diag.deactivate();
                    }

                }
            });
            this.init = true;
        }
    }

    public boolean isInside() {
        return this.mainPanel.isInside();
    }

    private GUIContentPane createTestTab() {
        GUIContentPane var1;
        if (this.specifiedMan != null) {
            var1 = this.mainPanel.addTab(this.specifiedMan.toString());
        } else {
            var1 = this.mainPanel.addTab(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_0);
        }

        var1.setTextBoxHeightLast(280);
        GUIEffectConfigGroupTestList var2;
        if (this.specifiedMan != null) {
            var2 = new GUIEffectConfigGroupTestList(this.getState(), var1.getContent(0), this, this.stat, this.specifiedMan.getConfigManager(), this.specifiedMan);
        } else {
            var2 = new GUIEffectConfigGroupTestList(this.getState(), var1.getContent(0), this, this.stat, this.stat.testManager, (ConfigManagerInterface)null);
        }

        var2.onInit();
        var1.getContent(0).attach(var2);
        var1.addNewTextBox(280);
        GUIEffectConfigTestList var3;
        if (this.specifiedMan != null) {
            var3 = new GUIEffectConfigTestList(this.getState(), var1.getContent(1), this, this.specifiedMan.getConfigManager());
        } else {
            var3 = new GUIEffectConfigTestList(this.getState(), var1.getContent(1), this, this.stat.testManager);
        }

        var3.onInit();
        var1.getContent(1).attach(var3);
        return var1;
    }

    private GUIContentPane createEffectsTab() {
        GUIContentPane var1;
        (var1 = this.mainPanel.addTab(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_1)).setTextBoxHeightLast(24);
        this.addMenuButtonPanel(var1, 0);
        var1.addNewTextBox(48);
        var1.addNewTextBox(280);
        this.addEditButtonPanel(var1, 1);
        GUIEffectConfigGroupList var2;
        (var2 = new GUIEffectConfigGroupList(this.getState(), var1.getContent(2), this, this.stat)).onInit();
        var1.getContent(2).attach(var2);
        var1.addNewTextBox(280);
        GUIEffectConfigElementList var3;
        (var3 = new GUIEffectConfigElementList(this.getState(), var1.getContent(3), this, this.stat)).onInit();
        var1.getContent(3).attach(var3);
        return var1;
    }

    private void addMenuButtonPanel(GUIContentPane var1, int var2) {
        GUIHorizontalButtonTablePane var3;
        (var3 = new GUIHorizontalButtonTablePane(this.getState(), 4, 1, var1.getContent(var2))).onInit();
        var3.addButton(0, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_2, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    (new PlayerOkCancelInput("CONFIRM", ModListPanel.this.getState(), 300, 140, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_3, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_4) {
                        public void pressedOK() {
                            ModListPanel.this.stat.configPool = new ConfigPool();
                            ModListPanel.this.stat.selectedElement = null;
                            ModListPanel.this.stat.selectedGroup = null;
                            ModListPanel.this.stat.change();
                            this.deactivate();
                        }

                        public void onDeactivate() {
                        }
                    }).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.configPool != null;
            }
        });
        var3.addButton(1, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_5, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    ModListPanel.this.stat.save(ModListPanel.this.getState(), (String)null);
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.configPool != null;
            }
        });
        var3.addButton(2, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_6, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    (new PlayerTextInput("INININSKS", ModListPanel.this.getState(), 256, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_7, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_8, ModListPanel.this.stat.getLoadedPath()) {
                        public void onFailedTextCheck(String var1) {
                        }

                        public String handleAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
                            return null;
                        }

                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        public boolean onInput(String var1) {
                            ModListPanel.this.stat.save(this.getState(), var1);
                            return true;
                        }

                        public void onDeactivate() {
                        }
                    }).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.configPool != null;
            }
        });
        var3.addButton(3, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_9, HButtonColor.YELLOW, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    (new PlayerTextInput("INININSKS", ModListPanel.this.getState(), 256, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_10, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_11, ModListPanel.this.stat.getLoadedPath()) {
                        public void onFailedTextCheck(String var1) {
                        }

                        public String handleAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
                            return null;
                        }

                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        public boolean onInput(String var1) {
                            ModListPanel.this.stat.load(this.getState(), var1, true);
                            ModListPanel.this.stat.selectedElement = null;
                            ModListPanel.this.stat.selectedGroup = null;
                            ModListPanel.this.stat.change();
                            return true;
                        }

                        public void onDeactivate() {
                        }
                    }).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive();
            }
        });
        var1.getContent(var2).attach(var3);
    }

    private void addEditButtonPanel(GUIContentPane var1, int var2) {
        GUIHorizontalButtonTablePane var3;
        (var3 = new GUIHorizontalButtonTablePane(this.getState(), 3, 2, var1.getContent(var2))).onInit();
        var3.addButton(0, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_12, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    PlayerTextInput var3;
                    (var3 = new PlayerTextInput("TXTTSTTTS", ModListPanel.this.getState(), 64, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_23, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_24) {
                        public void onFailedTextCheck(String var1) {
                        }

                        public String handleAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
                            return null;
                        }

                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        public boolean onInput(String var1) {
                            if (var1.trim().length() > 0) {
                                ConfigGroup var2 = new ConfigGroup(var1.toLowerCase(Locale.ENGLISH));
                                if (!ModListPanel.this.stat.configPool.poolMapLowerCase.containsKey(var2.id)) {
                                    ModListPanel.this.stat.configPool.add(var2);
                                    ModListPanel.this.stat.selectedGroup = var2;
                                    ModListPanel.this.stat.change();
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }

                        public void onDeactivate() {
                        }
                    }).getInputPanel().onInit();
                    var3.activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive();
            }
        });
        var3.addButton(1, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_15, HButtonColor.PINK, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse() && ModListPanel.this.stat.selectedGroup != null) {
                    PlayerTextInput var3;
                    (var3 = new PlayerTextInput("TXTTSTTTS", ModListPanel.this.getState(), 64, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_16, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_17, ModListPanel.this.stat.selectedGroup.id) {
                        public void onFailedTextCheck(String var1) {
                        }

                        public String handleAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
                            return null;
                        }

                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        public boolean onInput(String var1) {
                            if (var1.trim().length() <= 0) {
                                return false;
                            } else if (ModListPanel.this.stat.selectedGroup == null) {
                                return false;
                            } else {
                                ConfigGroup var4 = new ConfigGroup(var1.toLowerCase(Locale.ENGLISH));
                                Iterator var2 = ModListPanel.this.stat.selectedGroup.elements.iterator();

                                while(var2.hasNext()) {
                                    EffectConfigElement var3 = (EffectConfigElement)var2.next();
                                    var3 = new EffectConfigElement(var3);
                                    var4.elements.add(var3);
                                }

                                if (!ModListPanel.this.stat.configPool.poolMapLowerCase.containsKey(var4.id)) {
                                    ModListPanel.this.stat.configPool.add(var4);
                                    ModListPanel.this.stat.selectedGroup = var4;
                                    ModListPanel.this.stat.change();
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }

                        public void onDeactivate() {
                        }
                    }).getInputPanel().onInit();
                    var3.activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.selectedGroup != null;
            }
        });
        var3.addButton(2, 0, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_18, HButtonColor.RED, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse() && ModListPanel.this.stat.selectedGroup != null) {
                    ModListPanel.this.stat.configPool.remove(ModListPanel.this.stat.selectedGroup);
                    ModListPanel.this.stat.selectedGroup = null;
                    ModListPanel.this.stat.change();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.selectedGroup != null;
            }
        });
        var3.addButton(0, 1, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_19, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (ModListPanel.this.stat.selectedGroup != null && var2.pressedLeftMouse()) {
                    ObjectArrayList var8 = new ObjectArrayList();
                    StatusEffectType[] var9;
                    int var3 = (var9 = StatusEffectType.values()).length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        StatusEffectType var5 = var9[var4];
                        GUIAncor var6 = new GUIAncor(ModListPanel.this.getState(), 200.0F, 24.0F);
                        GUITextOverlayTable var7;
                        (var7 = new GUITextOverlayTable(200, 24, ModListPanel.this.getState())).setTextSimple(var5.getCategory().getName() + " - " + var5.getName());
                        var7.setPos(5.0F, 5.0F, 0.0F);
                        var6.attach(var7);
                        var6.setUserPointer(var5);
                        var8.add(var6);
                    }

                    (new PlayerDropDownInput("DDDJSKMDKS", ModListPanel.this.getState(), 400, 300, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_20, 24, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_21, var8) {
                        public void pressedOK(GUIListElement var1) {
                            StatusEffectType var3 = (StatusEffectType)var1.getContent().getUserPointer();
                            EffectConfigElement var2;
                            (var2 = new EffectConfigElement()).init(var3);
                            ModListPanel.this.stat.selectedGroup.elements.add(var2);
                            ModListPanel.this.stat.change();
                            this.deactivate();
                        }

                        public void onDeactivate() {
                        }
                    }).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.selectedGroup != null;
            }
        });
        var3.addButton(1, 1, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_22, HButtonColor.YELLOW, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse() && ModListPanel.this.stat.selectedGroup != null) {
                    PlayerTextInput var3;
                    (var3 = new PlayerTextInput("TXTTSTTTS", ModListPanel.this.getState(), 64, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_13, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_14, ModListPanel.this.stat.selectedGroup.id) {
                        public void onFailedTextCheck(String var1) {
                        }

                        public String handleAutoComplete(String var1, TextCallback var2, String var3) throws PrefixNotFoundException {
                            return null;
                        }

                        public String[] getCommandPrefixes() {
                            return null;
                        }

                        public boolean onInput(String var1) {
                            if (var1.trim().length() > 0) {
                                if (ModListPanel.this.stat.selectedGroup != null) {
                                    ModListPanel.this.stat.configPool.remove(ModListPanel.this.stat.selectedGroup);
                                    String var2 = ModListPanel.this.stat.selectedGroup.id;
                                    ModListPanel.this.stat.selectedGroup.id = var1.trim().toLowerCase(Locale.ENGLISH);
                                    ModListPanel.this.stat.configPool.add(ModListPanel.this.stat.selectedGroup);
                                    System.err.println("CHANGED ID TO " + var2 + " -> " + ModListPanel.this.stat.selectedGroup.id);
                                    ModListPanel.this.stat.change();
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }

                        public void onDeactivate() {
                        }
                    }).getInputPanel().onInit();
                    var3.activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.selectedGroup != null;
            }
        });
        var3.addButton(2, 1, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_GUI_EFFECTCONFIG_GUIEFFECTCONFIGPANEL_25, HButtonColor.RED, new GUICallback() {
            public boolean isOccluded() {
                return !ModListPanel.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse() && ModListPanel.this.stat.selectedGroup != null && ModListPanel.this.stat.selectedGroup.elements.contains(ModListPanel.this.stat.selectedElement)) {
                    ModListPanel.this.stat.selectedGroup.elements.remove(ModListPanel.this.stat.selectedElement);
                    ModListPanel.this.stat.selectedElement = null;
                    ModListPanel.this.stat.change();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ModListPanel.this.isActive() && ModListPanel.this.stat.selectedGroup != null && ModListPanel.this.stat.selectedElement != null;
            }
        });
        var1.getContent(var2).attach(var3);
    }

    public float getHeight() {
        return 0.0F;
    }

    public float getWidth() {
        return 0.0F;
    }

    public boolean isActive() {
        List var1 = this.getState().getController().getInputController().getPlayerInputs();
        return !MainMenuGUI.runningSwingDialog && (var1.isEmpty() || ((DialogInterface)var1.get(var1.size() - 1)).getInputPanel() == this);
    }
}
