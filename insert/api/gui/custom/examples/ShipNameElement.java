package api.gui.custom.examples;

import api.entity.Entity;
import api.entity.Player;
import api.gui.custom.CustomHudText;
import api.main.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.schema.game.common.data.player.faction.FactionRelation;
import org.schema.schine.input.InputState;

import javax.vecmath.Vector4f;
import java.util.ArrayList;

public class ShipNameElement extends CustomHudText {
    public ShipNameElement(UnicodeFont unicodeFont, InputState inputState) {
        super(100, 20, unicodeFont, Color.gray, inputState);
    }

    public Entity entity;
    public void setEntity(Entity e){
        entity = e;
    }

    @Override
    public void onInit() {
        super.onInit();
        setTextSimple(new Object(){
            @Override
            public String toString() {
                if(entity == null) return "";
                return entity.getName();
            }
        });
    }
}
