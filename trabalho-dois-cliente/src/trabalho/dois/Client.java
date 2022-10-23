/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import trabalho.dois.shared.Appointment;
import trabalho.dois.shared.ClientInterface;
import trabalho.dois.shared.ServerInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;

/**
 *
 * @author otavio
 */
public class Client extends UnicastRemoteObject implements ClientInterface {
    private final String name;
    private final ServerInterface server;
    private final Security security;
    private PublicKey serverPublicKey = null;
    
    public Client(String name, ServerInterface server) throws RemoteException {
        this.name = name;
        this.server = server;
        this.security = new Security(name);
    }
    
    public void initialize() throws RemoteException {
       this.serverPublicKey = this.server.registerUser(this.name, this);
       Logger.info("Registering on server", this.name);
       
       // TODO: start interface handler
    }

    @Override
    public void onAppointmentNotification(String message, Appointment appointment) throws RemoteException {
        Logger.info("Server Message: %s, Appointment:(name=%s, owner=%s, date=%s, attendees=%s)", message, appointment.name, appointment.owner, appointment.date, appointment.attendees.keySet());
    }

    @Override
    public Appointment.InviteResponse onAppointmentInvite(Appointment.Invite invite, byte[] signature) throws RemoteException {
        if (!this.security.isValidSignature(this.serverPublicKey, signature)) {
            return null;
        }
        
        Appointment.InviteResponse response = invite.createResponse(true, Appointment.Reminder.ON_TIME);
        return response;
    }
}
