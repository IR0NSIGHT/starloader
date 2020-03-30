//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.elements.activation;

import api.inventory.ItemStack;
import api.listener.events.block.BlockActivateEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Iterator;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer.Hos;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentController;
import org.schema.game.common.controller.elements.BlockMetaDataDummy;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.meta.weapon.MarkerBeam;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.MouseEvent.ShootButton;
import org.schema.schine.graphicsengine.core.settings.ContextFilter;
import org.schema.schine.input.InputType;
import org.schema.schine.input.KeyboardMappings;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class ActivationCollectionManager extends ControlBlockElementCollectionManager<AbstractUnit, ActivationCollectionManager, ActivationElementManager> implements PlayerUsableInterface {
    public int currentSignal;
    private MarkerBeam destination;

    public ActivationCollectionManager(SegmentPiece var1, SegmentController var2, ActivationElementManager var3) {
        super(var1, (short)32767, var2, var3);
    }

    public int getMargin() {
        return 0;
    }

    protected Class<AbstractUnit> getType() {
        return AbstractUnit.class;
    }

    public boolean needsUpdate() {
        return false;
    }

    public boolean isUsingIntegrity() {
        return false;
    }

    public AbstractUnit getInstance() {
        return new AbstractUnit();
    }

    public void onChangedCollection() {
        super.onChangedCollection();
        if (!this.getSegmentController().isOnServer()) {
            ((GameClientState)this.getSegmentController().getState()).getWorldDrawer().getGuiDrawer().managerChanged(this);
        }

    }

    public GUIKeyValueEntry[] getGUICollectionStats() {
        return new GUIKeyValueEntry[0];
    }

    public String getModuleName() {
        return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_ACTIVATION_ACTIVATIONCOLLECTIONMANAGER_0;
    }

    public boolean hasTag() {
        return this.destination != null;
    }

    protected void applyMetaData(BlockMetaDataDummy var1) {
        assert this.destination == null;

        this.destination = ((ActivationDestMetaDataDummy)var1).dest;
    }

    protected Tag toTagStructurePriv() {
        return new Tag(Type.STRUCT, (String)null, new Tag[]{this.destination == null ? new Tag(Type.BYTE, (String)null, (byte)0) : this.destination.toTag(), FinishTag.INST});
    }

    public int onActivate(ActivationElementManager var1, SegmentPiece var2, boolean var3, boolean var4) {
        if (!this.isOnServer()) {
            return 0;
        } else {
            LongOpenHashSet var5;
            if ((var5 = (LongOpenHashSet)this.getSegmentController().getControlElementMap().getControllingMap().getAll().get(ElementCollection.getIndex(this.getControllerPos()))) != null && var5.size() > 0 && this.getTotalSize() < var5.size()) {
                System.err.println(this.getSegmentController().getState() + " " + this.getSegmentController() + " CANNOT ACTIVATE: totalSize: " + this.getTotalSize() + " / controlMap: " + (var5 != null ? var5.size() : 0) + "::: HashSet: " + var5);
                return -1;
            } else {
                assert var2.getAbsoluteIndex() == this.getControllerElement().getAbsoluteIndex() : var2 + "; " + this.getControllerElement();

                if (ElementKeyMap.isValidType(var2.getType())) {
                    //INSERTED CODE
                    BlockActivateEvent ev = new BlockActivateEvent(var1, var2, new ItemStack(var2.getType()));
                    StarLoader.fireEvent(BlockActivateEvent.class, ev);
                    if(ev.isCanceled()){
                        return 0;
                    }
                    ///
                    long var6;
                    ActivationCollectionManager var8;
                    int var9;
                    int var10;
                    if (var2.getType() == 408) {
                        var6 = var2.getAbsoluteIndex();

                        for(var10 = 0; var10 < var1.getCollectionManagers().size(); ++var10) {
                            var8 = (ActivationCollectionManager)var1.getCollectionManagers().get(var10);

                            for(var9 = 0; var9 < var8.getElementCollections().size(); ++var9) {
                                if (((AbstractUnit)var8.getElementCollections().get(var9)).contains(var6)) {
                                    var8.getControllerElement().refresh();
                                    var4 = var4 && var8.getControllerElement().isActive();
                                }
                            }
                        }
                    } else if (var2.getType() != 409) {
                        if (var2.getType() == 410) {
                            var4 = !var4;
                        } else if (var2.getType() == 979) {
                            var4 = Math.random() > 0.5D;
                        } else if (!ElementKeyMap.isButton(var2.getType())) {
                            if (var2.getType() == 667) {
                                if (var4) {
                                    var4 = !var3;
                                } else {
                                    var4 = var3;
                                }
                            } else {
                                var2.getType();
                            }
                        }
                    } else {
                        var6 = var2.getAbsoluteIndex();

                        for(var10 = 0; var10 < var1.getCollectionManagers().size(); ++var10) {
                            var8 = (ActivationCollectionManager)var1.getCollectionManagers().get(var10);

                            for(var9 = 0; var9 < var8.getElementCollections().size(); ++var9) {
                                if (((AbstractUnit)var8.getElementCollections().get(var9)).contains(var6)) {
                                    var8.getControllerElement().refresh();
                                    var4 = var4 || var8.getControllerElement().isActive();
                                }
                            }
                        }
                    }

                    if (var3 != var4) {
                        Iterator var11 = this.getElementCollections().iterator();

                        while(var11.hasNext()) {
                            ((AbstractUnit)var11.next()).onActivate(this, var1, var2, var4);
                        }
                    }

                    this.currentSignal = ((SendableSegmentController)this.getSegmentController()).signalId;
                    var2.setActive(var4);
                }

                return var4 ? 1 : 0;
            }
        }
    }

    public void handleControl(ControllerStateInterface var1, Timer var2) {
        if (var1.isFlightControllerActive() && var1.clickedOnce(0) && this.getSegmentController().isOnServer()) {
            ((SendableSegmentController)this.getSegmentController()).activateSwitchSingleServer(this.getControllerIndex());
        }

    }

    public MarkerBeam getDestination() {
        return this.destination;
    }

    public void setDestination(MarkerBeam var1) {
        this.destination = var1;
    }

    public boolean isControllerConnectedTo(long var1, short var3) {
        return var3 == 1;
    }

    public boolean isPlayerUsable() {
        return this.getControllerElement().getType() == 670;
    }

    public boolean isAddToPlayerUsable() {
        return this.isPlayerUsable();
    }

    public void onPlayerDetachedFromThisOrADock(ManagedUsableSegmentController<?> var1, PlayerState var2, PlayerControllable var3) {
    }

    public WeaponRowElementInterface getWeaponRow() {
        return super.getWeaponRow();
    }

    public void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2) {
    }

    public void addHudConext(ControllerStateUnit var1, HudContextHelpManager var2, Hos var3) {
        if (this.getControllerElement() != null) {
            this.getControllerElement().refresh();
            boolean var4 = this.getControllerElement().isActive();
            var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), var4 ? Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_ACTIVATION_ACTIVATIONCOLLECTIONMANAGER_1 : Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_ACTIVATION_ACTIVATIONCOLLECTIONMANAGER_2, var3, ContextFilter.IMPORTANT);
        }

    }
}
