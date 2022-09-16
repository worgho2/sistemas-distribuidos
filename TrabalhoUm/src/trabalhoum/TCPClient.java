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
public class TCPClient {
    private DataOutputStream dataOutputStream;
    private Socket socket;

    public TCPClient(String hostname, int serverPort) {
        try {
            socket = new Socket(hostname, serverPort);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("TCPClient exception: " + e.getMessage());
        }
    }

    public void send(String message) {
        try {
            dataOutputStream.writeUTF(message);
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
