/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.cliente.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.Date;

/**
 *
 * @author otavio
 */
public interface ServerInterface extends Remote {
    public PublicKey registerUser(String clientName, ClientInterface clientInterface) throws RemoteException;
    public void createAppointment(String clientName, Appointment appointment) throws RemoteException;
    public void cancelAppointmentOrAlert(String clientName, String appointmentName, Boolean onlyAlert) throws RemoteException;
    public Appointment[] listAppointments(String clientName, Date date) throws RemoteException;
}
