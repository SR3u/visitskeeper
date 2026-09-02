import {fetchCompositions, fetchVisits} from "./util";
import {avatarUrlFix, createCompositionsDisplay, createVisitsDisplay, itemName} from "./ItemViewUtil";
import {
    Avatar,
    Box,
    Grid,
    Paper,
    Skeleton,
    Typography,
    useMediaQuery,
    useTheme,
} from "@mui/material";
import React, {useEffect} from "react";

const VenueView = ({item, selectItemC, setHeader}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const isTablet = useMediaQuery(theme.breakpoints.down('md'));

    useEffect(() => {
        setHeader(itemName(item));
    }, [item, setHeader]);

    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        venueId: item.id
    }));
    let compositionsDisplay = createCompositionsDisplay(selectItemC, (page, pageSize) => fetchCompositions({
        page: page,
        pageSize: pageSize,
        venueId: item.id
    }));

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
                        {item ? (
                            <Avatar
                                src={avatarUrlFix(item.avatarUrl)}
                                alt={itemName(item)}
                                variant="rounded"
                                sx={{
                                    width: {xs: 100, sm: 120, md: 140},
                                    height: {xs: 100, sm: 120, md: 140},
                                    borderRadius: 2,
                                    boxShadow: 3,
                                    border: '3px solid',
                                    borderColor: 'divider',
                                    flexShrink: 0,
                                }}
                            />
                        ) : (
                            <Skeleton
                                variant="rounded"
                                sx={{
                                    width: {xs: 100, sm: 120, md: 140},
                                    height: {xs: 100, sm: 120, md: 140},
                                    borderRadius: 2,
                                }}
                            />
                        )}
                        <Box sx={{
                            flex: 1,
                            width: {xs: '100%', sm: 'auto'},
                            textAlign: {xs: 'center', sm: 'left'},
                        }}>
                            {item ? (
                                <Typography
                                    variant={isMobile ? 'h6' : 'h5'}
                                    fontWeight={700}
                                    gutterBottom
                                >
                                    {itemName(item)}
                                </Typography>
                            ) : (
                                <Skeleton variant="text" width={200} height={32} />
                            )}
                            {item?.fullName && item?.displayName && (
                                <Typography variant="body2" color="text.secondary">
                                    {item.displayName}
                                </Typography>
                            )}
                        </Box>
                    </Paper>
                </Grid>

                <Grid item xs={12}>
                    {compositionsDisplay}
                </Grid>

                <Grid item xs={12}>
                    {visitsDisplay}
                </Grid>
            </Grid>
        </Paper>
    );
};

export default VenueView;