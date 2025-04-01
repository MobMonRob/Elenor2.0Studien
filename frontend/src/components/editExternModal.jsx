import React, {useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient} from "../HttpClient";

const EditExternModal = ({
                         closeWindow,
                         setExterns,
                         externs,
                         extern,
                         resetDisplayedTransactions,
                         renameTransactions,
                         isDeletable,
                         setTransactionFilterName
                        }) => {

    const [isSaveDisabled, setIsSaveDisabled] = useState(true);
    const [updatedExtern, setUpdatedExtern] = useState(extern);

    const handleSave = async () => {
        try{
            const response = await httpClient.put("/externs/" + extern.id, updatedExtern);
            setExterns(externs.map(cashRegister => cashRegister.id === response.data.id ? response.data : cashRegister));
        }catch(e){
            console.error("An error occurred during updating cash register:",e);
        }
        renameTransactions(extern.id, extern.name);
        setTransactionFilterName(updatedExtern.name);
        closeWindow();
    }

    const handleDelete = async () => {
        await httpClient.delete("/externs/" + extern.id);
        resetDisplayedTransactions();
        setExterns(externs.filter(ex => ex.id !== extern.id));
        closeWindow();
    }

    const handleNameChange = (e) => {
        if (externs.some(extern => extern.name === e.target.value)) {
            setIsSaveDisabled(true)
        }else{
            setIsSaveDisabled(false);
        }
        setUpdatedExtern({...updatedExtern, name: e.target.value})
    }

    return(
        <div>
            <Modal show={true} onHide={closeWindow} centered={true} >
                <Modal.Header closeButton>
                    <Modal.Title>Externes Konto - {extern.name}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Name:</label>
                            <input
                                type="text"
                                name="firstName"
                                value={updatedExtern.name}
                                onChange={(e) => handleNameChange(e)}
                                className="form-control"
                            />
                        </div>
                    </div>
                    {!isDeletable && (
                        <div className="alert-danger alert mt-3">
                            Das Konto kann nicht gelöscht werden, da noch Transaktionen mit dem Konto in Verbindung stehen.
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
    )
}

export default EditExternModal;