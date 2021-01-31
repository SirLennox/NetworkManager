package me.sirlennox.networkManager;

import me.sirlennox.networkManager.event.events.PreRequestSentEvent;
import me.sirlennox.networkManager.event.events.RequestSentEvent;

import java.io.*;
import java.net.Socket;
import java.net.URL;

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
   /*     try {
            String rawContent = "";

            Scanner socketToProxyContentScanner = new Scanner(clientSocket.getInputStream());
            ArrayList<String> contentLines = new ArrayList<>();
            while (socketToProxyContentScanner.hasNextLine()) {
                String str = socketToProxyContentScanner.nextLine();
                contentLines.add(str);
                rawContent += str + "\r\n";
            }
            if (contentLines.isEmpty()) return;

            String request = contentLines.get(0);
            String[] splittedRequest = request.split(" ");
            String requestMethod = splittedRequest[0];
            String host = splittedRequest[1];
            ArrayList<String> headerStringArray = (ArrayList<String>) contentLines.clone();
            ArrayList<Header> headers = new ArrayList<>();

            if (headerStringArray.size() > 1) {
                headerStringArray.remove(0);
                for (String str : headerStringArray) {
                    System.out.println(str);
                    String[] split = str.split(": ");
                    if (split.length >= 2) headers.add(new Header(split[0], split[1]));
                }
            }
            Request req = new Request(rawContent, requestMethod, host, headers, clientSocket);
            if (this.networkManager.onEvent(new RequestSentEvent(this.networkManager, req))) {
                Header hostHeader = req.getHeader("Host");
                String rawHost = hostHeader.value;
                String[] hostSplit = rawHost.split(":");
                String ip;
                int port = 80;

                if (hostSplit.length >= 1) ip = hostSplit[0];
                else return;
                try {
                    if (hostSplit.length >= 2) port = Integer.parseInt(hostSplit[1]);
                } catch (NumberFormatException e) {
                    return;
                }

                Socket toHostSocket = new Socket(ip, port);
                OutputStream toHostOut = toHostSocket.getOutputStream();
                //Writes the content of the request to the outputstream of the socket that goes to the host
                toHostOut.write(rawContent.getBytes());
                toHostOut.flush();
                InputStream in = toHostSocket.getInputStream();
                Scanner inScanner = new Scanner(in);
                //Writes the output of the host to the clients inputstream
                PrintWriter hostToClient = new PrintWriter(clientSocket.getOutputStream());
                while (inScanner.hasNextLine()) {
                    String line = inScanner.nextLine();
                    System.out.println("Line: " + line);
                    hostToClient.println(line);
                }
                hostToClient.flush();
            }
        }catch (Throwable t) {
            t.printStackTrace();
        }*/
        try {
            InputStream clientToProxy = clientSocket.getInputStream();
            String request = "";
            StringBuilder fullRequest = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = br.readLine();
            fullRequest.append(request);
            fullRequest.append("\r\n");
            String line = "";
            while (!(line = br.readLine()).equals("")) {
                fullRequest.append(line);
                fullRequest.append("\r\n");
            }

            String[] arr = request.split(" ");
            String urlAdress = arr[1];

            String url = "";
            String connectionMethod = arr[0];
            if (connectionMethod.equals("CONNECT")) {
                String[] tmp = arr[1].split(":");
                url = tmp[0];
            }

            int port = -1;
            boolean result = arr[1].matches(":\\d*");
            if (result) {
                int ind = arr[1].indexOf(":", 6);
                String strPort = arr[1].substring(ind);
                port = Integer.parseInt(strPort);
            }
            Request req = new Request(fullRequest.toString(), connectionMethod, url, clientSocket);
            if(!networkManager.onEvent(new PreRequestSentEvent(networkManager, req))) return;
            connection = null;
            boolean isHttps = false;

            if (!arr[1].startsWith("http://")) isHttps = true;


            if (port > 0) {
                connection = (isHttps ? new Socket(url, port) : new Socket(new URL(urlAdress).getHost(), port));
            } else {
                connection = (isHttps ? new Socket(url, 443) : new Socket(new URL(urlAdress).getHost(), 80));
            }
            if(!networkManager.onEvent(new RequestSentEvent(networkManager, req, connection))) return;


            BufferedWriter proxyToclientBW = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            if (!isHttps) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())));
                out.println();
                out.print(fullRequest);
                out.println();
                out.flush();

                InputStream connectionToProxy = connection.getInputStream();

                int k;
                while ((k = connectionToProxy.read()) != -1) {
                    clientSocket.getOutputStream().write((char) k);
                }

                clientSocket.getOutputStream().flush();

                connectionToProxy.close();
                out.close();
            } else {
                proxyToclientBW.write("HTTP/1.0 200 Connection established\r\n" +
                        "Proxy-Agent: ProxyServer/1.0\r\n" +
                        "\r\n");
                proxyToclientBW.flush();


                new Thread(() -> makeHttpsConnection(clientSocket, connection)).start();

                makeHttpsConnection(connection, clientSocket);

                clientSocket.close();
                proxyToclientBW.close();
                br.close();
            }

            if (connection != null) {
                connection.close();
            }

            clientToProxy.close();
        } catch (IOException ignored) { }
        super.run();
    }

    private void makeHttpsConnection(Socket clientSocket, Socket connection) {
        byte[] buffer = new byte[4096];
        int read;
        try {
            do {
                read = clientSocket.getInputStream().read(buffer);
                if (read > 0) {
                    connection.getOutputStream().write(buffer, 0, read);
                    if (clientSocket.getInputStream().available() < 1) {
                        connection.getOutputStream().flush();
                    }
                }
            } while (read >= 0);
        } catch (IOException ignored) { }
    }
}
