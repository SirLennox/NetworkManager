package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

public class HttpResponseSentEvent extends Event {

    public Request req;
    public String res;


    public HttpResponseSentEvent(NetworkManager networkManager, Request req, String res) {
        super(networkManager);
        this.req = req;
        this.res = res;
    }
}
