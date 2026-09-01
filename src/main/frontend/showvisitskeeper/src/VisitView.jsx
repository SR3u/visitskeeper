import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Avatar,
    Box,
    Chip,
    Divider,
    Grid,
    Paper,
    Stack,
    TextareaAutosize,
    Typography,
    useMediaQuery,
    useTheme,
} from "@mui/material";
import {avatarUrlFix, itemName, productionsView} from "./ItemViewUtil";
import React, {useEffect} from "react";


const VisitView = ({item, selectItemC, selectableItem, setHeader}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const isTablet = useMediaQuery(theme.breakpoints.down('md'));

    useEffect(() => {
        setHeader(item?.date + ' ' + item?.venue?.shortName?.toUpperCase());
    }, [item, setHeader]);

    let avatarUrl = item?.composition?.avatarUrl;

    return (
        <Paper
            elevation={0}
            sx={{
                p: {xs: 1.5, sm: 2, md: 3},
                borderRadius: 3,
                bgcolor: 'background.default',
            }}
        >
            <Grid container spacing={{xs: 2, sm: 3}}>
                {/* Header section with avatar and main info */}
                <Grid item xs={12}>
                    <Paper
                        elevation={2}
                        sx={{
                            p: {xs: 2, sm: 3},
                            borderRadius: 3,
                            display: 'flex',
                            flexDirection: {xs: 'column', sm: 'row'},
                            gap: {xs: 2, sm: 3},
                            alignItems: {xs: 'center', sm: 'flex-start'},
                        }}
                    >
                        {avatarUrl && (
                            <Avatar
                                src={avatarUrlFix(avatarUrl)}
                                alt={itemName(item)}
                                variant="rounded"
                                sx={{
                                    width: {xs: 100, sm: 120, md: 140},
                                    height: {xs: 100, sm: 120, md: 140},
                                    borderRadius: 2,
                                    boxShadow: '0 8px 16px rgba(0,0,0,0.3)',
                                    border: '3px solid rgba(255,255,255,0.3)',
                                    flexShrink: 0,
                                }}
                            />
                        )}
                        <Box sx={{
                            flex: 1,
                            width: {xs: '100%', sm: 'auto'},
                            textAlign: {xs: 'center', sm: 'left'},
                        }}>
                            <Typography
                                variant={isMobile ? 'h6' : 'h5'}
                                fontWeight={700}
                                gutterBottom
                            >
                                {item?.date}
                            </Typography>
                            <Box sx={{mb: 1}}>
                                {selectableItem(item?.venueId, 'venue', item?.venue?.displayName, item?.composition?.type?.avatarUrl)}
                            </Box>
                            {productionsView(item, selectableItem)}
                        </Box>
                    </Paper>
                </Grid>

                {/* Conductor */}
                {item?.conductorId && (
                    <Grid item xs={12}>
                        <Paper
                            elevation={1}
                            sx={{
                                p: {xs: 1.5, sm: 2},
                                borderRadius: 2,
                                display: 'flex',
                                flexDirection: {xs: 'column', sm: 'row'},
                                alignItems: {xs: 'flex-start', sm: 'center'},
                                gap: {xs: 0.5, sm: 1.5},
                            }}
                        >
                            <Typography
                                variant="subtitle2"
                                sx={{
                                    color: 'text.secondary',
                                    minWidth: {sm: 100},
                                }}
                            >
                                Дирижёр:
                            </Typography>
                            {selectableItem(item?.conductorId, 'person', item?.conductor?.displayName, item?.conductor?.avatarUrl, true)}
                        </Paper>
                    </Grid>
                )}

                {/* Artists accordion */}
                <Grid item xs={12}>
                    <Accordion
                        defaultExpanded={item?.artists?.length > 0}
                        sx={{
                            borderRadius: 2,
                            overflow: 'hidden',
                            '&::before': {display: 'none'},
                        }}
                    >
                        <AccordionSummary
                            aria-controls="artists-content"
                            id="artists-header"
                            sx={{
                                bgcolor: 'grey.100',
                                '&:hover': {bgcolor: 'grey.200'},
                                minHeight: {xs: 40, sm: 48},
                            }}
                        >
                            <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <Typography variant={isMobile ? 'body1' : 'subtitle1'} fontWeight={600}>
                                    Исполнители
                                </Typography>
                                <Chip
                                    label={item?.artists?.length || 0}
                                    size="small"
                                    color="primary"
                                    sx={{height: 22}}
                                />
                            </Box>
                        </AccordionSummary>
                        <AccordionDetails sx={{p: {xs: 1.5, sm: 2}}}>
                            <Stack spacing={1.5}>
                                {item?.artists?.map((person) => (
                                    <Paper
                                        key={person.id}
                                        elevation={0}
                                        sx={{
                                            p: {xs: 1, sm: 1.5},
                                            borderRadius: 1.5,
                                            bgcolor: 'grey.50',
                                            '&:hover': {bgcolor: 'grey.100'},
                                            transition: 'background-color 0.2s',
                                        }}
                                    >
                                        {selectableItem(person.id, 'person', person.displayName, person.avatarUrl, true)}
                                    </Paper>
                                ))}
                            </Stack>
                        </AccordionDetails>
                    </Accordion>
                </Grid>

                {/* Attendees accordion */}
                <Grid item xs={12}>
                    <Accordion
                        defaultExpanded={item?.attendees?.length > 0}
                        sx={{
                            borderRadius: 2,
                            overflow: 'hidden',
                            '&::before': {display: 'none'},
                        }}
                    >
                        <AccordionSummary
                            aria-controls="attendees-content"
                            id="attendees-header"
                            sx={{
                                bgcolor: 'grey.100',
                                '&:hover': {bgcolor: 'grey.200'},
                                minHeight: {xs: 40, sm: 48},
                            }}
                        >
                            <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <Typography variant={isMobile ? 'body1' : 'subtitle1'} fontWeight={600}>
                                    Посетители
                                </Typography>
                                <Chip
                                    label={item?.attendees?.length || 0}
                                    size="small"
                                    color="secondary"
                                    sx={{height: 22}}
                                />
                            </Box>
                        </AccordionSummary>
                        <AccordionDetails sx={{p: {xs: 1.5, sm: 2}}}>
                            <Stack spacing={1.5}>
                                {item?.attendees?.map((person) => (
                                    <Paper
                                        key={person.id}
                                        elevation={0}
                                        sx={{
                                            p: {xs: 1, sm: 1.5},
                                            borderRadius: 1.5,
                                            bgcolor: 'grey.50',
                                            '&:hover': {bgcolor: 'grey.100'},
                                            transition: 'background-color 0.2s',
                                        }}
                                    >
                                        {selectableItem(person.id, 'person', person.displayName)}
                                    </Paper>
                                ))}
                            </Stack>
                        </AccordionDetails>
                    </Accordion>
                </Grid>

                {/* Ticket price and Notes row */}
                <Grid item xs={12}>
                    <Grid container spacing={{xs: 2, sm: 3}}>
                        {/* Ticket price */}
                        {item?.ticketPrice && (
                            <Grid item xs={12} sm={isTablet ? 12 : 4}>
                                <Paper
                                    elevation={1}
                                    sx={{
                                        p: {xs: 1.5, sm: 2},
                                        borderRadius: 2,
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: 1,
                                        height: '100%',
                                    }}
                                >
                                    <Typography variant="subtitle2" color="text.secondary">
                                        Цена билета:
                                    </Typography>
                                    <Typography
                                        variant={isMobile ? 'body1' : 'h6'}
                                        fontWeight={600}
                                        color="primary"
                                    >
                                        {item?.ticketPrice}
                                    </Typography>
                                </Paper>
                            </Grid>
                        )}

                        {/* Notes */}
                        {item?.notes && (
                            <Grid item xs={12} sm={isTablet ? 12 : 8}>
                                <Paper
                                    elevation={1}
                                    sx={{
                                        p: {xs: 1.5, sm: 2},
                                        borderRadius: 2,
                                    }}
                                >
                                    <Typography
                                        variant="subtitle2"
                                        color="text.secondary"
                                        gutterBottom
                                    >
                                        Примечания
                                    </Typography>
                                    <Divider sx={{mb: 1.5}} />
                                    <TextareaAutosize
                                        readOnly
                                        minRows={2}
                                        maxRows={8}
                                        style={{
                                            width: '100%',
                                            border: 'none',
                                            outline: 'none',
                                            resize: 'none',
                                            fontFamily: 'inherit',
                                            fontSize: isMobile ? '0.8125rem' : '0.875rem',
                                            lineHeight: 1.6,
                                            color: 'inherit',
                                            backgroundColor: 'transparent',
                                        }}
                                    >
                                        {item?.notes}
                                    </TextareaAutosize>
                                </Paper>
                            </Grid>
                        )}
                    </Grid>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default VisitView
