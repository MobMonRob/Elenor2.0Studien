import React, {useEffect, useState} from "react";
import Transaction from "./transaction";
import { httpClient, keycloak } from "../HttpClient";
import UpdateTransactionWindow from "./updateTransaction";

const TransactionPage = ({setTransactionFilter, setTransactionFilterName, transactionFilterName, transactionFilter}) => {
    const [displayedTransactions, setDisplayedTransactions] = useState([]);
    const [everyTransaction, setEveryTransaction] = useState([]);
    const [subjectId, setSubjectId] = useState(null);
    const [isUpdatedWindowOpen, setIsUpdatedWindowOpen] = useState(false);
    const [editedTransaction, setEditedTransaction] = useState(null);

    const deleteTransaction = async (transactionId) => {
        try {
            await httpClient.delete(`/payments/${transactionId}`);
            this.setState({
                everyTransaction: this.state.everyTransaction.filter((transaction) => transaction.transactionId !== transactionId),
                displayedTransactions: this.state.displayedTransactions.filter((transaction) => transaction.transactionId !== transactionId)
            });
        } catch (error) {
            console.error("Error deleting transaction:", error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!keycloak.authenticated) {
                    await keycloak.init({ onLoad: "login-required" });
                }
                const token = keycloak.token;
                httpClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

                const response = await httpClient.get("/payments");
                setEveryTransaction(response.data);
                setDisplayedTransactions(response.data);
                setSubjectId(keycloak.tokenParsed?.sub);
            } catch (error) {
                console.error("Error fetching payments:", error);
            }
        };
        fetchData();
    }, []);

    useEffect(() => {
        if (transactionFilter) {
            setDisplayedTransactions(
                everyTransaction.filter((transaction) =>
                    transaction.overVcr?.entityId === transactionFilter ||
                    transaction.sender.entityId === transactionFilter ||
                    transaction.receiver.entityId === transactionFilter
                )
            );
        } else {
            setDisplayedTransactions(everyTransaction);
        }
    }, [transactionFilter, everyTransaction]);

    const getTransactionOutputFormatFromInputFormat = (transaction) => {
        return {
            senderId: transaction.sender.entityId,
            receiverId: transaction.receiver.entityId,
            amount: transaction.amount,
            ...(transaction.overVcr && transaction.overVcr.entityId ? { vcrId: transaction.overVcr.entityId } : {})
        };
    }

    const updateTransaction = async (updatedTransaction) => {
        try{
            const response = await httpClient.put(
                `/payments/${updatedTransaction.transactionId}`,
                getTransactionOutputFormatFromInputFormat(updatedTransaction)
            );

            const updatedTransactionFromApi = response.data;

            setEveryTransaction((prevTransactions) =>
                prevTransactions.map((transaction) =>
                    transaction.transactionId === updatedTransactionFromApi.transactionId ? updatedTransactionFromApi : transaction
                )
            );

            setDisplayedTransactions((prevDisplayed) =>
                prevDisplayed.map((transaction) =>
                    transaction.transactionId === updatedTransactionFromApi.transactionId ? updatedTransactionFromApi : transaction
                )
            );
        } catch (error) {
            console.error("Error updating transaction:", error);
        }
    };

    return (
        <div className="container mt-4 position-relative">
            <div className="d-flex justify-content-between align-items-center">
                <button className="btn btn-outline-dark position-absolute" style={{top: "3px", left: "12px"}} onClick={() =>
                {
                    setTransactionFilter("");
                    setTransactionFilterName("");
                }} disabled={!transactionFilter}>
                    <i className="bi bi-x-circle"></i> Filter löschen
                </button>
                <h2 className="text-center mb-4 flex-grow-1">
                    Transaktionsübersicht{transactionFilterName && ` (${transactionFilterName})`}
                </h2>
            </div>
            <table className="table table-striped" style={{ border: '2px solid #dee2e6', overflow: 'hidden', tableLayout: 'fixed' }}>
                <thead>
                <tr>
                    <th scope="col">Art</th>
                    <th scope="col">Sender</th>
                    <th scope="col">Empfänger</th>
                    <th scope="col">Über Kasse</th>
                    <th scope="col">Betrag</th>
                    <th scope="col">Datum</th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <tbody>
                {displayedTransactions.map((transaction) => (
                    <Transaction
                        key={transaction.transactionId}
                        transaction={transaction}
                        jwtSubject={subjectId}
                        deleteTransaction={deleteTransaction}
                        openUpdatedWindow={() => setIsUpdatedWindowOpen(true)}
                        setEditedTransaction={setEditedTransaction}
                    />
                ))}
                </tbody>
            </table>
            {isUpdatedWindowOpen
                && editedTransaction
                && (
                    <UpdateTransactionWindow
                        transaction={editedTransaction}
                        updateTransaction={updateTransaction}
                        closeWindow={() => setIsUpdatedWindowOpen(false)}
                    />
                )}

        </div>
    );

}

export default TransactionPage;
