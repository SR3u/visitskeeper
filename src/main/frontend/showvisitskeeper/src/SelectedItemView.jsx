import React, {useCallback, useEffect, useReducer, useState} from "react";
import {Accordion, AccordionDetails, AccordionSummary, Button, Grid, Paper, Stack, Typography} from "@mui/material";
import GridView from "./GridView";
import {styled} from '@mui/material/styles';

const BASE_URL = 'http://localhost:8080'
const SEARCH_URL = BASE_URL + '/search/json'
const PAGES_URL = BASE_URL + '/search/pages'
const ITEM_URL = BASE_URL + '/item/'

function fetchSearch(type, p) {
    var params = new URLSearchParams(
        p
    )
    if (!type) {
        type = 'PERSON'
    }
    type = type.toLowerCase()
    var fetchUrl = ITEM_URL + type + '/search' + '?' + params
    //console.log(fetchUrl)
    return fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    }).then(res => res.json())
}

function fetchVisits(p) {
    return fetchSearch('visit', p)
}

function fetchCompositions(p) {
    return fetchSearch('composition', p)
}

function fetchItem(itemId, itemType) {
    var params = new URLSearchParams({
        'id': itemId,
    })
    var type = itemType
    if (!type) {
        type = 'PERSON'
    }
    type = type.toLowerCase()
    var fetchUrl = ITEM_URL + type + '?' + params
    //console.log(fetchUrl)
    return fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    }).then(res => res.json())
}

const Item = styled(Paper)(({theme}) => ({
    backgroundColor: '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: (theme.vars ?? theme).palette.text.secondary,
    ...theme.applyStyles('dark', {
        backgroundColor: '#1A2027',
    }),
}));

