import React, { useEffect, useState } from "react";
import Navbar from "./components/navbar";
import Mainpage from "./components/mainpage";
import { httpClient, keycloak } from "./HttpClient";

const App = () => {
    const [authenticated, setAuthenticated] = useState(false);
    const [users, setUsers] = useState([]);
    const [cashRegisters, setCashRegisters] = useState([]);
    const [externs, setExterns] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const auth = await keycloak.init({ onLoad: "login-required" });
                setAuthenticated(auth);

                if (auth) {
                    const token = keycloak.token;
                    httpClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;
                    const [usersRes, cashRes, externsRes] = await Promise.all([
                        httpClient.get("/users"),
                        httpClient.get("/virtualcashregisters"),
                        httpClient.get("/externs")
                    ]);

                    setUsers(usersRes.data);
                    setCashRegisters(cashRes.data);
                    setExterns(externsRes.data);
                }
            } catch (error) {
                console.error("Error during authentication & fetching data:", error);
            }
        };

        fetchData();
    }, []);

    if (!authenticated) {
        return (
            <div>
                <Navbar logout={() => {}} />
                <h1 className="centered-label">Loading...</h1>
            </div>
        );
    }

    return (
        <div>
            <Navbar
                logout={keycloak.logout}
                users={users}
                setUsers={setUsers}
            />
            <Mainpage
                users={users}
                cashRegisters={cashRegisters}
                externs={externs}
                setUsers={setUsers}
                setCashRegisters={setCashRegisters}
                setExterns={setExterns}
            />
        </div>
    );
};

export default App;
