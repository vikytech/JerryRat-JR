package com.jerryrat.server;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.DataOutputStream;
import java.net.Socket;

public class RequestTest {

    Request request = new Request("/forum/login");

    @Test
    public void shouldSendRequestToAnotherServer() throws Exception {
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);
        request.send(dataOutputStream);
    }
}
