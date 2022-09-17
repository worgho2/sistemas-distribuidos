/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import java.net.*;
import java.io.*;

/**
 *
 * @author otavio
 */
public class MulticastHandler {
    private Integer port;
    private InetAddress inetAddress;
    private MulticastSocket multicastSocket;

    public MulticastHandler(String address, Integer port) {
        try {
            this.port = port;
            inetAddress = InetAddress.getByName(address);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(inetAddress);
        } catch (Exception e) {
            System.out.println("MulticastHandler exception: " + e.getMessage());
        }
    }

    public enum MulticastMessageType {
        NEW_COORDINATOR,
        COORDINATOR_HELLO,
        UNKNOWN
    }

    public class MulticastMessage {
        MulticastMessageType type;
        Integer senderPort;
        String content;

        public MulticastMessage(MulticastMessageType type, Integer senderPort, String content) {
            this.type = type;
            this.senderPort = senderPort;
            this.content = content;
        }

        @Override
        public String toString() {
            return String.format("MulticastMessage (port=%s type=%s content=%s)", senderPort, type, content);
        }
    }

    public void send(MulticastMessageType type, String content) {
        try {
            String payload = new String(type.toString() + "@" + content);
            byte[] buffer = payload.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
            multicastSocket.send(datagramPacket);
        } catch (IOException e) {
            System.out.println("MulticastHandler.send exception: " + e.getMessage());
        }
    }

    public MulticastMessage waitForMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(datagramPacket);
            String payload = new String(datagramPacket.getData());
            String[] components = payload.split("@");

            MulticastMessageType type;
            String content;

            if (components.length != 2) {
                type = MulticastMessageType.UNKNOWN;
                content = payload;
            } else {
                content = components[1];
                try {
                    type = MulticastMessageType.valueOf(components[0]);
                } catch (Exception e) {
                    type = MulticastMessageType.UNKNOWN;
                }
            }

            return new MulticastMessage(type, datagramPacket.getPort(), content);
        } catch (IOException e) {
            if (e.getMessage() != "Socket closed") {
                System.out.println("MulticastHandler.waitForMessage exception: " + e.getMessage());
            }

            return new MulticastMessage(MulticastMessageType.UNKNOWN, 0, "");
        }
    }

    public void restart() {
        try {
            close();
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(inetAddress);
        } catch (Exception e) {
            System.out.println("Falha ");
        }

    }

    public void close() {
        try {
            multicastSocket.leaveGroup(inetAddress);
        } catch (Exception e) {
            System.out.println("MulticastHandler.close exception: " + e.getMessage());
        }

        try {
            multicastSocket.close();
        } catch (Exception e) {
            System.out.println("MulticastHandler.close exception: " + e.getMessage());
        }
    }
}
