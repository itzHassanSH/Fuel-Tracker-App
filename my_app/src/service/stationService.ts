import api from "../apis/axios.ts"
import type {SearchStationRequest, StationResponse} from "../types/station.ts";
import type {Station} from "../types/station.ts";
import {toStation} from "../types/station.ts";

export const getStations = async(params: SearchStationRequest): Promise<Station[]> => {
    const response = await api.get<StationResponse[]>("/search/stations", {params})
    return response.data.map(toStation)

    // example query: GET /api/stations/search?lat=53.33&lon=-6.26&radius=10&fuelType=DIESEL
}