import React, {useState} from "react";
import {Modal, Button} from 'react-bootstrap';
import TransactionUtils from "../TransactionUtils";

const UpdateTransactionWindow = ({transaction, updateTransaction, closeWindow}) => {
    const [editedTransaction, setEditedTransaction] = useState(transaction);

    const handleSave = () => {
        updateTransaction(editedTransaction);
        closeWindow();
    };

    const handleAmountChange = (event) => {
        const { name, value } = event.target;
        setEditedTransaction(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const formattedDate = TransactionUtils.formatTimestamp(transaction.timestamp);

    return (
        <Modal show={true} onHide={closeWindow} centered={true}>
            <Modal.Header closeButton>
                <Modal.Title>Transaktion bearbeiten</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <form>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Type:</label>
                            <input
                                type="text"
                                name="type"
                                value={TransactionUtils.getPaymentTypeTranslation(editedTransaction.paymentType)}
                                disabled
                                className="form-control"
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Datum:</label>
                            <input
                                type="text"
                                name="date"
                                value={formattedDate}
                                disabled
                                className="form-control"
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Sender:</label>
                            <input
                                type="text"
                                name="sender"
                                value={editedTransaction.sender.name}
                                disabled
                                className="form-control"
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Empfänger:</label>
                            <input
                                type="text"
                                name="receiver"
                                value={editedTransaction.receiver.name}
                                disabled
                                className="form-control"
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Betrag:</label>
                            <input
                                type="number"
                                name="amount"
                                value={editedTransaction.amount}
                                onChange={handleAmountChange}
                                className="form-control"
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Über Kasse:</label>
                            <input
                                type="text"
                                name="overVcr"
                                value={editedTransaction.overVcr == null ? '-' : editedTransaction.overVcr.name}
                                disabled
                                className="form-control"
                            />
                        </div>
                    </div>
                </form>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={closeWindow}>
                    Abbrechen
                </Button>
                <Button variant="primary" onClick={handleSave}>
                    Speichern
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default UpdateTransactionWindow;