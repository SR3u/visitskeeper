import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Avatar,
    Grid,
    Skeleton,
    Stack,
    TextareaAutosize
} from "@mui/material";
import {avatarUrlFix, Item, itemName} from "./ItemViewUtil";
import React, {useEffect} from "react";

const VisitView = ({item, selectItemC, selectableItem, setHeader}) => {
    useEffect(() => {
        setHeader(item?.date + ' ' + item?.composition?.displayName);
    }, [item, setHeader]);
    let avatarSize = 128;
    let avatarUrl = item?.composition?.avatarUrl;

    function createCompositionsButtons(item) {
        let res = (<div/>)
        console.log(item)
        if(item?.compositions){
            for(const composition in item?.compositions) {
                res += (<Item>{selectableItem(composition?.id, 'composition', composition?.displayName, undefined)}</Item>)
            }
        }
        return res
    }

    return (<div>
        <Grid spacing={2}>
            <Grid container spacing={2}>
                {avatarUrl ? (
                    <Avatar
                        src={avatarUrlFix(avatarUrl)}
                        alt={itemName(item)}
                        variant="square"
                        sx={{
                            width: avatarSize,
                            height: avatarSize,
                            borderRadius: 8,
                            borderColor: '#000000'
                        }}
                    />): (<div/>)}
                <Item>{item?.date}</Item>
                {item?.compositions.map((composition) =>
                    (<Item>{selectableItem(composition?.id, 'composition', composition?.displayName, undefined)}</Item>))
                }
                {createCompositionsButtons(item)}
                <Item>{selectableItem(item?.composition?.typeId, 'composition_type', item?.composition?.type?.displayName, item?.composition?.type?.avatarUrl)}</Item>
                {selectableItem(item?.composition?.composerId)?
                    (<Item>{selectableItem(item?.composition?.composerId, 'person', item?.composition?.composer?.displayName, item?.composition?.composer?.avatarUrl, true)}</Item>):
                    (<div/>)}
                <Item>{selectableItem(item?.venueId, 'venue', item?.venue?.displayName, item?.composition?.type?.avatarUrl)}</Item>
            </Grid>

            {item?.directorId?
                (<Item>Режиссёр: {selectableItem(item?.directorId, 'person', item?.director?.displayName, item?.director?.avatarUrl, true)}</Item>):
                (<div/>)}
            {item?.conductorId?
                (<Item>Дирижёр: {selectableItem(item?.conductorId, 'person', item?.conductor?.displayName, item?.conductor?.avatarUrl, true)}</Item>):
                (<div/>)}
            <Accordion>
                <AccordionSummary
                    //expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1-content"
                    id="panel1-header"
                >Исполнители:</AccordionSummary>
                <AccordionDetails>
                    <Stack spacing={2}>
                        {item?.artists.map((person) => (
                            <Item>
                                {selectableItem(person.id, 'person', person.displayName, person.avatarUrl, true)}
                            </Item>))}
                    </Stack>
                </AccordionDetails>
            </Accordion>
            <Accordion>
                <AccordionSummary
                    //expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1-content"
                    id="panel1-header"
                >Посетители:</AccordionSummary>
                <AccordionDetails>
                    <Stack spacing={2}>
                        {item?.attendees.map((person) => (
                            <Item>
                                {selectableItem(person.id, 'person', person.displayName)}
                            </Item>))}
                    </Stack>
                </AccordionDetails>
            </Accordion>
            <Item>Цена билета: {item?.ticketPrice}</Item>
            <Item>
                <Stack spacing={2}>
                    Примечания:
                    <TextareaAutosize readOnly>{item?.details}</TextareaAutosize>
                </Stack>
            </Item>

        </Grid>
    </div>)
}

export default VisitView