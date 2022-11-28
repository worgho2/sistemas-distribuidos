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

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{appointment.name}</TableCell>
                <TableCell>{appointment.owner}</TableCell>

                <TableCell>
                    <Tooltip title={new Date(appointment.date).toISOString()}>
                        <Typography>{new Date(appointment.date).toLocaleDateString('en-US')}</Typography>
                    </Tooltip>
                </TableCell>

                <TableCell>{appointment.attendees.join(', ')}</TableCell>
            </TableRow>
        </React.Fragment>
    );
};

export default AppointmentsPageTableRow;
