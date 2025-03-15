import axios from "axios";
import Keycloak from "keycloak-js";

// Keycloak-Instanz erstellen
const keycloak = new Keycloak({
    url: "http://localhost:8081/",
    realm: "elinor-realm",
    clientId: "elinor",
});

export const httpClient = axios.create({
    baseURL: "http://localhost:8080/api/",
    timeout: 5000,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true
});

// Interceptor, um den Token für jede Anfrage zu setzen
httpClient.interceptors.request.use(async (config) => {
    if (!keycloak.authenticated) {
        await keycloak.init({ onLoad: "login-required" });
    }

    if (keycloak.token) {
        config.headers["Authorization"] = `Bearer ${keycloak.token}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

export { keycloak };