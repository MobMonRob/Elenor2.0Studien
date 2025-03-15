import React from "react";
import Transaction from "./transaction";

class TransactionPage extends React.Component {
    render() {
            return (
                <div className="container mt-4">
                    <h2 className="text-center mb-4">Transaktionsübersicht</h2>
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
                        <Transaction/>
                        </tbody>
                    </table>
                </div>
            );

    }
}

export default TransactionPage;