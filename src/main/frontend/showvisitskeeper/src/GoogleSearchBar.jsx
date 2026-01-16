import React, {useCallback, useEffect, useReducer, useState} from 'react'
import {Search} from 'lucide-react'
import StickyBox from "react-sticky-box";
import {Accordion, AccordionDetails, AccordionSummary, Button, Pagination, TextField, Typography} from "@mui/material";
import {DataGrid} from '@mui/x-data-grid';
import {type} from "@testing-library/user-event/dist/type";

const BASE_URL = 'http://localhost:8080'

const SEARCH_URL = BASE_URL + '/search/json'
const PAGES_URL = BASE_URL + '/search/pages'
const ITEM_URL = BASE_URL + '/item/'

const sampleData = [
    {
        "fullName": "ГРЕБЕНЩИКОВ",
        "description": "ACTOR",
        "type": "PERSON",
        "url": "/html/person?id=502b0191-24b5-4b4d-be42-4d983e6c5d4b",
        "id": "502b0191-24b5-4b4d-be42-4d983e6c5d4b"
    }, {
        "fullName": "ИРА",
        "description": "FAMILY",
        "type": "PERSON",
        "url": "/html/person?id=ea05767e-784f-41b2-88a9-31c24da67268",
        "id": "ea05767e-784f-41b2-88a9-31c24da67268"
    }, {
        "fullName": "ШАТРОВ",
        "description": "COMPOSER",
        "type": "PERSON",
        "url": "/html/person?id=2e1d116e-2e3b-4a45-b4a4-4eaa9e960359",
        "id": "2e1d116e-2e3b-4a45-b4a4-4eaa9e960359"
    },
]

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

function TextUpdater() {
    // Declare a state variable 'text' and a function 'setText' to update it.
    const [text, setText] = useState('Search Results: {page}/{pages}');

    const setPagePages = (page, pages) => {
        setText('Search Results: ' + page + '/' + pages); // Update the state
    };

    return (
        <h2>{text}</h2>
    );
}

