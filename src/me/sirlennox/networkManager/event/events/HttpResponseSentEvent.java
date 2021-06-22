package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

public class HttpResponseSentEvent extends Event {

    private final Request request;
    private final String response;


    public HttpResponseSentEvent(Request request, String response) {
        super();
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return this.request;
    }

    public String getResponse() {
        return this.response;
    }

}
