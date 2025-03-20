package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.VCRToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToVCRRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import de.dhbw.elinor2.utils.PaymentType;
import de.dhbw.elinor2.utils.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VCRToVCRService extends PaymentService<InputPayment, OutputPayment, VCRToVCR, UUID>
{
    private final VirtualCashRegisterRepository vcrRepository;

    @Autowired
    public VCRToVCRService(VCRToVCRRepository repository, VirtualCashRegisterRepository vcrRepository)
    {
        super(repository);
        this.vcrRepository = vcrRepository;
    }

    @Override
    public VCRToVCR convertToEntity(InputPayment inputPaymentLight, UUID id)
    {
        VirtualCashRegister sender = vcrRepository.findById(inputPaymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("Sender not found"));

        VirtualCashRegister receiver = vcrRepository.findById(inputPaymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Receiver not found"));

        VCRToVCR vcrToVCR = new VCRToVCR();
        vcrToVCR.setAmount(inputPaymentLight.getAmount());
        vcrToVCR.setSender(sender);
        vcrToVCR.setReceiver(receiver);
        if (id != null) vcrToVCR.setId(id);
        return vcrToVCR;
    }

    @Override
    public void executePayment(VCRToVCR vcrToVCR, Jwt jwt)
    {
        VirtualCashRegister sender = vcrToVCR.getSender();
        VirtualCashRegister receiver = vcrToVCR.getReceiver();
        sender.setBalance(sender.getBalance().subtract(vcrToVCR.getAmount()));
        receiver.setBalance(receiver.getBalance().add(vcrToVCR.getAmount()));
        vcrRepository.save(sender);
        vcrRepository.save(receiver);
    }

    @Override
    public void undoPayment(VCRToVCR vcrToVCR, Jwt jwt)
    {
        VirtualCashRegister sender = vcrToVCR.getSender();
        VirtualCashRegister receiver = vcrToVCR.getReceiver();
        sender.setBalance(sender.getBalance().add(vcrToVCR.getAmount()));
        receiver.setBalance(receiver.getBalance().subtract(vcrToVCR.getAmount()));
        vcrRepository.save(sender);
        vcrRepository.save(receiver);
    }

    @Override
    public OutputPayment convertEntityToOutputPattern(VCRToVCR entity)
    {
        return new OutputPayment(
                PaymentType.Vcr2Vcr,
                entity.getId(),
                entity.getAmount(),
                entity.getTimestamp(),
                new TransactionEntity(
                        entity.getSender().getId(),
                        entity.getSender().getName()
                ),
                new TransactionEntity(
                        entity.getReceiver().getId(),
                        entity.getReceiver().getName()
                )
        );
    }
}
