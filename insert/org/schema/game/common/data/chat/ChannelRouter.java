//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.chat;

import api.entity.Player;
import api.listener.events.player.PlayerChatEvent;
import api.listener.events.player.PlayerCommandEvent;
import api.main.GameServer;
import api.mod.StarLoader;
import api.server.Server;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.schema.common.util.StringTools;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.chat.prefixprocessors.AbstractPrefixProcessor;
import org.schema.game.common.data.chat.prefixprocessors.PrefixProcessorAdminCommand;
import org.schema.game.common.data.chat.prefixprocessors.PrefixProcessorEngineSettings;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.player.SimplePlayerCommands;
import org.schema.game.network.objects.ChatChannelModification;
import org.schema.game.network.objects.ChatMessage;
import org.schema.game.network.objects.ChatChannelModification.ChannelModType;
import org.schema.game.network.objects.ChatMessage.ChatMessageType;
import org.schema.game.network.objects.remote.RemoteChatChannel;
import org.schema.game.network.objects.remote.RemoteChatMessage;
import org.schema.game.network.objects.remote.RemoteChatMessageBuffer;
import org.schema.game.server.data.FactionState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.ChatListener;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.gui.ColoredTimedText;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.server.ServerStateInterface;
import org.schema.schine.resource.DiskWritable;
import org.schema.schine.resource.FileExt;
import org.schema.schine.resource.tag.FinishTag;
import org.schema.schine.resource.tag.Tag;
import org.schema.schine.resource.tag.Tag.Type;

public class ChannelRouter implements DiskWritable {
    public static final String LOG_DIR;
    public static final String FILENAME = "chatchannels.tag";
    public static final boolean VISIBLE_CHAT_INSERT_AT_ZERO = true;
    private static final boolean ALLOW_ADMINS_AS_MODS = true;
    public static int idGen;
    public static SimpleDateFormat logDateFormatter;
    public static String CHAT_LOG_DIR;
    public final ObjectArrayFIFOQueue<ChatMessage> receivedChat = new ObjectArrayFIFOQueue();
    private final boolean onServer;
    private final StateInterface state;
    private final List<ChatMessage> defaultVisibleChatLog = new ObjectArrayList();
    private final List<ChatMessage> defaultChatLog = new ObjectArrayList();
    private final List<AbstractPrefixProcessor> prefixProcessors = new ObjectArrayList();
    private Object2ObjectOpenHashMap<String, ChatChannel> channels = new Object2ObjectOpenHashMap();

    public ChannelRouter(StateInterface var1) {
        this.state = var1;
        this.onServer = var1 instanceof ServerStateInterface;
        if (this.isOnServer()) {
            this.createAndLoadChannels();
        } else {
            this.createPrefixProcessors(this.prefixProcessors);
        }

        (new FileExt(CHAT_LOG_DIR)).mkdirs();
    }

    public static void updateVisibleChat(Timer var0, List<? extends ColoredTimedText> var1) {
        for(int var2 = 0; var2 < var1.size(); ++var2) {
            ((ColoredTimedText)var1.get(var2)).update(var0);
            if (!((ColoredTimedText)var1.get(var2)).isAlive()) {
                var1.remove(var2);
                --var2;
            }
        }

    }

    public static boolean allowAdminClient(PlayerState var0) {
        return var0.getNetworkObject().isAdminClient.get();
    }

    private void createPrefixProcessors(List<AbstractPrefixProcessor> var1) {
        var1.add(new PrefixProcessorAdminCommand());
        var1.add(new PrefixProcessorEngineSettings());
        Collections.sort(var1);

        assert ((AbstractPrefixProcessor)var1.get(0)).getPrefixCommon().equals("/");

    }

