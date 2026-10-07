import {Link, Outlet} from "react-router-dom";
import logo from "../assets/fuel.png"
import "./Layout.css"
import {FolderBookmark} from "lucide-react";

export default function Layout() {
    return (
        <div>
            <header>
                <Link to={"/main"} className={"main-link"}>
                    <img src={logo} alt={"Home"} className={"main-link-img"}/>
                </Link>
                <Link to={"/favourites"} className={"favourites-link"}>
                    <FolderBookmark className={"favourites-link-img"}/>
                </Link>
            </header>

            <main>
                <Outlet />
            </main>
        </div>
    )
}