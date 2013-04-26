package com.jerryrat.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Request {
    String connectAddress;
    String method;

    public Request(String connectAddress, String method) {
        this.connectAddress = connectAddress;
        this.method = method;
    }

    public String send() throws Exception {
        Socket socket = null;
        socket = getNewSocket(socket);
        sendData(socket);
        return writeContent(socket);
    }

    private Socket getNewSocket(Socket socket) throws Exception {
        try {
            ServerCofig serverCofig = new ServerCofig();
            String[] split = serverCofig.getContent("upstream-url").split(":");
            InetAddress addr = InetAddress.getByName(split[0]);
            int port = Integer.parseInt(split[1]);
            socket = new Socket(addr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    String writeContent(Socket socket) {
        BufferedReader rd;
        String str = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((str = rd.readLine()) != null) {
                System.out.println(str + "\n");
                stringBuilder.append(str).append("\n");
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ActualString >>>>>" + stringBuilder.append("\n").toString());
        return stringBuilder.append("\n").toString();
    }

    void sendData(Socket socket) {
        try {
            assert socket != null;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            wr.write(method + " " + connectAddress + " HTTP/1.0");
            wr.newLine();
            wr.write("\nUser-Agent: Wget/1.9.1");
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}