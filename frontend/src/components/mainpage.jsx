import React, {useState} from "react";
import Sidebar from "./sidebar";
import TransactionPage from "./transactionPage";

const Mainpage = ({users, cashRegisters, externs}) => {

    const [transactionFilter, setTransactionFilter] = useState("");
    const [transactionFilterName, setTransactionFilterName] = useState("");

    return (
        <div className="d-flex">
            <Sidebar
                setTransactionFilter={setTransactionFilter}
                setTransactionFilterName={setTransactionFilterName}
                users={users}
                cashRegisters={cashRegisters}
                externs={externs}
            />
            <TransactionPage
                transactionFilter={transactionFilter}
                transactionFilterName={transactionFilterName}
                setTransactionFilter={setTransactionFilter}
                setTransactionFilterName={setTransactionFilterName}
                users={users}
                cashregisters={cashRegisters}
                externs={externs}
            />
        </div>
    );
}

export default Mainpage;