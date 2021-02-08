package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

import java.net.Socket;

public class HttpsResponseSentEvent extends Event {

    public byte[] buffer;
    public int read;

    public Request request;
    public Socket connection;
    public Socket client;


    public HttpsResponseSentEvent(NetworkManager networkManager, Request request, Socket client, Socket connection, byte[] buffer, int read) {
        super(networkManager);
        this.buffer = buffer;
        this.read = read;
        this.request = request;
        this.client = client;
        this.connection = connection;
    }
}
