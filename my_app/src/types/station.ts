export interface SearchStationRequest {
    location: string,
    radius: Radius,
    fuelType: FuelType,
    sort: Sort
}

export interface StationResponse {
    name: string,
    brand: string,
    externalId: string,

    street: string,
    postalCode:string ,
    houseNumber: string,
    place: string,  // E.g. Stuttgart

    distance: number,  // dist of user to this station

    lat: number,
    lng: number,

    diesel: number | null,
    e5: number | null,
    e10: number | null,

    isOpen: boolean
}

export interface Station {
    name: string,
    brand: string,
    externalId: string,

    address: string,  // merged street, postalCode, houseNumber and place
    coords: {lat:number, lng: number},
    distance: number,

    prices: {fuelType: 'DIESEL' | 'E5' | 'E10'; price: number}[],

    isOpen: boolean
}

function formatAddress(street: string, postalCode: string, houseNumber: string, place: string) : string {
    return `${street} ${houseNumber}, ${postalCode} ${place}`;
}

export function toStation(dto: StationResponse) : Station {
    const prices: Station['prices'] = [];
    if (dto.diesel !== null) prices.push({ fuelType: 'DIESEL', price: dto.diesel });
    if (dto.e5 !== null) prices.push({ fuelType: 'E5', price: dto.e5 });
    if (dto.e10 !== null) prices.push({ fuelType: 'E10', price: dto.e10 });

    return {
        name: dto.name,
        brand: dto.brand,
        externalId: dto.externalId,
        address: formatAddress(dto.street, dto.postalCode, dto.houseNumber, dto.place),
        coords: {lat: dto.lat, lng: dto.lng},
        distance: dto.distance,
        prices,
        isOpen: dto.isOpen,
    };
}

const RADIUS_OPTIONS = [1, 5, 10, 15, 20, 25] as const;
export type Radius = typeof RADIUS_OPTIONS[number];  // 1|5|...|25

const FUEL_TYPE_OPTIONS = ["E5", "E10", "DIESEL", "ALL"] as const;
export type FuelType = typeof FUEL_TYPE_OPTIONS[number];

const SORT_OPTIONS = ["PRICE", "DIST"] as const;
export type Sort = typeof SORT_OPTIONS[number];