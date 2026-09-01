import {
    Avatar,
    Box,
    Card,
    CardContent,
    Chip,
    Stack,
    Typography,
    useMediaQuery,
    useTheme
} from "@mui/material";
import {
    avatarUrlFix,
    createVisitsDisplay,
    itemName
} from "./ItemViewUtil";
import React, {useEffect} from "react";
import {fetchVisits} from "./util";


const ProductionView = ({item, selectItemC, selectableItem, setHeader}) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const isTablet = useMediaQuery(theme.breakpoints.between('sm', 'md'));

    useEffect(() => {
        setHeader(item?.composition?.displayName?.toUpperCase() + " (" + item?.directors?.map(d => d.displayName) + ")");
    }, [item, setHeader]);

    let avatarUrl = item?.avatarUrl ? item?.avatarUrl : item?.composition?.avatarUrl;
    let avatarSize = isMobile ? 80 : isTablet ? 100 : 128;

    let visitsDisplay = createVisitsDisplay(selectItemC, (page, pageSize) => fetchVisits({
        page: page,
        pageSize: pageSize,
        productionId: item?.id
    }));

    return (
        <Box sx={{px: {xs: 1, sm: 2, md: 3}, py: {xs: 1, sm: 2}}}>
            {/* Header Card */}
            <Card
                elevation={2}
                sx={{
                    borderRadius: 3,
                    overflow: 'hidden',
                    mb: 3,
                    background: theme.palette.mode === 'dark'
                        ? 'linear-gradient(145deg, #1a2027 0%, #2d3748 100%)'
                        : 'linear-gradient(145deg, #f8f9fa 0%, #e9ecef 100%)',
                }}
            >
                <CardContent sx={{p: {xs: 2, sm: 3}}}>
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: {xs: 'column', sm: 'row'},
                            alignItems: {xs: 'center', sm: 'flex-start'},
                            gap: {xs: 2, sm: 3},
                        }}
                    >
                        {/* Avatar */}
                        {avatarUrl && (
                            <Avatar
                                src={avatarUrlFix(avatarUrl)}
                                alt={itemName(item)}
                                variant="rounded"
                                sx={{
                                    width: avatarSize,
                                    height: avatarSize,
                                    boxShadow: 3,
                                    border: '3px solid',
                                    borderColor: theme.palette.mode === 'dark' ? 'grey.700' : 'common.white',
                                }}
                            />
                        )}

                        {/* Info Section */}
                        <Box sx={{flex: 1, textAlign: {xs: 'center', sm: 'left'}}}>
                            {/* Composition Title */}
                            <Typography
                                variant={isMobile ? 'h6' : 'h5'}
                                component="h2"
                                sx={{
                                    fontWeight: 700,
                                    mb: 0.5,
                                    lineHeight: 1.3,
                                }}
                            >
                                {item?.composition?.displayName}
                            </Typography>

                            {/* Composition Type */}
                            {item?.composition?.type?.displayName && (
                                <Chip
                                    label={item?.composition?.type?.displayName}
                                    size="small"
                                    color="primary"
                                    variant="outlined"
                                    sx={{mb: 1.5, fontWeight: 500}}
                                />
                            )}

                            {/* Directors */}
                            <Box sx={{mt: 1}}>
                                <Typography
                                    variant="caption"
                                    sx={{
                                        textTransform: 'uppercase',
                                        letterSpacing: 1,
                                        color: 'text.secondary',
                                        fontWeight: 600,
                                        display: 'block',
                                        mb: 0.5,
                                    }}
                                >
                                    {item?.directors?.length > 1 ? 'Режиссёры' : 'Режиссёр'}
                                </Typography>
                                <Stack
                                    direction="row"
                                    spacing={0.5}
                                    useFlexGap
                                    flexWrap="wrap"
                                    sx={{justifyContent: {xs: 'center', sm: 'flex-start'}}}
                                >
                                    {item?.directors?.map(director =>
                                        <Box key={director?.id}>
                                            {selectableItem(director?.id, 'person', director?.displayName)}
                                        </Box>
                                    )}
                                </Stack>
                            </Box>

                            {/* Composers */}
                            {item?.composition?.composers?.length > 0 && (
                                <Box sx={{mt: 1.5}}>
                                    <Typography
                                        variant="caption"
                                        sx={{
                                            textTransform: 'uppercase',
                                            letterSpacing: 1,
                                            color: 'text.secondary',
                                            fontWeight: 600,
                                            display: 'block',
                                            mb: 0.5,
                                        }}
                                    >
                                        {item?.composition?.composers?.length > 1 ? 'Композиторы' : 'Композитор'}
                                    </Typography>
                                    <Stack
                                        direction="row"
                                        spacing={0.5}
                                        useFlexGap
                                        flexWrap="wrap"
                                        sx={{justifyContent: {xs: 'center', sm: 'flex-start'}}}
                                    >
                                        {item?.composition?.composers?.map(composer =>
                                            <Box key={composer?.id}>
                                                {selectableItem(composer?.id, 'person', composer?.displayName)}
                                            </Box>
                                        )}
                                    </Stack>
                                </Box>
                            )}
                        </Box>
                    </Box>
                </CardContent>
            </Card>

            {/* Visits Section */}
            <Box sx={{mt: 2}}>
                {visitsDisplay}
            </Box>
        </Box>
    );
}

export default ProductionView
