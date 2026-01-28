import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Avatar,
    Grid,
    Skeleton,
    Stack,
    TextareaAutosize
} from "@mui/material";
import {
    avatarUrlFix,
    compositionsView, CompositionView,
    createVisitsDisplay,
    Item,
    itemName,
    productionDirectorsView
} from "./ItemViewUtil";
import React, {useEffect} from "react";
import {fetchVisits} from "./util";


const ProductionView = ({item, selectItemC, selectableItem, setHeader}) => {
    useEffect(() => {
        setHeader(item?.composition?.displayName?.toUpperCase()+" ("+item?.directors?.map(d=>d.displayName)+")");
    }, [item, setHeader]);
    let avatarSize = 128;
    let avatarUrl = item?.avatarUrl ? item?.avatarUrl : item?.composition?.avatarUrl;

    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        productionId: item?.id
    }))

    return (<div>
        <Grid spacing={2}>
            <Grid container spacing={2}>
                {avatarUrl ? (
                    <Avatar
                        src={avatarUrlFix(avatarUrl)}
                        alt={itemName(item)}
                        variant="square"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 8,
                            borderColor: '#000000'
                        }}
                    />) : (<div/>)}
                {CompositionView(item?.composition, selectableItem)}
                {productionDirectorsView(item, selectableItem)}
            </Grid>
            {visitsDisplay}


        </Grid>
    </div>)
}

export default ProductionView