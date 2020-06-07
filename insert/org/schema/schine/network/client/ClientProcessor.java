//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.schine.network.client;

import api.DebugFile;
import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.network.Command;
import org.schema.schine.network.DataInputStreamPositional;
import org.schema.schine.network.DataOutputStreamPositional;
import org.schema.schine.network.Header;
import org.schema.schine.network.NetUtil;
import org.schema.schine.network.NetworkProcessor;
import org.schema.schine.network.Request;
import org.schema.schine.network.StateInterface;
import org.schema.schine.network.exception.DisconnectException;
import org.schema.schine.network.objects.NetworkObject;
import org.schema.schine.network.server.ServerState;

public class ClientProcessor implements Runnable, NetworkProcessor {
    public static final boolean DEBUG_BIG_CHUNKS = false;
    private static final int MAX_PACKET_POOL_SIZE = 1000;
    private static final ArrayList<FastByteArrayOutputStream> packetPool = new ArrayList();
    public static int DELAY;
    public static int MAX_PACKET_SIZE;
    private final Map<Short, Request> pendingRequests = new HashMap();
    private final ObjectArrayFIFOQueue<ClientProcessor.Packet> sendingQueue = new ObjectArrayFIFOQueue();
    private final ObjectArrayList<NetworkObject> lastReceived = new ObjectArrayList();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    public long lastPacketId;
    boolean listening = true;
    private FastByteArrayOutputStream byteArrayOutDoubleBuffer;
    private DataOutputStreamPositional outDoubleBuffer;
    private DataInputStreamPositional inDoubleBuffer;
    private Socket receive;
    private ClientStateInterface state;
    private ClientToServerConnection clientToServerConnection;
    private DataInputStream dataInputStream;
    private Header headerTmp;
    private ClientProcessor.Pinger pinger;
    private Thread thread;
    private Command lastCommand;
    private boolean ping;
    private boolean pong;
    private long serverPacketSentTimestamp;
    private ClientProcessor.SendingQueueThread sendingQueueThread;
    private ClientProcessor.HandleThread handlerThread;
    private boolean waitingForPong;
    private long lastPingTime;
    private long lastPong;

    public ClientProcessor(ClientToServerConnection var1, ClientStateInterface var2) {
        this.outDoubleBuffer = new DataOutputStreamPositional(this.byteArrayOutDoubleBuffer = new FastByteArrayOutputStream(307200));
        this.state = var2;
        this.clientToServerConnection = var1;
        this.receive = var1.getConnection();
        this.sendingQueueThread = new ClientProcessor.SendingQueueThread();
        this.sendingQueueThread.start();
    }

    public static FastByteArrayOutputStream getNewPacket() {
        synchronized(packetPool) {
            if (!packetPool.isEmpty()) {
                return (FastByteArrayOutputStream)packetPool.remove(packetPool.size() - 1);
            }
        }

        return new FastByteArrayOutputStream(MAX_PACKET_SIZE);
    }

    public static void releasePacket(FastByteArrayOutputStream var0) {
        var0.reset();
        synchronized(packetPool) {
            if (packetPool.size() < 1000) {
                packetPool.add(var0);
            }

        }
    }

    public void closeSocket() throws IOException {
        this.sendLogout();
        System.err.println("[CLIENT] CLOSING SOCKET");
        if (!this.clientToServerConnection.getConnection().isClosed()) {
            System.err.println("[CLIENT] CLOSING SOCKET");
            this.clientToServerConnection.getConnection().close();
        }

    }

    public void flushDoubleOutBuffer() throws IOException {
        assert this.byteArrayOutDoubleBuffer.position() > 0L;

        FastByteArrayOutputStream var1;
        (var1 = getNewPacket()).write(this.byteArrayOutDoubleBuffer.array, 0, (int)this.byteArrayOutDoubleBuffer.position());
        this.resetDoubleOutBuffer();
        synchronized(this.sendingQueue) {
            if (this.listening) {
                this.sendingQueue.enqueue(new ClientProcessor.Packet(var1));
                this.sendingQueue.notify();
            }

        }
    }

