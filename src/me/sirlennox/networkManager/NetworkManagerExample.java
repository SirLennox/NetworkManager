package me.sirlennox.networkManager;

import me.sirlennox.networkManager.event.Event;
import me.sirlennox.networkManager.event.events.PreRequestSentEvent;
import me.sirlennox.networkManager.event.events.RequestSentEvent;
import me.sirlennox.networkManager.event.events.StartEvent;

import java.io.IOException;

public class NetworkManagerExample {

    public static void main(String[] args) {
        NetworkManager networkManager = new NetworkManager(8080) {
            @Override
            public boolean onEvent(Event e) {
                if(e instanceof PreRequestSentEvent) {
                    PreRequestSentEvent e2 = (PreRequestSentEvent) e;
                    Request req = e2.request;
                    System.out.println("-----------[START]-----------");
                    System.out.println(req.raw);
                    System.out.println("-----------[END]-------------\n\n");
                }
                if(e instanceof StartEvent) {
                    System.out.println("Started!");
                }
                return true;
            }
        };
        if(!networkManager.start()) {
            System.err.println("Could not start Proxy!");
            System.exit(-1);
        }
    }

}
