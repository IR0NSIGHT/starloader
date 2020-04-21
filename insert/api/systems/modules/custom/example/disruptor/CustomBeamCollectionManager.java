//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package api.systems.modules.custom.example.disruptor;

import api.element.block.Blocks;
import api.systems.modules.custom.CustomShipBeamElement;
import org.schema.common.util.StringTools;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer.Hos;
import org.schema.game.common.controller.Salvager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.BlockMetaDataDummy;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.beam.BeamCollectionManager;
import org.schema.game.common.controller.elements.beam.tractorbeam.*;
import org.schema.game.common.controller.elements.beam.tractorbeam.TractorBeamHandler.TractorMode;
import org.schema.game.common.data.ManagedSegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.beam.BeamHandler;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.network.objects.remote.RemoteValueUpdate;
import org.schema.game.network.objects.valueUpdate.NTValueUpdateInterface;
import org.schema.game.network.objects.valueUpdate.ValueUpdate.ValTypes;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.MouseEvent.ShootButton;
import org.schema.schine.graphicsengine.core.settings.ContextFilter;
import org.schema.schine.input.InputType;
import org.schema.schine.input.KeyboardMappings;

public class CustomBeamCollectionManager extends BeamCollectionManager<CustomBeamUnit, CustomBeamCollectionManager, CustomBeamElementManager> {
    private final TractorBeamHandler handler;
    CustomShipBeamElement customElement;
    private final ControlBlockElementCollectionManager<CustomBeamUnit, CustomBeamCollectionManager, CustomBeamElementManager>.ConnectedLogicCon cTmp;

    public CustomBeamCollectionManager(SegmentPiece var1, SegmentController var2, CustomBeamElementManager var3) {
        super(var1, var3.customElement.getControllerBlock().getId(), var2, var3);
        this.customElement = var3.customElement;
        this.cTmp = new ConnectedLogicCon();
        this.handler = new TractorBeamHandler((Salvager)var2, this);
    }

    public TractorBeamHandler getHandler() {
        return this.handler;
    }

    protected void applyMetaData(BlockMetaDataDummy var1) {
    }

    protected Class<CustomBeamUnit> getType() {
        return CustomBeamUnit.class;
    }

    public CustomBeamUnit getInstance() {
        return new CustomBeamUnit(customElement);
    }

    public void handleMouseEvent(ControllerStateUnit var1, MouseEvent var2) {
    }


    public void handleControlShot(ControllerStateInterface var1, Timer var2) {
        if (var1.isPrimaryShootButtonDown()) {
            super.handleControlShot(var1, var2);
        }

    }

    public void handleKeyEvent(ControllerStateUnit var1, KeyboardMappings var2) {
        super.handleKeyEvent(var1, var2);

    }


    public String getModuleName() {
        return "Custom Module";
    }

    public void addHudConext(ControllerStateUnit var1, HudContextHelpManager var2, Hos var3) {
        var2.addHelper(InputType.MOUSE, ShootButton.PRIMARY_FIRE.getButton(), Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORBEAMCOLLECTIONMANAGER_3, var3, ContextFilter.IMPORTANT);
        //var2.addHelper(InputType.KEYBOARD, KeyboardMappings.SWITCH_FIRE_MODE.getMapping(), StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_ELEMENTS_BEAM_TRACTORBEAM_TRACTORBEAMCOLLECTIONMANAGER_1, new Object[]{this.mode.getName()}), var3, ContextFilter.CRUCIAL);
    }

}
