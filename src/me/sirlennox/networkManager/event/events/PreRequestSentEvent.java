package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

public class PreRequestSentEvent extends Event {

    private final Request request;

    public PreRequestSentEvent(Request request) {
        super();
        this.request = request;
    }


    public Request getRequest() {
        return request;
    }
}
