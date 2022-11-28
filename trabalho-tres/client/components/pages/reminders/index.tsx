import React from 'react';
import { Paper } from '@mui/material';
import RemindersPageToolbar from './toolbar';
import RemindersPageTable from './table';

const RemindersPage: React.FC = () => {
    return (
        <Paper sx={{ p: 2, paddingBottom: 4 }}>
            <RemindersPageToolbar />
            <RemindersPageTable />
        </Paper>
    );
};

export default RemindersPage;
