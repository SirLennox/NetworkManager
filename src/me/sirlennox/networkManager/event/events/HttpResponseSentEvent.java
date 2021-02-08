package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

public class HttpResponseSentEvent extends Event {

    public Request request;
    public String response;


    public HttpResponseSentEvent(NetworkManager networkManager, Request request, String response) {
        super(networkManager);
        this.request = request;
        this.response = response;
    }
}
