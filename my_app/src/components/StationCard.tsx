import type {Station} from "../types/station"

type StationCardProps = {
    station: Pick<Station, "name" | "address" | "prices"> & {
        status?: "OPEN" | "CLOSED" | "NO_PRICES" | "UNAVAILABLE",
        fetchedAt?: string
    },
    isFavourited?: boolean,
    onToggleFavourite?: () => void
}

export default function StationCard ({ station, isFavourited=false, onToggleFavourite}: StationCardProps) {
    return (
        <div className={"station-card"}>
            <div className={"station-card__header"}>
                <div>
                    <p className={"station-card__name"}>{station.name}</p>
                    <p className="station-card__address">{station.address}</p>
                </div>
                {onToggleFavourite && (
                    <button
                        onClick={onToggleFavourite}
                        aria-label={isFavourited? "Remove from favourites" : "Add to favourites"}
                    >{isFavourited ? "★" : "☆"}</button>

                )}
            </div>

            {station.status && station.status !== "OPEN" && (
                <p className={"station-card__status"}>
                    {station.status === "CLOSED" ? "Closed" : "No prices available"}
                </p>
            )}

            <div className={"station-card__prices"}>
                {station.prices.map((p) => (
                    <div key={p.fuelType} className={"station-card__price"}>
                        <span>{p.fuelType}</span>
                        <span>{p.price.toFixed(3)} €</span>
                    </div>
                ))}
            </div>

            {station.fetchedAt && (
                <p className={"station-card__updated"}>
                    Updated at {new Date(station.fetchedAt).toLocaleTimeString()}
                </p>
            )}
        </div>
    )
}

