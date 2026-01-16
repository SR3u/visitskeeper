import React, {useCallback, useEffect, useState} from 'react'
import {ChevronsLeftIcon, InboxIcon, MailIcon, MenuIcon, SearchIcon} from 'lucide-react'
import {
    AppBar,
    IconButton,
    TextField,
    Box, List,
    Toolbar,
    Typography,
    Button,
    Drawer,
    ListItem,
    ListItemButton, ListItemText, ListItemIcon, Grid
} from "@mui/material";
import GridView from "./GridView";
import SelectedItemView from "./SelectedItemView";
import {GridCloseIcon} from "@mui/x-data-grid";

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

    function doSearch(term, page, pageSize) {
        if (!page) {
            page = 0
        }
        if (!pageSize) {
            pageSize = 25
        }
        if (!term) {
            term = searchTerm
        }
        setSearchTerm(term)

        var params = new URLSearchParams({
            's': term,
            'page': page,
            'pageSize': pageSize,
        })
        let fetchUrl = SEARCH_URL + '?' + params
        return fetch(fetchUrl, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then((res) => res.json())

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
        toggleDrawer(false)
    }

    function selectItem(item) {
        fetchItem(item.id, item.type)
            .then(p => displaySelected(p, p['_type']))
    }


    //const [, forceUpdate] = useReducer(x => x + 1, 0);

    // function updatePage(value) {
    //     setPage(value)
    //     doSearch(null);
    // }

    // const handleChange = (event, value) => {
    //     updatePage(value);
    // };

    const onSelectF = useCallback((id, type) => {
        selectItem({id: id, type: type});
    }, [selectItem]);

    const [open, setOpen] = React.useState(false);

    const toggleDrawer = (newOpen) => () => {
        setOpen(newOpen);
    };

    // Todo for you: Add the below code to the GoogleSearchBar component:
    return (
        //<div className="flex min-h-screen flex-col items-center bg-white p-4">
        <div>
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{mr: 2}}
                            onClick={toggleDrawer(true)}
                        >
                            <SearchIcon/>
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                            Поиск
                        </Typography>
                    </Toolbar>
                </AppBar>
            </Box>
            <Drawer open={open} onClose={toggleDrawer(false)}>
                <Box sx={{width: 480}} role="presentation">
                    <List>
                        <Grid container spacing={2}>
                            <TextField
                                type="text"
                                value={searchTerm}
                                onChange={handleInputChange}
                                className="w-full rounded-full border border-gray-200 bg-white px-5 py-3 pr-20 text-base shadow-md transition-shadow duration-200 hover:shadow-lg focus:border-gray-300 focus:outline-none"
                                placeholder="Поиск"
                            />

                            <IconButton size="large" type="submit" className="text-blue-500 hover:text-blue-600"
                                        onSubmit={handleSearch}>
                                <SearchIcon/>
                            </IconButton>
                            <Box sx={{flexGrow: 1}}/>
                            <IconButton size="large" type="submit" className="text-blue-500 hover:text-blue-600"
                                        onClick={toggleDrawer(false)}>
                                <ChevronsLeftIcon/>
                            </IconButton>
                        </Grid>

                        <GridView
                            columns={[
                                {field: 'fullName', headerName: 'Имя', width: 180},
                                {field: 'description', headerName: 'Описание', width: 180},
                                {field: 'type', headerName: 'Тип', width: 100},
                            ]}
                            fetchItems={(page, pageSize) => doSearch(searchTerm, page, pageSize)}
                            itemsType={'composition'}
                            onItemClick={onSelectF}
                        />
                    </List>
                </Box>
            </Drawer>
            <table className="table table-striped">
                <tbody>
                <tr>
                    <td valign='top' className="text-center">
                        <div>

                            <div><SelectedItemView initialItem={selectedItem}/></div>

                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    )
}

export default GoogleSearchBar