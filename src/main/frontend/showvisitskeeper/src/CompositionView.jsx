import {fetchProductions, fetchVisits} from "./util";
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

const CompositionView = ({item, selectItemC, selectableItem, setHeader}) => {
    useEffect(() => {
        setHeader(itemName(item));
    }, [item, setHeader]);
    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        compositionId: item?.id
    }))
    let productionsDisplay = createProductionsDisplay(selectItemC,(page, pageSize) => {
        return fetchProductions({
            page: page, pageSize: pageSize,
            compositionId: item?.id
        })
    })
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
                        {item?.composerIds?.length === 1 ?
                            (
                                <Item>Композитор: {selectableItem(item?.composerIds[0], 'person', item?.composers[0]?.displayName)}</Item>)
                            :
                            (
                                <Item>Композиторы:
                                    {item?.composers?.map(composer =>
                                        (<Item>{selectableItem(composer?.id, 'person',
                                            composer?.displayName)}</Item>)
                                    )}</Item>
                            )}
                    </Stack>
                    {productionsDisplay}
                    {
                        visitsDisplay
                    }
                </Grid>
            </Item>
        </div>
    )
}

export default CompositionView