package org.schema.game.network.commands;

import java.io.IOException;
import java.util.Arrays;

import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.Vehicle;
import org.schema.game.server.data.EntityRequest;
import org.schema.game.server.data.GameServerState;
import org.schema.schine.network.Command;
import org.schema.schine.network.client.ClientStateInterface;
import org.schema.schine.network.exception.NetworkObjectNotFoundException;
import org.schema.schine.network.server.ServerProcessor;
import org.schema.schine.network.server.ServerStateInterface;

import javax.swing.*;

public class CreateNewShip extends Command {
    public CreateNewShip() {
    }

    public void clientAnswerProcess(Object[] var1, ClientStateInterface var2, short var3) throws NetworkObjectNotFoundException, IOException {
        JOptionPane.showMessageDialog(null, Arrays.toString(var1));
    }

    public void serverProcess(ServerProcessor var1, Object[] var2, ServerStateInterface var3, short var4) throws NetworkObjectNotFoundException, IOException, Exception {
        JOptionPane.showMessageDialog(null, "someone spawned ship");
        EntityRequest var6 = null;
        if (var2[var2.length - 1].equals("Ship")) {
            var6 = new EntityRequest(var1.getClient().getId(), var2, Ship.class);
        }

        if (var2[var2.length - 1].equals("Station")) {
            var6 = new EntityRequest(var1.getClient().getId(), var2, SpaceStation.class);
        }

        if (var2[var2.length - 1].equals("Vehicle")) {
            System.err.println("REQUESTING ON SERVER VEHICLE");
            var6 = new EntityRequest(var1.getClient().getId(), var2, Vehicle.class);
        }

        assert var6 != null;

        if (var6 != null) {
            synchronized(((GameServerState)var3).getEntityRequests()) {
                ((GameServerState)var3).getEntityRequests().add(var6);
            }
        } else {
            System.err.println("[NT] ERROR: client made invalid entity request: " + var1.getClient());
        }
    }
}