package api.systems.addons.custom;

import api.DebugFile;
import api.element.block.Blocks;
import api.entity.Entity;
import api.entity.Player;
import api.main.GameServer;
import api.server.Server;
import api.universe.Sector;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.data.player.PlayerState;

import java.util.ArrayList;

public class SystemScannerAddOn extends CustomAddOn{
    public SystemScannerAddOn(ManagerContainer<?> var1) {
        super(var1);
    }

    @Override
    public float getChargeRate() {
        return 20;
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return entity.getMass();
    }

    @Override
    public long getUsableId() {
        return Blocks.SCANNER_ANTENNA.getPlayerUsableId();
    }

    @Override
    public String getWeaponRowName() {
        return "System Scanner";
    }

    @Override
    public short getWeaponRowIcon() {
        return Blocks.SCANNER_ANTENNA.getId();
    }

    @Override
    public float getDuration() {
        return 0;
    }

    @Override
    public boolean onExecute() {
        this.dischargeFully();
        StringBuilder sb = new StringBuilder();
        if(GameServer.getServerState() == null){
            return true;
        }
        if(this.isOnServer()) {
            GameServer.getServerState().scanOnServer(null, entity.getAttachedPlayers().get(0).getPlayerState());
        }
        sb.append("=!====== SCAN DATA =======!=");
        for (Sector sector : entity.getSector().getNearbySectors(4)) {
            for (Entity e : sector.getEntities()) {
                if(!e.isDocked() && e.isShip()) {
                    StringBuilder entry = new StringBuilder();
                    entry.append((e.isStation() ? "Station: " : "Ship: "));
                    entry.append(e.getRealName());
                    entry.append("  ");
                    entry.append("Sector: ").append(e.getSectorPosition().toString());
                    entry.append("  ");
                    ArrayList<Player> attachedPlayers = e.getAttachedPlayers();
                    entry.append("Mass: ").append(e.getMassString());
                    entry.append("    ");
                    if(!attachedPlayers.isEmpty()) {
                        entry.append("Pilot: ").append(attachedPlayers.get(0).getName());
                    }
                    sb.append(entry);
                    sb.append("\n");
                }
            }
        }
        for (Player player : entity.getAttachedPlayers()) {
            player.sendServerMessage(sb.toString());
            player.sendMail("System", "Scan Data", sb.toString());
        }
        return true;
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onInactive() {

    }

    @Override
    public String getName() {
        return "System Scanner";
    }
}
