import React from 'react';
import { IconButton, TableCell, TableRow, Typography } from '@mui/material';
import { AppointmentInvite, AppointmentReminder } from '../../../../../models/appointment';
import { useCalendarContext } from '../../../../../hooks/calendar-provider';
import { Reply } from '@mui/icons-material';

interface InvitesPageTableRowProps {
    invite: AppointmentInvite;
}

const InvitesPageTableRow: React.FC<InvitesPageTableRowProps> = (props) => {
    const { invite } = props;
    const { answerAppointmentInvite } = useCalendarContext();
    const date = new Date(invite.date);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{invite.name}</TableCell>
                <TableCell>{invite.owner}</TableCell>
                <TableCell>
                    <Typography>{`${date.toLocaleDateString('pt-BR')} ${date.toLocaleTimeString('pt-BR')}`}</Typography>
                </TableCell>

                <TableCell>
                    <IconButton onClick={() => {}}>
                        <Reply color="info" />
                    </IconButton>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
};

export default InvitesPageTableRow;
