package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.event.Event;

public class StartEvent extends Event {
    public StartEvent(NetworkManager networkManager) {
        super(networkManager);
    }
}
