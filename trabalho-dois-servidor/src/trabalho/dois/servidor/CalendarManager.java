/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.servidor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import trabalho.dois.servidor.shared.*;

/**
 *
 * @author otavio
 */
public class CalendarManager {
    private Security security;
    private final HashMap<String, ClientInterface> clientNamesClientInterfaces;
    private final LinkedList<Appointment> appointments;
    
    public CalendarManager(Security security) {
        this.security = security;
        this.clientNamesClientInterfaces = new HashMap<>();
        this.appointments = new LinkedList<>();
    }
    
    public void registerClient(String clientName, ClientInterface reference) {
        this.clientNamesClientInterfaces.put(clientName, reference);
    }
    
    public void createAppointment(String clientName, Appointment appointment) {
        this.appointments.push(appointment);
        
        if (appointment.reminder != Appointment.Reminder.DISABLED) {
             // TODO: create owner reminder
        }
        
        if (!appointment.attendees.isEmpty()) {
            // TODO: send confirmation message to all clients
            // If a success response was received, set the timer if enabled and the reminder field
            // Else remove the user form the appointment
        }
    }
    
    public void cancelAppointment(String clientName, String appointmentName) {
        ListIterator<Appointment> it = this.appointments.listIterator();
        
        while (it.hasNext()) {
            Appointment currentApp = it.next();
            
            if (currentApp.name.equals(appointmentName)) {
                if (currentApp.owner.equals(clientName)) {
                    Appointment removedAppointment = this.appointments.remove(it.nextIndex());
                    // TODO: notify all attendees the appointment was cancelled
                    // TODO: cancel all reminders;

                    return;
                } else if (currentApp.attendees.containsKey(clientName)) {
                    currentApp.attendees.remove(clientName);
                    // TODO: cancel client remider;

                    return;
                }
            }
        }
    }
    
    public void disableAppointmentReminder(String clientName, String appointmentName) {
        ListIterator<Appointment> it = this.appointments.listIterator();
        
        while (it.hasNext()) {
            Appointment currentApp = it.next();
            
            if (currentApp.name.equals(appointmentName)) {
                if (currentApp.owner.equals(clientName)) {
                    currentApp.reminder = Appointment.Reminder.DISABLED;
                    // TODO: Cancel owner reminder;

                    return;
                } else if (currentApp.attendees.containsKey(clientName)) {
                    currentApp.attendees.put(clientName, Appointment.Reminder.DISABLED);
                    // TODO: Cancel atendee reminder

                    return;
                }
            }
        }
    }
    
    public List<Appointment> listClientAppointments(String clientName, LocalDate date) {
        return this.appointments
            .stream()
            .filter(appointment -> 
                (appointment.attendees.containsKey(clientName))
                || (appointment.owner.equals(clientName))
            )
            .filter((appointment) ->
                (date.getYear() == appointment.date.getYear())
                && (date.getMonthValue() == appointment.date.getMonthValue())
                && (date.getDayOfMonth() == appointment.date.getDayOfMonth())
            )
            .collect(Collectors.toList());
    }
}
    
