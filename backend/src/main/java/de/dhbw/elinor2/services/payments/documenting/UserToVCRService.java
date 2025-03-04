package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToVCRRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserToVCRService extends PaymentService<PaymentLight, UserToVCR, UUID>
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    public UserToVCRService(UserToVCRRepository repository)
    {
        super(repository);
    }

    @Override
    public UserToVCR convertToEntity(PaymentLight paymentLight, UUID id)
    {
        User userSender = userRepository.findById(paymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User not found"));

        VirtualCashRegister vcrReceiver = virtualCashRegisterRepository.findById(paymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("VirtualCashRegister not found"));

        UserToVCR userToVCR = new UserToVCR();
        userToVCR.setUser(userSender);
        userToVCR.setVirtualCashRegister(vcrReceiver);
        userToVCR.setAmount(paymentLight.getAmount());
        if (id != null) userToVCR.setId(id);

        return userToVCR;
    }

    @Override
    public void executePayment(UserToVCR userToVCR, Jwt jwt)
    {
        userService.checkUserAuthorization(userToVCR.getUser().getId(), jwt);

        User user = userToVCR.getUser();
        user.setDebt(user.getDebt().add(userToVCR.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToVCR.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().add(userToVCR.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public void undoPayment(UserToVCR userToVCR, Jwt jwt)
    {
        userService.checkUserAuthorization(userToVCR.getUser().getId(), jwt);

        User user = userToVCR.getUser();
        user.setDebt(user.getDebt().subtract(userToVCR.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToVCR.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().subtract(userToVCR.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }
}
