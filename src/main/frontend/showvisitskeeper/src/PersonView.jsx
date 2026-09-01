import {fetchCompositions, fetchProductions, fetchVisits, translatedPersonType} from "./util";
import {
    avatarUrlFix,
    createCompositionsDisplay,
    createProductionsDisplay,
    createVisitsDisplay,
    itemName
} from "./ItemViewUtil";
import {Card, CardContent, CardMedia, Skeleton, Stack, Typography, useMediaQuery, useTheme} from "@mui/material";
import Box from "@mui/material/Box";
import React, {useEffect} from "react";

const PersonView = ({item, selectItemC, setHeader}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const isTablet = useMediaQuery(theme.breakpoints.between('sm', 'md'));

    useEffect(() => {
        setHeader(itemName(item));
    }, [item, setHeader]);
    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page, pageSize: pageSize,
        directorId: item?.id, conductorId: item?.id,
        composerId: item?.id,
        artistId: item?.id, attendeeId: item?.id
    }))
    let compositionsDisplay = createCompositionsDisplay(selectItemC,(page, pageSize) => {
        return fetchCompositions({
            page: page, pageSize: pageSize,
            directorId: item?.id, conductorId: item?.id,
            composerId: item?.id,
            artistId: item?.id, attendeeId: item?.id
        })
    })
    let productionsDisplay = createProductionsDisplay(selectItemC,(page, pageSize) => {
        return fetchProductions({
            page: page, pageSize: pageSize,
            directorId: item?.id, conductorId: item?.id,
            composerId: item?.id,
            artistId: item?.id, attendeeId: item?.id
        })
    })

    let avatarSize = isMobile ? 96 : isTablet ? 112 : 128;

    if(!item) {
        return (
            <Card sx={{m: {xs: 1, sm: 2}}}>
                <CardContent>
                    <Stack spacing={2}>
                        <Skeleton variant="rectangular" height={avatarSize + 32} />
                        <Skeleton variant="text" width="60%" />
                        <Skeleton variant="text" width="40%" />
                    </Stack>
                </CardContent>
            </Card>
        )
    }

    if (isMobile) {
        return (
            <Card sx={{m: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden'}}>
                <Box sx={{display: 'flex', justifyContent: 'center', pt: 2}}>
                    <CardMedia
                        component="img"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 2,
                        }}
                        image={avatarUrlFix(item?.avatarUrl)}
                        alt={itemName(item)}
                    />
                </Box>
                <CardContent>
                    <Typography variant="h6" component="div" sx={{textAlign: 'center', mb: 0.5}}>
                        {itemName(item)}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{textAlign: 'center', mb: 2}}>
                        {translatedPersonType(item?.type)}
                    </Typography>
                    {compositionsDisplay}
                    {productionsDisplay}
                    {visitsDisplay}
                </CardContent>
            </Card>
        )
    }

    return (
        <Card sx={{m: 2, overflow: 'hidden'}}>
            <CardContent>
                <Box sx={{display: 'flex', alignItems: 'center', gap: 2, mb: 2}}>
                    <Box sx={{flex: 1}}>
                        <Typography variant={isTablet ? 'h6' : 'h5'} component="div" sx={{mb: 0.5}}>
                            {itemName(item)}
                        </Typography>
                        <Typography variant={isTablet ? 'body2' : 'subtitle1'} color="text.secondary">
                            {translatedPersonType(item?.type)}
                        </Typography>
                    </Box>
                    <CardMedia
                        component="img"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 2,
                            flexShrink: 0
                        }}
                        image={avatarUrlFix(item?.avatarUrl)}
                        alt={itemName(item)}
                    />
                </Box>
                {compositionsDisplay}
                {productionsDisplay}
                {visitsDisplay}
            </CardContent>
        </Card>
    )
}

export default PersonView
