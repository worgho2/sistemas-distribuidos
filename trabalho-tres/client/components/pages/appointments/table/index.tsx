import React, { useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';
import AppointmentsPageTableRow from './row';

const AppointmentsPageTable: React.FC = () => {
    const { clientName, appointments, lastLoadedDate, loadAppointments } = useCalendarContext();

    useEffect(() => {
        if (clientName !== undefined && lastLoadedDate === undefined) {
            loadAppointments();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [clientName]);

    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell>Owner</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Attendees</TableCell>
                        <TableCell>Edit / Delete</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {appointments.map((appointment) => (
                        <AppointmentsPageTableRow key={appointment.name} appointment={appointment} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default AppointmentsPageTable;