function fetchSearch(type, p) {
    var params = new URLSearchParams(
        p
    )
    if (!type) {
        type = 'PERSON'
    }
    type = type.toLowerCase()
    var fetchUrl = ITEM_URL + type + '/search' + '?' + params
    console.log(fetchUrl)
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


const SelectedItemView = ({initialItem}) => {
    const [item, setItem] = useState({});

    useEffect(() => {
        const newItem = initialItem
        console.log(initialItem)
        setItem(newItem)
    }, [initialItem])

    const [, forceUpdate] = useReducer(x => x + 1, 0);

    // console.log('item', item);
    // console.log('initialItem', initialItem);

    if (!item || !item['_type']) {
        return (<label>Nothing Selected</label>)
    }

    function displaySelected(item) {
        return setItem(item);
    }

    function selectItem(id, type) {
        fetchItem(id, type)
            .then(p => displaySelected(p, p['_type']))
    }

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

    function createVisitsDisplay() {
        var visitsDisplay = (<tr>
            <td>
                <div>Нет</div>
            </td>
        </tr>)
        try {
            if (item.visits) {
                // visitsDisplay = (
                //     item.visits.map((visit) => (
                //         <tr>
                //             <td>
                //                 {selectableItem(visit.id, 'visit', visit.date + ' ' + visit.composition?.displayName+' '+visit?.venue.displayName)}
                //             </td>
                //         </tr>))
                // )
                // visitsDisplay = (
                //     <tr>
                //         <td><Accordion trigger={"Список"}>
                //             <AccordionSummary
                //                 //expandIcon={<ExpandMoreIcon/>}
                //                 aria-controls="panel1-content"
                //                 id="panel1-header"
                //             >
                //                 <Typography component="span">Список</Typography>
                //             </AccordionSummary>
                //             <AccordionDetails>
                //             <table>
                //                 <tbody>{visitsDisplay}</tbody>
                //             </table>
                //             </AccordionDetails>
                //         </Accordion>
                //         </td>
                //     </tr>
                // )
                var i=0
                const rows =
                    item.visits.map((visit) => {
                        var row = {}
                        row.id = i
                        row.date = visit.date;
                        row.composition = visit.composition?.displayName
                        row.venue = visit?.venue?.displayName
                        row.uuid=visit.id
                        row.itemType='visit'
                        i+=1
                        return row
                    } )

                var colsList = [
                    { field: 'date', headerName: 'Дата', width: 200 },
                    { field: 'composition', headerName: 'Произведение', width: 200 },
                    { field: 'venue', headerName: 'Площадка', width: 200 },
                ]
                visitsDisplay = (
                    <tr>
                        <td><Accordion trigger={"Список"}>
                            <AccordionSummary
                                //expandIcon={<ExpandMoreIcon/>}
                                aria-controls="panel1-content"
                                id="panel1-header"
                            >
                                <Typography component="span">Список</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <DataGrid rows={rows} columns={colsList} pageSizeOptions={[5, 10, 25, 50]}
                                          onCellClick={onCellClickF}
                                          initialState={{
                                              pagination: {
                                                  paginationModel: { pageSize: 10, page: 0 },
                                              },
                                          }}/>
                            </AccordionDetails>
                        </Accordion>
                        </td>
                    </tr>
                )
            }
        } catch (e) {
            console.log(e)
        }
        return visitsDisplay
    }

    function createCompositionsDisplay() {
        var compDisplay = (<tr>
            <td>
                <div>Нет</div>
            </td>
        </tr>)

        try {
            if (item.compositions) {
                // compDisplay = (
                //     item.compositions.map((composition) => (
                //         <tr>
                //             <td>
                //                 {selectableItem(composition.id, 'composition', composition.displayName)}
                //             </td>
                //         </tr>))
                // )
                // compDisplay = (
                //     <tr>
                //         <td><Accordion trigger={"Список"}>
                //             <AccordionSummary
                //                 //expandIcon={<ExpandMoreIcon/>}
                //                 aria-controls="panel1-content"
                //                 id="panel1-header"
                //             >
                //                 <Typography component="span">Список</Typography>
                //             </AccordionSummary>
                //             <AccordionDetails>
                //             <table>
                //                 <tbody>{compDisplay}</tbody>
                //             </table>
                //             </AccordionDetails>
                //         </Accordion>
                //         </td>
                //     </tr>
                // )
                var i=0
                const rows =
                    item.compositions.map((composition) => {
                        var row = {}
                        row.id = i
                        row.displayName = composition.displayName
                        row.type = composition.type?.displayName
                        row.uuid=composition.id
                        row.itemType='composition'
                        i+=1
                        return row
                    } )

                var colsList = [
                    { field: 'displayName', headerName: 'Имя', width: 200 },
                    { field: 'type', headerName: 'Тип', width: 200 },
                ]
                compDisplay = (
                    <tr>
                        <td><Accordion trigger={"Список"}>
                            <AccordionSummary
                                //expandIcon={<ExpandMoreIcon/>}
                                aria-controls="panel1-content"
                                id="panel1-header"
                            >
                                <Typography component="span">Список</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <DataGrid rows={rows} columns={colsList} pageSizeOptions={[5, 10, 25, 50]}
                                          onCellClick={onCellClickF}
                                          initialState={{
                                              pagination: {
                                                  paginationModel: { pageSize: 10, page: 0 },
                                              },
                                          }}/>
                            </AccordionDetails>
                        </Accordion>
                        </td>
                    </tr>
                )
            }
        } catch (e) {
            console.log(e)
        }
        return compDisplay
    }

    var visitsDisplay = createVisitsDisplay()
    var compositionsDisplay = createCompositionsDisplay()
    switch (item['_type']) {
        case 'venue':
            if (!item.visits) {
                fetchVisits({venueId: item.id})
                    .then((visits) => {
                        item.visits = visits
                        console.log(item.visits)
                        setItem(item)
                        forceUpdate()
                    })
            }
            if (!item.compositions) {
                fetchCompositions({
                    venueId: item.id
                })
                    .then((compositions) => {
                        item.compositions = compositions
                        //console.log(item.visits)
                        setItem(compositions)
                        forceUpdate()
                    })
            }
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>{item.displayName}</td>
                    </tr>
                    <tr>
                        <td>Произведения:</td>
                    </tr>
                    {compositionsDisplay}
                    <tr>
                        <td>Посещения</td>
                    </tr>
                    <tr>
                        <td>{
                            visitsDisplay
                        }</td>
                    </tr>
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
            if (!item.visits) {
                fetchVisits({
                    compositionId: item.id,
                })
                    .then((visits) => {
                        item.visits = visits
                        //console.log(item.visits)
                        setItem(item)
                        forceUpdate()
                    })
            }
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
                    <tr>
                        <td>Посещения:</td>
                    </tr>
                    {visitsDisplay}
                    </tbody>
                </table>
            )
        case 'person':
            if (!item.visits) {
                fetchVisits({
                    directorId: item.id, conductorId: item.id,
                    composerId: item.id,
                    artistId: item.id, attendeeId: item.id
                })
                    .then((visits) => {
                        item.visits = visits
                        //console.log(item.visits)
                        setItem(item)
                        forceUpdate()
                    })
            }
            if (!item.compositions) {
                fetchCompositions({
                    directorId: item.id, conductorId: item.id,
                    composerId: item.id,
                    artistId: item.id, attendeeId: item.id
                })
                    .then((compositions) => {
                        item.compositions = compositions
                        //console.log(item.visits)
                        setItem(compositions)
                        forceUpdate()
                    })
            }
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
                    <tr>
                        <td>Произведения:</td>
                    </tr>
                    {compositionsDisplay}
                    <tr>
                        <td>Посещения:</td>
                    </tr>
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

function ServerPaginationFilterSortGrid({searchText, onSelectF}) {

    const [rows, setRows] = React.useState([]);
    const [searchTerm, setSearchTerm] = React.useState(searchText);
    const [totalPages, setTotalPages] = React.useState(0);
    const [totalItems, setTotalItems] = React.useState(0);
    const [paginationModel, setPaginationModel] = React.useState({
        page: 0,
        pageSize: 10,
    });
    const [
        filterModel, setFilterModel] = React.useState({ items: [] });
    const [sortModel, setSortModel] = React.useState([]);
    const columns = [
        { field: 'fullName', headerName: 'Имя', width: 200 },
        { field: 'description', headerName: 'Описание', width: 200 },
    ]

    function onCellClickFunc(params, event, details) {
        let id = params.row.uuid;
        let type = params.row.type?.toLowerCase() || '';
        onSelectF(id, type)
    }

    useEffect(() => {
        const newSearchText = searchText
        console.log(searchText)
        setSearchTerm(newSearchText)
    }, [searchText])


    React.useEffect(() => {
        const fetcher = async () => {
            var pageSize = paginationModel.pageSize
            if (!pageSize) {
                pageSize = 10;
            }
            // fetch data from server
            var term=searchTerm
            if(!term) {
                term = searchText
            }
            if(typeof term !== 'string') {
                term = term.searchText
            }
            console.log(term)
            if(!term) {
                term = ''
            }
            let paramsDict = {
                's': term,
                'page': paginationModel.page,
                'pageSize': pageSize,
            };
            var params = new URLSearchParams(paramsDict)
            var fetchUrl = PAGES_URL + '?' + params
            var p = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
            })
                .then((res) => res.json())
            setTotalPages(p.pages)
            setTotalItems(p.items)
            console.log(fetchUrl)

            fetchUrl = SEARCH_URL + '?' + params

            const data = await  fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
            }).then((res) => res.json());
            for (let i = 0; i < data.length; i++) {
                data[i].uuid=data[i].id
                data[i].id=i + paginationModel.page*pageSize
            }
            console.log(data)
            setRows(data);
        };
        fetcher();
    }, [searchText, paginationModel, sortModel, filterModel, totalItems, searchTerm]);

    return (<div><DataGrid
        pageSizeOptions={[5, 10, 25, 50]}
        initialState={{
            pagination: {
                paginationModel: { pageSize: 10, page: 0 },
            },
        }}
        // pagination
        columns={columns}
        sortingMode="server"
        filterMode="server"
        paginationMode="server"
        rowCount={totalItems}
        rows={rows}
        onPaginationModelChange={setPaginationModel}
        onSortModelChange={setSortModel}
        onFilterModelChange={setFilterModel}
        onCellClick={onCellClickFunc}
    /></div>);
}

