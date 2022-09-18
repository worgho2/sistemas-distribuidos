/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import trabalhoum.TCPMessage.TCPMessageType;
import java.net.*;
import java.io.*;

/**
 *
 * @author otavio
 */
public class TCPServer {
    private ServerSocket serverSocket;

    public TCPServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("TCPServer exception: " + e.getMessage());
        }
    }

    public TCPMessage waitForMessage() {
        try {
            Socket connectionSocket = serverSocket.accept();
            DataInputStream inputStream = new DataInputStream(connectionSocket.getInputStream());
            String payload = inputStream.readUTF();
            connectionSocket.close();
            return new TCPMessage(payload);
        } catch (Exception e) {
            return new TCPMessage(TCPMessageType.UNKNOWN, 0);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("TCPServer.close exception: " + e.getMessage());
        }
    }
}
