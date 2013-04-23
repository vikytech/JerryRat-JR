package com.jerryrat.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClient {
    private final String serverAddress;
    private final int port;
    private Socket client;

    public SocketClient(String serverAddress, int port) {

        this.serverAddress = serverAddress;
        this.port = port;
    }

    public SocketAddress establishConnection() throws IOException {
        System.out.println("Connecting to " + serverAddress + " on port " + port);
        client = new Socket(serverAddress, port);
        return client.getRemoteSocketAddress();
    }

}
