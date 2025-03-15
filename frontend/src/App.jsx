import React, {useEffect, useState} from "react";
import Navbar from "./components/navbar";
import Mainpage from "./components/mainpage";
import {keycloak} from "./HttpClient";

const App = () => {
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        keycloak.init({onLoad: "login-required"}).then((auth) => {
            setAuthenticated(auth);
        }).catch((error) => {
            console.error("Keycloak init error:", error);
        });
    }, []);

    if (!authenticated) {
        return (
            <div>
                <Navbar logout={()=>{}}/>
                <h1 className="centered-label">Loading...</h1>
            </div>
        );
    }


    return (
        <div>
            <Navbar logout={keycloak.logout}/>
            <Mainpage/>
        </div>
    );
};

export default App;