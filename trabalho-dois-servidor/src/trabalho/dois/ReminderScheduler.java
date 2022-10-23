/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import trabalho.dois.shared.Appointment;
import trabalho.dois.shared.ClientInterface;
import java.rmi.RemoteException;
import java.time.*;
import java.util.*;

/**
 *
 * @author otavio
 */
public class ReminderScheduler {
    private final HashMap<String, Timer> reminderTimers;
    
    public ReminderScheduler() {
        this.reminderTimers = new HashMap<>();
    }
    
    private String getReminderKey(String clientName, Appointment appointment) {
        return clientName + "-" + appointment.name;
    }
    
    private String getReminderMessage(Appointment.Reminder reminder) {
        switch (reminder) {
            case TEN_MINUTES_BEFORE:
                return "Ten minutes before the event reminder";

            case FIVE_MINUTES_BEFORE:
                return "Five minutes before the event reminder";

            case ON_TIME:
                return "Event started reminder";
        }
        
        return "Unknown";
    }
    
    public void schedule(Appointment.Reminder reminder, String clientName, ClientInterface clientInterface, Appointment appointment) {
        if (reminder == Appointment.Reminder.DISABLED || reminder == Appointment.Reminder.PENDING) {
            return;
        }
        
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String message = getReminderMessage(reminder);
                    Logger.info("Sending appointment (%s) notification (%s) to client (%s)", appointment.name, reminder, clientName);
                    clientInterface.onAppointmentNotification(message, appointment);
                } catch (RemoteException e) {
                    Logger.error("ReminderScheduler.schedule exception", e.getMessage());
                }
            }
        };
        
        switch (reminder) {
            case TEN_MINUTES_BEFORE:
                Date date1 = Date.from(appointment.date.minusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
                timer.schedule(timerTask, date1);
                Logger.info("Scheduled appointment (%s) notification (%s) to client (%s) at (%s)", appointment.name, reminder, clientName, date1);
                break;

            case FIVE_MINUTES_BEFORE:
                Date date2 = Date.from(appointment.date.minusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
                timer.schedule(timerTask, date2);
                Logger.info("Scheduled appointment (%s) notification (%s) to client (%s) at (%s)", appointment.name, reminder, clientName, date2);
                break;

            case ON_TIME:
                Date date3 = Date.from(appointment.date.atZone(ZoneId.systemDefault()).toInstant());
                timer.schedule(timerTask, date3);
                Logger.info("Scheduled appointment (%s) notification (%s) to client (%s) at (%s)", appointment.name, reminder, clientName, date3);
                break;
        }
        
        
        String reminderKey = this.getReminderKey(clientName, appointment);
        this.reminderTimers.put(reminderKey, timer);
    }
    
    public void cancel(String clientName, Appointment appointment) {
        String reminderKey = this.getReminderKey(clientName, appointment);
        
        if (this.reminderTimers.containsKey(reminderKey)) {
            this.reminderTimers.get(reminderKey).cancel();
            this.reminderTimers.remove(reminderKey);
            Logger.info("Canceled appointment (%s) scheduled notification to client (%s)", appointment.name, clientName);
        }
    }
}
