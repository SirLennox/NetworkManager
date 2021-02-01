package me.sirlennox.networkManager;

import java.net.Socket;
import java.util.HashMap;

public class Request {

    public StringBuilder raw;
    public String method;
    public String url;
    public int port;
    public HashMap<String, String> headers;
    public Socket socket;

    public Request(StringBuilder raw, String method, String url, int port, HashMap<String, String> headers, Socket socket) {
        this.raw = raw;
        this.method = method;
        this.url = url;
        this.port = port;
        this.headers = headers;
        this.socket = socket;
    }

   /* public Header getHeader(String key) {
        return this.headers.stream().filter(h -> h.key.equalsIgnoreCase(key)).findFirst().orElse(null);
    }*/

}
