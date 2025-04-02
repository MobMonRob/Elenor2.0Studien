import React, {useEffect, useState} from "react";
import Transaction from "./transaction";
import { httpClient, keycloak } from "../HttpClient";
import UpdateTransactionWindow from "./updateTransaction";
import NewTransactionWindow from "./newTransaction";
import InfoProfileModal from "./infoProfileModal";
import {IoMdInformationCircleOutline} from "react-icons/io";
import {MdEdit} from "react-icons/md";
import EditCashRegisterModal from "./editCashRegisterModal";
import EditExternModal from "./editExternModal";

const TransactionPage = ({setTransactionFilter,
                             setTransactionFilterName,
                             transactionFilterName,
                             transactionFilter,
                             users,
                             externs,
                             cashregisters,
                             setUsers,
                             setCashRegisters,
                             setExterns
                         }) => {
    const [displayedTransactions, setDisplayedTransactions] = useState([]);
    const [everyTransaction, setEveryTransaction] = useState([]);
    const [subjectId, setSubjectId] = useState(null);
    const [isUpdatedWindowOpen, setIsUpdatedWindowOpen] = useState(false);
    const [isNewTransactionWindowOpen, setIsNewTransactionWindowOpen] = useState(false);
    const [editedTransaction, setEditedTransaction] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [displayedTransactionsOnPage, setDisplayedTransactionsOnPage] = useState([]);
    const [isUserInformationWindowOpen, setIsUserInformationWindowOpen] = useState(false);
    const [isCashRegisterEditWindowOpen, setIsCashRegisterEditWindowOpen] = useState(false);
    const [isExternEditWindowOpen, setIsExternEditWindowOpen] = useState(false);

    const transactionsPerPage = 10;
    const totalPages = Math.max(Math.ceil(displayedTransactions.length / transactionsPerPage), 1);
    const indexOfLastTransaction = currentPage * transactionsPerPage;
    const indexOfFirstTransaction = indexOfLastTransaction - transactionsPerPage;


    useEffect(() => {
        setDisplayedTransactionsOnPage(displayedTransactions.slice(indexOfFirstTransaction, indexOfLastTransaction))
    }, [currentPage, displayedTransactions, indexOfFirstTransaction, indexOfLastTransaction]);


    const deleteTransaction = async (transactionId) => {
        try {
            await httpClient.delete(`/payments/${transactionId}`);
            const transaction = everyTransaction.find((transaction) => transaction.transactionId === transactionId)
            setEveryTransaction(everyTransaction.filter((transaction) => transaction.transactionId !== transactionId));
            setDisplayedTransactions(displayedTransactions.filter((transaction) => transaction.transactionId !== transactionId));
            updateListsBecauseOfTransaction(transaction);
        } catch (error) {
            console.error("Error deleting transaction:", error);
        }
    };

    const newTransaction = async (newTransaction) => {
        try {
            const response = await httpClient.post(`/payments`, newTransaction);
            const transaction = response.data;
            setEveryTransaction([transaction, ...everyTransaction]);
            setDisplayedTransactions([transaction, ...displayedTransactions]);
            updateListsBecauseOfTransaction(transaction);
        } catch (error) {
            console.error("Error creating transaction:", error);
        }
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
            updateListsBecauseOfTransaction(updatedTransactionFromApi);
        } catch (error) {
            console.error("Error updating transaction:", error);
        }
    };

    const resetDisplayedTransactions = () => {
        setDisplayedTransactions(everyTransaction);
        setTransactionFilter("");
        setTransactionFilterName("");
    }

    const renameTransactions = (entityId, newName) => {

        setEveryTransaction(everyTransaction.map((
            transaction) => {
            if(transaction.sender.entityId === entityId){
                transaction.sender.name = newName;
            }
            if(transaction.receiver.entityId === entityId){
                transaction.receiver.name = newName;
            }
            if(transaction.overVcr?.entityId === entityId){
                transaction.overVcr.name = newName;
            }
            return transaction;
        }));
        setDisplayedTransactions(displayedTransactions.map((transaction) => {
            if(transaction.sender.entityId === entityId){
                transaction.sender.name = newName;
            }
            if(transaction.receiver.entityId === entityId){
                transaction.receiver.name = newName;
            }
            if(transaction.overVcr?.entityId === entityId){
                transaction.overVcr.name = newName;
            }
            return transaction;
        }));
    }


    useEffect(() => {
        const fetchData = async () => {
            try {
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

    const updateListsBecauseOfTransaction = (updatedTransaction) => {
        const paymentType = updatedTransaction.paymentType;
        if(paymentType.startsWith("User")){
            updateUserInList(updatedTransaction.sender);
        }else if(paymentType.startsWith("Vcr")){
            updateCashRegisterInList(updatedTransaction.sender);
        }

        if(paymentType.endsWith("2User")){
            updateUserInList(updatedTransaction.receiver);
        }else if(paymentType.endsWith("2Vcr")){
            updateCashRegisterInList(updatedTransaction.receiver);
        }else if(paymentType.endsWith("OverVcr")){
            updateCashRegisterInList(updatedTransaction.overVcr);
            if (paymentType.endsWith("2UserOverVcr")){
                updateUserInList(updatedTransaction.receiver);
            }
        }
    }

    const updateUserInList = async (entity) => {
        const id = entity.entityId;
        try {
            const response = await httpClient.get(`/users/${id}`);
            const updatedUser = response.data;
            setUsers((prevUsers) =>
                prevUsers.map((user) =>
                    user.id === updatedUser.id ? updatedUser : user
                )
            );
        } catch (error) {
            console.error("Error getting user:", error);
        }
    }

    const updateCashRegisterInList = async (entity) => {
        const id = entity.entityId;
        try{
            const response = await httpClient.get(`/virtualcashregisters/${id}`);
            const updatedCashRegister = response.data;
            setCashRegisters((prevCashRegisters) =>
                prevCashRegisters.map((cashRegister) =>
                    cashRegister.id === updatedCashRegister.id ? updatedCashRegister : cashRegister
                )
            );
        }catch(error){
            console.error("Error getting cash register:", error);
        }
    }

    return (
        <div className="mt-4 position-relative" style={{width: '75%', margin: 'auto'}}>
            <div className="d-flex justify-content-between align-items-center">
                <button className="btn btn-outline-dark position-absolute" style={{top: "3px", left: "12px"}} onClick={() =>
                {
                    setTransactionFilter("");
                    setTransactionFilterName("");
                }} disabled={!transactionFilter}>
                    <i className="bi bi-x-circle"></i> Filter löschen
                </button>
                <h2 className="text-center mb-4 flex-grow-1 d-inline-flex centered-label">
                    Transaktionsübersicht
                    {transactionFilterName && (
                        <>
                            {" (" + transactionFilterName}
                            {users.map(
                                user => user.id === transactionFilter && (
                                    <button
                                        className="mt-auto mb-auto text-primary nav-link active ms-2"
                                        onClick={() => setIsUserInformationWindowOpen(true)}
                                    >
                                        <IoMdInformationCircleOutline/>
                                    </button>
                                )
                            )}
                            {cashregisters.map(
                                cr => cr.id === transactionFilter && (
                                    <button
                                        className="mt-auto mb-auto text-primary nav-link active ms-2"
                                        onClick={() => setIsCashRegisterEditWindowOpen(true)}
                                    >
                                        <MdEdit/>
                                    </button>
                                )
                            )}
                            {externs.map(
                                extern => extern.id === transactionFilter && (
                                    <button
                                        className="mt-auto mb-auto text-primary nav-link active ms-2"
                                        onClick={() => setIsExternEditWindowOpen(true)}
                                    >
                                        <MdEdit/>
                                    </button>
                                )
                            )}
                            )
                        </>
                    )}
                </h2>
                <button className="btn btn-outline-dark position-absolute" style={{top: "3px", right: "12px"}} onClick={
                    () => setIsNewTransactionWindowOpen(true)
                }>
                    Neue Transaktion
                </button>
            </div>
            <table className="table table-striped" style={{ border: '2px solid #dee2e6', overflow: 'hidden', tableLayout: 'fixed'}}>
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
                {displayedTransactionsOnPage.map((transaction) => (
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
            <div className="d-flex justify-content-center mt-4 gap-4">
                <button
                    className="btn btn-primary mx-2"
                    onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                    disabled={currentPage === 1}
                >
                    Zurück
                </button>
                <span className="mx-2 m-auto">Seite {currentPage} von {totalPages}</span>
                <button
                    className="btn btn-primary mx-2"
                    onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                    disabled={currentPage === totalPages}
                >
                    Weiter
                </button>
            </div>
            {isUpdatedWindowOpen
                && editedTransaction
                && (
                    <UpdateTransactionWindow
                        transaction={editedTransaction}
                        updateTransaction={updateTransaction}
                        closeWindow={() => setIsUpdatedWindowOpen(false)}
                    />
                )}
            {isNewTransactionWindowOpen &&
                <NewTransactionWindow
                    closeWindow={() => setIsNewTransactionWindowOpen(false)}
                    users={users}
                    externs={externs}
                    cashregisters={cashregisters}
                    loggedInUserId={subjectId}
                    saveNewTransaction={newTransaction}
                />}
            {isUserInformationWindowOpen &&
                <InfoProfileModal
                    closeWindow={() => setIsUserInformationWindowOpen(false)}
                    user={users.find(user => user.id === transactionFilter)}
                />
            }
            {isCashRegisterEditWindowOpen &&
                <EditCashRegisterModal
                    closeWindow={() => setIsCashRegisterEditWindowOpen(false)}
                    cashRegister={cashregisters.find(cr => cr.id === transactionFilter)}
                    cashRegisters={cashregisters}
                    setCashRegisters={setCashRegisters}
                    resetDisplayedTransactions={resetDisplayedTransactions}
                    renameTransactions={renameTransactions}
                    isDeletable={displayedTransactions.length === 0}
                    setTransactionFilterName={setTransactionFilterName}
                />
            }
            {isExternEditWindowOpen &&
                <EditExternModal
                    closeWindow={() => setIsExternEditWindowOpen(false)}
                    extern={externs.find(ex => ex.id === transactionFilter)}
                    externs={externs}
                    setExterns={setExterns}
                    resetDisplayedTransactions={resetDisplayedTransactions}
                    renameTransactions={renameTransactions}
                    setTransactionFilterName={setTransactionFilterName}
                    isDeletable={displayedTransactions.length === 0}
                />
            }
        </div>
    );

}

export default TransactionPage;
