import React from "react";
import { httpClient, keycloak } from "../HttpClient";


class Sidebar extends React.Component {
    state = {
        users: [],
        cashRegisters: [],
        externs: [],
    };

    async componentDidMount() {
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
            this.setState({ users: response.data });
            const response2 = await httpClient.get("/virtualcashregisters");
            this.setState({ cashRegisters: response2.data });
            const response3 = await httpClient.get("/externs");
            this.setState({ externs: response3.data });
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    }

    render() {
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
                                {this.state.users.map((user) => (
                                    <button className="nav-link active" onClick={() => {
                                        this.props.setTransactionFilter(user.id);
                                        this.props.setTransactionFilterName(user.username);
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
                                {this.state.cashRegisters.map((cashRegister) => (
                                    <button className="nav-link active" onClick={() =>
                                    {
                                        this.props.setTransactionFilter(cashRegister.id);
                                        this.props.setTransactionFilterName(cashRegister.name);
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
                                {this.state.externs.map((extern) => (
                                    <button className="nav-link active" onClick={() =>
                                    {
                                        this.props.setTransactionFilter(extern.id);
                                        this.props.setTransactionFilterName(extern.name);
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
}

export default Sidebar;
