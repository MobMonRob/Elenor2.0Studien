import React, {useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient} from "../HttpClient";

const EditCashRegisterModal = ({
                              closeWindow,
                              setCashRegisters,
                              cashRegisters,
                              cashRegister,
                              resetDisplayedTransactions,
                              renameTransactions,
                              isDeletable
                          }) => {
    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [updatedCashRegister, setUpdatedCashRegister] = useState(cashRegister);

    const handleSave = async () => {
        try{
            const response = await httpClient.put("/virtualcashregisters/" + cashRegister.id, updatedCashRegister);
            setCashRegisters(cashRegisters.map(cashRegister => cashRegister.id === response.data.id ? response.data : cashRegister));
        }catch(e){
            console.error("An error occurred during updating cash register:",e);
        }
        renameTransactions(cashRegister.id, updatedCashRegister.name);
        closeWindow();
    }

    const handleDelete = async () => {
        await httpClient.delete("/virtualcashregisters/" + cashRegister.id);
        resetDisplayedTransactions();
        setCashRegisters(cashRegisters.filter(cr => cr.id !== cashRegister.id));
        closeWindow();
    }

    const handleNameChange = (e) => {
        if (cashRegisters.some(cashRegister => cashRegister.name === e.target.value)) {
            setIsSaveDisabled(true)
        }else{
            setIsSaveDisabled(false);
        }
        setUpdatedCashRegister({...updatedCashRegister, name: e.target.value})
    }

    return (
        <div>
            <Modal show={true} onHide={closeWindow} centered={true} >
                <Modal.Header closeButton>
                    <Modal.Title>Virtuelles Konto - {cashRegister.name}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Name:</label>
                            <input
                                type="text"
                                name="firstName"
                                value={updatedCashRegister.name}
                                onChange={(e) => handleNameChange(e)}
                                className="form-control"
                            />
                        </div>
                        <div className="col-md-6 form-group">
                            <label>Saldo:</label>
                            <input
                                type="text"
                                name="balance"
                                value={cashRegister.balance + " €"}
                                className="form-control"
                                disabled={true}
                            />
                        </div>
                    </div>
                    {!isDeletable && (
                        <div className="alert-danger alert mt-3">
                            Das Konto kann nicht gelöscht werden, da es noch Transaktionen enthält.
                        </div>
                    )}
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="danger" onClick={handleDelete} className="me-auto" disabled={!isDeletable}>
                        Konto löschen
                    </Button>
                    <Button variant="secondary" onClick={closeWindow}>
                        Schließen
                    </Button>
                    <Button variant="primary" onClick={handleSave} disabled={isSaveDisabled}>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default EditCashRegisterModal;