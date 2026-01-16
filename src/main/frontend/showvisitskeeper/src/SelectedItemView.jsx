import React, {useCallback, useEffect, useReducer, useState} from "react";
import {Accordion, AccordionDetails, AccordionSummary, Button, Typography} from "@mui/material";
import GridView from "./GridView";

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
        let header="Посещения";
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
        let header="Произведения";
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
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({page: page, pageSize: pageSize,venueId: item.id}))
            compositionsDisplay = createCompositionsDisplay(() => fetchCompositions({
                venueId: item.id
            }))
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>{item.displayName}</td>
                    </tr>

                    {compositionsDisplay}

                    {visitsDisplay}
                    </tbody>
                </table>
            )
        case 'visit':
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>{item.date}</td>
                        <td>
                            {selectableItem(item.compositionId, 'composition', item.composition?.displayName)}
                        </td>
                        <td>
                            {selectableItem(item.composition?.composerId, 'person', item.composition?.composer?.displayName)}
                        </td>
                        <td>
                            {selectableItem(item.venueId, 'venue', item.venue?.displayName)}
                        </td>
                    </tr>
                    <tr>
                        <td>Режиссёр:</td>
                        <td>
                            {selectableItem(item.directorId, 'person', item.director?.displayName)}
                        </td>
                    </tr>
                    <tr>
                        <td>Дирижёр:</td>
                        <td>
                            {selectableItem(item.conductorId, 'person', item.conductor?.displayName)}
                        </td>
                    </tr>
                    <tr>
                        <td>Исполнители:</td>
                    </tr>
                    {item.artists.map((person) => (
                        <tr>
                            <td>
                                {selectableItem(person.id, 'person', person.displayName)}
                            </td>
                        </tr>))}
                    <tr>
                        <td>Посетители:</td>
                    </tr>
                    {item.attendees.map((person) => (
                        <tr>
                            <td>
                                {selectableItem(person.id, 'person', person.displayName)}
                            </td>
                        </tr>))}
                    </tbody>
                </table>
            )
        case 'composition':
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({page: page, pageSize: pageSize,compositionId: item.id}))
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>{item.type.displayName}</td>
                        <td>{item.displayName}</td>
                    </tr>
                    <tr>
                        <td>Композитор:</td>
                        <td>
                            {selectableItem(item.composerId, 'person', item.composer?.displayName)}
                        </td>
                    </tr>
                    {visitsDisplay}
                    </tbody>
                </table>
            )
        case 'person':
            visitsDisplay = createVisitsDisplay((page, pageSize) => fetchVisits({page: page, pageSize: pageSize,
                directorId: item.id, conductorId: item.id,
                composerId: item.id,
                artistId: item.id, attendeeId: item.id
            }))
            compositionsDisplay = createCompositionsDisplay((page,pageSize) => {
                return fetchCompositions({
                    page:page, pageSize: pageSize,
                    directorId: item.id, conductorId: item.id,
                    composerId: item.id,
                    artistId: item.id, attendeeId: item.id
                })
            })
            //console.log(compositionsDisplay)
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>Имя:</td>
                        <td>{item.displayName}</td>
                    </tr>
                    <tr>
                        <td>Кто:</td>
                        <td>{item.type}</td>
                    </tr>

                    {compositionsDisplay}
                    {visitsDisplay}
                    </tbody>
                </table>
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