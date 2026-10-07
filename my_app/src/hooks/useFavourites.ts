import {useState} from "react";
import type {FavouriteStation} from "../types/favourite.ts";

const KEY = "favourites";
const MAX = 10;
const UUID = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

/**
 * x is FavouriteStation: If this function returns true, TypeScript can treat x as a FavouriteStation.
 */
function isFavouriteStation(x: unknown) : x is FavouriteStation {
    if (typeof x !== "object" || x === null) return false;
    /* "Let me temporarily treat this as an object, so I can inspect its properties"*/
    const o = x as Record<string, unknown>;
    return typeof o.id === "string" && UUID.test(o.id)
        && typeof o.name === "string"
        && typeof o.brand === "string"
        && typeof o.address === "string"
}

function load() : FavouriteStation[] {
    try {
        const raw = localStorage.getItem(KEY)  // string | null
        if (!raw) return []
        const parsed : unknown = JSON.parse(raw);  // may throw on garbage
        if (!Array.isArray(parsed)) return [];

        const seen = new Set<String>();
        return parsed
            .filter(isFavouriteStation)
            // A set object is truthy - Boolean(set) = true. If first statement was false, seen adds the new element and filter keeps said id.
            // However, if first statement was false, then && short-circuits at the first statement and never moves to adding into seen
            .filter(f => !seen.has(f.id) && seen.add(f.id))  // dedup
            .slice(0, MAX);  // enforce cap
    } catch {
        return [];
    }
}

export function useFavourites() {
    const[favourites, setFavourites] = useState<FavouriteStation[]>(load);  // lazy init

    function persist(next : FavouriteStation[]) {
        setFavourites(next);
        try {localStorage.setItem(KEY, JSON.stringify(next))} catch {/* storage full or blocked */}
    }

    function add(f: FavouriteStation) : boolean {
        if (favourites.some(x => x.id === f.id)) return true;
        if (favourites.length >= MAX) return false;  // caller must show "max 10" message
        persist([...favourites, f])
        return true;

    }

    function remove(id: String) {
        persist(favourites.filter(f => f.id !== id))
    }

    return { favourites, add, remove, isFavourited: (id: String) => favourites.some(f => f.id === id) };

}