//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.network.server;

import api.DebugFile;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.schema.common.LogUtil;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.network.Command;
import org.schema.schine.network.DataInputStreamPositional;
import org.schema.schine.network.DataOutputStreamPositional;
import org.schema.schine.network.Header;
import org.schema.schine.network.NetUtil;
import org.schema.schine.network.NetworkProcessor;
import org.schema.schine.network.Pinger;
import org.schema.schine.network.RegisteredClientOnServer;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.exception.NetworkObjectNotFoundException;
import org.schema.schine.network.objects.NetworkObject;

public class ServerProcessor extends Pinger implements Runnable, NetworkProcessor {
    private static final int heartbeatTimeOut = 10000;
    private static final int MAX_PING_RETRYS = 300;
    private static final int MAX_PACKET_POOL_SIZE = 1000;
    private static final ObjectArrayFIFOQueue<FastByteArrayOutputStream> packetPool = new ObjectArrayFIFOQueue();
    public static int DELAY;
    public static int PING_THREADS_RUNNING;
    public static int SENDING_THREADS_RUNNING;
    public static int PROCESSOR_THREADS_RUNNING;
    public static int idGen;
    public static int connections;
    public static int MAX_PACKET_SIZE;
    public static int totalPackagesQueued;
    private final ObjectArrayFIFOQueue<ServerProcessor.Packet> sendingQueue = new ObjectArrayFIFOQueue();
    private final ObjectArrayList<NetworkObject> lastReceived = new ObjectArrayList();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    public int id;
    public long p = 0L;
    Socket commandSocket;
    Boolean waitingForPong = false;
    byte[] receiveBuffer;
    int lastCheck;
    int lastHeader;
    int lastCommand;
    private boolean aliveLocal;
    private ServerStateInterface state;
    private RegisteredClientOnServer client;
    private long heartBeatTimeStamp;
    private long heartBeatTimeStampTimeoutHelper;
    private Object lock;
    private long connectionStartTime;
    private boolean pong;
    private boolean connected;
    private int pingRetrys;
    private long pingTime;
    private DataInputStream dataInputStream;
    private FastByteArrayOutputStream byteArrayOutDoubleBuffer;
    private DataOutputStreamPositional outDoubleBuffer;
    private DataInputStreamPositional inDoubleBuffer;
    private boolean ping;
    private Header tmpHeader;
    private Thread thread;
    private ServerProcessor.ServerPing serverPing;
    private Thread serverPingThread;
    private boolean stopTransmit;
    private ServerProcessor.SendingQueueThread sendingQueueThread;
    private boolean disconnectAfterSent;
    private boolean infoPinger;
    private Object[] lastParameters;
    private long lastSetSnapshop;

    public ServerProcessor(Socket var1, ServerStateInterface var2) throws SocketException {
        this.receiveBuffer = new byte[MAX_PACKET_SIZE];
        this.lastCheck = -12345678;
        this.lastHeader = -12345678;
        this.lastCommand = -12345678;
        this.aliveLocal = true;
        this.lock = new Object();
        this.connected = false;
        this.pingRetrys = 300;
        this.id = idGen++;
        this.commandSocket = var1;
        this.state = var2;
        this.heartBeatTimeStamp = System.currentTimeMillis();
        this.heartBeatTimeStampTimeoutHelper = System.currentTimeMillis();
        this.connectionStartTime = System.currentTimeMillis();
        this.outDoubleBuffer = new DataOutputStreamPositional(this.byteArrayOutDoubleBuffer = new FastByteArrayOutputStream(102400));
    }

    public static FastByteArrayOutputStream getNewPacket(int var0) {
        synchronized(packetPool) {
            if (!packetPool.isEmpty()) {
                return (FastByteArrayOutputStream)packetPool.dequeue();
            }
        }

        return new FastByteArrayOutputStream(MAX_PACKET_SIZE);
    }

    public static void releasePacket(FastByteArrayOutputStream var0) {
        var0.reset();
        synchronized(packetPool) {
            if (packetPool.size() < 1000) {
                packetPool.enqueue(var0);
            }

        }
    }

