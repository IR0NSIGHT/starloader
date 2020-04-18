//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.player.playermessage;

import api.listener.events.MailReceiveEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.schema.common.util.StringTools;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.controller.observer.DrawerObservable;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.game.network.objects.remote.RemotePlayerMessage;
import org.schema.game.network.objects.remote.RemotePlayerMessageBuffer;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.schine.common.language.Lng;
import org.schema.schine.network.commands.LoginRequest;
import org.schema.schine.network.objects.remote.RemoteIntBuffer;
import org.schema.schine.network.server.ServerMessage;

public class PlayerMessageController extends DrawerObservable {
    private final ClientChannel channel;
    public ObjectArrayList<PlayerMessage> messagesReceived = new ObjectArrayList();
    public boolean initialRequest;
    ObjectArrayFIFOQueue<PlayerMessage> received = new ObjectArrayFIFOQueue();
    private int requCount;
    private boolean unreadMessages;

    public PlayerMessageController(ClientChannel var1) {
        this.channel = var1;
    }

    public static PlayerMessage getNew(String var0, String var1, String var2, String var3) {
        PlayerMessage var4;
        (var4 = new PlayerMessage()).setFrom(var0);
        var4.setTo(var1);
        var4.setTopic(var2);
        var4.setSent(System.currentTimeMillis());
        var4.setMessage(var3);
        var4.setRead(false);
        return var4;
    }

