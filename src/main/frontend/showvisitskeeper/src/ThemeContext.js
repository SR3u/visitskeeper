import React, {createContext, useContext, useState, useEffect, useCallback, useMemo} from 'react';
import {ThemeProvider as MuiThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import {lightTheme, darkTheme} from './theme';

const ThemeModeContext = createContext();

const STORAGE_KEY = 'theme.mode';

function getSystemTheme() {
    if (typeof window !== 'undefined' && window.matchMedia) {
        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }
    return 'light';
}

function getStoredMode() {
    try {
        const stored = localStorage.getItem(STORAGE_KEY);
        if (stored === 'light' || stored === 'dark') {
            return stored;
        }
    } catch (e) {
        // localStorage unavailable
    }
    return null; // null means follow device
}

export function ThemeProvider({children}) {
    const [mode, setMode] = useState(() => getStoredMode());
    const [systemTheme, setSystemTheme] = useState(getSystemTheme);

    useEffect(() => {
        const mq = window.matchMedia('(prefers-color-scheme: dark)');
        const handler = (e) => setSystemTheme(e.matches ? 'dark' : 'light');
        mq.addEventListener('change', handler);
        return () => mq.removeEventListener('change', handler);
    }, []);

    const effectiveMode = mode ?? systemTheme;
    const theme = effectiveMode === 'dark' ? darkTheme : lightTheme;

    const toggleTheme = useCallback(() => {
        setMode((prev) => {
            const newMode = prev === null ? (systemTheme === 'dark' ? 'light' : 'dark') : (prev === 'dark' ? 'light' : 'dark');
            try {
                localStorage.setItem(STORAGE_KEY, newMode);
            } catch (e) {
                // localStorage unavailable
            }
            return newMode;
        });
    }, [systemTheme]);

    const resetToAuto = useCallback(() => {
        setMode(null);
        try {
            localStorage.removeItem(STORAGE_KEY);
        } catch (e) {
            // localStorage unavailable
        }
    }, []);

    const isAuto = mode === null;

    const value = useMemo(() => ({
        mode: effectiveMode,
        isAuto,
        toggleTheme,
        resetToAuto,
    }), [effectiveMode, isAuto, toggleTheme, resetToAuto]);

    return (
        <ThemeModeContext.Provider value={value}>
            <MuiThemeProvider theme={theme}>
                <CssBaseline/>
                {children}
            </MuiThemeProvider>
        </ThemeModeContext.Provider>
    );
}

export function useThemeMode() {
    const context = useContext(ThemeModeContext);
    if (!context) {
        throw new Error('useThemeMode must be used within a ThemeProvider');
    }
    return context;
}
