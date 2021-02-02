package me.sirlennox.networkManager;

import me.sirlennox.networkManager.event.Event;
import me.sirlennox.networkManager.event.events.HttpResponseSentEvent;
import me.sirlennox.networkManager.event.events.HttpsResponseSentEvent;
import me.sirlennox.networkManager.event.events.PreRequestSentEvent;
import me.sirlennox.networkManager.event.events.StartEvent;

import java.io.IOException;
import java.util.HashMap;

public class NetworkManagerExample {

    public static void main(String[] args) {
        NetworkManager networkManager = new NetworkManager(8080) {
            @Override
            public boolean onEvent(Event e) {
                if(e instanceof StartEvent) {
                    System.out.println("Started!");
                }
                if(e instanceof HttpResponseSentEvent) {
                    System.out.println("\n\n\n\n------[EVENT]----------");
                    System.out.println("\n\n------[REQUEST]-------");
                    System.out.println(((HttpResponseSentEvent) e).req.raw);
                    System.out.println("------[REQUEST END]-------");
                    System.out.println("\n\n------[RESPONSE]-------");
                    System.out.println(((HttpResponseSentEvent) e).res);
                    System.out.println("------[RESPONSE END]-------");
                    System.out.println("------[EVENT END]----------");
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
