import { Box } from '@mui/material';
import type { NextPage } from 'next';
import MainProvider from '../hooks/main-provider';
import MainAppBar from '../components/app-bar';
import MainMenu from '../components/menu';
import React from 'react';

const Home: NextPage = () => {
    return (
        <MainProvider>
            <Box component="div" sx={{ display: 'flex' }}>
                <MainAppBar />
                <MainMenu />
            </Box>
        </MainProvider>
    );
};

export default Home;
