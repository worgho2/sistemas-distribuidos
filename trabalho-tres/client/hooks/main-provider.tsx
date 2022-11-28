import React, { PropsWithChildren } from 'react';
import NotificationsProvider from './notifications-provider';
import { SnackbarProvider } from 'notistack';
import CalendarProvider from './calendar-provider';

const MainProvider: React.FC<PropsWithChildren> = ({ children }) => {
    return (
        <SnackbarProvider maxSnack={4}>
            <NotificationsProvider>
                <CalendarProvider>{children}</CalendarProvider>
            </NotificationsProvider>
        </SnackbarProvider>
    );
};

export default MainProvider;
