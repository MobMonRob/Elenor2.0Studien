import axios from "axios";
import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "http://localhost:8081/",
    realm: "balancebook-realm",
    clientId: "balancebook",
});

export const httpClient = axios.create({
    baseURL: "http://localhost:8080/api/",
    timeout: 5000,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true
});


httpClient.interceptors.request.use(async (config) => {
    if (!keycloak.authenticated) {
        await keycloak.init({ onLoad: "login-required" });
    }

    await keycloak.updateToken(30)
        .catch(() => {
            console.error("Token konnte nicht aktualisiert werden, Benutzer wird abgemeldet.");
            keycloak.logout();
        });

    if (keycloak.token) {
        config.headers["Authorization"] = `Bearer ${keycloak.token}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

export { keycloak };