const SelectedItemView = ({initialItem}) => {
    const [item, setItem] = useState({});

    const [compositionPaginationModel, setCompositionPaginationModel] = React.useState({
        page: 0,
        pageSize: 10,
    });
    const [visitPaginationModel, setVisitPaginationModel] = React.useState({
        page: 0,
        pageSize: 10,
    });

    const [totalCompositions, setTotalCompositions] = React.useState(0);
    const [totalVisits, setTotalVisits] = React.useState(0);

    useEffect(() => {
        const newItem = initialItem
        //console.log(initialItem)
        setItem(newItem)
    }, [initialItem])

    const [, forceUpdate] = useReducer(x => x + 1, 0);

    // console.log('item', item);
    // console.log('initialItem', initialItem);

    const displaySelected = useCallback((item) => setItem(item), [setItem]);

    const selectItem = useCallback((id, type) => {
        fetchItem(id, type)
            .then(p => displaySelected(p, p['_type']))
    }, [displaySelected]);

    const selectItemC = useCallback((id, type) => selectItem(id, type), [selectItem]);


    function selectableItem(id, type, text) {
        return (
            <Button
                onClick={() => selectItem(id, type)}
            >{text}</Button>
        )
    }

    function onCellClickF(params, event, details) {
        // console.log('params', params)
        // console.log('event',event)
        // console.log('details',details)
        let id = params.row.uuid;
        let type = params.row.itemType
        selectItem(id, type)
    }

    let subfieldDisplayName = (f) => f?.displayName;

    function createVisitsDisplay(fetchFunc) {
        let header = "Посещения";
        return (
            <tr>
                <td><Accordion trigger={header}>
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
                                {field: 'date', headerName: 'Дата', width: 200},
                                {
                                    field: 'composition',
                                    valueGetter: subfieldDisplayName,
                                    headerName: 'Произведение',
                                    width: 200
                                },
                                {
                                    field: 'venue',
                                    valueGetter: subfieldDisplayName,
                                    headerName: 'Площадка',
                                    width: 200
                                },
                            ]}
                            fetchItems={fetchFunc}
                            itemsType={'visit'}
                            onItemClick={selectItemC}
                        />
                    </AccordionDetails>
                </Accordion>
                </td>
            </tr>
        )
    }

    function createCompositionsDisplay(fetchFunc) {
        let header = "Произведения";
        return (
            <tr>
                <td><Accordion trigger={header}>
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
                                {field: 'type', valueGetter: subfieldDisplayName, headerName: 'Тип', width: 200},
                            ]}
                            fetchItems={fetchFunc}
                            itemsType={'composition'}
                            onItemClick={selectItemC}
                        />
                    </AccordionDetails>
                </Accordion>
                </td>
            </tr>
        )
    }

    const emptyData = {content: [], pages: {page: 0, items: 0}}

    var visitsDisplay = createVisitsDisplay(() => emptyData)
    var compositionsDisplay = createCompositionsDisplay(() => emptyData)

    if (!item || !item['_type']) {
        return (<label>Nothing Selected</label>)
    }

    switch (item['_type']) {
        case 'venue':
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({
                page: page,
                pageSize: pageSize,
                venueId: item.id
            }))
            compositionsDisplay = createCompositionsDisplay(() => fetchCompositions({
                venueId: item.id
            }))
            return (
                <div>
                    <Stack spacing={2}>
                        <Item>{item.displayName}</Item>
                        <Item>Композитор: {selectableItem(item.composerId, 'person', item.composer?.displayName)}</Item>
                    </Stack>
                    {compositionsDisplay}
                    {visitsDisplay}
                </div>
            )
        case 'visit':
            return (
                <div>
                    <Stack spacing={2}>
                        <Grid container spacing={2}>
                            <Item>{item.date}</Item>
                            <Item>{selectableItem(item.compositionId, 'composition', item.composition?.displayName)}</Item>
                            <Item>{selectableItem(item.composition?.composerId, 'person', item.composition?.composer?.displayName)}</Item>
                            <Item>{selectableItem(item.venueId, 'venue', item.venue?.displayName)}</Item>
                        </Grid>
                        <Item>Режиссёр: {selectableItem(item.directorId, 'person', item.director?.displayName)}</Item>
                        <Item>Дирижёр: {selectableItem(item.conductorId, 'person', item.conductor?.displayName)}</Item>
                        <Item>Композитор: {selectableItem(item.composerId, 'person', item.composer?.displayName)}</Item>
                        <Item>Исполнители:
                            <Stack spacing={2}>
                                {item.artists.map((person) => (
                                    <Item>
                                        {selectableItem(person.id, 'person', person.displayName)}
                                    </Item>))}
                            </Stack>
                        </Item>
                        <Item>Посетители:
                            <Stack spacing={2}>
                                {item.attendees.map((person) => (
                                    <Item>
                                        {selectableItem(person.id, 'person', person.displayName)}
                                    </Item>))}
                            </Stack>
                        </Item>
                    </Stack>
                </div>
            )
        case 'composition':
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({
                page: page,
                pageSize: pageSize,
                compositionId: item.id
            }))
            return (
                <div>
                    <Stack spacing={2}>
                        <Item>{item.type.displayName}</Item>
                        <Item>{item.displayName}</Item>
                        <Item>Композитор: {selectableItem(item.composerId, 'person', item.composer?.displayName)}</Item>
                    </Stack>
                    {visitsDisplay}
                </div>
            )
        case 'person':
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({
                page: page, pageSize: pageSize,
                directorId: item.id, conductorId: item.id,
                composerId: item.id,
                artistId: item.id, attendeeId: item.id
            }))
            compositionsDisplay = createCompositionsDisplay((page, pageSize) => {
                return fetchCompositions({
                    page: page, pageSize: pageSize,
                    directorId: item.id, conductorId: item.id,
                    composerId: item.id,
                    artistId: item.id, attendeeId: item.id
                })
            })
            //console.log(compositionsDisplay)
            return (
                <div>
                    <Stack spacing={2}>
                        <Item>
                            Имя: {item.displayName}
                        </Item>
                        <Item>Кто: {item.type}</Item>
                    </Stack>

                    {compositionsDisplay}
                    {visitsDisplay}

                </div>
            )

        default:
            return (<table>
                    <tbody>
                    <tr>
                        <td>Type:</td>
                        <td>{item['_type']}</td>
                    </tr>
                    <tr>
                        <td>{JSON.stringify(item, null, '\t')}</td>
                    </tr>
                    </tbody>
                </table>
            )
    }

}

export default SelectedItemView