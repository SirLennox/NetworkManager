package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.Request;
import me.sirlennox.networkManager.event.Event;

import java.io.IOException;
import java.net.Socket;

public class PreRequestSentEvent extends Event {

    public Request request;

    public PreRequestSentEvent(NetworkManager networkManager, Request request) {
        super(networkManager);
        this.request = request;
    }



}
