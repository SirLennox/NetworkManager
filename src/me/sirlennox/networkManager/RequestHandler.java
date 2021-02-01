package me.sirlennox.networkManager;

import me.sirlennox.networkManager.event.events.HttpsResponseSentEvent;
import me.sirlennox.networkManager.event.events.PreRequestSentEvent;
import me.sirlennox.networkManager.event.events.RequestSentEvent;
import me.sirlennox.networkManager.event.events.HttpResponseSentEvent;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class RequestHandler extends Thread {

    //The incoming request
    public Socket clientSocket;

    //The outgoing connection of the proxy
    public Socket connection;

    public NetworkManager networkManager;

    public RequestHandler(Socket socketToProxy, NetworkManager networkManager) {
        this.clientSocket = socketToProxy;
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        try {
            InputStream clientToProxy = clientSocket.getInputStream();
            StringBuilder fullRequest = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String statusLine = br.readLine();
            fullRequest.append(statusLine);
            fullRequest.append("\r\n");
            String line;
            HashMap<String, String> headers = new HashMap<>();
            while (!"".equals(line = br.readLine())) {
                if(line != null) {
                    fullRequest.append(line);
                    String[] headerSplit = line.split(": ");
                    if (headerSplit.length > 1)
                        headers.put(headerSplit[0], String.join(": ", Arrays.copyOfRange(headerSplit, 1, headerSplit.length)));
                    fullRequest.append("\r\n");
                }
            }

            String[] statusLineSplit = statusLine.split(" ");
            String statusUrlAddress = statusLineSplit[1];

            String httpMethod = statusLineSplit[0];
            String[] ipPortSplit = statusLineSplit[1].split(":");
            String url;


            String host = headers.get("Host");

            boolean isHttps = !statusLineSplit[1].startsWith("http://");

            int port = isHttps ? 443 : 80;
            if(host != null) {
                String[] hostSplit = host.split(":");
                url = hostSplit[0];
                if(hostSplit.length > 1) {
                    try {
                        port = Integer.parseInt(hostSplit[1]);
                    }catch (NumberFormatException ignored) {}
                }else port = 80;

            }else {
                if(isHttps) {
                    url = ipPortSplit[0];
                    if (statusLineSplit[1].matches(":\\d*")) {
                        int ind = statusLineSplit[1].indexOf(":", 6);
                        String strPort = statusLineSplit[1].substring(ind);
                        try {
                            port = Integer.parseInt(strPort);
                        }catch (NumberFormatException ignored) { }
                    }
                }else url = new URL(statusUrlAddress).getHost();

            }
            Request req = new Request(fullRequest, httpMethod, url, port, headers, clientSocket);
            if(!networkManager.onEvent(new PreRequestSentEvent(networkManager, req))) return;
            try {
                connection = new Socket(req.url, req.port);
            }catch (UnknownHostException e) {
                Utils.sendHTTPResponse(clientSocket, "404 DNS_PROBE_FINISHED_NXDOMAIN", new HashMap<>(), null);
                clientSocket.close();
                return;
            }


            BufferedWriter proxyToclientBW = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            if (!isHttps) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())));
                out.println();
                out.print(fullRequest);
                out.println();
                out.flush();

                InputStream connectionToProxy = connection.getInputStream();

                StringBuilder fullResponse = new StringBuilder();
                BufferedReader conToProxyReader = new BufferedReader(new InputStreamReader(connectionToProxy));

                while (!"".equals(line = conToProxyReader.readLine())) {
                    if(line != null) fullResponse.append(line).append("\r\n");
                }

                HttpResponseSentEvent event = new HttpResponseSentEvent(networkManager, req, fullResponse.toString());
                if(!networkManager.onEvent(event)) return;
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                pw.print(event.res);
                pw.flush();
                if(!networkManager.onEvent(new RequestSentEvent(networkManager, req, connection))) return;
                connectionToProxy.close();
                out.close();
            } else {
                proxyToclientBW.write("HTTP/1.0 200 Connection established\r\n" +
                        "Proxy-Agent: ProxyServer/1.0\r\n" +
                        "\r\n");
                proxyToclientBW.flush();


                new Thread(() -> makeHttpsConnection(clientSocket, connection)).start();

                makeHttpsConnection(connection, clientSocket);
                if(!networkManager.onEvent(new RequestSentEvent(networkManager, req, connection))) return;
                clientSocket.close();
                proxyToclientBW.close();
                br.close();
            }

            if (connection != null) {
                connection.close();
            }

            clientToProxy.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.run();
    }

    private void makeHttpsConnection(Socket clientSocket, Socket connection) {
        byte[] buffer = new byte[4096];
        int read;
        try {
            do {
                read = clientSocket.getInputStream().read(buffer);
                HttpsResponseSentEvent event = new HttpsResponseSentEvent(networkManager, clientSocket, connection, buffer, read);
                if(!networkManager.onEvent(event)) return;
                buffer = event.buffer;
                read = event.read;
                if (read > 0) {
                    connection.getOutputStream().write(buffer, 0, read);
                    if (clientSocket.getInputStream().available() < 1) connection.getOutputStream().flush();
                }
            } while (read >= 0);
        } catch (IOException ignored) { }
    }
}
