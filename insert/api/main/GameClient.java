package api.main;

import api.entity.Entity;
import api.network.Packet;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.schema.game.client.controller.GameClientController;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.PlayerControllable;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.common.data.physics.PhysicsExt;
import org.schema.game.common.data.physics.Vector3fb;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.RemoteSector;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.network.NetworkProcessor;

import javax.vecmath.Vector3f;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class GameClient {
    public static ArrayList<Entity> getNearbyEntities(){
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (SimpleTransformableSendableObject<?> value : getClientState().getCurrentSectorEntities().values()) {
            if(value instanceof SegmentController) {
                entities.add(new Entity((SegmentController) value));
            }
        }
        return entities;
    }

    public static GameClientState getClientState(){
        return GameClientState.instance;
    }
    public static GameClientController getClientController(){
        return GameClientState.instance.getController();
    }
    public static PlayerState getClientPlayerState(){
        return getClientState().getPlayer();
    }

    //Lots of internal stuff to be cleaned up later
    public static void makeChatMessage(String msg){
        GameClientState inst = getClientState();
        inst.chat(inst.getChat(), "[ALL]", msg, true);
    }
    public static void sendMessage(String s){
        getClientState().getChat().addToVisibleChat(s, "[ALL]", true);
    }

    public static void showPopupMessage(String str, int delay){
        getClientState().getController().popupInfoTextMessage(str, delay);
    }
    public static void showBigText(String header, String str, int time){
        getClientState().getController().showBigTitleMessage(header, str, time);
    }
    public static Collection<PlayerState> getConnectedPlayers(){
        return GameClientState.instance.getOnlinePlayersLowerCaseMap().values();
    }
    public static PlayerControllable getCurrentControl(){
        Set<ControllerStateUnit> units = getClientPlayerState().getControllerState().getUnits();
        if(units.isEmpty()) return null;
        ControllerStateUnit unit = units.iterator().next();
        return unit.playerControllable;
    }
    public static Ship getCurrentShip(){
        PlayerControllable con = getCurrentControl();
        if(con instanceof Ship){
            return (Ship) con;
        }else{
            return null;
        }
    }
    public static Entity getCurrentEntity(){
        PlayerControllable con = getCurrentControl();
        if(con instanceof SegmentController){
            return new Entity((SegmentController) con);
        }else{
            return null;
        }
    }
    public static Collection<Fleet> getAvailableFleets(){
        return getClientState().getFleetManager().getAvailableFleetsClient();
    }

    public static void sendPacketToServer(Packet apiPacket){
        try {
            NetworkProcessor processor = GameClient.getClientState().getProcessor();
            DataOutputStream output = new DataOutputStream(processor.getOutRaw());
            output.writeInt(-2); //Mod packet ID
            output.writeInt(apiPacket.getId()); //The packet ID we're sending
            apiPacket.writePacketData(output); //The info of the packet
            processor.getOutRaw().flush(); //Send
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setLoadString(String s){
        Controller.getResLoader().setLoadString(s);
    }

    public static void spawnBlockParticle(short id, Vector3f pos){
        GameClientState state = getClientState();
        if(state == null){
            return;
        }
        final Vector3fb vector3fb = new Vector3fb(pos);
        final Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set(vector3fb);
        state.getWorldDrawer().getShards().voronoiBBShatter((PhysicsExt)state.getPhysics(), transform, id, state.getCurrentSectorId(), transform.origin, null);
    }
}
