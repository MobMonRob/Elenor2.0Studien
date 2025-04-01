import React from "react";
import Select from "react-select";
import {FaRegTrashAlt} from "react-icons/fa";

const EditEntityPaymentInfo = ({
                                updatedEntityPaymentInfos,

                               }) => {


    return(
        <div>
            <label>Zahlungsinformationen:</label>
            {updatedUserPaymentInfos.map((userPaymentInfo, index) => (
                <div className="row" key={index}>
                    <div className="col-md-6 form-group">
                        <label></label>
                        <Select
                            options={
                                paymentInfoTypesOptions.filter(option =>
                                    !updatedUserPaymentInfos.map(info => info.paymentInfo.id).includes(option.value)
                                    || userPaymentInfo.paymentInfo.id === option.value)
                            }
                            value={paymentInfoTypesOptions.find(option => option.value === userPaymentInfo.paymentInfo.id) || undefined}
                            onChange={(e) => changePaymentInfoId(e, userPaymentInfo.paymentInfo.id)}
                            placeholder="Auswahl Zahlungsinformation"
                        />
                    </div>
                    <div className="col-md-6 form-group">
                        <label></label>
                        <div className="input-group">
                            <input
                                type="text"
                                name="zahlungsinformationInput"
                                value={userPaymentInfo.paymentAddress}
                                onChange={(e) => changePaymentInfoContent(e, userPaymentInfo.paymentInfo.id)}
                                placeholder="Zahlungsadresse"
                                className="form-control"
                            />
                            <button className="btn btn-danger input-group-sm" onClick={() => deletePaymentInfo(userPaymentInfo.paymentInfo.id)}>
                                <FaRegTrashAlt className="centered-label"/>
                            </button>
                        </div>
                    </div>
                </div>
            ))}
            {updatedUserPaymentInfos.length < 3 &&
                <div className="row">
                    <div className="col-md-6 form-group">
                        <label></label>
                        <Select
                            options={
                                paymentInfoTypesOptions.filter(option =>
                                    !updatedUserPaymentInfos.map(info => info.paymentInfo.id).includes(option.value))
                            }
                            value=""
                            onChange={(e) => addPaymentInfoId(e)}
                            placeholder="Zahlungsart"
                        />
                    </div>
                    <div className="col-md-6 form-group">
                        <label></label>
                        <input
                            type="text"
                            name="zahlungsinformationInput"
                            value={undefined}
                            className="form-control"
                            placeholder="Zahlungsadresse"
                            disabled={true}
                        />
                    </div>
                </div>
            }
            <div className="row">
                <div className="col-md-6 form-group">
                    <label></label>
                    <button
                        className="form-control bg-primary-subtle"
                        onClick={() => setNewPaymentTypeWindowOpen(true)}
                    >
                        Zahlungsart hinzufügen
                    </button>
                </div>
            </div>
        </div>
    )
}

export default EditEntityPaymentInfo;