import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient, keycloak} from "../HttpClient";
import EditEntityPaymentInfo from "./editEntityPaymentInfo";

const ChangeProfileModal = ({
                          closeWindow,
                          users

}) => {
    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [userPaymentInfos, setUserPaymentInfos] = useState([]);
    const [updatedUserPaymentInfos, setUpdatedUserPaymentInfos] = useState([]);

    const user = users.find(user => user.id === keycloak.tokenParsed.sub);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response2 = await httpClient.get("/users/" + keycloak.tokenParsed.sub + "/paymentinfos");

                const paymentInfos = response2.data.map(({ paymentInfo, paymentAddress }) => ({
                    paymentInfo,
                    paymentAddress
                }));
                setUserPaymentInfos(paymentInfos);
                setUpdatedUserPaymentInfos(paymentInfos);
            }catch(e){
                console.error("Error during fetching paymentInfos:", e);
            }
        }
        fetchPaymentInfos();
    }, [])

    const savePaymentInfos = () => {
        updatedUserPaymentInfos.forEach(
            async (updatedUserPaymentInfo) => {
                if(userPaymentInfos.find(info => info.paymentInfo.id === updatedUserPaymentInfo.paymentInfo.id
                    && info.paymentAddress !== updatedUserPaymentInfo.paymentAddress))
                {
                    await httpClient.put("/users/" + keycloak.tokenParsed.sub + "/paymentinfos/" + updatedUserPaymentInfo.paymentInfo.id,
                        updatedUserPaymentInfo.paymentAddress,
                        { headers: { "Content-Type": "text/plain" } });
                }else if (!userPaymentInfos.find(info => info.paymentInfo.id === updatedUserPaymentInfo.paymentInfo.id))
                {
                    await httpClient.post("/users/" + keycloak.tokenParsed.sub + "/paymentinfos/" + updatedUserPaymentInfo.paymentInfo.id,
                        updatedUserPaymentInfo.paymentAddress,
                        { headers: { "Content-Type": "text/plain" } });
                }
            }
        )
        userPaymentInfos.filter(
            async(userPaymentInfo) => {
            if(!updatedUserPaymentInfos.find(updatedUserPaymentInfo => updatedUserPaymentInfo.paymentInfo.id === userPaymentInfo.paymentInfo.id)){
                await deletePaymentInfo(userPaymentInfo.paymentInfo.id);
            }
        })
    }

    const deletePaymentInfo = async (id) => {
        if(userPaymentInfos.find(info => info.paymentInfo.id === id)){
            await httpClient.delete("/users/" + keycloak.tokenParsed.sub + "/paymentinfos/" + id);
            setUserPaymentInfos(userPaymentInfos.filter(info => info.paymentInfo.id !== id));
        }
        setUpdatedUserPaymentInfos(updatedUserPaymentInfos.filter(info => info.paymentInfo.id !== id));
    }

    const handleSave = () => {
        savePaymentInfos();
        closeWindow();
    }

    return (
        <div>
            <Modal show={true} onHide={closeWindow} centered={true}>
                <Modal.Header closeButton>
                    <Modal.Title>Profil bearbeiten</Modal.Title>
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
                                value={user.balance + " €"}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                    </div>

                    <EditEntityPaymentInfo
                        updatedEntityPaymentInfos={updatedUserPaymentInfos}
                        setUpdatedEntityPaymentInfos={setUpdatedUserPaymentInfos}
                        setIsSaveDisabled={setIsSaveDisabled}
                        deleteEntityPaymentInfo={deletePaymentInfo}
                    />

                    <label className="text-center text-danger">Die hier gemachten Angaben sind für die anderen Nutzer einsehbar!</label>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={closeWindow}>
                        Abbrechen
                    </Button>
                    <Button variant="primary" onClick={handleSave} disabled={isSaveDisabled}>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default ChangeProfileModal;