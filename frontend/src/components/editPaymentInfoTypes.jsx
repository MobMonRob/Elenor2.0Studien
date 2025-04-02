import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient} from "../HttpClient";
import {FaRegTrashAlt} from "react-icons/fa";

const EditPaymentInfoTypes = ({closeWindow, paymentInfoTypes, setPaymentInfoTypes}) => {
    const [updatedPaymentInfoTypes, setUpdatedPaymentInfoTypes] = useState(paymentInfoTypes);
    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [entityPaymentInfos, setEntityPaymentInfos] = useState([]);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response2 = await httpClient.get("/externs/paymentinfos");
                setEntityPaymentInfos(response2.data);
                const response3 = await httpClient.get("/users/paymentinfos");
                setEntityPaymentInfos(prev => prev.concat(response3.data));
            }catch(e){
                console.error("Error during fetching paymentInfoTypes:", e);
            }
        }
        fetchPaymentInfos();
    }, [])

    const handleNameChange = (e, id) => {
        setUpdatedPaymentInfoTypes(updatedPaymentInfoTypes.map(
            item => item.id === id ? {id: item.id, name: e.target.value} : item
        ));
        setIsSaveDisabled(false);
    }

    const deletePaymentType = (id) => {
        setUpdatedPaymentInfoTypes(updatedPaymentInfoTypes.filter(item => item.id !== id));
        setIsSaveDisabled(false);
    }

    const shouldShowDeletePaymentTypeButton = (id) => {
        return !entityPaymentInfos.some(item => item.paymentInfo.id === id);
    }

    const handleSave = () => {
        updatedPaymentInfoTypes.forEach(updatedPaymentInfoType => {
            if(paymentInfoTypes.find(x => x.id === updatedPaymentInfoType.id).name !== updatedPaymentInfoType.name) {
                httpClient.put("/paymentinfos/" + updatedPaymentInfoType.id, updatedPaymentInfoType)
                    .catch(e => console.error("Error during updating payment type:", e));
                setPaymentInfoTypes(
                    prev =>
                        prev.map(
                            paymentInfoType =>
                                paymentInfoType.id === updatedPaymentInfoType.id ?
                                    {id: updatedPaymentInfoType.id, name: updatedPaymentInfoType.name}
                                    : paymentInfoType
                        )
                );
            }
        })

        paymentInfoTypes.forEach(item => {
            if(!updatedPaymentInfoTypes.some(x => x.id === item.id)) {
                httpClient.delete("/paymentinfos/" + item.id)
                    .catch(e => console.error("Error during deleting payment type:", e));
                setPaymentInfoTypes(prev => prev.filter(x => x.id !== item.id));
            }
        })
        closeWindow()
    }

    return(
        <div>
            <Modal show={true} onHide={closeWindow} centered={true}>
                <Modal.Header closeButton>
                    <Modal.Title>Verwaltung Zahlungsarten</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>Alter Name</th>
                                <th>Neuer Name</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {updatedPaymentInfoTypes.map((item) => (
                                <tr key={item.id}>
                                    <td>{paymentInfoTypes.find(x => x.id === item.id).name}</td>
                                    <td>
                                        <input type="text"
                                               value={item.name}
                                               onChange={(e) => handleNameChange(e, item.id)}
                                        />
                                    </td>
                                    <td>
                                        <button className="btn btn-danger"
                                                onClick={() => deletePaymentType(item.id)}
                                                disabled={!shouldShowDeletePaymentTypeButton(item.id)}>
                                            <FaRegTrashAlt className="centered-label"/>
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            {updatedPaymentInfoTypes.length === 0 &&
                                (
                                    <tr>
                                        <td colSpan="3">Keine Zahlungsarten vorhanden</td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                    <div className="text-danger">
                        <p>Es können nur Zahlungsarten gelöscht werden, die nicht in Verbindung zu einem Benutzer stehen!</p>
                        <p>Die getätigten Änderungen sind für jeden Benutzer einsehbar!</p>
                    </div>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={closeWindow}>
                        Schließen
                    </Button>
                    <Button variant="primary" onClick={handleSave} disabled={isSaveDisabled}>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default EditPaymentInfoTypes;