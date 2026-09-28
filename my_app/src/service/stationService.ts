import api from "../apis/axios.ts"
import type {SearchStationRequest, StationResponse} from "../types/station.ts";

export const getStations = async(params: SearchStationRequest): Promise<StationResponse[]> => {
    const response = await api.get<StationResponse[]>("/search/stations", {params})
    // mapping logic (from StationResponse to Station) moved into StationsPage
    // Additionally Axios already throws on non-2xx statuses - the throw is caught within StationsPage
    return response.data

    // example query: GET /api/stations/search?lat=53.33&lon=-6.26&radius=10&fuelType=DIESEL
}