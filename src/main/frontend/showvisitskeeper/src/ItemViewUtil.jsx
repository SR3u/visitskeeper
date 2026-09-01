import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Typography,
    Paper,
    Avatar,
    Skeleton,
    Grid,
    Stack, Card, CardContent, IconButton,
    Box, CardMedia
} from "@mui/material";
import {useTheme} from "@mui/material/styles";
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

export function avatarUrlFix(avatarUrl, type = 'unknown') {
    if (type === 'person') {
        return personAvatarUrlFix(avatarUrl)
    }
    return generalAvatarUrlFix(avatarUrl)
}

export function generalAvatarUrlFix(avatarUrl) {
    if (!avatarUrl) {
        avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0I-0FDp-wBLGtpeOT-IGF8JHcmRAQiQBWneXpGk8RPA&s=10"
    }
    return avatarUrl;
}

export function personAvatarUrlFix(avatarUrl) {
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


export function CompositionView(composition, selectableItem) {
    const theme = useTheme();
    let avatarSize = 128
    let avatarWidth = avatarSize
    let avatarHeight = avatarSize
    return (
        <Card sx={{display: 'flex'}}>
            <Box sx={{display: 'flex', flexDirection: 'column'}}>
                <CardContent sx={{flex: '1 0 auto'}}>
                    <Typography component="div" variant="h5">
                        {selectableItem(composition?.id, 'composition', composition?.displayName, undefined)}
                    </Typography>
                    <Typography
                        variant="subtitle1"
                        component="div"
                        sx={{color: 'text.secondary'}}
                    >
                        {selectableItem(composition?.typeId, 'composition_type', composition?.type?.displayName, composition?.type?.avatarUrl)}
                    </Typography>
                    <Typography>
                        {composition?.composerIds?.length <= 1 ?
                            (
                                <Typography
                                    variant="body2">Композитор:{selectableItem(composition?.composerIds[0], 'person', composition?.composers[0]?.displayName)}</Typography>
                            )
                            :
                            (
                                <Stack maxWidth={400}>
                                    <Typography variant="body2">Композиторы:</Typography>
                                    {composition?.composers.map(composer =>
                                        (<Typography variant="body2">{selectableItem(composer?.id, 'person',
                                            composer?.displayName)}</Typography>)
                                    )}
                                </Stack>
                            )}
                    </Typography>
                </CardContent>
            </Box>
            {composition?.avatarUrl ? (
                <CardMedia
                    component="img"
                    sx={{width: avatarWidth}}
                    image={avatarUrlFix(composition?.avatarUrl)}
                    alt=""
                />
            ) : (<div/>)
            }
        </Card>
    );
}

export function compositionsView(item, selectableItem) {
    if (item?.compositions) {
        return (<div>
            {item?.compositions.map((composition) =>
                CompositionView(composition, selectableItem)
            )}
        </div>);
    }
    return (<Skeleton variant="rectangular" loading={true}></Skeleton>)
}

export function productionDirectorsView(production, selectableItem) {
    return <Typography variant="body2">{production?.directors?.length > 1 ?
        "Режиссёры:" :
        "Режиссёр:"
    }
        {production?.directors?.map(director => selectableItem(director?.id, 'person', director?.displayName))}
    </Typography>;
}

export function productionView(selectableItem, production) {
    let avatarSize = 128
    let avatarWidth = avatarSize
    let avatarHeight = avatarSize
    return (<Card sx={{display: 'flex'}}>
        {production?.avatarUrl ? (
            <CardMedia
                component="img"
                height={avatarHeight}
                image={avatarUrlFix(production?.avatarUrl)}
                alt=""
            />
        ) : (<div/>)
        }
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <CardContent sx={{flex: '1 0 auto'}}>
                {selectableItem(production?.id, 'production', 'Постановка')}
                {CompositionView(production?.composition, selectableItem)}
                {productionDirectorsView(production, selectableItem)}
            </CardContent>
        </Box>

    </Card>);
}

export function productionsView(item, selectableItem) {
    if (item?.productions) {
        return item?.productions?.map((production) => productionView(selectableItem, production))
    }
    return (<Skeleton variant="rectangular" loading={true}></Skeleton>)
}