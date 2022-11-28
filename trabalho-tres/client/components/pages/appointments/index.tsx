import React from 'react';
import { Paper } from '@mui/material';
import AppointmentsPageToolbar from './toolbar';

const AppointmentsPage: React.FC = () => {
    return (
        <Paper sx={{ p: 2, paddingBottom: 4 }}>
            <AppointmentsPageToolbar />
        </Paper>
    );
};

export default AppointmentsPage;
