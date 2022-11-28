import { MarkEmailUnread, NotificationsActive, Today } from '@mui/icons-material';
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Drawer,
    List,
    TextField,
    Toolbar,
} from '@mui/material';
import React, { useState } from 'react';
import { useCalendarContext } from '../../hooks/calendar-provider';
import AppointmentsPage from '../pages/appointments';
import InvitesPage from '../pages/invites';
import RemindersPage from '../pages/reminders';
import MainMenuItem from './item';

const drawerWidth = 240;

const MainMenu: React.FC = () => {
    const [selected, setSelected] = useState<string>('APPOINTMENTS');
    const [newClientName, setNewClientName] = useState<string>('');
    const { register, clientName } = useCalendarContext();

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

            <Dialog open={clientName === undefined}>
                <DialogTitle>Register</DialogTitle>
                <DialogContent>
                    <TextField
                        variant="outlined"
                        size="small"
                        margin="dense"
                        label="Name"
                        type="text"
                        fullWidth
                        autoFocus
                        onChange={(event) => {
                            setNewClientName(event.target.value);
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        color="success"
                        onClick={async () => {
                            if (newClientName.length > 0) {
                                await register(newClientName);
                            }
                        }}
                        disabled={newClientName.length < 1}
                    >
                        Register
                    </Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
};

export default MainMenu;
