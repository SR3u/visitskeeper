import React, {useCallback, useEffect, useState} from 'react'
import {ChevronsLeftIcon, SearchIcon} from 'lucide-react'
import {
    AppBar,
    IconButton,
    TextField,
    Box, List,
    Toolbar,
    Typography,
    Drawer,
    Grid, debounce
} from "@mui/material";
import GridView from "./GridView";
import SelectedItemView from "./SelectedItemView";
import {fetchItem, fetchSearch} from "./util";


const GoogleSearchBar = () => {
    var pageSize = 20

    const [pages, setPages] = useState(0)
    const [page, setPage] = useState(1)

    const [searchTerm, setSearchTerm] = useState('')
    const [searchTermFinal, setSearchTermFinal] = useState('')

    const [selectedItem, setSelectedItem] = useState(null);

    const [searchResultsPaginationModel, setSearchResultsPaginationModel] = useState({
        page: 0,
        pageSize: 10,
    });
    const onSearchResultsPaginationModelChange = useCallback((model, details) => {
        setSearchResultsPaginationModel(model)
    }, [setSearchResultsPaginationModel])

    const [header, setHeader] = useState("Поиск");




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
        //setSearchTerm(term)

        return fetchSearch(term, page, pageSize)
    }


    const handleSearch = useCallback(
        debounce((term) => {
            setSearchTermFinal(term)
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

    const selectItem = useCallback((item) => {
        fetchItem(item.id, item.type)
            .then(p => displaySelected(p, p['_type']))
    }, [fetchItem])


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
                            {header}
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
                            initialPaginationModel={searchResultsPaginationModel}
                            onPaginationModelChange={onSearchResultsPaginationModelChange}
                            fetchItemsState={searchTerm}
                        />
                    </List>
                </Box>
            </Drawer>
            <table className="table table-striped">
                <tbody>
                <tr>
                    <td valign='top' className="text-center">
                        <div>

                            <div><SelectedItemView initialItem={selectedItem} setHeader={setHeader}/></div>

                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    )
}

export default GoogleSearchBar