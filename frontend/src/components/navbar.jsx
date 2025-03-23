import React from "react";

const Navbar = ({logout}) => {

    return (
        <div>
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <a className="navbar-brand centered-label" href="/">BalanceBook</a>
                    <div className="collapse navbar-collapse" style={{marginLeft: "90px"}} id="navbarNav">
                        <button className="nav-link active" onClick={logout} style={{marginLeft: 'auto'}}>Abmelden</button>
                    </div>
                </div>
            </nav>
        </div>
    );

}

export default Navbar;