/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalhoum;

import java.util.Scanner;

/**
 *
 * @author otavio
 */
public class Main {
    private static final String multicastAddress = "228.5.6.7";
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
                System.out.println("[Invalid input error] Usage: java -jar TrabalhoUm.jar [processId]");
                System.exit(0);
            }

            Integer processId = Integer.parseInt(args[0]);

            if (processId < 0 || processId > processesPorts.length - 1) {
                System.out.println("[Invalid input error] processId is out of bounds");
                System.exit(0);
            }

            Peer peer = new Peer(
                    multicastAddress,
                    processesPorts,
                    processesPorts[processId],
                    processesPorts[processesPorts.length - 1]);

            boolean isCoordinator = processId == processesPorts.length - 1;

            if (isCoordinator) {
                System.out.println("This process is the Coordinator!");
            }

            System.out.println("ProcessId: " + processId);
            System.out.println("Port: " + processesPorts[processId]);
            System.out.println("");
            System.out.println("Press 'Enter' to start the peer");

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();

            System.out.print("\033[H\033[2J");

            peer.start();
        } catch (NumberFormatException e) {
            System.out.println("[Invalid input error] processId must be a number");
            System.exit(0);
        }

    }

}