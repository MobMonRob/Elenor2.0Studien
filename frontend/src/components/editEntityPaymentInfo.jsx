import React, {useEffect, useState} from "react";
import Select from "react-select";
import {FaRegTrashAlt} from "react-icons/fa";
import NewEntity from "./newEntity";
import {httpClient} from "../HttpClient";
import EditPaymentInfoTypes from "./editPaymentInfoTypes";

const EditEntityPaymentInfo = ({
                                updatedEntityPaymentInfos,
                                setUpdatedEntityPaymentInfos,
                                setIsSaveDisabled,
                                deleteEntityPaymentInfo
                               }) => {

    const [newPaymentTypeWindowOpen, setNewPaymentTypeWindowOpen] = useState(false);
    const [paymentInfoTypes, setPaymentInfoTypes] = useState([]);
    const [isEditPaymentTypesWindowOpen, setIsEditPaymentTypesWindowOpen] = useState(false);

    const paymentInfoTypesOptions = paymentInfoTypes.map(paymentInfoType => ({
        value: paymentInfoType.id,
        label: paymentInfoType.name
    }));

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response = await httpClient.get("/paymentinfos");
                setPaymentInfoTypes(response.data);
            }catch(e){
                console.error("Error during fetching paymentInfoTypes:", e);
            }
        }
        fetchPaymentInfos();
    }, [])

    const persistNewPaymentType = async (newPaymentType) => {
        try {
            const response = await httpClient.post("/paymentinfos", newPaymentType);
            setPaymentInfoTypes([...paymentInfoTypes, response.data]);
        } catch (error) {
            console.error("Error creating payment type:", error);
        }
    }



    const changePaymentInfoId = (selectedOption, oldId) => {
        setUpdatedEntityPaymentInfos(prevInfos =>
            prevInfos.map(info =>
                info.paymentInfo.id === oldId
                    ? { ...info, paymentInfo: { ...info.paymentInfo, id: selectedOption.value, name: selectedOption.label } }
                    : info
            )
        );

        setIsSaveDisabled(false);
    }


    const addPaymentInfoId = (selectedOption) => {
        setUpdatedEntityPaymentInfos(prevInfos => [
            ...prevInfos,
            {
                paymentInfo: { id: selectedOption.value, name: selectedOption.label },
                paymentAddress: ""
            }
        ]);
        if(updatedEntityPaymentInfos.some(info => info.paymentAddress === "")){
            setIsSaveDisabled(true);
        }
    }

    const changePaymentInfoContent = (e, id) => {
        const newValue = e.target.value;
        setUpdatedEntityPaymentInfos(prevInfos =>
            prevInfos.map(info =>
                info.paymentInfo.id === id
                    ? { ...info, paymentAddress: newValue }
                    : info
            )
        );

        if(e.target.value === "" || updatedEntityPaymentInfos.some(info => info.paymentAddress === "")){
            setIsSaveDisabled(true);
        }else{
            setIsSaveDisabled(false);
        }
    }

    return(
        <div>
            <label>Zahlungsinformationen:</label>
            {updatedEntityPaymentInfos.map((entityPaymentInfo, index) => (
                <div className="row" key={index}>
                    <div className="col-md-6 form-group">
                        <label></label>
                        <Select
                            options={
                                paymentInfoTypesOptions.filter(option =>
                                    !updatedEntityPaymentInfos.map(info => info.paymentInfo.id).includes(option.value)
                                    || entityPaymentInfo.paymentInfo.id === option.value)
                            }
                            value={paymentInfoTypesOptions.find(option => option.value === entityPaymentInfo.paymentInfo.id) || undefined}
                            onChange={(e) => changePaymentInfoId(e, entityPaymentInfo.paymentInfo.id)}
                            placeholder="Auswahl Zahlungsinformation"
                        />
                    </div>
                    <div className="col-md-6 form-group">
                        <label></label>
                        <div className="input-group">
                            <input
                                type="text"
                                name="zahlungsinformationInput"
                                value={entityPaymentInfo.paymentAddress}
                                onChange={(e) => changePaymentInfoContent(e, entityPaymentInfo.paymentInfo.id)}
                                placeholder="Zahlungsadresse"
                                className="form-control"
                            />
                            <button className="btn btn-danger input-group-sm" onClick={() => deleteEntityPaymentInfo(entityPaymentInfo.paymentInfo.id)}>
                                <FaRegTrashAlt className="centered-label"/>
                            </button>
                        </div>
                    </div>
                </div>
            ))}
            {updatedEntityPaymentInfos.length < 3 &&
                <div className="row">
                    <div className="col-md-6 form-group">
                        <label></label>
                        <Select
                            options={
                                paymentInfoTypesOptions.filter(option =>
                                    !updatedEntityPaymentInfos.map(info => info.paymentInfo.id).includes(option.value))
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
                <div className="col-md-6 form-group">
                    <label></label>
                    <button
                        className="form-control btn bg-secondary bg-opacity-25"
                        onClick={() => setIsEditPaymentTypesWindowOpen(true)}
                    >
                        Zahlungsarten bearbeiten
                    </button>
                </div>
            </div>
            {newPaymentTypeWindowOpen &&
                <NewEntity
                    closeWindow={() => setNewPaymentTypeWindowOpen(false)}
                    entities={paymentInfoTypes}
                    persistNewEntity={persistNewPaymentType}
                    title="Zahlungsart hinzufügen"
                />
            }
            {isEditPaymentTypesWindowOpen &&
                <EditPaymentInfoTypes
                    closeWindow={() => setIsEditPaymentTypesWindowOpen(false)}
                    paymentInfoTypes={paymentInfoTypes}
                    setPaymentInfoTypes={setPaymentInfoTypes}
                />
            }
        </div>
    )
}

export default EditEntityPaymentInfo;