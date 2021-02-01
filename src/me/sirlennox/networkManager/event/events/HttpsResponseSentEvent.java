package me.sirlennox.networkManager.event.events;

import me.sirlennox.networkManager.NetworkManager;
import me.sirlennox.networkManager.event.Event;

import java.net.Socket;

public class HttpsResponseSentEvent extends Event {

    public byte[] buffer;
    public int read;

    public Socket clientSocket;
    public Socket connection;


    public HttpsResponseSentEvent(NetworkManager networkManager, Socket clientSocket, Socket connection, byte[] buffer, int read) {
        super(networkManager);
        this.buffer = buffer;
        this.read = read;
        this.clientSocket = clientSocket;
        this.connection = connection;
    }
}
