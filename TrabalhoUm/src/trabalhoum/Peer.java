/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import java.net.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author otavio
 */
public class Peer {
    Integer[] processesPorts;
    Integer port;
    Integer currentCoordinator;

    public Peer(String multicastAddress, Integer[] processesPorts, Integer port, Integer currentCoordinator) {

        this.processesPorts = processesPorts;
        this.port = port;
        this.currentCoordinator = currentCoordinator;
    }

    private void sendMulticastHello() {

    }

    public void start() {

    }
}
