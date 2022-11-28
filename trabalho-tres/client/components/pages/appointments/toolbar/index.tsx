import React, { useState } from 'react';
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Toolbar,
    Typography,
} from '@mui/material';
import { LoadingButton } from '@mui/lab';
import { Add, Sync } from '@mui/icons-material';
import { useCalendarContext } from '../../../../hooks/calendar-provider';
import { Reminder } from '../../../../models/appointment';

const AppointmentsPageToolbar: React.FC = () => {
    const { appointments, loading, loadAppointments, createAppointment } = useCalendarContext();
    const [createDialogOpen, setCreateDialogOpen] = useState<boolean>(false);
    const [name, setName] = useState<string>('');
    const [date, setDate] = useState<Date | undefined>(undefined);
    const [reminder, setReminder] = useState<Reminder | undefined>(undefined);
    const [attendees, setAttendees] = useState<string[]>([]);

    return (
        <Toolbar sx={{ pl: { sm: 2 }, pr: { xs: 1, sm: 1 } }}>
            <Typography variant="h6" sx={{ flex: '1 1 100%' }}>
                Appointments {loading ? '' : `(${appointments.length})`}
            </Typography>
            <Box component="div" sx={{ paddingRight: 2 }}>
                <LoadingButton
                    color="primary"
                    variant="outlined"
                    onClick={async () => {
                        await loadAppointments();
                    }}
                    loading={loading}
                    loadingPosition="end"
                    endIcon={<Sync />}
                >
                    {loading ? 'Loading' : 'Reload'}
                </LoadingButton>
            </Box>
            <Box component="div" sx={{ paddingRight: 2 }}>
                <Button
                    color="success"
                    variant="outlined"
                    onClick={() => {
                        setCreateDialogOpen(true);
                    }}
                    endIcon={<Add />}
                    disabled={loading}
                >
                    Create
                </Button>
            </Box>
            <Dialog
                open={createDialogOpen}
                onClose={() => {
                    setCreateDialogOpen(false);
                }}
            >
                <DialogTitle>Create Appointment</DialogTitle>
                <DialogContent>
                    <TextField
                        variant="outlined"
                        size="medium"
                        margin="dense"
                        label="Name"
                        type="text"
                        fullWidth
                        autoFocus
                        onChange={(event) => {
                            setName(event.target.value);
                        }}
                    />

                    <TextField
                        variant="outlined"
                        size="medium"
                        margin="dense"
                        type="datetime-local"
                        fullWidth
                        onChange={(event) => {
                            setDate(new Date(event.target.value));
                        }}
                    />

                    <FormControl fullWidth>
                        <InputLabel id="demo-simple-select-label">Reminder</InputLabel>
                        <Select
                            variant="outlined"
                            size="medium"
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            label="Reminder"
                            onChange={(event) => {
                                setReminder(event.target.value as Reminder);
                            }}
                        >
                            <MenuItem value={Reminder.DISABLED}>Disabled</MenuItem>
                            <MenuItem value={Reminder.TEN_MINUTES_BEFORE}>Ten minutes before</MenuItem>
                            <MenuItem value={Reminder.FIVE_MINUTES_BEFORE}>Five minutes before</MenuItem>
                            <MenuItem value={Reminder.ON_TIME}>On time</MenuItem>
                        </Select>
                    </FormControl>
                    <TextField
                        variant="outlined"
                        size="medium"
                        margin="dense"
                        label="Attendeees (comma separated)"
                        type="text"
                        fullWidth
                        onChange={(event) => {
                            setAttendees(event.target.value.replace(' ', '').split(','));
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        color="inherit"
                        onClick={() => {
                            setCreateDialogOpen(false);
                        }}
                    >
                        Cancel
                    </Button>
                    <Button
                        color="success"
                        disabled={name === '' || date === undefined || reminder === undefined}
                        onClick={async () => {
                            if (name !== '' && date !== undefined && reminder !== undefined) {
                                await createAppointment(name, date, reminder, attendees);
                            }

                            setCreateDialogOpen(false);
                        }}
                    >
                        Create
                    </Button>
                </DialogActions>
            </Dialog>
        </Toolbar>
    );
};

export default AppointmentsPageToolbar;
