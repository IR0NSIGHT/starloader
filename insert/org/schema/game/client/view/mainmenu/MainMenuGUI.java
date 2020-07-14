//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.mainmenu;

import api.mod.ModBrowserDialog;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector4f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.schema.common.util.StringTools;
import org.schema.game.client.controller.GameMainMenuController;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.view.gui.advancedbuildmode.AdvancedBuildModeTest;
import org.schema.game.common.version.Version;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIColoredRectangle;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton;
import org.schema.schine.graphicsengine.forms.gui.GUIToolTip;
import org.schema.schine.graphicsengine.forms.gui.GUITextButton.ColorPalette;
import org.schema.schine.graphicsengine.forms.gui.newgui.DialogInterface;
import org.schema.schine.input.BasicInputController;

public class MainMenuGUI extends GUIAncor {
    private boolean init;
    private GUIColoredRectangle mainPanelBackground;
    private List<GUIElement> buttons = new ObjectArrayList();
    private final int buttonHeight = 38;
    private final int buttonSeperation = 8;
    private MainMenuOptionsMenu gameMenu;
    AdvancedBuildModeTest test;
    public static boolean runningSwingDialog = false;

    public MainMenuGUI(GameMainMenuController var1) {
        super(var1);
    }

    public GameMainMenuController getState() {
        return (GameMainMenuController)super.getState();
    }

    public void cleanUp() {
        super.cleanUp();
        this.mainPanelBackground.cleanUp();
        Iterator var1 = this.buttons.iterator();

        while(var1.hasNext()) {
            ((GUIElement)var1.next()).cleanUp();
        }

    }

    public boolean areButtonsUsable() {
        Iterator var1 = this.getState().getController().getInputController().getPlayerInputs().iterator();

        DialogInterface var2;
        do {
            if (!var1.hasNext()) {
                if (!runningSwingDialog) {
                    return true;
                }

                return false;
            }
        } while((var2 = (DialogInterface)var1.next()) instanceof MainMenuInputDialogInterface && !((MainMenuInputDialogInterface)var2).isInside());

        return false;
    }

    private void deactivateAllDialogs() {
        Iterator var1 = this.getState().getController().getInputController().getPlayerInputs().iterator();

        while(var1.hasNext()) {
            ((DialogInterface)var1.next()).deactivate();
        }

    }

