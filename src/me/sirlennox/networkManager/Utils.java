package me.sirlennox.networkManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Utils {

    public static void sendHTTPResponse(Socket s, String status, HashMap<String, String> headers, String content) throws IOException {
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.println("HTTP/1.0 " + (status == null ? "200 OK" : status));
        if(!headers.containsKey("Content-Type")) pw.println("Content-Type: text/html");
        for(String h : headers.keySet()) pw.println(h + ": " + headers.get(h));
        pw.println("");
        if(content != null) pw.print(content);
        pw.flush();
        pw.close();
    }

    public static boolean isSubdomainOf(String urlWithSubdomain, String url) {
        if (urlWithSubdomain.equalsIgnoreCase(url)) return true;
        String[] split = urlWithSubdomain.split("\\.");
        if (split.length > 2) {
            String withoutSubdomain = String.join(".",
                    Arrays.copyOfRange(split, split.length - 2, split.length));
            return withoutSubdomain.equalsIgnoreCase(url);
        } else return false;
    }

}
