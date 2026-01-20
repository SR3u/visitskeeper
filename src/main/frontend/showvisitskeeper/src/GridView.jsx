import {DataGrid} from "@mui/x-data-grid";
import React, {useCallback} from "react";


function GridView({columns, fetchItems, fetchItemsState, itemsType, onItemClick, initialPaginationModel, onPaginationModelChange}) {

    if (typeof (columns) == 'string') {
        columns = JSON.parse(columns);
    }

    if(!initialPaginationModel) {
        initialPaginationModel = {
            page: 0,
            pageSize: 10,
        };
    }

    const [rows, setRows] = React.useState([]);
    const [totalPages, setTotalPages] = React.useState(0);
    const [totalItems, setTotalItems] = React.useState(0);
    const [paginationModel, setPaginationModel] = React.useState(initialPaginationModel);

    const onPaginationModelChangeC = useCallback((model, details) => {
        setPaginationModel(model);
        if(onPaginationModelChange) {
            onPaginationModelChange(model, details);
        }
    }, [setPaginationModel])

    const [
        filterModel, setFilterModel] = React.useState({items: []});
    const [sortModel, setSortModel] = React.useState([]);

    function onCellClickF(params, event, details) {
        let id = params.row.uuid;
        let rowType = params?.row?._type;
        if (rowType && (typeof rowType != 'string')) {
            rowType = itemsType;
        }
        if ((typeof rowType == 'string') && rowType.length === 0) {
            rowType = itemsType;
        }
        let type = rowType?.toLowerCase() || '';
        //console.log({row: params.row, id: id, type: type});
        onItemClick(id, type)
    }


    React.useEffect(() => {
        const fetcher = async () => {
            var pageSize = paginationModel.pageSize
            if (!pageSize) {
                pageSize = 10;
            }
            // fetch data from server

            fetchItems(paginationModel.page, pageSize)
                .then(res => {
                //console.log(res);
                const data = res.content
                const p = res.pages
                setTotalPages(p.pages)
                setTotalItems(p.items)
                for (let i = 0; i < data.length; i++) {
                    data[i].uuid = data[i].id
                    data[i].id = i + paginationModel.page * pageSize
                    data[i].typeDisplayName = data[i]?.type?.displayName
                    if (!data[i]._type) {
                        data[i]._type = data[i].type
                    }
                }
                //console.log(data)
                setRows(data);
            })
        };
        fetcher();
    }, [fetchItems, paginationModel, sortModel, filterModel, totalItems]);

    return (

        <DataGrid
            pageSizeOptions={[5, 10, 25, 50]}
            initialState={{
                pagination: {
                    paginationModel: {pageSize: 10, page: 0},
                },
            }}
            // pagination
            columns={columns}
            // sortingMode="server"
            // filterMode="server"
            disableColumnSorting
            disableColumnFilter
            disableColumnMenu
            paginationMode="server"
            rowCount={totalItems}
            rows={rows}
            loading={!rows || rows.length === 0}
            paginationModel={paginationModel}
            onPaginationModelChange={onPaginationModelChangeC}
            onSortModelChange={setSortModel}
            onFilterModelChange={setFilterModel}
            onCellClick={onCellClickF}
            disableRowSelectionOnClick
        />

    )
}

export default GridView;