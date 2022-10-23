/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import trabalho.dois.shared.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.time.*;
import java.util.*;
import trabalho.dois.shared.Appointment.Reminder;

/**
 *
 * @author otavio
 */
public class Client extends UnicastRemoteObject implements ClientInterface {
    private final String name;
    private final ServerInterface server;
    private final Security security;
    private PublicKey serverPublicKey = null;
    private Boolean canScanMenu = true;
    
    public Client(String name, ServerInterface server) throws RemoteException {
        this.name = name;
        this.server = server;
        this.security = new Security();
    }
    
    public void initialize() throws RemoteException {
        this.serverPublicKey = this.server.registerUser(this.name, this);
        Logger.info("Registed on server", this.name);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }

            Logger.input("Select an option:\n1 -> Create appointment\n2 -> Cancel appointment or reminder\n3 -> List appointments");
            String menu = scanner.nextLine();
            if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }

            if (menu.equals("1")) {
                String appointmentName = "";
                LocalDateTime dateTime = null;
                Reminder reminder = Reminder.DISABLED;
                HashMap<String, Reminder> attendees = new HashMap<>();
                
                Logger.input("Name:");
                String appointmentNameOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                appointmentName = appointmentNameOption;
                
                Logger.input("Date (day/month/year hour:minute):");
                String appointmentDateTime = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] dateTimeComponents = appointmentDateTime.split(" ");
                if (dateTimeComponents.length != 2) { Logger.error("Invalid input"); continue; }
                String[] dateComponents = dateTimeComponents[0].split("/");
                if (dateComponents.length != 3) { Logger.error("Invalid input"); continue; }
                String[] timeComponents = dateTimeComponents[1].split(":");
                if (timeComponents.length != 2) { Logger.error("Invalid input"); continue; }
                dateTime = LocalDateTime.of(Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[0]), Integer.parseInt(timeComponents[0]), Integer.parseInt(timeComponents[1]));
                if (dateTime.isBefore(LocalDateTime.now())) { Logger.error("Invalid input"); continue; }
                
                Logger.input("Reminder:\n1 -> Disabled\n2 -> Five minutes before\n3 -> Ten minutes before\n4 -> On time");
                String reminderOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                else if (reminderOption.equals("1")) { reminder = Reminder.DISABLED; }
                else if (reminderOption.equals("2")) { reminder = Reminder.FIVE_MINUTES_BEFORE; }
                else if (reminderOption.equals("3")) { reminder = Reminder.FIVE_MINUTES_BEFORE; }
                else if (reminderOption.equals("4")) { reminder = Reminder.ON_TIME; }
                
                Logger.input("Attendees (name1 name2 ...):");
                String attendeesOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] attendeesArray = attendeesOption.split(" ");
                for (String attendeeName : attendeesArray) {
                    if (!attendeeName.equals(this.name)) {
                        attendees.put(attendeeName, Reminder.DISABLED);
                    }
                }
                
                Appointment newAppointment = new Appointment(appointmentName, dateTime, this.name, reminder, attendees);
                server.createAppointment(this.name, newAppointment);
                continue;
            }

            if (menu.equals("2")) {
                Logger.input("Appointment name:");
                String appointmentName = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                
                Logger.input("Disable only reminder? (y or n):");
                String onlyReminder = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                
                server.cancelAppointmentOrReminder(this.name, appointmentName, onlyReminder.equals("y"));
                continue;
            }

            if (menu.equals("3")) {
                Logger.input("Date (day/month/year):");
                String dateAsString = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] elements = dateAsString.split("/");
                if (elements.length != 3) { Logger.error("Invalid input"); continue; }
                int day = Integer.parseInt(elements[0]);
                int month = Integer.parseInt(elements[1]);
                int year = Integer.parseInt(elements[2]);
                List<Appointment> appointmentsList = server.listAppointments(this.name, LocalDate.of(year, month, day));
                Logger.info("Appointments at (%s):", dateAsString);
                if (appointmentsList.isEmpty()) {
                    Logger.info("No appointment found");
                }
                for (Appointment appointment: appointmentsList) {
                    Logger.info("Appointment: (%s)", appointment.print());
                }
                continue;
            }
            
            Logger.error("Invalid input");
        }
    }
    
    @Override
    public void onAppointmentNotification(String message, Appointment appointment) throws RemoteException {
        Logger.info("Server sent message (%s) of appointment (%s)", message, appointment.print());
    }

    @Override
    public Appointment.InviteResponse onAppointmentInvite(Appointment.Invite invite, byte[] signature) throws RemoteException {
        if (!this.security.isValidSignature(this.serverPublicKey, signature, invite.toSignature(this.name))) {
            return null;
        }
        
        this.canScanMenu = false;
        
        Scanner scanner = new Scanner(System.in);
        while(true) {
            Logger.input("Received appointment invite (%s):\n1 -> Accept\n2 -> Reject", invite.print());
            String inputMenu = scanner.nextLine();

            if (inputMenu.equals("1")) {
                Logger.input("Reminder:\n1 -> Disabled\n2 -> Five minutes before\n3 -> Ten minutes before\n4 -> On time");
                String acceptMenu = scanner.nextLine();
                
                if (acceptMenu.equals("1")) {
                    this.canScanMenu = true;
                    return invite.createResponse(true, Appointment.Reminder.DISABLED);
                }
                
                if (acceptMenu.equals("2")) {
                    this.canScanMenu = true;
                    return invite.createResponse(true, Appointment.Reminder.FIVE_MINUTES_BEFORE);
                }
                
                if (acceptMenu.equals("3")) {
                    this.canScanMenu = true;
                    return invite.createResponse(true, Appointment.Reminder.TEN_MINUTES_BEFORE);
                }
                
                if (acceptMenu.equals("4")) {
                    this.canScanMenu = true;
                    return invite.createResponse(true, Appointment.Reminder.ON_TIME);
                }
            }

            if (inputMenu.equals("2")) {
                this.canScanMenu = true;
                return invite.createResponse(false, Appointment.Reminder.DISABLED);
            }
        }
    }
}
