import { MarkEmailUnread, NotificationsActive, Today } from '@mui/icons-material';
import { Box, Drawer, List, Toolbar } from '@mui/material';
import React, { useState } from 'react';
import AppointmentsPage from '../pages/appointments';
import InvitesPage from '../pages/invites';
import RemindersPage from '../pages/reminders';
import MainMenuItem from './item';

const drawerWidth = 240;

const MainMenu: React.FC = () => {
    const [selected, setSelected] = useState<string>('APPOINTMENTS');

    return (
        <React.Fragment>
            <Drawer
                variant="permanent"
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
                }}
            >
                <Toolbar />
                <Box component="div" sx={{ overflow: 'clip', background: '#1F1D2B', height: '100vh' }}>
                    <List>
                        <MainMenuItem
                            name="Appointments"
                            selected={selected === 'APPOINTMENTS'}
                            icon={<Today htmlColor="#FFFFFF" />}
                            onClick={() => setSelected('APPOINTMENTS')}
                        />

                        <MainMenuItem
                            name="Invites"
                            selected={selected === 'INVITES'}
                            icon={<MarkEmailUnread htmlColor="#FFFFFF" />}
                            onClick={() => setSelected('INVITES')}
                        />

                        <MainMenuItem
                            name="Reminders"
                            selected={selected === 'REMINDERS'}
                            icon={<NotificationsActive htmlColor="#FFFFFF" />}
                            onClick={() => setSelected('REMINDERS')}
                        />
                    </List>
                </Box>
            </Drawer>
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Toolbar />
                {selected === 'APPOINTMENTS' ? <AppointmentsPage /> : null}
                {selected === 'INVITES' ? <InvitesPage /> : null}
                {selected === 'REMINDERS' ? <RemindersPage /> : null}
            </Box>
        </React.Fragment>
    );
};

export default MainMenu;
