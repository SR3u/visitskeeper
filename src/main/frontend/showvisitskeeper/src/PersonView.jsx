import {fetchCompositions, fetchProductions, fetchVisits, translatedPersonType} from "./util";
import {
    avatarUrlFix,
    createCompositionsDisplay,
    createProductionsDisplay,
    createVisitsDisplay,
    Item,
    itemName
} from "./ItemViewUtil";
import {Avatar, Grid, Skeleton, Stack} from "@mui/material";
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
    //console.log(item)
    if(!item) {
        return (
            <div>
                <Skeleton></Skeleton>
            </div>
        )
    }
    let avatarSize = 128;
    return (
        <div>
            <Item>
                <Grid container spacing={2}>
                    {item ? (
                    <Avatar
                        src={avatarUrlFix(item?.avatarUrl)}
                        alt={itemName(item)}
                        variant="square"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 8,
                            borderColor: '#000000'
                        }}
                    />) : (<Skeleton variant="circle" width={avatarSize} height={avatarSize} />)}
                    <Stack spacing={2}>
                        {item ? (<Item>
                            Имя: {itemName(item)}
                        </Item>) : (<Skeleton variant="rectangular"/>)}
                        {item ? (<Item>Кто: {translatedPersonType(item?.type)}</Item>) : (<Skeleton variant="rectangular"/>)}
                    </Stack>
                    {compositionsDisplay}
                    {productionsDisplay}
                    {visitsDisplay}
                </Grid>
            </Item>



        </div>
    )
}

export default PersonView