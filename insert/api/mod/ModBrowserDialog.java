package api.mod;


import org.schema.game.client.controller.GameMainMenuController;
import org.schema.game.client.controller.PlayerButtonTilesInput;
import org.schema.game.client.view.mainmenu.MainMenuGUI;
import org.schema.game.client.view.mainmenu.MainMenuInputDialogInterface;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUITilePane;
import org.schema.schine.input.InputState;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ModBrowserDialog extends PlayerButtonTilesInput implements MainMenuInputDialogInterface {
    private GUITilePane tiles;

    public ModBrowserDialog(GameMainMenuController var1) {
        super((String)null, var1, 650, 400, "Mod Browser", FontLibrary.getBlenderProBook16(), 200, 100);
    }

    public void onDeactivate() {
    }

    public boolean isActive() {
        return !MainMenuGUI.runningSwingDialog && (this.getState().getController().getPlayerInputs().isEmpty() || this.getState().getController().getPlayerInputs().get(this.getState().getController().getPlayerInputs().size() - 1) == this);
    }

    public void addButtons() {
        //INSERTED CODE @265
        for (final String mod : SMDModData.getInstance().getModDataMap()){
            //TODO: Change to green if installed
            SMDModInfo data = SMDModData.getInstance().getModData(mod);
            if(data.getTags().contains("starloader")) {
                this.addTile(mod, data.getTagLine(), GUIHorizontalArea.HButtonColor.PINK, new GUICallback() {
                    public boolean isOccluded() {
                        return !ModBrowserDialog.this.isActive();
                    }

                    public void callback(GUIElement var1, MouseEvent var2) {
                        if (var2.pressedLeftMouse()) {
                            try {
                                ModUpdater.downloadAndLoadMod(mod);

                            } catch (IOException | InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new GUIActivationCallback() {
                    public boolean isVisible(InputState var1) {
                        return true;
                    }

                    public boolean isActive(InputState var1) {
                        return ModBrowserDialog.this.isActive();
                    }
                });
            }
        }
        ///

    }


    public GameMainMenuController getState() {
        return (GameMainMenuController)super.getState();
    }
}
