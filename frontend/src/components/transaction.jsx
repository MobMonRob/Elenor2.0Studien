import React from "react";
import { FaRegTrashAlt } from "react-icons/fa";
import { MdEdit } from "react-icons/md";
import "../index.css"

class Transactions extends React.Component {
    state = {}
    render() {
        return (
            <tr>
                <td>Max Mustermann</td>
                <td>Erika Müller</td>
                <td>Bank 1</td>
                <td>100,00 €</td>
                <td>12.03.2025</td>
                <td>
                    <button className="btn btn-primary" style={{marginRight: "10px"}}><MdEdit className="centered-label"/></button>
                    <button className="btn btn-danger"><FaRegTrashAlt  className="centered-label"/></button>
                </td>
            </tr>
        );
    }
}

export default Transactions;