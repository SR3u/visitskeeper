import {fetchCompositions, fetchVisits} from "./util";
import {avatarUrlFix, createCompositionsDisplay, createVisitsDisplay, Item, itemName} from "./ItemViewUtil";
import {Avatar, Grid, Skeleton, Stack} from "@mui/material";
import React, {useEffect} from "react";

const CompostitionTypeView = ({item, selectItemC, setHeader}) => {
    useEffect(() => {
        setHeader(itemName(item));
    }, [item, setHeader]);
    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        compositionTypeId: item.id
    }))
    let compositionsDisplay = createCompositionsDisplay(selectItemC, (page, pageSize) => fetchCompositions({
        page: page,
        pageSize: pageSize,
        compositionTypeId: item.id
    }))
    return (
        <div>
            <Stack spacing={2}>
                <Item>{item.displayName}</Item>
            </Stack>
            {compositionsDisplay}
            {visitsDisplay}
        </div>
    )
}

export default CompostitionTypeView