package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.VCRToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToVCRRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VCRToVCRService extends PaymentService<PaymentLight, VCRToVCR, UUID>
{
    @Autowired
    private VirtualCashRegisterRepository vcrRepository;

    @Autowired
    public VCRToVCRService(VCRToVCRRepository repository)
    {
        super(repository);
    }

    @Override
    public VCRToVCR convertToEntity(PaymentLight paymentLight)
    {
        VirtualCashRegister sender = vcrRepository.findById(paymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("Sender not found"));

        VirtualCashRegister receiver = vcrRepository.findById(paymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Receiver not found"));

        VCRToVCR vcrToVCR = new VCRToVCR();
        vcrToVCR.setAmount(paymentLight.getAmount());
        vcrToVCR.setSender(sender);
        vcrToVCR.setReceiver(receiver);
        return vcrToVCR;
    }

    @Override
    public void executePayment(VCRToVCR vcrToVCR)
    {
        VirtualCashRegister sender = vcrToVCR.getSender();
        VirtualCashRegister receiver = vcrToVCR.getReceiver();
        sender.setBalance(sender.getBalance().subtract(vcrToVCR.getAmount()));
        receiver.setBalance(receiver.getBalance().add(vcrToVCR.getAmount()));
        vcrRepository.save(sender);
        vcrRepository.save(receiver);
    }

    @Override
    public void undoPayment(VCRToVCR vcrToVCR)
    {
        VirtualCashRegister sender = vcrToVCR.getSender();
        VirtualCashRegister receiver = vcrToVCR.getReceiver();
        sender.setBalance(sender.getBalance().add(vcrToVCR.getAmount()));
        receiver.setBalance(receiver.getBalance().subtract(vcrToVCR.getAmount()));
        vcrRepository.save(sender);
        vcrRepository.save(receiver);
    }
}
