/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import trabalhoum.MulticastMessage.MulticastMessageType;
import java.net.*;
import java.io.*;

/**
 *
 * @author otavio
 */
public class MulticastHandler {
    private Integer port;
    private Integer senderUnicastPort;
    private InetAddress inetAddress;
    private MulticastSocket multicastSocket;

    public MulticastHandler(String address, Integer port, Integer senderUnicastPort) {
        try {
            this.port = port;
            this.senderUnicastPort = senderUnicastPort;
            inetAddress = InetAddress.getByName(address);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(inetAddress);
        } catch (Exception e) {
            Logger.error("Failed starting MulticastHandler: %s", e.getMessage());
        }
    }

    public void send(MulticastMessageType type) {
        try {
            MulticastMessage message = new MulticastMessage(type, this.senderUnicastPort);
            byte[] buffer = message.toPayload().getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, this.inetAddress, this.port);
            multicastSocket.send(datagramPacket);
        } catch (IOException e) {
            Logger.error("Failed sending Multicast %s message", e.getMessage());
        }
    }

    public MulticastMessage waitForMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(datagramPacket);
            String payload = new String(datagramPacket.getData());
            return new MulticastMessage(payload);
        } catch (Exception e) {
            return new MulticastMessage(MulticastMessageType.UNKNOWN, 0);
        }
    }

    public void restart() {
        try {
            close();
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(inetAddress);
        } catch (Exception e) {
            Logger.error("Failed restarting MulticastHandler", e.getMessage());
        }

    }

    public void close() {
        try {
            multicastSocket.leaveGroup(inetAddress);
        } catch (Exception e) {
            Logger.error("Failed stopping MulticastHandler", e.getMessage());
        }

        try {
            multicastSocket.close();
        } catch (Exception e) {
            Logger.error("Failed stopping MulticastHandler", e.getMessage());
        }
    }
}
