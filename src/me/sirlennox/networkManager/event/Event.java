package me.sirlennox.networkManager.event;

import me.sirlennox.networkManager.NetworkManager;

public class Event {

    public String name;
    public boolean cancelled = true;

    public NetworkManager networkManager;

    public Event(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }


    public boolean call() {
        this.networkManager.onEvent(this);
        return this.cancelled;
    }



}
