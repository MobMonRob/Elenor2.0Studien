import React, {useState} from "react";
import NewTransactionWindow from "./newTransaction";

const Navbar = ({logout}) => {
    const [isNewTransactionWindowOpen, setIsNewTransactionWindowOpen] = useState(false);

    return (
        <div>
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <a className="navbar-brand" href="/">BalanceBook</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" style={{marginLeft: "90px"}} id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <button
                                    className="nav-link active"
                                    onClick={()=>setIsNewTransactionWindowOpen(true)}
                                >Neue Transaktion</button>
                            </li>
                        </ul>
                        <button className="nav-link active" onClick={logout} style={{marginLeft: 'auto'}}>Abmelden</button>
                    </div>
                </div>
            </nav>
            {isNewTransactionWindowOpen &&
                <NewTransactionWindow
                    closeWindow={() => setIsNewTransactionWindowOpen(false)}
                />}
        </div>
    );

}

export default Navbar;