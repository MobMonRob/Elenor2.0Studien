import React from "react";
import { FaRegTrashAlt } from "react-icons/fa";
import { MdEdit } from "react-icons/md";
import "../index.css"

class Transactions extends React.Component {
    state = {
        isUpdatedWindowOpen: false
    }
    render() {
        const { transaction } = this.props;

        if (!transaction || !transaction.timestamp) {
            return null;
        }

        const timestamp = transaction.timestamp;
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

        const paymentTypeTranslations = {
            "User2Vcr": "Mitglied zu Kasse",
            "Vcr2User": "Kasse zu Mitglied",
            "Vcr2Vcr": "Kasse zu Kasse",
            "Extern2UserOverVcr": "Extern zu Mitglied über Kasse",
            "User2ExternOverVcr": "Mitglied zu Extern über Kasse",
            "User2User": "Mitglied zu Mitglied",
        };

        const translatedPaymentType = paymentTypeTranslations[this.props.transaction.paymentType]  || "Unbekannt";

        const shouldShowButtons =
            this.props.transaction.paymentType === "VcrToVcr" ||
            this.props.jwtSubject === this.props.transaction.sender.entityId ||
            this.props.jwtSubject === this.props.transaction.receiver.entityId;

        const openUpdatedWindow = () => {
            this.props.openUpdatedWindow();
            this.props.setEditedTransaction(this.props.transaction);
            this.props.setEditedTransactionTranslatedPaymentType(translatedPaymentType);
        };

        return (
            <tr>
                <td>{translatedPaymentType}</td>
                <td>{this.props.transaction.sender.name}</td>
                <td>{this.props.transaction.receiver.name}</td>
                <td>{this.props.transaction.overVcr == null ? '-' : this.props.transaction.overVcr.name}</td>
                <td>{this.props.transaction.amount} €</td>
                <td>{formattedDate}</td>
                <td style={{ textAlign: "right" }}>
                    <button className="btn btn-primary" style={{ marginRight: "10px" }} onClick={openUpdatedWindow} disabled={!shouldShowButtons}>
                        <MdEdit className="centered-label"/>
                    </button>
                    <button className="btn btn-danger" onClick={() => this.props.delete(this.props.transaction.transactionId)} disabled={!shouldShowButtons}>
                        <FaRegTrashAlt className="centered-label"/>
                    </button>
                </td>
            </tr>
        );
    }
}

export default Transactions;