import api from "../apis/axios.ts"
import type {SearchStationRequest, StationResponse} from "../types/station.ts";
import type {RefreshResponse} from "../types/refreshStation.ts";

export const getStations = async(params: SearchStationRequest): Promise<StationResponse[]> => {
    const response = await api.get<StationResponse[]>("/search/stations", {params})
    // mapping logic (from StationResponse to Station) moved into StationsPage
    // Additionally Axios already throws on non-2xx statuses - the throw is caught within StationsPage
    return response.data

    // example query: GET /api/stations/search?lat=53.33&lon=-6.26&radius=10&fuelType=DIESEL
}

// query param name and component name (at backend) must be same! so ids and stationIds produce a mismatch
export const refreshStations = async (ids : string[]): Promise<RefreshResponse[]> => {
    const response = await api.get<RefreshResponse[]>("/favourites/refresh", {
        params: {stationIds: ids.join(",")}
    })
    return response.data;
}