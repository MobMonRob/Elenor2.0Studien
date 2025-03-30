import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient} from "../HttpClient";

const InfoProfileModal = ({
                                closeWindow,
                                user,
                            }) => {
    const [userPaymentInfos, setUserPaymentInfos] = useState([]);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response2 = await httpClient.get("/users/" + user.id + "/paymentinfos");
                setUserPaymentInfos(response2.data);
            }catch(e){
                console.error("Error during fetching paymentInfos:", e);
            }
        }
        fetchPaymentInfos();
    }, [user.id])

    return (
        <div>
            <Modal show={true} onHide={closeWindow} centered={true} >
                <Modal.Header closeButton>
                    <Modal.Title>Profilübersicht</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Vorname:</label>
                            <input
                                type="text"
                                name="firstName"
                                value={user.firstName}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                        <div className="col-md-6 form-group">
                            <label>Nachname:</label>
                            <input
                                type="text"
                                name="lastName"
                                value={user.lastName}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Benutzername:</label>
                            <input
                                type="text"
                                name="username"
                                value={user.username}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                        <div className="col-md-6 form-group">
                            <label>Saldo:</label>
                            <input
                                type="text"
                                name="balance"
                                value={user.balance}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                    </div>

                    {userPaymentInfos.length > 0 &&
                        <table className="table table-striped mt-3" style={{ border: '2px solid #dee2e6', overflow: 'hidden', tableLayout: 'fixed'}}>
                            <thead>
                            <tr>
                                <th scope="col">Zahlungsart</th>
                                <th scope="col">Zahlungsadresse</th>
                            </tr>
                            </thead>
                            <tbody>
                            {userPaymentInfos.map((userPaymentInfo) => (
                                <tr>
                                    <td>{userPaymentInfo.paymentInfo.name}</td>
                                    <td>{userPaymentInfo.paymentAddress}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    }
                    {userPaymentInfos.length === 0 &&
                        <label>Es sind keine Zahlungsinformationen vorhanden.</label>
                    }
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={closeWindow}>
                        Schließen
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default InfoProfileModal;