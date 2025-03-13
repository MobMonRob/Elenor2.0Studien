import React from "react";

class Navbar extends React.Component {
    state = {}
    render() {
        return (
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
                                <a className="nav-link active" href="/">Neue Transaktion</a>
                            </li>
                        </ul>
                        <button className="nav-link active" onClick={this.props.logout} style={{marginLeft: 'auto'}}>Abmelden</button>
                    </div>
                </div>
            </nav>
        );
    }
}

export default Navbar;