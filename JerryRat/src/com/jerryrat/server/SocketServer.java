package com.jerryrat.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.StringTokenizer;

public class SocketServer extends Thread {
    public void server() throws Exception {
        String requestMessageLine;
        String fileName;
        URL remoteUrl;
        URLConnection remoteConnection;

        ServerSocket listenSocket = new ServerSocket(8080);
        Socket connectionSocket = listenSocket.accept();


        DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());

        BufferedReader request = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


        requestMessageLine = request.readLine();

        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

        if (tokenizedLine.nextToken().equals("GET")) {
            fileName = tokenizedLine.nextToken();
            remoteUrl = new URL("file:///home/vikytech/Git" +
                    "" + fileName);

            System.out.println(fileName);
            System.out.println(remoteUrl);

            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
                response.writeBytes("HTTP/1.0 200 Document Follows\r\n");

                if (fileName.endsWith(".jpg") || fileName.endsWith(".gif")) {
                    sendImageResponse(response);
                    remoteUrl.openConnection();
                }

                remoteConnection = remoteUrl.openConnection();
                remoteConnection.setAllowUserInteraction(true);
                remoteConnection.setDoInput(true);
                remoteConnection.setDoOutput(true);
            }
        } else {
            response.writeBytes("Bad request.");
        }
        connectionSocket.close();
    }

    private static void sendImageResponse(DataOutputStream outToClient) throws IOException {
        outToClient.writeBytes("Server: JerryRat-JR\r\n");
        outToClient.writeBytes("Content-Type: image\r\n");
    }
}