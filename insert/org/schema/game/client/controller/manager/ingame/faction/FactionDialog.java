package org.schema.game.client.controller.manager.ingame.faction;

import java.util.regex.Pattern;

import api.listener.events.faction.FactionCreateEvent;
import api.mod.StarLoader;
import org.schema.game.client.controller.PlayerGameTextInput;
import org.schema.game.client.controller.manager.AbstractControlManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.schine.common.InputChecker;
import org.schema.schine.common.TextCallback;

public class FactionDialog extends PlayerGameTextInput {
    private AbstractControlManager man;

    public FactionDialog(GameClientState var1, String var2, AbstractControlManager var3) {
        super("FactionDialog", var1, 420, 200, 23, var2, "Enter a name for the new faction\n\nWARNING: if you aleady are in a faction,\nyou will leave that faction\nwhen creating a new one", (String)null);
        System.err.println("CURRENT FACTION CODE: " + var1.getPlayer().getFactionId());
        this.man = var3;
        this.setInputChecker(new InputChecker() {
            public boolean check(String var1, TextCallback var2) {
                if (var1.length() >= 6 && var1.length() < 24) {
                    if (Pattern.matches("[a-zA-Z0-9 _-]+", var1)) {
                        return true;
                    }

                    System.err.println("MATCH FOUND ^ALPHANUMERIC");
                }

                var2.onFailedTextCheck("Please only alphanumeric (and space, _, -) values \nand between 6 and 24 long!");
                return false;
            }
        });
    }

    public String[] getCommandPrefixes() {
        return null;
    }

    public String handleAutoComplete(String var1, TextCallback var2, String var3) {
        return var1;
    }

    public void onFailedTextCheck(String var1) {
        this.setErrorMessage(var1);
    }

    public boolean isOccluded() {
        return this.getState().getController().getPlayerInputs().indexOf(this) != this.getState().getController().getPlayerInputs().size() - 1;
    }

    public void onDeactivate() {
        this.man.suspend(false);
    }

    public boolean onInput(String var1) {
        this.getState().getPlayer().getFactionController().clientCreateFaction(var1, "a faction");
        //INSERTED CODE @56
        Faction faction = this.getState().getFaction();
        PlayerState player = this.getState().getPlayer();
        FactionCreateEvent factionCreateEvent = new FactionCreateEvent(faction, player);
        StarLoader.fireEvent(FactionCreateEvent.class, factionCreateEvent, true);
        //

        return true;
    }
}