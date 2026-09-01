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
} from "@mui/material";
import {avatarUrlFix, itemName, productionsView} from "./ItemViewUtil";
import React, {useEffect} from "react";


const VisitView = ({item, selectItemC, selectableItem, setHeader}) => {
    useEffect(() => {
        setHeader(item?.date + ' ' + item?.venue?.shortName?.toUpperCase());
    }, [item, setHeader]);
    let avatarSize = 140;
    let avatarUrl = item?.composition?.avatarUrl;

    return (
        <Paper
            elevation={0}
            sx={{
                p: 3,
                borderRadius: 3,
                bgcolor: 'background.default',
            }}
        >
            <Grid container spacing={3}>
                {/* Header section with avatar and main info */}
                <Grid item xs={12}>
                    <Paper
                        elevation={2}
                        sx={{
                            p: 3,
                            borderRadius: 3,
                            display: 'flex',
                            gap: 3,
                            alignItems: 'flex-start',
                            //background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            //color: 'white',
                        }}
                    >
                        {avatarUrl && (
                            <Avatar
                                src={avatarUrlFix(avatarUrl)}
                                alt={itemName(item)}
                                variant="rounded"
                                sx={{
                                    width: avatarSize,
                                    height: avatarSize,
                                    borderRadius: 2,
                                    boxShadow: '0 8px 16px rgba(0,0,0,0.3)',
                                    border: '3px solid rgba(255,255,255,0.3)',
                                    flexShrink: 0,
                                }}
                            />
                        )}
                        <Box sx={{flex: 1}}>
                            <Typography variant="h5" fontWeight={700} gutterBottom>
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
                                p: 2,
                                borderRadius: 2,
                                display: 'flex',
                                alignItems: 'center',
                                gap: 1.5,
                            }}
                        >
                            <Typography
                                variant="subtitle2"
                                sx={{
                                    color: 'text.secondary',
                                    minWidth: 100,
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
                            }}
                        >
                            <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <Typography variant="subtitle1" fontWeight={600}>
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
                        <AccordionDetails sx={{p: 2}}>
                            <Stack spacing={1.5}>
                                {item?.artists?.map((person) => (
                                    <Paper
                                        key={person.id}
                                        elevation={0}
                                        sx={{
                                            p: 1.5,
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
                            }}
                        >
                            <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <Typography variant="subtitle1" fontWeight={600}>
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
                        <AccordionDetails sx={{p: 2}}>
                            <Stack spacing={1.5}>
                                {item?.attendees?.map((person) => (
                                    <Paper
                                        key={person.id}
                                        elevation={0}
                                        sx={{
                                            p: 1.5,
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

                {/* Ticket price */}
                {item?.ticketPrice && (
                    <Grid item xs={12} sm={6}>
                        <Paper
                            elevation={1}
                            sx={{
                                p: 2,
                                borderRadius: 2,
                                display: 'flex',
                                alignItems: 'center',
                                gap: 1,
                            }}
                        >
                            <Typography variant="subtitle2" color="text.secondary">
                                Цена билета:
                            </Typography>
                            <Typography variant="h6" fontWeight={600} color="primary">
                                {item?.ticketPrice}
                            </Typography>
                        </Paper>
                    </Grid>
                )}

                {/* Notes */}
                {item?.notes && (
                    <Grid item xs={12}>
                        <Paper
                            elevation={1}
                            sx={{
                                p: 2,
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
                                    fontSize: '0.875rem',
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
        </Paper>
    );
}

export default VisitView
