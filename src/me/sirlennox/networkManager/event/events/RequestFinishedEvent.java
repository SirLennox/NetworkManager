package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

import java.net.Socket;

public class RequestFinishedEvent extends Event {
    private final Request request;
    private final Socket connection;

    public RequestFinishedEvent(Request request, Socket connection) {
        super();
        this.request = request;
        this.connection = connection;
    }

    public Request getRequest() {
        return request;
    }

    public Socket getConnection() {
        return connection;
    }
}
