import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';
import RemindersPageTableRow from './row';

const RemindersPageTable: React.FC = () => {
    const { appointmentReminders } = useCalendarContext();

    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell>Owner</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Reminder</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {appointmentReminders.map((reminder) => (
                        <RemindersPageTableRow key={reminder.name} reminder={reminder} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default RemindersPageTable;
