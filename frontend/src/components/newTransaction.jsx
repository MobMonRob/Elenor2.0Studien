import React, {useState} from "react";
import {Modal, Button} from 'react-bootstrap';
import Select from 'react-select';

const NewTransactionWindow = ({closeWindow, users, externs, cashregisters, loggedInUserId}) => {
    const [type, setType] = useState('');
    const [amount, setAmount] = useState('');
    const [sender, setSender] = useState('');
    const [receiver, setReceiver] = useState('');
    const [overVcr, setOverVcr] = useState('');
    const [overVcrDisabled, setOverVcrDisabled] = useState(true);
    const [senderDisabled, setSenderDisabled] = useState(true);
    const [receiverDisabled, setReceiverDisabled] = useState(true);
    const [optionsSender, setOptionsSender] = useState([]);
    const [optionsReceiver, setOptionsReceiver] = useState([]);


    const handleSave = () => {
        //newTransaction(this.state.newTransaction);
        closeWindow();
    };

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

    const optionsOverVcr = cashRegisterList;

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
            setSender(loggedInUserId);
        }else if(selectedOption.value === 'User2Vcr') {
            setSenderDisabled(true)
            setReceiverDisabled(false)
            setOverVcrDisabled(true)
            setOptionsReceiver(cashRegisterList);
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
            setOptionsReceiver(externList);
        }else if(selectedOption.value === 'Extern2UserOverVcr') {
            setSenderDisabled(false)
            setReceiverDisabled(true)
            setOverVcrDisabled(false)
            setOptionsSender(externList);
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
                                value={optionsType.find(option => option.value === type)}
                                onChange={(selectedOption) => changeType(selectedOption)}
                                className="form-control"
                                placeholder="Auswahl Typ"
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Betrag in €:</label>
                            <input
                                type="number"
                                name="amount"
                                value={amount}
                                onChange={(selectedOption) => setAmount(selectedOption.value)}
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
                                value={optionsSender.find(option => option.value === sender)}
                                onChange={(selectedOption) => setSender(selectedOption.value)}
                                className="form-control"
                                placeholder="Auswahl Sender"
                                isDisabled={senderDisabled}
                            />
                        </div>

                        <div className="col-md-6 form-group">
                            <label>Empfänger:</label>
                            <Select
                                options={optionsReceiver}
                                value={optionsReceiver.find(option => option.value === receiver)}
                                onChange={(selectedOption) => setReceiver(selectedOption.value)}
                                className="form-control"
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
                                value={optionsOverVcr.find(option => option.value === overVcr)}
                                onChange={(selectedOption) => setOverVcr(selectedOption.value)}
                                isDisabled={overVcrDisabled}
                                className="form-control"
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
                <Button variant="primary" onClick={handleSave}>
                    Speichern
                </Button>
            </Modal.Footer>
        </Modal>
    );

}

export default NewTransactionWindow;