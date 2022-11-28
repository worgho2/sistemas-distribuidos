import { useSnackbar, VariantType } from 'notistack';
import React, { createContext, PropsWithChildren, useContext } from 'react';

interface NotificationsContextData {
    enqueueSnackbar: (message: string, variant: VariantType) => void;
}

const NotificationsContext = createContext<NotificationsContextData>({
    enqueueSnackbar: () => {},
});

export function useNotificationsContext(): NotificationsContextData {
    return useContext<NotificationsContextData>(NotificationsContext);
}

const NotificationsProvider: React.FC<PropsWithChildren> = ({ children }) => {
    const { enqueueSnackbar } = useSnackbar();

    return (
        <NotificationsContext.Provider
            value={{
                enqueueSnackbar: (message, variant) =>
                    enqueueSnackbar(message, {
                        persist: false,
                        variant,
                    }),
            }}
        >
            {children}
        </NotificationsContext.Provider>
    );
};

export default NotificationsProvider;
