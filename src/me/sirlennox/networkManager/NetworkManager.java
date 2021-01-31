package me.sirlennox.networkManager;

import me.sirlennox.networkManager.event.Event;
import me.sirlennox.networkManager.event.events.StartEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class NetworkManager {

    private final int port;
    private boolean running;
    private Thread runThread;

    public ServerSocket proxy;

    public NetworkManager(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public boolean start() {
        this.running = true;
        try {
            this.proxy = new ServerSocket(port);
            this.runLoop(this.proxy);
            this.onEvent(new StartEvent(this));
            return true;
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    public boolean stop() {
        if(this.proxy == null || this.runThread == null || this.runThread.isInterrupted() || this.proxy.isClosed()) return false;
        try {
            this.running = false;
            this.proxy.close();
            this.runThread.interrupt();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void runLoop(ServerSocket server) {
        (this.runThread = new Thread(() -> {
            while (NetworkManager.this.running) {
                try {
                    Socket socketToProxy = server.accept();
                    new RequestHandler(socketToProxy, this).start();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        }, "SocketAcceptorThread")).start();
    }

    public abstract boolean onEvent(Event e);
}
