import React, { useState } from 'react';
import {
    Box,
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
    TableCell,
    TableRow,
    Typography,
} from '@mui/material';
import { AppointmentInvite, AppointmentReminder, Reminder } from '../../../../../models/appointment';
import { useCalendarContext } from '../../../../../hooks/calendar-provider';
import { Reply } from '@mui/icons-material';

interface InvitesPageTableRowProps {
    invite: AppointmentInvite;
}

const InvitesPageTableRow: React.FC<InvitesPageTableRowProps> = (props) => {
    const { invite } = props;
    const { answerAppointmentInvite } = useCalendarContext();
    const date = new Date(invite.date);

    const [replyOpen, setReplyOpen] = useState<boolean>(false);
    const [accept, setAccept] = useState<boolean>(true);
    const [reminder, setReminder] = useState<Reminder>(Reminder.ON_TIME);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>{invite.name}</TableCell>
                <TableCell>{invite.owner}</TableCell>
                <TableCell>
                    <Typography>{`${date.toLocaleDateString('pt-BR')} ${date.toLocaleTimeString('pt-BR')}`}</Typography>
                </TableCell>

                <TableCell>
                    <IconButton
                        onClick={() => {
                            setReplyOpen(true);
                        }}
                    >
                        <Reply color="info" />
                    </IconButton>
                </TableCell>
            </TableRow>
            <Dialog
                open={replyOpen}
                onClose={() => {
                    setReplyOpen(false);
                }}
            >
                <DialogTitle>Reply {invite.name} appointment invite</DialogTitle>
                <DialogContent>
                    <Box sx={{ paddingTop: 2 }}>
                        <FormControl fullWidth>
                            <InputLabel id="demo-simple-select-label">Accept</InputLabel>
                            <Select
                                value={accept}
                                variant="outlined"
                                size="medium"
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                label="Reminder"
                                onChange={(event) => {
                                    setAccept(event.target.value === 'true');
                                }}
                            >
                                <MenuItem value={'true'}>Yes</MenuItem>
                                <MenuItem value={'false'}>No</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>

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
                            setReplyOpen(false);
                        }}
                    >
                        Cancel
                    </Button>
                    <Button
                        color="success"
                        onClick={async () => {
                            if (accept === false) {
                                await answerAppointmentInvite(invite.name, accept, Reminder.DISABLED);
                            } else {
                                await answerAppointmentInvite(invite.name, accept, reminder);
                            }

                            setReplyOpen(false);
                        }}
                    >
                        Done
                    </Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
};

export default InvitesPageTableRow;
