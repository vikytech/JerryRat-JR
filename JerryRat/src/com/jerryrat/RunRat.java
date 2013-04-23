package com.jerryrat;

import com.jerryrat.server.SocketServer;

import java.net.ServerSocket;


public class RunRat {

    public static void main(String[] args) throws Exception {
        SocketServer socketServer = new SocketServer(new ServerSocket(8080));
        socketServer.server();
    }
}
