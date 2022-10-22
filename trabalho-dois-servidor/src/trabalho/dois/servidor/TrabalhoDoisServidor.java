/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trabalho.dois.servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author otavio
 */
public class TrabalhoDoisServidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Registry nameService = LocateRegistry.createRegistry(1099);
            Server server = new Server();
            nameService.rebind("trabalho-dois", server);
        } catch (RemoteException e) {
            Logger.error("Server remote exception %s", e.getMessage());
            System.exit(0);
        }
    }
    
}
