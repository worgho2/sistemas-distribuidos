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

    public void send(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
            multicastSocket.send(datagramPacket);
        } catch (IOException e) {
            System.out.println("MulticastHandler.send exception: " + e.getMessage());
        }
    }

    public String waitForMessage() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(datagramPacket);
            String message = datagramPacket.getData().toString();

            return message;
        } catch (IOException e) {
            System.out.println("MulticastHandler.waitForMessage exception: " + e.getMessage());
            return "";
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
