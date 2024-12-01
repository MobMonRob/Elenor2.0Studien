package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VCRToUser;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToUserRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VCRToUserService extends PaymentService<PaymentLight, VCRToUser, UUID>
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    public VCRToUserService(VCRToUserRepository repository)
    {
        super(repository);
    }

    @Override
    public VCRToUser convertToEntity(PaymentLight paymentLight)
    {
        VirtualCashRegister sender = virtualCashRegisterRepository.findById(paymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("Sender not found"));

        User receiver = userRepository.findById(paymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Receiver not found"));

        VCRToUser vcrToUser = new VCRToUser();
        vcrToUser.setAmount(paymentLight.getAmount());
        vcrToUser.setVirtualCashRegister(sender);
        vcrToUser.setUser(receiver);
        return vcrToUser;
    }

    @Override
    public void executePayment(VCRToUser vcrToUser)
    {
        VirtualCashRegister sender = vcrToUser.getVirtualCashRegister();
        User receiver = vcrToUser.getUser();
        sender.setBalance(sender.getBalance().subtract(vcrToUser.getAmount()));
        receiver.setDebt(receiver.getDebt().subtract(vcrToUser.getAmount()));
        virtualCashRegisterRepository.save(sender);
        userRepository.save(receiver);
    }

    @Override
    public void undoPayment(VCRToUser vcrToUser)
    {
        VirtualCashRegister sender = vcrToUser.getVirtualCashRegister();
        User receiver = vcrToUser.getUser();
        sender.setBalance(sender.getBalance().add(vcrToUser.getAmount()));
        receiver.setDebt(receiver.getDebt().add(vcrToUser.getAmount()));
        virtualCashRegisterRepository.save(sender);
        userRepository.save(receiver);
    }
}
