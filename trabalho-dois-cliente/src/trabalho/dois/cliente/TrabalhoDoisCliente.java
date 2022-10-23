/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalho.dois.cliente;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import trabalho.dois.cliente.shared.ServerInterface;

/**
 *
 * @author otavio
 */
public class TrabalhoDoisCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                Logger.error("Invalid input. Try: java -jar <executable>.jar [client_name]");
                System.exit(0);
            }

            String clientName = args[0];
            Registry nameService = LocateRegistry.getRegistry();
            ServerInterface server = (ServerInterface) nameService.lookup("trabalho-dois");
            Client client = new Client(clientName, server);
            client.initialize();
        } catch (NotBoundException | RemoteException e) {
            Logger.error("Main exception: %s", e.getMessage());
        }
    }
    
}
