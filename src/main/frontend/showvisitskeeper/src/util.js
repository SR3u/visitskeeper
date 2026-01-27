import {ITEM_URL, SEARCH_URL} from "./BackendApiConfig";


export function fetchItem(itemId, itemType) {
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

const debounce = (func, delay) => {
    let timeoutId
    return (...args) => {
        clearTimeout(timeoutId)
        timeoutId = setTimeout(() => func(...args), delay)
    }
}

export function fetchSearch(term, page, pageSize) {
    // if (!term || term.trim().length === 0) {
    //     return new Promise(()=> {
    //         return {
    //             pages: {
    //                 pages: 0,
    //                 items: 0,
    //                 page: page
    //             },
    //             content: []
    //         }
    //     })
    // }

    var params = new URLSearchParams({
        's': term,
        'page': page,
        'pageSize': pageSize,
    })
    let fetchUrl = SEARCH_URL + '?' + params
    //console.log(fetchUrl)
    return fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    })
        .then((res) => res.json())

}

export function fetchItemSearch(type, p) {
    var params = new URLSearchParams(
        p
    )
    if (!type) {
        type = 'PERSON'
    }
    type = type.toLowerCase()
    var fetchUrl = `${ITEM_URL + type}/search?${params}`
    console.log(fetchUrl)
    return fetch(fetchUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    }).then(res => res.json())
}

export function fetchVisits(p) {
    return fetchItemSearch('visit', p)
}

export function fetchCompositions(p) {
    return fetchItemSearch('composition', p)
}

export function fetchProductions(p) {
    return fetchItemSearch('production', p)
}

let TYPE_DICT = {
    person: 'человек',
    composition: 'произведение',
    visit: 'визит',
    venue: 'зал',
    production: 'постановка',
}

let PERSON_TYPE_DICT = {
    composer: 'композитор',
    conductor: 'дирижёр',
    director: 'режиссёр',
    family: 'семья',
    other: 'компания',
    actor: 'артист'
}

function translatedType(item, translationDict) {
    if (!item) {
        return undefined
    }
    if (typeof item === 'string') {
        var translatedType = item
        let newTranslatedType = translationDict[translatedType?.toLowerCase()]
        if (newTranslatedType) {
            translatedType = newTranslatedType
        }
        return translatedType
    }
    var translatedType = item?._type
    if (!translatedType) {
        translatedType = item?.type
    }
    if (translatedType) {
        let newTranslatedType = translationDict[translatedType?.toLowerCase()]
        if (newTranslatedType) {
            translatedType = newTranslatedType
        }
    }
    return translatedType;
}

export function translatedPersonType(item) {
    return translatedType(item, PERSON_TYPE_DICT);
}

export function translatedItemType(item) {
    return translatedType(item, TYPE_DICT);
}

export function translateItemType(item) {
    var translatedType = translatedItemType(item);
    if (translatedType) {
        item.translatedType = translatedType
    }
}