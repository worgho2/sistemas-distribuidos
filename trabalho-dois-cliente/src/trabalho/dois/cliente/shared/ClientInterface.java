/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.cliente.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author otavio
 */
public interface ClientInterface extends Remote {
    public void onAppointmentNotification(String message, Appointment appointment) throws RemoteException;
    public Appointment.InviteResponse onAppointmentInvite(Appointment.Invite invite, byte[] signature) throws RemoteException;
}
