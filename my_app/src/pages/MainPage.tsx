import type {SearchStationRequest} from "../types/station.ts";
import SearchForm from "../components/SearchForm.tsx"
import {useNavigate} from "react-router-dom";

export default function MainPage() {

    const navigate = useNavigate();

    function handleSearch(request: SearchStationRequest) {
       const params = new URLSearchParams({
           location: request.location,
           radius: String(request.radius),
           fuelType: String(request.fuelType),
           sort: String(request.sort)
       });
       navigate(`/search?${params.toString()}`);
    }

    return (
        <SearchForm onSearch={handleSearch} />
    )
}

