import React, {useEffect, useState} from "react";
import { httpClient, keycloak } from "../HttpClient";


const Sidebar = ({setTransactionFilter, setTransactionFilterName}) => {
    const [users, setUsers] = useState([]);
    const [cashRegisters, setCashRegisters] = useState([]);
    const [externs, setExterns] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Stelle sicher, dass Keycloak authentifiziert ist
                if (!keycloak.authenticated) {
                    await keycloak.init({ onLoad: "login-required" });
                }

                // Token sicherstellen
                const token = keycloak.token;
                httpClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;
                // API-Anfrage mit gültigem Token
                const response = await httpClient.get("/users");
                setUsers(response.data);
                const response2 = await httpClient.get("/virtualcashregisters");
                setCashRegisters(response2.data);
                const response3 = await httpClient.get("/externs");
                setExterns(response3.data);
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        }
        fetchData();
    }, []);

    return (
        <div className="bg-dark text-white p-3 vh-100" style={{ width: "350px" }}>
            <div className="accordion" id="accordionPanelsStayOpenExample">
                <div className="accordion-item">
                    <h2 className="accordion-header">
                        <button
                            className="accordion-button"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#panelsStayOpen-collapseOne"
                            aria-expanded="false"
                            aria-controls="panelsStayOpen-collapseOne"
                        >
                            Mitglieder
                        </button>
                    </h2>
                    <div id="panelsStayOpen-collapseOne" className="accordion-collapse collapse show">
                        <div className="accordion-body">
                            {users.map((user) => (
                                <button className="nav-link active" onClick={() => {
                                    setTransactionFilter(user.id);
                                    setTransactionFilterName(user.username);
                                }} key={user.id}>{user.username}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
                <div className="accordion-item">
                    <h2 className="accordion-header">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#panelsStayOpen-collapseTwo"
                            aria-expanded="false"
                            aria-controls="panelsStayOpen-collapseTwo"
                        >
                            Virtuelle Konten
                        </button>
                    </h2>
                    <div id="panelsStayOpen-collapseTwo" className="accordion-collapse collapse">
                        <div className="accordion-body">
                            {cashRegisters.map((cashRegister) => (
                                <button className="nav-link active" onClick={() =>
                                {
                                    setTransactionFilter(cashRegister.id);
                                    setTransactionFilterName(cashRegister.name);
                                }} key={cashRegister.id}>{cashRegister.name}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
                <div className="accordion-item">
                    <h2 className="accordion-header">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#panelsStayOpen-collapseThree"
                            aria-expanded="false"
                            aria-controls="panelsStayOpen-collapseThree"
                        >
                            Externe Konten
                        </button>
                    </h2>
                    <div id="panelsStayOpen-collapseThree" className="accordion-collapse collapse">
                        <div className="accordion-body">
                            {externs.map((extern) => (
                                <button className="nav-link active" onClick={() =>
                                {
                                    setTransactionFilter(extern.id);
                                    setTransactionFilterName(extern.name);
                                }} key={extern.id}>{extern.name}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Sidebar;
