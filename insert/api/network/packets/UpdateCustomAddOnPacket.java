package api.network.packets;

import api.DebugFile;
import api.entity.Entity;
import api.entity.Player;
import api.main.GameClient;
import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import api.systems.addons.custom.CustomAddOn;

import java.io.IOException;

public class UpdateCustomAddOnPacket extends Packet {
    public UpdateCustomAddOnPacket() {

    }

    public UpdateCustomAddOnPacket(String addOnName, float charge, int charges, boolean autoCharge) {
        this.addOnName = addOnName;
        this.charge = charge;
        this.charges = charges;
        this.autoCharge = autoCharge;
    }

    private String addOnName;
    private float charge;
    private int charges;
    private boolean autoCharge;

    @Override
    public void readPacketData(PacketReadBuffer buf) throws IOException {
        addOnName = buf.readString();
        charge = buf.readFloat();
        charges = buf.readInt();
        autoCharge = buf.readBoolean();
    }

    @Override
    public void writePacketData(PacketWriteBuffer buf) throws IOException {
        buf.writeString(addOnName);
        buf.writeFloat(charge);
        buf.writeInt(charges);
        buf.writeBoolean(autoCharge);
    }

    @Override
    public void processPacketOnClient() {
        Entity ent = GameClient.getCurrentEntity();
        if(ent == null){
            DebugFile.warn("Updating charge value does not point to player");
            return;
        }
        CustomAddOn addOn = ent.getCustomAddon(addOnName);
        addOn.setCharge(charge);
        addOn.setCharges(charges);
        addOn.setAutoChargeOn(autoCharge);
        GameClient.showPopupMessage(charge + ", " + charges + ", " + autoCharge, 0);
    }

    @Override
    public void processPacketOnServer(Player sender) {

    }
}
