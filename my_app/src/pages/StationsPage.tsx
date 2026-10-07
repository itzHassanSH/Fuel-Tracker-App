import {useEffect, useState} from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";

import {getStations} from "../service/stationService.ts";
import {
    type Station,
    type SearchStationRequest,
    type FuelType,
    type Radius,
    type Sort,
    toStation
} from "../types/station.ts";
import LoadingSpinner from "../components/LoadingSpinner.tsx";
import {UseErrorBanner} from "../hooks/useErrorBanner.ts";
import ErrorBanner from "../components/ErrorBanner.tsx";
import StationCard from "../components/StationCard.tsx";

import {RADIUS_OPTIONS, FUEL_TYPE_OPTIONS, SORT_OPTIONS} from "../types/station.ts"

import {useFavourites} from "../hooks/useFavourites.ts";
import {toFavourite} from "../types/favourite.ts";

// with query params, we call getStations here and display results
export default function StationsPage () {

    const [searchParams] = useSearchParams();
    const [stations, setStations] = useState<Station[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const {error, showError, clearError} = UseErrorBanner();

    const {isFavourited, add, remove} = useFavourites();

    useEffect(() => {
        const location = searchParams.get("location")
        const radius = Number(searchParams.get("radius"))
        const fuelType = searchParams.get("fuelType")
        const sort = searchParams.get("sort")

        function isFuelType(value: string | null): value is FuelType {
            return FUEL_TYPE_OPTIONS.includes(value as FuelType)
        }
        function isRadius (value: number): value is Radius {
            return RADIUS_OPTIONS.includes(value as Radius)
        }
        function isSort (value: string | null): value is Sort {
            return SORT_OPTIONS.includes(value as Sort)
        }
        if (Number.isNaN(radius) || !isRadius(radius) || !isFuelType(fuelType) || !isSort(sort) || !location) {
            showError("Invalid search parameters, please try again");
            setStations([])
            return;
        }

        const request: SearchStationRequest = {
            location: location,
            radius: radius,
            fuelType: fuelType,
            sort: sort
        };

        async function fetchResults () {
            setLoading(true);
            clearError(); // clear any earlier error the moment we know this attempt succeeded
            try {
                const response = await getStations(request);
                const stations: Station[] = response.map(toStation)

                setStations(stations);
            } catch (err) {
                const msg = axios.isAxiosError(err)
                    ? err.response?.data?.message ?? err.message
                    : err instanceof Error
                    ? err.message
                    : "Unknown Error"
                showError(msg)
            } finally {
                setLoading(false);
            }
        }

        fetchResults();
    }, [searchParams.toString()]);

    function toggleFavourite(s : Station) {
        if (isFavourited(s.externalId)) remove(s.externalId)
        else if (!add(toFavourite(s))) showError("You can only favourite upto 10 stations");
    }

    return (
        <div>
            {loading && (
                <LoadingSpinner colour={"black"} size={"medium"}/>
            )}
            {error && (
                <ErrorBanner text={error}/>
            )}
            {!loading && !error && (
                stations.map((s) => (
                    <StationCard station={s} isFavourited={isFavourited(s.externalId)}
                                 onToggleFavourite={() => toggleFavourite(s)}
                                 key={s.externalId}/>
                ))
            )}

        </div>
    )
}