const GoogleSearchBar = () => {
    var pageSize = 20

    const [pages, setPages] = useState(0)
    const [page, setPage] = useState(1)

    const [searchTerm, setSearchTerm] = useState('')
    const [searchResults, setSearchResults] = useState([])
    const [selectedItem, setSelectedItem] = useState(null);

    const debounce = (func, delay) => {
        let timeoutId
        return (...args) => {
            clearTimeout(timeoutId)
            timeoutId = setTimeout(() => func(...args), delay)
        }
    }

    function doSearch(term) {

        if(!term) {
            term = searchTerm
        }
        var params = new URLSearchParams({
            's': term,
            'page': page - 1,
            'pageSize': pageSize,
        })
        var fetchUrl = PAGES_URL + '?' + params
        fetch(fetchUrl, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then((res) => res.json())
            .then((p) => setPages(p.pages))
        fetchUrl = SEARCH_URL + '?' + params
        fetch(fetchUrl, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then((res) => res.json())
            .then((results) => setSearchResults(results))
    }

    const handleSearch = useCallback(
        debounce((term) => {

            doSearch(term);
//         const results = sampleData.filter((item) =>
//           item.fullName.toLowerCase().includes(term.toLowerCase()),
//         )
//         setSearchResults(results)

        }, 300),
        [],
    )

    useEffect(() => {
        handleSearch(searchTerm)
    }, [searchTerm, handleSearch])

    const handleInputChange = (e) => {
        setSearchTerm(e.target.value)
    }

    function displaySelected(item, type) {
        setSelectedItem(item)
    }

    function selectItem(item) {
        fetchItem(item.id, item.type)
            .then(p => displaySelected(p, p['_type']))
    }


    const [, forceUpdate] = useReducer(x => x + 1, 0);

    function updatePage(value) {
        setPage(value)
        doSearch(null);
    }

    const handleChange = (event, value) => {
        updatePage(value);
    };

    const onSelectF = useCallback((id, type)  =>  {
        selectItem({id:id, type:type});
    }, [selectItem]);

    // Todo for you: Add the below code to the GoogleSearchBar component:
    return (
        //<div className="flex min-h-screen flex-col items-center bg-white p-4">
        <table className="table table-striped">
            <tbody>
            <tr>
                <td width='368' valign='top' className="text-center">
                    <div>
                        <form
                            onSubmit={(e) => e.preventDefault()}
                            className="mb-8 w-full max-w-2xl"
                        >
                            <div className="relative">
                                <TextField
                                    type="text"
                                    value={searchTerm}
                                    onChange={handleInputChange}
                                    className="w-full rounded-full border border-gray-200 bg-white px-5 py-3 pr-20 text-base shadow-md transition-shadow duration-200 hover:shadow-lg focus:border-gray-300 focus:outline-none"
                                    placeholder="Search Google or type a URL"
                                />

                                <Button type="submit" className="text-blue-500 hover:text-blue-600">
                                    <Search size={45}/>{' '}
                                </Button>
                                {' '}
                            </div>
                            {' '}
                        </form>
                        {' '}

                            <ServerPaginationFilterSortGrid searchText={searchTerm} onSelectF={onSelectF}/>)

                    </div>

                </td>
                <td valign='top' className="text-center">
                    <div>
                        <StickyBox offsetTop={20} offsetBottom={20}>
                            <div><SelectedItemView initialItem={selectedItem}/></div>
                        </StickyBox>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    )
}

export default GoogleSearchBar