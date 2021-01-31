package me.sirlennox.networkManager;

import java.net.Socket;
import java.util.ArrayList;

public class Request {

    public String raw;
    public String method;
    public String host;
    /*public ArrayList<Header> headers;*/
    public Socket socket;

    public Request(String raw, String method, String host/*, ArrayList<Header> headers*/,Socket socket) {
        this.raw = raw;
        this.method = method;
        this.host = host;
     //   this.headers = headers;
        this.socket = socket;
    }

   /* public Header getHeader(String key) {
        return this.headers.stream().filter(h -> h.key.equalsIgnoreCase(key)).findFirst().orElse(null);
    }*/

}
