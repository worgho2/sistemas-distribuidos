/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalhoum;

import java.util.*;

/**
 *
 * @author otavio
 */
public class Main {
    private static final String multicastAddress = "228.5.6.7";
    private static final Integer multicastPort = 6789;
    private static final Integer[] processesPorts = {
            5101,
            5102,
            5103,
            5104
    };

    /**
     * @param args [processId]
     */
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                Logger.error("Invalid input. Try: java -jar TrabalhoUm.jar [processId]");
                System.exit(0);
            }

            Integer processId = Integer.parseInt(args[0]);

            if (processId < 0 || processId > processesPorts.length - 1) {
                Logger.error("Invalid input. 'processId' is out of bounds. (Min: 0, Max: %s)",
                        processesPorts.length - 1);
                System.exit(0);
            }

            if (processId == processesPorts.length - 1) {
                Logger.debug("Process with higher id. Will be the first coordinator");
            } else if (processId == 0) {
                Logger.debug("Process with lower id");
            }

            Logger.info("ProcessId: %s", processId);
            Logger.info("Port: %s", processesPorts[processId]);
            Logger.input("Press 'Enter' to start the peer");
            new Scanner(System.in).nextLine();

            Peer peer = new Peer(
                    multicastAddress,
                    multicastPort,
                    new LinkedList<Integer>(Arrays.asList(processesPorts)),
                    processesPorts[processId],
                    processesPorts[processesPorts.length - 1]);

            peer.start();
        } catch (Exception e) {
            Logger.error("Invalid input. 'processId' must be a number");
            System.exit(0);
        }

    }

}