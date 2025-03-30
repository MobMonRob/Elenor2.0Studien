import React, {useState} from "react";
import ChangeProfileModal from "./changeProfileModal";

const Navbar = ({
                    logout,
                    users,
                    setUsers
}) => {
    const [isProfileSettingOpen, setIsProfileSettingOpen] = useState(false);
    return (
        <div>
            <nav className="navbar navbar-expand-lg bg-body-tertiary" style={{position: 'fixed', top: '0', width: '100%', zIndex: '100', height: '55px'}}>
                <div className="container-fluid">
                    <a className="navbar-brand centered-label" href="/">BalanceBook</a>
                    <div className="collapse navbar-collapse  d-flex justify-content-end" id="navbarNav">
                        <button className="nav-link active me-5" onClick={() => setIsProfileSettingOpen(true)}>Profil</button>
                        <button className="nav-link active" onClick={logout}>Abmelden</button>
                    </div>
                </div>
            </nav>
            {isProfileSettingOpen &&
                <ChangeProfileModal
                    closeWindow={() => setIsProfileSettingOpen(false)}
                    users={users}
                    setUsers={setUsers}
                />
            }
        </div>
    );
}

export default Navbar;