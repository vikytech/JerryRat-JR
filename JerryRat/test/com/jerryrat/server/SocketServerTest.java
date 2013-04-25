package com.jerryrat.server;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SocketServerTest {
    SocketServer mockServer = Mockito.mock(SocketServer.class);

    @Before
    public void setUp() {
        Mockito.doNothing().when(mockServer).run();
        mockServer.run();
    }

    @Test
    public void shouldConnectToTheServer() throws Exception {
        mockServer.server();
        mockServer.isStatic(null);
        mockServer.sendBytes(null, null);
        Mockito.verify(mockServer).run();
        Mockito.verify(mockServer).server();
        Mockito.verify(mockServer).isStatic(null);
        Mockito.verify(mockServer).sendBytes(null, null);
    }

    @Test
    public void shouldConnectToTheServerAndSendNotFoundResponse() throws Exception {
        mockServer.sendNotFoundResponse(null);
        Mockito.verify(mockServer).sendNotFoundResponse(null);
    }
}
