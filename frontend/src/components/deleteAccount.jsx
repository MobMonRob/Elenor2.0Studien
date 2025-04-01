import React from "react";
import {Button, Modal} from "react-bootstrap";

const DeleteAccount = ({deleteAccount, closeWindow}) => {
    return (
        <div>
            <Modal show={true} onHide={closeWindow} centered={true}>
                <Modal.Header closeButton>
                    <Modal.Title>Account löschen</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <p className="alert-danger alert">Möchten Sie wirklich Ihren Account endgültig löschen?</p>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={closeWindow}>
                        Abbrechen
                    </Button>
                    <Button variant="danger" onClick={deleteAccount}>
                        Löschen
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default DeleteAccount;