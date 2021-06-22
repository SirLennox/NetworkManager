package me.sirlennox.networkManager;

import java.net.Socket;
import java.util.HashMap;

public class Request {

    private final StringBuilder raw;
    private final String method;
    private final String url;
    private final String host;
    private final int port;
    private final HashMap<String, String> headers;
    private final Socket socket;

    public Request(StringBuilder raw, String method, String host, String url, int port, HashMap<String, String> headers, Socket socket) {
        this.raw = raw;
        this.method = method;
        this.url = url;
        this.host = host;
        this.port = port;
        this.headers = headers;
        this.socket = socket;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public StringBuilder getRaw() {
        return raw;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }
}
