import React, { createContext, PropsWithChildren, useContext, useState } from 'react';
import { Appointment, AppointmentInvite, AppointmentReminder, Reminder } from '../models/appointment';
import { useNotificationsContext } from './notifications-provider';
import * as calendarService from '../services/calendar';

interface CalendarContextData {
    clientName?: string;
    publicKey?: string;
    appointments: Appointment[];
    appointmentInvites: AppointmentInvite[];
    appointmentReminders: AppointmentReminder[];
    loading: boolean;
    register: (clientName: string) => Promise<void>;
    loadAppointments: () => Promise<void>;
    createAppointment: (name: string, date: Date, reminder: Reminder, attendees: string[]) => Promise<void>;
    cancelAppointment: (appointmentName: string) => Promise<void>;
    updateAppointmentReminder: (appointmentName: string, reminder: Reminder) => Promise<void>;
    answerAppointmentInvite: (appointmentName: string, accept: boolean, reminder: Reminder) => Promise<void>;
}

const CalendarContext = createContext<CalendarContextData>({
    appointments: [],
    appointmentInvites: [],
    appointmentReminders: [],
    loading: false,
    register: async () => {},
    loadAppointments: async () => {},
    createAppointment: async () => {},
    cancelAppointment: async () => {},
    updateAppointmentReminder: async () => {},
    answerAppointmentInvite: async () => {},
});

export function useCalendarContext(): CalendarContextData {
    return useContext<CalendarContextData>(CalendarContext);
}

const ScansProvider: React.FC<PropsWithChildren> = ({ children }) => {
    const { enqueueSnackbar } = useNotificationsContext();
    const [clientName, setClientName] = useState<string | undefined>(undefined);
    const [publicKey, setPublicKey] = useState<string | undefined>(undefined);
    const [appointments, setAppointments] = useState<Appointment[]>([]);
    const [appointmentInvites, setAppointmentInvites] = useState<AppointmentInvite[]>([]);
    const [appointmentReminders, setAppointmentReminders] = useState<AppointmentReminder[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const register = async (clientName_: string) => {
        try {
            const response = await calendarService.register(clientName_);
            setClientName(clientName_);
            setPublicKey(response.publicKey);

            calendarService.listenToInvites(clientName_, response.publicKey, async (invite) => {
                enqueueSnackbar(`Client ${invite.onwer} invited you to the ${invite.name} appointment`, 'success');
                setAppointmentInvites((old) => [...old, invite]);
            });

            calendarService.listenToReminders(clientName_, async (appointmentReminder) => {
                let suffix = 'now';
                if (appointmentReminder.reminder === Reminder.FIVE_MINUTES_BEFORE) {
                    suffix = 'in five minutes';
                } else if (appointmentReminder.reminder === Reminder.TEN_MINUTES_BEFORE) {
                    suffix = 'in ten minutes';
                }

                enqueueSnackbar(`The appointment ${appointmentReminder.name} starts ${suffix}`, 'success');
                setAppointmentReminders((old) => [...old, appointmentReminder]);
            });

            enqueueSnackbar(`Client registered with success`, 'success');
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
        }
    };

    const loadAppointments = async () => {
        try {
            setLoading(true);

            if (clientName === undefined) {
                throw new Error('Client was not initialized');
            }

            setAppointments([]);
            const appointments = await calendarService.listAppointments(clientName);
            setAppointments(appointments);

            setLoading(false);
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
            setLoading(false);
        }
    };

    const createAppointment = async (name: string, date: Date, reminder: Reminder, attendees: string[]) => {
        try {
            if (clientName === undefined) {
                throw new Error('Client was not initialized');
            }

            await calendarService.createAppointment(clientName, { name, date, reminder, attendees });
            await loadAppointments();
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
        }
    };

    const cancelAppointment = async (appointmentName: string) => {
        try {
            if (clientName === undefined) {
                throw new Error('Client was not initialized');
            }

            await calendarService.cancelAppointment(clientName, appointmentName);
            await loadAppointments();
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
        }
    };

    const updateAppointmentReminder = async (appointmentName: string, reminder: Reminder) => {
        try {
            if (clientName === undefined) {
                throw new Error('Client was not initialized');
            }

            await calendarService.updateAppointmentReminder(clientName, appointmentName, { reminder });
            await loadAppointments();
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
        }
    };
    const answerAppointmentInvite = async (appointmentName: string, accept: boolean, reminder: Reminder) => {
        try {
            if (clientName === undefined) {
                throw new Error('Client was not initialized');
            }

            await calendarService.answerAppointmentInvite(clientName, appointmentName, { accept, reminder });

            // TODO: remover o invite
            await loadAppointments();
        } catch (error) {
            enqueueSnackbar(error instanceof Error ? error.message : 'error', 'error');
        }
    };

    return (
        <CalendarContext.Provider
            value={{
                clientName,
                publicKey,
                appointments,
                appointmentInvites,
                appointmentReminders,
                loading,
                register,
                loadAppointments,
                createAppointment,
                cancelAppointment,
                updateAppointmentReminder,
                answerAppointmentInvite,
            }}
        >
            {children}
        </CalendarContext.Provider>
    );
};

export default ScansProvider;
