import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient, keycloak} from "../HttpClient";
import Select from "react-select";
import NewEntity from "./newEntity";
import {FaRegTrashAlt} from "react-icons/fa";
import DeleteAccount from "./deleteAccount";

const ChangeProfileModal = ({
                          closeWindow,
                          users,
                          setUsers
}) => {
    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [paymentInfoTypes, setPaymentInfoTypes] = useState([]);
    const [userPaymentInfos, setUserPaymentInfos] = useState([]);
    const [updatedUserPaymentInfos, setUpdatedUserPaymentInfos] = useState([]);
    const [newPaymentTypeWindowOpen, setNewPaymentTypeWindowOpen] = useState(false);
    const [isDeleteAccountOpen, setIsDeleteAccountOpen] = useState(false);

    const user = users.find(user => user.id === keycloak.tokenParsed.sub);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response = await httpClient.get("/paymentinfos");
                setPaymentInfoTypes(response.data);
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

    const changePaymentInfoId = (selectedOption, oldId) => {
        setUpdatedUserPaymentInfos(prevInfos =>
            prevInfos.map(info =>
                info.paymentInfo.id === oldId
                    ? { ...info, paymentInfo: { ...info.paymentInfo, id: selectedOption.value, name: selectedOption.label } }
                    : info
            )
        );

        setIsSaveDisabled(false);
    }

    const addPaymentInfoId = (selectedOption) => {
        setUpdatedUserPaymentInfos(prevInfos => [
            ...prevInfos,
            {
                paymentInfo: { id: selectedOption.value, name: selectedOption.label },
                paymentAddress: ""
            }
        ]);
        if(updatedUserPaymentInfos.some(info => info.paymentAddress === "")){
            setIsSaveDisabled(true);
        }
    }

    const changePaymentInfoContent = (e, id) => {
        const newValue = e.target.value;
        setUpdatedUserPaymentInfos(prevInfos =>
            prevInfos.map(info =>
                info.paymentInfo.id === id
                    ? { ...info, paymentAddress: newValue }
                    : info
            )
        );

        if(e.target.value === "" || updatedUserPaymentInfos.some(info => info.paymentAddress === "")){
            setIsSaveDisabled(true);
        }else{
            setIsSaveDisabled(false);
        }
    }

    const mapsAreEqual = (map1, map2) => {
        if (map1.size !== map2.size) return false;
        return [...map1.entries()].every(([key, value]) => map2.get(key) === value);
    };

    const listsAreEqual = (list1, list2) => {
        if (list1.length !== list2.length) return false;
        return list1.every((map, index) => mapsAreEqual(map, list2[index]));
    };

    const savePaymentInfos = () => {
        if(listsAreEqual(userPaymentInfos, updatedUserPaymentInfos)){
            return;
        }
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
    }

    const deleteAccount = async () => {
        setIsDeleteAccountOpen(false)
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
                    {updatedUserPaymentInfos.map((userPaymentInfo, index) => (
                        <div className="row" key={index}>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <Select
                                    options={
                                        paymentInfoTypesOptions.filter(option =>
                                            !updatedUserPaymentInfos.map(info => info.paymentInfo.id).includes(option.value)
                                            || userPaymentInfo.paymentInfo.id === option.value)
                                    }
                                    value={paymentInfoTypesOptions.find(option => option.value === userPaymentInfo.paymentInfo.id) || undefined}
                                    onChange={(e) => changePaymentInfoId(e, userPaymentInfo.paymentInfo.id)}
                                    placeholder="Auswahl Zahlungsinformation"
                                />
                            </div>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <div className="input-group">
                                    <input
                                        type="text"
                                        name="zahlungsinformationInput"
                                        value={userPaymentInfo.paymentAddress}
                                        onChange={(e) => changePaymentInfoContent(e, userPaymentInfo.paymentInfo.id)}
                                        placeholder="Zahlungsadresse"
                                        className="form-control"
                                    />
                                    <button className="btn btn-danger input-group-sm" onClick={() => deletePaymentInfo(userPaymentInfo.paymentInfo.id)}>
                                        <FaRegTrashAlt className="centered-label"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                    {updatedUserPaymentInfos.length < 3 &&
                        <div className="row">
                            <div className="col-md-6 form-group">
                                <label></label>
                                <Select
                                    options={
                                        paymentInfoTypesOptions.filter(option =>
                                            !updatedUserPaymentInfos.map(info => info.paymentInfo.id).includes(option.value))
                                    }
                                    value=""
                                    onChange={(e) => addPaymentInfoId(e)}
                                    placeholder="Zahlungsart"
                                />
                            </div>
                            <div className="col-md-6 form-group">
                                <label></label>
                                <input
                                    type="text"
                                    name="zahlungsinformationInput"
                                    value={undefined}
                                    className="form-control"
                                    placeholder="Zahlungsadresse"
                                    disabled={true}
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
                    <Button className="me-auto" variant="danger" onClick={() => setIsDeleteAccountOpen(true)}>
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
            {isDeleteAccountOpen &&
                <DeleteAccount
                    closeWindow={() => setIsDeleteAccountOpen(false)}
                    deleteAccount={deleteAccount}
                />
            }
        </div>
    );
}

export default ChangeProfileModal;