import React, { useState } from 'react';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TableCell,
    TableRow,
    Typography,
} from '@mui/material';
import { useCalendarContext } from '../../../../../hooks/calendar-provider';
import { Appointment, Reminder } from '../../../../../models/appointment';
import { DeleteForever, Edit } from '@mui/icons-material';
import { Box } from '@mui/system';

interface AppointmentsPageTableRowProps {
    appointment: Appointment;
}

const AppointmentsPageTableRow: React.FC<AppointmentsPageTableRowProps> = (props) => {
    const { appointment } = props;
    const { cancelAppointment, updateAppointmentReminder } = useCalendarContext();
    const [editOpen, setEditOpen] = useState<boolean>(false);
    const [reminder, setReminder] = useState<Reminder>(Reminder.ON_TIME);

    const attendees = appointment.attendees.join(', ');
    const date = new Date(appointment.date);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{appointment.name}</TableCell>
                <TableCell>{appointment.owner}</TableCell>

                <TableCell>
                    <Typography>{`${date.toLocaleDateString('pt-BR')} ${date.toLocaleTimeString('pt-BR')}`}</Typography>
                </TableCell>

                <TableCell>{attendees.length === 0 ? '--' : attendees}</TableCell>

                <TableCell>
                    <Stack direction="row">
                        <IconButton
                            onClick={() => {
                                setEditOpen(true);
                            }}
                        >
                            <Edit color="info" />
                        </IconButton>

                        <IconButton onClick={() => cancelAppointment(appointment.name)}>
                            <DeleteForever color="error" />
                        </IconButton>
                    </Stack>
                </TableCell>
            </TableRow>
            <Dialog
                open={editOpen}
                onClose={() => {
                    setEditOpen(false);
                }}
            >
                <DialogTitle>Update {appointment.name} appointment reminder</DialogTitle>
                <DialogContent>
                    <Box sx={{ paddingTop: 2 }}>
                        <FormControl fullWidth>
                            <InputLabel id="demo-simple-select-label">Reminder</InputLabel>
                            <Select
                                value={reminder}
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
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button
                        color="inherit"
                        onClick={() => {
                            setEditOpen(false);
                        }}
                    >
                        Cancel
                    </Button>
                    <Button
                        color="success"
                        onClick={async () => {
                            await updateAppointmentReminder(appointment.name, reminder);
                            setEditOpen(false);
                        }}
                    >
                        Update
                    </Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
};

export default AppointmentsPageTableRow;
