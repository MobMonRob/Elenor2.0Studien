package de.dhbw.elinor2.services.payments.executiong;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToExtern;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToExternRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserToExternService extends PaymentService<PaymentOverVCRLight, UserToExtern, UUID>
{
    @Autowired
    private ExternRepository externRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    public UserToExternService(UserToExternRepository repository)
    {
        super(repository);
    }

    @Override
    public UserToExtern convertToEntity(PaymentOverVCRLight paymentOverVCRLight, UUID id)
    {
        User userSender = userRepository.findById(paymentOverVCRLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User not found"));

        Extern externReceiver = externRepository.findById(paymentOverVCRLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Extern not found"));

        VirtualCashRegister vcr = virtualCashRegisterRepository.findById(paymentOverVCRLight.getVcrId()).orElseThrow(()
                -> new IllegalArgumentException("VirtualCashRegister not found"));

        UserToExtern userToExtern = new UserToExtern();
        userToExtern.setUser(userSender);
        userToExtern.setExtern(externReceiver);
        userToExtern.setAmount(paymentOverVCRLight.getAmount());
        userToExtern.setVirtualCashRegister(vcr);
        if (id != null) userToExtern.setId(id);

        return userToExtern;
    }

    @Override
    public void executePayment(UserToExtern userToExtern)
    {
        User user = userToExtern.getUser();
        user.setDebt(user.getDebt().subtract(userToExtern.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToExtern.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().subtract(userToExtern.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public void undoPayment(UserToExtern userToExtern)
    {
        User user = userToExtern.getUser();
        user.setDebt(user.getDebt().add(userToExtern.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToExtern.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().add(userToExtern.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }
}
