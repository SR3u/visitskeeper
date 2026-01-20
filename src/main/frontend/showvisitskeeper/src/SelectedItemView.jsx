import React, {useCallback, useEffect, useReducer, useState} from "react";
import {
    Avatar,
    Button,
    Skeleton,
    Stack
} from "@mui/material";
import {fetchCompositions, fetchItem, fetchVisits} from "./util";
import {avatarUrlFix, createCompositionsDisplay, createVisitsDisplay, Item, itemName} from "./ItemViewUtil";
import PersonView from "./PersonView";
import VisitView from "./VisitView";
import CompositionView from "./CompositionView";
import VenueView from "./VenueView";
import CompostitionTypeView from "./CompositionTypeView";


const SelectedItemView = ({initialItem, setHeader}) => {
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


    const selectableItem = useCallback((id, type, text, avatarUrl = undefined, forceAvatar = undefined) => {
        var avatar = (<div/>)
        if (item) {
            if (avatarUrl || forceAvatar) {
                avatar = (<Avatar src={avatarUrlFix(avatarUrl)}/>)
            }
        }
        return (
            <Button
                onClick={() => selectItem(id, type)}
            >{avatar}{text}</Button>
        )
    }, [selectItem])

    function onCellClickF(params, event, details) {
        // console.log('params', params)
        // console.log('event',event)
        // console.log('details',details)
        let id = params.row.uuid;
        let type = params.row.itemType
        selectItem(id, type)
    }

    const emptyData = {content: [], pages: {page: 0, items: 0}}

    var visitsDisplay = createVisitsDisplay(selectItemC, () => emptyData)
    var compositionsDisplay = createCompositionsDisplay(selectItemC, () => emptyData)

    if (!item?._type) {
        return (<Item><Skeleton height={368} width={512} /></Item>)
    }

    switch (item?._type) {
        case 'venue':
            return (<VenueView item={item} selectItemC={selectItemC} selectableItem={selectableItem} setHeader={setHeader}/>)
        case 'visit':
            return (<VisitView item={item} selectItemC={selectItemC} selectableItem={selectableItem} setHeader={setHeader}/>)
        case 'composition':
            return (<CompositionView item={item} selectItemC={selectItemC} selectableItem={selectableItem} setHeader={setHeader}/>)
        case 'person':
            return (<PersonView item={item} selectItemC={selectItemC} setHeader={setHeader}/>)
        case 'composition_type':
            return (<CompostitionTypeView item={item} selectItemC={selectItemC} setHeader={setHeader}/>)
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