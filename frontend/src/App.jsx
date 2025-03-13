import React from "react";
import Navbar from "./components/navbar";
import Sidebar from "./components/sidebar";
import Mainpage from "./components/mainpage";
import Keycloak from "keycloak-js";
import {httpClient} from "./HttpClient";


const keycloak = new Keycloak({
    url: "http://localhost:8081/",
    realm: "elinor-realm",
    clientId: "elinor"
});

keycloak.init({
    onLoad: "login-required",
    checkLoginIframe: true
}).then((authenticated) => {
    if (authenticated) {
        httpClient.defaults.headers.common["Authorization"] = `Bearer ${keycloak.token}`;
    }
}).catch((error) => {
    console.error("Keycloak init error:", error);
});




class App extends React.Component {

    state = {}

    render() {
        /*
        if (!keycloak.token) {
            return (
                <div>
                    <Navbar/>
                    <h1 className="centered-label">Bitte warten...</h1>
                </div>
            );
        }*/
        return (
            <div>
                <Navbar logout={keycloak.logout} />
                <div className="d-flex">
                    <Sidebar/>
                    <Mainpage/>
                </div>
            </div>
        );
    }
}

export default App;