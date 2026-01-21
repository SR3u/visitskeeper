//const BASE_URL = 'http://localhost:8080'
//const BASE_URL = 'http://10.0.0.2:16980'
export const BASE_URL = process.env.REACT_APP_BACKEND_BASE_URL
//export const BASE_URL = import.meta.env.REACT_APP_BACKEND_BASE_URL

export const SEARCH_URL = BASE_URL + '/search/json'
export const ITEM_URL = BASE_URL + '/item/'