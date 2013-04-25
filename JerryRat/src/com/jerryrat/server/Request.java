package com.jerryrat.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Request {
    private String connectAddress;

    public Request(String connectAddress) {
        this.connectAddress = connectAddress;
    }

    public void send(DataOutputStream response) throws Exception {
        Socket socket = null;
        sendData(getNewSocket());
        writeContent(response, socket);
    }

    private Socket getNewSocket() throws Exception {
        Socket socket = null;
        try {
            ServerCofig serverCofig = new ServerCofig();
            String[] split = serverCofig.getContent("upstream-url").split(":");
            InetAddress addr = InetAddress.getByName(split[0]);
            int port = Integer.parseInt(split[1]);
            return new Socket(addr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    void writeContent(DataOutputStream response, Socket socket) {
        BufferedReader rd;
        try {
            String str;
            rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((str = rd.readLine()) != null) {
                System.out.println(str);
                response.writeBytes(str + "\n");
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData(Socket socket) {
        try {
            assert socket != null;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            wr.write("GET " + connectAddress + " HTTP/1.0");
            wr.newLine();
            wr.write("\nUser-Agent: Wget/1.9.1");
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}