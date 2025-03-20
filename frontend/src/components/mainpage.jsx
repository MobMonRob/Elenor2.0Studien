import React from "react";
import Sidebar from "./sidebar";
import TransactionPage from "./transactionPage";

class Mainpage extends React.Component {
    state = {
        transactionFilter: "",
        transactionFilterName: ""
    }

    setTransactionFilter = (filter) => {
        this.setState({
            transactionFilter: filter
        });
    }

    setTransactionFilterName = (filterName) => {
        this.setState({
            transactionFilterName: filterName
        });
    }

    render() {
        return (
            <div className="d-flex">
                <Sidebar setTransactionFilter={this.setTransactionFilter} setTransactionFilterName={this.setTransactionFilterName} />
                <TransactionPage
                    transactionFilter={this.state.transactionFilter}
                    transactionFilterName={this.state.transactionFilterName}
                    setTransactionFilter={this.setTransactionFilter}
                    setTransactionFilterName={this.setTransactionFilterName}
                />
            </div>
        );
    }
}

export default Mainpage;