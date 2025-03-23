import React from "react";
import { FaRegTrashAlt } from "react-icons/fa";
import { MdEdit } from "react-icons/md";
import "../index.css"
import TransactionUtils from "../TransactionUtils";

const Transactions = ({transaction, jwtSubject, openUpdatedWindow, setEditedTransaction, deleteTransaction}) => {
    if (!transaction || !transaction.timestamp) {
        return null;
    }

    const formattedDate = TransactionUtils.formatTimestamp(transaction.timestamp);

    const translatedPaymentType = TransactionUtils.getPaymentTypeTranslation(transaction.paymentType)  || "Unbekannt";

    const shouldShowButtons =
        transaction.paymentType === "VcrToVcr" ||
        jwtSubject === transaction.sender.entityId ||
        jwtSubject === transaction.receiver.entityId;

    const openUpdatedWindowComponent = () => {
        openUpdatedWindow();
        setEditedTransaction(transaction);
    };

    return (
        <tr>
            <td>{translatedPaymentType}</td>
            <td>{transaction.sender.name}</td>
            <td>{transaction.receiver.name}</td>
            <td>{transaction.overVcr == null ? '-' : transaction.overVcr.name}</td>
            <td>{transaction.amount} €</td>
            <td>{formattedDate}</td>
            <td style={{ textAlign: "right" }}>
                <button className="btn btn-primary" style={{ marginRight: "10px" }} onClick={openUpdatedWindowComponent} disabled={!shouldShowButtons}>
                    <MdEdit className="centered-label"/>
                </button>
                <button className="btn btn-danger" onClick={() => deleteTransaction(transaction.transactionId)} disabled={!shouldShowButtons}>
                    <FaRegTrashAlt className="centered-label"/>
                </button>
            </td>
        </tr>
    );

}

export default Transactions;