    public int getCurrentSize() {
        return (int)this.byteArrayOutDoubleBuffer.position();
    }

    public DataInputStreamPositional getIn() {
        return this.inDoubleBuffer;
    }

    public InputStream getInRaw() throws IOException {
        return this.receive.getInputStream();
    }

    public DataOutputStreamPositional getOut() {
        return this.outDoubleBuffer;
    }

    public OutputStream getOutRaw() throws IOException {
        return this.receive.getOutputStream();
    }

    public StateInterface getState() {
        return this.state;
    }

    public boolean isAlive() {
        return this.clientToServerConnection.isAlive();
    }

    public void notifyPacketReceived(short var1, Command var2) {
        if (var2.getMode() == 1) {
            Request var3 = null;
            synchronized(this.getPendingRequests()) {
                var3 = (Request)this.getPendingRequests().remove(var1);

                assert var3 != null : "Request #" + var1 + " not pending!!";

                assert var2.getMode() == 1 : "COMMAND DOES NOT RETURN BUT SENT RETURN VALUE: " + var2;

                synchronized(var3) {
                    assert !this.getPendingRequests().containsKey(var1);

                    var3.notify();
                }

            }
        }
    }

    public void resetDoubleOutBuffer() throws IOException {
        this.byteArrayOutDoubleBuffer.reset();
    }

    public void updatePing() throws IOException {
        this.pinger.execute();
    }

    public ReentrantReadWriteLock getBufferLock() {
        return this.rwl;
    }

    public ObjectArrayList<NetworkObject> getLastReceived() {
        return this.lastReceived;
    }

    public Map<Short, Request> getPendingRequests() {
        return this.pendingRequests;
    }

    public long getServerPacketSentTimestamp() {
        return this.serverPacketSentTimestamp;
    }

    public Thread getThread() {
        return this.thread;
    }

    public void setThread(Thread var1) {
        this.thread = var1;
    }

    public int getReceiveQueue() {
        return this.handlerThread.packets.size();
    }

