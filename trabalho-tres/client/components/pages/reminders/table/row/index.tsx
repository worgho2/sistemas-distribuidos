import React from 'react';
import { TableCell, TableRow, Typography } from '@mui/material';
import { AppointmentReminder } from '../../../../../models/appointment';

interface RemindersPageTableRowProps {
    reminder: AppointmentReminder;
}

const RemindersPageTableRow: React.FC<RemindersPageTableRowProps> = (props) => {
    const { reminder } = props;
    const date = new Date(reminder.date);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{reminder.name}</TableCell>
                <TableCell>{reminder.owner}</TableCell>
                <TableCell>
                    <Typography>{`${date.toLocaleDateString('pt-BR')} ${date.toLocaleTimeString('pt-BR')}`}</Typography>
                </TableCell>
                <TableCell>{reminder.reminder}</TableCell>
            </TableRow>
        </React.Fragment>
    );
};

export default RemindersPageTableRow;
