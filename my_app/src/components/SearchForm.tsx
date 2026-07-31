import type {SearchStationRequest} from "../types/station.ts";
import {type SyntheticEvent, useState} from "react";
import type {Radius} from "../types/station.ts"
import type {FuelType} from "../types/station.ts"
import type {Sort} from "../types/station.ts"

interface SearchFormProps {
    onSearch: (request: SearchStationRequest) => void;
    isLoading?:  boolean;
    initialData? : SearchStationRequest
}


export default function SearchForm ({onSearch, isLoading, initialData}: SearchFormProps) {
    const RADIUS_OPTIONS = [1, 5, 10, 15, 20, 25] as const;
    const FUEL_TYPE_OPTIONS = ["E5", "E10", "DIESEL", "ALL"] as const;
    const SORT_OPTIONS = ["PRICE", "DIST"] as const;

    const [formData, setFormData] = useState<SearchStationRequest>( initialData ??
        {
            location: "",
            radius: 1,
            fuelType: "ALL",
            sort: "PRICE"
        }
    );

    const handleSubmit = (e: SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        onSearch(formData)
    }

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={formData.location || ""}
                onChange={(e) =>
                    setFormData({...formData, location: e.target.value})
                }
            />
            <select
                value={formData.radius}
                onChange={ (e) =>
                    setFormData({...formData, radius: Number(e.target.value) as Radius})
            }>
                {RADIUS_OPTIONS.map((r) => (
                    <option key={r} value={r}>
                        {r} km
                    </option>
                ))}
            </select>
            {
                FUEL_TYPE_OPTIONS.map((fuel) => (
                    <label key={fuel}>
                        <input
                            type={"radio"}
                            name={"fuelType"}
                            value={fuel}
                            checked={formData.fuelType === fuel}
                            onChange={(e) =>
                                setFormData({...formData, fuelType:e.target.value as FuelType})}
                        />
                        {fuel}
                    </label>
                ))
            }
            {
                SORT_OPTIONS.map((sort) => (
                    <label key={sort}>
                        <input
                            type={"radio"}
                            name={"sort"}
                            value={sort}
                            checked={formData.sort === sort}
                            onChange={(e) =>
                                setFormData({...formData, sort: e.target.value as Sort})
                        }
                        />
                        {sort}
                    </label>
                ))
            }
            <button type="submit" disabled={isLoading}>
                {isLoading ? "Searching..." : "Search"}
            </button>
        </form>
    );
}