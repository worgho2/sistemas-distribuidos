import React from 'react';
import { TableCell, TableRow, Tooltip, Typography } from '@mui/material';
import { useCalendarContext } from '../../../../../hooks/calendar-provider';
import { Appointment } from '../../../../../models/appointment';

interface AppointmentsPageTableRowProps {
    appointment: Appointment;
}

const AppointmentsPageTableRow: React.FC<AppointmentsPageTableRowProps> = (props) => {
    const { appointment } = props;
    const { cancelAppointment, updateAppointmentReminder } = useCalendarContext();

    const attendees = appointment.attendees.join(', ');
    const date = new Date(appointment.date);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{appointment.name}</TableCell>
                <TableCell>{appointment.owner}</TableCell>

                <TableCell>
                    <Typography>{`${date.toLocaleDateString('pt-BR')} ${date.toLocaleTimeString('pt-BR')}`}</Typography>
                </TableCell>

                <TableCell>{attendees.length === 0 ? '--' : attendees}</TableCell>
            </TableRow>
        </React.Fragment>
    );
};

export default AppointmentsPageTableRow;
