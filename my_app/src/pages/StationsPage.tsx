import {useEffect, useState} from "react";
import { useSearchParams } from "react-router-dom";

import {getStations} from "../service/stationService.ts";
import type {Station, SearchStationRequest, FuelType, Radius, Sort} from "../types/station.ts";
import LoadingSpinner from "../components/LoadingSpinner.tsx";
import {UseErrorBanner} from "../components/UseErrorBanner.ts";
import ErrorBanner from "../components/ErrorBanner.tsx";
import StationCard from "../components/StationCard.tsx";

// with query params, we call getStations here and display results
export default function StationsPage () {

    const [searchParams] = useSearchParams();
    const [stations, setStations] = useState<Station[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const {error, showError, clearError} = UseErrorBanner();

    useEffect(() => {
        const request: SearchStationRequest = {
            location: searchParams.get("location")?? "",
            radius: Number(searchParams.get("radius")) as Radius,
            fuelType: searchParams.get("fuelType") as FuelType,
            sort: searchParams.get("sort") as Sort
        };

        async function fetchResults () {
            setLoading(true);
            clearError(); // clear any earlier error the moment we know this attempt succeeded
            try {
                const response = await getStations(request);
                setStations(response);
            } catch (err) {
                showError(err instanceof Error? err.message : "Unknown error")
            } finally {
                setLoading(false);
            }
        }

        fetchResults();
    }, [searchParams.toString()]);

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
                    <StationCard station={s}/>
                ))
            )}

        </div>
    )
}