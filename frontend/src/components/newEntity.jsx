import React, {useState} from "react";
import {Button, Modal} from "react-bootstrap";

const NewEntity = ({closeWindow, entities, persistNewEntity, title}) => {
    const [name, setName] = useState("");

    const handleSave = () => {
        const newEntity = {
            name: name
        }
        persistNewEntity(newEntity);
        closeWindow();
    };

    const nameExists = entities.some(
        (enitity) => enitity.name.toLowerCase() === name.toLowerCase().trim()
    );


    return (
        <Modal show={true} onHide={closeWindow} centered={true}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <div className="col-md-6 form-group">
                    <label>Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="form-control"
                        placeholder="Eingabe Name"
                    />
                </div>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={closeWindow}>
                    Abbrechen
                </Button>
                <Button variant="primary" onClick={handleSave} disabled={name.trim() === "" || nameExists}>
                    Hinzufügen
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default NewEntity;