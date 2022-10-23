/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import trabalho.dois.shared.ServerInterface;
import trabalho.dois.shared.Appointment;
import trabalho.dois.shared.ClientInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author otavio
 */
public class Server extends UnicastRemoteObject implements ServerInterface {
    private final Security security;
    private final CalendarManager calendar;
    
    public Server() throws RemoteException {
        super();
        
        this.security = new Security();
        this.calendar = new CalendarManager(this.security);
    }
    
    @Override
    public PublicKey registerUser(String clientName, ClientInterface clientInterface) throws RemoteException {
        calendar.registerClient(clientName, clientInterface);
        Logger.info("Client registered: (%s)", clientName);
        return security.getPublicKey();
    }

    @Override
    public void createAppointment(String clientName, Appointment appointment) throws RemoteException {
        this.calendar.createAppointment(clientName, appointment);
    }

    @Override
    public void cancelAppointmentOrReminder(String clientName, String appointmentName, Boolean onlyReminder) throws RemoteException {
        if (onlyReminder) {
           this.calendar.disableAppointmentReminder(clientName, appointmentName);
        } else {
            this.calendar.cancelAppointment(clientName, appointmentName); 
        }
    }

    @Override
    public List<Appointment> listAppointments(String clientName, LocalDate date) throws RemoteException {
        return this.calendar.listClientAppointments(clientName, date);
    }
}
