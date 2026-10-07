import {useEffect, useState} from "react";
import {useFavourites} from "../hooks/useFavourites.ts";
import {UseErrorBanner} from "../hooks/useErrorBanner.ts";
import LoadingSpinner from "../components/LoadingSpinner.tsx";
import ErrorBanner from "../components/ErrorBanner.tsx";
import {
    type RefreshResponse,
} from "../types/refreshStation.ts";
import {refreshStations} from "../service/stationService.ts";
import axios from "axios";
import StationCard from "../components/StationCard.tsx";
import {toPrices} from "../types/station.ts";



export default function FavouritesPage() {
    const {favourites, remove} = useFavourites();
    const [prices, setPrices] = useState<Record<string,RefreshResponse>>({});
    const {error, showError, clearError} = UseErrorBanner();

    const [loading, setLoading] = useState<boolean>(false);
    const [refresh, setRefresh] = useState(0)




    useEffect(() => {
        const request : string[] = favourites.map(f => f.id)
        console.log(request)

        if (request === null) {
            showError("No Stations favourited yet")
            return;
        }

        async function fetchPrices () {
            setLoading(true);
            clearError();
            try {
                const response : RefreshResponse[] = await refreshStations(request)

                setPrices(prev => {
                    const next = {...prev};
                    for (const r of response) next[r.stationId] = r;
                    return next;
                })


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

        fetchPrices();
    }, [refresh]);

    return (
        <div>
            <button onClick={() => setRefresh(r => r+1)}>
                Refresh
            </button>
            {loading && (
                <LoadingSpinner colour={"black"} size={"medium"}/>
            )}
            {error && (
                <ErrorBanner text={error}/>
            )}
            {!loading && !error && (
                favourites.map((f) => {
                    const r = prices[f.id];
                    return (
                        <StationCard
                            key = {f.id}
                            station={{
                                name: f.name,
                                address: f.address,
                                prices: toPrices(r?.e5 ?? null, r?.e10?? null, r?.diesel ?? null),
                                status: r?.status,
                                fetchedAt: r?.fetchedAt
                            }}
                            isFavourited={true}
                            onToggleFavourite={() => remove(f.id)}
                        />
                    )
                })
            )}
        </div>
    )
}