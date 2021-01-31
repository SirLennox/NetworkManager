package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

import java.net.Socket;

public class RequestSentEvent extends Event {
    public Request request;
    public Socket connection;

    public RequestSentEvent(NetworkManager networkManager, Request request, Socket connection) {
        super(networkManager);
        this.request = request;
        this.connection = connection;
    }
}