    private void createAndLoadChannels() {
        AllChannel var1 = new AllChannel(this.state, ++idGen);
        this.channels.put(var1.getUniqueChannelName(), var1);
        FileExt var5 = new FileExt(GameServerState.ENTITY_DATABASE_PATH + "chatchannels.tag");

        try {
            System.err.println(this.state + " Reading CHAT CHANNEL ROUTER " + var5.getAbsolutePath());

            try {
                this.fromTagStructure(Tag.readFrom(new BufferedInputStream(new FileInputStream(var5)), true, false));
            } catch (NullPointerException var2) {
                var2.printStackTrace();
            }
        } catch (FileNotFoundException var3) {
            System.err.println("[SERVER] Cant load chat channels. no saved data found");
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    public ChatChannel createClientPMChannel(PlayerState var1, PlayerState var2) {
        String var3 = DirectChatChannel.getChannelName(var1, var2);
        ChatChannel var4;
        if ((var4 = (ChatChannel)this.channels.get(var3)) == null) {
            System.err.println("CREATING NEW CLIENT CHAT CHANNEL: " + var3);
            var4 = new DirectChatChannel(this.state, -(++idGen), var1, var2);
            this.channels.put(var3, var4);
            var1.getPlayerChannelManager().addDirectChannel(var4);
            var2.getPlayerChannelManager().addDirectChannel(var4);
        }

        return var4;
    }

    public void receive(ChatMessage message) {
        System.err.println("[CHANNELROUTER] RECEIVED MESSAGE ON " + this.state + ": " + message.toDetailString());
        //INSERTED CODE @149
        if(message.text.startsWith("!") && GameServer.getServerState() != null && this.isOnServer()){
            try {
                PlayerState playerFromName = GameServer.getServerState().getPlayerFromName(message.sender);
                PlayerCommandEvent event = new PlayerCommandEvent(message.text.split(" ")[0].replaceAll("!", ""), new Player(playerFromName), message.text.split(" "));
                StarLoader.fireEvent(PlayerCommandEvent.class, event);
            } catch (PlayerNotFountException e) {
                e.printStackTrace();
            }

        }else {
            PlayerChatEvent event = new PlayerChatEvent(message, this);
            StarLoader.fireEvent(PlayerChatEvent.class, event);
            if (event.isCanceled()) {
                return;
            }
        }
        ///

        PlayerState var2;
        if (this.isOnServer()) {
            if ((var2 = ((GameServerState)this.state).getPlayerFromNameIgnoreCaseWOException(message.sender)) != null) {
                var2.getRuleEntityManager().triggerPlayerChat();
            }

            this.log(message);
            this.sendAsServer(message);
        } else {
            if (!this.isOnServer() && this.state.isPassive()) {
                Iterator var3 = ((GameClientState)this.state).getChatListeners().iterator();

                while(var3.hasNext()) {
                    ((ChatListener)var3.next()).notifyOfChat(message);
                }
            }

            var2 = (PlayerState)((GameClientState)this.state).getOnlinePlayersLowerCaseMap().get(message.sender.toLowerCase(Locale.ENGLISH));
            if (!((GameClientState)this.state).getPlayer().isIgnored(message.sender) || var2.getNetworkObject().isAdminClient.get()) {
                if (message.receiverType == ChatMessageType.DIRECT) {
                    String var7 = DirectChatChannel.getChannelName(message.sender, message.receiver);
                    ChatChannel var4;
                    if ((var4 = (ChatChannel)this.channels.get(var7)) == null) {
                        PlayerState var5 = (PlayerState)((GameClientState)this.state).getOnlinePlayersLowerCaseMap().get(message.receiver.toLowerCase(Locale.ENGLISH));
                        if (var2 == null || var5 == null) {
                            ((GameClientState)this.state).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_CHAT_CHANNELROUTER_1, new Object[]{message.sender, message.receiver}), 0.0F);
                            return;
                        }

                        System.err.println("CLIENT RECEIVED MESSAGE ON NOT EXISTING CLIENT CHANNEL, CREATING: " + var7 + "; Current: " + this.channels);
                        var4 = new DirectChatChannel(this.state, -(++idGen), var2, var5);
                        var2.getPlayerChannelManager().addDirectChannel(var4);
                        var5.getPlayerChannelManager().addDirectChannel(var4);
                        this.channels.put(var7, var4);

                        assert this.channels.size() > 0;
                    }

                    var4.receive(message);
                    return;
                }

                ChatChannel var6;
                if ((var6 = (ChatChannel)this.channels.get(message.receiver)) != null) {
                    var6.receive(message);
                    return;
                }

                ((GameClientState)this.state).getController().popupAlertTextMessage(StringTools.format(Lng.ORG_SCHEMA_GAME_COMMON_DATA_CHAT_CHANNELROUTER_2, new Object[]{message.sender, message.receiver}), 0.0F);
            }

        }
    }

