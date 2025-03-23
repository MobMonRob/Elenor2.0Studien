import React, {useState} from "react";
import Sidebar from "./sidebar";
import TransactionPage from "./transactionPage";

const Mainpage = () => {

    const [transactionFilter, setTransactionFilter] = useState("");
    const [transactionFilterName, setTransactionFilterName] = useState("");

    return (
        <div className="d-flex">
            <Sidebar setTransactionFilter={setTransactionFilter} setTransactionFilterName={setTransactionFilterName} />
            <TransactionPage
                transactionFilter={transactionFilter}
                transactionFilterName={transactionFilterName}
                setTransactionFilter={setTransactionFilter}
                setTransactionFilterName={setTransactionFilterName}
            />
        </div>
    );
}

export default Mainpage;