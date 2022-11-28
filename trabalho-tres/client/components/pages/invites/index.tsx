import React from 'react';
import { Paper } from '@mui/material';
import InvitesPageToolbar from './toolbar';
import InvitesPageTable from './table';

const InvitesPage: React.FC = () => {
    return (
        <Paper sx={{ p: 2, paddingBottom: 4 }}>
            <InvitesPageToolbar />
            <InvitesPageTable />
        </Paper>
    );
};

export default InvitesPage;
