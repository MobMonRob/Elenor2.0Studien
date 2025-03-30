import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient, keycloak} from "../HttpClient";
import Select from "react-select";
import NewEntity from "./newEntity";

const ChangeProfileModal = ({
                          closeWindow,
                          users,
                          setUsers
}) => {
    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [paymentInfoTypes, setPaymentInfoTypes] = useState([]);
    const [userPaymentInfos, setUserPaymentInfos] = useState([]);
    const [newPaymentTypeWindowOpen, setNewPaymentTypeWindowOpen] = useState(false);

    const user = users.find(user => user.id === keycloak.tokenParsed.sub);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response = await httpClient.get("/paymentinfos");
                setPaymentInfoTypes(response.data);
                const response2 = await httpClient.get("/users/" + keycloak.tokenParsed.sub + "/paymentinfos");
                setUserPaymentInfos(response2.data);
            }catch(e){
                console.error("Error during fetching paymentInfos:", e);
            }
        }
        fetchPaymentInfos();
    }, [])

    const paymentInfoTypesOptions = paymentInfoTypes.map(paymentInfoType => ({
        value: paymentInfoType.id,
        label: paymentInfoType.name
    }));

    const persistNewPaymentType = async (newPaymentType) => {
        try {
            const response = await httpClient.post("/paymentinfos", newPaymentType);
            setPaymentInfoTypes([...paymentInfoTypes, response.data]);
        } catch (error) {
            console.error("Error creating payment type:", error);
        }
    }

    const changePaymentInfoId = (e, id) => {
        setIsSaveDisabled(true);
    }

    const changePaymentInfoContent = (e, id) => {
        setIsSaveDisabled(true);

    }

    const savePaymentInfos = () => {

    }

    const deleteUser = () => {

    }

    const handleSave = () => {
        savePaymentInfos();
        closeWindow();
    }

    return (
        <div>
            <Modal show={true} onHide={closeWindow} centered={true} >
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
                                value={user.balance}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                    </div>

                    <label>Zahlungsinformationen:</label>
                    {userPaymentInfos.map((userPaymentInfo, index) => (
                        <div className="row" key={index}>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <Select
                                    options={
                                        paymentInfoTypesOptions.filter(option =>
                                            !userPaymentInfos.map(info => info.paymentInfo.id).includes(option.value)
                                            || userPaymentInfo.paymentInfo.id === option.value)
                                    }
                                    value={paymentInfoTypesOptions.find(option => option.value === userPaymentInfo.paymentInfo.id) || undefined}
                                    onChange={(e) => changePaymentInfoId(e, userPaymentInfo.paymentInfo.id)}
                                    placeholder="Auswahl Zahlungsinformation"
                                />
                            </div>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <input
                                    type="text"
                                    name="zahlungsinformationInput"
                                    value={userPaymentInfo.paymentAddress}
                                    onChange={(e) => changePaymentInfoContent(e, userPaymentInfo.paymentInfo.id)}
                                    className="form-control"
                                />
                            </div>
                        </div>
                    ))}
                    {userPaymentInfos.length < 3 &&
                        <div className="row">
                            <div className="col-md-6 form-group">
                                <label></label>
                                <Select
                                    options={
                                        paymentInfoTypesOptions.filter(option =>
                                            !userPaymentInfos.map(info => info.paymentInfo.id).includes(option.value))
                                    }
                                    value={undefined}
                                    onChange={(e) => {}}
                                    placeholder="Zahlungsart"
                                />
                            </div>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <input
                                    type="text"
                                    name="zahlungsinformationInput"
                                    value={user.phoneNumber}
                                    className="form-control"
                                    placeholder="Zahlungsadresse"
                                />
                            </div>
                        </div>
                    }
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label></label>
                            <button
                                className="form-control bg-primary-subtle"
                                onClick={() => setNewPaymentTypeWindowOpen(true)}
                            >
                                Zahlungsart hinzufügen
                            </button>
                        </div>
                    </div>
                    <label className="text-center text-danger">Die hier gemachten Angaben sind für die anderen Nutzer einsehbar!</label>
                </Modal.Body>

                <Modal.Footer>
                    <Button className="me-auto" variant="danger" onClick={deleteUser}>
                        Account löschen
                    </Button>
                    <Button variant="secondary" onClick={closeWindow}>
                        Abbrechen
                    </Button>
                    <Button variant="primary" onClick={handleSave} disabled={isSaveDisabled}>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
            {newPaymentTypeWindowOpen &&
                <NewEntity
                    closeWindow={() => setNewPaymentTypeWindowOpen(false)}
                    entities={paymentInfoTypes}
                    persistNewEntity={persistNewPaymentType}
                    title="Zahlungsart hinzufügen"
                />
            }
        </div>
    );
}

export default ChangeProfileModal;