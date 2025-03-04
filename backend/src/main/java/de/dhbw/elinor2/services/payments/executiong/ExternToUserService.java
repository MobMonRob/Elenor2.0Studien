package de.dhbw.elinor2.services.payments.executiong;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.ExternToUser;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.ExternToUserRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternToUserService extends PaymentService<PaymentOverVCRLight, ExternToUser, UUID>
{
    @Autowired
    private ExternRepository externRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    public ExternToUserService(ExternToUserRepository repository)
    {
        super(repository);
    }

    @Override
    public ExternToUser convertToEntity(PaymentOverVCRLight paymentOverVCRLight, UUID id)
    {
        User userReceiver = userRepository.findById(paymentOverVCRLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("User not found"));

        Extern externSender = externRepository.findById(paymentOverVCRLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("Extern not found"));

        VirtualCashRegister vcr = virtualCashRegisterRepository.findById(paymentOverVCRLight.getVcrId()).orElseThrow(()
                -> new IllegalArgumentException("VirtualCashRegister not found"));

        ExternToUser externToUser = new ExternToUser();
        externToUser.setAmount(paymentOverVCRLight.getAmount());
        externToUser.setUser(userReceiver);
        externToUser.setExtern(externSender);
        externToUser.setVirtualCashRegister(vcr);
        if (id != null) externToUser.setId(id);

        return externToUser;
    }

    @Override
    public void executePayment(ExternToUser externToUser, Jwt jwt)
    {
        userService.checkUserAuthorization(externToUser.getUser().getId(), jwt);

        User user = externToUser.getUser();
        user.setDebt(user.getDebt().add(externToUser.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = externToUser.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().add(externToUser.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public void undoPayment(ExternToUser externToUser, Jwt jwt)
    {
        userService.checkUserAuthorization(externToUser.getUser().getId(), jwt);

        User user = externToUser.getUser();
        user.setDebt(user.getDebt().subtract(externToUser.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = externToUser.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().subtract(externToUser.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }
}
