/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.Date;
import trabalho.dois.servidor.shared.*;

/**
 *
 * @author otavio
 */
public class Server extends UnicastRemoteObject implements ServerInterface {

    @Override    
    public PublicKey registerUser(String clientName, ClientInterface clientInterface) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createAppointment(String clientName, Appointment appointment) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancelAppointmentOrAlert(String clientName, String appointmentName, Boolean onlyAlert) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Appointment[] listAppointments(String clientName, Date date) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
