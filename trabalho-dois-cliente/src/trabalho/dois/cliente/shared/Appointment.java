/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.cliente.shared;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author otavio
 */
public class Appointment {
    public String name;
    public Date date;
    public String owner;
    public Reminder reminder;
    public HashMap<String, Reminder> attendees;

    public class Invite {
        public String name;
        public Date date;
        public String owner;

        public Invite(String name, Date date, String owner) {
            this.name = name;
            this.date = date;
            this.owner = owner;
        }
    }

    public class InviteResponse {
        public Boolean accepted;
        public Reminder reminder;

        public InviteResponse(Boolean accepted, Reminder reminder) {
            this.accepted = accepted;
            this.reminder = reminder;
        }

        public InviteResponse(Boolean accepted) {
            this.accepted = accepted;
            this.reminder = Reminder.DISABLED;
        }
    }

    public enum Reminder {
        PENDING,
        DISABLED,
        FIVE_MINUTES_BEFORE,
        TEN_MINUTES_BEFORE,
        ON_TIME;
    }

    public Appointment(String name, Date date, String owner, Reminder reminder, HashMap<String, Reminder> attendees) {
        this.name = name;
        this.date = date;
        this.owner = owner;
        this.reminder = reminder;
        this.attendees = attendees;
    }
}
