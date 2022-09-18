/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

import trabalhoum.MulticastMessage.MulticastMessageType;
import trabalhoum.TCPMessage.TCPMessageType;
import java.util.*;

/**
 *
 * @author otavio
 */
public class Peer {
    private MulticastHandler multicastHandler;
    private Timer sendMulticastHelloTimer;
    private boolean isSendMulticastHelloTimerActive;

    private List<Integer> processesPorts;
    private Integer port;
    private Integer currentCoordinatorPort;

    private boolean shouldStartNewElection;
    private boolean inElection;
    private List<TCPMessage> electionReplyBuffer;

    private TCPServer tcpServer;

    public Peer(String multicastAddress, Integer multicastPort, List<Integer> processesPorts, Integer port,
            Integer currentCoordinatorPort) {

        this.multicastHandler = new MulticastHandler(multicastAddress, multicastPort, port);
        this.sendMulticastHelloTimer = new Timer();
        this.isSendMulticastHelloTimerActive = false;

        this.processesPorts = processesPorts;
        this.port = port;
        this.currentCoordinatorPort = currentCoordinatorPort;

        this.shouldStartNewElection = false;
        this.inElection = false;
        this.electionReplyBuffer = new LinkedList<TCPMessage>();

        tcpServer = new TCPServer(port);
        new Thread(this.tcpThreadHandler).start();
    }

    private boolean isCoordinator() {
        return port == currentCoordinatorPort;
    }

    private boolean hasHigherId() {
        return port == processesPorts.get(processesPorts.size() - 1);
    }

    private void startElection() {
        try {
            System.out.println("Starting Election");
            shouldStartNewElection = false;
            Integer myIndex = processesPorts.indexOf(port);

            System.out.println("My index position is: " + myIndex);
            if (myIndex != -1) {
                for (int i = myIndex + 1; i < processesPorts.size(); i++) {
                    System.out.println("Sending NEW_ELECTION to " + processesPorts.get(i));
                    TCPClient electionClient = new TCPClient("localhost", processesPorts.get(i), port);
                    electionClient.send(TCPMessageType.NEW_ELECTION);
                }
            }
        } catch (Exception e) {
            System.out.println("Peer.startElection exception " + e.getMessage());
        }
    }

    private Runnable tcpThreadHandler = new Runnable() {
        @Override
        public void run() {
            while (true) {
                TCPMessage message = tcpServer.waitForMessage();

                if (message.type == TCPMessageType.NEW_ELECTION) {
                    TCPClient client = new TCPClient("localhost", message.senderPort, port);
                    client.send(TCPMessageType.ELECTION_REPLY);

                    if (shouldStartNewElection) {
                        startElection();
                    }
                } else if (message.type == TCPMessageType.ELECTION_REPLY) {
                    System.out.println(String.format("Received %s from %s", message.type, message.senderPort));
                    electionReplyBuffer.add(message);
                }
            }
        }
    };

    private class SendMulticastHello extends TimerTask {
        public void run() {
            multicastHandler.send(MulticastMessageType.COORDINATOR_HELLO);
        }
    }

    public void start() {
        while (true) {
            if (isCoordinator()) {
                if (isSendMulticastHelloTimerActive == false) {
                    multicastHandler.send(MulticastMessageType.NEW_COORDINATOR);
                    sendMulticastHelloTimer.scheduleAtFixedRate(new SendMulticastHello(), 1000, 2000);
                    isSendMulticastHelloTimerActive = true;
                } else {
                    MulticastMessage message = multicastHandler.waitForMessage();
                    System.out.println("Received multicast " + message.type);
                }
            } else if (!inElection) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        multicastHandler.restart();
                        processesPorts.removeIf(n -> (n == currentCoordinatorPort));
                        inElection = true;
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
                    System.out.println(String.format("O coordenador (%s) está ativo!", message.senderUnicastPort));
                }
            } else {
                if (hasHigherId()) {
                    System.out.println(
                            "This process has the higher id, starting a election will make it autimatically coordinator");
                }
                System.out.println(
                        String.format(
                                "The coordenador (%s) has died. Do you want to start a new election [('Enter') yes/no ('any')]?",
                                currentCoordinatorPort));
                String selection = new Scanner(System.in).nextLine();

                shouldStartNewElection = true;
                boolean shouldSkipElectionStart = false;

                if (!selection.isEmpty()) {
                    System.out.println("Skipping election start");
                    shouldSkipElectionStart = true;
                }

                if (shouldSkipElectionStart) {
                    startElection();
                }

                Timer timer2 = new Timer();
                TimerTask task2 = new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("No ELECTION_REPLY received. I should be the coordinator");
                        shouldStartNewElection = false;
                        currentCoordinatorPort = port;
                        isSendMulticastHelloTimerActive = false;
                        inElection = false;
                    }
                };
                timer2.schedule(task2, 5000);

                electionReplyBuffer.clear();
                while (electionReplyBuffer.size() == 0) {
                    /**
                     * Making sure the loop can me exited
                     */
                    System.out.print(Character.MIN_VALUE);
                    if (!this.inElection) {
                        System.out.println("Election has Ended");
                        break;
                    }
                }

                try {
                    timer2.cancel();
                    task2.cancel();
                } catch (Exception e) {
                    System.out.println("Failed canceling timer");
                }

                // Se ainda o coordenador não foi definido
                if (inElection) {
                    System.out.println("Waiting for new coordinator message");
                    // Entrou aqui se a mensagem de eleição recebeu pelo menos uma resposta

                    // Inicia outra eleição caso não receba NEW_COORDINATOR
                    Timer timer3 = new Timer();
                    TimerTask task3 = new TimerTask() {
                        @Override
                        public void run() {
                            // convoca outra eleição
                            inElection = true;
                            shouldStartNewElection = true;
                            multicastHandler.restart();
                        }
                    };
                    timer3.schedule(task3, 5000);

                    MulticastMessage message = multicastHandler.waitForMessage();

                    try {
                        timer3.cancel();
                        task3.cancel();
                    } catch (Exception e) {
                        System.out.println("Failed canceling timer");
                    }

                    if (message.type == MulticastMessageType.NEW_COORDINATOR) {
                        System.out.println(
                                String.format("The new coordinator (%s) is active", message.senderUnicastPort));
                        currentCoordinatorPort = message.senderUnicastPort;
                        inElection = false;
                    }
                }
            }
        }
    }
}
