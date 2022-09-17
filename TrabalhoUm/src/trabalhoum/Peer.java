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
    private String multicastAddress;
    private Integer multicastPort;

    private List<Integer> processesPorts;
    private Integer port;
    private Integer currentCoordinatorPort;

    private MulticastHandler multicastHandler;

    private Timer sendMulticastHelloTimer;
    private boolean isSendMulticastHelloTimerActive;
    private boolean hasNotifiedNewCoordinator;

    private boolean inElection;

    public Peer(String multicastAddress, Integer multicastPort, List<Integer> processesPorts, Integer port,
            Integer currentCoordinatorPort) {
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.processesPorts = processesPorts;
        this.port = port;
        this.currentCoordinatorPort = currentCoordinatorPort;

        this.multicastHandler = new MulticastHandler(this.multicastAddress, this.multicastPort);

        this.sendMulticastHelloTimer = new Timer();
        this.isSendMulticastHelloTimerActive = false;
        this.hasNotifiedNewCoordinator = false;

        this.inElection = false;
    }

    private boolean isCoordinator() {
        return port == currentCoordinatorPort;
    }

    private class SendMulticastHello extends TimerTask {
        public void run() {
            multicastHandler.send(MulticastMessageType.COORDINATOR_HELLO, "Take it easy, I'm here!");
        }
    }

    public void start() {
        while (true) {

            if (isCoordinator()) {
                if (hasNotifiedNewCoordinator == false) {
                    multicastHandler.send(MulticastMessageType.NEW_COORDINATOR, port.toString());
                    hasNotifiedNewCoordinator = true;
                } else if (isSendMulticastHelloTimerActive == false) {
                    sendMulticastHelloTimer.scheduleAtFixedRate(new SendMulticastHello(), 1000, 2000);
                    isSendMulticastHelloTimerActive = true;
                } else {
                    MulticastMessage message = multicastHandler.waitForMessage();
                    System.out.println(message);
                }
            } else if (!inElection) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        multicastHandler.restart();
                    }
                };
                timer.schedule(task, 4000);

                MulticastMessage message = multicastHandler.waitForMessage();

                try {
                    timer.cancel();
                    task.cancel();
                } catch (Exception e) {
                    System.out.println("Failed canceling timer");
                }

                if (message.type == MulticastMessageType.COORDINATOR_HELLO) {
                    System.out.println(String.format("O coordenador (%s) está ativo!", this.currentCoordinatorPort));
                } else if (message.type == MulticastMessageType.UNKNOWN) {
                    processesPorts.removeIf(n -> (n == currentCoordinatorPort));
                    inElection = true;
                }
            } else {
                if (port == processesPorts.get(processesPorts.size() - 1)) {
                    System.out.println(
                            "This process is the greater id, starting a election will make it autimatically coordinator");
                }
                System.out.println(
                        String.format(
                                "The coordenador (%s) has died. Do you want to start a new election [('Enter') yes/no ('any')]?",
                                currentCoordinatorPort));
                String selection = new Scanner(System.in).nextLine();

                boolean skipElectionStart = false;
                boolean stayOnElection = true;

                if (selection.isEmpty()) {
                    System.out.println("Starting Election");
                } else {
                    System.out.println("Waiting for a new coordinator multicast message or election unicast");
                    skipElectionStart = true;
                }

                while (stayOnElection && !skipElectionStart) {

                    /**
                     * mandar mensagem de eleição para todos os processos
                     * abrir tcp server para escutar mensagens
                     */
                    if (1 == 1) { // mensagem recebida
                        stayOnElection = false;
                    }
                }

                // Aguardando por mensagem de novo coordenador
                Timer timer2 = new Timer();
                TimerTask task2 = new TimerTask() {
                    @Override
                    public void run() {
                        multicastHandler.restart();
                    }
                };
                timer2.schedule(task2, 5000);

                // esperar o multicast

            }
        }
    }
}
