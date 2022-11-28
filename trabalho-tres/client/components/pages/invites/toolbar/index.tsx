import React from 'react';
import { Toolbar, Typography } from '@mui/material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';

const InvitesPageToolbar: React.FC = () => {
    const { appointmentInvites } = useCalendarContext();

    return (
        <Toolbar sx={{ pl: { sm: 2 }, pr: { xs: 1, sm: 1 } }}>
            <Typography variant="h6" sx={{ flex: '1 1 100%' }}>
                Invites {`(${appointmentInvites.length})`}
            </Typography>
        </Toolbar>
    );
};

export default InvitesPageToolbar;
