import React from "react";
import Transaction from "./transaction";
import { httpClient, keycloak } from "../HttpClient";

class TransactionPage extends React.Component {
    state = {
        displayedTransactions: [],
        everyTransaction: [],
        subjectId: null
    };

    deleteTransaction = async (transactionId) => {
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

    async componentDidMount() {
        try {
            if (!keycloak.authenticated) {
                await keycloak.init({ onLoad: "login-required" });
            }
            const token = keycloak.token;
            httpClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

            const response = await httpClient.get("/payments");
            this.setState({
                everyTransaction: response.data,
                displayedTransactions: response.data,
                subjectId: keycloak.tokenParsed?.sub
            });
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    }

    componentDidUpdate(prevProps) {
        if (prevProps.transactionFilter !== this.props.transactionFilter) {
            this.setState({
                displayedTransactions: this.props.transactionFilter
                    ? this.state.everyTransaction.filter((transaction) =>
                        transaction.overVcr?.entityId === this.props.transactionFilter ||
                        transaction.sender.entityId === this.props.transactionFilter ||
                        transaction.receiver.entityId === this.props.transactionFilter
                    )
                    : this.state.everyTransaction,
            });
        }
    }

    render() {
        return (
            <div className="container mt-4 position-relative">
                <div className="d-flex justify-content-between align-items-center">
                    <button className="btn btn-outline-dark position-absolute" style={{top: "3px", left: "12px"}} onClick={() =>
                    {
                        this.props.setTransactionFilter("");
                        this.props.setTransactionFilterName("");
                    }} disabled={!this.props.transactionFilter}>
                        <i className="bi bi-x-circle"></i> Filter löschen
                    </button>
                    <h2 className="text-center mb-4 flex-grow-1">
                        Transaktionsübersicht{this.props.transactionFilterName && ` (${this.props.transactionFilterName})`}
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
                    {this.state.displayedTransactions.map((transaction) => (
                        <Transaction key={transaction.transactionId} transaction={transaction} jwtSubject={this.state.subjectId} delete={this.deleteTransaction} />
                    ))}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default TransactionPage;
