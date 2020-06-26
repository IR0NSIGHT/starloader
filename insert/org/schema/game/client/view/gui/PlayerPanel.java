//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.client.view.gui;

import java.io.File;
import java.io.IOException;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;

import api.listener.events.gui.PlayerGUICreateEvent;
import api.listener.events.gui.PlayerGUIDrawEvent;
import api.mod.StarLoader;
import org.schema.common.FastMath;
import org.schema.common.util.StringTools;
import org.schema.game.client.controller.manager.AbstractControlManager;
import org.schema.game.client.controller.manager.ingame.ChatControlManager;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.controller.manager.ingame.SegmentBuildController;
import org.schema.game.client.controller.manager.ingame.SegmentControlManager;
import org.schema.game.client.controller.manager.ingame.ship.ShipControllerManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.advanced.AdvancedBuldModeLeftContainer;
import org.schema.game.client.view.gui.advancedbuildmode.AdvancedBuildMode;
import org.schema.game.client.view.gui.ai.newai.AIPanelNew;
import org.schema.game.client.view.gui.buildtools.BuildToolsPanel;
import org.schema.game.client.view.gui.catalog.newcatalog.CatalogPanelNew;
import org.schema.game.client.view.gui.chat.ChatPanel;
import org.schema.game.client.view.gui.faction.newfaction.FactionPanelNew;
import org.schema.game.client.view.gui.fleet.FleetPanel;
import org.schema.game.client.view.gui.inventory.InventorySlotOverlayElement;
import org.schema.game.client.view.gui.inventory.inventorynew.InventoryPanelNew;
import org.schema.game.client.view.gui.mapgui.MapToolsPanel;
import org.schema.game.client.view.gui.navigation.navigationnew.NavigationPanelNew;
import org.schema.game.client.view.gui.newgui.GUITopBar;
import org.schema.game.client.view.gui.shiphud.AiCrewAndFleetInformationPanel;
import org.schema.game.client.view.gui.shiphud.ShipInformationPanel;
import org.schema.game.client.view.gui.shiphud.newhud.BottomBarBuild;
import org.schema.game.client.view.gui.shiphud.newhud.TopBarNew;
import org.schema.game.client.view.gui.shop.shopnew.ShopPanelNew;
import org.schema.game.client.view.gui.structurecontrol.structurenew.StructurePanelNew;
import org.schema.game.client.view.gui.weapon.WeaponBottomBar;
import org.schema.game.client.view.gui.weapon.WeaponControllerPanelInterface;
import org.schema.game.client.view.gui.weapon.WeaponPanelNew;
import org.schema.game.client.view.gui.weapon.WeaponSlotOverlayElement;
import org.schema.game.common.controller.elements.ElementCollectionManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.inventory.Inventory;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.GlUtil;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.gui.Draggable;
import org.schema.schine.graphicsengine.forms.gui.GUIAncor;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.graphicsengine.forms.gui.GUITintScreenElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIResizableGrabbableWindow;
import org.schema.schine.input.InputState;
import org.schema.schine.input.KeyboardMappings;
import org.xml.sax.SAXException;

public class PlayerPanel extends GUIElement {
    public static String infoText;
    private GUIScrollablePanel infoScroll;
    private GUITintScreenElement tutorialTint;
    private FleetPanel fleetPanel;
    private FactionPanelNew factionPanelNew;
    private CatalogPanelNew catalogPanelNew;
    private ShopPanelNew shopPanelNew;
    private InventoryPanelNew inventoryPanelNew;
    private AIPanelNew aiPanelNew;
    private NavigationPanelNew navigationPanelNew;
    private WeaponPanelNew weaponManagerPanelNew;
    private HotbarInterface buildSideBar;
    private ShipInformationPanel shipInformationPanel;
    private WeaponBottomBar[] weaponSideBars;
    private GUIAncor inventoryTab;
    private GUIAncor weaponTab;
    private GUIAncor shopTab;
    private GUIAncor close;
    private GUIAncor naviTab;
    private GUIHelpPanelManager helpPanel;
    private TopBarInterface topBar;
    private GUIAncor aiMenuTab;
    private GUIAncor factionMenuTab;
    private GUIAncor catalogTab;
    private StructurePanelNew structurePanelNew;
    private GUIElement structureMenuTab;
    private AiCrewAndFleetInformationPanel aiCrewAndFleetInformationPanel;
    private BuildToolsPanel buildToolsAstronaut;
    private MapToolsPanel mapPanel;
    private long lastTutorialTint;
    private boolean panelActive;
    private GUITopBar topTaskBar;
    private ChatPanel chat;
    private boolean chatDryRun;
    private long firstDrawChat;
    private byte selectedWeaponBar;
    private float timeSpent;
    private boolean tabInfo;
    private GUITextOverlay infoTextOverlay;
    public final AdvancedBuildMode advancedBuildMode;
    public final AdvancedBuldModeLeftContainer advancedBuildModeContainer;
    public static boolean mouseInInfoScroll;

