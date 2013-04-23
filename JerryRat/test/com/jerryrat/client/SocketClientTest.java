package com.jerryrat.client;


import com.jerryrat.server.SocketServer;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertThat;

public class SocketClientTest {
    private Socket client;
    private SocketServer socketServer;

    @Before
    public void setUp() throws IOException {
        socketServer = new SocketServer(new ServerSocket(8080));
        socketServer.run();
    }

    @Test
    public void shouldEstablishConnection() throws IOException {
        SocketClient serverClient = new SocketClient("localhost", 8080);
        assertThat(serverClient.establishConnection().toString(), IsEqual.equalTo("localhost/127.0.0.1:8080"));
    }
}
