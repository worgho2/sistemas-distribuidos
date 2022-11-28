import React from 'react';
import { Toolbar, Typography } from '@mui/material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';

const RemindersPageToolbar: React.FC = () => {
    const { appointmentReminders } = useCalendarContext();

    return (
        <Toolbar sx={{ pl: { sm: 2 }, pr: { xs: 1, sm: 1 } }}>
            <Typography variant="h6" sx={{ flex: '1 1 100%' }}>
                Reminders {`(${appointmentReminders.length})`}
            </Typography>
        </Toolbar>
    );
};

export default RemindersPageToolbar;
