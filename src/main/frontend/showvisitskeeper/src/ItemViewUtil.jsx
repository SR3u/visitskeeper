import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Typography,
    Paper,
    Avatar,
    Skeleton,
    Grid,
    Stack
} from "@mui/material";
import GridView from "./GridView";
import React from "react";
import {styled} from '@mui/material/styles';

let subfieldDisplayName = (f) => f?.displayName;

let subfieldDisplayNames = (f) => {
    if (Array.isArray(f)) {
        return f.map((i) => {
            return subfieldDisplayName(i)
        }).join(",");
    } else {
        return subfieldDisplayName(f)
    }
}

function productionsCompositionName(ps) {
    if (Array.isArray(ps)) {
        return ps.map((p) => {
            return subfieldDisplayName(p?.composition)
        }).join(",");
    } else {
        return subfieldDisplayName(ps?.composition)
    }
}

export function createVisitsDisplay(onItemClick, fetchFunc) {
    let header = "Посещения";
    return (
        <Accordion trigger={header}>
            <AccordionSummary
                //expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1-content"
                id="panel1-header"
            >
                <Typography component="span">{header}</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <GridView
                    columns={[
                        {field: 'date', headerName: 'Дата', width: 100},
                        {
                            field: 'productions',
                            valueGetter: productionsCompositionName,
                            headerName: 'Произведение',
                            width: 200
                        },
                        {
                            field: 'venue',
                            valueGetter: subfieldDisplayName,
                            headerName: 'Площадка',
                            width: 120
                        },
                    ]}
                    fetchItems={fetchFunc}
                    itemsType={'visit'}
                    onItemClick={onItemClick}
                />
            </AccordionDetails>
        </Accordion>
    )
}

export function createCompositionsDisplay(onItemClick, fetchFunc) {
    let header = "Произведения";
    return (
        <Accordion trigger={header}>
            <AccordionSummary
                //expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1-content"
                id="panel1-header"
            >
                <Typography component="span">{header}</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <GridView
                    columns={[
                        {field: 'displayName', headerName: 'Имя', width: 200},
                        {field: 'type', valueGetter: subfieldDisplayName, headerName: 'Тип', width: 80},
                    ]}
                    fetchItems={fetchFunc}
                    itemsType={'composition'}
                    onItemClick={onItemClick}
                />
            </AccordionDetails>
        </Accordion>

    )
}

export function createProductionsDisplay(onItemClick, fetchFunc) {
    let header = "Постановки";
    return (
        <Accordion trigger={header}>
            <AccordionSummary
                //expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1-content"
                id="panel1-header"
            >
                <Typography component="span">{header}</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <GridView
                    columns={[
                        //{field: 'displayName', headerName: 'Имя', width: 200},
                        {field: 'displayName', headerName: 'Имя', width: 360},
                        //{field: 'type', valueGetter: subfieldDisplayName, headerName: 'Тип', width: 80},
                    ]}
                    fetchItems={fetchFunc}
                    itemsType={'production'}
                    onItemClick={onItemClick}
                />
            </AccordionDetails>
        </Accordion>

    )
}

export function avatarUrlFix(avatarUrl) {
    if (!avatarUrl) {
        avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyuW3mEEsxF2ck3SFVq5yho3Kva3Yyt-jSCg&s"
    }
    return avatarUrl;
}

export const Item = styled(Paper)(({theme}) => ({
    backgroundColor: '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: (theme.vars ?? theme).palette.text.secondary,
    ...theme.applyStyles('dark', {
        backgroundColor: '#1A2027',
    }),
}));


export function itemName(item) {
    return item?.fullName ? item?.fullName : item?.displayName
}


export function compositionView(composition, selectableItem) {
    let avatarSize = 128
    let avatarWidth = avatarSize
    let avatarHeight = avatarSize
    return (
        <Grid container spacing={2} maxWidth={400} display="flex">
            <Grid maxWidth={avatarWidth}
                  maxHeight={avatarHeight}>
                <Avatar
                    src={avatarUrlFix(composition?.avatarUrl)}
                    sx={{
                        width: avatarWidth,
                        height: avatarHeight,
                        borderRadius: 8,
                        borderColor: '#000000'
                    }}
                />
            </Grid>
            <Grid maxWidth={400 - avatarWidth}>
                <Grid container
                      spacing={2}
                      maxWidth={200}
                      columns={2}
                      direction="row"
                      display="flex"
                      sx={{
                          justifyContent: "left",
                          alignItems: "stretch",
                      }}
                >
                    <Grid>
                        {selectableItem(composition?.id, 'composition', composition?.displayName, undefined)}
                    </Grid>
                    <Grid>
                        {selectableItem(composition?.typeId, 'composition_type', composition?.type?.displayName, composition?.type?.avatarUrl)}
                    </Grid>
                    <Grid>
                        {composition?.composerIds?.length === 1 ?
                            (
                                <Grid>
                                    <Item>
                                        Композитор: {selectableItem(composition?.composerIds[0], 'person', composition?.composers[0]?.displayName)}
                                    </Item>
                                </Grid>
                            )
                            :
                            (
                                <Grid maxWidth={400}>
                                    <Item>Композиторы:</Item>
                                    {composition?.composers.map(composer =>
                                        (<Item>{selectableItem(composer?.id, 'person',
                                            composer?.displayName)}</Item>)
                                    )}
                                </Grid>
                            )}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>);
}

export function compositionsView(item, selectableItem) {
    if (item?.compositions) {
        return (<div>
            {item?.compositions.map((composition) =>
                compositionView(composition, selectableItem)
            )}
        </div>);
    }
    return (<Skeleton variant="rectangular" loading={true}></Skeleton>)
}

export function productionDirectorsView(production, selectableItem) {
    return <div>{production?.directors?.length > 1 ?
        "Режиссёры:" :
        "Режиссёр:"
    }
        {production?.directors?.map(director => selectableItem(director?.id, 'person', director?.displayName))}
    </div>;
}

export function productionView(selectableItem, production) {
    return (<Item>
        {selectableItem(production?.id, 'production', 'Постановка')}
        {compositionView(production?.composition, selectableItem)}
        {productionDirectorsView(production, selectableItem)}
    </Item>);
}

export function productionsView(item, selectableItem) {
    if (item?.productions) {
        return item?.productions?.map((production) => productionView(selectableItem, production))
    }
    return (<Skeleton variant="rectangular" loading={true}></Skeleton>)
}