    public PlayerPanel(InputState var1) {
        super(var1);
        this.advancedBuildMode = new AdvancedBuildMode((GameClientState)var1);
        this.advancedBuildModeContainer = new AdvancedBuldModeLeftContainer((GameClientState)var1);
    }

    private void activateAIManager() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().aiConfigurationAction((SegmentPiece)null);
    }

    private void activateCatalogManager() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(true);
    }

    private void activateStructureControllerManager() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(true);
    }

    private void activateFactionManager() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(true);
    }

    private void activateInventory() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().inventoryAction((Inventory)null);
    }

    private void activateNavigation() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(false);
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(true);
    }

    private void activateShop() {
        if (this.getState().isInShopDistance()) {
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(true);
        } else {
            this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_PLAYERPANEL_0, 0.0F);
        }
    }

    private void activateWeaponAssignManager() {
        if (this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().isActive()) {
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().setActive(false);
            this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().setActive(true);
        } else {
            this.getState().getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_PLAYERPANEL_1, 0.0F);
        }
    }

    public void afterGUIDraw() {
        this.weaponManagerPanelNew.checkForUpdate();
        GUIElement.enableOrthogonal();
        GlUtil.glPushMatrix();
        this.drawToolTips();
        GlUtil.glPopMatrix();
        GUIElement.disableOrthogonal();
    }

    private boolean aiManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getAiConfigurationManager().isTreeActive();
    }

    private boolean buildModeActive() {
        return this.getActiveBuildController() != null;
    }

    private boolean catalogControllerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getCatalogControlManager().isTreeActive();
    }

    private boolean fleetControllerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFleetControlManager().isTreeActive();
    }

    public void cleanUp() {
    }

    public void draw() {
        if (this.needsReOrientation()) {
            this.doOrientation();
        }

        GUIElement.enableOrthogonal();
        GlUtil.glPushMatrix();
        this.topBar.draw();
        if (!this.inventoryActive() && !this.shopActive()) {
            this.getBuildSideBar().activateDragging(false);
        } else {
            this.getBuildSideBar().activateDragging(true);
        }

        boolean var1 = false;
        this.aiCrewAndFleetInformationPanel.draw();
        if (this.inventoryActive()) {
            var1 = true;
            this.inventoryPanelNew.draw();
        } else if (isNewHud()) {
            this.inventoryPanelNew.reset();
            this.inventoryPanelNew.resetOthers();
        }

        if (EngineSettings.CONTROL_HELP.isOn()) {
            GlUtil.glPushMatrix();
            this.helpPanel.draw();
            GlUtil.glPopMatrix();
        }

        if (this.aiManagerActive()) {
            var1 = true;
            this.aiPanelNew.draw();
        } else {
            this.aiPanelNew.reset();
        }

        if (this.shopActive()) {
            var1 = true;
            this.shopPanelNew.draw();
        } else {
            this.shopPanelNew.reset();
        }

        if (this.navigationActive()) {
            var1 = true;
            this.navigationPanelNew.draw();
        } else {
            this.navigationPanelNew.reset();
        }

        if (this.factionControllerManagerActive()) {
            var1 = true;
            this.factionPanelNew.draw();
        } else {
            this.factionPanelNew.reset();
        }

        if (this.fleetControllerManagerActive()) {
            var1 = true;
            this.fleetPanel.draw();
        } else {
            this.fleetPanel.reset();
        }

        if (this.catalogControllerManagerActive()) {
            var1 = true;
            this.catalogPanelNew.draw();
        } else {
            this.catalogPanelNew.reset();
        }

        if (this.shipControllerManagerActive()) {
            var1 = true;
            this.weaponManagerPanelNew.draw();
        }

        if (this.thrustManagerManagerActive()) {
            var1 = true;
        }

        if (this.structureControllerManagerActive()) {
            var1 = true;
            this.structurePanelNew.draw();
        } else {
            this.structurePanelNew.reset();
        }

        if (this.inGameActive() && !this.mapActive()) {
            if (this.isDrawShipSideBar()) {
                this.weaponSideBars[this.selectedWeaponBar].draw();
            } else {
                this.getBuildSideBar().draw();
                if (!this.shopActive() && !this.inventoryActive() && !this.structureControllerManagerActive() && this.buildModeActive()) {
                    if (this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getBuildToolsManager().structureInfo) {
                        this.advancedBuildModeContainer.draw();
                    }

                    this.advancedBuildMode.draw();
                } else if (this.isInAstronautMode() && !this.shopActive() && !this.inventoryActive()) {
                    this.buildToolsAstronaut.draw();
                }
            }
        } else if (this.mapActive()) {
            var1 = false;
            this.mapPanel.draw();
            if (infoText != null) {
                this.infoTextOverlay.setTextSimple(infoText);
                this.infoTextOverlay.setHeight(this.infoTextOverlay.getTextHeight());
                mouseInInfoScroll = false;
                this.infoScroll.setPos((float)GLFrame.getWidth() - this.infoScroll.getWidth(), 50.0F, 0.0F);
                this.infoScroll.draw();
                infoText = null;
            }
        }

        if (this.getState().getController().getInputController().getDragging() != null && this.getState().getController().getInputController().getDragging() instanceof InventorySlotOverlayElement) {
            InventorySlotOverlayElement var2;
            if ((var2 = (InventorySlotOverlayElement)this.getState().getController().getInputController().getDragging()).getType() != 0 && var2.getCount(true) > 0) {
                this.inventoryPanelNew.drawDragging(var2);
            } else {
                this.getState().getController().getInputController().setDragging((Draggable)null);
            }
        }

        if (this.getState().getController().getInputController().getDragging() != null && this.getState().getController().getInputController().getDragging() instanceof WeaponSlotOverlayElement) {
            WeaponSlotOverlayElement var3;
            if ((var3 = (WeaponSlotOverlayElement)this.getState().getController().getInputController().getDragging()).getType() == 0) {
                this.getState().getController().getInputController().setDragging((Draggable)null);
            } else {
                this.weaponSideBars[this.selectedWeaponBar].drawDragging(var3);
            }
        }

        if (this.getState().getController().getTutorialMode() != null) {
            this.getState().getController().getTutorialMode();
            if (this.getState().getPlayer().isInTutorial() && (!this.getState().getController().getPlayerInputs().isEmpty() || this.getState().getUpdateTime() - this.lastTutorialTint < 300L)) {
                if (!this.getState().getController().getPlayerInputs().isEmpty()) {
                    this.lastTutorialTint = this.getState().getUpdateTime();
                }

                this.tutorialTint.draw();
            }
        }

        this.setPanelActive(var1);
        GlUtil.glPopMatrix();
        if (this.isPanelActive()) {
            this.topTaskBar.draw();
            GUIResizableGrabbableWindow.topHeightSubtract = (int)this.topTaskBar.getHeight();
        } else {
            GUIResizableGrabbableWindow.topHeightSubtract = 0;
        }
        //INSERTED CODE
        PlayerGUIDrawEvent event = new PlayerGUIDrawEvent(this);
        StarLoader.fireEvent(event, false);
        ///

        this.drawChat();
        GUIElement.disableOrthogonal();
    }

    public void onInit() {
        this.topBar = new TopBarNew(this.getState(), this);
        this.topBar.onInit();
        this.topTaskBar = new GUITopBar(this.getState());
        this.topTaskBar.onInit();
        this.tutorialTint = new GUITintScreenElement(this.getState());
        this.tutorialTint.getColor().set(0.1F, 0.1F, 0.1F, 0.8F);
        this.tutorialTint.onInit();
        this.shipInformationPanel = new ShipInformationPanel(this.getState());
        this.shipInformationPanel.onInit();
        this.chat = new ChatPanel(this.getState());
        this.chat.onInit();
        PlayerPanel.TabCallback var1 = new PlayerPanel.TabCallback();
        this.close = new GUIAncor(this.getState(), 39.0F, 26.0F);
        this.close.setUserPointer("X");
        this.close.setMouseUpdateEnabled(true);
        this.close.setCallback(var1);
        this.close.getPos().set(804.0F, 4.0F, 0.0F);
        this.close.onInit();
        this.inventoryTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.inventoryTab.setUserPointer("INVENTORY");
        this.inventoryTab.setMouseUpdateEnabled(true);
        this.inventoryTab.setCallback(var1);
        this.inventoryTab.getPos().set(216.0F, 26.0F, 0.0F);
        this.inventoryTab.onInit();
        this.aiCrewAndFleetInformationPanel = new AiCrewAndFleetInformationPanel(this.getState());
        this.aiCrewAndFleetInformationPanel.onInit();
        this.aiCrewAndFleetInformationPanel.orientate(17);
        this.aiMenuTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.aiMenuTab.setUserPointer("AI");
        this.aiMenuTab.setMouseUpdateEnabled(true);
        this.aiMenuTab.setCallback(var1);
        this.aiMenuTab.getPos().set(662.0F, 472.0F, 0.0F);
        this.aiMenuTab.onInit();
        this.factionMenuTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.factionMenuTab.setUserPointer("FACTION");
        this.factionMenuTab.setMouseUpdateEnabled(true);
        this.factionMenuTab.setCallback(var1);
        this.factionMenuTab.getPos().set(517.0F, 472.0F, 0.0F);
        this.factionMenuTab.onInit();
        this.structureMenuTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.structureMenuTab.setUserPointer("STRUCTURE");
        this.structureMenuTab.setMouseUpdateEnabled(true);
        this.structureMenuTab.setCallback(var1);
        this.structureMenuTab.getPos().set(216.0F, 472.0F, 0.0F);
        this.structureMenuTab.onInit();
        this.weaponTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.weaponTab.setUserPointer("WEAPON");
        this.weaponTab.setMouseUpdateEnabled(true);
        this.weaponTab.setCallback(var1);
        this.weaponTab.getPos().set(366.0F, 26.0F, 0.0F);
        this.weaponTab.onInit();
        this.catalogTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.catalogTab.setUserPointer("CATALOG");
        this.catalogTab.setMouseUpdateEnabled(true);
        this.catalogTab.setCallback(var1);
        this.catalogTab.getPos().set(366.0F, 472.0F, 0.0F);
        this.catalogTab.onInit();
        this.shopTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.shopTab.setUserPointer("SHOP");
        this.shopTab.setMouseUpdateEnabled(true);
        this.shopTab.setCallback(var1);
        this.shopTab.getPos().set(514.0F, 26.0F, 0.0F);
        this.shopTab.onInit();
        this.naviTab = new GUIAncor(this.getState(), 147.0F, 40.0F);
        this.naviTab.setUserPointer("NAVIGATION");
        this.naviTab.setMouseUpdateEnabled(true);
        this.naviTab.setCallback(var1);
        this.naviTab.getPos().set(662.0F, 26.0F, 0.0F);
        this.naviTab.onInit();
        this.fleetPanel = new FleetPanel(this.getState());
        this.fleetPanel.onInit();
        this.buildToolsAstronaut = new BuildToolsPanel(this.getState(), true);
        this.buildToolsAstronaut.onInit();
        this.aiPanelNew = new AIPanelNew(this.getState());
        this.aiPanelNew.onInit();
        this.inventoryPanelNew = new InventoryPanelNew(this.getState());
        this.inventoryPanelNew.onInit();
        this.weaponManagerPanelNew = new WeaponPanelNew(this.getState());
        this.weaponManagerPanelNew.onInit();
        this.navigationPanelNew = new NavigationPanelNew(this.getState());
        this.navigationPanelNew.onInit();
        this.structurePanelNew = new StructurePanelNew(this.getState());
        this.structurePanelNew.onInit();
        this.infoScroll = new GUIScrollablePanel(430.0F, 550.0F, this.getState());
        this.infoTextOverlay = new GUITextOverlay(10, 10, this.getState());
        this.infoTextOverlay.autoWrapOn = this.infoScroll;
        this.infoScroll.setContent(this.infoTextOverlay);
        this.infoScroll.onInit();
        this.shopPanelNew = new ShopPanelNew(this.getState());
        this.factionPanelNew = new FactionPanelNew(this.getState());
        this.catalogPanelNew = new CatalogPanelNew(this.getState());
        if (GuiDrawer.isNewHud()) {
            assert this.inventoryPanelNew != null;

            this.setBuildSideBar(new BottomBarBuild(this.getState(), this.inventoryPanelNew));
        } else {
            this.setBuildSideBar(new BuildSideBarOld(this.getState()));
        }

        this.getBuildSideBar().onInit();

        try {
            this.helpPanel = new GUIHelpPanelManager(this.getState(), "." + File.separator + "data" + File.separator + "tutorial" + File.separator + "ControlHelpers.xml");
            this.helpPanel.onInit();
        } catch (ParserConfigurationException var2) {
            var2.printStackTrace();
        } catch (SAXException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.weaponSideBars = new WeaponBottomBar[10];

        for(int var5 = 0; var5 < this.weaponSideBars.length; ++var5) {
            this.weaponSideBars[var5] = new WeaponBottomBar(this.getState(), var5);
            this.weaponSideBars[var5].onInit();
        }

        this.mapPanel = new MapToolsPanel(this.getState());
        this.mapPanel.onInit();
        this.advancedBuildMode.onInit();
        this.advancedBuildModeContainer.onInit();

        //INSERTED CODE
        PlayerGUICreateEvent event = new PlayerGUICreateEvent(this);
        StarLoader.fireEvent(event, false);
        ///

        this.doOrientation();
    }

    private boolean structureControllerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getStructureControlManager().isTreeActive();
    }

    private boolean thrustManagerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getThrustManager().isTreeActive();
    }

    public void deactivateAll() {
        this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().deactivateAll();
    }

    public void doOrientation() {
        this.shipInformationPanel.orientate(17);
        this.getBuildSideBar().orientate(40);
        this.helpPanel.orientate(9);
        Vector3f var10000 = this.helpPanel.getPos();
        var10000.y -= this.weaponSideBars[0].getHeight() / 2.0F - 30.0F;
        var10000 = this.helpPanel.getPos();
        var10000.x += 30.0F;

        for(int var1 = 0; var1 < this.weaponSideBars.length; ++var1) {
            this.weaponSideBars[var1].orientate(40);
        }

        this.mapPanel.orientate(40);
    }

    public float getHeight() {
        return 0.0F;
    }

    public GameClientState getState() {
        return (GameClientState)super.getState();
    }

    public float getWidth() {
        return 0.0F;
    }

    public boolean isPositionCenter() {
        return false;
    }

    public void drawChat() {
        if (!this.getState().getPlayer().getPlayerChannelManager().getAvailableChannels().isEmpty()) {
            GUIElement.enableOrthogonal();
            if (!this.chatDryRun) {
                if (this.firstDrawChat == 0L) {
                    this.firstDrawChat = this.getState().getUpdateTime();
                }

                GlUtil.setColorMask(false);
                this.chat.draw();
                GlUtil.setColorMask(true);
                if (this.getState().getUpdateTime() > this.firstDrawChat + 15000L) {
                    this.chatDryRun = true;
                }
            }

            if (this.chatManagerActive()) {
                this.chat.draw();
            } else {
                this.chat.drawAsHud();
            }

            GUIElement.disableOrthogonal();
        }

    }

    private boolean chatManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getChatControlManager().isActive();
    }

    public boolean isDrawShipSideBar() {
        return (this.shipExternalActive() || this.shipControllerManagerActive()) && !this.shopActive() && !this.inventoryActive();
    }

    public void drawToolTips() {
        if (EngineSettings.DRAW_TOOL_TIPS.isOn()) {
            this.getState().getController().getInputController().getGuiCallbackController().drawToolTips();
            if (this.inGameActive()) {
                GUIElement.enableOrthogonal();
                this.getBuildSideBar().drawToolTip();
                GUIElement.disableOrthogonal();
                GUIElement.enableOrthogonal();
                this.advancedBuildMode.drawToolTip(this.getState().getUpdateTime());
                GUIElement.disableOrthogonal();
                GUIElement.enableOrthogonal();
                this.advancedBuildModeContainer.drawToolTip(this.getState().getUpdateTime());
                GUIElement.disableOrthogonal();
            }

            if (this.inventoryActive()) {
                GUIElement.enableOrthogonal();
                this.inventoryPanelNew.drawToolTip();
                GUIElement.disableOrthogonal();
            }

            if (this.shipControllerManagerActive()) {
                GUIElement.enableOrthogonal();
                this.weaponManagerPanelNew.drawToolTip();
                GUIElement.disableOrthogonal();
                GUIElement.enableOrthogonal();
                this.weaponSideBars[this.selectedWeaponBar].drawToolTip();
                GUIElement.disableOrthogonal();
            }

            InventorySlotOverlayElement.drawFixedToolTips();
        }
    }

    private boolean factionControllerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getFactionControlManager().isTreeActive();
    }

    public boolean isInAstronautMode() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getPlayerCharacterManager().isTreeActive();
    }

    public SegmentBuildController getActiveBuildController() {
        if (this.getSegmentControlManager().getSegmentBuildController().isTreeActive()) {
            return this.getSegmentControlManager().getSegmentBuildController();
        } else {
            return this.getShipControllerManager().getSegmentBuildController().isTreeActive() ? this.getShipControllerManager().getSegmentBuildController() : null;
        }
    }

    public HotbarInterface getBuildSideBar() {
        return this.buildSideBar;
    }

    public void setBuildSideBar(HotbarInterface var1) {
        this.buildSideBar = var1;
    }

    public SegmentControlManager getSegmentControlManager() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getSegmentControlManager();
    }

    public ShipControllerManager getShipControllerManager() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().getShipControlManager();
    }

    private boolean inGameActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().isTreeActive();
    }

    private boolean inventoryActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getInventoryControlManager().isTreeActive();
    }

    public boolean isMouseOnPanel() {
        boolean var1 = PlayerInteractionControlManager.isAdvancedBuildMode(this.getState()) && this.advancedBuildMode.isInside();
        boolean var2 = this.advancedBuildModeContainer.isInside();
        return var1 || var2;
    }

    public void managerChanged(ElementCollectionManager<?, ?, ?> var1) {
        this.weaponManagerPanelNew.managerChanged(var1);
    }

    private boolean mapActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getMapControlManager().isTreeActive();
    }

    private boolean navigationActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getNavigationControlManager().isTreeActive();
    }

    public void modSelectedWeaponBottomBar(int var1) {
        var1 = FastMath.cyclicModulo(this.selectedWeaponBar + var1, this.weaponSideBars.length);
        this.selectedWeaponBar = (byte)var1;
    }

    public byte getSelectedWeaponBottomBar() {
        return this.selectedWeaponBar;
    }

    public void setSelectedWeaponBottomBar(byte var1) {
        var1 = (byte)Math.min(this.weaponSideBars.length - 1, Math.max(0, var1));
        this.selectedWeaponBar = var1;
    }

    private boolean shipControllerManagerActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getWeaponControlManager().isActive();
    }

    private boolean shipExternalActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getPlayerIntercationManager().getInShipControlManager().getShipControlManager().getShipExternalFlightController().isTreeActive();
    }

    private boolean shopActive() {
        return this.getState().getGlobalGameControlManager().getIngameControlManager().getPlayerGameControlManager().getShopControlManager().isTreeActive();
    }

    public void update(Timer var1) {
        this.topBar.updateCreditsAndSpeed();
        this.topBar.update(var1);
        this.weaponManagerPanelNew.update(var1);
        this.getBuildSideBar().update(var1);
        this.advancedBuildMode.update(var1);
        this.advancedBuildModeContainer.update(var1);
        this.timeSpent += var1.getDelta();
        if (!this.tabInfo && this.timeSpent > 15.0F) {
            this.tabInfo = true;
            this.getState().getController().showBigMessage("LLALLSSKKS", Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_PLAYERPANEL_2, StringTools.format(Lng.ORG_SCHEMA_GAME_CLIENT_VIEW_GUI_PLAYERPANEL_3, new Object[]{KeyboardMappings.RADIAL_MENU.getKeyChar()}), 0.0F);
        }

        for(int var2 = 0; var2 < this.weaponSideBars.length; ++var2) {
            this.weaponSideBars[var2].update(var1);
        }

        this.aiPanelNew.update(var1);
        this.buildToolsAstronaut.update(var1);
        this.mapPanel.update(var1);
        this.helpPanel.update(var1);
        if (this.factionPanelNew != null) {
            this.factionPanelNew.update(var1);
        }

        if (this.fleetPanel != null) {
            this.fleetPanel.update(var1);
        }

        this.chat.update(var1);
        super.update(var1);
    }

    public StructurePanelNew getStructurePanel() {
        return this.structurePanelNew;
    }

    public TopBarInterface getTopBar() {
        return this.topBar;
    }

    public void setTopBar(TopBarInterface var1) {
        this.topBar = var1;
    }

    public WeaponControllerPanelInterface getWeaponManagerPanel() {
        return this.weaponManagerPanelNew;
    }

    public HotbarInterface getWeaponSideBar() {
        return this.weaponSideBars[this.selectedWeaponBar];
    }

    public boolean isPanelActive() {
        return this.panelActive;
    }

    public void setPanelActive(boolean var1) {
        this.panelActive = var1;
    }

    public InventoryPanelNew getInventoryPanel() {
        return this.inventoryPanelNew;
    }

    public void notifySwitch(AbstractControlManager var1) {
        if (var1 instanceof ChatControlManager && this.chat != null) {
            this.chat.onActivateChat(((ChatControlManager)var1).isActive());
        }

    }

    public ChatPanel getChat() {
        return this.chat;
    }

    public GUIHelpPanelManager getHelpPanel() {
        return this.helpPanel;
    }

    public GUITopBar getTopTaskBar() {
        return this.topTaskBar;
    }

    public void setTopTaskBar(GUITopBar var1) {
        this.topTaskBar = var1;
    }

    class TabCallback implements GUICallback {
        private TabCallback() {
        }

        public void callback(GUIElement var1, MouseEvent var2) {
            if (var2.getEventButtonState() && var2.getEventButton() == 0) {
                if (var1.getUserPointer().equals("INVENTORY")) {
                    if (!PlayerPanel.this.inventoryActive()) {
                        PlayerPanel.this.activateInventory();
                        return;
                    }
                } else if (var1.getUserPointer().equals("STRUCTURE")) {
                    if (!PlayerPanel.this.structureControllerManagerActive()) {
                        PlayerPanel.this.activateStructureControllerManager();
                        return;
                    }
                } else if (var1.getUserPointer().equals("WEAPON")) {
                    if (!PlayerPanel.this.shipControllerManagerActive()) {
                        PlayerPanel.this.activateWeaponAssignManager();
                        return;
                    }
                } else if (var1.getUserPointer().equals("FACTION")) {
                    if (!PlayerPanel.this.factionControllerManagerActive()) {
                        PlayerPanel.this.activateFactionManager();
                        return;
                    }
                } else if (var1.getUserPointer().equals("CATALOG")) {
                    if (!PlayerPanel.this.catalogControllerManagerActive()) {
                        PlayerPanel.this.activateCatalogManager();
                        return;
                    }
                } else if (var1.getUserPointer().equals("AI")) {
                    if (!PlayerPanel.this.aiManagerActive()) {
                        PlayerPanel.this.activateAIManager();
                        return;
                    }
                } else if (var1.getUserPointer().equals("SHOP")) {
                    if (!PlayerPanel.this.shopActive()) {
                        PlayerPanel.this.activateShop();
                        return;
                    }
                } else if (var1.getUserPointer().equals("NAVIGATION")) {
                    if (!PlayerPanel.this.navigationActive()) {
                        PlayerPanel.this.activateNavigation();
                        return;
                    }
                } else {
                    if (var1.getUserPointer().equals("X")) {
                        PlayerPanel.this.deactivateAll();
                        return;
                    }

                    assert false : "not known command: " + var1.getUserPointer();
                }
            }

        }

        public boolean isOccluded() {
            return !PlayerPanel.this.getState().getPlayerInputs().isEmpty();
        }
    }
}
