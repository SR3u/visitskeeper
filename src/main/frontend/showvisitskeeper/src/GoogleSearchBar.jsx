import React, {useCallback, useEffect, useReducer, useState} from 'react'
import {Search} from 'lucide-react'
import StickyBox from "react-sticky-box";
import {Accordion} from "@mui/material";

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
        const newMovies = initialItem
        console.log(initialItem)
        setItem(newMovies)
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
            <button
                onClick={() => selectItem(id, type)}
            >{text}</button>
        )
    }

    function createVisitsDisplay() {
        var visitsDisplay = (<tr>
            <td>
                <div>Нет</div>
            </td>
        </tr>)
        try {
            if (item.visits) {
                visitsDisplay = (
                    item.visits.map((visit) => (
                        <tr>
                            <td>
                                {selectableItem(visit.id, 'visit', visit.date + ' ' + visit.composition?.displayName)}
                            </td>
                        </tr>))
                )
                visitsDisplay = (
                    <tr>
                        <td><Accordion trigger={"Список"}>
                            <table>
                                <tbody>{visitsDisplay}</tbody>
                            </table>
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
                compDisplay = (
                    item.compositions.map((composition) => (
                        <tr>
                            <td>
                                {selectableItem(composition.id, 'composition', composition.displayName)}
                            </td>
                        </tr>))
                )
                compDisplay = (
                    <tr>
                        <td><Accordion trigger={"Список"}>
                            <table>
                                <tbody>{compDisplay}</tbody>
                            </table>
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
            return (
                <table>
                    <tbody>
                    <tr>
                        <td>{item.displayName}</td>
                    </tr>
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


const GoogleSearchBar = () => {
    var page = 0
    var pageSize = 32767
    var pages = 0
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

    const handleSearch = useCallback(
        debounce((term) => {

            var params = new URLSearchParams({
                's': term,
                'page': page,
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
                .then((p) => pages = p)
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
                                <input
                                    type="text"
                                    value={searchTerm}
                                    onChange={handleInputChange}
                                    className="w-full rounded-full border border-gray-200 bg-white px-5 py-3 pr-20 text-base shadow-md transition-shadow duration-200 hover:shadow-lg focus:border-gray-300 focus:outline-none"
                                    placeholder="Search Google or type a URL"
                                />

                                <button type="submit" className="text-blue-500 hover:text-blue-600">
                                    <Search size={20}/>{' '}
                                </button>
                                {' '}
                            </div>
                            {' '}
                        </form>
                        {' '}
                        {searchResults.length > 0 && (
                            <div className="w-full max-w-2xl rounded-lg bg-white p-4 shadow-md">

                                <h2 className="mb-4 text-xl font-bold"> Search Results: {page}/{pages} </h2>{' '}
                                <ul>
                                    {' '}
                                    {searchResults.map((result) => (
                                        <li key={result.id} className="mb-2">
                                            {/*<a*/}
                                            {/*    // href={result.url}*/}
                                            {/*    className="text-blue-600 hover:underline"*/}
                                            {/*    target="_blank"*/}
                                            {/*    rel="noopener noreferrer"*/}
                                            {/*    onClick={() => selectItem(result)}*/}
                                            {/*>*/}
                                            {/*    {' '}*/}
                                            {/*    {result.fullName + ' ' + result.description}{' '}*/}
                                            {/*</a>{' '}*/}
                                            <button
                                                onClick={() => selectItem(result)}
                                            >
                                                {' '}
                                                {result.fullName + ' ' + result.description}{' '}
                                            </button>
                                            {' '}
                                        </li>
                                    ))}{' '}
                                </ul>
                                {' '}
                            </div>
                        )}{' '}
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