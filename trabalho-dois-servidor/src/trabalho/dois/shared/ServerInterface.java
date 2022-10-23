/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.time.*;
import java.util.*;

/**
 *
 * @author otavio
 */
public interface ServerInterface extends Remote {
    public PublicKey registerUser(String clientName, ClientInterface clientInterface) throws RemoteException;
    public void createAppointment(String clientName, Appointment appointment) throws RemoteException;
    public void cancelAppointmentOrReminder(String clientName, String appointmentName, Boolean onlyReminder) throws RemoteException;
    public List<Appointment> listAppointments(String clientName, LocalDate date) throws RemoteException;
}
