//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.mainmenu;

import javax.swing.*;

import api.main.GameClient;
import api.mod.EnabledModFile;
import api.mod.ModInfo;
import api.mod.StarLoader;
import api.mod.StarMod;
import org.schema.game.client.controller.GameMainMenuController;
import org.schema.game.client.controller.MainMenu;
import org.schema.game.client.controller.PlayerButtonTilesInput;
import org.schema.game.client.view.mainmenu.gui.effectconfig.EffectConfigDialog;
import org.schema.game.client.view.mainmenu.gui.effectconfig.GUIEffectStat;
import org.schema.game.client.view.mainmenu.gui.ruleconfig.GUIRuleSetStat;
import org.schema.game.client.view.mainmenu.gui.ruleconfig.RuleSetConfigDialogMainMenu;
import org.schema.game.common.Starter;
import org.schema.game.common.facedit.ElementEditorFrame;
import org.schema.game.common.gui.CatalogManagerEditorController;
import org.schema.game.common.starcalc.StarCalc;
import org.schema.game.common.staremote.Staremote;
import org.schema.schine.common.language.Lng;
import org.schema.schine.common.language.editor.LanguageEditor;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea.HButtonColor;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.input.InputState;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToolsAndModsDialog extends PlayerButtonTilesInput implements MainMenuInputDialogInterface {
    private GUITilePane tiles;

    public ToolsAndModsDialog(GameMainMenuController var1) {
        super((String)null, var1, 650, 400, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_0, FontLibrary.getBlenderProBook16(), 200, 100);
    }

    public void onDeactivate() {
    }

    public boolean isActive() {
        return !MainMenuGUI.runningSwingDialog && (this.getState().getController().getPlayerInputs().isEmpty() || this.getState().getController().getPlayerInputs().get(this.getState().getController().getPlayerInputs().size() - 1) == this);
    }

    public void addToolsAndModsButtons() {
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_1, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_3, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    Starter.cleanClientCacheWithoutBackup();
                    ToolsAndModsDialog.this.deactivate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_4, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_5, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    final ElementEditorFrame var3 = new ElementEditorFrame();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            var3.setVisible(true);
                            var3.requestFocus();
                        }
                    });
                    ToolsAndModsDialog.this.deactivate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_6, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_7, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    LanguageEditor.main(new String[]{"disposeonexit"});
                    ToolsAndModsDialog.this.deactivate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_8, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_9, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    Staremote var3;
                    (var3 = new Staremote()).exit = false;
                    var3.startConnectionGUI();
                    ToolsAndModsDialog.this.deactivate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_10, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_2, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    ToolsAndModsDialog.this.deactivate();
                    StarCalc.main((String[])null);
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_11, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_12, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    ToolsAndModsDialog.this.deactivate();
                    CatalogManagerEditorController var3;
                    (var3 = new CatalogManagerEditorController((JFrame)null)).setDefaultCloseOperation(2);
                    var3.setVisible(true);
                    var3.setAlwaysOnTop(true);
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_13, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_14, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    ToolsAndModsDialog.this.deactivate();
                    GUIEffectStat var3 = new GUIEffectStat(ToolsAndModsDialog.this.getState(), (String)null);
                    (new EffectConfigDialog(ToolsAndModsDialog.this.getState(), var3)).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        this.addTile(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_15, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_TOOLSANDMODSDIALOG_16, HButtonColor.BLUE, new GUICallback() {
            public boolean isOccluded() {
                return !ToolsAndModsDialog.this.isActive();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    ToolsAndModsDialog.this.deactivate();
                    GUIRuleSetStat var3 = new GUIRuleSetStat(ToolsAndModsDialog.this.getState(), (String)null);
                    (new RuleSetConfigDialogMainMenu(ToolsAndModsDialog.this.getState(), var3)).activate();
                }

            }
        }, new GUIActivationCallback() {
            public boolean isVisible(InputState var1) {
                return true;
            }

            public boolean isActive(InputState var1) {
                return ToolsAndModsDialog.this.isActive();
            }
        });
        //INSERTED CODE
        final EnabledModFile modFile = EnabledModFile.getInstance();
        for (StarMod mod : StarLoader.starMods){
            final ModInfo modInfo = mod.getInfo();
            HButtonColor color = modFile.isClientEnabled(modInfo) ? HButtonColor.GREEN : HButtonColor.RED;
            this.addTile("Mod: " + mod.modName, "Version: " + mod.modVersion + "\n" + mod.modDescription, color, new GUICallback() {
                public boolean isOccluded() {
                    return !ToolsAndModsDialog.this.isActive();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        modFile.setClientEnabled(modInfo, !modFile.isClientEnabled(modInfo));
                        ToolsAndModsDialog.this.deactivate();
                        ToolsAndModsDialog.this.addToolsAndModsButtons();
                        try {
                            Field frame = GameMainMenuController.class.getDeclaredField("frame");
                            frame.setAccessible(true);
                            MainMenuFrame o = (MainMenuFrame) frame.get(GameMainMenuController.currentMainMenu);
                            Field gui = MainMenuFrame.class.getDeclaredField("gui");
                            gui.setAccessible(true);
                            MainMenuGUI g = (MainMenuGUI) gui.get(o);
                            Method m = MainMenuGUI.class.getDeclaredMethod("popupToolsAndModsDialog");
                            m.setAccessible(true);
                            m.invoke(g);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //tiles.addButtonTile("", "", HButtonColor.BLUE, null, null);


                        //GUIEffectStat var3 = new GUIEffectStat(ToolsAndModsDialog.this.getState(), (String)null);
                        //(new EffectConfigDialog(ToolsAndModsDialog.this.getState(), var3)).activate();
                    }

                }
            }, new GUIActivationCallback() {
                public boolean isVisible(InputState var1) {
                    return true;
                }

                public boolean isActive(InputState var1) {
                    return ToolsAndModsDialog.this.isActive();
                }
            });
        }
        ///

    }


    public GameMainMenuController getState() {
        return (GameMainMenuController)super.getState();
    }
}