    private void log(ChatMessage var1) {
        String var5;
        PrintWriter var6;
        if (var1.receiverType == ChatMessageType.DIRECT) {
            try {
                var5 = DirectChatChannel.getChannelName(var1.sender, var1.receiver);
                (var6 = new PrintWriter(new BufferedWriter(new FileWriter(CHAT_LOG_DIR + var5 + ".txt", true)))).println(logDateFormatter.format(new Date(System.currentTimeMillis())) + " [" + var1.sender + " -> " + var1.receiver + "]: " + var1.text);
                var6.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        } else {
            ChatChannel var2;
            if ((var2 = (ChatChannel)this.channels.get(var1.receiver)) != null) {
                try {
                    var5 = var2.getUniqueChannelName();
                    (var6 = new PrintWriter(new BufferedWriter(new FileWriter(CHAT_LOG_DIR + var5 + ".txt", true)))).println(logDateFormatter.format(new Date(System.currentTimeMillis())) + " [" + var1.sender + "]: " + var1.text);
                    var6.close();
                    return;
                } catch (IOException var4) {
                    var4.printStackTrace();
                }
            }

        }
    }

    public void sendAsServer(ChatMessage var1) {
        assert this.isOnServer();

        PlayerState var3;
        if (var1.receiverType == ChatMessageType.DIRECT) {
            System.err.println("[SERVER][CHANNELROUTER] DIRECT MESSAGE RELAY: " + var1.toDetailString());

            try {
                PlayerState var7 = ((GameServerState)this.state).getPlayerFromName(var1.receiver);
                var3 = ((GameServerState)this.state).getPlayerFromName(var1.sender);
                boolean var8 = this.getAllChannel().isMuted(var3);
                if (var7.isIgnored(var1.sender) && var8) {
                    if (var8) {
                        var3.sendServerMessagePlayerError(new Object[]{182});
                        return;
                    }

                    var3.sendServerMessagePlayerError(new Object[]{183});
                } else {
                    if (var7.getClientChannel() != null) {
                        var7.getClientChannel().getNetworkObject().chatBuffer.add(new RemoteChatMessage(var1, this.isOnServer()));
                    }

                    if (var3.getClientChannel() != null) {
                        var3.getClientChannel().getNetworkObject().chatBuffer.add(new RemoteChatMessage(var1, this.isOnServer()));
                        return;
                    }
                }

            } catch (PlayerNotFountException var6) {
                var6.printStackTrace();
            }
        } else {
            ChatChannel var2 = (ChatChannel)this.channels.get(var1.receiver);

            assert var2 != null : "Channel not found: " + var1.receiver;

            try {
                var3 = ((GameServerState)this.state).getPlayerFromName(var1.sender);
                if (var2 != null && !var2.isMuted(var3)) {
                    ChatMessage var4 = var2.process(var1);
                    var2.send(var4);
                } else if (var2 != null) {
                    var3.sendServerMessagePlayerError(new Object[]{184});
                } else {
                    System.err.println("[SERVER] Exception: channel " + var1.receiver + " not found");
                }
            } catch (PlayerNotFountException var5) {
                var5.printStackTrace();
            }
        }
    }

    public void update(Timer var1) {
        if (this.isOnServer()) {
            ObjectIterator var2 = this.channels.values().iterator();

            while(var2.hasNext()) {
                ChatChannel var3;
                (var3 = (ChatChannel)var2.next()).update();
                if (!var3.isAlive()) {
                    System.err.println("[SERVER][ChannelRouter] CHANNEL NO LONGER ALIVE: " + var3.getUniqueChannelName());
                    var2.remove();
                    this.sendChannelRemovalOnServer(var3);
                }
            }
        }

        while(!this.receivedChat.isEmpty()) {
            this.receive((ChatMessage)this.receivedChat.dequeue());
        }

        if (!this.isOnServer()) {
            updateVisibleChat(var1, this.defaultVisibleChatLog);
        }

    }

    private void sendChannelRemovalOnServer(ChatChannel var1) {
        Iterator var2 = ((GameServerState)this.state).getPlayerStatesByName().values().iterator();

        while(var2.hasNext()) {
            PlayerState var3 = (PlayerState)var2.next();
            System.err.println("[SERVER] sending channel removed flag of " + var1.getUniqueChannelName() + " to " + var3.getName());
            if (var3.getClientChannel() != null) {
                var3.getClientChannel().getNetworkObject().chatChannelBuffer.add(new RemoteChatChannel(new ChatChannelModification(ChannelModType.REMOVED_ON_NOT_ALIVE, var1, new int[0]), this.isOnServer()));
            }
        }

    }

    public void onFactionChangedServer(ClientChannel var1) {
        if (this.isOnServer()) {
            System.err.println("[SERVER][ChannelRouter] Faction Changed by " + var1.getPlayer() + " to " + var1.getPlayer().getFactionId());
            this.getOrCreateFactionChannel(var1.getPlayer().getFactionId(), var1);
            ObjectIterator var2 = this.channels.values().iterator();

            while(var2.hasNext()) {
                ((ChatChannel)var2.next()).onFactionChangedServer(var1);
            }
        }

    }

    public void onLogin(ClientChannel var1) {
        if (this.isOnServer()) {
            this.addDefaultChannels(var1, var1.getPlayer().getPlayerChannelManager().getAvailableChannels());
            ObjectIterator var2 = this.channels.values().iterator();
            System.err.println("[SERVER] Sending all available channels to " + var1.getPlayer() + "; total existing: " + this.channels.size());

            while(var2.hasNext()) {
                ChatChannel var3;
                (var3 = (ChatChannel)var2.next()).onLoginServer(var1);

                assert !(var3 instanceof FactionChannel) || var3.getClientChannelsInChannel().size() > 0;

                if (var1.getPlayer().getPlayerChannelManager().getAvailableChannels().contains(var3)) {
                    System.err.println("[SERVER] Sending available channel to " + var1.getPlayer() + ": " + var3.getUniqueChannelName());
                    if (var3.isAutoJoinFor(var1)) {
                        var3.sendCreateUpdateToClient(var1);
                    } else {
                        var3.sendCreateWithoutJoinUpdateToClient(var1);
                    }
                }
            }
        }

    }

    public void onLogoff(PlayerState var1) {
        System.err.println("[SERVER][ChannelRouter] log off of player " + var1 + "; removing from all channels");
        ObjectIterator var2 = this.channels.values().iterator();

        while(var2.hasNext()) {
            ((ChatChannel)var2.next()).onLogoff(var1);
        }

    }

    public void addDefaultChannels(ClientChannel var1, ObjectOpenHashSet<ChatChannel> var2) {
        assert this.isOnServer();

        ObjectIterator var3 = this.channels.values().iterator();

        while(var3.hasNext()) {
            ChatChannel var4;
            if ((var4 = (ChatChannel)var3.next()).isPublic()) {
                var2.add(var4);
            }
        }

        FactionChannel var5;
        if ((var5 = this.getOrCreateFactionChannel(var1.getPlayer().getFactionId(), var1)) != null) {
            var2.add(var5);
        }

    }

    public FactionChannel getOrCreateFactionChannel(int var1, ClientChannel var2) {
        assert this.isOnServer();

        if (((FactionState)this.state).getFactionManager().existsFaction(var1)) {
            String var4 = "Faction" + var1;
            Object var3;
            if ((var3 = (ChatChannel)this.channels.get(var4)) == null) {
                var3 = new FactionChannel(this.state, ++idGen, var1);
                System.err.println("[SERVER][ChannelRouter] created Faction channel: " + ((ChatChannel)var3).getUniqueChannelName());
                this.channels.put(var4, (ChatChannel) var3);
            }

            return (FactionChannel)var3;
        } else {
            if (var1 != 0) {
                System.err.println("[SERVER][ChannelRouter] ERROR: cannot create faction channel because faction doesnt exist: " + var1);

                assert false : "ERROR: cannot create faction channel because faction doesnt exist: " + var1;
            }

            return null;
        }
    }

    public ChatChannel handleClientReceivedChannel(PlayerState var1, ChatChannelModification var2) {
        if (var2.type != ChannelModType.REMOVED_ON_NOT_ALIVE && var2.type != ChannelModType.DELETE) {
            ChatChannel var3;
            if ((var3 = (ChatChannel)this.channels.get(var2.channel)) == null) {
                if (var2.type == ChannelModType.CREATE) {
                    System.err.println("[CLIENT] Client received new channel: " + var2.channel + "; creating...");
                    var3 = var2.createNewChannel(this.state);

                    assert var3 != null : "NO CHANNEL: " + var2.channel + ", " + var2.type;

                    this.channels.put(var3.getUniqueChannelName(), var3);
                }
            } else {
                System.err.println("[CLIENT] Client received existing channel mod: " + var2.channel + "; " + var2.type.name());
            }

            if (var3 != null) {
                var3.handleMod(var2);
            }

            return var3;
        } else {
            return (ChatChannel)this.channels.remove(var2.channel);
        }
    }

    public void createNewChannelOnClient(PlayerState var1, String var2, String var3, boolean var4) {
        PublicChannel var5 = new PublicChannel(this.state, ++idGen, var2, var3, var4, new String[]{var1.getName().toLowerCase(Locale.ENGLISH)});
        ChatChannelModification var6 = new ChatChannelModification(ChannelModType.CREATE, var5, new int[]{var1.getId()});
        var1.getClientChannel().getNetworkObject().chatChannelBuffer.add(new RemoteChatChannel(var6, this.isOnServer()));
    }

    public void makeNewCreatedAllAvailableServer(ChatChannel var1) {
        Iterator var2 = ((GameServerState)this.state).getPlayerStatesByName().values().iterator();

        while(var2.hasNext()) {
            PlayerState var3;
            (var3 = (PlayerState)var2.next()).getPlayerChannelManager().addedToChat(var1);
            if (var3.getClientChannel() != null) {
                var1.sendCreateUpdateToClient(var3.getClientChannel());
            }
        }

    }

    public void handleReceivedChannelOnServer(PlayerState var1, ChatChannelModification var2) {
        assert this.isOnServer();

        if (var2.type == ChannelModType.CREATE) {
            PublicChannel var4;
            (var4 = new PublicChannel(this.state, ++idGen, var2.channel, var2.createPublicChannelPassword, var2.createPublicChannelAsPermanent, var2.mods)).addToModerator(new String[]{var1.getName().toLowerCase(Locale.ENGLISH)});
            if (!this.channels.containsKey(var4)) {
                this.channels.put(var4.getUniqueChannelName(), var4);
                if (var4.isPublic()) {
                    this.makeNewCreatedAllAvailableServer(var4);
                }

                var4.joinOnServerAndSend(var1.getClientChannel());
            }

        } else {
            ChatChannel var3;
            if ((var3 = (ChatChannel)this.channels.get(var2.channel)) != null) {
                if (var2.type != ChannelModType.DELETE) {
                    if (var2.type == ChannelModType.JOINED) {
                        if (!var3.isBanned(var1)) {
                            if (var3.hasPassword() && !var3.checkPassword(var2) && !((GameServerState)this.state).isAdmin(var1.getName())) {
                                var1.sendSimpleCommand(SimplePlayerCommands.FAILED_TO_JOIN_CHAT_INVALLID_PASSWD, new Object[]{var3.getUniqueChannelName()});
                                var1.sendServerMessagePlayerError(new Object[]{185, var3.getName()});
                                return;
                            }

                            System.err.println("[SERVER] JOINING ON SERVER: hasPasswd: " + var3.hasPassword() + "; Check " + var3.checkPassword(var2) + "; pw provided: " + var2.joinPw);
                            var3.joinOnServerAndSend(var1.getClientChannel());
                            return;
                        }

                        var1.sendServerMessagePlayerError(new Object[]{186, var3.getName()});
                        return;
                    }

                    if (var2.type == ChannelModType.LEFT) {
                        var3.leaveOnServerAndSend(var1.getClientChannel());
                        return;
                    }

                    var3.handleMod(var2);
                    return;
                }

                if (((GameServerState)this.state).isAdmin(var1.getName())) {
                    var3.deleteChannelOnServerAndSend();
                    this.channels.remove(var2.channel);
                    System.err.println("[SERVER] Channel deleted by admin: " + var3.getUniqueChannelName());
                    return;
                }
            } else {
                System.err.println("[SERVER] Error: chat channel to modify does not exist: " + var2.channel);
            }

        }
    }

    public boolean isOnServer() {
        return this.onServer;
    }

    public void addToDefaultChannelVisibleChatLogOnClient(ChatMessage var1) {
        assert !this.onServer;

        ChatMessage var2 = new ChatMessage(var1);
        ChatChannel var3;
        if (var1.receiverType == ChatMessageType.CHANNEL) {
            var3 = (ChatChannel)this.channels.get(var1.receiver);
        } else {
            String var4 = DirectChatChannel.getChannelName(var1.sender, var1.receiver);
            var3 = (ChatChannel)this.channels.get(var4);
        }

        var2.setChannel(var3);
        this.defaultVisibleChatLog.add(0, var2);
    }

    public void addToDefaultChannelChatLogOnClient(ChatMessage var1) {
        assert !this.onServer;

        ChatChannel var2;
        if (var1.receiverType == ChatMessageType.CHANNEL) {
            var2 = (ChatChannel)this.channels.get(var1.receiver);
        } else {
            String var3 = DirectChatChannel.getChannelName(var1.sender, var1.receiver);
            var2 = (ChatChannel)this.channels.get(var3);
        }

        var1.setChannel(var2);
        this.defaultChatLog.add(var1);
    }

    public List<ChatMessage> getDefaultVisibleChatLog() {
        return this.defaultVisibleChatLog;
    }

    public List<ChatMessage> getDefaultChatLog() {
        return this.defaultChatLog;
    }

    public AllChannel getAllChannel() {
        ChatChannel var1 = (ChatChannel)this.channels.get("all");

        assert var1 != null : this.channels;

        return (AllChannel)var1;
    }

    public boolean checkPrefixClient(ChatMessage var1, ChatChannel var2) {
        for(int var3 = 0; var3 < this.prefixProcessors.size(); ++var3) {
            AbstractPrefixProcessor var4;
            if ((var4 = (AbstractPrefixProcessor)this.prefixProcessors.get(var3)).fits(var1)) {
                var4.process(var1, var2, (GameClientState)this.state);
                return var4.sendChatMessageAfterProcessing();
            }
        }

        return true;
    }

    public String getUniqueIdentifier() {
        return null;
    }

    public boolean isVolatile() {
        return false;
    }

    public void fromTagStructure(Tag var1) {
        Tag[] var4 = (Tag[])var1.getValue();
        ObjectArrayList var2 = new ObjectArrayList();
        //(Byte)var4[0].getValue();
        var4 = (Tag[])var4[1].getValue();

        int var3;
        for(var3 = 0; var3 < var4.length - 1; ++var3) {
            var2.add(ChatChannel.loadServerChannel((GameServerState)this.state, var4[var3]));
        }

        for(var3 = 0; var3 < var2.size(); ++var3) {
            this.channels.put(((PublicChannel)var2.get(var3)).getUniqueChannelName(), (ChatChannel) var2.get(var3));
        }

    }

    public Tag toTagStructure() {
        ObjectArrayList var1 = new ObjectArrayList();
        Iterator var2 = this.channels.values().iterator();

        while(var2.hasNext()) {
            ChatChannel var3;
            if ((var3 = (ChatChannel)var2.next()) instanceof PublicChannel && ((PublicChannel)var3).isPermanent()) {
                var1.add((PublicChannel)var3);
            }
        }

        Tag[] var4;
        Tag[] var10000 = var4 = new Tag[var1.size() + 1];
        var10000[var10000.length - 1] = FinishTag.INST;

        for(int var5 = 0; var5 < var1.size(); ++var5) {
            var4[var5] = ((PublicChannel)var1.get(var5)).toServerTag();
        }

        return new Tag(Type.STRUCT, (String)null, new Tag[]{new Tag(Type.BYTE, "VERSION", (byte)0), new Tag(Type.STRUCT, "CHANNELS", var4), FinishTag.INST});
    }

    public ChatChannel getChannel(String var1) {
        return (ChatChannel)this.channels.get(var1);
    }

    static {
        LOG_DIR = "." + File.separator + "chatlogs" + File.separator;
        logDateFormatter = StringTools.getSimpleDateFormat(Lng.ORG_SCHEMA_GAME_COMMON_DATA_CHAT_CHANNELROUTER_0, "yyyy/MM/dd - HH:mm:ss");
        CHAT_LOG_DIR = "." + File.separator + "chatlogs" + File.separator;
    }

    public static enum ChannelType {
        ALL,
        FACTION,
        PUBLIC,
        PARTY,
        DIRECT;

        private ChannelType() {
        }
    }
}
