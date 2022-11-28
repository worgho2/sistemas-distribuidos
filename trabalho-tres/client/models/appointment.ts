export enum Reminder {
    PENDING = 'PENDING',
    DISABLED = 'DISABLED',
    FIVE_MINUTES_BEFORE = 'FIVE_MINUTES_BEFORE',
    TEN_MINUTES_BEFORE = 'TEN_MINUTES_BEFORE',
    ON_TIME = 'ON_TIME',
}

export type Appointment = {
    name: string;
    date: Date;
    owner: string;
    reminder: Reminder;
    attendees: string[];
};

export type AppointmentReminder = {
    name: string;
    date: Date;
    owner: string;
    reminder: Reminder;
};

export type AppointmentInvite = {
    name: string;
    date: Date;
    owner: string;
    signature: string;
};
