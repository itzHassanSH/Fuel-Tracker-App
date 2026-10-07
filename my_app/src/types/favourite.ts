import type {Station} from "./station.ts";

export interface FavouriteStation {
    id: string,
    name: string,
    brand: string,
    address: string
}

export function toFavourite(s : Station) : FavouriteStation {
    return {id: s.externalId, address:s.address, brand:s.brand, name:s.name}
}