    public void retriveInitial(int var1) {
        GameServerState var2 = (GameServerState)this.channel.getState();

        try {
            this.messagesReceived.addAll(var2.getDatabaseIndex().getTableManager().getPlayerMessagesTable().loadPlayerMessages(this.channel.getPlayer().getName().toLowerCase(Locale.ENGLISH), System.currentTimeMillis(), var1));
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

        Collections.sort(this.messagesReceived);
        if (this.channel.getPlayer().isNewPlayerServer()) {
            PlayerMessage var4;
            (var4 = new PlayerMessage()).setFrom("StarMade");
            var4.setTo(this.channel.getPlayer().getName());
            var4.setTopic("Welcome to the new Mail System");
            var4.setSent(System.currentTimeMillis());
            var4.setMessage("Messages are sent even when people are offline.\nThey are delivered when they come online!\n\nThanks for playing StarMade\n - schema");
            var4.setRead(false);
            this.messagesReceived.add(var4);
        }

        Iterator var5 = this.messagesReceived.iterator();

        while(var5.hasNext()) {
            PlayerMessage var6 = (PlayerMessage)var5.next();
            this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var6, true));
        }

    }

    public void retriveMore(int var1) {
        try {
            GameServerState var2 = (GameServerState)this.channel.getState();
            long var3 = System.currentTimeMillis();
            if (this.messagesReceived.size() > 0) {
                var3 = ((PlayerMessage)this.messagesReceived.get(this.messagesReceived.size() - 1)).getSent();
            }

            ObjectArrayList var7 = var2.getDatabaseIndex().getTableManager().getPlayerMessagesTable().loadPlayerMessages(this.channel.getPlayer().getName().toLowerCase(Locale.ENGLISH), var3, var1);
            System.err.println("[SERVER][MESSAGES] retrieveing " + var1 + " more messages for " + this.channel.getPlayer() + "; loaded: " + var7.size());
            this.messagesReceived.addAll(var7);
            Collections.sort(this.messagesReceived);
            Iterator var6 = var7.iterator();

            while(var6.hasNext()) {
                PlayerMessage var8 = (PlayerMessage)var6.next();
                this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var8, true));
            }

        } catch (SQLException var5) {
            var5.printStackTrace();
        }
    }
    long lastTimeMS = 0;
    public void update() {

        if (!this.received.isEmpty()) {
            while(true) {
                if (this.received.isEmpty()) {
                    Collections.sort(this.messagesReceived);
                    break;
                }

                PlayerMessage var1;
                synchronized(this.received) {
                    var1 = (PlayerMessage)this.received.dequeue();
                }
                System.err.println("[PLAYERMESSAGE] " + this.channel.getState() + " Handle Received PlayerMessage: " + var1);

                //INSERTED CODE @109
                MailReceiveEvent event = new MailReceiveEvent(var1, this.channel, this.channel.isOnServer());
                StarLoader.fireEvent(MailReceiveEvent.class, event);
                if(!event.isCanceled()) {
                    if (this.channel.isOnServer()) {
                        this.handleReceivedOnServer(var1, false);
                    } else {
                        this.handleReceivedOnClient(var1);
                    }
                }
                /////
            }
        }

        this.unreadMessages = false;
        Iterator var4 = this.messagesReceived.iterator();

        while(var4.hasNext()) {
            if (!((PlayerMessage)var4.next()).isRead()) {
                this.unreadMessages = true;
                break;
            }
        }

        if (this.requCount > 0) {
            int var5 = this.requCount;
            this.requCount = 0;
            if (this.messagesReceived.isEmpty()) {
                this.retriveInitial(var5);
                return;
            }

            this.retriveMore(var5);
        }

    }

    private void handleReceivedOnClient(PlayerMessage var1) {
        if (var1.isDeleted()) {
            boolean var2 = this.messagesReceived.remove(var1);
            System.err.println("[CLIENT][PLAYERMESSAGES] remove " + var1 + "; removed: " + var2);
        } else {
            this.messagesReceived.add(var1);
        }

        Iterator var3 = this.messagesReceived.iterator();

        while(var3.hasNext()) {
            if (!((PlayerMessage)var3.next()).isRead()) {
                ((GameClientState)this.channel.getState()).getController().popupInfoTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_0, 0.0F);
                break;
            }
        }

        this.notifyObservers(var1);
    }

    private void handleReceivedOnServer(PlayerMessage var1, boolean var2) {
        GameServerState var3 = (GameServerState)this.channel.getState();
        String var4;
        if ((var4 = var1.getTo()).toLowerCase(Locale.ENGLISH).startsWith("faction[")) {
            if (var4.toLowerCase(Locale.ENGLISH).endsWith("]")) {
                if (8 < var4.length() - 1) {
                    var4 = var4.substring(8, var4.length() - 1);
                    Iterator var16 = var3.getFactionManager().getFactionCollection().iterator();

                    Faction var10;
                    do {
                        if (!var16.hasNext()) {
                            return;
                        }
                    } while(!(var10 = (Faction)var16.next()).getName().toLowerCase(Locale.ENGLISH).equals(var4.toLowerCase(Locale.ENGLISH)));

                    Iterator var11 = var10.getMembersUID().values().iterator();

                    while(var11.hasNext()) {
                        FactionPermission var12 = (FactionPermission)var11.next();
                        PlayerMessage var13 = getNew(var1.getFrom(), var12.playerUID, "[FACTION MAIL] " + var1.getTopic(), var1.getMessage());
                        System.err.println("[CLIENT][PLAYERMESSAGE] sending message: " + var13);
                        this.handleReceivedOnServer(var13, true);
                    }

                }
            }
        } else {
            if (var1.isDeleted()) {
                try {
                    System.err.println("[SERVER][PLAYERMESSAGES] remove " + var1);
                    var3.getDatabaseIndex().getTableManager().getPlayerMessagesTable().deleteMessage(var1);
                    this.messagesReceived.remove(var1);
                    this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var1, this.channel.getNetworkObject()));
                    return;
                } catch (SQLException var9) {
                    var9.printStackTrace();
                }
            }

            try {
                var3.getDatabaseIndex().getTableManager().getPlayerMessagesTable().updateOrInsertMessage(var1);
            } catch (SQLException var7) {
                var7.printStackTrace();
            }

            boolean var14 = false;
            Iterator var5 = this.messagesReceived.iterator();

            while(var5.hasNext()) {
                PlayerMessage var6;
                if ((var6 = (PlayerMessage)var5.next()).getFrom().toLowerCase(Locale.ENGLISH).equals(var1.getFrom().toLowerCase(Locale.ENGLISH)) && var6.getTo().toLowerCase(Locale.ENGLISH).equals(var1.getTo().toLowerCase(Locale.ENGLISH)) && var6.getSent() == var1.getSent()) {
                    var6.setRead(var1.isRead());
                    var14 = true;
                    System.err.println("[SERVER][PLAYERMESSAGES] changed Player message " + var6);
                    break;
                }
            }

            if (!var14) {
                ServerMessage var15;
                try {
                    var3.getPlayerFromName(var1.getTo()).getClientChannel().getPlayerMessageController().receiveOnServer(var1);
                    if (!var2) {
                        var15 = new ServerMessage(new Object[]{303, var1.getTo()}, 1, this.channel.getPlayer().getId());
                        this.channel.getPlayer().sendServerMessage(var15);
                    }

                    return;
                } catch (PlayerNotFountException var8) {
                    if (!var2) {
                        var15 = new ServerMessage(new Object[]{304, var1.getTo()}, 2, this.channel.getPlayer().getId());
                        this.channel.getPlayer().sendServerMessage(var15);
                    }
                }
            }

        }
    }

    private void receiveOnServer(PlayerMessage var1) {
        this.messagesReceived.add(var1);
        Collections.sort(this.messagesReceived);
        this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var1, this.channel.getNetworkObject()));
    }

    public boolean clientSend(String var1, String var2, String var3, String var4) {
        assert !this.channel.isOnServer();

        if (var2.length() < 3) {
            ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_3, 0.0F);
            return false;
        } else if (var2.length() > 32) {
            ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_4, 0.0F);
            return false;
        } else if (LoginRequest.reserved.contains(var2)) {
            ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_5, new Object[]{var2}), 0.0F);
            return false;
        } else if (var2.toLowerCase(Locale.ENGLISH).startsWith("faction[")) {
            if (!var2.toLowerCase(Locale.ENGLISH).endsWith("]")) {
                ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_6, 0.0F);
                return false;
            } else if (8 < var2.length() - 1) {
                String var8 = var2.substring(8, var2.length() - 1);
                Iterator var6 = ((GameClientState)this.channel.getState()).getFactionManager().getFactionCollection().iterator();

                do {
                    if (!var6.hasNext()) {
                        ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_8, new Object[]{var8}), 0.0F);
                        return false;
                    }
                } while(!((Faction)var6.next()).getName().toLowerCase(Locale.ENGLISH).equals(var8.toLowerCase(Locale.ENGLISH)));

                PlayerMessage var7 = getNew(var1, var2, var3, var4);
                System.err.println("[CLIENT][PLAYERMESSAGE] sending message: " + var7);
                this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var7, this.channel.getNetworkObject()));
                ((GameClientState)this.channel.getState()).getController().popupInfoTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_7, new Object[]{var8}), 0.0F);
                return true;
            } else {
                ((GameClientState)this.channel.getState()).getController().popupAlertTextMessage(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_9, 0.0F);
                return false;
            }
        } else {
            PlayerMessage var5 = getNew(var1, var2, var3, var4);
            System.err.println("[CLIENT][PLAYERMESSAGE] sending message: " + var5);
            this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var5, this.channel.getNetworkObject()));
            ((GameClientState)this.channel.getState()).getController().popupInfoTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_PLAYER_PLAYERMESSAGE_PLAYERMESSAGECONTROLLER_10, new Object[]{var5.getTo()}), 0.0F);
            return true;
        }
    }

    public void serverSend(String var1, String var2, String var3, String var4) {
        assert this.channel.isOnServer();

        PlayerMessage var5 = getNew(var1, var2, var3, var4);
        System.err.println("[SERVER] sending message: " + var5);
        this.messagesReceived.add(var5);
        Collections.sort(this.messagesReceived);
        this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var5, true));
    }

    public void handleReceived(RemotePlayerMessageBuffer var1, RemoteIntBuffer var2) {
        int var3;
        for(var3 = 0; var3 < var1.getReceiveBuffer().size(); ++var3) {
            PlayerMessage var4 = (PlayerMessage)((RemotePlayerMessage)var1.getReceiveBuffer().get(var3)).get();
            synchronized(this.received) {
                System.err.println("[PLAYERMESSAGE] " + this.channel.getState() + " Received PlayerMessage: " + var4);
                this.received.enqueue(var4);
            }
        }

        for(var3 = 0; var3 < var2.getReceiveBuffer().size(); ++var3) {
            int var7 = var2.getReceiveBuffer().get(var3);
            System.err.println("[SERVER][PLAYERMESSAGE] received message request from " + this.channel.getPlayer() + " for " + var7 + " messages");
            this.requCount = var7;
        }

    }

    public void updateToNetworkObject() {
    }

    public void clientDelete(PlayerMessage var1) {
        assert !this.channel.isOnServer();

        var1.setDeleted(true);
        this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var1, this.channel.getNetworkObject()));
    }

    public boolean hasUnreadMessages() {
        return this.unreadMessages;
    }

    public ClientChannel getChannel() {
        return this.channel;
    }

    public void deleteAllMessagesClient() {
        Iterator var1 = this.messagesReceived.iterator();

        while(var1.hasNext()) {
            PlayerMessage var2;
            (var2 = (PlayerMessage)var1.next()).setDeleted(true);
            this.channel.getNetworkObject().playerMessageBuffer.add(new RemotePlayerMessage(var2, this.channel.getNetworkObject()));
        }

    }
}
