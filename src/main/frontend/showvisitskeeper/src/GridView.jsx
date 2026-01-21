import {DataGrid} from "@mui/x-data-grid";
import React, {useCallback} from "react";
import {itemName} from "./ItemViewUtil";
import {translateItemType} from "./util";


function GridView({columns, fetchItems, fetchItemsState, itemsType, onItemClick, initialPaginationModel, onPaginationModelChange, itemPreProcess}) {

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
    }, [onPaginationModelChange])

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
            // console.log('process.env', process.env);
            // console.log(process.env)
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
                    let item = data[i];
                    item.uuid = item.id
                    item.id = i + paginationModel.page * pageSize
                    item.typeDisplayName = item?.type?.displayName
                    if (!item._type) {
                        item._type = item.type
                    }
                    translateItemType(item)
                    item.searchResultName=itemName(item)
                    if (itemPreProcess) {
                        itemPreProcess(item)
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