import React, {useState} from "react";
import NewEntity from "./newEntity";
import {httpClient} from "../HttpClient";


const Sidebar = ({setTransactionFilter, setTransactionFilterName, users, cashRegisters, externs, setExterns, setCashRegisters}) => {
    const [isNewVirtualCashRegisterWindowOpen, setIsNewVirtualCashRegisterWindowOpen] = useState(false);
    const [isNewExternWindowOpen, setIsNewExternWindowOpen] = useState(false);

    const persistNewVirtualCashRegister = async (newCashRegister) => {
        try {
            const response = await httpClient.post("/virtualcashregisters", newCashRegister);
            setCashRegisters([...cashRegisters, response.data]);
        } catch (error) {
            console.error("Error creating virtual cash register:", error);
        }
    }

    const persistNewExtern = async (newExtern) => {
        try {
            const response = await httpClient.post("/externs", newExtern);
            setExterns([...externs, response.data]);
        } catch (error) {
            console.error("Error creating extern:", error);
        }
    }

    return (
        <div>
            <div className="bg-dark text-white p-3 sidebar">
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
                                    <div className="d-flex" key={user.id}>
                                        <button className="nav-link active" onClick={() => {
                                            setTransactionFilter(user.id);
                                            setTransactionFilterName(user.username);
                                        }}>{user.username}
                                        </button>
                                        <p className={`mt-auto mb-auto ms-auto me-3 ${user.balance < 0 ? "text-danger" : "text-success"}`}>
                                            {user.balance}€
                                        </p>
                                    </div>
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
                                    <div className="d-flex" key={cashRegister.id}>
                                        <button className="nav-link active" onClick={() =>
                                        {
                                            setTransactionFilter(cashRegister.id);
                                            setTransactionFilterName(cashRegister.name);

                                        }}>{cashRegister.name}
                                        </button>
                                        <p className={`mt-auto mb-auto ms-auto me-3 ${cashRegister.balance < 0 ? "text-danger" : "text-success"}`}>
                                            {cashRegister.balance}€
                                        </p>
                                    </div>
                                ))}
                                <button
                                    className="nav-link active mt-3"
                                    onClick={() => setIsNewVirtualCashRegisterWindowOpen(true)}
                                >
                                    + Virtuelles Konto hinzufügen
                                </button>
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
                                <button
                                    className="nav-link active mt-3"
                                    onClick={() => setIsNewExternWindowOpen(true)}
                                >
                                    + Externes Konto hinzufügen
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {isNewVirtualCashRegisterWindowOpen &&
                <NewEntity
                    closeWindow={() => setIsNewVirtualCashRegisterWindowOpen(false)}
                    entities={cashRegisters}
                    persistNewEntity={persistNewVirtualCashRegister}
                    title="Virtuelles Konto hinzufügen"
                />
            }
            {isNewExternWindowOpen &&
                <NewEntity
                    closeWindow={() => setIsNewExternWindowOpen(false)}
                    entities={externs}
                    persistNewEntity={persistNewExtern}
                    title="Externes Konto hinzufügen"
                />
            }
        </div>
    );
}

export default Sidebar;