    public void onInit() {
        super.onInit();
        Mouse.poll();

        while(Mouse.next()) {
        }

        if (!Version.isDev()) {
            Keyboard.poll();

            while(Keyboard.next()) {
            }
        }

        this.test = new AdvancedBuildModeTest(this.getState());
        if (this.getState().hasLastUsed()) {
            this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_0, new GUICallback() {
                public boolean isOccluded() {
                    return !MainMenuGUI.this.areButtonsUsable();
                }

                public void callback(GUIElement var1, MouseEvent var2) {
                    if (var2.pressedLeftMouse()) {
                        MainMenuGUI.this.deactivateAllDialogs();
                        MainMenuGUI.this.getState().startLastUsed();
                    }

                }
            }, new Object() {
                public String toString() {
                    String var10000 = EngineSettings.LAST_GAME.getCurrentState().toString().trim();
                    String[] var1 = null;
                    if (var10000.length() == 0) {
                        return "";
                    } else {
                        try {
                            boolean var2 = (var1 = EngineSettings.LAST_GAME.getCurrentState().toString().trim().split(";"))[0].equals("SP");
                            String var3 = var1[1];
                            int var4 = Integer.parseInt(var1[2]);
                            String var7 = var1[3];
                            return var2 ? StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_7, new Object[]{var3, var7}) : StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_8, new Object[]{var3, var4, var7});
                        } catch (Exception var6) {
                            var6.printStackTrace();
                            EngineSettings.LAST_GAME.setCurrentState("");

                            try {
                                EngineSettings.write();
                            } catch (IOException var5) {
                                var5.printStackTrace();
                            }

                            return "";
                        }
                    }
                }
            });
        }

        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_1, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    (new LocalUniverseDialog(MainMenuGUI.this.getState())).activate();
                }

            }
        }, (Object)null);
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_2, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    (new OnlineUniverseDialog(MainMenuGUI.this.getState())).activate();
                }

            }
        }, (Object)null);
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_6, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    MainMenuGUI.this.popupLanguageDialog();
                }

            }
        }, (Object)null);
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_3, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    MainMenuGUI.this.gameMenu = new MainMenuOptionsMenu(MainMenuGUI.this.getState());
                    MainMenuGUI.this.gameMenu.activate();
                }

            }
        }, (Object)null);
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_4, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    MainMenuGUI.this.popupToolsAndModsDialog();
                }

            }
        }, (Object)null);
        //INSERTED CODE
        this.addButton("MOD BROWSER", new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    MainMenuGUI.this.popupModBrowserDialog();
                }

            }
        }, (Object)null);
        ///
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_26, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    MainMenuGUI.this.deactivateAllDialogs();
                    MainMenuGUI.this.popupCreditsDialog();
                }

            }
        }, (Object)null);
        this.addButton(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_5, new GUICallback() {
            public boolean isOccluded() {
                return !MainMenuGUI.this.areButtonsUsable();
            }

            public void callback(GUIElement var1, MouseEvent var2) {
                if (var2.pressedLeftMouse()) {
                    GLFrame.setFinished(true);
                }

            }
        }, (Object)null);
        this.mainPanelBackground = new GUIColoredRectangle(this.getState(), 400.0F, (float)(16 + this.buttons.size() * 46), new Vector4f(0.3F, 0.3F, 0.3F, 0.0F));
        this.mainPanelBackground.rounded = 4.0F;
        Iterator var1 = this.buttons.iterator();

        while(var1.hasNext()) {
            GUIElement var2 = (GUIElement)var1.next();
            this.mainPanelBackground.attach(var2);
        }

        this.init = true;
    }
    //INSERTED CODE
    private void popupModBrowserDialog() {
        ModBrowserDialog d = new ModBrowserDialog(getState());
        d.getInputPanel().onInit();
        d.getInputPanel().background.setPos(470, 35, 0);
        d.getInputPanel().background.setWidth(GLFrame.getWidth()-435);
        d.getInputPanel().background.setHeight(GLFrame.getHeight()-70);
        d.addButtons();
        d.activate();
    }
    ///

    private void popupToolsAndModsDialog() {
        ToolsAndModsDialog var1;
        (var1 = new ToolsAndModsDialog(this.getState())).getInputPanel().onInit();
        var1.getInputPanel().background.setPos(470.0F, 35.0F, 0.0F);
        var1.getInputPanel().background.setWidth((float)(GLFrame.getWidth() - 435));
        var1.getInputPanel().background.setHeight((float)(GLFrame.getHeight() - 70));
        var1.addToolsAndModsButtons();
        var1.activate();
    }

    private void popupCreditsDialog() {
        PlayerOkCancelInput var1;
        (var1 = new PlayerOkCancelInput("Credits", this.getState(), 600, 800, Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_9, getCredits()) {
            public void onDeactivate() {
            }

            public void pressedOK() {
                this.deactivate();
            }
        }).getInputPanel().setCancelButton(false);
        var1.getInputPanel().onInit();
        var1.getInputPanel().background.setPos(470.0F, 35.0F, 0.0F);
        var1.getInputPanel().background.setWidth((float)(GLFrame.getWidth() - 435));
        var1.getInputPanel().background.setHeight((float)(GLFrame.getHeight() - 70));
        var1.activate();
    }

    private void popupLanguageDialog() {
        LanguageDialog var1;
        (var1 = new LanguageDialog(this.getState())).getInputPanel().onInit();
        var1.getInputPanel().background.setPos(470.0F, 35.0F, 0.0F);
        var1.getInputPanel().background.setWidth((float)(GLFrame.getWidth() - 435));
        var1.getInputPanel().background.setHeight((float)(GLFrame.getHeight() - 70));
        var1.addToolsAndModsButtons();
        var1.activate();
    }

    private void addButton(String var1, GUICallback var2, Object var3) {
        GUITextButton var4;
        (var4 = new GUITextButton(this.getState(), 400, 38, ColorPalette.TRANSPARENT, FontLibrary.getBlenderProHeavy30(), var1, var2)).setTextPos(10, 4);
        var4.setPos(0.0F, (float)(8 + this.buttons.size() * 46), 0.0F);
        if (var3 != null && EngineSettings.DRAW_TOOL_TIPS.isOn()) {
            var4.setToolTip(new GUIToolTip(this.getState(), var3, var4));
        }

        this.buttons.add(var4);
    }

    public void draw() {
        if (!this.init) {
            this.onInit();
        }

        GL11.glClear(256);
        GlUtil.glEnable(3553);
        GlUtil.glDisable(2929);
        GlUtil.glDepthMask(false);
        GlUtil.glEnable(3042);
        GlUtil.glBlendFunc(770, 771);
        GlUtil.glBlendFuncSeparate(770, 771, 1, 771);
        this.drawMenu();
        this.drawInputs();
        this.getState().getController().getInputController().getGuiCallbackController().drawToolTips();
        GlUtil.glDepthMask(true);
        GlUtil.setColorMask(true);
    }

    private void drawMenu() {
        GUIElement.enableOrthogonal();
        GlUtil.glPushMatrix();
        this.mainPanelBackground.setPos(80.0F, (float)GLFrame.getHeight() - (this.mainPanelBackground.getHeight() + (float)(GLFrame.getHeight() / 6)), 0.0F);
        this.mainPanelBackground.draw();
        GlUtil.glPopMatrix();
        GUIElement.disableOrthogonal();
    }

    private void drawInputs() {
        GUIElement.enableOrthogonal();
        GlUtil.glPushMatrix();
        BasicInputController var1;
        List var2 = (var1 = this.getState().getController().getInputController()).getPlayerInputs();

        int var3;
        for(var3 = 0; var3 < var2.size(); ++var3) {
            ((DialogInterface)var2.get(var3)).getInputPanel().draw();
            if (var1.getCurrentActiveDropdown() != null) {
                var1.getCurrentActiveDropdown().drawExpanded();
            }
        }

        GUIElement.deactivateCallbacks = true;

        for(var3 = 0; var3 < var1.getDeactivatedPlayerInputs().size(); ++var3) {
            DialogInterface var4;
            (var4 = (DialogInterface)var1.getDeactivatedPlayerInputs().get(var3)).updateDeacivated();
            var4.getInputPanel().draw();
            if (System.currentTimeMillis() - var4.getDeactivationTime() > 200L) {
                ((DialogInterface)var1.getDeactivatedPlayerInputs().get(var3)).getInputPanel().cleanUp();
                var1.getDeactivatedPlayerInputs().remove(var3);
                --var3;
            }
        }

        GUIElement.deactivateCallbacks = false;
        GlUtil.glPopMatrix();
        this.getState().getController().getInputController().drawDropdownAndContext();
        GUIElement.disableOrthogonal();
    }

    public void update(Timer var1) {
        this.getState().getController().getInputController().getGuiCallbackController().updateToolTips(var1);
    }

    private static Object getCredits() {
        StringBuilder var0;
        (var0 = new StringBuilder()).append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_10);
        var0.append("~     Robin Promesberger (schema)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_11);
        var0.append("~     Jordan Peck (Auburn)\n");
        var0.append("~     Mitch Petrie (micdoodle8)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_12);
        var0.append("~     Tom Berridge (kupu)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_13);
        var0.append("~     Jay Gaskell (Saber)\n");
        var0.append("~     Keaton Pursell (Omni)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_14);
        var0.append("~     Daniel Tusjak (danki)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_15);
        var0.append("~     Terra Rain (calani)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_16);
        var0.append("~     Brent Van Hoecke (lancake)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_17);
        var0.append("~     Andy Pï¿½ttmann (AndyP)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_18);
        var0.append("~     Michael Debevec (Bench)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_19);
        var0.append("~     Eric Hobson (Criss)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_20);
        var0.append("~     Tai Coromandel (DukeofRealms)\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_21);
        var0.append("~     Adam Boyle (bspkrs) - Game Development\n");
        var0.append("~     Joshua Keel (calbiri) - Assistant Game Design\n");
        var0.append("~     Kramer Campbell (kramerc) - Web\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_22);
        var0.append("~     Frank (SmilingDemon)\n");
        var0.append("~     Andrew\n");
        var0.append("~     Arsat\n");
        var0.append("~     Megacrafter127\n");
        var0.append("~     Danny May (Titansmasher)\n");
        var0.append("~     Ray Kohler (Sven_The_Slayer)\n");
        var0.append("~     Tim Night (spunkie)\n");
        var0.append("~     Samuel (Zackey_TNT)\n");
        var0.append("~     Schnellbier\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_23);
        var0.append("~     Ithirahad\n");
        var0.append("~     ErthParadine\n");
        var0.append("~     LordXaosa\n");
        var0.append("~     Nightrune\n");
        var0.append("~     Malacodor\n");
        var0.append("~     Croquelune\n");
        var0.append("~     Napther\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_24);
        var0.append("~     Maureen Blanchard - French\n");
        var0.append("~     Monika Viste - Polish\n\n");
        var0.append(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_MAINMENU_MAINMENUGUI_25);
        var0.append("~     Mikihiko Miyashita (oasisdog) - Japanese\n");
        var0.append("~     Ricardo Telles Carbonar - Portuguese, Brasilian\n");
        var0.append("~     Alfonso Sanchez Dominguez (Fonso_s) - Spanish\n");
        var0.append("~     Robert Ehelebe (Tarusol) - German\n");
        var0.append("~     Kirill Gaev (liptoh890) - Russian\n");
        var0.append("~     Alexander Sergeev (The_NorD) - Russian\n\n");
        var0.append("\nThanks to all who contributed to our community translation project!\n");
        return var0.toString();
    }
}
