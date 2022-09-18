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
    private Integer destinationPort;

    public TCPClient(String hostname, Integer destinationPort, Integer senderPort) {
        try {
            this.destinationPort = destinationPort;
            this.senderPort = senderPort;
            socket = new Socket(hostname, destinationPort);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Logger.error("Failed starting TCPClient: %s", e.getMessage());
        }
    }

    public void send(TCPMessageType type) {
        try {
            TCPMessage message = new TCPMessage(type, this.senderPort);
            dataOutputStream.writeUTF(message.toPayload());
        } catch (Exception e) {
            Logger.error("Failed sending TCP %s message to %s", type, destinationPort);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            Logger.error("Failed closing TCPClient: %s ", e.getMessage());
        }
    }
}
