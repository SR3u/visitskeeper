import {fetchCompositions, fetchProductions, fetchVisits, translatedPersonType} from "./util";
import {
    avatarUrlFix,
    createCompositionsDisplay,
    createProductionsDisplay,
    createVisitsDisplay,
    itemName
} from "./ItemViewUtil";
import {Card, CardContent, CardMedia, Skeleton, Stack, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import React, {useEffect} from "react";

const PersonView = ({item, selectItemC, setHeader}) => {
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

    if(!item) {
        return (
            <Card sx={{m: 2}}>
                <CardContent>
                    <Stack spacing={2}>
                        <Skeleton variant="rectangular" height={150} />
                        <Skeleton variant="text" width="60%" />
                        <Skeleton variant="text" width="40%" />
                    </Stack>
                </CardContent>
            </Card>
        )
    }

    let avatarSize = 128;
    return (
        <Card sx={{m: 2, display: 'flex', overflow: 'hidden'}}>
            <Box sx={{display: 'flex', flexDirection: 'column', flex: 1}}>
                <CardContent sx={{flex: '1 0 auto'}}>
                    <Typography variant="h5" component="div" sx={{mb: 0.5}}>
                        {itemName(item)}
                    </Typography>
                    <Typography variant="subtitle1" color="text.secondary" sx={{mb: 2}}>
                        {translatedPersonType(item?.type)}
                    </Typography>
                </CardContent>
                <Box sx={{p: 2, pt: 0}}>
                    {compositionsDisplay}
                    {productionsDisplay}
                    {visitsDisplay}
                </Box>
            </Box>
            <CardMedia
                component="img"
                sx={{
                    width: avatarSize,
                    height: avatarSize,
                    borderRadius: 2,
                    alignSelf: 'center',
                    mr: 2
                }}
                image={avatarUrlFix(item?.avatarUrl)}
                alt={itemName(item)}
            />
        </Card>
    )
}

export default PersonView