    private boolean checkPing(InputStream var1) throws IOException {
        byte var2;
        if ((var2 = (byte)var1.read()) == -1) {
            this.serverPing.sendPong();
            return true;
        } else if (var2 == -2) {
            this.setPingTime(System.currentTimeMillis() - this.heartBeatTimeStamp);
            this.waitingForPong = false;
            if (this.pingRetrys != 300) {
                System.err.println("[SERVER][WARNING] Recovered Ping for " + this.client + "; Retries left: " + this.pingRetrys + "; retries resetting");
            }

            this.pingRetrys = 300;
            this.setChanged();
            this.notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public void closeSocket() throws IOException {
        this.commandSocket.close();
    }

    public void flushDoubleOutBuffer() throws IOException {
        assert this.byteArrayOutDoubleBuffer.position() > 0L;

        int var1;
        FastByteArrayOutputStream var2;
        (var2 = getNewPacket(var1 = (int)this.byteArrayOutDoubleBuffer.position())).write(this.byteArrayOutDoubleBuffer.array, 0, var1);
        this.resetDoubleOutBuffer();
        synchronized(this.sendingQueue) {
            if (this.connected) {
                this.sendingQueue.enqueue(new ServerProcessor.Packet(var2));
                ++totalPackagesQueued;
                this.sendingQueue.notify();
            }

        }
    }

    public int getCurrentSize() {
        return (int)this.byteArrayOutDoubleBuffer.position();
    }

    public DataInputStreamPositional getIn() throws IOException {
        return this.inDoubleBuffer;
    }

    public InputStream getInRaw() throws IOException {
        return this.commandSocket.getInputStream();
    }

    public DataOutputStreamPositional getOut() {
        return this.outDoubleBuffer;
    }

    public OutputStream getOutRaw() throws IOException {
        return this.commandSocket.getOutputStream();
    }

    public StateInterface getState() {
        return this.state;
    }

    public boolean isAlive() {
        return this.aliveLocal;
    }

    public void notifyPacketReceived(short var1, Command var2) {
    }

    public void resetDoubleOutBuffer() throws IOException {
        this.byteArrayOutDoubleBuffer.reset();
    }

    public void updatePing() throws IOException {
        throw new IllegalStateException("METHOD NOT AVAILABLE");
    }

    public ReentrantReadWriteLock getBufferLock() {
        return this.rwl;
    }

    public ObjectArrayList<NetworkObject> getLastReceived() {
        return this.lastReceived;
    }

    public void disconnect() {
        this.connected = false;
        this.setStopTransmit(true);
        synchronized(this.sendingQueue) {
            this.sendingQueue.notify();
        }

        try {
            this.closeSocket();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        this.deleteObservers();
    }

    public void disconnectAfterSent() {
        this.disconnectAfterSent = true;
    }

    public void disconnectDelayed() {
        (new Thread() {
            public void run() {
                try {
                    ServerProcessor.this.setStopTransmit(true);
                    synchronized(ServerProcessor.this.sendingQueue) {
                        ServerProcessor.this.sendingQueue.notify();
                    }

                    Thread.sleep(3000L);
                } catch (InterruptedException var4) {
                    Object var1 = null;
                    var4.printStackTrace();
                }

                ServerProcessor.this.disconnect();
            }
        }).start();
    }

    public boolean needsFlush() {
        return this.byteArrayOutDoubleBuffer.position() > 0L;
    }

    public RegisteredClientOnServer getClient() {
        return this.client;
    }

    public void setClient(RegisteredClientOnServer var1) {
        this.client = var1;
    }

    public InetAddress getClientIp() {
        return this.commandSocket.getInetAddress();
    }

    public long getConnectionTime() {
        return System.currentTimeMillis() - this.connectionStartTime;
    }

    public String getIp() {
        return this.commandSocket.isConnected() ? this.commandSocket.getRemoteSocketAddress().toString() : "n/a";
    }

    public long getPingTime() {
        return this.pingTime;
    }

    public void setPingTime(long var1) {
        this.pingTime = var1;
    }

    public int getPort() {
        return this.commandSocket.isConnected() ? this.commandSocket.getLocalPort() : -1;
    }

    public boolean isConnectionAlive() {
        return this.commandSocket != null && this.commandSocket.isConnected();
    }

    public boolean isInfoPinger() {
        return this.infoPinger;
    }

    public void setInfoPinger(boolean var1) {
        this.infoPinger = var1;
    }

    public void run() {
        try {
            ++PROCESSOR_THREADS_RUNNING;
            ++connections;

            while(!this.commandSocket.isConnected()) {
                try {
                    System.err.println("[SERVER] waiting for socket to connect: " + this.commandSocket);
                    Thread.sleep(100L);
                } catch (InterruptedException var37) {
                    var37.printStackTrace();
                }
            }

            boolean var1 = false;
            byte var2 = 0;

            try {
                int var43;
                try {
                    this.connected = true;
                    this.setup();
                    System.currentTimeMillis();
                    System.out.println("[SERVER][PROCESSOR] client setup completed for PID: " + this.id + ". listening for input");

                    while(this.connected) {
                        this.thread.setName("SERVER-PROCESSOR: " + this.client + "; PID: " + this.id);
                        this.serverPingThread.setName("ServerPing " + this.client);
                        if ((var43 = this.dataInputStream.readInt()) > this.receiveBuffer.length) {
                            System.err.println("[SERVER] Exception: Unusual big update from client " + this.getClient() + ": growing receive buffer: " + var43);
                            if (var43 > 104857600) {
                                throw new IllegalArgumentException("Killing client: Tried to send more then 100MB at once");
                            }

                            this.receiveBuffer = new byte[var43];
                        }
                        //INSERTED CODE
                        DebugFile.log("RECV PACKET OF SIZE: " + var43);
                        if(var43 == -2){
                            //SPECIAL PACKET ID received (in this case its size)
                            String packetId = this.dataInputStream.readUTF();
                            //Construct packet
                            api.network.Packet packet = api.network.Packet.newPacket(packetId);
                            //Fill with data
                            try {
                                packet.readPacketData(dataInputStream);
                            }catch (IOException e){
                                e.printStackTrace();
                                DebugFile.logError(e, null);
                            }
                            //dispatch TODO Move packet to a queue to be executed on the main loop
                            packet.processPacketOnServer(null);
                            continue;
                        }
                        ///

                        assert var43 > 0 : " Empty update! " + var43;

                        try {
                            this.dataInputStream.readFully(this.receiveBuffer, 0, var43);
                        } catch (Exception var36) {
                            System.err.println("Exception happened with size " + var43);
                            throw var36;
                        }

                        FastByteArrayInputStream var3 = new FastByteArrayInputStream(this.receiveBuffer, 0, var43);
                        this.inDoubleBuffer = new DataInputStreamPositional(var3);
                        byte var44 = this.inDoubleBuffer.readByte();
                        this.lastCheck = var44;
                        if (var44 >= 0) {
                            if (var44 != 42 && var44 != 23 && var44 != 100 && var44 != 65) {
                                this.connected = false;
                                throw new IOException("SERVER CHECK FAILED: " + var44 + " for client " + this.getClient() + ": Received: " + var43 + ": " + Arrays.toString(Arrays.copyOf(this.receiveBuffer, var43)) + ": available: " + var3.available());
                            }

                            if (var44 == 100) {
                                this.connected = false;
                                var1 = true;
                                System.err.println("[SERVER] Probe was made CODE (#100). Closing connection!");
                                this.sendingQueueThread.sendTestByte();
                            } else if (var44 == 65) {
                                System.err.println("[SERVER][LOGOUT]#### soft client logout received. Logging out client: " + this.client);
                                this.connected = false;
                            } else {
                                long var5 = System.currentTimeMillis();
                                synchronized(this.state) {
                                    this.state.setSynched();
                                    if (var44 == 23) {
                                        boolean var45 = this.checkPing(this.getIn());

                                        assert var45;
                                    } else if (var44 == 42) {
                                        this.parseNextPacket(this.getIn());
                                    }

                                    this.state.addNTReceivedStatistics(this.client, var43, this.lastCheck, this.lastHeader, this.lastCommand, this.lastParameters, this.lastReceived);
                                    if (System.currentTimeMillis() - this.lastSetSnapshop > 1000L) {
                                        this.state.getDataStatsManager().snapshotDownload(this.state.getReceivedData());
                                        this.lastSetSnapshop = System.currentTimeMillis();
                                    }

                                    this.state.setUnsynched();
                                }

                                System.currentTimeMillis();
                                if (var5 < 20L) {
                                    System.err.println("[SERVER] PACKET WARNING! Client " + this + " processing receive packet took: " + var5 + "ms. Size of packet: " + var43);
                                }

                                if (this.inDoubleBuffer.available() > 0) {
                                    throw new IOException("MORE BYTES AVAILABLE: " + this.inDoubleBuffer.available() + "; total size" + var43);
                                }
                            }
                        }
                    }
                } catch (Exception var40) {
                    if (!this.disconnectAfterSent) {
                        System.err.println("[SERVER] NTException on: " + this.client + "; ProcessorID: " + this.id);
                        System.err.println("[SERVER] last received size: " + var2);
                        System.err.println("[SERVER] last received check: " + this.lastCheck);
                        System.err.println("[SERVER] last received header: " + this.lastHeader + ": " + (this.lastHeader == 111 ? "Param" : "Stream"));
                        if (this.lastCommand != -12345678) {
                            try {
                                System.err.println("[SERVER] last received command: " + NetUtil.commands.getById((byte)this.lastCommand));
                            } catch (RuntimeException var35) {
                                var35.printStackTrace();
                                System.err.println("[SERVER] last command sent was invalid and not part of the protocol " + this.lastCommand);
                            }
                        }

                        for(var43 = 0; var43 < this.lastReceived.size(); ++var43) {
                            NetworkObject var4;
                            if ((var4 = (NetworkObject)this.lastReceived.get(var43)) != null) {
                                System.err.println("[SERVER][LASTRECEIVED] decoded class #" + var43 + ": " + var4.getClass().getSimpleName() + "; decoded: " + var4.lastDecoded);
                            } else {
                                System.err.println("[SERVER][LASTRECEIVED] decoded class #" + var43 + ": debug NetworkObject is null");
                            }
                        }

                        var40.printStackTrace();
                    } else {
                        System.err.println("[SERVER] Executing scheduled disconnect!; ProcessorID: " + this.id);
                        var1 = true;
                    }

                    this.connected = false;
                }
            } finally {
                this.connected = false;
            }

            System.err.println("[SERVER][DISCONNECT] Client '" + (this.isInfoPinger() ? "Info-Pinger (server-lists)" : this.client) + "' HAS BEEN DISCONNECTED . PROBE: " + var1 + "; ProcessorID: " + this.id);
            LogUtil.log().fine("[DISCONNECT] Client '" + (this.isInfoPinger() ? "Info-Pinger (server-lists)" : this.client) + "' IP(" + this.getIp() + ") HAS BEEN DISCONNECTED . PROBE: " + var1 + "; ProcessorID: " + this.id);

            try {
                if (!var1) {
                    this.onError();
                }
            } catch (IOException var34) {
                var34.printStackTrace();
            } finally {
                this.deleteObservers();
                if (this.getClient() != null) {
                    System.err.println("[SERVER] UNREGISTERING CLIENT " + this.getClient());
                    this.state.getController().unregister(this.getClient().getId());
                    System.err.println("[SERVER] UNREGISTER DONE FOR CLIENT " + this.getClient());
                } else if (!this.isInfoPinger()) {
                    System.err.println("[SERVER] COULD NOT UNREGISTER CLIENT " + this.getClient());
                }

                if (this.getClient() != null && !this.state.filterJoinMessages()) {
                    ((ServerControllerInterface)this.getState().getController()).broadcastMessage(new Object[]{485, this.getClient().getPlayerName()}, 0);
                }

            }

            if (var1) {
                System.err.println("[SERVER] PROBE SUCCESSFULLY EXECUTED. STOPPING PROCESSOR. (Ping of a Starter to start server): PID " + this.id);
            } else {
                System.err.println("[SERVER] SERVER PROCESSOR STOPPED FOR " + this.client + "; PID " + this.id);
            }

            --PROCESSOR_THREADS_RUNNING;
        } finally {
            this.aliveLocal = false;
        }

    }

    public boolean isStopTransmit() {
        return this.stopTransmit;
    }

    public void setStopTransmit(boolean var1) {
        this.stopTransmit = var1;
    }

    private void onError() throws IOException {
        this.deleteObservers();
        if (this.getClient() != null) {
            this.state.getController().unregister(this.getClient().getId());
        } else if (!this.isInfoPinger()) {
            System.err.println("[SERVER] COULD NOT UNREGISTER CLIENT " + this.getClient());
        } else {
            System.err.println("[SERVER] Info pinger has diconencted " + this.getClient());
        }

        if (!this.commandSocket.isClosed()) {
            this.commandSocket.close();
        }

        --connections;
        if (this.getClient() != null) {
            System.err.println("[SERVER] Client <" + this.getClient().getId() + "> logged out from server. connections count: " + connections);
            if (!this.commandSocket.isClosed()) {
                System.err.println("[SERVER] ERROR: socket still open!");
            }

        }
    }

    private void parseNextPacket(DataInputStream var1) throws NetworkObjectNotFoundException, Exception {
        if (this.getClient() != null && !this.getClient().isConnected()) {
            this.connected = false;
            System.err.println("ERROR: client not connected!");
            this.onError();
        } else {
            this.tmpHeader.read(var1);
            this.lastHeader = this.tmpHeader.getType();
            this.lastCommand = this.tmpHeader.getCommandId();
            if (this.tmpHeader.getType() == 111) {
                Object[] var2 = NetUtil.commands.getById(this.tmpHeader.getCommandId()).readParameters(this.tmpHeader, var1);
                this.lastParameters = var2;
                NetUtil.commands.getById(this.tmpHeader.getCommandId()).serverProcess(this, var2, this.state, this.tmpHeader.packetId);
            } else if (this.tmpHeader.getType() == 123) {
                NetUtil.commands.getById(this.tmpHeader.getCommandId()).serverProcess(this, (Object[])null, this.state, this.tmpHeader.packetId);
            }

            var1.available();
        }
    }

    public void serverCommand(byte var1, int var2, Object... var3) throws IOException {
        System.err.println("SERVER COMMAND: " + Arrays.toString(var3));
        NetUtil.commands.getById(var1).writeAndCommitParametriziedCommand(var3, var2, this.getClient().getId(), (short)-32768, this.getClient().getProcessor());
        Command.sendingTime = System.currentTimeMillis();
    }

    public void setThread(Thread var1) {
        this.thread = var1;
    }

    private void setup() throws IOException {
        while(!this.commandSocket.isConnected() || !this.commandSocket.isBound() || this.commandSocket.isInputShutdown() || this.commandSocket.isOutputShutdown()) {
            System.err.println("Waiting for command socket! ");
        }

        this.dataInputStream = new DataInputStream(this.commandSocket.getInputStream());
        this.serverPing = new ServerProcessor.ServerPing();
        this.serverPingThread = new Thread(this.serverPing, "ServerPing");
        this.serverPingThread.setDaemon(true);
        this.serverPingThread.start();
        this.tmpHeader = new Header();
        this.sendingQueueThread = new ServerProcessor.SendingQueueThread(this.commandSocket.getOutputStream());
        this.sendingQueueThread.setDaemon(true);
        this.sendingQueueThread.start();
    }

    static {
        DELAY = (Integer)EngineSettings.N_ARTIFICIAL_DELAY.getCurrentState();
        MAX_PACKET_SIZE = 20480;
    }

    class Packet {
        private FastByteArrayOutputStream payload;
        private long time;

        public Packet(FastByteArrayOutputStream var2) {
            if (ServerProcessor.DELAY > 0) {
                this.time = System.currentTimeMillis();
            }

            this.payload = var2;
        }
    }

    class ServerPing implements Runnable {
        private static final long PING_WAIT = 1000L;
        private static final int NULL_TIMEOUT_IN_MS = 10000;
        private long lastPingSend;
        private long firstTry;

        private ServerPing() {
            this.firstTry = 0L;
        }

        public void execute() throws IOException {
            if (this.firstTry == 0L) {
                this.firstTry = System.currentTimeMillis();
            }

            if (ServerProcessor.this.getClient() != null) {
                if (!ServerProcessor.this.waitingForPong && System.currentTimeMillis() - this.lastPingSend > 1000L) {
                    this.sendPing(ServerProcessor.this.state.getId());
                    if (ServerProcessor.this.pingRetrys < 300) {
                        System.err.println("[SERVER] Std Ping; retries: " + ServerProcessor.this.pingRetrys + " to " + ServerProcessor.this.getClient() + "; ProcessorID: " + ServerProcessor.this.id);
                    }

                    ServerProcessor.this.heartBeatTimeStamp = System.currentTimeMillis();
                    ServerProcessor.this.heartBeatTimeStampTimeoutHelper = System.currentTimeMillis();
                    this.lastPingSend = System.currentTimeMillis();
                    ServerProcessor.this.waitingForPong = true;
                    return;
                }

                if (ServerProcessor.this.pingRetrys >= 0 && ServerProcessor.this.waitingForPong && System.currentTimeMillis() > ServerProcessor.this.heartBeatTimeStampTimeoutHelper + 10000L) {
                    System.err.println("[SERVERPROCESSOR][WARNING} PING timeout warning. resending ping to " + ServerProcessor.this.getClient() + " Retries left: " + ServerProcessor.this.pingRetrys + "; socket connected: " + ServerProcessor.this.commandSocket.isConnected() + "; socket closed: " + ServerProcessor.this.commandSocket.isClosed() + "; inputShutdown: " + ServerProcessor.this.commandSocket.isInputShutdown() + "; outputShutdown: " + ServerProcessor.this.commandSocket.isOutputShutdown() + "; ProcessorID: " + ServerProcessor.this.id);
                    this.sendPing(ServerProcessor.this.state.getId());
                    ServerProcessor.this.waitingForPong = true;
                    System.err.println("[SERVERPROCESSOR][WARNING} PING has been resent to " + ServerProcessor.this.client);
                    ServerProcessor.this.heartBeatTimeStampTimeoutHelper = System.currentTimeMillis();
                    ServerProcessor.this.pingRetrys--;
                    if (!ServerProcessor.this.commandSocket.isConnected() || ServerProcessor.this.commandSocket.isClosed()) {
                        throw new IOException("Connection Closed");
                    }
                } else if (ServerProcessor.this.pingRetrys < 300) {
                    System.err.println("RETRY STATUS: Retries: " + ServerProcessor.this.pingRetrys + "; waiting for pong " + ServerProcessor.this.waitingForPong + " (" + System.currentTimeMillis() + "/" + (ServerProcessor.this.heartBeatTimeStampTimeoutHelper + 10000L) + "); ProcessorID: " + ServerProcessor.this.id);
                }

                if (ServerProcessor.this.pingRetrys < 0) {
                    System.out.println("[SERVERPROCESSOR][ERROR] ping timeout (" + (System.currentTimeMillis() - ServerProcessor.this.heartBeatTimeStamp) + ") from client -> DISCONNECT" + ServerProcessor.this.getClient().getId() + "; ProcessorID: " + ServerProcessor.this.id);
                    ServerProcessor.this.connected = false;
                    ((ServerController)ServerProcessor.this.getState().getController()).unregister(ServerProcessor.this.client.getId());
                    System.err.println("[SERVER] PING TIMEOUT logged out client " + ServerProcessor.this.getClient().getId() + "; ProcessorID: " + ServerProcessor.this.id);
                    if (!ServerProcessor.this.commandSocket.isClosed()) {
                        ServerProcessor.this.commandSocket.close();
                        return;
                    }
                }
            } else if (System.currentTimeMillis() - this.firstTry > 1000L) {
                System.err.println("[SERVER] client has not send any login information: " + (System.currentTimeMillis() - this.firstTry) + " / 10000 ms; ProcessorID: " + ServerProcessor.this.id);
                if (System.currentTimeMillis() - this.firstTry > 10000L) {
                    System.err.println("[SERVER] NULL CLIENT TIMED OUT: DISCONENCTING; ProcessorID: " + ServerProcessor.this.id);
                    ServerProcessor.this.connected = false;
                    ServerProcessor.this.commandSocket.close();
                }
            }

        }

        private void sendPing(int var1) throws IOException {
            ServerProcessor.this.ping = true;
            synchronized(ServerProcessor.this.sendingQueue) {
                ServerProcessor.this.sendingQueue.notify();
            }
        }

        public void run() {
            ++ServerProcessor.PING_THREADS_RUNNING;

            while(true) {
                try {
                    if (!ServerProcessor.this.connected) {
                        return;
                    }

                    try {
                        this.execute();
                        if (ServerProcessor.this.connected) {
                            Thread.sleep(1000L);
                            continue;
                        }
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        System.out.println("[SERVER] client (ping processor) " + ServerProcessor.this.getClient() + " disconnected : " + var8.getMessage());
                        ServerProcessor.this.connected = false;
                        if (ServerProcessor.this.client != null) {
                            try {
                                System.err.println("[ERROR] SERVER PING FAILED FOR CLIENT " + ServerProcessor.this.client + " -> LOGGING OUT CLIENT");
                                ((ServerController)ServerProcessor.this.getState().getController()).unregister(ServerProcessor.this.client.getId());
                            } catch (Exception var7) {
                                var7.printStackTrace();
                            }
                        }

                        try {
                            ServerProcessor.this.onError();
                        } catch (IOException var6) {
                            var6.printStackTrace();
                        }
                        continue;
                    }
                } finally {
                    --ServerProcessor.PING_THREADS_RUNNING;
                }

                return;
            }
        }

        public void sendPong() throws IOException {
            ServerProcessor.this.pong = true;
            synchronized(ServerProcessor.this.sendingQueue) {
                ServerProcessor.this.sendingQueue.notify();
            }
        }
    }

    class SendingQueueThread extends Thread {
        private final DataOutputStream out;

        public SendingQueueThread(OutputStream var2) {
            this.setDaemon(true);
            this.out = new DataOutputStream(new FastBufferedOutputStream(var2, 8192));
        }

        public void sendPacket(FastByteArrayOutputStream var1) throws IOException {
            assert var1.position() > 0L;

            long var2 = System.currentTimeMillis();
            this.out.writeInt((int)var1.position());
            this.out.writeLong(var2);
            this.out.write(var1.array, 0, (int)var1.position());
        }

        private void sendPingAndPong() throws IOException {
            byte[] var1;
            if (ServerProcessor.this.ping) {
                var1 = Pinger.createPing();
                this.out.writeInt(var1.length);
                this.out.writeLong(System.currentTimeMillis());
                this.out.write(var1);
                if (ServerProcessor.this.state.flushPingImmediately()) {
                    this.out.flush();
                }

                ServerProcessor.this.ping = false;
            }

            if (ServerProcessor.this.pong) {
                var1 = Pinger.createPong();
                this.out.writeInt(var1.length);
                this.out.writeLong(System.currentTimeMillis());
                this.out.write(var1);
                if (ServerProcessor.this.state.flushPingImmediately()) {
                    this.out.flush();
                }

                ServerProcessor.this.pong = false;
            }

        }

        public void sendTestByte() throws IOException {
            this.out.write(100);
            this.out.flush();
        }

        public void run() {
            boolean var15 = false;

            label308: {
                try {
                    var15 = true;
                    ++ServerProcessor.SENDING_THREADS_RUNNING;

                    while(ServerProcessor.this.connected && !ServerProcessor.this.isStopTransmit()) {
                        if (ServerProcessor.this.sendingQueue.isEmpty() && ServerProcessor.this.connected) {
                            this.out.flush();
                        }

                        try {
                            sleep(2L);
                        } catch (InterruptedException var16) {
                        }

                        this.setName("SendingQueueThread(" + ServerProcessor.this.client + ")");
                        ServerProcessor.Packet var1 = null;
                        synchronized(ServerProcessor.this.sendingQueue) {
                            boolean var3 = false;

                            while(ServerProcessor.this.sendingQueue.isEmpty() && ServerProcessor.this.connected) {
                                ServerProcessor.this.sendingQueue.wait(5000L);
                                if (ServerProcessor.this.sendingQueue.isEmpty() && ServerProcessor.this.connected) {
                                    var3 = true;
                                    break;
                                }

                                if (!ServerProcessor.this.connected || ServerProcessor.this.isStopTransmit()) {
                                    break;
                                }

                                if (ServerProcessor.this.sendingQueue.size() > 700) {
                                    System.err.println("Exception: queue to send to " + ServerProcessor.this.client + " is too big (" + ServerProcessor.this.sendingQueue.size() + ") -> disconnecting this client");
                                    ServerProcessor.this.connected = false;
                                }
                            }

                            if (!var3) {
                                if (!ServerProcessor.this.connected || ServerProcessor.this.isStopTransmit()) {
                                    break;
                                }

                                var1 = (ServerProcessor.Packet)ServerProcessor.this.sendingQueue.dequeue();
                                --ServerProcessor.totalPackagesQueued;
                            }
                        }

                        if (var1 == null) {
                            this.sendPingAndPong();
                        } else {
                            long var2;
                            long var4;
                            long var6;
                            if (ServerProcessor.DELAY > 0 && (var4 = (var2 = System.currentTimeMillis()) - var1.time) < var2 && (var6 = (long)ServerProcessor.DELAY - var4) > 0L) {
                                Thread.sleep(var6);
                            }

                            this.sendPingAndPong();
                            this.sendPacket(var1.payload);
                            ServerProcessor.releasePacket(var1.payload);
                            if (ServerProcessor.this.sendingQueue.isEmpty() && ServerProcessor.this.disconnectAfterSent) {
                                System.err.println("[SERVER] SCHEDULED DISCONNECT EXECUTING");
                                this.out.writeInt(1);
                                this.out.writeLong(System.currentTimeMillis());
                                this.out.writeByte(65);
                                this.out.flush();
                                ServerProcessor.this.disconnect();
                            }
                        }
                    }

                    ServerProcessor.this.connected = false;
                    var15 = false;
                    break label308;
                } catch (Exception var21) {
                    System.err.println("SENDING THREAD ENDED of " + ServerProcessor.this.client);
                    var21.printStackTrace();
                    var15 = false;
                } finally {
                    if (var15) {
                        ServerProcessor.this.connected = false;
                        synchronized(ServerProcessor.this.sendingQueue) {
                            while(ServerProcessor.this.sendingQueue.size() > 0) {
                                ServerProcessor.releasePacket(((ServerProcessor.Packet)ServerProcessor.this.sendingQueue.dequeue()).payload);
                                --ServerProcessor.totalPackagesQueued;
                            }

                            ServerProcessor.this.sendingQueue.clear();
                        }

                        --ServerProcessor.SENDING_THREADS_RUNNING;
                    }
                }

                ServerProcessor.this.connected = false;
                synchronized(ServerProcessor.this.sendingQueue) {
                    while(ServerProcessor.this.sendingQueue.size() > 0) {
                        ServerProcessor.releasePacket(((ServerProcessor.Packet)ServerProcessor.this.sendingQueue.dequeue()).payload);
                        --ServerProcessor.totalPackagesQueued;
                    }

                    ServerProcessor.this.sendingQueue.clear();
                }

                --ServerProcessor.SENDING_THREADS_RUNNING;
                return;
            }

            ServerProcessor.this.connected = false;
            synchronized(ServerProcessor.this.sendingQueue) {
                while(ServerProcessor.this.sendingQueue.size() > 0) {
                    ServerProcessor.releasePacket(((ServerProcessor.Packet)ServerProcessor.this.sendingQueue.dequeue()).payload);
                    --ServerProcessor.totalPackagesQueued;
                }

                ServerProcessor.this.sendingQueue.clear();
            }

            --ServerProcessor.SENDING_THREADS_RUNNING;
        }
    }
}
