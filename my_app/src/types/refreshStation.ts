import {toPrices} from "./station.ts";

export interface RefreshResponse {
    status: Status,
    e5: number | null,
    e10: number | null,
    diesel: number | null,
    fetchedAt : string,
    stationId: string
}

export const STATUS_OPTIONS = ["OPEN", "CLOSED", "NO_PRICES", "UNAVAILABLE"] as const;
export type Status = typeof STATUS_OPTIONS[number];

export interface RefreshedPrice {
    status: Status,
    prices: {fuelType: 'DIESEL' | 'E5' | 'E10'; price: number}[],
    fetchedAt: string,
    stationId: string
}

export function toRefreshedPrice(response: RefreshResponse) : RefreshedPrice{
    const prices = toPrices(response.diesel, response.e5, response.e10)

    return {
        status: response.status,
        prices: prices,
        fetchedAt: response.fetchedAt,
        stationId: response.stationId
    }
}

