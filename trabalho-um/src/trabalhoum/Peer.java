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
        return port.equals(currentCoordinatorPort);
    }

    private boolean hasHigherId() {
        return port.equals(processesPorts.get(processesPorts.size() - 1));
    }

    private boolean hasLowerId() {
        return port.equals(processesPorts.get(0));
    }

    private void startElection() {
        try {
            shouldStartNewElection = false;
            Integer myIndex = processesPorts.indexOf(port);

            Logger.info("Starting Election");
            Logger.debug("My Id: %s", myIndex);
            Logger.debug("Number of Processes: %s", processesPorts.size());

            if (myIndex != -1) {
                for (int i = myIndex + 1; i < processesPorts.size(); i++) {
                    Logger.info("Sending TCP %s message to %s", TCPMessageType.NEW_ELECTION, processesPorts.get(i));

                    TCPClient electionClient = new TCPClient("localhost", processesPorts.get(i), port);
                    electionClient.send(TCPMessageType.NEW_ELECTION);
                }
            }
        } catch (Exception e) {
            Logger.error("Failed sending TCP messages on election start: %s", e.getMessage());
        }
    }

    private Runnable tcpThreadHandler = new Runnable() {
        @Override
        public void run() {
            while (true) {
                TCPMessage message = tcpServer.waitForMessage();

                if (message.type == TCPMessageType.NEW_ELECTION) {
                    Logger.info("Received TCP %s message from %s", message.type, message.senderPort);
                    Logger.info("Sending TCP %s message to %s", TCPMessageType.ELECTION_REPLY, message.senderPort);

                    TCPClient client = new TCPClient("localhost", message.senderPort, port);
                    client.send(TCPMessageType.ELECTION_REPLY);

                    if (shouldStartNewElection) {
                        startElection();
                    }
                } else if (message.type == TCPMessageType.ELECTION_REPLY) {
                    Logger.info("Received TCP %s message from %s", message.type, message.senderPort);
                    electionReplyBuffer.add(message);
                }
            }
        }
    };

    private class SendMulticastHello extends TimerTask {
        public void run() {
            Logger.info("Sending Multicast %s message", MulticastMessageType.COORDINATOR_HELLO);
            multicastHandler.send(MulticastMessageType.COORDINATOR_HELLO);
        }
    }

    public void start() {
        while (true) {
            if (isCoordinator()) {
                if (isSendMulticastHelloTimerActive == false) {
                    Logger.info("Sending Multicast %s message", MulticastMessageType.NEW_COORDINATOR);

                    multicastHandler.send(MulticastMessageType.NEW_COORDINATOR);
                    sendMulticastHelloTimer.scheduleAtFixedRate(new SendMulticastHello(), 1000, 2000);
                    isSendMulticastHelloTimerActive = true;
                } else {
                    MulticastMessage message = multicastHandler.waitForMessage();
                    Logger.debug("Received Multicast %s message", message.type);
                }
            } else if (!inElection) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Logger.info("Coordinator %s is not live, removing reference and preparing to start election",
                                currentCoordinatorPort);

                        processesPorts.removeIf(n -> (n.equals(currentCoordinatorPort)));
                        inElection = true;
                        multicastHandler.restart();
                    }
                };
                timer.schedule(task, 4000);

                Logger.debug("Waiting for Multicast %s message", MulticastMessageType.COORDINATOR_HELLO);
                MulticastMessage message = multicastHandler.waitForMessage();

                try {
                    timer.cancel();
                    task.cancel();
                } catch (Exception e) {
                    Logger.error("Failed stopping timer");
                }

                if (message.type == MulticastMessageType.COORDINATOR_HELLO) {
                    Logger.info("Received Multicast %s message. (Coordinator is live)",
                            MulticastMessageType.COORDINATOR_HELLO);
                }
            } else {
                if (hasHigherId()) {
                    Logger.debug("Process %s with higher id", port);
                } else if (hasLowerId()) {
                    Logger.debug("Process %s with lower id", port);
                }
                Logger.input("Do you want to start a election [('Enter') yes/no ('any')]?");
                String selection = new Scanner(System.in).nextLine();

                shouldStartNewElection = true;
                boolean shouldSkipElectionStart = false;

                if (!selection.isEmpty()) {
                    Logger.debug("Skipping election start messages");
                    shouldSkipElectionStart = true;
                }

                if (!shouldSkipElectionStart) {
                    startElection();
                }

                Timer timer2 = new Timer();
                TimerTask task2 = new TimerTask() {
                    @Override
                    public void run() {
                        Logger.info("No response. Becoming the Coordinator");
                        shouldStartNewElection = false;
                        currentCoordinatorPort = port;
                        isSendMulticastHelloTimerActive = false;
                        inElection = false;
                    }
                };
                timer2.schedule(task2, 7000);

                if (hasHigherId() && !shouldSkipElectionStart) {
                    Logger.info("Process has higher id, becaming coordinator directly");
                    shouldStartNewElection = false;
                    currentCoordinatorPort = port;
                    isSendMulticastHelloTimerActive = false;
                    inElection = false;
                } else {
                    Logger.debug("Waiting for first TCP %s message or Multicast %s message",
                            TCPMessageType.ELECTION_REPLY, MulticastMessageType.NEW_COORDINATOR);

                    Timer timerAux = new Timer();
                    TimerTask taskAux = new TimerTask() {
                        @Override
                        public void run() {
                            MulticastMessage message = multicastHandler.waitForMessage();
                            if (message.type == MulticastMessageType.NEW_COORDINATOR) {
                                Logger.info("Received Multicast %s message", MulticastMessageType.NEW_COORDINATOR);
                                currentCoordinatorPort = message.senderUnicastPort;
                                inElection = false;
                            }
                        }
                    };
                    timerAux.schedule(taskAux, 0);

                    electionReplyBuffer.clear();
                    while (electionReplyBuffer.size() == 0) {
                        // Making sure the loop can me exited
                        System.out.print(Character.MIN_VALUE);
                        if (!this.inElection) {
                            break;
                        }
                    }
                }

                try {
                    timer2.cancel();
                    task2.cancel();
                } catch (Exception e) {
                    Logger.error("Failed stopping timer");
                }

                // Se ainda o coordenador não foi definido
                if (inElection) {
                    Logger.debug("Should not be the coordinator");
                    // Entrou aqui se a mensagem de eleição recebeu pelo menos uma resposta

                    // Inicia outra eleição caso não receba NEW_COORDINATOR
                    Timer timer3 = new Timer();
                    TimerTask task3 = new TimerTask() {
                        @Override
                        public void run() {
                            // convoca outra eleição
                            Logger.info("No Multicast %s message received. Starting a new election",
                                    MulticastMessageType.NEW_COORDINATOR);
                            inElection = true;
                            shouldStartNewElection = true;
                            multicastHandler.restart();
                        }
                    };
                    timer3.schedule(task3, 10000);

                    Logger.debug("Waiting for Multicast %s message", MulticastMessageType.NEW_COORDINATOR);
                    MulticastMessage message = multicastHandler.waitForMessage();

                    try {
                        timer3.cancel();
                        task3.cancel();
                    } catch (Exception e) {
                        Logger.error("Failed stopping timer");
                    }

                    if (message.type == MulticastMessageType.NEW_COORDINATOR) {
                        Logger.info("Received Multicast %s message. New Coordinator become live", message.type);
                        this.currentCoordinatorPort = message.senderUnicastPort;
                        this.inElection = false;
                    }
                }
            }
        }
    }
}
