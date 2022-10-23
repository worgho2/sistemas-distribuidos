/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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
        this.security = new Security(name);
    }
    
    public void printInstructions() {
        Logger.input("Select an option:\n1 -> Create Appointment\n2 -> Cancel Appointment or reminder\n3 -> List Appointments");
    }
    
    public void initialize() throws RemoteException {
        this.serverPublicKey = this.server.registerUser(this.name, this);
        Logger.info("Registed on server", this.name);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }

            this.printInstructions();
            String menu = scanner.nextLine();
            if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }

            if (menu.equals("1")) {
                String appointmentName = "";
                LocalDateTime dateTime = null;
                Reminder reminder = Reminder.DISABLED;
                HashMap<String, Reminder> attendees = new HashMap<>();
                
                Logger.input("Enter the appointment name:");
                String appointmentNameOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                appointmentName = appointmentNameOption;
                
                Logger.input("Enter the appointment date (day/month/year hour:minute):");
                String appointmentDateTime = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] dateTimeComponents = appointmentDateTime.split(" ");
                if (dateTimeComponents.length != 2) { System.out.print(Character.MIN_VALUE); continue; }
                String[] dateComponents = dateTimeComponents[0].split("/");
                if (dateComponents.length != 3) { System.out.print(Character.MIN_VALUE); continue; }
                String[] timeComponents = dateTimeComponents[1].split(":");
                if (timeComponents.length != 2) { System.out.print(Character.MIN_VALUE); continue; }
                dateTime = LocalDateTime.of(Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[0]), Integer.parseInt(timeComponents[0]), Integer.parseInt(timeComponents[1]));

                Logger.input("Select a reminder option:\n1 -> Disabled\n2 -> Five minutes before\n3 -> Ten minutes before\n4 -> On time");
                String reminderOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                else if (reminderOption.equals("1")) { reminder = Reminder.DISABLED; }
                else if (reminderOption.equals("2")) { reminder = Reminder.FIVE_MINUTES_BEFORE; }
                else if (reminderOption.equals("3")) { reminder = Reminder.FIVE_MINUTES_BEFORE; }
                else if (reminderOption.equals("4")) { reminder = Reminder.ON_TIME; }
                
                Logger.input("Enter attendees (attendee1 attendee2 ...):");
                String attendeesOption = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] attendeesArray = attendeesOption.split(" ");
                for (String attendeeName : attendeesArray) { attendees.put(attendeeName, Reminder.DISABLED); }
                
                Appointment newAppointment = new Appointment(appointmentName, dateTime, this.name, reminder, attendees);
                server.createAppointment(this.name, newAppointment);
                continue;
            }

            if (menu.equals("2")) {
                Logger.input("Enter the appointment and the reminder option (appointmentName onlyReminder), E.g: event1 true :");
                String acceptMenu = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] elements = acceptMenu.split(" ");
                if (elements.length != 2) { System.out.print(Character.MIN_VALUE); continue; }
                String appointmentName = elements[0];
                Boolean onlyReminder = elements[1].equals("true");
                server.cancelAppointmentOrReminder(this.name, appointmentName, onlyReminder);
                continue;
            }

            if (menu.equals("3")) {
                Logger.input("Enter a date (day/month/year), E.g: 16/05/1998 :");
                String acceptMenu = scanner.nextLine();
                if (!canScanMenu) { System.out.print(Character.MIN_VALUE); continue; }
                String[] elements = acceptMenu.split("/");
                if (elements.length != 3) { System.out.print(Character.MIN_VALUE); continue; }
                int day = Integer.parseInt(elements[0]);
                int month = Integer.parseInt(elements[1]);
                int year = Integer.parseInt(elements[2]);
                List<Appointment> appointmentsList = server.listAppointments(this.name, LocalDate.of(year, month, day));
                Logger.info("Appointments on (%s): %s", acceptMenu, appointmentsList);
            }
        }
    }
    
    @Override
    public void onAppointmentNotification(String message, Appointment appointment) throws RemoteException {
        Logger.info("Server Notification: %s, Appointment:(name=%s, owner=%s, date=%s, attendees=%s)", message, appointment.name, appointment.owner, appointment.date, appointment.attendees.keySet());
    }

    @Override
    public Appointment.InviteResponse onAppointmentInvite(Appointment.Invite invite, byte[] signature) throws RemoteException {
        if (!this.security.isValidSignature(this.serverPublicKey, signature)) {
            return null;
        }
        
        /**
         * Dismiss the main menu and enable the invite menu
         */
        try {
            this.canScanMenu = false;
            
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            Logger.error("Robot exception: %s", e.getMessage());
        }
        
        Scanner scanner = new Scanner(System.in);
        while(true) {
            Logger.info("Received appointment invite: (name: %s, owner: %s, date: %s)", invite.name, invite.owner, invite.date);
            Logger.input("Select an option:\n1 -> Accept\n2 -> Reject");
            String inputMenu = scanner.nextLine();

            if (inputMenu.equals("1")) {
                Logger.input("Select a reminder option:\n1 -> Disabled\n2 -> Five minutes before\n3 -> Ten minutes before\n4 -> On time");
                String acceptMenu = scanner.nextLine();
                System.out.println("deu aqui");
                
                if (acceptMenu.equals("1")) {
                    System.out.println("deu aqui 2");
                    this.canScanMenu = true;
                    return invite.createResponse(true, Appointment.Reminder.DISABLED);
                }
                System.out.println("deu aqui 3");
                
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
