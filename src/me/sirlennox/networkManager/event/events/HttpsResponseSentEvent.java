package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

import java.net.Socket;

public class HttpsResponseSentEvent extends Event {

    private final byte[] buffer;
    private final int read;

    private final Request request;
    private final Socket connection;
    private final Socket client;


    public HttpsResponseSentEvent(Request request, Socket client, Socket connection, byte[] buffer, int read) {
        super();
        this.buffer = buffer;
        this.read = read;
        this.request = request;
        this.client = client;
        this.connection = connection;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getRead() {
        return read;
    }

    public Request getRequest() {
        return request;
    }

    public Socket getConnection() {
        return connection;
    }

    public Socket getClient() {
        return client;
    }
}
