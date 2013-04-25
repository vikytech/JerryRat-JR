package com.jerryrat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketServer implements Runnable {
    private ServerSocket listenSocket;

    public SocketServer(ServerSocket serverSocket) {
        listenSocket = serverSocket;
    }

    public void run() {
        while (true) {
            try {
                server();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void server() throws Exception {
        Socket connectionSocket = listenSocket.accept();
        DataOutputStream response = new DataOutputStream(connectionSocket.getOutputStream());
        BufferedReader request = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        String requestMessageLine = request.readLine();

        try {
            StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
            if (tokenizedLine.nextToken().equals("GET")) {
                handleRequest(response, tokenizedLine);
            }
        } catch (NullPointerException e) {
        } catch (FileNotFoundException notFound) {
            sendNotFoundResponse(response);
        } finally {
            connectionSocket.close();
        }
    }

    void handleRequest(DataOutputStream response, StringTokenizer tokenizedLine) throws Exception {
        String fileName;
        FileInputStream fileInputStream;
        fileName = tokenizedLine.nextToken();
        if (isStatic(fileName)) {
            fileInputStream = new FileInputStream("." + fileName);
            sendBytes(fileInputStream, response);
        } else if (isDynamic(fileName)) {
            Request URL = new Request(fileName);
            URL.send(response);
        } else throw new FileNotFoundException();
    }

    void sendNotFoundResponse(DataOutputStream response) throws IOException {
        String entityBody = "<HTML>" +
                "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                "<BODY><b>Not Found</b></BODY></HTML>";
        response.writeBytes(entityBody);
    }

    boolean isStatic(String fileName) throws Exception {
        ServerCofig serverCofig = new ServerCofig();
        return fileName.startsWith(serverCofig.getContent("root"));
    }

    private boolean isDynamic(String fileName) throws Exception {
        ServerCofig serverCofig = new ServerCofig();
        return fileName.startsWith(serverCofig.getContent("url-pattern"));
    }

    void sendBytes(InputStream fileInputStream, OutputStream response) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            response.write(buffer, 0, bytes);
        }
    }
}