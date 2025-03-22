import React from "react";
import {Modal, Button} from 'react-bootstrap';

class UpdateTransactionWindow extends React.Component {
    state = {
        editedTransaction: this.props.transaction
    }

    handleSave = () => {
        this.props.updateTransaction(this.state.editedTransaction);
        this.props.closeWindow();
    };

    handleAmountChange = (event) => {
        const { name, value } = event.target;
        this.setState(prevState => ({
            editedTransaction: {
                ...prevState.editedTransaction,
                [name]: value
            }
        }));
    };

    render() {
        const timestamp = this.props.transaction.timestamp;
        const date = new Date(
            timestamp[0], // Jahr
            timestamp[1] - 1, // Monat (0-basiert, daher -1)
            timestamp[2], // Tag
            timestamp[3], // Stunde
            timestamp[4], // Minuten
            timestamp[5], // Sekunden
            Math.floor(timestamp[6] / 1000000) // Millisekunden
        );
        const formattedDate = `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}; ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')} Uhr`;

        return (
            <Modal show={true} onHide={this.props.closeWindow} centered={true}>
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
                                    value={this.props.translatedPaymentType}
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
                                    value={this.state.editedTransaction.sender.name}
                                    disabled
                                    className="form-control"
                                />
                            </div>

                            <div className="col-md-6 form-group">
                                <label>Empfänger:</label>
                                <input
                                    type="text"
                                    name="receiver"
                                    value={this.state.editedTransaction.receiver.name}
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
                                    value={this.state.editedTransaction.amount}
                                    onChange={this.handleAmountChange}
                                    className="form-control"
                                />
                            </div>

                            <div className="col-md-6 form-group">
                                <label>Über Kasse:</label>
                                <input
                                    type="text"
                                    name="overVcr"
                                    value={this.state.editedTransaction.overVcr == null ? '-' : this.state.editedTransaction.overVcr.name}
                                    disabled
                                    className="form-control"
                                />
                            </div>
                        </div>
                    </form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.props.closeWindow}>
                        Abbrechen
                    </Button>
                    <Button variant="primary" onClick={this.handleSave}>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }
}

export default UpdateTransactionWindow;