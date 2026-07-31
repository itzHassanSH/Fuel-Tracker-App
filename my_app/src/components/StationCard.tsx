import type {Station} from "../types/station"

type StationCardProps = {
    station: Station,
    isFavourited?: boolean,
    onToggleFavourite?: (id: string) => void
}

export default function StationCard ({ station}: StationCardProps) {
    return (
        <div className={"station-card"}>
            <div className={"station-card__header"}>
                <div>
                    <p className={"station_card__name"}>{station.name}</p>
                    <p className="station-card__address">{station.address}</p>
                </div>
                < button>{'☆'}</button>
            </div>

            <div className={"station-card__prices"}>
                {station.prices.map((p) => (
                    <div key={p.fuelType} className={"station-card__price"}>
                        <span>{p.fuelType}</span>
                        <span>{p.price.toFixed(3)} €</span>
                    </div>
                ))}
            </div>
        </div>
    )
}

