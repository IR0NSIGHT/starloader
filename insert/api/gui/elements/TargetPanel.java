package api.gui.elements;

import api.entity.Entity;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;

public class TargetPanel {

    private org.schema.game.client.view.gui.shiphud.newhud.TargetPanel internalPanel;
    private int[] pos = new int[3];
    private Entity targetedEntity;

    public TargetPanel(int[] pos, Entity targetedEntity) {
        this.pos = pos;
        this.targetedEntity = targetedEntity;
    }

    public void draw() {
        SimpleTransformableSendableObject internalEntity = targetedEntity.internalEntity;

    }
}