    public void run() {
        this.setup();
        byte var1 = 0;

        try {
            this.handlerThread = new ClientProcessor.HandleThread(10);
            this.handlerThread.start();
            this.dataInputStream = new DataInputStream(this.receive.getInputStream());
            while(!this.receive.isConnected()) {
                System.err.println("waiting for connection " + this.state);
            }

            while(this.listening && !this.receive.isClosed()) {
                this.getThread().setName("client Processor: " + this.state.getId());
                int size = this.dataInputStream.readInt();

                //INSERTED CODE @337
                if(size == -2){
                    //SPECIAL PACKET ID received
                    short packetId = this.dataInputStream.readShort();
                    //Construct packet
                    api.network.Packet packet = api.network.Packet.newPacket(packetId);
                    //Fill with data
                    try {
                        packet.readPacketData(new PacketReadBuffer(dataInputStream));
                    }catch (IOException e){
                        e.printStackTrace();
                        DebugFile.logError(e, null);
                    }
                    //dispatch TODO Move packet to a queue to be executed on the main loop
                    packet.processPacketOnClient();
                    continue;
                }
                ///

                this.serverPacketSentTimestamp = this.dataInputStream.readLong();
                ((ClientState)this.state).setDebugBigChunk(size > 4000);
                if (this.state.isReadingBigChunk()) {
                    System.err.println("[CLIENT] WARNING: Received big chunk: " + size + " bytes");
                }

                assert size > 0 : " Empty update!";

                byte[] var2 = this.handlerThread.get(size);
                if (size > 131072) {
                    System.err.println("[CLIENT] WARNING: received large (>128kb) NT package: " + size + " / " + var2.length);
                }

                this.dataInputStream.readFully(var2, 0, size);
                if (size == 1 && var2[0] == 65) {
                    break;
                }
                //DebugFile.info("Received packet: " + var23 + ", data=" + Arrays.toString(var2));

                this.handlerThread.enqueueReadPacket(new ClientProcessor.ClientPacket(var2, size));
            }
        } catch (EOFException var19) {
            System.err.println("[CLIENT] EOFConnection (last file size: " + var1 + "; last packet timestamp: " + this.serverPacketSentTimestamp + ")");
            var19.printStackTrace();
            if (!ClientState.loginFailed && (this.state == null || !this.state.isDoNotDisplayIOException())) {
                if (this.state != null && !this.state.isPassive()) {
                    this.state.handleExceptionGraphically(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var19.getClass().getSimpleName()));
                } else {
                    GLFrame.processErrorDialogException(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var19.getClass().getSimpleName()), this.getState());
                }
            } else {
                ClientState.loginFailed = false;
            }
        } catch (IOException var20) {
            var20.printStackTrace();
            if (!ClientState.loginFailed && (this.state == null || !this.state.isDoNotDisplayIOException())) {
                if (var20 instanceof SocketException) {
                    if (this.state != null && !this.state.isPassive()) {
                        this.state.handleExceptionGraphically(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var20.getClass().getSimpleName()));
                    } else {
                        GLFrame.processErrorDialogExceptionWithoutReport(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var20.getClass().getSimpleName()), this.getState());
                    }
                } else if (this.state != null && !this.state.isPassive()) {
                    this.state.handleExceptionGraphically(var20);
                } else {
                    GLFrame.processErrorDialogException(var20, this.getState());
                }
            }
        } catch (Exception var21) {
            var21.printStackTrace();
            if (var21 instanceof SocketException) {
                if (this.state != null && !this.state.isPassive()) {
                    this.state.handleExceptionGraphically(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var21.getClass().getSimpleName()));
                } else {
                    GLFrame.processErrorDialogExceptionWithoutReport(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var21.getClass().getSimpleName()), this.getState());
                }
            } else if (this.state != null && !this.state.isPassive()) {
                this.state.handleExceptionGraphically(new DisconnectException("You have been disconnected from the Server \n(either connection problem or server crash)\nActualException: " + var21.getClass().getSimpleName()));
            } else {
                GLFrame.processErrorDialogException(var21, this.getState());
            }
        } finally {
            this.listening = false;
            synchronized(this.sendingQueue) {
                this.sendingQueue.notifyAll();
            }

            if (this.handlerThread != null) {
                synchronized(this.handlerThread.packets) {
                    this.handlerThread.packets.notifyAll();
                }
            }

        }

        System.out.println("[ClientProcessor] EXIT: Input Stream closed. Terminated Client Processor");
    }

    public void sendLogout() throws IOException {
        try {
            System.err.println(this + " sending logout");
            this.getBufferLock().writeLock().lock();
            this.getOut().writeByte(65);
            this.flushDoubleOutBuffer();
        } finally {
            this.getBufferLock().writeLock().unlock();
        }

    }

    public void sendPacket(FastByteArrayOutputStream var1) throws IOException {
        assert var1.position() > 0L;

        this.clientToServerConnection.getOutput().writeInt((int)var1.position());
        this.clientToServerConnection.getOutput().write(var1.array, 0, (int)var1.position());
    }

    private void setup() {
        this.headerTmp = new Header();
        this.pinger = new ClientProcessor.Pinger();
    }

    static {
        DELAY = (Integer)EngineSettings.N_ARTIFICIAL_DELAY.getCurrentState();
        MAX_PACKET_SIZE = 20480;
    }

    class HandleThread extends Thread {
        private ObjectArrayFIFOQueue<byte[]> free = new ObjectArrayFIFOQueue();
        private ObjectArrayFIFOQueue<ClientProcessor.ClientPacket> packets = new ObjectArrayFIFOQueue();
        private long lastSetSnapshop;
        private FastByteArrayInputStream byteArrayInputStream;

        public HandleThread(int var2) {
            super("ClientPacketHandlerThread");
            this.setDaemon(true);

            for(int var3 = 0; var3 < var2; ++var3) {
                this.free.enqueue(new byte[1048576]);
            }

            this.byteArrayInputStream = new FastByteArrayInputStream(new byte[1], 0, 1);
            ClientProcessor.this.inDoubleBuffer = new DataInputStreamPositional(this.byteArrayInputStream);
        }

        public void release(byte[] var1) {
            synchronized(this.free) {
                this.free.enqueue(var1);
                this.free.notify();
            }
        }

        public byte[] get(int var1) {
            byte[] var2;
            synchronized(this.free) {
                while(this.free.isEmpty()) {
                    try {
                        this.free.wait();
                    } catch (InterruptedException var4) {
                        var4.printStackTrace();
                    }
                }

                var2 = (byte[])this.free.dequeue();
            }

            if (var2.length < var1) {
                var2 = new byte[var1];
            }

            return var2;
        }

        private ClientProcessor.ClientPacket getReadPacket() {
            synchronized(this.packets) {
                while(this.packets.isEmpty()) {
                    try {
                        this.packets.wait();
                        if (!ClientProcessor.this.listening) {
                            Object var10000 = null;
                            return (ClientProcessor.ClientPacket)var10000;
                        }
                    } catch (InterruptedException var3) {
                        var3.printStackTrace();
                    }
                }

                ClientProcessor.ClientPacket var1 = (ClientProcessor.ClientPacket)this.packets.dequeue();
                return var1;
            }
        }

        public void enqueueReadPacket(ClientProcessor.ClientPacket var1) {
            synchronized(this.packets) {
                this.packets.enqueue(var1);
                this.packets.notify();
            }
        }

        public void handle(byte[] var1, int var2) throws Exception {
            synchronized(ClientProcessor.this.state) {
                ClientProcessor.this.state.setSynched();

                try {
                    this.byteArrayInputStream.array = var1;
                    this.byteArrayInputStream.offset = 0;
                    this.byteArrayInputStream.length = var2;
                    this.byteArrayInputStream.reset();
                    boolean var4 = false;

                    int var10;
                    for(int var5 = 0; (var10 = ClientProcessor.this.getIn().read()) >= 0; ++var5) {
                        assert var10 == 42 || var10 == 23 : "CLIENT CHECK FAILED: " + var10 + " " + Arrays.toString(Arrays.copyOf(var1, var2)) + "; available: " + this.byteArrayInputStream.available() + ", happend on object " + var5 + ";\nLast Command " + ClientProcessor.this.lastCommand + "; " + ClientProcessor.this.lastReceived;

                        if (var10 == 23) {
                            var4 = ClientProcessor.this.pinger.checkPing(ClientProcessor.this.getIn());

                            assert var4;
                        } else if (var10 == 42) {
                            this.parseNextPacket();
                        } else {
                            System.err.println("[CLIENT] WARNING: FAULTY PACKET " + this.byteArrayInputStream.available() + ", happend on object " + var5 + ";\nLast Command " + ClientProcessor.this.lastCommand + "; " + ClientProcessor.this.lastReceived);
                        }
                    }

                    if (this.byteArrayInputStream.available() > 0) {
                        System.err.println("[CLIENT] WARNING: PACKET NOT FULLY READ ( " + this.byteArrayInputStream.available() + "). last check: " + var10 + ": synched: " + ClientProcessor.this.state.isNetworkSynchronized() + "; last command " + ClientProcessor.this.lastCommand);
                        if (ClientProcessor.this.state.isNetworkSynchronized()) {
                            throw new IOException("[CRITICAL ERROR] PARSING PACKET NOT COMPLETED. BUT CLIENT WAS SYNCHRONIZED!");
                        }

                        System.err.println("[CLIENT] OK. state got out of synch. Already resynching!");
                    }

                    if (System.currentTimeMillis() - this.lastSetSnapshop > 1000L) {
                        ClientProcessor.this.state.getDataStatsManager().snapshotDownload(ClientProcessor.this.state.getReceivedData());
                        this.lastSetSnapshop = System.currentTimeMillis();
                    }
                } finally {
                    ClientProcessor.this.state.setUnsynched();
                }

            }
        }

        public void parseNextPacket() throws Exception {
            ClientProcessor.this.headerTmp.read(ClientProcessor.this.getIn());
            Command var1 = NetUtil.commands.getById(ClientProcessor.this.headerTmp.getCommandId());

            assert var1 != null : "could not find " + ClientProcessor.this.headerTmp.getCommandId();

            ClientProcessor.this.lastCommand = var1;
            if (ClientProcessor.this.headerTmp.getType() == 111) {
                Object[] var2 = var1.readParameters(ClientProcessor.this.headerTmp, ClientProcessor.this.getIn());
                var1.clientAnswerProcess(var2, ClientProcessor.this.state, ClientProcessor.this.headerTmp.packetId);
            } else if (ClientProcessor.this.headerTmp.getType() == 123) {
                var1.clientAnswerProcess((Object[])null, ClientProcessor.this.state, ClientProcessor.this.headerTmp.packetId);
            } else {
                System.err.println("[CRITICAL][ERROR] HEADER TYPE IS UNKNOWN");
            }

            if (NetUtil.commands.getById(ClientProcessor.this.headerTmp.getCommandId()) == null) {
                throw new IOException("[CRITICAL][ERROR] Could not find command " + ClientProcessor.this.headerTmp.getCommandId());
            } else {
                ClientProcessor.this.notifyPacketReceived(ClientProcessor.this.headerTmp.packetId, NetUtil.commands.getById(ClientProcessor.this.headerTmp.getCommandId()));
            }
        }

        public void run() {
            ClientProcessor.ClientPacket var1;
            try {
                for(; ClientProcessor.this.listening && !ClientProcessor.this.receive.isClosed(); this.release(var1.receiveBuffer)) {
                    var1 = this.getReadPacket();
                    if (!ClientProcessor.this.listening) {
                        return;
                    }

                    int var2 = var1.size;
                    byte[] var3 = var1.receiveBuffer;

                    try {
                        this.handle(var3, var2);
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }
                }
            } finally {
                System.err.println("[CLIENT] PacketHandler Terminated");
            }

        }
    }

    class ClientPacket {
        final byte[] receiveBuffer;
        final int size;

        public ClientPacket(byte[] var2, int var3) {
            this.receiveBuffer = var2;
            this.size = var3;
        }
    }

    class SendingQueueThread extends Thread {
        private SendingQueueThread() {
        }

        private void checkPingPong() throws IOException {
            byte[] var1;
            if (ClientProcessor.this.ping) {
                var1 = org.schema.schine.network.Pinger.createPing();
                ClientProcessor.this.clientToServerConnection.getOutput().writeInt(var1.length);
                ClientProcessor.this.clientToServerConnection.getOutput().write(var1);
                ClientProcessor.this.ping = false;
                ClientProcessor.this.lastPingTime = System.currentTimeMillis();
                ClientProcessor.this.waitingForPong = true;
            }

            if (ClientProcessor.this.pong) {
                var1 = org.schema.schine.network.Pinger.createPong();
                ClientProcessor.this.clientToServerConnection.getOutput().writeInt(var1.length);
                ClientProcessor.this.clientToServerConnection.getOutput().write(var1);
                ClientProcessor.this.pong = false;
            }

        }

        public void run() {
            long var1 = -1L;

            try {
                while(ClientProcessor.this.listening && !ClientProcessor.this.receive.isClosed()) {
                    if (var1 != this.getId()) {
                        this.setName("ClientSendingQueueThread(" + this.getId() + ")");
                        var1 = this.getId();
                    }

                    ClientProcessor.Packet var3;
                    synchronized(ClientProcessor.this.sendingQueue) {
                        while(ClientProcessor.this.sendingQueue.isEmpty() && ClientProcessor.this.listening) {
                            this.checkPingPong();
                            ClientProcessor.this.clientToServerConnection.getOutput().flush();
                            ClientProcessor.this.sendingQueue.wait(10000L);
                            if (!ClientProcessor.this.listening) {
                                break;
                            }

                            if (ClientProcessor.this.sendingQueue.size() > 50000) {
                                ClientProcessor.this.listening = false;
                            }
                        }

                        if (!ClientProcessor.this.listening) {
                            break;
                        }

                        var3 = (ClientProcessor.Packet)ClientProcessor.this.sendingQueue.dequeue();
                    }

                    long var5;
                    if (ClientProcessor.DELAY > 0 && (var5 = System.currentTimeMillis() - var3.time) < (long)ClientProcessor.DELAY) {
                        Thread.sleep((long)ClientProcessor.DELAY - var5);
                    }

                    if (ClientProcessor.this.listening) {
                        this.checkPingPong();
                        ClientProcessor.this.sendPacket(var3.payload);
                    }

                    ClientProcessor.releasePacket(var3.payload);

                    try {
                        sleep(2L);
                    } catch (InterruptedException var12) {
                    }
                }

                ClientProcessor.this.listening = false;
            } catch (Exception var15) {
                System.err.println("SENDING THREAD ENDED of " + this);
                var15.printStackTrace();
            } finally {
                System.err.println("[CLIENT] ClientSendingQueueThread Terminated");
                ClientProcessor.this.listening = false;
            }

            synchronized(ClientProcessor.this.sendingQueue) {
                while(ClientProcessor.this.sendingQueue.size() > 0) {
                    ClientProcessor.releasePacket(((ClientProcessor.Packet)ClientProcessor.this.sendingQueue.dequeue()).payload);
                }

                ClientProcessor.this.sendingQueue.clear();
            }
        }
    }

    class Packet {
        private FastByteArrayOutputStream payload;
        private long time;

        public Packet(FastByteArrayOutputStream var2) {
            if (ClientProcessor.DELAY > 0) {
                this.time = System.currentTimeMillis();
            }

            this.payload = var2;
        }
    }

    class Pinger {
        private Pinger() {
        }

        public boolean checkPing(InputStream var1) throws IOException {
            byte var2;
            if ((var2 = (byte)var1.read()) == -2) {
                ClientProcessor.this.waitingForPong = false;
                ClientProcessor.this.state.setPing(System.currentTimeMillis() - ClientProcessor.this.lastPingTime);
                ClientProcessor.this.lastPong = System.currentTimeMillis();
                return true;
            } else if (var2 == -1) {
                this.sendPong();
                return true;
            } else {
                return false;
            }
        }

        public void execute() throws IOException {
            if (ClientProcessor.this.clientToServerConnection.getId() > 0) {
                if (!ClientProcessor.this.waitingForPong && ClientProcessor.this.lastPingTime + 1000L < System.currentTimeMillis()) {
                    this.sendPing();
                }

                long var1 = System.currentTimeMillis() - ClientProcessor.this.lastPingTime;
                if (ClientProcessor.this.waitingForPong && var1 > 5000L) {
                    if (!ServerState.isCreated()) {
                        ((ClientControllerInterface)ClientProcessor.this.getState().getController()).alertMessage(Lng.ORG_SCHEMA_SCHINE_NETWORK_CLIENT_CLIENTPROCESSOR_0);
                    } else {
                        ((ClientControllerInterface)ClientProcessor.this.getState().getController()).alertMessage(Lng.ORG_SCHEMA_SCHINE_NETWORK_CLIENT_CLIENTPROCESSOR_2);
                    }

                    System.err.println("[CLIENTPROCESSOR][WARNING] Ping time of client (" + ClientProcessor.this.state.getId() + ") exceeded time limit: retrying! pending requests: " + ClientProcessor.this.getPendingRequests());
                    ClientProcessor.this.lastPingTime = System.currentTimeMillis();
                }
            }

        }

        public boolean sendPing() throws IOException {
            if (ClientProcessor.this.state.getId() < 0) {
                System.err.println("[CLIENT] not logged int to server: discarding ping");
                return false;
            } else {
                ClientProcessor.this.ping = true;
                synchronized(ClientProcessor.this.sendingQueue) {
                    ClientProcessor.this.sendingQueue.notify();
                    return true;
                }
            }
        }

        public void sendPong() {
            if (ClientProcessor.this.state.getId() < 0) {
                System.err.println("[CLIENT] not logged int to server: discarding pong");
            } else {
                ClientProcessor.this.pong = true;
                synchronized(ClientProcessor.this.sendingQueue) {
                    ClientProcessor.this.sendingQueue.notify();
                }
            }
        }
    }
}
