package com.jerryrat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
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
            String method = tokenizedLine.nextToken();

            if (method.equals("GET") || tokenizedLine.nextToken().equals("POST")) {
                handleRequest(response, tokenizedLine, method);
            }

        } catch (NullPointerException ignored) {
        } catch (FileNotFoundException notFound) {
            sendNotFoundResponse(response);
        } finally {
            connectionSocket.close();
        }
    }

    void handleRequest(DataOutputStream response, StringTokenizer tokenizedLine, String type) throws Exception {
        String fileName;
        FileInputStream fileInputStream;
        String method = type;
        fileName = tokenizedLine.nextToken();

        if (isStatic(fileName)) {
            fileInputStream = new FileInputStream("." + fileName);
            responseHeaderForStatic(response, fileName);
            sendBytes(fileInputStream, response);
        } else if (isDynamic(fileName)) {
            Request URL = new Request(fileName, method);
            FileWriter writer = new FileWriter("./forum/static/responseFile.jsp");
            BufferedWriter printer = new BufferedWriter(writer);
            printer.write(URL.send());
            printer.close();
            fileInputStream = new FileInputStream("./forum/static/responseFile.jsp");
            sendBytes(fileInputStream, response);
        } else throw new FileNotFoundException();
    }

    void sendNotFoundResponse(DataOutputStream response) throws IOException {
        String entityBody = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 22\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                " <h1>404 Not Found </h1>";
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

    void sendBytes(InputStream fileInputStream, DataOutputStream response) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes;

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            response.write(buffer, 0, bytes);
        }

        response.flush();
        response.close();
    }

    void responseHeaderForStatic(DataOutputStream response, String fileName) throws IOException {
        String entityBody = "HTTP/1.1 200 ok\r\n";
        response.writeBytes(entityBody);
        String serverName = "Server:JerryRat - JR \r\n";
        response.writeBytes(serverName);
        response.writeBytes("Date:" + new Date() + "\r\n");
        String length = "Content-Length:" + contentLength(fileName) + " \r\n";
        response.writeBytes(length);
        String contentType = "Content-Type:" + getContentType(fileName) + " \r\n\r\n";
        response.writeBytes(contentType);
    }

    String getContentType(String fileName) {
        String contentType;

        if (fileName.endsWith(".jpg"))
            contentType = "image/jpeg ";
        else if (fileName.endsWith(".html"))
            contentType = "text/html";
        else if (fileName.endsWith(".css"))
            contentType = "text/css ";
        else if (fileName.endsWith(".js"))
            contentType = "application/javascript";
        else
            contentType = "text/html";

        return contentType;
    }

    private int contentLength(String fileName) throws IOException {
        String length = null;
        FileReader fileReader = new FileReader("." + fileName);
        BufferedReader br = new BufferedReader(fileReader);

        while (br.readLine() != null) {
            length += br.readLine();
        }

        return length.length() * 2;
    }
}