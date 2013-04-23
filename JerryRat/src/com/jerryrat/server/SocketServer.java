package com.jerryrat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketServer extends Thread {
    private ServerSocket listenSocket;

    public SocketServer(ServerSocket serverSocket) {
        listenSocket = serverSocket;
    }

    public void server() throws Exception {
        Socket connectionSocket = listenSocket.accept();

        FileInputStream fileInputStream;
        DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
        BufferedReader request = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        String requestMessageLine = request.readLine();
        StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);

        if (tokenizedLine.nextToken().equals("GET")) {
            String fileName = tokenizedLine.nextToken();
            fileName = "." + fileName;
            try {
                fileInputStream = new FileInputStream(fileName);
                sendResponse(fileInputStream, response, fileName);
            } catch (FileNotFoundException notFound) {
                sendNotFoundResponse(response);
            } finally {
                connectionSocket.close();
            }
        }
    }

    private void sendNotFoundResponse(DataOutputStream response) throws IOException {
        String statusLine = "HTTP/1.0 404 Not Found\r\n";
        String contentTypeLine = "Content-type: text/html\r\n";
        String message = "<HTML>" +
                "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                "<BODY><b>Not Found<b></BODY></HTML>";

        response.writeBytes(statusLine);
        response.writeBytes(contentTypeLine);
        response.writeBytes(message);
    }

    private void sendResponse(FileInputStream request, DataOutputStream response, String fileName) throws Exception {
        ServerCofig serverCofig = new ServerCofig();
        if (fileName.startsWith(serverCofig.getContent("root"))) {
            String statusLine = "HTTP/1.0 200 OK\r\n";
            response.writeBytes(statusLine);

            if (fileName.endsWith(".jpg") || fileName.endsWith(".gif")) {
                response.writeBytes("Content-Type: image\r\n");
                response.writeBytes("Server: JerryRat-JR\r\n");
            }
        }
        sendBytes(request, response);
    }

    private void sendBytes(FileInputStream fileInputStream, DataOutputStream response) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            response.write(buffer, 0, bytes);
        }
    }

}