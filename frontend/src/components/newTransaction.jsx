import React, {useEffect, useState} from "react";
import {Modal, Button} from 'react-bootstrap';
import Select from 'react-select';

const NewTransactionWindow = ({closeWindow, users, externs, cashregisters, loggedInUserId, saveNewTransaction}) => {
    const [type, setType] = useState(undefined);
    const [amount, setAmount] = useState("");
    const [sender, setSender] = useState(undefined);
    const [receiver, setReceiver] = useState(undefined);
    const [overVcr, setOverVcr] = useState(undefined);
    const [overVcrDisabled, setOverVcrDisabled] = useState(true);
    const [senderDisabled, setSenderDisabled] = useState(true);
    const [receiverDisabled, setReceiverDisabled] = useState(true);
    const [optionsSender, setOptionsSender] = useState([]);
    const [optionsReceiver, setOptionsReceiver] = useState([]);
    const [saveDisabled, setSaveDisabled] = useState(true);

    const handleSave = () => {
        const savedAmount = Number(amount.replace(",", "."))

        const newTransaction = {
            senderId: sender,
            receiverId: receiver,
            amount: savedAmount,
            ...(overVcr ? { vcrId: overVcr } : {})
        }
        saveNewTransaction(newTransaction);
        closeWindow();
    };

    useEffect(() => {
        setSaveDisabled(
            (!type || amount === "" || (!senderDisabled && !sender) || (!receiverDisabled && !receiver) || (!overVcrDisabled && !overVcr))
        )
    }, [type, amount, sender, receiver, overVcr, senderDisabled, receiverDisabled, overVcrDisabled]);

    const optionsType = [
        { value: 'User2User', label: 'Mitglied zu Mitglied' },
        { value: 'User2Vcr', label: 'Mitglied zu Kasse' },
        { value: 'Vcr2User', label: 'Kasse zu Mitglied' },
        { value: 'Vcr2Vcr', label: 'Kasse zu Kasse' },
        { value: 'Extern2UserOverVcr', label: 'Extern zu Mitglied über Kasse' },
        { value: 'User2ExternOverVcr', label: 'Mitglied zu Extern über Kasse' },
    ];

    const userList = users.map(user => ({
        value: user.id,
        label: user.username
    }));

    const cashRegisterList = cashregisters.map(cashregister => ({
        value: cashregister.id,
        label: cashregister.name
    }));

    const externList = externs.map(extern => ({
        value: extern.id,
        label: extern.name
    }));

    let optionsOverVcr = cashRegisterList;

    const changeReceiver = (selectedOption) => {
        setReceiver(selectedOption.value)
        if (type === 'User2User' && selectedOption.value !== loggedInUserId) {
            setSender(loggedInUserId)
        }
    }

    const changeSender = (selectedOption) => {
        setSender(selectedOption.value)
        if (type === 'User2User' && selectedOption.value !== loggedInUserId) {
            setReceiver(loggedInUserId)
        }
    }

    const changeType = (selectedOption) => {
        setType(selectedOption.value)
        setSender("")
        setReceiver("")
        setOverVcr("")
        setOptionsSender([])
        setOptionsReceiver([])
        if(selectedOption.value === 'Vcr2Vcr') {
            setSenderDisabled(false)
            setReceiverDisabled(false)
            setOverVcrDisabled(true)
            setOptionsSender(cashRegisterList);
            setOptionsReceiver(cashRegisterList);
        }else if(selectedOption.value === 'Vcr2User') {
            setSenderDisabled(false)
            setReceiverDisabled(true)
            setOverVcrDisabled(true)
            setOptionsSender(cashRegisterList);
            setOptionsReceiver(userList);
            setReceiver(loggedInUserId);
        }else if(selectedOption.value === 'User2Vcr') {
            setSenderDisabled(true)
            setReceiverDisabled(false)
            setOverVcrDisabled(true)
            setOptionsSender(userList);
            setOptionsReceiver(cashRegisterList);
            setSender(loggedInUserId);
        }else if(selectedOption.value === 'User2User') {
            setSenderDisabled(false)
            setReceiverDisabled(false)
            setOverVcrDisabled(true)
            setOptionsSender(userList);
            setOptionsReceiver(userList);
        }else if(selectedOption.value === 'User2ExternOverVcr') {
            setSenderDisabled(true)
            setReceiverDisabled(false)
            setOverVcrDisabled(false)
            setOptionsSender(userList);
            setOptionsReceiver(externList);
            setSender(loggedInUserId);
        }else if(selectedOption.value === 'Extern2UserOverVcr') {
            setSenderDisabled(false)
            setReceiverDisabled(true)
            setOverVcrDisabled(false)
            setOptionsSender(externList);
            setOptionsReceiver(userList);
            setReceiver(loggedInUserId);
        }
    }

    return (
        <Modal show={true} onHide={closeWindow} centered={true} size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Transaktion erstellen</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <form>
                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Transaktionstyp:</label>
                            <Select
                                options={optionsType}
                                value={optionsType.find(option => option.value === type) || undefined}
                                onChange={(e) => changeType(e)}
                                placeholder="Auswahl Typ"
                                inputId="type-select"
                                id="type-select"
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Betrag in €:</label>
                            <input
                                type="text"
                                name="amount"
                                value={amount}
                                onChange={(e) => {
                                    let value = e.target.value.replace(/[^0-9.,]/g, '');
                                    if (/^\d*([.,]\d{0,2})?$/.test(value)) {
                                        setAmount(value);
                                    }
                                }}
                                className="form-control"
                                placeholder="Eingabe Betrag"
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Sender:</label>
                            <Select
                                options={optionsSender}
                                value={optionsSender.find(option => option.value === sender) || undefined}
                                onChange={(e) => changeSender(e)}
                                placeholder="Auswahl Sender"
                                isDisabled={senderDisabled}
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Empfänger:</label>
                            <Select
                                options={optionsReceiver}
                                value={optionsReceiver.find(option => option.value === receiver) || undefined}
                                onChange={(e) => changeReceiver(e)}
                                placeholder="Auswahl Empfänger"
                                isDisabled={receiverDisabled}
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-md-6 form-group">
                            <label>Über Kasse:</label>
                            <Select
                                options={optionsOverVcr}
                                value={optionsOverVcr.find(option => option.value === overVcr) || undefined}
                                onChange={(e) => setOverVcr(e.value)}
                                isDisabled={overVcrDisabled}
                                placeholder="Auswahl beteiligte Kasse"
                            />
                        </div>
                    </div>
                </form>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={closeWindow}>
                    Abbrechen
                </Button>
                <Button variant="primary" onClick={handleSave} disabled={saveDisabled}>
                    Speichern
                </Button>
            </Modal.Footer>
        </Modal>
    );

}

export default NewTransactionWindow;