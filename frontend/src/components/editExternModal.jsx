import React, {useEffect, useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {httpClient} from "../HttpClient";
import EditEntityPaymentInfo from "./editEntityPaymentInfo";

const EditExternModal = ({
                         closeWindow,
                         setExterns,
                         externs,
                         extern,
                         resetDisplayedTransactions,
                         renameTransactions,
                         isDeletable,
                         setTransactionFilterName
                        }) => {

    const [isSaveDisabledPaymentInfo, setIsSaveDisabledPaymentInfo] = useState(true);
    const [isSaveDisabledName, setIsSaveDisabledName] = useState(true);
    const [updatedExtern, setUpdatedExtern] = useState(extern);
    const [externPaymentInfos, setExternPaymentInfos] = useState([]);
    const [updatedExternPaymentInfos, setUpdatedExternPaymentInfos] = useState([]);

    useEffect(() => {
        async function fetchPaymentInfos() {
            try{
                const response = await httpClient.get("/externs/" + updatedExtern.id + "/paymentinfos");

                const paymentInfos = response.data.map(({ paymentInfo, paymentAddress }) => ({
                    paymentInfo,
                    paymentAddress
                }));
                setExternPaymentInfos(paymentInfos);
                setUpdatedExternPaymentInfos(paymentInfos);
            }catch(e){
                console.error("Error during fetching paymentInfos:", e);
            }
        }
        fetchPaymentInfos();
    }, [updatedExtern.id])

    const savePaymentInfos = () => {
        updatedExternPaymentInfos.forEach(
            async (updatedExternPaymentInfo) => {
                if(externPaymentInfos.find(info => info.paymentInfo.id === updatedExternPaymentInfo.paymentInfo.id
                    && info.paymentAddress !== updatedExternPaymentInfo.paymentAddress))
                {
                    await httpClient.put("/externs/" + extern.id + "/paymentinfos/" + updatedExternPaymentInfo.paymentInfo.id,
                        updatedExternPaymentInfo.paymentAddress,
                        { headers: { "Content-Type": "text/plain" } });
                }else if (!externPaymentInfos.find(info => info.paymentInfo.id === updatedExternPaymentInfo.paymentInfo.id))
                {
                    await httpClient.post("/externs/" + updatedExtern.id + "/paymentinfos/" + updatedExternPaymentInfo.paymentInfo.id,
                        updatedExternPaymentInfo.paymentAddress,
                        { headers: { "Content-Type": "text/plain" } });
                }
            }
        )
        externPaymentInfos.filter(
            async(externPaymentInfo) => {
                if(!updatedExternPaymentInfos.find(updatedExternPaymentInfo => updatedExternPaymentInfo.paymentInfo.id === externPaymentInfo.paymentInfo.id)){
                    await deletePaymentInfo(externPaymentInfo.paymentInfo.id);
                }
            })
    }

    const handleSave = async () => {
        try{
            const response = await httpClient.put("/externs/" + extern.id, updatedExtern);
            setExterns(externs.map(cashRegister => cashRegister.id === response.data.id ? response.data : cashRegister));
        }catch(e){
            console.error("An error occurred during updating cash register:",e);
        }
        renameTransactions(extern.id, extern.name);
        setTransactionFilterName(updatedExtern.name);
        savePaymentInfos()
        closeWindow();
    }

    const handleDelete = async () => {
        await httpClient.delete("/externs/" + extern.id);
        resetDisplayedTransactions();
        setExterns(externs.filter(ex => ex.id !== extern.id));
        closeWindow();
    }

    const handleNameChange = (e) => {
        if (externs.some(cashRegister => cashRegister.name === e.target.value)) {
            setIsSaveDisabledName(true)
        }else {
            setIsSaveDisabledName(false);
        }
        setUpdatedExtern({...updatedExtern, name: e.target.value})
    }

    const deletePaymentInfo = async (id) => {
        if(externPaymentInfos.find(info => info.paymentInfo.id === id)){
            await httpClient.delete("/externs/" + updatedExtern.id + "/paymentinfos/" + id);
            setExternPaymentInfos(externPaymentInfos.filter(info => info.paymentInfo.id !== id));
        }
        setUpdatedExternPaymentInfos(updatedExternPaymentInfos.filter(info => info.paymentInfo.id !== id));
    }

    return(
        <div>
            <Modal show={true} onHide={closeWindow} centered={true} >
                <Modal.Header closeButton>
                    <Modal.Title>Externes Konto - {extern.name}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Name:</label>
                            <input
                                type="text"
                                name="firstName"
                                value={updatedExtern.name}
                                onChange={(e) => handleNameChange(e)}
                                className="form-control"
                            />
                        </div>
                    </div>
                    <EditEntityPaymentInfo
                        updatedEntityPaymentInfos={updatedExternPaymentInfos}
                        setUpdatedEntityPaymentInfos={setUpdatedExternPaymentInfos}
                        setIsSaveDisabled={setIsSaveDisabledPaymentInfo}
                        deleteEntityPaymentInfo={deletePaymentInfo}
                    />
                    {!isDeletable && (
                        <div className="alert-danger alert mt-3">
                            Das Konto kann nicht gelöscht werden, da noch Transaktionen mit dem Konto in Verbindung stehen.
                        </div>
                    )}
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="danger" onClick={handleDelete} className="me-auto" disabled={!isDeletable}>
                        Konto löschen
                    </Button>
                    <Button variant="secondary" onClick={closeWindow}>
                        Schließen
                    </Button>
                    <Button variant="primary" onClick={handleSave} disabled={
                        (isSaveDisabledPaymentInfo && isSaveDisabledName) ||
                        (!isSaveDisabledPaymentInfo && isSaveDisabledName && extern.name !== updatedExtern.name)
                    }>
                        Speichern
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}

export default EditExternModal;