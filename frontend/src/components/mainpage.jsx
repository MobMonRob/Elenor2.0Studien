import React from "react";
import Sidebar from "./sidebar";
import TransactionPage from "./transactionPage";

class Mainpage extends React.Component {
    render() {
        return (
            <div className="d-flex">
                <Sidebar/>
                <TransactionPage/>
            </div>
        );
    }
}

export default Mainpage;