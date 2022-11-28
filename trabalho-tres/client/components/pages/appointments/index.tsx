import React from 'react';
import { Paper } from '@mui/material';
import AppointmentsPageToolbar from './toolbar';
import AppointmentsPageTable from './table';

const AppointmentsPage: React.FC = () => {
    return (
        <Paper sx={{ p: 2, paddingBottom: 4 }}>
            <AppointmentsPageToolbar />
            <AppointmentsPageTable />
        </Paper>
    );
};

export default AppointmentsPage;
