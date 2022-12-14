/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import trabalho.dois.shared.Appointment;
import trabalho.dois.shared.ClientInterface;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import trabalho.dois.shared.Appointment.*;

/**
 *
 * @author otavio
 */
public class CalendarManager {
    private final Security security;
    private final ReminderScheduler reminderScheduler;
    private final HashMap<String, ClientInterface> clientNamesClientInterfaces;
    private final LinkedList<Appointment> appointments;
    
    public CalendarManager(Security security) {
        this.reminderScheduler = new ReminderScheduler();
        this.security = security;
        this.clientNamesClientInterfaces = new HashMap<>();
        this.appointments = new LinkedList<>();
    }
    
    public void registerClient(String clientName, ClientInterface reference) {
        this.clientNamesClientInterfaces.put(clientName, reference);
        Logger.info("Client (%s) registered", clientName);
    }
    
    public void createAppointment(String clientName, Appointment appointment) {
        this.appointments.push(appointment);
        Logger.info("Client (%s) created appointment (%s)", clientName, appointment.print());
        
        if (appointment.reminder != Appointment.Reminder.DISABLED && clientNamesClientInterfaces.containsKey(clientName)) {
            this.reminderScheduler.schedule(appointment.reminder, clientName, clientNamesClientInterfaces.get(clientName), appointment);
        }
        
        for (String attendeeName : appointment.attendees.keySet()) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (clientNamesClientInterfaces.containsKey(attendeeName)) {
                            ClientInterface cli = clientNamesClientInterfaces.get(attendeeName);
                            Invite invite = appointment.getInvite(attendeeName);
                            byte[] signature = security.generateSignature(invite.toSignature(attendeeName));
                            Logger.info("Sending appointment (%s) invite to attendee (%s)", appointment.name, attendeeName);
                            InviteResponse response = cli.onAppointmentInvite(invite, signature);

                            for (Appointment app : appointments) {
                                if (app.name.equals(appointment.name)) {
                                    if (response.accepted) {
                                        app.attendees.put(attendeeName, response.reminder);
                                        Logger.info("Attendee (%s) accepted appointment (%s)", attendeeName, appointment.name);
                                        
                                        reminderScheduler.schedule(response.reminder, attendeeName, cli, app);
                                    } else {
                                        app.attendees.remove(attendeeName);
                                        Logger.info("Attendee (%s) rejected appointment (%s)", attendeeName, appointment.name);
                                    }
                                }
                            }
                        }
                    } catch (RemoteException e) {
                        Logger.error("CalendarManager.createAppointment exception %s", e.getMessage());
                    }
                }
            };
            timer.schedule(timerTask, 0);
        }
    }
    
    public void cancelAppointment(String clientName, String appointmentName) {
        ListIterator<Appointment> it = this.appointments.listIterator();
        
        while (it.hasNext()) {
            Appointment currentApp = it.next();
            
            if (currentApp.name.equals(appointmentName)) {
                if (currentApp.owner.equals(clientName)) {
                    int index = this.appointments.indexOf(currentApp);
                    Appointment removedAppointment = this.appointments.remove(index);
                    Logger.info("Client (%s) canceled appointment (%s)", clientName, appointmentName);
                    this.reminderScheduler.cancel(clientName, currentApp);
                    
                    for (String attendeeName : currentApp.attendees.keySet()) {
                        this.reminderScheduler.cancel(attendeeName, removedAppointment);
                        
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    if (clientNamesClientInterfaces.containsKey(attendeeName)) {
                                        ClientInterface cli = clientNamesClientInterfaces.get(attendeeName);
                                        Logger.info("Sending appointment (%s) canceled notification to attendee (%s)", appointmentName, attendeeName);
                                        cli.onAppointmentNotification("Appointment cancelled", currentApp);
                                    }
                                } catch (RemoteException e) {
                                    Logger.error("CalendarManager.cancelAppointment exception %s", e.getMessage());
                                }
                            }
                        };
                        timer.schedule(timerTask, 0);
                    }

                    return;
                } else if (currentApp.attendees.containsKey(clientName)) {
                    currentApp.attendees.remove(clientName);
                    Logger.info("Attendee (%s) exited appointment (%s)", clientName, appointmentName);
                    this.reminderScheduler.cancel(clientName, currentApp);
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
                    Logger.info("Client (%s) disabled appointment (%s) reminder", clientName, appointmentName);
                    this.reminderScheduler.cancel(clientName, currentApp);
                    return;
                } else if (currentApp.attendees.containsKey(clientName)) {
                    currentApp.attendees.put(clientName, Appointment.Reminder.DISABLED);
                    Logger.info("Attendee (%s) disabled appointment (%s) reminder", clientName, appointmentName);
                    this.reminderScheduler.cancel(clientName, currentApp);
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
    
