import {fetchCompositions, fetchVisits} from "./util";
import {avatarUrlFix, createCompositionsDisplay, createVisitsDisplay, Item, itemName} from "./ItemViewUtil";
import {Avatar, Grid, Skeleton, Stack} from "@mui/material";
import React from "react";

const VenueView = ({item, selectItemC, setHeader}) => {
    setHeader(itemName(item));
    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        venueId: item.id
    }))
    let compositionsDisplay = createCompositionsDisplay(selectItemC, (page, pageSize) => fetchCompositions({
        page: page,
        pageSize: pageSize,
        venueId: item.id
    }))
    let avatarSize = 128
    return (
        <Item>
            <Grid container spacing={2}>
                {item ? (
                    <Avatar
                        src={avatarUrlFix(item.avatarUrl)}
                        alt={itemName(item)}
                        variant="square"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 8,
                            borderColor: '#000000'
                        }}
                    />) : (<Skeleton variant="circle" width={avatarSize} height={avatarSize}/>)}
                <Stack spacing={2}>
                    {item ? (<Item>
                        {itemName(item)}
                    </Item>) : (<Skeleton variant="rectangular"/>)}
                </Stack>

                    {compositionsDisplay}
                    {visitsDisplay}

            </Grid>


        </Item>
    )
}

export default VenueView