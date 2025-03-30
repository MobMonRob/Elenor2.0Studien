import React, {useState} from "react";
import Sidebar from "./sidebar";
import TransactionPage from "./transactionPage";

const Mainpage = ({
    users,
    cashRegisters,
    externs,
    setUsers,
    setCashRegisters,
    setExterns
                  }) => {

    const [transactionFilter, setTransactionFilter] = useState("");
    const [transactionFilterName, setTransactionFilterName] = useState("");

    return (
        <div className="d-flex" style={{paddingTop: '55px'}}>
            <Sidebar
                setTransactionFilter={setTransactionFilter}
                setTransactionFilterName={setTransactionFilterName}
                users={users}
                cashRegisters={cashRegisters}
                externs={externs}
                setExterns={setExterns}
                setCashRegisters={setCashRegisters}
            />
            <TransactionPage
                transactionFilter={transactionFilter}
                transactionFilterName={transactionFilterName}
                setTransactionFilter={setTransactionFilter}
                setTransactionFilterName={setTransactionFilterName}
                users={users}
                cashregisters={cashRegisters}
                externs={externs}
                setUsers={setUsers}
                setCashRegisters={setCashRegisters}
            />
        </div>
    );
}

export default Mainpage;