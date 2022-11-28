import React from 'react';
import { IconButton, Stack, TableCell, TableRow, Typography } from '@mui/material';
import { useCalendarContext } from '../../../../../hooks/calendar-provider';
import { Appointment } from '../../../../../models/appointment';
import { DeleteForever, Edit } from '@mui/icons-material';

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

                <TableCell>
                    <Stack direction="row">
                        <IconButton onClick={() => {}}>
                            <Edit color="info" />
                        </IconButton>

                        <IconButton onClick={() => cancelAppointment(appointment.name)}>
                            <DeleteForever color="error" />
                        </IconButton>
                    </Stack>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
};

export default AppointmentsPageTableRow;
