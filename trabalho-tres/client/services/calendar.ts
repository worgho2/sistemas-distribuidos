import axios, { AxiosError } from 'axios';
import { Appointment, AppointmentInvite, AppointmentReminder, Reminder } from '../models/appointment';
import forge from 'node-forge';

function buildEndpointURL(path: string): URL {
    const baseUrl = new URL(`http://localhost:3333`).origin;
    return new URL(`${baseUrl}${path.startsWith('/') ? '' : '/'}${path}`);
}

function mapError(error: unknown) {
    let reason = 'unknown';
    if (error instanceof AxiosError) {
        const response = error.response;
        if (response !== undefined) {
            const data = response.data as { error?: string };
            if (data.error !== undefined) {
                reason = data.error;
            }
        }
    }

    return Error(`Calendar service exception: ${reason}`);
}

export async function register(clientName: string): Promise<{ publicKey: string }> {
    try {
        const response = await axios.post<{ publicKey: string }>(buildEndpointURL(`/users/register`).href, {
            clientName,
        });

        return response.data;
    } catch (error) {
        throw mapError(error);
    }
}

export function listenToReminders(clientName: string, callback: (notification: AppointmentReminder) => Promise<void>) {
    const eventSource = new EventSource(buildEndpointURL(`/events/stream`));

    eventSource.addEventListener(`reminders-${clientName}`, async (event) => {
        try {
            const payload = JSON.parse(event.data) as AppointmentReminder;
            await callback(payload);
        } catch (error) {
            console.log(error);
        }
    });
}

export function listenToInvites(
    clientName: string,
    publicKey: string,
    callback: (notification: AppointmentInvite) => Promise<void>
) {
    const eventSource = new EventSource(buildEndpointURL(`/events/stream`));
    const forgePublicKey = forge.pki.publicKeyFromPem(publicKey);
    const md = forge.md.sha256.create().update(clientName, 'utf8');

    eventSource.addEventListener(`invites-${clientName}`, async (event) => {
        try {
            const payload = JSON.parse(event.data) as AppointmentInvite;
            try {
                const isValidSignature = forgePublicKey.verify(md.digest().bytes(), payload.signature);
                if (!isValidSignature) {
                    throw new Error('Invalid Signature');
                }
            } catch {
                console.log('Signature is invalid');
            }

            await callback(payload);
        } catch (error) {
            console.log(error);
        }
    });
}

export async function createAppointment(
    clientName: string,
    data: {
        name: string;
        date: Date;
        reminder: Reminder;
        attendees: string[];
    }
): Promise<void> {
    try {
        await axios.post(buildEndpointURL(`/users/${clientName}/appointments`).href, data);
    } catch (error) {
        throw mapError(error);
    }
}

export async function cancelAppointment(clientName: string, appointmentName: string): Promise<void> {
    try {
        await axios.delete(buildEndpointURL(`/users/${clientName}/appointments/${appointmentName}`).href);
    } catch (error) {
        throw mapError(error);
    }
}

export async function updateAppointmentReminder(
    clientName: string,
    appointmentName: string,
    data: {
        reminder: Reminder;
    }
): Promise<void> {
    try {
        await axios.patch(buildEndpointURL(`/users/${clientName}/appointments/${appointmentName}/reminder`).href, data);
    } catch (error) {
        throw mapError(error);
    }
}

export async function listAppointments(clientName: string): Promise<Appointment[]> {
    try {
        const response = await axios.get<Appointment[]>(buildEndpointURL(`/users/${clientName}/appointments`).href);
        return response.data;
    } catch (error) {
        throw mapError(error);
    }
}

export async function answerAppointmentInvite(
    clientName: string,
    appointmentName: string,
    data: {
        accept: boolean;
        reminder: Reminder;
    }
): Promise<void> {
    try {
        await axios.put(buildEndpointURL(`/users/${clientName}/appointments/${appointmentName}/invite`).href, data);
    } catch (error) {
        throw mapError(error);
    }
}
