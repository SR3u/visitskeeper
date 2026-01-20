import {fetchCompositions, fetchVisits} from "./util";
import {avatarUrlFix, createCompositionsDisplay, createVisitsDisplay, Item, itemName} from "./ItemViewUtil";
import {Avatar, Grid, Skeleton, Stack} from "@mui/material";
import React from "react";

const CompositionView = ({item, selectItemC, selectableItem, setHeader}) => {
    setHeader(itemName(item));
    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        compositionId: item?.id
    }))
    return (
        <div>
            <Item>
                <Grid container spacing={2}>
                    <Avatar
                        src={avatarUrlFix(item?.avatarUrl)}
                        alt={itemName(item)}
                        variant="square"
                        sx={{
                            width: 168,
                            height: 168,
                            borderRadius: 8,
                            borderColor: '#000000'
                        }}
                    />
                    <Stack spacing={2}>
                        <Item>
                            {itemName(item)}
                        </Item>
                        <Item>{selectableItem(item?.typeId, 'composition_type', item?.type?.displayName)}</Item>
                        <Item>Композитор: {selectableItem(item?.composerId, 'person', item?.composer?.displayName)}</Item>
                    </Stack>
                    {visitsDisplay}
                </Grid>
            </Item>
        </div>
    )
}

export default CompositionView