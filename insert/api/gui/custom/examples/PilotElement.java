package api.gui.custom.examples;

import api.entity.Entity;
import api.entity.Player;
import api.gui.custom.CustomHudText;
import api.main.GameClient;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.schema.game.common.data.player.faction.FactionRelation;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;

import javax.vecmath.Vector4f;
import java.util.ArrayList;

public class PilotElement extends CustomHudText {
    public PilotElement(UnicodeFont unicodeFont, InputState inputState) {
        super(100, 20, unicodeFont, Color.green, inputState);
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
                ArrayList<Player> attachedPlayers = entity.getAttachedPlayers();
                if(attachedPlayers.size() > 0){
                    Player player = attachedPlayers.get(0);
                    FactionRelation.RType relation = GameClient.getClientState().getPlayer().getRelation(player.getPlayerState());
                    PilotElement.this.setColor(new Vector4f(relation.defaultColor.x, relation.defaultColor.y, relation.defaultColor.z, 255));
                    return player.getName();
                }else{
                    return "No Pilot";
                }
            }
        });
    }
}
