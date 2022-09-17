/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import java.net.*;
import java.util.*;
import java.io.*;

import trabalhoum.MulticastHandler.MulticastMessage;
import trabalhoum.MulticastHandler.MulticastMessageType;

/**
 *
 * @author otavio
 */
public class Peer {
    String multicastAddress;
    Integer multicastPort;

    Integer[] processesPorts;
    Integer port;
    Integer currentCoordinatorPort;

    MulticastHandler multicastHandler;

    Timer sendMulticastHelloTimer;
    boolean isSendMulticastHelloTimer;

    public Peer(String multicastAddress, Integer multicastPort, Integer[] processesPorts, Integer port,
            Integer currentCoordinatorPort) {
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.processesPorts = processesPorts;
        this.port = port;
        this.currentCoordinatorPort = currentCoordinatorPort;

        multicastHandler = new MulticastHandler(this.multicastAddress, this.multicastPort);

        sendMulticastHelloTimer = new Timer(true);
        isSendMulticastHelloTimer = false;
    }

    class SendMulticastHello extends TimerTask {
        public void run() {
            multicastHandler.send(MulticastMessageType.COORDINATOR_HELO, "Take it easy, I'm here!");
        }
    }

    public void start() {
        while (true) {

            if (port == currentCoordinatorPort) {
                if (isSendMulticastHelloTimer == false) {
                    sendMulticastHelloTimer.scheduleAtFixedRate(new SendMulticastHello(), 0, 2000);
                    isSendMulticastHelloTimer = true;
                } else {
                    MulticastMessage message = multicastHandler.waitForMessage();
                    System.out.println(message);
                }
            } else {
                MulticastMessage message = multicastHandler.waitForMessage();
                System.out.println(message);
            }

        }
    }
}
