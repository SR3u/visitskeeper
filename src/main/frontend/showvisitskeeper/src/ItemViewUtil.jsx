import {Accordion, AccordionDetails, AccordionSummary, Typography, Paper, Avatar} from "@mui/material";
import GridView from "./GridView";
import React from "react";
import {styled} from '@mui/material/styles';

let subfieldDisplayName = (f) => f?.displayName;

export function createVisitsDisplay(onItemClick, fetchFunc) {
    let header = "Посещения";
    return (
        <Accordion trigger={header}>
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
                        {field: 'date', headerName: 'Дата', width: 100},
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
                            width: 120
                        },
                    ]}
                    fetchItems={fetchFunc}
                    itemsType={'visit'}
                    onItemClick={onItemClick}
                />
            </AccordionDetails>
        </Accordion>
    )
}

export function createCompositionsDisplay(onItemClick, fetchFunc) {
    let header = "Произведения";
    return (
        <Accordion trigger={header}>
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
                        {field: 'type', valueGetter: subfieldDisplayName, headerName: 'Тип', width: 80},
                    ]}
                    fetchItems={fetchFunc}
                    itemsType={'composition'}
                    onItemClick={onItemClick}
                />
            </AccordionDetails>
        </Accordion>

    )
}

export function avatarUrlFix(avatarUrl) {
    if (!avatarUrl) {
        avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyuW3mEEsxF2ck3SFVq5yho3Kva3Yyt-jSCg&s"
    }
    return avatarUrl;
}

export const Item = styled(Paper)(({theme}) => ({
    backgroundColor: '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: (theme.vars ?? theme).palette.text.secondary,
    ...theme.applyStyles('dark', {
        backgroundColor: '#1A2027',
    }),
}));


export function itemName(item) {
    return item?.fullName ? item?.fullName : item?.displayName
}