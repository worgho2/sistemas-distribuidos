/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.time.LocalDateTime;

/**
 *
 * @author otavio
 */
public class Appointment implements Serializable {
    public String name;
    public LocalDateTime date;
    public String owner;
    public Reminder reminder;
    public HashMap<String, Reminder> attendees;

    public class Invite implements Serializable {
        public String name;
        public LocalDateTime date;
        public String owner;

        public Invite(String name, LocalDateTime date, String owner) {
            this.name = name;
            this.date = date;
            this.owner = owner;
        }
        
        public InviteResponse createResponse(Boolean accepted, Reminder reminder) {
            return new InviteResponse(accepted, reminder);
        }
    }

    public class InviteResponse implements Serializable {
        public Boolean accepted;
        public Reminder reminder;

        public InviteResponse(Boolean accepted, Reminder reminder) {
            this.accepted = accepted;
            this.reminder = reminder;
        }
    }

    public enum Reminder implements Serializable {
        PENDING,
        DISABLED,
        FIVE_MINUTES_BEFORE,
        TEN_MINUTES_BEFORE,
        ON_TIME;
    }

    public Appointment(String name, LocalDateTime date, String owner, Reminder reminder, HashMap<String, Reminder> attendees) {
        this.name = name;
        this.date = date;
        this.owner = owner;
        this.reminder = reminder;
        this.attendees = attendees;
    }
    
    public Invite getInvite(String atendeeName) {
        return new Invite(atendeeName, this.date, this.owner);
    }
}