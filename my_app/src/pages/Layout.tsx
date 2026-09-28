import {Link, Outlet} from "react-router-dom";
import logo from "../assets/fuel.png"
import "./Layout.css"

export default function Layout() {
    return (
        <div>
            <header>
                <Link to={"/main"} className={"logo"}>
                    <img src={logo} alt={"Home"} className={"logo-img"}/>
                </Link>
            </header>

            <main>
                <Outlet />
            </main>
        </div>
    )
}