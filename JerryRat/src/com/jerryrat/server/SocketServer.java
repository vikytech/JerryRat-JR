package com.jerryrat.server;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class SocketServer extends Thread {
    private ServerSocket listenSocket;

    public SocketServer(ServerSocket serverSocket) {
        listenSocket = serverSocket;
    }

    private static void sendImageResponse(DataOutputStream outToClient) throws IOException {
        outToClient.writeBytes("Server: JerryRat-JR\r\n");
        outToClient.writeBytes("Content-Type: image\r\n");
    }

    public void server() throws Exception {
        Socket connectionSocket = listenSocket.accept();
        DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
        BufferedReader request = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        String requestMessageLine = request.readLine();
        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
        if (tokenizedLine.nextToken().equals("GET")) {
            String fileName = tokenizedLine.nextToken();
            System.out.println("file name-------->" + fileName);
            sendResponse(response, fileName);
        } else {
            response.writeBytes("Bad request.");
        }
        connectionSocket.close();
    }

    private void sendResponse(DataOutputStream response, String fileName) throws IOException, ParserConfigurationException, SAXException {
        URL remoteUrl = new URL("http://localhost:8080"+fileName);
        System.out.println(remoteUrl);

        ServerCofig serverCofig = new ServerCofig();

        if (fileName.startsWith(serverCofig.getContent("root"))) {

            response.writeBytes("HTTP/1.0 200 Document Follows\r\n");

            if (fileName.endsWith(".jpg") || fileName.endsWith(".gif")) {
                sendImageResponse(response);
            }
            URLConnection remoteConnection = remoteUrl.openConnection();
            remoteConnection.setAllowUserInteraction(true);
            remoteConnection.setDoInput(true);
            remoteConnection.setDoOutput(true);
        }
    }
}