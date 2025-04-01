import React from "react";

const ChangeNameViewSaldo = ({name, balance, handleNameChange}) => {
    return (
        <div className="row">
            <div className="col-md-6 form-group">
                <label>Name:</label>
                <input
                    type="text"
                    name="firstName"
                    value={name}
                    onChange={(e) => handleNameChange(e)}
                    className="form-control"
                />
            </div>
            <div className="col-md-6 form-group">
                <label>Saldo:</label>
                <input
                    type="text"
                    name="balance"
                    value={balance + " €"}
                    className="form-control"
                    disabled={true}
                />
            </div>
        </div>
    )
}

export default ChangeNameViewSaldo;