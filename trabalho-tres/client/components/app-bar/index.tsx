import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';
import { useCalendarContext } from '../../hooks/calendar-provider';

const MainAppBar: React.FC = () => {
    const { clientName } = useCalendarContext();

    return (
        <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1, background: '#111111' }}>
            <Toolbar sx={{ pl: { sm: 1.5 }, pr: { sm: 1.5 } }}>
                <Typography variant="h5" noWrap component="div" sx={{ flex: '1 1 50%', paddingLeft: 1 }}>
                    Calendar
                </Typography>
                <Typography variant="body1" sx={{ paddingRight: 1 }} noWrap>
                    {clientName || 'Client not registered'}
                </Typography>
            </Toolbar>
        </AppBar>
    );
};

export default MainAppBar;
