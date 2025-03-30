package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToVCRRepository;
import de.dhbw.elinor2.services.UserService;
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
public class UserToVCRService extends PaymentService<InputPayment, OutputPayment, UserToVCR, UUID>
{
    private final UserRepository userRepository;
    private final VirtualCashRegisterRepository virtualCashRegisterRepository;

    private final UserService userService;

    @Autowired
    public UserToVCRService(UserToVCRRepository repository,
                            UserService userService,
                            UserRepository userRepository,
                            VirtualCashRegisterRepository virtualCashRegisterRepository)
    {
        super(repository);
        this.userService = userService;
        this.userRepository = userRepository;
        this.virtualCashRegisterRepository = virtualCashRegisterRepository;
    }

    @Override
    public UserToVCR convertToEntity(InputPayment inputPaymentLight, UUID id)
    {
        User userSender = userRepository.findById(inputPaymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User not found"));

        VirtualCashRegister vcrReceiver = virtualCashRegisterRepository.findById(inputPaymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("VirtualCashRegister not found"));

        UserToVCR userToVCR = new UserToVCR();
        userToVCR.setUser(userSender);
        userToVCR.setVirtualCashRegister(vcrReceiver);
        userToVCR.setAmount(inputPaymentLight.getAmount());
        if (id != null) userToVCR.setId(id);

        return userToVCR;
    }

    @Override
    public void executePayment(UserToVCR userToVCR, Jwt jwt)
    {
        userService.checkUserAuthorization(userToVCR.getUser().getId(), jwt);

        User user = userToVCR.getUser();
        user.setBalance(user.getBalance().subtract(userToVCR.getAmount()));
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
        user.setBalance(user.getBalance().add(userToVCR.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToVCR.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().subtract(userToVCR.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public OutputPayment convertEntityToOutputPattern(UserToVCR entity)
    {
        return new OutputPayment(
                PaymentType.User2Vcr,
                entity.getId(),
                entity.getAmount(),
                entity.getTimestamp(),
                new TransactionEntity(
                        entity.getUser().getId(),
                        entity.getUser().getUsername()
                ),
                new TransactionEntity(
                        entity.getVirtualCashRegister().getId(),
                        entity.getVirtualCashRegister().getName()
                )
        );
    }
}
