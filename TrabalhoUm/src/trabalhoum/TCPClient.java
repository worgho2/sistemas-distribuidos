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
public class TCPClient {
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private Integer senderPort;

    public TCPClient(String hostname, Integer destinationPort, Integer senderPort) {
        try {
            this.senderPort = senderPort;
            socket = new Socket(hostname, destinationPort);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("TCPClient exception: " + e.getMessage());
        }
    }

    public void send(TCPMessageType type) {
        try {
            TCPMessage message = new TCPMessage(type, this.senderPort);
            dataOutputStream.writeUTF(message.toPayload());
        } catch (Exception e) {
            System.out.println("TCPClient.send exception: " + e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("TCPClient.close exception: " + e.getMessage());
        }
    }
}
