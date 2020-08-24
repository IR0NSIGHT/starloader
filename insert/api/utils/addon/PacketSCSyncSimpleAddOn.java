package api.utils.addon;

import api.DebugFile;
import api.ModPlayground;
import api.common.GameClient;
import api.common.GameCommon;
import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import api.utils.game.SegmentControllerUtils;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.ManagedUsableSegmentController;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.network.objects.Sendable;

import java.io.IOException;

/**
 * Packet [Server -> Client]
 * Updates a simple add on
 */
public class PacketSCSyncSimpleAddOn extends Packet {
    /**
     * Default constructor, the data will be constructed from the PacketReadBuffer when readPacketData is called
     */
    public PacketSCSyncSimpleAddOn() {

    }

    /**
     * Constructor to set the data that will be written to the network
     */
    public PacketSCSyncSimpleAddOn(Sendable entity, SimpleAddOn addOn, float charge, int charges, boolean autoCharge) {
        this.entityId = entity.getId();
        this.usableId = addOn.getUsableId();
        this.charge = charge;
        this.charges = charges;
        this.autoCharge = autoCharge;
    }

    private int entityId;
    private long usableId;
    private float charge;
    private int charges;
    private boolean autoCharge;

    /**
     * Read data from a packet buffer (MUST BE IN THE SAME ORDER AS writePacketData)
     */
    @Override
    public void readPacketData(PacketReadBuffer buf) throws IOException {
        entityId = buf.readInt();
        usableId = buf.readLong();
        charge = buf.readFloat();
        charges = buf.readInt();
        autoCharge = buf.readBoolean();
    }

    @Override
    public void writePacketData(PacketWriteBuffer buf) throws IOException {
        buf.writeInt(entityId);
        buf.writeLong(usableId);
        buf.writeFloat(charge);
        buf.writeInt(charges);
        buf.writeBoolean(autoCharge);
    }
    //When the client receives the packet
    @Override
    public void processPacketOnClient() {
        Sendable s = GameCommon.getGameObject(entityId);
        assert s instanceof ManagedUsableSegmentController<?> : "Packet to update SimpleAddOn did not happen on a MUSC<?>";
        ManagedUsableSegmentController<?> ship = (ManagedUsableSegmentController<?>) s;
        PlayerUsableInterface addon = SegmentControllerUtils.getAddon(ship, usableId);
        assert addon instanceof SimpleAddOn : "SimpleAddOn update packet did not point to SimpleAddOn";
        SimpleAddOn on = (SimpleAddOn) addon;
        //
        on.setCharge(charge);
        on.setCharges(charges);
        on.setAutoChargeOn(autoCharge);
        //
        ModPlayground.broadcastMessage(charge + ", " + charges + ", " + autoCharge);
    }
    //When the server receives the packet
    @Override
    public void processPacketOnServer(PlayerState playerState) {

    }
}
