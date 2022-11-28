import { ThemeOptions } from '@mui/material/styles';

const lightThemeOptions: ThemeOptions = {
    palette: {
        mode: 'light',
        text: {
            primary: '#FFFFFF',
            secondary: '#999999',
        },
        primary: {
            main: '#792EEB',
            contrastText: '#FFFFFF',
        },
        secondary: {
            main: '#1F1D2B',
        },
        success: {
            main: '#23FC6C',
        },
        info: {
            main: '#2F80ED',
        },
        warning: {
            main: '#FF701C',
        },
        error: {
            main: '#FF0000',
        },
        action: {
            disabled: '#888888',
        },
        background: {
            default: '#111111',
            paper: '#1F1D2B',
        },
        divider: '#1F1D2B',
        common: {
            black: '#111111',
            white: '#FFFFFF',
        },
    },
};

export default lightThemeOptions;
