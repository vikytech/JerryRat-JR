package com.jerryrat.server;

import org.junit.Test;
import org.mockito.Mockito;

import java.net.ServerSocket;

public class SocketServerTest {

    @Test
    public void shouldConnectToTheServer() throws Exception {
        SocketServer mockServer = Mockito.mock(SocketServer.class);
        Mockito.doNothing().when(mockServer).run();
        mockServer.run();
        Mockito.verify(mockServer).run();
    }
    @Test
    public void shouldNotConnectToTheServer() throws Exception {
        SocketServer server = new SocketServer(new ServerSocket(1234));
        server.run();

    }


}
