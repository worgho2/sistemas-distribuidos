import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';
import InvitesPageTableRow from './row';

const InvitesPageTable: React.FC = () => {
    const { appointmentInvites } = useCalendarContext();

    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell>Owner</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Reply</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {appointmentInvites.map((invite) => (
                        <InvitesPageTableRow key={invite.name} invite={invite} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default InvitesPageTable;
