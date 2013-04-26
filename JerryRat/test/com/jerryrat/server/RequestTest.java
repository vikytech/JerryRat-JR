package com.jerryrat.server;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.DataOutputStream;

public class RequestTest {

    Request request = new Request("/forum/login", "GET");

    @Test
    public void shouldSendRequestToAnotherServer() throws Exception {
        DataOutputStream dataOutputStream = Mockito.mock(DataOutputStream.class);
        request.send();
    }
}
